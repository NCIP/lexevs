package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.naming.SupportedAssociation;
import org.lexevs.dao.database.graph.rest.client.LexEVSSpringRestClientImpl;
import org.lexevs.dao.database.utility.GraphingDatabaseUtil;
import org.lexevs.locator.LexEvsServiceLocator;

public class NodeGraphResolutionExtensionImpl extends AbstractExtendable implements NodeGraphResolutionExtension {

    /**
     * 
     */
    private static final long serialVersionUID = -2869847921528174582L;


    LexEVSSpringRestClientImpl getGraphClientService(String url){
      return  new LexEVSSpringRestClientImpl(url); 
    }


    @Override
    public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationTargetOf(
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model, String url){
        CodedNodeSet set = null; 

        try {
            if(associationName == null){ 
                set = this.getCodedNodeSetForScheme(reference);
                set = this.getCodedNodeSetForModelMatch(set, model, alg, textMatch);
                return new GraphNodeContentTrackingIterator(getConceptReferenceListForAllAssociations(reference, Direction.TARGET_OF, set, url));
            }
            if(!this.isValidAssociation(associationName, reference))
            {throw new RuntimeException("Not a valid association name: " 
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion());}
            set = this.getCodedNodeSetForScheme(reference);
            set = this.getCodedNodeSetForModelMatch(set, model, alg, textMatch);
            return new GraphNodeContentTrackingIterator(getConceptReferenceListForValidatedAssociation(reference, associationName, Direction.TARGET_OF, set, url));
           
        } catch (LBException e) {
            throw new RuntimeException("Not able to resolve an outgoing edge graph for this coding scheme and graph "
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion());     
        }
    }

    @Override
    public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationSourceOf(
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model, String url) {
    CodedNodeSet set = null; 

    try {
        if(associationName == null){ 
            set = this.getCodedNodeSetForScheme(reference);
            set = this.getCodedNodeSetForModelMatch(set, model, alg, textMatch);
            return new GraphNodeContentTrackingIterator(getConceptReferenceListForAllAssociations(reference, Direction.SOURCE_OF, set, url));
        }
        if(!this.isValidAssociation(associationName, reference))
        {throw new RuntimeException("Not a valid association name: " 
                + associationName 
                + " CodingScheme " 
                + reference.getCodingSchemeURN() 
                + " version " 
                + reference.getCodingSchemeVersion());}
        set = this.getCodedNodeSetForScheme(reference);
        set = this.getCodedNodeSetForModelMatch(set, model, alg, textMatch);
        return new GraphNodeContentTrackingIterator(getConceptReferenceListForValidatedAssociation(reference, associationName, Direction.SOURCE_OF, set, url));
       
    } catch (LBException e) {
        throw new RuntimeException("Not able to resolve an incoming edge graph for this coding scheme and graph "
                + associationName 
                + " CodingScheme " 
                + reference.getCodingSchemeURN() 
                + " version " 
                + reference.getCodingSchemeVersion());     
    }
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
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
    
    protected CodedNodeSet getCodedNodeSetForScheme(AbsoluteCodingSchemeVersionReference ref) throws LBException{
        return LexBIGServiceImpl.defaultInstance()
                .getCodingSchemeConcepts(
                        ref.getCodingSchemeURN(), 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(ref.getCodingSchemeVersion()));
    }
    
    protected CodedNodeSet getCodedNodeSetForModelMatch(CodedNodeSet set, ModelMatch model, AlgorithmMatch alg, String text) throws LBInvocationException, LBParameterException{
        switch(model){
        case NAME: return set.restrictToMatchingDesignations(text, SearchDesignationOption.PREFERRED_ONLY, alg.getMatch(), null);
        case CODE: return set.restrictToCodes(Constructors.createConceptReferenceList(text));
        case PROPERTY: 
            return set.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, text, alg.getMatch(), null);
        default: return set;
        }
    }
    
    
    protected List<ConceptReference> getConceptReferenceListForValidatedAssociation(
            AbsoluteCodingSchemeVersionReference ref, 
            String associationName, 
            Direction direction,  
            CodedNodeSet set, 
            String url){

        LexEVSSpringRestClientImpl lexClientService = getGraphClientService(url);
        try {
            ResolvedConceptReference[] list = getValidatedList(ref, associationName, set);
            if(isGetTargetOF(direction)){
            return Stream.of(list)
            .map(x -> lexClientService
                    .getOutBoundForGraphNode(
                            lexClientService.getBaseUrl(), 
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
            if(isGetSourceOF(direction)){
                return Stream.of(list)
                        .map(x -> lexClientService
                                .getInBoundForGraphNode(
                                        lexClientService.getBaseUrl(), 
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
        } catch (LBInvocationException | LBParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    private <T> Predicate<T> distinctByProperty(Function<? super T, ?> getProperty) {
        Set<Object> exists = ConcurrentHashMap.newKeySet();
        return t -> exists.add(getProperty.apply(t));
    }



    private List<ConceptReference> getConceptReferenceListForAllAssociations(AbsoluteCodingSchemeVersionReference ref,
            Direction direction, CodedNodeSet set, String url) {
        LexEVSSpringRestClientImpl lexClientService = getGraphClientService(url);
        try{            
            ResolvedConceptReference[] list  =  set.resolveToList(null, null, null, 10).getResolvedConceptReference();
            Map<String, List<String>> map = Stream
                    .of(list)
                    .map(x->x.getCode())
                    .collect(Collectors
                            .toMap(
                                    Function.identity(), 
                                        s -> getValidAssociationsForTargetOrSourceOf(ref, s)));
        if(isGetTargetOF(direction)){
            return map.entrySet().stream()
                    .flatMap(entry -> entry.getValue()
                        .stream()
                        .map(x -> lexClientService
                                .getInBoundForGraphNode(
                                    lexClientService.getBaseUrl(), 
                                    getNormalizedDbNameForTermServiceIdentifiers(ref),
                                    x, 
                                    entry.getKey())))
                    .flatMap(y -> y.stream())
                    .map(z -> 
                          Constructors
                              .createConceptReference(z.getCode(), z.getNamespace()))
                  //Stateful filtering where filter calls distinctByProperty once and predicate.test thereafter
                    .filter(distinctByProperty(ConceptReference::getCode))
                    .collect(Collectors.toList());
        }
        if(isGetSourceOF(direction)){
            return map.entrySet().stream()
             .flatMap(entry -> entry.getValue()
                     .stream()
                     .map(x -> lexClientService
                             .getOutBoundForGraphNode(
                                     lexClientService.getBaseUrl(), 
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
        } catch (LBInvocationException | LBParameterException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    @Override
    public List<ConceptReference> getConceptReferenceListResolvedFromGraphForEntityCode(
            AbsoluteCodingSchemeVersionReference reference,
            String associationName, 
            Direction direction, 
            String entityCode, 
            String url) {
        LexEVSSpringRestClientImpl lexClientService = getGraphClientService(url);
        if(isGetTargetOF(direction)){
        return lexClientService
        .getInBoundForGraphNode(
            lexClientService.getBaseUrl(), 
            getNormalizedDbNameForTermServiceIdentifiers(reference),
            associationName, 
            entityCode).stream().map(z -> 
            Constructors.createConceptReference(z.getCode(), z.getNamespace()))
                .collect(Collectors.toList());
        } else{
            return lexClientService
                    .getOutBoundForGraphNode(
                        lexClientService.getBaseUrl(), 
                        getNormalizedDbNameForTermServiceIdentifiers(reference),
                        associationName, 
                        entityCode).stream().map(z -> 
                        Constructors.createConceptReference(z.getCode(), z.getNamespace()))
                            .collect(Collectors.toList());
            
        }
    }


    @Override
    public List<ResolvedConceptReference> getCandidateConceptReferencesForTextAndAssociation(
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model, String url) {
        CodedNodeSet set = null;
        try {
            set = this.getCodedNodeSetForScheme(reference);
            set = this.getCodedNodeSetForModelMatch(set, model, alg, textMatch);
            ResolvedConceptReference[] list  =  set.resolveToList(null, null, null, 10).getResolvedConceptReference();
            return Stream
            .of(list).filter(
                    x -> isValidNodeForAssociation(reference, x.getCode(), 
                            associationName))
            .collect(Collectors.toList());
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    private ResolvedConceptReference[] getValidatedList(
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


    private boolean isGetSourceOF(Direction direction) {
        // TODO Auto-generated method stub
        return direction.equals(Direction.SOURCE_OF);
    }

    private boolean isGetTargetOF(Direction direction) {
        // TODO Auto-generated method stub
        return direction.equals(Direction.TARGET_OF);
    }

    protected Boolean isValidAssociation(String associationName, AbsoluteCodingSchemeVersionReference ref) throws LBParameterException {
        return ServiceUtility.IsValidParameter(ref.getCodingSchemeURN(),ref.getCodingSchemeVersion(), associationName, SupportedAssociation.class);
    }
    
    boolean isValidNodeForAssociation( AbsoluteCodingSchemeVersionReference ref, String entityCode, String associationName){
        return ServiceUtility.isValidNodeForAssociation(ref, entityCode, associationName);
    }
    
    private List<String> getValidAssociationsForTargetOrSourceOf(AbsoluteCodingSchemeVersionReference ref, String entityCode){
        return ServiceUtility.getValidAssociationsForTargetOrSource(ref, entityCode);
    }
    

    private String getNormalizedDbNameForTermServiceIdentifiers(AbsoluteCodingSchemeVersionReference ref){
        try {
            return GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(LexEvsServiceLocator
                    .getInstance()
                    .getSystemResourceService()
                    .getInternalCodingSchemeNameForUserCodingSchemeName(
                            ref.getCodingSchemeURN(), ref.getCodingSchemeVersion()));
        } catch (LBParameterException e) {
            throw new RuntimeException("Unable to retrieve and normalize database name for uri :" 
                    + ref.getCodingSchemeURN()
                    + " version: " + ref.getCodingSchemeVersion());
        }
    }
    

    
    public static void main(String ...args){
        AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b");
        NodeGraphResolutionExtensionImpl extension = null;
        try {
            extension = (NodeGraphResolutionExtensionImpl) LexBIGServiceImpl.defaultInstance().getGenericExtension("NodeGraphResolution");
        } catch (LBParameterException | LBInvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Startup Query");
        long start0 = System.currentTimeMillis();
        Iterator<ConceptReference> target0list = extension.getConceptReferencesForTextSearchAndAssociationTargetOf(ref, "Anatomic_Structure_Is_Physical_Part_Of", "Blood", AlgorithmMatch.CONTAINS, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
        while(target0list.hasNext()){
            System.out.println(target0list.next().getCode());
        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start0));
        System.out.println("\ntarget of Blood");
        long start = System.currentTimeMillis();
        Iterator<ConceptReference> targetlist = extension.getConceptReferencesForTextSearchAndAssociationTargetOf(ref, "Anatomic_Structure_Is_Physical_Part_Of", "Blood", AlgorithmMatch.CONTAINS, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
//        while(targetlist.hasNext()){
//            System.out.println(targetlist.next().getCode());
//        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start));
        System.out.println("\nsource of Blood");
        long start2 = System.currentTimeMillis();
        Iterator<ConceptReference> sourcelist = extension.getConceptReferencesForTextSearchAndAssociationSourceOf(ref, "Anatomic_Structure_Is_Physical_Part_Of", "Blood", AlgorithmMatch.CONTAINS, ModelMatch.NAME, "http://localhost:8080/graph-resolve");;
//        while(sourcelist.hasNext()){
//            System.out.println(sourcelist.next().getCode());
//        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start2));
        
        System.out.println("\ntarget of C61410");
        long start3 = System.currentTimeMillis();
        Iterator<ConceptReference> target2list = extension.getConceptReferencesForTextSearchAndAssociationTargetOf(ref, "subClassOf", "Clinical Data Interchange Standards Consortium Terminology", AlgorithmMatch.EXACT_MATCH, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
//        while(target2list.hasNext()){
//            System.out.println(target2list.next().getCode());
//        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start3));
        System.out.println("\nsource of C61410");
        long start4 = System.currentTimeMillis();
        Iterator<ConceptReference> source2list = extension.getConceptReferencesForTextSearchAndAssociationSourceOf(ref, "subClassOf", "Clinical Data Interchange Standards Consortium Terminology", AlgorithmMatch.EXACT_MATCH, ModelMatch.NAME, "http://localhost:8080/graph-resolve");;
//        while(source2list.hasNext()){
//            System.out.println(source2list.next().getCode());
//        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start4));
        
        System.out.println("\ntarget of Pharmacologic Substance");
        long start5 = System.currentTimeMillis();
        Iterator<ConceptReference> target5list = extension.getConceptReferencesForTextSearchAndAssociationTargetOf(ref, "subClassOf", "Pharmacologic Substance", AlgorithmMatch.EXACT_MATCH, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
//        while(target2list.hasNext()){
//            System.out.println(target2list.next().getCode());
//        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start5));
        System.out.println("\nsource of Pharmacologic Substance");
        long start6 = System.currentTimeMillis();
        Iterator<ConceptReference> source6list = extension.getConceptReferencesForTextSearchAndAssociationSourceOf(ref, "subClassOf", "Pharmacologic Substance", AlgorithmMatch.EXACT_MATCH, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
//        while(source2list.hasNext()){
//            System.out.println(source2list.next().getCode());
//        }
        
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start6));
        
        System.out.println("\nSourceOf Contains Blood for all Associations");
        long start7 = System.currentTimeMillis();
        Iterator<ConceptReference> source7list = extension.getConceptReferencesForTextSearchAndAssociationSourceOf(ref, null, "Blood", AlgorithmMatch.CONTAINS, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
//        while(sourcelist.hasNext()){
//            System.out.println(sourcelist.next().getCode());
//        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start7));
        
        System.out.println("\nTargetof contains Blood for all Associations");
        long start8 = System.currentTimeMillis();
        Iterator<ConceptReference> source8list = extension.getConceptReferencesForTextSearchAndAssociationTargetOf(ref, null, "Blood", AlgorithmMatch.CONTAINS, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
//        while(sourcelist.hasNext()){
//            System.out.println(sourcelist.next().getCode());
//        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start8));
        
        System.out.println("\nSourceOf Exact Match Blood for all Associations");
        long start9 = System.currentTimeMillis();
        GraphNodeContentTrackingIterator source9list = (GraphNodeContentTrackingIterator) extension.getConceptReferencesForTextSearchAndAssociationSourceOf(ref, null, "Blood", AlgorithmMatch.EXACT_MATCH, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
        while(source9list.hasNext()){
            System.out.println(source9list.next().getCode());
        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start9));
        
        System.out.println("\nTargetOf Exact Match Blood for all Associations");
        long start10 = System.currentTimeMillis();
        GraphNodeContentTrackingIterator source10list = (GraphNodeContentTrackingIterator) extension.getConceptReferencesForTextSearchAndAssociationTargetOf(ref, null, "Blood", AlgorithmMatch.EXACT_MATCH, ModelMatch.NAME, "http://localhost:8080/graph-resolve");
        while(source10list.hasNext()){
            System.out.println(source10list.next().getCode());
        }
        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start10));
        
        List<ConceptReference> cfList = extension.getConceptReferenceListResolvedFromGraphForEntityCode(ref, "Anatomic_Structure_Is_Physical_Part_Of", Direction.TARGET_OF, "C12434", "http://localhost:8080/graph-resolve");
        cfList.forEach(x -> System.out.println(x.getCode()));
        
        List<ResolvedConceptReference> cfLis2 = extension.getCandidateConceptReferencesForTextAndAssociation(ref, "Anatomic_Structure_Is_Physical_Part_Of", "BLOOD", AlgorithmMatch.LUCENE, ModelMatch.PROPERTY, "http://localhost:8080/graph-resolve");
        cfLis2.forEach(x -> System.out.println(x.getEntityDescription().getContent()));
    }



}
