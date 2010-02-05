package org.lexgrid.loader.wrappers;

import java.util.List;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.LexGrid.persistence.model.EntityAssnsToEquals;

public class RelationQualifierHolder {
	private EntityAssnsToEntity relation; 
	private List<EntityAssnsToEquals> qualifiers;
	
	public EntityAssnsToEntity getRelation() {
		return relation;
	}
	public void setRelation(EntityAssnsToEntity relation) {
		this.relation = relation;
	}
	public List<EntityAssnsToEquals> getQualifiers() {
		return qualifiers;
	}
	public void setQualifiers(List<EntityAssnsToEquals> qualifiers) {
		this.qualifiers = qualifiers;
	} 
}
