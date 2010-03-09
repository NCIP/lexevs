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

import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.ApproxNumOfConceptsPostProcessor;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.SupportedAttributePostProcessor;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexevs.dao.database.connection.SQLConnectionInfo;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.registry.WriteLockManager;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.options.DefaultOptionHolder;
import edu.mayo.informatics.lexgrid.convert.options.StringArrayOption;
import edu.mayo.informatics.lexgrid.convert.options.URIOption;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.PreferenceLoaderFactory;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.interfaces.PreferenceLoader;

/**
 * Common loader code.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class BaseLoader extends AbstractExtendable implements Loader{
    // TODO - loaders are not yet setting concept / relationship counts.
    // TODO - It would be nice if the loader would set the the approximate
    // concept count value when
    // they finish loading something
    
    private static String LOADER_POST_PROCESSOR_OPTION = "Loader Post Processor (Extension Name)";
    
    private AbsoluteCodingSchemeVersionReference[] codingSchemeReferences
        = new AbsoluteCodingSchemeVersionReference[0];

    protected boolean inUse = false;
    private CachingMessageDirectorIF md_;
    private LoadStatus status_;
   
    private OptionHolder options_;
    private CodingSchemeManifest codingSchemeManifest_;
    private LoaderPreferences loaderPreferences_;
    
    private URI codingSchemeManifestURI_;
    private URI resourceUri;
    
    private boolean doIndexing = true;
    private boolean doComputeTransitiveClosure = true;
    private boolean doRegister = true;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    public BaseLoader(){
        OptionHolder holder = new DefaultOptionHolder();
        URIOption manifiestOption = new URIOption("Manifest File");
        manifiestOption.addAllowedFileExtensions("*.xml");
        
        holder.getURIOptions().add(manifiestOption);
        
        URIOption loaderPreferencesOption = new URIOption("Loader Preferences File");
        loaderPreferencesOption.addAllowedFileExtensions("*.xml");
        
        holder.getURIOptions().add(loaderPreferencesOption);
        
        StringArrayOption loaderPostProcessorOption = new StringArrayOption(LOADER_POST_PROCESSOR_OPTION);
        loaderPostProcessorOption.getOptionValue().add(ApproxNumOfConceptsPostProcessor.EXTENSION_NAME);
        loaderPostProcessorOption.getOptionValue().add(SupportedAttributePostProcessor.EXTENSION_NAME);
        holder.getStringArrayOptions().add(loaderPostProcessorOption);
        
        this.options_= this.declareAllowedOptions(holder);
    }

    protected void baseLoad(boolean async) throws LBInvocationException {
       status_ = new LoadStatus();

        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));
        md_ = new CachingMessageDirectorImpl( new MessageDirector(getName(), status_));

        if (async) {
            Thread conversion = new Thread(new DoConversion());
            conversion.start();
        } else {
            new DoConversion().run();
        }
    }

    /**
     * Reindexes the specified registered code system - otherwise, if no code
     * system is specified, reindexes all registered code systems.
     * 
     * @param codingSchemeVersion
     * @throws LBInvocationException
     * @throws LBParameterException
     */
    protected void reindexCodeSystem(AbsoluteCodingSchemeVersionReference codingSchemeVersion, boolean async)
            throws LBInvocationException, LBParameterException {
        setInUse();
        ArrayList<AbsoluteCodingSchemeVersionReference> temp = new ArrayList<AbsoluteCodingSchemeVersionReference>();

        if (codingSchemeVersion == null) {
            // They want me to reindex all of the code systems...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            CodingSchemeRendering[] csr = lbs.getSupportedCodingSchemes().getCodingSchemeRendering();
            for (int i = 0; i < csr.length; i++) {
                temp.add(Constructors.createAbsoluteCodingSchemeVersionReference(csr[i].getCodingSchemeSummary()));
            }

        } else if (codingSchemeVersion.getCodingSchemeURN() == null
                || codingSchemeVersion.getCodingSchemeURN().length() == 0
                || codingSchemeVersion.getCodingSchemeVersion() == null
                || codingSchemeVersion.getCodingSchemeVersion().length() == 0) {
            inUse = false;
            throw new LBParameterException(
                    "If you supply a codingSchemeVersion, it needs to contain both the coding scheme and the version");
        } else {
            SQLConnectionInfo sci = ResourceManager.instance().getRegistry().getSQLConnectionInfoForCodeSystem(
                    codingSchemeVersion);
            if (sci == null) {
                inUse = false;
                String id = getLogger().error("Couldn't map urn / version to internal name");
                throw new LBInvocationException("There was an unexpected internal error", id);
            }

            temp.add(codingSchemeVersion);
        }

        status_ = new LoadStatus();
        status_.setLoadSource(null); // doesn't apply
        status_.setStartTime(new Date(System.currentTimeMillis()));
        md_ = new CachingMessageDirectorImpl( new MessageDirector(getName(), status_));

        if (async) {
            Thread reIndex = new Thread(new ReIndex(temp));
            reIndex.start();
        } else {
            new ReIndex(temp).run();
        }

    }

    /*
     * A loader class can only safely do one thing at a time.
     */
    protected void setInUse() throws LBInvocationException {
        if (inUse) {
            throw new LBInvocationException(
                    "This loader is already in use.  Construct a new loader to do two operations at the same time", "");
        }
        inUse = true;
    }
    
    protected AbsoluteCodingSchemeVersionReference[]
        urnVersionPairToAbsoluteCodingSchemeVersionReference(URNVersionPair[] versionPairs) {
        AbsoluteCodingSchemeVersionReference[] refs = new AbsoluteCodingSchemeVersionReference[versionPairs.length];
        for(int i=0;i<versionPairs.length;i++) {
            URNVersionPair pair = versionPairs[i];
            refs[i] = Constructors.createAbsoluteCodingSchemeVersionReference(pair.getUrn(), pair.getVersion());
        }
        return refs;
    }

    private class DoConversion implements Runnable {
 
        @SuppressWarnings("null")
        public void run() {
            SystemVariables sv = ResourceManager.instance().getSystemVariables();
            URNVersionPair[] locks = null;
            try {          
                
                // Actually do the load
                URNVersionPair[] loadedCodingSchemes = doLoad();
                codingSchemeReferences = urnVersionPairToAbsoluteCodingSchemeVersionReference(loadedCodingSchemes);

                String[] codingSchemeNames = new String[loadedCodingSchemes.length];
                for (int i = 0; i < loadedCodingSchemes.length; i++) {
                    codingSchemeNames[i] = loadedCodingSchemes[i].getUrn();
                }

                if (status_.getErrorsLogged() != null && !status_.getErrorsLogged().booleanValue()) {

                    md_.info("Finished loading the DB");
                    Snapshot snap = SimpleMemUsageReporter.snapshot();
                    md_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                    
                    doPostProcessing(options_, codingSchemeReferences);
                    
                    doTransitiveAndIndex(codingSchemeReferences);

                    md_.info("After Indexing");
                    snap = SimpleMemUsageReporter.snapshot();
                    md_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                    if(doRegister) {
                        register(loadedCodingSchemes);
                    }

                    status_.setState(ProcessState.COMPLETED);
                    md_.info("Load process completed without error");
                    
                }
            } catch (CodingSchemeAlreadyLoadedException e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal(e.getMessage());
            } catch (Exception e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal("Failed while running the conversion", e);
            } finally {
                if (status_.getState() == null || status_.getState().getType() != ProcessState.COMPLETED_TYPE) {
                    status_.setState(ProcessState.FAILED);

                    try {
                        if (locks != null) {
                            for (int i = 0; i < locks.length; i++) {
                                unlock(locks[i]);
                            }
                        }

                        getLogger().warn("Load failed.  Removing temporary resources...");
                        SystemResourceService service = 
                               LexEvsServiceLocator.getInstance().getSystemResourceService();
                        
                        for(AbsoluteCodingSchemeVersionReference ref : codingSchemeReferences) {
                            service.removeCodingSchemeResourceFromSystem(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
                        }

                    } catch (LBParameterException e) {
                        // do nothing - means that the requested delete item
                        // didn't exist.
                    } catch (Exception e) {
                        getLogger().warn("Problem removing temporary resources", e);
                    }

                }
                status_.setEndTime(new Date(System.currentTimeMillis()));
                inUse = false;
            }

        }
    }
    
    protected void doPostProcessing(OptionHolder options, AbsoluteCodingSchemeVersionReference[] references) throws LBParameterException {
        List<String> postProcessors =
            options.getStringArrayOption(LOADER_POST_PROCESSOR_OPTION).getOptionValue();
        
        for(String postProcessor : postProcessors) {
            md_.info("Running PostProcessor:" + postProcessor);
            
            for(AbsoluteCodingSchemeVersionReference ref : references) {
                getPostProcessor(postProcessor).runPostProcess(ref);
            }
        }
    }

    protected void doTransitiveAndIndex(AbsoluteCodingSchemeVersionReference[] references) throws Exception {
        if(doComputeTransitiveClosure) {
           // doTransitiveTable(codingSchemes, sci);
        }
        if(doIndexing) {
            doIndex(references);
        }
    }

    /**
     * Build root (or tail) nodes.
     * 
     * @param codingSchemes
     * @param associations
     * @param sci
     * @param root
     *            - true for root nodes, false for tail nodes.
     * @throws Exception
     */
    protected void buildRootNode(String[] codingSchemes, String[] associations, SQLConnectionInfo sci, boolean root)
            throws Exception {
        md_.info("Building the root node");
        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(sci.server, sci.driver, sci.username, sci.password);
            SQLTableUtilities stu = new SQLTableUtilities(c, sci.prefix);
            for (int i = 0; i < codingSchemes.length; i++) {
                stu.addRootRelationNode(codingSchemes[i], associations, stu.getNativeRelation(codingSchemes[i]), root);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        md_.info("Finished building root node");
    }

    protected void doIndex(AbsoluteCodingSchemeVersionReference[] references) throws Exception {
        Snapshot snap1 = SimpleMemUsageReporter.snapshot();
        md_.info("Building the index");

        buildIndex(references);
        Snapshot snap2 = SimpleMemUsageReporter.snapshot();
        md_.info("Finished indexing.   Time to index: "
                + SimpleMemUsageReporter.formatTimeDiff(snap2.getTimeDelta(snap1)) + "   Heap usage: "
                + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsage()) + "   Heap delta: "
                + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsageDelta(snap1)));

    }

    protected void doTransitiveTable(String[] codingSchemes, SQLConnectionInfo sci) throws Exception {
        Snapshot snap1 = SimpleMemUsageReporter.snapshot();
        md_.info("Loading transitive expansion table");
        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(sci.server, sci.driver, sci.username, sci.password);
            SQLTableUtilities stu = new SQLTableUtilities(c, sci.prefix);
            for (int i = 0; i < codingSchemes.length; i++) {
                stu.computeTransitivityTable(codingSchemes[i], md_);
                //There are memory issues with the new transitive closure implementation...Doesn't scale for large ontologies.               
                //TransitiveClosure tc= new TransitiveClosure(c, stu, codingSchemes[i],md_);
                //tc.computeTransitivityTable();
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        Snapshot snap2 = SimpleMemUsageReporter.snapshot();
        md_.info("Finished building transitive expansion.   Time taken: "
                + SimpleMemUsageReporter.formatTimeDiff(snap2.getTimeDelta(snap1)) + "   Heap usage: "
                + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsage()) + "   Heap delta: "
                + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsageDelta(snap1)));

    }

    protected void register(URNVersionPair[] loadedCodingSchemes) throws Exception {
         SystemResourceService service = LexEvsServiceLocator.getInstance().getSystemResourceService();
         for(URNVersionPair pair : loadedCodingSchemes ){
            if(service.containsCodingSchemeResource(pair.getUrn(), pair.getVersion())) {
                service.updateCodingSchemeResourceStatus(Constructors.
                        createAbsoluteCodingSchemeVersionReference(pair.getUrn(), pair.getVersion()),
                        CodingSchemeVersionStatus.INACTIVE);
            }
         }
    }

    private class ReIndex implements Runnable {
        ArrayList<AbsoluteCodingSchemeVersionReference> codingSchemeVersion_;

        public ReIndex(ArrayList<AbsoluteCodingSchemeVersionReference> codingSchemeVersion) {
            codingSchemeVersion_ = codingSchemeVersion;
        }

        public void run() {
            status_.setState(ProcessState.PROCESSING);
            for (int i = 0; i < codingSchemeVersion_.size(); i++) {
                boolean success = false;

                CodingSchemeVersionStatus status = null;
                try {
                    SQLConnectionInfo sci = ResourceManager.instance().getRegistry().getSQLConnectionInfoForCodeSystem(
                            codingSchemeVersion_.get(i));
                    String csn = ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(sci.urn,
                            sci.version);

                    status = ResourceManager.instance().getRegistry().getStatus(
                            codingSchemeVersion_.get(i).getCodingSchemeURN(),
                            codingSchemeVersion_.get(i).getCodingSchemeVersion());
                    ResourceManager.instance().setPendingStatus(codingSchemeVersion_.get(i));
                    WriteLockManager.instance().acquireLock(codingSchemeVersion_.get(i).getCodingSchemeURN(),
                            codingSchemeVersion_.get(i).getCodingSchemeVersion());

                    md_.info("beginning index process for " + csn);
                   
                    //TODO: Fix Reindexing
                    //buildIndex(new String[] { csn }, sci, md_);

                    // make sure that searches wont hit on a cache from the old
                    // index.
                    // If the index was missing outright - this can fail with a
                    // missing resource exception. recover
                    try {
                        ResourceManager.instance().getIndexInterface(csn, sci.version).reopenIndex(csn, sci.version);
                    } catch (MissingResourceException e) {
                        ResourceManager.instance().rereadAutoLoadIndexes();
                    }

                    success = true;
                    md_.info("Finished indexing " + csn);

                } catch (Exception e) {
                    status_.setState(ProcessState.FAILED);
                    md_.fatal("Failed while running the conversion", e);
                } finally {
                    try {
                        WriteLockManager.instance().releaseLock(codingSchemeVersion_.get(i).getCodingSchemeURN(),
                                codingSchemeVersion_.get(i).getCodingSchemeVersion());
                        if (status != null) {
                            // restore the state if we had a successful reindex.
                            if (status.getType() == CodingSchemeVersionStatus.ACTIVE_TYPE && success) {
                                ResourceManager.instance().getRegistry().activate(codingSchemeVersion_.get(i));
                            } else if (status.getType() == CodingSchemeVersionStatus.INACTIVE_TYPE && success) {
                                ResourceManager.instance().deactivate(codingSchemeVersion_.get(i), null);
                            }
                        }
                    } catch (Exception e) {
                        md_.fatal("Failed while running the conversion", e);
                    }
                    if (!success) {
                        status_.setState(ProcessState.FAILED);
                    }

                }
            }
            if (status_.getState().getType() == ProcessState.PROCESSING_TYPE) {
                status_.setState(ProcessState.COMPLETED);
            }
            status_.setEndTime(new Date(System.currentTimeMillis()));
            inUse = false;
        }
    }

    public LoadStatus getStatus() {
        return status_;
    }

    public LogEntry[] getLog(LogLevel level) {
        if (md_ == null) {
            return new LogEntry[] {};
        }
        return md_.getLog(level);
    }

    public void clearLog() {
        if (md_ != null) {
            md_.clearLog();
        }
    }

    public String getName() {
        return this.getExtensionDescription().getName();
    }

    public String getDescription() {
        return this.getExtensionDescription().getDescription();
    }

    public String getVersion() {
        return this.getExtensionDescription().getVersion();
    }

    public String getProvider() {
        return this.getExtensionDescription().getExtensionProvider().getContent();
    }

    protected void lock(URNVersionPair lockInfo) throws LBInvocationException, LBParameterException {
        if (lockInfo != null && lockInfo.getUrn() != null && lockInfo.getVersion() != null) {
            WriteLockManager.instance().acquireLock(lockInfo.getUrn(), lockInfo.getVersion());
            WriteLockManager.instance().checkForRegistryUpdates();
        }
    }

    protected void unlock(URNVersionPair lockInfo) throws LBInvocationException, LBParameterException {
        if (lockInfo != null && lockInfo.getUrn() != null && lockInfo.getVersion() != null) {
            WriteLockManager.instance().releaseLock(lockInfo.getUrn(), lockInfo.getVersion());
        }
    }

    private void buildIndex(AbsoluteCodingSchemeVersionReference[] references) throws Exception {
       EntityIndexService service = LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
                getEntityIndexService();
       
       for(AbsoluteCodingSchemeVersionReference reference : references) {
           service.createIndex(reference);
       } 
    }

    public String getStringFromURI(URI uri) throws LBParameterException {
        if ("file".equals(uri.getScheme()))

        {
            File temp = new File(uri);
            return temp.getAbsolutePath();
        } else {
            inUse = false;
            throw new LBParameterException("Currently only supports file based URI's", "uri", uri.toString());
        }

    }

    /**
     * Set the CodingSchemeManifest that would be used to modify the ontology
     * content. Once the ontology is loaded from the source, the manifest would
     * then be applied to modify the loaded content.
     * 
     * @param csm
     */
    public void setCodingSchemeManifest(CodingSchemeManifest codingSchemeManifest) {
        codingSchemeManifest_ = codingSchemeManifest;
    }

    /**
     * Get the CodingSchemeManifest that would be used to modify the ontology
     * content. Once the ontology is loaded from the source, the manifest would
     * then be applied to modify the loaded content.
     * 
     * @param csm
     */
    public CodingSchemeManifest getCodingSchemeManifest() {
        return codingSchemeManifest_;
    }

    /**
     * Set the URI of the codingSchemeManifest that would be used to modify the
     * ontology content. The codingSchemeManifest object is set using the
     * content pointed by the URI. Once the ontology is loaded from the source,
     * the manifest would then be applied to modify the loaded content.
     * 
     * @param csm
     */
    public void setCodingSchemeManifestURI(URI codingSchemeManifestURI) {
        this.codingSchemeManifestURI_ = codingSchemeManifestURI;
        ManifestUtil util = new ManifestUtil(codingSchemeManifestURI, md_);

        codingSchemeManifest_ = util.getManifest();

    }

    /**
     * Get the URI of the codingSchemeManifest that would be used to modify the
     * ontology content. Once the ontology is loaded from the source, the
     * manifest would then be applied to modify the loaded content.
     * 
     * @param csm
     */
    public URI getCodingSchemeManifestURI() {
        return codingSchemeManifestURI_;
    }

    /**
     * Returns the current LoaderPreferences object.
     * 
     * @return The current LoaderPreferences
     */
    public LoaderPreferences getLoaderPreferences() {
        return loaderPreferences_;
    }

    /**
     * Sets the Loader's LoaderPreferences.
     * 
     * @param loaderPreferences
     *            The LoaderPreference object to be loaded. It is recommended
     *            that all subclasses override and check if the
     *            LoaderPreferences object is valid for the particular loader.
     * @throws LBParameterException
     */
    public void setLoaderPreferences(LoaderPreferences loaderPreferences) throws LBParameterException {
        loaderPreferences_ = loaderPreferences;
    }

    /**
     * Sets the Loader's LoaderPreferences.
     * 
     * @param loaderPreferences
     *            The LoaderPreference XML URI to be loaded.
     * @throws LBParameterException
     */
    public void setLoaderPreferences(URI loaderPreferences) throws LBParameterException {
        try {
            PreferenceLoader prefLoader = PreferenceLoaderFactory.createPreferenceLoader(loaderPreferences);
            if (!prefLoader.validate()) {
                throw new LBParameterException("Error",
                        "Preferences File is not a valid LoaderPreference file for the Source Format you are trying to load.");
            } else {
                loaderPreferences_ = prefLoader.load();
            }
        } catch (LgConvertException e) {
            throw new LBParameterException("Could not create Preference Loader from the URI specified.");
        }

    }
    
    protected abstract URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException;

    public void load(URI resource){
        this.resourceUri = resource;
        try {
            baseLoad(true);
        } catch (LBInvocationException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected abstract OptionHolder declareAllowedOptions(OptionHolder holder);
    
    public void register() throws LBParameterException, LBException {
        
        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerLoadExtension(
                super.getExtensionDescription());
    }
    
    protected LoaderPostProcessor getPostProcessor(String postProcessorName) throws LBParameterException {
        try {
            return LexBIGServiceImpl.defaultInstance().getServiceManager(null).
                getExtensionRegistry().
                getGenericExtension(postProcessorName, LoaderPostProcessor.class);
        } catch (LBInvocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBooleanOptionValue(String optionName, Boolean value){
        this.getOptions().getBooleanOption(optionName).setOptionValue(value);
    }
    

    public AbsoluteCodingSchemeVersionReference[] getCodingSchemeReferences() {
        return codingSchemeReferences;
    }

    public OptionHolder getOptions() {
        return options_;
    }

    public void setOptions(OptionHolder options) {
        options_ = options;
    }

    public URI getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(URI resourceUri) {
        this.resourceUri = resourceUri;
    }

    public boolean isDoIndexing() {
        return doIndexing;
    }

    public void setDoIndexing(boolean doIndexing) {
        this.doIndexing = doIndexing;
    }

    public boolean isDoComputeTransitiveClosure() {
        return doComputeTransitiveClosure;
    }

    public void setDoComputeTransitiveClosure(boolean doComputeTransitiveClosure) {
        this.doComputeTransitiveClosure = doComputeTransitiveClosure;
    }

    public boolean isDoRegister() {
        return doRegister;
    }

    public void setDoRegister(boolean doRegister) {
        this.doRegister = doRegister;
    }
    
    public CachingMessageDirectorIF getMessageDirector() {
        return md_;
    }
}