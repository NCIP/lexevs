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
import java.io.Reader;
import java.net.URI;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.loaders.metadata.OBOMetaDataLoader;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.apache.commons.lang.BooleanUtils;
import org.jdom.input.SAXBuilder;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.utility.MetaData;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load OBO files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MetaDataLoaderImpl extends BaseLoader implements MetaData_Loader {
    private static final long serialVersionUID = -205479865592766865L;
    public final static String name = "MetaDataLoader";
    private final static String description = "This loader loads metadata xml files into the system.";
    private ManifestUtil manifestUtil_ = null;

    public MetaDataLoaderImpl() {
        super.name_ = MetaDataLoaderImpl.name;
        super.description_ = MetaDataLoaderImpl.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(MetaDataLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(MetaDataLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        temp.setVersion(version_);

        // I'm registering them this way to avoid the lexBig service manager API.
        // If you are writing an add-on extension, you should register them
        // through the proper interface.
        ExtensionRegistryImpl.instance().registerLoadExtension(temp);
    }

    public void validateAuxiliaryData(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            int validationLevel) throws LBParameterException {
        try {
            setInUse();

            if (codingSchemeVersion == null || codingSchemeVersion.getCodingSchemeURN() == null
                    || codingSchemeVersion.getCodingSchemeURN().length() == 0
                    || codingSchemeVersion.getCodingSchemeVersion() == null
                    || codingSchemeVersion.getCodingSchemeVersion().length() == 0) {
                throw new LBParameterException("The coding scheme URN and version must be supplied.");
            }

            try {
                Reader temp;
                if (source.getScheme().equals("file")) {
                    temp = new FileReader(new File(source));
                } else {
                    temp = new InputStreamReader(source.toURL().openConnection().getInputStream());
                }
                if (validationLevel == 0) {
                    SAXBuilder saxBuilder = new SAXBuilder();
                    saxBuilder.build(new BufferedReader(temp));
                }
            }

            catch (Exception e) {
                throw new ConnectionFailure("The meta source file '" + source + "' cannot be read or is invalid.", e);
            }

        } catch (ConnectionFailure e) {
            throw new LBParameterException("The metadata file path appears to be invalid - " + e);
        } catch (LBInvocationException e) {
            throw new LBParameterException(
            "Each loader can only do one thing at a time.  Please create a new loader to do multiple loads at once.");
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new LBParameterException(e.getMessage());
        } finally {
            inUse = false;
        }

    }

    public void loadAuxiliaryData(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            boolean overwrite, boolean stopOnErrors, boolean async) throws LBParameterException, LBInvocationException {

        validateAuxiliaryData(source, codingSchemeVersion, 0);
        setInUse();
        options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));

        status_ = new LoadStatus();
        status_.setLoadSource(source.toString());

        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));
        md_ = new MessageDirector(getName(), status_);

        if (async) {
            Thread conversion = new Thread(new DoConversion(source, codingSchemeVersion, overwrite));
            conversion.start();
        } else {
            new DoConversion(source, codingSchemeVersion, overwrite).run();
        }

    }

    public void loadAuxiliaryData(Map<Object, Object> source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            boolean overwrite, boolean stopOnErrors, boolean async) throws LBException {
    }

    public void loadLexGridManifest(CodingSchemeManifest source,
            AbsoluteCodingSchemeVersionReference codingSchemeURNVersion, boolean stopOnErrors, boolean async)
    throws LBException {
        setInUse();
        options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));

        status_ = new LoadStatus();
        status_.setLoadSource(codingSchemeURNVersion.getCodingSchemeURN());

        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));

        md_ = new MessageDirector(getName(), status_);

        if (async) {
            Thread loadManifest = new Thread(new LoadManifest(source, codingSchemeURNVersion, md_));
            loadManifest.start();
        } else {

            new LoadManifest(source, codingSchemeURNVersion, md_).run();

        }

    }

    public void loadLexGridManifest(URI source, AbsoluteCodingSchemeVersionReference codingSchemeURNVersion,
            boolean stopOnErrors, boolean async) throws LBException {

        setInUse();
        options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));

        status_ = new LoadStatus();
        status_.setLoadSource(source.toString());

        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));

        md_ = new MessageDirector(getName(), status_);

        manifestUtil_ = new ManifestUtil(source, md_);
        CodingSchemeManifest manifest = manifestUtil_.getManifest();

        if (async) {
            Thread loadManifest = new Thread(new LoadManifest(manifest, codingSchemeURNVersion, md_));
            loadManifest.start();
        } else {

            new LoadManifest(manifest, codingSchemeURNVersion, md_).run();

        }

    }

    public void validateLexGridManifest(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            int validationLevel) throws LBException {
        // TODO Auto-generated method stub

    }

    private class DoConversion implements Runnable {
        URI source_;
        AbsoluteCodingSchemeVersionReference lacsvr_;
        boolean overwrite_;

        public DoConversion(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion, boolean overwrite) {
            source_ = source;
            lacsvr_ = codingSchemeVersion;
            overwrite_ = overwrite;
        }

        public void run() {
            try {

                md_.info("Loading the OBO MetaData");
                new OBOMetaDataLoader(lacsvr_.getCodingSchemeURN(), lacsvr_.getCodingSchemeVersion(), source_, false,
                        true, true, true, !overwrite_);
                md_.info("Finished loading the Metadata");
                status_.setState(ProcessState.COMPLETED);
                md_.info("Load process completed without error");
            } catch (Exception e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal("Failed while running the conversion", e);

            } finally {
                if (status_.getState() == null || status_.getState().getType() != ProcessState.COMPLETED_TYPE) {
                    status_.setState(ProcessState.FAILED);
                }
                status_.setEndTime(new Date(System.currentTimeMillis()));
                inUse = false;
            }
        }
    }

    private class LoadManifest implements Runnable {

        CodingSchemeManifest manifest_;
        AbsoluteCodingSchemeVersionReference currentURNVersion_;
        AbsoluteCodingSchemeVersionReference newURNVersion_ = new AbsoluteCodingSchemeVersionReference();

        JDBCConnectionDescriptor sqlConfig;
        SQLInterface sqlInterface;
        String tablePrefix;
        MessageDirector message_ = null;

        public LoadManifest(CodingSchemeManifest source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
                MessageDirector md) {
            manifest_ = source;
            currentURNVersion_ = codingSchemeVersion;
            message_ = md;
        }

        public void run() {

            message_.info("Start Time : " + new Date(System.currentTimeMillis()));
            try {

                String urn = currentURNVersion_.getCodingSchemeURN();
                String version = currentURNVersion_.getCodingSchemeVersion();

                boolean overrideURN = 
                    (manifest_.getCodingSchemeURI() != null
                        ? BooleanUtils.toBoolean(manifest_.getCodingSchemeURI().getToOverride())
                        : false);
                boolean overrideVersion =
                    (manifest_.getRepresentsVersion() != null
                        ? BooleanUtils.toBoolean(manifest_.getRepresentsVersion().getToOverride())
                        : false);

                if (overrideURN) {
                    newURNVersion_.setCodingSchemeURN(manifest_.getCodingSchemeURI().getContent());
                } else {
                    newURNVersion_.setCodingSchemeURN(urn);
                }

                if (overrideVersion) {
                    newURNVersion_.setCodingSchemeVersion(manifest_.getRepresentsVersion().getContent());
                } else {
                    newURNVersion_.setCodingSchemeVersion(version);
                }

                /* -Check if supplied urn version is valid- */
                try {
                    ResourceManager.instance()
                    .getInternalCodingSchemeNameForUserCodingSchemeName(urn, version);

                } catch (LBParameterException e1) {
                    message_.fatalAndThrowException("Supplied Coding Scheme URN Version pair is not valid.");

                }

                /* -Check if ontology with same URN and Version already exists.- */
                try {
                    if ((!urn.equals(newURNVersion_.getCodingSchemeURN()) && overrideURN)
                            || (!version.equals(newURNVersion_.getCodingSchemeVersion()) && overrideVersion)) {

                        String codingScheme = ResourceManager.instance()
                        .getInternalCodingSchemeNameForUserCodingSchemeName(
                                newURNVersion_.getCodingSchemeURN(), newURNVersion_.getCodingSchemeVersion());
                        if (codingScheme != null) {
                            message_
                            .fatalAndThrowException("An ontology with the URN and Version specified in manifest is already loaded.");
                        }

                    }

                } catch (LBParameterException e1) {
                    // do nothing
                }

                /*-Get JDBCConnectionDescriptor, tablePrefix & currentURNVersion-*/
                String internalVersion = null;
                String internalCSName = null;

                try {
                    internalVersion = ResourceManager.instance().getInternalVersionStringFor(
                            currentURNVersion_.getCodingSchemeURN(), null);
                    internalCSName = ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(
                            currentURNVersion_.getCodingSchemeURN(), internalVersion);

                } catch (LBParameterException e1) {
                    message_
                    .fatalAndThrowException("Exception occured while obtaining internalVersion and internal codingscheme: "
                            + e1.getMessage());
                }

                JDBCConnectionDescriptor sqlConfig = null;
                String tablePrefix = null;

                try {

                    sqlInterface = ResourceManager.instance().getSQLInterface(internalCSName, internalVersion);

                } catch (MissingResourceException e) {
                    message_
                    .fatalAndThrowException("Exception occured while obtaining sqlInterface: " + e.getMessage());
                }

                sqlConfig = sqlInterface.getConnectionDescriptor();
                tablePrefix = sqlInterface.getTablePrefix();

                URNVersionPair currentURNVersion = new URNVersionPair(urn, version);

                /* --------------------- Apply Manifest ---------------------- */
                message_.info("Applying manifest entries to the coding scheme...");
                try {

                    manifestUtil_.applyManifest(manifest_, sqlConfig, tablePrefix, currentURNVersion);

                } catch (LgConvertException e) {
                    message_.fatalAndThrowException("Exception occured while applying manifest: " + e.getMessage());
                } catch (SQLException e) {
                    message_.fatalAndThrowException("Exception occured while applying manifest: " + e.getMessage());
                }

                message_.info("Coding scheme is updated with manifest entries.");

                boolean urnChanged = !urn.equals(newURNVersion_.getCodingSchemeURN()) && overrideURN;
                boolean versionChanged = !version.equals(newURNVersion_.getCodingSchemeVersion()) && overrideVersion;

                /*-------- Update Registry if urn or version changed --------*/
                if (urnChanged || versionChanged) {
                    message_.info("Updating registry.xml...");
                    Registry reg = ResourceManager.instance().getRegistry();
                    try {

                        reg.updateURNVersion(currentURNVersion_, newURNVersion_);
                        ResourceManager.reInit(null);

                    } catch (LBInvocationException e) {
                        message_.error("Exception occured while updating registry: " + e.getMessage());
                    } catch (LBParameterException e) {
                        message_.error("Exception occured while updating registry: " + e.getMessage());
                    }

                    message_.info("Finished updating registry.xml...");
                }

                /*-------- Update Index Metadata if version changed --------*/
                if (versionChanged) {
                    message_.info("Updating index metadata...");
                    IndexInterface indexInterface = ResourceManager.instance().getMetaDataIndexInterface();
                    IndexerService indexService = indexInterface.getBaseIndexerService();
                    try {
                        MetaData metadata = indexService.getMetaData();
                        String[] keys = metadata.getIndexMetaDataKeys();

                        for (int i = 0; i < keys.length; i++) {
                            if (keys[i].startsWith(internalCSName) && keys[i].endsWith(internalVersion)) {
                                String indexName = metadata.getIndexMetaDataValue(keys[i]);

                                // Remove old header mapping from scheme/version to index name,
                                // and the version within the index section ...
                                metadata.removeIndexMetaDataValue(keys[i]);
                                metadata.removeIndexMetaDataValue(indexName, internalVersion);

                                // Add the new header and version entry ...
                                String newVersion = newURNVersion_.getCodingSchemeVersion();
                                metadata.setIndexMetaDataValue(internalCSName + "[:]" + newVersion, indexName);
                                metadata.setIndexMetaDataValue(indexName, "version", newVersion);

                                // Force refresh of cached resource info ...
                                try {
                                    ResourceManager.reInit(null);
                                } catch (Exception e) {
                                }
                                break;
                            }
                        }
                    } catch (InternalErrorException e) {
                        message_.error("Exception occured updating index metadata : " + e.getMessage());
                    }

                    message_.info("Finished updating index metadata...");
                }

                status_.setState(ProcessState.COMPLETED);

                message_.info("Manifest process completed without error!");

            } catch (Exception e) {
                message_.fatal("Load failed due to exception." + e.getMessage());

            } finally {
                if (status_.getState() == null || status_.getState().getType() != ProcessState.COMPLETED_TYPE) {
                    status_.setState(ProcessState.FAILED);
                }
                status_.setEndTime(new Date(System.currentTimeMillis()));
                inUse = false;
            }
        }
    }
}