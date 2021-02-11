
package org.LexGrid.LexBIG.Impl.test;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;

/**
 * Random test code used during the developement process.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SimpleTests {
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        try {
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            @SuppressWarnings("unused")
            CodedNodeSet cns;
            @SuppressWarnings("unused")
            ConvenienceMethods cm = new ConvenienceMethods(lbs);

            // System.out.println("Supported Match Algorithms");
            // System.out.println(ObjectToString.toString(lbs.getMatchAlgorithms()));
            //
            // System.out.println("Supported Sort Algorithms");
            // System.out.println(ObjectToString.toString(lbs.getSortAlgorithms(null)));

            @SuppressWarnings("unused")
            String codingScheme = "NCI_Thesaurus";

            // System.out.println("root nodes");
            // System.out.println(ObjectToString.toString(cm.getTopNodes(codingScheme,
            // null, null, null)));

            /*
             * Get the available coding schemes
             */
            // System.out.println("Get all available terminologies");
            // CodingSchemeRenderingList csrl = lbs.getSupportedCodingSchemes();
            // System.out.println(ObjectToString.toString(csrl));
            /*
             * Test for Jim
             */

            // CodedNodeSet jcns = lbs.getCodingSchemeConcepts("NCI Thesaurus",
            // null, true);
            // jcns.restrictToMatchingDesignations("hox11", false, "exactMatch",
            // null);
            //
            // CodedNodeGraph jcng = lbs.getNodeGraph("NCI Thesaurus", null,
            // "relations");
            // jcng.restrictToAssociations(cm.createConceptReferenceList("Gene_Product_Encoded_by_Gene"),
            // null);
            // jcng.restrictToTargetCodes(jcns);
            //
            // System.out.println(ObjectToString.toString(jcng.toNodeList(null,
            // true,
            // true).resolveToList(null, null, 0)));
            /*
             * Test for Tracy
             */

            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, true);
            //
            // cns.restrictToMatchingDesignations("liver", false, "exactMatch",
            // null);
            //
            // System.out.println(ObjectToString.toString(cns.resolveToList(null,
            // null, 0)));
            //
            // cns.restrictToCodes(cm.createConceptReferenceList(new
            // String[]{"C12392"}, codingScheme));
            //
            // System.out.println(ObjectToString.toString(cns.resolveToList(null,
            // null, 0)));
            // System.out.println("graph from liver concept code - C12392");
            //			
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, true);
            // cns.restrictToMatchingDesignations("Hepatocellular", false,
            // "LuceneQuery", null);
            //			
            //
            // cng = lbs.getNodeGraph(codingScheme, null, null);
            // cng.restrictToSourceCodes(cns);
            // cng.restrictToAssociations(cm
            // .createConceptReferenceList(new String[] {
            // "Disease_Has_Associated_Anatomic_Site",
            // "Disease_Has_Primary_Anatomic_Site" }), null);
            //
            // System.out.println(ObjectToString.toString(cng.resolveAsList(cm
            // .createConceptReference("C12392",
            // codingScheme), false, true, -1, 1, null, null, 0)));
            /*
             * Kim Question Given a bit of information - "Body" - find all
             * concepts that match this information. Then, return all concepts
             * that have a role that links to these found concepts.
             */

            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            //
            // cns.restrictToMatchingDesignations("body", false, "LuceneQuery",
            // null);
            //
            // cng = lbs.getNodeGraph(codingScheme, null, null);
            // cng.restrictToTargetCodes(cns);
            // cng.restrictToAssociations(cm.createConceptReferenceList("Disease_Has_Primary_Anatomic_Site"),
            // null);
            //
            // ResolvedConceptReferenceList rcrl = cng.resolveAsList(null, true,
            // false, -1, 1, null, null, 0);
            //
            // // Print out code
            //
            // ResolvedConceptReference[] rcr =
            // rcrl.getResolvedConceptReference();
            //
            // for (int i = 0; i < rcr.length; i++)
            // {
            // System.out.println(rcr[i].getEntityDescription().getContent());
            // String temp = " ";
            // Association[] asn = rcr[i].getSourceOf().getAssociation();
            //
            // for (int j = 0; j < asn.length; j++)
            // {
            // System.out.println(temp + asn[j].getAssociationName());
            // }
            // System.out.println();
            //
            // }
            //			
            /*
             * Test the resolveCodingScheme method
             */

            System.out.println("All coding scheme details:");
            System.out.println(ObjectToString.toString(lbs.resolveCodingScheme("gene_ontology", null)));
            /*
             * construct a query with two different restrictions, and then print
             * the results. this query first matches on all designations that
             * contain the word "heart" anywhere, and then it further restricts
             * the results to concepts with a semantic type property of
             * Congenital Abnormality Only returns the properties UMLS_CUI and
             * textualPresentation
             */

            // System.out.println("Example double restriction query");
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            //
            // cns.restrictToMatchingDesignations("heart", false,
            // MatchAlgorithms.LuceneQuery.toString(),
            // null);
            //
            // cns.restrictToMatchingProperties(cm.createLocalNameList("Semantic_Type"),
            // "Congenital
            // Abnormality",
            // "exactMatch", null);
            //
            // // only return the UMLS_CUI property and the textualPresentation
            // properties
            // LocalNameList restrictTo = cm.createLocalNameList(new
            // String[]{"UMLS_CUI",
            // "textualPresentation"});
            // list = cns.resolveToList(null, restrictTo, 0);
            //
            // System.out.println(ObjectToString.toString(list));
            /*
             * This query matches all documents with a designation that sounds
             * like "hart ventricle" and limits the result list to size 6 -
             * sorts by "matchToQuery" - for the "best" results first, and then
             * by conceptCode if two results match equally well.
             */

            // System.out.println("Example sounds like query for 'hart ventricle'");
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            //
            // cns.restrictToMatchingDesignations("hart ventricle", false,
            // "DoubleMetaphoneLuceneQuery",
            // null);
            //
            // LocalNameList sort = cm.createLocalNameList(new
            // String[]{"matchToQuery", "code"});
            // list = cns.resolveToList(sort, null, 6);
            //
            // System.out.println(ObjectToString.toString(list));
            /*
             * Example of a non-case sensitive "startsWith" query
             */

            // System.out.println("StartsWith query for 'Malposition of'");
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            //
            // cns.restrictToMatchingDesignations("Malposition of", false,
            // "startsWith", null);
            //
            // System.out.println(ObjectToString.toString(cns.resolveToList(null,
            // null, 0)));
            /*
             * example of an non-case sensitive exact match query
             */

            // System.out.println("Exact match query for 'Office Nursing'");
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            //
            // cns.restrictToMatchingDesignations("Office Nursing", false,
            // "exactMatch", null);
            //
            // System.out.println(ObjectToString.toString(cns.resolveToList(null,
            // null, 0)));
            /*
             * Example of restrict to codes
             */

            // System.out.println("Restrict to codes");
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            // ConceptReferenceList crl = cm.createConceptReferenceList(new
            // String[]{"C14199"},
            // codingScheme);
            // cns.restrictToCodes(crl);
            // ResolvedConceptReferencesIterator foo = cns.resolve(null,
            // cm.createLocalNameList("invalid"));
            // while (foo.hasNext())
            // {
            // ResolvedConceptReference[] foo2 =
            // foo.next(1000).getResolvedConceptReference();
            // for (int i = 0; i < foo2.length; i++)
            // {
            // System.out.println(foo2[i].getConceptCode());
            // // System.out.println(ObjectToString.toString(foo2[i]));
            // }
            // }
            // debugging....
            //            
            // CodedNodeSet nodes = lbs.getCodingSchemeConcepts("GEMET", null,
            // false);
            // nodes.restrictToMatchingProperties(ConvenienceMethods.createLocalNameList("textualPresentation"),
            // "carbon monoxide", "exactMatch", null);
            //            
            // System.out.println(ObjectToString.toString(nodes.resolveToList(null,
            // null, 0)));
            /*
             * example of isCodeInGraph
             */

            // cng = lbs.getNodeGraph(codingScheme, null, null);
            // ConceptReferenceList associations =
            // cm.createConceptReferenceList(new String[]{
            // "Allele_Associated_With_Disease", "Allele_Has_Abnormality"});
            // cng.restrictToAssociations(associations, null);
            //
            // System.out.println("Is Code In Graph: "
            // + cng.isCodeInGraph(cm.createConceptReference("C46061",
            // codingScheme)));
            /*
             * Example of areCodesRelated
             */

            // cng = lbs.getNodeGraph(codingScheme, null, null);
            //
            // ConceptReference asn = cm.createConceptReference("hasSubtype",
            // null);
            //
            // ConceptReference ref1 = cm.createConceptReference("C1012",
            // codingScheme);
            // ConceptReference ref2 = cm.createConceptReference("C47443",
            // codingScheme);
            //
            // System.out.println("Are Codes Related: " +
            // cng.areCodesRelated(asn, ref1, ref2, true));
            //
            // ConceptReference ref3 = cm.createConceptReference("C2516",
            // codingScheme);
            //
            // System.out.println("Are Codes Related: " +
            // cng.areCodesRelated(asn, ref1, ref3, true));
            /*
             * Example of listCodeRelationships
             */

            // cng = lbs.getNodeGraph(codingScheme, null, null);
            //
            // ConceptReference ref4 = cm.createConceptReference("C1012",
            // codingScheme);
            // ConceptReference ref5 = cm.createConceptReference("C47443",
            // codingScheme);
            //
            // System.out.println("ListCodeRelationships: "
            // + ObjectToString.toString(cng.listCodeRelationships(ref4, ref5,
            // true)));
            //            
            // System.out.println("ListCodeRelationships: (not direct only)"
            // + ObjectToString.toString(cng.listCodeRelationships(ref4, ref5,
            // false)));
            /*
             * Create a graph, restrict it to the source Code C19442. Then,
             * print the graph, focused on C19442, walking both down and up to a
             * depth of 2.
             */

            // cng = lbs.getNodeGraph(codingScheme, null, null);
            // cng.restrictToSourceCodes(cm.createCodedNodeSet(new
            // String[]{"C19442"}, codingScheme));
            //
            // System.out.println(ObjectToString.toString(cng.resolveAsList(cm.createConceptReference("C19442",
            // codingScheme), true,
            // true, 1, 2, null, null, 50)));
            /*
             * Print the same graph defined above as a node list.
             */
            // cng = lbs.getNodeGraph(codingScheme, null, null);
            // cng.restrictToSourceCodes(cm.createCodedNodeSet(new
            // String[]{"C19442"}, codingScheme));
            //
            // System.out.println("Convert graph to node list:");
            // System.out.println(ObjectToString.toString(cng
            // .toNodeList(cm.createConceptReference("C19442", codingScheme),
            // true, true).resolveToList(null,
            // null, 0)));
            //
            /*
             * Test out the convenience method to get the next set of children
             * in a graph...
             */

            // System.out.println("Get the immediate children of (hasSubtype) C19442");
            // System.out.println(ObjectToString.toString(cm.getAssociationForwardOneLevel("C19442",
            // null, "hasSubtype",
            // codingScheme,
            // false)));
            /*
             * Test out the convenience method to get the next set of parents in
             * a graph...
             */

            // System.out.println("Get the immediate parents of (hasSubtype) C19442");
            // System.out.println(ObjectToString.toString(cm.getAssociationReverseOneLevel("C19442",
            // null, "hasSubtype",
            // codingScheme,
            // false)));
            /*
             * Test the convenience methods for going from CONCEPT_NAME to code
             */
            // System.out.println(cm.nameToCode("Sympathetic_Nervous_System",
            // codingScheme));
            // /*
            // * Test the convenience methods for going from code to
            // CONCEPT_NAME
            // */
            // System.out.println(cm.codeToName("C23797", codingScheme));
            //             
            // System.out.println(cm.codeToName("GM", "Automobiles"));
            // System.out.println(cm.nameToCode("General Motors",
            // "Automobiles"));
            //             
            //             
            // System.out.println(ObjectToString.toString(lbs.getNodeGraph(codingScheme,
            // null, null)
            // .resolveAsList(
            // ConvenienceMethods.createConceptReference("C12219",
            // codingScheme),
            // false, true, 1, 1, new LocalNameList(), null, 1024)));
            // System.out.println(ObjectToString.toString(
            // lbs.getNodeGraph(codingScheme, null, null)
            // .resolveAsList(
            // ConvenienceMethods.createConceptReference("C43652",
            // codingScheme),
            // false, true, 1, 1, new LocalNameList(), null, 1024)));
            // iterator tests
            // System.out.println("Example double restriction query");
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            //
            // cns.restrictToMatchingDesignations("heart", false,
            // MatchAlgorithms.LuceneQuery.toString(),
            // null);
            //
            // ResolvedConceptReferencesIterator iter = cns.resolve(null, null);
            //
            // System.out.println("Has next? " + iter.hasNext());
            //            
            // while (iter.hasNext())
            // {
            // System.out.println("sleeping for 3 minutes");
            // Thread.sleep(180000);
            //                
            // }
            //            
            // System.out.println(ObjectToString.toString(iter.next(10)));
            // get entire coding scheme...
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            // System.out.println("Constructing the iterator");
            // long temp = System.currentTimeMillis();
            // ResolvedConceptReferencesIterator iter =
            // cns.resolve(Constructors.createLocalNameList("code"),
            // null);
            // System.out.println(System.currentTimeMillis() - temp);
            // System.out.println("Test of the undocumented features - search by
            // concept code (with wild
            // cards)");
            // cns = lbs.getCodingSchemeConcepts(codingScheme, null, false);
            //
            // cns.restrictToMatchingProperties(cm.createLocalNameList("conceptCode"),
            // "C1220*",
            // "LuceneQuery", null);
            //
            // System.out.println(ObjectToString.toString(cns.resolveToList(null,
            // null, 0)));
            //            
            // System.out.println("Test of the undocumented features - search by concept status");
            // cns = lbs.getCodingSchemeConcepts("Automobiles", null, false);
            //
            // cns.restrictToMatchingProperties(cm.createLocalNameList("conceptStatus"),
            // "Retired",
            // "LuceneQuery", null);
            //
            // System.out.println(ObjectToString.toString(cns.resolveToList(null,
            // null, 0)));
            /*
             * NCI Metathesaurus test
             */
            // System.out.println("StartsWith query for 'heart'");
            // cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null,
            // false);
            //
            // cns.restrictToMatchingDesignations("heart", false, "startsWith",
            // null);
            //
            // LocalNameList sort = cm.createLocalNameList(new
            // String[]{"matchToQuery", "code"});
            // System.out.println(ObjectToString.toString(cns.resolveToList(sort,
            // null, 5)));
            //            
            // System.out.println("StartsWith query for 'brain'");
            // cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null,
            // false);
            //
            // cns.restrictToMatchingDesignations("brain", false, "startsWith",
            // null);
            //
            // System.out.println(ObjectToString.toString(cns.resolveToList(sort,
            // null, 5)));
            // CodedNodeGraph cng = lbs.getNodeGraph("NCI MetaThesaurus", null,
            // null);
            //                
            // cng.restrictToAssociations(ConvenienceMethods.createConceptReferenceList("hasSubtype"),
            // null);
            //                
            // System.out.println(ObjectToString.toString(cng.resolveAsList(ConvenienceMethods.createConceptReference("CL347268",
            // "NCI MetaThesaurus"), true, false, -1, 1,
            // ConvenienceMethods.createLocalNameList("Invalid"),
            // null, 0)));
            // System.out.println("Restrict to 1 codes");
            // cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null,
            // false);
            //
            // crl = cm.createConceptReferenceList(new String[]{"C0000039"},
            // "NCI MetaThesaurus");
            // cns.restrictToCodes(crl);
            //          
            // long time = System.currentTimeMillis();
            // Object foo = cns.resolveToList(null, null, 0);
            // System.out.println((System.currentTimeMillis() - time) + "");
            //
            // System.out.println(ObjectToString.toString(foo));
            /*
             * Test deactivator code.
             */

            // Date time = new Date(System.currentTimeMillis() + 30 * 1000);
            // System.out.println(time.getTime());
            // System.out.println(time);
            // lbs
            // .getServiceManager(null)
            // .deactivateCodingSchemeVersion(
            // Constructors
            // .createAbsoluteCodingSchemeVersionReference(
            // "urn:oid:11.11.0.2",
            // "2.0"), time);
            //
            // while (true)
            // {
            // Thread.sleep(5000);
            // lbs.getCodingSchemeConcepts("urn:oid:11.11.0.2", null, false);
            // }
            // Run a test of doing a union of all of the code systems, and then
            // doing various queries.

            // test isCodeRetired convenience method.
            // System.out.println(cm.isCodeRetired("C10906","NCI_Thesaurus",
            // ConvenienceMethods.createProductionTag()));
            // System.out.println(cm.isCodeRetired("C1000","NCI_Thesaurus",
            // ConvenienceMethods.createProductionTag()));
            // System.out.println(cm.isCodeRetired("fred","NCI_Thesaurus",
            // ConvenienceMethods.createProductionTag()));
            //            
            // String query = "aa";
            //            
            // testHelp(lbs, query, "startsWith",
            // ConvenienceMethods.createLocalNameList("invalidProper<"), 25 );
            //            
            // long avg = 0;
            // for (int i = 0; i < 50; i++)
            // {
            // query = DBUtility.computeNextIdentifier(query);
            // avg += testHelp(lbs, query, "startsWith", null, 25 );
            // }
            //            
            // System.out.println("Average " + avg / 50);
            //            
            // avg = 0;
            // query = DBUtility.computeNextIdentifier(query);
            // avg += testHelp(lbs, "head", "LuceneQuery", null, 10 );
            //
            //            
            // query = DBUtility.computeNextIdentifier(query);
            // avg += testHelp(lbs, "heart", "LuceneQuery", null, 10 );
            //            
            // query = DBUtility.computeNextIdentifier(query);
            // avg += testHelp(lbs, "brain", "LuceneQuery", null, 10 );
            //            
            // query = DBUtility.computeNextIdentifier(query);
            // avg += testHelp(lbs, "lung attack", "LuceneQuery", null, 10 );
            //            
            // query = DBUtility.computeNextIdentifier(query);
            // avg += testHelp(lbs, "strange words", "LuceneQuery", null, 10 );
            //            
            // System.out.println("Average " + avg / 5);
            // System.out.println("Get rendering detail for a coding scheme");
            // System.out.println(ObjectToString.toString(cm.getRenderingDetail("NCI Thesaurus",
            // null)));

            cns = lbs.getCodingSchemeConcepts("Dictyostelium discoideum anatomy", null);

            // System.out.println("end  nodes");
            // System.out.println(ObjectToString.toString(cm.getEndNodes("gene_ontology",
            // null, null, null)));

            // regexp on code test.
            cns = lbs.getCodingSchemeConcepts("mesh_ABDEGH.go", null);
            cns.restrictToMatchingProperties(Constructors.createLocalNameList("conceptCode"), null,
                    "mesh:h\\.01\\.671\\.538", MatchAlgorithms.RegExp.name(), null);

            System.out.println(ObjectToString.toString(cns.resolveToList(null, null, null, 50)));

            // CodedNodeGraph cng = lbs.getNodeGraph(codingScheme, null, null);
            // cng.restrictToAssociations(Constructors.createNameAndValueList("Gene_Product_Encoded_By_Gene"),
            // Constructors.createNameAndValueList("someValuesFrom"));
            // System.out.println(ObjectToString.toString(cng.resolveAsList(Constructors.createConceptReference("C17324",
            // null),
            // true, false, 1, 1, Constructors.createLocalNameList("invalid"),
            // null, -1)));

            // cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null);
            // cns.restrictToMatchingProperties(null, new PropertyType[]
            // {PropertyType.DEFINITION},
            // Constructors.createLocalNameList("MSH"),
            // null, null, "peripheral tissue",
            // MatchAlgorithms.LuceneQuery.toString(), null);
            //            
            // System.out.println(ObjectToString.toString(cns.resolveToList(null,
            // null, 5)));

            // cns.restrictToMatchingProperties(Constructors.createLocalNameList("conceptCode"),"DDANAT:000013?",
            // MatchAlgorithms.LuceneQuery.toString(),null);
            //            
            // System.out.println(ObjectToString.toString(cns.resolveToList(null,
            // null, 50)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long testHelp(LexBIGService lbs, String query, String algorithm, LocalNameList resolveProps,
            int resultCount) throws LBInvocationException, LBParameterException, LBException {
        CodingSchemeRendering[] csr = lbs.getSupportedCodingSchemes().getCodingSchemeRendering();
        CodedNodeSet master = lbs.getCodingSchemeConcepts(csr[0].getCodingSchemeSummary().getCodingSchemeURI(),
                ConvenienceMethods.createProductionTag());

        for (int i = 1; i < csr.length; i++) {
            if (csr[i].getRenderingDetail().getVersionStatus().equals(CodingSchemeVersionStatus.ACTIVE)) {
                master.union(lbs.getCodingSchemeConcepts(csr[i].getCodingSchemeSummary().getCodingSchemeURI(),
                        ConvenienceMethods.createProductionTag()));
            }
        }

        master.restrictToMatchingDesignations(query, SearchDesignationOption.ALL, algorithm, null);
        long temp = System.currentTimeMillis();
        ResolvedConceptReference[] r = master.resolveToList(null, resolveProps, null, resultCount)
                .getResolvedConceptReference();
        System.out.println("Result count - " + r.length);

        long time = System.currentTimeMillis() - temp;
        System.out.println("---- Resolve Time ---- " + time + "");
        return time;
    }

}