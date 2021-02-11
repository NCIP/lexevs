
package org.lexevs.dao.database.key;

/**
 * The Class NanoTimeLongKeyGenerator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NanoTimeLongKeyGenerator implements KeyProvider<Object,Long> {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.KeyProvider#getKey(java.lang.Object)
	 */
	private Long lastLong = null;
	
	public synchronized Long getKey(Object record) {
		if(lastLong == null) {
			lastLong = getNanoTimeLongKey();
		}
		for(Long currentTime = getNanoTimeLongKey();currentTime == lastLong;currentTime = getNanoTimeLongKey()) {
			//
		}
		
		return lastLong = getNanoTimeLongKey();
	}
	
	public static long getNanoTimeLongKey() {
		return System.nanoTime();
	}
	
	public static void main(String[] args) {
		NanoTimeLongKeyGenerator gen = new NanoTimeLongKeyGenerator();
		for(int i=0;i<25;i++) {
			System.out.println(gen.getKey(null));
		}
	}
}