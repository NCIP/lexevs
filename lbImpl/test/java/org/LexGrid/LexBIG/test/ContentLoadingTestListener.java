/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.test;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.BaseLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContents;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.test.InMemoryLuceneDirectoryCreator;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The Class ContentLoadingTestListener.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ContentLoadingTestListener extends AbstractTestExecutionListener {

    private static final String CONTENT_LOADED = "contentLoaded";
 
    private enum ContentScope {METHOD, GLOBAL}
    
    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        super.afterTestClass(testContext);
        
        boolean contentLoaded = this.getContentLoaded(testContext, ContentScope.GLOBAL);
        if(contentLoaded) {
            this.cleanDatabase();
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        super.afterTestMethod(testContext);
        
        boolean contentLoaded = this.getContentLoaded(testContext, ContentScope.METHOD);
        if(contentLoaded) {
            this.cleanDatabase();
        }
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {        
        super.beforeTestClass(testContext);
        boolean contentLoaded = this.loadContent(testContext.getTestClass());
        
        this.setContentLoaded(testContext, contentLoaded, ContentScope.GLOBAL);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        super.beforeTestMethod(testContext);
        
        boolean contentLoaded = this.loadContent(testContext.getTestMethod());
        
        this.setContentLoaded(testContext, contentLoaded, ContentScope.METHOD);
    }
    
    private void setContentLoaded(TestContext testContext, boolean loaded, ContentScope scope){
        testContext.setAttribute(CONTENT_LOADED + scope.toString(), loaded);
    }
    
    private boolean getContentLoaded(TestContext testContext, ContentScope scope){
        Object value = testContext.getAttribute(CONTENT_LOADED + scope.toString());
        if(value == null){
            return false;
        } else {
            return (Boolean) value;
        }
    }
 
    protected boolean loadContent(Class<?> clazz) {
        List<LoadContent> contents = new ArrayList<LoadContent>();
        
        if(clazz.isAnnotationPresent(LoadContent.class)){
            contents.add(clazz.getAnnotation(LoadContent.class));
        }

        LoadContents loadContents = clazz.getAnnotation(LoadContents.class);
        if (loadContents != null) {
            contents.addAll(Arrays.asList(loadContents.value()));
        }
        
        return this.doLoadContent(contents);
    }
    
    protected boolean loadContent(Method method) {
        List<LoadContent> contents = new ArrayList<LoadContent>();
        
        if(method.isAnnotationPresent(LoadContent.class)){
            contents.add(method.getAnnotation(LoadContent.class));
        }

        LoadContents loadContents = method.getAnnotation(LoadContents.class);
        if (loadContents != null) {
            contents.addAll(Arrays.asList(loadContents.value()));
        }
        
        return this.doLoadContent(contents);
    }
    
    protected boolean doLoadContent(List<LoadContent> contents) {
        if (CollectionUtils.isEmpty(contents)) {
            return false;
        } else {
            LexEvsDatabaseOperations dbOps = LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations();
            try {
                dbOps.createAllTables();
            } catch (Exception e) {
                //
            }

            for (LoadContent content : contents) {
                if (content != null) {
                    try {
                        load(content);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return true;
        }
    }

    private void cleanDatabase() {
        try {
            LexEvsDatabaseOperations dbOps = LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations();

            DataSource ds = LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().getDataSource();

            this.clearDatabase(ds);

            dbOps.createAllTables();

            InMemoryLuceneDirectoryCreator.clearAll();
            
            LexEvsServiceLocator.getInstance().getSystemResourceService().refresh();
        } catch (Exception e) {
            //
        }
    }

    /**
     * Load.
     * 
     * @param content
     *            the content
     * @throws Exception
     *             the exception
     */
    protected void load(LoadContent content) throws Exception {
        String[] paths = StringUtils.split(content.contentPath(), ',');

        for (String path : paths) {
            LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

            Resource resource = this.getResource(path);

            Loader loader = lbsm.getLoader(content.loader());

            loader.getOptions().getBooleanOption(BaseLoader.ASYNC_OPTION).setOptionValue(false);

            loader.load(resource.getURI());

            assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
            assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

            try{
                lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

                lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
            } catch(Exception e){
                //error activating -- it could be a ValueSetDefinition.
            }
        }
    }

    /**
     * Inits the url handler.
     * 
     * @param path
     *            the path
     * @return the resource
     */
    public Resource getResource(String path) {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(LexEvsServiceLocator.getInstance()
                .getSystemResourceService().getClassLoader());

        return resourceLoader.getResource(path);
    }

    public void clearDatabase(DataSource ds) throws Exception {
        Connection connection = null;
        try {
            connection = ds.getConnection();
            try {
                Statement stmt = connection.createStatement();
                try {
                    stmt.execute("DROP SCHEMA PUBLIC CASCADE");
                    connection.commit();
                } finally {
                    stmt.close();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new Exception(e);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
