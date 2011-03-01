package org.cts2.internal.profile.query;

import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.profile.query.AssociationQueryService;
import org.cts2.uri.AssociationDirectoryURI;

/**
 * The class LexEVSAssociationQuery
 * 
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class LexEvsAssociationQueryService extends 
	AbstractDirectoryResolvableQueryService<AssociationDirectoryURI, AssociationDirectory, AssociationList> 
	implements AssociationQueryService {

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.AssociationQuery#getAllAssociations()
	 */
	@Override
	public AssociationDirectoryURI getAllAssociations() {
		return this.getDirectoryURIFactory().getDirectoryURI();
	}
}
