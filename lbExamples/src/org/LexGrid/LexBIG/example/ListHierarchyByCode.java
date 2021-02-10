
package org.LexGrid.LexBIG.example;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

/**
 * Example showing how to determine and display the hierarchical relationships
 * for a specific code, ancestors or descendants, within a fixed distance.
 * 
 * This program accepts two parameters, indicating the code and distance. The
 * first parameter is the code (required). The second parameter is the distance
 * (optional). If 1, immediate children are displayed. If 2, grandchildren are
 * displayed, etc. If absent or < 0, all downstream branches are displayed.
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
public class ListHierarchyByCode {
    final static int DEFAULT_DISTANCE = -1;

    public ListHierarchyByCode() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: ListHierarchyByCode \"C0000\" 2");
            return;
        }
        ;

        try {
            String code = args[0];
            int distance = args.length == 1 ? DEFAULT_DISTANCE : Integer.parseInt(args[1]);
            new ListHierarchyByCode().run(code, distance);
        } catch (NumberFormatException nfe) {
            System.out.println("Parameter 2 must indicate a number representing maximum distance to display.\n"
                    + "Example: ListHierarchyByCode \"C0000\" 2");
            return;
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String code, int maxDistance) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        long ms = System.currentTimeMillis();
        try {
            if (css != null) {
                LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
                LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbSvc
                        .getGenericExtension("LexBIGServiceConvenienceMethods");

                String scheme = css.getCodingSchemeURI();
                CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
                csvt.setVersion(css.getRepresentsVersion());
                String desc = null;
                try {
                    desc = lbscm.createCodeNodeSet(new String[] { code }, scheme, csvt).resolveToList(null, null, null,
                            1).getResolvedConceptReference(0).getEntityDescription().getContent();
                } catch (Exception e) {
                    desc = "<not found>";
                }
                Util.displayMessage("============================================================");
                Util.displayMessage("Focus code: " + code + ":" + desc);
                Util.displayMessage("============================================================");

                // Iterate through all hierarchies and levels ...
                String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
                for (int i = 0; i < hierarchyIDs.length; i++) {
                    String hierarchyID = hierarchyIDs[i];
                    String reportedDistance = maxDistance >= 0 ? Integer.toString(maxDistance) : "ALL";
                    Util.displayMessage("------------------------------------------------------------");
                    Util.displayMessage("Hierarchy ID: " + hierarchyID);
                    Util.displayMessage("------------------------------------------------------------");
                    Util.displayMessage("Paths to ancestors (distance <= " + reportedDistance + ") ...");
                    printLevelPrev(lbscm, scheme, csvt, hierarchyID, code, maxDistance, 0);
                    Util.displayMessage("");
                    Util.displayMessage("Paths to descendants (distance <= " + reportedDistance + ") ...");
                    printLevelNext(lbscm, scheme, csvt, hierarchyID, code, maxDistance, 0);
                    Util.displayMessage("");
                }
            }
        } finally {
            System.out.println("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
    }

    /**
     * Displays ancestors for a code within the specified hierarchy up to the
     * given distance or until paths to root nodes are exhausted, recursing as
     * necessary.
     * 
     * @param lbcsm
     * @param scheme
     * @param csvt
     * @param hierarchyID
     * @param code
     * @param maxDistance
     * @param currentDistance
     * @throws LBException
     */
    protected void printLevelPrev(LexBIGServiceConvenienceMethods lbscm, String scheme, CodingSchemeVersionOrTag csvt,
            String hierarchyID, String code, int maxDistance, int currentDistance) throws LBException {
        if (maxDistance < 0 || currentDistance < maxDistance) {
            StringBuffer indent = new StringBuffer();
            for (int i = 0; i <= currentDistance; i++)
                indent.append("    ");

            AssociationList associations = lbscm.getHierarchyLevelPrev(scheme, csvt, hierarchyID, code, false, null);
            for (int i = 0; i < associations.getAssociationCount(); i++) {
                Association assoc = associations.getAssociation(i);
                AssociatedConceptList concepts = assoc.getAssociatedConcepts();
                for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                    AssociatedConcept concept = concepts.getAssociatedConcept(j);
                    String prevCode = concept.getConceptCode();
                    String prevDesc = concept.getEntityDescription() == null ? "NO DESCRIPTION" : concept
                            .getEntityDescription().getContent();
                    Util.displayMessage(indent + assoc.getDirectionalName() + "<-" + prevCode + ":" + prevDesc);
                    printLevelPrev(lbscm, scheme, csvt, hierarchyID, prevCode, maxDistance, currentDistance + 1);
                }
            }
        }
    }

    /**
     * Displays descendants for a code within the specified hierarchy up to the
     * given distance or until all paths to leaf nodes is exhausted, recursing
     * as necessary.
     * 
     * @param lbscm
     * @param scheme
     * @param csvt
     * @param hierarchyID
     * @param code
     * @param maxDistance
     * @param currentDistance
     * @throws LBException
     */
    protected void printLevelNext(LexBIGServiceConvenienceMethods lbscm, String scheme, CodingSchemeVersionOrTag csvt,
            String hierarchyID, String code, int maxDistance, int currentDistance) throws LBException {
        if (maxDistance < 0 || currentDistance < maxDistance) {
            StringBuffer indent = new StringBuffer();
            for (int i = 0; i <= currentDistance; i++)
                indent.append("    ");

            AssociationList associations = lbscm.getHierarchyLevelNext(scheme, csvt, hierarchyID, code, false, null);
            for (int i = 0; i < associations.getAssociationCount(); i++) {
                Association assoc = associations.getAssociation(i);
                AssociatedConceptList concepts = assoc.getAssociatedConcepts();
                for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                    AssociatedConcept concept = concepts.getAssociatedConcept(j);
                    String nextCode = concept.getConceptCode();
                    String nextDesc = concept.getEntityDescription().getContent();
                    Util.displayMessage(indent + assoc.getDirectionalName() + "->" + nextCode + ":" + nextDesc);
                    printLevelNext(lbscm, scheme, csvt, hierarchyID, nextCode, maxDistance, currentDistance + 1);
                }
            }
        }
    }
}