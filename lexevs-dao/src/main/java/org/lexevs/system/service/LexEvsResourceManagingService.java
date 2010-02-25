package org.lexevs.system.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.logging.LoggingBean;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.utility.MyClassLoader;
import org.springframework.beans.factory.InitializingBean;

@Cacheable(cacheName = "DelegatingResourceManagingService")
public class LexEvsResourceManagingService extends LoggingBean implements SystemResourceService, InitializingBean {

	private Registry registry;
	private PrefixResolver prefixResolver;
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	private SystemVariables systemVariables;
	
	private MyClassLoader myClassLoader;
	private CodingSchemeService codingSchemeService;

	private List<CodingSchemeAliasHolder> aliasHolder = new ArrayList<CodingSchemeAliasHolder>();

	public void afterPropertiesSet() throws Exception {
		readCodingSchemeAliasesFromServer();
	}
	
	protected void readCodingSchemeAliasesFromServer(){
		List<RegistryEntry> entries = registry.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
		for(RegistryEntry entry : entries){
			CodingScheme codingScheme = codingSchemeService.getCodingSchemeByUriAndVersion(
					entry.getResourceUri(), 
					entry.getResourceVersion());
			aliasHolder.add(
					this.codingSchemeToAliasHolder(codingScheme));
		}
	}
	
	public CodingSchemeAliasHolder codingSchemeToAliasHolder(CodingScheme codingScheme){
		CodingSchemeAliasHolder aliasHolder = new CodingSchemeAliasHolder();
		aliasHolder.setCodingSchemeName(codingScheme.getCodingSchemeName());
		aliasHolder.setCodingSchemeUri(codingScheme.getCodingSchemeURI());
		aliasHolder.setLocalNames(Arrays.asList(codingScheme.getLocalName()));
		aliasHolder.setRepresentsVersion(codingScheme.getRepresentsVersion());
		aliasHolder.setFormalName(codingScheme.getFormalName());
		
		return aliasHolder;
	}

	
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
	
	@ClearCache
	public void removeCodingSchemeResourceFromSystem(String uri, String version) throws LBParameterException {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);

		if(! isSingleTableMode() ){
			lexEvsDatabaseOperations.dropTables(uri, version);
		} else {
			this.getCodingSchemeService().destroyCodingScheme(uri, version);
		}
		
		RegistryEntry entry = registry.getCodingSchemeEntry(ref);
		this.registry.removeEntry(entry);
	}

	@ClearCache
	public void removeNonCodingSchemeResourceFromSystem(String uri) {
		//RegistryEntry entry = registry.getNonCodingSchemeEntry(uri);
		
	}

	@CacheMethod
	public String getInternalCodingSchemeNameForUserCodingSchemeName(
			 String codingSchemeName, String version) throws LBParameterException {
		for(CodingSchemeAliasHolder alias : this.aliasHolder){
			if(alias.getRepresentsVersion().equals(version)){
				if( hasAlias(alias, codingSchemeName)){
					return alias.getCodingSchemeName();
				}
			}
		}
		 throw new LBParameterException("No coding scheme could be located for the values you provided",
                 SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
                 codingSchemeName + ", " + version);
	}
	
	public String getUriForUserCodingSchemeName(
			String codingSchemeName, String version) throws LBParameterException {
		for(CodingSchemeAliasHolder alias : this.aliasHolder){
			if(alias.getRepresentsVersion().equals(version)){
				if( hasAlias(alias, codingSchemeName)){
					return alias.getCodingSchemeUri();
				}
			}
		}
		 throw new LBParameterException("No coding scheme could be located for the values you provided",
                 SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
                 codingSchemeName + ", " + version);
	}
	
	protected boolean hasAlias(CodingSchemeAliasHolder holder, String alias){
		return holder.getCodingSchemeName().equals(alias) ||
			holder.getCodingSchemeUri().equals(alias) ||
			holder.getFormalName().equals(alias) ||
			holder.getLocalNames().contains(alias);	
	}
	
	protected List<String> getUriForCodingSchemeName(String codingSchemeName){
		List<String> returnList = new ArrayList<String>();

		for(CodingSchemeAliasHolder alias : this.aliasHolder){
			if( hasAlias(alias, codingSchemeName)){
				returnList.add(alias.getCodingSchemeUri());
			}
		}
		return returnList;
	}
	
	public String getUriForUserCodingSchemeName(String codingSchemeName)
	throws LBParameterException {
			List<String> uris = getUriForCodingSchemeName(codingSchemeName);
			if(uris == null || uris.size() == 0){
				throw new LBParameterException("No URI found for Coding Scheme Name: " + codingSchemeName);
			}
			
			String uri = uris.get(0);
			for(int i=1;i<uris.size();i++){
				if(! uris.get(i).equals(uri)){
					throw new LBParameterException("Found multiple URIs for Coding Scheme Name: " + codingSchemeName);
				}
			}
			return uri;
	}

	@CacheMethod
	public String getInternalVersionStringForTag(String codingSchemeName,
			String tag) throws LBParameterException {
		
		List<String> uris = getUriForCodingSchemeName(codingSchemeName);
		
		List<RegistryEntry> foundEntries = new ArrayList<RegistryEntry>();
		for(String uri : uris){
			List<RegistryEntry> entries = this.registry.getEntriesForUri(uri);
			
			for(RegistryEntry entry : entries){
				foundEntries.add(entry);
			}
		}
		
		if(foundEntries.size() > 1){
			
			List<RegistryEntry> taggedEntries = getTaggedEntries(foundEntries, tag);
			if(taggedEntries.size() == 0){
				 throw new LBParameterException("No Coding Schemes were found for the values you provided: ",
		                 SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
		                 codingSchemeName + ", " + tag);
			}
			
			if(taggedEntries.size() > 1){
				 throw new LBParameterException("Multiple Coding Schemes were found for the values you provided: ",
		                 SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
		                 codingSchemeName + ", " + tag);
			}
			
			return taggedEntries.get(0).getResourceVersion();
		}
		
		if(foundEntries.size() == 1){
			RegistryEntry entry = foundEntries.get(0);
			if(entry.getTag().equals(tag)){
				return entry.getResourceVersion();
			}
		}
		
		 throw new LBParameterException("No coding scheme could be located for the values you provided",
                 SQLTableConstants.TBLCOL_CODINGSCHEMENAME + ", " + SQLTableConstants.TBLCOL_VERSION,
                 codingSchemeName + ", " + tag);
	}

	protected List<RegistryEntry> getTaggedEntries(List<RegistryEntry> entries, String tag){
		List<RegistryEntry> foundEntries = new ArrayList<RegistryEntry>();
		for(RegistryEntry entry : foundEntries){
			if(entry.getTag().equals(tag)){
				foundEntries.add(entry);
			}
		}
		return foundEntries;
	}

	public boolean containsNonCodingSchemeResource(String uri)
	throws LBParameterException {
		return registry.containsNonCodingSchemeEntry(uri);
	}

	public boolean containsCodingSchemeResource(String uri, String version)
	throws LBParameterException {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(uri);
		ref.setCodingSchemeVersion(version);
		
		return registry.containsCodingSchemeEntry(ref);
	}
	
	@ClearCache
	public void updateCodingSchemeResourceTag(
			AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
			throws LBParameterException {
		RegistryEntry entry = registry.getCodingSchemeEntry(codingScheme);
		entry.setTag(newTag);
		registry.updateEntry(entry);
		
	}

	@ClearCache
	public void updateCodingSchemeResourceStatus(
			AbsoluteCodingSchemeVersionReference codingScheme,
			CodingSchemeVersionStatus status) throws LBParameterException {

		RegistryEntry entry = registry.getCodingSchemeEntry(codingScheme);
		entry.setStatus(status.toString());
		registry.updateEntry(entry);
	}

	public void updateNonCodingSchemeResourceStatus(String uri,
			CodingSchemeVersionStatus status) throws LBParameterException {
		RegistryEntry entry = registry.getNonCodingSchemeEntry(uri);
		entry.setStatus(status.toString());
		registry.updateEntry(entry);
	}

	public void updateNonCodingSchemeResourceTag(String uri, String newTag)
			throws LBParameterException {
		RegistryEntry entry = registry.getNonCodingSchemeEntry(uri);
		entry.setTag(newTag);
		registry.updateEntry(entry);
	}

	protected boolean isSingleTableMode(){
		return systemVariables.isSingleTableMode();
	}

	public MyClassLoader getClassLoader() {
		return this.myClassLoader;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	public LexEvsDatabaseOperations getLexEvsDatabaseOperations() {
		return lexEvsDatabaseOperations;
	}

	public void setLexEvsDatabaseOperations(
			LexEvsDatabaseOperations lexEvsDatabaseOperations) {
		this.lexEvsDatabaseOperations = lexEvsDatabaseOperations;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public void setMyClassLoader(MyClassLoader myClassLoader) {
		this.myClassLoader = myClassLoader;
	}
	
	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

	protected static class CodingSchemeAliasHolder {
		
		private String codingSchemeName;
		private String codingSchemeUri;
		private String representsVersion;
		private String formalName;
		private List<String> localNames = new ArrayList<String>();
		
		public String getCodingSchemeName() {
			return codingSchemeName;
		}
		public void setCodingSchemeName(String codingSchemeName) {
			this.codingSchemeName = codingSchemeName;
		}
		public String getCodingSchemeUri() {
			return codingSchemeUri;
		}
		public void setCodingSchemeUri(String codingSchemeUri) {
			this.codingSchemeUri = codingSchemeUri;
		}
		public String getRepresentsVersion() {
			return representsVersion;
		}
		public void setRepresentsVersion(String representsVersion) {
			this.representsVersion = representsVersion;
		}
		public String getFormalName() {
			return formalName;
		}
		public void setFormalName(String formalName) {
			this.formalName = formalName;
		}
		public List<String> getLocalNames() {
			return localNames;
		}
		public void setLocalNames(List<String> localNames) {
			this.localNames = localNames;
		}
	}
}
