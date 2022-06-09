package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;
import javax.annotation.Resource;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:lexevsDao.xml"})
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class IbatisValueSetDefinitionDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    IbatisValueSetDefinitionDao definitionDao;

    @Test
    public void getValueSetDefinitionByURI() {
        ValueSetDefinition definition =definitionDao.getValueSetDefinitionByURI("GM");
        assertNotNull("definition null", definition);
    }

    @Test
    public void getGuidFromvalueSetDefinitionURI() {
       String guid =  definitionDao.getGuidFromvalueSetDefinitionURI("uri");
       assertNotNull("guid null",guid);
    }

    @Test
    public void getAllValueSetDefinitionsWithNoName() {
        try {
            List<String> noNames = definitionDao.getAllValueSetDefinitionsWithNoName();
            assertNotNull("noNames null", noNames);
        }
        catch (LBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getValueSetDefinitionURIsForName() {
        try {
            List<String> definitionURIS = definitionDao.getValueSetDefinitionURIsForName("GM");
            assertNotNull("definition URIs null", definitionURIS);
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