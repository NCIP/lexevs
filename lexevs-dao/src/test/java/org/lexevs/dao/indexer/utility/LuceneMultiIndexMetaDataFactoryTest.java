package org.lexevs.dao.indexer.utility;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.index.lucenesupport.LuceneMultiIndexMetaDataFactory;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-testLLMD.xml"})
public class LuceneMultiIndexMetaDataFactoryTest {
	
	private ConcurrentMetaData metadataFactory;
	
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test() {
		assertNotNull(metadataFactory);
	}

	public ConcurrentMetaData getMetadataFactory() {
		return metadataFactory;
	}

	public void setMetadataFactory(ConcurrentMetaData metadataFactory) {
		this.metadataFactory = metadataFactory;
	}
	
	

}
