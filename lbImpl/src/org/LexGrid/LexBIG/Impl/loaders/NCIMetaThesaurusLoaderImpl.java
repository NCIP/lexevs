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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.NCI_MetaThesaurusLoader;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Utility.Constructors;
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

import edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon.LoadNCIMetaThesMetadata;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.ConversionLauncher;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OptionHolder;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIMetaThesaurusSQL;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.RRFFiles;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.SQLOut;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.PreferenceLoaderConstants;

/**
 * Loader code for the NCI_MetaThesaurus.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class NCIMetaThesaurusLoaderImpl extends BaseLoader implements NCI_MetaThesaurusLoader {
    private static final long serialVersionUID = -5547663355411478675L;
    public final static String name = "NCIMetaThesaurusLoader";
    public final static String description = "This loader loads NCI MetaThesaurus RRF Files into the database.";

    public static final String MetaName = "NCI MetaThesaurus";
    private String metaURN = "urn:oid:2.16.840.1.113883.3.26.1.2";
    private String metaSourceVersion;
    private String metaManifestVersion;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public NCIMetaThesaurusLoaderImpl() {
        super.name_ = name;
        super.description_ = description;
    }

    /**
     * This override changes the Registered Name of the coding scheme when the
     * manifest is applied. Because this must be done before any loading to the
     * database can be done, we want to make sure that the Registered Name is
     * set as soon as the manifest is set.
     */
    public void setCodingSchemeManifestURI(URI manifestLocation) {
        super.setCodingSchemeManifestURI(manifestLocation);
        if (codingSchemeManifest_ != null) {
            CsmfCodingSchemeURI registeredName = codingSchemeManifest_.getCodingSchemeURI();
            if (registeredName != null && registeredName.isToOverride()) {
                metaURN = registeredName.getContent();
            }
            CsmfVersion version = codingSchemeManifest_.getRepresentsVersion();
            if (version != null && version.isToOverride()) {
                metaManifestVersion = version.getContent();
            }
        }
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(NCIMetaThesaurusLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(NCIMetaThesaurusLoaderImpl.class.getName());
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

    public void load(URI source, boolean stopOnErrors, boolean async) throws LBException {
        setInUse();
        status_ = new LoadStatus();
        status_.setLoadSource(source.toString());
        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));

        try {
            in_ = new RRFFiles(source);
            in_.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            throw new LBParameterException("The RRF file path appears to be invalid - " + e);
        }

        metaSourceVersion = getVersionFromRRF(source);
        if (metaManifestVersion == null) {
            metaManifestVersion = metaSourceVersion;
        }

        URNVersionPair lockInfo = new URNVersionPair(metaURN, metaManifestVersion);
        lock(lockInfo);

        // make sure that this version of the MetaThesaurus hasn't already been
        // loaded
        if (isLoaded(metaURN, source, metaManifestVersion)) {
            inUse = false;
            unlock(lockInfo);
            throw new LBParameterException("Version '" + metaManifestVersion
                    + "' of the metathesaurus is already loaded.");
        }

        SQLConnectionInfo sci = null;
        SQLConnectionInfo sci_rrf = null;
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

        URNVersionPair schemeToLoad = new URNVersionPair(metaURN, metaSourceVersion);
        if (async) {
            Thread conversion = new Thread(new DoLoad(sci, sci_rrf, rrfOut, schemeToLoad, false));
            conversion.start();
        } else {
            new DoLoad(sci, sci_rrf, rrfOut, schemeToLoad, false).run();
        }
    }

    private class DoLoad implements Runnable {
        private SQLConnectionInfo sci_;
        private SQLConnectionInfo rrf_sci_;
        private SQLOut rrfOut_;
        private URNVersionPair lockInfo_;
        private boolean rootRecalc_;

        public DoLoad(SQLConnectionInfo sci, SQLConnectionInfo rrf_sci, SQLOut rrfOut, URNVersionPair lockInfo,
                boolean rootRecalc) {
            sci_ = sci;
            rrf_sci_ = rrf_sci;
            rrfOut_ = rrfOut;
            lockInfo_ = lockInfo;
            rootRecalc_ = rootRecalc;
        }

        public void run() {
            SystemVariables sv = ResourceManager.instance().getSystemVariables();
            try {
                options_.add(new Option(Option.SKIP_NON_LEXGRID_FILES, Boolean.TRUE));
                options_.add(new Option(Option.ROOT_RECALC, new Boolean(rootRecalc_)));

                md_.info("Loading the RRF Files into a temporary SQL database");
                URNVersionPair[] loadedCodingSchemes = null;
                URNVersionPair[] RRFTables = ConversionLauncher.startConversion(in_, rrfOut_, null, options_, md_);

                if (status_.getErrorsLogged() != null && !status_.getErrorsLogged().booleanValue()) {
                    md_.info("Done loading temporary database with RRF content");

                    // Create the Input format to use for the newly created RRF
                    // content databases
                    NCIMetaThesaurusSQL nciIn = new NCIMetaThesaurusSQL(rrfOut_.getUsername(), rrfOut_.getPassword(),
                            rrfOut_.getServer(), rrfOut_.getDriver());
                    nciIn.setLoaderPreferences(getLoaderPreferences());
                    nciIn.setManifestLocation(codingSchemeManifestURI_);

                    // now do the 'real' load.
                    md_.info("Converting to LexGrid SQL format");

                    options_ = new OptionHolder();

                    options_.add(new Option(Option.ENFORCE_INTEGRITY, Boolean.TRUE));
                    options_.add(new Option(Option.SQL_FETCH_SIZE, "20000"));
                    options_.add(new Option(Option.ROOT_RECALC, new Boolean(rootRecalc_)));

                    loadedCodingSchemes = ConversionLauncher.startConversion(nciIn, out_, null, options_, md_);

                    // Apply the post-load manifest (if it is present)
                    if (codingSchemeManifestURI_ != null) {
                        JDBCConnectionDescriptor sqlConfig = new JDBCConnectionDescriptor();
                        sqlConfig.setDbUid(sci_.username);
                        sqlConfig.setDbPwd(sci_.password);
                        sqlConfig.setDbUrl(sci_.server);
                        sqlConfig.setUseUTF8(true);
                        sqlConfig.setAutoRetryFailedConnections(true);
                        try {
                            sqlConfig.setDbDriver(sci_.driver);
                        } catch (ClassNotFoundException e) {
                            md_.error("Error Applying Manifest");
                        }
                        ManifestUtil manifestUtil = new ManifestUtil(codingSchemeManifestURI_, md_);
                        manifestUtil.applyManifest(codingSchemeManifest_, sqlConfig, sci_.prefix, lockInfo_);
                    }

                    // Load Metadata if the path has been specified in the
                    // Loader Preferences
                    if (loaderPreferences_ != null && loaderPreferences_.getXMLMetadataFilePath() != null) {
                        String fileName = PreferenceLoaderConstants.META_METADATA_FILE_NAME;

                        File metaDataFile = new File(loaderPreferences_.getXMLMetadataFilePath() + "/" + fileName);

                        try {
                            // try to create the file - if the file path is
                            // invalid
                            // we discontinue metadata loading.
                            metaDataFile.createNewFile();

                            // Create the Metadata Loader,
                            LoadNCIMetaThesMetadata metadataLoader = new LoadNCIMetaThesMetadata(metaDataFile.toURI(),
                                    rrfOut_.getServer(), rrfOut_.getDriver(), rrfOut_.getUsername(), rrfOut_
                                            .getPassword(), md_);

                            md_.info("Loading Metadata from: " + metaDataFile.getAbsolutePath());

                            // Output Metadata XML file
                            metadataLoader.loadMetadata();

                            // Load in the Metadata XML
                            MetaData_Loader mdLoader = new MetaDataLoaderImpl();
                            mdLoader.loadAuxiliaryData(
                        		metaDataFile.toURI(),
                        		Constructors.createAbsoluteCodingSchemeVersionReference(metaURN, metaManifestVersion),
                        		true, false, true);
                        } catch (IOException e) {
                            md_.warn("Supplied Metadata XML file location is invalid. Not loading Metadata.", e);
                        }
                    } else {
                        md_.info("No file path was specified in the Loader Preferences, not loading Metadata.");
                    }

                }

                // drop the RRF db
                md_.info("Dropping the temporary RRF database or tables");
                if (sv.getAutoLoadSingleDBMode()) {
                    Connection temp = DBUtility.connectToDatabase(rrfOut_.getServer(), rrfOut_.getDriver(), rrfOut_
                            .getUsername(), rrfOut_.getPassword());
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

                if (status_.getErrorsLogged() != null && !status_.getErrorsLogged().booleanValue()) {
                    md_.info("Finished loading the DB");

                    if (!rootRecalc_) {
                        // this will create a blank table, but will leave it
                        // empty.
                        doTransitiveTable(new String[] { "" }, sci_);

                        doIndex(URNVersionPair.getCodingSchemeNames(loadedCodingSchemes), sci_);
                    }

                    if (sv.getAutoLoadSingleDBMode()) {
                        // some databases (access in particular) won't see new
                        // tables unless you open a new
                        // connection to them.
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

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.Loaders.LoaderExtensions.NCI_MetaThesaurusLoader#
     * recalcRootNodes(java.net.URI, boolean)
     */
    public void recalcRootNodes(URI source, boolean async) throws LBException {
        setInUse();

        status_ = new LoadStatus();
        status_.setLoadSource(source.toString());
        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));

        URNVersionPair lockInfo = null;
        SQLConnectionInfo sci = null;
        SQLConnectionInfo sci_rrf = null;
        SQLOut rrfOut = null;

        try {
            try {
                in_ = new RRFFiles(source);
                in_.testConnection();
            } catch (ConnectionFailure e) {
                throw new LBParameterException("The RRF file path appears to be invalid - " + e);
            }

            metaSourceVersion = getVersionFromRRF(source);
            if (metaManifestVersion == null) {
                metaManifestVersion = metaSourceVersion;
            }
            if (!isLoaded(metaURN, source, metaManifestVersion)) {
                throw new LBParameterException("Root nodes cannot be recalculated.  Version '" + metaManifestVersion
                        + "' of the metathesaurus is not loaded.");
            }

            try {
                lockInfo = new URNVersionPair(metaURN, metaManifestVersion);
                lock(lockInfo);

                // Get connect information for the currently loaded source.
                AbsoluteCodingSchemeVersionReference acsvr = Constructors.createAbsoluteCodingSchemeVersionReference(
                        metaURN, lockInfo.getVersion());
                sci = ResourceManager.instance().getRegistry().getSQLConnectionInfoForCodeSystem(acsvr);
                out_ = new LexGridSQLOut(sci.username, sci.password, sci.server, sci.driver, sci.prefix);

                // Get a database to use for this temporary RRF data.
                sci_rrf = ResourceManager.instance().getSQLConnectionInfoForLoad();
                rrfOut = new SQLOut(sci_rrf.username, sci_rrf.password, sci_rrf.server, sci_rrf.driver);
                rrfOut.testConnection();
            } catch (ConnectionFailure e) {
                String id = getLogger().error("Problem connecting to the sql server", e);
                throw new LBInvocationException("There was a problem connecting to the internal sql server", id);
            }
        } catch (Exception e) {
            inUse = false;
            unlock(lockInfo);
            if (e instanceof LBException)
                throw (LBException) e;
            String id = getLogger().error("Unexpected error", e);
            throw new LBInvocationException("An unexpected error occurred attempting to process the request.", id);
        }

        md_ = new MessageDirector(getName(), status_);
        if (async) {
            Thread operation = new Thread(new DoLoad(sci, sci_rrf, rrfOut, lockInfo, true));
            operation.start();
        } else {
            new DoLoad(sci, sci_rrf, rrfOut, lockInfo, true).run();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Loaders.LoaderExtensions.NCI_MetaThesaurusLoader#validate
     * (java.net.URI, int)
     */
    public void validate(URI source, int validationLevel) throws LBException {
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

    /**
     * Indicates whether or not a specific version of the NCI MetaThesaurus is
     * loaded to the LexGrid repository.
     * 
     * @param manifestRegisteredName
     *            Registered Name specified by the Manifest (if any)
     * @param sourceURI
     *            Location of the RRF source files being loaded; not null.
     * @param version
     *            Version identifier to check. If null, the version is retrieved
     *            from the RRF.
     * @return true if loaded; false otherwise.
     * @throws LBException
     */
    protected boolean isLoaded(String name, URI sourceURI, String version) throws LBException {
        boolean isLoaded = false;

        if (version == null)
            version = getVersionFromRRF(sourceURI);
        try {
            ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(name, version);
            isLoaded = true;
        } catch (LBParameterException lbe) {
        }
        return isLoaded;
    }

    /**
     * Attempt to resolve the version from RRF files (specifically, MRDOC)
     * stored in the given source directory.
     * 
     * @param sourceURI
     *            Location of the RRF source files being loaded; not null.
     * @return The version string; null if not available in the source.
     * @throws LBException
     */
    protected String getVersionFromRRF(URI sourceURI) throws LBException {
        String version = null;
        BufferedReader reader = null;
        try {
            URI temp = sourceURI.resolve("MRDOC.RRF");
            reader = ("file".equalsIgnoreCase(temp.getScheme()) ? new BufferedReader(new FileReader(new File(temp)))
                    : new BufferedReader(new InputStreamReader(temp.toURL().openConnection().getInputStream())));

            String line;
            String matchPrefix = "RELEASE|umls.release.name|release_info|";
            while ((line = reader.readLine()) != null && version == null) {
                if (line.startsWith(matchPrefix)) {
                    version = line.substring(matchPrefix.length());
                    if (version.endsWith("|"))
                        version = version.substring(0, version.length() - 1);
                }
            }
        } catch (Exception e) {
            inUse = false;
            throw new LBParameterException(
                    "Version information could not be found in the MRDOC.RRF file.  "
                            + "Please add a line with this format to the MRDOC.RRF file - 'RELEASE|umls.release.name|release_info|2006AB|'");
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                }
            ;
        }
        return version;
    }

    protected void loadMetadata() {
        status_.getLoadSource();
    }
}