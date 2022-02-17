
package org.LexGrid.LexBIG.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
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
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.apache.commons.lang.StringUtils;

/**
 * NOTE: This example is intended to run only against code systems
 * representing the entire Metathesaurus.  It depends on the presence of
 * concepts based on CUIs and the import of source and hierarchical
 * definitions as property and association qualifiers.  These
 * attributes are only populated by the NCI Meta loader.
 * 
 * Example displays explicitly asserted source hierarchies (based on
 * import of MRHIER HCD) for a CUI.  The program takes a single argument
 * (a UMLS CUI), prompts for the code system to query in the LexGrid
 * repository, and displays the HCD-based context relationships
 * along with other details.
 * 
 * Note that this example does not print intra-CUI associations (links
 * that might exist between individual terms on a single concept) or 
 * hierarchies not explicitly tagged by HCD.
 * 
 * The selected code system must represent the full Metathesaurus.
 */
public class FindUMLSContextsForCUI {
    static String[] hierAssocToParentNodes_ = new String[] { "PAR", "isa", "branch_of", "part_of", "tributary_of" };
    static String[] hierAssocToChildNodes_ = new String[] { "CHD", "hasSubtype" };
    LexBIGServiceConvenienceMethods lbscm_ = null;
    LexBIGService lbsvc_ = null;
    
    public FindUMLSContextsForCUI() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: FindUMLSContextsForCUI \"C0030920\"");
            return;
        }

        try {
            String cui = args[0];
            new FindUMLSContextsForCUI().run(cui);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    /**
     * Process the provided code.
     * @throws LBException
     */
    public void run(String cui) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css != null) {
            String scheme = css.getCodingSchemeURI();
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(css.getRepresentsVersion());

            ResolvedConceptReference ref = printCode(cui, scheme, csvt);
            if (ref != null)
                printContext(ref, scheme, csvt);
        }
    }

    /**
     * Display name and description information for the given CUI.
     * @return A fully resolved concept reference for the code.
     * @throws LBException
     */
    protected ResolvedConceptReference printCode(String cui, String scheme,
            CodingSchemeVersionOrTag csvt) throws LBException {
        
        // Locate the matching concept ...
        CodedNodeSet cns = getLexBIGService().getCodingSchemeConcepts(scheme, csvt);
        cns.restrictToMatchingProperties(ConvenienceMethods.createLocalNameList("conceptCode"),
                null, cui, "exactMatch", null);
        ResolvedConceptReferenceList cnsList = cns.resolveToList(null, // sort options
                null, // property names
                new PropertyType[] { PropertyType.PRESENTATION }, // property types
                1);
        if (cnsList.getResolvedConceptReferenceCount() == 0) {
            Util.displayMessage("Unable to resolve a concept for CUI = '" + cui + "'");
            return null;
        }
        ResolvedConceptReference rcr = cnsList.getResolvedConceptReference(0);
        Util.displayMessage("Concept code: " + rcr.getConceptCode());
        Util.displayMessage("Description : " + rcr.getEntityDescription().getContent());

        return rcr;
    }

    /**
     * Recursively display each source-asserted hierarchy that the code
     * participates in, based on tagging by HCD qualifier, with
     * additional information.
     * @throws LBException
     */
    protected void printContext(ResolvedConceptReference rcr, String scheme,
            CodingSchemeVersionOrTag csvt) throws LBException {
        Entity node = rcr.getEntity();

        // Step through the presentations and evaluate any tagged with
        // hcd(s) one by one ...
        int hcdsFound = 0;
        for (Presentation pres : node.getPresentation()) {
            for (PropertyQualifier qual : pres.getPropertyQualifier()) {
                if (qual.getPropertyQualifierName().equalsIgnoreCase("HCD")) {
                    String hcd = qual.getValue().getContent();
                    Util.displayMessage("");
                    Util.displayMessage("======================================================================");
                    Util.displayMessage("Hierarchical Context Definition (HCD): " + hcd);
                    Util.displayMessage("======================================================================");
                    hcdsFound++;

                    // Define a code set for all concepts tagged with the HCD.
                    CodedNodeSet hcdConceptMatch = getLexBIGService().getCodingSchemeConcepts(scheme, csvt);
                    hcdConceptMatch.restrictToProperties(null, // property names
                            new PropertyType[] { PropertyType.PRESENTATION }, // property types
                            null, // source list
                            null, // context list
                            ConvenienceMethods.createNameAndValueList("HCD", hcd) // qualifier list
                            );
                    
                    // List CUIs and specific terms linked by the HCD.
                    ResolvedConceptReferencesIterator linkedConcepts =
                        hcdConceptMatch.resolve(
                            Constructors.createSortOptionList(new String[] {"code"}),
                            null, null,
                            new PropertyType[] { PropertyType.PRESENTATION }, true);
                    Util.displayMessage("");
                    Util.displayMessage("  Linked atoms for this context (ordered by CUI)");
                    Util.displayMessage("  Each term is displayed with additional info in");
                    Util.displayMessage("  the format [CUI]:[AUI]:[SAB]:[Text].");
                    Util.displayMessage("  ----------------------------------------------");
                    while (linkedConcepts.hasNext()) {
                        ResolvedConceptReference ref = linkedConcepts.next();
                        Util.displayMessage(
                            new StringBuffer().append("  ")
                                .append(getDetailedText(ref, hcd, scheme, csvt))
                                .toString());
                    }
                    
                    // List hierarchical relationships between the linked terms,
                    // starting from the original CUI and navigated either
                    // forward or backward.
                    Util.displayMessage("");
                    Util.displayMessage("  Asserted hierarchies between HCD-linked atoms");
                    Util.displayMessage("    '>' identifies the original requested node");
                    Util.displayMessage("    '*' indicates a repeated node, whose expansion");
                    Util.displayMessage("        is already printed in another branch.");
                    Util.displayMessage("  ------------------------------------------------");
                    TreeItem[] paths = buildPaths(rcr, scheme, csvt, hcd, hcdConceptMatch);
                    for (TreeItem path : paths)
                        printPath(path, rcr.getCode(), 1, new HashMap<TreeItem, Integer>());
                }
            }
        }
        if (hcdsFound == 0) {
            Util.displayMessage("No hierarchical definitions (HCD qualifiers) found.");
            return;
        }
    }
    
    /**
     * Build and return a tree item that represents the
     * hierarchical entries for the given concept reference and
     * hierarchical context identifier (HCD).  The returned
     * tree items will represent the root of resolved paths
     * for printing.
     * @throws LBException
     */
    protected TreeItem[] buildPaths(ResolvedConceptReference rcr,
            String scheme, CodingSchemeVersionOrTag csvt,
            String hcd, CodedNodeSet hcdConceptMatch) throws LBException {
        
        // Create a starting point for tree building.
        TreeItem ti = new TreeItem(rcr.getCode(), getHCDText(rcr, hcd));
        
        // Maintain root tree items.
        Set<TreeItem> rootItems = new HashSet<TreeItem>();

        // Natural flow of hierarchy relations moves forward
        // from tree root to leaves.  Build the tree here,
        // processing both downstream (parent to child) and
        // upstream (child to parent) relationships.
        buildPathsToLowerNodes(
            ti, rcr, scheme, csvt, hcd, hcdConceptMatch,
            new HashMap<String, TreeItem>());
        buildPathsToUpperNodes(
            ti, rcr, scheme, csvt, hcd, hcdConceptMatch,
            new HashMap<String, TreeItem>(),
            rootItems);
        
        // Return root items discovered during child to parent
        // processing.
        return rootItems.toArray(new TreeItem[rootItems.size()]);
    }

    /**
     * Add all hierarchical relationships that start from the
     * referenced concept and move forward in the tree. If
     * the natural flow of relations is thought of moving from tree
     * root to leaves, this method handles downstream processing
     * from parent to child.
     * @throws LBException
     */
    protected void buildPathsToLowerNodes(TreeItem ti, ResolvedConceptReference rcr,
            String scheme, CodingSchemeVersionOrTag csvt,
            String hcd, CodedNodeSet hcdConceptMatch, Map<String, TreeItem> code2Tree)
        throws LBException {
        
        // Only need to process a code once ...
        if (code2Tree.containsKey(rcr.getCode()))
            return;
            
        // Cache for future reference.
        code2Tree.put(rcr.getCode(), ti);

        // UMLS relations can be defined with forward direction
        // being parent to child or child to parent on a source
        // by source basis.  Iterate twice to ensure completeness;
        // once navigating parent to child relations forward
        // and once navigating child to parent relations
        // backward.  Both have the net effect of navigating
        // from the top of the hierarchy to the bottom.
        for (int i = 0; i <= 1; i++) {
           boolean fwd = i < 1;
           String[] hierAssoc = fwd ? hierAssocToChildNodes_ : hierAssocToParentNodes_;
            
            // Define a code graph for all relationships tagged with
            // the context ID.
            CodedNodeGraph hcdGraph = getLexBIGService().getNodeGraph(scheme, csvt, null);
            hcdGraph.restrictToAssociations(
                ConvenienceMethods.createNameAndValueList(hierAssoc),
                ConvenienceMethods.createNameAndValueList("HCD", hcd));
    
            // Now restrict the graph to HCD-coded nodes (filter out
            // nodes that do not contain the desired tag).
            hcdGraph.restrictToCodes(hcdConceptMatch);
            
            // Resolve one hop, retrieving presentations for
            // comparison of HCD values.
            ResolvedConceptReference[] refs = hcdGraph.resolveAsList(rcr,
                fwd, !fwd, Integer.MAX_VALUE, 1,
                null, new PropertyType[] { PropertyType.PRESENTATION },
                Constructors.createSortOptionList(new String[] {"code"}),
                null, 500).getResolvedConceptReference();
            
            // Add a tree item for each downstream node and
            // recurse to go deeper (if available).
            if (refs.length > 0) {
                AssociationList aList = fwd ? refs[0].getSourceOf() : refs[0].getTargetOf();
                
                // Each associated concept represents a downstream branch.
                for (Association assoc : aList.getAssociation()) {
    
                    // Go through the concepts one by one, adding the
                    // downstream node as a child of the current tree
                    // item.
                    for (AssociatedConcept refChild : assoc.getAssociatedConcepts().getAssociatedConcept())
                        if (isValidForHCD(refChild, hcd)) {
    
                            // Fetch the term for this context ...
                            String hcdText = getHCDText(refChild, hcd);
                            if (hcdText != null) {
                                
                                // We need to take into account direction of
                                // navigation on each pass to get the right label.
                                String directionalName = getDirectionalLabel(scheme, csvt, assoc, fwd);
                                
                                // Add the child, and recurse to include the lowest
                                // level available.
                                TreeItem tiChild = new TreeItem(refChild.getCode(), hcdText);
                                ti.addChild(directionalName, tiChild);
                                buildPathsToLowerNodes(tiChild, refChild, scheme, csvt, hcd, hcdConceptMatch, code2Tree);
                            }
                        }
                }
            }
        }
    }

    /**
     * Add all hierarchical relationships that start from the
     * referenced concept and move backward in the tree. If
     * the natural flow of relations is thought of moving from tree
     * root to leaves, this method handles upstream processing
     * from child to parent.
     * @throws LBException
     */
    protected void buildPathsToUpperNodes(TreeItem ti, ResolvedConceptReference rcr,
            String scheme, CodingSchemeVersionOrTag csvt,
            String hcd, CodedNodeSet hcdConceptMatch, Map<String, TreeItem> code2Tree,
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
           String[] hierAssoc = fwd ? hierAssocToParentNodes_ : hierAssocToChildNodes_;
            
            // Define a code graph for all relationships tagged with
            // the context ID.
            CodedNodeGraph hcdGraph = getLexBIGService().getNodeGraph(scheme, csvt, null);
            hcdGraph.restrictToAssociations(
                ConvenienceMethods.createNameAndValueList(hierAssoc),
                ConvenienceMethods.createNameAndValueList("HCD", hcd));
    
            // Now restrict the graph to HCD-coded nodes (filter out
            // nodes that do not contain the desired tag).
            hcdGraph.restrictToCodes(hcdConceptMatch);
            
            // Resolve one hop, retrieving presentations for
            // comparison of HCD values.
            ResolvedConceptReference[] refs = hcdGraph.resolveAsList(rcr,
                fwd, !fwd, Integer.MAX_VALUE, 1,
                null, new PropertyType[] { PropertyType.PRESENTATION },
                Constructors.createSortOptionList(new String[] {"code"}),
                null, 500).getResolvedConceptReference();
            
            // Create a new tree item for each upstream node, add the current
            // tree item as a child, and recurse to go higher (if available).
            if (refs.length > 0) {
                
                // Each associated concept represents an upstream branch.
                AssociationList aList = fwd ? refs[0].getSourceOf() : refs[0].getTargetOf();
                for (Association assoc : aList.getAssociation()) {
                    
                    // Go through the concepts one by one, adding the
                    // current tree item as a child of a new tree item
                    // representing the upstream node. If a tree item
                    // already exists for the parent, we reuse it to
                    // keep a single branch per parent.
                    for (AssociatedConcept refParent : assoc.getAssociatedConcepts().getAssociatedConcept()) {
                        
                        // Fetch the term for this context ...
                        String hcdText = getHCDText(refParent, hcd);
                        if (hcdText != null) {
                       
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
                                tiParent = new TreeItem(parentCode, hcdText);
                            
                                // Try to go higher through recursion.
                                buildPathsToUpperNodes(tiParent, refParent,
                                    scheme, csvt, hcd, hcdConceptMatch,
                                    code2Tree, roots);
                            }
                            
                            // Add the child
                            tiParent.addChild(directionalName, ti);
                            isRoot = false;
                        }
                    }
                }
            }
        }
        if (isRoot)
            roots.add(ti);
    }

    // /////////////////////////////////////////////////////
    // Helper methods and classes
    // /////////////////////////////////////////////////////
    
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
     * Returns contextual text for the given item in the format
     * [CUI]:[AUI]:[SAB]:[Text]. This method iterates through the
     * available presentations to find one qualified to match
     * the specified HCD context.
     * @throws LBException
     */
    protected String getDetailedText(ResolvedConceptReference rcr, String hcd,
            String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        
        String text = null;
        
        // Ensure the associated entity was resolved, and look at each
        // assigned presentation for a matching HCD qualifier.  Stop on
        // the first match.
        Presentation hcdPres = null;
        if (rcr.getEntity() != null)
            for (Presentation p : rcr.getEntity().getPresentation())
                for (PropertyQualifier pq : p.getPropertyQualifier()) {
                    if ("HCD".equals(pq.getPropertyQualifierName())
                            && hcd.equals(pq.getValue().getContent())) {
                        hcdPres = p;
                        break;
                    }
                if (hcdPres != null) break;
            }

        // If we found the matching presentation, qualify the returned
        // text with the associated CUI, AUI, and source info.
        if (hcdPres != null) {
            // Add the CUI
            StringBuffer sb = new StringBuffer(rcr.getCode());
            
            // Add the AUIs
            sb.append(':');
            boolean first = true;
            for (PropertyQualifier pq : hcdPres.getPropertyQualifier())
                if ("AUI".equalsIgnoreCase(pq.getPropertyQualifierName())) {
                    if (!first)
                        sb.append('|');
                    sb.append(pq.getValue().getContent());
                    first = false;
                }
            
            // Add source SABs
            sb.append(':');
            List<String> sources = new ArrayList<String>();
            for (int i = 0; i < hcdPres.getSourceCount(); i++) {
                String source = hcdPres.getSource(i).getContent();
                sources.add(source);
                if (i > 0)
                    sb.append('|');
                sb.append(source);
            }
            
            // Add the qualifier text
            sb.append(':').append(hcdPres.getValue().getContent());
            text = sb.toString();
        }
        return text;
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
     * Returns text for an assigned presentation matching the given
     * context identifier (HCD). This method iterates through the
     * available presentations to find one qualified to match
     * the specified HCD context.
     * 
     * @param rcr
     *            Concept resolved from a query.
     * @param hcd
     *            The hierarchical context identifier.
     * @return Text for the first embedded
     *         presentation matching the HCD as qualifier;
     *         null if not available.
     */
    protected String getHCDText(ResolvedConceptReference rcr, String hcd) {
        
        // Ensure the associated entity was resolved, and look at each
        // assigned presentation for a matching HCD qualifier.  Stop on
        // the first match.
        if (rcr.getEntity() != null) {
            for (Presentation p : rcr.getEntity().getPresentation()) {
                for (PropertyQualifier pq : p.getPropertyQualifier()) {
                    if ("HCD".equals(pq.getPropertyQualifierName())
                            && hcd.equals(pq.getValue().getContent())) {
                        return p.getValue().getContent();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns a string containing the names of all sources
     * asserted on the association to the concept delimited
     * by '|'.
     */
    protected String getSourceText(AssociatedConcept ac) {
        StringBuffer text = new StringBuffer();
        boolean first = true;
        for (NameAndValue next : ac.getAssociationQualifiers().getNameAndValue()) {
            if ("Source".equalsIgnoreCase(next.getContent())) {
                if (!first)
                    text.append('|');
                text.append(next.getName());
                first = false;
            }
        }
        return text.toString();
    }
    
    /**
     * Prints the given item, recursing to print all children.
     */
    protected void printPath(TreeItem ti, String focusCode, int depth, Map<TreeItem, Integer> item2Depth) {

        // Increase indent based on depth.
        StringBuffer indent = new StringBuffer();
        indent.append("  ");
        for (int i = 2 ; i < depth * 2; i++)
            indent.append(" | ");

        // Determine if the item was already printed at a
        // previous depth.  If not, remember the node for
        // future reference.
        Integer priorDepth = item2Depth.get(ti);
        if (priorDepth == null)
            item2Depth.put(ti, depth);
        
        // Construct and display text for the item.
        StringBuffer codeAndText =
            new StringBuffer(indent)
                .append(focusCode.equals(ti.code)
                    ? ">" : (priorDepth != null ? "*" : " "))
                .append(ti.code).append(':')
                .append(StringUtils.abbreviate(ti.text, 60));
        Util.displayMessage(codeAndText.toString());

        // Print all children, if the item is not a
        // recursive entry.
        if (priorDepth == null) {
            indent.append(" | ");
            for (String association : ti.assocToChildMap.keySet()) {
                Util.displayMessage(indent.toString() + ' ' + association);
                List<TreeItem> children = ti.assocToChildMap.get(association);
                Collections.sort(children);
                for (TreeItem childItem : children)
                    printPath(childItem, focusCode, depth + 1, item2Depth);
            }
        }
    }

    /**
     * Indicates whether the given associated concept contains
     * a qualifier for the given hierarchical context (HCD).
     * @return true if a qualifier exists; false otherwise.
     */
    protected boolean isValidForHCD(AssociatedConcept ac, String hcd) {
        for (NameAndValue qualifier : ac.getAssociationQualifiers().getNameAndValue())
            if ("HCD".equalsIgnoreCase(qualifier.getName())
                    && hcd.equalsIgnoreCase(qualifier.getContent()))
                return true;
        return false;
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
                    }
                }
            } else
                children.add(child);
        }
    }
}