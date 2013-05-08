package org.cts2.internal.model.uri;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.types.SetOperator;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedMapVersionDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedMapVersionListDirectory;
import org.cts2.internal.model.uri.restrict.IterableBasedResolvingRestrictionHandler;
import org.cts2.map.MapVersionDirectory;
import org.cts2.map.MapVersionList;
import org.cts2.service.core.NameOrURIList;
import org.cts2.uri.MapVersionDirectoryURI;
import org.cts2.uri.restriction.MapVersionDirectoryRestrictionState;
import org.cts2.uri.restriction.SetComposite;


import scala.actors.threadpool.Arrays;

/**
 * 
 * @author <a href="mailto:lian.zonghui@mayo.edu">Zonghui Lian</a>
 * 
 */
public class DefaultMapVersionDirectoryURI
		extends
		AbstractIterableLexEvsBackedResolvingDirectoryURI<CodingSchemeRendering, MapVersionDirectoryURI>
		implements MapVersionDirectoryURI {

	private BeanMapper beanMapper;
	private LexBIGService lexBigService;
	private CodingSchemeRenderingList codingSchemeRenderingList;
	private MapVersionDirectoryRestrictionState restrictionState = new MapVersionDirectoryRestrictionState(); // TODO
																												// double
																												// check
																												// MapVersionDirectoryRestrictionState

	/**
	 * Instantiates a new default map version directory uri.
	 * 
	 * @param lexBigService
	 * @param codingSchemeRenderingList
	 * @param restrictionHandler
	 * @param beanMapper
	 */
	public DefaultMapVersionDirectoryURI(
			LexBIGService lexBigService,
			CodingSchemeRenderingList codingSchemeRenderingList,
			IterableBasedResolvingRestrictionHandler<CodingSchemeRendering, MapVersionDirectoryURI> restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.lexBigService = lexBigService;
		this.beanMapper = beanMapper;
		this.codingSchemeRenderingList = codingSchemeRenderingList;
	}

	@Override
	public MapVersionDirectoryRestrictionState getRestrictionState() {
		return this.restrictionState;
	}

	@Override
	public MapVersionDirectoryURI restrictToCodeSystems(
			MapVersionDirectoryURI directory, NameOrURIList codeSystems) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Iterable<CodingSchemeRendering> getOriginalState() {
		return Arrays.asList(this.codingSchemeRenderingList
				.getCodingSchemeRendering());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <O> O transform(Iterable<CodingSchemeRendering> lexevsObject,
			Class<O> clazz) {
		if (clazz.equals(MapVersionDirectory.class)) {
			try {
				return (O) new ResolvedConceptReferencesIteratorBackedMapVersionDirectory(
						lexevsObject, this.beanMapper);
			} catch (LBException e) {
				throw new RuntimeException(e);
			}
		}
		if (clazz.equals(MapVersionList.class)) {
			try {
				return (O) new ResolvedConceptReferencesIteratorBackedMapVersionListDirectory(
						lexevsObject, this.beanMapper);
			} catch (LBException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
//		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
//		csrl.setCodingSchemeRendering(Iterables.toArray(lexevsObject,
//				CodingSchemeRendering.class));
//
//		return this.beanMapper.map(csrl, clazz);
	}

	@Override
	protected MapVersionDirectoryURI createSetOperatedDirectoryURI(
			SetOperator setOperator, MapVersionDirectoryURI directoryUri1,
			MapVersionDirectoryURI directoryUri2) {
		DefaultMapVersionDirectoryURI newUri = new DefaultMapVersionDirectoryURI(
				this.lexBigService, this.codingSchemeRenderingList,
				this.getRestrictionHandler(), this.beanMapper);

		newUri.getRestrictionState().setSetComposite(
				new SetComposite<MapVersionDirectoryURI>());
		newUri.getRestrictionState().getSetComposite()
				.setDirectoryUri1(directoryUri1);
		newUri.getRestrictionState().getSetComposite()
				.setDirectoryUri2(directoryUri2);

		newUri.getRestrictionState().getSetComposite()
				.setSetOperator(setOperator);

		return newUri;
	}

	@Override
	protected MapVersionDirectoryURI clone() {
		// TODO
		return this;
	}

}
