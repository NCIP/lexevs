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
package org.cts2.internal.model.directory;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.helpers.ResolvedConceptReferencesIteratorAdapter;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.cts2.entity.EntityList;
import org.cts2.entity.EntityListEntry;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.iterator.DirectoryEntryIterator;
import org.cts2.internal.util.PagingList;

/**
 * The Class ResolvedConceptReferencesIteratorBackedEntityList.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvedConceptReferencesIteratorBackedEntityList extends EntityList {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -235309050016482174L;
	
	/** The cache. */
	private List<EntityListEntry> cache;
	
	/**
	 * Instantiates a new resolved concept references iterator backed entity directory.
	 *
	 * @param codedNodeSet the coded node set
	 * @param beanMapper the bean mapper
	 * @throws LBException the LB exception
	 */
	public ResolvedConceptReferencesIteratorBackedEntityList(
			CodedNodeSet codedNodeSet, 
			BeanMapper beanMapper) throws LBException {
		this(codedNodeSet.resolve(null, null, null, null, false), beanMapper);
	}
	
	/**
	 * Instantiates a new resolved concept references iterator backed entity directory.
	 *
	 * @param iterator the iterator
	 * @param beanMapper the bean mapper
	 */
	public ResolvedConceptReferencesIteratorBackedEntityList(
			ResolvedConceptReferencesIterator iterator, 
			BeanMapper beanMapper){
		try {
			this.cache = this.buildCacheList(iterator, beanMapper);
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Builds the cache list.
	 *
	 * @param iterator the iterator
	 * @param beanMapper the bean mapper
	 * @return the list
	 * @throws LBException the LB exception
	 */
	protected List<EntityListEntry> buildCacheList(ResolvedConceptReferencesIterator iterator, BeanMapper beanMapper) 
		throws LBException{
		return new PagingList<EntityListEntry>(
				this.buildDirectoryEntryIterator(iterator, beanMapper),
				iterator.numberRemaining());
	}
	
	/**
	 * Builds the directory entry iterator.
	 *
	 * @param iterator the iterator
	 * @param beanMapper the bean mapper
	 * @return the iterator
	 */
	protected Iterator<EntityListEntry>
		buildDirectoryEntryIterator(ResolvedConceptReferencesIterator iterator, BeanMapper beanMapper){
			return new 
				DirectoryEntryIterator<ResolvedConceptReference, EntityListEntry>(
						new ResolvedConceptReferencesIteratorAdapter(iterator), EntityListEntry.class, beanMapper);
	}

	
	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#getEntry(int)
	 */
	@Override
	public EntityListEntry getEntry(int index)
			throws IndexOutOfBoundsException {
		return this.cache.get(index);
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#getEntry()
	 */
	@Override
	public EntityListEntry[] getEntry() {
		return this.cache.toArray(new EntityListEntry[this.cache.size()]);
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#iterateEntry()
	 */
	@Override
	public Iterator<? extends EntityListEntry> iterateEntry() {
		return this.cache.iterator();
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#getEntryCount()
	 */
	@Override
	public int getEntryCount() {
		return this.cache.size();
	}
}
