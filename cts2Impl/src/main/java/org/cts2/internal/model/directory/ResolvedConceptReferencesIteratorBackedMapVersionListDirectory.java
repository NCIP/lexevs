package org.cts2.internal.model.directory;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Impl.helpers.ResolvedConceptReferencesIteratorAdapter;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.cts2.core.descriptors.DirectoryEntryDescriptor;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.iterator.DirectoryEntryIterator;
import org.cts2.internal.util.PagingList;
import org.cts2.map.MapVersionDirectoryEntry;
import org.cts2.map.MapVersionList;
import org.cts2.map.MapVersionListEntry;

/**
 * The class ResolvedConceptReferencesIteratorBackedMapVersionListDirectory
 * 
 * @author <a href="mailto:lian.zonghui@mayo.edu">Zonghui Lian</a>
 * 
 */
public class ResolvedConceptReferencesIteratorBackedMapVersionListDirectory
		extends MapVersionList {
	private static final long serialVersionUID = -68126108480534728L;

	/* The cache */
	private List<MapVersionListEntry> cache;

	public ResolvedConceptReferencesIteratorBackedMapVersionListDirectory(
			Mapping mapping, BeanMapper beanMapper) throws LBException {
		this(mapping.resolveMapping(), beanMapper);
	}

	public ResolvedConceptReferencesIteratorBackedMapVersionListDirectory(
			ResolvedConceptReferencesIterator iterator, BeanMapper beanMapper) {
		try {
			this.buildCacheList(iterator, beanMapper);
		} catch (LBResourceUnavailableException e) {
			throw new RuntimeException(e);
		}
	}

	private List<MapVersionListEntry> buildCacheList(
			ResolvedConceptReferencesIterator iterator, BeanMapper beanMapper)
			throws LBResourceUnavailableException {
		return new PagingList<MapVersionListEntry>(
				this.buildMapVersionListEntryIterator(iterator, beanMapper),
				iterator.numberRemaining());
	}

	private Iterator<MapVersionListEntry> buildMapVersionListEntryIterator(
			ResolvedConceptReferencesIterator iterator, BeanMapper beanMapper) {
		return new DirectoryEntryIterator<ResolvedConceptReference, MapVersionListEntry>(
				new ResolvedConceptReferencesIteratorAdapter(iterator),
				MapVersionListEntry.class, beanMapper);
	}

	@Override
	public MapVersionListEntry getEntry(int index)
			throws IndexOutOfBoundsException {
		return this.cache.get(index);
	}

	@Override
	public MapVersionListEntry[] getEntry() {
		return this.cache.toArray(new MapVersionListEntry[this.cache.size()]);
	}

	@Override
	public Iterator<? extends MapVersionListEntry> iterateEntry() {
		return this.cache.iterator();
	}

	@Override
	public int getEntryCount() {
		return this.cache.size();
	}
}
