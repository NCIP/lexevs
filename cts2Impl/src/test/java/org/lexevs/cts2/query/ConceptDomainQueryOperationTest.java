/**
 * 
 */
package org.lexevs.cts2.query;

import static org.junit.Assert.fail;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;

/**
 * @author m004181
 *
 */
public class ConceptDomainQueryOperationTest {

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodingScheme(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testGetConceptDomainCodingScheme() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntity(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 */
	@Test
	public void testGetConceptDomainEntity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntitisWithName(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetConceptDomainEntitisWithName() {
		fail("Not yet implemented");
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
		ConceptDomainQueryOperation cdqueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getConceptDomainQueryOperation();
		
		List<String> cdIds = cdqueryop.listAllConceptDomainIds(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
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
