
package org.lexevs.dao.database.access.association.batch;

import org.LexGrid.relations.AssociationQualification;

/**
 * The Class AssociationqualifierBatchInsertItem.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationQualifierBatchInsertItem {

	/** The parent id. */
	private String parentId;
	
	/** The association qualifier. */
	private AssociationQualification associationQualifier;
	
	/**
	 * Instantiates a new association qualifier batch insert item.
	 */
	public AssociationQualifierBatchInsertItem(){
		super();
	}
	
	/**
	 * Instantiates a new association qualifier batch insert item.
	 * 
	 * @param parentId the parent id
	 * @param associationQualifier the association qualifier
	 */
	public AssociationQualifierBatchInsertItem(String parentId, AssociationQualification associationQualifier) {
		super();
		this.parentId = parentId;
		this.associationQualifier = associationQualifier;
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
	 * Gets the association qualifier.
	 * 
	 * @return the association qualifier
	 */
	public AssociationQualification getAssociationQualifier() {
		return associationQualifier;
	}

	/**
	 * Sets the association qualifier.
	 * 
	 * @param associationQualifier the new association qualifier
	 */
	public void setAssociationQualifier(AssociationQualification associationQualifier) {
		this.associationQualifier = associationQualifier;
	}

}