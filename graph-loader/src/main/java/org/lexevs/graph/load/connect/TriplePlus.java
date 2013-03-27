package org.lexevs.graph.load.connect;

import org.lexevs.dao.database.access.association.model.Triple;

public class TriplePlus extends Triple {

	String sourceSchemeUri;
	String sourceSchemeVersion;
	String targetSchemeUri;
	String targetSchemeVersion;
	String associationName;
	
	public String getSourceSchemeUri() {
		return sourceSchemeUri;
	}
	public void setSourceSchemeUri(String sourceSchemeUri) {
		this.sourceSchemeUri = sourceSchemeUri;
	}
	public String getSourceSchemeVersion() {
		return sourceSchemeVersion;
	}
	public void setSourceSchemeVersion(String sourceSchemeVersion) {
		this.sourceSchemeVersion = sourceSchemeVersion;
	}
	public String getTargetSchemeUri() {
		return targetSchemeUri;
	}
	public void setTargetSchemeUri(String targetSchemeUri) {
		this.targetSchemeUri = targetSchemeUri;
	}
	public String getTargetSchemeVersion() {
		return targetSchemeVersion;
	}
	public void setTargetSchemeVersion(String targetSchemeVersion) {
		this.targetSchemeVersion = targetSchemeVersion;
	}
	public String getAssociationName() {
		return associationName;
	}
	public void setAssociaitonName(String associaitonName) {
		this.associationName = associaitonName;
	}
	
	

}
