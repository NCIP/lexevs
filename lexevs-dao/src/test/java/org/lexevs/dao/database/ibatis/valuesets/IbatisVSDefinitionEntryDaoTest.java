package org.lexevs.dao.database.ibatis.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class IbatisVSDefinitionEntryDaoTest {

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
            definitionDao.resolveDefinitionEntryByRevision("vsDefURI", "ruleOrder", "revisionId");
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getVSDefinitionEntryByUId() {
        definitionDao.getVSDefinitionEntryByUId("vsEntryUid");
    }
}