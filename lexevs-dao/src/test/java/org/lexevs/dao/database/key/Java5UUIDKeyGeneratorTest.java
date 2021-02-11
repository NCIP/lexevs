
package org.lexevs.dao.database.key;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.test.annotation.Repeat;

/**
 * The Class Java5UUIDKeyGeneratorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class Java5UUIDKeyGeneratorTest extends LexEvsDbUnitTestBase{

	/**
	 * Test get uuid not null.
	 */
	@Test
	public void testGetUUIDNotNull(){
		Java5UUIDKeyGenerator generator = new Java5UUIDKeyGenerator();
		assertNotNull(generator.getKey(null));
	}
	
	/**
	 * Test get uuid unique.
	 */
	@Test
	@Repeat(10)
	public void testGetUUIDUnique(){
		Set<UUID> set = new HashSet<UUID>();
		Java5UUIDKeyGenerator generator = new Java5UUIDKeyGenerator();
		
		int iterations = 10000;
		
		for(int i=0;i<iterations;i++){
			UUID uuid = generator.getKey(null);
			if(set.contains(uuid)){
				fail("Found a non-unique UUID");
			}
			set.add(uuid);
		}	
	}
}