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
package org.LexGrid.LexBIG.Impl.testUtility;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.LexBIG.Impl.CodedNodeGraphImplTest;
//import org.LexGrid.LexBIG.Impl.CodedNodeGraphImplTest;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImplTest;
import org.LexGrid.LexBIG.Impl.CodedNodeSetSerializationTest;
import org.LexGrid.LexBIG.Impl.ServiceManagerTest;
//import org.LexGrid.LexBIG.Impl.ServiceManagerTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.LexBIGServiceConvenienceMethodsImplTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.MappingExtensionImplTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.MappingExtensionSerializationTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.SearchExtensionImplTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search.ScoreDocIteratoSerializationTest;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search.SearchScoreDocIteratorTest;
import org.LexGrid.LexBIG.Impl.Extensions.Search.AbstractSearchTest;
import org.LexGrid.LexBIG.Impl.Extensions.Sort.AbstractSortTest;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeIteratorTest;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNodeJsonNameSpaceTest;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNodeTest;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeTest;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.MultipleNamspaceErrorLEXEVS_598_Test;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.PathToRootTreeServiceImplTest;
import org.LexGrid.LexBIG.Impl.Extensions.tree.test.TestGetTree;
import org.LexGrid.LexBIG.Impl.History.NCIThesaurusHistoryServiceTest;
import org.LexGrid.LexBIG.Impl.History.UMLSHistoryServiceTest;
import org.LexGrid.LexBIG.Impl.bugs.GForge15976;
import org.LexGrid.LexBIG.Impl.bugs.GForge19492;
import org.LexGrid.LexBIG.Impl.bugs.GForge19573;
import org.LexGrid.LexBIG.Impl.bugs.GForge19628;
import org.LexGrid.LexBIG.Impl.bugs.GForge19629;
import org.LexGrid.LexBIG.Impl.bugs.GForge19650;
import org.LexGrid.LexBIG.Impl.bugs.GForge19702;
import org.LexGrid.LexBIG.Impl.bugs.GForge19716;
import org.LexGrid.LexBIG.Impl.bugs.GForge19741;
import org.LexGrid.LexBIG.Impl.bugs.GForge20525;
import org.LexGrid.LexBIG.Impl.bugs.GForge20651;
import org.LexGrid.LexBIG.Impl.bugs.GForge20875;
import org.LexGrid.LexBIG.Impl.bugs.GForge21211;
import org.LexGrid.LexBIG.Impl.bugs.GForge21567;
import org.LexGrid.LexBIG.Impl.bugs.GForge21923;
import org.LexGrid.LexBIG.Impl.bugs.GForge21935;
import org.LexGrid.LexBIG.Impl.bugs.GForge22826;
import org.LexGrid.LexBIG.Impl.bugs.GForge23103;
import org.LexGrid.LexBIG.Impl.bugs.GForge26741;
import org.LexGrid.LexBIG.Impl.bugs.GForge27457;
import org.LexGrid.LexBIG.Impl.bugs.GForge29772;
import org.LexGrid.LexBIG.Impl.bugs.GForge29839;
import org.LexGrid.LexBIG.Impl.bugs.GForge29840;
import org.LexGrid.LexBIG.Impl.bugs.GForge29841;
import org.LexGrid.LexBIG.Impl.bugs.GForge29842;
import org.LexGrid.LexBIG.Impl.bugs.GForge29860;
import org.LexGrid.LexBIG.Impl.bugs.GForge29924;
import org.LexGrid.LexBIG.Impl.bugs.GForge29940;
import org.LexGrid.LexBIG.Impl.bugs.TestBugFixes;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtilityTest;
import org.LexGrid.LexBIG.Impl.dataAccess.RegistryTest;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManagerTest;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethodsTest;
import org.LexGrid.LexBIG.Impl.dataAccess.TestLoaderPreferences;
import org.LexGrid.LexBIG.Impl.dataAccess.TestPasswordEncryption;
import org.LexGrid.LexBIG.Impl.featureRequests.AddNamespaceToIndex;
import org.LexGrid.LexBIG.Impl.featureRequests.ChangeConfigFileName;
import org.LexGrid.LexBIG.Impl.featureRequests.GForge17019;
import org.LexGrid.LexBIG.Impl.function.codednodeset.CodedNodeSetOperationsTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.DifferenceTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.ExtensionCodedNodeSetTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.IntersectionTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.MultipeRestrictionsTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.ResolveMappingCodedNodeSetTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.ResolveTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.ResolveToListTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.RestrictToMatchingDesignationsTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.RestrictToMatchingPropertiesTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.RestrictToPropertiesTest;
import org.LexGrid.LexBIG.Impl.function.codednodeset.UnionTest;
import org.LexGrid.LexBIG.Impl.function.history.TestProductionTags;
import org.LexGrid.LexBIG.Impl.function.mapping.MappingToNodeListTest;
import org.LexGrid.LexBIG.Impl.function.metadata.TestMetaDataSearch;
import org.LexGrid.LexBIG.Impl.function.metadata.TestNCIThesMetadata;
import org.LexGrid.LexBIG.Impl.function.query.TestApproximateStringMatch;
import org.LexGrid.LexBIG.Impl.function.query.TestAttributePresenceMatch;
import org.LexGrid.LexBIG.Impl.function.query.TestAttributeValueMatch;
import org.LexGrid.LexBIG.Impl.function.query.TestChildIndicator;
import org.LexGrid.LexBIG.Impl.function.query.TestCodingSchemesWithSupportedAssociation;
import org.LexGrid.LexBIG.Impl.function.query.TestContentExtraction;
import org.LexGrid.LexBIG.Impl.function.query.TestDAGWalking;
import org.LexGrid.LexBIG.Impl.function.query.TestDescribeSearchTechniques;
import org.LexGrid.LexBIG.Impl.function.query.TestDescribeSupportedSearchCriteria;
import org.LexGrid.LexBIG.Impl.function.query.TestDiscoverAvailableVocabulariesandVersions;
import org.LexGrid.LexBIG.Impl.function.query.TestEnumerateAllConcepts;
import org.LexGrid.LexBIG.Impl.function.query.TestEnumerateAssociationNames;
import org.LexGrid.LexBIG.Impl.function.query.TestEnumerateConceptsbyRelationship;
import org.LexGrid.LexBIG.Impl.function.query.TestEnumerateProperties;
import org.LexGrid.LexBIG.Impl.function.query.TestEnumerateRelationsbyRange;
import org.LexGrid.LexBIG.Impl.function.query.TestEnumerateRelationships;
import org.LexGrid.LexBIG.Impl.function.query.TestEnumerateSourceConceptsforRelationandTarget;
import org.LexGrid.LexBIG.Impl.function.query.TestGenerateDAG;
import org.LexGrid.LexBIG.Impl.function.query.TestHierarchyAPI;
import org.LexGrid.LexBIG.Impl.function.query.TestLexicalMatchingTechniques;
import org.LexGrid.LexBIG.Impl.function.query.TestLimitReturnedValues;
import org.LexGrid.LexBIG.Impl.function.query.TestMRRANK;
import org.LexGrid.LexBIG.Impl.function.query.TestMapAttributestoTypes;
import org.LexGrid.LexBIG.Impl.function.query.TestMapSynonymtoPreferredNames;
import org.LexGrid.LexBIG.Impl.function.query.TestMembershipinVocabulary;
import org.LexGrid.LexBIG.Impl.function.query.TestOWLLoaderPreferences;
import org.LexGrid.LexBIG.Impl.function.query.TestOtherMatchingTechniques;
import org.LexGrid.LexBIG.Impl.function.query.TestPagedReturns;
import org.LexGrid.LexBIG.Impl.function.query.TestQuerybyRelationshipDomain;
import org.LexGrid.LexBIG.Impl.function.query.TestRelationshipInquiry;
import org.LexGrid.LexBIG.Impl.function.query.TestRestrictToDirectionalNames;
import org.LexGrid.LexBIG.Impl.function.query.TestRetrieveConceptandAttributesbyCode;
import org.LexGrid.LexBIG.Impl.function.query.TestRetrieveConceptandAttributesbyPreferredName;
import org.LexGrid.LexBIG.Impl.function.query.TestRetrieveMostRecentVersionofConcept;
import org.LexGrid.LexBIG.Impl.function.query.TestRetrieveRelationsforConcept;
import org.LexGrid.LexBIG.Impl.function.query.TestSameCodeDifferentNamespace;
import org.LexGrid.LexBIG.Impl.function.query.TestSearchbyStatus;
import org.LexGrid.LexBIG.Impl.function.query.TestSetofVocabulariesforSearch;
import org.LexGrid.LexBIG.Impl.function.query.TestSpecifyReturnOrder;
import org.LexGrid.LexBIG.Impl.function.query.TestSubsetExtraction;
import org.LexGrid.LexBIG.Impl.function.query.TestTransitiveClosure;
import org.LexGrid.LexBIG.Impl.function.query.TestTraverseGraphviaRoleLinks;
import org.LexGrid.LexBIG.Impl.function.query.TestVersionChanges;
import org.LexGrid.LexBIG.Impl.function.query.TestVersioningandAuthorityEnumeration;
import org.LexGrid.LexBIG.Impl.function.query.TestforCurrentOrObsoleteConcept;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestContains;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestContainsLiteralContains;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestDoubleMetaphone;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestExactMatch;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLeadingAndTrailingWildcard;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLiteral;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLiteralContains;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLiteralLiteralContains;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLiteralSpellingErrorTolerantSubString;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestLiteralSubString;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestPhrase;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestRegExp;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestSearchByPreferred;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestSpellingErrorTolerantSubString;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestStartsWith;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestStemming;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestSubString;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestSubStringLiteralSubString;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestSubStringNonLeadingWildcardLiteralSubString;
import org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.TestWeightedDoubleMetaphone;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturnTest;
import org.LexGrid.LexBIG.Impl.helpers.ConfigureTest;
import org.LexGrid.LexBIG.Impl.helpers.ResolvedConceptReferencesIteratorImplTest;
import org.LexGrid.LexBIG.Impl.helpers.comparator.ResultComparatorTest;
import org.LexGrid.LexBIG.Impl.helpers.lazyloading.LazyLoadableCodeToReturnTest;
import org.LexGrid.LexBIG.Impl.load.meta.DefinitionPropertyDataTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.DefinitionQualifiersDataTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.EntityAssnsToEntityDataTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.EntityAssnsToEntityQualsDataTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.EntityDataTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.GenericPropertySourceQualifierTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.MetaVersionTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.MetadataLoadTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.MrhierAssocQualifierTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.MrhierPropertyQualifierTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.MrrankQualifierDataTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.MrstyPropertyDataTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.PresentationPropertyDataTestIT;
import org.LexGrid.LexBIG.Impl.load.meta.PresentationQualifiersDataTestIT;
import org.LexGrid.LexBIG.Impl.namespace.DefaultNamespaceHandlerTest;
import org.LexGrid.LexBIG.Utility.OrderingTestRunnerTest;
import org.junit.runners.model.InitializationError;
import org.lexevs.dao.database.service.listener.DuplicatePropertyIdListenerTest;
import org.lexevs.dao.index.operation.DefaultLexEVSIndexOperationsCleanupIndexesTest;
import org.lexevs.dao.index.operation.DefaultLexEVSIndexOperationsCreateIndexTest;
import org.lexevs.dao.index.operation.DefaultLexEVSIndexOperationsCreateMultipleIndexesTest;
import org.lexevs.dao.index.operation.DefaultLexEVSIndexOperationsRemoveTest;
import org.lexevs.dao.index.operation.ManifestLoadWithAssociationTest;
import org.lexevs.dao.index.operation.OrphanedIndexTest;
import org.lexevs.dao.index.operation.SameSessionLoadandQueryTest;
import org.lexevs.dao.indexer.lucene.analyzers.SnowballAnalyzerTest;
import org.lexevs.dao.indexer.lucene.analyzers.StringAnalyzerTest;
import org.lexevs.dao.indexer.lucene.analyzers.WhiteSpaceLowerCaseAnalyzerTest;
import org.lexevs.dao.indexer.lucene.hitcollector.BestScoreOfEntityHitCollectorTest;
import org.lexevs.dao.indexer.lucene.query.SerializableRegexCapabilitiesTest;
import org.lexevs.dao.indexer.lucene.query.SerializableRegexQueryTest;

import edu.mayo.informatics.lexgrid.convert.directConversions.owl2.NewOWL2SnippetTestIT;
import edu.mayo.informatics.lexgrid.convert.directConversions.owl2.NewOWL2UnannotatedSnippetTestIT;
import edu.mayo.informatics.lexgrid.convert.directConversions.owl2.OWL2LoaderLexGridTest;
import edu.mayo.informatics.lexgrid.convert.directConversions.owl2.OWL2PrimitivesSnippetTestIT;
import edu.mayo.informatics.lexgrid.convert.directConversions.owl2.OWL2PrimitivesUnannotatedSnippetTestIT;
import edu.mayo.informatics.lexgrid.convert.directConversions.owl2.OWL2SpecialCaseSnippetTestIT;
import edu.mayo.informatics.lexgrid.convert.directConversions.owl2.PresentationPropertyTestIT;
import edu.mayo.informatics.lexgrid.convert.directConversions.owlapi.OWL2UnitTests;
import edu.mayo.informatics.lexgrid.convert.directConversions.owlapi.OWLUnitTests;
import edu.mayo.informatics.lexgrid.convert.indexer.LuceneLoaderCodeTest;


public class AllTestsNormalConfig {

    private static final java.lang.String TREE_TESTS_ENV = "TREE_TESTS";

    public static Test suite() throws Exception {
        TestSuite mainSuite = new TestSuite("LexBIG validation tests");
        ServiceHolder.configureForSingleConfig();

        mainSuite.addTestSuite(ConfigureTest.class);
        mainSuite.addTestSuite(OrderingTestRunnerTest.class);
        mainSuite.addTest(orderedSuite(LoadTestDataTest.class));
        mainSuite.addTestSuite(CodeToReturnTest.class);
        mainSuite.addTestSuite(NCIThesaurusHistoryServiceTest.class);
        mainSuite.addTestSuite(UMLSHistoryServiceTest.class);
        mainSuite.addTestSuite(LexBIGServiceConvenienceMethodsImplTest.class);
        mainSuite.addTestSuite(CodedNodeGraphImplTest.class);
        mainSuite.addTestSuite(CodedNodeSetImplTest.class);
        mainSuite.addTestSuite(CodedNodeSetSerializationTest.class);
        mainSuite.addTestSuite(TestMetaDataSearch.class);
        mainSuite.addTestSuite(ServiceManagerTest.class);
        mainSuite.addTestSuite(RegistryTest.class);
        mainSuite.addTestSuite(TestNCIThesMetadata.class);
        mainSuite.addTestSuite(ResourceManagerTest.class); 
        mainSuite.addTestSuite(SQLImplementedMethodsTest.class);  
        mainSuite.addTestSuite(ResolvedConceptReferencesIteratorImplTest.class);
        
        mainSuite.addTestSuite(AbstractSortTest.class);
        mainSuite.addTestSuite(AbstractSearchTest.class);
        
        TestSuite metaLoaderSuite = new TestSuite("MetaLoader Tests");
        metaLoaderSuite.addTestSuite(DefinitionPropertyDataTestIT.class);
        metaLoaderSuite.addTestSuite(DefinitionQualifiersDataTestIT.class);
        metaLoaderSuite.addTestSuite(EntityAssnsToEntityDataTestIT.class);
        metaLoaderSuite.addTestSuite(EntityAssnsToEntityQualsDataTestIT.class);
        metaLoaderSuite.addTestSuite(EntityDataTestIT.class);
        metaLoaderSuite.addTestSuite(MetadataLoadTestIT.class);
        metaLoaderSuite.addTestSuite(MrstyPropertyDataTestIT.class);
        metaLoaderSuite.addTestSuite(MrhierAssocQualifierTestIT.class);
        metaLoaderSuite.addTestSuite(MrhierPropertyQualifierTestIT.class);
        metaLoaderSuite.addTestSuite(MrrankQualifierDataTestIT.class);
        metaLoaderSuite.addTestSuite(PresentationPropertyDataTestIT.class);
        metaLoaderSuite.addTestSuite(PresentationQualifiersDataTestIT.class);
        metaLoaderSuite.addTestSuite(MetaVersionTestIT.class);
        metaLoaderSuite.addTestSuite(GenericPropertySourceQualifierTestIT.class);

        mainSuite.addTest(metaLoaderSuite);
        
        TestSuite owlLoaderSuite = new TestSuite("OWL Tests");
        owlLoaderSuite.addTestSuite(OWLUnitTests.class);
        mainSuite.addTest(owlLoaderSuite);
        
        TestSuite owl2LoaderSuite = new TestSuite("OWL2 Tests");
        owl2LoaderSuite.addTestSuite(PresentationPropertyTestIT.class);
        owl2LoaderSuite.addTestSuite(OWL2LoaderLexGridTest.class);
        owl2LoaderSuite.addTestSuite(NewOWL2SnippetTestIT.class);
        owl2LoaderSuite.addTestSuite(NewOWL2UnannotatedSnippetTestIT.class);
        owl2LoaderSuite.addTestSuite(OWL2PrimitivesSnippetTestIT.class);
        owl2LoaderSuite.addTestSuite(OWL2PrimitivesUnannotatedSnippetTestIT.class);
        owl2LoaderSuite.addTestSuite(OWL2SpecialCaseSnippetTestIT.class);
        owl2LoaderSuite.addTestSuite(OWL2UnitTests.class);
        mainSuite.addTest(owl2LoaderSuite);
        
        
        TestSuite umlsLoaderSuite = new TestSuite("UmlsLoader Tests");
        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.EntityAssnsToEntityDataTestIT.class);
        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.EntityAssnsToEntityQualsDataTestIT.class);
        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.HierarchyRootsTestIT.class);
        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.PresentationPropertyDataTestIT.class);
        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.ReverseAssocDirectionalityTestIT.class);
        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.SameCodeDifferentCuiTestIT.class); 
        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.TuiPropertyDataTestIT.class);
        umlsLoaderSuite.addTestSuite(org.LexGrid.LexBIG.Impl.load.umls.IsoMapTest.class);
        mainSuite.addTest(umlsLoaderSuite);
        
        TestSuite hl7MifVocabularyLoaderSuite = new TestSuite("HL7 MIF Vocabulary Loader Tests");
        hl7MifVocabularyLoaderSuite.addTestSuite(edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.MifVocabularyHierarchyRootsTestIT.class);
        hl7MifVocabularyLoaderSuite.addTestSuite(edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.DefinitionPropertyDataTestIT.class);
        hl7MifVocabularyLoaderSuite.addTestSuite(edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.EntityAssnsToEntityDataTestIT.class);
        hl7MifVocabularyLoaderSuite.addTestSuite(edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.PresentationPropertyDataTestIT.class);
        mainSuite.addTest(hl7MifVocabularyLoaderSuite);
        
        TestSuite luceneSuite = new TestSuite("Lucene Tests");
        luceneSuite.addTestSuite(SnowballAnalyzerTest.class);
        luceneSuite.addTestSuite(StringAnalyzerTest.class);
        luceneSuite.addTestSuite(WhiteSpaceLowerCaseAnalyzerTest.class);
        luceneSuite.addTestSuite(BestScoreOfEntityHitCollectorTest.class);
        luceneSuite.addTestSuite(LuceneLoaderCodeTest.class);
        luceneSuite.addTestSuite(SerializableRegexCapabilitiesTest.class);
        luceneSuite.addTestSuite(SerializableRegexQueryTest.class);
        mainSuite.addTest(luceneSuite);    
        
        TestSuite luceneSearchSuite = new TestSuite("Lucene Search Tests");
        luceneSearchSuite.addTestSuite(TestContains.class);
        luceneSearchSuite.addTestSuite(TestContainsLiteralContains.class);
        luceneSearchSuite.addTestSuite(TestDoubleMetaphone.class);      
        luceneSearchSuite.addTestSuite(TestExactMatch.class);
        luceneSearchSuite.addTestSuite(TestLeadingAndTrailingWildcard.class);
        luceneSearchSuite.addTestSuite(TestLiteral.class); 
        luceneSearchSuite.addTestSuite(TestLiteralContains.class); 
        luceneSearchSuite.addTestSuite(TestLiteralLiteralContains.class); 
        luceneSearchSuite.addTestSuite(TestLiteralSpellingErrorTolerantSubString.class); 
        luceneSearchSuite.addTestSuite(TestLiteralSubString.class); 
        luceneSearchSuite.addTestSuite(TestPhrase.class);
        luceneSearchSuite.addTestSuite(TestRegExp.class);
        luceneSearchSuite.addTestSuite(TestSearchByPreferred.class);
        luceneSearchSuite.addTestSuite(TestSpellingErrorTolerantSubString.class); 
        luceneSearchSuite.addTestSuite(TestStartsWith.class);
        luceneSearchSuite.addTestSuite(TestStemming.class);
        luceneSearchSuite.addTestSuite(TestSubString.class); 
        luceneSearchSuite.addTestSuite(TestSubStringLiteralSubString.class);
        luceneSearchSuite.addTestSuite(TestWeightedDoubleMetaphone.class);
        luceneSearchSuite.addTestSuite(TestSubStringNonLeadingWildcardLiteralSubString.class);
        mainSuite.addTest(luceneSearchSuite);
        
        TestSuite lazyLoadingSuite = new TestSuite("Lazy Loading Tests");
        lazyLoadingSuite.addTestSuite(LazyLoadableCodeToReturnTest.class);
        mainSuite.addTest(lazyLoadingSuite);
        
        TestSuite comparatorSuite = new TestSuite("Comparator Tests");
        comparatorSuite.addTestSuite(ResultComparatorTest.class);
        mainSuite.addTest(comparatorSuite);
        
        TestSuite namespaceSuite = new TestSuite("Namespace Tests");
        namespaceSuite.addTestSuite(DefaultNamespaceHandlerTest.class);
        mainSuite.addTest(namespaceSuite);
      
        TestSuite codedNodeSetSuite = new TestSuite("CodedNodeSet Tests");
        codedNodeSetSuite.addTestSuite(ResolveTest.class);
        codedNodeSetSuite.addTestSuite(ResolveToListTest.class);
        codedNodeSetSuite.addTestSuite(DifferenceTest.class);
        codedNodeSetSuite.addTestSuite(CodedNodeSetOperationsTest.class);
        codedNodeSetSuite.addTestSuite(DifferenceTest.class);
        codedNodeSetSuite.addTestSuite(IntersectionTest.class);
        codedNodeSetSuite.addTestSuite(UnionTest.class);
        codedNodeSetSuite.addTestSuite(RestrictToMatchingDesignationsTest.class);
        codedNodeSetSuite.addTestSuite(RestrictToMatchingPropertiesTest.class);
        codedNodeSetSuite.addTestSuite(RestrictToPropertiesTest.class);
        codedNodeSetSuite.addTestSuite(MultipeRestrictionsTest.class);
        
        
        
        codedNodeSetSuite.addTestSuite(ResolveMappingCodedNodeSetTest.class);
        codedNodeSetSuite.addTestSuite(ExtensionCodedNodeSetTest.class);
        mainSuite.addTest(codedNodeSetSuite);
       
        TestSuite codedNodeGraphSuite = new TestSuite("CodedNodeGraph Tests");
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToAssociationsTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToDirectionalNamesTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToSourceCodesTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToTargetCodesTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.ResolveToListTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.SortGraphTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.ToNodeListTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.IntersectionTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.FilterTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RootsTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.FocusTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.UnionTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.CrossOntologyResolveTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToAnonymousTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.RestrictToEntityTypesTest.class);
        codedNodeGraphSuite.addTestSuite(org.LexGrid.LexBIG.Impl.function.codednodegraph.CodingSchemeExtensionResolveTest.class);
        mainSuite.addTest(codedNodeGraphSuite);

        TestSuite functionalTests = new TestSuite("Functional Tests");
        functionalTests.addTest(orderedSuite(TestProductionTags.class));
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
//        functionalTests.addTestSuite(TestOrphanedConcept.class);
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
        functionalTests.addTestSuite(TestTransitiveClosure.class);
        functionalTests.addTestSuite(TestTraverseGraphviaRoleLinks.class);
        functionalTests.addTestSuite(TestVersionChanges.class);
        functionalTests.addTestSuite(TestVersioningandAuthorityEnumeration.class);
        functionalTests.addTestSuite(TestCodingSchemesWithSupportedAssociation.class);
        functionalTests.addTestSuite(TestEnumerateAssociationNames.class);
        functionalTests.addTestSuite(TestChildIndicator.class);
//        functionalTests.addTestSuite(TestPreLoadManifest.class);
        functionalTests.addTestSuite(TestMRRANK.class);
        functionalTests.addTestSuite(TestLoaderPreferences.class);
        functionalTests.addTestSuite(TestOWLLoaderPreferences.class);
        functionalTests.addTestSuite(TestSameCodeDifferentNamespace.class);
        functionalTests.addTestSuite(TestPasswordEncryption.class);
//
        mainSuite.addTest(functionalTests);

//        if(System.getProperty(TREE_TESTS_ENV) != null) {
            TestSuite treeTests = new TestSuite("tree extension tests");
            treeTests.addTestSuite(PagingChildNodeIteratorTest.class);
            treeTests.addTestSuite(LexEvsTreeNodeTest.class);
            treeTests.addTestSuite(LexEvsTreeTest.class);
            treeTests.addTestSuite(MultipleNamspaceErrorLEXEVS_598_Test.class);
            treeTests.addTestSuite(PathToRootTreeServiceImplTest.class);
            treeTests.addTestSuite(LexEvsTreeNodeJsonNameSpaceTest.class);
            treeTests.addTestSuite(TestGetTree.class);

            mainSuite.addTest(treeTests);
//        }
        
        TestSuite bugTests = new TestSuite("Bug Regression Tests");
        bugTests.addTestSuite(TestBugFixes.class);
        bugTests.addTest(orderedSuite(GForge19650.class));
        bugTests.addTestSuite(GForge19492.class);
        bugTests.addTestSuite(GForge19573.class);
        bugTests.addTestSuite(GForge19628.class);
        bugTests.addTestSuite(GForge19629.class);
        bugTests.addTestSuite(GForge19702.class);
        bugTests.addTestSuite(GForge19741.class);
        bugTests.addTestSuite(GForge19716.class);
        bugTests.addTestSuite(GForge15976.class); 
        bugTests.addTestSuite(GForge20525.class); 
        bugTests.addTestSuite(GForge20651.class); 
        bugTests.addTestSuite(GForge21211.class); 
        bugTests.addTestSuite(GForge21567.class);
        bugTests.addTestSuite(GForge21935.class);
        bugTests.addTestSuite(GForge21923.class);
        bugTests.addTestSuite(GForge22826.class);
        bugTests.addTestSuite(GForge20875.class);
        bugTests.addTestSuite(GForge23103.class);
        bugTests.addTestSuite(GForge26741.class);
        bugTests.addTestSuite(GForge29772.class);
        bugTests.addTestSuite(GForge29839.class);
        bugTests.addTestSuite(GForge29840.class);
        bugTests.addTestSuite(GForge29841.class);
        bugTests.addTestSuite(GForge29842.class);
        bugTests.addTestSuite(GForge29860.class);
        bugTests.addTestSuite(GForge29924.class);
        bugTests.addTestSuite(GForge29940.class);
        bugTests.addTestSuite(GForge27457.class);
        
        bugTests.addTestSuite(DuplicatePropertyIdListenerTest.class);
        
        mainSuite.addTest(bugTests);
        
        TestSuite featureRequestTests = new TestSuite("Feature Request Tests");
        featureRequestTests.addTestSuite(AddNamespaceToIndex.class);
        featureRequestTests.addTestSuite(ChangeConfigFileName.class);  
        featureRequestTests.addTestSuite(GForge17019.class);
        mainSuite.addTest(featureRequestTests);
        
        //Mapping tests
        TestSuite mappingTests = new TestSuite("Mapping Tests");
        mappingTests.addTestSuite(MappingToNodeListTest.class);
        mainSuite.addTest(mappingTests);
      
        //Mapping Extension tests
        mainSuite.addTestSuite(MappingExtensionImplTest.class);
		mainSuite.addTestSuite(MappingExtensionSerializationTest.class);
        
        //Search Extension tests
        mainSuite.addTestSuite(SearchExtensionImplTest.class);
        mainSuite.addTestSuite(ScoreDocIteratoSerializationTest.class);
        mainSuite.addTestSuite(SearchScoreDocIteratorTest.class);
        
        //Association Authoring Mapping tests
        mainSuite.addTest(org.LexGrid.LexBIG.mapping.MappingAllTests.suite());
        
        mainSuite.addTestSuite(CleanUpTest.class);
       
        //ValueSets tests
        mainSuite.addTest(org.LexGrid.valueset.test.VDAllTests.suite());
        
        //LexGrid XML Exporter tests
        mainSuite.addTest(org.LexGrid.LexBIG.Impl.export.xml.lgxml.AllTests.suite());
        
        //MRMAP RRF load tests
        mainSuite.addTest(edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrMapAllTests.suite());
        
        //Broken Load Clean Up Function tests

        mainSuite.addTest(new JUnit4TestAdapter(CleanUpUtilityTest.class));
        mainSuite.addTest(new JUnit4TestAdapter(DefaultLexEVSIndexOperationsCleanupIndexesTest.class));
        mainSuite.addTest(new JUnit4TestAdapter(DefaultLexEVSIndexOperationsCreateIndexTest.class));
        mainSuite.addTest(new JUnit4TestAdapter(DefaultLexEVSIndexOperationsCreateMultipleIndexesTest.class));
        mainSuite.addTest(orderedSuite(DefaultLexEVSIndexOperationsRemoveTest.class));
        mainSuite.addTest(new JUnit4TestAdapter(SameSessionLoadandQueryTest.class));
        mainSuite.addTest(new JUnit4TestAdapter(ManifestLoadWithAssociationTest.class));
        mainSuite.addTest(new JUnit4TestAdapter(OrphanedIndexTest.class));
        // $JUnit-END$

        return mainSuite;
    }

    public static Test orderedSuite(Class<?> clazz) throws InitializationError {
        JUnit4TestAdapter adapter = new JUnit4TestAdapter(clazz);

        return adapter;
    }
}