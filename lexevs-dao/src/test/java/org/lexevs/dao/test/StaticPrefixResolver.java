package org.lexevs.dao.test;

import org.lexevs.dao.database.prefix.PrefixResolver;

public class StaticPrefixResolver implements PrefixResolver {

	private String prefix = "";
	
	public StaticPrefixResolver(){
		super();
	}
	
	public StaticPrefixResolver(String prefix){
		this.prefix = prefix;
	}
	
	public String resolvePrefix() {
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
}
