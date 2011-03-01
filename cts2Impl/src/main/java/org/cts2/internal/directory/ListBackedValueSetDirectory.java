/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.cts2.internal.directory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.valueset.ValueSetDirectory;
import org.cts2.valueset.ValueSetDirectoryEntry;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

/**
 * The Class ListBackedValueSetDirectory.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ListBackedValueSetDirectory extends ValueSetDirectory {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -235309050016482174L;
	
	/** The cache. */
	private List<ValueSetDirectoryEntry> list;
	
	/**
	 * Instantiates a new resolved concept references iterator backed entity directory.
	 *
	 * @param codedNodeSet the coded node set
	 * @param beanMapper the bean mapper
	 * @throws LBException the LB exception
	 */
	public ListBackedValueSetDirectory(
			List<ResolvedValueSetDefinition> resolvedValueSetDefinitions, 
			BeanMapper beanMapper) throws LBException {
		this.list = this.createValueSetDirectoryEntryList(resolvedValueSetDefinitions, beanMapper);
	}
	
	protected List<ValueSetDirectoryEntry> createValueSetDirectoryEntryList(
			List<ResolvedValueSetDefinition> resolvedValueSetDefinitions,
			BeanMapper beanMapper) {
		List<ValueSetDirectoryEntry> returnList = new ArrayList<ValueSetDirectoryEntry>();
		for(ResolvedValueSetDefinition resolvedValueSetDefinition : resolvedValueSetDefinitions) {
			returnList.add(beanMapper.map(resolvedValueSetDefinition, ValueSetDirectoryEntry.class));
		}
		
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#getEntry(int)
	 */
	@Override
	public ValueSetDirectoryEntry getEntry(int index)
			throws IndexOutOfBoundsException {
		return this.list.get(index);
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#getEntry()
	 */
	@Override
	public ValueSetDirectoryEntry[] getEntry() {
		return this.list.toArray(new ValueSetDirectoryEntry[this.list.size()]);
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#iterateEntry()
	 */
	@Override
	public Iterator<? extends ValueSetDirectoryEntry> iterateEntry() {
		return this.list.iterator();
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#getEntryCount()
	 */
	@Override
	public int getEntryCount() {
		return this.list.size();
	}
}
