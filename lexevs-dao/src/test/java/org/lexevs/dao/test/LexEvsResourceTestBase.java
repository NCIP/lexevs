package org.lexevs.dao.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsResource-test.xml"})
public class LexEvsResourceTestBase {

	
	@Test
	public void testConfig(){
		assertNotNull(this);
	}
	
	
}
