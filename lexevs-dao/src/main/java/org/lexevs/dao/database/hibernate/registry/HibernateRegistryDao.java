
package org.lexevs.dao.database.hibernate.registry;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.registry.model.Registry;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class HibernateRegistryDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Transactional(readOnly=false)
public class HibernateRegistryDao extends org.springframework.orm.hibernate5.support.HibernateDaoSupport implements RegistryDao {
	
	/** The Constant REGISTRY_ID. */
	private static final int REGISTRY_ID = 0;
	
	/** The default history prefix. */
	private String defaultHistoryPrefix = "aaaa";
	
	/** The default coding scheme prefix. */
	private String defaultCodingSchemePrefix = "aaaa";
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#updateLastUpdateTime(java.util.Date)
	 */
	public void updateLastUpdateTime(Date lastUpdateTime) {
		Registry registry = getRegistryMetadataEntry();
		registry.setLastUpdateTime(new Timestamp(lastUpdateTime.getTime()));
		this.getHibernateTemplate().update(registry);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#getLastUpdateTime()
	 */
	public Date getLastUpdateTime() {
		Registry registry = getRegistryMetadataEntry();
		return registry.getLastUpdateTime();
	}
	
	/**
	 * Gets the registry metadata entry.
	 * 
	 * @return the registry metadata entry
	 */
	protected Registry getRegistryMetadataEntry(){
		return (Registry)this.getHibernateTemplate().get(Registry.class, REGISTRY_ID);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#deleteRegistryEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	public void deleteRegistryEntry(
			RegistryEntry entry) {
		this.getHibernateTemplate().delete(entry);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#getLastUsedDbIdentifier()
	 */
	public String getLastUsedDbIdentifier() {
		return this.getRegistryMetadataEntry().getLastUsedDbIdentifer();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#getLastUsedHistoryIdentifier()
	 */
	public String getLastUsedHistoryIdentifier() {
		return this.getRegistryMetadataEntry().getLastUsedHistoryIdentifer();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#insertRegistryEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	public void insertRegistryEntry(RegistryEntry entry) {
		this.getHibernateTemplate().save(entry);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#getRegistryEntryForUriAndVersion(java.lang.String, java.lang.String)
	 */
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
	
	@SuppressWarnings("unchecked")
	public List<RegistryEntry> getAllRegistryEntriesOfUriAndTypes(String uri,
			ResourceType... types) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RegistryEntry.class);

		Criterion typeRestriction = Restrictions.in("resourceType", types);
		
		Criterion uriRestriction = Restrictions.eq("resourceUri", uri);
		
		List<RegistryEntry> entries = (List<RegistryEntry>) this.getHibernateTemplate().findByCriteria(		
				criteria.add(Restrictions.and(uriRestriction, typeRestriction)));
		
		return entries;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#updateRegistryEntry(org.lexevs.registry.model.RegistryEntry)
	 */
	public void updateRegistryEntry(RegistryEntry entry) {
		this.getHibernateTemplate().merge(entry);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#updateLastUsedDbIdentifier(java.lang.String)
	 */
	public void updateLastUsedDbIdentifier(String databaseIdentifier) {
		Registry registry = this.getRegistryMetadataEntry();
		registry.setLastUsedDbIdentifer(databaseIdentifier);
		this.getHibernateTemplate().update(registry);	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#getAllRegistryEntriesOfType(org.lexevs.registry.service.Registry.ResourceType)
	 */
	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(type);
		return this.getHibernateTemplate().findByExample(entry);
	}
	
	public List<RegistryEntry> getAllRegistryEntriesOfTypeAndURI(ResourceType type, String uri) {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(type);
		entry.setResourceUri(uri);
		return this.getHibernateTemplate().findByExample(entry);
	}
	
	public List<RegistryEntry> getAllRegistryEntriesOfTypeURIAndVersion(ResourceType type, String uri, String version) {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(type);
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		return this.getHibernateTemplate().findByExample(entry);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#getAllRegistryEntries()
	 */
	public List<RegistryEntry> getAllRegistryEntries() {
		return this.getHibernateTemplate().findByExample(new RegistryEntry());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#getRegistryEntriesForUri(java.lang.String)
	 */
	public List<RegistryEntry> getRegistryEntriesForUri(String uri) {
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri(uri);

		List<RegistryEntry> entries = this.getHibernateTemplate().findByExample(entry);
		if(entries == null || entries.size() == 0){
			throw new RuntimeException("No entry for: " + uri);
		}
		return entries;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.registry.RegistryDao#initRegistryMetadata()
	 */
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

	/**
	 * Gets the default history prefix.
	 * 
	 * @return the default history prefix
	 */
	public String getDefaultHistoryPrefix() {
		return defaultHistoryPrefix;
	}

	/**
	 * Sets the default history prefix.
	 * 
	 * @param defaultHistoryPrefix the new default history prefix
	 */
	public void setDefaultHistoryPrefix(String defaultHistoryPrefix) {
		this.defaultHistoryPrefix = defaultHistoryPrefix;
	}

	/**
	 * Gets the default coding scheme prefix.
	 * 
	 * @return the default coding scheme prefix
	 */
	public String getDefaultCodingSchemePrefix() {
		return defaultCodingSchemePrefix;
	}

	/**
	 * Sets the default coding scheme prefix.
	 * 
	 * @param defaultCodingSchemePrefix the new default coding scheme prefix
	 */
	public void setDefaultCodingSchemePrefix(String defaultCodingSchemePrefix) {
		this.defaultCodingSchemePrefix = defaultCodingSchemePrefix;
	}
}