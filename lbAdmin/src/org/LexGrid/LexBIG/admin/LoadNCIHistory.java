
package org.LexGrid.LexBIG.admin;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.NCIHistoryLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

/**
 * Imports NCI History data to the LexBIG repository.
 * 
 * <pre>
 * Example: java org.LexGrid.LexBIG.admin.LoadNCIHistory
 *   -in,--input &lt;uri&gt; URI or path specifying location of the history file
 *   -vf,--versionFile &lt;uri&gt; URI or path specifying location of the file
 *         containing version identifiers for the history to be loaded
 *   -v, --validate &lt;int&gt; Perform validation of the candidate
 *         resource without loading data.  If specified, the '-r' option
 *         is ignored.  Supported levels of validation include:
 *         0 = Verify top 10 lines are correctly formatted
 *         1 = Verify correct format for the entire file
 *   -r, --replace If not specified, the provided history file will
 *         be added into the current history database; otherwise the
 *         current database will be replaced by the new content.
 *         
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.LexGrid.LexBIG.admin.LoadNCIHistory -in &quot;file:///path/to/history.file&quot; -vf &quot;file:///path/to/version.file&quot;
 * -or-
 *  org.LexGrid.LexBIG.admin.LoadNCIHistory -in &quot;file:///path/to/history.file&quot; -v 0
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
@LgAdminFunction
public class LoadNCIHistory {

    public static void main(String[] args) {
        try {
            new LoadNCIHistory().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogError("Resource Unavailable: " + e.getMessage() , e);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadNCIHistory() {
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
                Util.displayCommandOptions("LoadNCIHistory", options,
                        "\n LoadNCIHistory -in \"file:///path/to/history.file\""
                                + "\n LoadNCIHistory -in \"file:///path/to/history.file\" -v 0" + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            URI source = Util.string2FileURI(cl.getOptionValue("in"));
            URI versions = Util.string2FileURI(cl.getOptionValue("vf"));
            boolean replace = vl <= 0 && cl.hasOption("r");
            if (vl >= 0) {
                Util.displayAndLogMessage("VALIDATING SOURCE URI: " + source.toString());
            } else {
                Util.displayAndLogMessage("LOADING FROM URI: " + source.toString());
                Util.displayAndLogMessage("LOADING VERSION IDENTIFIERS FROM URI: " + versions.toString());
            }

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            NCIHistoryLoader loader = (NCIHistoryLoader) lbsm
                    .getLoader(org.LexGrid.LexBIG.Impl.loaders.NCIHistoryLoaderImpl.name);

            // Perform the load ...
            if (vl >= 0) {
                loader.validate(source, versions, vl);
                Util.displayAndLogMessage("VALIDATION SUCCESSFUL");
            } else {
                loader.load(source, versions, !replace, false, true);
                Util.displayLoaderStatus(loader);
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

        o = new Option("in", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("vf", "versionFile", true, "URI or path specifying location of the file containing version "
                + "identifiers for the history to be loaded");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'r' "
                + "is ignored. 0 to verify 10 lines; 1 to check whole document.");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("r", "replace", false, "If specified, the current history database is overwritten. "
                + "If not, history is added.");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}