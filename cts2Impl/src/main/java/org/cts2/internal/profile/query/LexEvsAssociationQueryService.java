package org.cts2.internal.profile.query;

import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.core.TargetExpression;
import org.cts2.internal.model.uri.DefaultAssociationDirectoryURI;
import org.cts2.profile.query.AssociationQueryService;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;
import org.cts2.uri.EntityDirectoryURI;

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
	public AssociationDirectory resolve(
			AssociationDirectoryURI associationQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		return associationQueryURI.get(queryControl, readContext, AssociationDirectory.class);
	}

	@Override
	public AssociationList resolveAsList(
			AssociationDirectoryURI associationQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		return associationQueryURI.get(queryControl, readContext, AssociationList.class);
	}

	@Override
	public EntityDirectoryURI getAllSourceAndTargetEntities(
			EntityDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityDirectoryURI getPredicates(AssociationDirectoryURI directory,
			QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityDirectoryURI getSourceEntities(
			AssociationDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityDirectoryURI getTargetEntities(
			AssociationDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectoryURI restrictToCodeSystemVersion(
			AssociationDirectoryURI directory, NameOrURI codeSystemVersion) {
		return null;
	}

	@Override
	public AssociationDirectoryURI restrictToPredicate(
			AssociationDirectoryURI directory, EntityNameOrURI predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectoryURI restrictToSourceEntity(
			AssociationDirectoryURI directory, EntityNameOrURI sourceEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectoryURI restrictToSourceOrTargetEntity(
			AssociationDirectoryURI directory, EntityNameOrURI entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectoryURI restrictToTargetEntity(
			AssociationDirectoryURI directory, EntityNameOrURI target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectoryURI restrictToTargetExpression(
			AssociationDirectoryURI directory, TargetExpression target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectoryURI restrictToTargetLiteral(
			AssociationDirectoryURI directory, String target) {
		// TODO Auto-generated method stub
		return null;
	}






}
