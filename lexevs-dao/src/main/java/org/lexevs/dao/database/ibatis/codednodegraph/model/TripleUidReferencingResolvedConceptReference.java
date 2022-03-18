
package org.lexevs.dao.database.ibatis.codednodegraph.model;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.proxy.CastorProxy;

@LgClientSideSafe
public class TripleUidReferencingResolvedConceptReference extends ResolvedConceptReference implements CastorProxy {

	private static final long serialVersionUID = 4411803013641184558L;
	
	private String tripleUid;

	public void setTripleUid(String tripleUid) {
		this.tripleUid = tripleUid;
	}

	public String getTripleUid() {
		return tripleUid;
	}
}