
package org.lexevs.dao.database.ibatis.codingscheme.parameter;


import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertURIMapBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateURIMapBean extends IdableParameterBean{

	/** The coding scheme id. */
	private String codingSchemeUId;
	
	/** The supported attribute tag. */
	private String supportedAttributeTag;
	
	/** The uri map. */
	private URIMap uriMap;
	
	/** Parent object type */
	private String referenceType;
	
	private String associationNames;
	private String csSuppAttribGuid;
	private String codingSchemeGuid;
	private String id;
	private String uri;
	private String idValue;
	private String rootCode;
	private Boolean isForwardNavigable;
	private Boolean isImported;
	private String equivalentCodingScheme;
	private String assemblyRule;
	private String assnCodingScheme;
	private String assnNamespace;
	private String assnEntityCode;
	private String propertyType;
	
	/**
	 * Sets the supported attribute tag.
	 * 
	 * @param supportedAttributeTag the new supported attribute tag
	 */
	public void setSupportedAttributeTag(String supportedAttributeTag) {
		this.supportedAttributeTag = supportedAttributeTag;
	}

	/**
	 * Gets the supported attribute tag.
	 * 
	 * @return the supported attribute tag
	 */
	public String getSupportedAttributeTag() {
		return supportedAttributeTag;
	}
	
	
	/**
	 * Sets the uri map.
	 * 
	 * @param uriMap the new uri map
	 */
	public void setUriMap(URIMap uriMap) {
		this.uriMap = uriMap;
	}

	/**
	 * Gets the uri map.
	 * 
	 * @return the uri map
	 */
	public URIMap getUriMap() {
		return uriMap;
	}

	/**
	 * Sets the coding scheme id.
	 * 
	 * @param codingSchemeUId the new coding scheme id
	 */
	public void setCodingSchemeUId(String codingSchemeUId) {
		this.codingSchemeUId = codingSchemeUId;
	}

	/**
	 * Gets the coding scheme id.
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeUId() {
		return codingSchemeUId;
	}

	/**
	 * @return the referenceType
	 */
	public String getReferenceType() {
		return referenceType;
	}

	/**
	 * @param referenceType the referenceType to set
	 */
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	/**
	 * @return the associationNames
	 */
	public String getAssociationNames() {
		return associationNames;
	}

	/**
	 * @param associationNames the associationNames to set
	 */
	public void setAssociationNames(String associationNames) {
		this.associationNames = associationNames;
	}

	public String getCsSuppAttribGuid() {
		return csSuppAttribGuid;
	}

	public void setCsSuppAttribGuid(String csSuppAttribGuid) {
		this.csSuppAttribGuid = csSuppAttribGuid;
	}

	public String getCodingSchemeGuid() {
		return codingSchemeGuid;
	}

	public void setCodingSchemeGuid(String codingSchemeGuid) {
		this.codingSchemeGuid = codingSchemeGuid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

	public String getRootCode() {
		return rootCode;
	}

	public void setRootCode(String rootCode) {
		this.rootCode = rootCode;
	}

	public Boolean getIsForwardNavigable() {
		return isForwardNavigable;
	}

	public void setIsForwardNavigable(Boolean boolean1) {
		this.isForwardNavigable = boolean1;
	}

	public Boolean getIsImported() {
		return isImported;
	}

	public void setIsImported(Boolean isImported) {
		this.isImported = isImported;
	}

	public String getEquivalentCodingScheme() {
		return equivalentCodingScheme;
	}

	public void setEquivalentCodingScheme(String equivalentCodingScheme) {
		this.equivalentCodingScheme = equivalentCodingScheme;
	}

	public String getAssemblyRule() {
		return assemblyRule;
	}

	public void setAssemblyRule(String assemblyRule) {
		this.assemblyRule = assemblyRule;
	}

	public String getAssnCodingScheme() {
		return assnCodingScheme;
	}

	public void setAssnCodingScheme(String assnCodingScheme) {
		this.assnCodingScheme = assnCodingScheme;
	}

	public String getAssnNamespace() {
		return assnNamespace;
	}

	public void setAssnNamespace(String assnNamespace) {
		this.assnNamespace = assnNamespace;
	}

	public String getAssnEntityCode() {
		return assnEntityCode;
	}

	public void setAssnEntityCode(String assnEntityCode) {
		this.assnEntityCode = assnEntityCode;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
}