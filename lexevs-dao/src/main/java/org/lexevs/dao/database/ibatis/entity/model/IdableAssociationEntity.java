
package org.lexevs.dao.database.ibatis.entity.model;

import org.LexGrid.proxy.CastorProxy;
import org.LexGrid.relations.AssociationEntity;

public class IdableAssociationEntity extends AssociationEntity implements CastorProxy {

	private static final long serialVersionUID = 6631733263624593955L;
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}