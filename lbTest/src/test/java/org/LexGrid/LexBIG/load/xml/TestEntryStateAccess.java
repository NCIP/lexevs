
package org.LexGrid.LexBIG.load.xml;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationTargetDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.ibatis.codingscheme.IbatisCodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

public class TestEntryStateAccess extends TestCase {
	protected LexBIGService service;
	protected DatabaseServiceManager dbManager;
	protected CodingSchemeService csService;
	protected LexEvsServiceLocator locator;
	protected EntityDao entityDao;
	protected IbatisCodingSchemeDao csDao;
	protected AssociationTargetDao assDao;
	protected PropertyDao propsDao;
	protected DaoCallbackService daoCallbackService;
	protected EntryState es;
	public TestEntryStateAccess (String serverName) {
		super(serverName);
	}

	public void setUp() {
		ServiceHolder.configureForSingleConfig();
		service = ServiceHolder.instance().getLexBIGService();
		locator = LexEvsServiceLocator.getInstance();
		dbManager = locator.getDatabaseServiceManager();
		csService = dbManager.getCodingSchemeService();
		daoCallbackService = dbManager.getDaoCallbackService();
	}
	
	public void testEntryState(){
		assertTrue(getEntryStateForCodingScheme().getContainingRevision().equals("testRelease2010Feb_testData"));
	}
	
	private EntryState getEntryStateForCodingScheme(){
        return daoCallbackService.executeInDaoLayer(new DaoCallback<EntryState>() {

            public EntryState execute(DaoManager daoManager) {
              
                csDao = (IbatisCodingSchemeDao)daoManager.getCodingSchemeDao("urn:oid:22.22.0.2", "2.0");
                CodingScheme cs = csDao .getCodingSchemeByUriAndVersion("urn:oid:22.22.0.2", "2.0");
                return cs.getEntryState();
            }
        });
	}
}