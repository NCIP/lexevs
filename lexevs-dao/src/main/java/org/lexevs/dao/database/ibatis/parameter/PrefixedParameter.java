
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class PrefixedParameter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedParameter extends PrefixedTableParameterBean{

	/** The param1. */
	private String param1;
	
	/**
	 * Instantiates a new prefixed parameter.
	 */
	public PrefixedParameter(){
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 */
	public PrefixedParameter(String prefix, String param1) {
		super(prefix);
		this.param1 = param1;
	}
	
	/**
	 * Gets the param1.
	 * 
	 * @return the param1
	 */
	public String getParam1() {
		return param1;
	}
	
	/**
	 * Sets the param1.
	 * 
	 * @param param1 the new param1
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
}