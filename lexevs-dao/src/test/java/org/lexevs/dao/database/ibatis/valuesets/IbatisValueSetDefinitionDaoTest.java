package org.lexevs.dao.database.ibatis.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class IbatisValueSetDefinitionDaoTest {

    IbatisValueSetDefinitionDao definitionDao = new IbatisValueSetDefinitionDao();

    @Test
    public void getValueSetDefinitionByURI() {
        definitionDao.getValueSetDefinitionByURI("uri");
    }

    @Test
    public void getGuidFromvalueSetDefinitionURI() {
        definitionDao.getGuidFromvalueSetDefinitionURI("uri");
    }

    @Test
    public void getAllValueSetDefinitionsWithNoName() {
        try {
            definitionDao.getAllValueSetDefinitionsWithNoName();
        }
        catch (LBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getValueSetDefinitionURIsForName() {
        try {
            definitionDao.getValueSetDefinitionURIsForName("name");
        }
        catch (LBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getValueSetDefinitionSchemeRefForTopNodeSourceCode() {
        definitionDao.getValueSetDefinitionSchemeRefForTopNodeSourceCode("code");
    }

    @Test
    public void getValueSetDefinitionDefRefForTopNodeSourceCode() {
        definitionDao.getValueSetDefinitionDefRefForTopNodeSourceCode("code");
    }

    @Test
    public void getValueSetDefinitionURIs() {
        definitionDao.getValueSetDefinitionURIs();
    }

    @Test
    public void getPrefix() {
        definitionDao.getPrefix();
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
        definitionDao.doGetSupportedLgSchemaVersions();
    }

    @Test
    public void getVersionsDao() {
        definitionDao.getVersionsDao();
    }

    @Test
    public void getVsPropertyDao() {
        definitionDao.getVsPropertyDao();
    }

    @Test
    public void getVsEntryStateDao() {
        definitionDao.getVsEntryStateDao();
    }

    @Test
    public void getValueSetDefEntryStateUId() {
        definitionDao.getValueSetDefEntryStateUId("uid");
    }

    @Test
    public void getVsDefinitionEntryDao() {
        definitionDao.getVsDefinitionEntryDao();
    }

    @Test
    public void getValueSetDefinitionURIForSupportedTagAndValue() {
        definitionDao.getValueSetDefinitionURIForSupportedTagAndValue("tag", "value", "uri");
    }

    @Test
    public void getLatestRevision() {
        definitionDao.getLatestRevision("uid");
    }

    @Test
    public void entryStateExists() {
        definitionDao.entryStateExists("entryID");
    }

    @Test
    public void getValueSetDefinitionByRevision() {
        try {
            definitionDao.getValueSetDefinitionByRevision("uri", "revision");
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getValueSetURIsForContext() {
        definitionDao.getValueSetURIsForContext("contextURI");
    }

    @Test
    public void getValueSetURIMapToDefinitions() {
        definitionDao.getValueSetURIMapToDefinitions();
    }
}