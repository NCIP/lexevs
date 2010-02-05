package org.lexevs.dao.database.ibatis.parameter;

public class PrefixedParameter {
	
	private String prefix;
	private String param1;
	
	public PrefixedParameter(){
		super();
	}
	
	public PrefixedParameter(String prefix, String param1) {
		super();
		this.prefix = prefix;
		this.param1 = param1;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getPrefix() {
		return prefix;
	}
}
