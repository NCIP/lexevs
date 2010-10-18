/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;

public class GetEntityAssnUidsCountBean extends PrefixedTableParameterBean {
	
	private String codingSchemeUid;
	private String relationsContainerName;
	private String entityCode;
	private String entityCodeNamespace;
	private List<QualifierNameValuePair> associationQualifiers;
	private List<String> associations;
	private List<CodeNamespacePair> mustHaveCodes;
	private List<String> mustHaveNamespaces;
	private List<String> mustHaveEntityTypes;
	private Boolean restrictToAnonymous;
	private TripleNode tripleNode;

	public List<String> getMustHaveEntityTypes() {
		return mustHaveEntityTypes;
	}
	public void setMustHaveEntityTypes(List<String> mustHaveEntityTypes) {
		this.mustHaveEntityTypes = mustHaveEntityTypes;
	}
	public Boolean isRestrictToAnonymous() {
		return restrictToAnonymous;
	}
	public void setRestrictToAnonymous(Boolean restrictToAnonymous) {
		this.restrictToAnonymous = restrictToAnonymous;
	}
	
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
	public void setTripleNode(TripleNode tripleNode) {
		this.tripleNode = tripleNode;
	}
	public TripleNode getTripleNode() {
		return tripleNode;
	}
	public void setAssociationQualifiers(List<QualifierNameValuePair> associationQualifiers) {
		this.associationQualifiers = associationQualifiers;
	}
	public List<QualifierNameValuePair> getAssociationQualifiers() {
		return associationQualifiers;
	}
	public void setMustHaveCodes(List<CodeNamespacePair> mustHaveCodes) {
		this.mustHaveCodes = mustHaveCodes;
	}
	public List<CodeNamespacePair> getMustHaveCodes() {
		return mustHaveCodes;
	}
	public void setAssociations(List<String> associations) {
		this.associations = associations;
	}
	public List<String> getAssociations() {
		return associations;
	}
	public void setMustHaveNamespaces(List<String> mustHaveNamespaces) {
		this.mustHaveNamespaces = mustHaveNamespaces;
	}
	public List<String> getMustHaveNamespaces() {
		return mustHaveNamespaces;
	}
	public void setRelationsContainerName(String relationsContainerName) {
		this.relationsContainerName = relationsContainerName;
	}
	public String getRelationsContainerName() {
		return relationsContainerName;
	}

	public boolean isNeedsEntityJoin() {
		return CollectionUtils.isNotEmpty(this.mustHaveEntityTypes) || (this.restrictToAnonymous != null);
	}
}