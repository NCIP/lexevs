
package org.lexgrid.loader.processor;

import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class AbstractSupportedAttributeRegisteringProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSupportedAttributeRegisteringProcessor<I,O> extends CodingSchemeIdAwareProcessor implements ItemProcessor<I,O>{

	/** The supported attribute template. */
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	/** The register supported attributes. */
	private boolean registerSupportedAttributes = true;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public final O process(I item) throws Exception {
		O output = this.doProcess(item);

		if(registerSupportedAttributes && output != null) {
			this.registerSupportedAttributes(this.getSupportedAttributeTemplate(),
					output);
		}
		return output;
	}
	
	/**
	 * Do process.
	 * 
	 * @param item the item
	 * 
	 * @return the o
	 * 
	 * @throws Exception the exception
	 */
	public abstract O doProcess(I item) throws Exception;
		
	/**
	 * Register supported attributes.
	 * 
	 * @param s the s
	 * @param item the item
	 */
	protected abstract void registerSupportedAttributes(SupportedAttributeTemplate s, O item);
	
	/**
	 * Gets the supported attribute template.
	 * 
	 * @return the supported attribute template
	 */
	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}

	/**
	 * Sets the supported attribute template.
	 * 
	 * @param supportedAttributeTemplate the new supported attribute template
	 */
	public void setSupportedAttributeTemplate(
			SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}

	/**
	 * Checks if is register supported attributes.
	 * 
	 * @return true, if is register supported attributes
	 */
	public boolean isRegisterSupportedAttributes() {
		return registerSupportedAttributes;
	}

	/**
	 * Sets the register supported attributes.
	 * 
	 * @param registerSupportedAttributes the new register supported attributes
	 */
	public void setRegisterSupportedAttributes(boolean registerSupportedAttributes) {
		this.registerSupportedAttributes = registerSupportedAttributes;
	}
}