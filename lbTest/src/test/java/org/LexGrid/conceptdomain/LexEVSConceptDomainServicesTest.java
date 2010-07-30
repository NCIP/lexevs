/**
 * 
 */
package org.LexGrid.conceptdomain;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexgrid.conceptdomain.LexEVSConceptDomainServices;
import org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;


/**
 * @author m004181
 *
 */
public class LexEVSConceptDomainServicesTest extends TestCase {

	private LexEVSConceptDomainServices cdServ_;
	private LexBIGService lbServ_;
	
	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#insertConceptDomain(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.commonTypes.Properties)}.
	 * @throws LBException 
	 */
	@Test
	public void testInsertConceptDomainStringStringStringStringProperties() throws LBException {
		CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTag("TEST", 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		getConceptDomainService().insertConceptDomain("cd001", "cd1 name", null, "cd1 desc", "cd1 status", true, null, csvt);
	}

	
	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#getConceptDomainCodingScheme()}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainCodingScheme() throws LBException {
		CodingScheme cs = getConceptDomainService().getConceptDomainCodingScheme(Constructors.createCodingSchemeVersionOrTag(null, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION));
		assertTrue(cs.getCodingSchemeURI().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI));
		assertTrue(cs.getCodingSchemeName().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		assertTrue(cs.getRepresentsVersion().equals(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION));		
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#insertConceptDomain(org.LexGrid.concepts.Entity)}.
	 * @throws LBException 
	 */
	@Test
	public void testInsertConceptDomainEntity() throws LBException {
		Entity cd = new Entity();
		cd.setEntityCode("Autos");
		cd.setEntityCodeNamespace(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		cd.setEntityType(new String[] {ConceptDomainConstants.CONCEPT_DOMAIN_ENTITY_TYPE});
		EntityDescription ed = new EntityDescription();
		ed.setContent("Autos");
		cd.setEntityDescription(ed);
		
		CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTag("TEST", 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		getConceptDomainService().insertConceptDomain(cd, csvt);
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#listAllConceptDomainEntities()}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainEntities() throws LBException {
		List<Entity> entities = getConceptDomainService().listAllConceptDomainEntities(Constructors.createCodingSchemeVersionOrTag(null, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION));
		
		assertTrue(entities.size() == 2);
		for (Entity entity : entities)
		{
			assertTrue(entity.getEntityCode().equals("cd001") || entity.getEntityCode().equals("Autos"));
		}
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#listAllConceptDomainIds()}.
	 * @throws LBException 
	 */
	@Test
	public void testGetConceptDomainIds() throws LBException {
		
		List<String> cdIds = getConceptDomainService().listAllConceptDomainIds(Constructors.createCodingSchemeVersionOrTag(null, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION));
		for (String id : cdIds)
		{
			assertTrue(id.equals("cd001") || id.equals("Autos"));
		}
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#getValueSetDefinitionURIsForConceptDomain(java.lang.String)}.
	 * @throws LBException 
	 */
	@Test
	public void testGetValueSetDefinitionURIsForConceptDomain() throws LBException {
		List<String> vsdURIS = getConceptDomainService().getConceptDomainBindings("Autos", null);
		for (String uri : vsdURIS)
		{
			System.out.println(uri);
		}
	}

	/**
	 * Test method for {@link org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl#removeConceptDomain(java.lang.String)}.
	 * @throws LBException 
	 */
	@Test
	public void testRemoveConceptDomain() throws LBException {
		CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTag("TEST", 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		List<String> cdIds = getConceptDomainService().listAllConceptDomainIds(csvt);
		assertTrue(cdIds.size() == 2);
		for (String id : cdIds)
		{
			assertTrue(id.equals("cd001") || id.equals("Autos"));
		}
		
		getConceptDomainService().removeConceptDomain("cd001", csvt);	
		cdIds = getConceptDomainService().listAllConceptDomainIds(csvt);
		assertTrue(cdIds.size() == 1);
		for (String id : cdIds)
		{
			assertTrue(id.equals("Autos"));
		}
	}
	
	@Test
	public void testRemoveConceptDomainCodingSceheme() throws LBException{		
		LexBIGServiceManager lbsm = getLexBIGService().getServiceManager(null);
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI);
		acsvr.setCodingSchemeVersion(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		lbsm.deactivateCodingSchemeVersion(acsvr, new Date());
		lbsm.removeCodingSchemeVersion(acsvr);
	}
	
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
