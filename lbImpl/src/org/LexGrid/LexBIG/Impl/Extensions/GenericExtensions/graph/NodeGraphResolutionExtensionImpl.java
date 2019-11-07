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
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.naming.SupportedAssociation;
import org.lexevs.dao.database.graph.rest.client.LexEVSSpringRestClientImpl;
import org.lexevs.dao.database.utility.GraphingDatabaseUtil;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

public class NodeGraphResolutionExtensionImpl extends AbstractExtendable implements NodeGraphResolutionExtension {

    
    /**
     * 
     */
    private static final long serialVersionUID = -2869847921528174582L;
    
    
    public LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    public void logAndThrowRuntimeException(String message){
        getLogger().error(message);
        throw new RuntimeException(message);
    }
    
    public void logAndThrowRuntimeException(String message, Exception e){
        getLogger().error(message);
        throw new RuntimeException(message, e);
    }


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
            {
                logAndThrowRuntimeException("Not a valid association name: " 
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion());}
            set = this.getCodedNodeSetForScheme(reference);
            set = this.getCodedNodeSetForModelMatch(set, model, alg, textMatch);
            return new GraphNodeContentTrackingIterator(getConceptReferenceListForValidatedAssociation(reference, associationName, Direction.TARGET_OF, set, url));
           
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
        {logAndThrowRuntimeException("Not a valid association name: " 
                + associationName 
                + " CodingScheme " 
                + reference.getCodingSchemeURN() 
                + " version " 
                + reference.getCodingSchemeVersion());}
        set = this.getCodedNodeSetForScheme(reference);
        set = this.getCodedNodeSetForModelMatch(set, model, alg, textMatch);
        return new GraphNodeContentTrackingIterator(getConceptReferenceListForValidatedAssociation(reference, associationName, Direction.SOURCE_OF, set, url));
       
    } catch (LBException e) {
        logAndThrowRuntimeException("Not able to resolve an incoming edge graph for this coding scheme and graph "
                + associationName 
                + " CodingScheme " 
                + reference.getCodingSchemeURN() 
                + " version " 
                + reference.getCodingSchemeVersion(), e);     
    }
    return null;
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
                //We are creating a map from an entity code to a list of vertexes
                //and eventually combining that group of lists to single list of distinct vertexes
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
                //We are creating a map from an entity code to a list of vertexes
                //and eventually combining that group of lists to single list of distinct vertexes
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
            logAndThrowRuntimeException("Calls to LexEVS have failed in an unexpected way: ", e);
        }
        return null;
    }
    
    // A filter by property to be called once to create the predicate, which can
    // then be used to test given property values for duplicates
    <T> Predicate<T> distinctByProperty(Function<? super T, ?> getProperty) {
        Set<Object> exists = ConcurrentHashMap.newKeySet();
        return t -> exists.add(getProperty.apply(t));
    }



    protected List<ConceptReference> getConceptReferenceListForAllAssociations(AbsoluteCodingSchemeVersionReference ref,
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
                                .getOutBoundForGraphNode(
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
                             .getInBoundForGraphNode(
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
            logAndThrowRuntimeException("Calls to LexEVS have failed in an unexpected way: ", e);
        }
        return null;
    }
    

    @Override
    public List<ConceptReference> getConceptReferenceListResolvedFromGraphForEntityCode(
            AbsoluteCodingSchemeVersionReference reference, String associationName, Direction direction,
            String entityCode, String url) {
        LexEVSSpringRestClientImpl lexClientService = getGraphClientService(url);
        if (isGetTargetOF(direction)) {
            return lexClientService
                    .getOutBoundForGraphNode(lexClientService.getBaseUrl(),
                            getNormalizedDbNameForTermServiceIdentifiers(reference), associationName, entityCode)
                    .stream()
                    .map(z -> Constructors.createConceptReference(z.getCode(), z.getNamespace()))
                    .collect(Collectors.toList());
        } else {
            return lexClientService
                    .getInBoundForGraphNode(lexClientService.getBaseUrl(),
                            getNormalizedDbNameForTermServiceIdentifiers(reference), associationName, entityCode)
                    .stream()
                    .map(z -> Constructors.createConceptReference(z.getCode(), z.getNamespace()))
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


    ResolvedConceptReference[] getValidatedList(
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
        return direction.equals(Direction.SOURCE_OF);
    }

    private boolean isGetTargetOF(Direction direction) {
        return direction.equals(Direction.TARGET_OF);
    }

    protected Boolean isValidAssociation(String associationName, AbsoluteCodingSchemeVersionReference ref) throws LBParameterException {
        return ServiceUtility.IsValidParameter(ref.getCodingSchemeURN(),ref.getCodingSchemeVersion(), associationName, SupportedAssociation.class);
    }
    
    boolean isValidNodeForAssociation( AbsoluteCodingSchemeVersionReference ref, String entityCode, String associationName){
        return ServiceUtility.isValidNodeForAssociation(ref, entityCode, associationName);
    }
    
    List<String> getValidAssociationsForTargetOrSourceOf(AbsoluteCodingSchemeVersionReference ref, String entityCode){
        return ServiceUtility.getValidAssociationsForTargetOrSource(ref, entityCode);
    }
    

    private String getNormalizedDbNameForTermServiceIdentifiers(AbsoluteCodingSchemeVersionReference ref){
        try {
            return ServiceUtility.normalizeGraphandGraphDatabaseName(ref);
        } catch (LBParameterException e) {
            logAndThrowRuntimeException("Unable to retrieve and normalize database name for uri: " 
                    + ref.getCodingSchemeURN()
                    + " version: " + ref.getCodingSchemeVersion(), e);
        }
        return null;
    }

}
