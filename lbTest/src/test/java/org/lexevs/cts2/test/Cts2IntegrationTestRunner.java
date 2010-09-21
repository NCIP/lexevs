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
import org.lexevs.cts2.author.association.TestAssocAuthoringAll;
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
	TestAssocAuthoringAll.class,
	ValueSetAuthoringOperationImplTest.class,
	
	ConceptDomainAuthoringOperationImplTest.class, //load, authoring and query
	UsageContextAuthoringOperationImplTest.class //load, authoring and query	
	})
public class Cts2IntegrationTestRunner {
	
}


