package org.cts2.internal.profile.query;

import java.util.List;

import org.cts2.core.EntityReference;
import org.cts2.profile.query.ValueSetDefinitionQueryService;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;
import org.cts2.valueset.ValueSetDefinitionDirectory;
import org.cts2.valueset.ValueSetDefinitionList;

/**
 * Implementation of CTS2 ValueSetDefinitionQueryService.
 * 
 *	@author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEvsValueSetDefinitionQueryService extends AbstractBaseQueryService<ValueSetDefinitionDirectoryURI> 
	implements ValueSetDefinitionQueryService {

	/*
	 * (non-Javadoc)
	 * @see org.cts2.profile.query.ValueSetDefinitionQueryService#resolve(org.cts2.uri.ValueSetDefinitionDirectoryURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public ValueSetDefinitionDirectory resolve(
			ValueSetDefinitionDirectoryURI valueSetDefinitionDirectoryURI,
			QueryControl queryControl,
			ReadContext readContext) {
		return valueSetDefinitionDirectoryURI.get(queryControl, readContext, ValueSetDefinitionDirectory.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.cts2.profile.query.ValueSetDefinitionQueryService#resolveAsList(org.cts2.uri.ValueSetDefinitionDirectoryURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public ValueSetDefinitionList resolveAsList(
			ValueSetDefinitionDirectoryURI valueSetDefinitionDirectoryURI,
			QueryControl queryControl,
			ReadContext context) {
		return valueSetDefinitionDirectoryURI.get(queryControl, context, ValueSetDefinitionList.class);
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.ValueSetDefinitionQueryService#restrictToEntities(org.cts2.uri.ValueSetDefinitionDirectoryURI, java.util.List)
	 */
	@Override
	public ValueSetDefinitionDirectoryURI restrictToEntities(
			ValueSetDefinitionDirectoryURI valueSetDefinitionDirectoryURI,
			List<EntityReference> entityList) {
		// TODO Auto-generated method stub
		return null;
	}
}
