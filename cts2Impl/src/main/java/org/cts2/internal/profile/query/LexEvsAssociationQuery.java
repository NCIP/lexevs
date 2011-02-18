package org.cts2.internal.profile.query;

import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.profile.query.AssociationQuery;
import org.cts2.uri.DirectoryURI;

/**
 * The class LexEVSAssociationQuery
 * 
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class LexEvsAssociationQuery extends AbstractBaseQueryService<AssociationDirectory> implements AssociationQuery{

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.AssociationQuery#getAllAssociations()
	 */
	@Override
	public DirectoryURI<AssociationDirectory> getAllAssociations() {
		return this.getDirectoryURIFactory().getDirectoryURI();
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.AssociationQuery#resolveAsList(org.cts2.uri.DirectoryURI)
	 */
	@Override
	public AssociationList resolveAsList(
			DirectoryURI<AssociationDirectory> directoryUri) {
		// TODO Auto-generated method stub
		return null;
	}

}
