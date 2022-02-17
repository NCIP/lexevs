
package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

public class GetCountConceptReferenceBean extends GetEntityAssnUidsCountBean {
	
	private List<ConceptReference> conceptReferences;

	public void setConceptReferences(List<ConceptReference> conceptReferences) {
		this.conceptReferences = conceptReferences;
	}

	public List<ConceptReference> getConceptReferences() {
		return conceptReferences;
	}
}