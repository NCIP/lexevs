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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.cts2.association.AssociationList;
import org.cts2.association.AssociationListEntry;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.iterator.DirectoryEntryIterator;
import org.cts2.internal.util.PagingList;
/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class ResolvedConceptReferencesIteratorBackedAssociationList extends
		AssociationList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The cache. */
	private List<AssociationListEntry> cache;

	/**
	 * Instantiates a new resolved concept references iterator backed
	 * Association directory.
	 * 
	 * @param codedNodeSet
	 *            the coded node set
	 * @param beanMapper
	 *            the bean mapper
	 * @throws LBException
	 *             the LB exception
	 */
	public ResolvedConceptReferencesIteratorBackedAssociationList(
			CodedNodeGraph codedNodeGraph, BeanMapper beanMapper)
			throws LBException {
		this(codedNodeGraph.resolveAsList(null, true, false, 1000, -1, null,
				null, null, null, -1), beanMapper);
	}

	/**
	 * Instantiates a new resolved concept references list backed
	 * Association directory.
	 * 
	 * @param list
	 *            the iterator
	 * @param beanMapper
	 *            the bean mapper
	 */
	public ResolvedConceptReferencesIteratorBackedAssociationList(
			ResolvedConceptReferenceList list, BeanMapper beanMapper) {
		try {
			this.cache = this.buildCacheList(list, beanMapper);
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}

	protected List<AssociationListEntry> buildCacheList(
			ResolvedConceptReferenceList list, BeanMapper beanMapper)
			throws LBException {
		@SuppressWarnings("unchecked")
		Iterator<ResolvedConceptReference> iterator = (Iterator<ResolvedConceptReference>) list
				.iterateResolvedConceptReference();
		return new PagingList<AssociationListEntry>(
				this.buildDirectoryEntryIterator(iterator, beanMapper),
				list.getResolvedConceptReferenceCount());
		
	}

	/**
	 * Builds the List entry iterator.
	 * 
	 * @param iterator
	 *            the iterator
	 * @param beanMapper
	 *            the bean mapper
	 * @return the iterator
	 */
	private Iterator<AssociationListEntry> buildDirectoryEntryIterator(
			Iterator<ResolvedConceptReference> iterator, BeanMapper beanMapper) {
		return new 
		DirectoryEntryIterator<ResolvedConceptReference,AssociationListEntry>(
				iterator, AssociationListEntry.class, beanMapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cts2.association.AssociationList#getEntry(int)
	 */
	@Override
	public AssociationListEntry getEntry(int index)
			throws IndexOutOfBoundsException {
		return this.cache.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cts2.association.AssociationList#getEntry()
	 */
	@Override
	public AssociationListEntry[] getEntry() {
		return this.cache.toArray(new AssociationListEntry[this.cache.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cts2.association.AssociationList#iterateEntry()
	 */
	@Override
	public Iterator<? extends AssociationListEntry> iterateEntry() {
		return this.cache.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cts2.association.AssociationList#getEntryCount()
	 */
	@Override
	public int getEntryCount() {
		return this.cache.size();
	}
}
