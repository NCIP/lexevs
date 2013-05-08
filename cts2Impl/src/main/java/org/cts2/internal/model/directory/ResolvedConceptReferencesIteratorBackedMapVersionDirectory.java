package org.cts2.internal.model.directory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
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
			Iterable<CodingSchemeRendering> csrIterator, BeanMapper beanMapper)
			throws LBException {

		this.cache = this.buildCacheList(csrIterator, beanMapper);
	}

	private List<MapVersionDirectoryEntry> buildCacheList(
			Iterable<CodingSchemeRendering> csrIterator, BeanMapper beanMapper)
			throws LBResourceUnavailableException {

		return new PagingList<MapVersionDirectoryEntry>(
				this.buildMapVersionDirectoryEntryIterator(csrIterator,
						beanMapper), Iterators.size(csrIterator.iterator()));
	}

	private Iterator<MapVersionDirectoryEntry> buildMapVersionDirectoryEntryIterator(
			Iterable<CodingSchemeRendering> csrIterator, BeanMapper beanMapper) {
		return new DirectoryEntryIterator<CodingSchemeRendering, MapVersionDirectoryEntry>(
				csrIterator.iterator(), MapVersionDirectoryEntry.class,
				beanMapper);
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
