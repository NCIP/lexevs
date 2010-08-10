package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;



import java.io.FileNotFoundException;

import org.LexGrid.relations.Relations;

import junit.framework.TestCase;

public class ProcessMrSatToRelationsTest extends TestCase {
public void testProcessMrSatToRelations() throws SecurityException, IllegalArgumentException, FileNotFoundException, NoSuchFieldException, IllegalAccessException{
	  MRMAP2LexGrid map = new MRMAP2LexGrid(null, null, null);
	  Relations relation = map.processMrSatBean("resources/testData/mrmap_mapping/MRSAT.RRF");
	  assertNotNull(relation != null);
	  assertNotNull(relation.getProperties());
	  assertNotNull(relation.getProperties().getProperty(0));
	  assertNotNull(relation.getContainerName());
	  assertNotNull(relation.getOwner());
	  assertNotNull(relation.getRepresentsVersion());
}
}
