package org.LexGrid.custom.relations;

public class TerminologyMapBean {
	
	
	static final String SOURCE = "Source";
	static final String	SOURCE_CODE = "Source Code";
	static final String	SOURCE_NAME = "Source Name";
	static final String	REL = "REL";
	static final String	MAP_RANK = "Map Rank";
	static final String	TARGET = "Target";
	static final String	TARGET_CODE = "Target Code";
	static final String TARGET_NAME = "Target Name";
	
	String source;
	String sourceCode;
	String sourceName;
	String rel;
	String mapRank;
	String target;
	String targetCode;
	String targetName;

	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String reL) {
		this.rel = reL;
	}
	public String getMapRank() {
		return mapRank;
	}
	public void setMapRank(String mapRank) {
		this.mapRank = mapRank;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	
	public String toString(){
		String string = getSource() == null ?" ":getSource() + "," +
						getSourceCode() == null ?" ":getSourceCode() + "," +
						getSourceName() == null ?" ":getSourceName() + "," +
						getRel() == null ?" ":getRel() + "," +
						getMapRank() == null ?" ":getMapRank() + "," +	
						getTarget() == null ?" ":getTarget() + "," +
						getTargetCode() == null ?" ":getTargetCode() + "," +
						getTargetName() == null ?" ":getTargetName();								
		return string;
	}
}
