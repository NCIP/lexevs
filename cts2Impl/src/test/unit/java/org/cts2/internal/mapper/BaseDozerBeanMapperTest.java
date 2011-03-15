package org.cts2.internal.mapper;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.cts2.internal.mapper.BaseDozerBeanMapper;
import org.cts2.test.BaseCts2UnitTest;
import org.junit.Test;

public class BaseDozerBeanMapperTest extends BaseCts2UnitTest{
	
	@Resource
	protected BaseDozerBeanMapper baseDozerBeanMapper;
	
	@Test
	public void testInitDozer(){
		assertNotNull(baseDozerBeanMapper);
	}
}
