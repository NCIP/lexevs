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
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.RemoveFromDistributedTests;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.SupportedProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(BlockJUnit4ClassRunner.class)
public class LexBIGServiceConvenienceMethodsImplTest extends LexBIGServiceTestCase {
    final static String testID = "LexBIGServiceConvenienceMethodsImplTest";

    private LexBIGService lbs;
    private LexBIGServiceConvenienceMethodsImpl lbscm;
    
    @Before
    public void setUp() throws LBException{
        lbs = ServiceHolder.instance().getLexBIGService(); 
        lbscm = (LexBIGServiceConvenienceMethodsImpl)lbs.getGenericExtension("LexBIGServiceConvenienceMethods");
        lbscm.setLexBIGService(lbs);
    }

    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    @Test
    public void testGetNodespath() throws LBException {
    	String codingSchemeUri = "urn:oid:11.11.0.1";
    	CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
    	versionOrTag.setVersion("1.0");
    	String containerName ="relations";
    	String associationName = "hasSubtype";
    	String sourceCode = "005", sourceNS = "Automobiles", targetCode = "C", targetNS = "Automobiles";
    	
    	ResolvedConceptReference path = lbscm.getNodesPath(codingSchemeUri, versionOrTag, containerName, associationName, sourceCode, sourceNS, targetCode, targetNS);
    	// the path string should be 
    	//005|,Automobiles->A|,Automobiles->B|,Automobiles->C|,Automobiles
    	// now let is traverse the graph to see if it is correct
    	
    	// for root
    	assertEquals("005", path.getCode());
    	assertEquals("Automobiles", path.getCodeNamespace());
    	assertEquals("urn:oid:11.11.0.1", path.getCodingSchemeURI());
    	assertEquals("1.0", path.getCodingSchemeVersion());
    	
    	AssociationList assnList = path.getSourceOf();
    	assertEquals(1, assnList.getAssociationCount());
    	
    	Association assn = assnList.getAssociation(0); 
    	if (assn == null)
    		fail("associaton is null");
    	assertEquals("hasSubtype", assn.getAssociationName());
    	
    	AssociatedConceptList assnConList = assn.getAssociatedConcepts();
    	assertEquals(1, assnConList.getAssociatedConceptCount());
    	
    	// 2nd node
    	ResolvedConceptReference assnCon = assnConList.getAssociatedConcept(0);
    	assertEquals("A", assnCon.getCode());
    	assertEquals("Automobiles", assnCon.getCodeNamespace());
    	assertEquals("urn:oid:11.11.0.1", assnCon.getCodingSchemeURI());
    	assertEquals("1.0", assnCon.getCodingSchemeVersion());
    	
    	assnList = assnCon.getSourceOf();
    	assertEquals(1, assnList.getAssociationCount());
    	
    	assn = assnList.getAssociation(0); 
    	if (assn == null)
    		fail("associaton is null");
    	assertEquals("hasSubtype", assn.getAssociationName());
    	
    	assnConList = assn.getAssociatedConcepts();
    	assertEquals(1, assnConList.getAssociatedConceptCount());
    	
    	// 3rd node
    	assnCon = assnConList.getAssociatedConcept(0);
    	assertEquals("B", assnCon.getCode());
    	assertEquals("Automobiles", assnCon.getCodeNamespace());
    	assertEquals("urn:oid:11.11.0.1", assnCon.getCodingSchemeURI());
    	assertEquals("1.0", assnCon.getCodingSchemeVersion());
    	
    	assnList = assnCon.getSourceOf();
    	assertEquals(1, assnList.getAssociationCount());
    	
    	 assn = assnList.getAssociation(0); 
    	if (assn == null)
    		fail("associaton is null");
    	assertEquals("hasSubtype", assn.getAssociationName());
    	
    	assnConList = assn.getAssociatedConcepts();
    	assertEquals(1, assnConList.getAssociatedConceptCount());
    	
    	// 4th node
    	assnCon = assnConList.getAssociatedConcept(0);
    	assertEquals("C", assnCon.getCode());
    	assertEquals("Automobiles", assnCon.getCodeNamespace());
    	assertEquals("urn:oid:11.11.0.1", assnCon.getCodingSchemeURI());
    	assertEquals("1.0", assnCon.getCodingSchemeVersion());

    }
    
    @Test
    public void testGetAssociationForwardName() throws LBException {
    	String forwardName = lbscm.getAssociationForwardName("A1", AUTO_SCHEME, null);
    	assertEquals("GoingForward", forwardName);
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void testThreadSafeCodingSchemeCaches() throws Throwable {
        Map cache = lbscm.getCache_CodingSchemes();
        runCacheThreadSaveTest(cache);       
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void testThreadSafeCopyRightsCaches() throws Throwable {
        Map cache = lbscm.getCache_CopyRights();
        runCacheThreadSaveTest(cache);        
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void testThreadSafeHIDCaches() throws Throwable {
        Map cache = lbscm.getCache_HIDs();
        runCacheThreadSaveTest(cache);      
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void testThreadSafeHPathToRootExistsCaches() throws Throwable {
        Map cache = lbscm.getCache_HPathToRootExists();
        runCacheThreadSaveTest(cache);      
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void testThreadSafeHRootCodesCaches() throws Throwable {
        Map cache = lbscm.getCache_HRootCodes();
        runCacheThreadSaveTest(cache);       
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void testThreadSafeHRootsCaches() throws Throwable {
        Map cache = lbscm.getCache_HRoots();
        runCacheThreadSaveTest(cache);      
    }
    
    @Test
    public void testGetAssociationNameFromAssociationCode() throws Exception {
    	String assocName = lbscm.getAssociationNameFromAssociationCode(AUTO_SCHEME, null, "AssocEntity");
    	assertTrue(assocName.equals("A1"));
    }
    
    @Test
    public void testGetAssociationCodeFromAssociationName() throws Exception {
    	String assocCode = lbscm.getAssociationCodeFromAssociationName(AUTO_SCHEME, null, "A1");
    	assertTrue(assocCode.equals("AssocEntity"));
    }
    
    @Test
    public void testGetAssociationNameForDirectionalNameReverseName() throws Exception {
    	String[] assocNames = lbscm.getAssociationNameForDirectionalName(AUTO_SCHEME, null, "isA");
    	assertEquals(1, assocNames.length);
    	assertEquals("hasSubtype", assocNames[0]);
    }
    
    @Test
    public void testGetAssociationNameForDirectionalNameForwardName() throws Exception {
    	String[] assocNames = lbscm.getAssociationNameForDirectionalName(AUTO_SCHEME, null, "hasSubtype");
    	assertEquals(1, assocNames.length);
    	assertEquals("hasSubtype", assocNames[0]);
    }
    
    @Test
    public void testGetAssociationNameFromAssociationCodeWrong() throws Exception {
    	try {
			lbscm.getAssociationNameFromAssociationCode(AUTO_SCHEME, null, "A1WRONG");
		} catch (LBParameterException e) {
			//this is good -- doesn't exist
			return;
		}
		fail("Should have thrown an exception.");
    }
    
    @Test
    public void testGetAssociationCodeFromAssociationNameWrong() throws Exception {
    	try {
			lbscm.getAssociationCodeFromAssociationName(AUTO_SCHEME, null, "differentEntityCodeAssocWRONG");
		} catch (LBParameterException e) {
			//this is good -- doesn't exist
			return;
		}
		fail("Should have thrown an exception.");
    }
    
    /**
     * fix for gForge # 24699. Identify supportedProperty by propertyType.
     * @throws Exception
     */
    @Test
    public void testGetSupportedPropertiesOfTypePresentation() throws Exception {
    	List<SupportedProperty> props = lbscm.getSupportedPropertiesOfTypePresentation(AUTO_SCHEME, null);
    	assertTrue(props.size() >= 1);
    	for (SupportedProperty sp : props)
    	{
    		assertTrue(sp.getPropertyType().name().equalsIgnoreCase(PropertyTypes.PRESENTATION.name()));
    	}
    }
    
    @Test
    public void testGetDistinctNamespacesOfCode() throws Exception {
    	List<String> namespaces = lbscm.getDistinctNamespacesOfCode(AUTO_SCHEME, null, "C0001");
    	
    	assertEquals(1,namespaces.size());
    	
    	assertEquals("Automobiles",namespaces.get(0));
    }
    
    @Test
    public void testGetDistinctNamespacesOfCodeWithMultiple() throws Exception {
    	List<String> namespaces = lbscm.getDistinctNamespacesOfCode(PARTS_SCHEME, null, "codeWithMultipleNs");
    	
    	assertEquals(4,namespaces.size());
    	
    	assertTrue(namespaces.contains("ns1"));
    	assertTrue(namespaces.contains("ns2"));
    	assertTrue(namespaces.contains("ns3"));
    	assertTrue(namespaces.contains("ns4"));
    }
    
    @Test
    public void testGetAncestorsInTransitiveClosure( ) throws LBParameterException{
    	List<ResolvedConceptReference> refs = lbscm.getAncestorsInTransitiveClosure(AUTO_SCHEME, null, "005", "hasSubtype");
    	assertTrue(refs.size() > 0);
    	assertTrue(refs.get(0).getCode().equals("Ford"));
    	assertTrue(refs.get(0).getCodeNamespace().equals("Automobiles"));
    	assertTrue(refs.get(0).getEntityDescription().getContent().equals("Ford Motor Company"));    	
    }
    
    @Test
    public void testGetDecendentsInTransitiveClosure( ) throws LBParameterException{
    	List<ResolvedConceptReference> refs = lbscm.getDescendentsInTransitiveClosure(AUTO_SCHEME, null, "B", "hasSubtype");
    	assertTrue(refs.size() > 0);
    	assertTrue(refs.get(0).getCode().equals("A"));
    	assertTrue(refs.get(0).getCodeNamespace().equals("Automobiles"));
    	assertTrue(refs.get(0).getEntityDescription().getContent().equals("First Code in cycle"));    	
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
	public void testGetAllIncomingConcepts() throws LBInvocationException, LBParameterException, LBException{
    	AssociatedConceptList refs = lbscm.getallIncomingConceptsForAssociation(AUTO_SCHEME, null, "B", "hasSubtype", 10);
    	assertTrue(refs.getAssociatedConceptCount() > 0);
    	assertTrue(refs.getAssociatedConcept(0).getCode().equals("A"));
    	assertTrue(refs.getAssociatedConcept(0).getCodeNamespace().equals("Automobiles"));
    	assertTrue(refs.getAssociatedConcept(0).getEntityDescription().getContent().equals("First Code in cycle"));
    	
    }
    
    @Test
    public void searchAllDecendentsInTransitiveClosureBaseline() throws LBException {
    	
     CodingSchemeVersionOrTag vOt = Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION);

     	   CodedNodeSet nodeSet = null;
           nodeSet = lbscm.getLexBIGService().getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, vOt);
           nodeSet = nodeSet.restrictToMatchingDesignations("patient", 
                   SearchDesignationOption.PREFERRED_ONLY, "LuceneQuery" , null);
           ResolvedConceptReferenceList refs = nodeSet.resolveToList(null, null, null, -1);
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("MildlySickCancerPatient")));
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
    }
    
    @Test
    public void searchAllDecendentsInTransitiveClosureDomainMildlySickPatient( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("MildlySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchDescendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION), 
    			codes, 
    			"subClassOf", 
    			"patient", 
    			LBConstants.MatchAlgorithms.LuceneQuery.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).size() > 0);
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("slightly sick patient")));    	
    }
    
    @Test
    public void searchAllDecendentsInTransitiveClosureDomainVerySickPatient( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("VerySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchDescendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION),
    			codes, 
    			"subClassOf", 
    			"patient",
    			LBConstants.MatchAlgorithms.LuceneQuery.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	List<ResolvedConceptReference> list = Arrays.asList(refs.getResolvedConceptReference());
    	assertTrue(list.size() > 0);
    	assertTrue(list.stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("MildlySickCancerPatient")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("very sick cancer patient")));    	
    }
    
    @Test
    public void searchAllDecendentsInTransitiveClosureDomainVerySickPatientSourceSpecific( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("VerySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchDescendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION),
    			codes, 
    			"subClassOf", 
    			"patient",
    			LBConstants.MatchAlgorithms.LuceneQuery.name(),
    			SearchDesignationOption.ALL, 
    			Constructors.createLocalNameList("NCI"));
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	List<ResolvedConceptReference> list = Arrays.asList(refs.getResolvedConceptReference());
    	assertTrue(list.size() > 0);
    	assertTrue(list.stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("MildlySickCancerPatient")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("very sick cancer patient")));    	
    }
    
    @Test
    public void searchAllDecendentsInTransitiveClosureScaledCodeList( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("MildlySickPatient");
    	codes.add("VerySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchDescendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION), 
    			codes, 
    			"subClassOf", 
    			"patient",
    			LBConstants.MatchAlgorithms.LuceneQuery.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).size() > 0);
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("MildlySickCancerPatient")));
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("very sick cancer patient")));    	
    }
    
    protected void runCacheThreadSaveTest(Map cache) throws Throwable {
        TestRunnable[] runnables = {
                new TestCachePut(cache, 1000, 1),
                new TestCachePut(cache, 1000, 1)
        };

        MultiThreadedTestRunner runner = new MultiThreadedTestRunner(runnables);

        runner.runTestRunnables();   
    }
  
    
    class TestCachePut extends TestRunnable {
        private int count;
        private int sleepTime;
        private Map cache;

        public TestCachePut( Map cache, int count, int delay )
        {
            this.cache = cache;
            this.count = count;
            this.sleepTime = delay;
        }

        public void runTest() throws Throwable {
            for (int i = 0; i < this.count; ++i) {
                Thread.sleep( this.sleepTime );
                cache.put(i, i + i);
            }
        }
    }
    
}