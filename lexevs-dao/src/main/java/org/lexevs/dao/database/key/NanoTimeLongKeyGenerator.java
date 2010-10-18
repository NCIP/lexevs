/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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