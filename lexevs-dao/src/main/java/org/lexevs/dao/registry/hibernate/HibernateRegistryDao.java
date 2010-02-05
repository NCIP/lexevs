package org.lexevs.dao.registry.hibernate;

import java.util.Calendar;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.registry.model.CodingSchemeEntry;
import org.lexevs.dao.registry.model.Registry;
import org.lexevs.dao.registry.repository.RegistryDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class HibernateRegistryDao extends HibernateDaoSupport implements RegistryDao {
	
	private static final int REGISTRY_ID = 0;

	public void updateLastUpdateTime(Calendar lastUpdateTime) {
		Registry registry = getRegistryEntry();
		registry.setLastUpdateTime(lastUpdateTime);
		this.getHibernateTemplate().update(registry);
		
	}
	
	public Calendar getLastUpdateTime() {
		Registry registry = getRegistryEntry();
		return registry.getLastUpdateTime();
	}
	
	protected Registry getRegistryEntry(){
		Registry registry = (Registry)this.getHibernateTemplate().get(Registry.class, REGISTRY_ID);
		if(registry == null){
			Registry newRegistry = buildDefaultRegistry();
			this.getHibernateTemplate().save(newRegistry);
			return newRegistry;
		} else {
			return registry;
		}
	}
	
	protected Registry buildDefaultRegistry(){
		Registry registry = new Registry();
		registry.setLastUpdateTime(Calendar.getInstance());
		registry.setId(0);
		return registry;
	}

	public void removeCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference entry) {
		// TODO Auto-generated method stub
		
	}

	public void updateTag(AbsoluteCodingSchemeVersionReference entry,
			String newTag) {
		// TODO Auto-generated method stub
		
	}

	public void insertCodingSchemeEntry(CodingSchemeEntry entry) {
		Registry registry = this.getRegistryEntry();
		System.out.println(registry.getCodingSchemeEntry().size());
		registry.getCodingSchemeEntry().add(entry);
		this.getHibernateTemplate().save(registry);
	}
	
	public CodingSchemeEntry getCodingSchemeEntry(AbsoluteCodingSchemeVersionReference codingScheme){
		CodingSchemeEntry entry = new CodingSchemeEntry();
		entry.setUrn(codingScheme.getCodingSchemeURN());
		entry.setVersion(codingScheme.getCodingSchemeVersion());
		List<CodingSchemeEntry> entries = this.getHibernateTemplate().findByExample(entry);
		if(entries == null || entries.size() == 0){
			throw new RuntimeException("No entry for: " + codingScheme.getCodingSchemeURN()
					+ " - version " + codingScheme.getCodingSchemeVersion());
		} else if(entries.size() > 1){
			throw new RuntimeException("More than one entry for: " + codingScheme.getCodingSchemeURN()
					+ " - version " + codingScheme.getCodingSchemeVersion());
		} 
		return entries.get(0);
	}
}
