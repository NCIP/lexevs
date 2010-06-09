/**
 * 
 */
package org.LexGrid.conceptdomain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexgrid.conceptdomain.LexEVSConceptDomainServices;
import org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;


/**
 * @author m004181
 *
 */
public class LexEVSConceptDomainServicesTest {

	private LexEVSConceptDomainServices cdServ_;
	private LexBIGService lbServ_;
	
	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#insertConceptDomain(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.commonTypes.Properties)}.
	 * @throws LBException 
	 */
	@Test
	public void testInsertConceptDomainStringStringStringStringProperties() throws LBException {
		getConceptDomainService().insertConceptDomain("cd001", "cd1 name", null, "cd1 desc", "cd1 status", null);
	}

	
	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#getConceptDomainCodingScheme()}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainCodingScheme() throws LBException {
		CodingScheme cs = getConceptDomainService().getConceptDomainCodingScheme();
		assertTrue(cs.getCodingSchemeURI().equals(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI));
		assertTrue(cs.getCodingSchemeName().equals(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_FORMAL_NAME));
		assertTrue(cs.getRepresentsVersion().equals(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION));		
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#insertConceptDomain(org.LexGrid.concepts.Entity)}.
	 */
	@Test
	public void testInsertConceptDomainEntity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#listAllConceptDomainEntities()}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainEntities() throws LBException {
		List<Entity> entities = getConceptDomainService().listAllConceptDomainEntities();
		for (Entity entity : entities)
		{
			assertTrue(entity.getEntityCode().equals("cd001"));
			assertTrue(entity.getEntityDescription().getContent().equals("cd1 desc"));
			assertTrue(entity.getStatus().equals("cd1 status"));
		}
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#listAllConceptDomainIds()}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainIds() throws LBException {
		List<String> cdIds = getConceptDomainService().listAllConceptDomainIds();
		for (String id : cdIds)
		{
			assertTrue(id.equals("cd001"));
		}
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#getValueSetDefinitionURIsForConceptDomain(java.lang.String)}.
	 */
	@Test
	public void testGetValueSetDefinitionURIsForConceptDomain() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#removeConceptDomain(java.lang.String)}.
	 */
	@Test
	public void testRemoveConceptDomain() {
		fail("Not yet implemented");
	}
	
//	@Test
//	public void testRemoveConceptDomainCodingSceheme() throws LBException{
//		LexBIGServiceManager lbsm = getLexBIGService().getServiceManager(null);
//		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
//		acsvr.setCodingSchemeURN(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI);
//		acsvr.setCodingSchemeVersion(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION);
//		lbsm.deactivateCodingSchemeVersion(acsvr, new Date());
//		lbsm.removeCodingSchemeVersion(acsvr);
//	}
	
	private LexEVSConceptDomainServices getConceptDomainService(){
		if (cdServ_ == null)
			cdServ_ = LexEVSConceptDomainServicesImpl.defaultInstance();
		
		return cdServ_;
	}
	
	private LexBIGService getLexBIGService(){
		if (lbServ_ == null)
			lbServ_ = LexBIGServiceImpl.defaultInstance();
		
		return lbServ_;
	}

}
