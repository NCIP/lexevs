package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;
import javax.annotation.Resource;
import org.LexGrid.concepts.Entity;
import org.LexGrid.commonTypes.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.association.model.DefinedNode;
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
public class IbatisSourceAssertedValueSetDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    IbatisSourceAssertedValueSetDao valueSetDao;

    @Test
    public void doGetSupportedLgSchemaVersions() {
        List<LexGridSchemaVersion> versions =  valueSetDao.doGetSupportedLgSchemaVersions();
        assertNotNull("versions null", versions);
        assertFalse("versions empty", versions.isEmpty());
        assertEquals("version missing", 2, versions.get(0).getMajorVersion());
    }



    @Test
    public void getValueSetEntityCount() {
        int valueSetCount =valueSetDao.getValueSetEntityCount("C85492", "2003", "2062");
        assertEquals("value set count wrong", 439,valueSetCount);
    }

    @Test
    public void getSourceAssertedValueSetTopNodeForEntityCode() {
        List<Entity> valueSets = valueSetDao.getSourceAssertedValueSetTopNodeForEntityCode("C85492", "2003");
        assertNotNull("value sets null", valueSets);
        assertFalse("value sets empty", valueSets.isEmpty());
        assertEquals("value sets wrong size", 1, valueSets.size());
    }


    @Test
    public void getSourceAssertedValueSetEntitiesForEntityCode() {
        List<Entity> valueSets = valueSetDao.getSourceAssertedValueSetEntitiesForEntityCode("C85492", "relation",
                "2062", "2003");
        assertNotNull("value sets null", valueSets);
        assertFalse("value sets empty", valueSets.isEmpty());
        assertTrue("value sets too small", valueSets.size()>430);
        assertTrue("value sets too large", valueSets.size()<460);
    }

    @Test
    public void getPagedValueSetEntities() {
        List<Entity> valueSets =valueSetDao.getPagedValueSetEntities("C85492", "2003", "2062", 0, 100);
        assertNotNull("value sets null", valueSets);
        assertFalse("value sets empty", valueSets.isEmpty());
        assertEquals("value sets wrong size", 100, valueSets.size());
    }

    @Test
    public void getSourceAssertedValueSetTopNodeDescription() {
        List<Entity> valueSets =valueSetDao.getSourceAssertedValueSetTopNodeDescription("CDISC SDTM Method Terminology","2003" );
        assertNotNull("value sets null", valueSets);
        assertFalse("value sets empty", valueSets.isEmpty());
        assertEquals("value sets wrong size", 1, valueSets.size());
    }

    @Test
    public void getValueSetEntityUids() {
        List<String> valueSets = valueSetDao.getValueSetEntityUids("2003", "2062", 0, -1);
        assertNotNull("value sets null", valueSets);
        assertFalse("value sets empty", valueSets.isEmpty());
        assertEquals("value sets wrong size", 100272, valueSets.size());
    }

    @Test
    public void getValueSetEntityUidForTopNodeEntityCode() {
        List<String> valueSets =valueSetDao.getValueSetEntityUidForTopNodeEntityCode("2003", "2062", "C85492", 0, -1);
        assertNotNull("value sets null", valueSets);
        assertFalse("value sets empty", valueSets.isEmpty());
        assertEquals("value sets wrong size", 439, valueSets.size());
    }




    @Test
    public void getSourceAssertedValueSetsForVSMemberEntityCode() {
        List<Entity> valueSets =valueSetDao.getSourceAssertedValueSetsForVSMemberEntityCode("C85492", "relation", "2062", "2003");
        assertNotNull("value sets null", valueSets);
        assertFalse("value sets empty", valueSets.isEmpty());
        assertEquals("value sets wrong size", 2, valueSets.size());
    }

    @Test
    public void getAllValidValueSetTopNodeCodes() {
        List<DefinedNode> nodes = valueSetDao.getAllValidValueSetTopNodeCodes("FULL_SYN", "SDTM-METHOD", "2062", "2003");
        assertNotNull("nodes null", nodes);
        assertFalse("nodes empty", nodes.isEmpty());
        assertEquals("nodes wrong size", 1, nodes.size());
    }

    @Test
    public void getValueSetEntityProperties() {
        List<Property> properties = valueSetDao.getValueSetEntityProperties("C85492", "2003");
        assertNotNull("properties null", properties);
        assertFalse("properties empty", properties.isEmpty());
        assertEquals("properties wrong size", 16, properties.size());
    }
}