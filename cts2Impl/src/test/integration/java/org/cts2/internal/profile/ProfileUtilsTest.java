package org.cts2.internal.profile;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.cts2.test.BaseCts2IntegrationTest;
import org.junit.Test;


public class ProfileUtilsTest  extends BaseCts2IntegrationTest{
	
	@Resource
	private LexBIGServiceImpl lexBigServiceImpl;
	
	
	
	@Test
    @LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void getAllGraphsFromTheService() throws LBException{


			CodedNodeGraph graph = ProfileUtils.unionAllGraphs(lexBigServiceImpl);
			ResolvedConceptReferenceList list  = graph.resolveAsList(null, true, false, 100, -1, null, null, null, null, -1);
			ResolvedConceptReference[] array = list.getResolvedConceptReference();
			int count = 0;
			for(ResolvedConceptReference rcr: array){
				AssociatedConceptList concepts = rcr.getSourceOf().getAssociation(0).getAssociatedConcepts();
				AssociatedConcept[] conceptArray = concepts.getAssociatedConcept();
				for(AssociatedConcept concept: conceptArray)
				{count++;}
			}
			assertTrue(count > 0);		

	}
	

}
