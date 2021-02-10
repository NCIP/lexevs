
package org.LexGrid.custom.concepts;

import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.relations.AssociationEntity;



public class EntityFactory {

/**
	 * Create a default Concept with its Entity Type set to Concept
	 * @return
	 */
public static AssociationEntity createAssociation() {
		AssociationEntity entity = new AssociationEntity();
		entity.getEntityTypeAsReference().add(EntityTypes.ASSOCIATION.toString());
		return entity;
	}	
}