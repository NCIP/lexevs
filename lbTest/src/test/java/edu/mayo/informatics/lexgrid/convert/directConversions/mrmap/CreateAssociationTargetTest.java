
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import org.LexGrid.relations.AssociationTarget;

import junit.framework.TestCase;

public class CreateAssociationTargetTest extends TestCase {
	public void testCreateAssociationTarget() throws Exception{
	MrMap map = new MrMap();
	map.setMapid("AT102971857");
	map.setToexpr("C123");
	MRMAP2LexGrid mapping = new MRMAP2LexGrid(null, null, null);
	AssociationTarget target = mapping.createAssociationTarget(map, "targetNamespace");
	assertSame(target.getAssociationInstanceId(),"AT102971857");
	assertSame(target.getTargetEntityCode(),"C123");
	assertTrue(target.getAssociationQualification().length > 0);
	}
}