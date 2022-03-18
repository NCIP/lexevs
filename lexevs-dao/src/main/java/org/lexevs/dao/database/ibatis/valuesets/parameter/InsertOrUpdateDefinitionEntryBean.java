
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import org.LexGrid.valueSets.DefinitionEntry;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class to handle insert or update DefinitionEntry table.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class InsertOrUpdateDefinitionEntryBean extends IdableParameterBean {

	/** The pick list entry node. */
	private DefinitionEntry definitionEntry;
	
	private String valueSetDefUId;
	private String codingSchemeReference;
	private String valueSetDefReference;
	private String entityCode;				
	private String entityCodeNamespace;
	private Boolean leafOnly;
	private String referenceAssociation;
	private Boolean targetToSource;
	private Boolean transitiveClosure;
	private String propertyRefCodingScheme;
	private String propertyName;
	private String propertyMatchValue;
	private String matchAlgorithm;
	private String format;
	
	/**
	 * @return the definitionEntry
	 */
	public DefinitionEntry getDefinitionEntry() {
		return definitionEntry;
	}

	/**
	 * @param definitionEntry the definitionEntry to set
	 */
	public void setDefinitionEntry(DefinitionEntry definitionEntry) {
		this.definitionEntry = definitionEntry;
	}

	/**
	 * @return the valueSetDefGuid
	 */
	public String getValueSetDefUId() {
		return valueSetDefUId;
	}

	/**
	 * @param valueSetDefUId the valueSetDefGuid to set
	 */
	public void setValueSetDefUId(String valueSetDefUId) {
		this.valueSetDefUId = valueSetDefUId;
	}

	/**
	 * @return the codingSchemeReference
	 */
	public String getCodingSchemeReference() {
		return codingSchemeReference;
	}

	/**
	 * @param codingSchemeReference the codingSchemeReference to set
	 */
	public void setCodingSchemeReference(String codingSchemeReference) {
		this.codingSchemeReference = codingSchemeReference;
	}

	/**
	 * @return the valueSetDefReference
	 */
	public String getValueSetDefReference() {
		return valueSetDefReference;
	}

	/**
	 * @param valueSetDefReference the valueSetDefReference to set
	 */
	public void setValueSetDefReference(String valueSetDefReference) {
		this.valueSetDefReference = valueSetDefReference;
	}

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @return the entityCodeNamespace
	 */
	public String getEntityCodeNamespace() {
		return entityCodeNamespace;
	}

	/**
	 * @param entityCodeNamespace the entityCodeNamespace to set
	 */
	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}

	/**
	 * @return the leafOnly
	 */
	public Boolean isLeafOnly() {
		return leafOnly;
	}

	/**
	 * @param leafOnly the leafOnly to set
	 */
	public void setLeafOnly(Boolean leafOnly) {
		this.leafOnly = leafOnly;
	}

	/**
	 * @return the referenceAssociation
	 */
	public String getReferenceAssociation() {
		return referenceAssociation;
	}

	/**
	 * @param referenceAssociation the referenceAssociation to set
	 */
	public void setReferenceAssociation(String referenceAssociation) {
		this.referenceAssociation = referenceAssociation;
	}

	/**
	 * @return the targetToSource
	 */
	public Boolean isTargetToSource() {
		return targetToSource;
	}

	/**
	 * @param targetToSource the targetToSource to set
	 */
	public void setTargetToSource(Boolean targetToSource) {
		this.targetToSource = targetToSource;
	}

	/**
	 * @return the transitiveClosure
	 */
	public Boolean isTransitiveClosure() {
		return transitiveClosure;
	}

	/**
	 * @param transitiveClosure the transitiveClosure to set
	 */
	public void setTransitiveClosure(Boolean transitiveClosure) {
		this.transitiveClosure = transitiveClosure;
	}

	/**
	 * @return the propertyRefCodingScheme
	 */
	public String getPropertyRefCodingScheme() {
		return propertyRefCodingScheme;
	}

	/**
	 * @param propertyRefCodingScheme the propertyRefCodingScheme to set
	 */
	public void setPropertyRefCodingScheme(String propertyRefCodingScheme) {
		this.propertyRefCodingScheme = propertyRefCodingScheme;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the propertyMatchValue
	 */
	public String getPropertyMatchValue() {
		return propertyMatchValue;
	}

	/**
	 * @param propertyMatchValue the propertyMatchValue to set
	 */
	public void setPropertyMatchValue(String propertyMatchValue) {
		this.propertyMatchValue = propertyMatchValue;
	}

	/**
	 * @return the matchAlgorithm
	 */
	public String getMatchAlgorithm() {
		return matchAlgorithm;
	}

	/**
	 * @param matchAlgorithm the matchAlgorithm to set
	 */
	public void setMatchAlgorithm(String matchAlgorithm) {
		this.matchAlgorithm = matchAlgorithm;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

}