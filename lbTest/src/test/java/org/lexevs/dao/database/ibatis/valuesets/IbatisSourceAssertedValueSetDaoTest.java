
package org.lexevs.dao.database.ibatis.valuesets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetServiceImpl;
import org.lexevs.locator.LexEvsServiceLocator;

public class IbatisSourceAssertedValueSetDaoTest {

	private DaoCallbackService daoCallbackService;
    private IbatisSourceAssertedValueSetDao savsDao;
	private String matchCode;
	protected String assertedRelation;
	protected String predicateUID;
	protected String csUID;
	private AssertedValueSetServiceImpl svc;

	@Before
	public void setUp() throws Exception {
		daoCallbackService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getDaoCallbackService();
		AssertedValueSetParameters params = new AssertedValueSetParameters.Builder("0.1.5").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl")
				.build();
		svc = (AssertedValueSetServiceImpl)LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		svc.init(params);
		csUID = svc.getCsUid();
		predicateUID = svc.getPredUid(csUID);
		matchCode = "C99999";
	}

	@Test
	public void testGetSourceAssertedValueSetEntitiesForEntityCode() {
        List<Entity> list = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

			public List<Entity> execute(DaoManager daoManager) {
                savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                return savsDao.getSourceAssertedValueSetEntitiesForEntityCode(matchCode, assertedRelation, predicateUID, csUID);
            }
        });
        
        assertNotNull(list);
        assertTrue(list.size() > 0);
	}
	
	@Test
	public void testGetSourceAssertedValueSetTopNodeForEntityCode() {
        List<Entity> list = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

			public List<Entity> execute(DaoManager daoManager) {
                savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                return savsDao.getSourceAssertedValueSetTopNodeForEntityCode(matchCode, csUID);
            }
        });
        
        assertNotNull(list);
        assertTrue(list.size() > 0);
	}
	
	@Test
	public void testGetValueSetEntityUidForTopNodeEntityCode() {
        List<String> list = daoCallbackService.executeInDaoLayer(new DaoCallback<List<String>>() {

			public List<String> execute(DaoManager daoManager) {
                savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                return savsDao.getValueSetEntityUidForTopNodeEntityCode(csUID, predicateUID, matchCode, 0, 10);
            }
        });
        
        assertNotNull(list);
        assertTrue(list.size() > 0);
	}
	
	@Test
	public void testGetValueSetEntityUids() {
        List<String> list = daoCallbackService.executeInDaoLayer(new DaoCallback<List<String>>() {

			public List<String> execute(DaoManager daoManager) {
                savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                return savsDao.getValueSetEntityUids(csUID, predicateUID, 0, 10);
            }
        });
        
        assertNotNull(list);
        assertTrue(list.size() > 0);
	}
	
	@Test
	public void testGetValueSetEntities() {
        List<Entity> list = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

			public List<Entity> execute(DaoManager daoManager) {
                savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                return savsDao.getPagedValueSetEntities(matchCode,csUID, predicateUID, 0, 10);
            }
        });
        
        assertNotNull(list);
        assertTrue(list.size() > 0);
	}
	
	@Test
	public void testGetValueSetEntityCount() {
        Integer count = daoCallbackService.executeInDaoLayer(new DaoCallback<Integer>() {

			public Integer execute(DaoManager daoManager) {
                savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                return savsDao.getValueSetEntityCount(matchCode, csUID, predicateUID);
            }
        });
        
        assertNotNull(count);
        assertTrue(count.intValue() == 2);
	}

}