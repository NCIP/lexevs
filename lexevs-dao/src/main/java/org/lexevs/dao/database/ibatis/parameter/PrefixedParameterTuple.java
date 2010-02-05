package org.lexevs.dao.database.ibatis.parameter;

public class PrefixedParameterTuple extends PrefixedParameter{
	
	private String param2;
	
	public PrefixedParameterTuple(){
		super();
	}
	
	public PrefixedParameterTuple(String prefix, String param1, String param2) {
		super(prefix, param1);
		this.param2 = param2;
	}
	
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}

}
