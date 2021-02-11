
package org.lexevs.tree.evstree;

import org.lexevs.tree.service.ApplicationContextFactory;

/**
 * A factory for creating JsonConverter objects.
 */
@Deprecated
public class EvsTreeConverterFactory {
	
	/**
	 * Gets the json converter.
	 * 
	 * @return the json converter
	 */
	public static EvsTreeConverter getEvsTreeConverter(){
		return (EvsTreeConverter)ApplicationContextFactory.getInstance().getApplicationContext().getBean("childPagingEvsTreeConverter");
	}
}