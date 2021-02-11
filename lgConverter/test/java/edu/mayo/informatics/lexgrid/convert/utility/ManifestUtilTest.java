
package edu.mayo.informatics.lexgrid.convert.utility;

import javax.annotation.Resource;

import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfAssociationDefinition;
import org.LexGrid.LexOnt.CsmfFormalName;
import org.LexGrid.LexOnt.CsmfLocalName;
import org.LexGrid.LexOnt.CsmfMappings;
import org.LexGrid.LexOnt.CsmfSource;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entities;
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.relations.AssociationEntity;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.event.registry.BaseListenerRegistry;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class ManifestUtilTest extends LexEvsDbUnitTestBase {
    
    @Resource
    private CodingSchemeService codingSchemeService;
    
    @Resource
    private AuthoringService authoringService;
    
    @Resource
    private BaseListenerRegistry baseListenerRegistry;
    
    @Before
    public void clearEntityListeners() {
        baseListenerRegistry.setEnableListeners(false);
    }
    
    @BeforeClass
    public static void setSystemProp() {
        System.setProperty("LG_CONFIG_FILE", "../lexevs-dao/src/test/resources/lbconfig.props");
    }
    
    @Test
    public void testApplyCodingSchemeFormalNameWithOverride() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        cs.setFormalName("preChangeFormalName");
        
        CodingSchemeManifest mf = new CodingSchemeManifest();
        CsmfFormalName formalName = new CsmfFormalName();
        formalName.setContent("testFormalName");
        formalName.setToOverride(true);
        mf.setFormalName(formalName);
        
        util.applyManifest(mf, cs);
        
        assertEquals("testFormalName", cs.getFormalName());
    }
    
    @Test
    public void testApplyCodingSchemeFormalNameWithoutOverride() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        cs.setFormalName("preChangeFormalName");
        
        CodingSchemeManifest mf = new CodingSchemeManifest();
        CsmfFormalName formalName = new CsmfFormalName();
        formalName.setContent("testFormalName");
        formalName.setToOverride(false);
        mf.setFormalName(formalName);
        
        util.applyManifest(mf, cs);
        
        assertEquals("preChangeFormalName", cs.getFormalName());
    }
    
    @Test
    public void testApplyCodingSchemeFormalNameWithOverrideNull() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        
        CodingSchemeManifest mf = new CodingSchemeManifest();
        CsmfFormalName formalName = new CsmfFormalName();
        formalName.setContent("testFormalName");
        formalName.setToOverride(false);
        mf.setFormalName(formalName);
        
        util.applyManifest(mf, cs);
        
        assertEquals("testFormalName", cs.getFormalName());
    }
    
    @Test
    public void testApplyCodingSchemeFormalNameWithOverrideBlank() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        cs.setFormalName("");
        
        CodingSchemeManifest mf = new CodingSchemeManifest();
        CsmfFormalName formalName = new CsmfFormalName();
        formalName.setContent("testFormalName");
        formalName.setToOverride(false);
        mf.setFormalName(formalName);
        
        util.applyManifest(mf, cs);
        
        assertEquals("testFormalName", cs.getFormalName());
    }
    
    @Test
    public void testApplyCodingSchemeLocalName() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        
        CodingSchemeManifest mf = new CodingSchemeManifest();
        CsmfLocalName localName = new CsmfLocalName();
        localName.setContent("testLocalName");
        localName.setToAdd(true);
        mf.addLocalName(localName);
        
        util.applyManifest(mf, cs);
        
        assertEquals(1, cs.getLocalNameCount());
        assertEquals("testLocalName", cs.getLocalName(0));
    }
    
    @Test
    public void testApplyCodingSchemeLocalNameWithToAddFalse() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        
        CodingSchemeManifest mf = new CodingSchemeManifest();
        CsmfLocalName localName = new CsmfLocalName();
        localName.setContent("testLocalName");
        localName.setToAdd(false);
        mf.addLocalName(localName);
        
        util.applyManifest(mf, cs);
        
        assertEquals(1, cs.getLocalNameCount());
        assertEquals("testLocalName", cs.getLocalName(0));
    }
    
    @Test
    public void testApplyCodingSchemeLocalNameToAddTrue() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        cs.addLocalName("existingLocalName");
        
        CodingSchemeManifest mf = new CodingSchemeManifest();
        CsmfLocalName localName = new CsmfLocalName();
        localName.setContent("testLocalName");
        localName.setToAdd(true);
        mf.addLocalName(localName);
        
        util.applyManifest(mf, cs);
        
        assertEquals(2, cs.getLocalNameCount());
        assertTrue(cs.getLocalNameAsReference().contains("existingLocalName"));
        assertTrue(cs.getLocalNameAsReference().contains("testLocalName"));
    }
    
    @Test
    public void testApplyCodingSchemeLocalNameToAddFalseWithExisting() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        cs.addLocalName("testLocalName");
        
        CodingSchemeManifest mf = new CodingSchemeManifest();
        CsmfLocalName localName = new CsmfLocalName();
        localName.setContent("testLocalName2");
        localName.setToAdd(false);
        mf.addLocalName(localName);
        
        util.applyManifest(mf, cs);
        
        assertEquals(1, cs.getLocalNameCount());
        assertEquals("testLocalName", cs.getLocalName(0));
    }
    
    @Test
    public void testPreLoadAddSourcesToUpdateTrue() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        Source original = new Source();
        original.setRole("originalRole");
        original.setContent("content");
        original.setSubRef("subref");
        cs.addSource(original);
        
        
        CsmfSource mfSource = new CsmfSource();
        mfSource.setContent("content");
        mfSource.setSubRef("updatedSubRef");
        mfSource.setToUpdate(true);
        
        util.preLoadAddSources(cs, new CsmfSource[] {mfSource});
        
        assertEquals(1, cs.getSourceCount());
        assertEquals("originalRole", cs.getSource(0).getRole());
        assertEquals("content", cs.getSource(0).getContent());
        assertEquals("updatedSubRef", cs.getSource(0).getSubRef());
        
    }
    
    @Test
    public void testPreLoadAddSourcesToUpdateFalse() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        Source original = new Source();
        original.setRole("originalRole");
        original.setContent("content");
        original.setSubRef("subref");
        cs.addSource(original);
        
        
        CsmfSource mfSource = new CsmfSource();
        mfSource.setContent("content");
        mfSource.setSubRef("updatedSubRef");
        mfSource.setToUpdate(false);
        
        util.preLoadAddSources(cs, new CsmfSource[] {mfSource});
        
        assertEquals(1, cs.getSourceCount());
        assertEquals("originalRole", cs.getSource(0).getRole());
        assertEquals("content", cs.getSource(0).getContent());
        assertEquals("subref", cs.getSource(0).getSubRef());
        
    }
    
    @Test
    public void testPreLoadAddSourcesAdd() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        Source original = new Source();
        original.setRole("originalRole");
        original.setContent("content");
        original.setSubRef("subref");
        cs.addSource(original);
        
        
        CsmfSource mfSource = new CsmfSource();
        mfSource.setContent("content2");
        mfSource.setSubRef("updatedSubRef");
        mfSource.setToUpdate(false);
        
        util.preLoadAddSources(cs, new CsmfSource[] {mfSource});
        
        assertEquals(2, cs.getSourceCount());        
    }
    
    @Test
    public void testPreLoadAddSupportedMappings() {
        ManifestUtil util = new ManifestUtil();
        
        CodingScheme cs = new CodingScheme();
        cs.setMappings(new Mappings());

        SupportedCodingScheme scs = new SupportedCodingScheme();
        scs.setContent("scs");
        scs.setIsImported(true);
        scs.setLocalId("id");
        scs.setUri("uri");
        
        cs.getMappings().addSupportedCodingScheme(scs);
        
        CsmfMappings manifestMappings = new CsmfMappings();
        SupportedCodingScheme scsModified = new SupportedCodingScheme();
        scsModified.setContent("scs2");
        scsModified.setIsImported(false);
        scsModified.setLocalId("id");
        scsModified.setUri("uri2");
        manifestMappings.addSupportedCodingScheme(scsModified);
        
        util.preLoadAddSupportedMappings(cs, manifestMappings);
        
        assertEquals(1, cs.getMappings().getSupportedCodingSchemeCount());
        assertEquals("scs2", cs.getMappings().getSupportedCodingScheme(0).getContent());
        assertFalse(cs.getMappings().getSupportedCodingScheme(0).getIsImported());
        assertEquals("id", cs.getMappings().getSupportedCodingScheme(0).getLocalId());
        assertEquals("uri2", cs.getMappings().getSupportedCodingScheme(0).getUri());
    }
    
    @Test
    public void testApplyPostLoadManifestFormalName() throws Exception {
        
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csname");
        cs.setCodingSchemeURI("uri");
        cs.setRepresentsVersion("version");
        cs.setFormalName("original formal name");
        
        authoringService.loadRevision(cs, null, null);
        
        CodingSchemeManifest csManifest = new CodingSchemeManifest();
        CsmfFormalName formalName = new CsmfFormalName();
        formalName.setContent("modified Formal Name");
        formalName.setToOverride(true);
        csManifest.setFormalName(formalName);
        
        ManifestUtil util = new ManifestUtil();
        util.applyManifest(csManifest, new URNVersionPair("uri", "version"));
        
        CodingScheme moddedCs = codingSchemeService.getCodingSchemeByUriAndVersion("uri", "version");
        
        assertEquals("modified Formal Name", moddedCs.getFormalName());
    }
    
    @Test
    public void testApplyPostLoadManifestAddSupportedAttribute() throws Exception {
           
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csname");
        cs.setCodingSchemeURI("uri");
        cs.setRepresentsVersion("version");
        cs.setFormalName("original formal name");
        
        SupportedCodingScheme scs = new SupportedCodingScheme();
        scs.setLocalId("testCs");
        cs.setMappings(new Mappings());
        cs.getMappings().addSupportedCodingScheme(scs);
        
        authoringService.loadRevision(cs, null, null);
        
        CodingSchemeManifest csManifest = new CodingSchemeManifest();
        csManifest.setMappings(new CsmfMappings());
        csManifest.getMappings().setToUpdate(true);
        
        SupportedCodingScheme scs2 = new SupportedCodingScheme();
        scs2.setLocalId("testCs2");
        
        csManifest.getMappings().addSupportedCodingScheme(scs2);
        
        ManifestUtil util = new ManifestUtil();
        util.applyManifest(csManifest, new URNVersionPair("uri", "version"));
        
        CodingScheme moddedCs = codingSchemeService.getCodingSchemeByUriAndVersion("uri", "version");
        
        assertEquals(2, moddedCs.getMappings().getSupportedCodingSchemeCount());
    }
    
    @Test
    public void testApplyPostLoadManifestAddSupportedAttributeUpdate() throws Exception {
            
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csname");
        cs.setCodingSchemeURI("uri");
        cs.setRepresentsVersion("version");
        cs.setFormalName("original formal name");
        
        SupportedCodingScheme scs = new SupportedCodingScheme();
        scs.setLocalId("testCs");
        cs.setMappings(new Mappings());
        cs.getMappings().addSupportedCodingScheme(scs);
        
        authoringService.loadRevision(cs, null, null);
        
        CodingSchemeManifest csManifest = new CodingSchemeManifest();
        csManifest.setMappings(new CsmfMappings());
        csManifest.getMappings().setToUpdate(true);
        
        SupportedCodingScheme scs2 = new SupportedCodingScheme();
        scs2.setLocalId("testCs");
        scs2.setUri("some uri");
        
        csManifest.getMappings().addSupportedCodingScheme(scs2);
        
        ManifestUtil util = new ManifestUtil();
        util.applyManifest(csManifest, new URNVersionPair("uri", "version"));
        
        CodingScheme moddedCs = codingSchemeService.getCodingSchemeByUriAndVersion("uri", "version");
        
        assertEquals(1, moddedCs.getMappings().getSupportedCodingSchemeCount());
        assertEquals("some uri", moddedCs.getMappings().getSupportedCodingScheme(0).getUri());
    }
    
    @Test
    public void testApplyPreLoadManifestAddAssociationEntity() throws Exception {
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csname");
        cs.setCodingSchemeURI("uri");
        cs.setRepresentsVersion("version");
        cs.setFormalName("original formal name");
        
        ManifestUtil util = new ManifestUtil();
        
        CodingSchemeManifest csManifest = new CodingSchemeManifest();
        
        CsmfAssociationDefinition assocDefs = new CsmfAssociationDefinition();
        AssociationEntity assoc = new AssociationEntity();
        assoc.setEntityCode("code");
        assoc.setEntityCodeNamespace("ns");
        assoc.setIsTransitive(true);
        
        assocDefs.addAssoc(assoc);
        csManifest.setAssociationDefinitions(assocDefs);
        
        csManifest.setAssociationDefinitions(assocDefs);
        
        util.applyManifest(csManifest, cs);
        
        assertEquals(1, cs.getEntities().getEntityCount());   
        assertEquals("code", cs.getEntities().getEntity(0).getEntityCode());
        assertEquals("ns", cs.getEntities().getEntity(0).getEntityCodeNamespace());
        assertTrue(((AssociationEntity)cs.getEntities().getEntity(0)).getIsTransitive());
    }
    
    @Test
    public void testApplyPreLoadManifestModifyAssociationEntityWithToUpdateTrue() throws Exception {
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csname");
        cs.setCodingSchemeURI("uri");
        cs.setRepresentsVersion("version");
        cs.setFormalName("original formal name");
        
        cs.setEntities(new Entities());
        AssociationEntity assoc1 = new AssociationEntity();
        assoc1.setEntityCode("code");
        assoc1.setEntityCodeNamespace("ns");
        assoc1.setIsTransitive(true);
        cs.getEntities().addEntity(assoc1);
        
        ManifestUtil util = new ManifestUtil();
        
        CodingSchemeManifest csManifest = new CodingSchemeManifest();
        
        CsmfAssociationDefinition assocDefs = new CsmfAssociationDefinition();
        assocDefs.setToUpdate(true);
        AssociationEntity assoc2 = new AssociationEntity();
        assoc2.setEntityCode("code");
        assoc2.setEntityCodeNamespace("ns");
        assoc2.setIsTransitive(false);
        
        assocDefs.addAssoc(assoc2);
        csManifest.setAssociationDefinitions(assocDefs);
        
        csManifest.setAssociationDefinitions(assocDefs);
        
        util.applyManifest(csManifest, cs);
        
        assertEquals(1, cs.getEntities().getEntityCount());   
        assertEquals("code", cs.getEntities().getEntity(0).getEntityCode());
        assertFalse(((AssociationEntity)cs.getEntities().getEntity(0)).getIsTransitive());
    }
    
    @Test
    public void testApplyPreLoadManifestModifyAssociationEntityWithToUpdateFalse() throws Exception {
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csname");
        cs.setCodingSchemeURI("uri");
        cs.setRepresentsVersion("version");
        cs.setFormalName("original formal name");
        
        cs.setEntities(new Entities());
        AssociationEntity assoc1 = new AssociationEntity();
        assoc1.setEntityCode("code");
        assoc1.setEntityCodeNamespace("ns");
        assoc1.setIsTransitive(true);
        cs.getEntities().addEntity(assoc1);
        
        ManifestUtil util = new ManifestUtil();
        
        CodingSchemeManifest csManifest = new CodingSchemeManifest();
        
        CsmfAssociationDefinition assocDefs = new CsmfAssociationDefinition();
        assocDefs.setToUpdate(false);
        AssociationEntity assoc2 = new AssociationEntity();
        assoc2.setEntityCode("code");
        assoc2.setEntityCodeNamespace("ns");
        assoc2.setIsTransitive(false);
        
        assocDefs.addAssoc(assoc2);
        csManifest.setAssociationDefinitions(assocDefs);
        
        csManifest.setAssociationDefinitions(assocDefs);
        
        util.applyManifest(csManifest, cs);
        
        assertEquals(1, cs.getEntities().getEntityCount());   
        assertEquals("code", cs.getEntities().getEntity(0).getEntityCode());
        assertTrue(((AssociationEntity)cs.getEntities().getEntity(0)).getIsTransitive());
    }
    
    @Test
    public void testApplyPostLoadManifestAddAssociationEntity() throws Exception {
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csname");
        cs.setCodingSchemeURI("uri");
        cs.setRepresentsVersion("version");
        cs.setFormalName("original formal name");
        
        this.authoringService.loadRevision(cs, null, null);
        
        ManifestUtil util = new ManifestUtil();
        
        CodingSchemeManifest csManifest = new CodingSchemeManifest();
        
        CsmfAssociationDefinition assocDefs = new CsmfAssociationDefinition();
        AssociationEntity assoc = new AssociationEntity();
        assoc.setEntityCode("code");
        assoc.setEntityCodeNamespace("ns");
        assoc.setIsTransitive(true);
        
        assocDefs.addAssoc(assoc);
        csManifest.setAssociationDefinitions(assocDefs);
        
        csManifest.setAssociationDefinitions(assocDefs);
        
        URNVersionPair pair = new URNVersionPair("uri", "version");
        util.applyManifest(csManifest, pair);
        
        CodingScheme updatedCs = codingSchemeService.getCompleteCodingScheme("uri", "version");
        
        assertEquals(1, updatedCs.getEntities().getEntityCount());   
        assertEquals("code", updatedCs.getEntities().getEntity(0).getEntityCode());
        assertEquals("ns", updatedCs.getEntities().getEntity(0).getEntityCodeNamespace());
        assertTrue(((AssociationEntity)updatedCs.getEntities().getEntity(0)).getIsTransitive());
    }
    
    @Test
    public void testApplyPostLoadManifestAddAssociationEntityUpdate() throws Exception {
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csname");
        cs.setCodingSchemeURI("uri");
        cs.setRepresentsVersion("version");
        cs.setFormalName("original formal name");
        
        cs.setEntities(new Entities());
        AssociationEntity assoc1 = EntityFactory.createAssociation();
        assoc1.setEntityCode("code");
        assoc1.setEntityCodeNamespace("ns");
        assoc1.setIsTransitive(true);
        cs.getEntities().addEntity(assoc1);
        
        this.authoringService.loadRevision(cs, null, null);
        //this.entityService.insertEntity("uri", "version", assoc1);
        
        ManifestUtil util = new ManifestUtil();
        
        CodingSchemeManifest csManifest = new CodingSchemeManifest();
        
        CsmfAssociationDefinition assocDefs = new CsmfAssociationDefinition();
        AssociationEntity assoc = new AssociationEntity();
        assoc.setEntityCode("code");
        assoc.setEntityCodeNamespace("ns");
        assoc.setIsTransitive(false);
        
        assocDefs.addAssoc(assoc);
        csManifest.setAssociationDefinitions(assocDefs);
        
        csManifest.setAssociationDefinitions(assocDefs);
        
        URNVersionPair pair = new URNVersionPair("uri", "version");
        util.applyManifest(csManifest, pair);
        
        CodingScheme updatedCs = codingSchemeService.getCompleteCodingScheme("uri", "version");
        
        assertEquals(1, updatedCs.getEntities().getEntityCount());   
        assertEquals("code", updatedCs.getEntities().getEntity(0).getEntityCode());
        assertEquals("ns", updatedCs.getEntities().getEntity(0).getEntityCodeNamespace());
        
        AssociationEntity assocEntity = (AssociationEntity)updatedCs.getEntities().getEntity(0);
        assertFalse(assocEntity.getIsTransitive());
    }
}