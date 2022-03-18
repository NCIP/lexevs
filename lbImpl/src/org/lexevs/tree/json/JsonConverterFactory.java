
package org.lexevs.tree.json;

import org.lexevs.tree.service.ApplicationContextFactory;

/**
 * A factory for creating JsonConverter objects.
 */
@Deprecated
public class JsonConverterFactory {
	
	/**
	 * Gets the json converter.
	 * 
	 * @return the json converter
	 */
	public static JsonConverter getJsonConverter(){
		return (JsonConverter)ApplicationContextFactory.getInstance().getApplicationContext().getBean("childPagingJsonConverter");
	}
}