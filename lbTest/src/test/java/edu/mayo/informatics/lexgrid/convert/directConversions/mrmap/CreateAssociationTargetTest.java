package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import org.LexGrid.relations.AssociationTarget;

import junit.framework.TestCase;

public class CreateAssociationTargetTest extends TestCase {
	public void testCreateAssociationTarget() throws Exception{
	MrMap map = new MrMap();
	map.setMapid("AT102971857");
	map.setToid("C123");
	MRMAP2LexGrid mapping = new MRMAP2LexGrid(false, null, null, null);
	AssociationTarget target = mapping.createAssociationTarget(map);
	assertSame(target.getAssociationInstanceId(),"AT102971857");
	assertSame(target.getTargetEntityCode(),"C123");
	assertTrue(target.getAssociationQualification().length > 0);
	}
}
