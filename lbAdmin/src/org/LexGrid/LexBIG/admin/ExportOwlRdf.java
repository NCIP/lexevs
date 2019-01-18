package org.LexGrid.LexBIG.admin;

import java.net.URI;
import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.Impl.exporters.OwlRdfExporterImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.CnsCngPair;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.FilterParser;

public class ExportOwlRdf {

    public static void main(String[] args) {
        try {
            new ExportOwlRdf().run(args);
        } catch (LBResourceUnavailableException e) {
            Util.displayAndLogMessage(e.getMessage());
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public ExportOwlRdf() {
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
                Util.displayCommandOptions("ExportOwlRdf", options,
                        "\n ExportOwlRdf -out \"file:///path/to/dir\" -u \"name\" -v \"PRODUCTION\" -f"
                                + Util.getURIHelp(), e);
                Util.displayMessage(Util.getPromptForSchemeHelp());
                return;
            }

            // Interpret provided values ...
            URI destination = Util.string2FileURI(cl.getOptionValue("out"));
            
            String urn = cl.getOptionValue("u");
            String ver = cl.getOptionValue("v");
            boolean overwrite = cl.hasOption("f");
            Util.displayAndLogMessage("WRITING TO: " + destination.toString());

            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            LexBIGServiceManager lbsm = lbs.getServiceManager(null);

            // Find in list of registered vocabularies ...
            CodingSchemeSummary css = null;
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

            // Find the registered extension handling this type of export ...
            OwlRdfExporterImpl exporter = (OwlRdfExporterImpl) lbsm.getExporter(OwlRdfExporterImpl.name);

            // Perform the requested action ...
            CnsCngPair cngCngPair = FilterParser.parse(lbs, css.getCodingSchemeURI(), css.getRepresentsVersion(), cl);
            exporter.setCng(cngCngPair.getCng());
            exporter.setCns(cngCngPair.getCns());
            
            exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(css), destination, overwrite,
                    false, true);
            Util.displayExporterStatus(exporter);
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

        o = new Option("out", "output", true, "URI or path of the directory to contain the "
                + "resulting owl file.  The file name will be automatically " + "derived from the coding scheme name.");
        o.setArgName("uri");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("u", "urn", true, "URN or local name of the coding scheme to export.");
        o.setArgName("name");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("v", "version", true,
                "The assigned tag/label or absolute version identifier of the coding scheme.");
        o.setRequired(true);
        options.addOption(o);

        o = new Option("f", "force", false, "If specified, allows the destination file to be overwritten "
                + "if present.");
        o.setRequired(false);
        options.addOption(o);
        
        return options;
    }

}