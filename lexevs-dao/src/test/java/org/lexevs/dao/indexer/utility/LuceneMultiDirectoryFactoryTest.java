
package org.lexevs.dao.indexer.utility;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-testLLMD.xml"})
public class LuceneMultiDirectoryFactoryTest {

	@Autowired
	protected ApplicationContext applicationContext;
	
	private Resource indexDirectory;
	private LuceneDirectoryCreator luceneDirectoryCreator;
	
	@Test
	public void testResourceExists() {
		Resource resource = (Resource)applicationContext.getBean("multiIndexLocation");
		assertNotNull(resource);
		assertTrue(resource.getFilename().length() > 0);
	}
	
	@Test
	public void testLuceneDirectoryCreatorExists() {
		LuceneDirectoryCreator luceneDirectoryCreator = (LuceneDirectoryCreator)applicationContext.getBean("defaultLuceneDirectoryCreator");
		assertNotNull(luceneDirectoryCreator);
	}

	public LuceneDirectoryCreator getLuceneDirectoryCreator() {
		return luceneDirectoryCreator;
	}

	public void setLuceneDirectoryCreator(
			LuceneDirectoryCreator luceneDirectoryCreator) {
		this.luceneDirectoryCreator = luceneDirectoryCreator;
	}

	public Resource getIndexDirectory() {
		return indexDirectory;
	}

	public void setIndexDirectory(Resource indexDirectory) {
		this.indexDirectory = indexDirectory;
	}	
	
	
		
}