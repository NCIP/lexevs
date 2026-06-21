
package org.lexevs.dao.database.ibatis.property.parameter;

import java.util.Date;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertPropertyBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdatePropertyBean extends IdableParameterBean {

	/** The property. */
	private Property property;
	
	/** The entity id. */
	private String parentUId;
	
	/** The reference type. */
	private String parentType;
	
	private Boolean isPreferred;
	
	private Boolean matchIfNoContext;
	
	private String degreeOfFidelity;
	
	private String representationalForm;
	
	private List<InsertPropertyMultiAttribBean> propertyMultiAttribList = null;
	
	private String propertyGuid;
	private String referenceGuid;
	private String referenceType;
	private String propertyId;
	private String propertyType;
	private String propertyName;
	private String language;
	private String format;
    private String propertyValue;
    private Boolean isActive;
    private String owner;
    private String status;
    private Date effectiveDate;
    private Date expirationDate;
    private String entryStateGuid;
	

	public String getPropertyGuid() {
		return propertyGuid;
	}

	public void setPropertyGuid(String propertyGuid) {
		this.propertyGuid = propertyGuid;
	}

	public String getReferenceGuid() {
		return referenceGuid;
	}

	public void setReferenceGuid(String referenceGuid) {
		this.referenceGuid = referenceGuid;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean boolean1) {
		this.isActive = boolean1;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date date) {
		this.effectiveDate = date;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date date) {
		this.expirationDate = date;
	}

	public String getEntryStateGuid() {
		return entryStateGuid;
	}

	public void setEntryStateGuid(String entryStateGuid) {
		this.entryStateGuid = entryStateGuid;
	}

	/**
	 * Gets the property.
	 * 
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}
	
	/**
	 * Sets the property.
	 * 
	 * @param property the new property
	 */
	public void setProperty(Property property) {
		this.property = property;
	}
	
	/**
	 * Gets the entity id.
	 * 
	 * @return the entity id
	 */
	public String getParentUId() {
		return parentUId;
	}
	
	/**
	 * Sets the parent id.
	 * 
	 * @param parentUId the new entity id
	 */
	public void setParentUId(String parentUId) {
		this.parentUId = parentUId;
	}
	
	/**
	 * Sets the parent type.
	 * 
	 * @param parentType the new reference type
	 */
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	
	/**
	 * Gets the reference type.
	 * 
	 * @return the reference type
	 */
	public String getParentType() {
		return parentType;
	}

	/**
	 * @return the isPreferred
	 */
	public Boolean getIsPreferred() {
		if(this.property != null && property instanceof Presentation) {
			return ((Presentation)property).getIsPreferred();
		} else if(this.property != null && property instanceof Definition) {
			return ((Definition)property).getIsPreferred();
		} else {
			return null;
		}
	}
	
	public void setIsPreferred(Boolean isPreferred) {
		this.isPreferred = isPreferred;
	}
	

	/**
	 * @return the representationalForm
	 */
	public String getDegreeOfFidelity() {
		if(this.property != null && property instanceof Presentation) {
			return ((Presentation)property).getDegreeOfFidelity();
		} else {
			return null;
		}
	}
	
	public void setDegreeOfFidelity(String degreeOfFidelity) {
		this.degreeOfFidelity = degreeOfFidelity;
	}

	/**
	 * @return the representationalForm
	 */
	public String getRepresentationalForm() {
		if(this.property != null && property instanceof Presentation) {
			return ((Presentation)property).getRepresentationalForm();
		} else {
			return null;
		}
	}
	
	public void setRepresentationalForm(String representationalForm) {
		this.representationalForm = representationalForm;
	}

	/**
	 * @return the matchIfNoContext
	 */
	public Boolean getMatchIfNoContext() {
		if(this.property != null && property instanceof Presentation) {
			return ((Presentation)property).getMatchIfNoContext();
		} else {
			return null;
		}
	}
	

	public void setMatchIfNoContext(Boolean matchIfNoContext) {
		this.matchIfNoContext = matchIfNoContext;
	}

	/**
	 * @return the propertyMultiAttribList
	 */
	public List<InsertPropertyMultiAttribBean> getPropertyMultiAttribList() {
		return propertyMultiAttribList;
	}

	/**
	 * @param propertyMultiAttribList the propertyMultiAttribList to set
	 */
	public void setPropertyMultiAttribList(
			List<InsertPropertyMultiAttribBean> propertyMultiAttribList) {
		this.propertyMultiAttribList = propertyMultiAttribList;
	}
}