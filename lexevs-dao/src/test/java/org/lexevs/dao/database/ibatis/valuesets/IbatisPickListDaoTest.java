package org.lexevs.dao.database.ibatis.valuesets;


import java.util.List;
import javax.annotation.Resource;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.PickListDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.valuesets.PickListEntryNodeDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:lexevsDao.xml"})
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class IbatisPickListDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
    //EVS never uses picklists.  We have none loaded
    @Resource
    IbatisPickListDao ibatisPickListDao;

    @Test
    public void getPickListDefinitionById() {
        PickListDefinition pickListDefinition = ibatisPickListDao.getPickListDefinitionById("REPLACE");
    }

    @Test
    public void getPickListGuidFromPickListId() {
        String guid = ibatisPickListDao.getPickListGuidFromPickListId("REPLACE");
    }

    @Test
    public void getPickListDefinitionIdForValueSetDefinitionURI() {
        List<String> definitionId = ibatisPickListDao.getPickListDefinitionIdForValueSetDefinitionURI("REPLACE");
    }


    @Test
    public void getPickListIds() {
        List<String> pickListIds = ibatisPickListDao.getPickListIds();
    }

    @Test
    public void getPickListEntryNodeGuidByPickListIdAndPLEntryId() {
        String entryId =
                ibatisPickListDao.getPickListEntryNodeGuidByPickListIdAndPLEntryId("REPLACE ID", "REPLACE");
    }

    @Test
    public void getVsPropertyDao() {
        VSPropertyDao vsPropertyDao = ibatisPickListDao.getVsPropertyDao();
    }

    @Test
    public void getPickListDefinitionIdForEntityReference() {
        List<String> entityReference =
                ibatisPickListDao.getPickListDefinitionIdForEntityReference("code", "namespace", "propertyId");
    }

    @Test
    public void getVsEntryStateDao() {
        VSEntryStateDao vsEntryStateDao = ibatisPickListDao.getVsEntryStateDao();
    }

    @Test
    public void getPickListDefinitionIdForSupportedTagAndValue() {
        List<String> tagAndValue =
                ibatisPickListDao.getPickListDefinitionIdForSupportedTagAndValue("tag", "value");
    }

    @Test
    public void getPickListEntryStateUId() {
        String uid = ibatisPickListDao.getPickListEntryStateUId("uid");
    }

    @Test
    public void getPickListEntryNodeDao() {
        PickListEntryNodeDao pickListEntryNodeDao = ibatisPickListDao.getPickListEntryNodeDao();
    }

    @Test
    public void getLatestRevision() {
        String latestRevision = ibatisPickListDao.getLatestRevision("uid");
    }

    @Test
    public void entryStateExists() {
        boolean entryStateExists = ibatisPickListDao.entryStateExists("uid");

        entryStateExists = ibatisPickListDao.entryStateExists("prefix", "uid");
    }

    @Test
    public void resolvePickListByRevision() {
        try {
            PickListDefinition pickListDefinition = ibatisPickListDao.resolvePickListByRevision("picklist",
                    "revistion", 0);
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }
}