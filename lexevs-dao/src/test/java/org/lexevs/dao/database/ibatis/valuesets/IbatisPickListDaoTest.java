package org.lexevs.dao.database.ibatis.valuesets;

import javafx.scene.control.TableColumn;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class IbatisPickListDaoTest {

    IbatisPickListDao ibatisPickListDao = new IbatisPickListDao();

    @Test
    public void getPickListDefinitionById() {
        ibatisPickListDao.getPickListDefinitionById("REPLACE");
    }

    @Test
    public void getPickListGuidFromPickListId() {
        ibatisPickListDao.getPickListGuidFromPickListId("REPLACE");
    }

    @Test
    public void getPickListDefinitionIdForValueSetDefinitionURI() {
        ibatisPickListDao.getPickListDefinitionIdForValueSetDefinitionURI("REPLACE");
    }


    @Test
    public void getPickListIds() {
        ibatisPickListDao.getPickListIds();
    }

    @Test
    public void getPickListEntryNodeGuidByPickListIdAndPLEntryId() {
        ibatisPickListDao.getPickListEntryNodeGuidByPickListIdAndPLEntryId("REPLACE ID", "REPLACE");
    }

    @Test
    public void getVsPropertyDao() {
        ibatisPickListDao.getVsPropertyDao();
    }

    @Test
    public void getPickListDefinitionIdForEntityReference() {
        ibatisPickListDao.getPickListDefinitionIdForEntityReference("code", "namespace", "propertyId");
    }

    @Test
    public void getVsEntryStateDao() {
        ibatisPickListDao.getVsEntryStateDao();
    }

    @Test
    public void getPickListDefinitionIdForSupportedTagAndValue() {
        ibatisPickListDao.getPickListDefinitionIdForSupportedTagAndValue("tag", "value");
    }

    @Test
    public void getPickListEntryStateUId() {
        ibatisPickListDao.getPickListEntryStateUId("uid");
    }

    @Test
    public void getPickListEntryNodeDao() {
        ibatisPickListDao.getPickListEntryNodeDao();
    }

    @Test
    public void getLatestRevision() {
        ibatisPickListDao.getLatestRevision("uid");
    }

    @Test
    public void entryStateExists() {
        ibatisPickListDao.entryStateExists("uid");

        ibatisPickListDao.entryStateExists("prefix", "uid");
    }

    @Test
    public void resolvePickListByRevision() {
        try {
            ibatisPickListDao.resolvePickListByRevision("picklist", "revistion", 0);
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }
}