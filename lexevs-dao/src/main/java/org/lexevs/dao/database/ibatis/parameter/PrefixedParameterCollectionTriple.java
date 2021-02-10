
package org.lexevs.dao.database.ibatis.parameter;

import java.util.Collection;

public class PrefixedParameterCollectionTriple extends PrefixedParameterCollectionTuple {

/** The param4. */
private Collection<String> param4;

	public PrefixedParameterCollectionTriple() {
		super();
	}

	public PrefixedParameterCollectionTriple(String prefix, String param1,
			Collection<String> param2, Collection<String> param3, Collection<String> param4) {
		super(prefix, param1, param2, param3);
		this.param4 = param4;
	}

	public Collection<String> getParam4() {
		return param4;
	}

	public void setParam4(Collection<String> param4) {
		this.param4 = param4;
	}
}