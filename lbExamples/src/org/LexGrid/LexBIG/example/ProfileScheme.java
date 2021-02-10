
package org.LexGrid.LexBIG.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 
 * Requires loading valid scheme (must have root node named @ pointing to top
 * nodes) Profiles a coding scheme based on unique URN, version, relation and
 * scheme name.
 * 
 * Note: If the URN and version values are unspecified, a list of available
 * coding schemes will be presented for user selection.
 * 
 * <pre>
 *     Example: java org.LexGrid.LexBIG.example.ProfileScheme
 *      -u, --urn &amp;lturn&amp;gt; URN uniquely identifying the code system.
 *      -v, --version &amp;ltversionId&amp;gt; Version identifier.
 * 
 *     Example: java -Xmx1500m -cp lgRuntime.jar
 *     org.LexGrid.LexBIG.example.ProfileScheme
 *       -u &quot;urn:oid:2.16.840.1.113883.3.26.1.1&quot; -v &quot;05.09e&quot;
 * </pre>
 * 
 * @author <A HREF="mailto: scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto: johnson.thomas@mayo.edu">Thomas Johnson</A>
 * 
 */
@LgAdminFunction
public class ProfileScheme {
    int combinedDepth, numBranches, numBranchPoints;
    Map<String, Integer> prop2Freq = new HashMap<String, Integer>();
    Map<String, Integer> propQual2Freq = new HashMap<String, Integer>();
    Map<String, Integer> assoc2srcFreq = new HashMap<String, Integer>();
    Map<String, Integer> assoc2tgtFreq = new HashMap<String, Integer>();

    /**
     * Association type defaults to hasSubtype in this constructor
     */
    public ProfileScheme() {
        super();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            new ProfileScheme().run(args);
        } catch (LBException lbe) {
            Util.displayTaggedMessage("Operation failed: " + lbe.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("Operation failed", e);
        }
    }

    /**
     * Primary entry point for the program.
     * 
     * @param args
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        // Initialize commonly referenced services ...
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

        // Parse the command line ...
        CommandLine cl = null;
        Options options = getCommandOptions();
        try {
            cl = new BasicParser().parse(options, args);
        } catch (ParseException e) {
            Util.displayCommandOptions("ProfileScheme", options,
                    "ProfileScheme -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" " + "-v \"05.09e\"", e);
            Util.displayMessage(Util.promptForSchemeHelp());
            return;
        }

        // Interpret provided values ...
        String urn = cl.getOptionValue("u");
        String ver = cl.getOptionValue("v");
        CodingSchemeSummary css = null;

        // Find in list of registered vocabularies ...
        if (urn != null && ver != null) {
            urn = urn.trim();
            ver = ver.trim();
            Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                    .enumerateCodingSchemeRendering();
            while (schemes.hasMoreElements() && css == null) {
                CodingSchemeSummary summary = schemes.nextElement().getCodingSchemeSummary();
                if (urn.equalsIgnoreCase(summary.getCodingSchemeURI())
                        && ver.equalsIgnoreCase(summary.getRepresentsVersion()))
                    css = summary;
            }
        }

        // Found it? If not, prompt...
        if (css == null) {
            if (urn != null || ver != null) {
                Util.displayMessage("No matching coding scheme was found for the given URN or version.");
                Util.displayMessage("");
            }
            css = Util.promptForCodeSystem();
            if (css == null)
                return;
        }

        runReport(lbs, css);
    }

    /**
     * Initiates profiling of different code system attributes and displays a
     * report for the caller.
     */
    protected void runReport(LexBIGService lbs, CodingSchemeSummary css) throws LBException {
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        Util.displayMessage("==============================================================================");
        Util.displayMessage("==============================================================================");
        Util.displayMessage("NOTE: This may take some time depending on the vocabulary size.");
        Util.displayMessage("");

        // Get the name and version of the coding scheme ...
        String schemeName = css.getLocalName(); // local scheme name
        CodingSchemeVersionOrTag csVorT = Constructors.createCodingSchemeVersionOrTagFromVersion(css
                .getRepresentsVersion()); // Coding Scheme version

        // Output Number of concepts and the time in milliseconds required to
        // enumerate...
        long begin = System.currentTimeMillis();
        Util.displayMessage("==============================================================================");
        Util.displayMessage("Profiling Concepts ...");
        Util.displayMessage("==============================================================================");
        int conceptCount = profileConcepts(lbs, schemeName, csVorT);
        Util.displayMessage("");
        Util.displayMessage("Number of concepts: " + conceptCount);

        Set<String> sortedNames = new TreeSet<String>(prop2Freq.keySet());
        if (sortedNames.size() > 0) {
            Util.displayMessage("Number of concepts assigned the following properties:");
            for (Iterator<String> propNames = sortedNames.iterator(); propNames.hasNext();) {
                String propName = propNames.next();
                Util.displayMessage("  " + propName + ": " + prop2Freq.get(propName));
            }
        }
        sortedNames = new TreeSet<String>(propQual2Freq.keySet());
        if (sortedNames.size() > 0) {
            Util.displayMessage("Number of concepts assigned the following property qualifiers:");
            for (Iterator<String> qualNames = sortedNames.iterator(); qualNames.hasNext();) {
                String qualName = qualNames.next();
                Util.displayMessage("  " + qualName + ": " + propQual2Freq.get(qualName));
            }
        }
        Util.displayMessage("Time elapsed (ms): " + (System.currentTimeMillis() - begin) + "\n");

        begin = System.currentTimeMillis();
        Util.displayMessage("==============================================================================");
        Util.displayMessage("Profiling Associations ...");
        Util.displayMessage("==============================================================================");
        profileAssociations(lbs, lbscm, schemeName, csVorT);
        sortedNames = new TreeSet<String>(assoc2srcFreq.keySet());
        if (sortedNames.size() > 0) {
            Util.displayMessage("");
            for (Iterator<String> assocNames = sortedNames.iterator(); assocNames.hasNext();) {
                String assocName = assocNames.next();
                Util.displayMessage("Name: '" + assocName + "' fwd: '"
                        + lbscm.getAssociationForwardName(assocName, schemeName, csVorT) + "' rev: '"
                        + lbscm.getAssociationReverseName(assocName, schemeName, csVorT) + '\'');
                int srcFreq = assoc2srcFreq.get(assocName);
                int tgtFreq = assoc2tgtFreq.get(assocName);
                if (srcFreq >= 0 && tgtFreq >= 0) {
                    Util.displayMessage("  # sources: " + srcFreq + "  targets: " + tgtFreq);
                }
            }
        }
        Util.displayMessage("Time elapsed (ms): " + (System.currentTimeMillis() - begin) + "\n");

        // Get the top nodes for each supported hierarchy and recurse through
        // branches ...
        String[] hIDs = lbscm.getHierarchyIDs(schemeName, csVorT);
        for (String hID : hIDs) {
            // Initialize processing for hierarchy and print header ...
            combinedDepth = 0;
            numBranches = 0;
            numBranchPoints = 0;
            List<ResolvedConceptReference> deepestPath = new ArrayList<ResolvedConceptReference>();
            Util.displayMessage("==============================================================================");
            Util.displayMessage("Profiling Hierarchy: " + hID);
            Util.displayMessage("==============================================================================");
            ResolvedConceptReferenceList refList = lbscm.getHierarchyRoots(schemeName, csVorT, hID);
            ResolvedConceptReference[] rcref = refList.getResolvedConceptReference();

            // Starting from root nodes, evaluate each hierarchy ...
            begin = System.currentTimeMillis();
            for (ResolvedConceptReference rcr : rcref) {
                Util.displayMessage("Checking Root: " + rcr.getConceptCode() + ':'
                        + rcr.getEntityDescription().getContent());
                List<ResolvedConceptReference> currentPath = profileHierarchy(lbs, lbscm, schemeName, csVorT, hID, rcr,
                        0);
                if (currentPath.size() > 0) {
                    Util.displayMessage("");
                    Util.displayMessage("Example of Deepest Branch: ");
                    printPath(currentPath);
                }
                if (currentPath.size() > deepestPath.size())
                    deepestPath = currentPath;
                Util.displayMessage("");
            }
            Util.displayMessage("Number of Leaf Nodes ......................... : " + numBranches);
            Util.displayMessage("Number of Intermediate Nodes ................. : " + numBranchPoints);
            Util.displayMessage("Average Branch Depth for Hierarchy (all roots) : " + (combinedDepth / numBranches));
            Util.displayMessage("Deepest Branch Level for Hierarchy (all roots) : " + deepestPath.size());
            Util.displayMessage("Time elapsed (ms): " + (System.currentTimeMillis() - begin));
            Util.displayMessage("");
        }
    }

    /**
     * @param lbs
     *            - service object
     * @param schemeName
     *            - coding scheme name
     * @param csVorT
     *            - coding scheme version or tag (version in this case)
     * @return the number of enumerated concepts
     * @throws LBException
     */
    protected int profileConcepts(LexBIGService lbs, String schemeName, CodingSchemeVersionOrTag csVorT)
            throws LBException {
        // Init frequency for supported properties and qualifiers. This way
        // we will know if property or qualifier was registered but not used.
        CodingScheme scheme = lbs.resolveCodingScheme(schemeName, csVorT);
        for (SupportedProperty supported : scheme.getMappings().getSupportedProperty()) {
            prop2Freq.put(supported.getLocalId(), 0);
        }
        for (SupportedPropertyQualifier supported : scheme.getMappings().getSupportedPropertyQualifier()) {
            propQual2Freq.put(supported.getLocalId(), 0);
        }

        // Iterate through all concepts ...
        int count = 0;
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(schemeName, csVorT);
        cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);

        ResolvedConceptReferencesIterator rcrIt = cns.resolve(null, null, null, null, true);
        while (rcrIt.hasNext()) {
            // Bump the concept count.
            count++;
            ResolvedConceptReference rcr = rcrIt.next();

            // Create a combined list of all embedded properties ...
            Entity node = rcr.getEntity();
            List<Property> properties = new ArrayList<Property>();
            properties.addAll(Arrays.asList(node.getComment()));
            properties.addAll(Arrays.asList(node.getProperty()));
            properties.addAll(Arrays.asList(node.getDefinition()));
            properties.addAll(Arrays.asList(node.getPresentation()));

            // To keep track of processed items; we only want to
            // count a property or qualifier once per concept.
            Set<String> propsAlreadyProcessed = new HashSet<String>();
            Set<String> qualsAlreadyProcessed = new HashSet<String>();

            // Iterate over all properties and qualifiers...
            for (Property prop : properties) {
                String propName = prop.getPropertyName();
                if (!propsAlreadyProcessed.contains(propName)) {
                    Integer propCount = prop2Freq.get(propName);
                    prop2Freq.put(propName, propCount == null ? 1 : propCount + 1);
                    propsAlreadyProcessed.add(propName);
                }
                for (PropertyQualifier qual : prop.getPropertyQualifier()) {
                    String qualName = qual.getPropertyQualifierName();
                    if (!qualsAlreadyProcessed.contains(qualName)) {
                        Integer propCount = propQual2Freq.get(qualName);
                        propQual2Freq.put(qualName, propCount == null ? 1 : propCount + 1);
                        qualsAlreadyProcessed.add(qualName);
                    }
                }
            }
            // Since this step can be extensive provide periodic feedback to the
            // user
            // to indicate the process is still active ...
            if (count % 100 == 0)
                System.out.print('.');
            if (count % 7800 == 0)
                System.out.println();
        }
        return count;
    }

    /**
     * @param lbs
     *            - service context
     * @param lbscm
     *            - convenience methods
     * @param csVorT
     *            - coding scheme version or tag (version in this case)
     * @throws LBException
     */
    protected void profileAssociations(LexBIGService lbs, LexBIGServiceConvenienceMethods lbscm, String schemeName,
            CodingSchemeVersionOrTag csVorT) throws LBException {
        // Init frequency for supported properties and qualifiers. This way
        // we will know if property or qualifier was registered but not used.
        CodingScheme scheme = lbs.resolveCodingScheme(schemeName, csVorT);
        Set<String> assocNames = new TreeSet<String>();
        for (SupportedAssociation supported : scheme.getMappings().getSupportedAssociation()) {
            assocNames.add(supported.getLocalId());
            assoc2srcFreq.put(supported.getLocalId(), 0);
            assoc2tgtFreq.put(supported.getLocalId(), 0);
        }

        // For each association, determine frequencies ...
        int count = 0;
        try {
            // Resolve and evaluate all source to target relationships.
            // Since this step can be extensive provide periodic feedback to the
            // user
            // to indicate the process is still active ...
            CodedNodeSet allCodes = lbs.getCodingSchemeConcepts(schemeName, csVorT);
            LocalNameList noProps = Constructors.createLocalNameList("--no-property--");
            ResolvedConceptReferencesIterator potentialSources = allCodes.resolve(null, noProps, null);

            // Brute force approach to evaluate each possible source, concept by
            // concept.
            // Time intensive, but scales better and helps avoid possible
            // bottlenecks related
            // to exceeding system-defined search limits.
            while (potentialSources.hasNext()) {
                ResolvedConceptReference potentialSource = potentialSources.next();
                CodedNodeGraph cng = lbs.getNodeGraph(schemeName, csVorT, null);
                ResolvedConceptReferenceList rcrList = cng.resolveAsList(potentialSource, true, false, -1, 1, noProps,
                        null, null, null, -1);
                for (ResolvedConceptReference rcr : rcrList.getResolvedConceptReference()) {
                    AssociationList assocList = rcr.getSourceOf();
                    for (Association assoc : assocList.getAssociation()) {
                        // Bump source count ...
                        String assocName = assoc.getAssociationName();
                        Integer freq = assoc2srcFreq.get(assocName);
                        assoc2srcFreq.put(assocName, (freq == null ? 1 : freq + 1));

                        // Bump target count ...
                        freq = assoc2tgtFreq.get(assocName);
                        int targetCount = assoc.getAssociatedConcepts().getAssociatedConceptCount();
                        assoc2tgtFreq.put(assocName, (freq == null ? targetCount : freq + targetCount));
                    }
                    if (count++ % 100 == 0)
                        System.out.print('.');
                    if (count % 7800 == 0)
                        System.out.println();
                }
            }
        } catch (Exception e) {
            Util.displayMessage(e.getMessage());
        }
    }

    /**
     * Recursive traversal method gauges maximum depth, average depth
     * 
     * @param lbs
     *            - service context
     * @param lbscm
     *            - convenience methods
     * @param schemeName
     *            - coding scheme name
     * @param csVorT
     *            - coding scheme version or tag (version in this case)
     * @param hID
     *            - hierarchy ID
     * @param root
     *            - concept to navigate from
     * @param level
     *            - current level of recursion
     * @return references representing deepest path in branch (root first)
     * @throws LBException
     */
    protected List<ResolvedConceptReference> profileHierarchy(LexBIGService lbs, LexBIGServiceConvenienceMethods lbscm,
            String schemeName, CodingSchemeVersionOrTag csVorT, String hID, ResolvedConceptReference root, int level)
            throws LBException {
        List<ResolvedConceptReference> deepestPath = new ArrayList<ResolvedConceptReference>();
        deepestPath.add(root);

        AssociationList assocList = lbscm.getHierarchyLevelNext(schemeName, csVorT, hID, root.getConceptCode(), false,
                null);
        if (assocList.getAssociationCount() > 0) {
            List<ResolvedConceptReference> deepestSubpath = new ArrayList<ResolvedConceptReference>();

            // Traverse registered associations ...
            for (Association assoc : assocList.getAssociation()) {
                AssociatedConceptList conceptList = assoc.getAssociatedConcepts();

                // If tree splits here, mark it as starting point of new
                // sub-branches.
                // Bump cumulative statistics, and since this recursion can be
                // extensive provide periodic feedback to the user to indicate
                // the
                // process is still active ...
                if (conceptList.getAssociatedConceptCount() > 1) {
                    numBranchPoints++;
                    if (numBranchPoints % 10 == 0)
                        System.out.print('.');
                    if (numBranchPoints % 780 == 0)
                        System.out.println();
                }

                // Recurse through all child nodes.
                for (Iterator<? extends AssociatedConcept> subsumed = conceptList.iterateAssociatedConcept(); subsumed.hasNext();) {
                    AssociatedConcept childConcept = subsumed.next();

                    // Recurse to determine depth of sub-branch for this child.
                    List<ResolvedConceptReference> tempPath = profileHierarchy(lbs, lbscm, schemeName, csVorT, hID,
                            childConcept, level + 1);

                    // Sub-branch starting at this child is deepest so far?
                    if (tempPath.size() > deepestSubpath.size()) {
                        deepestSubpath = tempPath;
                    }
                }
            }

            // Add the deepest subpath to root for return as deepest path at
            // this level.
            deepestPath.addAll(deepestSubpath);

        } else {
            // Branch ends here, mark a complete branch and add depth to
            // cumulative stats.
            numBranches++;
            combinedDepth += (level + 1);
        }

        // Contains only the root if it is a leaf; otherwise follows with
        // an example path traversing from root to deepest level.
        return deepestPath;
    }

    /**
     * Print the description of concepts in the path, indenting for each
     * represented level.
     * 
     * @param path
     *            Chain of concept references, root first.
     */
    protected void printPath(List<ResolvedConceptReference> path) {
        for (int i = 0; i < path.size(); i++) {
            StringBuffer sb = new StringBuffer(256);
            ResolvedConceptReference ref = path.get(i);
            for (int j = 0; j <= i; j++)
                sb.append("  ");
            sb.append(ref.getConceptCode());
            if (ref.getEntityDescription() != null)
                sb.append(':').append(ref.getEntityDescription().getContent());
            Util.displayMessage(sb.toString());
        }
    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    /**
     * @return
     */
    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("u", "urn", true, "URN uniquely identifying the code system.");
        o.setArgName("name");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "version", true, "Version identifier.");
        o.setArgName("id");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}