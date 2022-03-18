
package org.LexGrid.LexBIG.admin;

import java.net.URI;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * Loads optional XML-based metadata to be associated with an existing coding
 * scheme.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadMetadata
 *   -u,--urn &lt;name&gt; URN uniquely identifying the code system.
 *   -v,--version &lt;id&gt; Version identifier.
 *   -in,--input &lt;uri&gt; URI or path specifying location of the XML file.
 *   -v, --validate &lt;int&gt; Perform validation of the input file
 *         without loading data.  If specified, the '-f', and '-o'
 *         options are ignored.  Supported levels of validation include:
 *         0 = Verify document is valid
 *   -o, --overwrite If specified, existing metadata for the code system
 *         will be erased. Otherwise, new metadata will be appended to
 *         existing metadata (if present).  
 *   -f,--force Force overwrite (no confirmation).
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadMetadata -in &quot;file:///path/to/file.xml&quot; -o
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadMetadata -in &quot;file:///path/to/file.xml&quot;
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
@LgAdminFunction
public class LoadMetadata {

    public static void main(String[] args) {
        try {
            new LoadMetadata().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogError("Resource Unavialable: " + e.getMessage() , e);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadMetadata() {
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
            int vl = -1;
            try {
                cl = new BasicParser().parse(options, args);
                if (cl.hasOption("v"))
                    vl = Integer.parseInt(cl.getOptionValue("v"));
            } catch (ParseException e) {
                Util.displayCommandOptions("LoadMetadata", options,
                        "\n LoadMetadata -in \"file:///path/to/file.obo\" -o"
                                + "\n LoadMetadata -in \"file:///path/to/file.obo\"" + Util.getURIHelp(), e);
                Util.displayAndLogError("Requires correctly configured file locations", e );
                return;
            }

            // Interpret provided values ...
            URI source = Util.string2FileURI(cl.getOptionValue("in"));
            String urn = cl.getOptionValue("u");
            String ver = cl.getOptionValue("v");
            boolean force = cl.hasOption("f");
            boolean overwrite = cl.hasOption("o");
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

            if (vl >= 0) {
                Util.displayAndLogMessage("VALIDATING SOURCE URI: " + source.toString());
            } else {
                Util.displayAndLogMessage("LOADING FROM URI: " + source.toString());
                Util.displayAndLogMessage(overwrite ? "OVERWRITE EXISTING METADATA" : "APPEND TO EXISTING METADATA");
            }

            // Find the registered extension handling this type of load ...
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            MetaData_Loader loader = (MetaData_Loader) lbsm.getLoader("MetaDataLoader");

            // Perform the requested load or validate action ...
            if (vl >= 0) {
                loader.validateAuxiliaryData(source, null, vl);
                Util.displayAndLogMessage("VALIDATION SUCCESSFUL");
            } else {
                boolean confirmed = true;
                if (overwrite && !force) {
                    Util.displayMessage("OVERWRITE EXISTING METADATA? ('Y' to confirm, any other key to cancel)");
                    char choice = Util.getConsoleCharacter();
                    confirmed = choice == 'Y' || choice == 'y';
                }
                if (confirmed) {
                    loader.loadAuxiliaryData(source, Constructors.createAbsoluteCodingSchemeVersionReference(css),
                            overwrite, false, true);
                    Util.displayLoaderStatus(loader);
                }
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

        o = new Option("in", "input", true, "URI or path specifying location of the XML file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'o' and 'f' "
                + "are ignored. 0 to verify the document is well-formed.");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("o", "overwrite", false, "If specified, existing metadata for the code system "
                + "will be erased. Otherwise, new metadata will be appended " + "to existing metadata (if present). ");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("f", "force", false, "Force overwrite (no confirmation).");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}