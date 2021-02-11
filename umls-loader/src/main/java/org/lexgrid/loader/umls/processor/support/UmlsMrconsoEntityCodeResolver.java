
package org.lexgrid.loader.umls.processor.support;

import org.lexgrid.loader.processor.support.EntityCodeResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.data.entity.DefaultMrconsoNoCodeHandler;
import org.lexgrid.loader.rrf.data.entity.NoCodeHandler;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class UmlsMrconsoEntityCodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrconsoEntityCodeResolver implements EntityCodeResolver<Mrconso>{

	/** The no code handler. */
	private NoCodeHandler<Mrconso> noCodeHandler = new DefaultMrconsoNoCodeHandler();
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityCodeResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(Mrconso item){
		if(item.getCode().equals(RrfLoaderConstants.NO_CODE)){
			return noCodeHandler.handleNoCode(item);
		} else {
			return item.getCode();
		}
	}

	/**
	 * Gets the no code handler.
	 * 
	 * @return the no code handler
	 */
	public NoCodeHandler<Mrconso> getNoCodeHandler() {
		return noCodeHandler;
	}

	/**
	 * Sets the no code handler.
	 * 
	 * @param noCodeHandler the new no code handler
	 */
	public void setNoCodeHandler(NoCodeHandler<Mrconso> noCodeHandler) {
		this.noCodeHandler = noCodeHandler;
	}
}