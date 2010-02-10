package org.lexevs.dao.database.prefix;

import org.lexevs.system.constants.SystemVariables;

public class DefaultPrefixResolver implements PrefixResolver {

	private SystemVariables systemVariables;
	
	public String resolvePrefix() {
		return systemVariables.getAutoLoadDBPrefix();
	}

	public String resolvePrefixForCodingScheme(String codingSchemeName,
			String version) {
		return resolvePrefix();
	}

	public String resolvePrefixForCodingScheme(String codingSchemeId) {
		return resolvePrefix();
	}
	
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public String getNextCodingSchemePrefix() {
		// TODO Auto-generated method stub
		return "000";
	}

	
}
