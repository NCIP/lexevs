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
import org.lexevs.cts2.author.TestCTS2AssociationAuthoring;
import org.lexevs.cts2.author.ValueSetAuthoringOperationImplTest;
import org.lexevs.cts2.query.AssociationQueryOperationImplTest;
import org.lexevs.cts2.query.CodeSystemQueryOperationImplTest;
import org.lexevs.cts2.query.ValueSetQueryOperationImplTest;
import org.lexevs.cts2.test.integration.SetupCts2IntegrationTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SetupCts2IntegrationTests.class,
	// Admin operations
	CodeSystemLoadOperationTest.class,
	ValueSetLoadOperationImplTest.class,
	CodeSystemExportOperationImplTest.class,
	AssociationExportOperationImplTest.class,
	ValueSetExportOperationImplTest.class,
	// Query operations
	AssociationQueryOperationImplTest.class,
	CodeSystemQueryOperationImplTest.class,
//	ConceptDomainQueryOperationTest.class,
	ValueSetQueryOperationImplTest.class,
	// authoring operations
	CodeSystemAuthoringOperationImplTest.class,
	ConceptDomainAuthoringOperationImplTest.class,
	TestCTS2AssociationAuthoring.class,
	ValueSetAuthoringOperationImplTest.class
//	TearDownCts2IntegrationTests.class
	})
public class Cts2IntegrationTestRunner {
	
}


