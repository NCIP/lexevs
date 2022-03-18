
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.AssociationData;
import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MRMAP2LexGrid;
import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrMap;
import junit.framework.TestCase;

public class CreateNewAssociationSourceWithTargetTest extends TestCase {
 public void testCreateNewAssociationSourceWithTarget() throws Exception{

		MrMap map = new MrMap();
		map.setAtn("asdfa");
		map.setAtv("dghd");
		map.setFromexpr("jgkg");
		map.setFromid("jkl;;j");
		map.setFromres("ruyr");
		map.setFromrule("qer");
		map.setFromsid("afa");
		map.setFromtype("fjhf");
		map.setMapid("AT102971857");
		map.setScore("dghd");
		map.setMapres("xbv");
		map.setMaprule("vmnv");
		map.setMapsab("wrt");
		map.setMapsetcui("afa");
		map.setMapsid("q15r");
		map.setScore("u5i");
		map.setMapres("hjf");
		map.setMapsubsetid("qer");
		map.setMaptype("adf");
		map.setToexpr("<Carcinoma> AND <Glacoma>");
		map.setToid("adf");
		map.setTores("rqer");
		map.setTorule("adf");
		map.setTosid("afa");
		map.setTotype("afd");
		map.setRel("df");
		map.setRela("ew");
		
		MRMAP2LexGrid mapping = new MRMAP2LexGrid(null, null, null);
		AssociationSource source = mapping.createNewAssociationSourceWithTarget(map, "toNameSpace");
		assertTrue(source.getSourceEntityCode().equals("jkl;;j"));
		assertNull(source.getSourceEntityCodeNamespace());
		AssociationTarget target = source.getTarget(0);
		assertTrue(target.getAssociationInstanceId().equals("AT102971857"));
		assertTrue(target.getTargetEntityCode().equals("Carcinoma"));
		assertTrue(target.getTargetEntityCodeNamespace().equals("toNameSpace"));
		assertTrue(target.getAssociationQualification().length == 23);
		assertTrue(source.getTargetData()[0].getAssociationInstanceId().equals("AT102971857"));
		assertTrue(source.getTargetData()[0].getAssociationDataText().getContent().equals("<Carcinoma> AND <Glacoma>"));
 }		
}