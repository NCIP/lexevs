/**
 * 
 */
package org.lexevs.cts2.author;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;

/**
 * @author m004181
 *
 */
public class ConceptDomainAuthoringOperationImplTest {

	private static ConceptDomainAuthoringOperation CD_AUTH_OP;
	
	private static List<String> revIds_ = new ArrayList<String>();
	
	@BeforeClass
	public static void runBeforeClass(){
		CD_AUTH_OP = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
	}
	
	@AfterClass
	public static void runAfterClass(){
		CD_AUTH_OP = null;
	}
	
	@Test
	public void createConceptDomainCodeSystem() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Mappings maps = new Mappings();
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setContent(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		scs.setUri(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI);
		scs.setLocalId(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		scs.setIsImported(false);
		
		maps.addSupportedCodingScheme(scs);
		
		CD_AUTH_OP.createConceptDomainCodeSystem(rev, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				"en", 0, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, null, null, null, maps);
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#createConceptDomain(java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 * @throws IOException 
	 */
	@Test
	public void createConceptDomain() throws LBException, IOException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Properties props = new Properties();
		Property prop = new Property();
		prop.setPropertyId("cd1propId1");
		Text value = new Text();
		value.setContent("cd1content");
		prop.setValue(value);
		prop.setPropertyName("cd1propertyName");
		props.addProperty(prop);
		
		CD_AUTH_OP.createConceptDomain("cdunitest1", "cd unit test 1", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, "cd unit test 1", "testing", 
				false, props, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		
		props = new Properties();
		prop = new Property();
		prop.setPropertyId("cd2propId1");
		value = new Text();
		value.setContent("cd2content");
		prop.setValue(value);
		prop.setPropertyName("cd2propertyName");
		prop.setIsActive(true);
		prop.setOwner("owner test");
		props.addProperty(prop);
		
		
		rev.setRevisionId(getRevId());
		
		CD_AUTH_OP.createConceptDomain("cdunitest2", "cd unit test 2", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, "cd unit test 2", "testing", 
				false, props, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
	}
	
	@Test
	public void createConceptDomainFromFile() throws LBException, IOException {
		RevisionInfo rev = new RevisionInfo();
		
		File file = new File("src/test/resources/testData/ConceptDomainTestData.txt");
		 
		BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
		String line = null;
		int row = 0;
		int col = 0;
		 
		List<ConceptDomainData> cdDatas = new ArrayList<ConceptDomainData>();
		ConceptDomainData cdData = null;
		//read each line of text file
		
		while((line = bufRdr.readLine()) != null && row <= 10)
		{
			if (line.startsWith("#")) // ignore the comment record
				continue;
			
			StringTokenizer st = new StringTokenizer(line,"|");
			cdData = new ConceptDomainData();
			col = 1;
			while (st.hasMoreTokens())
			{
				//get next token and store it in the array
				switch(col)
				{
					case 1 : cdData.id = st.nextToken();break;
					case 2 : cdData.name = st.nextToken();break;
					case 3 : cdData.description = st.nextToken();break;
				}
				col++;
			}
			cdDatas.add(cdData);
			row++;
		}
		 
		//close the file
		bufRdr.close();
		for (ConceptDomainData cd : cdDatas)
		{
			rev.setRevisionId(getRevId());
			
			CD_AUTH_OP.createConceptDomain(cd.id, cd.name, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, cd.description, "testing", 
					false, null, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		}
		
		cdDatas = null;
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainStatus(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void updateConceptDomainStatus() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		CD_AUTH_OP.updateConceptDomainStatus("cdunitest1", null, "New Status " + revId, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#activateConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void activateConceptDomain() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		CD_AUTH_OP.activateConceptDomain("cdunitest1", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#deactivateConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void deactivateConceptDomain() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		CD_AUTH_OP.deactivateConceptDomain("cdunitest1", null, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainVersionable(java.lang.String, org.LexGrid.commonTypes.Versionable, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void updateConceptDomainVersionable() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		Versionable changedVersionable = new Versionable();
		changedVersionable.setEffectiveDate(new Date());
		changedVersionable.setIsActive(true);
		changedVersionable.setOwner("new Owner - " + revId);
		changedVersionable.setStatus("new status - " + revId);
		
		CD_AUTH_OP.updateConceptDomainVersionable("cdunitest1", null, changedVersionable, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#addConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void addConceptDomainProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		Text value = new Text();
		value.setContent("cd2content2");
		prop.setValue(value);
		prop.setPropertyName("cd2propertyName2");
		
		CD_AUTH_OP.addConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void updateConceptDomainProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		String revId = getRevId();
		rev.setRevisionId(revId);
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		Text value = new Text();
		value.setContent("cd2content2 updated - cdR212");
		prop.setValue(value);
		prop.setPropertyName("cd2propertyName2");
		prop.setLanguage("en updated");
		prop.setIsActive(true);
		prop.setOwner("owner updated");
		
		CD_AUTH_OP.updateConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#removeConceptDomainProperty(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void removeConceptDomainProperty() throws LBException {
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		prop.setPropertyName("cd2propertyName2");
		
		CD_AUTH_OP.removeConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}
	
	@Test
	public void removeConceptDomain() throws LBException{
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId(getRevId());
		
		CD_AUTH_OP.removeConceptDomain("AcknowledgementCondition", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME,
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#addConceptDomainToValueSetBinding(java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)}.
	 */
//	@Test
//	public void addConceptDomainToValueSetBinding() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#removeConceptDomainToValueSetBinding(java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)}.
//	 */
//	@Test
//	public void removeConceptDomainToValueSetBinding() {
//		fail("Not yet implemented");
//	}

	
	@Test
	public void removeConceptDomainCodeSystem() throws LBException{
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId(getRevId());
		
	    
	    String codingSchemeURI = ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI;
		String representsVersion = ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION;
		
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
	public void removeRevisionRecordById() throws LBException {
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		for (String revId : revIds_)
		{
			System.out.println(authServ.removeRevisionRecordbyId(revId));
		}
	}
	
	class ConceptDomainData{
		String id;
		String name;
		String description;
	}
	
	private String getRevId(){
		String revId = UUID.randomUUID().toString();
		revIds_.add(revId);
		
		return revId;
	}
}
