
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class PrefixedParameterTuple.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedParameterTuple extends PrefixedParameter{
	
	/** The param2. */
	private String param2;
	
	/**
	 * Instantiates a new prefixed parameter tuple.
	 */
	public PrefixedParameterTuple(){
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter tuple.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 * @param param2 the param2
	 */
	public PrefixedParameterTuple(String prefix, String param1, String param2) {
		super(prefix, param1);
		this.param2 = param2;
	}
	
	/**
	 * Gets the param2.
	 * 
	 * @return the param2
	 */
	public String getParam2() {
		return param2;
	}
	
	/**
	 * Sets the param2.
	 * 
	 * @param param2 the new param2
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

}