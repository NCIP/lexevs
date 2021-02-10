
package org.LexGrid.LexBIG.Utility;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * JUnit TestRunner to allow for ordering of methods using Springs {@link Order} annotation.
 *
 * Test are run in order of the value of the {@link Order} annotation, from low values to high,
 * with un-annotated tests given a value of 0.
 *
 * For example:
 * {@code
 * public static class Test {
 *
 *       @Order(2)
 *       @Test
 *       public void test1() {
 *       // this is run fourth
 *       }
 *
 *       @Order(1)
 *       @Test
 *       public void test2() {
 *       // this is run third
 *       }
 *
 *       @Test
 *       public void test3() {
 *       // this is run second (un-annotated tests are assumed a value of 0)
 *       }
 *
 *       @Order(-1)
 *       @Test
 *       public void test4() {
 *       // this is run first
 *       }
 * }
 * }
 *
 * To enable ordering, annotate the test class with the {@link org.junit.runner.RunWith} annotation:
 *
 * {@code @RunWith(OrderingTestRunner.class)
 * public class MyOrderedTest {
 *     //
 * }
 * }
 */
public class OrderingTestRunner extends BlockJUnit4ClassRunner {

    public OrderingTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> tests = super.computeTestMethods();

        Collections.sort(tests, new Comparator<FrameworkMethod>() {

            @Override
            public int compare(FrameworkMethod method1, FrameworkMethod method2) {
                Order order1 = method1.getAnnotation(Order.class);
                Order order2 = method2.getAnnotation(Order.class);

                int value1 = order1 == null ? 0 : order1.value();
                int value2 = order2 == null ? 0 : order2.value();

                return value1 - value2;
            }
        });

        return tests;
    }
}