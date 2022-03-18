
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class PrefixedParameterQuad.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class PrefixedParameterQuad extends PrefixedParameterTriple{
	
	/** The param3. */
	private String param4;
	
	/**
	 * Instantiates a new prefixed parameter quad.
	 */
	public PrefixedParameterQuad() {
		super();
	}
	
	/**
	 * Instantiates a new prefixed parameter quad.
	 * 
	 * @param prefix the prefix
	 * @param param1 the param1
	 * @param param2 the param2
	 * @param param3 the param3
	 * @param param4 the param4
	 */
	public PrefixedParameterQuad(String prefix, String param1, String param2, String param3, String param4) {
		super(prefix, param1, param2, param3);
		this.param4 = param4;
	}
	
	/**
	 * Gets the param4.
	 * 
	 * @return the param4
	 */
	public String getParam4() {
		return param4;
	}
	
	/**
	 * Sets the param4.
	 * 
	 * @param param3 the new param4
	 */
	public void setParam4(String param4) {
		this.param4 = param4;
	}
}