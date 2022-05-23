
package org.lexevs.dao.database.ibatis.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.association.AssociationDataDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisAssociationDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
public class IbatisAssociationTargetDaoTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	/** The ibatis association dao. */
	@Resource
	private IbatisAssociationTargetDao ibatisAssociationTargetDao;

    @Test
    public void doGetSupportedLgSchemaVersions() {
    	List<LexGridSchemaVersion> versions = ibatisAssociationTargetDao.doGetSupportedLgSchemaVersions();
		assertNotNull("versions null", versions);
		assertTrue("versions empty",versions.size()>0);
		LexGridSchemaVersion lgsv = new LexGridSchemaVersion();
		lgsv.setMajorVersion(2);
		lgsv.setMinorVersion(0);
		assertTrue("versions missing value", versions.contains(lgsv));
    }

    @Test
    public void getVersionsDao() {
    	VersionsDao dao = ibatisAssociationTargetDao.getVersionsDao();
		assertNotNull("dao null",dao);
		assertTrue("dao incorrect object", dao instanceof VersionsDao);

    }


    @Test
    public void getAssociationTargetUId() {
    	String uid = ibatisAssociationTargetDao.getAssociationTargetUId("3", "instance001");
		assertNotNull("uid null",uid);
		assertEquals("uid wrong","162", uid);
    }


    @Test
    public void getLatestRevision() {
    	String revision = ibatisAssociationTargetDao.getLatestRevision("3", "A");
    	assertNotNull("revision null", revision);
    	assertEquals("revision wrong", "111", revision);
    }


    @Test
    public void entryStateExists() {
    	boolean exists = ibatisAssociationTargetDao.entryStateExists("3", "4");
    	assertTrue("exists is wrong", exists);
    }

    @Test
    public void getEntryStateUId() {
    	String uid = ibatisAssociationTargetDao.getEntryStateUId("3", "190");
		assertNotNull("uid null",uid);
		assertEquals("uid wrong", uid, "191");
    }

    @Test
    public void insertAssociationTarget() {
    }

    @Test
    public void testInsertAssociationTarget() {
    }

    @Test
    public void updateAssociationTarget() {
    }

    @Test
    public void testInsertAssociationTarget1() {
    }

    @Test
    public void doInsertAssociationTarget() {
    }

    @Test
    public void insertHistoryAssociationTarget() {
    }

    @Test
    public void deleteAssnTargetByUId() {
    }

    @Test
    public void updateVersionableChanges() {
    }

    @Test
    public void setVersionsDao() {
    }
    
    @Test
    public void deleteAssociationMultiAttribsByAssociationTargetUId() {
    }
    
    @Test
    public void getTripleByUid() {
    }

    @Test
    public void getHistoryTripleByRevision() {
    }

//    @Test
////	@Transactional
//	public void getTriple() throws SQLException {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//				"values ('1', '1', 'c-name')");
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into " +
//				"entityassnstoentity (" +
//				"entityAssnsGuid, " +
//				"associationPredicateGuid, " +
//				"sourceEntityCode, " +
//				"sourceEntityCodeNamespace, " +
//				"targetEntityCode, " +
//				"targetEntityCodeNamespace " +
//				") values " +
//				"('1', " +
//				"'1'," +
//				"'sc'," +
//				"'sns'," +
//				"'tc'," +
//				"'tns')");
//
//		template.execute("insert into " +
//				"entityassnstodata (" +
//				"entityAssnsDataGuid, " +
//				"associationPredicateGuid, " +
//				"sourceEntityCode, " +
//				"sourceEntityCodeNamespace) values " +
//				"('2', " +
//				"'1'," +
//				"'sc'," +
//				"'sns')");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'2'," +
//				"'qualName'," +
//				"'qualValue'," +
//				"'1' )");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'2', " +
//				"'1'," +
//				"'qualName'," +
//				"'qualValue'," +
//				"'2' )");
//
//
//
//		AssociationSource source = ibatisAssociationTargetDao.getTripleByUid("1", "1");
//		assertNotNull(source);
//
//		assertEquals("sc", source.getSourceEntityCode());
//		assertEquals("sns", source.getSourceEntityCodeNamespace());
//
//		assertEquals(1,source.getTargetCount());
//
//		AssociationTarget target = source.getTarget(0);
//
//		assertEquals("tc", target.getTargetEntityCode());
//		assertEquals("tns", target.getTargetEntityCodeNamespace());
//	}
}