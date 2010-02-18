package org.lexevs.dao.database.prefix;

import org.lexevs.system.constants.SystemVariables;

public class DefaultPrefixResolver implements PrefixResolver {

	private SystemVariables systemVariables;
	
	public String resolveDefaultPrefix() {
		return systemVariables.getAutoLoadDBPrefix();
	}

	public String resolvePrefixForCodingScheme(String codingSchemeName,
			String version) {
		return resolveDefaultPrefix();
	}

	public String resolvePrefixForCodingScheme(String codingSchemeId) {
		return resolveDefaultPrefix();
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
