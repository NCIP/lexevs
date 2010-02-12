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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.UMLS_Loader;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.LexGrid.LexOnt.CsmfVersion;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.connection.SQLInterfaceBase;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;

import edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon.UMLSBaseCode;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.ConversionLauncher;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OptionHolder;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.RRFFiles;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.SemNetFiles;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.UMLSSQL;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.SQLOut;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Loader code for the UMLS.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class UMLSLoaderImpl extends BaseLoader implements Loader, UMLS_Loader {
    private static final long serialVersionUID = 8781875750618588633L;
    public static final String name = "UMLSLoader";
    private static final String description = "This loader loads coding schemes from the UMLS into the database.";

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    private String umlsRegisteredName_;
    private String umlsManifestVersion_;
    private String umlsSourceVersion_;

    public UMLSLoaderImpl() {
        super.name_ = name;
        super.description_ = description;
    }

    /**
     * This override changes the Registered Name of the coding scheme when the
     * manifest is applied. Because this most be done before any loading to the
     * database can be done, we want to make sure that the Registered Name is
     * set as soon as the manifest is set.
     */
    public void setCodingSchemeManifestURI(URI manifestLocation) {
        super.setCodingSchemeManifestURI(manifestLocation);
        if (codingSchemeManifest_ != null) {
            CsmfCodingSchemeURI registeredName = codingSchemeManifest_.getCodingSchemeURI();
            if (registeredName != null && registeredName.isToOverride()) {
                umlsRegisteredName_ = registeredName.getContent();
            }
            CsmfVersion version = codingSchemeManifest_.getRepresentsVersion();
            if (version != null && version.isToOverride()) {
                umlsManifestVersion_ = version.getContent();
            }
        }
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(UMLSLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(UMLSLoaderImpl.class.getName());
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
     * This load method is for loading from the UMLS that is already loaded into
     * a DB (in UMLS format)
     */
    public void load(URI source, String uid, String pwd, String driver, LocalNameList targetTerminologies,
            boolean stopOnErrors, boolean async) throws LBInvocationException, LBParameterException {
        load(source, uid, pwd, driver, targetTerminologies, 1, stopOnErrors, async);
    }

    /**
     * This load method is for loading from the UMLS that is already loaded into
     * a DB (in UMLS format)
     */
    public void load(URI source, String uid, String pwd, String driver, LocalNameList targetTerminologies,
            int hierarchyOpt, boolean stopOnErrors, boolean async) throws LBInvocationException, LBParameterException {
        if (targetTerminologies == null || targetTerminologies.getEntryCount() != 1) {
            throw new LBParameterException(
                    "Sorry, the UMLS loader currently only supports loading 1 (and only 1) terminology at a time."
                            + "  Please provide a single terminology in the targetTerminologies variable.");
        }

        String codingSchemeSAB = targetTerminologies.getEntry()[0];

        setInUse();
        status_ = new LoadStatus();
        status_.setLoadSource("Provided SQL database " + source.toString());
        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));

        URNVersionPair lockInfo = null;

        in_ = null; // this stays null - in this case its a one step load,
                    // instead of two.
        // This is used as the start point for a two step load.
        SQLConnectionInfo sci = null; // this is where the final data will go
        SQLConnectionInfo sci_rrf = null; // this is is not used for the one
                                          // step load.
        SQLOut rrfOut = null; // this is where I need to put the RRF Database
                              // information.

        Connection c = null;
        try {
            rrfOut = new SQLOut(uid, pwd, source.toString(), driver);

            c = DBUtility.connectToDatabase(source.toString(), driver, uid, pwd);
            PreparedStatement getCodingSchemeInfo = c.prepareStatement("SELECT  SVER, SSN FROM MRSAB WHERE RSAB = ?");
            getCodingSchemeInfo.setString(1, codingSchemeSAB);

            ResultSet results = getCodingSchemeInfo.executeQuery();

            if (results.next()) {
                String codingSchemeName = results.getString("SSN");

                if (this.umlsRegisteredName_ == null) {
                    umlsRegisteredName_ = (String) UMLSBaseCode.getIsoMap().get(codingSchemeSAB);
                }
                umlsSourceVersion_ = results.getString("SVER");

                lockInfo = lockAndVerify(codingSchemeName, umlsManifestVersion_, umlsRegisteredName_);
            } else {
                inUse = false;
                throw new LBParameterException("The requested UMLS SAB is not present in the provided database",
                        "codingSchemeSAB", codingSchemeSAB);
            }
            results.close();
        } catch (LBParameterException e) {
            inUse = false;
            unlock(lockInfo);
            throw e;
        } catch (LBInvocationException e) {
            inUse = false;
            unlock(lockInfo);
            throw e;
        } catch (Exception e) {
            inUse = false;
            unlock(lockInfo);
            getLogger().error("Problem connecting to the sql server containing the UMLS data", e);
            throw new LBParameterException("There was a problem connecting to the sql server containing the UMLS data."
                    + "  Perhaps one of the connection parameters is incorrect.  " + e.toString(), "");
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    // noop
                }
            }
        }

        try {
            sci = ResourceManager.instance().getSQLConnectionInfoForLoad();

            out_ = new LexGridSQLOut(sci.username, sci.password, sci.server, sci.driver, sci.prefix);
            out_.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            unlock(lockInfo);
            String id = getLogger().error("Problem connecting to the sql internal sql server", e);
            throw new LBInvocationException("There was a problem connecting to the internal sql server", id);
        }

        md_ = new MessageDirector(getName(), status_);

        if (async) {
            Thread conversion = new Thread(new DoLoad(sci, sci_rrf, rrfOut, codingSchemeSAB, lockInfo));
            conversion.start();
        } else {
            new DoLoad(sci, sci_rrf, rrfOut, codingSchemeSAB, lockInfo).run();
        }
    }

    private URNVersionPair lockAndVerify(String codingSchemeName, String version, String urn)
            throws LBParameterException, LBInvocationException {
        boolean alreadyLoaded = true;
        URNVersionPair lockInfo = new URNVersionPair(urn, version);
        lock(lockInfo);
        try {
            ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeName, version);
        } catch (LBParameterException e) {
            // this is a good thing, means that is hasn't been loaded.
            alreadyLoaded = false;
        }

        if (alreadyLoaded) {
            inUse = false;
            unlock(lockInfo);
            throw new LBParameterException("That version (" + version
                    + ") of the requested coding scheme is already loaded.");
        }

        return lockInfo;
    }

    /**
     * This load method is for the UMLS in RRF form.
     */
    public void load(URI source, LocalNameList targetTerminologies, boolean stopOnErrors, boolean async)
            throws LBException {
        if (targetTerminologies == null || targetTerminologies.getEntryCount() != 1) {
            throw new LBParameterException(
                    "Sorry, the UMLS loader currently only supports loading 1 (and only 1) terminology at a time."
                            + "  Please provide a single terminology in the targetTerminologies variable.");
        }

        setInUse();
        status_ = new LoadStatus();
        status_.setLoadSource(source.toString());
        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));

        URNVersionPair lockInfo = null;

        try {
            in_ = new RRFFiles(source);
            in_.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            throw new LBParameterException("The RRF file path appears to be invalid - " + e);
        }

        String codingSchemeSAB = targetTerminologies.getEntry()[0];

        try {
            URI temp = source.resolve("MRSAB.RRF");

            if (this.umlsRegisteredName_ == null) {
                umlsRegisteredName_ = (String) UMLSBaseCode.getIsoMap().get(codingSchemeSAB);
            }

            BufferedReader reader = null;
            if (temp.getScheme().equals("file")) {
                reader = new BufferedReader(new FileReader(new File(temp)));
            } else {
                reader = new BufferedReader(new InputStreamReader(temp.toURL().openConnection().getInputStream()));
            }

            boolean found = false;
            String line = reader.readLine();

            while (line != null && line.length() > 0 && !found) {
                ArrayList<String> tokens = new ArrayList<String>();
                int start = 0;
                int pos = line.indexOf('|', start);
                while (pos >= 0) {
                    tokens.add(line.substring(start, pos));
                    start = pos + 1;
                    if (start <= line.length()) {
                        pos = line.indexOf('|', start);
                    } else {
                        pos = -1;
                    }
                }

                if (tokens.size() < 24) {
                    // invalid line
                    line = reader.readLine();
                    continue;
                }

                // RSAB is the 4th token
                if (tokens.get(3).equals(codingSchemeSAB)) {
                    umlsSourceVersion_ = tokens.get(6);

                    // if we're not using the manifest, use the source
                    // version for the lock.
                    if (umlsManifestVersion_ == null) {
                        umlsManifestVersion_ = umlsSourceVersion_;
                    }

                    lockInfo = lockAndVerify(umlsRegisteredName_, umlsManifestVersion_, umlsRegisteredName_);
                    found = true;
                }
                line = reader.readLine();
            }
            reader.close();
            if (!found) {
                inUse = false;
                throw new LBParameterException("The requested UMLS SAB is not present in the provided RRF Files",
                        "codingSchemeSAB", codingSchemeSAB);
            }

        } catch (LBParameterException e) {
            inUse = false;
            unlock(lockInfo);
            throw e;
        } catch (LBInvocationException e) {
            inUse = false;
            unlock(lockInfo);
            throw e;
        } catch (Exception e) {
            inUse = false;
            unlock(lockInfo);
            throw new LBParameterException(
                    "There was a problem reading the version information from the MRSAB.RRF file - " + e);
        }

        SQLConnectionInfo sci = null; // this is where the final data will go
        SQLConnectionInfo sci_rrf = null; // this is where I will load the rrf
                                          // files to on a temporary
        // basis.
        SQLOut rrfOut = null;
        try {
            sci = ResourceManager.instance().getSQLConnectionInfoForLoad();

            out_ = new LexGridSQLOut(sci.username, sci.password, sci.server, sci.driver, sci.prefix);
            out_.testConnection();

            // Get a sql database to use for this temporary RRF data.
            sci_rrf = ResourceManager.instance().getSQLConnectionInfoForLoad();

            rrfOut = new SQLOut(sci_rrf.username, sci_rrf.password, sci_rrf.server, sci_rrf.driver);
            rrfOut.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            unlock(lockInfo);
            String id = getLogger().error("Problem connecting to the sql server", e);
            throw new LBInvocationException("There was a problem connecting to the internal sql server", id);
        }

        md_ = new MessageDirector(getName(), status_);

        URNVersionPair schemeToLoad = new URNVersionPair(umlsRegisteredName_, umlsSourceVersion_);
        if (async) {
            Thread conversion = new Thread(new DoLoad(sci, sci_rrf, rrfOut, codingSchemeSAB, schemeToLoad));
            conversion.start();
        } else {
            new DoLoad(sci, sci_rrf, rrfOut, codingSchemeSAB, schemeToLoad).run();
        }
    }

    private class DoLoad implements Runnable {
        private SQLConnectionInfo sci_;
        private SQLConnectionInfo rrf_sci_;
        private SQLOut rrfOut_;
        private String codingSchemeSAB_;
        private URNVersionPair lockInfo_;

        public DoLoad(SQLConnectionInfo sci, SQLConnectionInfo rrf_sci, SQLOut rrfOut, String codingSchemeSAB,
                URNVersionPair lockInfo) {
            sci_ = sci;
            rrf_sci_ = rrf_sci;
            rrfOut_ = rrfOut;
            codingSchemeSAB_ = codingSchemeSAB;
            lockInfo_ = lockInfo;
        }

        @SuppressWarnings("null")
        public void run() {
            SystemVariables sv = ResourceManager.instance().getSystemVariables();
            try {
                options_.add(new Option(Option.SKIP_NON_LEXGRID_FILES, new Boolean(true)));
                options_.add(new Option(Option.ROOT_RECALC, Boolean.FALSE));
                URNVersionPair[] loadedCodingSchemes = null;
                URNVersionPair[] RRFTables = null;

                // If they provided RRF files, need to load those to a DB first.
                if (in_ != null) {
                    md_.info("Loading the RRF Files into a temporary SQL database");

                    RRFTables = ConversionLauncher.startConversion(in_, rrfOut_, null, options_, md_);
                    md_.info("Done loading temporary database with RRF content");
                }

                // RRF files will now be loaded.
                try {
                    if (status_.getErrorsLogged() != null && !status_.getErrorsLogged().booleanValue()) {
                        // now do the 'real' load.
                        md_.info("Converting to LexGrid SQL format");

                        options_ = new OptionHolder();

                        options_.add(new Option(Option.ENFORCE_INTEGRITY, new Boolean(true)));
                        options_.add(new Option(Option.SQL_FETCH_SIZE, "20000"));
                        options_.add(new Option(Option.ROOT_RECALC, Boolean.FALSE));

                        UMLSSQL umlsIn = new UMLSSQL(rrfOut_.getUsername(), rrfOut_.getPassword(), rrfOut_.getServer(),
                                rrfOut_.getDriver());
                        umlsIn.setLoaderPreferences(getLoaderPreferences());
                        umlsIn.setManifestLocation(getCodingSchemeManifestURI());
                        loadedCodingSchemes = ConversionLauncher.startConversion(umlsIn, out_,
                                new String[] { codingSchemeSAB_ }, options_, md_);

                        // Apply the manifest (if it is present)
                        if (codingSchemeManifest_ != null) {
                            JDBCConnectionDescriptor sqlConfig = new JDBCConnectionDescriptor();
                            sqlConfig.setDbUid(sci_.username);
                            sqlConfig.setDbPwd(sci_.password);
                            sqlConfig.setDbUrl(sci_.server);
                            sqlConfig.setUseUTF8(true);
                            sqlConfig.setAutoRetryFailedConnections(true);
                            try {
                                sqlConfig.setDbDriver("org.gjt.mm.mysql.Driver");
                            } catch (ClassNotFoundException e) {
                                md_.error("Error Applying Manifest");
                            }
                            ManifestUtil manifestUtil = new ManifestUtil(codingSchemeManifestURI_, md_);
                            manifestUtil.applyManifest(codingSchemeManifest_, sqlConfig, sci_.prefix, lockInfo_);
                        }
                    }
                } finally {
                    if (in_ != null) {
                        // drop the RRF db since I created it.
                        md_.info("Dropping the temporary RRF database or tables");
                        if (sv.getAutoLoadSingleDBMode()) {
                            Connection temp = DBUtility.connectToDatabase(rrfOut_.getServer(), rrfOut_.getDriver(),
                                    rrfOut_.getUsername(), rrfOut_.getPassword());
                            GenericSQLModifier gsm = new GenericSQLModifier(temp);
                            PreparedStatement dropTable;
                            for (int i = 0; i < RRFTables.length; i++) {
                                dropTable = temp.prepareStatement(gsm.modifySQL("DROP TABLE " + RRFTables[i].getUrn()
                                        + " {CASCADE}"));
                                dropTable.executeUpdate();
                                dropTable.close();
                            }
                            temp.close();
                        } else {
                            DBUtility.dropDatabase(sv.getAutoLoadDBURL(), sv.getAutoLoadDBDriver(), rrf_sci_.dbName, sv
                                    .getAutoLoadDBUsername(), sv.getAutoLoadDBPassword());
                        }
                    }
                }

                if (status_.getErrorsLogged() != null && !status_.getErrorsLogged().booleanValue()) {
                    md_.info("Finished loading the DB");

                    doTransitiveTable(URNVersionPair.getCodingSchemeNames(loadedCodingSchemes), sci_);

                    doIndex(URNVersionPair.getCodingSchemeNames(loadedCodingSchemes), sci_);

                    if (sv.getAutoLoadSingleDBMode()) {
                        // some databases (access in particular) won't see new
                        // tables unless you open a
                        // new connection to them.
                        // if we are in single db mode, there may already be
                        // open connections. close them.
                        SQLInterfaceBase sib = ResourceManager.instance().getSQLInterfaceBase(sci_.username,
                                sci_.password, sci_.server, sci_.driver);
                        sib.closeUnusedConnections();
                    }

                    register(sci_);

                    status_.setState(ProcessState.COMPLETED);
                    md_.info("Load process completed without error");

                }
            } catch (Exception e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal("Failed while running the conversion", e);
            } finally {
                try {
                    if (status_.getState() == null || status_.getState().getType() != ProcessState.COMPLETED_TYPE) {
                        status_.setState(ProcessState.FAILED);
                        try {
                            getLogger().warn("Load failed.  Removing temporary resources...");
                            CleanUpUtility.removeUnusedDatabase(sv.getAutoLoadSingleDBMode() ? out_.getTablePrefix()
                                    : sci_.dbName);
                            CleanUpUtility.removeUnusedIndex(sv.getAutoLoadSingleDBMode() ? out_.getTablePrefix()
                                    : sci_.dbName);
                        } catch (LBParameterException e) {
                            // do nothing - means that the requested delete item
                            // didn't exist.
                        } catch (Exception e) {
                            getLogger().warn("Problem removing temporary resources", e);
                        }

                    }
                } finally {
                    // Load may be processed synchronously or asynchronously.
                    // Responsibility of this load thread to perform cleanup
                    // of locks and in use indicator.
                    try {
                        unlock(lockInfo_);
                    } catch (Exception e) {
                    }
                    ;
                    status_.setEndTime(new Date(System.currentTimeMillis()));
                    inUse = false;
                }
            }

        }
    }

    public void validate(URI source, LocalNameList targetTerminologies, int validationLevel) throws LBException {
        validate(source, targetTerminologies, 1, validationLevel);
    }

    public void validate(URI source, LocalNameList targetTerminologies, int hierarchyOpt, int validationLevel)
            throws LBException {
        try {
            setInUse();
            in_ = new RRFFiles(source);
            in_.testConnection();
        } catch (ConnectionFailure e) {
            throw new LBParameterException("The RRF file path appears to be invalid - " + e);
        } finally {
            inUse = false;
        }
    }

    public void validate(URI source, String uid, String pwd, String driver, LocalNameList targetTerminologies,
            int validationLevel) throws LBException {
        validate(source, uid, pwd, driver, targetTerminologies, 1, validationLevel);
    }

    public void validate(URI source, String uid, String pwd, String driver, LocalNameList targetTerminologies,
            int hierarchyOpt, int validationLevel) throws LBException {
        if (targetTerminologies == null || targetTerminologies.getEntryCount() != 1) {
            throw new LBParameterException(
                    "Sorry, the UMLS loader currently only supports loading 1 (and only 1) terminology at a time."
                            + "  Please provide a single terminology in the targetTerminologies variable.");
        }

        String codingSchemeSAB = targetTerminologies.getEntry()[0];

        Connection c = null;
        try {
            setInUse();
            c = DBUtility.connectToDatabase(source.toString(), driver, uid, pwd);
            PreparedStatement getCodingSchemeInfo = c.prepareStatement("SELECT  SVER, SSN FROM MRSAB WHERE RSAB = ?");
            getCodingSchemeInfo.setString(1, codingSchemeSAB);

            ResultSet results = getCodingSchemeInfo.executeQuery();

            if (results.next()) {
                // things look good...
            } else {
                inUse = false;
                throw new LBParameterException("The requested UMLS SAB is not present in the provided database",
                        "codingSchemeSAB", codingSchemeSAB);
            }
            results.close();
        } catch (Exception e) {
            inUse = false;
            getLogger().error("Problem connecting to the sql server containing the UMLS data", e);
            throw new LBParameterException("There was a problem connecting to the sql server containing the UMLS data."
                    + "  Perhaps one of the connection parameters is incorrect.  " + e.toString(), "");
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    // noop
                }
            }
        }
    }

    public void loadSemnet(URI source, boolean stopOnErrors, boolean async) throws LBException {
        setInUse();
        setLoaderPreferences(loaderPreferences_);
        try {
            SemNetFiles semNet = new SemNetFiles(source);
            semNet.setCodingSchemeManifest(this.getCodingSchemeManifest());
            semNet.setLoaderPreferences(this.getLoaderPreferences());
            in_ = semNet;
            in_.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            throw new LBParameterException("The Semantic Net file path appears to be invalid - " + e);
        }
        status_ = new LoadStatus();
        status_.setLoadSource(source.toString());

        options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));

        baseLoad(async);
    }

    public void validateSemnet(URI source, int validationLevel) throws LBException {
        setInUse();
        try {
            in_ = new SemNetFiles(source);
            in_.testConnection();
        } catch (ConnectionFailure e) {

            throw new LBParameterException("The Semantic Net file path appears to be invalid - " + e);
        } finally {
            inUse = false;
        }
    }
}