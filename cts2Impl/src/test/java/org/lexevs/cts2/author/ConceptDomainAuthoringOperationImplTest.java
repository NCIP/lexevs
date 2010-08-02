/**
 * 
 */
package org.lexevs.cts2.author;

import static org.junit.Assert.fail;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;

/**
 * @author m004181
 *
 */
public class ConceptDomainAuthoringOperationImplTest {

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#createConceptDomain(java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)}.
	 * @throws LBException 
	 */
	@Test
	public void testCreateConceptDomain() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR200");
		
		Properties props = new Properties();
		Property prop = new Property();
		prop.setPropertyId("cd3propId1");
		Text value = new Text();
		value.setContent("cd3content");
		prop.setValue(value);
		prop.setPropertyName("cd3propertyName");
		props.addProperty(prop);
		
		cdAuthop.createConceptDomain("cdunitest3", "cd unit test 3", rev, "cd unit test 3", "testing", false, props, null);
		
		props = new Properties();
		prop = new Property();
		prop.setPropertyId("cd4propId1");
		value = new Text();
		value.setContent("cd4content");
		prop.setValue(value);
		prop.setPropertyName("cd4propertyName");
		props.addProperty(prop);
		
		cdAuthop.createConceptDomain("cdunitest4", "cd unit test 4", rev, "cd unit test 4", "testing", true, props, null);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainStatus(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testUpdateConceptDomainStatus() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR201");
		
		cdAuthop.updateConceptDomainStatus("cdunitest4", "pending", null, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#activateConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testActivateConceptDomain() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR202");
		
		cdAuthop.activateConceptDomain("cdunitest1", null, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#deactivateConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws LBException 
	 */
	@Test
	public void testDeactivateConceptDomain() throws LBException {
		ConceptDomainAuthoringOperation cdAuthop = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getConceptDomainAuthoringOperation();
		
		RevisionInfo rev = new RevisionInfo();
		rev.setRevisionId("cdR202");
		
		cdAuthop.deactivateConceptDomain("cdunitest1", null, rev);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainVersionable(java.lang.String, org.LexGrid.commonTypes.Versionable, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 */
	@Test
	public void testUpdateConceptDomainVersionable() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#addConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 */
	@Test
	public void testAddConceptDomainProperty() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#updateConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 */
	@Test
	public void testUpdateConceptDomainProperty() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ConceptDomainAuthoringOperationImpl#removeConceptDomainProperty(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)}.
	 */
	@Test
	public void testRemoveConceptDomainProperty() {
		fail("Not yet implemented");
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
	
}
