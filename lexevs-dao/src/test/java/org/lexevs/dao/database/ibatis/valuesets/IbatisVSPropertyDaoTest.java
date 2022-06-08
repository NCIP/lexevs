package org.lexevs.dao.database.ibatis.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.junit.Test;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;

import static org.junit.Assert.*;

public class IbatisVSPropertyDaoTest {
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