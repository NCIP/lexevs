package org.cts2.internal.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.lexevs.logging.LoggerFactory;

@Aspect
public class LoggingAspect {

	@Around("@within(org.cts2.internal.logging.annotation.Loggable) && " +
			"( @annotation(org.cts2.internal.logging.annotation.Loggable) )")
	public Object logMethod(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature sig = (MethodSignature)pjp.getSignature();
		
		String msg  = this.logMethod(sig);
		LoggerFactory.getLogger().info(msg);
		
		return pjp.proceed();	
	}
	
	protected String logMethod(MethodSignature signature){
		return signature.toLongString();
	}
}
