
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import org.LexGrid.relations.AssociationData;
import junit.framework.TestCase;

public class CreateNewAssociationDataTest extends TestCase {
	public void testCreateTargetData(){
		MrMap map = new MrMap();
		map.setMapid("AT102971857");
		map.setToexpr("<TRUTH> OR <DARE>");
		MRMAP2LexGrid mapping = new MRMAP2LexGrid(null, null, null);
		AssociationData data = mapping.createTargetData(map);
		assertTrue(data.getAssociationDataText().getContent().equals("<TRUTH> OR <DARE>"));
		assertTrue(data.getAssociationInstanceId().equals("AT102971857"));
	}
}