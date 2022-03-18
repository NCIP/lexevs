
package org.lexevs.dao.database.key;

import java.util.UUID;

/**
 * The Class Java5UUIDKeyGenerator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class Java5UUIDKeyGenerator implements KeyProvider<Object,UUID> {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.KeyProvider#getKey(java.lang.Object)
	 */
	public UUID getKey(Object record) {
		//the actual record doesn't matter -- all generated keys will be unique.
		return UUID.randomUUID();
	}
	
	/**
	 * Gets the random uuid.
	 * 
	 * @return the random uuid
	 */
	public static UUID getRandomUUID(){
		return UUID.randomUUID();
	}
}