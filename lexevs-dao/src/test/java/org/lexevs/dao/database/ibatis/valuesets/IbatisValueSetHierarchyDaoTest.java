package org.lexevs.dao.database.ibatis.valuesets;

import org.junit.Test;

import static org.junit.Assert.*;

public class IbatisValueSetHierarchyDaoTest {

    IbatisValueSetHierarchyDao hierarchyDao = new IbatisValueSetHierarchyDao();
    @Test
    public void getAllVSTriplesTrOfVSNode() {
        hierarchyDao.getAllVSTriplesTrOfVSNode("codingScheme", "code","assocGuid",
                "source","publishName", "canPublish",0, -1);
    }

    @Test
    public void getAllVSTriples() {
        hierarchyDao.getAllVSTriples("codingScheme", "assocGuid", "publishName",
                "canPublish", 0, -1);
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
        hierarchyDao.doGetSupportedLgSchemaVersions();
    }
}