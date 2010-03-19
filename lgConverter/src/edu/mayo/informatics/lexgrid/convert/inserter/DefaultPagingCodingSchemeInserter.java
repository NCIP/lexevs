/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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

/**
 * The Class DefaultPagingCodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultPagingCodingSchemeInserter extends AbstractPagingCodingSchemeInserter {

    /** The entity page size. */
    public int entityPageSize = 100;
    
    /** The association instance page. */
    public int associationInstancePage = 100;
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractPagingCodingSchemeInserter#loadNonPagedItems(org.LexGrid.codingSchemes.CodingScheme)
     */
    @Override
    protected void loadNonPagedItems(final CodingScheme codingScheme) {
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<Object>() {

            public Object execute(DaoManager daoManager) {
                daoManager.getCurrentCodingSchemeDao().insertCodingScheme(codingScheme);
                
                return null;
            }
        });
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractPagingCodingSchemeInserter#pageEntities(java.lang.String, java.lang.String, org.LexGrid.concepts.Entities)
     */
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

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractPagingCodingSchemeInserter#pageRelations(java.lang.String, java.lang.String, org.LexGrid.relations.Relations)
     */
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
    
    /**
     * Insert entity.
     * 
     * @param codingSchemeId the coding scheme id
     * @param batch the batch
     */
    protected void insertEntity(final String codingSchemeId, final List<Entity> batch) {
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
    
                    daoManager.getCurrentEntityDao().insertBatchEntities(codingSchemeId, batch);
                    
                    return null;
            }
        });
    }
    
    /**
     * Insert association source.
     * 
     * @param codingSchemeId the coding scheme id
     * @param batch the batch
     */
    protected void insertAssociationSource(final String codingSchemeId, final List<AssociationSourceBatchInsertItem> batch) {
        super.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

            public String execute(DaoManager daoManager) {
    
                    daoManager.getCurrentAssociationDao().insertBatchAssociationSources(codingSchemeId, batch);
                    
                    return null;
            }
        });
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
                    daoManager.getCurrentAssociationDao().insertRelations(codingSchemeId, relations);
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
                    daoManager.getCurrentAssociationDao().insertAssociationPredicate(codingSchemeId, relationsId, predicate);
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
                    daoManager.getCodingSchemeDao(uri, version).getCodingSchemeIdByUriAndVersion(uri, version);
            }
        });
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
}
