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
package org.LexGrid.LexBIG.Impl;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.SortDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Extensions.Load.RxNormBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.UmlsBatchLoader;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.LexBIGServiceConvenienceMethodsImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.MappingExtensionImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search.SearchExtensionImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.supplement.SupplementExtensionImpl;
import org.LexGrid.LexBIG.Impl.Extensions.Search.ContainsSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.DoubleMetaphoneSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.ExactMatchSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.LeadingAndTrailingWildcardSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.LiteralContainsSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.LiteralSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.LiteralSubStringSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.LuceneSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.NonLeadingWildcardLiteralSubStringSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.PhraseSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.RegExpSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.SpellingErrorTolerantSubStringSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.StartsWithSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.StemmedSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.SubStringSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Search.WeightedDoubleMetaphoneSearch;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.CodePostSort;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.CodeSort;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.CodeSystemSort;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.ConceptStatusSort;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.EntityDescriptionSort;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.MatchToQuerySort;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.NumberOfChildrenSort;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.isActiveSort;
import org.LexGrid.LexBIG.Impl.History.HistoryServiceFactory;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.Impl.exporters.OBOExport;
import org.LexGrid.LexBIG.Impl.exporters.OwlRdfExporterImpl;
import org.LexGrid.LexBIG.Impl.loaders.ClaMLLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.HL7LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MIFVocabularyLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MedDRALoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MetaDataLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MrmapRRFLoader;
import org.LexGrid.LexBIG.Impl.loaders.NCIHistoryLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OBOLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OWL2LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.RadLexProtegeFramesLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.SemNetLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.TextLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.UMLSHistoryLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.ApproxNumOfConceptsPostProcessor;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.HierarchyCheckingPostProcessor;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.OntologyFormatAddingPostProcessor;
import org.LexGrid.LexBIG.Impl.loaders.postprocessor.SupportedAttributePostProcessor;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.event.SystemEventListener;

/**
 * Implementation of the LexBIGService Interface.
 * 
 * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexBIGServiceImpl implements LexBIGService {
    private static LexBIGServiceImpl lexbigService_ = null;
    private static final long serialVersionUID = 6785772690490820208L;
    private DatabaseServiceManager databaseServiceManager = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
    private Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
    private CodedNodeGraphFactory codedNodeGraphFactory = new CodedNodeGraphFactory();
    private CodedNodeSetFactory codedNodeSetFactory = new CodedNodeSetFactory();
    private HistoryServiceFactory historyServiceFactory = new HistoryServiceFactory();

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * Returns a default singleton instance of the service.
     * <p>
     * Note: This is the recommended method of acquiring the service, since it
     * will allow the application to run without change in distributed LexBIG
     * environments (in which case the default instance is actually a
     * distributed service). However, use of the public constructor is supported
     * to preserve backward compatibility.
     * 
     * @return LexBIGServiceImpl
     */
    public static LexBIGServiceImpl defaultInstance() {
        if (lexbigService_ == null)
            try {
                lexbigService_ = new LexBIGServiceImpl();
            } catch (LBException e) {
                LoggerFactory.getLogger().error("Error initializing service", e);
            }
        return lexbigService_;
    }

    /**
     * Assigns the default singleton instance of the service.
     * <p>
     * Note: While this method is public, it is generally not intended to be
     * part of the externalized API. It is made public so that the runtime
     * system has the ability to assign the default instance when running in
     * distributed LexBIG environments, etc.
     * 
     * @param LexBIGServiceImpl
     *            the default instance.
     */
    public static void setDefaultInstance(LexBIGServiceImpl defaultInstance) {
        lexbigService_ = defaultInstance;
    }

    /**
     * IMPORTANT!!! - This constructor may only be called *ONCE* per JVM. If using this, make sure
     * it is instantiated via a singleton factory or other mechanism to prevent multiple instantiations.
     * 
     * This constructor will be made private in a future release. Please change client code to use:
     * {@link #defaultInstance()}, as this will guarantee only one instantiation.
     * 
     * @throws LBInvocationException
     * @throws Exception
     */
    @Deprecated
    public LexBIGServiceImpl() throws LBException {
        LexEvsServiceLocator r = LexEvsServiceLocator.getInstance();

        registerExtensions();
        
        //new LuceneWarmUpThread().start();
        if (r == null) {
            System.err.println("Initialization Failure.  Beginning debug print:");
            System.out.println("Initialization Failure.  Beginning debug print:");
            throw new LBInvocationException(
                    "There was a problem starting up.  Please see the log files and / or system.out", "");

        }
    }

    /**
     * @throws LBInvocationException
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getSupportedCodingSchemes()
     */
    public CodingSchemeRenderingList getSupportedCodingSchemes() throws LBInvocationException {
        getLogger().logMethod();
        try {
            CodingSchemeRenderingList temp = new CodingSchemeRenderingList();
            
            //see if we can get by without the refresh here....
            //systemResourceService.refresh();

            List<RegistryEntry> entries = registry.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
            
            for(RegistryEntry entry : entries){
                CodingSchemeRendering rendering = new CodingSchemeRendering();
                
                rendering.setCodingSchemeSummary(
                        this.databaseServiceManager.getCodingSchemeService().
                            getCodingSchemeSummaryByUriAndVersion(
                                    entry.getResourceUri(), 
                                    entry.getResourceVersion()));
                

                RenderingDetail detail = new RenderingDetail();
                detail.setDeactivateDate(entry.getDeactivationDate());
                detail.setLastUpdateTime(entry.getLastUpdateDate());
                
                String tag = entry.getStatus();
                if(StringUtils.isNotBlank(tag)){
                    detail.setVersionStatus(CodingSchemeVersionStatus.valueOf(tag.toUpperCase()));
                }

                CodingSchemeTagList tagList = new CodingSchemeTagList();
                if(StringUtils.isNotBlank(entry.getTag())){
                    tagList.addTag(entry.getTag());
                }
                detail.setVersionTags(tagList);
                
                rendering.setRenderingDetail(detail);
                
                temp.addCodingSchemeRendering(rendering);  
            }

            return temp;
        } catch (Exception e) {
            String id = getLogger().error("There was an unexpected error", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

    /**
     * 
     * 
     * @throws LBInvocationException
     * @throws LBParameterException
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#resolveCodingScheme(String,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    public CodingScheme resolveCodingScheme(String codingSchemeName, CodingSchemeVersionOrTag tagOrVersion)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codingSchemeName, tagOrVersion });
        
        AbsoluteCodingSchemeVersionReference ref = 
            ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingSchemeName, tagOrVersion, true);

        CodingScheme codingScheme;
        try {
            codingScheme = databaseServiceManager.getCodingSchemeService().
                getCodingSchemeByUriAndVersion(
                        ref.getCodingSchemeURN(),
                        ref.getCodingSchemeVersion());
        } catch (Exception e) {
            String id = getLogger().error("There was an unexpected error", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
        

        RegistryEntry entry = registry.getCodingSchemeEntry(ref);
        
        if(StringUtils.isNotBlank(entry.getSupplementsUri())
                &&
                StringUtils.isNotBlank(entry.getSupplementsVersion())){
           
            CodingScheme supplementCodingScheme = databaseServiceManager.getCodingSchemeService().
                getCodingSchemeByUriAndVersion(
                        entry.getSupplementsUri(),
                        entry.getSupplementsVersion());
            
            Mappings mergedMappings = 
                DaoUtility.mergeURIMappings(codingScheme.getMappings(), supplementCodingScheme.getMappings());
            
            codingScheme.setMappings(mergedMappings);
        }
        
        return codingScheme;
    }

    /**
     * 
     * 
     * @throws LBInvocationException
     * @throws LBParameterException
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#resolveCodingScheme(String,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    public String resolveCodingSchemeCopyright(String codingSchemeName, CodingSchemeVersionOrTag tagOrVersion)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codingSchemeName, tagOrVersion });

        return this.resolveCodingScheme(codingSchemeName, tagOrVersion).getCopyright().getContent();
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getCodingSchemeConcepts(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeURNorName,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, boolean)
     * @deprecated
     */
    @Deprecated
    public CodedNodeSet getCodingSchemeConcepts(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            boolean activeOnly) throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag, new Boolean(activeOnly) });
        CodedNodeSet cns = codedNodeSetFactory.getCodedNodeSet(codingScheme, versionOrTag, new Boolean(activeOnly), Constructors.createLocalNameList("concept"));
        cns.restrictToAnonymous(AnonymousOption.NON_ANONYMOUS_ONLY);
        return cns;
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getCodingSchemeConcepts(java.lang.String,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    public CodedNodeSet getCodingSchemeConcepts(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag });
        CodedNodeSet cns = codedNodeSetFactory.getCodedNodeSet(
                codingScheme, 
                versionOrTag, 
                null,
                Constructors.createLocalNameList("concept"));
        cns.restrictToAnonymous(AnonymousOption.NON_ANONYMOUS_ONLY);
        return cns;
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getNodeSet(java.lang.String,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList)
     */
    public CodedNodeSet getNodeSet(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            LocalNameList entityTypes) throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag, entityTypes });
        CodedNodeSet cns = codedNodeSetFactory.getCodedNodeSet(codingScheme, versionOrTag, null, entityTypes);
        
        cns.restrictToAnonymous(AnonymousOption.NON_ANONYMOUS_ONLY);
        return cns;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getNodeGraph(String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, String)
     */
    public CodedNodeGraph getNodeGraph(String codingScheme, CodingSchemeVersionOrTag tagOrVersion, String relationContainerName)
            throws LBParameterException, LBInvocationException, LBResourceUnavailableException {
        getLogger().logMethod(new Object[] { codingScheme, tagOrVersion, relationContainerName });
   
        return codedNodeGraphFactory.getCodedNodeGraph(codingScheme, tagOrVersion, relationContainerName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getLastUpdateTime()
     */
    public Date getLastUpdateTime() {
        getLogger().logMethod();
        return registry.getLastUpdateTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGService#getServiceManager(Object)
     */
    @LgClientSideSafe
    public LexBIGServiceManager getServiceManager(Object credentials) throws LBParameterException,
            LBInvocationException {
        getLogger().logMethod(new Object[] { credentials });
        try {
            //TODO: Implement some sort of Security
            // Also, inject the LexBIGServiceManager here, with Spring or ServiceLocator or otherwise.
            return new LexBIGServiceManagerImpl();
        } catch (Exception e) {
            String id = getLogger().error("There was an unexpected error", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGService#getHistoryService(String)
     */
    @LgClientSideSafe
    public HistoryService getHistoryService(String codingScheme) throws LBException {
        getLogger().logMethod(new Object[] { codingScheme });

        return this.historyServiceFactory.getHistoryService(codingScheme);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getMatchAlgorithms()
     */
    @LgClientSideSafe
    public ModuleDescriptionList getMatchAlgorithms() {
        ModuleDescriptionList mdl = new ModuleDescriptionList();
        
        ExtensionDescriptionList edl = 
            ExtensionRegistryImpl.instance().getSearchExtensions();
        
        for(ExtensionDescription ed : edl.getExtensionDescription()){
            mdl.addModuleDescription(ed);
        }
        
        return mdl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGService#getSortAlgorithms(SortContext
     * )
     */
    @LgClientSideSafe
    public SortDescriptionList getSortAlgorithms(SortContext context) {
        getLogger().logMethod(new Object[] { context });
        
        SortDescriptionList result = new SortDescriptionList();

        // Get the sort extensions.

        SortDescriptionList sdl = ExtensionRegistryImpl.instance().getSortExtensions();
        for (int i = 0; i < sdl.getSortDescriptionCount(); i++) {
            SortDescription cur = sdl.getSortDescription(i);
            if (context == null) {
                result.addSortDescription(cur);
            } else {
                SortContext[] temp = cur.getRestrictToContext();
                for (int j = 0; j < temp.length; j++) {
                    if (temp[j].equals(context)) {
                        result.addSortDescription(cur);
                        break;
                    }
                }
            }
        }

        return result;
    }

    @LgClientSideSafe
    public GenericExtension getGenericExtension(String name) throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] { name });
        try {
            ClassLoader temp = LexEvsServiceLocator.getInstance().getSystemResourceService().getClassLoader();
            ExtensionDescription ed = ExtensionRegistryImpl.instance().getGenericExtension(name);

            if (ed == null) {
                throw new LBParameterException("No generic extension is available for ", "name", name);
            }
            return (GenericExtension) temp.loadClass(ed.getExtensionClass()).newInstance();
        }

        catch (Exception e) {
            String id = getLogger().error("Error getting generic extension " + name, e);
            throw new LBInvocationException("Unexpected error getting generic extension for " + name, id);
        }
    }
    
    @LgClientSideSafe
    public ExtensionDescriptionList getGenericExtensions() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance().getGenericExtensions();
    }

    @LgClientSideSafe
    public Filter getFilter(String name) throws LBException {
        getLogger().logMethod(new Object[] { name });
        return ExtensionRegistryImpl.instance().getFilter(name);
    }

    @LgClientSideSafe
    public ExtensionDescriptionList getFilterExtensions() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance().getFilterExtensions();
    }

    @LgClientSideSafe
    public Sort getSortAlgorithm(String name) throws LBException {
        getLogger().logMethod(new Object[] { name });
        return ExtensionRegistryImpl.instance().getSortAlgorithm(name);
    }

    @LgClientSideSafe
    public LexBIGServiceMetadata getServiceMetadata() throws LBException {
        getLogger().logMethod(new Object[] {});
        return new LexBIGServiceMetadataImpl();
    }
    
    private void registerExtensions() throws LBParameterException, LBException {
        // sort extensions
        new EntityDescriptionSort().register();
        new ConceptStatusSort().register();
        new isActiveSort().register();
        new CodeSystemSort().register();
        new CodeSort().register();
        new CodePostSort().register();
        new MatchToQuerySort().register();
        new NumberOfChildrenSort().register();
        
        //search extensions;
        new ContainsSearch().register();
        new DoubleMetaphoneSearch().register();
        new WeightedDoubleMetaphoneSearch().register();
        new ExactMatchSearch().register();
        new LuceneSearch().register();
        new RegExpSearch().register();
        new StartsWithSearch().register();
        new StemmedSearch().register();
        new PhraseSearch().register();
        new SubStringSearch().register();
        new LeadingAndTrailingWildcardSearch().register();
        new SpellingErrorTolerantSubStringSearch().register();
        new LiteralSearch().register();
        new LiteralContainsSearch().register();
        new LiteralSubStringSearch().register();
        new NonLeadingWildcardLiteralSubStringSearch().register();
        
        // load extensions
        new TextLoaderImpl().register();

        new LexGridMultiLoaderImpl().register();
        new OWLLoaderImpl().register();
        new OWL2LoaderImpl().register();
        new OBOLoaderImpl().register();
        new MetaDataLoaderImpl().register();
        new RadLexProtegeFramesLoaderImpl().register();
        new HL7LoaderImpl().register();
        new ClaMLLoaderImpl().register();
        new NCIHistoryLoaderImpl().register();
        new UMLSHistoryLoaderImpl().register();
        new MrmapRRFLoader().register();
        new SemNetLoaderImpl().register();
        new MedDRALoaderImpl().register();
        new MIFVocabularyLoaderImpl().register();
        
        //Meta Batch Loader Extension
        ExtensionDescription meta = new ExtensionDescription();
        meta.setExtensionBaseClass(MetaBatchLoader.class.getName());
        meta.setExtensionClass("org.lexgrid.loader.meta.MetaBatchLoaderImpl");
        meta.setDescription(MetaBatchLoader.DESCRIPTION);
        meta.setName(MetaBatchLoader.NAME);
        meta.setVersion(MetaBatchLoader.VERSION);
        try {
            ExtensionRegistryImpl.instance().registerLoadExtension(meta);
            
            LexEvsServiceLocator.getInstance().getSystemResourceService().addSystemEventListeners(new SystemEventListener() {
                //register a listener to clean up all the batch stuff on delete
                public void onRemoveCodingSchemeResourceFromSystemEvent(String uri,
                        String version) {
                    try {
                        MetaBatchLoader metaLoader = (MetaBatchLoader) getServiceManager(null).getLoader(MetaBatchLoader.NAME);
                        metaLoader.removeLoad(uri, version);
                    } catch (Exception e) {
                        getLogger().info(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            getLogger().warn(meta.getName() + " is not on the classpath or could not be loaded as an Extension.",e);
        }

        //Umls Batch Loader Extension
        ExtensionDescription umls = new ExtensionDescription();
        umls.setExtensionBaseClass(UmlsBatchLoader.class.getName());
        umls.setExtensionClass("org.lexgrid.loader.umls.UmlsBatchLoaderImpl");
        umls.setDescription(UmlsBatchLoader.DESCRIPTION);
        umls.setName(UmlsBatchLoader.NAME);
        umls.setVersion(UmlsBatchLoader.VERSION);
        try {
            ExtensionRegistryImpl.instance().registerLoadExtension(umls);
            
            LexEvsServiceLocator.getInstance().getSystemResourceService().addSystemEventListeners(new SystemEventListener() {
                //register a listener to clean up all the batch stuff on delete
                public void onRemoveCodingSchemeResourceFromSystemEvent(String uri,
                        String version) {
                    try {
                        UmlsBatchLoader umlsLoader = (UmlsBatchLoader) getServiceManager(null).getLoader(UmlsBatchLoader.NAME);
                        umlsLoader.removeLoad(uri, version);
                    } catch (Exception e) {
                        getLogger().info(e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            getLogger().warn(umls.getName() + " is not on the classpath or could not be loaded as an Extension.",e);
        }

        
        //Rxn Batch Loader Extension
        ExtensionDescription rxn = new ExtensionDescription();
        rxn.setExtensionBaseClass(RxNormBatchLoader.class.getName());
        rxn.setExtensionClass("org.lexgrid.loader.rxn.RxnBatchLoaderImpl");
        rxn.setDescription(RxNormBatchLoader.DESCRIPTION);
        rxn.setName(RxNormBatchLoader.NAME);
        rxn.setVersion(RxNormBatchLoader.VERSION);
        try {
            ExtensionRegistryImpl.instance().registerLoadExtension(rxn);
            
            LexEvsServiceLocator.getInstance().getSystemResourceService().addSystemEventListeners(new SystemEventListener() {
                //register a listener to clean up all the batch stuff on delete
                public void onRemoveCodingSchemeResourceFromSystemEvent(String uri,
                        String version) {
                    try {
                        RxNormBatchLoader rxnLoader = (RxNormBatchLoader) getServiceManager(null).getLoader(RxNormBatchLoader.NAME);
                        rxnLoader.removeLoad(uri, version);
                    } catch (Exception e) {
                        getLogger().info(e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            getLogger().warn(rxn.getName() + " is not on the classpath or could not be loaded as an Extension.",e);
        }
        
        //Rxn Batch Loader Extension
        ExtensionDescription rvsl = new ExtensionDescription();
        rvsl.setExtensionBaseClass(ResolvedValueSetDefinitionLoader.class.getName());
        rvsl.setExtensionClass("org.lexgrid.loader.ResolvedValueSetDefinitionLoaderImpl");
        rvsl.setDescription(ResolvedValueSetDefinitionLoader.DESCRIPTION);
        rvsl.setName(ResolvedValueSetDefinitionLoader.NAME);
        rvsl.setVersion(ResolvedValueSetDefinitionLoader.VERSION);
        try {
            ExtensionRegistryImpl.instance().registerLoadExtension(rvsl);

        } catch (Exception e) {
            getLogger().warn(rvsl.getName() + " is not on the classpath or could not be loaded as an Extension.",e);
        }
        // export extensions
        LexGridExport.register();
        OBOExport.register();
        OwlRdfExporterImpl.register();

        // Generic Extensions
        new SupportedAttributePostProcessor().register();
        new ApproxNumOfConceptsPostProcessor().register();
        LexBIGServiceConvenienceMethodsImpl.register();
        new MappingExtensionImpl().register();
        new OntologyFormatAddingPostProcessor().register();
        new SupplementExtensionImpl().register();
        new HierarchyCheckingPostProcessor().register();
        new SearchExtensionImpl().register();
    }
}
