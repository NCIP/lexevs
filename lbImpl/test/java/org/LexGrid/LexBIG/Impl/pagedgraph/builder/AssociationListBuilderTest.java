
package org.LexGrid.LexBIG.Impl.pagedgraph.builder;

import java.util.Iterator;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.StubReturningCycleDetectingCallback;
import org.junit.Test;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;
import org.springframework.jdbc.core.JdbcTemplate;

public class AssociationListBuilderTest extends LexEvsDbUnitTestBase{

    @Resource
    private Registry registry;
    
    @Test
    public void testAssociations() throws Exception {
        JdbcTemplate template = new JdbcTemplate(this.getDataSource());

        registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("uri", "version"));
        
        template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
        "values ('cs-guid', 'csname', 'uri', 'version')");

        template.execute("insert into " +
                "relation (relationGuid, codingSchemeGuid, containerName) " +
        "values ('rel-guid', 'cs-guid', 'c-name')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid', 'rel-guid', 'assocName')");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid1'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code1'," +
                " 't-ns1'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid2'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
    
        AssociationListBuilder builder = new AssociationListBuilder();
        
        GraphQuery query = new GraphQuery();
        
        AssociationList list =
            builder.buildSourceOfAssociationList(
                    "uri", 
                    "version", 
                    "s-code", 
                    "s-ns", 
                    null, 
                    true,
                    true,
                    -1,
                    -1, 
                    -1,
                    query, null, null, null, null, null);
        
        assertNotNull(list);
        assertEquals(1, list.getAssociationCount());
        assertEquals("assocName", list.getAssociation(0).getAssociationName());
    }
    
    @Test
    public void testAssociationsWithAssociationRestriction() throws Exception {
        JdbcTemplate template = new JdbcTemplate(this.getDataSource());

        registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("uri", "version"));
        
        template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
        "values ('cs-guid', 'csname', 'uri', 'version')");

        template.execute("insert into " +
                "relation (relationGuid, codingSchemeGuid, containerName) " +
        "values ('rel-guid', 'cs-guid', 'c-name')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid', 'rel-guid', 'assocName')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid2', 'rel-guid', 'assocName2')");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid1'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code1'," +
                " 't-ns1'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid2'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
    
        AssociationListBuilder builder = new AssociationListBuilder();
        
        GraphQuery query = new GraphQuery();
        query.getRestrictToAssociations().add("assocName2");
        
        AssociationList list =
            builder.buildSourceOfAssociationList(
                    "uri", "version", "s-code", "s-ns", 
                    null, 
                    true,
                    true,
                    -1,
                    -1, 
                    -1,
                    query, null, null, null, null, null);
        
        assertNotNull(list);
        assertEquals(1, list.getAssociationCount());
        assertEquals("assocName2", list.getAssociation(0).getAssociationName());
    }
    
    @Test
    public void testAssociationsWithAssociationAndQualifierRestriction() throws Exception {
        JdbcTemplate template = new JdbcTemplate(this.getDataSource());

        registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("uri", "version"));
        
        template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
        "values ('cs-guid', 'csname', 'uri', 'version')");

        template.execute("insert into " +
                "relation (relationGuid, codingSchemeGuid, containerName) " +
        "values ('rel-guid', 'cs-guid', 'c-name')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid', 'rel-guid', 'assocName')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid2', 'rel-guid', 'assocName2')");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid1'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code1'," +
                " 't-ns1'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid2'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid3'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into " +
                "entityassnquals values ( " +
                "'eaeq-guid', " +
                "'eae-guid3'," +
                "'qualName'," +
                "'qualValue'," +
                "'1' )");
    
        AssociationListBuilder builder = new AssociationListBuilder();
        
        GraphQuery query = new GraphQuery();
        query.getRestrictToAssociations().add("assocName2");
        query.getRestrictToAssociationsQualifiers().add(new QualifierNameValuePair("qualName", null));
        
        AssociationList list =
            builder.buildSourceOfAssociationList(
                    "uri", "version", "s-code", "s-ns", null,
                    true,
                    true,
                    -1,
                    -1, 
                    -1,
                    query, null, null, null, null, null);
        
        assertNotNull(list);
        assertEquals(1, list.getAssociationCount());
        assertEquals("assocName2", list.getAssociation(0).getAssociationName());
        assertEquals(1, list.getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());
    }
    
    @Test
    public void testAssociationsWithAssociationAndQualifierNameAndValueRestriction() throws Exception {
        JdbcTemplate template = new JdbcTemplate(this.getDataSource());

        registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("uri", "version"));
        
        template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
        "values ('cs-guid', 'csname', 'uri', 'version')");

        template.execute("insert into " +
                "relation (relationGuid, codingSchemeGuid, containerName) " +
        "values ('rel-guid', 'cs-guid', 'c-name')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid', 'rel-guid', 'assocName')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid2', 'rel-guid', 'assocName2')");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid1'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code1'," +
                " 't-ns1'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid2'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid3'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into " +
                "entityassnquals values ( " +
                "'eaeq-guid', " +
                "'eae-guid3'," +
                "'qualName'," +
                "'qualValue'," +
                "'1' )");
    
        AssociationListBuilder builder = new AssociationListBuilder();
        
        GraphQuery query = new GraphQuery();
        query.getRestrictToAssociations().add("assocName2");
        query.getRestrictToAssociationsQualifiers().add(new QualifierNameValuePair("qualName", "qualValue"));
        
        AssociationList list =
            builder.buildSourceOfAssociationList(
                    "uri", "version", "s-code", "s-ns", 
                    null, true,
                    true,
                    -1,
                    -1, 
                    -1,
                    query, null, null, null, null, null);
        
        assertNotNull(list);
        assertEquals(1, list.getAssociationCount());
        assertEquals("assocName2", list.getAssociation(0).getAssociationName());
        assertEquals(1, list.getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount());
    }
    
    @Test
    public void testAssociationsWithAssociationAndBadQualifierRestriction() throws Exception {
        JdbcTemplate template = new JdbcTemplate(this.getDataSource());

        registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("uri", "version"));
        
        template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
        "values ('cs-guid', 'csname', 'uri', 'version')");

        template.execute("insert into " +
                "relation (relationGuid, codingSchemeGuid, containerName) " +
        "values ('rel-guid', 'cs-guid', 'c-name')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid', 'rel-guid', 'assocName')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid2', 'rel-guid', 'assocName2')");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid1'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code1'," +
                " 't-ns1'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid2'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid3'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into " +
                "entityassnquals values ( " +
                "'eaeq-guid', " +
                "'eae-guid3'," +
                "'qualName'," +
                "'qualValue'," +
                "'1' )");
    
        AssociationListBuilder builder = new AssociationListBuilder();
        
        GraphQuery query = new GraphQuery();
        query.getRestrictToAssociations().add("assocName2");
        query.getRestrictToAssociationsQualifiers().add(new QualifierNameValuePair("qualNameBAD", null));
        
        AssociationList list =
            builder.buildSourceOfAssociationList(
                    "uri", "version", "s-code", "s-ns", 
                    null, 
                    true,
                    true,
                    -1,
                    -1, 
                    -1,
                    query, null, null, null, null, null);
        
        assertNull(list);
    }
    
    @Test
    public void testAssociationsWithAssociationAndBadQualifierNameAndValueRestriction() throws Exception {
        JdbcTemplate template = new JdbcTemplate(this.getDataSource());

        registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("uri", "version"));
        
        template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
        "values ('cs-guid', 'csname', 'uri', 'version')");

        template.execute("insert into " +
                "relation (relationGuid, codingSchemeGuid, containerName) " +
        "values ('rel-guid', 'cs-guid', 'c-name')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid', 'rel-guid', 'assocName')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid2', 'rel-guid', 'assocName2')");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid1'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code1'," +
                " 't-ns1'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid2'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid3'," +
                " 'ap-guid2'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into " +
                "entityassnquals values ( " +
                "'eaeq-guid', " +
                "'eae-guid3'," +
                "'qualName'," +
                "'qualValue'," +
                "'1' )");
    
        AssociationListBuilder builder = new AssociationListBuilder();
        
        GraphQuery query = new GraphQuery();
        query.getRestrictToAssociations().add("assocName2");
        query.getRestrictToAssociationsQualifiers().add(new QualifierNameValuePair("qualName", "BAD_VALUE"));
        
        AssociationList list =
            builder.buildSourceOfAssociationList(
                    "uri", "version", "s-code", "s-ns", 
                    null, 
                    true,
                    true,
                    -1,
                    -1, 
                    -1,
                    query, null, null, null, null, null);
        
        assertNull(list);
    }
    
    @Test
    public void testAssociatedConcepts() throws Exception {
        JdbcTemplate template = new JdbcTemplate(this.getDataSource());

        registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("uri", "version"));
        
        template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
        "values ('cs-guid', 'csname', 'uri', 'version')");

        template.execute("insert into " +
                "relation (relationGuid, codingSchemeGuid, containerName) " +
        "values ('rel-guid', 'cs-guid', 'c-name')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid', 'rel-guid', 'assocName')");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid1'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code1'," +
                " 't-ns1'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid2'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id', null, null, null, null, null, null, null, null)");
    
        AssociationListBuilder builder = new AssociationListBuilder();
        
        AssociationList list =
            builder.buildSourceOfAssociationList(
                    "uri", "version", "s-code", "s-ns", null, 
                    true,
                    true,
                    -1,
                    -1, 
                    -1,
                    new GraphQuery(), null, null, null, null, new StubReturningCycleDetectingCallback());
        
        assertNotNull(list);
        assertEquals(1, list.getAssociationCount());
        assertEquals("assocName", list.getAssociation(0).getAssociationName());
   
        assertNotNull(list.getAssociation(0).getAssociatedConcepts());
        
        assertNotNull(list.getAssociation(0).getAssociatedConcepts().iterateAssociatedConcept());
        
        Iterator<? extends AssociatedConcept> itr = 
            list.getAssociation(0).getAssociatedConcepts().iterateAssociatedConcept();
    
        assertTrue(itr.hasNext());
   
        AssociatedConcept assocConcept = itr.next();
        
        assertEquals("t-code1", assocConcept.getCode());
        
        assertTrue(itr.hasNext());
        
        assocConcept = itr.next();
        
        assertEquals("t-code2", assocConcept.getCode());
        
        assertFalse(itr.hasNext());
    }
    
    @Test
    public void testResolveGraphToClosure() throws Exception {
        JdbcTemplate template = new JdbcTemplate(this.getDataSource());

        registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("uri", "version"));
        
        template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
        "values ('cs-guid', 'csname', 'uri', 'version')");

        template.execute("insert into " +
                "relation (relationGuid, codingSchemeGuid, containerName) " +
        "values ('rel-guid', 'cs-guid', 'c-name')");
        
        template.execute("insert into " +
                "associationpredicate (associationPredicateGuid," +
                "relationGuid, associationName) values " +
        "('ap-guid', 'rel-guid', 'assocName')");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid1'," +
                " 'ap-guid'," +
                " 's-code', " +
                " 's-ns'," +
                " 't-code1'," +
                " 't-ns1'," +
        " 'ai-id1', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid2'," +
                " 'ap-guid'," +
                " 't-code1', " +
                " 't-ns1'," +
                " 't-code2'," +
                " 't-ns2'," +
        " 'ai-id2', null, null, null, null, null, null, null, null)");
        
        template.execute("insert into entityassnstoentity" +
                " values ('eae-guid3'," +
                " 'ap-guid'," +
                " 't-code2', " +
                " 't-ns2'," +
                " 't-code3'," +
                " 't-ns3'," +
        " 'ai-id3', null, null, null, null, null, null, null, null)");
    
        AssociationListBuilder builder = new AssociationListBuilder();
        
        AssociationList list =
            builder.buildSourceOfAssociationList(
                    "uri", "version", "s-code", "s-ns", null, 
                    true,
                    true,
                    -1,
                    -1, 
                    -1,
                    new GraphQuery(), null, null, null, null, new StubReturningCycleDetectingCallback());
        
        assertNotNull(list);
        assertEquals(1, list.getAssociationCount());
        assertEquals("assocName", list.getAssociation(0).getAssociationName());
   
        assertNotNull(list.getAssociation(0).getAssociatedConcepts());
        
        assertNotNull(list.getAssociation(0).getAssociatedConcepts().iterateAssociatedConcept());
        
        Iterator<? extends AssociatedConcept> itr = 
            list.getAssociation(0).getAssociatedConcepts().iterateAssociatedConcept();
    
        assertTrue(itr.hasNext());
   
        AssociatedConcept assocConcept = itr.next();
        
        assertEquals("t-code1", assocConcept.getCode());
        
        assertFalse(itr.hasNext());
        
        assertEquals(1, assocConcept.getSourceOf().getAssociationCount());
        
        assertEquals("t-code2", assocConcept.getSourceOf().
                getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getCode());
        
        assertFalse(itr.hasNext());
    }
}