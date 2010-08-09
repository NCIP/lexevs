package org.lexevs.cts2.author;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import junit.framework.TestSuite;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.test.Cts2BaseTest;
import org.lexevs.cts2.test.Cts2TestConstants;
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
		
		String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
		String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
		
		String codingSchemeName = "New Coding Scheme";
	    String formalName = "CTS 2 API Created Code System";
	    String defaultLanguage = "";
	    Long approxNumConcepts = new Long(1);
	    List<String> localNameList = Arrays.asList(""); 
	    
	    Source source = new Source();
	    source.setContent("source");
	    List<Source> sourceList = Arrays.asList(source);
	    
	    Text copyright = new Text();
	    Mappings mappings = new Mappings();
	    
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		
		CodingScheme codeScheme = codeSystemAuthOp.createCodeSystem(revInfo, codingSchemeName, codingSchemeURI, formalName, defaultLanguage, approxNumConcepts, representsVersion, localNameList, sourceList, copyright, mappings);
		
		assertEquals(Cts2TestConstants.CTS2_CREATE_URI,codeScheme.getCodingSchemeURI());
		
		
	}

	@Test
	public void testRemoveCodeSystem()   throws LBException, URISyntaxException{
		
		
		String randomID = UUID.randomUUID().toString();
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(randomID);
		
	    
	    String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
		String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
		
		Boolean removeStatus = false;
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();

	    try {
			removeStatus = codeSystemAuthOp.removeCodeSystem(revInfo, codingSchemeURI, representsVersion);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		assertTrue(removeStatus);

		
	}

	@Test
		public void testUpdateCodeSystem()   throws LBException, URISyntaxException{


			String randomID = UUID.randomUUID().toString();
		
			RevisionInfo revInfo = new RevisionInfo();
			revInfo.setChangeAgent("changeAgent");
			revInfo.setChangeInstruction("changeInstruction");
			revInfo.setDescription("new description");
			revInfo.setEditOrder(1L);
			revInfo.setRevisionDate(new Date());
			revInfo.setRevisionId(randomID);
			
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
	        
	        
	        String codingSchemeURI = Cts2TestConstants.CTS2_CREATE_URI;
			String representsVersion = Cts2TestConstants.CTS2_CREATE_VERSION;
	        
			String codingSchemeName = "New Coding Scheme - Updated";
	        String formalName = null;
	        String defaultLanguage = null;
	        Long approxNumConcepts = 0L;
	        List<String> localNameList = null; 
	        List<Source> sourceList = null;
	        Text copyright = null;
	        Mappings mappings = null;
	        
	        
	        
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
	        															mappings 
	        															);
	        
	
	        assertEquals("New Coding Scheme - Updated", codeScheme.getCodingSchemeName());
	        
			
		}

	@Test
		public void testUpdateCodeSystemMappings()   throws LBException, URISyntaxException{

			
			RevisionInfo revInfo = new RevisionInfo();
			revInfo.setChangeAgent("changeAgent");
			revInfo.setChangeInstruction("changeInstruction - update mappings");
			revInfo.setDescription("new description");
			revInfo.setEditOrder(1L);
			revInfo.setRevisionDate(new Date());
			revInfo.setRevisionId("R901");
			

	        Mappings mappings = new Mappings();
	        SupportedCodingScheme scScheme = new SupportedCodingScheme();
	        scScheme.setLocalId("localId");
	        mappings.addSupportedCodingScheme(scScheme);
	        
	        
	        String codingSchemeURI = "urn:oid:11.11.0.99";
	        String representsVersion = "1.0";
	        
			String codingSchemeName = null;
	        String formalName = null;
	        String defaultLanguage = null;
	        Long approxNumConcepts = 0L;
	        List<String> localNameList = null; 
	        List<Source> sourceList = null;
	        Text copyright = null;

	        
	        
	        
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
	        															mappings 
	        															);
	        
	
				
			System.out.println("Coding Scheme URI : " + codeScheme.getCodingSchemeURI());
			
		}

	@Test
	public void testAddCodeSystemProperty()   throws LBException, URISyntaxException{
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("new description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R502");
	    
	    String codingSchemeURI = "urn:oid:11.11.0.99";
	    String representsVersion = "1.0";
	    
		String codingSchemeName = null;
	
	    
	    Properties properties = new Properties();
	    
	    Presentation prop = new Presentation();
		prop.setPropertyId("propertyId1");
		prop.setPropertyName("propertyName");
		prop.setIsActive(false);
		prop.setLanguage("english updated 206");
		prop.setOwner("owner updated 206");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("content updated 206");
		text.setDataType("Text datatype");
		prop.setValue(text);
		prop.setDegreeOfFidelity("degreeOfFidelity");
		prop.setMatchIfNoContext(true);
		prop.setRepresentationalForm("representationalForm");
		
		properties.addProperty(prop);
		
		Presentation prop2 = new Presentation();
		prop2.setPropertyId("propertyId2");
		prop2.setPropertyName("propertyName-2");
		prop2.setIsActive(false);
		prop2.setLanguage("english updated 206-2");
		prop2.setOwner("owner updated 206-2");
		prop2.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text2 = new Text();
		text2.setContent("content updated 206-2");
		text2.setDataType("Text datatype-2");
		prop2.setValue(text);
		prop2.setDegreeOfFidelity("degreeOfFidelity-2");
		prop2.setMatchIfNoContext(true);
		prop2.setRepresentationalForm("representationalForm-2");
		
		properties.addProperty(prop2);
		
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	           
	    CodingScheme codeScheme = codeSystemAuthOp.addCodeSystemProperties (revInfo, 
	    															codingSchemeName, 
	    															codingSchemeURI,  
	    															representsVersion, 
	    															properties);
	    
			
		System.out.println("Coding Scheme URI : " + codeScheme.getCodingSchemeURI());
		
	}
	
	@Test
	public void testUpdateCodeSystemProperty()   throws LBException, URISyntaxException{
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("new description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R503");
	    
	    String codingSchemeURI = "urn:oid:11.11.0.99";
	    String representsVersion = "1.0";
	    
		String codingSchemeName = null;
	
	    
	    Properties properties = new Properties();
	    
	    Presentation prop = new Presentation();
		prop.setPropertyId("propertyId1");
		prop.setPropertyName("propertyName");
		prop.setIsActive(false);
		prop.setLanguage("english updated 503");
		prop.setOwner("owner updated 503");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("content updated 503");
		text.setDataType("Text datatype");
		prop.setValue(text);
		prop.setDegreeOfFidelity("degreeOfFidelity");
		prop.setMatchIfNoContext(true);
		prop.setRepresentationalForm("representationalForm");
		
		properties.addProperty(prop);
		
		
		
	    
	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
	           
	    CodingScheme codeScheme = codeSystemAuthOp.updateCodeSystemProperties (revInfo, 
	    															codingSchemeName, 
	    															codingSchemeURI,  
	    															representsVersion, 
	    															properties);
	    
			
		System.out.println("Coding Scheme URI : " + codeScheme.getCodingSchemeURI());
		
	}

	@Test
	public void testRemoveCodeSystemProperty()   throws LBException, URISyntaxException{
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("new description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R603");
        
        String codingSchemeURI = "urn:oid:11.11.0.99";
        String representsVersion = "1.0";
        
        String propertyId = "propertyId1";
        
        
        CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
               
        CodingScheme codeScheme = codeSystemAuthOp.removeCodeSystemProperty (revInfo, 
        															codingSchemeURI,  
        															representsVersion, 
        															propertyId);
        
			
		System.out.println("Coding Scheme URI : " + codeScheme.getCodingSchemeURI());
		
	}

	@Test
	public void testUpdateCodeSystemVersionStatus()   throws LBException, URISyntaxException{
		
		
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
	public void testUpdateConcept() throws LBException {
		CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
        RevisionInfo info = new RevisionInfo();
        info.setRevisionId(UUID.randomUUID().toString());
        
		Entity entityToUpdate = new Entity();
		entityToUpdate.setEntityCode("005");
		entityToUpdate.setEntityCodeNamespace(Cts2TestConstants.CTS2_AUTOMOBILES_NAME);
		entityToUpdate.setEntityDescription(Constructors.createEntityDescription("Modified ED"));
		
		codeSystemAuthOp.updateConcept(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				entityToUpdate, 
				info);
		
		CodedNodeSet cns = super.getLexBIGService().getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				null, 
				null);
		
		ResolvedConceptReferenceList refList = cns.restrictToCodes(Constructors.createConceptReferenceList("005")).resolveToList(null, null, null, -1);
	
		assertEquals(1,refList.getResolvedConceptReferenceCount());
		
		ResolvedConceptReference ref = refList.getResolvedConceptReference(0);
		
		assertEquals("Modified ED",ref.getEntityDescription().getContent());
	}
	
	@Test
	public void testUpdateProperty() throws LBException {
		CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
        RevisionInfo info = new RevisionInfo();
        info.setRevisionId(UUID.randomUUID().toString());
        
		Property propertyToUpdate = new Property();
		propertyToUpdate.setPropertyName("textualPresentation");
		propertyToUpdate.setPropertyId("p1");
		propertyToUpdate.setValue(Constructors.createText("Modded text"));
		
		codeSystemAuthOp.updateConceptProperty(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				"005",
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME,
				propertyToUpdate, 
				info);
		
		CodedNodeSet cns = super.getLexBIGService().getNodeSet(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI,
				null, 
				null);
		
		ResolvedConceptReferenceList refList = cns.restrictToCodes(Constructors.createConceptReferenceList("005")).resolveToList(null, null, null, -1);
	
		assertEquals(1,refList.getResolvedConceptReferenceCount());
		
		ResolvedConceptReference ref = refList.getResolvedConceptReference(0);
		
		Property foundProp = DataTestUtils.getPropertyWithId(ref.getEntity().getAllProperties(), "p1");
		
		assertEquals("Modded text",foundProp.getValue().getContent());
	}
	
	protected void testRemoveRevisionRecordById(String revisionID) throws LBException {
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		System.out.println(authServ.removeRevisionRecordbyId(revisionID));
	
	}
	

}
