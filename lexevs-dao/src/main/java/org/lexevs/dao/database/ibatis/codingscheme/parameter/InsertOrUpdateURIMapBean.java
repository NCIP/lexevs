
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
}