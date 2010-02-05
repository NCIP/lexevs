package org.lexevs.dao.database.ibatis.parameter;

public class PrefixedParameter extends PrefixedTableParameterBean{

	private String param1;
	
	public PrefixedParameter(){
		super();
	}
	
	public PrefixedParameter(String prefix, String param1) {
		super(prefix);
		this.param1 = param1;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
}
