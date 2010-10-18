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
package org.lexevs.cache;

public class CacheSessionManager {
	
	private static class CacheSessionThreadLocal extends ThreadLocal<Boolean> {
		@Override
		protected Boolean initialValue() {
			return true;
		}
	}
	
	private static CacheSessionThreadLocal SESSION_STATE = new CacheSessionThreadLocal();
	
	public static void turnOffCaching() {
		SESSION_STATE.set(false);
	}
	
	public static void turnOnCaching() {
		SESSION_STATE.set(true);
	}
	
	public static boolean getCachingStatus() {
		return SESSION_STATE.get();
	}
}