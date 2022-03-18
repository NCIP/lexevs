
package org.lexevs.cache;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * The Class MethodCachingProxy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Aspect
public class MethodCachingProxy extends AbstractMethodCachingBean<ProceedingJoinPoint> {
	
	@Around("@within(org.lexevs.cache.annotation.Cacheable) && " +
	"( @annotation(org.lexevs.cache.annotation.CacheMethod) || @annotation(org.lexevs.cache.annotation.ClearCache) )")
	public Object cacheMethod(ProceedingJoinPoint pjp) throws Throwable {
		return super.doCacheMethod(pjp);
	}

	@Override
	protected Method getMethod(ProceedingJoinPoint joinPoint) {
		MethodSignature sig = (MethodSignature)joinPoint.getSignature();
		
		return sig.getMethod();
	}

	@Override
	protected Object[] getArguments(ProceedingJoinPoint joinPoint) {
		return joinPoint.getArgs();
	}

	@Override
	protected Object getTarget(ProceedingJoinPoint joinPoint) {
		return joinPoint.getTarget();
	}

	@Override
	protected Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
		return joinPoint.proceed();
	}
}