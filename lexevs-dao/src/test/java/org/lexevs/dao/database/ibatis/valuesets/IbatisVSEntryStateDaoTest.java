package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;
import javax.annotation.Resource;
import org.LexGrid.versions.EntryState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:lexevsDao.xml"})
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class IbatisVSEntryStateDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    IbatisVSEntryStateDao entryStateDao ;


    @Test
    public void getEntryStateByUId() {
//TODO gives null pointer on PrefixResolver
        EntryState entryState = entryStateDao.getEntryStateByUId("21566786");
        assertNotNull(entryState);
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
         List<LexGridSchemaVersion> versions = entryStateDao.doGetSupportedLgSchemaVersions();
         assertNotNull("versions null",versions);
         assertTrue("versions wrong size", versions.size()==1);
         assertEquals(2, versions.get(0).getMajorVersion());
    }
}