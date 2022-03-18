
package org.lexgrid.loader.rrf.staging.populatorstep;

import java.util.Map;

import org.lexevs.dao.database.type.DatabaseType;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.FactoryBean;

/**
 * A factory for creating PopulatorStep objects.
 */
public class PopulatorStepFactory implements FactoryBean {

	/** The database type. */
	private DatabaseType databaseType;
	
	/** The populator steps. */
	private Map<DatabaseType,Step> populatorSteps;
	
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		Step type = populatorSteps.get(databaseType);
		if(type == null){
			throw new RuntimeException("Could not find a Staging Table Populator Strategy for database: " + databaseType.toString());
		}
		return type;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Step.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Gets the database type.
	 * 
	 * @return the database type
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	/**
	 * Sets the database type.
	 * 
	 * @param databaseType the new database type
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * Gets the populator steps.
	 * 
	 * @return the populator steps
	 */
	public Map<DatabaseType, Step> getPopulatorSteps() {
		return populatorSteps;
	}

	/**
	 * Sets the populator steps.
	 * 
	 * @param populatorSteps the populator steps
	 */
	public void setPopulatorSteps(Map<DatabaseType, Step> populatorSteps) {
		this.populatorSteps = populatorSteps;
	}
}