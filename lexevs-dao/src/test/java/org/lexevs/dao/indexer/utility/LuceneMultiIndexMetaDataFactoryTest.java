package org.lexevs.dao.indexer.utility;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-testLLMD.xml"})
public class LuceneMultiIndexMetaDataFactoryTest {
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	private ConcurrentMetaData metadataFactory;
	
	/**
	 * Test that we can use Spring to load our Context and retrieve the ConcurrentMetaData
	 * from it.
	 */
	@Test
	public void testConcurrentMetaDataExists() {
		Object factoryBean = applicationContext.getBean("concurrentCodingSchemeFactory");
		assertNotNull(factoryBean);
	}	
	
	public ConcurrentMetaData getMetadataFactory() {
		return this.metadataFactory;
	}

	public void setMetadataFactory(ConcurrentMetaData metadataFactory) {
		this.metadataFactory = metadataFactory;
	}
}
