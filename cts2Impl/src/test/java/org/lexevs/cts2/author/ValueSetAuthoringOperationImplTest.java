/**
 * 
 */
package org.lexevs.cts2.author;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;

/**
 * @author m004181
 *
 */
public class ValueSetAuthoringOperationImplTest {

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#addDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 */
	@Test
	public void testAddDefinitionEntry() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#addValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 */
	@Test
	public void testAddValueSetProperty() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.author.ValueSetAuthoringOperationImpl#createValueSet(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.LexGrid.commonTypes.Properties, org.LexGrid.valueSets.DefinitionEntry, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)}.
	 */
	@Test
	public void testCreateValueSetUsingURI() {
		fail("Not yet implemented"); // TODO
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
		
		EntryState es = new EntryState();
		es.setChangeType(ChangeType.NEW);
		es.setContainingRevision("R001");
		es.setRelativeOrder(0L);
		
		ValueSetAuthoringOperation valueSetAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		URI vsdURI = valueSetAuthOp.createValueSet(vsd, revInfo, es);
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
	 */
	@Test
	public void testUpdateValueSetMetaData() {
		fail("Not yet implemented"); // TODO
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
