
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.naming.SupportedAssociation;
import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.graph.rest.client.LexEVSSpringRestClientImpl;
import org.lexevs.logging.LoggerFactory;


public class NodeGraphResolutionExtensionImpl extends AbstractExtendable implements NodeGraphResolutionExtension {

/**
     * url is the REST service URL.  It must be initialized by calling init()
     */
String url;
    
    /**
     * 
     */
    private static final long serialVersionUID = -2869847921528174582L;
    
    //API interface implementations
    @Override
    public void init(String url){
        this.url = url;
    }

    @Override
    public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationTargetOf(
            AbsoluteCodingSchemeVersionReference reference, 
            String associationName, 
            String textMatch,
            AlgorithmMatch alg, 
            ModelMatch model){
        return getConceptReferencesForTextSearchAndAssociationTargetOf(-1, reference, associationName, textMatch, alg, model);

    }

    @Override
    public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationSourceOf(
            AbsoluteCodingSchemeVersionReference reference, 
            String associationName, 
            String textMatch,
            AlgorithmMatch alg, 
            ModelMatch model) {
        return getConceptReferencesForTextSearchAndAssociationSourceOf(-1, reference, associationName, textMatch, alg, model);
    }
    
    
    @Override
    public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationSourceOf(
            int depth, AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model) {
        
        return getConceptReferencesForTextSearchAndAssociationSourceOf(
                depth, reference, associationName, textMatch, alg, model, null, null);
    }
    
    @Override
    public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationSourceOf(int depth,
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model, LocalNameList sources, NameAndValueList qualifiers) {
        CodedNodeSet set = null; 
        if(reference == null || textMatch == null || alg == null || model == null){
            logAndThrowRuntimeException("null value of any parameter but assocationName is not allowed");
        }
        try {
            if(associationName == null){ 
                set = preProcessNodeSetForQueryValues(reference, set, model, alg, textMatch, sources, qualifiers);
                return new GraphNodeContentTrackingIterator(
                        getConceptReferenceListForAllAssociations(depth, reference, Direction.SOURCE_OF, set));
            }
            if(!this.isValidAssociation(associationName, reference))
            {
                logAndThrowRuntimeException("Not a valid association name: " 
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion());}
            set = preProcessNodeSetForQueryValues(reference, set, model, alg, textMatch, sources, qualifiers);
            return new GraphNodeContentTrackingIterator(
                    getConceptReferenceListForValidatedAssociation(depth, reference, associationName, Direction.SOURCE_OF, set));
           
        } catch (LBException e) {
            logAndThrowRuntimeException("Not able to resolve an outgoing edge graph for this coding scheme and graph "
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion(), e);     
        }
        return null;
    }

    
    

    @Override
    public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationTargetOf(
            int depth, AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model) {
        return getConceptReferencesForTextSearchAndAssociationTargetOf(
                depth, reference, associationName, textMatch, alg, model, null, null);
    }
    
    @Override
    public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationTargetOf(int depth,
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model, LocalNameList sources, NameAndValueList qualifiers) {
        CodedNodeSet set = null; 
        if(reference == null || textMatch == null || alg == null || model == null){
            logAndThrowRuntimeException("null value of any parameter but assocationName is not allowed");
        }
        try {
            if(associationName == null){ 
                set = preProcessNodeSetForQueryValues(reference, set, model, alg, textMatch, sources, qualifiers);
                return new GraphNodeContentTrackingIterator(
                        getConceptReferenceListForAllAssociations(depth, reference, Direction.TARGET_OF, set));
            }
            if(!this.isValidAssociation(associationName, reference))
            {
                logAndThrowRuntimeException("Not a valid association name: " 
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion());}
            set =  preProcessNodeSetForQueryValues(reference, set, model, alg, textMatch, sources, qualifiers);
            return new GraphNodeContentTrackingIterator(
                    getConceptReferenceListForValidatedAssociation(depth, reference, associationName, Direction.TARGET_OF, set));
           
        } catch (LBException e) {
            logAndThrowRuntimeException("Not able to resolve an outgoing edge graph for this coding scheme and graph "
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion(), e);     
        }
        return null;
    }


    @Override
    public List<ResolvedConceptReference> getCandidateConceptReferencesForTextAndAssociation(
            AbsoluteCodingSchemeVersionReference reference, 
            String associationName, 
            String textMatch,
            AlgorithmMatch alg, 
            ModelMatch model) {
        return getCandidateConceptReferencesForTextAndAssociation(
                reference, associationName, textMatch, alg, model, null, null);

    }
    

    @Override
    public List<ResolvedConceptReference> getCandidateConceptReferencesForTextAndAssociation(
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model, LocalNameList sources, NameAndValueList qualifiers) {
        if(reference == null || associationName == null || textMatch == null || alg == null || model == null){
            logAndThrowRuntimeException("null value for any parameter other than sources or qualifiers is not allowed");
        }
        CodedNodeSet set = null;
        try {
            set = preProcessNodeSetForQueryValues(reference, set, model, alg, textMatch, sources, qualifiers);
            ResolvedConceptReference[] list  =  set.resolveToList(null, null, null, 10).getResolvedConceptReference();
            return Stream.of(list)
            .filter(
                    x -> isValidNodeForAssociation(reference, x.getCode(), 
                            associationName))
            .collect(Collectors.toList());
        } catch (LBException e) {
            logAndThrowRuntimeException("Something went wrong while querying for "
                    + "candidate matches for:  " + textMatch + " associated with: "
                    + associationName, e);
        }
        return null;
    }

    
    @Override
    public List<ConceptReference> getConceptReferenceListResolvedFromGraphForEntityCode(
            AbsoluteCodingSchemeVersionReference reference, 
            String associationName, 
            Direction direction,
            String entityCode) {
        return getConceptReferenceListResolvedFromGraphForEntityCode(reference, -1, associationName, direction, entityCode);
    }
    
    @Override
    public List<ConceptReference> getConceptReferenceListResolvedFromGraphForEntityCode(
            AbsoluteCodingSchemeVersionReference reference, 
            int depth,
            String associationName, 
            Direction direction,
            String entityCode) {
            if(reference == null || associationName == null || direction == null || entityCode == null){
                logAndThrowRuntimeException("null value for any parameter is not allowed");
            }
            LexEVSSpringRestClientImpl lexClientService = getGraphClientService();
                return lexClientService
                        .getVertexesForGraphNode(direction.getDirection(), depth, 
                                getNormalizedDbNameForTermServiceIdentifiers(reference), associationName, entityCode)
                        .stream()
                        .map(z -> Constructors.createConceptReference(z.getCode(), z.getNamespace()))
                        .collect(Collectors.toList());
    }
    
    @Override
    public List<ResolvedConceptReference> doGetResolvedConceptReferenceListResolvedFromGraphForEntityCode(
            AbsoluteCodingSchemeVersionReference reference, 
            int depth,
            String associationName, 
            Direction direction,
            String entityCode) {
            if(reference == null || associationName == null || direction == null || entityCode == null){
                logAndThrowRuntimeException("null value for any parameter is not allowed");
            }
            LexEVSSpringRestClientImpl lexClientService = getGraphClientService();
                return lexClientService
                        .getVertexesForGraphNode(direction.getDirection(), depth, 
                                getNormalizedDbNameForTermServiceIdentifiers(reference), associationName, entityCode)
                        .stream()
                        .map(z -> createResolvedConceptReference(z.getCode(), z.getNamespace(), z.getDescription(), null, null, null))
                        .collect(Collectors.toList());
    }
    
    private ResolvedConceptReference createResolvedConceptReference( String code, String namespace, String description, String codingSchemeURI, String codingSchemeVersion, String codingSchemeName){
        ResolvedConceptReference ref = new ResolvedConceptReference();
        ref.setCode(code);
        ref.setCodeNamespace(namespace);
        ref.setEntityDescription(Constructors.createEntityDescription(description));
        ref.setCodingSchemeURI(codingSchemeURI);
        ref.setCodingSchemeVersion(codingSchemeVersion);
        ref.setCodingSchemeName(codingSchemeName);
        return ref;
    }


    @Override
    public String getNormalizedDbNameForTermServiceIdentifiers(
            AbsoluteCodingSchemeVersionReference ref){
        try {
            return ServiceUtility.normalizeGraphandGraphDatabaseName(ref);
        } catch (LBParameterException e) {
            logAndThrowRuntimeException("Unable to retrieve and normalize database name for uri: " 
                    + ref.getCodingSchemeURN()
                    + " version: " + ref.getCodingSchemeVersion(), e);
        }
        return null;
    }

    @Override
    public List<String> getTerminologyGraphDatabaseList() {
        return getGraphClientService().systemMetadata().getDataBases();
    }

    @Override
    public List<String> getGraphsForCodingSchemeName(String name) {
        return getGraphClientService().getGraphDatabaseMetadata(name).getGraphs();
    }
    
    
    //Utility methods
    
    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    private void logAndThrowRuntimeException(String message){
        getLogger().error(message);
        throw new RuntimeException(message);
    }
    
    private void logAndThrowRuntimeException(
            String message, 
            Exception e){
        getLogger().error(message);
        throw new RuntimeException(message, e);
    }
    


    private LexEVSSpringRestClientImpl getGraphClientService(){
      return  new LexEVSSpringRestClientImpl(url); 
    }

    

    protected ResolvedConceptReference[] getValidatedList(
            AbsoluteCodingSchemeVersionReference ref, 
            String association, 
            CodedNodeSet set) throws LBInvocationException, LBParameterException {
       ResolvedConceptReferenceList list =  set.resolveToList(null, null, null, 10);
       return Stream
               .of(list.getResolvedConceptReference())
                   .filter(x -> isValidNodeForAssociation(ref, x.getCode(), association))
                   .collect(Collectors.toList())
                   .toArray(new ResolvedConceptReference[]{});
    }

    protected Boolean isValidAssociation(
            String associationName, 
            AbsoluteCodingSchemeVersionReference ref) throws LBParameterException {
        return ServiceUtility.IsValidParameter(ref.getCodingSchemeURN(),ref.getCodingSchemeVersion(), associationName, SupportedAssociation.class);
    }
    
    boolean isValidNodeForAssociation( 
            AbsoluteCodingSchemeVersionReference ref, 
            String entityCode, 
            String associationName){
        return ServiceUtility.isValidNodeForAssociation(ref, entityCode, associationName);
    }
    
    protected List<String> getValidAssociationsForTargetOrSourceOf(
            AbsoluteCodingSchemeVersionReference ref, 
            String entityCode){
        return ServiceUtility.getValidAssociationsForTargetOrSource(ref, entityCode);
    }
    
    protected CodedNodeSet getCodedNodeSetForScheme(
            AbsoluteCodingSchemeVersionReference ref) throws LBException{
        return LexBIGServiceImpl.defaultInstance()
                .getCodingSchemeConcepts(
                        ref.getCodingSchemeURN(), 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(ref.getCodingSchemeVersion()));
    }
    
    protected CodedNodeSet getCodedNodeSetForModelMatch(
            CodedNodeSet set, 
            ModelMatch model, 
            AlgorithmMatch alg, 
            String text,
            LocalNameList sourceList,
            NameAndValueList qualifierList) throws LBInvocationException, LBParameterException{
        switch(model){
        case NAME: return set.restrictToMatchingDesignations(text, SearchDesignationOption.PREFERRED_ONLY, alg.getMatch(), null);
        case CODE: return set.restrictToCodes(Constructors.createConceptReferenceList(text));
        case PROPERTY: 
            return set.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, sourceList,
                    null,
                    qualifierList,
                    text,
                    alg.getMatch(), 
                    null);
        default: return set;
        }
    }
    
    
    // A filter (by property) to be called once to create the predicate, which can
    // then be used to test given property values for duplicates
    protected <T> Predicate<T> distinctByProperty(Function<? super T, ?> getProperty) {
        Set<Object> exists = ConcurrentHashMap.newKeySet();
        return t -> exists.add(getProperty.apply(t));
    }

   public CodedNodeSet preProcessNodeSetForQueryValues(
           AbsoluteCodingSchemeVersionReference reference, 
           CodedNodeSet set, ModelMatch model, AlgorithmMatch alg, 
           String textMatch, LocalNameList sources, NameAndValueList qualifiers) throws LBException{
       set = getCodedNodeSetForScheme(reference);
       set = getCodedNodeSetForModelMatch(set, model, alg, textMatch, sources, qualifiers);
       return set;
   }

    protected List<ConceptReference> getConceptReferenceListForAllAssociations(int depth,
            AbsoluteCodingSchemeVersionReference ref,
            Direction direction, 
            CodedNodeSet set) {
        try{            
            ResolvedConceptReference[] list  =  set.resolveToList(null, null, null, 10)
                    .getResolvedConceptReference();
            Map<String, List<String>> map = Stream.of(list)
                    .map(x->x.getCode())
                    .collect(Collectors
                            .toMap(Function.identity(), 
                                        s -> getValidAssociationsForTargetOrSourceOf(ref, s)));
           return processVertexesForMap(map, direction, depth, ref);
        } catch (LBInvocationException | LBParameterException e) {
            logAndThrowRuntimeException("Calls to LexEVS have failed in an unexpected way: ", e);
        }
        return null;
    }
    
    protected List<ConceptReference> getConceptReferenceListForValidatedAssociation(int depth,
            AbsoluteCodingSchemeVersionReference ref, String associationName, Direction direction,
            CodedNodeSet set) {
        try {
            ResolvedConceptReference[] list = getValidatedList(ref, associationName, set);
            return processVertexesForList(list, direction, depth, ref, associationName);
        } catch (LBInvocationException | LBParameterException e) {
            logAndThrowRuntimeException("Calls to LexEVS have failed in an unexpected way: ", e);
        }
        return null;
    }
    
    protected List<ConceptReference> processVertexesForList(
            ResolvedConceptReference[] list, 
            Direction direction, 
            int depth, 
            AbsoluteCodingSchemeVersionReference ref, 
            String associationName){
        LexEVSSpringRestClientImpl lexClientService = getGraphClientService();
        //We are creating a map from an entity code to a list of vertexes resolved from a graph
        //and eventually combining that group of lists to single list of distinct vertexes
        return Stream.of(list)
                .map(x -> lexClientService.getVertexesForGraphNode(direction.getDirection(), depth, 
                        getNormalizedDbNameForTermServiceIdentifiers(ref),
                        associationName, 
                        x.getCode()))
                .flatMap(y -> y.stream())
                .map(z -> 
                        Constructors.createConceptReference(z.getCode(), z.getNamespace()))
        //Stateful filtering where filter calls distinctByProperty once and predicate.test thereafter
                .filter(distinctByProperty(ConceptReference::getCode))
                .collect(Collectors.toList());
        
    }
    
    protected List<ResolvedConceptReference> processVertexesForMinimallyResolvedList(
            ResolvedConceptReference[] list, 
            Direction direction, 
            int depth, 
            AbsoluteCodingSchemeVersionReference ref, 
            String associationName){
        LexEVSSpringRestClientImpl lexClientService = getGraphClientService();
        //We are creating a map from an entity code to a list of vertexes resolved from a graph
        //and eventually combining that group of lists to single list of distinct vertexes
        return Stream.of(list)
                .map(x -> getResovledConceptReferencesForVertexList(lexClientService.getVertexesForGraphNode(direction.getDirection(), depth, 
                        getNormalizedDbNameForTermServiceIdentifiers(ref == null? getCSReferenceFromResolvedRefConcept(x): ref),
                        associationName, 
                        x.getCode()), x))
                .flatMap(y -> y.stream())
        //Stateful filtering where filter calls distinctByProperty once and predicate.test thereafter
                .filter(distinctByProperty(ConceptReference::getCode))
                .collect(Collectors.toList());
        
    }
    
    private List<ResolvedConceptReference> getResovledConceptReferencesForVertexList(List<LexVertex> vertexes, ResolvedConceptReference startVertex){
        return vertexes.stream().map(x -> createResolvedConceptReference(
                x.getCode(), 
                x.getNamespace(), 
                x.getDescription(), 
                startVertex.getCodingSchemeURI(), 
                startVertex.getCodingSchemeVersion(), 
                startVertex.getCodingSchemeName()))
                .collect(Collectors.toList());
    }
    
    private AbsoluteCodingSchemeVersionReference getCSReferenceFromResolvedRefConcept(ResolvedConceptReference x) {
        AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
        ref.setCodingSchemeURN(x.getCodingSchemeURI());
        ref.setCodingSchemeVersion(x.getCodingSchemeVersion());
        return ref;
    }

    protected List<ConceptReference> processVertexesForMap(
            Map<String, List<String>> map, 
            Direction direction, 
            int depth, 
            AbsoluteCodingSchemeVersionReference ref){
        LexEVSSpringRestClientImpl lexClientService = getGraphClientService();
        //We are using a map from an entity code to a list of associations to create a set of 
        //vertexes resolved from the graph of each association.  
        //Eventually we combine the groups of lists to single list of distinct vertexes
        return map.entrySet().stream()
         .flatMap(entry -> entry.getValue()
                 .stream()
                 .map(x -> lexClientService.getVertexesForGraphNode(direction.getDirection(), depth, 
                                 getNormalizedDbNameForTermServiceIdentifiers(ref),
                                 x, 
                                 entry.getKey())))
                 .flatMap(y -> y.stream())                                
         .map(z -> 
                Constructors.createConceptReference(z.getCode(), z.getNamespace()))
       //Stateful filtering where filter calls distinctByProperty once and predicate.test thereafter
         .filter(distinctByProperty(ConceptReference::getCode))
         .collect(Collectors.toList());
        
    }

    //Extension specific methods
    @Override
    protected void doRegister(
            ExtensionRegistry registry, 
            ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Node Graph Resolution Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(NodeGraphResolutionExtensionImpl.class.getName());
        ed.setName("NodeGraphResolution");
        ed.setVersion("1.0");
        
        return ed;
    }

    @Override
    public GraphNodeContentTrackingIterator<ResolvedConceptReference> getResolvedConceptReferenceListResolvedFromGraphForEntityCode(
            AbsoluteCodingSchemeVersionReference reference, String associationName, Direction direction,
            String entityCode) {
        List<ResolvedConceptReference> list = doGetResolvedConceptReferenceListResolvedFromGraphForEntityCode(
                reference, -1, associationName, direction, entityCode);
        return new GraphNodeContentTrackingIterator<ResolvedConceptReference>(list);
    }

    private ResolvedConceptReference generateMinimalResolvedConceptReference(AbsoluteCodingSchemeVersionReference reference, ConceptReference x) {
        try {
            return ServiceUtility.getResolvedConceptReference(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), x.getCode(), x.getCodeNamespace());
        } catch (LBException e) {
            throw new RuntimeException("Failed to resolve concept reference: " + x.getCode() + ":" + x.getCodeNamespace() 
            + " from coding scheme: " + reference.getCodingSchemeURN() + ":" + reference.getCodingSchemeVersion(), e);
        }
    }

    public static void main(String ...strings){
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        try {
            NodeGraphResolutionExtensionImpl service = (NodeGraphResolutionExtensionImpl) lbs.getGenericExtension("NodeGraphResolution"); 
            service.init("http://localhost:8080/graph-resolve");
            
            long begin = System.currentTimeMillis();
            
            List<ConceptReference> itr2 = service.getConceptReferenceListResolvedFromGraphForEntityCode(
                    Constructors.createAbsoluteCodingSchemeVersionReference(
                            "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "19.10d"), 
                            "Anatomic_Structure_Is_Physical_Part_Of", Direction.SOURCE_OF, "C12508");
            System.out.println("time: " + (System.currentTimeMillis() - begin));
            System.out.println("size: " + itr2.size());
            
            long start = System.currentTimeMillis();
            GraphNodeContentTrackingIterator<ResolvedConceptReference> itr = service.getResolvedConceptReferenceListResolvedFromGraphForEntityCode(
                    Constructors.createAbsoluteCodingSchemeVersionReference(
                            "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "19.10d"), 
                            "Anatomic_Structure_Is_Physical_Part_Of", Direction.SOURCE_OF, "C12508");
            System.out.println("time: " + (System.currentTimeMillis() - start));
            System.out.println("size: " + itr.getTotalCacheSize());
            
            long begin2 = System.currentTimeMillis();
            List<ConceptReference> itr3 = service.getConceptReferenceListResolvedFromGraphForEntityCode(
                    Constructors.createAbsoluteCodingSchemeVersionReference(
                            "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "19.10d"), 
                            "Concept_In_Subset", Direction.SOURCE_OF, "C63923");
            System.out.println("time: " + (System.currentTimeMillis() - begin2));
            System.out.println("size: " + itr3.size());
            
            long start1 = System.currentTimeMillis();
            GraphNodeContentTrackingIterator<ResolvedConceptReference> itr4 = service.getResolvedConceptReferenceListResolvedFromGraphForEntityCode(
                    Constructors.createAbsoluteCodingSchemeVersionReference(
                            "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "19.10d"), 
                            "Concept_In_Subset", Direction.SOURCE_OF, "C63923");
            System.out.println("time: " + (System.currentTimeMillis() - start1));
            System.out.println("size: " + itr4.getTotalCacheSize());
            
            System.out.println("Timing iterator");
            long startItr = System.currentTimeMillis();
            int count = 0;
            while(itr4.hasNext()){
               // count++;
                itr4.next();
//                if(count == 500){
//                    break;
//                }
            }
            System.out.println("Iterator time: " + (System.currentTimeMillis() - startItr));
                   
            final String interesting = "Terms & Properties Preferred Name:  Blood "
                    + "Definition:  A liquid tissue; its major function is to transport oxygen throughout the body. "
                    + "It also supplies the tissues with nutrients, removes waste products, "
                    + "and contains various components of the immune system defending the body against infection. "
                    + "Several hormones also travel in the blood.CDISC "
                    + "Definition:  A liquid tissue with the primary function of transporting oxygen and carbon dioxide. "
                    + "It supplies the tissues with nutrients, removes waste products, and contains various components "
                    + "of the immune system defending the body against infection. "
                    + "NCI-GLOSS Definition:  A tissue with red blood cells, white blood cells, platelets, and other substances "
                    + "suspended in fluid called plasma. Blood takes oxygen and nutrients to the tissues, and carries away wastes."
                    + "Display Name:  Blood"
                    + "Label:  Blood"
                    + "NCI Thesaurus Code:  C12434 "
                    + "Blood"
                    + "blood"
                    + "BLOOD"
                    + "Peripheral Blood"
                    + "peripheral blood"
                    + "Reticuloendothelial System, Blood"
                    + "Whole Blood"
                    + "UMLS CUI    C0005767"
                    + "code    C12434"
                    + "Contributing_Source CDISC"
                    + "Contributing_Source CPTAC"
                    + "Contributing_Source CTRP"
                    + "Contributing_Source GDC"
                    + "Legacy_Concept_Name Blood";
            final byte[] utf8Bytes = interesting.getBytes("UTF-8");
            System.out.println("Bytes length for fully resolved concept reference: " + utf8Bytes.length); 
            final String interestingToo = "Blood C12434 ncit";
                 
            final byte[] utf8BytesToo = interestingToo.getBytes("UTF-8");
            System.out.println("Bytes length for minimally resolved concept reference: " + utf8BytesToo.length); 
            

        CodedNodeSet cns =  lbs.getCodingSchemeConcepts(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", 
                Constructors.createCodingSchemeVersionOrTagFromVersion("19.10d"));
        PropertyType[] propertyTypes = new PropertyType[]{PropertyType.PRESENTATION};
        cns.restrictToMatchingDesignations("blood", SearchDesignationOption.PREFERRED_ONLY, "LuceneQuery", null);
        ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, 1);
        ResolvedConceptReference ref =   list.getResolvedConceptReference(0);
        ref.getCode();
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<ResolvedConceptReference> getAssociatedConcepts(CodedNodeSet cns, Direction direction, int depth,
            NameAndValueList associations) {
        //null and forbidden checks
        if(cns == null){throw new RuntimeException("CodedNodeSet cannot be null");}
        if(direction == null){throw new RuntimeException("Direction cannot be null");}
        if(depth == 0){return null;}
        
        ResolvedConceptReferenceList list = null;
        try {
            list = cns.resolveToList(null, null, null, 10);
            if(list == null || list.getResolvedConceptReferenceCount() == 0){return new ArrayList<ResolvedConceptReference>();}
         } catch (LBInvocationException | LBParameterException e) {
             throw new RuntimeException("Problem Resolving a search for text and search type", e);
         }
        List<String> validAssociations = null;
        if(associations == null){ validAssociations = Stream.of(list.getResolvedConceptReference())
                .map(ref -> 
                    getValidAssociationsForTargetOrSourceOf(
                                Constructors.createAbsoluteCodingSchemeVersionReference(
                                        ref.getCodingSchemeURI(), ref.getCodingSchemeVersion()), ref.getCode()))
            .flatMap(names -> names.stream())
            .distinct()
            .collect(Collectors.toList());}
        else{ validAssociations = Stream.of(associations.getNameAndValue())
                .map(nv -> nv.getContent())
                .collect(Collectors.toList());}
       final ResolvedConceptReference[] array = list.getResolvedConceptReference();
       return validAssociations.stream()
               .map(association ->processVertexesForMinimallyResolvedList(array, direction, depth, null, association))
               .flatMap(y -> y.stream())
               .collect(Collectors.toList());
    }


}