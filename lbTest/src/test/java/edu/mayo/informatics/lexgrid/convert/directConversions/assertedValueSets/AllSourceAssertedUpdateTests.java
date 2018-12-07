package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LoadAndUpdateSourceAssertedValueSetsTest.class,
		NCItSourceAssertedValueSetUpdateServiceTest.class, CleanUpResolvedValueSetUpdateLoads.class })
public class AllSourceAssertedUpdateTests {

}
