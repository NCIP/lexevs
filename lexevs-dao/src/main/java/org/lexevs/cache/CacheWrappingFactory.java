
package org.lexevs.cache;

import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

public class CacheWrappingFactory {

	private MethodCachingInterceptor interceptor;
	
	@SuppressWarnings("unchecked")
	public <T> T wrapForCaching(T target) {
		ProxyFactory pf = new ProxyFactory(target);

		pf.setProxyTargetClass(true);
		
		AnnotationMatchingPointcut pointcutCache = 
			new AnnotationMatchingPointcut(Cacheable.class, CacheMethod.class);
		
		AnnotationMatchingPointcut pointcutClear = 
			new AnnotationMatchingPointcut(Cacheable.class, ClearCache.class);
		
		pf.addAdvisor(
				new DefaultPointcutAdvisor(
						pointcutCache, interceptor));
		
		pf.addAdvisor(
				new DefaultPointcutAdvisor(
						pointcutClear, interceptor));

		return (T) pf.getProxy();
	}

	public MethodCachingInterceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(MethodCachingInterceptor interceptor) {
		this.interceptor = interceptor;
	}
}