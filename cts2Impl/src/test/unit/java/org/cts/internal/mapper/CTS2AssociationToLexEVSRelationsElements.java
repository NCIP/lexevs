package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedData;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedCodedNodeReference;
import org.LexGrid.concepts.Entity;
import org.junit.Test;

public class CTS2AssociationToLexEVSRelationsElements extends
		BaseDozerBeanMapperTest {

	@Test
	public void AssociationToLexBIGAssociationgTest() {

		org.LexGrid.LexBIG.DataModel.Core.Association lbAssoc = new org.LexGrid.LexBIG.DataModel.Core.Association();
		lbAssoc.setAssociationName("is_a");
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				lbAssoc, org.cts2.association.Association.class);
		assertEquals("is_a", cts2Assoc.getPredicate().getMeaning());
	}

	@Test
	public void AssociationToResolvedCodedNodeReferenceTest() {
		ResolvedCodedNodeReference rcnr = new ResolvedCodedNodeReference();
		Entity entity = new Entity();
		entity.setEntityCode("C1234");
		entity.setEntityCodeNamespace("NCIt");
		rcnr.setEntity(entity);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				rcnr, org.cts2.association.Association.class);
		assertEquals("C1234", cts2Assoc.getSubject().getLocalEntityName()
				.getName());
		assertEquals("NCIt", cts2Assoc.getSubject().getLocalEntityName()
				.getNamespace());
	}

	@Test
	public void AssociationToAssociatedConceptTest() {
		AssociatedConcept ac = new AssociatedConcept();
		Entity entity = new Entity();
		entity.setEntityCode("C4321");
		entity.setEntityCodeNamespace("NCIt");
		ac.setReferencedEntry(entity);
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				ac, org.cts2.association.Association.class);
		assertEquals("C4321", cts2Assoc.getTarget().getEntity()
				.getLocalEntityName().getName());
		assertEquals("NCIt", cts2Assoc.getSubject().getLocalEntityName()
				.getNamespace());
	}

	@Test
	public void AssociationToAssociatedDataTest() {
		AssociatedData ad = new AssociatedData();
		ad.setId("id123");
		org.cts2.association.Association cts2Assoc = baseDozerBeanMapper.map(
				ad, org.cts2.association.Association.class);
		assertEquals("id123", cts2Assoc.getExternalStatementId());
	}
}
