package org.lexevs.registry.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.registry.model.RegistryEntry;

public class DelegatingDatabaseToXmlRegistry implements Registry {
	
	private Registry databaseRegistry;
	private Registry xmlRegistry;


	public void addNewItem(RegistryEntry entry) throws Exception {
	    databaseRegistry.addNewItem(entry);
	}

	public List<RegistryEntry> getAllRegistryEntries() {
		List<RegistryEntry> allEntries = new ArrayList<RegistryEntry>();
		allEntries.addAll(this.databaseRegistry.getAllRegistryEntries());
		allEntries.addAll(this.xmlRegistry.getAllRegistryEntries());
		
		return allEntries;
	}

	public List<RegistryEntry> getAllRegistryEntriesOfType(ResourceType type) {
		List<RegistryEntry> allEntries = new ArrayList<RegistryEntry>();
		allEntries.addAll(this.databaseRegistry.getAllRegistryEntriesOfType(type));
		allEntries.addAll(this.xmlRegistry.getAllRegistryEntriesOfType(type));
		
		return allEntries;
	}

	public List<RegistryEntry> getEntriesForUri(String uri)
			throws LBParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getLastUpdateTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNextDBIdentifier() throws LBInvocationException {
		return databaseRegistry.getNextDBIdentifier();
	}

	public String getNextHistoryIdentifier() throws LBInvocationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference codingScheme) {
		return databaseRegistry.containsCodingSchemeEntry(codingScheme) ||
			xmlRegistry.containsCodingSchemeEntry(codingScheme);
	}

	public boolean containsNonCodingSchemeEntry(String uri) {
		return databaseRegistry.containsNonCodingSchemeEntry(uri) ||
			xmlRegistry.containsNonCodingSchemeEntry(uri);
	}

	public RegistryEntry getCodingSchemeEntry(
			AbsoluteCodingSchemeVersionReference codingScheme)
			throws LBParameterException {
		if(this.databaseRegistry.containsCodingSchemeEntry(codingScheme)){
			return databaseRegistry.getCodingSchemeEntry(codingScheme);
		} else {
			return xmlRegistry.getCodingSchemeEntry(codingScheme);
		}
	}

	public RegistryEntry getNonCodingSchemeEntry(String uri)
			throws LBParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeEntry(RegistryEntry entry) throws LBParameterException {
		// TODO Auto-generated method stub
		
	}
	
	public void updateEntry(RegistryEntry entry) throws LBParameterException {
		if(entry.getResourceType().equals(ResourceType.CODING_SCHEME)) {
			
			AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
			ref.setCodingSchemeURN(entry.getResourceUri());
			ref.setCodingSchemeVersion(entry.getResourceVersion());
			
			if(this.databaseRegistry.containsCodingSchemeEntry(ref)){
				 databaseRegistry.updateEntry(entry);
			} else {
				 xmlRegistry.updateEntry(entry);
			}	
		} else {
			
			if(this.databaseRegistry.containsNonCodingSchemeEntry(entry.getResourceUri())){
				 databaseRegistry.updateEntry(entry);
			} else {
				 xmlRegistry.updateEntry(entry);
			}	
		}
	}
	
	public void updateCodingSchemeEntryTag(
			AbsoluteCodingSchemeVersionReference codingScheme, String newTag)
			throws LBParameterException {
		if(this.databaseRegistry.containsCodingSchemeEntry(codingScheme)){
			RegistryEntry entry = databaseRegistry.getCodingSchemeEntry(codingScheme);
			entry.setTag(newTag);
			databaseRegistry.updateEntry(entry);
		} else {
			RegistryEntry entry = xmlRegistry.getCodingSchemeEntry(codingScheme);
			entry.setTag(newTag);
			xmlRegistry.updateEntry(entry);
		}	
	}
	
	public Registry getDatabaseRegistry() {
		return databaseRegistry;
	}

	public void setDatabaseRegistry(Registry databaseRegistry) {
		this.databaseRegistry = databaseRegistry;
	}

	public Registry getXmlRegistry() {
		return xmlRegistry;
	}

	public void setXmlRegistry(Registry xmlRegistry) {
		this.xmlRegistry = xmlRegistry;
	}
}
