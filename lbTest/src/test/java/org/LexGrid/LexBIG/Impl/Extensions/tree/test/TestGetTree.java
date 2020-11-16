package org.LexGrid.LexBIG.Impl.Extensions.tree.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.ChildTreeNodeIterator;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTree;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.PathToRootTreeServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeService;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeServiceFactory;
import org.LexGrid.LexBIG.Impl.Extensions.tree.utility.OntologyNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.utility.PrintUtility;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoveFromDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.TestCase;

public class TestGetTree extends TestCase{
	
	public PathToRootTreeServiceImpl pathToRootTreeServiceImpl;
	public LexBIGService lbs;
	public ChildTreeNodeIterator iterator;


	@Test
	public void testGetTreeWithMultipleHierarchyAsscNames() {
		 lbs = ServiceHolder.instance().getLexBIGService();
		  pathToRootTreeServiceImpl = (PathToRootTreeServiceImpl) TreeServiceFactory.getInstance().getTreeService(lbs);
		  iterator = pathToRootTreeServiceImpl.getTree("urn:oid:11.11.0.1", 
				  null, "005").getCurrentFocus().getChildIterator();
		  assertNotNull(iterator.next());
	}

	
	@Test
	public void testGetTreeWithMultipleHieØrarchyAsscDirectionNames() {
		 lbs = ServiceHolder.instance().getLexBIGService();
		  pathToRootTreeServiceImpl = (PathToRootTreeServiceImpl) TreeServiceFactory.getInstance().getTreeService(lbs);
		  iterator = pathToRootTreeServiceImpl.getTree("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
				  Constructors.createCodingSchemeVersionOrTagFromVersion("0.1.5"), "Patient").getCurrentFocus().getChildIterator();
		  assertNotNull(iterator.next());
	}
	
	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testGetTreeWithMultipleHierarchyAsscNamesPatient() {
		 lbs = ServiceHolder.instance().getLexBIGService();
		  pathToRootTreeServiceImpl = (PathToRootTreeServiceImpl) TreeServiceFactory.getInstance().getTreeService(lbs);
	        LexEvsTree tree = pathToRootTreeServiceImpl.getTree(
	        		"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", 
	                Constructors.createCodingSchemeVersionOrTagFromVersion("0.2.0"), "C12434");
	        LexEvsTreeNode node = tree.getCodeMap().get("C12434");
	       assertEquals(ExpandableStatus.IS_NOT_EXPANDABLE,  node.getExpandableStatus());
	}
	
	@Test
	public void testGetTreeWithMoreThanFiveChildren(){
        LexBIGService lbsi = LexBIGServiceImpl.defaultInstance();
        TreeService service = TreeServiceFactory.getInstance().getTreeService(lbsi);
        LexEvsTree tree = null;
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion("TestForMultiNamespace");
        tree = service.getTree("npo", csvt, "NPO_1607", "npo", "is_a");	            
            LexEvsTreeNode focusNode = tree.getCurrentFocus();
            List<LexEvsTreeNode> listForTree = service.getEvsTreeConverter().buildEvsTreePathFromRootTree(focusNode);
            String json = service.getJsonConverter(-1).buildJsonPathFromRootTree(focusNode);	        
            List <LexEvsTreeNode> listOfone = new ArrayList<LexEvsTreeNode>();
            listOfone.add(focusNode);
            System.out.println("Printing from focus Node");
            PrintUtility.print(focusNode);
            System.out.println("Printing tree");
            PrintUtility.print(listForTree);
            System.out.println("Printing Json");
            System.out.println(json);
            Gson gson = new Gson();
            OntologyNode[] nodes = gson.fromJson(json, OntologyNode[].class);
            assertTrue(nodes.length > 0);
            //Drilling down to the "process" node which has more than the default 5 children
            OntologyNode processNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("processual_entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("process")).findFirst().get();
            assertNotNull(processNode);
            assertEquals(processNode.getOntology_node_name(), "process");
            //child count just indicates there is or is not children
            assertTrue(processNode.getOntology_node_child_count() > 0);
            assertTrue(processNode.getOntology_node_child_count() == 1);
            //checking actual size
            assertTrue(processNode.getChildren_nodes().size() > 0);
            assertTrue(processNode.getChildren_nodes().size() == 20);
            //this wouldn't be seen using default children sizing
            assertTrue(processNode.getChildren_nodes().stream().anyMatch(x -> x.getOntology_node_name().equals("nanoparticle response to stimulus")));
            
            OntologyNode emulsificationNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("processual_entity")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("process")).findFirst().get()
                     .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("emulsification")).findFirst().get();
            assertNotNull(emulsificationNode);
            assertEquals(emulsificationNode.getOntology_node_name(), "emulsification");
            //child count just indicates there is or is not children
            assertTrue(emulsificationNode.getOntology_node_child_count() == 0);
            assertTrue(emulsificationNode.getOntology_node_child_count() < 1);
            //checking actual size
            assertTrue(emulsificationNode.getChildren_nodes().size() == 0);
            
            OntologyNode occurrentNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get();
            assertNotNull(occurrentNode);
            assertEquals(occurrentNode.getOntology_node_name(), "occurrent");
            //child count just indicates there is or is not children
            assertTrue(occurrentNode.getOntology_node_child_count() > 0);
            assertTrue(occurrentNode.getOntology_node_child_count() == 1);
            //checking actual size
            assertTrue(occurrentNode.getChildren_nodes().size() == 3);
            
	}
	
	@Test
	public void testGetTreeWithFiveOrLessChildren(){
        LexBIGService lbsi = LexBIGServiceImpl.defaultInstance();
        TreeService service = TreeServiceFactory.getInstance().getTreeService(lbsi);
        LexEvsTree tree = null;
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion("TestForMultiNamespace");
        tree = service.getTree("npo", csvt, "NPO_1607", "npo", "is_a");	            
            LexEvsTreeNode focusNode = tree.getCurrentFocus();
            List<LexEvsTreeNode> listForTree = service.getEvsTreeConverter().buildEvsTreePathFromRootTree(focusNode);
            String json = service.getJsonConverter(-1).buildJsonPathFromRootTree(focusNode);	        
            List <LexEvsTreeNode> listOfone = new ArrayList<LexEvsTreeNode>();
            listOfone.add(focusNode);
            System.out.println("Printing from focus Node");
            PrintUtility.print(focusNode);
            System.out.println("Printing tree");
            PrintUtility.print(listForTree);
            System.out.println("Printing Json");
            System.out.println(json);
            Gson gson = new Gson();
            OntologyNode[] nodes = gson.fromJson(json, OntologyNode[].class);
            assertTrue(nodes.length > 0);
            //Drilling down to the "process" node which could have more than the default 5 children
            OntologyNode processNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("processual_entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("process")).findFirst().get();
            assertNotNull(processNode);
            assertEquals(processNode.getOntology_node_name(), "process");
            //child count just indicates there is or is not children
            assertTrue(processNode.getOntology_node_child_count() > 0);
            assertTrue(processNode.getOntology_node_child_count() == 1);
            //checking actual size -- 6th child is an ellipsis  (...)
            assertTrue(processNode.getChildren_nodes().size() > 0);
            assertEquals(processNode.getChildren_nodes().size(),20);
            //this wouldn't be seen using default children sizing
            assertTrue(processNode.getChildren_nodes().stream().anyMatch(x -> x.getOntology_node_name().equals("nanoparticle response to stimulus")));
            
            OntologyNode emulsificationNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("processual_entity")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("process")).findFirst().get()
                     .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("emulsification")).findFirst().get();
            assertNotNull(emulsificationNode);
            assertEquals(emulsificationNode.getOntology_node_name(), "emulsification");
            //child count just indicates there is or is not children
            assertTrue(emulsificationNode.getOntology_node_child_count() == 0);
            assertTrue(emulsificationNode.getOntology_node_child_count() < 1);
            //checking actual size
            assertTrue(emulsificationNode.getChildren_nodes().size() == 0);
            
            OntologyNode occurrentNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get();
            assertNotNull(occurrentNode);
            assertEquals(occurrentNode.getOntology_node_name(), "occurrent");
            //child count just indicates there is or is not children
            assertTrue(occurrentNode.getOntology_node_child_count() > 0);
            assertTrue(occurrentNode.getOntology_node_child_count() == 1);
            //checking actual size
            assertTrue(occurrentNode.getChildren_nodes().size() == 3);
	}
	
	@Test
	public void testGetTreeWithJustMoreThanFiveChildren(){
        LexBIGService lbsi = LexBIGServiceImpl.defaultInstance();
        TreeService service = TreeServiceFactory.getInstance().getTreeService(lbsi);
        LexEvsTree tree = null;
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion("TestForMultiNamespace");
        tree = service.getTree("npo", csvt, "NPO_1607", "npo", "is_a");	            
            LexEvsTreeNode focusNode = tree.getCurrentFocus();
            String json = service.getJsonConverter(-1).buildJsonPathFromRootTree(focusNode);	        
            Gson gson = new Gson();
            OntologyNode[] nodes = gson.fromJson(json, OntologyNode[].class);
            assertTrue(nodes.length > 0);
            //Drilling down to the "process" node which has more than the default 5 children
            OntologyNode processNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("processual_entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("process")).findFirst().get();
            assertNotNull(processNode);
            assertEquals(processNode.getOntology_node_name(), "process");
            //child count just indicates there is or is not children
            assertTrue(processNode.getOntology_node_child_count() > 0);
            assertTrue(processNode.getOntology_node_child_count() == 1);
            //checking actual size
            assertTrue(processNode.getChildren_nodes().size() > 0);
            assertEquals(processNode.getChildren_nodes().size(), 20);
            //this wouldn't be seen using default children sizing
            assertTrue(processNode.getChildren_nodes().stream().anyMatch(x -> x.getOntology_node_name().equals("biological process")));
            
            OntologyNode emulsificationNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("processual_entity")).findFirst().get()
                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("process")).findFirst().get()
                     .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("emulsification")).findFirst().get();
            assertNotNull(emulsificationNode);
            assertEquals(emulsificationNode.getOntology_node_name(), "emulsification");
            //child count just indicates there is or is not children
            assertTrue(emulsificationNode.getOntology_node_child_count() == 0);
            assertTrue(emulsificationNode.getOntology_node_child_count() < 1);
            //checking actual size
            assertTrue(emulsificationNode.getChildren_nodes().size() == 0);
            
            OntologyNode occurrentNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get();
            assertNotNull(occurrentNode);
            assertEquals(occurrentNode.getOntology_node_name(), "occurrent");
            //child count just indicates there is or is not children
            assertTrue(occurrentNode.getOntology_node_child_count() > 0);
            assertTrue(occurrentNode.getOntology_node_child_count() == 1);
            //checking actual size
            assertTrue(occurrentNode.getChildren_nodes().size() == 3);
	}
	
	@Test
	public void testGetTreeWithLessThanFiveChildren(){
        LexBIGService lbsi = LexBIGServiceImpl.defaultInstance();
        TreeService service = TreeServiceFactory.getInstance().getTreeService(lbsi);
        LexEvsTree tree = null;
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion("TestForMultiNamespace");
        tree = service.getTree("npo", csvt, "NPO_1607", "npo", "is_a");	            
            LexEvsTreeNode focusNode = tree.getCurrentFocus();
            String json = service.getJsonConverter(-1).buildJsonPathFromRootTree(focusNode);	        
            Gson gson = new Gson();
            OntologyNode[] nodes = gson.fromJson(json, OntologyNode[].class);
            assertTrue(nodes.length > 0);
            //Drilling down to the "process" node which could have more than the default 5 children
            OntologyNode processNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("processual_entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("process")).findFirst().get();
            assertNotNull(processNode);
            assertEquals(processNode.getOntology_node_name(), "process");
            //child count just indicates there is or is not children
            assertTrue(processNode.getOntology_node_child_count() > 0);
            assertTrue(processNode.getOntology_node_child_count() == 1);
            //checking actual size
            assertTrue(processNode.getChildren_nodes().size() > 0);
            assertEquals(processNode.getChildren_nodes().size(), 20);
            //this wouldn't be seen using default children sizing
            assertTrue(processNode.getChildren_nodes().stream().anyMatch(x -> x.getOntology_node_name().equals("biological process")));
            assertTrue(processNode.getChildren_nodes().stream().anyMatch(x -> x.getOntology_node_name().equals("antineoplastic activity")));
            
//There are no child-less nodes in this shorter list so we can test for this here
//            OntologyNode emulsificationNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
//                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get()
//                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("processual_entity")).findFirst().get()
//                    .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("process")).findFirst().get()
//                     .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("emulsification")).findFirst().get();
//            assertNotNull(emulsificationNode);
//            assertEquals(emulsificationNode.getOntology_node_name(), "emulsification");
//            //child count just indicates there is or is not children
//            assertTrue(emulsificationNode.getOntology_node_child_count() == 0);
//            assertTrue(emulsificationNode.getOntology_node_child_count() < 1);
//            //checking actual size
//            assertTrue(emulsificationNode.getChildren_nodes().size() == 0);
            
            
            OntologyNode occurrentNode = Arrays.asList(nodes).stream().filter(x -> x.getOntology_node_name().equals("entity")).findFirst().get()
            .getChildren_nodes().stream().filter(x -> x.getOntology_node_name().equals("occurrent")).findFirst().get();
            assertNotNull(occurrentNode);
            assertEquals(occurrentNode.getOntology_node_name(), "occurrent");
            //child count just indicates there is or is not children
            assertTrue(occurrentNode.getOntology_node_child_count() > 0);
            assertTrue(occurrentNode.getOntology_node_child_count() == 1);
            //checking actual size, limit hit so while this should be three the ellipsis is added increasing size
            assertEquals(occurrentNode.getChildren_nodes().size(), 3);
            //assertTrue(occurrentNode.getChildren_nodes().stream().anyMatch(x -> x.getOntology_node_name().equals("...")));
	}
}
