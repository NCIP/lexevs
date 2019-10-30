package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

public class NodeGraphResolutionExtensionTest {
	NodeGraphResolutionExtensionImpl ngr;

	@Before
	public void setUp() throws Exception {
		ngr = (NodeGraphResolutionExtensionImpl) LexBIGServiceImpl
				.defaultInstance()
				.getGenericExtension("NodeGraphResolution");
	}

	
	@Test
	public void testValidAssociation() throws LBParameterException {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b");
		assertTrue(ngr.isValidAssociation("subClassOf", ref));
	}
	@Test
	public void testValidNodeInAssociation() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b");
		assertTrue(ngr.isValidNodeForAssociation(ref, "C12434", "subClassOf"));
	}
	
	@Test
	public void testValidNodeInAssociationNot() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b");
		assertFalse(ngr.isValidNodeForAssociation(ref, "C19448", "Anatomic_Structure_Is_Physical_Part_Of"));
	}

}
