package org.cts2.internal.profile.read;

import org.cts2.association.Association;
import org.cts2.profile.read.AssociationReadService;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

public class LexEvsAssociationReadService extends AbstractBaseReadService<Association> implements AssociationReadService {

	//private AssociationFactory associationFactory;

	@Override
	public boolean exists(NameOrURI entryID, ReadContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsByExternalStatementId(
			NameOrURI assertingCodeSystemVersion, String externalStatementId,
			ReadContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Association readByExternalStatementId(
			NameOrURI assertingCodeSystemVersion, String externalStatementId,
			QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Association read(NameOrURI entryID, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
