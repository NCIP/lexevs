package org.lexevs.dao.database.access;

import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

public interface LexGridSchemaVersionAwareDao {

	public boolean supportsLgSchemaVersion(LexGridSchemaVersion version);
}
