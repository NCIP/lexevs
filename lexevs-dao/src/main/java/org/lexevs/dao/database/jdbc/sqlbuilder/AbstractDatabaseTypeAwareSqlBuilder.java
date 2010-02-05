package org.lexevs.dao.database.jdbc.sqlbuilder;

import org.lexevs.dao.database.type.DatabaseType;

public class AbstractDatabaseTypeAwareSqlBuilder {

	private DatabaseType databaseType;

	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	public DatabaseType getDatabaseType() {
		return databaseType;
	}
}
