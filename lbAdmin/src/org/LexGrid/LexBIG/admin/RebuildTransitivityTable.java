
package org.LexGrid.LexBIG.admin;

import java.io.IOException;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * Rebuilds the Transitivity Table associated with the specified coding scheme.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.RebuildTransitivityTable
 *   -u,--urn &lt;name&gt; URN uniquely identifying the code system.
 *   -v,--version &lt;id&gt; Version identifier.
 *   -f,--force Force clear (no confirmation).
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.RebuildTransitivityTable
 *   -u &quot;urn:oid:2.16.840.1.113883.3.26.1.1&quot; -v &quot;05.09e&quot;
 * </pre>
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
@LgAdminFunction
public class RebuildTransitivityTable {

    public static void main(String[] args) {
        try {
            new RebuildTransitivityTable().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public RebuildTransitivityTable() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
    public void run(String[] args) throws Exception {

        // Parse the command line ...
        CommandLine cl = null;
        Options options = getCommandOptions();
        try {
            cl = new BasicParser().parse(options, args);
        } catch (ParseException e) {
            Util.displayCommandOptions("RebuildTransitivityTable", options,
                    "RebuildTransitivityTable -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" -v \"05.09e\"", e);
            Util.displayMessage(Util.getPromptForSchemeHelp());
            return;
        }

        // Interpret provided values ...
        String urn = cl.getOptionValue("u");
        String ver = cl.getOptionValue("v");
        boolean force = cl.hasOption("f");
        CodingSchemeSummary css = null;

        // Find in list of registered vocabularies ...
        if (urn != null && ver != null) {
            urn = urn.trim();
            ver = ver.trim();
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
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

        AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(css);

        rebuildTransitivityTable(ref, force);
    }

    /**
     * Rebuild a single named index extension for the specified scheme.
     * 
     * @param lbsm
     * @param ref
     * @param indexName
     * @paam force
     * @throws LBException
     */
    protected void rebuildTransitivityTable(AbsoluteCodingSchemeVersionReference ref,
            boolean force) throws LBException {
        
        String codingScheme = "URI: " + ref.getCodingSchemeURN() + " VERSION: " + 
            ref.getCodingSchemeVersion();

        // Confirm the action (if not bypassed by force option) ...
        boolean confirmed = force;
        if (!confirmed) {
            Util.displayMessage("REBUILD TRANSITIVITY TABLE FOR " + codingScheme + "? ('Y' to confirm, any other key to cancel)");
            try {
                char choice = Util.getConsoleCharacter();
                confirmed = choice == 'Y' || choice == 'y';
            } catch (IOException e) {
            }
        }

        // Action confirmed?
        if (confirmed) {
            try {
               
                Util.displayAndLogMessage("Recreation Transitivity Table '" + 
                        codingScheme + "' in progress...");
                LexEvsServiceLocator.
                    getInstance().
                        getLexEvsDatabaseOperations().
                            reComputeTransitiveTable(
                                    ref.getCodingSchemeURN(), 
                                    ref.getCodingSchemeVersion());
            } catch (UnsupportedOperationException e) {
                Util.displayAndLogError("Recreation of Transitivity Table '" + codingScheme + "' is not supported.", e);
            }
        } else {
            Util.displayAndLogMessage("Recreation of Transitivity Table '" + codingScheme + "' cancelled by user.");
        }
    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
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

        o = new Option("f", "force", false, "Force clear (no confirmation).");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}