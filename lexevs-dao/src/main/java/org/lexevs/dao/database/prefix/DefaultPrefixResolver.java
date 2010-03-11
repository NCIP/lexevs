package org.lexevs.dao.database.prefix;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.constants.SystemVariables;

public class DefaultPrefixResolver implements PrefixResolver {

	private SystemVariables systemVariables;
	
	private Registry registry;
	
	private String historyPrefix = "h_";
	
	public String resolveDefaultPrefix() {
		return systemVariables.getAutoLoadDBPrefix();
	}

	public String resolveHistoryPrefix() {
		return this.resolveDefaultPrefix() + historyPrefix;
	}
	
	public String resolvePrefixForCodingScheme(String codingSchemeName,
			String version) {
		return resolveDefaultPrefix();
	}

	public String resolvePrefixForCodingScheme(String codingSchemeId) {
		return resolveDefaultPrefix();
	}

	public String getNextCodingSchemePrefix() {
		try {
			return registry.getNextDBIdentifier();
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		}
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
	

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public void setHistoryPrefix(String historyPrefix) {
		this.historyPrefix = historyPrefix;
	}

	public String getHistoryPrefix() {
		return historyPrefix;
	}
}
