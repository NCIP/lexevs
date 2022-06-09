package org.lexevs.dao.database.ibatis.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.DefinitionEntry;
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
public class IbatisVSDefinitionEntryDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    IbatisVSDefinitionEntryDao definitionDao = new IbatisVSDefinitionEntryDao();

    @Test
    public void doGetSupportedLgSchemaVersions() {
        definitionDao.doGetSupportedLgSchemaVersions();
    }

    @Test
    public void getDefinitionEntryUId() {
        definitionDao.getDefinitionEntryUId("vsURI", "ruleOrder");
    }

    @Test
    public void getVsEntryStateDao() {
        definitionDao.getVsEntryStateDao();
    }

    @Test
    public void getLatestRevision() {
        definitionDao.getLatestRevision("vsDefId");
    }

    @Test
    public void getVsPropertyDao() {
        definitionDao.getVsPropertyDao();
    }

    @Test
    public void resolveDefinitionEntryByRevision() {
        try {
            //We have no revisions for any entry states
            DefinitionEntry entry = definitionDao.resolveDefinitionEntryByRevision("SRITEST:AUTO:EveryThing", "2", null);
            assertNotNull("entry null", entry);
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getVSDefinitionEntryByUId() {
        DefinitionEntry entry = definitionDao.getVSDefinitionEntryByUId("SRITEST:AUTO:EveryThing");
        assertNotNull("entry null", entry);
    }
}