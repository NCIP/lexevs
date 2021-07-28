
package org.lexevs.dao.database.ibatis.valuesets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.Constructors;
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
        assertTrue(list.stream().anyMatch(x -> x.getEntityCode().equals("C99989")));
        assertTrue(list.stream().anyMatch(x -> x.getEntityCode().equals("C99988")));
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
        assertEquals("C99999",list.get(0).getEntityCode());
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
        System.out.println(list.get(0));
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
        list.stream().forEach(x -> System.out.println(x));
	}
	
	@Test
	public void testGetValueSetEntities() {
        List<Entity> list = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

			public List<Entity> execute(DaoManager daoManager) {
                savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 0, 1);
            }
        });
        
        assertNotNull(list);
        assertTrue(list.size() == 1);
        
        List<Entity> list2 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 1, 1);
             }
         });
        
        assertNotNull(list2);
        assertTrue(list2.size() == 1);
        
        List<Entity> list3 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 2, 1);
             }
         });
        
        assertNotNull(list3);
        assertTrue(list3.size() == 1);
        
        assertTrue(list.get(0) != list2.get(0));
        assertTrue(list.get(0) != list3.get(0));
        assertTrue(list2.get(0) != list3.get(0));
        
        List<Entity> list4 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 2, 1);
             }
         });
        
        assertNotNull(list4);
        assertTrue(list4.size() == 1);
        
        List<Entity> list5 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 0, 3);
             }
         });
        
        assertNotNull(list5);
        assertTrue(list5.size() == 3);
        
        
        List<Entity> list6 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 1, 3);
             }
         });
        
        assertNotNull(list6);
        assertTrue(list6.size() == 2);
        
        
        List<Entity> list7 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 2, 3);
             }
         });
        
        assertNotNull(list7);
        assertTrue(list7.size() == 1);
        
        List<Entity> list8 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 3, 3);
             }
         });
        
        assertNotNull(list8);
        assertTrue(list8.size() == 0);
        
        List<Entity> list9 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 3, 2);
             }
         });
        
        assertNotNull(list9);
        assertTrue(list9.size() == 0);
        
        
        List<Entity> list10 = daoCallbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

 			public List<Entity> execute(DaoManager daoManager) {
                 savsDao = (IbatisSourceAssertedValueSetDao)daoManager.getCurrentAssertedValueSetDao();
                 return savsDao.getPagedValueSetEntities("C54453",csUID, predicateUID, 0, 0);
             }
         });
        
        assertNotNull(list10);
        assertTrue(list10.size() == 0);
        
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