
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;



import java.io.FileNotFoundException;
import java.util.HashMap;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.relations.Relations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lexevs.logging.LoggerFactory;

import junit.framework.TestCase;

public class ProcessMrSatToRelationsTest extends TestCase {
private LgMessageDirectorIF messages = LoggerFactory.getLogger();

public void testProcessMrSatToRelations() throws SecurityException, IllegalArgumentException, FileNotFoundException, NoSuchFieldException, IllegalAccessException{
	  MappingRelationsUtil map = new  MappingRelationsUtil();
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