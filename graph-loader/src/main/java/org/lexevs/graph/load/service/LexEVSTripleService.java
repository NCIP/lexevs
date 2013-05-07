package org.lexevs.graph.load.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;

public class LexEVSTripleService {

	private DatabaseServiceManager databaseServiceManager;
	private LexEvsServiceLocator service;
	private Map<String, String> predicateToAnonStatusMap;

	public LexEVSTripleService(String codingSchemeUri, String version) {
		service = LexEvsServiceLocator.getInstance();
		databaseServiceManager = service.getDatabaseServiceManager();
		predicateToAnonStatusMap = initPredicateAnonStatusMap(codingSchemeUri, version);
	}

	public Map<String, String> getPredicateToAnonStatusMap() {
		return predicateToAnonStatusMap;
	}

	public void setPredicateToAnonStatusMap(
			Map<String, String> predicateToAnonStatusMap) {
		this.predicateToAnonStatusMap = predicateToAnonStatusMap;
	}
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

	public List<String> getAssociationPredicateIds(
			final String codingSchemeUri, final String version) {

		return databaseServiceManager.getDaoCallbackService()
				.executeInDaoLayer(new DaoCallback<List<String>>() {

					@Override
					public List<String> execute(DaoManager daoManager) {

						CodingSchemeDao codingSchemeDao = daoManager
								.getCurrentCodingSchemeDao();
						AssociationDao associationDao = daoManager
								.getCurrentAssociationDao();

						String codingSchemeId = codingSchemeDao
								.getCodingSchemeUIdByUriAndVersion(
										codingSchemeUri, version);

						List<String> relationsIds = associationDao
								.getRelationsUIdsForCodingSchemeUId(codingSchemeId);

						List<String> associationPredicateIds = new ArrayList<String>();
						for (String relationsId : relationsIds) {
							associationPredicateIds.addAll(associationDao
									.getAssociationPredicateUIdsForRelationsUId(
											codingSchemeId, relationsId));
						}
						return associationPredicateIds;
					}
				});
	}

	protected List<String> getAssociationNameforPredicateId(
			final String codingSchemeUri, final String version,
			final String predicateId) {
		return databaseServiceManager.getDaoCallbackService()
				.executeInDaoLayer(new DaoCallback<List<String>>() {

					@Override
					public List<String> execute(DaoManager daoManager) {
						AssociationDao associationDao = daoManager
								.getCurrentAssociationDao();
						CodingSchemeDao codingSchemeDao = daoManager
								.getCodingSchemeDao(codingSchemeUri, version);
						String codingSchemeId = codingSchemeDao
								.getCodingSchemeUIdByUriAndVersion(
										codingSchemeUri, version);
						List<String> list = new ArrayList<String>();
						list.add(associationDao
								.getAssociationPredicateNameForUId(
										codingSchemeId, predicateId));
						return list;
					}
				});
	}
	
	protected String getAnonStatusForPredicateId(final String codingSchemeUri, final String version,
			final String predicateId){
		return databaseServiceManager.getDaoCallbackService()
				.executeInDaoLayer(new DaoCallback<String>() {

					@Override
					public String execute(DaoManager daoManager) {
						AssociationDao associationDao = daoManager
								.getCurrentAssociationDao();
						CodingSchemeDao codingSchemeDao = daoManager
								.getCodingSchemeDao(codingSchemeUri, version);
						String codingSchemeId = codingSchemeDao
								.getCodingSchemeUIdByUriAndVersion(
										codingSchemeUri, version);

						return associationDao.getAnonDesignationForPredicate(
								codingSchemeId, predicateId);

					}
				});
		
	}
	
	public static class GraphIterator extends AbstractPageableIterator<GraphDbTriple>{

		private static final long serialVersionUID = -8462823912578300117L;

		private static final int DEFAULT_PAGE_SIZE = 1000;

		private DatabaseServiceManager databaseServiceManager;
		private String codingSchemeUri;
		private String version;
		private String associationPredicateId;

		public GraphIterator(DatabaseServiceManager databaseServiceManager,
				String codingSchemeUri, String version) {
			this(databaseServiceManager, codingSchemeUri, version, DEFAULT_PAGE_SIZE);
		}

		public GraphIterator(DatabaseServiceManager databaseServiceManager,
				String codingSchemeUri, String version, int pageSize) {

			super(pageSize);
			this.codingSchemeUri = codingSchemeUri;
			this.version = version;
			this.databaseServiceManager = databaseServiceManager;
		}
		
		protected List<String> getAssociationNameforPredicateId(
				final String codingSchemeUri, final String version,
				final String predicateId) {
			return databaseServiceManager.getDaoCallbackService()
					.executeInDaoLayer(new DaoCallback<List<String>>() {

						@Override
						public List<String> execute(DaoManager daoManager) {
							AssociationDao associationDao = daoManager
									.getCurrentAssociationDao();
							CodingSchemeDao codingSchemeDao = daoManager
									.getCodingSchemeDao(codingSchemeUri, version);
							String codingSchemeId = codingSchemeDao
									.getCodingSchemeUIdByUriAndVersion(
											codingSchemeUri, version);
							List<String> list = new ArrayList<String>();
							list.add(associationDao
									.getAssociationPredicateNameForUId(
											codingSchemeId, predicateId));
							return list;
						}
					});
		}
		
		@Override
		protected List<GraphDbTriple> doPage(int currentPosition,
				int pageSize) {
			return getTriples(codingSchemeUri, version,
					currentPosition, pageSize);
		}

		
		private List<GraphDbTriple> getTriples(
				final String codingSchemeUri,
				final String version,
				final int start, final int pageSize) {
			final List<String> guids = databaseServiceManager.getDaoCallbackService()
					.executeInDaoLayer(new DaoCallback<List<String>>() {

						@Override
						public List<String> execute(DaoManager daoManager) {
							String codingSchemeId = daoManager
									.getCurrentCodingSchemeDao()
									.getCodingSchemeUIdByUriAndVersion(
											codingSchemeUri, version);

							return daoManager.getCurrentAssociationDao()
									.getAllEntityAssocToEntityGuidsOfCodingScheme(
											codingSchemeId,
											associationPredicateId, start,
											pageSize);
						}
					});
			if(guids.size() <= 0){
				return new ArrayList<GraphDbTriple>();
			}
			return databaseServiceManager.getDaoCallbackService()
					.executeInDaoLayer(new DaoCallback<List<GraphDbTriple>>() {

						@Override
						public List<GraphDbTriple> execute(DaoManager daoManager) {
							String codingSchemeId = daoManager
									.getCurrentCodingSchemeDao()
									.getCodingSchemeUIdByUriAndVersion(
											codingSchemeUri, version);

							return daoManager.getCurrentAssociationDao()
									.getAllGraphDbTriplesOfCodingScheme(
											codingSchemeId,guids);
						}
					});
			
			
		}
		
	}

	public String getPredicateName(String codingSchemeUri, String version,
			String predicateId) {

		List<String> list = getAssociationNameforPredicateId(codingSchemeUri,
				version, predicateId);
		String result = list.get(0);
		if (result.startsWith("[")) {
			result.replaceFirst("[", "");
		}
		if (result.endsWith("]")) {
			result.subSequence(0, result.length() - 1);
		}
		return result;
	}
	
	public GraphIterator getGraphIterator(String codingSchemeUri,
	String version) {
		return new GraphIterator(databaseServiceManager, codingSchemeUri,
		version);
	}
	
	private Map<String, String> initPredicateAnonStatusMap(String uri, String version){
		List<String> predicateIds = getAssociationPredicateIds(uri, version);
		Map<String, String> predAnonMap = new HashMap<String, String>();
		for(String s: predicateIds){
			predAnonMap.put(s, getAnonStatusForPredicateId(uri, version, s));
		}
		return predAnonMap;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String uri = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl";
		String version = "12.01f";
		LexEVSTripleService service = new LexEVSTripleService(uri, version);
		long start = System.currentTimeMillis();
		List<String> predicateIds = service.getAssociationPredicateIds(uri,
				version);
		long predidtime = System.currentTimeMillis();
		System.out.println("time getting predicates: " + ((predidtime - start)/1000));
		long startiterator = System.currentTimeMillis();
		int count = 0;
//		for (String s : predicateIds) {
//			System.out.println("predicate id: " + s);

//			GraphTripleIterator triples = new GraphTripleIterator(
//					service.getDatabaseServiceManager(), uri, version, "35");
//			for (GraphDbTriple t : triples) {
//
////				System.out.println("\n New Triple");
////				System.out.println(t.getSourceEntityCode());
////				System.out.println(t.getSourceEntityNamespace());
////				System.out.println(t.getTargetEntityCode());
////				System.out.println(t.getTargetEntityNamespace());
////				System.out.println(t.getAssciationName());
////				System.out.println(t.getAssociationInstanceId());
////
////				System.out.println(t.getAssociationQualification());
////				System.out.println(t.getEntityAssnsGuid());
//				count++;
//				if(count % 10000 == 0){
//				System.out.println("Triples loaded: " + count);
//				long end = System.currentTimeMillis();
//				System.out.println("Load time: " + ((end-start)/1000));
//				}
//			}
//		}
		GraphIterator triples = new GraphIterator(service.databaseServiceManager, uri, version);
			while(triples.hasNext()){
				GraphDbTriple t = triples.next();
				count++;
				if(count % 10000 == 0){
				System.out.println("Triples loaded: " + count);
				long end = System.currentTimeMillis();
				System.out.println("Load time: " + ((end-start)/1000));}
			}
//			System.out.println("predicate name: "
//					+ service.getPredicateName(uri, version, s));
			System.out.println("triple count " + count);
			long forPred = System.currentTimeMillis();
			System.out.println("Time for this predicate: " + ((forPred - startiterator)/1000));
//	}
		long end = System.currentTimeMillis();
		System.out.println("Total Iterator loop: " + ((end - startiterator)/1000));
	}
			


}
