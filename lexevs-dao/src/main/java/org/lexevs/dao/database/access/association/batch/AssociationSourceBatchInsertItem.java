
package org.lexevs.dao.database.access.association.batch;

import org.LexGrid.relations.AssociationSource;

/**
 * The Class AssociationSourceBatchInsertItem.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationSourceBatchInsertItem {

	/** The parent id. */
	private String parentId;
	
	/** The association source. */
	private AssociationSource associationSource;
	
	/**
	 * Instantiates a new association source batch insert item.
	 */
	public AssociationSourceBatchInsertItem(){
		super();
	}
	
	/**
	 * Instantiates a new association source batch insert item.
	 * 
	 * @param parentId the parent id
	 * @param associationSource the association source
	 */
	public AssociationSourceBatchInsertItem(String parentId, AssociationSource associationSource) {
		super();
		this.parentId = parentId;
		this.associationSource = associationSource;
	}

	/**
	 * Gets the parent id.
	 * 
	 * @return the parent id
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * Sets the parent id.
	 * 
	 * @param parentId the new parent id
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * Gets the association source.
	 * 
	 * @return the association source
	 */
	public AssociationSource getAssociationSource() {
		return associationSource;
	}

	/**
	 * Sets the association source.
	 * 
	 * @param associationSource the new association source
	 */
	public void setAssociationSource(AssociationSource associationSource) {
		this.associationSource = associationSource;
	}

}