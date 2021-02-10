
package org.lexevs.dao.test;

import org.lexevs.dao.database.prefix.PrefixResolver;

/**
 * The Class StaticPrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class StaticPrefixResolver implements PrefixResolver {

	/** The prefix. */
	private String prefix = "";
	
	/** The history prefix. */
	private String historyPrefix = "h_";
	
	/**
	 * Instantiates a new static prefix resolver.
	 */
	public StaticPrefixResolver(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolveHistoryPrefix()
	 */
	@Override
	public String resolveHistoryPrefix() {
		return prefix + historyPrefix;
	}
	
	/**
	 * Instantiates a new static prefix resolver.
	 * 
	 * @param prefix the prefix
	 */
	public StaticPrefixResolver(String prefix){
		this.prefix = prefix;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolveDefaultPrefix()
	 */
	public String resolveDefaultPrefix() {
		return prefix;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolvePrefixForCodingScheme(java.lang.String, java.lang.String)
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeName,
			String version) {
		return prefix;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#resolvePrefixForCodingScheme(java.lang.String)
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeId) {
		return prefix;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.PrefixResolver#getNextCodingSchemePrefix()
	 */
	public String getNextCodingSchemePrefix() {
		return prefix;
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the prefix.
	 * 
	 * @param prefix the new prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Sets the history prefix.
	 * 
	 * @param historyPrefix the new history prefix
	 */
	public void setHistoryPrefix(String historyPrefix) {
		this.historyPrefix = historyPrefix;
	}

	/**
	 * Gets the history prefix.
	 * 
	 * @return the history prefix
	 */
	public String getHistoryPrefix() {
		return historyPrefix;
	}

	@Override
	public String resolvePrefixForHistoryCodingScheme(String codingSchemeId) {
		String prefix = this.resolvePrefixForCodingScheme(codingSchemeId);

		return prefix + "h_";
	}

	
}