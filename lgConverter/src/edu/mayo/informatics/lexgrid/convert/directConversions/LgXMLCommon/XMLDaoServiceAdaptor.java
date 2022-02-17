
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.association.AssociationDataService;
import org.lexevs.dao.database.service.association.AssociationService;
import org.lexevs.dao.database.service.association.AssociationTargetService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.error.ErrorCallbackListener;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.database.service.relation.RelationService;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

import edu.mayo.informatics.lexgrid.convert.errorcallback.ErrorCallbackListenerFactory;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class XMLDaoServiceAdaptor {

    LexEvsServiceLocator locator = null;
    DatabaseServiceManager dbManager = null;
    EntityService entityService = null;
    CodingSchemeService codingSchemeService = null;
    AssociationService assocService = null;
    RelationService relationService = null;
    DaoCallbackService daoCallbackService;
    PropertyService propertyService = null;
    PickListDefinitionService pickListService;
    ValueSetDefinitionService valueSetService;
    AuthoringService authoringService;
    AssociationTargetService assocTargetService = null;
    AssociationDataService assocDataService = null;
    
    ArrayList<AssociationPredicate> associationList = null;
    ArrayList<String> relationList = null;
    
    ErrorCallbackListener errorCallbackListener = ErrorCallbackListenerFactory.getErrorCallbackListener();
    
    /**
     * constructor initializes all DAO services
     */
    public XMLDaoServiceAdaptor() {
        locator = LexEvsServiceLocator.getInstance();
        dbManager = locator.getDatabaseServiceManager();
        assocDataService = dbManager.getAssociationDataService();
        authoringService = dbManager.getAuthoringService();
        entityService = dbManager.wrapServiceForErrorHandling(dbManager.getEntityService(), errorCallbackListener);
        codingSchemeService = dbManager.getCodingSchemeService();
        assocService = dbManager.getAssociationService();
        relationService = dbManager.getRelationService();
        associationList = new ArrayList<AssociationPredicate>();
        relationList = new ArrayList<String>();
        daoCallbackService = dbManager.getDaoCallbackService();
        propertyService = dbManager.getPropertyService();
        pickListService = dbManager.getPickListDefinitionService();
        valueSetService = dbManager.getValueSetDefinitionService();
        assocTargetService = dbManager.getAssociationTargetService();
    }

    /**
     * Store a Coding Scheme using the DAO service. Intended to capture
     * the meta data of the coding scheme
     * @param scheme
     * @throws CodingSchemeAlreadyLoadedException
     * @throws LBRevisionException 
     */
    public void storeCodingScheme(CodingScheme scheme) throws CodingSchemeAlreadyLoadedException, LBRevisionException {
        authoringService.loadRevision(scheme, null, null);
    }
    public void storeCodingSchemeSystemReleaseRevision(Revision revision, String releaseId) throws CodingSchemeAlreadyLoadedException, LBRevisionException {
        authoringService.loadRevision(revision, releaseId, null);
    }
    /**
     * Store an Entity using the DAO service
     * @param entity
     * @param c
     */
    public void storeEntity(Entity entity, CodingScheme c) {
        entityService.insertEntity(c.getCodingSchemeURI(), c.getRepresentsVersion(), entity);
    }

    /**
     * Store a coding scheme relation
     * @param codingSchemeUri
     * @param version
     * @param relation
     */
    public void storeRelation(String codingSchemeUri, String version, Relations relation) {
        if (relationList.contains(relation.getContainerName()))
            return;
        relationService.insertRelation(codingSchemeUri, version, relation);
        relationList.add(relation.getContainerName());
    }

    /**
     * Store an association (source)
     * @param uri
     * @param version
     * @param containerName
     * @param associationPredicateName
     * @param source
     */
    public void storeAssociation(String uri, String version, String containerName, String associationPredicateName,
            AssociationSource source) {
        assocService.insertAssociationSource(uri, version, containerName, associationPredicateName, source);
    }

    /**
     * Store an association predicate
     * @param uri
     * @param version
     * @param relationsName
     * @param predicate
     */
    public void storeAssociationPredicate(final String uri, final String version, final String relationsName,
            final AssociationPredicate predicate) {

        daoCallbackService.executeInDaoLayer(new DaoCallback<Object>() {

            public Object execute(DaoManager daoManager) {
                String codingSchemeId = daoManager.getCurrentCodingSchemeDao().getCodingSchemeUIdByUriAndVersion(uri,
                        version);
                String relationsId = daoManager.getCurrentAssociationDao()
                        .getRelationUId(codingSchemeId, relationsName);
                //Adding this step since predicates are not versionable and may be changed in revisions.
               String predicateUId = daoManager.getCurrentAssociationDao().getAssociationPredicateUIdByContainerName(codingSchemeId, relationsName, predicate.getAssociationName());
                if(predicateUId == null)
               daoManager.getCurrentAssociationDao().insertAssociationPredicate(codingSchemeId, relationsId,
                        predicate, true);
                return null;
            }
        });
    }

    /**
     * Store properties.
     * @param codingSchemeUri
     * @param version
     * @param entityCode
     * @param entityCodeNamespace
     * @param property
     */
    public void storeProperty(String codingSchemeUri, String version, String entityCode, String entityCodeNamespace,
            Property property) {
        propertyService.insertEntityProperty(codingSchemeUri, version, entityCode, entityCodeNamespace, property);
    }
    public void storeRevision(String codingSchemeUri, String revsionId,
            Revision revision) throws LBRevisionException {
        authoringService.loadRevision(revision, codingSchemeUri, null);
    }
    
    public void storeSystemRelease( SystemRelease release) throws LBRevisionException {
        authoringService.loadSystemRelease(release, null);
    }
    
    public void storeValueSet(ValueSetDefinition valueSet, String systemReleaseURI, Mappings mappings) throws LBException {
        valueSetService.insertValueSetDefinition(valueSet, systemReleaseURI, mappings);
    }
    public void storeValueSetDefinition(ValueSetDefinition valueSet) throws LBException {
        //valueSetService.insertDefinitionEntry(valueSet, valueSet.getDefinitionEntry(0));
        valueSetService.insertValueSetDefinition(valueSet,null, null);
    }
    public void storePickList(PickListDefinition picklist, String systemReleaseURI, Mappings mappings) throws LBParameterException, LBException {
        pickListService.insertPickListDefinition(picklist, systemReleaseURI, mappings);
    }
    
    public void storePickListDefinition(PickListDefinition picklist) throws LBParameterException, LBException {
        pickListService.insertPickListDefinition(picklist, null, null);
    }
    /**
     * Activate a given coding scheme
     * @param urn
     * @param version
     * @throws LBParameterException
     */
    public void activateScheme(String urn, String version) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference codingScheme = new AbsoluteCodingSchemeVersionReference();
        codingScheme.setCodingSchemeURN(urn);
        codingScheme.setCodingSchemeVersion(version);
        locator.getSystemResourceService().updateCodingSchemeResourceStatus(codingScheme,
                CodingSchemeVersionStatus.ACTIVE);
    }
    
    public void storeRevisionMetaData(final Revision revision) {

        daoCallbackService.executeInDaoLayer(new DaoCallback<Object>() {

            public Object execute(DaoManager daoManager){
                try {
                    daoManager.getRevisionDao().insertRevisionEntry(revision, null);
                } catch (LBRevisionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

        });
    }

    public void storeCodingSchemeRevision(CodingScheme scheme) {
        try {
            codingSchemeService.revise(scheme ,null, null);
        } catch (LBException e) {
            throw new RuntimeException(e);
        }
        
    }

    public void storeEntityRevision(Entity entity, CodingScheme c) {
        try {
            entityService.revise(c.getCodingSchemeURI(), c.getRepresentsVersion(), entity);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public void storeCodingSchemePropertyRevision(Property property, CodingScheme c) {
        try {
            propertyService.reviseCodingSchemeProperty(c.getCodingSchemeURI(), c.getRepresentsVersion(), property);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public void storeRelationsPropertyRevision(String codingSchemeUri, String version,String relationContainerName,
            Property property) {
        try {
            propertyService.reviseRelationProperty(codingSchemeUri, version, relationContainerName, property);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void storeEntityPropertyRevision(String codingSchemeUri, String version, String entityCode, String entityCodeNamespace,
            Property property) {
        try {
            propertyService.reviseEntityProperty(codingSchemeUri, version, entityCode, entityCodeNamespace, property);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void storeRelationsRevision(String URI, String version, Relations relations){
        try {
            if(relationList.contains(relations.getContainerName()))
                return;
            relationService.revise(URI, version, relations);
            relationList.add(relations.getContainerName());
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void storeAssociationRevision( String scheme, String containerName, String associationName, String version, AssociationSource source, AssociationTarget target) {
        try {
            assocTargetService.revise(scheme, containerName , associationName, version, source, target);
        } catch (LBException e) {
            e.printStackTrace();
        }
        
    }
    public void storeAssociatonData(String codingSchemeUri, String associationPredicateName, String relationContainerName, String version, AssociationSource source, AssociationData data){
        try {
            assocDataService.revise(codingSchemeUri, version, relationContainerName, associationPredicateName, source, data);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void storeValueSetDefinitionRevision(ValueSetDefinition vsDefinition){
        try {
            valueSetService.revise(vsDefinition, null, null);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void storePickListDefinitionRevision(PickListDefinition plDefinition){
        try {
            pickListService.revise(plDefinition, null, null);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void storePickListDefinitionSystemReleaseRevision(PickListDefinition plDefinition, Mappings mappings,  String revisionId ) {
        try {
            pickListService.revise(plDefinition, mappings, revisionId);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void storeValueSetDefinitionSystemReleaseRevision(ValueSetDefinition vsDefinition, Mappings mappings,  String revisionId) {
        try {
            valueSetService.revise(vsDefinition, mappings, revisionId);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}