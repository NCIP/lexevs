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

import junit.framework.TestCase;

public class ProcessMrSatRowTest extends TestCase {
	public void testProcessMrSatRow() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{

			  String[] rawData = new String[13];
			  Random generator = new Random();
			  for(int i = 0; i < rawData.length; i++){
				  
				  rawData[i] = String.valueOf(generator.nextInt(10000));
			  }
			  MRMAP2LexGrid mrsat = new MRMAP2LexGrid(null, null, null);
			  int count = 0;
			  MrSat sat = mrsat.processMrSatRow(rawData, count);
			  //Should be     
			  //	cui
			  //	lui
			  //	sui
			  //  metaui
			  //	stype
			  	//	code    
			  //	atui    
			  //	satui   
			  //	atn
			  //	sab 
			  //	atv
			  //	suppress
			  //	cvf 
			  
			  assertTrue(sat.getCui().equals(rawData[0]));
			  assertTrue(sat.getLui().equals(rawData[1]));
			  assertTrue(sat.getSui().equals(rawData[2]));
			  assertTrue(sat.getMetaui().equals(rawData[3]));
			  assertTrue(sat.getStype().equals(rawData[4]));
			  assertTrue(sat.getCode().equals(rawData[5]));
			  assertTrue(sat.getAtui().equals(rawData[6]));
			  assertTrue(sat.getSatui().equals(rawData[7]));
			  assertTrue(sat.getAtn().equals(rawData[8]));
			  assertTrue(sat.getSab().equals(rawData[9]));
			  assertTrue(sat.getAtv().equals(rawData[10]));
			  assertTrue(sat.getSuppress().equals(rawData[11]));
			  assertTrue(sat.getCvf().equals(rawData[12]));
			  
	}
}