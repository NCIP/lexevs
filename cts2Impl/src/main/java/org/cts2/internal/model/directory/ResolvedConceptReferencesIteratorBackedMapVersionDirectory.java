package org.cts2.internal.model.directory;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Impl.helpers.ResolvedConceptReferencesIteratorAdapter;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.iterator.DirectoryEntryIterator;
import org.cts2.internal.util.PagingList;
import org.cts2.map.MapVersionDirectory;
import org.cts2.map.MapVersionDirectoryEntry;


/**
 * The class ResolvedConceptReferencesIteratorBackedMapVersionDirectory
 * @author <a href="mailto:lian.zonghui@mayo.edu">Zonghui Lian</a>
 *
 */
public class ResolvedConceptReferencesIteratorBackedMapVersionDirectory extends
		MapVersionDirectory {

	private static final long serialVersionUID = 6312390720438822076L;
	private List<MapVersionDirectoryEntry> cache;

	public ResolvedConceptReferencesIteratorBackedMapVersionDirectory(
			Mapping mapping, BeanMapper beanMapper) throws LBException {
		this(mapping.resolveMapping(), beanMapper);
	}

	public ResolvedConceptReferencesIteratorBackedMapVersionDirectory(
			ResolvedConceptReferencesIterator iterator, BeanMapper beanMapper) {
		try {
			this.cache = this.buildCacheList(iterator, beanMapper);
		} catch (LBResourceUnavailableException e) {
			throw new RuntimeException(e);
		}
	}

	private List<MapVersionDirectoryEntry> buildCacheList(
			ResolvedConceptReferencesIterator iterator, BeanMapper beanMapper)
			throws LBResourceUnavailableException {
		return new PagingList<MapVersionDirectoryEntry>(
				this.buildMapVersionDirectoryEntryIterator(iterator, beanMapper),
				iterator.numberRemaining());
	}

	private Iterator<MapVersionDirectoryEntry> buildMapVersionDirectoryEntryIterator(
			ResolvedConceptReferencesIterator iterator, BeanMapper beanMapper) {
		return new DirectoryEntryIterator<ResolvedConceptReference, MapVersionDirectoryEntry>(
				new ResolvedConceptReferencesIteratorAdapter(iterator),
				MapVersionDirectoryEntry.class, beanMapper);
	}

	@Override
	public MapVersionDirectoryEntry getEntry(int index)
			throws IndexOutOfBoundsException {
		return this.cache.get(index);
	}

	@Override
	public MapVersionDirectoryEntry[] getEntry() {
		return this.cache.toArray(new MapVersionDirectoryEntry[this.cache
				.size()]);
	}

	@Override
	public Iterator<? extends MapVersionDirectoryEntry> iterateEntry() {
		return this.cache.iterator();
	}

	@Override
	public int getEntryCount() {
		return this.cache.size();
	}

}
