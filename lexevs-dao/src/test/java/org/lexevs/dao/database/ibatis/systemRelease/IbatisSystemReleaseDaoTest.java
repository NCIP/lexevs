package org.lexevs.dao.database.ibatis.systemRelease;

import java.util.List;
import javax.annotation.Resource;
import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(propagation= Propagation.REQUIRED,readOnly=false)
public class IbatisSystemReleaseDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    IbatisSystemReleaseDao ibatisSystemReleaseDao;

    @Test
    public void doGetSupportedLgSchemaVersions() {
        List<LexGridSchemaVersion> versions = ibatisSystemReleaseDao.doGetSupportedLgSchemaVersions();
        assertNotNull("versions null", versions);
    }

    @Test
    public void getSystemReleaseUIdByUri() {
        String id = ibatisSystemReleaseDao.getSystemReleaseUIdByUri("urn:oid:2.16.840.1.113883.3.26.1.2:200611");
        assertNotNull("id null", id);
        assertEquals("id wrong", "20790222", id);
    }

    @Test
    public void getAllSystemRelease() {
        List<SystemRelease> allSystemReleases = ibatisSystemReleaseDao.getAllSystemRelease();
        assertNotNull("allSystemReleases null", allSystemReleases);
        assertFalse("releases empty", allSystemReleases.isEmpty());
        assertEquals("wrong size", 12, allSystemReleases.size());
    }

    @Test
    public void getSystemReleaseMetadataById() {
        SystemRelease systemRelease = ibatisSystemReleaseDao.getSystemReleaseMetadataById("200509");
        assertNotNull("systemRelease null", systemRelease);
        assertEquals("wrong systemRelease", "urn:oid:2.16.840.1.113883.3.26.1.2:200509",systemRelease.getReleaseURI());
    }

    @Test
    public void getSystemReleaseMetadataByUri() {
        SystemRelease metadataByUri = ibatisSystemReleaseDao.getSystemReleaseMetadataByUri("urn:oid:2.16.840.1.113883.3.26.1.2:200509");
        assertNotNull("metadataByUri null", metadataByUri);
        assertEquals("release wrong","200509", metadataByUri.getReleaseId());
    }

}