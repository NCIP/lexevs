
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class PrefixedParameterQuad.
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public class PrefixedParameterQuint extends PrefixedParameterQuad {
	
	/** The param5. */
	private String param5;

	public PrefixedParameterQuint() {
		super();
	}

	public PrefixedParameterQuint(String prefix, String param1, String param2, 
			String param3, String param4, String param5) {
		super(prefix, param1, param2, param3, param4);
		this.param5 = param5;
	}

	/**
	 * @return the param5
	 */
	public String getParam5() {
		return param5;
	}

	/**
	 * @param param5 the param5 to set
	 */
	public void setParam5(String param5) {
		this.param5 = param5;
	}

}