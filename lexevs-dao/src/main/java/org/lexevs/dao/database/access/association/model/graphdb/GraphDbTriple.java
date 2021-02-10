
package org.lexevs.dao.database.access.association.model.graphdb;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.lexevs.dao.database.access.association.model.Triple;

public class GraphDbTriple extends Triple {
	
	public String getEntityAssnsGuid() {
		return entityAssnsGuid;
	}
	public void setEntityAssnsGuid(String entityAssnsGuid) {
		this.entityAssnsGuid = entityAssnsGuid;
	}
	public String getAssociationInstanceId() {
		return associationInstanceId;
	}
	public void setAssociationInstanceId(String associationInstanceId) {
		this.associationInstanceId = associationInstanceId;
	}
	public String getQualifierName() {
		return qualifierName;
	}
	public void setQualifierName(String qualifierName) {
		this.qualifierName = qualifierName;
	}
	public String getQualifierValue() {
		return qualifierValue;
	}
	public void setQualifierValue(String qualifierValue) {
		this.qualifierValue = qualifierValue;
	}
	
	public String getAssociationName() {
		return associationName;
	}
	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}
	public List<?> getAssociationQualification() {
		return associationQualification;
	}
	public void setAssociationQualification(List<?> associationQualification) {
		this.associationQualification = associationQualification;
	}
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
	
	public boolean getAnonymousStatus() {
		return anonymousStatus;
	}
	public void setAnonymousStatus(boolean b) {
		this.anonymousStatus = b;
	}

	public String getSourceDescription() {
		return sourceDescription;
	}
	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	public String getTargetDescription() {
		return targetDescription;
	}
	public void setTargetDescription(String targetDescription) {
		this.targetDescription = targetDescription;
	}

	private List<?> associationQualification;
	private String entityAssnsGuid;
	private String associationInstanceId;
	private String qualifierName;
	private String qualifierValue;
	private String associationName;
	private String sourceSchemeUri;
	private String sourceSchemeVersion;
	private String targetSchemeUri;
	private String targetSchemeVersion;
	private String sourceDescription;
	private String targetDescription;
	private boolean anonymousStatus;


}