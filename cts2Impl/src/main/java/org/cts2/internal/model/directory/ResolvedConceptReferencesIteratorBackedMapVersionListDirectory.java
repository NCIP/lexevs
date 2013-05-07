package org.cts2.internal.model.directory;

import java.util.ArrayList;
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
import org.cts2.map.MapVersionList;
import org.cts2.map.MapVersionListEntry;

import com.google.common.collect.Iterators;

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
			Iterable<CodingSchemeRendering> csrIterator, BeanMapper beanMapper)
			throws LBException {
		this.cache = this.buildCacheList(csrIterator, beanMapper);
	}

	private List<MapVersionListEntry> buildCacheList(
			Iterable<CodingSchemeRendering> csrIterator, BeanMapper beanMapper)
			throws LBResourceUnavailableException {
		return new PagingList<MapVersionListEntry>(
				this.buildMapVersionListEntryIterator(csrIterator, beanMapper),
				Iterators.size(csrIterator.iterator()));
	}

	private Iterator<MapVersionListEntry> buildMapVersionListEntryIterator(
			Iterable<CodingSchemeRendering> csrIterator, BeanMapper beanMapper) {
		return new DirectoryEntryIterator<CodingSchemeRendering, MapVersionListEntry>(
				csrIterator.iterator(), MapVersionListEntry.class, beanMapper);
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
