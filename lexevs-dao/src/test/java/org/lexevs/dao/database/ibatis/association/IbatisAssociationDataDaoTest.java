package org.lexevs.dao.database.ibatis.association;

import org.LexGrid.commonTypes.Text;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.versions.VersionsDao;
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

import java.util.List;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
public class IbatisAssociationDataDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource
	private IbatisAssociationDataDao ibatisAssociationDataDao;
	
    @Test
    public void doGetSupportedLgSchemaVersions() {
    	List<LexGridSchemaVersion> versions = ibatisAssociationDataDao.doGetSupportedLgSchemaVersions();
		assertNotNull("versions null", versions);
		assertTrue("versions empty",versions.size()>0);
		LexGridSchemaVersion lgsv = new LexGridSchemaVersion();
		lgsv.setMajorVersion(2);
		lgsv.setMinorVersion(0);
		assertTrue("versions missing value", versions.contains(lgsv));
    }



    @Test
    public void getVersionsDao() {
    	VersionsDao dao = ibatisAssociationDataDao.getVersionsDao();
		assertNotNull("dao null", dao);
		assertTrue("dao incorrect object", dao instanceof VersionsDao);
    }

    @Test
    public void getAssociationDataByUid() {
    	AssociationData data = ibatisAssociationDataDao.getAssociationDataByUid("20804209", "20804483");
    	assertNotNull("data null", data);
    	assertTrue("data correct object", data instanceof AssociationData);
    	Text text = data.getAssociationDataText();
    	text.getContent();
    }

    @Test
    public void getHistoryAssociationDataByRevision() {
    	//We don't have a previous revision of an association data object to generate this result
    	AssociationData data = ibatisAssociationDataDao.getHistoryAssociationDataByRevision("20804209", "20804483", "3291a00d-c674-49a5-8bc1-ce8c0b4092df");
    	assertNotNull("data null", data);
    	assertTrue("data correct object", data instanceof AssociationData);
    }

    @Test
    public void getAssociationDataUId() {
    	String uid = ibatisAssociationDataDao.getAssociationDataUId("20804209", "@_c5dc3cd8-4e7b-4c64-a870-f7c778570fd9");
    	assertNotNull("uid null", uid);
    	assertEquals("uid wrong",uid, "20804483");
    	
    }

    @Test
    public void getLatestRevision() {
    	String revision = ibatisAssociationDataDao.getLatestRevision("20804209", "20804483");
    	//No previous data object available 
    	assertNotNull("revision null", revision);
    	assertEquals("revision wrong", revision, "1.0");
    }

    @Test
    public void entryStateExists() {
    	boolean exists =ibatisAssociationDataDao.entryStateExists("3", "4");
    	assertTrue("exists true",exists);
    }

    @Test
    public void getEntryStateUId() {
    	String uid = ibatisAssociationDataDao.getEntryStateUId("20804209", "20804483");
    	assertNotNull("uid null", uid);
    	assertEquals("uid wrong",uid, "20804484");
    }
//    
//    @Test
//    public void insertAssociationData() {
//
//    	ibatisAssociationDataDao.insertAssociationData(null, null, null, null);
//    }
//
//    @Test
//    public void testInsertAssociationData() {
//    	AssociationSource source = new AssociationSource();
//    	ibatisAssociationDataDao.insertAssociationData(null, null, source, null, null);
//    }
//
//    @Test
//    public void testInsertAssociationData1() {
//    	ibatisAssociationDataDao.insertAssociationData(null, null, "test", null, null);
//    }
//    
//    @Test
//    public void setVersionsDao() {
//    	ibatisAssociationDataDao.setVersionsDao(null);
//    }
//    
//    @Test
//    public void insertHistoryAssociationData() {
//    	ibatisAssociationDataDao.insertHistoryAssociationData(null, null, null, null);
//    }
//
//    @Test
//    public void updateAssociationData() {
//    	ibatisAssociationDataDao.updateAssociationData(null, null, null);
//    }
//
//    @Test
//    public void deleteAllAssocQualsByAssocDataUId() {
//    	ibatisAssociationDataDao.deleteAllAssocQualsByAssocDataUId(null, null);
//    }
//
//    @Test
//    public void deleteAssociationData() {
//    	ibatisAssociationDataDao.deleteAssociationData(null, null);
//    }
//
//    @Test
//    public void updateVersionableChanges() {
//    	ibatisAssociationDataDao.updateVersionableChanges(null, null, null);
//    }
//    
//
//    @Test
//    public void doInsertAssociationData() {
//    	ibatisAssociationDataDao.doInsertAssociationData(null, null, null, null, null, null);
//    }
//
//
//    @Test
//    public void getTripleByUid() {
//    	ibatisAssociationDataDao.getTripleByUid(null, null);
//    }

}