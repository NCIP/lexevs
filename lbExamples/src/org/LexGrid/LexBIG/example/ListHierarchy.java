
package org.LexGrid.LexBIG.example;

import java.util.Arrays;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.commonTypes.EntityDescription;

/**
 * Example showing how to determine and display an unsorted list of root and
 * subsumed nodes, up to a specified depth, for hierarchical relationships.
 * 
 * This program accepts two parameters:
 * 
 * The first parameter indicates the depth to display for the hierarchy. If 1,
 * nodes immediately subsumed by the root are displayed. If 2, grandchildren are
 * displayed, etc. If absent or < 0, a default depth of 3 is assumed.
 * 
 * The second parameter optionally indicates a specific hierarchy to navigate.
 * If provided, this must match a registered identifier in the coding scheme
 * supported hierarchy metadata. If left unspecified, all hierarchical
 * associations are navigated. If an incorrect value is specified, a list of
 * supported values will be output for future reference.
 * 
 * BACKGROUND: From a database perspective, LexBIG stores relationships
 * internally in a forward direction, source to target. Due to differences in
 * source formats, however, a wide variety of associations may be used ('PAR',
 * 'CHD', 'isa', 'hasSubtype', etc). In addition, the direction of navigation
 * may vary ('isa' expands in a reverse direction whereas 'hasSubtype' expands
 * in a forward direction.
 * 
 * The intent of the getHierarchy* methods on the
 * LexBIGServiceConvenienceMethods interface is to simplify the process of
 * hierarchy discovery and navigation. These methods significantly reduce the
 * need to understand conventions for root nodes, associations, and direction of
 * navigation for a specific source format.
 * 
 */
public class ListHierarchy {
    final static int DEFAULT_DEPTH = 3;

    public ListHierarchy() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            int maxDepth = args.length < 1 ? DEFAULT_DEPTH : Integer.parseInt(args[0]);
            String hID = args.length < 2 ? null : args[1];
            new ListHierarchy().run(maxDepth, hID);
        } catch (NumberFormatException nfe) {
            System.out.println("Parameter 1 must indicate a maximum depth of the hierarchy to display.\n"
                    + "Example: ListHierarchy 3");
            return;
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(int maxDepth, String hID) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        long ms = System.currentTimeMillis();
        try {
            if (css != null) {
                Util.displayMessage("Displaying " + (maxDepth > 0 ? Integer.toString(maxDepth) : "ALL")
                        + " levels of hierarchy: " + (hID == null ? "ANY" : hID));

                LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
                String scheme = css.getCodingSchemeURI();
                CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
                csvt.setVersion(css.getRepresentsVersion());
                printHierarchies(lbSvc, scheme, csvt, maxDepth, hID);
            }
        } finally {
            System.out.println("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
    }

    /**
     * Discovers all registered hierarchies for the coding scheme and display
     * each in turn.
     * 
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @param maxDepth
     * @param hierarchyID
     * @throws LBException
     */
    protected void printHierarchies(LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt, int maxDepth,
            String hierarchyID) throws LBException {
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbSvc
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Validate the ID, if specified ...
        if (hierarchyID != null) {
            String[] supportedIDs = lbscm.getHierarchyIDs(scheme, csvt);
            Arrays.sort(supportedIDs);
            if (Arrays.binarySearch(supportedIDs, hierarchyID) < 0) {
                Util.displayMessage("The specified hierarchy identifier is not supported by the selected code system.");
                Util.displayMessage("Supported values: ");
                for (String id : supportedIDs)
                    Util.displayMessage("    " + id);
                return;
            }
        }

        // Print all branches from root ...
        ResolvedConceptReferenceList roots = lbscm.getHierarchyRoots(scheme, csvt, hierarchyID);
        for (int j = 0; j < roots.getResolvedConceptReferenceCount(); j++) {
            ResolvedConceptReference root = roots.getResolvedConceptReference(j);
            printHierarchyBranch(lbscm, scheme, csvt, null, root, 1, maxDepth, null);
        }
    }

    /**
     * Handles recursive display of hierarchy for the given start node, up to
     * the maximum specified depth.
     * 
     * @param lbscm
     * @param scheme
     * @param csvt
     * @param hierarchyID
     * @param branchRoot
     * @param currentDepth
     * @param maxDepth
     * @param assocName
     * @throws LBException
     */
    protected void printHierarchyBranch(LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, String hierarchyID, ResolvedConceptReference branchRoot, int currentDepth,
            int maxDepth, String assocName) throws LBException {
        // Print the referenced node; indent based on current depth ...
        StringBuffer indent = new StringBuffer();
        for (int i = 0; i < currentDepth; i++)
            indent.append("    ");

        String code = branchRoot.getConceptCode();
        EntityDescription desc = branchRoot.getEntityDescription();
        Util.displayMessage(new StringBuffer().append(indent).append(assocName != null ? (assocName + "->") : "")
                .append(code).append(':').append(desc != null ? desc.getContent() : "").toString());

        // Print each association and recurse ...
        if (currentDepth < maxDepth) {
            AssociationList assocList = lbscm.getHierarchyLevelNext(scheme, csvt, hierarchyID, code, false, null);
            if (assocList != null)
                for (int i = 0; i < assocList.getAssociationCount(); i++) {
                    Association assoc = assocList.getAssociation(i);
                    AssociatedConceptList nodes = assoc.getAssociatedConcepts();
                    for (Iterator<? extends AssociatedConcept> subsumed = nodes.iterateAssociatedConcept(); subsumed.hasNext();) {
                        printHierarchyBranch(lbscm, scheme, csvt, hierarchyID, subsumed.next(), currentDepth + 1,
                                maxDepth, assoc.getDirectionalName());
                    }
                }
        }
    }

}