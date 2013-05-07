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

import org.LexGrid.relations.AssociationTarget;

import junit.framework.TestCase;

public class CreateAssociationTargetTest extends TestCase {
	public void testCreateAssociationTarget() throws Exception{
	MrMap map = new MrMap();
	map.setMapid("AT102971857");
	map.setToexpr("C123");
	MRMAP2LexGrid mapping = new MRMAP2LexGrid(null, null, null);
	AssociationTarget target = mapping.createAssociationTarget(map, "targetNamespace");
	assertSame(target.getAssociationInstanceId(),"AT102971857");
	assertSame(target.getTargetEntityCode(),"C123");
	assertTrue(target.getAssociationQualification().length > 0);
	}
}