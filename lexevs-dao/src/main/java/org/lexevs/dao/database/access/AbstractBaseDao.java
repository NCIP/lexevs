package org.lexevs.dao.database.access;

import java.util.List;

import org.lexevs.dao.database.key.Java5UUIDKeyGenerator;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

public abstract class AbstractBaseDao implements LexGridSchemaVersionAwareDao{

	private PrefixResolver prefixResolver;

	public boolean supportsLgSchemaVersion(LexGridSchemaVersion version) {
		for(LexGridSchemaVersion supportedVersion : doGetSupportedLgSchemaVersions()){
			if(version.isEqualVersion(supportedVersion)){
				return true;
			}
		}
		return false;
	}
	
	public abstract List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions();
	
	
	protected String createUniqueId(){
		return Java5UUIDKeyGenerator.getRandomUUID().toString();
	}
	
	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}
}
