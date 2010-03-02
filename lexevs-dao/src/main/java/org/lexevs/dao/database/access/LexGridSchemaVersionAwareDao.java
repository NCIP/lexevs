package org.lexevs.dao.database.access;

import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

public interface LexGridSchemaVersionAwareDao {

	public boolean supportsLgSchemaVersion(LexGridSchemaVersion version);
	
	public <T> T executeInTransaction(IndividualDaoCallback<T> callback);
	
	public interface IndividualDaoCallback<T> {
		public T execute();
	}
}
