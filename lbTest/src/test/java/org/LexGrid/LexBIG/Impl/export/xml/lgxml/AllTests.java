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
package org.LexGrid.LexBIG.Impl.export.xml.lgxml;

import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;

import junit.framework.Test;
import junit.framework.TestSuite;




public class AllTests {

	public static Test suite() {
		ServiceHolder.configureForSingleConfig();
		TestSuite suite = new TestSuite("LexGrid XML Exporter Tests");
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.ExportAutomobiles2.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.AssociationLoop.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.MultipuleRelations.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.EntitiesOnly.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.AssociationsOnly.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.RestrictToAssociation.class);
		return suite;
	}	
}