package org.lexevs.dao.database.ibatis.association.parameter;

import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;

public class GetEntityAssnUidsBean extends PrefixedTableParameterBean {
	
	private String codingSchemeUid;
	private String associationPredicateUid;
	private String entityCode;
	private String entityCodeNamespace;
	private TripleNode tripleNode;
	
	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}
	public String getEntityCodeNamespace() {
		return entityCodeNamespace;
	}
	public String getCodingSchemeUid() {
		return codingSchemeUid;
	}
	public void setCodingSchemeUid(String codingSchemeUid) {
		this.codingSchemeUid = codingSchemeUid;
	}
	public String getAssociationPredicateUid() {
		return associationPredicateUid;
	}
	public void setAssociationPredicateUid(String associationPredicateUid) {
		this.associationPredicateUid = associationPredicateUid;
	}
	public void setTripleNode(TripleNode tripleNode) {
		this.tripleNode = tripleNode;
	}
	public TripleNode getTripleNode() {
		return tripleNode;
	}
}
