/**
 * 
 */
package org.lexevs.cts2.author;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.query.UsageContextQueryOperation;
import org.lexgrid.usagecontext.util.UsageContextConstants;

/**
 * @author m004181
 *
 */
public class UsageContextAuthoringOperationImplTest {

	private static UsageContextAuthoringOperation UC_AUTH_OP;
	private static UsageContextQueryOperation UC_QUERY_OP;
	
	private static List<String> revIds_ = new ArrayList<String>();
	
	@BeforeClass
	public static void init() throws Exception {
		UC_AUTH_OP = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getUsageContextAuthoringOperation();
		UC_QUERY_OP = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getUsageContextQueryOperation();
		
		createUsageContextCodeSystem();
		createUsageContextFromFile();
	}
	
	private static void createUsageContextCodeSystem() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Mappings maps = new Mappings();
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setContent(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		scs.setUri(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI);
		scs.setLocalId(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		scs.setIsImported(false);
		
		maps.addSupportedCodingScheme(scs);
		
		UC_AUTH_OP.createUsageContextCodeSystem(rev, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				"en", 0, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, null, null, null, maps);
		
		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
		csLoadOp.activateCodeSystem(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		CodingScheme ucCS = UC_QUERY_OP.getUsageContextCodingScheme(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI,
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(ucCS.getCodingSchemeName().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		assertTrue(ucCS.getCodingSchemeURI().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI));
		assertTrue(ucCS.getDefaultLanguage().equals("en"));
	}
	
	private static void createUsageContextFromFile() throws LBException, IOException {
		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
		File file = new File("resources/testData/cts2/usageContextCodingScheme_1.0.xml");
		
		csLoadOp.load(file.toURI(), null, null, "LexGrid_Loader", true, true, false, "test", true);
		
		CodingScheme ucCS = UC_QUERY_OP.getUsageContextCodingScheme("http://lexevs/codeSystem/test/usageContext", "1.0");
		
		assertTrue(ucCS.getCodingSchemeName().equals("usageContextTestCodeSystem"));
		assertTrue(ucCS.getCodingSchemeURI().equals("http://lexevs/codeSystem/test/usageContext"));
		assertTrue(ucCS.getDefaultLanguage().equals("ENG"));
	}
	
	@Test
	public void testCreateUsageContext() throws LBException, IOException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Properties props = new Properties();
		Property prop = new Property();
		prop.setPropertyId("propId1");
		Text value = new Text();
		value.setContent("Automobile");
		prop.setValue(value);
		prop.setPropertyName("textualPresentation");
		props.addProperty(prop);
		
		UC_AUTH_OP.createUsageContext("automobile", "Automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, 
				"Automobile Usage Context", "testing", false, props, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		Entity uc = UC_QUERY_OP.getUsageContextEntity("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("automobile"));
		assertTrue(uc.getStatus().equals("testing"));
		assertFalse(uc.getIsActive());
		
		props = new Properties();
		prop = new Property();
		prop.setPropertyId("propId1");
		value = new Text();
		value.setContent("Weather");
		prop.setValue(value);
		prop.setPropertyName("textualPresentation");
		prop.setIsActive(true);
		prop.setOwner("owner");
		props.addProperty(prop);		
		
		rev.setRevisionId(getRevId());
		
		UC_AUTH_OP.createUsageContext("weather", "Weather", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, 
				"Weather Usage Context", "testing", false, props, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		uc = UC_QUERY_OP.getUsageContextEntity("weather", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("weather"));
		assertTrue(uc.getStatus().equals("testing"));
		assertFalse(uc.getIsActive());
	}
	
	@Test
	public void testUpdateUsageContextStatus() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		//from 'testing' to 'pending'
		UC_AUTH_OP.updateUsageContextStatus("automobile", null, "pending", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, rev);
		

		Entity uc = UC_QUERY_OP.getUsageContextEntity("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("automobile"));
		assertTrue(uc.getStatus().equals("pending"));
	}

	@Test
	public void testActivateUsageContext() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		UC_AUTH_OP.activateUsageContext("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, rev);
		
		Entity uc = UC_QUERY_OP.getUsageContextEntity("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("automobile"));
		assertTrue(uc.getIsActive());		
	}

	@Test
	public void testDeactivateUsageContext() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		assertTrue(UC_AUTH_OP.deactivateUsageContext("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, rev));
		
		Entity uc = UC_QUERY_OP.getUsageContextEntity("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("automobile"));
		assertFalse(uc.getIsActive());		
	}

	@Test
	public void testUpdateUsageContextVersionable() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		Versionable changedVersionable = new Versionable();
		changedVersionable.setEffectiveDate(new Date());
		changedVersionable.setIsActive(true);
		changedVersionable.setOwner("new Owner");
		changedVersionable.setStatus("new status");
		
		assertTrue(UC_AUTH_OP.updateUsageContextVersionable("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				changedVersionable, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, rev));
		
		Entity uc = UC_QUERY_OP.getUsageContextEntity("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("automobile"));
		assertTrue(uc.getIsActive());		
		assertTrue(uc.getOwner().equals("new Owner"));
		assertTrue(uc.getStatus().equals("new status"));
	}

	@Test
	public void testAddUsageContextProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Property prop = new Property();
		prop.setPropertyId("propId2");
		Text value = new Text();
		value.setContent("Autos");
		prop.setValue(value);
		prop.setPropertyName("textualPresentation");
		
		assertTrue(UC_AUTH_OP.addUsageContextProperty("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				prop, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, rev));
		
		Entity uc = UC_QUERY_OP.getUsageContextEntity("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("automobile"));
		assertTrue(uc.getIsActive());		
		assertTrue(uc.getOwner().equals("new Owner"));
		assertTrue(uc.getStatus().equals("new status"));
		
		assertTrue(uc.getPropertyCount() == 2);
		
		for(Property pres : uc.getPropertyAsReference())
		{
			assertTrue(pres.getValue().getContent().equals("Automobile")
					|| pres.getValue().getContent().equals("Autos"));
		}
	}

	@Test
	public void testUpdateUsageContextProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		Property prop = new Property();
		prop.setPropertyId("propId2");
		Text value = new Text();
		value.setContent("Autos-Updated");
		prop.setValue(value);
		prop.setPropertyName("textualPresentation");
		prop.setLanguage("ENG");
		
		assertTrue(UC_AUTH_OP.updateUsageContextProperty("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, prop, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, rev));
		
		Entity uc = UC_QUERY_OP.getUsageContextEntity("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("automobile"));
		
		assertTrue(uc.getPropertyCount() == 2);
		
		for(Property pres : uc.getPropertyAsReference())
		{
			assertTrue(pres.getValue().getContent().equals("Automobile")
					|| pres.getValue().getContent().equals("Autos-Updated"));
			
			if (pres.getPropertyId().equals("propId2"))
			{
				assertTrue(pres.getPropertyName().equals("textualPresentation"));
				assertTrue(pres.getLanguage().equals("ENG"));
			}
		}		
	}
	
	@Test
	public void testGetUsageContextCodingScheme() throws LBException {
		CodingScheme cs = UC_QUERY_OP.getUsageContextCodingScheme(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cs.getCodingSchemeName().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		assertTrue(cs.getCodingSchemeURI().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI));
		assertTrue(cs.getFormalName().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME));
	}

	@Test
	public void testGetUsageContextEntitisWithName() throws LBException {
		List<Entity> entityList = UC_QUERY_OP.getUsageContextEntitisWithName("Auto", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, null, null, "subString", null);
		
		assertTrue(entityList.size() == 1);
		for (Entity entity : entityList)
		{
			assertTrue(entity.getEntityCode().equals("automobile"));
			assertTrue(entity.getEntityCodeNamespace().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		}
	}

	@Test
	public void testGetUsageContextEntity() throws LBException {
		Entity entity = UC_QUERY_OP.getUsageContextEntity("weather", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(entity.getEntityCode().equalsIgnoreCase("weather"));
		assertTrue(entity.getEntityCodeNamespace().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		assertTrue(entity.getPresentationCount() == 1);
	}
	
	@Test
	public void testListAllUsageContextEntities() throws LBException {
		List<Entity> entities = UC_QUERY_OP.listAllUsageContextEntities(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(entities.size() >= 2);
	}

	@Test
	public void testListAllUsageContextIds() throws LBException {
		
		List<String> cdIds = UC_QUERY_OP.listAllUsageContextIds(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(cdIds.size() >= 2);
	}
	
	@Test
	public void testRemoveUsageContextProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Property prop = new Property();
		prop.setPropertyId("propId2");
		prop.setPropertyName("textualPresentation");
		
		UC_AUTH_OP.removeUsageContextProperty("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, prop, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, rev);
		
		Entity uc = UC_QUERY_OP.getUsageContextEntity("automobile", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc.getEntityCode().equals("automobile"));
		
		assertTrue(uc.getPresentationCount() == 1);
		
		for(Presentation pres : uc.getPresentationAsReference())
		{
			assertTrue(pres.getValue().getContent().equals("Automobile"));
		}
	}
	
	public static void removeUsageContext() throws LBException{
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		UC_AUTH_OP.removeUsageContext("weather", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME,
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION, rev);
		
		Entity uc = UC_QUERY_OP.getUsageContextEntity("weather", UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME, UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		
		assertTrue(uc == null);
	}

	@AfterClass
	public static void cleanup() throws Exception {
		removeUsageContext();
		
		UC_AUTH_OP = null;
		UC_QUERY_OP = null;
	
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(getRevId());
		
	    
	    String codingSchemeURI = "http://lexevs/codeSystem/test/usageContext";
		String representsVersion = "1.0";
		
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

	private static String getRevId(){
		String revId = UUID.randomUUID().toString();
		revIds_.add(revId);
		
		return revId;
	}
}
