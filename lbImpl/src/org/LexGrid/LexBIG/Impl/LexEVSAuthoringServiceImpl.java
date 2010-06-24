package org.LexGrid.LexBIG.Impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexEVSAuthoringService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedContainerName;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.ResourceManager;



public class LexEVSAuthoringServiceImpl implements LexEVSAuthoringService{
    

    CodingSchemeRendering[] codingSchemes;
    LexBIGService lbs;
    AuthoringService service;
    DatabaseServiceManager dbManager;
    LexEvsServiceLocator locator;
    IndexServiceManager indexService;
    
    
    //Coding Scheme Mapping Code System Defaults
    public static String CODING_SCHEME_NAME = "Mapping_Container";
    public static String CODING_SCHEME_URI = "http://default.mapping.container";
    public static String  FORMAL_NAME = "Mapping";
    public static String DEFAULT_LANGUAGE = "EN";
    public static String REPRESENTS_VERSION = "1.0";
    public static List<String> LOCAL_NAME_LIST = Arrays.asList("Mapping");    
    public static String REVISIONID = "NEW_MAPPING";
    //Relations Container defaults
    public static String CONTAINER_NAME = "Mapping_relations";
    public static String REPRESENTS_MAPPING_VERSION = "1.0";
    public static boolean IS_MAPPING = true;
    //Default association name
    public static String DEFAULT_MAPPING_NAME = "SY";
    //EntryState Defaults
    public static ChangeType CHANGE_TYPE = ChangeType.NEW;
    public static Long RELATIVE_ORDER = new Long(1);


    public LexEVSAuthoringServiceImpl(){
      
        lbs = LexBIGServiceImpl.defaultInstance();
        locator = LexEvsServiceLocator.getInstance();
        dbManager = locator.getDatabaseServiceManager();
        service = dbManager.getAuthoringService();
        indexService = locator.getIndexServiceManager();
        setCodingSchemes();
    }
    
    @Override
    public Association createAssociation() throws LBException {
        // TODO Auto-generated method stub
        return null;
    }
    private void setCodingSchemes(){
        try {
            codingSchemes = lbs.getSupportedCodingSchemes().getCodingSchemeRendering();
        } catch (LBInvocationException e) {
            
            e.printStackTrace();
        }
    }
    
    public CodingSchemeRendering[] getCodingSchemes(){
        return codingSchemes;
    }
    
    //This is more "Maintain Mapping"
    @Override
    public void createAssociationMapping(
            EntryState entryState, 
            AbsoluteCodingSchemeVersionReference sourceCodingScheme,
            AbsoluteCodingSchemeVersionReference targetCodingScheme, 
            AssociationSource[] associationSource, 
            String associationType,
            String relationsContainerName,
            Date effectiveDate,
            AssociationQualification[] associationQualifiers,
            String revisionId,
            long relativeOrder
            )
            throws LBException {
        
    }
//        
//        if (!codingSchemeExists(codingSchemes, sourceCodingScheme)) {
//            throw new LBResourceUnavailableException("Association source code system not found");
//        }
//
//        if (!codingSchemeExists(codingSchemes, targetCodingScheme)) {
//            throw new LBResourceUnavailableException("Association target code system not found");
//        }
//
//        // We have a target and source for this mapping. Now we'll resolve the source
//        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
//        csvt.setVersion(sourceCodingScheme.getCodingSchemeVersion());
//        CodingScheme scheme = lbs.resolveCodingScheme(sourceCodingScheme.getCodingSchemeURN(), csvt);
//
//        Relations relations = createRelationsContainer(
//                entryState,
//                scheme,
//                relationsContainerName,
//                effectiveDate,
//                sourceCodingScheme,
//                targetCodingScheme, 
//                true, 
//                associationType,
//                null);
//        AssociationPredicate predicate = createAssociationPredicate(associationType, associationSource);
//        for(AssociationSource source: associationSource){
//            
//        //Here it should "createSource -- checking for source in existing coding scheme each time.
//        predicate.addSource(source);
//        }
//        relations.addAssociationPredicate(predicate);
//        scheme.addRelations(relations);
////        if(associationQualifiers != null && !supportedAssociationQualifiersExist(scheme, associationQualifiers)){
////            throw new LBException("This association qualifier does not exist in " + scheme.getCodingSchemeName()
////                    + " vocabulary");
////        }  
//        service.loadRevision(scheme, "MappingRelease");
//       
//    }

//    @Override
//    public AssociationPredicate createAssociationPredicate(CodingScheme scheme,  String associationName) throws LBException {
//        
//       //Creating a new coding scheme.  Should we reserve the right to create an association type?
//        // probably should make this a message.
//        if (!supportedAssociationExists(scheme, associationName)) {
//            throw new LBException("This association type does not exist in " + scheme.getCodingSchemeName()
//                    + " vocabulary");
//        }
//        
//        AssociationPredicate predicate = new AssociationPredicate();
//        predicate.setAssociationName(associationName);
//        return predicate;
//    }
    
    @Override
    public AssociationPredicate createAssociationPredicate(  
            String associationName, 
            AssociationSource[] assocSources) throws LBException {
       AssociationPredicate predicate  = new AssociationPredicate();
       predicate.setAssociationName(associationName);
       predicate.setSource(Arrays.asList(assocSources));
        return predicate;
    }
    
    @Override
    public AssociationSource createAssociationSource(EntryState entryState,
            AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier, String sourceConceptCodeIdentifier,
            String relationsContainerName, String associationName, AssociationTarget[] targetList) throws LBException {

        // Initializing some objects we'll need to populate
        Revision revision = new Revision();
        CodingScheme scheme = getCodingSchemeMetaData(sourceCodeSystemIdentifier);
        Mappings mappings = new Mappings();
        AssociationPredicate predicate = new AssociationPredicate();
        AssociationSource source = new AssociationSource();

        // populate some revision and entry State values here
        revision.setRevisionId(entryState.getContainingRevision());
        entryState.setPrevRevision(scheme.getEntryState().getPrevRevision());
        entryState.setChangeType(ChangeType.DEPENDENT);

        // Processing and populating Relations
        Relations[] relations = scheme.getRelations();
        Relations revisedRelations = new Relations();
        for (Relations rel : relations) {
            if (rel.getContainerName().equals(relationsContainerName)) {
                revisedRelations.setContainerName(rel.getContainerName());
            }
        }
        if (revisedRelations.getContainerName() == null) {
            throw new LBException("relations container name " + relationsContainerName
                    + " not found in this CodingScheme");
        }
        revisedRelations.setEntryState(entryState);

        CodingScheme revisedScheme = populateCodingScheme(scheme.getCodingSchemeName(), scheme.getCodingSchemeURI(),
                null, null, scheme.getApproxNumConcepts(), scheme.getRepresentsVersion(), null, null, null, mappings,
                null, null, null);
        revisedScheme.setEntryState(entryState);
        
        // Predicate existance check;
        if (associationPredicateExists(associationName, scheme.getRelations())) {
            predicate.setAssociationName(associationName);
        } else {
            throw new LBException("No association predicate with name " + associationName + " exists in the "
                    + scheme.getCodingSchemeName() + " terminology");
        }
        
        // Checking to see to see if the source code exists in the coding Scheme
        Entity sourceEntity = getEntity(sourceConceptCodeIdentifier, scheme.getCodingSchemeURI(), scheme
                .getRepresentsVersion());
        // If so we can populate the Association source with some values
        if (sourceEntity != null) {
            source.setSourceEntityCode(sourceConceptCodeIdentifier);
            if (sourceEntity.getEntityCodeNamespace() != null) {
                source.setSourceEntityCodeNamespace(sourceEntity.getEntityCodeNamespace());
            } else {
                throw new LBException("Source code namespace should be enforced for this entity");
            }

        } else {
            throw new LBException("Source code for this association source does not exist");
        }

        //These should be the new element(s) we'll mark them as "NEW" revisions and add them to the
        //Source.
        EntryState newEntry = new EntryState();
        //newEntry.setChangeType(ChangeType.NEW);
        newEntry.setContainingRevision(entryState.getContainingRevision());
        newEntry.setPrevRevision(entryState.getPrevRevision());
        newEntry.setRelativeOrder(entryState.getRelativeOrder());
        newEntry.setChangeType(ChangeType.NEW);
        mapTargetsToSource(newEntry, revisedScheme, source, sourceCodeSystemIdentifier, targetList);
        predicate.addSource(source);
        revisedRelations.addAssociationPredicate(predicate);
        revisedScheme.addRelations(revisedRelations);
        ChangedEntry entry = new ChangedEntry();
        entry.setChangedCodingSchemeEntry(revisedScheme);
        ChangedEntry[] changes = new ChangedEntry[] { entry };

        revision.setChangedEntry(changes);
        service.loadRevision(revision, null);
        return source;
    }

    private Entity getEntity(String code, String codingSchemeURI, String representsVersion) throws LBException {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(representsVersion);
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(codingSchemeURI, csvt);
        cns = cns.restrictToCodes(ConvenienceMethods.createConceptReferenceList(code));
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, 1);
        return rcrl.getResolvedConceptReference(0).getEntity();
    }

    private boolean associationPredicateExists(String associationName, Relations[] relations) {
        for(Relations rel : relations){

            AssociationPredicate[] predicates = rel.getAssociationPredicate();
            for( AssociationPredicate predicate: predicates){
               if(predicate.getAssociationName().equals(associationName)){
                   return true;
               }
            }
        }
        return false;
    
    }

    @Override
    public AssociationTarget createAssociationTarget(
            EntryState entryState,
           // CodingScheme scheme, 
            AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
            String targetConceptCodeIdentifier) throws LBException {
        AssociationTarget target = new AssociationTarget();
     // Checking to see to see if the target code exists in the coding Scheme
        Entity sourceEntity = getEntity(targetConceptCodeIdentifier, 
                targetCodeSystemIdentifier.getCodingSchemeURN(), 
                targetCodeSystemIdentifier.getCodingSchemeVersion());
        // If so we can populate the Association target with some values
        if (sourceEntity != null) {
            target.setTargetEntityCode(targetConceptCodeIdentifier);
            if (sourceEntity.getEntityCodeNamespace() != null) {
                target.setTargetEntityCodeNamespace(sourceEntity.getEntityCodeNamespace());
            } else {
                throw new LBException("code namespace should be enforced for this entity");
            }

        } else {
            throw new LBException("code for this association target does not exist");
        }
        target.setEntryState(entryState);
        return target;
    }


    public CodingScheme getCodingSchemeMetaData(AbsoluteCodingSchemeVersionReference reference) throws LBException{
       
      CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
      csvt.setVersion(reference.getCodingSchemeVersion());
      try {
        return lbs.resolveCodingScheme(reference.getCodingSchemeURN(), csvt);
    } catch (LBException e) {
       throw new LBException("CodingScheme does not exist in this terminology service");
    }
    }
    @Override
    public Properties createCodingSchemeProperties(CodingScheme scheme) throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entities createEntities(CodingScheme scheme) throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entity createEntity(Entities entities) throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Relations createRelationsContainer(
            EntryState entryState,
            CodingScheme scheme,
            String containerName,
            Date effectiveDate,
            AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
            AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier, 
            boolean isMapping, 
            String associationType,
            Properties relationProperties) throws LBException {
      Relations relations = new Relations();
      relations.setContainerName(containerName);
      relations.setEffectiveDate(effectiveDate);
      return relations;
    }
    @Override
    public EntryState createDefaultMappingsEntryState(String prevRevisionId){
        EntryState entryState = new EntryState();
        entryState.setChangeType(CHANGE_TYPE);
        entryState.setContainingRevision(REVISIONID);
        entryState.setPrevRevision(prevRevisionId);
        entryState.setRelativeOrder(RELATIVE_ORDER);
        return entryState;
    }
    
    @Override
    public AssociationSource mapTargetsToSource(
            EntryState entryState,
            CodingScheme scheme, 
            AssociationSource source,
            AbsoluteCodingSchemeVersionReference codingSchemeIdentifier,
            AssociationTarget[] associationTargets)
            throws LBException {
        //This should be the new element(s) we'll mark them as "NEW" revisions and add them to the
        //Source.
        entryState.setChangeType(ChangeType.NEW);
        
        for (AssociationTarget at : associationTargets) {
            
            Entity targetEntity = getEntity(at.getTargetEntityCode(), scheme.getCodingSchemeURI(), scheme
                    .getRepresentsVersion());
            if(targetEntity != null){
            at.setEntryState(entryState);
            source.addTarget(at);
            }
            
        }
        return source;
    }
    
    @Override
    public void createMappingWithDefaultValues(AssociationSource[] sourcesAndTargets, String sourceCodingScheme,
            String sourceCodingSchemeVersion, String targetCodingScheme, String targetCodingSchemeVersion,
            String associationName) throws LBException {
        
        CodingScheme newScheme;
        //Create an entry state to attach to all version-able elements
        EntryState entryState = createDefaultMappingsEntryState(null);
        //Creating a mappings enabled relations container
        Relations relationsContainer = createDefaultRelationsContainer(sourceCodingScheme, sourceCodingSchemeVersion,
                targetCodingScheme, targetCodingSchemeVersion, associationName);
        //Set the association target entry states
        setAssociationEntryStates(sourcesAndTargets, entryState);
        //set up the association predicate.  We create just one for a mappings container.
        AssociationPredicate predicate = createAssociationPredicate(associationName, sourcesAndTargets);
        relationsContainer.setAssociationPredicate(Arrays.asList(predicate));
        //Set up a codingScheme with some default meta data
        newScheme = createDefaultMappingCodingScheme();
        newScheme.setRelations(Arrays.asList(relationsContainer));
        
        //process the entities for the mappings coding scheme.
        Entities entities = processMappingEntities(sourcesAndTargets,sourceCodingScheme,
        sourceCodingSchemeVersion, targetCodingScheme, targetCodingSchemeVersion, entryState);
        newScheme.setApproxNumConcepts(new Long(entities.getEntityCount()));
        newScheme.setEntities(entities);
        
        //Set up the supported entitities necessary for this coding scheme.
        newScheme.setMappings(processMappingsForAssociationMappings(sourceCodingScheme,
            sourceCodingSchemeVersion, targetCodingScheme,targetCodingSchemeVersion,
            associationName, null, null));
        
        //Set some entry states
        newScheme.setEntryState(entryState);
        relationsContainer.setEntryState(entryState);
        Revision revision = new Revision();
        revision.setRevisionId(REVISIONID);
        ChangedEntry changedEntry = new ChangedEntry();
        changedEntry.setChangedCodingSchemeEntry(newScheme);
        revision.addChangedEntry(changedEntry);
        //load as revision
        service.loadRevision(revision, "MappingRelease");
        AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
        reference.setCodingSchemeURN(newScheme.getCodingSchemeURI());
        reference.setCodingSchemeVersion(newScheme.getRepresentsVersion());
        
        //index the loaded mapping coding scheme
        indexService.getEntityIndexService().createIndex(reference);
    }
    
    private void setAssociationEntryStates(AssociationSource[] sourcesAndTargets, EntryState entryState) {

        for (AssociationSource as : sourcesAndTargets) {

            AssociationTarget[] targets = as.getTarget();
            for (AssociationTarget at : targets) {

                try {
                    at.setEntryState(entryState);
                } catch (IndexOutOfBoundsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private Mappings processMappingsForAssociationMappings(String sourceCodingScheme,
            String sourceCodingSchemeVersion, String targetCodingScheme, String targetCodingSchemeVersion,
            String associationName, String CodingSchemeURI, String relationsContainerName) throws IndexOutOfBoundsException, LBException{
        
        //TODO provide default name space and supported coding scheme values if not supported in sourc
        // and target coding schemes?????
         Mappings mappings = new Mappings();
         Mappings sourceMappings = null;
         Mappings targetMappings = null;

         SupportedAssociation supportedAssociation = new SupportedAssociation();

         SupportedContainerName containerName = new SupportedContainerName();
         sourceMappings = getMappingsFromScheme(sourceCodingScheme, sourceCodingSchemeVersion);
         targetMappings = getMappingsFromScheme(targetCodingScheme, targetCodingSchemeVersion);
         mappings.addSupportedCodingScheme(getSupportedCodingSchemeFromMappings(sourceMappings, sourceCodingScheme));
         mappings.addSupportedCodingScheme(getSupportedCodingSchemeFromMappings(targetMappings, targetCodingScheme));
         supportedAssociation.setLocalId(associationName);
         //TODO determine whether and how supported association should be created or determined from the source coding scheme.
         if(containerName == null){
         supportedAssociation.setUri(CodingSchemeURI);
         }
         else{
             supportedAssociation.setUri(CodingSchemeURI);
         }
         supportedAssociation.setContent(associationName);
         mappings.addSupportedAssociation(supportedAssociation);
         
         mappings.addSupportedNamespace(getSupportedNameSpaceFromMappings(sourceMappings, sourceCodingScheme));
         mappings.addSupportedNamespace(getSupportedNameSpaceFromMappings(targetMappings, targetCodingScheme));
         if(relationsContainerName == null){
         containerName.setContent(CONTAINER_NAME);
         containerName.setLocalId(CONTAINER_NAME);
         }
         else{
             containerName.setContent(relationsContainerName);
             containerName.setLocalId(relationsContainerName);
         }
         mappings.addSupportedContainerName(containerName);
        return mappings;
        
    }
    
    private SupportedNamespace getSupportedNameSpaceFromMappings(Mappings mappings, String codingScheme) throws LBException{

        SupportedNamespace[] nameSpaces = mappings.getSupportedNamespace();
        for (SupportedNamespace nspace : nameSpaces){
    
            if(nspace.getEquivalentCodingScheme() != null && nspace.getEquivalentCodingScheme().equals(codingScheme))
                    {  return nspace;}
        }
        throw new LBException("No supportedNameSpace found for this coding scheme mapping: " + codingScheme);
    }
    private SupportedCodingScheme getSupportedCodingSchemeFromMappings(Mappings mappings, String scheme) throws LBException{
        SupportedCodingScheme[] schemes = mappings.getSupportedCodingScheme();
        for (SupportedCodingScheme s : schemes){
            if(s.getLocalId().equals(scheme)){
                return s;
            }
        }
        
        throw new LBException("Supported Scheme not found for this mapping");
    }
   private Mappings getMappingsFromScheme(String codingScheme, String version){
       CodingScheme scheme = null;;
    Mappings mappings = null;
       CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
       csvt.setVersion(version);
       try {
       scheme = lbs.resolveCodingScheme(codingScheme, csvt);
      mappings = scheme.getMappings();
    } catch (LBException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       return mappings;
   }
    
    private Entities processMappingEntities(
            AssociationSource[] sourcesAndTargets, 
            String sourceScheme, String sourceVersion, 
            String targetScheme, 
            String targetVersion, 
            EntryState entryState) throws LBException {
        Entities entities = new Entities();
        ArrayList<String> cache = new ArrayList<String>();
        

        for(AssociationSource as : sourcesAndTargets){
            Entity entity = new Entity();

                try {
                   if(!cache.contains(as.getSourceEntityCode())){
                    entities.addEntity(retrieveEntityForMappingScheme(as.getSourceEntityCode(), sourceScheme, sourceVersion, entryState));
                    cache.add(as.getSourceEntityCode());
                   }
                } catch (IndexOutOfBoundsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (LBException e) {
                    e.printStackTrace();
                    throw new LBException("Problems matching and loading source entity " 
                            + as.getSourceEntityCode() +
                            " : "  +
                            as.getSourceEntityCodeNamespace());
                }
      
            AssociationTarget[] targets = as.getTarget();
            for(AssociationTarget at : targets){

                try {
                    if(!cache.contains(at.getTargetEntityCode())){
                    entities.addEntity(retrieveEntityForMappingScheme(at.getTargetEntityCode(), targetScheme, targetVersion, entryState));
                    cache.add(at.getTargetEntityCode());}
                } catch (IndexOutOfBoundsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (LBException e) {
                    e.printStackTrace();
                    throw new LBException("Problems matching and loading target entity " 
                            + at.getTargetEntityCode() +
                            " : "  +
                            at.getTargetEntityCodeNamespace());
                  
                }
            }
        } 

        return entities;
    }


    private Entity retrieveEntityForMappingScheme(String code, 
            String codingScheme, 
            String version, 
            EntryState entryState) throws LBException{
        Entity entity = new Entity();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(version);
        CodedNodeSet sourceCns = lbs.getCodingSchemeConcepts(codingScheme, csvt);
        sourceCns = sourceCns.restrictToCodes(ConvenienceMethods.createConceptReferenceList(code));
        ResolvedConceptReferenceList rcrl = sourceCns.resolveToList(null, null, null, null, true, 1);
        ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
        entity = rcr.getEntity();
        entity.setEntryState(entryState);
        return entity;
    }
    private CodingScheme createDefaultMappingCodingScheme() {
        CodingScheme newScheme = new CodingScheme();
        Source source = new Source();
        source.setContent("source");
        List<Source> sourceList = Arrays.asList(source);
        newScheme.setSource(sourceList);
        newScheme.setCodingSchemeName(CODING_SCHEME_NAME);
        newScheme.setCodingSchemeURI(CODING_SCHEME_URI);
        newScheme.setFormalName(FORMAL_NAME);
        newScheme.setDefaultLanguage(DEFAULT_LANGUAGE);
        newScheme.setRepresentsVersion(REPRESENTS_VERSION);
        newScheme.setLocalName(LOCAL_NAME_LIST);
        return newScheme;
    }
    
    private Relations createDefaultRelationsContainer(
            String sourceCodingScheme,
            String sourceCodingSchemeVersion,
            String targetCodingScheme,
            String targetCodingSchemeVersion,
            String associationName
            ){
       Relations relations = new Relations();
       relations.setContainerName(CONTAINER_NAME);
       relations.setIsMapping(IS_MAPPING);
       relations.setRepresentsVersion(REPRESENTS_MAPPING_VERSION);
       relations.setSourceCodingScheme(sourceCodingScheme);
       relations.setSourceCodingSchemeVersion(sourceCodingSchemeVersion);
       relations.setTargetCodingScheme(targetCodingSchemeVersion);
       relations.setTargetCodingSchemeVersion(targetCodingSchemeVersion);
       AssociationPredicate predicate = new AssociationPredicate();
       predicate.setAssociationName(associationName);
       relations.getAssociationPredicateAsReference().add(predicate);
       return relations;
    }
    
    //TODO return void and persist the coding scheme as a revision
    @Override
    public CodingScheme createCodingScheme(String codingSchemeName, String codingSchemeURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings, Properties properties, Entities entities,
            List<Relations>  relationsList, EntryState entryState) throws LBException {
        if(codingSchemeName == null){
            throw new LBException("Coding scheme name cannot be null");
        }
        if(codingSchemeURI == null){
            throw new LBException("Coding scheme URI cannot be null");
        }
        if(representsVersion == null){
            throw new LBException("Coding scheme version cannot be null");
        }
        if(mappings == null){
            throw new LBException("Coding scheme mappings cannot be null");
        }
        CodingScheme scheme = new CodingScheme();
        scheme.setCodingSchemeName(codingSchemeName);
        scheme.setCodingSchemeURI(codingSchemeURI);
 
        scheme.setFormalName(formalName);

        scheme.setDefaultLanguage(defaultLanguage);
        scheme.setApproxNumConcepts(approxNumConcepts);
        scheme.setRepresentsVersion(representsVersion);

        scheme.setLocalName(localNameList);

        scheme.setSource(sourceList);

        scheme.setCopyright(copyright);

        scheme.setMappings(mappings);

        scheme.setProperties(properties);
        scheme.setEntities(entities);
        
        return scheme;
    }
    public CodingScheme populateCodingScheme(String codingSchemeName, String codingSchemeURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings, Properties properties, Entities entities,
            List<Relations>  relationsList) throws LBException{
        if(codingSchemeName == null){
            throw new LBException("Coding scheme name cannot be null");
        }
        if(codingSchemeURI == null){
            throw new LBException("Coding scheme URI cannot be null");
        }
        if(representsVersion == null){
            throw new LBException("Coding scheme version cannot be null");
        }
        if(mappings == null){
            throw new LBException("Coding scheme mappings cannot be null");
        }
        CodingScheme scheme = new CodingScheme();
        scheme.setCodingSchemeName(codingSchemeName);
        scheme.setCodingSchemeURI(codingSchemeURI);
        if(formalName != null)
        scheme.setFormalName(formalName);
        if(defaultLanguage != null)
        scheme.setDefaultLanguage(defaultLanguage);
        scheme.setApproxNumConcepts(approxNumConcepts);
        scheme.setRepresentsVersion(representsVersion);
        if(localNameList != null)
        scheme.setLocalName(localNameList);
        if(sourceList != null)
        scheme.setSource(sourceList);
        if(copyright != null)
        scheme.setCopyright(copyright);
        
        scheme.setMappings(mappings);
        if(properties != null)
        scheme.setProperties(properties);
        if(entities != null)
        scheme.setEntities(entities);
        return scheme;
    }
    public boolean codingSchemeExists(CodingSchemeRendering[] codingSchemes,
            AbsoluteCodingSchemeVersionReference codingScheme) {
        for (CodingSchemeRendering cr : codingSchemes) {
            CodingSchemeSummary summary = cr.getCodingSchemeSummary();
            if (summary.getCodingSchemeURI().equals(codingScheme.getCodingSchemeURN())
                    && summary.getRepresentsVersion().equals(codingScheme.getCodingSchemeVersion())) {
                return true;
            }

        }
        return false;
    }

    public boolean supportedAssociationExists(CodingScheme scheme, String associationName) {
        // Check for supported association and qualifiers.
        SupportedAssociation[] associations = scheme.getMappings().getSupportedAssociation();

        for (SupportedAssociation sa : associations) {
            if (sa.getContent().equals(associationName))
                return true;
        }
        return false;
    }

    // probably don't want to get this fine grained. Something to discuss.
    public boolean supportedAssociationQualifiersExists(CodingScheme scheme, AssociationQualification[] assocQualifiers) {
        SupportedAssociationQualifier[] qualifiers = scheme.getMappings().getSupportedAssociationQualifier();
        for (SupportedAssociationQualifier saq : qualifiers) {
            for (AssociationQualification aq : assocQualifiers) {
                if (saq.getContent().equals(aq.getAssociationQualifier())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String getCodingSchemeNamespace(CodingScheme scheme, String URI){
      ListIterator<SupportedNamespace> mappings = scheme.getMappings().getSupportedNamespaceAsReference().listIterator();
      while(mappings.hasNext()){
          SupportedNamespace nameSpace = mappings.next();
          if(nameSpace.getUri().equals(URI)){
              return nameSpace.getContent();
          }
      }
        return null;
    }
    
    public boolean conceptCodeExists(String code, String codingSchemeURI, String version)throws LBException {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(version);
        try {
            CodedNodeSet cns = lbs.getCodingSchemeConcepts(codingSchemeURI, csvt);
            cns.restrictToCodes(ConvenienceMethods.createConceptReferenceList(code));
            ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, 1);
            if (list.getResolvedConceptReferenceCount() > 0) {
                return true;
            }
        } catch (LBException e) {
            throw new LBException("Concept for code " +
                    code + " does not exist in coding scheme with URI " +
                    codingSchemeURI);
        }
        
        return false;
    }
    

    
    
    public static void main(String[] args){
        String URI = "urn:oid:11.11.0.1";
        String version = "1.0";
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
        ref.setCodingSchemeURN(URI);
        ref.setCodingSchemeVersion(version);
        csvt.setVersion(version);
        try {
            LexEVSAuthoringServiceImpl authoring = new LexEVSAuthoringServiceImpl();
           EntryState entryState = authoring.createDefaultMappingsEntryState(null);
 //           EntryState entryState = new EntryState();
//            entryState.setChangeType(ChangeType.NEW);
   
           
           AssociationTarget target = authoring.createAssociationTarget(null, ref, "005");
           AssociationTarget[] targets = new AssociationTarget[]{target};
            authoring.createAssociationSource(entryState, ref, "A0001", "relations", "hasSubtype", targets);
          
           
//           AssociationSource source = new AssociationSource();
//           AssociationSource source1 = new AssociationSource();
//           AssociationSource source2 = new AssociationSource();
//           source.setSourceEntityCode("T0001");       
//           source.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
//           AssociationTarget target = new AssociationTarget();
//           target.setTargetEntityCode("005");
//           target.setTargetEntityCodeNamespace("Automobiles");
//           source.addTarget(target);
//           
//           source1.setSourceEntityCode("P0001");
//           source1.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
//           AssociationTarget target1 = new AssociationTarget();
//           target1.setTargetEntityCode("A0001");
//           target1.setTargetEntityCodeNamespace("Automobiles");
//           source1.addTarget(target1);
//           
//           source2.setSourceEntityCode("P0001");
//           source2.setSourceEntityCodeNamespace("GermanMadePartsNamespace"); 
//           AssociationTarget target2 = new AssociationTarget();
//           target2.setTargetEntityCode("005");
//           target2.setTargetEntityCodeNamespace("Automobiles");
//           source2.addTarget(target2);
//           AssociationSource[] sources = new AssociationSource[]{source, source1, source2};
//           authoring.createMappingWithDefaultValues(sources, "GermanMadeParts", "2.0", "Automobiles", "1.0", "SY");
           // System.out.println(authoring.conceptCodeExists("005", URI, version));
          //  CodingScheme scheme = authoring.lbs.resolveCodingScheme(URI, csvt);
           // System.out.println(authoring.getCodingSchemeNamespace(scheme, URI));
          // EntryState es = authoring.createMappingsEntryState("ENTRYTEST:Jan2010", new Long(0),ChangeType.NEW, null);
          // System.out.println(ObjectToString.toString(es));
//           
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//               
    }



    

}
