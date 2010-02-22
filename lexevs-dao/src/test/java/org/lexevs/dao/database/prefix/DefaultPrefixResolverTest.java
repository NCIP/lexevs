package org.lexevs.dao.database.prefix;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.hibernate.registry.HibernateRegistryDao;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDaoPrefixResolver-test.xml"})
public class DefaultPrefixResolverTest extends LexEvsDbUnitTestBase {

	@Autowired
	protected DefaultPrefixResolver defaultPrefixResolver;
	
	@Autowired
	protected HibernateRegistryDao hibernateRegistryDao;

	@Test
	public void testGetPrefixNotNull(){
		hibernateRegistryDao.initRegistryMetadata();
		String prefix = defaultPrefixResolver.getNextCodingSchemePrefix();
		assertNotNull(prefix);
	}
	
	@Test
	public void testGetNextPrefix(){
		hibernateRegistryDao.initRegistryMetadata();
		String prefix1 = defaultPrefixResolver.getNextCodingSchemePrefix();
		String prefix2 = defaultPrefixResolver.getNextCodingSchemePrefix();
		assertNotSame(prefix1, prefix2);
	}
}
