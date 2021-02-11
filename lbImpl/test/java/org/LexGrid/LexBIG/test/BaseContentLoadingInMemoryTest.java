
package org.LexGrid.LexBIG.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * The Class BaseContentLoadingInMemoryTest.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RunWith(LexEvsTestRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class, ContentLoadingTestListener.class }) 
public class BaseContentLoadingInMemoryTest {

}