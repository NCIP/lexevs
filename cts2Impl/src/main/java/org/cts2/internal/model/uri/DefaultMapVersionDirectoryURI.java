package org.cts2.internal.model.uri;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.types.SetOperator;
import org.cts2.entity.EntityDirectory;
import org.cts2.entity.EntityList;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedEntityDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedEntityList;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedMapVersionDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedMapVersionListDirectory;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.map.MapVersion;
import org.cts2.map.MapVersionList;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.EntityDirectoryURI;
import org.cts2.uri.MapVersionDirectoryURI;
import org.cts2.uri.restriction.EntityDirectoryRestrictionState;
import org.cts2.uri.restriction.MapVersionDirectoryRestrictionState;
import org.cts2.uri.restriction.RestrictionState;
import org.cts2.uri.restriction.SetComposite;

public class DefaultMapVersionDirectoryURI
		extends
		AbstractNonIterableLexEvsBackedResolvingDirectoryURI<Mapping, MapVersionDirectoryURI>
		implements MapVersionDirectoryURI {

	/** The coded node set. */
	// private CodedNodeSet codedNodeSet;

	/** The bean mapper. */
	private BeanMapper beanMapper;

	private LexBIGService lexBigService;

	private MapVersionDirectoryRestrictionState mapVersionDirectoryRestrictionState 
				= new MapVersionDirectoryRestrictionState();

	public DefaultMapVersionDirectoryURI(LexBIGService lexBigService,
			NonIterableBasedResolvingRestrictionHandler<Mapping, MapVersionDirectoryURI> restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.lexBigService = lexBigService;
		this.beanMapper = beanMapper;
	}

	@Override
	public RestrictionState<? extends DirectoryURI> getRestrictionState() {
		return this.mapVersionDirectoryRestrictionState;
	}

	@Override
	protected Mapping getOriginalState() {
		return ProfileUtils.getMapping(lexBigService);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <O> O transform(Mapping lexevsObject, Class<O> clazz) {
		try {
			if(clazz.equals(MapVersion.class)){
				return (O) new ResolvedConceptReferencesIteratorBackedMapVersionDirectory(lexevsObject, this.beanMapper);
			}
			if(clazz.equals(MapVersionList.class)){
				return (O) new ResolvedConceptReferencesIteratorBackedMapVersionListDirectory(lexevsObject, this.beanMapper);
			}
		} catch (LBException e) {
			//TODO: real cts2 exception here
			throw new IllegalStateException();
		}
		
		//TODO: real cts2 exception here
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

		DefaultEntityDirectoryURI uri = new DefaultMapVersionDirectoryURI(
				this.lexBigService, this.getRestrictionHandler(),
				this.beanMapper);

		uri.getRestrictionState().setSetComposite(
				new SetComposite<EntityDirectoryURI>());
		uri.getRestrictionState().getSetComposite().setSetOperator(setOperator);
		uri.getRestrictionState().getSetComposite()
				.setDirectoryUri1(directoryUri1);
		uri.getRestrictionState().getSetComposite()
				.setDirectoryUri2(directoryUri2);

		return uri;
	}

}
