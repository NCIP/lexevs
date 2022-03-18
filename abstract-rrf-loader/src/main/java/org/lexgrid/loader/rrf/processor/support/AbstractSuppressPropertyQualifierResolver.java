
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class AbstractSuppressPropertyQualifierResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSuppressPropertyQualifierResolver<T> extends AbstractPropertyQualifierResolver<T>{
	
	private String YES = "Y";

	public String getQualifierName(T item) {
		return RrfLoaderConstants.SUPPRESS_QUALIFIER;
	}

	@Override
	protected boolean isProcessableValue(String value) {
		return value.equals(YES);
	}
}