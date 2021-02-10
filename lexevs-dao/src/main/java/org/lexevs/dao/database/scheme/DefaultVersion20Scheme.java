
package org.lexevs.dao.database.scheme;

import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.registry.setup.LexEvsDatabaseSchemaSetup;

/**
 * The Class DefaultVersion20Scheme.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultVersion20Scheme implements PersistenceScheme {

	private static LexGridSchemaVersion SCHEME_VERSION = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	/** The lex evs database schema setup. */
	private LexEvsDatabaseSchemaSetup lexEvsDatabaseSchemaSetup;
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.scheme.PersistenceScheme#destroyScheme()
	 */
	@Override
	public void destroyScheme() {
		//
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.scheme.PersistenceScheme#getLexGridSchemaVersion()
	 */
	@Override
	public LexGridSchemaVersion getLexGridSchemaVersion() {
		return SCHEME_VERSION;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.scheme.PersistenceScheme#initScheme()
	 */
	@Override
	public void initScheme() {
		try {
			this.lexEvsDatabaseSchemaSetup.setUpLexEvsDbSchema();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.scheme.PersistenceScheme#registerDaos(org.lexevs.dao.database.access.DaoManager)
	 */
	@Override
	public void registerDaos(DaoManager daoManager) {
		//done via Spring
	}

	/**
	 * Sets the lex evs database schema setup.
	 * 
	 * @param lexEvsDatabaseSchemaSetup the new lex evs database schema setup
	 */
	public void setLexEvsDatabaseSchemaSetup(LexEvsDatabaseSchemaSetup lexEvsDatabaseSchemaSetup) {
		this.lexEvsDatabaseSchemaSetup = lexEvsDatabaseSchemaSetup;
	}

	/**
	 * Gets the lex evs database schema setup.
	 * 
	 * @return the lex evs database schema setup
	 */
	public LexEvsDatabaseSchemaSetup getLexEvsDatabaseSchemaSetup() {
		return lexEvsDatabaseSchemaSetup;
	}
}