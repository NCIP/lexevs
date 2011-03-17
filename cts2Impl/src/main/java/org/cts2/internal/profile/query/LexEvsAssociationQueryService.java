package org.cts2.internal.profile.query;

import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.profile.query.AssociationQueryService;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;

/**
 * The class LexEVSAssociationQuery
 * 
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class LexEvsAssociationQueryService extends 
	AbstractBaseQueryService<AssociationDirectoryURI> 
	implements AssociationQueryService {

	@Override
	public AssociationDirectoryURI getAllAssociations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectory resolve(
			AssociationDirectoryURI codeSystemQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		return codeSystemQueryURI.get(queryControl, readContext, AssociationDirectory.class);
	}

	@Override
	public AssociationList resolveAsList(
			AssociationDirectoryURI codeSystemQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		return codeSystemQueryURI.get(queryControl, readContext, AssociationList.class);
	}

}
