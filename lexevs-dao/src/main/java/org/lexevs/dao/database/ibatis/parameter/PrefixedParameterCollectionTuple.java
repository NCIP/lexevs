
package org.lexevs.dao.database.ibatis.parameter;

import java.util.Collection;

public class PrefixedParameterCollectionTuple extends PrefixedParameterCollection{

/** The param2. */
private Collection<String> param3;

	public PrefixedParameterCollectionTuple() {
		super();
	}

	public PrefixedParameterCollectionTuple(String prefix, String param1,
			Collection<String> param2, Collection<String> param3) {
		super(prefix, param1, param2);
		this.param3 = param3;
	}

	public void setParam3(Collection<String> param3) {
		this.param3 = param3;
	}

	public Collection<String> getParam3() {
		return param3;
	}
}