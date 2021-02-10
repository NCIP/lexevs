
package org.lexevs.dao.database.ibatis.property.parameter;

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