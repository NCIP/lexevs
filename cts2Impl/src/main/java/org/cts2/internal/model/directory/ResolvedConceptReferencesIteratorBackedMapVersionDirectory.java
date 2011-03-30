package org.cts2.internal.model.directory;

import java.util.ArrayList;
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

import com.google.common.collect.Iterators;

/**
 * The class ResolvedConceptReferencesIteratorBackedMapVersionDirectory
 * 
 * @author <a href="mailto:lian.zonghui@mayo.edu">Zonghui Lian</a>
 * 
 */
public class ResolvedConceptReferencesIteratorBackedMapVersionDirectory extends
		MapVersionDirectory {

	private static final long serialVersionUID = -7474745464435213892L;
	private List<MapVersionDirectoryEntry> cache;

	public ResolvedConceptReferencesIteratorBackedMapVersionDirectory(
			List<Mapping> mappingList, BeanMapper beanMapper)
			throws LBException {
		List<ResolvedConceptReferencesIterator> list = new ArrayList<ResolvedConceptReferencesIterator>();
		for (Mapping mapping : mappingList) {
			list.add(mapping.resolveMapping());
		}

		this.cache = this.buildCacheList(list, beanMapper);
	}

	private List<MapVersionDirectoryEntry> buildCacheList(
			List<ResolvedConceptReferencesIterator> list, BeanMapper beanMapper)
			throws LBResourceUnavailableException {
		Iterator<MapVersionDirectoryEntry> allIterator = null;
		int counter = 0;
		for (ResolvedConceptReferencesIterator iterator : list) {
			Iterator<MapVersionDirectoryEntry> i = this
					.buildMapVersionDirectoryEntryIterator(iterator, beanMapper);
			counter = counter + iterator.numberRemaining();
			allIterator = Iterators.concat(allIterator, i);
		}
		return new PagingList<MapVersionDirectoryEntry>(allIterator, counter);

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
