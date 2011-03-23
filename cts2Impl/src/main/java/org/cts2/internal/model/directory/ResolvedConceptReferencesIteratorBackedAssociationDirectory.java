package org.cts2.internal.model.directory;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationDirectoryEntry;
import org.cts2.internal.model.directory.iterator.DirectoryEntryIterator;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.util.PagingList;

public class ResolvedConceptReferencesIteratorBackedAssociationDirectory extends
		AssociationDirectory {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The cache. */
	private List<AssociationDirectoryEntry> cache;
	
	public ResolvedConceptReferencesIteratorBackedAssociationDirectory(
			CodedNodeGraph codedNodeGraph, 
			BeanMapper beanMapper) throws LBException {
		this(codedNodeGraph.resolveAsList(null, true, false, -1, -1, null, null, null, null, -1), beanMapper);
	}
	
	public ResolvedConceptReferencesIteratorBackedAssociationDirectory(
			ResolvedConceptReferenceList list, 
			BeanMapper beanMapper){
		try {
			this.cache = this.buildCacheList(list, beanMapper);
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}

	private List<AssociationDirectoryEntry> buildCacheList(
			ResolvedConceptReferenceList list, BeanMapper beanMapper) throws LBResourceUnavailableException {
		@SuppressWarnings("unchecked")
		Iterator<ResolvedConceptReference> iterator = (Iterator<ResolvedConceptReference>) list.iterateResolvedConceptReference();
		return new PagingList<AssociationDirectoryEntry>(
				this.buildDirectoryEntryIterator(iterator, beanMapper),
				list.getResolvedConceptReferenceCount());
	}

	private Iterator<AssociationDirectoryEntry> buildDirectoryEntryIterator(
			Iterator<ResolvedConceptReference> iterator, BeanMapper beanMapper) {
		return new 
		DirectoryEntryIterator<ResolvedConceptReference,AssociationDirectoryEntry>(
				iterator, AssociationDirectoryEntry.class, beanMapper);
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#getEntry(int)
	 */
	@Override
	public AssociationDirectoryEntry getEntry(int index)
			throws IndexOutOfBoundsException {
		return this.cache.get(index);
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#getEntry()
	 */
	@Override
	public AssociationDirectoryEntry[] getEntry() {
		return this.cache.toArray(new AssociationDirectoryEntry[this.cache.size()]);
	}

	/* (non-Javadoc)
	 * @see org.cts2.entity.EntityDirectory#iterateEntry()
	 */
	@Override
	public Iterator<? extends AssociationDirectoryEntry> iterateEntry() {
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
