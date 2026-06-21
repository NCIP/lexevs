
package org.lexevs.dao.database.ibatis.parameter;

import org.lexevs.logging.Logger;
import org.lexevs.system.constants.SystemVariables;

/**
 * The Class PrefixedTableParameterBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedTableParameterBean {

	/** The prefix. */
	private String prefix;
	
	private String defaultPrefix;
		
	private String actualTableSetPrefix;
	
	/**
	 * Instantiates a new prefixed table parameter bean.
	 */
	public PrefixedTableParameterBean() {
		super();
		defaultPrefix = getDefaultPrefix();
	}

	/**
	 * Instantiates a new prefixed table parameter bean.
	 * 
	 * @param prefix the prefix
	 */
	public PrefixedTableParameterBean(String prefix) {
		super();
		this.prefix = prefix;
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
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	public void setActualTableSetPrefix(String actualTableSetPrefix) {
		this.actualTableSetPrefix = actualTableSetPrefix;
	}

	public String getActualTableSetPrefix() {
		if(actualTableSetPrefix == null) {
			return this.getPrefix();
		} else {
			return actualTableSetPrefix;
		}
	}
	
	public String getDefaultPrefix() {
		
		try {
			defaultPrefix = new SystemVariables(new Logger()).getAutoLoadDBPrefix();
		} catch (Exception e) {
			throw new RuntimeException("Database prefix resolution from SystemVariables Failed", e);
		}
		return defaultPrefix;
	}
	
	public void setDefaultPrefix(String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}
}