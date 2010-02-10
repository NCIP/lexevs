package org.lexevs.dao.system.constants;

import javax.annotation.Resource;

import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class IndividualVariableFactoryTest extends LexEvsDbUnitTestBase {
	
	@Resource
	private String dbPrefix;
	
	public void testGetPrefix() throws Exception{
		assertEquals("lb", dbPrefix);
	}
}
