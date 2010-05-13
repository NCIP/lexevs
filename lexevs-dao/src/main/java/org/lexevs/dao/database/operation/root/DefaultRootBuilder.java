package org.lexevs.dao.database.operation.root;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.RootOrTail;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.util.Assert;

public class DefaultRootBuilder implements RootBuilder {
	
	private static String MULTI_ASSN_ROOT_PREDICATE_NAME = "-multi-assn-@-root-";
	private static String ROOT_NODE = "@";
	private static String TAIL_NODE = "@@";

	private DatabaseServiceManager databaseServiceManager;
	
	private SystemResourceService systemResourceService;

	@Override
	public void addRootRelationNode(
			String codingSchemeUri,
			String codingSchemeVersion, 
			List<String> associationNames,
			String relationContainerName, 
			RootOrTail rootOrTail) {
		List<ConceptReference> refs;
		
		if(rootOrTail.equals(RootOrTail.ROOT)) {
			refs = databaseServiceManager.getCodedNodeGraphService().
			getRootConceptReferences(
					codingSchemeUri, 
					codingSchemeVersion, 
					relationContainerName, 
					associationNames);
		} else {
			refs = databaseServiceManager.getCodedNodeGraphService().
			getTailConceptReferences(
					codingSchemeUri, 
					codingSchemeVersion, 
					relationContainerName, 
					associationNames);
		}
		
		if(CollectionUtils.isNotEmpty(refs)) {
			if(CollectionUtils.isEmpty(associationNames)){
				associationNames = databaseServiceManager.getCodedNodeGraphService().
					getAssociationPredicateNamesForCodingScheme(
							relationContainerName,
							codingSchemeUri, 
							codingSchemeVersion);
			}
		} else {
			return;
		}
		
		if(associationNames.size() > 1) {
			databaseServiceManager.getAssociationService().
			insertAssociationPredicate(
					codingSchemeUri, 
					codingSchemeVersion, 
					relationContainerName, 
					createMultiAssociationPredicate());
		}
		
		AssociationSource source = buildAssociationSource(
				codingSchemeUri, 
				codingSchemeVersion,
				rootOrTail);
		
		for(ConceptReference ref : refs) {
			source.addTarget(this.buildAssociationTarget(ref));
		}

		databaseServiceManager.getAssociationService().
			insertAssociationSource(
					codingSchemeUri, 
					codingSchemeVersion, 
					relationContainerName, 
					getAssociationPredicateName(associationNames), 
					source);

	}
	
	private String getAssociationPredicateName(List<String> associations) {
		Assert.notEmpty(associations);
		
		if(associations.size() > 1) {
			return MULTI_ASSN_ROOT_PREDICATE_NAME;
		} else {
			return associations.get(0);
		}
	}
	
	private AssociationPredicate createMultiAssociationPredicate() {
		AssociationPredicate predicate = new AssociationPredicate();
		predicate.setAssociationName(MULTI_ASSN_ROOT_PREDICATE_NAME);
		
		return predicate;
	}
	
	private AssociationTarget buildAssociationTarget(ConceptReference ref) {
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode(ref.getCode());
		target.setTargetEntityCodeNamespace(ref.getCodeNamespace());
		
		return target;
	}
	
	private AssociationSource buildAssociationSource(
			String codingSchemeUri, 
			String codingSchemeVersion,
			RootOrTail rootOrTail) {
		String namespace;
		try {
			namespace = this.systemResourceService.
				getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, codingSchemeVersion);
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode(rootOrTail.equals(RootOrTail.ROOT)? ROOT_NODE : TAIL_NODE);
		source.setSourceEntityCodeNamespace(namespace);
		
		return source;
	}
	

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}
}
