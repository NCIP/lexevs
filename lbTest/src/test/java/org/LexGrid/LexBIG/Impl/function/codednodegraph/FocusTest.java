
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;
import org.junit.Test;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.experimental.categories.Category;

/**
 * The Class FocusTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
@Category(IncludeForDistributedTests.class)
public class FocusTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testFocusWithJustCode() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("005",roots.getResolvedConceptReference(0).getCode());
    }
    
	@Test
    public void testFocusWithWrongCode() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("WRONG_CODE");
    	
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
    
	@Test
    public void testFocusWithCodeAndNamespace() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("005",roots.getResolvedConceptReference(0).getCode());
    }
    
	@Test
    public void testFocusWithCodeAndNonExistentNamespace() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Not_a_real_namespace");
    	
    	try {
			ResolvedConceptReferenceList roots = 
				this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
		} catch (LBParameterException e) {
			return;
		}
		fail();
    }
    
	@Test
    public void testFocusWithCodeAndWrongNamespace() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("GermanMadePartsNamespace");

    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);

    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
    
	@Test
    public void testFocusWithCodeAndCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodingSchemeName("Automobiles");

    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);

    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("005",roots.getResolvedConceptReference(0).getCode());
    }

	@Test
    public void testFocusWithCodeAndNamespaceAndCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodingSchemeName("Automobiles");
    	ref.setCodeNamespace("Automobiles");

    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);

    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("005",roots.getResolvedConceptReference(0).getCode());
    }
    
	@Test
    public void testFocusWithCodeAndNonExistentNamespaceAndCorrectCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Not_a_real_namespace");
    	ref.setCodingSchemeName("Automobiles");
    	
    	try {
			ResolvedConceptReferenceList roots = 
				this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
		} catch (LBParameterException e) {
			return;
		}
		fail();
    }
    
	@Test
    public void testFocusWithCodeAndCorrectNamespaceAndWrongCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("GermanMadeParts");

    	try {
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	} catch (LBParameterException e) {
			return;
		}
		fail();
    }
    
	@Test
    public void testFocusWithCodeAndCorrectNamespaceAndCorrectCodingSchemeWithUnion() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("E0001");
    	ref.setCodeNamespace("GermanMadePartsNamespace");
    	ref.setCodingSchemeName("GermanMadeParts");

    	CodedNodeGraph gmp = this.lbs.getNodeGraph(PARTS_SCHEME, null, null);
    	
    	CodedNodeGraph union = gmp.union(cng);
  
    	ResolvedConceptReferenceList roots = 
    		union.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("E0001",roots.getResolvedConceptReference(0).getCode());
    }
    
	@Test
    public void testFocusWithCodeAndCorrectNamespaceAndCorrectCodingSchemeWithUnionOtherCodingScheme() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("A0001");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("Automobiles");

    	CodedNodeGraph gmp = this.lbs.getNodeGraph(PARTS_SCHEME, null, null);
    	
    	CodedNodeGraph union = gmp.union(cng);
  
    	ResolvedConceptReferenceList roots = 
    		union.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	assertEquals("A0001",roots.getResolvedConceptReference(0).getCode());
    }
    
	@Test
    public void testFocusWithCodeAndCorrectNamespaceAndWrongCodingSchemeWithUnion() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("GermanMadeParts");

    	CodedNodeGraph gmp = this.lbs.getNodeGraph(PARTS_SCHEME, null, null);
    	
    	CodedNodeGraph union = gmp.union(cng);

    	ResolvedConceptReferenceList roots = 
    		union.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
    
	@Test
    public void testFocusWithCodeAndCorrectNamespaceAndWrongCodingSchemeWithIntersection() throws Exception {
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("GermanMadeParts");

    	CodedNodeGraph gmp = this.lbs.getNodeGraph(PARTS_SCHEME, null, null);
    	
    	CodedNodeGraph union = gmp.intersect(cng);

    	ResolvedConceptReferenceList roots = 
    		union.resolveAsList(ref, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
}