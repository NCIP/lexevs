
package org.LexGrid.LexBIG.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Presentation;
import org.apache.commons.lang.StringUtils;

/**
 * NOTE: This example is intended to run only against code systems
 * representing the entire Metathesaurus.  It depends on the presence of
 * concepts based on CUIs and the import of source and hierarchical
 * definitions as property and association qualifiers.  These
 * attributes are only populated by the NCI Meta loader.
 * 
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
 * The second parameter is also required.  It should provide a
 * source abbreviation (SAB) used to constrain the relationships navigated
 * to a single Meta source.  If not specified, all sources are navigated.
 * 
 * Note that this example does not print intra-CUI associations (links
 * that might exist between individual terms on a single concept).
 * 
 * The selected code system must represent the full Metathesaurus.
 */
public class BuildTreeForMetaCodeBySource {
    static String[] hierAssocToParentNodes_ = new String[] { "PAR", "isa", "branch_of", "part_of", "tributary_of" };
    static String[] hierAssocToChildNodes_ = new String[] { "CHD", "hasSubtype" };
    static SortOptionList sortByCode_ = Constructors.createSortOptionList(new String[] {"code"});

    LocalNameList noopList_ = Constructors.createLocalNameList("_noop_");
    LexBIGServiceConvenienceMethods lbscm_ = null;
    LexBIGService lbsvc_ = null;
    
    public BuildTreeForMetaCodeBySource() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Example: BuildTreeForMetaCodeBySource \"C0245662\" NCI");
            return;
        }

        try {
            String cui = args[0];
            String sab = args[1];
            new BuildTreeForMetaCodeBySource().run(cui, sab);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    /**
     * Process the provided code, constraining relationships
     * to the given source abbreviation.
     * @throws LBException
     */
    public void run(String cui, String sab) throws LBException {
        // Resolve the coding scheme.
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css == null)
            return;
        
        String scheme = css.getCodingSchemeURI();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(css.getRepresentsVersion());

        // Resolve the requested concept.
        ResolvedConceptReference rcr = resolveConcept(scheme, csvt, cui);
        if (rcr == null) {
            Util.displayMessage("Unable to resolve a concept for CUI = '" + cui + "'");
            return;
        }
        
        // Print a header for the item being processed.
        Util.displayMessage("============================================================");
        Util.displayMessage("Concept Information");;
        Util.displayMessage("============================================================");
        printHeader(rcr, sab);

        // Print the hierarchies for the requested SAB.
        Util.displayMessage("");
        Util.displayMessage("============================================================");
        Util.displayMessage("Hierarchies applicable for CUI " + cui + " for SAB " + sab);
        Util.displayMessage("============================================================");
        TreeItem ti = new TreeItem("<Start>", "Start of Tree", null);
        long ms = System.currentTimeMillis();
        int pathsResolved = 0;
        try {
            // Identify the set of all codes on path from root
            // to the focus code ...
            TreeItem[] pathsFromRoot = buildPathsToRoot(rcr, scheme, csvt, sab);
            pathsResolved = pathsFromRoot.length;
            for (TreeItem rootItem : pathsFromRoot)
                ti.addChild("CHD", rootItem);
        } finally {
            System.out.println("Run time (milliseconds): " + (System.currentTimeMillis() - ms) + " to resolve "
                    + pathsResolved + " paths from root.");
        }
        printTree(ti, cui, 0);
        
        // Print the neighboring CUIs/AUIs for this SAB.
        Util.displayMessage("");
        Util.displayMessage("============================================================");
        Util.displayMessage("Neighboring CUIs and AUIs for CUI " + cui + " for SAB " + sab);;
        Util.displayMessage("============================================================");
        printNeighborhood(scheme, csvt, rcr, sab);
    }

    /**
     * Prints formatted text providing context for
     * the given item including CUI, SAB, AUI, and Text.
     * @throws LBException
     */
    protected void printHeader(ResolvedConceptReference rcr, String sab)
            throws LBException {
        
        Util.displayMessage("CUI ....... : " + rcr.getConceptCode());
        Util.displayMessage("Description : " + StringUtils.abbreviate(rcr.getEntityDescription().getContent(), 60));
        Util.displayMessage("SAB ....... : " + sab);
        Util.displayMessage("");
        Util.displayMessage("AUIs with this CUI associated for this SAB :");
        for (String line : getAtomText(rcr, sab).split("\\|"))
            Util.displayMessage("  {" + line + '}');
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
            .append(focusCode.equals(ti.code) ? ">" : " ")
            .append(ti.code).append(':')
            .append(StringUtils.abbreviate(ti.text, 60))
            .append(ti.expandable ? " [+]" : "");
        if (ti.auis != null)
            for (String line : ti.auis.split("\\|"))
                codeAndText.append('\n').append(indent)
                    .append("    {")
                    .append(StringUtils.abbreviate(line, 60))
                    .append('}');
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

    /**
     * Prints formatted text with the CUIs and AUIs of
     * neighboring concepts for the requested SAB.
     * @throws LBException
     */
    protected void printNeighborhood(String scheme, CodingSchemeVersionOrTag csvt,
            ResolvedConceptReference rcr, String sab)
            throws LBException {
        
        // Resolve neighboring concepts with associations
        // qualified by the SAB.
        CodedNodeGraph neighborsBySource = getLexBIGService().getNodeGraph(scheme, csvt, null);
        neighborsBySource.restrictToAssociations(null, Constructors.createNameAndValueList(sab, "Source"));
        ResolvedConceptReferenceList nodes = neighborsBySource.resolveAsList(
            rcr, true, true, Integer.MAX_VALUE, 1,
            null, new PropertyType[] { PropertyType.PRESENTATION },
            sortByCode_, null, -1);
        
        List<AssociatedConcept> neighbors = new ArrayList<AssociatedConcept>();
        for (ResolvedConceptReference node : nodes.getResolvedConceptReference()) {
            // Process sources and targets ...
            if (node.getSourceOf() != null){
                for (Association assoc : node.getSourceOf().getAssociation())
                    for (AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept())
                        if (isValidForSAB(ac, sab))
                            neighbors.add(ac);
                if(node.getTargetOf() != null){
            if (node.getTargetOf() != null)
                for (Association assoc : node.getTargetOf().getAssociation())
                    for (AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept())
                        if (isValidForSAB(ac, sab))
                            neighbors.add(ac);
                }
            // Add to printed output
            for (ResolvedConceptReference neighbor : neighbors) {
                Util.displayMessage(neighbor.getCode() + ':' +
                        StringUtils.abbreviate(neighbor.getEntityDescription().getContent(), 60));
                for (String line : getAtomText(neighbor, sab).split("\\|"))
                    Util.displayMessage("    {" + StringUtils.abbreviate(line, 60) + '}');
            }
            }
        }
    }

    /**
     * Populate child nodes for a single branch of the tree, and indicates
     * whether further expansion (to grandchildren) is possible.
     */
    protected void addChildren(TreeItem ti, String scheme, CodingSchemeVersionOrTag csvt,
            String sab, String branchRootCode, Set<String> codesToExclude,
            String[] associationsToNavigate, boolean associationsNavigatedFwd) throws LBException {

        LexBIGService lbsvc = getLexBIGService();
        
        // Resolve the next branch, representing children of the given
        // code, navigated according to the provided relationship and
        // direction. Resolve the children as a code graph, looking 2
        // levels deep but leaving the final level unresolved.
        CodedNodeGraph cng = lbsvc.getNodeGraph(scheme, csvt, null);
        ConceptReference focus = Constructors.createConceptReference(branchRootCode, scheme);
        cng = cng.restrictToAssociations(
                Constructors.createNameAndValueList(associationsToNavigate), 
                ConvenienceMethods.createNameAndValueList(sab, "Source"));
        ResolvedConceptReferenceList branch = cng.resolveAsList(
                focus, associationsNavigatedFwd, !associationsNavigatedFwd,
                Integer.MAX_VALUE, 2,
                null, new PropertyType[] { PropertyType.PRESENTATION },
                sortByCode_, null, -1, true);

        // The resolved branch will be represented by the first node in
        // the resolved list. The node will be subdivided by source or
        // target associations (depending on direction). The associated
        // nodes define the children.
        for (ResolvedConceptReference node : branch.getResolvedConceptReference()) {
            AssociationList childAssociationList = associationsNavigatedFwd ? node.getSourceOf() : node.getTargetOf();

            // Process each association defining children ...
            if(childAssociationList != null){
            for (Association child : childAssociationList.getAssociation()) {
                String childNavText = getDirectionalLabel(scheme, csvt, child, associationsNavigatedFwd);

                // Each association may have multiple children ...
                AssociatedConceptList branchItemList = child.getAssociatedConcepts();
                for (AssociatedConcept branchItemNode : branchItemList.getAssociatedConcept())
                    if (isValidForSAB(branchItemNode, sab)) {
                        String branchItemCode = branchItemNode.getCode();
    
                        // Add here if not in the list of excluded codes.
                        // This is also where we look to see if another level
                        // was indicated to be available. If so, mark the
                        // entry with a '+' to indicate it can be expanded.
                        if (!codesToExclude.contains(branchItemCode)) {
                            TreeItem childItem =
                                new TreeItem(branchItemCode,
                                    branchItemNode.getEntityDescription().getContent(),
                                    getAtomText(branchItemNode, sab));
                            AssociationList grandchildBranch =
                                associationsNavigatedFwd ? branchItemNode.getSourceOf()
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
     * Build and returns tree items that represent the root
     * and core concepts of resolved paths for printing.
     * @throws LBException
     */
    protected TreeItem[] buildPathsToRoot(ResolvedConceptReference rcr,
            String scheme, CodingSchemeVersionOrTag csvt,
            String sab) throws LBException {
        
        // Create a starting point for tree building.
        TreeItem ti =
            new TreeItem(rcr.getCode(), rcr.getEntityDescription().getContent(),
                getAtomText(rcr, sab));
        
        // Maintain root tree items.
        Set<TreeItem> rootItems = new HashSet<TreeItem>();

        // Natural flow of hierarchy relations moves forward
        // from tree root to leaves.  Build the paths to root here
        // by processing upstream (child to parent) relationships.
        buildPathsToUpperNodes(
            ti, rcr, scheme, csvt, sab,
            new HashMap<String, TreeItem>(),
            rootItems);
        
        // Return root items discovered during child to parent
        // processing.
        return rootItems.toArray(new TreeItem[rootItems.size()]);
    }

    /**
     * Add all hierarchical paths to root that start from the
     * referenced concept and move backward in the tree. If
     * the natural flow of relations is thought of moving from tree
     * root to leaves, this method processes nodes in the
     * reverse direction (from child to parent).
     * @throws LBException
     */
    protected void buildPathsToUpperNodes(TreeItem ti, ResolvedConceptReference rcr,
            String scheme, CodingSchemeVersionOrTag csvt,
            String sab, Map<String, TreeItem> code2Tree,
            Set<TreeItem> roots)
        throws LBException {
        
        // Only need to process a code once ...
        if (code2Tree.containsKey(rcr.getCode()))
            return;
            
        // Cache for future reference.
        code2Tree.put(rcr.getCode(), ti);

        // UMLS relations can be defined with forward direction
        // being parent to child or child to parent on a source
        // by source basis.  Iterate twice to ensure completeness;
        // once navigating child to parent relations forward
        // and once navigating parent to child relations
        // backward.  Both have the net effect of navigating
        // from the bottom of the hierarchy to the top.
        boolean isRoot = true;
        for (int i = 0; i <= 1; i++) {
           boolean fwd = i < 1;
           String[] upstreamAssoc = fwd ? hierAssocToParentNodes_ : hierAssocToChildNodes_;
            
            // Define a code graph for all relationships tagged with
            // the specified sab.
            CodedNodeGraph graph = getLexBIGService().getNodeGraph(scheme, csvt, null);
            graph.restrictToAssociations(
                ConvenienceMethods.createNameAndValueList(upstreamAssoc),
                ConvenienceMethods.createNameAndValueList(sab, "Source"));
    
            // Resolve one hop, retrieving presentations for
            // comparison of source assignments.
            ResolvedConceptReference[] refs = graph.resolveAsList(
                rcr, fwd, !fwd, Integer.MAX_VALUE, 1,
                null, new PropertyType[] { PropertyType.PRESENTATION },
                sortByCode_, null, -1).getResolvedConceptReference();
            
            // Create a new tree item for each upstream node, add the current
            // tree item as a child, and recurse to go higher (if available).
            if (refs.length > 0) {
                
                // Each associated concept represents an upstream branch.
                AssociationList aList = fwd ? refs[0].getSourceOf() : refs[0].getTargetOf();
                if(aList != null){
                for (Association assoc : aList.getAssociation()) {
                    
                    // Go through the concepts one by one, adding the
                    // current tree item as a child of a new tree item
                    // representing the upstream node. If a tree item
                    // already exists for the parent, we reuse it to
                    // keep a single branch per parent.
                    for (AssociatedConcept refParent : assoc.getAssociatedConcepts().getAssociatedConcept())
                        if (isValidForSAB(refParent, sab)) {
                            
                            // Fetch the term for this context ...
                            Presentation[] sabMatch = getSourcePresentations(refParent, sab);
                            if (sabMatch.length > 0) {
                           
                                // We need to take into account direction of
                                // navigation on each pass to get the right label.
                                String directionalName = getDirectionalLabel(scheme, csvt, assoc, !fwd);
                                
                                // Check for a previously registered item for the
                                // parent.  If found, re-use it.  Otherwise, create
                                // a new parent tree item.
                                String parentCode = refParent.getCode();
                                TreeItem tiParent = code2Tree.get(parentCode);
                                if (tiParent == null) {
                                    
                                    // Create a new tree item.
                                    tiParent =
                                        new TreeItem(parentCode, refParent.getEntityDescription().getContent(),
                                            getAtomText(refParent, sab));
                                    
                                    // Add immediate children of the parent code with an
                                    // indication of sub-nodes (+).  Codes already
                                    // processed as part of the path are ignored since
                                    // they are handled through recursion.
                                    String[] downstreamAssoc = fwd ? hierAssocToChildNodes_ : hierAssocToParentNodes_;
                                    addChildren(tiParent, scheme, csvt, sab, parentCode, code2Tree.keySet(),
                                            downstreamAssoc, fwd);
                                
                                    // Try to go higher through recursion.
                                    buildPathsToUpperNodes(tiParent, refParent,
                                        scheme, csvt, sab, code2Tree, roots);
                                }
                                
                                // Add the child
                                tiParent.addChild(directionalName, ti);
                                isRoot = false;
                            }
                        }
                }
                }
            }
        }
        if (isRoot)
            roots.add(ti);
    }

    /**
     * Returns a resolved concept for the specified code and
     * scheme.
     * @throws LBException
     */
    protected ResolvedConceptReference resolveConcept(String scheme,
            CodingSchemeVersionOrTag csvt, String code) 
            throws LBException {
        
        CodedNodeSet cns = getLexBIGService().getCodingSchemeConcepts(scheme, csvt);
        cns.restrictToMatchingProperties(ConvenienceMethods.createLocalNameList("conceptCode"),
            null, code, "exactMatch", null);
        ResolvedConceptReferenceList cnsList = cns.resolveToList(
            null, null,  new PropertyType[] { PropertyType.PRESENTATION },
            1);
        return (cnsList.getResolvedConceptReferenceCount() == 0) ? null
                : cnsList.getResolvedConceptReference(0);        
    }

    /**
     * Returns a cached instance of a LexBIG service.
     */
    protected LexBIGService getLexBIGService() throws LBException {
        if (lbsvc_ == null)
            lbsvc_ = LexBIGServiceImpl.defaultInstance();
        return lbsvc_;
    }

    /**
     * Returns a cached instance of convenience methods.
     */
    protected LexBIGServiceConvenienceMethods getConvenienceMethods() throws LBException {
        if (lbscm_ == null)
            lbscm_ = (LexBIGServiceConvenienceMethods)
                getLexBIGService().getGenericExtension("LexBIGServiceConvenienceMethods");
        return lbscm_;
    }

    /**
     * Returns the label to display for the given association and directional
     * indicator.
     */
    protected String getDirectionalLabel(String scheme, CodingSchemeVersionOrTag csvt,
            Association assoc, boolean navigatedFwd) throws LBException {
        
        LexBIGServiceConvenienceMethods lbscm = getConvenienceMethods();
        String assocLabel = navigatedFwd ? lbscm.getAssociationForwardName(assoc.getAssociationName(), scheme, csvt)
                : lbscm.getAssociationReverseName(assoc.getAssociationName(), scheme, csvt);
        if (StringUtils.isBlank(assocLabel))
            assocLabel = (navigatedFwd ? "" : "[Inverse]") + assoc.getAssociationName();
        return assocLabel;
    }
    
    /**
     * Returns a string representing the AUIs and
     * text presentations applicable only for the
     * given source abbreviation (SAB). All AUI
     * text combinations are qualified by SAB and
     * delimited by '|'.
     */
    protected String getAtomText(ResolvedConceptReference rcr, String sab) {
        StringBuffer text = new StringBuffer();
        boolean first = true;
        for (Presentation p : getSourcePresentations(rcr, sab)) {
            if (!first)
                text.append('|');
            text.append(sab).append(':')
                .append(getAtomText(p)).append(':')
                .append('\'')
                .append(p.getValue().getContent())
                .append('\'');
            first = false;
        }
        return
            text.length() > 0 ? text.toString()
                : "<No Match for SAB>";
    }

    /**
     * Returns text for AUI qualifiers for the given property.
     * This method iterates through available property qualifiers.
     * Typically only one AUI is expected.  If more are
     * discovered, returned values are delimited by '|'.
     */
    protected String getAtomText(Property prop) {
        StringBuffer text = new StringBuffer();
        boolean first = true;
        for (PropertyQualifier pq : prop.getPropertyQualifier())
            if ("AUI".equalsIgnoreCase(pq.getPropertyQualifierName())) {
                if (!first)
                    text.append('|');
                text.append(pq.getValue().getContent());
                first = false;
            }
        return
            text.length() > 0 ? text.toString()
                : "<No AUI>";
    }

    /**
     * Returns all assigned presentations matching the given
     * source abbreviation (SAB). This method iterates through the
     * available presentations to find any qualified to match
     * the specified source.
     */
    protected Presentation[] getSourcePresentations(ResolvedConceptReference rcr, String sab) {
        
        // Ensure the associated entity was resolved, and look at each
        // assigned presentation for a matching source qualifier.
        List<Presentation> matches = new ArrayList<Presentation>();
        if (rcr.getEntity() != null)
            for (Presentation p : rcr.getEntity().getPresentation())
                for (Source src : p.getSource())
                    if (sab.equalsIgnoreCase(src.getContent()))
                        matches.add(p);
        return matches.toArray(new Presentation[matches.size()]);
    }

    /**
     * Indicates whether the given associated concept contains
     * a qualifier for the given source abbreviation (SAB).
     * @return true if a qualifier exists; false otherwise.
     */
    protected boolean isValidForSAB(AssociatedConcept ac, String sab) {
        for (NameAndValue qualifier : ac.getAssociationQualifiers().getNameAndValue())
            if ("Source".equalsIgnoreCase(qualifier.getContent())
                    && sab.equalsIgnoreCase(qualifier.getName()))
                return true;
        return false;
    }
    
    /**
     * Inner class to hold tree items for printout.
     */
    protected class TreeItem implements Comparable<TreeItem> {
        public String code = null;
        public String text = null;
        public String auis = null;
        public boolean expandable = false;
        public Map<String, List<TreeItem>> assocToChildMap = new TreeMap<String, List<TreeItem>>();

        public boolean equals(Object o) {
            return o instanceof TreeItem && code.compareTo(((TreeItem) o).code) == 0;
        }

        public int compareTo(TreeItem ti) {
            String c1 = code;
            String c2 = ti.code;
            if (c1.startsWith("@")) return 1;
            if (c2.startsWith("@")) return -1;
            int i = c1.compareTo(c2);
            return i != 0 ? i
                : text.compareTo(ti.text);
        }

        public TreeItem(String code, String text, String auiText) {
            super();
            this.code = code;
            this.text = text;
            this.auis = auiText;
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