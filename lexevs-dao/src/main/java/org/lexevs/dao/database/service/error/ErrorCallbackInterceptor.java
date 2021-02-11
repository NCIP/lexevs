
package org.lexevs.dao.database.service.error;

import org.aopalliance.intercept.MethodInvocation;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * The Class LazyLoadingCodeToReturnInterceptor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ErrorCallbackInterceptor extends TransactionInterceptor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8816574427519953487L;

    /** The callback. */
    private ErrorCallbackListener errorCallbackListener;
    
    /** The UNKNOWN_ERROR_CODE Constant. */
    private static String UNKNOWN_ERROR_CODE = DatabaseError.UNKNOWN_ERROR_CODE;
    
    /**
     * Instantiates a new dao validating interceptor.
     * 
     * @param errorCallbackListener the error callback listener
     */
    public ErrorCallbackInterceptor(ErrorCallbackListener errorCallbackListener){
        this.errorCallbackListener = errorCallbackListener;
        this.setTransactionManager(
        		LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().getTransactionManager());
       
        this.setTransactionAttributeSource( new AnnotationTransactionAttributeSource());
        this.afterPropertiesSet();
    }
    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
   
    	DatabaseErrorIdentifier errorId = 
    		AnnotationUtils.findAnnotation(methodInvocation.getMethod(), DatabaseErrorIdentifier.class);

    	final String errorCode;
    	if(errorId == null) {
    		errorCode = UNKNOWN_ERROR_CODE;
    	} else {
    		errorCode = errorId.errorCode();
    	}

    	try {
    		return super.invoke(methodInvocation);
    	} catch (Exception e) {
    		errorCallbackListener.onDatabaseError(new DefaultDatabaseError(errorCode, methodInvocation.getArguments(), e)); 
    		return null;
    	} 
    }	
}