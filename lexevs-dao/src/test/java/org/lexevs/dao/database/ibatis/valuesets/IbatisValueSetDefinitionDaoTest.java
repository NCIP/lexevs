package org.lexevs.dao.database.ibatis.valuesets;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.valuesets.VSDefinitionEntryDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.versions.VersionsDao;
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
public class IbatisValueSetDefinitionDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    IbatisValueSetDefinitionDao definitionDao;

    @Test
    public void getValueSetDefinitionByURI() {
        ValueSetDefinition definition =definitionDao.getValueSetDefinitionByURI("SRITEST:AUTO:GM");
        assertNotNull("definition null", definition);
    }

    @Test
    public void getGuidFromvalueSetDefinitionURI() {
       String guid =  definitionDao.getGuidFromvalueSetDefinitionURI("SRITEST:AUTO:GM");
       assertNotNull("guid null",guid);
    }

    @Test
    public void getAllValueSetDefinitionsWithNoName() {
        try {
            List<String> noNames = definitionDao.getAllValueSetDefinitionsWithNoName();
            assertNotNull("noNames null", noNames);
            assertTrue("noNames should be empty", noNames.isEmpty());
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
        List<AbsoluteCodingSchemeVersionReference> versionReferences = definitionDao.getValueSetDefinitionSchemeRefForTopNodeSourceCode("code");
        assertNotNull("versions null",versionReferences);
        assertFalse("versions empty", versionReferences.isEmpty());
        assertEquals("versions wrong size", 1, versionReferences.size());
    }

    @Test
    public void getValueSetDefinitionDefRefForTopNodeSourceCode() {
        List<AbsoluteCodingSchemeVersionReference> versionReferences =definitionDao.getValueSetDefinitionDefRefForTopNodeSourceCode("GM");
        assertNotNull("versions null",versionReferences);
        assertFalse("versions empty", versionReferences.isEmpty());
        assertEquals("versions wrong size", 7, versionReferences.size());
    }

    @Test
    public void getValueSetDefinitionURIs() {
        List<String> definitionURIS =  definitionDao.getValueSetDefinitionURIs();
        assertNotNull("definitions null",definitionURIS);
        assertFalse("definitions empty", definitionURIS.isEmpty());
        assertEquals("definitions wrong size", 27, definitionURIS.size());
    }

    @Test
    public void getPrefix() {
        String prefix = definitionDao.getPrefix();
        assertNotNull("prefix null", prefix);
        assertEquals("prefix wrong", "lb",prefix);
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
         List<LexGridSchemaVersion> versions= definitionDao.doGetSupportedLgSchemaVersions();
         assertNotNull("versions null",versions);
         assertFalse("versions empty",versions.isEmpty());
         assertEquals("versions wrong size",1, versions.size());
    }

    @Test
    public void getVersionsDao() {
        VersionsDao versionsDao = definitionDao.getVersionsDao();
        assertNotNull("versionsDao null", versionsDao);
        assertTrue("versionsDao wrong type", versionsDao instanceof VersionsDao);
    }


    @Test
    public void getVsPropertyDao() {
        VSPropertyDao vsPropertyDao = definitionDao.getVsPropertyDao();
        assertNotNull("propertyDao null", vsPropertyDao);
        assertTrue("propertyDao wrong type", vsPropertyDao instanceof VSPropertyDao);
    }

    @Test
    public void getVsEntryStateDao() {
        VSEntryStateDao vsEntryStateDao = definitionDao.getVsEntryStateDao();
        assertNotNull("entryStateDao null", vsEntryStateDao);
        assertTrue("entryStateDao wrong type", vsEntryStateDao instanceof VSEntryStateDao);
    }

    @Test
    public void getVsDefinitionEntryDao() {
        VSDefinitionEntryDao definitionEntryDao = definitionDao.getVsDefinitionEntryDao();
        assertNotNull("entryDao null", definitionEntryDao);
        assertTrue("entryDao wrong type", definitionEntryDao instanceof VSDefinitionEntryDao);
    }

    @Test
    public void getValueSetDefEntryStateUId() {
        String uid = definitionDao.getValueSetDefEntryStateUId("SRITEST:AUTO:GM");
        assertNotNull("uid null", uid);
        assertEquals("uid wrong","uid", uid);
    }

    @Test
    public void getValueSetDefinitionURIForSupportedTagAndValue() {
        List<String> uris =  definitionDao.getValueSetDefinitionURIForSupportedTagAndValue("CodingScheme", "German Made Parts", null);
        assertNotNull("uri null", uris);
        assertFalse("uris empty", uris.isEmpty());
        assertEquals("uris wrong size", 21, uris.size());


        uris =  definitionDao.getValueSetDefinitionURIForSupportedTagAndValue("CodingScheme", "German Made Parts", "urn:oid:11.11.0.2");
        assertNotNull("uri2 null", uris);
        assertFalse("uris2 empty", uris.isEmpty());
        assertEquals("uris2 wrong size", 21, uris.size());

    }

    @Test
    public void getLatestRevision() {
        //TODO We don't use revisions
        String revision = definitionDao.getLatestRevision("21566660");
        assertNotNull("revision null", revision);
    }

    @Test
    public void entryStateExists() {
        boolean exists = definitionDao.entryStateExists("21566786");
        assertTrue("entryState exists", exists);
    }

    @Test
    public void getValueSetDefinitionByRevision() {
        try {
            ValueSetDefinition valueSetDefinition = definitionDao.getValueSetDefinitionByRevision("uri",
                    "revision");
            assertNotNull("definition null", valueSetDefinition);
        }
        catch (LBRevisionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getValueSetURIsForContext() {
        //TODO we don't have any value set URIs under the "Context" attributeTag
        List<String> contextURIs = definitionDao.getValueSetURIsForContext("contextURI");
        assertNotNull("contextURIs null" , contextURIs);
        assertFalse("contextURIs empty", contextURIs.isEmpty());
        assertEquals("contextURIs wrong size",1,contextURIs );
    }

    @Test
    public void getValueSetURIMapToDefinitions() {
        Map<String, ValueSetDefinition> definitions = definitionDao.getValueSetURIMapToDefinitions();
        assertNotNull("definitions null", definitions);
        assertTrue("definitions empty",definitions.size()>0);
        assertNotNull("definition missing",definitions.get("REPLACE") );
    }
}