
package org.lexevs.dao.database.ibatis.parameter;

/**
 * The Class PrefixedParameterQuad.
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public class PrefixedParameterSextet extends PrefixedParameterQuint {
	/** The param6. */
	private String param6;
	
	public PrefixedParameterSextet() {
		super();
	}

	public PrefixedParameterSextet(String prefix, String param1, String param2, String param3, String param4,
			String param5, String param6) {
		super(prefix, param1, param2, param3, param4, param5);
		this.setParam6(param6);
	}

	/**
	 * @return the param6
	 */
	public String getParam6() {
		return param6;
	}

	/**
	 * @param param6 the param6 to set
	 */
	public void setParam6(String param6) {
		this.param6 = param6;
	}

}