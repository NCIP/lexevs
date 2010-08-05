/**
 * 
 */
package org.lexevs.cts2.author;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.apache.commons.lang.StringUtils;
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

	@Test
	public void testCreateConceptDomainCodeSystem() throws LBException {
		ConceptDomainAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR100");
		
		Mappings maps = new Mappings();
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setContent(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		scs.setUri(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI);
		scs.setLocalId(ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		scs.setIsImported(false);
		
		maps.addSupportedCodingScheme(scs);
		
		csAuthOp.createConceptDomainCodeSystem(rev, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, 
				"en", 0, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, null, null, null, maps);
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#createConceptDomain(java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 * @throws IOException 
	 */
	@Test
	public void testCreateConceptDomain() throws LBException, IOException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR110");
		
		Properties props = new Properties();
		Property prop = new Property();
		prop.setPropertyId("cd1propId1");
		Text value = new Text();
		value.setContent("cd1content");
		prop.setValue(value);
		prop.setPropertyName("cd1propertyName");
		props.addProperty(prop);
		
		cdAuthop.createConceptDomain("cdunitest1", "cd unit test 1", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, "cd unit test 1", "testing", 
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
		
		
		rev.setRevisionId("cdR120");
		
		cdAuthop.createConceptDomain("cdunitest2", "cd unit test 2", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, "cd unit test 2", "testing", 
				false, props, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
	}
	
	@Test
	public void testCreateConceptDomainFromFile() throws LBException, IOException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		String revPrefix = "cdR";
		
		RevisionInfo rev = new RevisionInfo();
		
		File file = new File("src/test/resources/testData/ConceptDomainTestData.txt");
		 
		BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
		String line = null;
		int row = 0;
		int col = 0;
		 
		List<ConceptDomainData> cdDatas = new ArrayList<ConceptDomainData>();
		ConceptDomainData cdData = null;
		//read each line of text file
		while((line = bufRdr.readLine()) != null)
		{
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
		
		Properties props = null;
		Property prop = null;
		int i = 110;
		
		for (ConceptDomainData cd : cdDatas)
		{
//			props = new Properties();
//			prop = new Property();			
//			props.addProperty(prop);
//			
//			if (StringUtils.isNotEmpty(cd.description))
//			{
//				prop.setPropertyType(PropertyTypes.DEFINITION.name());
//				Text value = new Text();
//				value.setDataType("html");
//				value.setContent(cd.description);
//				prop.setValue(value);
//				prop.setPropertyId("p1");
//				prop.setPropertyName("Description");
//			}
			
			rev.setRevisionId(revPrefix + i++);
			
			cdAuthop.createConceptDomain(cd.id, cd.name, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, rev, cd.description, "testing", 
					false, props, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION);
		}
		
		cdDatas = null;
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainStatus(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testUpdateConceptDomainStatus() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR111");
		
		cdAuthop.updateConceptDomainStatus("cdunitest1", null, "New Status cdr112", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_URI, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#activateConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testActivateConceptDomain() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR112");
		
		cdAuthop.activateConceptDomain("cdunitest1", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#deactivateConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testDeactivateConceptDomain() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR113");
		
		cdAuthop.deactivateConceptDomain("cdunitest1", null, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainVersionable(java.lang.String, org.LexGrid.commonTypes.Versionable, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testUpdateConceptDomainVersionable() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR114");
		
		Versionable changedVersionable = new Versionable();
		changedVersionable.setEffectiveDate(new Date());
		changedVersionable.setIsActive(true);
		changedVersionable.setOwner("new Owner - cdR114");
		changedVersionable.setStatus("new status - cdR114");
		
		cdAuthop.updateConceptDomainVersionable("cdunitest1", null, changedVersionable, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#addConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testAddConceptDomainProperty() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR211");
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		Text value = new Text();
		value.setContent("cd2content2");
		prop.setValue(value);
		prop.setPropertyName("cd2propertyName2");
		
		cdAuthop.addConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testUpdateConceptDomainProperty() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR212");
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		Text value = new Text();
		value.setContent("cd2content2 updated - cdR212");
		prop.setValue(value);
		prop.setPropertyName("cd2propertyName2");
		prop.setLanguage("en updated - cdR212");
		prop.setIsActive(true);
		prop.setOwner("owner updated - cdR212");
		
		cdAuthop.updateConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#removeConceptDomainProperty(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testRemoveConceptDomainProperty() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR213");
		
		Property prop = new Property();
		prop.setPropertyId("cd2propId2");
		prop.setPropertyName("cd2propertyName2");
		
		cdAuthop.removeConceptDomainProperty("cdunitest2", null, prop, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}
	
	@Test
	public void testRemoveConceptDomain() throws LBException{
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR220");
		
		cdAuthop.removeConceptDomain("#ConceptDomainId", ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME,
				ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_FORMAL_NAME, ConceptDomainConstants.CONCEPT_DOMAIN_DEFAULT_CODING_SCHEME_VERSION, rev);
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#addConceptDomainToValueSetBinding(java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)}.
	 */
	@Test
	public void testAddConceptDomainToValueSetBinding() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#removeConceptDomainToValueSetBinding(java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)}.
	 */
	@Test
	public void testRemoveConceptDomainToValueSetBinding() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveRevisionRecordById() throws LBException {
		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
		System.out.println(authServ.removeRevisionRecordbyId("cdR100"));
		System.out.println(authServ.removeRevisionRecordbyId("cdR110"));
		System.out.println(authServ.removeRevisionRecordbyId("cdR120"));
		System.out.println(authServ.removeRevisionRecordbyId("cdR111"));
		System.out.println(authServ.removeRevisionRecordbyId("cdR112"));
		System.out.println(authServ.removeRevisionRecordbyId("cdR113"));
		System.out.println(authServ.removeRevisionRecordbyId("cdR114"));
	}
	
	class ConceptDomainData{
		String id;
		String name;
		String description;
	}
}
