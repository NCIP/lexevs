package org.lexevs.cts2.admin.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.cts2.test.Cts2BaseTest;
import org.lexevs.cts2.test.Cts2TestConstants;

public class AssociationExportOperationImplTest extends Cts2BaseTest {

	private AssociationExportOperationImpl operation = new AssociationExportOperationImpl();
	
	private File exportFile ;
	
	@Before
	public void createFile() {
		exportFile = new File(System.getProperty("java.io.tmpdir") + File.separator + 
				Cts2TestConstants.CTS2_AUTOMOBILES_NAME + "_" + Cts2TestConstants.CTS2_AUTOMOBILES_VERSION + ".xml");
	}
	
	@After
	public void deleteFile() {
		assertTrue(exportFile.delete());
	}
	
	@Test
	public void testExportAssociation() throws Exception {
		
		
		operation.exportAssociation(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				super.getLexBIGService().getNodeGraph(
						Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
						Constructors.createCodingSchemeVersionOrTagFromVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION), 
						null), 
				new File(System.getProperty("java.io.tmpdir")).toURI(), 
				true, 
				true, 
				false);
	
		CodingScheme cs = CodingScheme.unmarshalCodingScheme(new FileReader(exportFile));
		
		assertEquals(1, cs.getRelationsCount());
		assertEquals(3, cs.getRelations(0).getAssociationPredicateCount());	
	}
	
	@Test
	public void testExportOnlyOneAssociation() throws Exception {
		CodedNodeGraph cng = super.getLexBIGService().getNodeGraph(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(Cts2TestConstants.CTS2_AUTOMOBILES_VERSION), 
				null);
		
		cng = cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), null);
		
		operation.exportAssociation(
				Cts2TestConstants.CTS2_AUTOMOBILES_URI, 
				Cts2TestConstants.CTS2_AUTOMOBILES_VERSION, 
				cng, 
				new File(System.getProperty("java.io.tmpdir")).toURI(), 
				true, 
				true, 
				false);
	
		CodingScheme cs = CodingScheme.unmarshalCodingScheme(new FileReader(exportFile));
		
		assertEquals(1, cs.getRelationsCount());
		
		AssociationPredicate hasSubType = this.getAssociationPredicate(cs.getRelations(0), "hasSubtype");
		AssociationPredicate uses = this.getAssociationPredicate(cs.getRelations(0), "uses");
		AssociationPredicate differentEntityCodeAssoc = this.getAssociationPredicate(cs.getRelations(0), "differentEntityCodeAssoc");
		
		assertTrue(hasSubType.getSourceCount() > 0);
		assertEquals(0,uses.getSourceCount());
		assertEquals(0,differentEntityCodeAssoc.getSourceCount());
	}
	
	private AssociationPredicate getAssociationPredicate(Relations relations, String predicateName) {
		for(AssociationPredicate predicate : relations.getAssociationPredicate()) {
			if(predicate.getAssociationName().equals(predicateName)) {
				return predicate;
			}
		}
		
		throw new RuntimeException("not found");
	}
	
}
