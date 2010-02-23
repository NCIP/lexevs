package org.lexevs.dao.database.ibatis.parameter;

public class PrefixedParameterTriple extends PrefixedParameterTuple{
	
	private String param3;
	
	public PrefixedParameterTriple() {
		super();
	}
	
	public PrefixedParameterTriple(String prefix, String param1, String param2, String param3) {
		super(prefix, param1, param2);
		this.param3 = param3;
	}
	
	public String getParam3() {
		return param3;
	}
	
	public void setParam3(String param3) {
		this.param3 = param3;
	}
}
