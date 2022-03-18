
package org.lexevs.dao.database.scheme;

import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

/**
 * A factory for creating CurrentPersistenceSchemeVersion objects.
 */
public class CurrentPersistenceSchemeVersionFactory implements FactoryBean {

	/** The system variables. */
	private SystemVariables systemVariables;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Object getObject() throws Exception {
		String versionString = this.systemVariables.getCurrentPersistenceScheme();
		return LexGridSchemaVersion.parseStringToVersion(versionString);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return LexGridSchemaVersion.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Sets the system variables.
	 * 
	 * @param systemVariables the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}
}