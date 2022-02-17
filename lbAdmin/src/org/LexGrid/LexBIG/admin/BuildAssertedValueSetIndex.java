
package org.LexGrid.LexBIG.admin;

import java.io.IOException;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.AssertedValueSetIndexLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.IndexLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.ProcessRunner;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Builds indexes associated with the source asserted 
 * value sets of the specified coding scheme.
 * 
 * <pre>
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.BuildAssertedValueSetIndex
 *  -u &quot;urn:oid:2.16.840.1.113883.3.26.1.1&quot; -v &quot;05.09e&quot;
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * </pre>
 * 
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer</A>
 */
@LgAdminFunction
public class BuildAssertedValueSetIndex {

    public static void main(String[] args) {
        try {
            new BuildAssertedValueSetIndex().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
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
            Util.displayAndLogError("Parsing of command line options failed: " + e.getMessage() , e);
            Util.displayCommandOptions("BuildAssertedValueSetIndex", options,
                    "BuildAssertedValueSetIndex -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" -v \"17.08d\"", e);
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

        buildMonoLithicSourceAssertedValueSetIndex(ref, force);
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
    protected void buildMonoLithicSourceAssertedValueSetIndex(AbsoluteCodingSchemeVersionReference ref,
            boolean force) throws LBException {
        
        String indexName = "URI: " + ref.getCodingSchemeURN() + " VERSION: " + 
            ref.getCodingSchemeVersion();

        // Confirm the action (if not bypassed by force option) ...
        boolean confirmed = force;
        if (!confirmed) {
            Util.displayMessage("BUILD INDEX FOR " + indexName + "? ('Y' to confirm, any other key to cancel)");
            try {
                char choice = Util.getConsoleCharacter();
                confirmed = choice == 'Y' || choice == 'y';
            } catch (IOException e) {
            }
        }

        // Action confirmed?
        if (confirmed) {
            try {
                ProcessRunner loader = new AssertedValueSetIndexLoaderImpl();
                Util.displayAndLogMessage("Re-creation of index extension '" + 
                        indexName + "' in progress...");
                long start = System.currentTimeMillis();
                Util.displayVSIndexerStatus(loader.runProcess(ref, null));
                System.out.println("indexed in " + ((System.currentTimeMillis() - start)/1000) + "  seconds");
            } catch (UnsupportedOperationException e) {
                Util.displayAndLogMessage("Build index extension for '" + indexName + "' is not supported.");
            }
        } else {
            Util.displayAndLogMessage("Build of index '" + indexName + "' cancelled by user.");
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