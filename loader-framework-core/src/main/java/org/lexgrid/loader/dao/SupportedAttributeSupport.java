
package org.lexgrid.loader.dao;

import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;

/**
 * The Class SupportedAttributeSupport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SupportedAttributeSupport {

	/** The supported attribute template. */
	private SupportedAttributeTemplate supportedAttributeTemplate;

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
}