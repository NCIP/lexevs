
package org.lexevs.dao.database.access;

import java.util.List;
import java.util.UUID;

import org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

/**
 * The Class AbstractBaseDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBaseDao implements LexGridSchemaVersionAwareDao{

	/** The prefix resolver. */
	private PrefixResolver prefixResolver;
	
	private PrimaryKeyIncrementer primaryKeyIncrementer;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao#supportsLgSchemaVersion(org.lexevs.dao.database.schemaversion.LexGridSchemaVersion)
	 */
	public boolean supportsLgSchemaVersion(LexGridSchemaVersion version) {
		for(LexGridSchemaVersion supportedVersion : doGetSupportedLgSchemaVersions()){
			if(version.isEqualVersion(supportedVersion)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Do get supported lg schema versions.
	 * 
	 * @return the list< lex grid schema version>
	 */
	public abstract List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions();
	
	protected String createUniqueId(){
		return primaryKeyIncrementer.nextKey();
	}
	
	protected String createRandomIdentifier() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */
	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	public void setPrimaryKeyIncrementer(PrimaryKeyIncrementer primaryKeyIncrementer) {
		this.primaryKeyIncrementer = primaryKeyIncrementer;
	}

	public PrimaryKeyIncrementer getPrimaryKeyIncrementer() {
		return primaryKeyIncrementer;
	}
}