package org.lexevs.dao.database.ibatis.valuesets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:lexevsDao.xml"})
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class IbatisValueSetHierarchyDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

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