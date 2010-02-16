package org.lexevs.dao.database.hibernate.registry;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.registry.model.Registry;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateRegistryDao extends HibernateDaoSupport implements RegistryDao {
	
	private static final int REGISTRY_ID = 0;

	public void updateLastUpdateTime(Date lastUpdateTime) {
		Registry registry = getRegistryEntry();
		registry.setLastUpdateTime(new Timestamp(lastUpdateTime.getTime()));
		this.getHibernateTemplate().update(registry);
		
	}
	
	public Date getLastUpdateTime() {
		Registry registry = getRegistryEntry();
		return registry.getLastUpdateTime();
	}
	
	protected Registry getRegistryEntry(){
		return (Registry)this.getHibernateTemplate().get(Registry.class, REGISTRY_ID);
	}

	public void removeRegistryEntry(
			AbsoluteCodingSchemeVersionReference entry) {
		// TODO Auto-generated method stub
		
	}

	public void updateTag(String uri, String version,
			String newTag) {
		RegistryEntry entry = this.getRegistryEntryForUriAndVersion(uri, version);
		entry.setTag(newTag);
		
		this.getHibernateTemplate().update(entry);
	}

	public String getLastUsedDbIdentifier() {
		return this.getRegistryEntry().getLastUsedDbIdentifer();
	}

	public String getLastUsedHistoryIdentifier() {
		return this.getRegistryEntry().getLastUsedHistoryIdentifer();
	}

	public void removeRegistryEntry(String uri, String version) {
		// TODO Auto-generated method stub
		
	}

	public void insertRegistryEntry(RegistryEntry entry) {
		this.getHibernateTemplate().save(entry);
	}
	
	public RegistryEntry getRegistryEntryForUriAndVersion(String uri, String version){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		List<RegistryEntry> entries = this.getHibernateTemplate().findByExample(entry);
		if(entries == null || entries.size() == 0){
			throw new RuntimeException("No entry for: " + uri
					+ " - version " + version);
		} else if(entries.size() > 1){
			throw new RuntimeException("More than one entry for: " + uri
					+ " - version " + version);
		} 
		return entries.get(0);
	}


	public void updateRegistryEntry(RegistryEntry entry) {
		// TODO Auto-generated method stub
		
	}

	public void updateLastUsedDbIdentifier(String databaseIdentifier) {
		Registry registry = this.getRegistryEntry();
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
}
