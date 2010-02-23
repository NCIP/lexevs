/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;
import java.sql.Connection;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.UMLSHistoryLoader;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.connection.SQLInterfaceBase;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;

import edu.mayo.informatics.lexgrid.convert.directConversions.UMLSHistoryFileToSQL;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.ConversionLauncher;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.UMLSHistoryFile;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;

public class UMLSHistoryLoaderImpl extends BaseLoader implements UMLSHistoryLoader {

/** Serial Version ID */
private static final long serialVersionUID = 1L;
    /** Holds the loader name String Constant */
    public final static String name = "NCIMetaHistoryLoader";
    /** Holds the loader description string constant. */
    private final static String description = "This loader loads NCI Meta-Thesaurus history files into the database.";

    /**
     * Constructor sets the loader name and description.
     */
    public UMLSHistoryLoaderImpl() {
        super.name_ = UMLSHistoryLoaderImpl.name;
        super.description_ = UMLSHistoryLoaderImpl.description;
    }

    /**
     * method registers the loader in resource manager.
     * 
     * @throws LBParameterException
     * @throws LBException
     */
    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(UMLSHistoryLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(UMLSHistoryLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        temp.setVersion(version_);

        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerLoadExtension(temp);
    }

    /**
     * Method reads the related RRF files and loads the history details into the
     * database.
     * 
     * @param URI
     *            source - URI of the folder containing the RRF file.
     * @param boolean append - boolean value to indicate whether to append the
     *        new data to the already existing history data (if any).
     * @param boolean stopOnErrors - If true, loader halts on any error. If
     *        false, loader continues to laod data on recoverable errors.
     * @param boolean async - loader runs on a seperate thread if selected true.
     * @throws LBException
     */
    public void load(URI source, boolean append, boolean stopOnErrors, boolean async) throws LBException {

        this.setInUse();

        status_ = new LoadStatus();
        status_.setLoadSource(source.toString());
        md_ = new MessageDirector(getName(), status_);

        try {

            in_ = new UMLSHistoryFile(source, stopOnErrors, md_);
            in_.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            throw new LBParameterException("Validation Exception for NCI MetaThesaurus history file - "
                    + e.getMessage());
        }

        options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));
        options_.add(new Option(Option.DELIMITER, new String("|")));
        options_.add(new Option(Option.OVERWRITE, new Boolean(!append)));

        SQLConnectionInfo sci = null;
        try {
            if (append) {
                SQLConnectionInfo[] temp = ResourceManager.instance().getRegistry().getSQLConnectionInfoForHistory(
                        HistoryService.metaURN);
                if (temp != null && temp.length > 0) {
                    sci = temp[0];
                }
            }
            if (sci == null) {
                // didn't find one, or the don't want us to append.
                sci = ResourceManager.instance().getSQLConnectionInfoForHistoryLoad();
            }

            out_ = new LexGridSQLOut(sci.username, sci.password, sci.server, sci.driver, sci.prefix);
            out_.testConnection();
        } catch (ConnectionFailure e) {
            String id = getLogger().error("Problem connecting to the sql server", e);
            inUse = false;
            throw new LBInvocationException("There was a problem connecting to the internal sql server", id);
        }

        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));

        if (async) {
            Thread conversion = new Thread(new DoLoad(sci, append));
            conversion.start();
        } else {
            new DoLoad(sci, append).run();
        }
    }

    private class DoLoad implements Runnable {
        private SQLConnectionInfo sci_;
        private boolean append_;

        /**
         * Constructor
         * 
         * @param sci
         * @param append
         */
        public DoLoad(SQLConnectionInfo sci, boolean append) {
            sci_ = sci;
            append_ = append;
        }

        public void run() {
            try {
                md_.info("Loading NCI MetaThesaurus History...");
                Snapshot snap = SimpleMemUsageReporter.snapshot();
                md_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                        + " Heap Usage : " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                        + " Heap Delta : " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
                ConversionLauncher.startConversion(in_, out_, null, options_, md_);
                SystemVariables sv = ResourceManager.instance().getSystemVariables();

                if (status_.getErrorsLogged() != null && !status_.getErrorsLogged().booleanValue()) {
                    md_.info("Done loading NCI MetaThesaurus History.");
                    snap = SimpleMemUsageReporter.snapshot();
                    md_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage : " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta : " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));
                    // this replaces any currently existing history entry with
                    // this URN with the new one
                    md_.info("Updating the registry.");
                    org.lexevs.registry.service.XmlRegistry.HistoryEntry oldEntry = ResourceManager.instance().getRegistry().addNewHistory(
                            HistoryService.metaURN, sci_.server, sci_.dbName, sci_.prefix);
                    if (sv.getAutoLoadSingleDBMode()) {
                        // some databases (access in particular) won't see new
                        // tables unless you open a new connection to them.
                        // if we are in single db mode, there may already be
                        // open connections. close them.
                        SQLInterfaceBase sib = ResourceManager.instance().getSQLInterfaceBase(sci_.username,
                                sci_.password, sci_.server, sci_.driver);
                        sib.closeUnusedConnections();
                    }

                    // update the history maps.
                    ResourceManager.instance().readHistories();

                    // drop any old database as required
                    if (!append_ && oldEntry != null) {
                        md_.info("Dropping the old history.");

                        if (sv.getAutoLoadSingleDBMode()) {
                            Connection connection = DBUtility.connectToDatabase(sv.getAutoLoadDBURL(), sv
                                    .getAutoLoadDBDriver(), sv.getAutoLoadDBUsername(), sv.getAutoLoadDBPassword());
                            SQLTableUtilities stu = new SQLTableUtilities(connection, oldEntry.prefix);
                            stu.dropTables();
                            connection.close();
                        } else {
                            DBUtility.dropDatabase(sv.getAutoLoadDBURL(), sv.getAutoLoadDBDriver(), oldEntry
                                    .dbName, sv.getAutoLoadDBUsername(), sv.getAutoLoadDBPassword());
                        }
                    }

                    acsvr_ = new AbsoluteCodingSchemeVersionReference[1];
                    acsvr_[0] = new AbsoluteCodingSchemeVersionReference();
                    acsvr_[0].setCodingSchemeURN(HistoryService.metaURN);
                    acsvr_[0].setCodingSchemeVersion(null);

                    status_.setState(ProcessState.COMPLETED);
                    md_.info("Load process completed without error.");

                }
            } catch (Exception e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal("Failed while running the conversion.", e);
            } finally {
                if (status_.getState() == null || status_.getState().getType() != ProcessState.COMPLETED_TYPE) {
                    status_.setState(ProcessState.FAILED);
                }
                status_.setEndTime(new Date(System.currentTimeMillis()));
                inUse = false;
            }

        }
    }

    public void validate(URI source, int validationLevel) throws LBException {
        try {
            setInUse();
            boolean entireFile = false;
            if (validationLevel == 1) {
                entireFile = true;
            }

            UMLSHistoryFileToSQL.validateFile(source, null, entireFile);
        } catch (Exception e) {
            throw new LBParameterException("The NCI MetaThesaurus History File appears to be invalid - " + e);
        } finally {
            inUse = false;
        }

    }
}