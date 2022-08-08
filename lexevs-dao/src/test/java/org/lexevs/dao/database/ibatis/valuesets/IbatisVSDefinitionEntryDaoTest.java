package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;
import javax.annotation.Resource;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.DefinitionEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.valuesets.VSDefinitionEntryDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
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
public class IbatisVSDefinitionEntryDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    IbatisVSDefinitionEntryDao definitionDao;

    @Test
    public void doGetSupportedLgSchemaVersions() {
        List<LexGridSchemaVersion> versions = definitionDao.doGetSupportedLgSchemaVersions();
        assertNotNull("versions null",versions);
        assertFalse("versions empty", versions.isEmpty());
        assertEquals("version wrong", 2, versions.get(0).getMajorVersion());
    }

    @Test
    public void getDefinitionEntryUId() {
        String definitionEntryUId = definitionDao.getDefinitionEntryUId("SRITEST:AUTO:Automobiles", "1");
        assertNotNull("definitionId null", definitionEntryUId);
        assertEquals("21566475", definitionEntryUId);
    }

    @Test
    public void getVsEntryStateDao() {
        VSEntryStateDao vsEntryStateDao = definitionDao.getVsEntryStateDao();
        assertNotNull("entryStateDao null", vsEntryStateDao);
        assertTrue("entryStateDao wrong type", vsEntryStateDao instanceof VSEntryStateDao);
    }

    @Test
    public void getLatestRevision() {
        //TODO No revisions exist so this will always fail
        String latestRevision = definitionDao.getLatestRevision("vsDefId");
        assertNotNull("revision null",latestRevision);
    }

    @Test
    public void getVsPropertyDao() {
        VSPropertyDao vsPropertyDao = definitionDao.getVsPropertyDao();
        assertNotNull("propertyDao null", vsPropertyDao);
        assertTrue("wrong type", vsPropertyDao instanceof VSPropertyDao);
    }

    @Test
    public void resolveDefinitionEntryByRevision() {
        try {
            //We have no revisions for any entry states
            DefinitionEntry entry = definitionDao.resolveDefinitionEntryByRevision("SRITEST:AUTO:EveryThing", "2", null);
            assertNotNull("entry null", entry);
            String entityCode = entry.getEntityReference().getEntityCode();
            assertEquals("wrong code","005", entityCode);
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getVSDefinitionEntryByUId() {
        DefinitionEntry entry = definitionDao.getVSDefinitionEntryByUId("21566610");
        assertNotNull("entry null", entry);
        String entityCode = entry.getEntityReference().getEntityCode();
        assertEquals("wrong code", "005", entityCode);
    }
}