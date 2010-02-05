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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG42.Impl.CodedNodeGraphImplTest;
import org.LexGrid.LexBIG42.Impl.CodedNodeSetImplTest;
import org.LexGrid.LexBIG42.Impl.bugs.TestBugFixes;
import org.LexGrid.LexBIG42.Impl.dataAccess.TestLoaderPreferences;
import org.LexGrid.LexBIG42.Impl.function.query.TestApproximateStringMatch;
import org.LexGrid.LexBIG42.Impl.function.query.TestAttributePresenceMatch;
import org.LexGrid.LexBIG42.Impl.function.query.TestAttributeValueMatch;
import org.LexGrid.LexBIG42.Impl.function.query.TestChildIndicator;
import org.LexGrid.LexBIG42.Impl.function.query.TestCodingSchemesWithSupportedAssociation;
import org.LexGrid.LexBIG42.Impl.function.query.TestContentExtraction;
import org.LexGrid.LexBIG42.Impl.function.query.TestDAGWalking;
import org.LexGrid.LexBIG42.Impl.function.query.TestDescribeSearchTechniques;
import org.LexGrid.LexBIG42.Impl.function.query.TestDescribeSupportedSearchCriteria;
import org.LexGrid.LexBIG42.Impl.function.query.TestDiscoverAvailableVocabulariesandVersions;
import org.LexGrid.LexBIG42.Impl.function.query.TestEnumerateAllConcepts;
import org.LexGrid.LexBIG42.Impl.function.query.TestEnumerateAssociationNames;
import org.LexGrid.LexBIG42.Impl.function.query.TestEnumerateConceptsbyRelationship;
import org.LexGrid.LexBIG42.Impl.function.query.TestEnumerateProperties;
import org.LexGrid.LexBIG42.Impl.function.query.TestEnumerateRelationsbyRange;
import org.LexGrid.LexBIG42.Impl.function.query.TestEnumerateRelationships;
import org.LexGrid.LexBIG42.Impl.function.query.TestEnumerateSourceConceptsforRelationandTarget;
import org.LexGrid.LexBIG42.Impl.function.query.TestGenerateDAG;
import org.LexGrid.LexBIG42.Impl.function.query.TestHierarchyAPI;
import org.LexGrid.LexBIG42.Impl.function.query.TestLexicalMatchingTechniques;
import org.LexGrid.LexBIG42.Impl.function.query.TestLimitReturnedValues;
import org.LexGrid.LexBIG42.Impl.function.query.TestMapAttributestoTypes;
import org.LexGrid.LexBIG42.Impl.function.query.TestMapSynonymtoPreferredNames;
import org.LexGrid.LexBIG42.Impl.function.query.TestMembershipinVocabulary;
import org.LexGrid.LexBIG42.Impl.function.query.TestOtherMatchingTechniques;
import org.LexGrid.LexBIG42.Impl.function.query.TestPagedReturns;
import org.LexGrid.LexBIG42.Impl.function.query.TestQuerybyRelationshipDomain;
import org.LexGrid.LexBIG42.Impl.function.query.TestRelationshipInquiry;
import org.LexGrid.LexBIG42.Impl.function.query.TestRestrictToDirectionalNames;
import org.LexGrid.LexBIG42.Impl.function.query.TestRetrieveConceptandAttributesbyCode;
import org.LexGrid.LexBIG42.Impl.function.query.TestRetrieveConceptandAttributesbyPreferredName;
import org.LexGrid.LexBIG42.Impl.function.query.TestRetrieveMostRecentVersionofConcept;
import org.LexGrid.LexBIG42.Impl.function.query.TestRetrieveRelationsforConcept;
import org.LexGrid.LexBIG42.Impl.function.query.TestSearchbyStatus;
import org.LexGrid.LexBIG42.Impl.function.query.TestSetofVocabulariesforSearch;
import org.LexGrid.LexBIG42.Impl.function.query.TestSpecifyReturnOrder;
import org.LexGrid.LexBIG42.Impl.function.query.TestSubsetExtraction;
import org.LexGrid.LexBIG42.Impl.function.query.TestTraverseGraphviaRoleLinks;
import org.LexGrid.LexBIG42.Impl.function.query.TestVersioningandAuthorityEnumeration;
import org.LexGrid.LexBIG42.Impl.function.query.TestforCurrentOrObsoleteConcept;

public class AllTestsNormalConfig
{

    public static Test suite() throws Exception
    {
        System.setProperty("LG_CONFIG_FILE", "compatibilitytest/resources/config/lbconfig.props");
        TestSuite mainSuite = new TestSuite("LexBIG validation tests");
        ServiceHolder.configureForSingleConfig();

        // currentSuite.addTestSuite(ConfigureTest.class);
        mainSuite.addTestSuite(LoadTestDataTest.class);
        //This test cannont be safely run - will corrupt already loaded history data.
        //mainSuite.addTestSuite(NCIThesaurusHistoryServiceTest.class);
        mainSuite.addTestSuite(CodedNodeGraphImplTest.class);
        mainSuite.addTestSuite(CodedNodeSetImplTest.class);

        TestSuite functionalTests = new TestSuite("Functional Tests");
        //functionalTests.addTestSuite(TestProductionTags.class);
        functionalTests.addTestSuite(TestApproximateStringMatch.class);
        functionalTests.addTestSuite(TestAttributePresenceMatch.class);
        functionalTests.addTestSuite(TestAttributeValueMatch.class);
        functionalTests.addTestSuite(TestContentExtraction.class);
        functionalTests.addTestSuite(TestDAGWalking.class);
        functionalTests.addTestSuite(TestDescribeSearchTechniques.class);
        functionalTests.addTestSuite(TestDescribeSupportedSearchCriteria.class);
        functionalTests.addTestSuite(TestDiscoverAvailableVocabulariesandVersions.class);
        functionalTests.addTestSuite(TestEnumerateAllConcepts.class);
        functionalTests.addTestSuite(TestEnumerateConceptsbyRelationship.class);
        functionalTests.addTestSuite(TestEnumerateProperties.class);
        functionalTests.addTestSuite(TestEnumerateRelationsbyRange.class);
        functionalTests.addTestSuite(TestEnumerateRelationships.class);
        functionalTests.addTestSuite(TestEnumerateSourceConceptsforRelationandTarget.class);
        functionalTests.addTestSuite(TestforCurrentOrObsoleteConcept.class);
        functionalTests.addTestSuite(TestGenerateDAG.class);
        functionalTests.addTestSuite(TestHierarchyAPI.class);
        functionalTests.addTestSuite(TestLexicalMatchingTechniques.class);
        functionalTests.addTestSuite(TestLimitReturnedValues.class);
        functionalTests.addTestSuite(TestMapAttributestoTypes.class);
        functionalTests.addTestSuite(TestMapSynonymtoPreferredNames.class);
        functionalTests.addTestSuite(TestMembershipinVocabulary.class);
        functionalTests.addTestSuite(TestOtherMatchingTechniques.class);
        functionalTests.addTestSuite(TestPagedReturns.class);
        functionalTests.addTestSuite(TestQuerybyRelationshipDomain.class);
        functionalTests.addTestSuite(TestRelationshipInquiry.class);
        functionalTests.addTestSuite(TestRetrieveConceptandAttributesbyCode.class);
        functionalTests.addTestSuite(TestRetrieveConceptandAttributesbyPreferredName.class);
        functionalTests.addTestSuite(TestRetrieveMostRecentVersionofConcept.class);
        functionalTests.addTestSuite(TestRetrieveRelationsforConcept.class);
        functionalTests.addTestSuite(TestRestrictToDirectionalNames.class);
        functionalTests.addTestSuite(TestSearchbyStatus.class);
        functionalTests.addTestSuite(TestSetofVocabulariesforSearch.class);
        functionalTests.addTestSuite(TestSpecifyReturnOrder.class);
        functionalTests.addTestSuite(TestSubsetExtraction.class);
        functionalTests.addTestSuite(TestTraverseGraphviaRoleLinks.class);
        //this test cannot be safely run on their config - will maul their history.
        //functionalTests.addTestSuite(TestVersionChanges.class);
        functionalTests.addTestSuite(TestVersioningandAuthorityEnumeration.class);
        functionalTests.addTestSuite(TestCodingSchemesWithSupportedAssociation.class);
        functionalTests.addTestSuite(TestEnumerateAssociationNames.class);
        functionalTests.addTestSuite(TestChildIndicator.class);
        functionalTests.addTestSuite(TestLoaderPreferences.class);
        
        mainSuite.addTest(functionalTests);
        
        TestSuite bugTests = new TestSuite("Bug Regression Tests");
        bugTests.addTestSuite(TestBugFixes.class);
        
        mainSuite.addTest(bugTests);

        mainSuite.addTestSuite(CleanUpTest.class);

        // $JUnit-END$

        return mainSuite;
    }
}