package org.lexevs.dao.database.operation.transitivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationEntity;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.paging.AbstractPageableIterator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.KnownTags;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.service.SystemResourceService;

public class DefaultTransitivityBuilder implements TransitivityBuilder {

	private DatabaseServiceManager databaseServiceManager;

	private SystemResourceService systemResourceService;

	private Registry registry;

	private LgLoggerIF logger;

	public void computeTransitivityTable(String codingSchemeUri, String version) {

		List<String> transitiveAssociations = this.getTransitiveAssociationPredicateIds(codingSchemeUri, version);

		for (String associationPredicateId : transitiveAssociations) {
			// make a hashset the holds the entire current set of
			// relations.        

			String sourceECNS = null;
			String sourceEC = null;
			String targetECNS = null;
			String targetEC = null;

			LRUMap insertedCache = new LRUMap(50000);

			TripleIterator tripleIterator = new TripleIterator(codingSchemeUri, version, associationPredicateId);

			for(Triple triple : tripleIterator) {
				sourceECNS = triple.getSourceEntityNamespace();
				sourceEC = triple.getSourceEntityCode();
				targetECNS = triple.getTargetEntityNamespace();
				targetEC = triple.getTargetEntityCode();

				if (!sourceEC.equals("@") && !targetEC.equals("@@"))
				{
					StringTriple sourceCode = new StringTriple();                            
					sourceCode.a = sourceECNS;
					sourceCode.c = sourceEC;
					StringTriple targetCode = new StringTriple();                            
					targetCode.a = targetECNS;
					targetCode.c = targetEC;

					insertIntoTransitiveClosure(
							codingSchemeUri, 
							version, 
							sourceEC, 
							sourceECNS,
							targetEC, 
							targetECNS,
							associationPredicateId, 
							insertedCache);
				}
			}  

			// get the unique source codes for this relationship - and
			// get all of the codes.
			logger.info("ComputeTransitive - Processing " + associationPredicateId);     

			List<Node> distinctSourceTriples = getDistinctSourceTriples(codingSchemeUri, version, associationPredicateId);

			ArrayList<StringTriple> sourceCodes = new ArrayList<StringTriple>();
			sourceECNS = null;
			sourceEC = null;
			targetECNS = null;
			targetEC = null;
			for (Node sourceNode : distinctSourceTriples) {
				sourceECNS = sourceNode.getEntityCodeNamespace();
				sourceEC = sourceNode.getEntityCode();
				if (!sourceEC.equals("@"))
				{
					StringTriple temp = new StringTriple();

					temp.a = sourceECNS;
					temp.c = sourceEC;
					sourceCodes.add(temp);
				}

				// Now I have all of the top source codes for this
				// relationship. Need to recurse down the
				// tree
				// adding nodes to the transitive table as necessary.

				for (int j = 0; j < sourceCodes.size(); j++) {

					List<Node> targetNodes = getTargetTriples(
							codingSchemeUri, 
							version, 
							associationPredicateId,
							sourceNode.getEntityCode(),
							sourceNode.getEntityCodeNamespace());

					ArrayList<StringTriple> targetCodes = new ArrayList<StringTriple>();
					sourceECNS = null;
					sourceEC = null;
					targetECNS = null;
					targetEC = null;
					for(Node targetNode : targetNodes) {
						targetECNS = targetNode.getEntityCodeNamespace();
						targetEC = targetNode.getEntityCode();
						if (!targetEC.equals("@@"))
						{
							StringTriple temp = new StringTriple();
							temp.a = targetECNS;
							temp.c = targetEC;    
							targetCodes.add(temp);
						}
					}

					processTransitive(codingSchemeUri, version, associationPredicateId,
							sourceCodes.get(j), targetCodes, insertedCache);
				}
			}
		}
	}

	private List<Node> getTargetTriples(
			final String codingSchemeUri, 
			final String version,
			final String associationPredicateUid, 
			final String sourceEntityCode,
			final String sourceEntityCodeNamespace) {
		return databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<Node>>(){

			@Override
			public List<Node> execute(DaoManager daoManager) {
				String codingSchemeUid = daoManager.getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

				return daoManager.getCodedNodeGraphDao(codingSchemeUri, version).
				getTargetNodesForSource(codingSchemeUid, associationPredicateUid, sourceEntityCode, sourceEntityCodeNamespace);
			}
		});
	}

	private List<Node> getDistinctSourceTriples(
			final String codingSchemeUri, 
			final String version, 
			final String associationPredicateUid) {
		return databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<Node>>(){

			@Override
			public List<Node> execute(DaoManager daoManager) {
				String codingSchemeUid = daoManager.getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

				return daoManager.getCodedNodeGraphDao(codingSchemeUri, version).
				getDistinctSourceNodesForAssociationPredicate(codingSchemeUid, associationPredicateUid);
			}

		});
	}

	private void processTransitive(
			String codingSchemeUri, 
			String codingSchemeVersion, 
			String associationPredicateUid, 
			StringTriple sourceCode,
			ArrayList<StringTriple> targetCodes, 
			LRUMap insertedCache) {
		// The next target of each of the passed in targetCodes needs to be
		// added to the transitive table.

		for (int i = 0; i < targetCodes.size(); i++) {

			ArrayList<StringTriple> targetTargets = new ArrayList<StringTriple>();
			String targetECNS = null;
			String targetEC = null;

			List<Node> targetNodes = getTargetTriples(
					codingSchemeUri, 
					codingSchemeVersion, 
					associationPredicateUid, 
					targetCodes.get(i).a, 
					targetCodes.get(i).c);
			for(Node targetNode : targetNodes) {
				targetECNS = targetNode.getEntityCodeNamespace();
				targetEC =  targetNode.getEntityCode();
				if (!targetEC.equals("@@"))
				{
					StringTriple temp = new StringTriple();
					temp.a = targetECNS;
					temp.c = targetEC;

					targetTargets.add(temp);
				}
			}

			// need to add an entry for the source code to each of these target
			// codes (if it doesn't already
			// exist, and if there isn't an entry in the regular table already

			for (int j = 0; j < targetTargets.size(); j++) {
				if (sourceCode.a.equals(targetTargets.get(j).a)
						&& sourceCode.c.equals(targetTargets.get(j).c)) {
					// if they equal each other, there is something wrong with
					// the code system. But I don't
					// want to fail.. so skip it.

					continue;
				}

				boolean iInserted = insertIntoTransitiveClosure(
						codingSchemeUri, 
						codingSchemeVersion, 
						associationPredicateUid, 
						sourceCode, 
						targetTargets.get(j), 
						insertedCache);               
				if (!iInserted) {
					// If I didn't insert it into the transitive table, it was
					// already there
					// or unnecessary. No need to do the recursion below, so
					// remove it.
					targetTargets.remove(j);
					j--;
				}
			}

			// Now, need to recurse.
			while (targetTargets.size() > 0) {
				if (sourceCode.a.equals( targetTargets.get(0).a)
						// && sourceCode.b.equals((
						// targetTargets.get(0)).b)
						&& sourceCode.c.equals( targetTargets.get(0).c)) {
					// if they equal each other, there is something wrong with
					// the code system. But I don't
					// want to fail.. so skip it.
					targetTargets.remove(0);
					continue;
				}

				// need to pass in an array list - put the current item in one.
				ArrayList<StringTriple> temp = new ArrayList<StringTriple>();
				temp.add(targetTargets.get(0));
				// remove it, since we will be done with it after this.
				targetTargets.remove(0);
				processTransitive(codingSchemeUri, codingSchemeVersion, associationPredicateUid, sourceCode, temp, insertedCache);
			}
		}
	}

	private boolean insertIntoTransitiveClosure(String codingSchemeUri, String codingSchemeVersion,
			String associationPredicateId, StringTriple sourceCode, StringTriple targetCode, LRUMap insertedCache) {
		String key = sourceCode.a + ":" + sourceCode.c + ":" + targetCode.a + ":" + targetCode.c;

		boolean iInserted = false;

		if (!insertedCache.containsKey(key)) {
			// if it is not loaded in the main table, or already loaded
			// in the transitive table


			insertedCache.put(key, null);
			iInserted = true;

		}
		return iInserted;
	}

	protected void insertIntoTransitiveClosure(
			final String codingSchemeUri, 
			final String version, 
			final String sourceCode,
			final String sourceNamespace,
			final String targetCode, 
			final String targetNamespace, 
			final String associationPredicateId,
			Map cache) {
		this.databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<Object>(){

			@Override
			public Object execute(DaoManager daoManager) {
				String codingSchemeId = daoManager.getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

				daoManager.getAssociationDao(codingSchemeUri, version).
				insertIntoTransitiveClosure(
						codingSchemeId, 
						associationPredicateId, 
						sourceCode, 
						sourceNamespace, 
						targetCode, 
						targetNamespace);
				return null;
			}

		});
	}

	private class StringTriple {
		String a;
		String b;
		String c;
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
				String codingSchemeId = daoManager.getCodingSchemeDao(codingSchemeUri, version).
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

				return daoManager.getAssociationDao(codingSchemeUri, version).
				getAllTriplesOfCodingScheme(codingSchemeId, associationPredicateId, start, pageSize);
			}

		});
	}

	protected List<String> getTransitiveAssociationPredicateIds(
			final String codingSchemeUri, 
			final String version){

		return databaseServiceManager.getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<String>>(){

			@Override
			public List<String> execute(DaoManager daoManager) {
				List<String> transitivePredicateIds = new ArrayList<String>();

				CodingSchemeDao codingSchemeDao = daoManager.getCodingSchemeDao(codingSchemeUri, version);
				AssociationDao associationDao = daoManager.getAssociationDao(codingSchemeUri, version);			

				String codingSchemeId = codingSchemeDao.
				getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

				Mappings mappings = codingSchemeDao.getMappings(codingSchemeId);;

				List<String> relationsIds = associationDao
				.getRelationsUIdsForCodingSchemeUId(codingSchemeId);

				for(String relationsId : relationsIds) {
					List<String> associatinPredicateIds = 
						associationDao.getAssociationPredicateIdsForRelationsId(codingSchemeId, relationsId);

					for(String associationPredicateId : associatinPredicateIds) {
						String associationName = associationDao.getAssociationPredicateNameForId(codingSchemeId, associationPredicateId);

						SupportedAssociation sa = getSupportedAssociationWithName(mappings, associationName);

						RegistryEntry entry = getRegistryEntryForCodingSchemeName(sa.getCodingScheme());

						String entityCode = sa.getEntityCode();
						String namespace = sa.getEntityCodeNamespace();

						boolean isTransitive = isTransitive(entry.getResourceUri(), entry.getResourceVersion(), entityCode, namespace);

						if(isTransitive) {
							transitivePredicateIds.add(associationPredicateId);
						}
					}
				}

				return transitivePredicateIds;
			}
		});
	}

	protected SupportedAssociation getSupportedAssociationWithName(Mappings mappings, String associationName) {
		for(SupportedAssociation sa : mappings.getSupportedAssociation()) {
			if(sa.getLocalId().equals(associationName)) {
				return sa;
			}
		}
		return null;
	}

	protected boolean isTransitive(String codingSchemeUri, String version, String code, String namespace) {
		AssociationEntity associationEntity = 
			this.databaseServiceManager.getEntityService().
			getAssociationEntity(codingSchemeUri, version, code, namespace);
		return (associationEntity != null && associationEntity.getIsTransitive());
	}

	protected RegistryEntry getRegistryEntryForCodingSchemeName(String codingSchemeName) {
		try {
			String uri = this.systemResourceService.getUriForUserCodingSchemeName(codingSchemeName);

			List<RegistryEntry> entries = 
				this.registry.getAllRegistryEntriesOfTypeAndURI(ResourceType.CODING_SCHEME, uri);

			if(entries.size() == 0) {
				return entries.get(0);
			} else {
				//try to find the production scheme
				for(RegistryEntry entry : entries) {
					if(StringUtils.isNotBlank(entry.getTag()) &&
							entry.getTag().equals(KnownTags.PRODUCTION.toString())) {
						return entry;
					}
				}

				//if there isn't one marked production, just pick the first one
				return entries.get(0);
			}
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public LgLoggerIF getLogger() {
		return logger;
	}

	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

	private class TripleIterator extends AbstractPageableIterator<Triple>{

		private String codingSchemeUri; 
		private String version; 
		private String associationPredicateId;

		private TripleIterator(String codingSchemeUri, String version, String associationPredicateId) {
			super(1000);
			this.codingSchemeUri = codingSchemeUri;
			this.version = version;
			this.associationPredicateId = associationPredicateId;
		}
		@Override
		protected List<Triple> doPage(int currentPosition, int pageSize) {
			return getTriples(codingSchemeUri, version, associationPredicateId, currentPosition, pageSize);
		}

	}
}
