
package org.lexevs.dao.database.service;

import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.event.DatabaseServiceEventSupport;
import org.lexevs.logging.Logger;

/**
 * The Class AbstractDatabaseService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AbstractDatabaseService extends DatabaseServiceEventSupport {

	/** The dao manager. */
	private DaoManager daoManager;
	
	/** The logger. */
	private Logger logger;

	/**
	 * Gets the coding scheme id.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeUId(String codingSchemeUri, String codingSchemeVersion){
		return daoManager.
			getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
	}
	
	
	/**
	 * Checks if is change type dependent.
	 * 
	 * @param versionable the versionable
	 * 
	 * @return true, if is change type dependent
	 */
	protected boolean isChangeTypeDependent(Versionable versionable) {
		return versionable.getEntryState() != null 
				&&
				versionable.getEntryState().getChangeType().equals(ChangeType.DEPENDENT);
	}
	
	/**
	 * Checks if is change type remove.
	 * 
	 * @param versionable the versionable
	 * 
	 * @return true, if is change type remove
	 */
	protected boolean isChangeTypeRemove(Versionable versionable) {
		return versionable.getEntryState() != null 
				&&
				versionable.getEntryState().getChangeType().equals(ChangeType.REMOVE);
	}
	
	/**
	 * Gets the dao manager.
	 * 
	 * @return the dao manager
	 */
	public DaoManager getDaoManager() {
		return daoManager;
	}
	
	/**
	 * Sets the dao manager.
	 * 
	 * @param daoManager the new dao manager
	 */
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}

	/**
	 * Sets the logger.
	 * 
	 * @param logger the new logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Gets the logger.
	 * 
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}


}