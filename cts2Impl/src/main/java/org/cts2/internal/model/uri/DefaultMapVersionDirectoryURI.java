package org.cts2.internal.model.uri;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.core.types.SetOperator;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedMapVersionDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedMapVersionListDirectory;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.map.MapVersion;
import org.cts2.map.MapVersionDirectory;
import org.cts2.map.MapVersionList;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.EntityDirectoryURI;
import org.cts2.uri.MapVersionDirectoryURI;
import org.cts2.uri.restriction.MapVersionDirectoryRestrictionState;
import org.cts2.uri.restriction.RestrictionState;
import org.cts2.uri.restriction.SetComposite;

public class DefaultMapVersionDirectoryURI
		extends
		AbstractNonIterableLexEvsBackedResolvingDirectoryURI<CodingSchemeRenderingList, MapVersionDirectoryURI>
		implements MapVersionDirectoryURI {

	/** The coded node set. */
	// private CodedNodeSet codedNodeSet;

	/** The bean mapper. */
	private BeanMapper beanMapper;

	private LexBIGService lexBigService;

	private MapVersionDirectoryRestrictionState mapVersionDirectoryRestrictionState = new MapVersionDirectoryRestrictionState();

	public DefaultMapVersionDirectoryURI(
			LexBIGService lexBigService,
			NonIterableBasedResolvingRestrictionHandler<CodingSchemeRenderingList, MapVersionDirectoryURI> restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.lexBigService = lexBigService;
		this.beanMapper = beanMapper;
	}

	@Override
	public MapVersionDirectoryRestrictionState getRestrictionState() {
		return this.mapVersionDirectoryRestrictionState;
	}

	@Override
	protected CodingSchemeRenderingList getOriginalState() {
		CodingSchemeRenderingList codingSchemeRenderingList = new CodingSchemeRenderingList();
		try {
			MappingExtension mappingExtension = (MappingExtension) this.lexBigService
					.getGenericExtension("MappingExtension");
			for (CodingSchemeRendering csr : this.lexBigService
					.getSupportedCodingSchemes().getCodingSchemeRendering()) {
				if (mappingExtension.isMappingCodingScheme(csr
						.getCodingSchemeSummary().getCodingSchemeURI(),
						Constructors
								.createCodingSchemeVersionOrTagFromVersion(csr
										.getCodingSchemeSummary()
										.getRepresentsVersion()))) {
					codingSchemeRenderingList.addCodingSchemeRendering(csr);
				}
			}
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException(e);
		} catch (LBException e) {
			throw new RuntimeException(e);
		}

		return codingSchemeRenderingList;
	}

	private List<Mapping> getMappingList(
			CodingSchemeRenderingList codingSchemeRenderingList)
			throws LBException {
		List<Mapping> list = new ArrayList<Mapping>();
		MappingExtension mappingExtension = (MappingExtension) this.lexBigService
				.getGenericExtension("MappingExtension");
		for (CodingSchemeRendering csr : codingSchemeRenderingList
				.getCodingSchemeRendering()) {
			list.add(mappingExtension.getMapping(csr.getCodingSchemeSummary()
					.getCodingSchemeURI(), Constructors
					.createCodingSchemeVersionOrTagFromVersion(csr
							.getCodingSchemeSummary().getRepresentsVersion()),
					null));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <O> O transform(CodingSchemeRenderingList lexevsObject,
			Class<O> clazz) {
		try {
			List<Mapping> list = this.getMappingList(lexevsObject);
			if (clazz.equals(MapVersionDirectory.class)) {
				return (O) new ResolvedConceptReferencesIteratorBackedMapVersionDirectory(
						list, this.beanMapper);
			}
			if (clazz.equals(MapVersionList.class)) {
				return (O) new ResolvedConceptReferencesIteratorBackedMapVersionListDirectory(
						list, this.beanMapper);
			}
		} catch (LBException e) {
			// TODO: real cts2 exception here
			throw new IllegalStateException();
		}

		// TODO: real cts2 exception here
		throw new IllegalStateException();
	}

	@Override
	protected MapVersionDirectoryURI clone() {
		return this;
	}

	@Override
	protected int doCount(ReadContext readContext) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected MapVersionDirectoryURI createSetOperatedDirectoryURI(
			SetOperator setOperator, MapVersionDirectoryURI directoryUri1,
			MapVersionDirectoryURI directoryUri2) {
		// TODO Auto-generated method stub
		return null;


	}

}
