
package org.lexevs.dao.database.key.incrementer;

import java.util.List;

import org.lexevs.dao.database.type.DatabaseType;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

/**
 * The Class AbstractKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractKeyIncrementer implements PrimaryKeyIncrementer {

	/** The data field max value incrementer. */
	private DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer;
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#nextKey()
	 */
	@Override
	public String nextKey() {
		if(dataFieldMaxValueIncrementer == null) {
			dataFieldMaxValueIncrementer = this.createDataFieldMaxValueIncrementer();
		}
		return this.dataFieldMaxValueIncrementer.nextStringValue();
	}
	
	/**
	 * Creates the data field max value incrementer.
	 * 
	 * @return the data field max value incrementer
	 */
	protected abstract DataFieldMaxValueIncrementer createDataFieldMaxValueIncrementer();	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#supportsDatabases(org.lexevs.dao.database.type.DatabaseType)
	 */
	@Override
	public boolean supportsDatabases(DatabaseType databaseType) {
		for(DatabaseType dbType : getSupportedDatabaseTypes()){
			if(dbType.equals(databaseType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the supported database types.
	 * 
	 * @return the supported database types
	 */
	protected abstract List<DatabaseType> getSupportedDatabaseTypes();
}