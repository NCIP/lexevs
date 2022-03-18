
package org.LexGrid.LexBIG.admin;

import java.net.URI;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexEVSArangoGraphingDbLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.lexevs.locator.LexEvsServiceLocator;

@LgAdminFunction
public class LoadGraphDatabase {

    public LoadGraphDatabase() {
        super();
    }

    public static void main(String[] args) {
        try {
            new LoadGraphDatabase().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogError("Resource Unavailable: " + e.getMessage() , e);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String[] args) throws Exception {
        synchronized (LexEvsServiceLocator.getInstance()) {

            // Parse the command line ...
            CommandLine cl = null;
            String vl = null;
            Options options = getCommandOptions();
            
            try {
                cl = new BasicParser().parse(options, args);
            } catch (Exception e) {
                Util
                        .displayCommandOptions(
                                "LoadGraphDatabase",
                                options,
                                "\n LoadGraphDatabase -in \"http://target.terminology url\" -v \"1.0\"", e);
                return;
            }
                        
            // Interpret provided values ...
            String urn = cl.getOptionValue("in");
            String ver = cl.getOptionValue("v");
            CodingSchemeSummary css = null;
            boolean isActive = false;

            // Find in list of registered vocabularies ...
            if (urn != null && ver != null) {
                urn = urn.trim();
                ver = ver.trim();
                LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
                Enumeration<? extends CodingSchemeRendering> schemes = lbs.getSupportedCodingSchemes()
                        .enumerateCodingSchemeRendering();
                while (schemes.hasMoreElements() && css == null) {
                    CodingSchemeRendering rendering = schemes.nextElement();
                    CodingSchemeSummary summary = rendering.getCodingSchemeSummary();
                    if (urn.equalsIgnoreCase(summary.getCodingSchemeURI())
                            && ver.equalsIgnoreCase(summary.getRepresentsVersion()))
                        css = summary;
                        isActive = rendering.getRenderingDetail().getVersionStatus().equals(
                            CodingSchemeVersionStatus.ACTIVE);
                }
            }

            // Found CodingScheme? If not, prompt...
            if (css == null) {
                if (urn != null || ver != null) {
                    Util.displayMessage("No matching coding scheme was found for the given URN or version.");
                    Util.displayMessage("");
                }
                css = Util.promptForCodeSystem();
                if (css == null)
                    return;
                CodingSchemeRendering csr = Util.getRenderingDetail(css);
                isActive = csr != null
                        && csr.getRenderingDetail().getVersionStatus().equals(CodingSchemeVersionStatus.ACTIVE);
            }
            
            // Coding Scheme must be active before loading the graph.
            if (!isActive) {
                Util.displayAndLogMessage("Coding Scheme " + css.getFormalName() +
                        " must be active before loading its graph.");
                Util.displayAndLogMessage("Request complete");
                return;
            }
            
            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            LexEVSArangoGraphingDbLoader loader = (LexEVSArangoGraphingDbLoader) lbsm
                    .getLoader(org.LexGrid.LexBIG.Impl.loaders.LexEVSArangoGraphingDbLoader.name);

            URI source = new URI(css.getCodingSchemeURI());
            loader.useVersionOrTag(css.getRepresentsVersion());
            loader.load(source);
        }
    }

    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("in", "input", true, "Unique URI identifier of the source terminology");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("v", "version", true, "Terminology version");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}