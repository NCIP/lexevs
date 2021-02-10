
package org.lexevs.dao.database.ibatis.parameter;

import java.util.HashMap;

import org.springframework.util.Assert;

public class SequentialMappedParameterBean extends HashMap<String,Object>{

	private static String PARAMETER_PREFIX = "param";
	private static String PREFIX_PARAMETER = "prefix";
	private String ACTUAL_PREFIX_PARAMETER = "actualTableSetPrefix";

	private static final long serialVersionUID = 4510691698169582467L;

	public SequentialMappedParameterBean(Object... parameters) {
		Assert.notNull(parameters);

		int currentIndex = 1;
		
		for(Object param : parameters) {
			this.put(PARAMETER_PREFIX + Integer.toString(currentIndex++), param);
		}
	}

	public void setPrefix(String prefix) {
		this.put(PREFIX_PARAMETER, prefix);
	}
	
	public void setActualTableSetPrefix(String prefix) {
		this.put(ACTUAL_PREFIX_PARAMETER, prefix);
	}
}