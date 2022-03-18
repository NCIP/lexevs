
package org.lexevs.dao.database.ibatis.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.ibatis.codingscheme.IbatisCodingSchemeDao;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisEntityDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class IbatisEntityDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis entity dao. */
	@Autowired
	private IbatisEntityDao ibatisEntityDao;
	
	/** The ibatis coding scheme dao. */
	@Autowired
	private IbatisCodingSchemeDao ibatisCodingSchemeDao;
	
	/** The cs id. */
	private String csId;
	
	/**
	 * Insert coding scheme.
	 */
	@Before
	public void insertCodingScheme() {
	CodingScheme cs = new CodingScheme();
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		csId = ibatisCodingSchemeDao.insertCodingScheme(cs, null, true);
	}
	
	@Test
	public void testDefaultIsActiveEntity(){
		
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		
		this.ibatisEntityDao.insertEntity(csId, entity, false);
		
		Entity foundEntity = 
			this.ibatisEntityDao.getEntityByCodeAndNamespace(csId, "code", "namespace");
		
		assertTrue(foundEntity.getIsActive());
	}
	
	/**
	 * Insert entity.
	 */
	@Test
	public void insertEntity(){
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		entity.addEntityType("type");
		
		entity.setOwner("entity owner");
		
		entity.setStatus("testing");
		
		entity.setEffectiveDate(effectiveDate);
		entity.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.DEPENDENT);
		es.setRelativeOrder(23l);
		entity.setEntryState(es);
		
		final String id = ibatisEntityDao.insertEntity(csId, entity, true);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[])template.queryForObject("Select * from Entity", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String id = rs.getString(1);
				assertTrue(rs.getString(2).equals(csId));
				assertTrue(rs.getString(3).equals("code"));
				assertTrue(rs.getString(4).equals("namespace"));
				assertTrue(rs.getBoolean(5));
				assertTrue(rs.getBoolean(6));
				assertTrue(rs.getString(7).equals("a description"));
				assertEquals('0',rs.getString(8));
				assertTrue(rs.getString(9).equals("entity owner"));
				assertTrue(rs.getString(10).equals("testing"));
				assertTrue(rs.getTimestamp(11).equals(effectiveDate));
				assertTrue(rs.getTimestamp(12).equals(expirationDate));
				
				String entryStateId = rs.getString(13);
				
				String[] keys = new String[]{id, entryStateId};
				return keys;
			}
		});
		
		assertEquals(id,keys[0]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), keys[1]);
				assertEquals(rs.getString(2), keys[0]);
				assertEquals(rs.getString(3), "entity");
				assertEquals(rs.getString(4), ChangeType.DEPENDENT.toString());
				assertEquals(rs.getLong(5), 23l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
		
		template.queryForObject("Select * from EntityType", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), id);
				assertEquals(rs.getString(2), "type");
				
				return null;
			}
		});
	}
	
	@Test
	public void insertAssociationEntity(){
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		AssociationEntity entity = new AssociationEntity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		entity.setForwardName("aForwardName");
		entity.setReverseName("aReverseName");
		entity.setIsNavigable(true);
		entity.setIsTransitive(true);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		entity.addEntityType("type");
		
		entity.setOwner("entity owner");
		
		entity.setStatus("testing");
		
		entity.setEffectiveDate(effectiveDate);
		entity.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.DEPENDENT);
		es.setRelativeOrder(23l);
		entity.setEntryState(es);
		
		final String id = ibatisEntityDao.insertEntity(csId, entity, true);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[])template.queryForObject("Select * from Entity", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String id = rs.getString(1);
				assertTrue(rs.getString(2).equals(csId));
				assertTrue(rs.getString(3).equals("code"));
				assertTrue(rs.getString(4).equals("namespace"));
				assertTrue(rs.getBoolean(5) == true);
				assertTrue(rs.getBoolean(6) == true);
				assertTrue(rs.getString(7).equals("a description"));
				assertEquals("0",rs.getString(8));
				assertTrue(rs.getString(9).equals("entity owner"));
				assertTrue(rs.getString(10).equals("testing"));
				assertTrue(rs.getTimestamp(11).equals(effectiveDate));
				assertTrue(rs.getTimestamp(12).equals(expirationDate));
				String entryStateId = rs.getString(13);
				assertEquals("aForwardName", rs.getString(14));
				assertEquals("aReverseName", rs.getString(15));
				assertEquals(true, rs.getBoolean(16));
				assertEquals(true, rs.getBoolean(17));
				
				
				
				String[] keys = new String[]{id, entryStateId};
				return keys;
			}
		});
		
		assertEquals(id,keys[0]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), keys[1]);
				assertEquals(rs.getString(2), keys[0]);
				assertEquals(rs.getString(3), "entity");
				assertEquals(rs.getString(4), ChangeType.DEPENDENT.toString());
				assertEquals(rs.getLong(5), 23l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
		
		template.queryForObject("Select * from EntityType", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), id);
				assertEquals(rs.getString(2), "type");
				
				return null;
			}
		});
	}
	
	/**
	 * Insert entity.
	 */
	@Test
	public void insertHistoryEntity(){
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		
		String entityUId = ibatisEntityDao.insertEntity(csId, entity, false);
		
		ibatisEntityDao.insertHistoryEntity(csId, entityUId, entity);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(1, template.queryForInt("select count(*) from h_entity"));
	}
	
	/*
	
	@Test
	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntity() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");

		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
		
		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid3', 'rid3', NOW() )");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid1', 'eguid', 'entity', 'MODIFY', '0', 'rguid1')");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid3', 'csguid', 'codingScheme', 'MODIFY', '0', 'rguid3')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
		
		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid3", "eguid");
		
		assertEquals("rid2",revision);
	}
	
	@Test
	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntityEqual() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");

		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
		
		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid3', 'rid3', NOW() )");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid1', 'eguid', 'entity', 'MODIFY', '0', 'rguid1')");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid3', 'csguid', 'codingScheme', 'MODIFY', '0', 'rguid3')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
		
		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid2", "eguid");
		
		assertEquals("rid2",revision);
	}
	
	@Test
	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntityInHistory() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");

		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
		
		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid3', 'rid3', NOW() )");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid1', 'eguid', 'entity', 'NEW', '0', 'rguid1')");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid3', 'csguid', 'codingScheme', 'MODIFY', '0', 'rguid3')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid1')");
		
		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid1", "eguid");
		
		assertEquals("rid1",revision);
	}
	
	@Test
	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntityInHistoryWithDependentChange() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");

		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
		
		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid3', 'rid3', NOW() )");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid1', 'eguid', 'entity', 'NEW', '0', 'rguid1')");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid2', 'eguid', 'entity', 'DEPENDENT', '0', 'rguid2')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid3', 'csguid', 'codingScheme', 'MODIFY', '0', 'rguid3')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid1')");
		
		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid2", "eguid");
		
		assertEquals("rid1",revision);
	}
	
	@Test
	public void testGetPreviousRevisionIdFromGivenRevisionIdForEntityInHistoryWithDependentChangeAfter() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");

		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
		
		Thread.sleep(1000);
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid3', 'rid3', NOW() )");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid1', 'eguid', 'entity', 'NEW', '0', 'rguid1')");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('esguid3', 'csguid', 'codingScheme', 'DEPENDENT', '0', 'rguid3')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid1')");
		
		String revision = this.ibatisEntityDao.getPreviousRevisionIdFromGivenRevisionIdForEntity("", "rid3", "eguid");
		
		assertEquals("rid2",revision);
	}
	*/
	
	
	/**
	 * Insert entity.
	 */
	@Test
	@Transactional
	public void insertEntityWithException(){
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		
		ibatisEntityDao.insertEntity(csId, entity, true);
		
		try {
			ibatisEntityDao.insertEntity(csId, entity, true);
		} catch (Exception e) {
			assertTrue( e instanceof DataIntegrityViolationException );
		}
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(1, template.queryForInt("select count(*) from entity"));

	}
	
	/**
	 * Test get all entities of coding scheme.
	 */
	@Test
	@Transactional
	public void testGetAllEntitiesOfCodingScheme() {
		int limit = 1000;
		
		for(int i=0;i<limit;i++) {
			Entity entity = new Entity();
			entity.setEntityCode("code" + String.valueOf(i));
			entity.setEntityCodeNamespace("namespace");
			entity.setIsDefined(true);
			entity.setIsAnonymous(true);
			entity.setIsActive(false);
			
			this.ibatisEntityDao.insertEntity(csId, entity, true);
		}
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 0, -1);
		
		assertEquals(limit, entities.size());
		
	}
	
	/**
	 * Test get all entities of coding scheme with limit.
	 */
	@Test
	@Transactional
	public void testGetAllEntitiesOfCodingSchemeWithLimit() {
		int limit = 1000;
		
		for(int i=0;i<limit;i++) {
			Entity entity = new Entity();
			entity.setEntityCode("code" + String.valueOf(i));
			entity.setEntityCodeNamespace("namespace");
			entity.setIsDefined(true);
			entity.setIsAnonymous(true);
			entity.setIsActive(false);
			
			this.ibatisEntityDao.insertEntity(csId, entity, true);
		}
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 0, 10);
		
		assertEquals(10, entities.size());
	}
	
	/**
	 * Test get all entities of coding scheme with limit and start.
	 */
	@Test
	@Transactional
	public void testGetAllEntitiesOfCodingSchemeWithLimitAndStart() {
		int limit = 1000;
		
		for(int i=0;i<limit;i++) {
			Entity entity = new Entity();
			entity.setEntityCode("code" + String.valueOf(i));
			entity.setEntityCodeNamespace("namespace");
			entity.setIsDefined(true);
			entity.setIsAnonymous(true);
			entity.setIsActive(false);
			
			this.ibatisEntityDao.insertEntity(csId, entity, true);
		}
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 100, 100);
		
		assertEquals(100, entities.size());
	}
	
	/**
	 * Test lazy load presentations.
	 */
	@Test
	@Transactional
	public void testEntityPresentations() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme("1", 0, -1);
		
		assertEquals(1, entities.size());
		
		Entity entity = entities.get(0);
		
		Presentation pres = entity.getPresentation()[0];
		
		assertNotNull(pres);
	}
	
	/**
	 * Test lazy load presentations.
	 */
	@Test
	@Transactional
	public void testGetAllEntitiesByUids() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('2', '1', 'ecode2', 'ens2')");
		
		List<Entity> entities = ibatisEntityDao.getEntities("1", DaoUtility.createNonTypedList("1","2"));
		
		assertEquals(2, entities.size());
	}
	
	@Test
	public void testGetEntityUId() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
		
		String entityUid = ibatisEntityDao.getEntityUId("1", "ecode", "ens");
		
		assertEquals("1", entityUid);
	}
	
	/**
	 * Test lazy load presentations.
	 */
	@Test
	@Transactional
	public void testEntityDefinition() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('1', '1', 'entity', 'pid', 'pvalue', 'definition')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme("1", 0, -1);
		
		assertEquals(1, entities.size());
		
		Entity entity = entities.get(0);
		
		assertEquals(0, entity.getCommentCount());
		assertEquals(0, entity.getPropertyCount());
		assertEquals(0, entity.getPresentationCount());
		assertEquals(1, entity.getDefinitionCount());
	}
	
	/**
	 * Test entity count.
	 */
	@Test
	@Transactional
	public void testEntityCount() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('2', '1', 'ecode2', 'ens2')");
		
		int count = ibatisEntityDao.getEntityCount("1");
		
		assertEquals(2, count);
		
		int count2 = ibatisEntityDao.getEntityCount("9999");
		
		assertEquals(0, count2);
	}
	
	/**
	 * Test lazy load presentations.
	 */
	@Test
	@Transactional
	public void testGetHistoryEntity() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
		
		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation', '1')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
				"values ('1', '1', 'ecode', 'ens', '1')");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('1', 'rid1', NOW() )");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('1', '1', 'entity', 'NEW', '0', '1')");
		
		Entity entity = ibatisEntityDao.getHistoryEntityByRevision("1", "1", "rid1");
		
		assertNotNull(entity);
	}
	
	@Test
	@Transactional
	public void testGetHistoryEntityWithTwoInHistory() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
	
		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation', '1')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid, description) " +
				"values ('1', '1', 'ecode', 'ens', '1', 'd1')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid, description) " +
				"values ('1', '1', 'ecode', 'ens', '2', 'd2')");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('1', 'rid1', NOW() )");
		
		Thread.sleep(10);
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('2', 'rid2', NOW() )");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('1', '1', 'entity', 'NEW', '0', '1')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('2', '1', 'entity', 'MODIFY', '0', '2')");
		
		Entity entity = ibatisEntityDao.getHistoryEntityByRevision("1", "1", "rid2");
		
		assertNotNull(entity);
		
		assertEquals(entity.getEntityDescription().getContent(), "d2");
	}
	
	@Test
	@Transactional
	public void testGetAssociationEntity() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
	
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'association')");
			
		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("1", "ecode", "ens");
		
		assertNotNull(entity);
		
		assertTrue(entity instanceof AssociationEntity);	
	}
	
	@Test
	@Transactional
	public void testGetEntityWithTwoTypes() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'instance')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'concept')");
			
		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("1", "ecode", "ens");
		
		assertNotNull(entity);
		
		assertEquals(2, entity.getEntityTypeCount());
		
		assertTrue(Arrays.asList(entity.getEntityType()).contains("instance"));
		assertTrue(Arrays.asList(entity.getEntityType()).contains("concept"));	
	}
	
	@Test
	@Transactional
	public void testGetResolvedCodedNodeReferenceByCodeAndNamespaceCheckForTypes() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");

		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'concept')");
			
		ResolvedConceptReference entity = ibatisEntityDao.getResolvedCodedNodeReferenceByCodeAndNamespace("1", "ecode", "ens");
		
		assertNotNull(entity);
		
		assertEquals(1, entity.getEntityTypeCount());
		
		assertTrue(Arrays.asList(entity.getEntityType()).contains("concept"));	
	}
	
	@Test
	@Transactional
	public void testGetResolvedCodedNodeReferenceByCodeAndNamespaceCheckForTypesMultiple() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");

		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'concept')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('1', 'instance')");
			
		ResolvedConceptReference entity = ibatisEntityDao.getResolvedCodedNodeReferenceByCodeAndNamespace("1", "ecode", "ens");
		
		assertNotNull(entity);
		
		assertEquals(2, entity.getEntityTypeCount());
		
		assertTrue(Arrays.asList(entity.getEntityType()).contains("concept"));	
		assertTrue(Arrays.asList(entity.getEntityType()).contains("instance"));
	}
	
	@Test
	@Transactional
	public void testGetEntityWithEverything() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp timestamp1 = new Timestamp(1l);
		Timestamp timestamp2 = new Timestamp(2l);
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isDefined, isAnonymous, description, isActive, owner, status,       effectiveDate,                 expirationDate) " +
			"values 							('1',            '1',         'ecode',       'ens',            '0',        '1',         'ed',       '1',   'me',  'test', '"+ timestamp1.toString()+"', '" + timestamp2.toString()+ "')");
		
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, propertyId) " +
			"values ('1', '1', 'entity', 'pid1', 'pvalue', 'presentation', 'propId1')");
		
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, propertyId) " +
			"values ('2', '1', 'entity', 'pid2', 'pvalue', 'presentation', 'propId2')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'instance')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'concept')");
		
		template.execute("Insert into propertylinks " +
			"values ('1', '1', '1', 'propertyLink1', '2', null)");
		
		template.execute("Insert into propertylinks " +
			"values ('2', '1', '2', 'propertyLink2', '1', null)");
			
		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("1", "ecode", "ens");
		
		assertNotNull(entity);
		
		assertEquals(2, entity.getEntityTypeCount());
		
		assertTrue(Arrays.asList(entity.getEntityType()).contains("instance"));
		assertTrue(Arrays.asList(entity.getEntityType()).contains("concept"));	
		
		assertEquals("ecode", entity.getEntityCode());
		assertEquals("ens", entity.getEntityCodeNamespace());
		assertFalse(entity.getIsDefined());
		assertTrue(entity.getIsAnonymous());
		assertEquals("ed", entity.getEntityDescription().getContent());
		assertTrue(entity.getIsActive());
		assertEquals("me", entity.getOwner());
		assertEquals("test", entity.getStatus());
		assertEquals(timestamp1.getTime(), entity.getEffectiveDate().getTime());
		assertEquals(timestamp2.getTime(), entity.getExpirationDate().getTime());
		
		assertEquals(2, entity.getPropertyLinkCount());
		
		for(PropertyLink link : entity.getPropertyLink()) {
			if(link.getPropertyLink().equals("propertyLink1")) {
				assertEquals("propId1", link.getSourceProperty());
				assertEquals("propId2", link.getTargetProperty());
			} else if(link.getPropertyLink().equals("propertyLink2")) {
				assertEquals("propId2", link.getSourceProperty());
				assertEquals("propId1", link.getTargetProperty());
			} else {
				fail();
			}
		}
	}
	
	@Test
	@Transactional
	public void testGetEntityAssociationEntity() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'association')");
			
		Entity entity = ibatisEntityDao.getEntityByCodeAndNamespace("1", "ecode", "ens");
		
		assertNotNull(entity);
		
		assertEquals(1, entity.getEntityTypeCount());

		assertTrue(Arrays.asList(entity.getEntityType()).contains("association"));	
		
		assertTrue(entity instanceof AssociationEntity);
	}
	
	@Test
	@Transactional
	public void testUpdateEntityEntityDescription() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 'ecode', 'ens')");

		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'association')");
			
		Entity modifiedEntity = new Entity();
		modifiedEntity.setEntityCode("ecode");
		modifiedEntity.setEntityCodeNamespace("ens");
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("updated content");
		modifiedEntity.setEntityDescription(ed);
		
		ibatisEntityDao.updateEntity("1", "1", modifiedEntity);

		template.queryForObject("Select * from entity", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				assertTrue(rs.getString(7).equals("updated content"));
				
				return null;
				
			}
		});
	}
}