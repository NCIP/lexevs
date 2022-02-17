
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
	
	public static void destroy(){
		SESSION_STATE.remove();
	}
}