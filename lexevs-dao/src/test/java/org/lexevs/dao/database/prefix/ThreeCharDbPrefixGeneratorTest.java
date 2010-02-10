package org.lexevs.dao.database.prefix;

import org.junit.Test;

public class ThreeCharDbPrefixGeneratorTest {

	@Test
	public void testUnique(){
		ThreeCharDbPrefixGenerator generator = new ThreeCharDbPrefixGenerator();
		String currentIdentifier = "a";
		for(int i = 0;i<10;i++){
			String nextId = generator.generateNextDatabasePrefix(currentIdentifier);
			System.out.println(nextId);
			currentIdentifier = nextId;
		}
	}
}
