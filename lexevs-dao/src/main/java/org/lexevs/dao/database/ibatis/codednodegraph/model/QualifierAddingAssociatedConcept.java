package org.lexevs.dao.database.ibatis.codednodegraph.model;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.proxy.CastorProxy;

public class QualifierAddingAssociatedConcept extends AssociatedConcept implements CastorProxy {

	private static final long serialVersionUID = 4411803013641184558L;
	
	private String guid;
	
	private List<?> associationQualification;
	
	public void setAssociationQualification(NameAndValue qualification) {
		if(qualification != null) {
			if(this.getAssociationQualifiers() == null) {
				this.setAssociationQualifiers(new NameAndValueList());
			}

			this.getAssociationQualifiers().addNameAndValue(qualification);
		}
	}
}
