package org.lexevs.dao.database.ibatis.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:lexevsDao.xml"})
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class IbatisVSPropertyDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
    IbatisVSPropertyDao propertyDao = new IbatisVSPropertyDao();

    @Test
    public void doGetPropertyMultiAttrib() {

        propertyDao.doGetPropertyMultiAttrib("propGuid", null);
    }

    @Test
    public void getPropertyTypeString() {
        propertyDao.getPropertyTypeString(new Property());
    }

    @Test
    public void getAllPropertiesOfParent() {
        propertyDao.getAllPropertiesOfParent("parentGuid", VSPropertyDao.ReferenceType.VALUESETDEFINITION);
    }

    @Test
    public void getAllHistoryPropertiesOfParentByRevisionGuid() {
        propertyDao.getAllHistoryPropertiesOfParentByRevisionGuid("parentGuid", "revisionGuid",
                VSPropertyDao.ReferenceType.DEFINITIONENTRY);
    }

    @Test
    public void getPropertyGuidFromParentGuidAndPropertyId() {
        propertyDao.getPropertyGuidFromParentGuidAndPropertyId("parentGuid", "propertyId");
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
        propertyDao.doGetSupportedLgSchemaVersions();
    }

    @Test
    public void getIbatisVersionsDao() {
        propertyDao.getIbatisVersionsDao();
    }

    @Test
    public void getVsEntryStateDao() {
        propertyDao.getVsEntryStateDao();
    }

    @Test
    public void getLatestRevision() {
        propertyDao.getLatestRevision("propertyUid");
    }

    @Test
    public void resolveVSPropertyByRevision() {
        try {
            propertyDao.resolveVSPropertyByRevision("parentGuid", "propertyId", "revisionId");
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPropertyByUId() {
        propertyDao.getPropertyByUId("vsPropertyUid");
    }
}