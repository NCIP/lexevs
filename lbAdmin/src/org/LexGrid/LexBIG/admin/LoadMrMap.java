package org.LexGrid.LexBIG.admin;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.MrMap_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.lexevs.system.ResourceManager;


public class LoadMrMap {

    public static void main(String[] args) {
        try {
            new LoadMrMap().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayTaggedMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public LoadMrMap() {
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

            } catch (Exception e) {
                Util.displayCommandOptions(
                                "LoadMrMapp",
                                options,"\n LoadMrMapp -inMap \"file:///path/to/MRMAP.RRF \" -inSat \"file:///path/to/MRSAT.RRF\" "
                                        		+ Util.getURIHelp(), e);
                return;
            }

            // Interpret provided values ...
            URI source = Util.string2FileURI(cl.getOptionValue("inMap"));
            URI sourceSat = Util.string2FileURI(cl.getOptionValue("inSat"));

                Util.displayTaggedMessage("LOADING FROM URI FOR MRMAP: " + source.toString());
                Util.displayTaggedMessage("LOADING FROM URI FOR MRSAT: " + sourceSat.toString());
                Util.displayTaggedMessage("POST LOAD AND ACTIVATION AVAILABLE ONLY ON MRMAP LOADS");


            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            MrMap_Loader loader = (MrMap_Loader) lbsm
                    .getLoader(org.LexGrid.LexBIG.Extensions.Load.MrMap_Loader.name);

                loader.load(source, sourceSat, null, null, null, null, null, null, null, null, null, false, true);
                Util.displayLoaderStatus(loader);

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

        o = new Option("inMap", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("inSat", "input", true, "URI or path specifying location of the source file.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        return options;
    }


}
