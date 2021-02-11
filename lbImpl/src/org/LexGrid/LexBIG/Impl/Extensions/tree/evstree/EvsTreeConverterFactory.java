
package org.LexGrid.LexBIG.Impl.Extensions.tree.evstree;

import org.LexGrid.LexBIG.Impl.Extensions.tree.service.ApplicationContextFactory;

/**
 * A factory for creating JsonConverter objects.
 */
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