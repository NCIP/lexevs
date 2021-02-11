
package org.lexevs.logging;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;

/**
 * The Class LoggingBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLoggingBean implements LoggingBean {

	/** The logger. */
	private LgLoggerIF logger;

	/**
	 * Sets the logger.
	 * 
	 * @param logger the new logger
	 */
	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

	/**
	 * Gets the logger.
	 * 
	 * @return the logger
	 */
	public LgLoggerIF getLogger() {
		return logger;
	}

}