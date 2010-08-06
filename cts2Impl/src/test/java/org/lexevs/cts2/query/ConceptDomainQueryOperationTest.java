/**
 * 
 */
package org.lexevs.cts2.query;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;

/**
 * @author m004181
 *
 */
public class ConceptDomainQueryOperationTest {
	private static ConceptDomainQueryOperation CD_QUERY_OP;
	
	@BeforeClass
	public static void runBeforeClass(){
		CD_QUERY_OP = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getConceptDomainQueryOperation();
	}
	
	@AfterClass
	public static void runAfterClass(){
		CD_QUERY_OP = null;
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodingScheme(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void getConceptDomainCodingScheme() throws LBException {
		CodingScheme cs = CD_QUERY_OP.getConceptDomainCodingScheme(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cs.getCodingSchemeName().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		assertTrue(cs.getCodingSchemeURI().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI));
		assertTrue(cs.getFormalName().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntity(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void getConceptDomainEntity() throws LBException {
		Entity entity = CD_QUERY_OP.getConceptDomainEntity("ActCode", null, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(entity.getEntityCode().equalsIgnoreCase("ActCode"));
		assertTrue(entity.getEntityCodeNamespace().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntitisWithName(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)}.
	 * @throws LBException 
	 */
	@Test
	public void getConceptDomainEntitisWithName() throws LBException {
		List<Entity> entityList = CD_QUERY_OP.getConceptDomainEntitisWithName("BloodDonation", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, null, null, "subString", null);
		
		assertTrue(entityList.size() == 2);
		for (Entity entity : entityList)
		{
			assertTrue(entity.getEntityCode().equals("ActBloodDonationType")
					|| entity.getEntityCode().equals("ActBloodDonationSite"));
			assertTrue(entity.getEntityCodeNamespace().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodedNodeSet(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testGetConceptDomainCodedNodeSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#listAllConceptDomainEntities(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testListAllConceptDomainEntities() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#listAllConceptDomainIds(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void testListAllConceptDomainIds() throws LBException {
		
		List<String> cdIds = CD_QUERY_OP.listAllConceptDomainIds(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		System.out.println(cdIds.size());
		for(String cdId : cdIds)
		{
			System.out.println(cdId);
		}
		
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainBindings(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testGetConceptDomainBindings() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#isEntityInConceptDomain(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.util.List)}.
	 */
	@Test
	public void testIsEntityInConceptDomain() {
		fail("Not yet implemented");
	}

}
