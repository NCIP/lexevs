package org.lexevs.dao.database.ibatis.valuesets;

import org.junit.Test;

import static org.junit.Assert.*;

public class IbatisVSEntryStateDaoTest {

    IbatisVSEntryStateDao entryStateDao = new IbatisVSEntryStateDao();

    @Test
    public void getEntryStateByUId() {
        entryStateDao.getEntryStateByUId("uid");
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
        entryStateDao.doGetSupportedLgSchemaVersions();
    }
}