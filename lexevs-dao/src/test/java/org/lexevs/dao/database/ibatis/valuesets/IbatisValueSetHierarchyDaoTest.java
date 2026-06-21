package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.association.model.DefinedNode;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
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

    @Resource
    IbatisValueSetHierarchyDao hierarchyDao;
    @Test
    public void getAllVSTriplesTrOfVSNode() {
        List<VSHierarchyNode> nodes=hierarchyDao.getAllVSTriplesTrOfVSNode("2003", "C100110","2062",
                "Contributing_Source","Publish_Value_Set", "Yes",0, 20);
        assertNotNull("nodes null", nodes);
        assertFalse("nodes empty", nodes.isEmpty());
        assertEquals("node missing", 20, nodes.size());
//TODO I am concerned that entityCode and namespace are not being returned
    }

    @Test
    public void getAllVSTriples() {
        List<DefinedNode> allVSTriples = hierarchyDao.getAllVSTriples("2003", "2062","Publish_Value_Set" ,"Yes"
                , 0, 20);
        assertNotNull("triples null", allVSTriples);
        assertFalse("triples empty", allVSTriples.isEmpty());
        assertEquals("triples wrong size", 20, allVSTriples.size());
        DefinedNode node = allVSTriples.get(0);
        assertEquals("triple wrong", "ncit", node.getEntityCodeNamespace());
    }


    @Test
    public void doGetSupportedLgSchemaVersions() {
        List<LexGridSchemaVersion> versions = hierarchyDao.doGetSupportedLgSchemaVersions();
        assertNotNull("versions null", versions);
        assertFalse("versions empty", versions.isEmpty());
        LexGridSchemaVersion version = new LexGridSchemaVersion();
        version.setMajorVersion(2);
        version.setMinorVersion(0);
        assertTrue("version missing", versions.contains(version));
    }
}