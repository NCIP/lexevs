package org.lexevs.dao.database.ibatis.codednodegraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.custom.relations.TerminologyMapBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.Sextuple;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.ibatis.codednodegraph.model.EntityReferencingAssociatedConcept;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService.Order;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService.Sort;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService.QualifierSort;
import org.lexevs.dao.database.service.codednodegraph.model.ColumnSortType;
import org.lexevs.dao.database.service.codednodegraph.model.CountConceptReference;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The Class IbatisAssociationDaoTest.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:lexevsDao.xml"})
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class IbatisCodedNodeGraphDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    /**
     * The ibatis association dao.
     */
    @Resource
    private IbatisCodedNodeGraphDao ibatisCodedNodeGraphDao;

    @Test
    public void doGetConceptReferences() {
        List<ConceptReference> subjects = new ArrayList<ConceptReference>();
        ConceptReference subject = new ConceptReference();
        subject.setConceptCode("C10001");
        subject.setCodeNamespace("ncit");
        subject.addEntityType("concept");
        subjects.add(subject);
        List<ConceptReference> cRefs = ibatisCodedNodeGraphDao.doGetConceptReferences("2003",
                "roles", subjects, null, null,
                null, null, null,
                null, null, 0, -1);
        assertNotNull("references null", cRefs);
        assertTrue("references empty", cRefs.size() > 0);
        assertEquals("reference wrong", 5, cRefs.size());
    }

    @Test
    public void doGetCountConceptReferences() {
        ConceptReference conceptReference = new ConceptReference();
        conceptReference.setCodeNamespace("ncit");
        conceptReference.setCode("C10001");
        List<ConceptReference> references = new ArrayList<ConceptReference>();
        references.add(conceptReference);
        List<CountConceptReference> cRefs = ibatisCodedNodeGraphDao.doGetCountConceptReferences("2003",
                "roles", references, null,
                null, null, null,
                null, null, TripleNode.SUBJECT);
        assertNotNull("cRefs null", cRefs);
        assertFalse("cRefs empty", cRefs.isEmpty());
		assertEquals("cRefs wrong size", cRefs.size(), 1);
        assertEquals("wrong child count", 5, cRefs.get(0).getChildCount());
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
        List<LexGridSchemaVersion> versions = ibatisCodedNodeGraphDao.doGetSupportedLgSchemaVersions();
        assertNotNull("versions null", versions);
        assertTrue("versions empty", versions.size() > 0);
        LexGridSchemaVersion version = new LexGridSchemaVersion();
        version.setMinorVersion(0);
        version.setMajorVersion(2);
        assertTrue("version should exist", versions.contains(version));
    }

    @Test
    public void doGetTripleUids() {
        List<String> tripleUids = ibatisCodedNodeGraphDao.doGetTripleUids("2003",
                "2076", "C100051", "ncit",
                null, null, null,
                null, null, null, null, 0, -1);
        assertNotNull("tripleUids null", tripleUids);
        assertFalse("tripleUids empty", tripleUids.isEmpty());
        assertTrue("tripleUid missing", tripleUids.contains("5573914"));
    }

    @Test
    public void doGetTripleUidsCount() {
        Map<String, Integer> tripleUids = ibatisCodedNodeGraphDao.doGetTripleUidsCount("2003",
                "roles", "C100051", "ncit",
                null, null, null,
                null, null, null, null);
        assertNotNull("tripleUids null", tripleUids);
        assertFalse("tripleUids empty", tripleUids.isEmpty());
        Integer testInt = tripleUids.get("Disease_Has_Normal_Cell_Origin");
        assertNotNull("value is null", testInt);
		assertEquals("value wrong", 1, (int) testInt);
    }

    @Test
    public void doesEntityParticipateInRelationships() {
        boolean entityParticipates = ibatisCodedNodeGraphDao.doesEntityParticipateInRelationships("13513003",
                "relations", "C0001", "Automobiles");
        assertTrue("entity participates", entityParticipates);

        entityParticipates = ibatisCodedNodeGraphDao.doesEntityParticipateInRelationships("13513003",
                "relations", "GM", "Automobiles");
        assertFalse("entity does not participate", entityParticipates);
    }

    @Test
    public void getAssociatedConceptsFromUid() {
        List<String> tripleUids = new ArrayList<String>();
        tripleUids.add("5573914");
        tripleUids.add("5611978");
        tripleUids.add("5573962");
        List<Sort> sorts = new ArrayList<Sort>();
        sorts.add(new Sort(ColumnSortType.CODE, Order.ASC));
        List<EntityReferencingAssociatedConcept> associatedConcepts =
 ibatisCodedNodeGraphDao.getAssociatedConceptsFromUid("2003", tripleUids, sorts, TripleNode.SUBJECT);
        assertNotNull("associatedConcepts null", associatedConcepts);
        assertFalse("associatedConcepts empty", associatedConcepts.isEmpty());
        assertEquals("Result count wrong", 3, associatedConcepts.size());
        String qualifierName = associatedConcepts.get(0).getAssociationQualifiers().getNameAndValue(0).getName();
        assertEquals("qualifier should be some", "some", qualifierName);
    }

    @Test
    public void getAssociationPredicateNamesForCodingSchemeUid() {
        List<String> predicateNames = ibatisCodedNodeGraphDao.getAssociationPredicateNamesForCodingSchemeUid("2003",
		"roles");
        assertNotNull("Names null", predicateNames);
        assertTrue("Names empty", predicateNames.size() > 0);
        assertTrue("Name missing", predicateNames.contains("Disease_Has_Finding"));
    }

    @Test
    public void getConceptReferencesContainingObject() {
        List<ConceptReference> objects = new ArrayList<ConceptReference>();
        ConceptReference object = new ConceptReference();
        object.setConceptCode("C633");
        object.setCodeNamespace("ncit");
        objects.add(object);
        List<ConceptReference> cRefs = ibatisCodedNodeGraphDao.getConceptReferencesContainingObject("2003",
                "roles", objects, null, null,
                null, null, null,
                false, null, 0, -1);
        assertNotNull("references null", cRefs);
        assertTrue("references empty", cRefs.size() > 0);
        assertNotNull("contained cRef is null", cRefs.get(0));
        assertEquals("cRefs wrong size", 98, cRefs.size());
    }

    @Test
    public void getConceptReferencesContainingSubject() {
        List<ConceptReference> subjects = new ArrayList<ConceptReference>();
        ConceptReference subject = new ConceptReference();
        subject.setConceptCode("C10001");
        subject.setCodeNamespace("ncit");
        subjects.add(subject);
        List<ConceptReference> cRefs = ibatisCodedNodeGraphDao.getConceptReferencesContainingSubject("2003",
                "roles", subjects, null, null,
                null, null, null,
                false, null, 0, -1);
        assertNotNull("references null", cRefs);
        assertTrue("references empty", cRefs.size() > 0);
        assertNotNull("contained cRef is null", cRefs.get(0));
        assertEquals("cRefs wrong size", 5, cRefs.size());
    }

    @Test
    public void getConceptReferencesFromUid() {
        List<String> tripleUids = new ArrayList<String>();
        tripleUids.add("5573151");
        tripleUids.add("5562242");
        tripleUids.add("5572929");
        List<Sort> sorts = new ArrayList<Sort>();
        sorts.add(new Sort(ColumnSortType.CODE, Order.ASC));
        List<ConceptReference> cRefs = ibatisCodedNodeGraphDao.getConceptReferencesFromUid("2003",
                tripleUids, TripleNode.SUBJECT, sorts);
        assertNotNull("cRefs null", cRefs);
        assertFalse("cRefs empty", cRefs.isEmpty());
		assertEquals("cRefs wrong size", 1, cRefs.size());
        assertEquals("cRef missing", "C10001", cRefs.get(0).getCode());
    }

    @Test
    public void getCountConceptReferencesContainingObject() {
        List<ConceptReference> references = new ArrayList<ConceptReference>();
        ConceptReference reference = new CountConceptReference();
        reference.setCodeNamespace("ncit");
        reference.setCode("C405");
        references.add(reference);
        List<CountConceptReference> cRefs = ibatisCodedNodeGraphDao.getCountConceptReferencesContainingObject("2003",
                "roles", references, null,
                null, null, null,
                null, null);
        assertNotNull("cRefs null", cRefs);
        assertFalse("cRefs empty", cRefs.isEmpty());
        assertEquals("cref wrong size", 1, cRefs.size());
    }

    @Test
    public void getCountConceptReferencesContainingSubject() {
        List<ConceptReference> references = new ArrayList<ConceptReference>();
        ConceptReference reference = new CountConceptReference();
        reference.setCodeNamespace("ncit");
        reference.setCode("C10001");
        references.add(reference);
        List<CountConceptReference> cRefs = ibatisCodedNodeGraphDao.getCountConceptReferencesContainingSubject("2003",
                "roles", references, null,
                null, null, null,
                null, Boolean.FALSE);
        assertNotNull("cRefs null", cRefs);
        assertFalse("cRefs empty", cRefs.isEmpty());
		assertEquals("cRefs wrong size", 1, cRefs.size());
        assertEquals("wrong child count", 5, cRefs.get(0).getChildCount());
    }

    @Test
    public void getDistinctSourceNodesForAssociationPredicate() {
        List<Node> nodes = ibatisCodedNodeGraphDao.getDistinctSourceNodesForAssociationPredicate("2003", "2076");
        assertNotNull("nodes null", nodes);
        assertTrue("nodes empty", nodes.size() > 0);
        Node node = new Node();
        node.setEntityCodeNamespace("ncit");
        node.setEntityCode("C40359");
        assertTrue("node missing", nodes.contains(node));
    }

    @Test
    public void getDistinctTargetNodesForAssociationPredicate() {
        List<Node> nodes = ibatisCodedNodeGraphDao.getDistinctTargetNodesForAssociationPredicate("2003", "2076");
        assertNotNull("nodes null", nodes);
        assertTrue("nodes empty", nodes.size() > 0);
        Node node = new Node();
        node.setEntityCodeNamespace("ncit");
        node.setEntityCode("C47817");
        assertTrue("node missing", nodes.contains(node));
    }

    @Test
    public void getMapAndTermsForMappingAndReferences() {

        List<TerminologyMapBean> mapBeans = ibatisCodedNodeGraphDao.getMapAndTermsForMappingAndReferences("13513003",
                "3", "13512003", null, null);
        assertNotNull("mapBeans null", mapBeans);
        assertTrue("mapBeans empty", mapBeans.size() > 0);
    }

    @Test
    public void getRootNodes() {
        List<String> assocPredUid = new ArrayList<String>();
        assocPredUid.add("1023");
        List<Sort> sorts = new ArrayList<Sort>();
        sorts.add(new Sort(ColumnSortType.CODE, Order.ASC));
        List<ConceptReference> cRefs = ibatisCodedNodeGraphDao.getRootNodes("2003",
                assocPredUid, null, null,
                null, TraverseAssociations.TOGETHER, sorts, 0, -1);
        assertNotNull("cRefs null", cRefs);
        assertFalse("cRefs empty", cRefs.isEmpty());
        assertTrue("cRef missing", cRefs.contains(new ConceptReference()));
    }

    @Test
    public void getSourceNodesForTarget() {
        List<Node> nodes = ibatisCodedNodeGraphDao.getSourceNodesForTarget("2003", "2059", "C77218", "ncit");
        assertNotNull("nodes null", nodes);
        assertTrue("nodes empty", nodes.size() > 0);
        Node node = new Node();
        node.setEntityCode("C1019");
        node.setEntityCodeNamespace("ncit");
        assertTrue("node missing", nodes.contains(node));
    }

    @Test
    public void getTailNodes() {
        List<String> assocPredUid = new ArrayList<String>();
        assocPredUid.add("1023");
        List<Sort> sorts = new ArrayList<Sort>();
        sorts.add(new Sort(ColumnSortType.CODE, Order.ASC));
        List<ConceptReference> cRefs = ibatisCodedNodeGraphDao.getTailNodes("2003",
                assocPredUid, null, null,
                null, TraverseAssociations.TOGETHER, sorts, 0, -1);
        assertNotNull("cRefs null", cRefs);
        assertFalse("cRefs empty", cRefs.isEmpty());
        assertTrue("cRef missing", cRefs.contains(new ConceptReference()));
    }

    @Test
    public void getTargetNodesForSource() {
        List<Node> nodes = ibatisCodedNodeGraphDao.getTargetNodesForSource("2003", "2059", "C1019", "ncit");
        assertNotNull("nodes null", nodes);
        assertTrue("nodes empty", nodes.size() > 0);
        Node node = new Node();
        node.setEntityCode("C77218");
        node.setEntityCodeNamespace("ncit");
        assertTrue("node missing", nodes.contains(node));
    }

    @Test
    public void getTransitiveTableCount() {
        int tableCount = ibatisCodedNodeGraphDao.getTransitiveTableCount("2003");
        assertTrue("tableCount should be >0", tableCount > 0);
		assertEquals("tableCount wrong", 2365823, tableCount);
    }

    @Test
    public void getTripleUidsContainingObject() {
        List<String> tripleUids = ibatisCodedNodeGraphDao.getTripleUidsContainingObject("2003",
                "2062", "C61410", "ncit",
                null, null, null,
                null, null, null, null, 0, -1);
        assertNotNull("tripleUids null", tripleUids);
        assertFalse("tripleUids empty", tripleUids.isEmpty());
        assertTrue("tripleUid missing", tripleUids.contains("5575635"));
    }

    @Test
    public void getTripleUidsContainingObjectCount() {
        Map<String, Integer> containCount = ibatisCodedNodeGraphDao.getTripleUidsContainingObjectCount("2003",
                "roles", "C405", "ncit",
                null, null, null,
                null, null, null);
        assertNotNull("containCount null", containCount);
        assertFalse("containCount empty", containCount.isEmpty());

        Integer testInt = containCount.get("Chemotherapy_Regimen_Has_Component");
        assertNotNull("value is null", testInt);
		assertEquals("value wrong", 2, testInt.intValue());
    }

    @Test
    public void getTripleUidsContainingSubject() {
        List<String> tripleUids = ibatisCodedNodeGraphDao.getTripleUidsContainingSubject("2003",
                null, "C100051", "ncit",
                null, null, null,
                null, null, null, null, 0, -1);
        assertNotNull("tripleUids null", tripleUids);
        assertFalse("tripleUids empty", tripleUids.isEmpty());
        assertTrue("tripleUid missing", tripleUids.contains("5573734"));

    }

    @Test
    public void getTripleUidsContainingSubjectCount() {
        Map<String, Integer> containCount = ibatisCodedNodeGraphDao.getTripleUidsContainingSubjectCount("2003",
                "roles", "C100051", "ncit",
                null, null, null,
                null, null, null);
        assertNotNull("containCount null", containCount);
        assertFalse("containCount empty", containCount.isEmpty());
        Integer testInt = containCount.get("Disease_May_Have_Finding");

        assertNotNull("value is null", testInt);
		assertEquals("value wrong", 1, testInt.intValue());

    }

    @Test
    public void getTripleUidsForMappingRelationsContainer() {
        List<String> tripleUids = ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer("13513003",
                "3", "13512003", "AutoToGMPMappings", null, 0, -1);
        assertNotNull("tripleUids null", tripleUids);
        assertFalse("tripleUids empty", tripleUids.isEmpty());
        assertTrue("triple exists", tripleUids.contains("13513034"));
    }

    @Test
    public void getTripleUidsForMappingRelationsContainerAndCodes() {
        List<ConceptReference> cRefs = new ArrayList<ConceptReference>();
        ConceptReference subject = new ConceptReference();
        subject.setConceptCode("C0001");
        cRefs.add(subject);
        List<Sort> sorts = new ArrayList<Sort>();
        sorts.add( new QualifierSort(ColumnSortType.SOURCE_CODE, Order.ASC,"some",null));
        String relationName = "AutoToGMPMappings";
        List<String> tripleUids = ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes("13513003"
        , "3", "13512003", relationName, cRefs, null, null, sorts, 0, -1);
        assertNotNull("tripleUids null 1", tripleUids);
//        assertFalse("tripleUids empty 1", tripleUids.isEmpty());
        tripleUids = ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes("13513003", "3",
        "13512003", relationName, null, cRefs, null, null, 0, -1);
        assertNotNull("tripleUids null 2", tripleUids);
//        assertFalse("tripleUids empty 2", tripleUids.isEmpty());
        tripleUids = ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes("13513003", "3",
        "13512003", relationName, null, null, cRefs, null, 0, -1);
        assertNotNull("tripleUids null 3", tripleUids);
//        assertFalse("tripleUids empty 3", tripleUids.isEmpty());
        tripleUids = ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes("13513003",
         relationName, cRefs, null, null);
        assertNotNull("tripleUids null 4", tripleUids);
        assertFalse("tripleUids empty 4", tripleUids.isEmpty());
        tripleUids = ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes("13513003",
         relationName, null, cRefs, null);
		assertNotNull("tripleUids null 5", tripleUids);
//		assertFalse("tripleUids empty 5", tripleUids.isEmpty());
        tripleUids = ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes("13513003",
         relationName, null, null, cRefs);
        assertNotNull("tripleUids null 6", tripleUids);
        assertFalse("tripleUids empty 6", tripleUids.isEmpty());
        tripleUids = ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer("13513003", "3", "13512003",
         relationName, null, 0, -1);
        assertNotNull("tripleUids null 7", tripleUids);
        assertFalse("tripleUids empty 7", tripleUids.isEmpty());
    }

    @Test
    public void getTriplesForMappingRelationsContainer() {
        List<String> tripleUids = new ArrayList<String>();
        tripleUids.add("13513029");
        tripleUids.add("13513034");
        tripleUids.add("13513041");
        List<? extends ResolvedConceptReference> tripleRef =
         ibatisCodedNodeGraphDao.getTriplesForMappingRelationsContainer("13513003", "3", "13512003",
         "AutoToGMPMappings", tripleUids);
        assertNotNull("ref null", tripleRef);
        assertFalse("refs empty", tripleRef.isEmpty());
        ResolvedConceptReference reference = tripleRef.get(0);
        assertNotNull("contained reference is null", reference);
        assertEquals("source codingScheme should be Automobiles", "Automobiles", reference.getCodeNamespace());

    }

    @Test
    public void getTriplesForMappingRelationsContainerAndCodesCount() {
        List<ConceptReference> cRefs = new ArrayList<ConceptReference>();
        ConceptReference conceptReference = new ConceptReference();
        conceptReference.setConceptCode("C10001");
        conceptReference.setCodeNamespace("ncit");
        cRefs.add(conceptReference);
        ConceptReference conceptReference1 = new ConceptReference();
        conceptReference1.setConceptCode("C405");
        conceptReference1.setCodeNamespace("ncit");
        cRefs.add(conceptReference1);
        int tripleCount = ibatisCodedNodeGraphDao.getTriplesForMappingRelationsContainerAndCodesCount("2003",
                "roles", cRefs, null, null);
        assertTrue("tripleCount empty 1", tripleCount > 0);
//TODO better error handling when the references come up emtpy
        tripleCount = ibatisCodedNodeGraphDao.getTriplesForMappingRelationsContainerAndCodesCount("2003",
                "roles", null, cRefs, null);
        assertTrue("tripleCount empty 2", tripleCount > 0);
        tripleCount = ibatisCodedNodeGraphDao.getTriplesForMappingRelationsContainerAndCodesCount("2003",
                "roles", cRefs, null, cRefs);
        assertTrue("tripleCount empty 3", tripleCount > 0);
    }

    @Test
    public void getTriplesForMappingRelationsContainerCount() {
        int tripleCount = ibatisCodedNodeGraphDao.getTriplesForMappingRelationsContainerCount("2003", "roles");
        assertTrue("count is 0", tripleCount > 0);
		assertEquals("triple count wrong", 1786644, tripleCount);
    }

    @Test
    public void getValidPredicatesForTargetandSourceOf() {
        List<String> predicates = ibatisCodedNodeGraphDao.getValidPredicatesForTargetandSourceOf("2003", "C10000");
        assertNotNull("predicates null", predicates);
        assertTrue("predicates empty", predicates.size() > 0);
        assertTrue("predicate missing", predicates.contains("Chemotherapy_Regimen_Has_Component"));
    }

    @Test
    public void getValidSexTuplesOfAssociation() {
        List<Sextuple> sextuples = ibatisCodedNodeGraphDao.getValidSexTuplesOfAssociation("2003", "2067");
        assertNotNull("sextuples null", sextuples);
        assertFalse("sextuples empty", sextuples.isEmpty());
        assertEquals("sextuples wrong size", 11, sextuples.size());
		assertEquals("sextuple missing", "2067", sextuples.get(0).getAssociationPredicateId());
    }

    @Test
    public void getValidTriplesOfAssociation() {
        List<Triple> triples = ibatisCodedNodeGraphDao.getValidTriplesOfAssociation("2003", "2076");
        assertNotNull("triples null", triples);
        assertTrue("triples empty", triples.size() > 0);
        Triple triple = new Triple();
        triple.setSourceEntityNamespace("ncit");
        triple.setTargetEntityNamespace("ncit");
        triple.setSourceEntityCode("C105555");
        triple.setTargetEntityCode("C36115");
        triple.setAssociationPredicateId("2076");
        assertTrue("triple missing", triples.contains(triple));
    }

    @Test
    public void listCodeRelationships() {
        List<String> codeRel = ibatisCodedNodeGraphDao.listCodeRelationships("2003",
                "roles", "C10001", "ncit",
                "C405", "ncit", null,
                null, null, null, null,
                null, null, Boolean.FALSE, true);
        assertNotNull("codeRel null", codeRel);
        assertTrue("codeRel empty", codeRel.size() > 0);
        assertTrue("codeRel missing", codeRel.contains("REPLACE"));
    }

    @Test
    public void testGetTriplesForMappingRelationsContainer() {
        List<Triple> triples = ibatisCodedNodeGraphDao.getTriplesForMappingRelationsContainer("2003", "roles");
        assertNotNull("triples null", triples);
        assertTrue("triples empty", triples.size() > 0);
        Triple triple = new Triple();
        triple.setSourceEntityNamespace("ncit");
        triple.setTargetEntityNamespace("ncit");
        triple.setSourceEntityCode("C10000");
        triple.setTargetEntityCode("C61007");
        triple.setAssociationPredicateId("2066");
        boolean contained = triples.contains(triple);
        assertTrue("triple missing", contained);
    }

    @Test
    public void validateNodeInAssociation() {
        Integer validation = ibatisCodedNodeGraphDao.validateNodeInAssociation("2003", "2076", "C140518");
        assertNotNull("validation null", validation);
        assertTrue("should be valid", validation > 0);
    }


//	@Before
//	public void loadCodingScheme() throws Exception{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//				"values ('1', 'csname', 'csuri', 'csversion')");
//	}
//
//	@Test
//	public void testListCodeRelationshipsNoTransitive() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			null, null, false);
//
//
//		assertEquals(1, rels.size());
//		assertTrue(rels.contains("1"));
//	}
//
//	@Test
//	public void testListCodeRelationshipsEntityTypeRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//		"values ('2', '1', 't-code1', 't-ns1')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//		"values ('2', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			DaoUtility.createNonTypedList("concept"), null, true);
//
//
//		assertEquals(1, rels.size());
//		assertTrue(rels.contains("1"));
//	}
//
//	@Test
//	public void testListCodeRelationshipsEntityTypeRestrictionOneWrong() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'definition')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//		"values ('2', '1', 't-code1', 't-ns1')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//		"values ('2', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			DaoUtility.createNonTypedList("concept"), null, true);
//
//
//		assertEquals(0, rels.size());
//	}
//
//	@Test
//	public void testListCodeRelationshipsEntityTypeRestrictionBothWrong() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'definition')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//		"values ('2', '1', 't-code1', 't-ns1')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//		"values ('2', 'instance')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			DaoUtility.createNonTypedList("concept"), null, true);
//
//
//		assertEquals(0, rels.size());
//	}
//
//	@Test
//	public void testListCodeRelationshipsEntityTypeRestrictionTwoTypes() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'definition')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//		"values ('2', '1', 't-code1', 't-ns1')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//		"values ('2', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			DaoUtility.createNonTypedList("concept", "definition"), null, true);
//
//
//		assertEquals(1, rels.size());
//	}
//
//	@Test
//	public void testListCodeRelationshipsAnonymousRestrictionFalse() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//			"values ('1', '1', 's-code', 's-ns', '0')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//		"values ('2', '1', 't-code1', 't-ns1', '0')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			null, false, true);
//
//		assertEquals(1, rels.size());
//	}
//
//	@Test
//	public void testListCodeRelationshipsAnonymousRestrictionTrue() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//			"values ('1', '1', 's-code', 's-ns', '1')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//		"values ('2', '1', 't-code1', 't-ns1', '1')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			null, true, true);
//
//		assertEquals(1, rels.size());
//	}
//
//	@Test
//	public void testListCodeRelationshipsAnonymousRestrictionOneWrong() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//			"values ('1', '1', 's-code', 's-ns', '1')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//		"values ('2', '1', 't-code1', 't-ns1', '0')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			null, true, true);
//
//		assertEquals(0, rels.size());
//	}
//
//	@Test
//	public void testListCodeRelationshipsWithTransitive() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('12', '1', 'apname2')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentitytr" +
//				" values ('1'," +
//				" '12'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//				" null)");
//
//		List<String> rels = ibatisCodedNodeGraphDao.
//			listCodeRelationships("1", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null,
//			null, null, true);
//
//		assertEquals(2, rels.size());
//		assertTrue(rels.contains("1"));
//		assertTrue(rels.contains("12"));
//	}
//
//	@Test
//	public void testGetTripleUids() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		List<String> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubject(
//					"1",
//					"1",
//					"s-code",
//					"s-ns",
//					null,
//					null,
//					null,
//					null,
//					null,
//					null,
//					null,
//					0,
//					-1);
//
//		assertEquals(2, uids.size());
//		assertTrue(uids.contains("1"));
//		assertTrue(uids.contains("2"));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCount() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubjectCount(
//					"1", null, "s-code", "s-ns", null, null, null, null, null, null);
//
//		assertEquals(1, uids.keySet().size());
//		assertEquals(new Integer(2), uids.get("apname"));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingObjectCount() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingObjectCount(
//					"1", null, "t-code1", "t-ns1", null, null, null, null, null, null);
//
//		assertEquals(1, uids.keySet().size());
//		assertEquals(new Integer(1), uids.get("apname"));
//	}
//
//	@Test
//	public void testAssociatedConceptsSourceOf() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<? extends AssociatedConcept> associatedConcepts =
//			ibatisCodedNodeGraphDao.getAssociatedConceptsFromUid(
//					"1",
//					DaoUtility.createNonTypedList("1", "2"),
//					null,
//					TripleNode.SUBJECT);
//
//		assertEquals(2,associatedConcepts.size());
//	}
//
//	@Test
//	public void testAssociatedConceptsSourceOfWithSortDesc() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 'as-code', " +
//				" 'as-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'bs-code', " +
//				" 'bs-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Sort sort = new Sort(ColumnSortType.CODE, Order.DESC);
//
//		List<? extends AssociatedConcept> associatedConcepts =
//			ibatisCodedNodeGraphDao.getAssociatedConceptsFromUid(
//					"1",
//					DaoUtility.createNonTypedList("1", "2"),
//					Arrays.asList(sort),
//					TripleNode.SUBJECT);
//
//		assertEquals(2,associatedConcepts.size());
//		assertEquals("bs-code", associatedConcepts.get(0).getCode());
//		assertEquals("as-code", associatedConcepts.get(1).getCode());
//	}
//
//	@Test
//	public void testAssociatedConceptsSourceOfWithSortAsc() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 'as-code', " +
//				" 'as-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'bs-code', " +
//				" 'bs-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Sort sort = new Sort(ColumnSortType.CODE, Order.ASC);
//
//		List<? extends AssociatedConcept> associatedConcepts =
//			ibatisCodedNodeGraphDao.getAssociatedConceptsFromUid(
//					"1",
//					DaoUtility.createNonTypedList("1", "2"),
//					Arrays.asList(sort),
//					TripleNode.SUBJECT);
//
//		assertEquals(2,associatedConcepts.size());
//		assertEquals("as-code", associatedConcepts.get(0).getCode());
//		assertEquals("bs-code", associatedConcepts.get(1).getCode());
//	}
//
//	@Test
//	public void testAssociatedConceptTargetOf() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//		"values ('2', '1', 't-code', 't-ns')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<? extends AssociatedConcept> associatedConcepts = ibatisCodedNodeGraphDao.getAssociatedConceptsFromUid(
//				"1",
//				DaoUtility.createNonTypedList("1"), null, TripleNode.OBJECT);
//
//		assertEquals(1,associatedConcepts.size());
//		AssociatedConcept associatedConcept = associatedConcepts.get(0);
//
//		assertEquals("t-code",associatedConcept.getCode());
//		assertEquals("t-ns",associatedConcept.getCodeNamespace());
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithAnonymousRestrictionWithNone() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Map<String,Integer> uids =
//			ibatisCodedNodeGraphDao.
//				getTripleUidsContainingSubjectCount("1", null, "s-code", "s-ns", null, null, null, null, null, false);
//
//		assertTrue(uids.isEmpty());
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithAnonymousRestrictionOnlyAnonymous() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//			"values ('1', '1', 's-code', 's-ns', '1')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//			"values ('2', '1', 't-code', 't-ns', '1')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Map<String,Integer> uids =
//			ibatisCodedNodeGraphDao.
//				getTripleUidsContainingSubjectCount("1", null, "s-code", "s-ns", null, null, null, null, null, true);
//
//		assertEquals(1,uids.size());
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithAnonymousRestrictionOnlyNonAnonymous() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		isAnonymous) " +
//			"values ('2', '1', 't-code', 't-ns', '0')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('2', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Map<String,Integer> uids =
//			ibatisCodedNodeGraphDao.
//				getTripleUidsContainingSubjectCount("1", null, "s-code", "s-ns", null, null, null, null, null, false);
//
//		assertEquals(1,uids.size());
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithEntityTypeRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 't-code', 't-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Map<String,Integer> uids =
//			ibatisCodedNodeGraphDao.
//				getTripleUidsContainingSubjectCount("1", null, "s-code", "s-ns", null, null, null, null, DaoUtility
//				.createNonTypedList("concept"), null);
//
//		assertEquals(1, uids.keySet().size());
//		assertEquals(new Integer(1), uids.get("apname"));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithWrongEntityTypeRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Map<String,Integer> uids =
//			ibatisCodedNodeGraphDao.
//				getTripleUidsContainingSubjectCount("1", null, "s-code", "s-ns", null, null, null, null, DaoUtility
//				.createNonTypedList("WRONG_ENTITY_TYPE"), null);
//
//		assertTrue(uids.isEmpty());
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithMultipleEntityTypeRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 't-code', 't-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'some_other_type')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		Map<String,Integer> uids =
//			ibatisCodedNodeGraphDao.
//				getTripleUidsContainingSubjectCount("1", null, "s-code", "s-ns", null, null, null, null, DaoUtility
//				.createNonTypedList("some_other_type"), null);
//
//		assertEquals(1, uids.keySet().size());
//		assertEquals(new Integer(1), uids.get("apname"));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithQualifierName() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'1'," +
//				"'qualName'," +
//				"'qualValue'," +
//				"'1' )");
//		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
//		list.add(new QualifierNameValuePair("qualName", null));
//
//		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubjectCount(
//					"1", null, "s-code", "s-ns", null, list, null, null, null, null);
//
//		assertEquals(1, uids.keySet().size());
//		assertEquals(new Integer(1), uids.get("apname"));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithBadQualifierName() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'1'," +
//				"'qualName'," +
//				"'qualValue'," +
//				"'1' )");
//		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
//		list.add(new QualifierNameValuePair("BAD_qualName", null));
//
//		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubjectCount(
//					"1", null, "s-code", "s-ns", null, list, null, null, null, null);
//
//		assertEquals(0, uids.keySet().size());
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithQualifierNameAndValue() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'1'," +
//				"'qualName'," +
//				"'qualValue'," +
//				"'1'  )");
//		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
//		list.add(new QualifierNameValuePair("qualName", "qualValue"));
//
//		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubjectCount(
//					"1", null, "s-code", "s-ns", null, list, null, null, null, null);
//
//		assertEquals(1, uids.keySet().size());
//		assertEquals(new Integer(1), uids.get("apname"));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithQualifierNameAndBadValue() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'1'," +
//				"'qualName'," +
//				"'qualValue'," +
//				"'1' )");
//		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
//		list.add(new QualifierNameValuePair("qualName", "BAD_qualValue"));
//
//		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubjectCount(
//					"1", null, "s-code", "s-ns", null, list, null, null, null, null);
//
//		assertEquals(0, uids.keySet().size());
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithRestrictToCode() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'1'," +
//				"'qualName'," +
//				"'qualValue'," +
//				"'1' )");
//
//		CodeNamespacePair pair = new CodeNamespacePair("t-code2", "t-ns2");
//
//		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubjectCount(
//					"1", null, "s-code", "s-ns", null, null, DaoUtility.createNonTypedList(pair), null, null, null);
//
//		assertEquals(1, uids.keySet().size());
//		assertEquals(new Integer(1), uids.get("apname"));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectCountWithRestrictToCodesystem() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubjectCount(
//					"1", null, "s-code", "s-ns", null, null, null, DaoUtility.createNonTypedList("t-ns2"), null, null);
//
//		assertEquals(1, uids.keySet().size());
//		assertEquals(new Integer(1), uids.get("apname"));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectWithRestrictToCodesystem() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<String> uids = ibatisCodedNodeGraphDao.
//			getTripleUidsContainingSubject(
//					"1", "1", "s-code", "s-ns", null, null, null, DaoUtility.createNonTypedList("t-ns2"), null, null,
//					null, 0, -1);
//
//		assertEquals(1, uids.size());
//		assertEquals("2", uids.get(0));
//	}
//
//	@Test
//	public void testGetRootsWithNoAssociationRestrictionIndividually() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id2', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("1", null, null, null, null,
//		TraverseAssociations.INDIVIDUALLY, null, 0, -1);
//
//		assertEquals(1, uids.size());
//
//		assertEquals("s-code", uids.get(0).getCode());
//	}
//
//	public void testGetRootsWithNoAssociationRestrictionTogether() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("1", null, null, null, null,
//		TraverseAssociations.TOGETHER, null, 0, -1);
//
//		assertEquals(1, uids.size());
//
//		assertEquals("s-code", uids.get(0).getCode());
//	}
//
//	@Test
//	public void testGetRootsWithAssociationRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id2', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("1", DaoUtility.createNonTypedList("1"),
//		null, null, null,TraverseAssociations.TOGETHER, null, 0, -1);
//
//		assertEquals(1, uids.size());
//
//		assertEquals("s-code", uids.get(0).getCode());
//	}
//
//	@Test
//	public void testGetRootsWithWrongAssociationRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.
//		getRootNodes("1", DaoUtility.createNonTypedList("999"), null,null, null,
//				TraverseAssociations.INDIVIDUALLY, null, 0, -1);
//
//		assertEquals(0, uids.size());
//	}
//
//	@Test
//	public void testGetRootsWithOneWrongOneRightAssociationRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("1",
//				DaoUtility.createNonTypedList("999", "1"), null,null, null, TraverseAssociations.TOGETHER, null, 0,
//				-1);
//
//		assertEquals(1, uids.size());
//
//		assertEquals("s-code", uids.get(0).getCode());
//	}
//
//	@Test
//	public void testGetRootsWithQualifierRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnquals" +
//				" values ('1'," +
//				" '2'," +
//				" 'test', " +
//				" 'testValue'," +
//				" null )");
//
//		QualifierNameValuePair quals = new QualifierNameValuePair("test","testValue");
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("1",
//				DaoUtility.createNonTypedList("1"), Arrays.asList(quals), null, null,TraverseAssociations.TOGETHER,
//				null, 0, -1);
//
//		assertEquals(1, uids.size());
//
//		assertEquals("t-code1", uids.get(0).getCode());
//	}
//
//	@Test
//	public void testGetTailsWithNoAssociationRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("1", null, null, null, null,
//		TraverseAssociations.INDIVIDUALLY, null, 0, -1);
//
//		assertEquals(1, uids.size());
//
//		assertEquals("t-code2", uids.get(0).getCode());
//	}
//
//	@Test
//	public void testGetTailsWithAssociationRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("1", DaoUtility.createNonTypedList("1"),
//		null, null, null,TraverseAssociations.INDIVIDUALLY, null, 0, -1);
//
//		assertEquals(1, uids.size());
//
//		assertEquals("t-code2", uids.get(0).getCode());
//	}
//
//	@Test
//	public void testGetTailsWithWrongAssociationRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("1", DaoUtility.createNonTypedList("999"),
//		null, null, null, TraverseAssociations.INDIVIDUALLY, null, 0, -1);
//
//		assertEquals(0, uids.size());
//	}
//
//	@Test
//	public void testGetTailsWithOneWrongOneRightAssociationRestriction() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 't-code1', " +
//				" 't-ns1'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//
//		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("1", DaoUtility.createNonTypedList("999",
//		"1"), null, null, null, TraverseAssociations.INDIVIDUALLY, null, 0, -1);
//
//		assertEquals(1, uids.size());
//
//		assertEquals("t-code2", uids.get(0).getCode());
//	}
//
//	@Test
//	public void testGetCountConceptReferencesContainingSubject() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref = new ConceptReference();
//		ref.setCode("s-code");
//		ref.setCodeNamespace("s-ns");
//
//		List<ConceptReference> codeList = Arrays.asList(ref);
//
//		List<CountConceptReference> refs =
//			ibatisCodedNodeGraphDao.
//				getCountConceptReferencesContainingSubject("1", null, codeList, null, null, null, null, null, null);
//
//		assertEquals(1,refs.size());
//
//		assertEquals("s-code", refs.get(0).getCode());
//		assertEquals("s-ns", refs.get(0).getCodeNamespace());
//	}
//
//	@Test
//	public void testGetCountConceptReferencesContainingObject() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref = new ConceptReference();
//		ref.setCode("t-code");
//		ref.setCodeNamespace("t-ns");
//
//		List<ConceptReference> codeList = Arrays.asList(ref);
//
//		List<CountConceptReference> refs =
//			ibatisCodedNodeGraphDao.
//				getCountConceptReferencesContainingObject("1", null, codeList, null, null, null, null, null, null);
//
//		assertEquals(1,refs.size());
//
//		assertEquals("t-code", refs.get(0).getCode());
//		assertEquals("t-ns", refs.get(0).getCodeNamespace());
//	}
//
//	@Test
//	public void testGetConceptReferencesContainingSubject() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref = new ConceptReference();
//		ref.setCode("s-code");
//		ref.setCodeNamespace("s-ns");
//
//		List<ConceptReference> codeList = Arrays.asList(ref);
//
//		List<ConceptReference> refs =
//			ibatisCodedNodeGraphDao.
//				getConceptReferencesContainingSubject("1", null, codeList, null, null, null, null, null, null, null,
//				0, -1);
//
//		assertEquals(1,refs.size());
//
//		assertEquals("t-code", refs.get(0).getCode());
//		assertEquals("t-ns", refs.get(0).getCodeNamespace());
//	}
//
//	@Test
//	public void testGetConceptReferencesContainingSubjectWithMultiple() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref = new ConceptReference();
//		ref.setCode("s-code");
//		ref.setCodeNamespace("s-ns");
//
//		List<ConceptReference> codeList = Arrays.asList(ref);
//
//		List<ConceptReference> refs =
//			ibatisCodedNodeGraphDao.
//				getConceptReferencesContainingSubject("1", null, codeList, null, null, null, null, null, null, null,
//				0, -1);
//
//		assertEquals(2,refs.size());
//
//		assertTrue(TestUtils.containsConceptReference(refs, "t-code"));
//		assertTrue(TestUtils.containsConceptReference(refs, "t-code2"));
//	}
//
//	@Test
//	public void testGetConceptReferencesContainingObject() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref = new ConceptReference();
//		ref.setCode("t-code");
//		ref.setCodeNamespace("t-ns");
//
//		List<ConceptReference> codeList = Arrays.asList(ref);
//
//		List<ConceptReference> refs =
//			ibatisCodedNodeGraphDao.
//				getConceptReferencesContainingObject("1", null, codeList, null, null, null, null, null, null, null, 0,
//				-1);
//
//		assertEquals(1,refs.size());
//
//		assertEquals("s-code", refs.get(0).getCode());
//		assertEquals("s-ns", refs.get(0).getCodeNamespace());
//	}
//
//	@Test
//	public void testGetConceptReferencesContainingObjectWithLimit() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code2', " +
//				" 's-ns2'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref = new ConceptReference();
//		ref.setCode("t-code");
//		ref.setCodeNamespace("t-ns");
//
//		List<ConceptReference> codeList = Arrays.asList(ref);
//
//		List<ConceptReference> refs =
//			ibatisCodedNodeGraphDao.
//				getConceptReferencesContainingObject("1", null, codeList, null, null, null, null, null, null, null, 0,
//				1);
//
//		assertEquals(1,refs.size());
//
//		ConceptReference firstResult = refs.get(0);
//
//		refs =
//			ibatisCodedNodeGraphDao.
//				getConceptReferencesContainingObject("1", null, codeList, null, null, null, null, null, null, null, 1,
//				1);
//
//		ConceptReference secondResult = refs.get(0);
//
//		assertFalse(firstResult.getCode().equals(secondResult.getCode()));
//	}
//
//	@Test
//	public void testGetTripleUidsContainingSubjectWithSort() throws SQLException{
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 's-code', 's-ns')");
//
//		template.execute("Insert into entitytype (entityGuid, entityType) " +
//			"values ('1', 'concept')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//		"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id2', null, null, null, null, null, null, null, null)");
//
//		Sort sort = new Sort(ColumnSortType.CODE, Order.DESC);
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsContainingSubject("1", null, "s-code", "s-ns", null, null, null,
//			null, null, null, Arrays.asList(sort), 0, -1);
//
//		assertEquals(2, uids.size());
//		assertEquals("2", uids.get(0));
//		assertEquals("1", uids.get(1));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerNoSorts() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('1', '2', 's-code', 's-ns', 'source-mapping-description')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('2', '3', 't-code', 't-ns', 'target-mapping-description')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer("1", "2", "3", "c-name", null, 0, -1);
//
//		assertEquals(1,uids.size());
//		assertEquals("1", uids.get(0));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerSourceEnityCodeSortAsc() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('1', '2', 's-code', 's-ns', 'source-mapping-description')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('2', '3', 't-code', 't-ns', 'target-mapping-description')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		MappingSortOption sort = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer(
//					"1",
//					"2",
//					"3",
//					"c-name",
//					DaoUtility.mapMappingSortOptionListToSort(Arrays.asList(sort)).getSorts(),
//					0,
//					-1);
//
//		assertEquals(2,uids.size());
//		assertEquals("2", uids.get(0));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerSourceEnityCodeSortDesc() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('1', '2', 's-code', 's-ns', 'source-mapping-description')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('2', '3', 't-code', 't-ns', 'target-mapping-description')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 'a-t-code1'," +
//				" 'a-t-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		MappingSortOption sort = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.DESC);
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer(
//					"1",
//					"2",
//					"3",
//					"c-name",
//					DaoUtility.mapMappingSortOptionListToSort(Arrays.asList(sort)).getSorts(),
//					0,
//					-1);
//
//		assertEquals(2,uids.size());
//		assertEquals("1", uids.get(0));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerTargetEnityCodeSortAsc() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('1', '2', 's-code', 's-ns', 'source-mapping-description')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('2', '3', 't-code', 't-ns', 'target-mapping-description')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 'a-t-code'," +
//				" 'a-t-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		MappingSortOption sort = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer(
//					"1",
//					"2",
//					"3",
//					"c-name",
//					DaoUtility.mapMappingSortOptionListToSort(Arrays.asList(sort)).getSorts(),
//					0,
//					-1);
//
//		assertEquals(2,uids.size());
//		assertEquals("1", uids.get(0));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerTargetEnityCodeSortDesc() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('1', '2', 's-code', 's-ns', 'source-mapping-description')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('2', '3', 't-code', 't-ns', 'target-mapping-description')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 'a-t-code'," +
//				" 'a-t-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		MappingSortOption sort = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.DESC);
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer(
//					"1",
//					"2",
//					"3",
//					"c-name",
//					DaoUtility.mapMappingSortOptionListToSort(Arrays.asList(sort)).getSorts(),
//					0,
//					-1);
//
//		assertEquals(2,uids.size());
//		assertEquals("2", uids.get(0));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerSourceEnityDescriptionSortAsc() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('1', '2', 's-code1', 's-ns1', 'b-source-mapping-description')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('2', '2', 's-code2', 's-ns2', 'a-source-mapping-description')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code1', " +
//				" 's-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code2', " +
//				" 's-ns2'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		MappingSortOption sort = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer(
//					"1",
//					"2",
//					"3",
//					"c-name",
//					DaoUtility.mapMappingSortOptionListToSort(Arrays.asList(sort)).getSorts(),
//					0,
//					-1);
//
//		assertEquals(2,uids.size());
//		assertEquals("2", uids.get(0));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerSourceEnityDescriptionSortDesc() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('1', '2', 's-code1', 's-ns1', 'b-source-mapping-description')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace,
//		description) " +
//			"values ('2', '2', 's-code2', 's-ns2', 'a-source-mapping-description')");
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code1', " +
//				" 's-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code2', " +
//				" 's-ns2'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		MappingSortOption sort = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction
//		.DESC);
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer(
//					"1",
//					"2",
//					"3",
//					"c-name",
//					DaoUtility.mapMappingSortOptionListToSort(Arrays.asList(sort)).getSorts(),
//					0,
//					-1);
//
//		assertEquals(2,uids.size());
//		assertEquals("1", uids.get(0));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerQualifierSortAsc() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code1', " +
//				" 's-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'1'," +
//				"'qualName'," +
//				"'2'," +
//				"'1' )");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code2', " +
//				" 's-ns2'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'2', " +
//				"'2'," +
//				"'qualName'," +
//				"'1'," +
//				"'2' )");
//
//		MappingSortOption sort = new QualifierSortOption(Direction.ASC, "qualName");
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer(
//					"1",
//					"2",
//					"3",
//					"c-name",
//					DaoUtility.mapMappingSortOptionListToSort(Arrays.asList(sort)).getSorts(),
//					0,
//					-1);
//
//		assertEquals(2,uids.size());
//		assertEquals("2", uids.get(0));
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerQualifierSortDesc() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code1', " +
//				" 's-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'1'," +
//				"'qualName'," +
//				"'2'," +
//				"'1' )");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code2', " +
//				" 's-ns2'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'2', " +
//				"'2'," +
//				"'qualName'," +
//				"'1'," +
//				"'2' )");
//
//		MappingSortOption sort = new QualifierSortOption(Direction.DESC, "qualName");
//
//		List<String> uids =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainer(
//					"1",
//					"2",
//					"3",
//					"c-name",
//					DaoUtility.mapMappingSortOptionListToSort(Arrays.asList(sort)).getSorts(),
//					0,
//					-1);
//
//		assertEquals(2,uids.size());
//		assertEquals("1", uids.get(0));
//	}
//
//	@Test
//	public void testGetTriplesForMappingRelationsCount() throws Exception {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('2', 'sourcecsname', 'csuri-source', 'csversion')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri,
//		representsVersion) " +
//			"values ('3', 'targetcsname', 'csuri-target', 'csversion')");
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-source", "csversion"));
//
//		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri-target", "csversion"));
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code1', " +
//				" 's-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'1', " +
//				"'1'," +
//				"'qualName'," +
//				"'2'," +
//				"'1' )");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'2', " +
//				"'1'," +
//				"'qualName2'," +
//				"'22'," +
//				"'2' )");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code2', " +
//				" 's-ns2'," +
//				" 't-code2'," +
//				" 't-ns2'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into " +
//				"entityassnquals values ( " +
//				"'3', " +
//				"'2'," +
//				"'qualName'," +
//				"'1'," +
//				"'3' )");
//
//		int count =
//			ibatisCodedNodeGraphDao.getTriplesForMappingRelationsContainerCount(
//					"1",
//					"c-name");
//
//		assertEquals(2,count);
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerAndCodesWithOne() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref = new ConceptReference();
//		ref.setCode("s-code");
//
//		List<ConceptReference> refList = Arrays.asList(ref);
//
//		int count =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes(
//					"1",
//					"c-name",
//					refList,
//					null,
//					null
//					).size();
//
//		assertEquals(1,count);
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerAndCodesWithOneSourceAndTarget() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref1 = new ConceptReference();
//		ref1.setCode("s-code");
//
//		ConceptReference ref2 = new ConceptReference();
//		ref2.setCode("t-code");
//
//		List<ConceptReference> refList = Arrays.asList(ref1,ref2);
//
//		int count =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes(
//					"1",
//					"c-name",
//					refList,
//					refList,
//					null
//					).size();
//
//		assertEquals(1,count);
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerAndCodesWithOneSourceAndTargetAndOrSource(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref1 = new ConceptReference();
//		ref1.setCode("s-code");
//
//		ConceptReference ref2 = new ConceptReference();
//		ref2.setCode("t-code");
//
//		ConceptReference ref3 = new ConceptReference();
//		ref3.setCode("s-code");
//
//		List<ConceptReference> refList = Arrays.asList(ref1,ref2);
//
//		List<ConceptReference> orList = Arrays.asList(ref3);
//
//		int count =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes(
//					"1",
//					"c-name",
//					refList,
//					refList,
//					orList
//					).size();
//
//		assertEquals(1,count);
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerAndCodesWithOneSourceAndTargetAndOrTarget(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref1 = new ConceptReference();
//		ref1.setCode("s-code");
//
//		ConceptReference ref2 = new ConceptReference();
//		ref2.setCode("t-code");
//
//		ConceptReference ref3 = new ConceptReference();
//		ref3.setCode("t-code");
//
//		List<ConceptReference> refList = Arrays.asList(ref1,ref2);
//
//		List<ConceptReference> orList = Arrays.asList(ref3);
//
//		int count =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes(
//					"1",
//					"c-name",
//					refList,
//					refList,
//					orList
//					).size();
//
//		assertEquals(1,count);
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerAndCodesWithOneSourceAndTargetAndOrInvalid(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 'a-s-code', " +
//				" 'a-s-ns1'," +
//				" 't-code1'," +
//				" 't-ns1'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref1 = new ConceptReference();
//		ref1.setCode("s-code");
//
//		ConceptReference ref2 = new ConceptReference();
//		ref2.setCode("t-code");
//
//		ConceptReference ref3 = new ConceptReference();
//		ref3.setCode("___INVALID___");
//
//		List<ConceptReference> refList = Arrays.asList(ref1,ref2);
//
//		List<ConceptReference> orList = Arrays.asList(ref3);
//
//		int count =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes(
//					"1",
//					"c-name",
//					refList,
//					refList,
//					orList
//					).size();
//
//		assertEquals(0,count);
//	}
//
//	@Test
//	public void testGetTripleUidsForMappingRelationsContainerAndCodesWithTwo() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("insert into " +
//				"relation (relationGuid, codingSchemeGuid, containerName) " +
//		"values ('1', '1', 'c-name')");
//
//		template.execute("insert into " +
//				"associationpredicate (associationPredicateGuid," +
//				"relationGuid, associationName) values " +
//				"('1', '1', 'apname')");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('1'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 't-code'," +
//				" 't-ns'," +
//		" 'ai-id1', null, null, null, null, null, null, null, null)");
//
//		template.execute("insert into entityassnstoentity" +
//				" values ('2'," +
//				" '1'," +
//				" 's-code', " +
//				" 's-ns'," +
//				" 'some-other-code'," +
//				" 'some-other-namespace'," +
//		" 'ai-id2', null, null, null, null, null, null, null, null)");
//
//		ConceptReference ref = new ConceptReference();
//		ref.setCode("s-code");
//
//		List<ConceptReference> refList = Arrays.asList(ref);
//
//		int count =
//			ibatisCodedNodeGraphDao.getTripleUidsForMappingRelationsContainerAndCodes(
//					"1",
//					"c-name",
//					refList,
//					null,
//					null
//					).size();
//
//		assertEquals(2,count);
//	}
}