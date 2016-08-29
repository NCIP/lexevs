package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;


import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;

public class ContentScrubberTestIT extends DataLoadTestBase {
	/** The test entity. */
	private Entity testEntity;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		cns.restrictToCodes(Constructors.createConceptReferenceList("23106:TG"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntity = rcr1.getEntity();
	}
	@Test
	public void contentscrubberTest() {
		assertNotNull(testEntity.getDefinition());
		assertTrue(testEntity.getDefinition().length == 1);
		Definition[] defs = testEntity.getDefinition();
		assertFalse(defs[0].getValue().getContent().contains("\t"));
		assertFalse(defs[0].getValue().getContent().contains("<p>"));
		assertFalse(defs[0].getValue().getContent().contains("</p>"));
		assertFalse(defs[0].getValue().getContent().contains("<b>"));
		assertFalse(defs[0].getValue().getContent().contains("</b>"));
		assertFalse(defs[0].getValue().getContent().contains("<i>"));
		assertFalse(defs[0].getValue().getContent().contains("</i>"));
		assertFalse(defs[0].getValue().getContent().contains("<li>"));
		assertFalse(defs[0].getValue().getContent().contains("</li>"));
		assertFalse(defs[0].getValue().getContent().contains("<ul>"));
		assertFalse(defs[0].getValue().getContent().contains("</ul>"));
		assertTrue(defs[0].getValue().getContent().contains("\n"));
		assertTrue(defs[0].getValue().getContent().contains(">"));
		
	}

}
