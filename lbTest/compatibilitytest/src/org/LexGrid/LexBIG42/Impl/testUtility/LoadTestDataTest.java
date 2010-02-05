/*
 * Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG42.Impl.testUtility;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.NCIHistoryLoader;
import org.LexGrid.LexBIG.Extensions.Load.NCI_MetaThesaurusLoader;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Extensions.Load.UMLS_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.dataAccess.WriteLockManager;
import org.LexGrid.LexBIG.Impl.loaders.HL7LoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.NCIMetaThesaurusLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OBOLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.UMLSLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.junit.Test;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.textui.TestRunner;
import test.helper.classloader.BackwardsCompatibilityClassLoader;

/**
 * This set of tests loads the necessary data for the full suite of JUnit tests.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoadTestDataTest extends TestCase
{
    private BackwardsCompatibilityClassLoader classLoader = 
        new BackwardsCompatibilityClassLoader();

    public static void main(String[] args) throws Exception {
       
        LoadTestDataTest test = new LoadTestDataTest();
        
      
    }
    
    public void setUp(){
        System.setProperty("LG_CONFIG_FILE", "compatibilitytest/resources/config/lbconfig.props");
    }
    
    public void tearDown() throws Exception {
        WriteLockManager.instance().checkForRegistryUpdates();
        
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        
        for(CodingSchemeRendering csr : lbs.getSupportedCodingSchemes().getCodingSchemeRendering()){
            lbs.getServiceManager(null).activateCodingSchemeVersion(
                    Constructors.createAbsoluteCodingSchemeVersionReference(csr.getCodingSchemeSummary()));
            
            lbs.getServiceManager(null).setVersionTag(
                    Constructors.createAbsoluteCodingSchemeVersionReference(csr.getCodingSchemeSummary()), 
                            "PRODUCTION");
        }
    }
    
    public void runLoader(String loaderName, String methodName,
            Object[] args) throws Exception {
        
        Class[] clazzes = new Class[args.length];
        for(int i=0;i<args.length;i++){
            if(args[i] == null){
                clazzes[i] = URI.class;
            } else {
                if(args[i] instanceof Boolean){
                    clazzes[i] = boolean.class;
                }else if(args[i] instanceof Integer){
                    clazzes[i] = int.class;
                }else {
                    clazzes[i] = args[i].getClass();
                }
            }
        }
        
        Object loader = classLoader.loadClass(loaderName).newInstance();
        Method method = loader.getClass().getMethod(methodName, clazzes);
        method.invoke(loader, args);
    }

    public void testLoadAutombiles() throws Exception {
        
        this.runLoader(LexGridLoaderImpl.class.getName(), 
                "load",
                new Object[]{
            new File("compatibilitytest/resources/testData/Automobiles.xml").toURI(),
            false, false}); 
    }

    public void testLoadGermanMadeParts() throws Exception {
         
        
        this.runLoader(LexGridLoaderImpl.class.getName(), 
                "load", 
                new Object[]{
            new File("compatibilitytest/resources/testData/German Made Parts.xml").toURI(), 
            false, false});

    }

 /*
    public void testLoadHistory() throws Exception {
      
        this.runLoader(NCIHistoryLoader.class.getName(), 
                "load", new Object[]{
            new File("compatibilitytest/resources/testData/Filtered_pipe_out_12f.txt").toURI(),
            new File("compatibilitytest/resources/testData/SystemReleaseHistory.txt").toURI(), 
            false, true, true});
        
        }
*/
 
    public void testLoadObo() throws Exception {
       
        this.runLoader(OBOLoaderImpl.class.getName(), "load",
                new Object[]{
                new File("compatibilitytest/resources/testData/cell.obo").toURI(), 
                null, false, false}
        );
      }

    public void testLoadOwl() throws Exception {
       
        this.runLoader(OWLLoaderImpl.class.getName(), "loadNCI", new Object[]{
            new File("compatibilitytest/resources/testData/sample.owl").toURI(), null, false, false, false
        }
        );
    }

   
    public void testLoadGenericOwl() throws Exception {
      
        this.runLoader(OWLLoaderImpl.class.getName(), "loadNCI", new Object[]{new File("compatibilitytest/resources/testData/amino-acid.owl").toURI(), new File(
        "compatibilitytest/resources/testData/amino-acid-manifest.xml").toURI(), false, false, false});
    }

 /*
    public void testLoadNCIMeta() throws Exception {
      
        this.runLoader(NCIMetaThesaurusLoaderImpl.class.getName(), "load", new Object[]{
        
        new File("compatibilitytest/resources/testData/sampleNciMeta").toURI(), true, true

        });
    }

    public void testLoadNCIMeta2() throws Exception {
        this.runLoader(NCIMetaThesaurusLoaderImpl.class.getName(), "load", new Object[]{
                
          new File("compatibilitytest/resources/testData/sampleNciMetaWithPropertyLinks").toURI(), true, true

        });
    }

  
    public void testLoadUMLS() throws Exception {
        Object lnl = classLoader.loadClass(LocalNameList.class.getName()).newInstance();
        
        Method method = lnl.getClass().getMethod("addEntry", String.class);
        
        method.invoke(lnl, "AIR");
        
       
        this.runLoader(UMLSLoaderImpl.class.getName(), "load", new Object[]{
                
            new File("compatibilitytest/resources/testData/sampleUMLS-AIR").toURI(), lnl,
                        true, true

        
        });
       

    }
    */ 
}