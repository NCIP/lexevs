package org.lexgrid.loader.writer;

import java.util.List;

import org.LexGrid.relations.AssociationQualification;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.lexgrid.loader.wrappers.ParentIdHolder;


public class AssociationQualifierWriter extends AbstractParentIdHolderWriter<AssociationQualification> {

	@Override
	public void doWrite(CodingSchemeUriVersionPair codingSchemeId, List<ParentIdHolder<AssociationQualification>> items) {
		for(ParentIdHolder<AssociationQualification> paq: items) {
			
			this.getDatabaseServiceManager().getDaoCallbackService().
		executeInDaoLayer(new DaoCallback<Object>(){

		public Object execute(DaoManager daoManager) {
			String codingSchemeIdInDb = daoManager.getCodingSchemeDao(
					codingSchemeId.getUri(), 
					codingSchemeId.getVersion()).
					getCodingSchemeUIdByUriAndVersion(
							codingSchemeId.getUri(), 
							codingSchemeId.getVersion());
			daoManager.getAssociationDao(
					codingSchemeId.getUri(), 
					codingSchemeId.getVersion())
						.insertAssociationQualifier(codingSchemeIdInDb, paq.getParentId(), paq.getItem());
			return null;
		}
	});	
	}
		
	}

}
