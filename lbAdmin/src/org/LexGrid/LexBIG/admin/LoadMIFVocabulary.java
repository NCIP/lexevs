package org.LexGrid.LexBIG.admin;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.MIFVocabularyLoader;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.resourcereader.core.StringUtils;

@LgAdminFunction
public class LoadMIFVocabulary {

    public LoadMIFVocabulary() {
        super();
    }

    public static void main(String[] args) {
        try {
            new LoadMIFVocabulary().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogError("Resource Unavailable: " + e.getMessage() , e);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

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
            } catch (Exception e) {
                Util
                        .displayCommandOptions(
                                "LoadMIFVocabulary",
                                options,
                                "\n LoadMIFVocabulary -in \"file:///path/to/file.xml\" -a"
                                        + "\n LoadMIFVocabulary -in \"file:///path/to/file.xml\" -mf \"file:///path/to/myCodingScheme-manifest.xml\" -a"
                                        + "\n LoadMIFVocabulary -in \"file:///path/to/file.xml\" -v 0" + Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            URI source = Util.string2FileURI(cl.getOptionValue("in"));
            URI manifest = null;
            if (!StringUtils.isNull(cl.getOptionValue("mf")))
                manifest = Util.string2FileURI(cl.getOptionValue("mf"));
            boolean activate = vl < 0 && cl.hasOption("a");
            if (vl >= 0) {
                Util.displayAndLogMessage("VALIDATION SOURCE URI: " + source.toString());
            } else {
                Util.displayAndLogMessage("LOADING FROM URI: " + source.toString());
                Util.displayAndLogMessage(activate ? "ACTIVATE ON SUCCESS" : "NO ACTIVATION");
            }

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            MIFVocabularyLoader loader = (MIFVocabularyLoader) lbsm
                    .getLoader(org.LexGrid.LexBIG.Impl.loaders.MIFVocabularyLoaderImpl.name);

            // Perform the requested load or validate action ...
            if (vl >= 0) {
                loader.validate(source, vl);
                Util.displayAndLogMessage("VALIDATION SUCCESSFUL");
            } else {
                loader.load(source, manifest,  false, true);
                Util.displayLoaderStatus(loader);
            }

            // If requested, activate the newly loaded scheme(s) ...
            if (activate) {
                AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
                for (int i = 0; i < refs.length; i++) {
                    AbsoluteCodingSchemeVersionReference ref = refs[i];
                    lbsm.activateCodingSchemeVersion(ref);
                    Util.displayAndLogMessage("Scheme activated>> " + ref.getCodingSchemeURN() + " Version>> "
                            + ref.getCodingSchemeVersion());
                }
            }
        }
    }

    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("in", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("v", "validate", true, "Validation only; no load. If specified, 'a' and 't' "
                + "are ignored. 0 to verify well-formed xml; 1 to check validity.");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("a", "activate", false, "ActivateScheme on successful load; if unspecified the "
                + "vocabulary is loaded but not activated.");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("t", "tag", true, "An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.");
        o.setArgName("id");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("mf", "manifest", true, "URI or path specifying location of the manifest file.");
        o.setArgName("uri");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

    
}
