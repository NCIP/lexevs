package org.lexevs.dao.database.key;

import java.util.UUID;

public class Java5UUIDKeyGenerator implements KeyProvider<Object,UUID> {

	public UUID getKey(Object record) {
		//the actual record doesn't matter -- all generated keys will be unique.
		return UUID.randomUUID();
	}
	
	public static UUID getRandomUUID(){
		return UUID.randomUUID();
	}
}
