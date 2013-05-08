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
package org.lexevs.cts2.author.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

import junit.framework.TestCase;

public class TestRemoveRevisions extends TestCase {
public void testRemoveRevisionsCleanUP() throws LBException{
	AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	assertTrue(authServ.removeRevisionRecordbyId("NEW_MAPPING"));
	assertTrue(authServ.removeRevisionRecordbyId("TestNewForExistingCTSMapping"));
	assertTrue(authServ.removeRevisionRecordbyId("TestModifyForUpdatePredicate"));
	assertTrue(authServ.removeRevisionRecordbyId("TestNewForExistingScheme"));
}
}