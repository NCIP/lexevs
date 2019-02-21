/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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

import java.util.Random;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MRMAP2LexGrid;
import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrMap;
import junit.framework.TestCase;

public class MrMapProcessorTest extends TestCase {
	public void testMrMapProcessor() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
  String[] rawData = new String[25];
  Random generator = new Random();
  for(int i = 0; i < rawData.length; i++){
	  
	  rawData[i] = String.valueOf(generator.nextInt(10000));
  }
  MRMAP2LexGrid mrmap = new MRMAP2LexGrid(null, null, null);
  
  MrMap map = mrmap.processMrMapRow(rawData);
  //Should be MAPSETCUI	MAPSAB	MAPSUBSETID	MAPRANK	MAPID	
  //  MAPSID	FROMID	FROMSID	FROMEXPR	FROMTYPE	FROMRULE	
  //  FROMRES	REL	RELA	TOID	TOSID	TOEXPR	TOTYPE	TORULE	
  //  TORES	MAPRULE	MAPRES	MAPTYPE	ATN	ATV
  assertTrue(map.getMapsetcui().equals(rawData[0]));
  assertTrue(map.getMapsab().equals(rawData[1]));
  assertTrue(map.getMapsubsetid().equals(rawData[2]));
  assertTrue(map.getScore().equals(rawData[3]));
  assertTrue(map.getMapid().equals(rawData[4]));
  assertTrue(map.getMapsid().equals(rawData[5]));
  assertTrue(map.getFromid().equals(rawData[6]));
  assertTrue(map.getFromsid().equals(rawData[7]));
  assertTrue(map.getFromexpr().equals(rawData[8]));
  assertTrue(map.getFromtype().equals(rawData[9]));
  assertTrue(map.getFromrule().equals(rawData[10]));
  assertTrue(map.getFromres().equals(rawData[11]));
  assertTrue(map.getRel().equals(rawData[12]));
  assertTrue(map.getRela().equals(rawData[13]));
  assertTrue(map.getToid().equals(rawData[14]));
  assertTrue(map.getTosid().equals(rawData[15]));
  assertTrue(map.getToexpr().equals(rawData[16]));
  assertTrue(map.getTotype().equals(rawData[17]));
  assertTrue(map.getTorule().equals(rawData[18]));
  assertTrue(map.getTores().equals(rawData[19]));
  assertTrue(map.getMaprule().equals(rawData[20]));
  assertTrue(map.getMapres().equals(rawData[21]));
  assertTrue(map.getMaptype().equals(rawData[22]));
  assertTrue(map.getAtn().equals(rawData[23]));
  assertTrue(map.getAtv().equals(rawData[24]));
	}
  
  
}