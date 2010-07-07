/**
 * 
 */
package org.lexevs.cts2.author;

import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.valueSets.CodingSchemeReference;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;

/**
 * @author m004181
 *
 */
public class ValueSetAuthoringOperationImplTest {

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#addDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void testAddDefinitionEntry() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("R002");
		revInfo.setChangeAgent("testChangeAgent");
		revInfo.setChangeInstruction("Adding new definition entry");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("adding new def entry");
		revInfo.setRevisionDate(new Date());
		
		DefinitionEntry newDefEntry = new DefinitionEntry();
		newDefEntry.setOperator(DefinitionOperator.OR);
		newDefEntry.setRuleOrder(0L);
		
		CodingSchemeReference csRef = new CodingSchemeReference();
		csRef.setCodingScheme("Automobiles");
		
		newDefEntry.setCodingSchemeReference(csRef);
		ValueSetAuthoringOperation valueSetAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		
		valueSetAuthOp.addDefinitionEntry(new URI("VSD:AUTHOR:JUNIT:TEST1"), newDefEntry, revInfo);
	}
	
	@Test
	public void testAddDefinitionEntry2() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("R007");
		revInfo.setChangeAgent("testChangeAgent");
		revInfo.setChangeInstruction("Adding new definition entry");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("adding new def entry");
		revInfo.setRevisionDate(new Date());
		
		DefinitionEntry newDefEntry = new DefinitionEntry();
		newDefEntry.setOperator(DefinitionOperator.OR);
		newDefEntry.setRuleOrder(0L);
		
		ValueSetDefinitionReference vsdRef = new ValueSetDefinitionReference();
		vsdRef.setValueSetDefinitionURI("somevaluesetref");
		
		newDefEntry.setValueSetDefinitionReference(vsdRef);
		ValueSetAuthoringOperation valueSetAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		
		valueSetAuthOp.addDefinitionEntry(new URI("VSD:AUTHOR:JUNIT:TEST1"), newDefEntry, revInfo);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#addValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void testAddValueSetProperty() throws LBException, URISyntaxException {
		Property prop = new Property();
		prop.setPropertyId("propertyId1");
		prop.setPropertyName("propertyName");
		prop.setIsActive(true);
		prop.setLanguage("en");
		prop.setOwner("owner");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("content");
		prop.setValue(text);
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R102");
		
		ValueSetAuthoringOperation valueSetAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		valueSetAuthOp.addValueSetProperty(new URI("VSD:AUTHOR:JUNIT:TEST1"), 
				prop, revInfo);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#createValueSet(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.LexGrid.commonTypes.Properties, org.LexGrid.valueSets.DefinitionEntry, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void testCreateValueSetUsingURI() throws LBException, URISyntaxException {
		Properties props = new Properties();
		Property prop = new Property();
		prop.setPropertyId("p1");
		prop.setIsActive(true);
		prop.setLanguage("en");
		prop.setOwner("owner");
		prop.setPropertyName("propertyName");
		prop.setPropertyType(PropertyTypes.PROPERTY.name());
		Text text = new Text();
		text.setContent("prop value");
		prop.setValue(text);
		
		props.addProperty(prop);
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setChangeAgent("changeAgent");
		revInfo.setChangeInstruction("changeInstruction");
		revInfo.setDescription("description");
		revInfo.setEditOrder(1L);
		revInfo.setRevisionDate(new Date());
		revInfo.setRevisionId("R101");
		
		ValueSetAuthoringOperation valueSetAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		URI vsdURI = valueSetAuthOp.createValueSet(new URI("VSD:AUTHORING:JUNIT:TEST2"), 
				"Authoring create vsd junit test2",
				"Automobiles", "Autos", null, null, 
				props, null, null, revInfo);
		System.out.println("vsdURI : " + vsdURI);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#createValueSet(org.LexGrid.valueSets.ValueSetDefinition, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 * @throws LBException 
	 */
	@Test
	public void testCreateValueSetUsingValueSetDefinition() throws LBException {
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("VSD:AUTHOR:JUNIT:TEST1");
		vsd.setValueSetDefinitionName("Authoring create junit test1");
		vsd.setConceptDomain("testConceptDomain");
		
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("R001");
		revInfo.setChangeAgent("testChangeAgent");
		revInfo.setChangeInstruction("testChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testDescription");
		revInfo.setRevisionDate(new Date());
		
		ValueSetAuthoringOperation valueSetAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		URI vsdURI = valueSetAuthOp.createValueSet(vsd, revInfo);
		System.out.println("vsdURI : " + vsdURI);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 */
	@Test
	public void testUpdateDefinitionEntry() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateValueSetMetaData(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)}.
	 * @throws URISyntaxException 
	 * @throws LBException 
	 */
	@Test
	public void testUpdateValueSetMetaData() throws LBException, URISyntaxException {
		RevisionInfo revInfo = new RevisionInfo();
		revInfo.setRevisionId("R201");
		revInfo.setChangeAgent("testUpdateChangeAgent");
		revInfo.setChangeInstruction("testUpdateChangeInstruction");
		revInfo.setEditOrder(0L);
		revInfo.setDescription("testUpdateDescription");
		revInfo.setRevisionDate(new Date());
		
		List<Source> srcList = new ArrayList<Source>();
		Source src = new Source();
		src.setContent("LexEVS as src");
		srcList.add(src);
		src = new Source();
		src.setContent("Mayo as src");
		srcList.add(src);
		
		List<String> contextList = new ArrayList<String>();
		contextList.add("Autos as context");
		contextList.add("lexevs as context");
		
		ValueSetAuthoringOperation valueSetAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		boolean success = valueSetAuthOp.updateValueSetMetaData(new URI("VSD:AUTHORING:JUNIT:TEST2"), 
				"Authoring create vsd junit test2 updated",
				"Automobiles updated", "Autos updated", srcList, contextList, 
				revInfo);
		System.out.println("success : " + success);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 */
	@Test
	public void testUpdateValueSetProperty() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateValueSetStatus(java.net.URI, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 */
	@Test
	public void testUpdateValueSetStatus() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#updateValueSetVersionable(java.net.URI, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 */
	@Test
	public void testUpdateValueSetVersionable() {
		fail("Not yet implemented"); // TODO
	}

}
