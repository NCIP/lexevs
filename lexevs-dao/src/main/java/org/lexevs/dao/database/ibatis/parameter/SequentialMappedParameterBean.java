package org.lexevs.dao.database.ibatis.parameter;

import java.util.HashMap;

import org.junit.Assert;

public class SequentialMappedParameterBean extends HashMap<String,Object>{

	private static String PARAMETER_PREFIX = "param";

	private static final long serialVersionUID = 4510691698169582467L;

	public SequentialMappedParameterBean(Object... parameters) {
		Assert.assertNotNull(parameters);

		int currentIndex = 1;
		
		for(Object param : parameters) {
			this.put(PARAMETER_PREFIX + Integer.toString(currentIndex++), param);
		}
	}
}
