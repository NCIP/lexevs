package org.lexevs.dao.database.access.association.batch;

import org.LexGrid.relations.AssociationSource;

public class AssociationSourceBatchInsertItem {

	private String parentId;
	private AssociationSource associationSource;
	
	public AssociationSourceBatchInsertItem(){
		super();
	}
	
	public AssociationSourceBatchInsertItem(String parentId, AssociationSource associationSource) {
		super();
		this.parentId = parentId;
		this.associationSource = associationSource;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public AssociationSource getAssociationSource() {
		return associationSource;
	}

	public void setAssociationSource(AssociationSource associationSource) {
		this.associationSource = associationSource;
	}

}
