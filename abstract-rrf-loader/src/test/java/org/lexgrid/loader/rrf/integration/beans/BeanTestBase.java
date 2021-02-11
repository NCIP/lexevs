
package org.lexgrid.loader.rrf.integration.beans;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class BeanTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@ContextConfiguration(locations = {"/abstractRrfLoaderTest.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BeanTestBase {
	
	@Test
	public void testInit(){
		assertNotNull(this);
	}

}