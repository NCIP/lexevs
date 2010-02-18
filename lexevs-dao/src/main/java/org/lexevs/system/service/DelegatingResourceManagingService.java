package org.lexevs.system.service;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.logging.LoggingBean;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.utility.MyClassLoader;

public class DelegatingResourceManagingService extends LoggingBean implements SystemResourceService {

	private Registry registry;
	private PrefixResolver prefixResolver;
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	private SystemVariables systemVariables;
	
	private SystemResourceService delegate;

	
	public String createNewTablesForLoad() {
		String prefix;
		if( isSingleTableMode() ){
			this.getLogger().info("In single-table mode -- not creating a new set of tables.");
			prefix = prefixResolver.resolveDefaultPrefix();
		} else {
			this.getLogger().info("In multi-table mode -- creating a new set of tables.");
			prefix = prefixResolver.getNextCodingSchemePrefix();
		}
		return prefix;
	}
	
	public void removeCodingSchemeFromSystem(String uri, String version) {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);
		
		this.registry.removeCodingScheme(ref);
		
		if(! isSingleTableMode() ){
			lexEvsDatabaseOperations.dropTables(uri, version);
		}
	}

	public void removeResourceFromSystem(String uri) {
		this.registry.removeRegistryEntry(uri);
	}

	public String getInternalCodingSchemeNameForUserCodingSchemeName(
			String codingSchemeName, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInternalVersionStringForTag(String codingSchemeName,
			String tag) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected boolean isSingleTableMode(){
		return systemVariables.isSingleTableMode();
	}

	public void setDelegate(SystemResourceService delegate) {
		this.delegate = delegate;
	}

	public SystemResourceService getDelegate() {
		return delegate;
	}

	public MyClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

}
