
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class PrefixedParameterTriple.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedParameterTriple extends PrefixedParameterTuple{
	
	/** The param3. */
	private String param3;
	
	/**
	 * Instantiates a new prefixed parameter triple.
	 */
	public PrefixedParameterTriple() {
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter triple.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 * @param param2 the param2
	 * @param param3 the param3
	 */
	public PrefixedParameterTriple(String prefix, String param1, String param2, String param3) {
		super(prefix, param1, param2);
		this.param3 = param3;
	}
	
	/**
	 * Gets the param3.
	 * 
	 * @return the param3
	 */
	public String getParam3() {
		return param3;
	}
	
	/**
	 * Sets the param3.
	 * 
	 * @param param3 the new param3
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}
}