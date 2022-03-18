
package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.apache.commons.beanutils.BeanUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;

import edu.mayo.informatics.lexgrid.convert.inserter.error.AssociationSourceBatchInsertError;
import edu.mayo.informatics.lexgrid.convert.inserter.error.EntityBatchInsertError;
import edu.mayo.informatics.lexgrid.convert.inserter.error.AssociationSourceBatchInsertError.AssociationSourceBatchInsertErrorItem;
import edu.mayo.informatics.lexgrid.convert.inserter.error.EntityBatchInsertError.EntityBatchInsertErrorItem;
import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.exception.LoadValidationException;
import edu.mayo.informatics.lexgrid.convert.validator.processor.AllFatalResolverProcessor;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ResolverProcessor;

/**
 * The Class DefaultPagingCodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultPagingCodingSchemeInserter extends AbstractPagingCodingSchemeInserter {
    
    private ResolverProcessor resolverProcessor = new AllFatalResolverProcessor();

    /** The entity page size. */
    public int entityPageSize = 100;
    
    /** The association instance page. */
    public int associationInstancePage = 100;
    
    public DefaultPagingCodingSchemeInserter(){
        super();
    }
    
    public DefaultPagingCodingSchemeInserter(ResolverProcessor resolverProcessor) {
        super();
        this.resolverProcessor = resolverProcessor;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractPagingCodingSchemeInserter#loadNonPagedItems(org.LexGrid.codingSchemes.CodingScheme)
     */
    @Override
    protected List<LoadValidationError> loadNonPagedItems(final CodingScheme codingScheme) {
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        try {
            doLoadNonPagedItems(codingScheme);
        } catch (LoadValidationException e) {
            errors.add(e.getLoadValidationError());
        }
        return errors;
    }

    @SuppressWarnings("deprecation")
    protected void doLoadNonPagedItems(final CodingScheme codingScheme) throws LoadValidationException { 
        CodingScheme onlyNonPagedItems;
        try {
            onlyNonPagedItems = (CodingScheme)BeanUtils.cloneBean(codingScheme);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
        
        onlyNonPagedItems.setEntities(null);
        onlyNonPagedItems.setRelationsAsReference(new ArrayList<Relations>());
        
        try {
            super.getDatabaseServiceManager().getAuthoringService().loadRevision(onlyNonPagedItems, null, null);
        } catch (LBRevisionException e) {
           throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractPagingCodingSchemeInserter#pageEntities(java.lang.String, java.lang.String, org.LexGrid.concepts.Entities)
     */
    @Override
    protected List<LoadValidationError> pageEntities(String codingSchemeUri, String codingSchemeVersion, Entities entities) {
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        List<Entity> batch = new ArrayList<Entity>();
        
        String codingSchemeId = getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
        
        for(AssociationEntity associationEntity : entities.getAssociationEntity()) {
            batch.add(associationEntity);
            if(batch.size() >= this.entityPageSize) {
                try {
                    this.insertEntityBatch(codingSchemeUri, codingSchemeVersion, codingSchemeId, batch);
                } catch (LoadValidationException e) {
                    errors.add(e.getLoadValidationError());
                }
                batch.clear();
            }
        }
        
        for(Entity entity : entities.getEntity()) {
            batch.add(entity);
            if(batch.size() >= this.entityPageSize) {
                try {
                    this.insertEntityBatch(codingSchemeUri, codingSchemeVersion, codingSchemeId, batch);
                } catch (LoadValidationException e) {
                    errors.add(e.getLoadValidationError());
                }
                batch.clear();
            }
        }
        
        if(batch.size() > 0) {
            try {
                insertEntityBatch(codingSchemeUri, codingSchemeVersion, codingSchemeId, batch);
            } catch (LoadValidationException e) {
                errors.add(e.getLoadValidationError());
            }
        }
        return errors;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractPagingCodingSchemeInserter#pageRelations(java.lang.String, java.lang.String, org.LexGrid.relations.Relations)
     */
    @Override
    protected List<LoadValidationError> pageRelations(String codingSchemeUri, String codingSchemeVersion, Relations relations) {
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        List<AssociationSourceBatchInsertItem> batch = new ArrayList<AssociationSourceBatchInsertItem>();

        String codingSchemeId = getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
        String relationsId = insertRelations(codingSchemeId, relations);
        for(AssociationPredicate predicate : relations.getAssociationPredicate()) {
            String predicateId = insertAssociationPredicate(codingSchemeId, relationsId, predicate);
            for(AssociationSource source : predicate.getSource()) {
                batch.add(new AssociationSourceBatchInsertItem(predicateId, source));
                if(batch.size() >= this.associationInstancePage) {
                    try {
                        insertAssociationSourceBatch(codingSchemeId, batch);
                    } catch (LoadValidationException e) {
                        errors.add(e.getLoadValidationError());
                    }
                    batch.clear();
                }
            }
        }
        
        if(batch.size() > 0) {
            try {
                insertAssociationSourceBatch(codingSchemeId, batch);
            } catch (LoadValidationException e) {
                errors.add(e.getLoadValidationError());
            }
        }
        
        return errors;
    }
    
    /**
     * Insert entity.
     * 
     * @param codingSchemeId the coding scheme id
     * @param batch the batch
     */
    protected void insertEntityBatch(String codingSchemeUri, String codingSchemeVersion, final String codingSchemeId, final List<Entity> batch) throws LoadValidationException {
        try {
            super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

                public String execute(DaoManager daoManager) {
   
                        daoManager.getCurrentEntityDao().
                            insertBatchEntities(
                                    codingSchemeId, 
                                    batch,
                                    true);
                        
                        return null;
                }
            });
        } catch (Exception e) {
            EntityBatchInsertErrorItem errorItem = new EntityBatchInsertErrorItem(codingSchemeUri, codingSchemeVersion, codingSchemeId, new ArrayList<Entity>(batch));
            
            throw new LoadValidationException(new EntityBatchInsertError(errorItem, e));
        }
    }
    
    /**
     * Insert association source.
     * 
     * @param codingSchemeId the coding scheme id
     * @param batch the batch
     */
    protected void insertAssociationSourceBatch(final String codingSchemeId, final List<AssociationSourceBatchInsertItem> batch) throws LoadValidationException {
        try {
            super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

                public String execute(DaoManager daoManager) {
   
                        daoManager.getCurrentAssociationDao().
                            insertBatchAssociationSources(
                                    codingSchemeId, 
                                    batch);
                        
                        return null;
                }
            });
        } catch (Exception e) {
            AssociationSourceBatchInsertErrorItem errorItem = new AssociationSourceBatchInsertErrorItem(codingSchemeId, new ArrayList<AssociationSourceBatchInsertItem>(batch));
        
            throw new LoadValidationException(new AssociationSourceBatchInsertError(errorItem, e));
        }
    }
    
    /**
     * Insert relations.
     * 
     * @param codingSchemeId the coding scheme id
     * @param relations the relations
     * 
     * @return the string
     */
    protected String insertRelations(final String codingSchemeId, final Relations relations) {
        return
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
                return 
                    daoManager.getCurrentAssociationDao().
                        insertRelations(
                                codingSchemeId, 
                                relations,
                                false);
            }
        });
    }
    
    /**
     * Insert association predicate.
     * 
     * @param codingSchemeId the coding scheme id
     * @param relationsId the relations id
     * @param predicate the predicate
     * 
     * @return the string
     */
    protected String insertAssociationPredicate(final String codingSchemeId, final String relationsId, final AssociationPredicate predicate) {
        return
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
                return 
                    daoManager.getCurrentAssociationDao().
                        insertAssociationPredicate(
                                codingSchemeId, 
                                relationsId, 
                                predicate,
                                false);
            }
        });
    }
    
    /**
     * Gets the coding scheme id.
     * 
     * @param uri the uri
     * @param version the version
     * 
     * @return the coding scheme id
     */
    protected String getCodingSchemeId(final String uri, final String version) {
        return
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
                return 
                    daoManager.getCurrentCodingSchemeDao().getCodingSchemeUIdByUriAndVersion(uri, version);
            }
        });
    }
   
    protected List<ResolvedLoadValidationError> doResolveErrors(List<LoadValidationError> errors){
        return resolverProcessor.resolve(errors);
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.PagingCodingSchemeInserter#setAssociationInstancePageSize(int)
     */
    public void setAssociationInstancePageSize(int pageSize) {
        this.associationInstancePage = pageSize;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.PagingCodingSchemeInserter#setEntityPageSize(int)
     */
    public void setEntityPageSize(int pageSize) {
        this.entityPageSize = pageSize;
    }

    public void setResolverProcessor(ResolverProcessor resolverProcessor) {
        this.resolverProcessor = resolverProcessor;
    }

    public ResolverProcessor getResolverProcessor() {
        return resolverProcessor;
    }
}