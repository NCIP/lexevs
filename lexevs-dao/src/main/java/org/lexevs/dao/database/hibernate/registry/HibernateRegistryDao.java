package org.lexevs.dao.database.hibernate.registry;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.registry.model.Registry;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

public class HibernateRegistryDao extends HibernateDaoSupport implements RegistryDao {
	
	private static final int REGISTRY_ID = 0;
	
	private String defaultHistoryPrefix = "aaaa";
	private String defaultCodingSchemePrefix = "aaaa";
	

	public void updateLastUpdateTime(Date lastUpdateTime) {
		Registry registry = getRegistryMetadataEntry();
		registry.setLastUpdateTime(new Timestamp(lastUpdateTime.getTime()));
		this.getHibernateTemplate().update(registry);
		
	}
	
	public Date getLastUpdateTime() {
		Registry registry = getRegistryMetadataEntry();
		return registry.getLastUpdateTime();
	}
	
	protected Registry getRegistryMetadataEntry(){
		return (Registry)this.getHibernateTemplate().get(Registry.class, REGISTRY_ID);
	}

	public void removeRegistryEntry(
			AbsoluteCodingSchemeVersionReference entry) {
		// TODO Auto-generated method stub
		
	}

	public void updateTag(String uri, String version,
			String newTag) throws LBParameterException {
		RegistryEntry entry = this.getRegistryEntryForUriAndVersion(uri, version);
		entry.setTag(newTag);
		
		this.getHibernateTemplate().update(entry);
	}

	public String getLastUsedDbIdentifier() {
		return this.getRegistryMetadataEntry().getLastUsedDbIdentifer();
	}

	public String getLastUsedHistoryIdentifier() {
		return this.getRegistryMetadataEntry().getLastUsedHistoryIdentifer();
	}

	public void removeRegistryEntry(RegistryEntry entry) {
		this.getHibernateTemplate().delete(entry);
	}

	public void insertRegistryEntry(RegistryEntry entry) {
		this.getHibernateTemplate().save(entry);
	}
	
	public RegistryEntry getRegistryEntryForUriAndVersion(String uri, String version) throws LBParameterException {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		List<RegistryEntry> entries = this.getHibernateTemplate().findByExample(entry);
		if(entries == null || entries.size() == 0){
			throw new LBParameterException("No entry for: " + uri
					+ " - version " + version);
		} else if(entries.size() > 1){
			throw new LBParameterException("More than one entry for: " + uri
					+ " - version " + version);
		} 
		return entries.get(0);
	}


	public void updateRegistryEntry(RegistryEntry entry) {
		// TODO Auto-generated method stub
		
	}

	public void updateLastUsedDbIdentifier(String databaseIdentifier) {
		Registry registry = this.getRegistryMetadataEntry();
		registry.setLastUsedDbIdentifer(databaseIdentifier);
		this.getHibernateTemplate().update(registry);	
	}

	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(type);
		return this.getHibernateTemplate().findByExample(entry);
	}
	
	public List<RegistryEntry> getAllRegistryEntries() {
		return this.getHibernateTemplate().findByExample(new RegistryEntry());
	}

	public List<RegistryEntry> getRegistryEntriesForUri(String uri) {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri(uri);

		List<RegistryEntry> entries = this.getHibernateTemplate().findByExample(entry);
		if(entries == null || entries.size() == 0){
			throw new RuntimeException("No entry for: " + uri);
		}
		return entries;
	}

	public void initRegistryMetadata() {
		Assert.isNull(this.getRegistryMetadataEntry(), "Registry Metadata has already been initialized.");

		Registry metadata = new Registry();
		metadata.setId(REGISTRY_ID);
		metadata.setLastUsedDbIdentifer(this.getDefaultCodingSchemePrefix());
		metadata.setLastUsedHistoryIdentifer(this.getDefaultHistoryPrefix());
		
		Date now = new Date(); 
		metadata.setLastUpdateTime(new Timestamp(now.getTime()));
		
		this.getHibernateTemplate().save(metadata);
	}

	public String getDefaultHistoryPrefix() {
		return defaultHistoryPrefix;
	}

	public void setDefaultHistoryPrefix(String defaultHistoryPrefix) {
		this.defaultHistoryPrefix = defaultHistoryPrefix;
	}

	public String getDefaultCodingSchemePrefix() {
		return defaultCodingSchemePrefix;
	}

	public void setDefaultCodingSchemePrefix(String defaultCodingSchemePrefix) {
		this.defaultCodingSchemePrefix = defaultCodingSchemePrefix;
	}
}
