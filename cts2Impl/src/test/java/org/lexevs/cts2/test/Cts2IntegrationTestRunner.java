package org.lexevs.cts2.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.lexevs.cts2.admin.export.AssociationExportOperationImplTest;
import org.lexevs.cts2.test.integration.SetupCts2IntegrationTests;
import org.lexevs.cts2.test.integration.TearDownCts2IntegrationTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SetupCts2IntegrationTests.class,
	AssociationExportOperationImplTest.class,
	TearDownCts2IntegrationTests.class})
public class Cts2IntegrationTestRunner {
	
}


