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
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.ResourceManager;



public class LexEVSAuthoringServiceImpl implements LexEVSAuthoringService{
    

    CodingSchemeRendering[] codingSchemes;
    LexBIGService lbs;
    AuthoringService service;
    DatabaseServiceManager dbManager;
    LexEvsServiceLocator locator;
    
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


    public LexEVSAuthoringServiceImpl(){
      
        lbs = LexBIGServiceImpl.defaultInstance();
        locator = LexEvsServiceLocator.getInstance();
        dbManager = locator.getDatabaseServiceManager();
        service = dbManager.getAuthoringService();
        setCodingSchemes();
    }
    
    @Override
    public Association createAssociation() throws LBException {
        // TODO Auto-generated method stub
        return null;
    }
    public void setCodingSchemes(){
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
        
        if (!codingSchemeExists(codingSchemes, sourceCodingScheme)) {
            throw new LBResourceUnavailableException("Association source code system not found");
        }

        if (!codingSchemeExists(codingSchemes, targetCodingScheme)) {
            throw new LBResourceUnavailableException("Association target code system not found");
        }

        // We have a target and source for this mapping. Now we'll resolve the source
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(sourceCodingScheme.getCodingSchemeVersion());
        CodingScheme scheme = lbs.resolveCodingScheme(sourceCodingScheme.getCodingSchemeURN(), csvt);

        Relations relations = createRelationsContainer(
                entryState,
                scheme,
                relationsContainerName,
                effectiveDate,
                sourceCodingScheme,
                targetCodingScheme, 
                true, 
                associationType,
                null);
        AssociationPredicate predicate = createAssociationPredicate(associationType, associationSource);
        for(AssociationSource source: associationSource){
            
        //Here it should "createSource -- checking for source in existing coding scheme each time.
        predicate.addSource(source);
        }
        relations.addAssociationPredicate(predicate);
        scheme.addRelations(relations);
//        if(associationQualifiers != null && !supportedAssociationQualifiersExist(scheme, associationQualifiers)){
//            throw new LBException("This association qualifier does not exist in " + scheme.getCodingSchemeName()
//                    + " vocabulary");
//        }  
        service.loadRevision(scheme, "MappingRelease");
       
    }

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
    public AssociationSource createAssociationSource(CodingScheme scheme, Relations relations,
            AssociationPredicate predicate, AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
            String sourceConceptCodeIdentifier, AssociationTarget[] targetList) throws LBException {
        AssociationSource source = new AssociationSource();
        CodingSchemeRendering[] localCodingSchemes = getCodingSchemes();
        if (!conceptCodeExists(sourceConceptCodeIdentifier, scheme.getCodingSchemeURI(), scheme.getRepresentsVersion())) {
            source.setSourceEntityCode(sourceConceptCodeIdentifier);
        } else {
            throw new LBException("Source code for this association source does not exist");
        }
        String namespace = getCodingSchemeNamespace(scheme, scheme.getCodingSchemeURI());
        if (namespace != null) {
            source.setSourceEntityCodeNamespace(namespace);
        } else {
            throw new LBException("Source namespace does not exist or is not supported in this coding scheme");
        }

        if (codingSchemeExists(localCodingSchemes, sourceCodeSystemIdentifier)) {
            for (AssociationTarget at : targetList) {
                source.addTarget(at);
            }
        }
        return source;
    }

    @Override
    public AssociationTarget createAssociationTarget(
            EntryState entryState,
            CodingScheme scheme, 
            AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
            String targetConceptCodeIdentifier) throws LBException {
        AssociationTarget target = new AssociationTarget();
        CodingSchemeRendering[] localCodingSchemes = getCodingSchemes();
        if (!codingSchemeExists(localCodingSchemes, targetCodeSystemIdentifier)) {
            throw new LBException("Target code system does not exist in this service");
        }
        if (!conceptCodeExists(targetConceptCodeIdentifier, scheme.getCodingSchemeURI(), scheme.getRepresentsVersion())) {
            target.setTargetEntityCode(targetConceptCodeIdentifier);
        } else {
            throw new LBException("Target code for this association source does not exist");
        }
        String namespace = getCodingSchemeNamespace(scheme, scheme.getCodingSchemeURI());
        if (namespace != null) {
            target.setTargetEntityCodeNamespace(namespace);
        } else {
            throw new LBException("Target namespace does not exist or is not supported in this coding scheme");
        }
        target.setEntryState(entryState);
        return target;
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
    public EntryState createMappingsEntryState(String revisionId, Long relativeOrder, ChangeType changeType, String prevRevisionId  ){
        EntryState entryState = new EntryState();
        entryState.setChangeType(changeType);
        entryState.setContainingRevision(revisionId);
        entryState.setPrevRevision(prevRevisionId);
        entryState.setRelativeOrder(relativeOrder);
        return entryState;
    }
    
    @Override
    public AssociationSource mapTargetsToSource(
            EntryState entryState,
            CodingScheme scheme, 
            String sourceCode,
            AbsoluteCodingSchemeVersionReference codingSchemeIdentifier,
            AssociationTarget[] associationTargets)
            throws LBException {
        AssociationSource source = new AssociationSource();
        // TODO Check each target - source relationship for existence in the source coding system.
        CodingSchemeRendering[] localCodingSchemes = getCodingSchemes();
        if (!conceptCodeExists(sourceCode, scheme.getCodingSchemeURI(), scheme.getRepresentsVersion())) {
            source.setSourceEntityCode(sourceCode);
        } else {
            throw new LBException("Source code for this association source does not exist");
        }
        String namespace = getCodingSchemeNamespace(scheme, scheme.getCodingSchemeURI());
        if (namespace != null) {
            source.setSourceEntityCodeNamespace(namespace);
        } else {
            throw new LBException("Source namespace does not exist or is not supported in this coding scheme");
        }
        
        if (codingSchemeExists(localCodingSchemes, codingSchemeIdentifier)) {
            for (AssociationTarget at : associationTargets) {
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
        //TODO in any case the coding scheme needs mappings for supported codingschemes, namespaces and other values as needed
        //TODO dynamically create entities from the codes in the mappings
        //TODO create an "approximate number" of those entities to attach to the coding scheme
        Relations relationsContainer = createDefaultRelationsContainer(sourceCodingScheme, sourceCodingSchemeVersion,
                targetCodingScheme, targetCodingSchemeVersion, associationName);
        AssociationPredicate predicate = createAssociationPredicate(associationName, sourcesAndTargets);
        relationsContainer.setAssociationPredicate(Arrays.asList(predicate));
        Source source = new Source();
        source.setContent("source");
        List<Source> sourceList = Arrays.asList(source);
        newScheme = createDefaultMappingCodingScheme(sourceList, new Long(10));
        newScheme.setRelations(Arrays.asList(relationsContainer));
        Entities entities = processMappingEntities(sourcesAndTargets,sourceCodingScheme,
        sourceCodingSchemeVersion, targetCodingScheme, targetCodingSchemeVersion);
        newScheme.setApproxNumConcepts(new Long(entities.getEntityCount()));
        newScheme.setEntities(entities);
        newScheme.setMappings(processMappingsForAssociationMappings(sourceCodingScheme,
            sourceCodingSchemeVersion, targetCodingScheme,targetCodingSchemeVersion,
            associationName, null, null));
        service.loadRevision(newScheme, "MappingRelease");
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
            if(nspace.getEquivalentCodingScheme().equals(codingScheme))
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
            String targetVersion) throws LBException {
        Entities entities = new Entities();

        for(AssociationSource as : sourcesAndTargets){
            Entity entity = new Entity();

                try {
                    entities.addEntity(retrieveEntityForMappingScheme(as.getSourceEntityCode(), sourceScheme, sourceVersion));
                } catch (IndexOutOfBoundsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (LBException e) {
                    e.printStackTrace();
                    throw new LBException("Problems matching and loading source entity ");
                }
      
            AssociationTarget[] targets = as.getTarget();
            for(AssociationTarget at : targets){

                try {
                    entities.addEntity(retrieveEntityForMappingScheme(at.getTargetEntityCode(), targetScheme, targetVersion));
                } catch (IndexOutOfBoundsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (LBException e) {
                    e.printStackTrace();
                    throw new LBException("Problems matching and loading target entity ");
                  
                }
            }
        } 

       
        return entities;
    }


    private Entity retrieveEntityForMappingScheme(String code, String codingScheme, String version) throws LBException{
        Entity entity = new Entity();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(version);
        CodedNodeSet sourceCns = lbs.getCodingSchemeConcepts(codingScheme, csvt);
        sourceCns = sourceCns.restrictToCodes(ConvenienceMethods.createConceptReferenceList(code));
        ResolvedConceptReferenceList rcrl = sourceCns.resolveToList(null, null, null, 1);
        ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
        entity = rcr.getEntity();
        return entity;
    }
    private CodingScheme createDefaultMappingCodingScheme(List<Source> sources, Long approximateNoConcepts) {
        CodingScheme newScheme = new CodingScheme();
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
    
    @Override
    public CodingScheme createCodingScheme(String codingSchemeName, String codingSchemeURI, String formalName,
            String defaultLanguage, long approxNumberofConcepts, String representsVerison, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings, Properties properties, Entities entities,
            List<Relations>  relationsList) throws LBException {
        // TODO Auto-generated method stub
        return null;
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    

    
    
    public static void main(String[] args){
        String URI = "urn:oid:11.11.0.1";
        String version = "1.0";
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(version);
        try {
            LexEVSAuthoringServiceImpl authoring = new LexEVSAuthoringServiceImpl();
            
          
           
           AssociationSource source = new AssociationSource();
           source.setSourceEntityCode("T0001");
           source.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
           AssociationTarget target = new AssociationTarget();
           target.setTargetEntityCode("005");
           target.setTargetEntityCodeNamespace("Automobiles");
           source.addTarget(target);
           AssociationSource[] sources = new AssociationSource[]{source};
           authoring.createMappingWithDefaultValues(sources, "GermanMadeParts", "2.0", "Automobiles", "1.0", "SY");
           // System.out.println(authoring.conceptCodeExists("005", URI, version));
          //  CodingScheme scheme = authoring.lbs.resolveCodingScheme(URI, csvt);
           // System.out.println(authoring.getCodingSchemeNamespace(scheme, URI));
          // EntryState es = authoring.createMappingsEntryState("ENTRYTEST:Jan2010", new Long(0),ChangeType.NEW, null);
          // System.out.println(ObjectToString.toString(es));
           
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
               
    }



    

}
