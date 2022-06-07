
package org.lexevs.dao.database.access.association.model;

public class Triple {

	private String sourceEntityCode;
	private String sourceEntityNamespace;
	private String targetEntityCode;
	private String targetEntityNamespace;
	private String associationPredicateId;
	
	public String getSourceEntityCode() {
		return sourceEntityCode;
	}
	public void setSourceEntityCode(String sourceEntityCode) {
		this.sourceEntityCode = sourceEntityCode;
	}
	public String getSourceEntityNamespace() {
		return sourceEntityNamespace;
	}
	public void setSourceEntityNamespace(String sourceEntityNamespace) {
		this.sourceEntityNamespace = sourceEntityNamespace;
	}
	public String getTargetEntityCode() {
		return targetEntityCode;
	}
	public void setTargetEntityCode(String targetEntityCode) {
		this.targetEntityCode = targetEntityCode;
	}
	public String getTargetEntityNamespace() {
		return targetEntityNamespace;
	}
	public void setTargetEntityNamespace(String targetEntityNamespace) {
		this.targetEntityNamespace = targetEntityNamespace;
	}
	public String getAssociationPredicateId() {
		return associationPredicateId;
	}
	public void setAssociationPredicateId(String associationPredicateId) {
		this.associationPredicateId = associationPredicateId;
	}

	@Override
	public boolean equals(Object o){
		if( o== this){
			return true;
		}

		if(!(o instanceof Triple)){
			return false;
		}

		Triple triple = (Triple) o;

		if(!triple.getSourceEntityCode().equals(getSourceEntityCode())){
			return false;
		}

		if(!triple.getTargetEntityCode().equals(getTargetEntityCode())){
			return false;
		}

		if(!triple.getSourceEntityNamespace().equals(getSourceEntityNamespace())){
			return false;
		}

		if(!triple.getTargetEntityNamespace().equals(this.getTargetEntityNamespace())){
			return false;
		}

		if(!triple.getAssociationPredicateId().equals(this.getAssociationPredicateId())){
			return false;
		}

		return true;
	}

	public int hashCode(){
		int hash=7;
		hash = 31*hash+(null == sourceEntityCode?0:sourceEntityCode.hashCode());
		hash = 31*hash+(null == sourceEntityCode?0:sourceEntityNamespace.hashCode());
		hash =31*hash+(null == sourceEntityCode?0:targetEntityCode.hashCode());
		hash =31*hash+(null == sourceEntityCode?0:targetEntityNamespace.hashCode());
		hash =31*hash+(null == sourceEntityCode?0:associationPredicateId.hashCode());

		return hash;
	}
}