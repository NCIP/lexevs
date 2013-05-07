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

import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import junit.framework.TestCase;

public class AddTargetToExistingSourceTest extends TestCase {
public void testaddTargetToExistingSource() throws IndexOutOfBoundsException, Exception{
	MRMAP2LexGrid mapping = new MRMAP2LexGrid(null, null, null);
	MrMap map = new MrMap();
	map.setFromid("C346");
	map.setMapid("AT095285");
	map.setToexpr("<UP> OR <DOWN>");
	map.setToid("C555");
	AssociationSource source1 = new AssociationSource();
	source1.setSourceEntityCode("C3465");
	AssociationPredicate predicate = new AssociationPredicate();
	AssociationSource source2 = new AssociationSource();
	source2.setSourceEntityCode("C34655555");
	predicate.addSource(source1);
	predicate.addSource(source2);
	predicate.setAssociationName("mapped_to");
	predicate = mapping.addTargetToExistingSource(map, predicate, "fromSource", "toTarget");
	assertNotNull(ObjectToString.toString(predicate), predicate);
	AssociationSource[] sources = predicate.getSource();
	for(AssociationSource s: sources){
		if(s.getSourceEntityCode().equals("c346")){
			assertTrue(s.getTarget(0) != null);
			assertTrue(s.getTarget(0).getTargetEntityCode().equals("C555"));
		}
		if(s.getSourceEntityCode().equals("c3465")){
			assertTrue(s.getTarget(0) == null);
		}
	}
}

//public void testaddTargetToExistingSource2() throws IndexOutOfBoundsException, Exception{
//	MRMAP2LexGrid mapping = new MRMAP2LexGrid(false, null, null, null);
//	MrMap map = new MrMap();
//	map.setFromid("C346");
//	map.setMapid("AT095285");
//	map.setToexpr("<UP> OR <DOWN>");
//	map.setToid("C555");
//	AssociationSource source = new AssociationSource();
//	source.setSourceEntityCode("C346");
//	AssociationSource source1 = new AssociationSource();
//	source1.setSourceEntityCode("C3465");
//	AssociationPredicate predicate = new AssociationPredicate();
//	AssociationSource source2 = new AssociationSource();
//	source2.setSourceEntityCode("C34655555");
//	predicate.addSource(source);
//	predicate.addSource(source1);
//	predicate.addSource(source2);
//	predicate.setAssociationName("mapped_to");
//	mapping.addTargetToExistingSource(map, predicate);
//		
//}
//
//public void testaddTargetToExistingSource3() throws IndexOutOfBoundsException, Exception{
//	MRMAP2LexGrid mapping = new MRMAP2LexGrid(false, null, null, null);
//	MrMap map = new MrMap();
//	map.setFromid("C346");
//	map.setMapid("AT095285");
//	map.setToexpr("<UP> OR <DOWN>");
//	map.setToid("C555");
//	AssociationSource source = new AssociationSource();
//	source.setSourceEntityCode("C346");
//	AssociationSource source1 = new AssociationSource();
//	source1.setSourceEntityCode("C3465");
//	AssociationPredicate predicate = new AssociationPredicate();
//	AssociationSource source2 = new AssociationSource();
//	source2.setSourceEntityCode("C34655555");
//	predicate.addSource(source);
//	predicate.addSource(source1);
//	predicate.addSource(source2);
//	predicate.setAssociationName("mapped_to");
//	mapping.addTargetToExistingSource(map, predicate);
//		
//}

}