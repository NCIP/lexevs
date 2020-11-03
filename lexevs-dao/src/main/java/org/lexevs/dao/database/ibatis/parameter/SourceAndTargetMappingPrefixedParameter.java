package org.lexevs.dao.database.ibatis.parameter;

public class SourceAndTargetMappingPrefixedParameter extends PrefixedParameter {
	
	String sourcePrefix;
	String targetPrefix;
	
	public SourceAndTargetMappingPrefixedParameter(
			String prefix, 
			String sourcePrefix, 
			String targetPrefix, 
			String param1) {
		super(prefix, param1);
		this.sourcePrefix = sourcePrefix;
		this.targetPrefix = targetPrefix;
	}

	
	public String getSourcePrefix() {
		return sourcePrefix;
	}
	public void setSourcePrefix(String sourcePrefix) {
		this.sourcePrefix = sourcePrefix;
	}
	public String getTargetPrefix() {
		return targetPrefix;
	}
	public void setTargetPrefix(String targetPrefix) {
		this.targetPrefix = targetPrefix;
	}

}
