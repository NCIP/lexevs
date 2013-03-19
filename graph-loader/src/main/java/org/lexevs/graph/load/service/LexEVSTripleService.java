package org.lexevs.graph.load.service;

import java.util.List;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;



public class LexEVSTripleService {


	private DatabaseServiceManager databaseServiceManager;
	private LexEvsServiceLocator service;

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}


	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}


	public LexEvsServiceLocator getService() {
		return service;
	}


	public void setService(LexEvsServiceLocator service) {
		this.service = service;
	}



	public LexEVSTripleService(){
		service = LexEvsServiceLocator.getInstance();
		databaseServiceManager = service.getDatabaseServiceManager();
	}

	
	protected List<String> getTransitiveAssociationPredicateIds(
			final String codingSchemeUri, 
			final String version){

		return databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<String>>(){

			@Override
			public List<String> execute(DaoManager daoManager) {

				CodingSchemeDao codingSchemeDao = daoManager.getCurrentCodingSchemeDao();
				AssociationDao associationDao = daoManager.getCurrentAssociationDao();			

				String codingSchemeId = codingSchemeDao.
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

				List<String> relationsIds = associationDao
				.getRelationsUIdsForCodingSchemeUId(codingSchemeId);

				List<String> associationPredicateIds = null;
				for(String relationsId : relationsIds) {
					associationPredicateIds = 
						associationDao.getAssociationPredicateUIdsForRelationsUId(codingSchemeId, relationsId);
				}
				return associationPredicateIds;
			}
		});
	}	
	
	
public static class TripleIterator extends AbstractPageableIterator<Triple>{

		private static final long serialVersionUID = -4390395000937078077L;
		
		private static final int DEFAULT_PAGE_SIZE = 1000;
		
		private DatabaseServiceManager databaseServiceManager;
		private String codingSchemeUri; 
		private String version; 
		private String associationPredicateId;

		public TripleIterator(
				DatabaseServiceManager databaseServiceManager,
				String codingSchemeUri, 
				String version, 
				String associationPredicateId) {
			this(databaseServiceManager, codingSchemeUri, version, associationPredicateId, DEFAULT_PAGE_SIZE);
		}
		
		public TripleIterator(
				DatabaseServiceManager databaseServiceManager,
				String codingSchemeUri, 
				String version, 
				String associationPredicateId, 
				int pageSize) {
			
			super(pageSize);
			this.codingSchemeUri = codingSchemeUri;
			this.version = version;
			this.associationPredicateId = associationPredicateId;
			this.databaseServiceManager = databaseServiceManager;
		}
		
		@Override
		protected List<Triple> doPage(int currentPosition, int pageSize) {
			return getTriples(codingSchemeUri, version, associationPredicateId, currentPosition, pageSize);
		}
		
		protected List<Triple> getTriples(
				final String codingSchemeUri,
				final String version,
				final String associationPredicateId, 
				final int start, 
				final int pageSize){
			return databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<Triple>>(){

				@Override
				public List<Triple> execute(DaoManager daoManager) {
					String codingSchemeId = daoManager.getCurrentCodingSchemeDao().
					getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

					return daoManager.getCurrentAssociationDao().
					getAllTriplesOfCodingScheme(codingSchemeId, associationPredicateId, start, pageSize);
				}
			});
		}
	}

	public TripleIterator getTripleIteratorforPredicate(String codingSchemeUri, 
				String version, 
				String associationPredicateId ){
		return new TripleIterator(databaseServiceManager, codingSchemeUri, version, associationPredicateId);
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
