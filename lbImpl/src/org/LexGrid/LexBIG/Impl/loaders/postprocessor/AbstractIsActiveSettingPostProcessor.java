/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.loaders.postprocessor;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;

/**
 * The Class AbstractIsActiveSettingPostProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractIsActiveSettingPostProcessor extends AbstractExtendable implements LoaderPostProcessor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2828520523031693573L;
    
    /** The page size. */
    private int pageSize = 1000;

    /**
     * Register.
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws LBException the LB exception
     */
    public void register() throws LBParameterException, LBException {
        ExtensionRegistryImpl.instance().registerGenericExtension(
                super.getExtensionDescription());
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor#runPostProcess(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
     */
    /**
     * Run post process.
     * 
     * @param reference the reference
     */
    public void runPostProcess(AbsoluteCodingSchemeVersionReference reference) {
        EntityIterator itr = new EntityIterator(
                reference.getCodingSchemeURN(), reference.getCodingSchemeVersion(), pageSize);
        
        for(Entity entity : itr) {
            if(shouldIsActiveBeUpdated(entity)) {
                entity.setIsActive(getNewIsActive(entity));
                updateEntity(entity);
            }
        }
    }
    
    /**
     * Update entity.
     * 
     * @param entity the entity
     */
    private void updateEntity(Entity entity) {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /**
     * Should is isActive flag be be updated.
     * 
     * @param entity the entity
     * 
     * @return true, if successful
     */
    protected abstract boolean shouldIsActiveBeUpdated(Entity entity);
    
    /**
     * Gets the new isActive value.
     * 
     * @param entity the entity
     * 
     * @return the new is active
     */
    protected abstract Boolean getNewIsActive(Entity entity);

    /**
     * Gets the page size.
     * 
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size.
     * 
     * @param pageSize the new page size
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /**
     * The Class EntityIterator.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class EntityIterator extends AbstractPageableIterator<Entity> {
        
        /**
         * 
         */
        private static final long serialVersionUID = 5064022428583560362L;

        /** The uri. */
        private String uri;
        
        /** The version. */
        private String version;
        
        /**
         * Instantiates a new entity iterator.
         * 
         * @param uri the uri
         * @param version the version
         * @param pageSize the page size
         */
        private EntityIterator(String uri, String version, int pageSize) {
            super(pageSize);
            this.uri = uri;
            this.version = version;
        }

        /** The entity service. */
        EntityService entityService = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();
        
        /* (non-Javadoc)
         * @see org.lexevs.paging.AbstractPageableIterator#doPage(int, int)
         */
        @Override
        protected List<? extends Entity> doPage(int currentPosition, int pageSize) {
            return entityService.getEntities(uri, version, currentPosition, pageSize);
        }  
    }
}