package org.LexGrid.LexBIG.Utility;

import junit.framework.TestCase;
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


    public static class Test {

        @Order(2)
        @org.junit.Test
        public void zTest() {
            //
        }

        @Order(1)
        @org.junit.Test
        public void aTest() {
            //
        }

        @org.junit.Test
        public void jTest() {
            //
        }

        @Order(-1)
        @org.junit.Test
        public void qTest() {
            //
        }
    }
}
