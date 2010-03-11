package org.lexevs.dao.test;

import org.lexevs.dao.database.prefix.PrefixResolver;

public class StaticPrefixResolver implements PrefixResolver {

	private String prefix = "";
	private String historyPrefix = "_h";
	
	public StaticPrefixResolver(){
		super();
	}
	
	@Override
	public String resolveHistoryPrefix() {
		return prefix + historyPrefix;
	}
	
	public StaticPrefixResolver(String prefix){
		this.prefix = prefix;
	}
	
	public String resolveDefaultPrefix() {
		return prefix;
	}

	public String resolvePrefixForCodingScheme(String codingSchemeName,
			String version) {
		return prefix;
	}

	public String resolvePrefixForCodingScheme(String codingSchemeId) {
		return prefix;
	}

	public String getNextCodingSchemePrefix() {
		return prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setHistoryPrefix(String historyPrefix) {
		this.historyPrefix = historyPrefix;
	}

	public String getHistoryPrefix() {
		return historyPrefix;
	}

	
}
