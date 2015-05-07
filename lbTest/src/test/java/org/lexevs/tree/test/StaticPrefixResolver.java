package org.lexevs.tree.test;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.lexevs.tree.dao.prefixresolver.PrefixResolver;

public class StaticPrefixResolver implements PrefixResolver {

	public String prefix;
	
	public String getPrefix(String codingSchemeName,
			CodingSchemeVersionOrTag tagOrVersion) throws Exception {
		return prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
