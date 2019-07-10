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

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.TerminologyServiceDesignation;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.RemoveFromDistributedTests;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class LexBIGServiceConvenienceMethodsImplTest extends LexBIGServiceTestCase {
    final static String testID = "LexBIGServiceConvenienceMethodsImplTest";

    private static LexBIGService lbs;
    private LexBIGServiceConvenienceMethodsImpl lbscm;
    
    @Before
    public void setUp() throws LBException{
        lbs = getLexBIGService(); 
        ((LexBIGServiceImpl)lbs).setAssertedValueSetConfiguration(new AssertedValueSetParameters.Builder("0.1.5").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl").
				rootConcept("C54453")
				.build());
        lbscm = (LexBIGServiceConvenienceMethodsImpl)lbs.getGenericExtension("LexBIGServiceConvenienceMethods");
        lbscm.setLexBIGService(lbs);
    }

    
	public static LexBIGService getLexBIGService(){
		if(lbs == null){
			lbs = LexBIGServiceImpl.defaultInstance();
		}
		return lbs;
	}
	
	public void setLexBIGService(LexBIGService lbsvc){
		lbs = lbsvc;
	}


	@Override
    protected String getTestID() {
        return testID;
    }
    
   @Test
   public void testGetLexevsBuildVersion() throws LBException {
	   String version = lbs.getLexEVSBuildVersion();
	   System.out.println("LexEVS Build Version: " + version);
	   assertNotNull(version);
	   assertTrue(!version.equals("@VERSION@"));
   }
   
   @Test
   public void testGetLexevsBuildTimestamp() throws LBException {
	   String timestamp = lbs.getLexEVSBuildTimestamp();
	   System.out.println("LexEVS Build Timestamp: " + timestamp);
	   assertNotNull(timestamp);
	   assertTrue(!timestamp.equals("@TIMESTAMP@"));
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
       //	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("MildlySickCancerPatient")));
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
      // 	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
    }
    
    @Test
    public void searchAllAscendentsInTransitiveClosureBaseline() throws LBException {
    	//See if this works in a regular node graph resolution
        CodingSchemeVersionOrTag vOt = Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION);

        	   CodedNodeGraph nodeGraph = null;
              nodeGraph= lbscm.getLexBIGService().getNodeGraph(OWL2_SNIPPET_INDIVIDUAL_URN, vOt, null);
            //insure it's in the hierarchy classification
            nodeGraph = nodeGraph.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
			ResolvedConceptReferenceList refs = nodeGraph.resolveAsList(Constructors.createConceptReference("Patient", "owl2lexevs"), true, false, 20, 20, null, null, null, -1);
			ResolvedConceptReference ref = refs.getResolvedConceptReference(0);
			Association ass = ref.getSourceOf().getAssociation(0);
			//has one ancestor
			assertTrue(ass.getAssociatedConcepts().getAssociatedConceptCount() == 1);
          	assertTrue(Arrays.asList(ass.getAssociatedConcepts().getAssociatedConcept()).stream().anyMatch(y -> y.getCode().equals("Person")));
          	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
       }
    
    @Test
    public void searchAllAscendentsInTransitiveClosureBaselineNodeSetQuery() throws LBException {
    	//See if this works in a regular node graph resolution
        CodingSchemeVersionOrTag vOt = Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION);

        	   CodedNodeGraph nodeGraph = null;
              nodeGraph= lbscm.getLexBIGService().getNodeGraph(OWL2_SNIPPET_INDIVIDUAL_URN, vOt, null);
            //insure it's in the hierarchy classification
            nodeGraph = nodeGraph.restrictToAssociations(Constructors.createNameAndValueList("subClassOf"), null);
			ResolvedConceptReferenceList refs = nodeGraph.resolveAsList(Constructors.createConceptReference("VerySickCancerPatient", "owl2lexevs"), true, false, 20, 20, null, null, null, -1);
			ResolvedConceptReference ref = refs.getResolvedConceptReference(0);
			Association ass = ref.getSourceOf().getAssociation(0);
			CodedNodeSet set = lbscm.getLexBIGService().getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, vOt);
			ConceptReferenceList list = new ConceptReferenceList();
			list.setConceptReference((ConceptReference[])ass.getAssociatedConcepts().getAssociatedConcept());
			//has one ancestor
			set = set.restrictToCodes(list);
			set = set.restrictToMatchingProperties(null, 					
					new CodedNodeSet.PropertyType[] 
							{
									CodedNodeSet.PropertyType.PRESENTATION, 
									CodedNodeSet.PropertyType.GENERIC
									}, 
							Constructors.createLocalNameList("NCI"),
							null, null, "Patient", "LuceneQuery", null);
//			set = set.restrictToMatchingDesignations("Patient", SearchDesignationOption.ALL, "LuceneQuery", null);
			ResolvedConceptReferenceList results = set.resolveToList(null, null, null, -1);
			assertTrue(results.getResolvedConceptReferenceCount() > 0);
          	assertFalse(Arrays.asList(ass.getAssociatedConcepts().getAssociatedConcept()).stream().anyMatch(y -> y.getCode().equals("Person")));
          	assertTrue(Arrays.asList(results.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("VerySickPatient")));
       }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void searchAllDecendentsInTransitiveClosureDomainMildlySickPatient( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("MildlySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchDescendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION), 
    			codes, 
    			"subClassOf", 
    			"Patient", 
    			LBConstants.MatchAlgorithms.LuceneQuery.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).size() > 0);
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("MidlySickCancerPatient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("Person")));    	
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void searchAllAscendentsInTransitiveClosureDomainMildlySickPersonRootText( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("MildlySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchAscendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION), 
    			codes, 
    			"subClassOf", 
    			"person", 
    			LBConstants.MatchAlgorithms.LuceneQuery.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).size() > 0);
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("Person")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("Patient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("Person")));    	
    }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void searchAllAscendentsInTransitiveClosureDomainMildlySickPatientText( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("MildlySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchAscendentsInTransitiveClosure(
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
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("Person")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("Patient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("Patient")));    	
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
    	assertFalse(list.size() > 0);
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
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("VerySickCancerPatient")));    	
    }
    
    @Test
    public void searchAllAscendentsInTransitiveClosureDomainVerySickCancerPatientSourceSpecific( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("VerySickCancerPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchAscendentsInTransitiveClosure(
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
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("VerySickPatient")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("SickPatient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs"))); }
    
    @Test
    public void searchAllAscendentsInTransitiveClosureDomainVerySickCancerPatientSourceSpecificExactMatch( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("VerySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchAscendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION),
    			codes, 
    			"subClassOf", 
    			"Patient",
    			LBConstants.MatchAlgorithms.exactMatch.name(),
    			SearchDesignationOption.ALL, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	List<ResolvedConceptReference> list = Arrays.asList(refs.getResolvedConceptReference());
    	assertTrue(list.size() > 0);
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("VerySickPatient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("Patient")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("SickPatient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs"))); }
    
    @Test
    @Category(RemoveFromDistributedTests.class)
    public void searchAllDecendentsInTransitiveClosureDomainMildlySickPatientSourceSpecificPreferred( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("MildlySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchDescendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION),
    			codes, 
    			"subClassOf", 
    			"sick",
    			LBConstants.MatchAlgorithms.LuceneQuery.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	List<ResolvedConceptReference> list = Arrays.asList(refs.getResolvedConceptReference());
    	assertTrue(list.size() > 0);
    	assertTrue(list.stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("MildlySickCancerPatient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("slightly sick patient"))); 
    }
    
    @Test
    public void searchAllDecendentsInTransitiveClosureDomainMildlySickPatientContainsPreferred( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("MildlySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchDescendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION),
    			codes, 
    			"subClassOf", 
    			"Patient",
    			LBConstants.MatchAlgorithms.contains.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	List<ResolvedConceptReference> list = Arrays.asList(refs.getResolvedConceptReference());
    	assertTrue(list.size() > 0);
    	assertTrue(list.stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("slightly sick patient"))); 
    }
    
    @Test
    public void searchAllDecendentsInTransitiveClosureScaledCodeListExactMatch( ) throws LBParameterException{
    	long start = System.currentTimeMillis();
    	List<String> codes = new ArrayList<String>();
    	codes.add("MildlySickPatient");
    	codes.add("VerySickPatient");
    	ResolvedConceptReferenceList refs = lbscm.searchDescendentsInTransitiveClosure(
    			OWL2_SNIPPET_INDIVIDUAL_URN, 
    			Constructors.createCodingSchemeVersionOrTagFromVersion(OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION), 
    			codes, 
    			"subClassOf", 
    			"slightly sick patient",
    			LBConstants.MatchAlgorithms.exactMatch.name(),
    			SearchDesignationOption.PREFERRED_ONLY, 
    			null);
    	System.out.println("Execution time: " + (System.currentTimeMillis() - start));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).size() > 0);
       	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(y -> y.getCode().equals("MildlySickCancerPatient")));
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
       	assertFalse(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("VerySickCancerPatient")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("slightly sick patient")));    	
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
       	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCode().equals("PatientWithCold")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getCodeNamespace().equals("owl2lexevs")));
    	assertTrue(Arrays.asList(refs.getResolvedConceptReference()).stream().anyMatch(x -> x.getEntityDescription().getContent().equals("slightly sick patient")));    	
    }   
    
    
    
    @Test
    public void getTerminologyServiceDesignationForUri(){
    	assertEquals(lbscm.getTerminologyServiceObjectType(AUTO_URN).getDesignation(), TerminologyServiceDesignation.REGULAR_CODING_SCHEME);
    	assertEquals(lbscm.getTerminologyServiceObjectType(MAPPING_SCHEME_URI).getDesignation(), TerminologyServiceDesignation.MAPPING_CODING_SCHEME);
    	assertEquals(lbscm.getTerminologyServiceObjectType("urn:oid:CL413321.MDR.CST").getDesignation(), TerminologyServiceDesignation.MAPPING_CODING_SCHEME);
    	assertEquals(lbscm.getTerminologyServiceObjectType("SRITEST:AUTO:AllDomesticButGM").getDesignation(), TerminologyServiceDesignation.RESOLVED_VALUESET_CODING_SCHEME);
    	assertEquals(lbscm.getTerminologyServiceObjectType("http://evs.nci.nih.gov/valueset/FDA/C54453").getDesignation(), TerminologyServiceDesignation.ASSERTED_VALUE_SET_SCHEME);
    	assertEquals(lbscm.getTerminologyServiceObjectType("urn:oid:2.16.840.1.113883.3.26.1.2").getDesignation(), TerminologyServiceDesignation.REGULAR_CODING_SCHEME);
    	assertEquals(lbscm.getTerminologyServiceObjectType("urn:oid:2.NON.SENSE.URI.2").getDesignation(), TerminologyServiceDesignation.UNIDENTIFIABLE);
    }
}