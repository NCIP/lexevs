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
package org.lexevs.cts2.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.lexevs.cts2.admin.export.AssociationExportOperationImplTest;
import org.lexevs.cts2.admin.export.CodeSystemExportOperationImplTest;
import org.lexevs.cts2.admin.export.ValueSetExportOperationImplTest;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperationTest;
import org.lexevs.cts2.admin.load.ValueSetLoadOperationImplTest;
import org.lexevs.cts2.author.CodeSystemAuthoringOperationImplTest;
import org.lexevs.cts2.author.ConceptDomainAuthoringOperationImplTest;
import org.lexevs.cts2.author.UsageContextAuthoringOperationImplTest;
import org.lexevs.cts2.author.ValueSetAuthoringOperationImplTest;
import org.lexevs.cts2.author.association.TestCTS2AssociationAuthoring;
import org.lexevs.cts2.query.AssociationQueryOperationImplTest;
import org.lexevs.cts2.query.CodeSystemQueryOperationImplTest;
import org.lexevs.cts2.query.ValueSetQueryOperationImplTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	// Admin operations
	CodeSystemLoadOperationTest.class,
	ValueSetLoadOperationImplTest.class,
	CodeSystemExportOperationImplTest.class,
	AssociationExportOperationImplTest.class,
	ValueSetExportOperationImplTest.class,
	// Query operations
	AssociationQueryOperationImplTest.class,
	CodeSystemQueryOperationImplTest.class,
	ValueSetQueryOperationImplTest.class,
	// authoring operations
	CodeSystemAuthoringOperationImplTest.class,
	TestCTS2AssociationAuthoring.class,
	ValueSetAuthoringOperationImplTest.class,
	
	ConceptDomainAuthoringOperationImplTest.class, //load, authoring and query
	UsageContextAuthoringOperationImplTest.class //load, authoring and query	
	})
public class Cts2IntegrationTestRunner {
	
}