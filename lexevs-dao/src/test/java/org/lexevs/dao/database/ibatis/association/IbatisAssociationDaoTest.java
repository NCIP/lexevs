
package org.lexevs.dao.database.ibatis.association;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.association.AssociationTargetDao;
import org.lexevs.dao.database.access.association.model.InstanceToGuid;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The Class IbatisAssociationDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
public class IbatisAssociationDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	/** The ibatis association dao. */
	@Resource
	private IbatisAssociationDao ibatisAssociationDao;

	@Test
	public void getRelationsContainerNameForAssociationInstanceId() {
		String containerName = ibatisAssociationDao.getRelationsContainerNameForAssociationInstanceId("3", "instance001");
		assertNotNull("containerName null",containerName);
		assertEquals("containerName incorrect", containerName,"relations");
		
	}

	@Test
	public void getHistoryRelationByRevisionId() {
		//TODO create revision?
		Relations relations = ibatisAssociationDao.getHistoryRelationByRevisionId("1003", "1021", null);
		assertNotNull("relations null",relations);
		assertTrue("relations has no values", relations.getAssociationPredicateCount()>0);
	}


	@Test
	public void getAnonDesignationForPredicate() {
		String description = ibatisAssociationDao.getAnonDesignationForPredicate("1003", "1023");
		assertNotNull("description null",description);
		assertEquals("description incorrect", "1", description);
	}

	@Test
	public void getAssociationPredicateUIdByContainerUId() {
		String predicateID =ibatisAssociationDao.getAssociationPredicateUIdByContainerUId("1003", "1021", "hasSubtype");
		assertNotNull("predicateID null", predicateID);
		assertEquals("predicateID incorrect","1023", predicateID);
	}

	@Test
	public void getAssociationPredicateUIdByContainerName() {
		String predicateUID = ibatisAssociationDao.getAssociationPredicateUIdByContainerName("3", "relations", "hasSubtype");
		assertNotNull("predicateUID null",predicateUID);
		assertTrue("predicateUID empty", predicateUID.length()>0);
	}

	@Test
	public void getAssociationPredicateUidsForAssociationName() {
		List<String> predicateUIDs= ibatisAssociationDao.getAssociationPredicateUidsForAssociationName("3", "relations", "hasSubtype");
		assertNotNull("predicateUID null", predicateUIDs);
		assertTrue("predicateUIDs empty", predicateUIDs.size()>0);
	}

	@Test
	public void getAssociationPredicateUidsForDirectionalName() {
		//TODO Deprected - not working? Remove?
		List<String> predicateUIDs =ibatisAssociationDao.getAssociationPredicateUidsForDirectionalName("3", "hasSubtype");
		assertNotNull("predicateUID null", predicateUIDs);
		assertTrue("predicateUIDs empty", predicateUIDs.size()>0);
	}

	@Test
	public void getRelationUId() {
		Relations relations = ibatisAssociationDao.getRelationsByUId("3", "155", true);
		assertNotNull("relations null",relations);
		assertTrue("relations has no values", relations.getAssociationPredicateCount()>0);
	}

	@Test
	public void getRelationEntryStateUId() {
		String stateID = ibatisAssociationDao.getRelationEntryStateUId("3", "155");
		assertNotNull("stateID null", stateID);
		assertTrue("stateID empty", stateID.length()>0);
		assertEquals("stateID wrong", stateID, "156");
		
	}

	@Test
	public void getAssociationPredicateUIdsForRelationsUId() {
		List<String> predicateUIDs = ibatisAssociationDao.getAssociationPredicateUIdsForRelationsUId("3", "155");
		assertNotNull("predicateUID null", predicateUIDs);
		assertTrue("predicateUIDs empty", predicateUIDs.size()>0);
	}

	@Test
	public void getRelationsUIdsForCodingSchemeUId() {
		List<String> relationsUIDs = ibatisAssociationDao.getRelationsUIdsForCodingSchemeUId("3");
		assertNotNull("relationsUIDs null", relationsUIDs);
		assertTrue("relationsUIDs empty", relationsUIDs.size()>0);
	}

	@Test
	public void getNodesPath() {
		String nodePath = ibatisAssociationDao.getNodesPath("3", "Ford", "Automobiles", "Jaguar", "Automobiles", "157");
		assertNotNull("nodePath null",nodePath);
		assertTrue("nodePath empty",nodePath.length()> 0);
	}

	@Test
	public void getRelationsNamesForCodingSchemeUId() {
		List<String> relationNames = ibatisAssociationDao.getRelationsNamesForCodingSchemeUId("3");
		assertNotNull("relationNames null", relationNames);
		assertTrue("relationNames empty", relationNames.size()>0);
		assertTrue("value missing from relationNames", relationNames.contains("relations"));
	}
	


	@Test
	public void getAssociationPredicateNameForUId() {
		String predicateName = ibatisAssociationDao.getAssociationPredicateNameForUId("3", "192");
		assertNotNull("predicateName null", predicateName);
		assertEquals("predicateName wrong", predicateName, "uses");
	}
	
	@Test
	public void getAssociationPredicateByUId() {
		AssociationPredicate predicate = ibatisAssociationDao.getAssociationPredicateByUId("3", "157");
		assertNotNull("predicate null", predicate);
		assertTrue("predicate wrong", predicate.getAssociationName().equals("hasSubtype"));
		
	}

	@Test
	public void getRelationsByUId() {
		Relations relations = ibatisAssociationDao.getRelationsByUId("1003", "1021", true);
		assertNotNull("relations null", relations);
		assertTrue("relations empty", relations.getAssociationPredicateCount()>0);
		assertEquals("relations incorrect name","relations", relations.getContainerName());
	}

	@Test
	public void getKeyForAssociationInstanceId() {
		String associationID = ibatisAssociationDao.getKeyForAssociationInstanceId("3", "instance001");
		assertNotNull("associationID null", associationID);
		assertEquals("associationID wrong", "162", associationID);
	}

	@Test
	public void getInstanceToGuidCache() {
		Map<String,String> instanceGuidMap =ibatisAssociationDao.getInstanceToGuidCache("3");
		assertNotNull("instanceGuidMap null", instanceGuidMap);
		assertTrue("instanceGuidMap empty", instanceGuidMap.size()>0);
		assertTrue("instanceGuidMap missing value", instanceGuidMap.containsValue("202"));
	}

	@Test
	public void doGetSupportedLgSchemaVersions() {
		List<LexGridSchemaVersion> versions = ibatisAssociationDao.doGetSupportedLgSchemaVersions();
		assertNotNull("versions null", versions);
		assertTrue("versions empty",versions.size()>0);
		LexGridSchemaVersion lgsv = new LexGridSchemaVersion();
		lgsv.setMajorVersion(2);
		lgsv.setMinorVersion(0);
		assertTrue("versions missing value", versions.contains(lgsv));
		
	}

	@Test
	public void getIbatisVersionsDao() {
		IbatisVersionsDao dao = ibatisAssociationDao.getIbatisVersionsDao();
		assertNotNull("dao null", dao);
		assertTrue("dao incorrect object", dao instanceof IbatisVersionsDao);
	}
	
	@Test
	public void getRelationLatestRevision() {
		String latestVersion = ibatisAssociationDao.getRelationLatestRevision("21566139", "1021");
		//TODO create revision?
		assertNotNull("latestVersion null", latestVersion);
		assertEquals("latestVersion incorrect", latestVersion,"1.0");
	}
	
	@Test
	public void getAllEntityAssocToEntityGuidsOfCodingScheme() {
		List<String> AssocID = this.ibatisAssociationDao.getAllEntityAssocToEntityGuidsOfCodingScheme("3", "157", 0, 10);
		assertNotNull("AssocID null",AssocID);
		assertTrue("AssocID set has no values",AssocID.size()>0);
		assertTrue("Missing value", AssocID.contains("168"));
	}

	@Test
	public void getGuidToInstanceMap() {
		List<InstanceToGuid> guidList = this.ibatisAssociationDao.getGuidToInstanceMap("3");
		assertNotNull("guidList null",guidList);
		assertTrue(guidList.size()>0);
	}
	

	@Test
	public void getAssociationTargetDao() {
		AssociationTargetDao dao = ibatisAssociationDao.getAssociationTargetDao();
		assertNotNull("dao null", dao);
		assertTrue("dao incorrect object", dao instanceof AssociationTargetDao);
	}

	@Test
	public void getAssociationDataDao() {
		AssociationDataDao dao = ibatisAssociationDao.getAssociationDataDao();
		assertNotNull("dao null",dao);
		assertTrue("dao incorrect object", dao instanceof AssociationDataDao);
	}

	@Test
	public void getPropertyDao() {
		PropertyDao propDao = ibatisAssociationDao.getPropertyDao();
		assertNotNull("propDao null",propDao);
		assertTrue ("propDao incorrect object", propDao instanceof PropertyDao);
	}


	@Test
	public void getAssociationPredicateNameForAssociationInstanceId() {
		String predicateName = ibatisAssociationDao.getAssociationPredicateNameForAssociationInstanceId("3", "instance001");
		assertNotNull("predicate name null", predicateName);
		assertEquals("predicate name wrong","hasSubtype",predicateName);
	}

	@Test
	public void deleteAssociationQualificationsByCodingSchemeUId() {
	}


	@Test
	public void insertRelations() {
	}

	@Test
	public void doInsertRelations() {
	}

	@Test
	public void insertAssociationEntity() {
	}

	@Test
	public void testInsertAssociationEntity() {
	}

	@Test
	public void updateAssociationEntity() {
	}

	@Test
	public void insertAssociationPredicate() {
	}

	@Test
	public void insertBatchAssociationSources() {
	}

	@Test
	public void insertBatchAssociationQualifiers() {
	}

	@Test
	public void insertAssociationSource() {
	}

	@Test
	public void testInsertBatchAssociationSources() {
	}

	@Test
	public void doInsertIntoTransitiveClosure() {
	}

	@Test
	public void insertIntoTransitiveClosure() {
	}

	@Test
	public void insertBatchTransitiveClosure() {
	}

	@Test
	public void testInsertAssociationSource1() {
	}

	@Test
	public void insertAssociationQualifier() {
	}


	@Test
	public void setIbatisVersionsDao() {
	}

	@Test
	public void insertHistoryRelation() {
	}

	@Test
	public void doInsertHistoryRelation() {
	}

	@Test
	public void updateRelation() {
	}

	@Test
	public void doUpdateRelation() {
	}

	@Test
	public void removeRelationByUId() {
	}

	@Test
	public void updateRelationVersionableChanges() {
	}

	@Test
	public void updateRelationEntryStateUId() {
	}

	@Test
	public void deleteAssociationQualificationsByRelationUId() {
	}

	@Test
	public void setPropertyDao() {
	}


	@Test
	public void setAssociationTargetDao() {
	}

	@Test
	public void setAssociationDataDao() {
	}

	@Test
	public void entryStateExists() {
	}



	/**
	 * Test get key for association instance id.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
//	@Test
//	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
//	public void testGetKeyForAssociationInstanceId() throws SQLException {
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//		template.execute("insert into entityassnstoentity"
//				+ " values ('1'," + " '1'," + " 's-code', "
//				+ " 's-ns'," + " 't-code'," + " 't-ns',"
//				+ " 'ai-id', null, null, null, null, null, null, null, null)");
//
//		String key = ibatisAssociationDao.getKeyForAssociationInstanceId(
//				"1", "ai-id");
//		assertEquals("1", key);
//	}
//
//	/**
//	 * Test insert association qualifier.
//	 * 
//	 * @throws SQLException
//	 *             the SQL exception
//	 */
//	@Test
//	@Transactional
//	public void testInsertAssociationQualifier() throws SQLException {
//		AssociationQualification qual = new AssociationQualification();
//		qual.setAssociationQualifier("qualName");
//		qual.setQualifierText(DaoUtility.createText("qual text"));
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//		template.execute("insert into entityassnstoentity"
//				+ " values ('1'," + " '1'," + " 's-code', "
//				+ " 's-ns'," + " 't-code'," + " 't-ns',"
//				+ " 'ai-id', null, null, null, null, null, null, null, null)");
//
//		ibatisAssociationDao.insertAssociationQualifier("1", "ai-id",
//				qual);
//
//		template.queryForObject("Select * from entityassnquals",
//				new RowMapper() {
//
//					public Object mapRow(ResultSet rs, int arg1)
//							throws SQLException {
//
//						assertNotNull(rs.getString(1));
//						assertEquals(rs.getString(2), "1");
//						assertEquals(rs.getString(3), "qualName");
//						assertEquals(rs.getString(4), "qual text");
//
//						return true;
//					}
//				});
//	}
//
//	@Test
//	@Transactional
//	public void getAllTriplesOfCodingSchemeLimit1() throws SQLException {
//		int limit = 1000;
//
//		AssociationQualification qual = new AssociationQualification();
//		qual.setAssociationQualifier("qualName");
//		qual.setQualifierText(DaoUtility.createText("qual text"));
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//
//		for (int i = 0; i < limit; i++) {
//			template
//					.execute("insert into entityassnstoentity"
//							+ " values ('1"
//							+ String.valueOf(i)
//							+ "',"
//							+ " '1',"
//							+ " 's"
//							+ String.valueOf(i)
//							+ "', "
//							+ " 's-ns',"
//							+ " 't"
//							+ String.valueOf(i)
//							+ "', "
//							+ " 't-ns',"
//							+ " 'ai-id', null, null, null, null, null, null, null, null)");
//		}
//
//		List<Triple> triples = ibatisAssociationDao
//				.getAllTriplesOfCodingScheme("1", "1", 0, 1);
//		assertEquals(1, triples.size());
//
//		Triple triple = triples.get(0);
//		assertEquals("s0", triple.getSourceEntityCode());
//		assertEquals("t0", triple.getTargetEntityCode());
//		assertEquals("s-ns", triple.getSourceEntityNamespace());
//		assertEquals("t-ns", triple.getTargetEntityNamespace());
//		assertEquals("1", triple.getAssociationPredicateId());
//	}
//
//	public void getAllTriplesOfCodingSchemeTestStart() throws SQLException {
//		int limit = 1000;
//
//		AssociationQualification qual = new AssociationQualification();
//		qual.setAssociationQualifier("qualName");
//		qual.setQualifierText(DaoUtility.createText("qual text"));
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid) values " + "('1', '1')");
//
//		for (int i = 0; i < limit; i++) {
//			template
//					.execute("insert into entityassnstoentity"
//							+ " values ('1"
//							+ String.valueOf(i)
//							+ "',"
//							+ " '1',"
//							+ " 's"
//							+ String.valueOf(i)
//							+ "', "
//							+ " 's-ns',"
//							+ " 't"
//							+ String.valueOf(i)
//							+ "', "
//							+ " 't-ns',"
//							+ " 'ai-id', null, null, null, null, null, null, null, null)");
//		}
//
//		List<Triple> triples = ibatisAssociationDao
//				.getAllTriplesOfCodingScheme("1", "1", 500, 1);
//		assertEquals(1, triples.size());
//
//		Triple triple = triples.get(0);
//		assertEquals("s499", triple.getSourceEntityCode());
//		assertEquals("t499", triple.getTargetEntityCode());
//		assertEquals("s-ns", triple.getSourceEntityNamespace());
//		assertEquals("t-ns", triple.getTargetEntityNamespace());
//		assertEquals("1", triple.getAssociationPredicateId());
//	}
//	
//	@Test
//	@Transactional
//	public void getAllGraphtTriplesPlusEntityOfCodingSchemeLimit1() throws SQLException {
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//
//		template.execute("insert into "
//				+ "associationpredicate values " + "('1', '1' , 'AssnsName')");
//
//		template.execute("insert into "
//				+ "entity (entityGuid, codingSchemeGuid, entityCode, " +
//				"entityCodeNamespace, isDefined, isAnonymous, description, isActive) " + 
//				"values ('1', '1' , 's499', 's-ns', null, null,'sourceDescription', '1')");
//		template.execute("insert into "
//				+ "entity (entityGuid,codingSchemeGuid,entityCode,entityCodeNamespace,isDefined, isAnonymous, description, isActive) " + 
//				"values ('2', '1' , 't499','t-ns',null, null,null, '1')");
//		template
//		.execute("insert into entityassnstoentity"
//				+ " values ('1499','1','s499', 's-ns','t499', 't-ns', 'ai-id', null, null, null, null, null, null, null, null)");
//		
//		
//        template.execute("Insert into entityassnquals(entityAssnQualsGuid, " +
//        		"referenceGuid, " +
//        		"qualifierName, " +
//        		"qualifierValue, " +
//        		"entryStateGuid)" +
//        		"values('1', '1499', 'qname', 'qvalue', '1')");
//		List<GraphDbTriple> triples = ibatisAssociationDao
//				.getAllGraphDbTriplesOfCodingScheme("1", "1", 0, 1);
//		assertEquals(1, triples.size());
//
//		GraphDbTriple triple = triples.get(0);
//		assertEquals("s499", triple.getSourceEntityCode());
//		assertEquals("t499", triple.getTargetEntityCode());
//		assertEquals("s-ns", triple.getSourceEntityNamespace());
//		assertEquals("t-ns", triple.getTargetEntityNamespace());
//		assertEquals("1", triple.getAssociationPredicateId());
//		assertEquals("1499", triple.getEntityAssnsGuid());
//		assertEquals("AssnsName", triple.getAssociationName());
//		assertEquals("ai-id", triple.getAssociationInstanceId());
//		assertEquals("sourceDescription", triple.getSourceDescription());
//		assertEquals("Description Missing",triple.getTargetDescription());
//	}
//	
//	@Test
//	@Transactional
//	public void getAllGraphtTriplesOfCodingSchemeTestStart() throws SQLException {
//		int limit = 1000;
//
//		AssociationQualification qual = new AssociationQualification();
//		qual.setAssociationQualifier("qualName");
//		qual.setQualifierText(DaoUtility.createText("qual text"));
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//
//		for (int i = 0; i < limit; i++) {
//			template
//					.execute("insert into entityassnstoentity"
//							+ " values ('1"
//							+ String.valueOf(i)
//							+ "',"
//							+ " '1',"
//							+ " 's"
//							+ String.valueOf(i)
//							+ "', "
//							+ " 's-ns',"
//							+ " 't"
//							+ String.valueOf(i)
//							+ "', "
//							+ " 't-ns',"
//							+ " 'ai-id', null, null, null, null, null, null, null, null)");
//		}
//
//		List<GraphDbTriple> triples = ibatisAssociationDao
//				.getAllGraphDbTriplesOfCodingScheme("1", "1", 0, 1);
//		assertEquals(1, triples.size());
//
//		GraphDbTriple triple = triples.get(0);
//		assertEquals("s0", triple.getSourceEntityCode());
//		assertEquals("t0", triple.getTargetEntityCode());
//		assertEquals("s-ns", triple.getSourceEntityNamespace());
//		assertEquals("t-ns", triple.getTargetEntityNamespace());
//		assertEquals("1", triple.getAssociationPredicateId());
//	}
//
//	
//	@Test
//	@Transactional
//	public void getAllTriplesTrAncestorsTest() throws SQLException {
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//		template.execute("insert into "
//				+ "entity (entityGuid, codingSchemeGuid, entityCode, " +
//				"entityCodeNamespace, isDefined, isAnonymous, description, isActive) " + 
//				"values ('1', '1' , 's0', 's-ns', null, null,'sourceDescription', '1')");
//		template.execute("insert into "
//				+ "entity (entityGuid, codingSchemeGuid, entityCode, " +
//				"entityCodeNamespace, isDefined, isAnonymous, description, isActive) " + 
//				"values ('2', '1' , 't0', 't-ns', null, null,'targetDescription', '1')");
//
//
//			template
//					.execute("insert into entityassnstoentitytr"
//							+ " values ('10', '1', 's0', 's-ns', 't0',  't-ns', 'path')");
//
//
//		List<GraphDbTriple> triples = ibatisAssociationDao
//				.getAllAncestorTriplesTrOfCodingScheme("1", "s0", "apname", 0, 1);
//		assertEquals(1, triples.size());
//
//		GraphDbTriple triple = triples.get(0);
//		assertEquals("s0", triple.getSourceEntityCode());
//		assertEquals("t0", triple.getTargetEntityCode());
//		assertEquals("s-ns", triple.getSourceEntityNamespace());
//		assertEquals("t-ns", triple.getTargetEntityNamespace());
//		assertEquals("1", triple.getAssociationPredicateId());
//	}
//	@Test
//	@Transactional
//	public void getAllTriplesTrDescendantsTest() throws SQLException {
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//		template.execute("insert into "
//				+ "entity (entityGuid, codingSchemeGuid, entityCode, " +
//				"entityCodeNamespace, isDefined, isAnonymous, description, isActive) " + 
//				"values ('1', '1' , 's0', 's-ns', null, null,'sourceDescription', '1')");
//		template.execute("insert into "
//				+ "entity (entityGuid, codingSchemeGuid, entityCode, " +
//				"entityCodeNamespace, isDefined, isAnonymous, description, isActive) " + 
//				"values ('2', '1' , 't0', 't-ns', null, null,'targetDescription', '1')");
//
//
//			template
//					.execute("insert into entityassnstoentitytr"
//							+ " values ('10', '1', 's0', 's-ns', 't0',  't-ns', 'path')");
//
//
//		List<GraphDbTriple> triples = ibatisAssociationDao
//				.getAllDescendantTriplesTrOfCodingScheme("1", "t0", "apname", 0, 1);
//		assertEquals(1, triples.size());
//
//		GraphDbTriple triple = triples.get(0);
//		assertEquals("s0", triple.getSourceEntityCode());
//		assertEquals("t0", triple.getTargetEntityCode());
//		assertEquals("s-ns", triple.getSourceEntityNamespace());
//		assertEquals("t-ns", triple.getTargetEntityNamespace());
//		assertEquals("1", triple.getAssociationPredicateId());
//	}
//	/**
//	 * Test insert relations.
//	 * 
//	 * @throws SQLException
//	 *             the SQL exception
//	 */
//	@Test
//	@Transactional
//	public void testInsertRelations() throws SQLException {
//
//		Relations relations = new Relations();
//		relations.setContainerName("container name");
//
//		EntityDescription ed = new EntityDescription();
//		ed.setContent("a description");
//		relations.setEntityDescription(ed);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		ibatisAssociationDao.insertRelations("1", relations, true);
//
//		template.queryForObject("Select * from relation", new RowMapper() {
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//
//				assertNotNull(rs.getString(1));
//				assertEquals(rs.getString(2), "1");
//				assertEquals(rs.getString(3), "container name");
//				assertEquals(rs.getString(10), "a description");
//
//				return true;
//			}
//		});
//	}
//
//	/**
//	 * Test insert association source.
//	 * 
//	 * @throws SQLException
//	 *             the SQL exception
//	 */
//	@Test
//	@Transactional
//	public void testInsertAssociationSource() throws SQLException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//
//		final Timestamp effectiveDate = new Timestamp(1l);
//		final Timestamp expirationDate = new Timestamp(2l);
//
//		AssociationSource source = new AssociationSource();
//		source.setSourceEntityCode("s-code");
//		source.setSourceEntityCodeNamespace("s-ns");
//
//		AssociationTarget target = new AssociationTarget();
//		target.setAssociationInstanceId("aii");
//		target.setTargetEntityCode("t-code");
//		target.setTargetEntityCodeNamespace("t-ns");
//		target.setIsDefining(true);
//		target.setIsInferred(false);
//		target.setIsActive(true);
//
//		AssociationQualification qual = new AssociationQualification();
//		qual.setAssociationQualifier("qualName");
//		qual.setQualifierText(DaoUtility.createText("qual value"));
//
//		target.addAssociationQualification(qual);
//		target.addUsageContext("usage Context");
//
//		source.addTarget(target);
//
//		target.setOwner("source owner");
//
//		target.setStatus("testing");
//
//		target.setEffectiveDate(effectiveDate);
//		target.setExpirationDate(expirationDate);
//
//		ibatisAssociationDao.insertAssociationSource("1", "1",
//				source);
//
//		template.queryForObject("Select * from entityassnstoentity",
//				new RowMapper() {
//
//					public Object mapRow(ResultSet rs, int arg1)
//							throws SQLException {
//
//						assertNotNull(rs.getString(1));
//						assertEquals(rs.getString(2), "1");
//						assertEquals(rs.getString(3), "s-code");
//						assertEquals(rs.getString(4), "s-ns");
//						assertEquals(rs.getString(5), "t-code");
//						assertEquals(rs.getString(6), "t-ns");
//						assertEquals(rs.getString(7), "aii");
//						assertEquals(rs.getBoolean(8), true);
//						assertEquals("0",rs.getString(9));
//						assertEquals("1",rs.getString(10));
//						assertEquals(rs.getString(11), "source owner");
//						assertEquals(rs.getString(12), "testing");
//						assertEquals(rs.getTimestamp(13), effectiveDate);
//						assertEquals(rs.getTimestamp(14), expirationDate);
//
//						return true;
//					}
//				});
//
//		assertEquals(new Integer(2), template.queryForObject(
//				"Select count(*) from entityassnquals", Integer.class));
//	}
//
//	@Test
//	@Transactional
//	public void testDeleteAllAssocQuals() throws SQLException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//
//		template.execute("insert into " + "entityassnstoentity ("
//				+ "entityAssnsGuid, " + "associationPredicateGuid, "
//				+ "sourceEntityCode, " + "sourceEntityCodeNamespace, "
//				+ "targetEntityCode, " + "targetEntityCodeNamespace "
//				+ ") values " + "('1', " + "'1'," + "'sc',"
//				+ "'sns'," + "'tc'," + "'tns')");
//
//		template.execute("insert into " + "entityassnstodata ("
//				+ "entityAssnsDataGuid, " + "associationPredicateGuid, "
//				+ "sourceEntityCode, " + "sourceEntityCodeNamespace) values "
//				+ "('1', " + "'1'," + "'sc'," + "'sns')");
//
//		template.execute("insert into " + "entityassnquals values ( "
//				+ "'1', " + "'1'," + "'qualName1',"
//				+ "'qualValue'," + "'1' )");
//
//		template.execute("insert into " + "entityassnquals values ( "
//				+ "'2', " + "'1'," + "'qualName2',"
//				+ "'qualValue'," + "'1' )");
//
//		assertEquals(2, template
//				.queryForObject("select count(*) from entityassnquals",Integer.class).intValue());
//
//		ibatisAssociationDao
//				.deleteAssociationQualificationsByCodingSchemeUId("1");
//
//		assertEquals(0, template
//				.queryForObject("select count(*) from entityassnquals",Integer.class).intValue());
//	}
//
//	@Test
//	@Transactional
//	public void testGetAssociationPredicateIdsForRelationsId()
//			throws SQLException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//
//		template
//				.execute("insert into "
//						+ "associationpredicate (associationPredicateGuid, relationGuid, associationName) values "
//						+ "('11', '1', 'apName1')");
//
//		template
//				.execute("insert into "
//						+ "associationpredicate (associationPredicateGuid, relationGuid, associationName) values "
//						+ "('12', '1', 'apName2')");
//
//		List<String> assocPredIds = ibatisAssociationDao
//				.getAssociationPredicateUIdsForRelationsUId("1",
//						"1");
//
//		assertEquals(2, assocPredIds.size());
//
//		assertTrue(assocPredIds.contains("11"));
//		assertTrue(assocPredIds.contains("12"));
//	}
//
//	@Test
//	@Transactional
//	public void testGetRelationsIdsForCodingSchemeId() throws SQLException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('11', '1', 'c-name1')");
//
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('12', '1', 'c-name2')");
//
//		List<String> relationsIds = ibatisAssociationDao
//				.getRelationsUIdsForCodingSchemeUId("1");
//
//		assertEquals(2, relationsIds.size());
//
//		assertTrue(relationsIds.contains("11"));
//		assertTrue(relationsIds.contains("12"));
//	}
//
//	/**
//	 * Test insert association predicate.
//	 * 
//	 * @throws SQLException
//	 *             the SQL exception
//	 */
//	@Test
//	@Transactional
//	public void testInsertAssociationPredicate() throws SQLException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//
//		AssociationPredicate predicate = new AssociationPredicate();
//		predicate.setAssociationName("assoc-name");
//
//		ibatisAssociationDao.insertAssociationPredicate("1", "1",
//				predicate, true);
//
//		template.queryForObject("Select * from associationpredicate",
//				new RowMapper() {
//
//					public Object mapRow(ResultSet rs, int arg1)
//							throws SQLException {
//
//						assertNotNull(rs.getString(1));
//						assertEquals(rs.getString(2), "1");
//						assertEquals(rs.getString(3), "assoc-name");
//
//						return true;
//					}
//				});
//	}
//
//	/**
//	 * Test insert transitive closure.
//	 * 
//	 * @throws SQLException
//	 *             the SQL exception
//	 */
//	@Test
//	@Transactional
//	public void testInsertTransitiveClosure() throws SQLException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template
//				.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//
//		ibatisAssociationDao.insertIntoTransitiveClosure("1", "1",
//				"sc", "sns", "tc", "tns", "path|,path->path|,path");
//
//		template.queryForObject("Select * from entityassnstoentitytr",
//				new RowMapper() {
//
//					public Object mapRow(ResultSet rs, int arg1)
//							throws SQLException {
//
//						assertNotNull(rs.getString(1));
//						assertEquals("1", rs.getString(2));
//						assertEquals("sc", rs.getString(3));
//						assertEquals("sns", rs.getString(4));
//						assertEquals("tc", rs.getString(5));
//						assertEquals("tns", rs.getString(6));
//						assertEquals("path|,path->path|,path", rs.getString(7));
//						return true;
//					}
//				});
//	}
//
//	@Test
//	public void testGetNodesPath() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'apname')");
//		template.execute("Insert into entityassnstoentitytr (entityAssnsTrGuid, associationPredicateGuid, sourceEntityCode, sourceEntityCodeNamespace, targetEntityCode, targetEntityCodeNamespace, path)"
//				+ "values ('1', '1', 'src-code', 'src-ns', 'tgt-code', 'tgt-ns', 'path')");
//		String path = ibatisAssociationDao.getNodesPath("1","src-code", "src-ns",
//				"tgt-code", "tgt-ns", "1");
//		assertEquals("path", path);
//		// AssociationPredicate id is null
//		path = ibatisAssociationDao.getNodesPath("1", "src-code", "src-ns",
//				"tgt-code", "tgt-ns", null);
//		assertEquals("path", path);
//	}
//	
//	@Test
//	public void testGetAnonymousDesignationForPredicateId(){
//		
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) "
//						+ "values ('1', 'csname', 'csuri', 'csversion')");
//		template.execute("insert into "
//				+ "relation (relationGuid, codingSchemeGuid, containerName) "
//				+ "values ('1', '1', 'c-name')");
//		template.execute("insert into "
//				+ "associationpredicate (associationPredicateGuid,"
//				+ "relationGuid, associationName) values "
//				+ "('1', '1', 'someRelation')");
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous, entryStateGuid) " +
//				"values ('eguid', '1', 'ecode', 'ens','0','esguid2')");
//		template.execute("Insert into csSupportedAttrib(csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnEntityCode)" +
//				"values ('1', '1', 'Association', 'someRelation', 'ecode')");
//		String anon = ibatisAssociationDao.getAnonDesignationForPredicate("1", "1");
//		assertEquals(anon, "0");
//	}
}