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
package org.LexGrid.valueset.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.valueset.impl.LexEVSPickListServicesImplTest;
import org.LexGrid.valueset.impl.LexEVSResolvedValueSetTest;
import org.LexGrid.valueset.impl.LexEVSValueSetDefServicesImplTest;

/**
 * Main test suite to test Value Set and Pick List Definition.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VDAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"LG Value Set and Pick List Definition Test");
		//$JUnit-BEGIN$
		suite.addTestSuite(LoadTestDataTest.class);
		suite.addTestSuite(LexEVSValueSetDefServicesImplTest.class);
		suite.addTestSuite(LexEVSPickListServicesImplTest.class);
		suite.addTestSuite(LexEVSResolvedValueSetTest.class);
		suite.addTestSuite(CleanUpTest.class);
		//$JUnit-END$
		return suite;
	}

}