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