
package org.lexevs.dao.database.ibatis.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.relations.AssociationEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.codingscheme.IbatisCodingSchemeDao;
import org.lexevs.dao.database.ibatis.property.IbatisPropertyDao;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * The Class IbatisEntityDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
//@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(propagation= Propagation.REQUIRED,readOnly=false)
public class IbatisEntityDaoTest extends AbstractTransactionalJUnit4SpringContextTests  {

	/** The ibatis entity dao. */
	@Autowired
	private IbatisEntityDao ibatisEntityDao;
	
	/** The ibatis coding scheme dao. */
	@Autowired
	private IbatisCodingSchemeDao ibatisCodingSchemeDao;
	
	/** The cs id. */
	private String csId;

    @Test
    public void getEntityByCodeAndNamespace() {
		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("2003", "C100044", "ncit");
		assertNotNull("entity null",entity);
		assertTrue("entity count wrong", entity.getEntityTypeCount()==1);
		assertEquals("entity type wrong", entity.getEntityType(0),"concept");
    }


    @Test
    public void getEntities() {
		List<String> uids = new ArrayList<String>();
		uids.add("37");
		List<Entity> entities = ibatisEntityDao.getEntities("3", uids);
		assertTrue("entities empty", entities.size()>0);
		assertTrue("entities wrong size", entities.size()==1);
		assertTrue("entity missing", entities.get(0).getEntityDescription().getContent().equals("Domestic Auto Makers"));

    }

    @Test
    public void getEntitiesWithUidMap() {
		List<String> uids = new ArrayList<String>();
		uids.add("2183");
		uids.add("136278");
		List<String> propertyNames = new ArrayList<>();
		propertyNames.add("definition");
		propertyNames.add("textualPresentation");
		List<String> propertyTypes = new ArrayList<>();
		propertyTypes.add("definition");
		Map<String,Entity> entityMap = new HashMap<String,Entity>();
		Map<String,Entity> entityMap2 = new HashMap<String,Entity>();
		Map<String,Entity> entityMap3 = new HashMap<String,Entity>();
		Entity testEntity, testEntity2, testEntity3;
		
		entityMap = ibatisEntityDao.getEntitiesWithUidMap("2003", null, null, uids);
		assertTrue("entityMap empty", entityMap.size()>0);
		assertTrue("entityMap wrong size", entityMap.size()==2);
		testEntity = entityMap.get("136278");
		assertNotNull("testEntity null",testEntity);
		assertEquals("testEntity wrong", "C1036", testEntity.getEntityCode());


		entityMap2 = ibatisEntityDao.getEntitiesWithUidMap("2003", propertyNames, null, uids);
		assertTrue("entityMap2 empty", entityMap2.size()>0);
		//only change property list returned.  Size of map shouldn't change
		assertTrue("entityMap2 wrong size", entityMap2.size()==entityMap.size());
		testEntity2 = entityMap.get("136278");
		assertNotNull("testEntity2 null", testEntity2);
		assertEquals("testEntity2 wrong", "C1036", testEntity2.getEntityCode());

		
		entityMap3 = ibatisEntityDao.getEntitiesWithUidMap("2003", null, propertyTypes, uids);
		assertTrue("entityMap3 empty", entityMap3.size()>0);
		//only change property list returned.  Size of map shouldn't change
		assertTrue("entityMap2 wrong size", entityMap2.size()==entityMap.size());
		testEntity3 = entityMap3.get("136278");
		assertNotNull("testEntity3 null",testEntity3);

		
		entityMap3 = ibatisEntityDao.getEntitiesWithUidMap("2003", propertyNames, propertyTypes, uids);
		assertTrue("entityMap empty", entityMap2.size()>0);
		//only change property list returned.  Size of map shouldn't change
		assertTrue("entityMap2 wrong size", entityMap2.size()==entityMap.size());
		testEntity2 = entityMap.get("136278");
		assertNotNull("testEntity null", testEntity);
		
		
		entityMap = ibatisEntityDao.getEntitiesWithUidMap("3", null, null, null);
		assertTrue("uids required", entityMap.isEmpty());

		entityMap = ibatisEntityDao.getEntitiesWithUidMap("3",propertyNames,null,null);
		assertTrue("uids required", entityMap.isEmpty());
		
		entityMap = ibatisEntityDao.getEntitiesWithUidMap("3", null, propertyTypes, null);
		assertTrue("uids required", entityMap.isEmpty());




		try {
			ibatisEntityDao.getEntitiesWithUidMap(null, propertyNames, null, uids);
			fail("Should throw error");
		} catch(Exception ex){
			assertTrue("wrong exception",ex instanceof RuntimeException);
		}
    }

    @Test
    public void testGetEntities() {
		List<String> uids = new ArrayList<String>();
		uids.add("79");
		uids.add("103");
		List<String> propertyNames = new ArrayList<>();
		propertyNames.add("definition");
		propertyNames.add("textualPresentation");
		List<String> propertyTypes = new ArrayList<>();
		propertyTypes.add("definition");

		Entity testEntity, testEntity2;
		
		List<Entity> entities1 = ibatisEntityDao.getEntities("3", uids);
		assertTrue("entityMap empty", entities1.size()>0);
		assertTrue("entityMap wrong size", entities1.size()==2);
		testEntity = entities1.get(0);
		assertNotNull("testEntity null",testEntity);

		List<Entity> entities2 = ibatisEntityDao.getEntities("3", null, null, uids);
		assertNotNull("entity null", entities2);
		testEntity2 = entities2.get(0);
		assertTrue("entity wrong type", testEntity2 instanceof Entity);
		assertEquals("entity wrong", "C0001",testEntity2.getEntityCode());
		assertTrue("entity should match", testEntity2.equals(testEntity));

		entities2 = ibatisEntityDao.getEntities("3",propertyNames,propertyTypes,uids);
		assertNotNull("entity null", entities2);
		testEntity2 = entities2.get(0);
		assertTrue("entity wrong type", testEntity2 instanceof Entity);
		assertEquals("entity wrong", "C0001",testEntity2.getEntityCode());


		entities2 = ibatisEntityDao.getEntities("3", null, propertyTypes, uids);
		assertNotNull("entity null", entities2);
		testEntity2 = entities2.get(0);
		assertTrue("entity wrong type", testEntity2 instanceof Entity);
		assertEquals("entity wrong", "C0001",testEntity2.getEntityCode());


		entities2 = ibatisEntityDao.getEntities("3", propertyNames, null, uids);
		assertNotNull("entity null", entities2);
		testEntity2 = entities2.get(0);
		assertTrue("entity wrong type", testEntity2 instanceof Entity);
		assertEquals("entity wrong", "C0001",testEntity2.getEntityCode());


		entities2 = ibatisEntityDao.getEntities("3", propertyNames, null, null);
		assertNotNull("entity null", entities2);
		assertTrue("entities should be empty", entities2.isEmpty());



		entities2 = ibatisEntityDao.getEntities("3", null, propertyTypes, null);
		assertNotNull("entity null", entities2);
		assertTrue("entities should be empty", entities2.isEmpty());




		try {
		ibatisEntityDao.getEntities(null, propertyNames,null , uids);
		fail("Should throw error");
	} catch(Exception ex){
		assertTrue("wrong exception",ex instanceof RuntimeException);
	}
    }

    @Test
    public void getAssociationEntityByCodeAndNamespace() {
		AssociationEntity entity = ibatisEntityDao.getAssociationEntityByCodeAndNamespace("3", "AssocEntity","Automobiles");
		assertNotNull("entity null",entity);
		assertTrue("entity incorrect type", entity instanceof AssociationEntity);
		assertEquals("entity wrong", "An AssociationEntity", entity.getEntityDescription().getContent());

		entity = ibatisEntityDao.getAssociationEntityByCodeAndNamespace("3", "AssocEntity", null);
		assertNull("namespace required",entity);
    }

    @Test
    public void getResolvedCodedNodeReferenceByCodeAndNamespace() {
		ResolvedConceptReference conceptRef = ibatisEntityDao.getResolvedCodedNodeReferenceByCodeAndNamespace("3", "C0001", "Automobiles");
		assertNotNull("concept reference null", conceptRef);
		assertTrue("reference is wrong type", conceptRef instanceof ResolvedConceptReference);
		//This does not actually resolve.  It gets a skeleton object with no conceptRef within
//		assertNotNull("referenced entity null", conceptRef.getReferencedEntry());

		

		conceptRef = ibatisEntityDao.getResolvedCodedNodeReferenceByCodeAndNamespace("3", "C0001",null);
		assertNotNull("concept reference null", conceptRef);
		assertTrue("reference is wrong type", conceptRef instanceof ResolvedConceptReference);

		conceptRef = ibatisEntityDao.getResolvedCodedNodeReferenceByCodeAndNamespace("3", null, "Automobiles");
		assertNull("code required", conceptRef);
		
		try {
			conceptRef = ibatisEntityDao.getResolvedCodedNodeReferenceByCodeAndNamespace(null, "Ford", "Automobiles");
		fail("Should have thrown an error");
		}catch(RuntimeException ex) {
			assertTrue("Wrong error",ex instanceof RuntimeException);
		}

    }

    @Test
    public void getEntityByEntryStateUid() {
    	//TODO load or create a testable revision
		Entity entity = ibatisEntityDao.getEntityByEntryStateUid("3", "103", "2");
		assertNotNull("entity null", entity);
		assertTrue("entity wrong type", entity instanceof Entity);
		assertEquals("entity wrong", "Ford",entity.getEntityCode());
    }

    @Test
    public void getEntityByUId() {
		List<String> propertyNames = new ArrayList<>();
		propertyNames.add("definition");
		propertyNames.add("textualPresentation");
		List<String> propertyTypes = new ArrayList<>();
		propertyTypes.add("definition");
		
		Entity entity = ibatisEntityDao.getEntityByUId("3","79");
		assertNotNull("entity null 1", entity);
		assertTrue("entity wrong type 1", entity instanceof Entity);
		assertEquals("entity wrong 1", "C0001",entity.getEntityCode());

		Entity entity2 = ibatisEntityDao.getEntityByUId("3", "79", null, null);
		assertNotNull("entity null 2", entity2);
		assertTrue("entity wrong type 2", entity2 instanceof Entity);
		assertEquals("entity wrong 2", "C0001",entity2.getEntityCode());
		assertTrue("entity should match 2", entity.equals(entity2));

		entity = ibatisEntityDao.getEntityByUId("3", "79", propertyNames, null);
		assertNotNull("entity null 3", entity);
		assertTrue("entity wrong type 3", entity instanceof Entity);
		assertEquals("entity wrong 3", "C0001",entity.getEntityCode());

		

		entity2 = ibatisEntityDao.getEntityByUId("3", "79", null, propertyTypes);
		assertNotNull("entity null 4", entity2);
		assertTrue("entity wrong type 4", entity2 instanceof Entity);
		assertEquals("entity wrong 4", "C0001",entity2.getEntityCode());


		ibatisEntityDao.getEntityByUId("3", "79", propertyNames, propertyTypes);
		assertNotNull("entity null 5", entity2);
		assertTrue("entity wrong type 5", entity2 instanceof Entity);
		assertEquals("entity wrong 5", "C0001",entity2.getEntityCode());

		

		
		   entity = ibatisEntityDao.getEntityByUId("3", null, null, propertyTypes);
		   assertNull("Uid required", entity);


    }


    @Test
    public void getHistoryEntityByRevision() {
    	//TODO load or create a testable revision
		Entity entity = ibatisEntityDao.getHistoryEntityByRevision("3", "C0001",null);
		assertNull("entity should be null", entity);

		entity = ibatisEntityDao.getHistoryEntityByRevision("3", "C0001","1.0");
		assertNotNull("entity null", entity);
		assertEquals("entity wrong", "C0001", entity.getEntityCode());
    }

    @Test
    public void doGetPropertyLinks() {
		List<String> uids = new ArrayList<String>();
		uids.add("005");
		uids.add("Ford");
		List<PropertyLink> links = ibatisEntityDao.doGetPropertyLinks("lbaaab", "3", uids);
		assertNotNull("property links null", links);
		//TODO This may not return any results, ever
    }

    @Test
    public void getEntityCount() {
    	int count = ibatisEntityDao.getEntityCount("3");
    	assertEquals("Automobiles count wrong",24,count);

    	count = ibatisEntityDao.getEntityCount("2003");
    	assertEquals("ncit count wrong",203452, count);

    	try {
    		ibatisEntityDao.getEntityCount("10");
    		fail("Exception not thrown");
    	}
    	catch(RuntimeException ex) {
    		assertTrue("wrong exception thrown",ex instanceof RuntimeException);
    	}


    }

    @Test
    public void getAllEntitiesOfCodingScheme() {
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme("3", 0, 10);
		assertNotNull("entities null", entities);
		assertTrue("entities empty", entities.size()>0);
		assertTrue("should be Entity", entities.get(0) instanceof Entity);
		

		ibatisEntityDao.getAllEntitiesOfCodingScheme("3", 0, -1);
		assertNotNull("entities null", entities);
		assertTrue("entities empty", entities.size()>0);
		assertTrue("should be Entity", entities.get(0) instanceof Entity);

		ibatisEntityDao.getAllEntitiesOfCodingScheme("2003", 5, 50);
		assertNotNull("entities null", entities);
		assertTrue("entities empty", entities.size()>0);
		assertTrue("should be Entity", entities.get(0) instanceof Entity);
    }

    @Test
    public void getEntityUId() {
		String entityUid = ibatisEntityDao.getEntityUId("3", "Ford", "Automobiles");
		assertNotNull("entityUid null", entityUid);
		assertEquals("entityUid wrong", "103", entityUid);
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
		List<LexGridSchemaVersion> versions = ibatisEntityDao.doGetSupportedLgSchemaVersions();
		LexGridSchemaVersion vers = new LexGridSchemaVersion();
		vers.setMajorVersion(2);
		vers.setMinorVersion(0);
		assertNotNull("versions null", versions);
		assertTrue("version missing", versions.contains(vers));
    }

    @Test
    public void getIbatisVersionsDao() {
		IbatisVersionsDao versionsDao = ibatisEntityDao.getIbatisVersionsDao();
		assertNotNull("versionsDao null", versionsDao);
		assertTrue("versionsDao wrong object", versionsDao instanceof IbatisVersionsDao);
    }

    @Test
    public void getIbatisPropertyDao() {
		IbatisPropertyDao propertyDao = ibatisEntityDao.getIbatisPropertyDao();
		assertNotNull("propertyDao null", propertyDao);
		assertTrue("propertyDao wrong object", propertyDao instanceof IbatisPropertyDao);
    }

    @Test
    public void getIbatisAssociationDao() {
		IbatisAssociationDao assocDao = ibatisEntityDao.getIbatisAssociationDao();
		assertNotNull("assocDao null", assocDao);
		assertTrue("assocDao wrong object", assocDao instanceof IbatisAssociationDao);
    }

	@Test
	public void getIbatisCodingSchemeDao() {
		IbatisCodingSchemeDao csDao = ibatisEntityDao.getIbatisCodingSchemeDao();
		assertNotNull("csDao null", csDao);
		assertTrue("csDao wrong object", csDao instanceof IbatisCodingSchemeDao);
	}

    @Test
    public void getLatestRevision() {
    	//TODO enter a testable revision
		String revision = ibatisEntityDao.getLatestRevision("2003", "2003");
		assertNotNull("revision null", revision);
		assertEquals("revision wrong", "2.0", revision);
    }

    @Test
    public void entityInUse() {
		boolean entityInUse = ibatisEntityDao.entityInUse("3", "C0001","Automobiles");
		assertTrue("entityInUse false", entityInUse);

		entityInUse = ibatisEntityDao.entityInUse("2003", "C12663", "ncit");
		assertTrue("entityInUse false", entityInUse);
    }

    @Test
    public void getEntryStateUId() {
		String entryStateUid = ibatisEntityDao.getEntryStateUId("3","97");
		assertNotNull("entryStateUid null", entryStateUid);
		assertEquals("entryStateUid wrong", "98", entryStateUid);
    }

    @Test
    public void getDistinctEntityNamespacesFromCode() {
		List<String> namespaces = ibatisEntityDao.getDistinctEntityNamespacesFromCode("3", "Ford");
		assertNotNull("namespaces null", namespaces);
		assertTrue("namespaces empty", namespaces.size()>0);
		assertTrue("namespace missing", namespaces.contains("Automobiles"));
    }

    @Test
    public void getEntryState() {
    	//TODO load or create a testable revision
		String entryState = ibatisEntityDao.getEntryState("3", "C0001", "2");
		assertNotNull("entryState null", entryState);
		assertEquals("entryState wrong", "REPLACE", entryState);
		
    }

    @Test
    public void entryStateExists() {
		boolean entryStateExists = ibatisEntityDao.entryStateExists("3", "205");
		assertTrue("entryStateExists false", entryStateExists);
		
		entryStateExists = ibatisEntityDao.entryStateExists("3", "76");
		assertFalse("entryStateExists false", entryStateExists);
    }

    @Test
    public void getEntityDescription() {
		EntityDescription entityDescription =ibatisEntityDao.getEntityDescription("3", "C0001", "Automobiles");
		assertNotNull("entityDescription null", entityDescription);
		assertEquals("entityDescription wrong", "Car", entityDescription.getContent());
    }

    @Test
    public void getEntityDescriptionAsString() {
		String entityDescription = ibatisEntityDao.getEntityDescriptionAsString("3", "C0001", "Automobiles");
		assertNotNull("entityDescription null", entityDescription);
		assertEquals("entityDescription wrong", "Car", entityDescription);
    }
//
//    /**
//	 * Insert coding scheme.
//	 */
//	@Before
//	public void insertCodingScheme() {
//	CodingScheme cs = new CodingScheme();
//
//		cs.setCodingSchemeName("csName");
//		cs.setCodingSchemeURI("uri");
//		cs.setRepresentsVersion("1.2");
//		cs.setFormalName("csFormalName");
//		cs.setDefaultLanguage("lang");
//		cs.setApproxNumConcepts(22l);
//
//		csId = ibatisCodingSchemeDao.insertCodingScheme(cs, null, true);
//	}
//
//	@Test
//	public void testDefaultIsActiveEntity(){
//
//		Entity entity = new Entity();
//		entity.setEntityCode("code");
//		entity.setEntityCodeNamespace("namespace");
//
//		this.ibatisEntityDao.insertEntity(csId, entity, false);
//
//		Entity foundEntity =
//			this.ibatisEntityDao.getEntityByCodeAndNamespace(csId, "code", "namespace");
//
//		assertTrue(foundEntity.getIsActive());
//	}
//
//	/**
//	 * Insert entity.
//	 */
//	@Test
//	public void insertEntity(){
//		final Timestamp effectiveDate = new Timestamp(1l);
//		final Timestamp expirationDate = new Timestamp(2l);
//
//		Entity entity = new Entity();
//		entity.setEntityCode("code");
//		entity.setEntityCodeNamespace("namespace");
//		entity.setIsDefined(true);
//		entity.setIsAnonymous(true);
//		entity.setIsActive(false);
//
//		EntityDescription ed = new EntityDescription();
//		ed.setContent("a description");
//		entity.setEntityDescription(ed);
//		entity.addEntityType("type");
//
//		entity.setOwner("entity owner");
//
//		entity.setStatus("testing");
//
//		entity.setEffectiveDate(effectiveDate);
//		entity.setExpirationDate(expirationDate);
//
//		EntryState es = new EntryState();
//		es.setChangeType(ChangeType.DEPENDENT);
//		es.setRelativeOrder(23l);
//		entity.setEntryState(es);
//
//		final String id = ibatisEntityDao.insertEntity(csId, entity, true);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		final String[] keys = (String[])template.queryForObject("Select * from Entity", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				String id = rs.getString(1);
//				assertTrue(rs.getString(2).equals(csId));
//				assertTrue(rs.getString(3).equals("code"));
//				assertTrue(rs.getString(4).equals("namespace"));
//				assertTrue(rs.getBoolean(5));
//				assertTrue(rs.getBoolean(6));
//				assertTrue(rs.getString(7).equals("a description"));
//				assertEquals('0',rs.getString(8));
//				assertTrue(rs.getString(9).equals("entity owner"));
//				assertTrue(rs.getString(10).equals("testing"));
//				assertTrue(rs.getTimestamp(11).equals(effectiveDate));
//				assertTrue(rs.getTimestamp(12).equals(expirationDate));
//
//				String entryStateId = rs.getString(13);
//
//				String[] keys = new String[]{id, entryStateId};
//				return keys;
//			}
//		});
//
//		assertEquals(id,keys[0]);
//
//		template.queryForObject("Select * from EntryState", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertEquals(rs.getString(1), keys[1]);
//				assertEquals(rs.getString(2), keys[0]);
//				assertEquals(rs.getString(3), "entity");
//				assertEquals(rs.getString(4), ChangeType.DEPENDENT.toString());
//				assertEquals(rs.getLong(5), 23l);
//
//				//TODO: Test with a Revision GUID
//				//TODO: Test with a Previous Revision GUID
//				//TODO: Test with a Previous EntryState GUID
//
//				return null;
//			}
//		});
//
//		template.queryForObject("Select * from EntityType", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertEquals(rs.getString(1), id);
//				assertEquals(rs.getString(2), "type");
//
//				return null;
//			}
//		});
//	}
//
//	@Test
//	public void insertAssociationEntity(){
//		final Timestamp effectiveDate = new Timestamp(1l);
//		final Timestamp expirationDate = new Timestamp(2l);
//
//		AssociationEntity entity = new AssociationEntity();
//		entity.setEntityCode("code");
//		entity.setEntityCodeNamespace("namespace");
//		entity.setIsDefined(true);
//		entity.setIsAnonymous(true);
//		entity.setIsActive(false);
//
//		entity.setForwardName("aForwardName");
//		entity.setReverseName("aReverseName");
//		entity.setIsNavigable(true);
//		entity.setIsTransitive(true);
//
//		EntityDescription ed = new EntityDescription();
//		ed.setContent("a description");
//		entity.setEntityDescription(ed);
//		entity.addEntityType("type");
//
//		entity.setOwner("entity owner");
//
//		entity.setStatus("testing");
//
//		entity.setEffectiveDate(effectiveDate);
//		entity.setExpirationDate(expirationDate);
//
//		EntryState es = new EntryState();
//		es.setChangeType(ChangeType.DEPENDENT);
//		es.setRelativeOrder(23l);
//		entity.setEntryState(es);
//
//		final String id = ibatisEntityDao.insertEntity(csId, entity, true);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		final String[] keys = (String[])template.queryForObject("Select * from Entity", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				String id = rs.getString(1);
//				assertTrue(rs.getString(2).equals(csId));
//				assertTrue(rs.getString(3).equals("code"));
//				assertTrue(rs.getString(4).equals("namespace"));
//				assertTrue(rs.getBoolean(5) == true);
//				assertTrue(rs.getBoolean(6) == true);
//				assertTrue(rs.getString(7).equals("a description"));
//				assertEquals("0",rs.getString(8));
//				assertTrue(rs.getString(9).equals("entity owner"));
//				assertTrue(rs.getString(10).equals("testing"));
//				assertTrue(rs.getTimestamp(11).equals(effectiveDate));
//				assertTrue(rs.getTimestamp(12).equals(expirationDate));
//				String entryStateId = rs.getString(13);
//				assertEquals("aForwardName", rs.getString(14));
//				assertEquals("aReverseName", rs.getString(15));
//				assertEquals(true, rs.getBoolean(16));
//				assertEquals(true, rs.getBoolean(17));
//
//
//
//				String[] keys = new String[]{id, entryStateId};
//				return keys;
//			}
//		});
//
//		assertEquals(id,keys[0]);
//
//		template.queryForObject("Select * from EntryState", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertEquals(rs.getString(1), keys[1]);
//				assertEquals(rs.getString(2), keys[0]);
//				assertEquals(rs.getString(3), "entity");
//				assertEquals(rs.getString(4), ChangeType.DEPENDENT.toString());
//				assertEquals(rs.getLong(5), 23l);
//
//				//TODO: Test with a Revision GUID
//				//TODO: Test with a Previous Revision GUID
//				//TODO: Test with a Previous EntryState GUID
//
//				return null;
//			}
//		});
//
//		template.queryForObject("Select * from EntityType", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertEquals(rs.getString(1), id);
//				assertEquals(rs.getString(2), "type");
//
//				return null;
//			}
//		});
//	}
//
//	/**
//	 * Insert entity.
//	 */
//	@Test
//	public void insertHistoryEntity(){
//		Entity entity = new Entity();
//		entity.setEntityCode("code");
//		entity.setEntityCodeNamespace("namespace");
//		entity.setIsDefined(true);
//		entity.setIsAnonymous(true);
//		entity.setIsActive(false);
//
//		EntityDescription ed = new EntityDescription();
//		ed.setContent("a description");
//		entity.setEntityDescription(ed);
//
//		String entityUId = ibatisEntityDao.insertEntity(csId, entity, false);
//
//		ibatisEntityDao.insertHistoryEntity(csId, entityUId, entity);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		assertEquals(1, template.queryForObject("select count(*) from h_entity",Integer.class).intValue());
//	}
//
//	/*
//
//	@Test
//	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntity() throws InterruptedException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid1', 'rid1', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid2', 'rid2', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid3', 'rid3', NOW() )");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid1', 'eguid', 'entity', 'MODIFY', '0', 'rguid1')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid3', 'csguid', 'codingScheme', 'MODIFY', '0', 'rguid3')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('csguid', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
//
//		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid3", "eguid");
//
//		assertEquals("rid2",revision);
//	}
//
//	@Test
//	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntityEqual() throws InterruptedException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid1', 'rid1', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid2', 'rid2', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid3', 'rid3', NOW() )");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid1', 'eguid', 'entity', 'MODIFY', '0', 'rguid1')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid3', 'csguid', 'codingScheme', 'MODIFY', '0', 'rguid3')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('csguid', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
//
//		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid2", "eguid");
//
//		assertEquals("rid2",revision);
//	}
//
//	@Test
//	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntityInHistory() throws InterruptedException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid1', 'rid1', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid2', 'rid2', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid3', 'rid3', NOW() )");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid1', 'eguid', 'entity', 'NEW', '0', 'rguid1')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid3', 'csguid', 'codingScheme', 'MODIFY', '0', 'rguid3')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('csguid', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid1')");
//
//		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid1", "eguid");
//
//		assertEquals("rid1",revision);
//	}
//
//	@Test
//	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntityInHistoryWithDependentChange() throws InterruptedException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid1', 'rid1', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid2', 'rid2', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid3', 'rid3', NOW() )");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid1', 'eguid', 'entity', 'NEW', '0', 'rguid1')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid2', 'eguid', 'entity', 'DEPENDENT', '0', 'rguid2')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid3', 'csguid', 'codingScheme', 'MODIFY', '0', 'rguid3')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('csguid', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid1')");
//
//		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid2", "eguid");
//
//		assertEquals("rid1",revision);
//	}
//
//	@Test
//	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntityInHistoryWithDependentChangeAfter() throws InterruptedException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid1', 'rid1', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid2', 'rid2', NOW() )");
//
//		Thread.sleep(1000);
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//			"values ('rguid3', 'rid3', NOW() )");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid1', 'eguid', 'entity', 'NEW', '0', 'rguid1')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//			"values ('esguid3', 'csguid', 'codingScheme', 'DEPENDENT', '0', 'rguid3')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('csguid', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid1')");
//
//		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid3", "eguid");
//
//		assertEquals("rid2",revision);
//	}
//	*/
//
//
//	/**
//	 * Insert entity.
//	 */
//	@Test
//	@Transactional
//	public void insertEntityWithException(){
//		Entity entity = new Entity();
//		entity.setEntityCode("code");
//		entity.setEntityCodeNamespace("namespace");
//		entity.setIsDefined(true);
//		entity.setIsAnonymous(true);
//		entity.setIsActive(false);
//
//		EntityDescription ed = new EntityDescription();
//		ed.setContent("a description");
//		entity.setEntityDescription(ed);
//
//		ibatisEntityDao.insertEntity(csId, entity, true);
//
//		try {
//			ibatisEntityDao.insertEntity(csId, entity, true);
//		} catch (Exception e) {
//			assertTrue( e instanceof DataIntegrityViolationException );
//		}
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		assertEquals(1, template.queryForObject("select count(*) from entity",Integer.class).intValue());
//
//	}
//
//	/**
//	 * Test get all entities of coding scheme.
//	 */
//	@Test
//	@Transactional
//	public void testGetAllEntitiesOfCodingScheme() {
//		int limit = 1000;
//
//		for(int i=0;i<limit;i++) {
//			Entity entity = new Entity();
//			entity.setEntityCode("code" + String.valueOf(i));
//			entity.setEntityCodeNamespace("namespace");
//			entity.setIsDefined(true);
//			entity.setIsAnonymous(true);
//			entity.setIsActive(false);
//
//			this.ibatisEntityDao.insertEntity(csId, entity, true);
//		}
//
//		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 0, -1);
//
//		assertEquals(limit, entities.size());
//
//	}
//
//	/**
//	 * Test get all entities of coding scheme with limit.
//	 */
//	@Test
//	@Transactional
//	public void testGetAllEntitiesOfCodingSchemeWithLimit() {
//		int limit = 1000;
//
//		for(int i=0;i<limit;i++) {
//			Entity entity = new Entity();
//			entity.setEntityCode("code" + String.valueOf(i));
//			entity.setEntityCodeNamespace("namespace");
//			entity.setIsDefined(true);
//			entity.setIsAnonymous(true);
//			entity.setIsActive(false);
//
//			this.ibatisEntityDao.insertEntity(csId, entity, true);
//		}
//
//		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 0, 10);
//
//		assertEquals(10, entities.size());
//	}
//
//	/**
//	 * Test get all entities of coding scheme with limit and start.
//	 */
//	@Test
//	@Transactional
//	public void testGetAllEntitiesOfCodingSchemeWithLimitAndStart() {
//		int limit = 1000;
//
//		for(int i=0;i<limit;i++) {
//			Entity entity = new Entity();
//			entity.setEntityCode("code" + String.valueOf(i));
//			entity.setEntityCodeNamespace("namespace");
//			entity.setIsDefined(true);
//			entity.setIsAnonymous(true);
//			entity.setIsActive(false);
//
//			this.ibatisEntityDao.insertEntity(csId, entity, true);
//		}
//
//		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 100, 100);
//
//		assertEquals(100, entities.size());
//	}
//
//	/**
//	 * Test lazy load presentations.
//	 */
//	@Test
//	@Transactional
//	public void testEntityPresentations() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme("1", 0, -1);
//
//		assertEquals(1, entities.size());
//
//		Entity entity = entities.get(0);
//
//		Presentation pres = entity.getPresentation()[0];
//
//		assertNotNull(pres);
//	}
//
//	/**
//	 * Test lazy load presentations.
//	 */
//	@Test
//	@Transactional
//	public void testGetAllEntitiesByUids() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('2', '1', 'ecode2', 'ens2')");
//
//		List<Entity> entities = ibatisEntityDao.getEntities("1", DaoUtility.createNonTypedList("1","2"));
//
//		assertEquals(2, entities.size());
//	}
//
//	@Test
//	public void testGetEntityUId() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		String entityUid = ibatisEntityDao.getEntityUId("1", "ecode", "ens");
//
//		assertEquals("1", entityUid);
//	}
//
//	/**
//	 * Test lazy load presentations.
//	 */
//	@Test
//	@Transactional
//	public void testEntityDefinition() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'definition')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme("1", 0, -1);
//
//		assertEquals(1, entities.size());
//
//		Entity entity = entities.get(0);
//
//		assertEquals(0, entity.getCommentCount());
//		assertEquals(0, entity.getPropertyCount());
//		assertEquals(0, entity.getPresentationCount());
//		assertEquals(1, entity.getDefinitionCount());
//	}
//
//	/**
//	 * Test entity count.
//	 */
//	@Test
//	@Transactional
//	public void testEntityCount() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('2', '1', 'ecode2', 'ens2')");
//
//		int count = ibatisEntityDao.getEntityCount("1");
//
//		assertEquals(2, count);
//
//		int count2 = ibatisEntityDao.getEntityCount("9999");
//
//		assertEquals(0, count2);
//	}
//
//	/**
//	 * Test lazy load presentations.
//	 */
//	@Test
//	@Transactional
//	public void testGetHistoryEntity() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//			"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation', '1')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//				"values ('1', '1', 'ecode', 'ens', '1')");
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('1', 'rid1', NOW() )");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('1', '1', 'entity', 'NEW', '0', '1')");
//
//		Entity entity = ibatisEntityDao.getHistoryEntityByRevision("1", "1", "rid1");
//
//		assertNotNull(entity);
//	}
//
//	@Test
//	@Transactional
//	public void testGetHistoryEntityWithTwoInHistory() throws InterruptedException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//			"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation', '1')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid, description) " +
//				"values ('1', '1', 'ecode', 'ens', '1', 'd1')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid, description) " +
//				"values ('1', '1', 'ecode', 'ens', '2', 'd2')");
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('1', 'rid1', NOW() )");
//
//		Thread.sleep(10);
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('2', 'rid2', NOW() )");
//
//		template.execute("Insert into
//		entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('1', '1', 'entity', 'NEW', '0', '1')");
//
//		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('2', '1', 'entity', 'MODIFY', '0', '2')");
//
//		Entity entity = ibatisEntityDao.getHistoryEntityByRevision("1", "1", "rid2");
//
//		assertNotNull(entity);
//
//		assertEquals(entity.getEntityDescription().getContent(), "d2");
//	}
//
//	@Test
//	@Transactional
//	public void testGetAssociationEntity() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'association')");
//
//		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("1", "ecode", "ens");
//
//		assertNotNull(entity);
//
//		assertTrue(entity instanceof AssociationEntity);
//	}
//
//	@Test
//	@Transactional
//	public void testGetEntityWithTwoTypes() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'instance')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("1", "ecode", "ens");
//
//		assertNotNull(entity);
//
//		assertEquals(2, entity.getEntityTypeCount());
//
//		assertTrue(Arrays.asList(entity.getEntityType()).contains("instance"));
//		assertTrue(Arrays.asList(entity.getEntityType()).contains("concept"));
//	}
//
//	@Test
//	@Transactional
//	public void testGetResolvedCodedNodeReferenceByCodeAndNamespaceCheckForTypes() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		ResolvedConceptReference entity = ibatisEntityDao.getResolvedCodedNodeReferenceByCodeAndNamespace("1", "ecode", "ens");
//
//		assertNotNull(entity);
//
//		assertEquals(1, entity.getEntityTypeCount());
//
//		assertTrue(Arrays.asList(entity.getEntityType()).contains("concept"));
//	}
//
//	@Test
//	@Transactional
//	public void testGetResolvedCodedNodeReferenceByCodeAndNamespaceCheckForTypesMultiple() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//				"values ('1', 'instance')");
//
//		ResolvedConceptReference entity = ibatisEntityDao.getResolvedCodedNodeReferenceByCodeAndNamespace("1", "ecode", "ens");
//
//		assertNotNull(entity);
//
//		assertEquals(2, entity.getEntityTypeCount());
//
//		assertTrue(Arrays.asList(entity.getEntityType()).contains("concept"));
//		assertTrue(Arrays.asList(entity.getEntityType()).contains("instance"));
//	}
//
//	@Test
//	@Transactional
//	public void testGetEntityWithEverything() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		Timestamp timestamp1 = new Timestamp(1l);
//		Timestamp timestamp2 = new Timestamp(2l);
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isDefined, isAnonymous, description, isActive, owner, status,       effectiveDate,                 expirationDate) " +
//			"values 							('1',            '1',         'ecode',       'ens',            '0',        '1',         'ed',       '1',   'me',  'test', '"+ timestamp1.toString()+"', '" + timestamp2.toString()+ "')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, propertyId) " +
//			"values ('1', '1', 'entity', 'pid1', 'pvalue', 'presentation', 'propId1')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, propertyId) " +
//			"values ('2', '1', 'entity', 'pid2', 'pvalue', 'presentation', 'propId2')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'instance')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("Insert into propertylinks " +
//			"values ('1', '1', '1', 'propertyLink1', '2', null)");
//
//		template.execute("Insert into propertylinks " +
//			"values ('2', '1', '2', 'propertyLink2', '1', null)");
//
//		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("1", "ecode", "ens");
//
//		assertNotNull(entity);
//
//		assertEquals(2, entity.getEntityTypeCount());
//
//		assertTrue(Arrays.asList(entity.getEntityType()).contains("instance"));
//		assertTrue(Arrays.asList(entity.getEntityType()).contains("concept"));
//
//		assertEquals("ecode", entity.getEntityCode());
//		assertEquals("ens", entity.getEntityCodeNamespace());
//		assertFalse(entity.getIsDefined());
//		assertTrue(entity.getIsAnonymous());
//		assertEquals("ed", entity.getEntityDescription().getContent());
//		assertTrue(entity.getIsActive());
//		assertEquals("me", entity.getOwner());
//		assertEquals("test", entity.getStatus());
//		assertEquals(timestamp1.getTime(), entity.getEffectiveDate().getTime());
//		assertEquals(timestamp2.getTime(), entity.getExpirationDate().getTime());
//
//		assertEquals(2, entity.getPropertyLinkCount());
//
//		for(PropertyLink link : entity.getPropertyLink()) {
//			if(link.getPropertyLink().equals("propertyLink1")) {
//				assertEquals("propId1", link.getSourceProperty());
//				assertEquals("propId2", link.getTargetProperty());
//			} else if(link.getPropertyLink().equals("propertyLink2")) {
//				assertEquals("propId2", link.getSourceProperty());
//				assertEquals("propId1", link.getTargetProperty());
//			} else {
//				fail();
//			}
//		}
//	}
//
//	@Test
//	@Transactional
//	public void testGetEntityAssociationEntity() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'association')");
//
//		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("1", "ecode", "ens");
//
//		assertNotNull(entity);
//
//		assertEquals(1, entity.getEntityTypeCount());
//
//		assertTrue(Arrays.asList(entity.getEntityType()).contains("association"));
//
//		assertTrue(entity instanceof AssociationEntity);
//	}
//
//	@Test
//	@Transactional
//	public void testUpdateEntityEntityDescription() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'association')");
//
//		Entity modifiedEntity = new Entity();
//		modifiedEntity.setEntityCode("ecode");
//		modifiedEntity.setEntityCodeNamespace("ens");
//
//		EntityDescription ed = new EntityDescription();
//		ed.setContent("updated content");
//		modifiedEntity.setEntityDescription(ed);
//
//		ibatisEntityDao.updateEntity("1", "1", modifiedEntity);
//
//		template.queryForObject("Select * from entity", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//
//				assertTrue(rs.getString(7).equals("updated content"));
//
//				return null;
//
//			}
//		});
//	}
}