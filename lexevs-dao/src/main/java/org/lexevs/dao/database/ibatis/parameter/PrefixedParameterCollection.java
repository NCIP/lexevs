
package org.lexevs.dao.database.ibatis.parameter;

import java.util.Collection;

/**
 * The Class PrefixedParameterTuple.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedParameterCollection extends PrefixedParameter{
	
	/** The param2. */
	private Collection<String> param2;
	
	/**
	 * Instantiates a new prefixed parameter tuple.
	 */
	public PrefixedParameterCollection(){
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter tuple.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 * @param param2 the param2
	 */
	public PrefixedParameterCollection(String prefix, String param1, Collection<String> param2) {
		super(prefix, param1);
		this.param2 = param2;
	}

	public Collection<String> getParam2() {
		return param2;
	}

	public void setParam2(Collection<String> param2) {
		this.param2 = param2;
	}
}