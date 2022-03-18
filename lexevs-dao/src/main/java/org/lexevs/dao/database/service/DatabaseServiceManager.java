
package org.lexevs.dao.database.service;

import org.lexevs.dao.database.service.association.AssociationDataService;
import org.lexevs.dao.database.service.association.AssociationService;
import org.lexevs.dao.database.service.association.AssociationTargetService;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.error.ErrorCallbackDatabaseServiceFactory;
import org.lexevs.dao.database.service.error.ErrorCallbackListener;
import org.lexevs.dao.database.service.event.registry.ListenerRegistry;
import org.lexevs.dao.database.service.graphdb.GraphingDataBaseService;
import org.lexevs.dao.database.service.ncihistory.NciHistoryService;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.service.relation.RelationService;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.VSPropertyService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyService;
import org.lexevs.dao.database.service.version.AuthoringService;

/**
 * The Class DatabaseServiceManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseServiceManager {
	
	/** The error callback database service factory. */
	private ErrorCallbackDatabaseServiceFactory errorCallbackDatabaseServiceFactory = 
		new ErrorCallbackDatabaseServiceFactory();
	
	/** The listener registry. */
	private ListenerRegistry listenerRegistry;
	
	/** The coding scheme service. */
	private CodingSchemeService codingSchemeService;
	
	/** The entity service. */
	private EntityService entityService;
	
	/** The property service. */
	private PropertyService propertyService;
	
	/** The relation service. */
	private RelationService relationService;
	
	/** The association service. */
	private AssociationService associationService;
	
	/** The associationTarget service. */
	private AssociationTargetService associationTargetService;
	
	/** The associationData service. */
	private AssociationDataService associationDataService;
	
	/** The pick list service. */
	private PickListDefinitionService pickListDefinitionService;
	
	/** The value set definition service. */
	private ValueSetDefinitionService valueSetDefinitionService;
	
	private ValueSetHierarchyService valueSetHierarchyService;
	
	private AssertedValueSetService assertedValueSetService;
	
	/** The vsproperty service. */
	private VSPropertyService vsPropertyService;
	
	/** The authoring service. */
	private AuthoringService authoringService;
	
	/** The coded node graph service. */
	private CodedNodeGraphService codedNodeGraphService;
	
	/** The coded node graph service. */
	private GraphingDataBaseService graphingDatabaseService;
	
	/** The nci history service. */
	private NciHistoryService nciHistoryService;
	
	/** The dao callback service. */
	private DaoCallbackService daoCallbackService;
	
	/**
	 * Wrap service for error handling.
	 * 
	 * @param service the service
	 * @param errorCallbackListener the error callback listener
	 * 
	 * @return the t
	 */
	public <T> T wrapServiceForErrorHandling(T service, ErrorCallbackListener errorCallbackListener) {
		return errorCallbackDatabaseServiceFactory.
			getErrorCallbackDatabaseService(service, errorCallbackListener);
	}
	
	/**
	 * Sets the coding scheme service.
	 * 
	 * @param codingSchemeService the new coding scheme service
	 */
	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	/**
	 * Gets the coding scheme service.
	 * 
	 * @return the coding scheme service
	 */
	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

	/**
	 * Sets the entity service.
	 * 
	 * @param entityService the new entity service
	 */
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	/**
	 * Gets the entity service.
	 * 
	 * @return the entity service
	 */
	public EntityService getEntityService() {
		return entityService;
	}

	/**
	 * Gets the property service.
	 * 
	 * @return the property service
	 */
	public PropertyService getPropertyService() {
		return propertyService;
	}
	
	/**
	 * Sets the property service.
	 * 
	 * @param propertyService the new property service
	 */
	public void setPropertyService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	/**
	 * Sets the association service.
	 * 
	 * @param associationService the new association service
	 */
	public void setAssociationService(AssociationService associationService) {
		this.associationService = associationService;
	}
	
	/**
	 * Gets the association service.
	 * 
	 * @return the association service
	 */
	public AssociationService getAssociationService() {
		return associationService;
	}
	
	/**
	 * Sets the dao callback service.
	 * 
	 * @param daoCallbackService the new dao callback service
	 */
	public void setDaoCallbackService(DaoCallbackService daoCallbackService) {
		this.daoCallbackService = daoCallbackService;
	}

	/**
	 * Gets the dao callback service.
	 * 
	 * @return the dao callback service
	 */
	public DaoCallbackService getDaoCallbackService() {
		return daoCallbackService;
	}
	
	/**
	 * Gets the authoring service.
	 * 
	 * @return the authoring service
	 */
	public AuthoringService getAuthoringService() {
		return authoringService;
	}

	/**
	 * Sets the authoring service.
	 * 
	 * @param authoringService the new authoring service
	 */
	public void setAuthoringService(AuthoringService authoringService) {
		this.authoringService = authoringService;
	}

	/**
	 * Sets the pick list definition service.
	 * 
	 * @param pickListDefinitionService the new pick list definition service
	 */
	public void setPickListDefinitionService(PickListDefinitionService pickListDefinitionService) {
		this.pickListDefinitionService = pickListDefinitionService;
	}

	/**
	 * Gets the pick list definition service.
	 * 
	 * @return the pick list definition service
	 */
	public PickListDefinitionService getPickListDefinitionService() {
		return pickListDefinitionService;
	}

	/**
	 * Gets the value set definition service.
	 * 
	 * @return the valueSetDefinitionService
	 */
	public ValueSetDefinitionService getValueSetDefinitionService() {
		return valueSetDefinitionService;
	}

	/**
	 * Sets the value set definition service.
	 * 
	 * @param valueSetDefinitionService the valueSetDefinitionService to set
	 */
	public void setValueSetDefinitionService(
			ValueSetDefinitionService valueSetDefinitionService) {
		this.valueSetDefinitionService = valueSetDefinitionService;
	}

	/**
	 * Gets the vs property service.
	 * 
	 * @return the vsPropertyService
	 */
	public VSPropertyService getVsPropertyService() {
		return vsPropertyService;
	}

	/**
	 * Sets the vs property service.
	 * 
	 * @param vsPropertyService the vsPropertyService to set
	 */
	public void setVsPropertyService(VSPropertyService vsPropertyService) {
		this.vsPropertyService = vsPropertyService;
	}

	public ErrorCallbackDatabaseServiceFactory getErrorCallbackDatabaseServiceFactory() {
		return errorCallbackDatabaseServiceFactory;
	}

	public void setErrorCallbackDatabaseServiceFactory(
			ErrorCallbackDatabaseServiceFactory errorCallbackDatabaseServiceFactory) {
		this.errorCallbackDatabaseServiceFactory = errorCallbackDatabaseServiceFactory;
	}

	public ValueSetHierarchyService getValueSetHierarchyService() {
		return valueSetHierarchyService;
	}

	public void setValueSetHierarchyService(ValueSetHierarchyService valueSetHierarchyService) {
		this.valueSetHierarchyService = valueSetHierarchyService;
	}

	/**
	 * @return the assertedValueSetService
	 */
	public AssertedValueSetService getAssertedValueSetService() {
		return assertedValueSetService;
	}

	/**
	 * @param assertedValueSetService the assertedValueSetService to set
	 */
	public void setAssertedValueSetService(AssertedValueSetService assertedValueSetService) {
		this.assertedValueSetService = assertedValueSetService;
	}

	/**
	 * Sets the coded node graph service.
	 * 
	 * @param codedNodeGraphService the new coded node graph service
	 */
	public void setCodedNodeGraphService(CodedNodeGraphService codedNodeGraphService) {
		this.codedNodeGraphService = codedNodeGraphService;
	}

	/**
	 * Gets the coded node graph service.
	 * 
	 * @return the coded node graph service
	 */
	public CodedNodeGraphService getCodedNodeGraphService() {
		return codedNodeGraphService;
	}


	/**
	 * Gets the relation service.
	 * 
	 * @return the relationService
	 */
	public RelationService getRelationService() {
		return relationService;
	}

	/**
	 * Sets the relation service.
	 * 
	 * @param relationService the relationService to set
	 */
	public void setRelationService(RelationService relationService) {
		this.relationService = relationService;
	}

	/**
	 * Gets the association target service.
	 * 
	 * @return the associationTargetService
	 */
	public AssociationTargetService getAssociationTargetService() {
		return associationTargetService;
	}

	/**
	 * Sets the association target service.
	 * 
	 * @param associationTargetService the associationTargetService to set
	 */
	public void setAssociationTargetService(
			AssociationTargetService associationTargetService) {
		this.associationTargetService = associationTargetService;
	}

	/**
	 * Gets the association data service.
	 * 
	 * @return the associationDataService
	 */
	public AssociationDataService getAssociationDataService() {
		return associationDataService;
	}

	/**
	 * Sets the association data service.
	 * 
	 * @param associationDataService the associationDataService to set
	 */
	public void setAssociationDataService(
			AssociationDataService associationDataService) {
		this.associationDataService = associationDataService;
	}

	/**
	 * @return the graphingDatabaseService
	 */
	public GraphingDataBaseService getGraphingDatabaseService() {
		return graphingDatabaseService;
	}

	/**
	 * @param graphingDatabaseService the graphingDatabaseService to set
	 */
	public void setGraphingDatabaseService(GraphingDataBaseService graphingDatabaseService) {
		this.graphingDatabaseService = graphingDatabaseService;
	}

	/**
	 * Gets the nci history service.
	 * 
	 * @return the nci history service
	 */
	public NciHistoryService getNciHistoryService() {
		return nciHistoryService;
	}

	/**
	 * Sets the nci history service.
	 * 
	 * @param nciHistoryService the new nci history service
	 */
	public void setNciHistoryService(NciHistoryService nciHistoryService) {
		this.nciHistoryService = nciHistoryService;
	}

	/**
	 * Sets the listener registry.
	 * 
	 * @param listenerRegistry the new listener registry
	 */
	public void setListenerRegistry(ListenerRegistry listenerRegistry) {
		this.listenerRegistry = listenerRegistry;
	}

	/**
	 * Gets the listener registry.
	 * 
	 * @return the listener registry
	 */
	public ListenerRegistry getListenerRegistry() {
		return listenerRegistry;
	}
}