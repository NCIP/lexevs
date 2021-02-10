
package org.lexevs.dao.database.service.error;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * A factory for creating ErrorCallbackDatabaseService objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ErrorCallbackDatabaseServiceFactory {

	/**
	 * Gets the error callback database service.
	 * 
	 * @param databaseService the database service
	 * @param callback the callback
	 * 
	 * @return the error callback database service
	 */
	@SuppressWarnings("unchecked")
	public <T> T getErrorCallbackDatabaseService(T databaseService, ErrorCallbackListener callback) {
		ErrorHandlingService serviceAnnotation = AnnotationUtils.findAnnotation(databaseService.getClass(), ErrorHandlingService.class);
		
		if(serviceAnnotation == null) {
			throw new RuntimeException("Class: " + databaseService.getClass().getName() + " is not an Error Handling Service.");
		}
		
		ProxyFactory pf = new ProxyFactory(databaseService);

		pf.setProxyTargetClass(true);
		ErrorCallbackInterceptor interceptor = new ErrorCallbackInterceptor(callback);

		if(serviceAnnotation.matchAllMethods()){
			pf.addAdvice(interceptor);
		} else {
			AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, DatabaseErrorIdentifier.class);
			Advisor advisor = new DefaultPointcutAdvisor(
					pointcut, interceptor);
			pf.addAdvisor(advisor);

			Class<?>[] annotationClasses = serviceAnnotation.matchAnnotatedMethods();

			if(!ArrayUtils.isEmpty(annotationClasses)){
				for(Class clazz : annotationClasses) {
					AnnotationMatchingPointcut methodAnnotationPointcuts = 
						new AnnotationMatchingPointcut(ErrorHandlingService.class, clazz);
					pf.addAdvisor(
							new DefaultPointcutAdvisor(
									methodAnnotationPointcuts, interceptor));
				}
			}
		}
		
		Object obj = pf.getProxy();
		return (T)obj;
	}
}