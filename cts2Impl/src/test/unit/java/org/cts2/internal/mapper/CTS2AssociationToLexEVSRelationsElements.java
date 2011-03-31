package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedData;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.relations.AssociationEntity;
import org.junit.Test;

public class CTS2AssociationToLexEVSRelationsElements extends
		BaseDozerBeanMapperTest {
	String codingSchemeName = "NCI Thesaurus";
	String codingSchemeURI = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	String association = "is_a";
	String conceptCode = "C1234";
	String conceptCode1 = "C4321";
	String nameSpace = "NCIt";
	

	@Test
	public void AssociationDirectoryEntryToLexBIGAssociationgTest() {

		org.LexGrid.LexBIG.DataModel.Core.Association lbAssoc = new org.LexGrid.LexBIG.DataModel.Core.Association();
		lbAssoc.setAssociationName(association);
		org.cts2.association.AssociationDirectoryEntry cts2Assoc = baseDozerBeanMapper.map(
				lbAssoc, org.cts2.association.AssociationDirectoryEntry.class);
		assertEquals(association, cts2Assoc.getPredicate().getContent());
	}

	@Test
	public void AssociationDirectoryEntryToResolvedConceptReferenceTest() {
		ResolvedConceptReference rcnr = new ResolvedConceptReference();
		Entity entity = new Entity();
		entity.setEntityCode(conceptCode);
		entity.setEntityCodeNamespace(nameSpace);
		rcnr.setEntity(entity);
		org.cts2.association.AssociationDirectoryEntry cts2Assoc = baseDozerBeanMapper.map(
				rcnr, org.cts2.association.AssociationDirectoryEntry.class);
		assertEquals(conceptCode, cts2Assoc.getSubject().getLocalEntityName()
				.getName());
		assertEquals(nameSpace, cts2Assoc.getSubject().getLocalEntityName()
				.getNamespace());
	}
	@Test
	public void AssociationDirectoryEntryToCodingSchemeTest(){
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName(codingSchemeName);
		cs.setCodingSchemeURI(codingSchemeURI);
		org.cts2.association.AssociationDirectoryEntry cts2Assoc = baseDozerBeanMapper.map(
				cs, org.cts2.association.AssociationDirectoryEntry.class);
		assertEquals(codingSchemeName, cts2Assoc.getAssertedBy().getContent());
		assertEquals(codingSchemeURI, cts2Assoc.getAssertedBy().getMeaning());
	}
	@Test
	public void AssociationDirectoryEntryToSupportedCodingScheme(){
		SupportedCodingScheme supCodingScheme = new SupportedCodingScheme();
		supCodingScheme.setUri(codingSchemeURI);
		org.cts2.association.AssociationDirectoryEntry cts2Assoc = baseDozerBeanMapper.map(
				supCodingScheme, org.cts2.association.AssociationDirectoryEntry.class);
		assertEquals(codingSchemeURI,cts2Assoc.getPredicate().getMeaning());
	}
	@Test
	public void AssociationDirectoryEntryToAssociatedConceptTest() {
		AssociatedConcept ac = new AssociatedConcept();
		Entity entity = new Entity();
		entity.setEntityCode(conceptCode1);
		entity.setEntityCodeNamespace(nameSpace);
		ac.setReferencedEntry(entity);
		org.cts2.association.AssociationDirectoryEntry cts2Assoc = baseDozerBeanMapper.map(
				ac, org.cts2.association.AssociationDirectoryEntry.class);
		assertEquals(conceptCode1, cts2Assoc.getTarget().getEntity()
				.getLocalEntityName().getName());
		assertEquals(nameSpace, cts2Assoc.getSubject().getLocalEntityName()
				.getNamespace());
	}
	@Test
	public void AssociationDirectoryEntryToCodingSchemeDefinition(){
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName(codingSchemeName);
		cs.setCodingSchemeURI(codingSchemeURI);
		org.cts2.association.AssociationDirectoryEntry cts2Assoc = baseDozerBeanMapper.map(
				cs, org.cts2.association.AssociationDirectoryEntry.class);
		assertEquals(codingSchemeName, cts2Assoc.getAssertedBy().getContent());
		assertEquals(codingSchemeURI, cts2Assoc.getAssertedBy().getMeaning());
	}

	@Test
	public void AssociationToAssociatedDataTest() {
		AssociatedData ad = new AssociatedData();
		ad.setId("id123");
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				ad, org.cts2.association.Association.class);
		assertEquals("id123", cts2Assoc.getExternalStatementId());
	}
	@Test
	public void AssociationToLexBIGAssociationgTest() {

		org.LexGrid.LexBIG.DataModel.Core.Association lbAssoc = new org.LexGrid.LexBIG.DataModel.Core.Association();
		lbAssoc.setAssociationName(association);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				lbAssoc, org.cts2.association.Association.class);
		assertEquals(association, cts2Assoc.getPredicate().getContent());
	}

	@Test
	public void AssociationToResolvedConcepteferenceTest() {
		ResolvedConceptReference rcnr = new ResolvedConceptReference();
		Entity entity = new Entity();
		entity.setEntityCode(conceptCode);
		entity.setEntityCodeNamespace(nameSpace);
		rcnr.setEntity(entity);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				rcnr, org.cts2.association.Association.class);
		assertEquals(conceptCode, cts2Assoc.getSubject().getLocalEntityName()
				.getName());
		assertEquals(nameSpace, cts2Assoc.getSubject().getLocalEntityName()
				.getNamespace());
	}
	@Test
	public void AssociationToCodingSchemeTest(){
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName(codingSchemeName);
		cs.setCodingSchemeURI(codingSchemeURI);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				cs, org.cts2.association.Association.class);
		assertEquals(codingSchemeName, cts2Assoc.getAssertedBy().getContent());
		assertEquals(codingSchemeURI, cts2Assoc.getAssertedBy().getMeaning());
	}
	@Test
	public void AssociationToSupportedCodingScheme(){
		SupportedCodingScheme supCodingScheme = new SupportedCodingScheme();
		supCodingScheme.setUri(codingSchemeURI);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				supCodingScheme, org.cts2.association.Association.class);
		assertEquals(codingSchemeURI,cts2Assoc.getPredicate().getMeaning());
	}
	@Test
	public void AssociationToAssociatedConceptTest() {
		AssociatedConcept ac = new AssociatedConcept();
		Entity entity = new Entity();
		entity.setEntityCode(conceptCode1);
		entity.setEntityCodeNamespace(nameSpace);
		ac.setReferencedEntry(entity);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				ac, org.cts2.association.Association.class);
		assertEquals(conceptCode1, cts2Assoc.getTarget().getEntity()
				.getLocalEntityName().getName());
		assertEquals(nameSpace, cts2Assoc.getSubject().getLocalEntityName()
				.getNamespace());
	}
	@Test
	public void AssociationToCodingSchemeDefinition(){
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName(codingSchemeName);
		cs.setCodingSchemeURI(codingSchemeURI);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				cs, org.cts2.association.Association.class);
		assertEquals(codingSchemeName, cts2Assoc.getAssertedBy().getContent());
		assertEquals(codingSchemeURI, cts2Assoc.getAssertedBy().getMeaning());
		assertEquals(codingSchemeName, cts2Assoc.getAssertedIn().getContent());
		assertEquals(codingSchemeURI, cts2Assoc.getAssertedIn().getMeaning());
	}
	@Test
	public void AssociationGraphToResolvedConceptReferenceTest() {
		ResolvedConceptReference rcnr = new ResolvedConceptReference();
		Entity entity = new Entity();
		entity.setEntityCode(conceptCode);
		entity.setEntityCodeNamespace(nameSpace);
		rcnr.setEntity(entity);
		org.cts2.association.AssociationGraph cts2Assoc = baseDozerBeanMapper.map(
				rcnr, org.cts2.association.AssociationGraph.class);
		assertEquals(conceptCode, cts2Assoc.getFocusEntity().getLocalEntityName()
				.getName());
		assertEquals(nameSpace, cts2Assoc.getFocusEntity().getLocalEntityName()
				.getNamespace());
	}
	@Test
	public void GraphNodeToLexBIGAssociationgTest() {

		org.LexGrid.LexBIG.DataModel.Core.Association lbAssoc = new org.LexGrid.LexBIG.DataModel.Core.Association();
		lbAssoc.setAssociationName(association);
		org.cts2.association.GraphNode cts2Assoc = baseDozerBeanMapper.map(
				lbAssoc, org.cts2.association.GraphNode.class);
		assertEquals(association, cts2Assoc.getPredicate().getContent());
	}
	@Test
	public void GraphNodeToAssociatedConceptTest() {
		AssociatedConcept ac = new AssociatedConcept();
		Entity entity = new Entity();
		entity.setEntityCode(conceptCode1);
		entity.setEntityCodeNamespace(nameSpace);
		ac.setReferencedEntry(entity);
		org.cts2.association.GraphNode cts2Assoc = baseDozerBeanMapper.map(
				ac, org.cts2.association.GraphNode.class);
		assertEquals(conceptCode1, cts2Assoc.getTarget().getEntity()
				.getLocalEntityName().getName());

	}
	@Test
	public void ResolvedConceptReferenceListToAssociationDiretory(){
		ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();
		ResolvedConceptReference rcr = new ResolvedConceptReference();
		Entity entity = new Entity();
		entity.setEntityCode(conceptCode1);
		entity.setEntityCodeNamespace(nameSpace);
		rcr.setEntity(entity);
		rcrl.addResolvedConceptReference(rcr);
		org.cts2.association.AssociationDirectory cts2Assoc = baseDozerBeanMapper.map(
				rcrl, org.cts2.association.AssociationDirectory.class);
		assertEquals(conceptCode1, cts2Assoc.getEntry(0).getSubject().getLocalEntityName().getName());
		assertEquals(nameSpace, cts2Assoc.getEntry(0).getSubject().getLocalEntityName().getNamespace());
	}
	
	public void AssociationEntityToAssociation(){
		AssociationEntity entity = new AssociationEntity();
		entity.setEntityCode(conceptCode1);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				entity, org.cts2.association.Association.class);
		assertEquals(conceptCode1, cts2Assoc.getTarget().getEntity()
				.getLocalEntityName().getName());
	}

}
