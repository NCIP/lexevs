package org.lexevs.cts2.author;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;

public class CodeSystemAuthoringOperationImplTest {

	@Test
	public void testCommitCodeSystem() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateCodeSystem()   throws LBException, URISyntaxException{
		//fail("Not yet implemented");
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R101");
		
		String codingSchemeName = "New Coding Scheme";
	    String codingSchemeURI = "urn:oid:11.11.0.99";
	    String formalName = "CTS 2 API Created Code System";
	    String defaultLanguage = "";
	    Long approxNumConcepts = new Long(1);
	    String representsVersion = "1.0";
	    List<String> localNameList = Arrays.asList(""); 
	    
	    Source source = new Source();
	    source.setContent("source");
	    List<Source> sourceList = Arrays.asList(source);
	    
	    Text copyright = new Text();
	    Mappings mappings = new Mappings();
	    Properties properties = new Properties();
	    Entities entities = new Entities();
	    
	    
	    Relations relations = new Relations();
	    List<Relations> relationsList = Arrays.asList(relations);
	    
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	    
	
		
		CodingScheme codeScheme = codeSystemAuthOp.createCodeSystem(revInfo, codingSchemeName, codingSchemeURI, formalName, defaultLanguage, approxNumConcepts, representsVersion, localNameList, sourceList, copyright, mappings, properties, entities, relationsList);
		
		System.out.println("Coding Scheme URI : " + codeScheme.getCodingSchemeURI());
		
	}

	@Test
	public void testRemoveCodeSystem()   throws LBException, URISyntaxException{
		//fail("Not yet implemented");
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R201");
		
	    String codingSchemeURI = "urn:oid:11.11.0.99";
	    String representsVersion = "1.0";
	    
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	    
		try {
			codeSystemAuthOp.removeCodeSystem(revInfo, codingSchemeURI, representsVersion);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	@Test
	public void testUpdateCodeSystem()   throws LBException, URISyntaxException{
		//fail("Not yet implemented");
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("new description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R301");
		
		String codingSchemeName = "New Coding Scheme - Updated";
        String codingSchemeURI = "urn:oid:11.11.0.99";
        String formalName = "CTS 2 API Created Code System";
        String defaultLanguage = "";
        Long approxNumConcepts = new Long(1);
        String representsVersion = "1.0";
        List<String> localNameList = Arrays.asList(""); 
        
        Source source = new Source();
        source.setContent("source");
        List<Source> sourceList = Arrays.asList(source);
        
        Text copyright = new Text();
        Mappings mappings = new Mappings();
        Properties properties = new Properties();
        Entities entities = new Entities();
        
        
        Relations relations = new Relations();
        List<Relations> relationsList = Arrays.asList(relations);
        
        
        CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
        

		
		CodingScheme codeScheme = codeSystemAuthOp.updateCodeSystem(revInfo, codingSchemeName, codingSchemeURI, formalName, defaultLanguage, approxNumConcepts, representsVersion, localNameList, sourceList, copyright, mappings, properties, entities, relationsList);
		
		System.out.println("Coding Scheme URI : " + codeScheme.getCodingSchemeURI());
		
	}

	@Test
	public void testCreateCodeSystemChangeSet() {
		fail("Not yet implemented");
	}

}
