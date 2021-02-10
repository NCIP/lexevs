
package org.lexevs.dao.database.key;

import java.util.Date;
import java.util.UUID;

/**
 * The Class Java5UUIDKeyGenerator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class COMBKeyGenerator implements KeyProvider<Object,UUID> {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.KeyProvider#getKey(java.lang.Object)
	 */
	public UUID getKey(Object record) {
		//the actual record doesn't matter -- all generated keys will be unique.
		return getSequentialUUID();
	}
	
	/**
	 * Gets the random uuid.
	 * 
	 * @return the random uuid
	 */
	public static UUID getSequentialUUID(){
		Date date = new Date();
		
		long time = date.getTime();
		long uuidLong = UUID.randomUUID().getMostSignificantBits();
		
		return new UUID(uuidLong,time);
	}
	
	public static void main(String[] args) {
		for(int i=0;i<25;i++) {
			System.out.println(getSequentialUUID());
		}
	}
}