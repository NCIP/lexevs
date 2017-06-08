/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.BaseOption;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.ApproxNumOfConceptsPostProcessor;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.HierarchyCheckingPostProcessor;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.OntologyFormatAddingPostProcessor;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.SupportedAttributePostProcessor;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.RootOrTail;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.registry.WriteLockManager;
import org.lexevs.system.service.SystemResourceService;
import org.lexevs.system.utility.MyClassLoader;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.inserter.CodingSchemeInserter;
import edu.mayo.informatics.lexgrid.convert.inserter.DefaultPagingCodingSchemeInserter;
import edu.mayo.informatics.lexgrid.convert.inserter.PreValidatingInserterDecorator;
import edu.mayo.informatics.lexgrid.convert.inserter.resolution.EntityBatchInsertResolver;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.options.CodingSchemeReferencesStringArrayPickListOption;
import edu.mayo.informatics.lexgrid.convert.options.DefaultOptionHolder;
import edu.mayo.informatics.lexgrid.convert.options.StringArrayOption;
import edu.mayo.informatics.lexgrid.convert.options.StringOption;
import edu.mayo.informatics.lexgrid.convert.options.URIOption;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.PreferenceLoaderFactory;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.interfaces.PreferenceLoader;
import edu.mayo.informatics.lexgrid.convert.validator.NullNamespaceValidator;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.processor.DefaultResolverProcessor;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ReflectionValidationProcessor;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ResolverProcessor;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.NullNamespaceResolver;

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
    private static final long serialVersionUID = -1360861293147077920L;

    private ManifestUtil manifestUtil = new ManifestUtil();
    
    public static String LOADER_POST_PROCESSOR_OPTION = "Loader Post Processor (Extension Name)";
    public static String SUPPLEMENT_OPTION = "To Extend";
    public static String MANIFEST_FILE_OPTION = "Manifest File";
    public static String LOADER_PREFERENCE_FILE_OPTION = "Loader Preferences File";
    public static String ASYNC_OPTION = "Async Load";
    public static String FAIL_ON_ERROR_OPTION = 
        edu.mayo.informatics.lexgrid.convert.formats.Option.getNameForType(edu.mayo.informatics.lexgrid.convert.formats.Option.FAIL_ON_ERROR);
    
    private AbsoluteCodingSchemeVersionReference[] codingSchemeReferences
        = new AbsoluteCodingSchemeVersionReference[0];

    protected boolean inUse = false;
    private CachingMessageDirectorIF md_;
    public CachingMessageDirectorIF getMd_() {
        return md_;
    }

    public void setMd_(CachingMessageDirectorIF md_) {
        this.md_ = md_;
    }

    private LoadStatus status_;
    
    private OptionHolder options_;
    private CodingSchemeManifest codingSchemeManifest_;
    private LoaderPreferences loaderPreferences_;
    
    private URI codingSchemeManifestURI_;
    private URI resourceUri;
    
    private boolean doIndexing = true;
    private boolean doComputeTransitiveClosure = true;
    private boolean doRegister = true;
    private boolean doApplyPostLoadManifest = true;
    private boolean doRemoveOnFailure = true;
    
    private boolean hasSupplementOptionsBeenInitialized = false;
    
 
    public BaseLoader(){
        OptionHolder holder = new DefaultOptionHolder();
        URIOption manifiestOption = new URIOption(MANIFEST_FILE_OPTION);
        manifiestOption.addAllowedFileExtensions("*.xml");
        manifiestOption.setHelpText("A Manifest enables the user to control various\n" +
        		                    "Coding Scheme metadata characteristics, such as\n" +
                                    "adding a Supported Attribute, modifying an\n" +
                                    "Association Name, etc.");
        
        holder.getURIOptions().add(manifiestOption);
        
        URIOption loaderPreferencesOption = new URIOption(LOADER_PREFERENCE_FILE_OPTION);
        loaderPreferencesOption.addAllowedFileExtensions("*.xml");
        loaderPreferencesOption.setHelpText("Loader Preferences are loader-specific configuration\n" +
                                    "files that may be used to alter the behavior of a\n" +
                                    "specific loader.");
        
        holder.getURIOptions().add(loaderPreferencesOption);
        
        BooleanOption asyncOption = new BooleanOption(ASYNC_OPTION, true);
        asyncOption.setHelpText("An ASYNC load will execute in a background thread.\n" +
                                "A non-ASYNC load will execute in the main thread,\n" + 
                                "blocking all other operations until completion.");
        holder.getBooleanOptions().add(asyncOption);
        
        
        BooleanOption failOnErrorOption = new BooleanOption(FAIL_ON_ERROR_OPTION, false);
        holder.getBooleanOptions().add(failOnErrorOption);
        failOnErrorOption.setHelpText(
                edu.mayo.informatics.lexgrid.convert.formats.Option.getDescriptionForType(
                        edu.mayo.informatics.lexgrid.convert.formats.Option.FAIL_ON_ERROR));
        
        this.options_ = holder;
    }
    
    /**
     * Inits the supplement options. These need to be Lazy-initialized because
     * they may interfere with LexEVS startup of Extensions.
     */
    protected void initLazyInitializedOptions() {

        if(!hasSupplementOptionsBeenInitialized) {
            StringOption supplementOption;
            try {
                supplementOption = new CodingSchemeReferencesStringArrayPickListOption(
                        SUPPLEMENT_OPTION, 
                        LexBIGServiceImpl.defaultInstance().getSupportedCodingSchemes());
            } catch (LBInvocationException e) {
                throw new RuntimeException(e);
            }
            
            supplementOption.setHelpText("A Coding Scheme may 'Supplement' another Coding Scheme,\n" + 
                                         "meaing the Coding Scheme will become an extension of its parent.\n" +
                                         "All content of the Parent will be available through the supplement.");
            
            options_.getStringOptions().add(supplementOption);
            
            StringArrayOption loaderPostProcessorOption = new StringArrayOption(LOADER_POST_PROCESSOR_OPTION);

            //TODO: Do we want to enable these by default?
            loaderPostProcessorOption.getOptionValue().add(ApproxNumOfConceptsPostProcessor.EXTENSION_NAME);
            loaderPostProcessorOption.getOptionValue().add(OntologyFormatAddingPostProcessor.EXTENSION_NAME);
            loaderPostProcessorOption.getOptionValue().add(SupportedAttributePostProcessor.EXTENSION_NAME);
            loaderPostProcessorOption.getOptionValue().add(HierarchyCheckingPostProcessor.EXTENSION_NAME);
            loaderPostProcessorOption.getPickList().addAll(this.getPostProcessorExtensionNames());
            
            loaderPostProcessorOption.setHelpText("User defined 'Post Processor' options to be run after\n" +
                                                  "the completion of the load. Please see the individual\n" +
                                                  "Post Processor documenation for details on each.");
            
            options_.getStringArrayOptions().add(loaderPostProcessorOption);
 
            this.options_= this.declareAllowedOptions(options_);
            
            hasSupplementOptionsBeenInitialized = true;
        }
    }
    
    protected void baseLoad(boolean async) throws LBInvocationException {
       status_ = new LoadStatus();

        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));
        md_ = createCachingMessageDirectorIF();

        if (async) {
            Thread conversion = new Thread(new DoConversion());
            conversion.start();
        } else {
            new DoConversion().run();
        }
    }
    
    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerLoadExtension(description);
    }
    
    protected CachingMessageDirectorIF createCachingMessageDirectorIF() {
    	return new CachingMessageDirectorImpl( new MessageDirector(getName(), status_));
    }


    public boolean isInUse() {
        return inUse;
    }
  
    public void setManifestUtil(ManifestUtil manifestUtil) {
        this.manifestUtil = manifestUtil;
    }

    public void setCodingSchemeReferences(AbsoluteCodingSchemeVersionReference[] codingSchemeReferences) {
        this.codingSchemeReferences = codingSchemeReferences;
    }

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
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

    protected class DoConversion implements Runnable {

        public void run() {
            URNVersionPair[] locks = null;
            try {          
                
                // Actually do the load
                URNVersionPair[] loadedCodingSchemes = doLoad();
                if(loadedCodingSchemes == null || loadedCodingSchemes.length == 0) {
                    return;
                }
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

                    //Pre-register to make this available before indexing.
                    if(doRegister) {
                        register(loadedCodingSchemes);
                    }
                    
                    try {
                        doPostProcessing(options_, codingSchemeReferences);
                    } catch (Exception e) {
                        getLogger().warn("There was an error running the Post Processing Operations."+
                                " Please see the log for more information. The load process will continue.", 
                                e);
                    }
                    
                    try {
                        doTransitiveAndIndex(codingSchemeReferences);
                    } catch (Exception e) {
                        getLogger().warn("There was an error running Computing the Transitivity Table or during Lucene Indexing."+
                                " Please see the log for more information. The load process will continue.", 
                                e);
                    }
                    
                    try {
                        if(doApplyPostLoadManifest) {
                            doApplyManifest(codingSchemeReferences);
                        }
                    } catch (Exception e) {
                        getLogger().warn("There was an error applying the Post Load Manifest."+
                                " Please see the log for more information. The load process will continue.", 
                                e);
                    }

                    md_.info("After Indexing");
                    snap = SimpleMemUsageReporter.snapshot();
                    md_.info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                    status_.setState(ProcessState.COMPLETED);
                    md_.info("Load process completed without error");
                    
                    //Register again (to set as INACTIVE)
                    if(doRegister) {
                        register(loadedCodingSchemes);
                    }
                    
                    if(doesOptionExist(getOptions().getStringOptions(), SUPPLEMENT_OPTION)) {
                        String parentSupplementString = getOptions().getStringOption(SUPPLEMENT_OPTION).getOptionValue();
                        if(StringUtils.isNotBlank(parentSupplementString)) {
                            try {
                                AbsoluteCodingSchemeVersionReference parent = 
                                    getAbsoluteCodingSchemeVersionReferenceFromOptionString(parentSupplementString);

                                md_.info("Registering as a supplement to URI: " + 
                                        parent.getCodingSchemeURN() + ", Version: " + parent.getCodingSchemeVersion());

                                for(AbsoluteCodingSchemeVersionReference supplement : getCodingSchemeReferences()) {
                                    LexEvsServiceLocator.getInstance().getSystemResourceService().
                                    registerCodingSchemeSupplement(parent, supplement);
                                }
                            } catch (Exception e) {
                                md_.warn("Error registring supplements, none will be registered.", e);
                            }
                        } else {
                            md_.info("Not registering as a supplement.");
                        }
                    }
                }
            } catch (CodingSchemeAlreadyLoadedException e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal(e.getMessage());
            } catch (Exception e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal("Failed while running the conversion", e);
                getLogger().warn("Load failed: " + (e.getCause() != null?e.getCause():e.getMessage()));
            } finally {
                if (status_.getState() == null || 
                        (
                                !status_.getState().equals(ProcessState.COMPLETED)
                                        &&
                                !status_.getState().equals(ProcessState.PENDING)
                        
                        )) {
                    status_.setState(ProcessState.FAILED);

                    try {
                        if (locks != null) {
                            for (int i = 0; i < locks.length; i++) {
                                unlock(locks[i]);
                            }
                        }

                        if(doRemoveOnFailure && codingSchemeReferences.length >0) {
                            getLogger().warn("Load failed.  Removing temporary resources...");
                            SystemResourceService service = 
                                   LexEvsServiceLocator.getInstance().getSystemResourceService();
                            
                            for(AbsoluteCodingSchemeVersionReference ref : codingSchemeReferences) {
                                service.removeCodingSchemeResourceFromSystem(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
                            }
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
    
    protected AbsoluteCodingSchemeVersionReference getAbsoluteCodingSchemeVersionReferenceFromOptionString(String optionString) throws LBParameterException, LBInvocationException {
        return CodingSchemeReferencesStringArrayPickListOption.getAbsoluteCodingSchemeVersionReference(
                optionString,
                LexBIGServiceImpl.defaultInstance().getSupportedCodingSchemes()
                );
    }
    
    private void doApplyManifest(AbsoluteCodingSchemeVersionReference[] codingSchemeReferences) throws LgConvertException {
        for(AbsoluteCodingSchemeVersionReference reference : codingSchemeReferences) {
            md_.info("Applying Post-Load Manifest (if any).");
            this.getManifestUtil().applyManifest(
                    this.getCodingSchemeManifest(), 
                    new URNVersionPair(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion()));
        }
    }
    
    protected void doPostProcessing(OptionHolder options, AbsoluteCodingSchemeVersionReference[] references) throws LBParameterException {
        if(doesOptionExist(options.getStringArrayOptions(), LOADER_POST_PROCESSOR_OPTION)) {
            List<String> postProcessors =
                options.getStringArrayOption(LOADER_POST_PROCESSOR_OPTION).getOptionValue();

            if (postProcessors != null)
            {
                for(String postProcessor : postProcessors) {
                    md_.info("Running PostProcessor:" + postProcessor);

                    for(AbsoluteCodingSchemeVersionReference ref : references) {
                        getPostProcessor(postProcessor).runPostProcess(ref, getOntologyFormat());
                    }
                }
            }
        }
    }

    protected void doTransitiveAndIndex(AbsoluteCodingSchemeVersionReference[] references) throws Exception {
        if(doComputeTransitiveClosure) {
            try {
                doTransitiveTable(references);
            } catch (Exception e) {
                getLogger().warn("There was an error running Computing the Transitivity Table."+
                        " Please see the log for more information. The load process will continue.", 
                        e);
            }
        }
        if(doIndexing) {
            try {
                doIndex(references);
            } catch (Exception e) {
                getLogger().warn("There was an error during Lucene Indexing."+
                        " Please see the log for more information. The load process will continue." +
                        " A Re-index may be necessary.", 
                        e);
            }
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
    protected void buildRootNode(
            AbsoluteCodingSchemeVersionReference reference,
            List<String> associationNames,
            String relationContainerName,
            RootOrTail rootOrTail,
            TraverseAssociations traverse)
            throws Exception {
        md_.info("Building the root node");

        LexEvsServiceLocator.getInstance().
        getLexEvsDatabaseOperations().addRootRelationNode(
                reference.getCodingSchemeURN(), 
                reference.getCodingSchemeVersion(), 
                associationNames, 
                relationContainerName, 
                rootOrTail,
                traverse);

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

    protected void doTransitiveTable(AbsoluteCodingSchemeVersionReference[] references) throws Exception {

        for(AbsoluteCodingSchemeVersionReference reference : references) {
            Snapshot snap1 = SimpleMemUsageReporter.snapshot();
            md_.info("Loading transitive expansion table");

            LexEvsServiceLocator.getInstance().
                getLexEvsDatabaseOperations().computeTransitiveTable(
                    reference.getCodingSchemeURN(), 
                    reference.getCodingSchemeVersion());

            Snapshot snap2 = SimpleMemUsageReporter.snapshot();
            md_.info("Finished building transitive expansion.   Time taken: "
                    + SimpleMemUsageReporter.formatTimeDiff(snap2.getTimeDelta(snap1)) + "   Heap usage: "
                    + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsage()) + "   Heap delta: "
                    + SimpleMemUsageReporter.formatMemStat(snap2.getHeapUsageDelta(snap1)));
        }
    }

    protected void register(URNVersionPair[] loadedCodingSchemes) throws Exception {
         SystemResourceService service = LexEvsServiceLocator.getInstance().getSystemResourceService();
         for(URNVersionPair pair : loadedCodingSchemes ){
            if(service.containsCodingSchemeResource(pair.getUrn(), pair.getVersion())) {
                service.updateCodingSchemeResourceStatus(Constructors.
                        createAbsoluteCodingSchemeVersionReference(pair.getUrn(), pair.getVersion()),
                        CodingSchemeVersionStatus.INACTIVE);
            } else {
                service.addCodingSchemeResourceToSystem(pair.getUrn(), pair.getVersion());
            }
         }
    }

   
    
    protected void persistCodingSchemeToDatabase(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        persistCodingSchemeToDatabase(createDefaultInserter(codingScheme), codingScheme);
    }
    
    protected void persistCodingSchemeToDatabase(CodingSchemeInserter inserter, CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        if(this.getCodingSchemeManifest() != null) {
            md_.info("Applying Pre-Load Manifest.");
            this.setDoApplyPostLoadManifest(false);
            this.getManifestUtil().applyManifest(this.getCodingSchemeManifest(), codingScheme);
        }

        List<ResolvedLoadValidationError> errors = inserter.insertCodingScheme(codingScheme);
        
        if(errors.size() > 0) {
            this.getStatus().setWarningsLogged(true);
        }
       
        for(ResolvedLoadValidationError error : errors) {
            this.getMessageDirector().info(error.toString());
        }
    }

    protected CodingSchemeInserter createDefaultInserter() {
     
        DefaultPagingCodingSchemeInserter defaultInserter = 
            new DefaultPagingCodingSchemeInserter();
         
        ResolverProcessor resolverProcessor = new DefaultResolverProcessor();
        resolverProcessor.addResolver(new EntityBatchInsertResolver());
        
        defaultInserter.setResolverProcessor(resolverProcessor);
        
        return defaultInserter;
    }
    
    protected CodingSchemeInserter createDefaultInserter(CodingScheme codingScheme) {

        PreValidatingInserterDecorator decorator = 
            new 
            PreValidatingInserterDecorator(
                       createDefaultInserter());
        
        ResolverProcessor resolverProcessor = new DefaultResolverProcessor();
        resolverProcessor.addResolver(new NullNamespaceResolver(codingScheme));
        decorator.setResolverProcessor(resolverProcessor);
        
        ReflectionValidationProcessor<CodingScheme> validationProcessor = new ReflectionValidationProcessor<CodingScheme>();
        validationProcessor.addValidator(new NullNamespaceValidator());
        decorator.setValidationProcessor(validationProcessor);
        
        return decorator;
    }

    public LoadStatus getStatus() {
        return status_;
    }
    
    public void setStatus(LoadStatus status){
        status_ = status;
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
        if(codingSchemeManifestURI == null) {return;}
        this.codingSchemeManifestURI_ = codingSchemeManifestURI;
        
        codingSchemeManifest_ = manifestUtil.getManifest(codingSchemeManifestURI);
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
        if(loaderPreferences == null) {return;}
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
    
    protected abstract URNVersionPair[] doLoad() throws Exception;

    public void load(URI resource){
        //incase these haven't been initialized already.
        initLazyInitializedOptions();
        
        this.resourceUri = resource;
        try {
            boolean async = this.getOptions().getBooleanOption(ASYNC_OPTION).getOptionValue();
            if(doesOptionExist(this.getOptions().getURIOptions(), MANIFEST_FILE_OPTION)){
                this.setCodingSchemeManifestURI(this.getOptions().getURIOption(MANIFEST_FILE_OPTION).getOptionValue());
            } 
            if(doesOptionExist(this.getOptions().getURIOptions(), LOADER_PREFERENCE_FILE_OPTION)){
                this.setLoaderPreferences(this.getOptions().getURIOption(LOADER_PREFERENCE_FILE_OPTION).getOptionValue());
            }
            baseLoad(async);
        } catch (LBException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected boolean doesOptionExist(List<? extends BaseOption<?>> options, String optionName) {
        for(BaseOption<?> option : options) {
            if(option.getOptionName().equals(optionName)) {
                return true;
            }
        }
        return false;
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
    
    private List<String> getPostProcessorExtensionNames(){
        List<String> returnList = new ArrayList<String>();
        for(ExtensionDescription extension : ExtensionRegistryImpl.instance().getGenericExtensions().getExtensionDescription()) {
            try {
                if(ClassUtils.isAssignable(Class.forName(
                        extension.getExtensionClass(), false, MyClassLoader.instance()), LoaderPostProcessor.class)){
                    returnList.add(extension.getName());
                }
            } catch (ClassNotFoundException e) {
               //don't show invalid extensions
            }
        }
        return returnList;
    }
    
    protected URNVersionPair[] constructVersionPairsFromCodingSchemes(Object... loadedObject) {
        URNVersionPair[] pairs = new URNVersionPair[loadedObject.length];
        
        for(int i=0;i<loadedObject.length;i++) {
            String uri = null;
            String version = null;
            if (loadedObject[i] instanceof CodingScheme)
            {
                CodingScheme cs = (CodingScheme) loadedObject[i];
                uri = cs.getCodingSchemeURI();
                version = cs.getRepresentsVersion();
            }
            else if (loadedObject[i] instanceof ValueSetDefinition)
            {
                ValueSetDefinition vsd = (ValueSetDefinition) loadedObject[i];
                uri = vsd.getValueSetDefinitionURI();
                if (vsd.getEntryState() != null)
                    version = vsd.getEntryState().getContainingRevision();
            }
            else if (loadedObject[i] instanceof PickListDefinition)
            {
                PickListDefinition pld = (PickListDefinition) loadedObject[i];
                uri = pld.getPickListId();
                if (pld.getEntryState() != null)
                    version = pld.getEntryState().getContainingRevision();
            }
            
            pairs[i] = new URNVersionPair(uri, version);
        }
        
        return pairs;
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
        initLazyInitializedOptions();
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
    
    public boolean isDoApplyPostLoadManifest() {
        return doApplyPostLoadManifest;
    }

    public void setDoApplyPostLoadManifest(boolean doApplyPostLoadManifest) {
        this.doApplyPostLoadManifest = doApplyPostLoadManifest;
    }

    public CachingMessageDirectorIF getMessageDirector() {
        return md_;
    }

    protected void setCachingMessageDirectorIF(CachingMessageDirectorIF md) {
        md_ = md;
    }

    public ManifestUtil getManifestUtil() {
        return this.manifestUtil;
    }

    public void setDoRemoveOnFailure(boolean doRemoveOnFailure) {
        this.doRemoveOnFailure = doRemoveOnFailure;
    }

    public boolean isDoRemoveOnFailure() {
        return doRemoveOnFailure;
    }
    
    public OntologyFormat getOntologyFormat(){
        return null;
    }
}