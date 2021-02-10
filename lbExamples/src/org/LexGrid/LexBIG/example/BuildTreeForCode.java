
package org.LexGrid.LexBIG.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.HierarchyPathResolveOption;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.naming.SupportedHierarchy;
import org.apache.commons.lang.StringUtils;

/**
 * Attempts to provide a tree, based on a focus code, that includes the
 * following information:
 * 
 * <pre>
 * - All paths from the hierarchy root to one or more focus codes.
 * - Immediate children of every node in path to root
 * - Indicator to show whether any unexpanded node can be further expanded
 * </pre>
 * 
 * This example accepts two parameters... The first parameter is required, and
 * must contain at least one code in a comma-delimited list. A tree is produced
 * for each code. Time to produce the tree for each code is printed in
 * milliseconds. In order to factor out costs of startup and shutdown, resolving
 * multiple codes may offer a better overall estimate performance.
 * 
 * The second parameter is optional, and can indicate the identifier of a
 * supported hierarchy for the coding scheme to navigate when resolving
 * child nodes. If not provided, "is_a" is assumed.
 * 
 * Note: This example is written to handle sources that define the supported
 * hierarchy in a uni-directional fashion.  In particular, it does not
 * support the Metathesaurus, which can define hierarchical relationships
 * using both forward and backward navigation.  For examples showing
 * techniques to navigate the Metathesaurus, please refer to other examples
 * such as BuildTreeForMetaCodeBySource, ListHierarchyMetaBySource and
 * FindUMLSContextsForCUI.
 */
public class BuildTreeForCode {
    LocalNameList noopList_ = Constructors.createLocalNameList("textualPresentation");

    public BuildTreeForCode() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: BuildTreeForCode \"73,GM is_a\"");
            return;
        }

        try {
            // Prompt for a coding scheme to run against...
            CodingSchemeSummary css = Util.promptForCodeSystem();
            if (css != null) {

                // Declare common service classes for processing ...
                LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();
                LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbsvc
                        .getGenericExtension("LexBIGServiceConvenienceMethods");

                // Pull the URI and version from the selected value ...
                String scheme = css.getCodingSchemeURI();
                CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();

                // For any given ontology, we know the hierarchy
                // information. But to keep the code as generic
                // as possible and protect against changes, we
                // resolve the supported hierarchy info from the
                // coding scheme...
                String hierarchyID = args.length > 1 ? args[1] : "is_a";
                SupportedHierarchy hierarchyDefn = getSupportedHierarchy(lbsvc, scheme, csvt, hierarchyID);

                // Loop through each provided code.
                // The time to produce the tree for each code is printed in
                // milliseconds. In order to factor out costs of startup
                // and shutdown, resolving multiple codes may offer a
                // better overall estimate performance.
                String[] codes = args[0].split(",");
                for (String code : codes)
                    new BuildTreeForCode().run(lbsvc, lbscm, scheme, csvt, hierarchyDefn, code);
            }
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    /**
     * Prints the tree for an individual code.
     */
    public void run(LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, SupportedHierarchy hierarchyDefn, String focusCode) throws LBException {

        // Print a header and define a new tree for the code being processed.
        Util.displayMessage("============================================================");
        Util.displayMessage("Focus code: " + focusCode);
        Util.displayMessage("============================================================");

        TreeItem ti = new TreeItem("<Start>", "Start of Tree");
        long ms = System.currentTimeMillis();
        int pathsResolved = 0;
        try {

            // Resolve hierarchy info by ID. This example will
            // need to make some calls outside of what is covered
            // by existing convenience methods, but we use the
            // registered hierarchy to prevent need to hard code
            // relationship and direction info used on lookup ...
            String hierarchyID = hierarchyDefn.getLocalId();
            String[] associationsToNavigate = hierarchyDefn.getAssociationNames();
            boolean associationsNavigatedFwd = hierarchyDefn.getIsForwardNavigable();

            // Identify the set of all codes on path from root
            // to the focus code ...
            Map<String, EntityDescription> codesToDescriptions = new HashMap<String, EntityDescription>();
            AssociationList pathsFromRoot = getPathsFromRoot(lbsvc, lbscm, scheme, csvt, hierarchyID, focusCode,
                    codesToDescriptions);

            // Typically there will be one path, but handle multiple just in
            // case. Each path from root provides a 'backbone', from focus
            // code to root, for additional nodes to hang off of in our
            // printout. For every backbone node, one level of children is
            // printed, along with an indication of whether those nodes can
            // be expanded.
            for (Iterator<? extends Association> paths = pathsFromRoot.iterateAssociation(); paths.hasNext();) {
                addPathFromRoot(ti, lbsvc, lbscm, scheme, csvt, paths.next(), associationsToNavigate,
                        associationsNavigatedFwd, codesToDescriptions);
                pathsResolved++;
            }

        } finally {
            System.out.println("Run time (milliseconds): " + (System.currentTimeMillis() - ms) + " to resolve "
                    + pathsResolved + " paths from root.");
        }

        // Print the result ..
        printTree(ti, focusCode, 0);
    }

    /**
     * The given path represents a multi-tier association with associated
     * concepts and targets. This method expands the association content and
     * adds results to the given tree item, recursing as necessary to process
     * each level in the path.
     * <p>
     * Nodes in the association act as the backbone for the display. For each
     * backbone node, additional children are resolved one level deep along with
     * an indication of further expandability.
     */
    protected void addPathFromRoot(TreeItem ti, LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm,
            String scheme, CodingSchemeVersionOrTag csvt, Association path, String[] associationsToNavigate,
            boolean associationsNavigatedFwd, Map<String, EntityDescription> codesToDescriptions) throws LBException {

        // First, add the branch point from the path ...
        ConceptReference branchRoot = path.getAssociationReference();
        String branchCode = branchRoot.getCode();
        String branchCodeDescription = codesToDescriptions.containsKey(branchCode) ? codesToDescriptions
                .get(branchCode).getContent() : getCodeDescription(lbsvc, scheme, csvt, branchCode);

        TreeItem branchPoint = new TreeItem(branchCode, branchCodeDescription);
        String branchNavText = getDirectionalLabel(lbscm, scheme, csvt, path, associationsNavigatedFwd);

        // Now process elements in the branch ...
        AssociatedConceptList concepts = path.getAssociatedConcepts();
        for (int i = 0; i < concepts.getAssociatedConceptCount(); i++) {

            // Determine the next concept in the branch and
            // add a corresponding item to the tree.
            AssociatedConcept concept = concepts.getAssociatedConcept(i);
            String code = concept.getCode();
            TreeItem branchItem = new TreeItem(code, getCodeDescription(concept));
            branchPoint.addChild(branchNavText, branchItem);

            // Recurse to process the remainder of the backbone ...
            AssociationList nextLevel = concept.getSourceOf();
            if (nextLevel != null) {
                if (nextLevel.getAssociationCount() != 0) {
                    // Add immediate children of the focus code with an
                    // indication of sub-nodes (+).  Codes already
                    // processed as part of the path are ignored since
                    // they are handled through recursion.
                    addChildren(branchItem, lbsvc, lbscm, scheme, csvt, code, codesToDescriptions.keySet(),
                            associationsToNavigate, associationsNavigatedFwd);

                    // More levels left to process ...
                    for (int j = 0; j < nextLevel.getAssociationCount(); j++)
                        addPathFromRoot(branchPoint, lbsvc, lbscm, scheme, csvt, nextLevel.getAssociation(j),
                                associationsToNavigate, associationsNavigatedFwd, codesToDescriptions);
                } else {
                    // End of the line ...
                    // Always add immediate children of the focus code,
                    // in this case with no exclusions since we are moving
                    // beyond the path to root and allowed to duplicate
                    // nodes that may have occurred in the path to root.
                    addChildren(branchItem, lbsvc, lbscm, scheme, csvt, code, Collections.EMPTY_SET,
                            associationsToNavigate, associationsNavigatedFwd);
                }
            } else {
                // Add immediate children of the focus code with an
                // indication of sub-nodes (+).  Codes already
                // processed as part of the path are ignored since
                // they are handled through recursion.
                addChildren(branchItem, lbsvc, lbscm, scheme, csvt, code, codesToDescriptions.keySet(),
                        associationsToNavigate, associationsNavigatedFwd);
            }
        }
        
        // Add immediate children of the node being processed,
        // and indication of sub-nodes.
        addChildren(branchPoint, lbsvc, lbscm, scheme, csvt, branchCode, codesToDescriptions.keySet(),
                associationsToNavigate, associationsNavigatedFwd);

        // Add the populated tree item to those tracked from root.
        ti.addChild(branchNavText, branchPoint);
    }

    /**
     * Populate child nodes for a single branch of the tree, and indicates
     * whether further expansion (to grandchildren) is possible.
     */
    protected void addChildren(TreeItem ti, LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, String branchRootCode, Set<String> codesToExclude,
            String[] associationsToNavigate, boolean associationsNavigatedFwd) throws LBException {

        // Resolve the next branch, representing children of the given
        // code, navigated according to the provided relationship and
        // direction. Resolve the children as a code graph, looking 2
        // levels deep but leaving the final level unresolved.
        CodedNodeGraph cng = lbsvc.getNodeGraph(scheme, csvt, null);
        ConceptReference focus = Constructors.createConceptReference(branchRootCode, scheme);
        cng = cng.restrictToAssociations(Constructors.createNameAndValueList(associationsToNavigate), null);
        ResolvedConceptReferenceList branch = cng.resolveAsList(focus, associationsNavigatedFwd,
                !associationsNavigatedFwd, -1, 2, noopList_, null, null, null, -1, true);

        // The resolved branch will be represented by the first node in
        // the resolved list. The node will be subdivided by source or
        // target associations (depending on direction). The associated
        // nodes define the children.
        for (ResolvedConceptReference node : branch.getResolvedConceptReference()) {
            AssociationList childAssociationList = associationsNavigatedFwd ? node.getSourceOf() : node.getTargetOf();

            // Process each association defining children ...
            if(childAssociationList != null){
            for (Association child : childAssociationList.getAssociation()) {
                String childNavText = getDirectionalLabel(lbscm, scheme, csvt, child, associationsNavigatedFwd);

                // Each association may have multiple children ...
                AssociatedConceptList branchItemList = child.getAssociatedConcepts();
                for (AssociatedConcept branchItemNode : branchItemList.getAssociatedConcept()) {
                    String branchItemCode = branchItemNode.getCode();

                    // Add here if not in the list of excluded codes.
                    // This is also where we look to see if another level
                    // was indicated to be available. If so, mark the
                    // entry with a '+' to indicate it can be expanded.
                    if (!codesToExclude.contains(branchItemCode)) {
                        TreeItem childItem = new TreeItem(branchItemCode, getCodeDescription(branchItemNode));
                        AssociationList grandchildBranch = associationsNavigatedFwd ? branchItemNode.getSourceOf()
                                : branchItemNode.getTargetOf();
                        if (grandchildBranch != null)
                            childItem.expandable = true;
                        ti.addChild(childNavText, childItem);
                    }
                }
            }
        }
        }
    }

    /**
     * Prints the given tree item, recursing through all branches.
     * 
     * @param ti
     */
    protected void printTree(TreeItem ti, String focusCode, int depth) {
        StringBuffer indent = new StringBuffer();
        for (int i = 0; i < depth * 2; i++)
            indent.append("| ");

        StringBuffer codeAndText = new StringBuffer(indent)
                .append(focusCode.equals(ti.code) ? ">>>>" : "")
                .append(ti.code).append(':').append(StringUtils.abbreviate(ti.text, 60))
                .append(ti.expandable ? " [+]" : "");
        Util.displayMessage(codeAndText.toString());

        indent.append("| ");
        for (String association : ti.assocToChildMap.keySet()) {
            Util.displayMessage(indent.toString() + association);
            List<TreeItem> children = ti.assocToChildMap.get(association);
            Collections.sort(children);
            for (TreeItem childItem : children)
                printTree(childItem, focusCode, depth + 1);
        }
    }

    // /////////////////////////////////////////////////////
    // Helper Methods
    // /////////////////////////////////////////////////////

    /**
     * Returns the entity description for the given code.
     */
    protected String getCodeDescription(LexBIGService lbsvc, String scheme, CodingSchemeVersionOrTag csvt, String code)
            throws LBException {

        CodedNodeSet cns = lbsvc.getCodingSchemeConcepts(scheme, csvt);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(code, scheme));
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, noopList_, null, 1);
        if (rcrl.getResolvedConceptReferenceCount() > 0) {
            EntityDescription desc = rcrl.getResolvedConceptReference(0).getEntityDescription();
            if (desc != null)
                return desc.getContent();
        }
        return "<Not assigned>";
    }

    /**
     * Returns the entity description for the given resolved concept reference.
     */
    protected String getCodeDescription(ResolvedConceptReference ref) throws LBException {
        EntityDescription desc = ref.getEntityDescription();
        if (desc != null)
            return desc.getContent();
        return "<Not assigned>";
    }

    /**
     * Returns the label to display for the given association and directional
     * indicator.
     */
    protected String getDirectionalLabel(LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, Association assoc, boolean navigatedFwd) throws LBException {

        String assocLabel = navigatedFwd ? lbscm.getAssociationForwardName(assoc.getAssociationName(), scheme, csvt)
                : lbscm.getAssociationReverseName(assoc.getAssociationName(), scheme, csvt);
        if (StringUtils.isBlank(assocLabel))
            assocLabel = (navigatedFwd ? "" : "[Inverse]") + assoc.getAssociationName();
        return assocLabel;
    }

    /**
     * Resolves one or more paths from the hierarchy root to the given code
     * through a list of connected associations defined by the hierarchy.
     */
    protected AssociationList getPathsFromRoot(LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm,
            String scheme, CodingSchemeVersionOrTag csvt, String hierarchyID, String focusCode,
            Map<String, EntityDescription> codesToDescriptions) throws LBException {

        // Get paths from the focus code to the root from the
        // convenience method. All paths are resolved. If only
        // one path is required, it would be possible to use
        // HierarchyPathResolveOption.ONE to reduce processing
        // and improve overall performance.
        AssociationList pathToRoot = lbscm.getHierarchyPathToRoot(scheme, csvt, null, focusCode, null, false,
                HierarchyPathResolveOption.ALL, null);

        // But for purposes of this example we need to display info
        // in order coming from root direction. Process the paths to root
        // recursively to reverse the order for processing ...
        AssociationList pathFromRoot = new AssociationList();
        for (int i = pathToRoot.getAssociationCount() - 1; i >= 0; i--)
            reverseAssoc(lbsvc, lbscm, scheme, csvt, pathToRoot.getAssociation(i), pathFromRoot, codesToDescriptions);

        return pathFromRoot;
    }

    /**
     * Returns a description of the hierarchy defined by the given coding scheme
     * and matching the specified ID.
     */
    protected static SupportedHierarchy getSupportedHierarchy(LexBIGService lbsvc, String scheme,
            CodingSchemeVersionOrTag csvt, String hierarchyID) throws LBException {

        CodingScheme cs = lbsvc.resolveCodingScheme(scheme, csvt);
        if (cs == null) {
            throw new LBResourceUnavailableException("Coding scheme not found: " + scheme);
        }
        for (SupportedHierarchy h : cs.getMappings().getSupportedHierarchy())
            if (h.getLocalId().equals(hierarchyID))
                return h;
        throw new LBResourceUnavailableException("Hierarchy not defined: " + hierarchyID);
    }

    /**
     * Recursive call to reverse order of the given association list, adding
     * results to the given list. In context of this program we use this
     * technique to determine the path from root, starting from the path to root
     * provided by the standard convenience method.
     */
    protected AssociationList reverseAssoc(LexBIGService lbsvc, LexBIGServiceConvenienceMethods lbscm, String scheme,
            CodingSchemeVersionOrTag csvt, Association assoc, AssociationList addTo,
            Map<String, EntityDescription> codeToEntityDescriptionMap) throws LBException {

        ConceptReference acRef = assoc.getAssociationReference();
        AssociatedConcept acFromRef = new AssociatedConcept();
        acFromRef.setCodingSchemeName(acRef.getCodingSchemeName());
        acFromRef.setConceptCode(acRef.getConceptCode());
        AssociationList acSources = new AssociationList();
        acFromRef.setIsNavigable(Boolean.TRUE);
        acFromRef.setSourceOf(acSources);

        // Use cached description if available (should be cached
        // for all but original root) ...
        if (codeToEntityDescriptionMap.containsKey(acRef.getConceptCode()))
            acFromRef.setEntityDescription(codeToEntityDescriptionMap.get(acRef.getConceptCode()));
        // Otherwise retrieve on demand ...
        else
            acFromRef.setEntityDescription(Constructors.createEntityDescription(getCodeDescription(lbsvc, scheme, csvt,
                    acRef.getConceptCode())));

        AssociatedConceptList acl = assoc.getAssociatedConcepts();
        for (AssociatedConcept ac : acl.getAssociatedConcept()) {
            // Create reverse association (same non-directional name)
            Association rAssoc = new Association();
            rAssoc.setAssociationName(assoc.getAssociationName());

            // On reverse, old associated concept is new reference point.
            ConceptReference ref = new ConceptReference();
            ref.setCodingSchemeName(ac.getCodingSchemeName());
            ref.setConceptCode(ac.getConceptCode());
            rAssoc.setAssociationReference(ref);

            // And old reference is new associated concept.
            AssociatedConceptList rAcl = new AssociatedConceptList();
            rAcl.addAssociatedConcept(acFromRef);
            rAssoc.setAssociatedConcepts(rAcl);

            // Set reverse directional name, if available.
            String dirName = assoc.getDirectionalName();
            if (dirName != null)
                try {
                    rAssoc.setDirectionalName(lbscm.isForwardName(scheme, csvt, dirName) ? lbscm
                            .getAssociationReverseName(assoc.getAssociationName(), scheme, csvt) : lbscm
                            .getAssociationReverseName(assoc.getAssociationName(), scheme, csvt));
                } catch (LBException e) {
                }

            // Save code desc for future reference when setting up
            // concept references in recursive calls ...
            codeToEntityDescriptionMap.put(ac.getConceptCode(), ac.getEntityDescription());

            AssociationList sourceOf = ac.getSourceOf();
            if (sourceOf != null)
                for (Association sourceAssoc : sourceOf.getAssociation()) {
                    AssociationList pos = reverseAssoc(lbsvc, lbscm, scheme, csvt, sourceAssoc, addTo,
                            codeToEntityDescriptionMap);
                    pos.addAssociation(rAssoc);
                }
            else
                addTo.addAssociation(rAssoc);
        }
        return acSources;
    }

    // /////////////////////////////////////////////////////
    // Helper classes
    // /////////////////////////////////////////////////////

    /**
     * Inner class to hold tree items for printout.
     */
    protected class TreeItem implements Comparable<TreeItem> {
        public String code = null;
        public String text = null;
        public boolean expandable = false;
        public Map<String, List<TreeItem>> assocToChildMap = new TreeMap<String, List<TreeItem>>();

        public boolean equals(Object o) {
            return o instanceof TreeItem && code.compareTo(((TreeItem) o).code) == 0;
        }

        public int compareTo(TreeItem ti) {
            String c1 = code;
            String c2 = ti.code;
            if (c1.startsWith("@"))
                return 1;
            if (c2.startsWith("@"))
                return -1;
            return c1.compareTo(c2);
        }

        public TreeItem(String code, String text) {
            super();
            this.code = code;
            this.text = text;
        }

        public void addAll(String assocText, List<TreeItem> children) {
            for (TreeItem item : children)
                addChild(assocText, item);
        }

        public void addChild(String assocText, TreeItem child) {
            List<TreeItem> children = assocToChildMap.get(assocText);
            if (children == null) {
                children = new ArrayList<TreeItem>();
                assocToChildMap.put(assocText, children);
            }
            int i;
            if ((i = children.indexOf(child)) >= 0) {
                TreeItem existingTreeItem = children.get(i);
                for (String assoc : child.assocToChildMap.keySet()) {
                    List<TreeItem> toAdd = child.assocToChildMap.get(assoc);
                    if (!toAdd.isEmpty()) {
                        existingTreeItem.addAll(assoc, toAdd);
                        existingTreeItem.expandable = false;
                    }
                }
            } else
                children.add(child);
        }
    }

}