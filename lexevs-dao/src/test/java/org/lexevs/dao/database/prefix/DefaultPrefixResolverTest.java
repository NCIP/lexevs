package org.lexevs.dao.database.prefix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDaoPrefixResolver-test.xml"})
public class DefaultPrefixResolverTest extends LexEvsDbUnitTestBase {

	@Autowired
	protected DefaultPrefixResolver defaultPrefixResolver;
	
	@Test
	public void testGetPrefixNotNull(){
		String prefix = defaultPrefixResolver.getNextCodingSchemePrefix();
		assertNotNull(prefix);
	}
	
	@Test
	public void testGetNextPrefix(){
		String prefix1 = defaultPrefixResolver.getNextCodingSchemePrefix();
		String prefix2 = defaultPrefixResolver.getNextCodingSchemePrefix();
		assertNotSame(prefix1, prefix2);
	}
}
