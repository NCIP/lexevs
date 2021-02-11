
package org.LexGrid.LexBIG.admin;

import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * Clears optionally loaded metadata associated with the specified coding
 * scheme.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.RemoveMetadata
 *  -u,--urn &lt;name&gt; URN uniquely identifying the code system.
 *  -v,--version &lt;id&gt; Version identifier.
 *  -f,--force Force clear (no confirmation).
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 * org.LexGrid.LexBIG.admin.RemoveMetadata
 *    -u &quot;urn:oid:2.16.840.1.113883.3.26.1.1&quot; -v &quot;05.09e&quot;
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
@LgAdminFunction
public class RemoveMetadata {

    public static void main(String[] args) {
        try {
            new RemoveMetadata().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public RemoveMetadata() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        synchronized (ResourceManager.instance()) {

            // Parse the command line ...
            CommandLine cl = null;
            Options options = getCommandOptions();
            try {
                cl = new BasicParser().parse(options, args);
            } catch (ParseException e) {
                Util.displayAndLogError("Parsing of command line options failed: " + e.getMessage() , e);
                Util.displayCommandOptions("RemoveMetadata", options,
                        "RemoveMetadata -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" -v \"05.09e\"", e);
                Util.displayMessage(Util.getPromptForSchemeHelp());
                return;
            }

            // Interpret provided values ...
            String urn = cl.getOptionValue("u");
            String ver = cl.getOptionValue("v");
            boolean force = cl.hasOption("f");
            CodingSchemeSummary css = null;

            // Find in list of registered vocabularies ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
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

            // Continue and confirm the action (if not bypassed by force option)
            // ...
            Util.displayAndLogMessage("A matching coding scheme was found ...");
            boolean confirmed = true;
            if (!force) {
                Util.displayMessage("CLEAR OPTIONAL METADATA? ('Y' to confirm, any other key to cancel)");
                char choice = Util.getConsoleCharacter();
                confirmed = choice == 'Y' || choice == 'y';
            }
            if (confirmed) {
                lbs.getServiceManager(null).removeCodingSchemeVersionMetaData(
                        Constructors.createAbsoluteCodingSchemeVersionReference(css));
                Util.displayAndLogMessage("Request complete");
            } else {
                Util.displayAndLogMessage("Action cancelled by user");
            }
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