package org.LexGrid.LexBIG.admin;

import java.net.URI;

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
                if (cl.hasOption("v"))
                    vl = cl.getOptionValue("v");
            } catch (Exception e) {
                Util
                        .displayCommandOptions(
                                "LoadGraphDatabase",
                                options,
                                "\n LoadGraphDatabase -in \"http://target.terminology url\" -v \"1.0\"", e);
                return;
            }

            // Interpret provided values ...
            URI source = new URI(cl.getOptionValue("in"));

            // Find the registered extension handling this type of load ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);
            LexEVSArangoGraphingDbLoader loader = (LexEVSArangoGraphingDbLoader) lbsm
                    .getLoader(org.LexGrid.LexBIG.Impl.loaders.LexEVSArangoGraphingDbLoader.name);

            if(vl != null){
                loader.useVersionOrTag(vl);}
                loader.load(source);


        }
    }

    private Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("in", "input", true, "Unique URI identifier of the source terminology");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("v", "version", true, "Terminology version");
        o.setArgName("int");
        o.setRequired(false);
        options.addOption(o);


        return options;
    }

    
}
