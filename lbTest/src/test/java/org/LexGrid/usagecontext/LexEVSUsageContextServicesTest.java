
package org.LexGrid.usagecontext;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexgrid.usagecontext.LexEVSUsageContextServices;
import org.lexgrid.usagecontext.impl.LexEVSUsageContextServicesImpl;
import org.lexgrid.usagecontext.util.UsageContextConstants;

/**
 * @author m004181
 *
 */
public class LexEVSUsageContextServicesTest extends TestCase {

	private LexEVSUsageContextServices ucServ_;
	private LexBIGService lbServ_;
	
	@Test
	public void testGetUsageContextCodingScheme() throws LBException {
		CodingScheme cs = getUsageContextService().getUsageContextCodingScheme(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, Constructors.createCodingSchemeVersionOrTag(null, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION));
		assertTrue(cs.getCodingSchemeURI().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI));
		assertTrue(cs.getCodingSchemeName().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		assertTrue(cs.getRepresentsVersion().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION));		
	}

	@Test
	public void testGetUsageContextEntities() throws LBException {
		List<Entity> entities = getUsageContextService().listAllUsageContextEntities(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, Constructors.createCodingSchemeVersionOrTag(null, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION));
		
		assertTrue(entities.size() == 2);
		for (Entity entity : entities)
		{
			assertTrue(entity.getEntityCode().equals("uc001") || entity.getEntityCode().equals("Autos"));
		}
	}

	@Test
	public void testGetUsageContextIds() throws LBException {
		
		List<String> cdIds = getUsageContextService().listAllUsageContextIds(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, Constructors.createCodingSchemeVersionOrTag(null, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION));
		for (String id : cdIds)
		{
			assertTrue(id.equals("uc001") || id.equals("Autos"));
		}
	}

	@Test
	public void testRemoveUsageContextCodingSceheme() throws LBException{		
		LexBIGServiceManager lbsm = getLexBIGService().getServiceManager(null);
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI);
		acsvr.setCodingSchemeVersion(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		lbsm.deactivateCodingSchemeVersion(acsvr, new Date());
		lbsm.removeCodingSchemeVersion(acsvr);
	}
	
	private LexEVSUsageContextServices getUsageContextService(){
		if (ucServ_ == null)
			ucServ_ = LexEVSUsageContextServicesImpl.defaultInstance();
		
		return ucServ_;
	}
	
	private LexBIGService getLexBIGService(){
		if (lbServ_ == null)
			lbServ_ = LexBIGServiceImpl.defaultInstance();
		
		return lbServ_;
	}

}