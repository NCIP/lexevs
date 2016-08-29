package org.LexGrid.LexBIG.Utility;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.LexGrid.LexBIG.Impl.testUtility.AllTestsNormalConfig;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderingTestRunnerTest extends TestCase {

    @org.junit.Test
    public void testOrder() throws InitializationError {
        OrderingTestRunner runner = new OrderingTestRunner(Test.class);

        List<FrameworkMethod> methods = runner.computeTestMethods();

        List<String> names = new ArrayList<String>();

        for(FrameworkMethod method : methods) {
            names.add(method.getName());
        }

        assertEquals(Arrays.asList("qTest", "jTest", "aTest", "zTest"), names);
    }

    @org.junit.Test
    public void testOrderWithSuite() throws InitializationError {
        TestSuite mainSuite = new TestSuite("Test");

        mainSuite.addTest(AllTestsNormalConfig.orderedSuite(Test.class));

        TestResult result = new TestResult();
        mainSuite.run(result);

        assertEquals(0, result.errorCount());
    }

    private static int last = 0;

    @RunWith(OrderingTestRunner.class)
    public static class Test extends TestCase {

        @Order(2)
        @org.junit.Test
        public void zTest() {
            assertEquals(last, 3);
        }

        @Order(1)
        @org.junit.Test
        public void aTest() {
            assertEquals(last, 2);
            last = 3;
        }

        @org.junit.Test
        public void jTest() {
            assertEquals(last, 1);
            last = 2;
        }

        @Order(-1)
        @org.junit.Test
        public void qTest() {
            assertEquals(last, 0);
            last = 1;
        }
    }
}
