package org.cts.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( 
		value = "/cts2-application-config-test.xml")
public class BaseCts2UnitTest {
	
	@Test
	public void testInit(){
		//test to check initialization of test context
	}

}
