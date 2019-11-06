package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.AlgorithmMatch;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.ModelMatch;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.graph.rest.client.LexEVSSpringRestClientImpl;

public class NodeGraphPerformanceTestNCItSpecific {
	
	NodeGraphResolutionExtensionImpl ngr;
	LexEVSSpringRestClientImpl client;
	String url = "http://localhost:8080/graph-resolve";

	@Before
	public void setUp() throws Exception {
		ngr = (NodeGraphResolutionExtensionImpl) LexBIGServiceImpl
				.defaultInstance()
				.getGenericExtension("NodeGraphResolution");
	}
	
	@Test
	public void runPerformanceTest(){
			//Create scheme version reference
	        AbsoluteCodingSchemeVersionReference ref = 
	        		Constructors
	        		.createAbsoluteCodingSchemeVersionReference(
	        				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b");
	        
	        //Initialize the extension
	        NodeGraphResolutionExtensionImpl extension = null;
	        try {
	            extension = (NodeGraphResolutionExtensionImpl) LexBIGServiceImpl
	            		.defaultInstance()
	            		.getGenericExtension("NodeGraphResolution");
	        } catch (LBParameterException | LBInvocationException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
	        //Create a startup query to normalize return times
	        System.out.println("Startup Query using Blood, Anatomic_Structure_Is_Physical_Part_Of");
	        long start0 = System.currentTimeMillis();
	        Iterator<ConceptReference> target0list = extension
	        		.getConceptReferencesForTextSearchAndAssociationTargetOf(
	        				ref, 
	        				"Anatomic_Structure_Is_Physical_Part_Of", 
	        				"Blood", AlgorithmMatch.CONTAINS, ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        while(target0list.hasNext()){
	            System.out.println(target0list.next().getCode());
	        }
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start0));
	        System.out.println("We can safely ignore time delay on the startup query");
	        
	        System.out.println("\nTarget of Blood");
	        long start = System.currentTimeMillis();
	        Iterator<ConceptReference> targetlist = extension
	        		.getConceptReferencesForTextSearchAndAssociationTargetOf(
	        				ref, 
	        				"Anatomic_Structure_Is_Physical_Part_Of", 
	        				"Blood", 
	        				AlgorithmMatch.CONTAINS, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println(targetlist.next().getCode());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start));
	        
	        System.out.println("\nSource of Blood");
	        long start2 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator sourcelist = 
	        		(GraphNodeContentTrackingIterator) extension
	        		.getConceptReferencesForTextSearchAndAssociationSourceOf(
	        				ref, 
	        				"Anatomic_Structure_Is_Physical_Part_Of", 
	        				"Blood", 
	        				AlgorithmMatch.CONTAINS, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + sourcelist.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start2));
	        
	        System.out.println("\nTarget of C61410");
	        long start3 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator target2list = (GraphNodeContentTrackingIterator)extension
	        		.getConceptReferencesForTextSearchAndAssociationTargetOf(
	        				ref, 
	        				"subClassOf", 
	        				"Clinical Data Interchange Standards Consortium Terminology", 
	        				AlgorithmMatch.EXACT_MATCH, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + target2list.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start3));
	        
	        System.out.println("\nSource of C61410");
	        long start4 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator  source2list = (GraphNodeContentTrackingIterator) extension
	        		.getConceptReferencesForTextSearchAndAssociationSourceOf(
	        				ref, 
	        				"subClassOf", 
	        				"Clinical Data Interchange Standards Consortium Terminology", 
	        				AlgorithmMatch.EXACT_MATCH, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + source2list.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start4));
	        
	        System.out.println("\nTarget of Pharmacologic Substance");
	        long start5 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator  target5list = (GraphNodeContentTrackingIterator) extension.
	        		getConceptReferencesForTextSearchAndAssociationTargetOf(
	        				ref, 
	        				"subClassOf", 
	        				"Pharmacologic Substance", 
	        				AlgorithmMatch.EXACT_MATCH, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + target5list.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start5));
	        
	        System.out.println("\nSource of Pharmacologic Substance");
	        long start6 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator  source6list = (GraphNodeContentTrackingIterator) 
	        		extension
	        		.getConceptReferencesForTextSearchAndAssociationSourceOf(
	        				ref, 
	        				"subClassOf", 
	        				"Pharmacologic Substance", 
	        				AlgorithmMatch.EXACT_MATCH, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + source6list.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start6));
	        
	        System.out.println("\nSourceOf Contains Blood for all Associations");
	        long start7 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator  source7list = (GraphNodeContentTrackingIterator) extension
	        		.getConceptReferencesForTextSearchAndAssociationSourceOf(
	        				ref, 
	        				null, 
	        				"Blood", 
	        				AlgorithmMatch.CONTAINS, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + source7list.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start7));
	        
	        System.out.println("\nTargetof contains Blood for all Associations");
	        long start8 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator source8list = 
	        		(GraphNodeContentTrackingIterator) extension
			.getConceptReferencesForTextSearchAndAssociationTargetOf(
					ref, 
					null, 
					"Blood", 
					AlgorithmMatch.CONTAINS, 
					ModelMatch.NAME, 
					"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + source8list.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start8));
	        
	        System.out.println("\nSource of Exact Match Blood for all Associations");
	        long start9 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator source9list = (GraphNodeContentTrackingIterator) extension.
	        		getConceptReferencesForTextSearchAndAssociationSourceOf(
	        				ref, 
	        				null, 
	        				"Blood", 
	        				AlgorithmMatch.EXACT_MATCH, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + source9list.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start9));
	        
	        System.out.println("\nTargetOf Exact Match Blood for all Associations");
	        long start10 = System.currentTimeMillis();
	        GraphNodeContentTrackingIterator source10list = (GraphNodeContentTrackingIterator) extension
	        		.getConceptReferencesForTextSearchAndAssociationTargetOf(
	        				ref, 
	        				null, 
	        				"Blood", 
	        				AlgorithmMatch.EXACT_MATCH, 
	        				ModelMatch.NAME, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + source10list.getTotalCacheSize());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start10));
	        
	        System.out.println("\nTargetOf code for one association");
	        long start11 = System.currentTimeMillis();
	        List<ConceptReference> cfList = extension
	        		.getConceptReferenceListResolvedFromGraphForEntityCode(
	        				ref, 
	        				"Anatomic_Structure_Is_Physical_Part_Of", 
	        				Direction.TARGET_OF, 
	        				"C12434", 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + cfList.size());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start11));
	        
	        System.out.println("\nCandidate references for 'BLOOD'");
	        long start12 = System.currentTimeMillis();
	        List<ResolvedConceptReference> cfLis2 = extension
	        		.getCandidateConceptReferencesForTextAndAssociation(
	        				ref, 
	        				"Anatomic_Structure_Is_Physical_Part_Of", 
	        				"BLOOD", 
	        				AlgorithmMatch.LUCENE, 
	        				ModelMatch.PROPERTY, 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total entities returned: " + cfLis2.size());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start12));
	        
	        System.out.println("\nTargetOf code for one association");
	        long start13 = System.currentTimeMillis();
	        List<ConceptReference> cfListz = extension
	        		.getConceptReferenceListResolvedFromGraphForEntityCode(
	        				ref, 
	        				"subClassOf", 
	        				Direction.TARGET_OF, 
	        				"C1909", 
	        				"http://localhost:8080/graph-resolve");
	        System.out.println("Total vertexes returned: " + cfListz.size());
	        System.out.println("Millisecond return time: " + (System.currentTimeMillis() - start13));
	}
}

