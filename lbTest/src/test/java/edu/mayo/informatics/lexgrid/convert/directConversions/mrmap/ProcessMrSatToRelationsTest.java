/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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