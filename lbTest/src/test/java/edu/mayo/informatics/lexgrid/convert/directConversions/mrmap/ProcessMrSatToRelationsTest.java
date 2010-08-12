package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;



import java.io.FileNotFoundException;
import java.util.HashMap;

import org.LexGrid.relations.Relations;

import junit.framework.TestCase;

public class ProcessMrSatToRelationsTest extends TestCase {
public void testProcessMrSatToRelations() throws SecurityException, IllegalArgumentException, FileNotFoundException, NoSuchFieldException, IllegalAccessException{
	  MRMAP2LexGrid map = new MRMAP2LexGrid(null, null, null);
	 HashMap<String, Relations> relationsMap = map.processMrSatBean("resources/testData/mrmap_mapping/MRSAT.RRF", "resources/testData/mrmap_mapping/MRMAP.RRF");
	 Object[] relations = relationsMap.values().toArray();
	  for(Object r: relations){

	  assertNotNull((Relations)r!= null);
	  assertNotNull(((Relations)r).getProperties());
	  assertNotNull(((Relations)r).getProperties().getProperty(0));
	  assertNotNull(((Relations)r).getContainerName());
	  assertNotNull(((Relations)r).getOwner());
	  assertNotNull(((Relations)r).getRepresentsVersion());
	  }
}
}
