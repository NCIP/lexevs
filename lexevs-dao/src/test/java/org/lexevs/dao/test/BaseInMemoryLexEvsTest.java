
package org.lexevs.dao.test;

import java.lang.reflect.Field;

import org.junit.BeforeClass;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class BaseInMemoryLexEvsTest.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BaseInMemoryLexEvsTest {
	
	/**
	 * Inits the in memory.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void initInMemory() throws Exception{
	Field field = LexEvsServiceLocator.class.getDeclaredField("CONTEXT_FILE");
		
		field.setAccessible(true);
		
		field.set(null, "lexevsDao-all-in-memory-test.xml");
		
		LexEvsServiceLocator.getInstance();
	}
}