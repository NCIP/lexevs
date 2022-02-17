
package org.lexgrid.valuesets.dto;

import java.io.Serializable;

/**
 * Bean for resolved pick list entries.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ResolvedPickListEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6594033127042171912L;
	private String pickText;
	private String entityCode;
	private String entityCodeNamespace;
	private String propertyId;
	private boolean isDefault;
	private Integer entryOrder;

	/**
	 * @return the pickText
	 */
	public String getPickText() {
		return pickText;
	}

	/**
	 * @param pickText
	 *            the pickText to set
	 */
	public void setPickText(String pickText) {
		this.pickText = pickText;
	}

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode
	 *            the entityCode to set
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
	 * @param entityCodeNamespace
	 *            the entityCodeNamespace to set
	 */
	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}

	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId
	 *            the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	/**
	 * @return the entryOrder
	 */
	public Integer getEntryOrder() {
		return this.entryOrder;
	}
	
	/**
	 * @param entryOrder
	 *            the entryOrder to set
	 */
	public void setEntryOrder(Integer entryOrder) {
		this.entryOrder = entryOrder;
	}
}