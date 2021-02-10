
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import java.util.Arrays;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.util.PrintUtility;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class UnionTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class UnionTest extends BaseCodedNodeGraphTest {

    /**
     * Test intersect.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testMultipleToNodeListUnionsWithPropertyRestriction() throws Exception {
    	CodedNodeSet cns1 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					Constructors.createConceptReference("A0001", AUTO_SCHEME), true, false, 0, -1);
		
		CodedNodeSet cns2 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					Constructors.createConceptReference("T0001", AUTO_SCHEME), true, false, 0, -1);
		
		CodedNodeSet cns3 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					Constructors.createConceptReference("C0001", AUTO_SCHEME), true, false, 0, -1);
		
		CodedNodeSet cns4 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					Constructors.createConceptReference("Ford", AUTO_SCHEME), true, false, 0, -1);
		
		CodedNodeSet cns5 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					null, true, false, -1, -1);
		
		cns5 = cns5.restrictToMatchingDesignations("General", SearchDesignationOption.ALL, "LuceneQuery", null);

		
		//should be GM, C0001, T0001, A0001, and Ford
		CodedNodeSet cns6 = cns1.union(cns2).union(cns3).union(cns4).union(cns5);
		
		PrintUtility.print(cns6);
		
		ResolvedConceptReferenceList list = cns6.resolveToList(null, null, null, -1);
		
		assertEquals(5,list.getResolvedConceptReferenceCount());
		
		DataTestUtils.isConceptReferencePresent(Arrays.asList(list.getResolvedConceptReference()), "GM");
		DataTestUtils.isConceptReferencePresent(Arrays.asList(list.getResolvedConceptReference()), "C0001");
		DataTestUtils.isConceptReferencePresent(Arrays.asList(list.getResolvedConceptReference()), "T0001");
		DataTestUtils.isConceptReferencePresent(Arrays.asList(list.getResolvedConceptReference()), "A0001");
		DataTestUtils.isConceptReferencePresent(Arrays.asList(list.getResolvedConceptReference()), "Ford");
    }
    
	@Test
    public void testMultipleToNodeListUnionsWithOverallPropertyRestriction() throws Exception {
    	CodedNodeSet cns1 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					Constructors.createConceptReference("A0001", AUTO_SCHEME), true, false, 0, -1);
		
		CodedNodeSet cns2 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					Constructors.createConceptReference("T0001", AUTO_SCHEME), true, false, 0, -1);
		
		CodedNodeSet cns3 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					Constructors.createConceptReference("C0001", AUTO_SCHEME), true, false, 0, -1);
		
		CodedNodeSet cns4 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					Constructors.createConceptReference("Ford", AUTO_SCHEME), true, false, 0, -1);
		
		CodedNodeSet cns5 = 
			this.lbs.getNodeGraph(AUTO_SCHEME, null, null).toNodeList(
					null, true, false, -1, -1);
		
		CodedNodeSet cns6 = cns1.union(cns2).union(cns3).union(cns4).union(cns5);
		
		cns6 = cns6.restrictToMatchingDesignations("General", SearchDesignationOption.ALL, "LuceneQuery", null);

		//should be just GM because of the restriction placed on the unioned set
		ResolvedConceptReferenceList list = cns6.resolveToList(null, null, null, -1);
		
		assertEquals(1,list.getResolvedConceptReferenceCount());
		
		DataTestUtils.isConceptReferencePresent(Arrays.asList(list.getResolvedConceptReference()), "GM");
    }
}