package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;

public class DefaultPagingCodingSchemeInserter extends AbstractPagingCodingSchemeInserter {

    public int entityPageSize = 100;
    public int associationInstancePage = 100;
    
    @Override
    protected void loadNonPagedItems(final CodingScheme codingScheme) {
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<Object>() {

            public Object execute(DaoManager daoManager) {
                daoManager.getCurrentCodingSchemeDao().insertCodingScheme(codingScheme);
                
                return null;
            }
        });
    }

    @Override
    protected void pageEntities(String codingSchemeUri, String codingSchemeVersion, Entities entities) {
        List<Entity> batch = new ArrayList<Entity>();
        
        String codingSchemeId = getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
        
        for(Entity entity : entities.getEntity()) {
            batch.add(entity);
            if(batch.size() >= this.entityPageSize) {
                this.insertEntity(codingSchemeId, batch);
                batch.clear();
            }
        }
        
        if(batch.size() > 0) {
            insertEntity(codingSchemeId, batch);
        }
    }

    @Override
    protected void pageRelations(String codingSchemeUri, String codingSchemeVersion, Relations relations) {
        List<AssociationSourceBatchInsertItem> batch = new ArrayList<AssociationSourceBatchInsertItem>();

        String codingSchemeId = getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
        String relationsId = insertRelations(codingSchemeId, relations);
        for(AssociationPredicate predicate : relations.getAssociationPredicate()) {
            String predicateId = insertAssociationPredicate(codingSchemeId, relationsId, predicate);
            for(AssociationSource source : predicate.getSource()) {
                batch.add(new AssociationSourceBatchInsertItem(predicateId, source));
                if(batch.size() >= this.associationInstancePage) {
                    insertAssociationSource(codingSchemeId, batch);
                    batch.clear();
                }
            }
        }
        
        if(batch.size() > 0) {
            insertAssociationSource(codingSchemeId, batch);
        }
    }
    
    protected void insertEntity(final String codingSchemeId, final List<Entity> batch) {
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
    
                    daoManager.getCurrentEntityDao().insertBatchEntities(codingSchemeId, batch);
                    
                    return null;
            }
        });
    }
    
    protected void insertAssociationSource(final String codingSchemeId, final List<AssociationSourceBatchInsertItem> batch) {
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
    
                    daoManager.getCurrentAssociationDao().insertBatchAssociationSources(codingSchemeId, batch);
                    
                    return null;
            }
        });
    }
    
    protected String insertRelations(final String codingSchemeId, final Relations relations) {
        return
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
                return 
                    daoManager.getCurrentAssociationDao().insertRelations(codingSchemeId, relations);
            }
        });
    }
    
    protected String insertAssociationPredicate(final String codingSchemeId, final String relationsId, final AssociationPredicate predicate) {
        return
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
                return 
                    daoManager.getCurrentAssociationDao().insertAssociationPredicate(codingSchemeId, relationsId, predicate);
            }
        });
    }
    
    protected String getCodingSchemeId(final String uri, final String version) {
        return
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
                return 
                    daoManager.getCodingSchemeDao(uri, version).getCodingSchemeIdByUriAndVersion(uri, version);
            }
        });
    }

    public void setAssociationInstancePageSize(int pageSize) {
        this.associationInstancePage = pageSize;
    }

    public void setEntityPageSize(int pageSize) {
        this.entityPageSize = pageSize;
    }
}
