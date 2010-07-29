package org.lexevs.cts2.author;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.test.Cts2BaseTest;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

public class CodeSystemAuthoringOperationImplTest extends Cts2BaseTest {

	@Test
	public void testCreateCodeSystem()   throws LBException, URISyntaxException{
		
		String randomID = UUID.randomUUID().toString();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
		
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
	    
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		
		CodingScheme codeScheme = codeSystemAuthOp.createCodeSystem(revInfo, codingSchemeName, codingSchemeURI, formalName, defaultLanguage, approxNumConcepts, representsVersion, localNameList, sourceList, copyright, mappings, properties);
		
		System.out.println("Coding Scheme URI : " + codeScheme.getCodingSchemeURI());
		
	}

	@Test
	public void testRemoveCodeSystem()   throws LBException, URISyntaxException{
		//fail("Not yet implemented");
		
		String randomID = UUID.randomUUID().toString();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
		
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
			
	//		String codingSchemeName = "New Coding Scheme - Updated";
	//        String codingSchemeURI = "urn:oid:11.11.0.99";
	//        String formalName = "CTS 2 API Created Code System";
	//        String defaultLanguage = "";
	//        Long approxNumConcepts = new Long(1);
	//        String representsVersion = "1.0";
	//        List<String> localNameList = Arrays.asList(""); 
	//        
	//        Source source = new Source();
	//        source.setContent("source");
	//        List<Source> sourceList = Arrays.asList(source);
	//        
	//        Text copyright = new Text();
	//        Mappings mappings = new Mappings();
	//        Properties properties = new Properties();
	        
	        String codingSchemeURI = "urn:oid:11.11.0.99";
	        String representsVersion = "1.0";
	        
			String codingSchemeName = "New Coding Scheme - Updated";
	        String formalName = null;
	        String defaultLanguage = null;
	        Long approxNumConcepts = 0L;
	        List<String> localNameList = null; 
	        List<Source> sourceList = null;
	        Text copyright = null;
	        Mappings mappings = null;
	        Properties properties = null;
	        
	        
	        CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	               
	        CodingScheme codeScheme = codeSystemAuthOp.updateCodeSystem(revInfo, 
	        															codingSchemeName, 
	        															codingSchemeURI, 
	        															formalName, 
	        															defaultLanguage, 
	        															approxNumConcepts, 
	        															representsVersion, 
	        															localNameList, 
	        															sourceList, 
	        															copyright, 
	        															mappings, 
	        															properties);
	        
	
				
			System.out.println("Coding Scheme URI : " + codeScheme.getCodingSchemeURI());
			
		}

	@Test
	public void testUpdateCodeSystemVersionStatus()   throws LBException, URISyntaxException{
		//fail("Not yet implemented");
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("Description - Update status.");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R_CS_401");
		

        
        String codingSchemeURI = "urn:oid:11.11.0.99";
        String representsVersion = "1.0";
        
        String status = "test status";
        boolean isActive = true;
     
        
        
        CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
               
        try {
        	codeSystemAuthOp.updateCodeSystemVersionStatus(codingSchemeURI, representsVersion, status, isActive, revInfo);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	
		
	}

	@Test
	public void testRemoveRevisionRecordById() throws LBException {
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		System.out.println(authServ.removeRevisionRecordbyId("R_CS_401"));
	
	}

}
