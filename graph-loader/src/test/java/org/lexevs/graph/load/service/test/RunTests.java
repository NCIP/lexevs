package org.lexevs.graph.load.service.test;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	LoadTestContentTestIT.class, 
	LexEVSTripleServiceImpleTestIT.class,
	CleanupContentTestITTest.class
	})
public class RunTests {}
