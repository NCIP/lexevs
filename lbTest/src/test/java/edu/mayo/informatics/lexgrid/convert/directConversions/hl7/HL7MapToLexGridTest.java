
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7;

import static org.junit.Assert.*;

import javax.management.relation.Relation;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HL7MapToLexGridTest {

	String accessPath = "resources/testData/rimSample.mdb";
	CodingScheme codingScheme = null;
	
	@Before
	public void setUp() {
		try {
			HL72LGMain loader = new HL72LGMain();
			LgMessageDirectorIF messages = 
				new MessageDirector(this.getClass().getName(), new ProcessStatus());		
			codingScheme = new CodingScheme();
			codingScheme = loader.map(accessPath, false, messages);
		}
		catch (Exception e) {
			fail("Unable to connect to the MS Access database.");
		}
	}
	
	@After
	public void tearDown() {
		codingScheme = null;
	}
	
	@Test
	public void testRelations() {
		assertEquals(2, codingScheme.getRelations()[0].getAssociationPredicateCount());
		
		int sourceCount = 0;
		int targetCount = 0;		
		for (AssociationPredicate ap : codingScheme.getRelations()[0].getAssociationPredicate()) {
			sourceCount += ap.getSourceCount();
			for (AssociationSource source : ap.getSource()) {
				if (source.getSourceEntityCode().equals("10199:INT"))
					assertEquals(6, source.getTargetCount());
			    targetCount += source.getTargetCount();
			}
		}		
		assertEquals(12, sourceCount);
		assertEquals(34, targetCount);
	}
}