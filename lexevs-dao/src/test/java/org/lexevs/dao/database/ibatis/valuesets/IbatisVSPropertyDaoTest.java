package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;
import javax.annotation.Resource;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:lexevsDao.xml"})
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class IbatisVSPropertyDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Resource
    IbatisVSPropertyDao propertyDao;

    @Test
    public void doGetPropertyMultiAttrib() {
        Class<Object> clz = (Class) Source.class;
        List<Object> multiAttrib = propertyDao.doGetPropertyMultiAttrib("21566959", clz);
        assertNotNull("multiAttrib null",multiAttrib);
        assertFalse("multiAttrib empty", multiAttrib.isEmpty());
        assertEquals("wrong size", 6, multiAttrib.size());

    }

    @Test
    public void getPropertyTypeString() {
        Property property = new Property();
        property.setPropertyId("1234");
        property.setPropertyType("property");
        String propertyTypeString = propertyDao.getPropertyTypeString(property);
        assertNotNull("propertyType null", propertyTypeString);
        assertEquals("property wrong", "property", propertyTypeString);
    }

    @Test
    public void getAllPropertiesOfParent() {
        List<Property> properties = propertyDao.getAllPropertiesOfParent("21566710",
                VSPropertyDao.ReferenceType.VALUESETDEFINITION);
        assertNotNull("properties null", properties);
        assertFalse("properties empty", properties.isEmpty());
        assertEquals("wrong size", 1, properties.size());
        assertEquals("wrong property", "textualPresentation", properties.get(0).getPropertyName());
    }

    @Test
    public void getAllHistoryPropertiesOfParentByRevisionGuid() {
        //TODO we have no revisions so this will always fail
        List<Property> properties =
                propertyDao.getAllHistoryPropertiesOfParentByRevisionGuid("21566710", "revisionGuid",
                VSPropertyDao.ReferenceType.DEFINITIONENTRY);
    }

    @Test
    public void getPropertyGuidFromParentGuidAndPropertyId() {
        String propertyId = propertyDao.getPropertyGuidFromParentGuidAndPropertyId(
                "21566710", "@_a7e829ea-986a-4454-8373-8595a0538386");
        assertNotNull("propertyId null", propertyId);
        assertEquals("wrong propertyId", "21566714", propertyId);
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
        List<LexGridSchemaVersion> versions = propertyDao.doGetSupportedLgSchemaVersions();
        assertNotNull("versions null", versions);
        assertFalse("versions empty", versions.isEmpty());
        assertEquals("version wrong", 2,versions.get(0).getMajorVersion());
    }

    @Test
    public void getIbatisVersionsDao() {
        IbatisVersionsDao versionsDao = propertyDao.getIbatisVersionsDao();
        assertNotNull("versionsDao null", versionsDao);
        assertTrue("versionsDao wrong", versionsDao instanceof IbatisVersionsDao);
    }

    @Test
    public void getVsEntryStateDao() {
        VSEntryStateDao vsEntryStateDao = propertyDao.getVsEntryStateDao();
        assertNotNull("dao null", vsEntryStateDao);
        assertTrue("dao wrong", vsEntryStateDao instanceof VSEntryStateDao);
    }

    @Test
    public void getLatestRevision() {
        // We have no revisions so this will always fail
         String latestRevision = propertyDao.getLatestRevision("21566714");
    }

    @Test
    public void resolveVSPropertyByRevision() {
        try {
            // We have no revisions so this will always fail
            Property property = propertyDao.resolveVSPropertyByRevision("21566714", "textualPresentation", "revisionId");
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPropertyByUId() {
        Property property = propertyDao.getPropertyByUId("21566477");
        assertNotNull("property null", property);
        assertEquals("property wrong", "textualPresentation", property.getPropertyName());
    }
}