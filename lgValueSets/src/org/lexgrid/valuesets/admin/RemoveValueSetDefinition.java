
package org.lexgrid.valuesets.admin;


import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.annotations.LgAdminFunction;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.ResourceManager;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class RemoveValueSetDefinition {

/**
     * @param args
     */
@LgAdminFunction
    public static void main(String[] args) {
        try {
            new RemoveValueSetDefinition().run(args);
        } catch (final Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }

    }

    public void run(String[] args) throws Exception {
        synchronized (ResourceManager.instance()) {

            CommandLine c1 = null;
            final Options options = getCommandOptions();
            try {
                c1 = new BasicParser().parse(options, args);
            } catch (final ParseException e) {
                Util.displayCommandOptions(
                        "RemoveValueSetDefinition",
                        options,
                        "RemoveValueSetDefinition -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" ",
                        e);
                Util.displayMessage(Util.getPromptForSchemeHelp());
                return;
            }

            String urn = c1.getOptionValue("u");
            final boolean force = c1.hasOption("f");
            String matchedURN = null;
            final boolean isActive = false;

            final LexEVSValueSetDefinitionServices vss = new LexEVSValueSetDefinitionServicesImpl();

            if (urn != null) {
                urn = urn.trim();
                final List<String> uris = vss.listValueSetDefinitionURIs();
                final Iterator<String> iter = uris.iterator();
                while (iter.hasNext() && matchedURN == null) {
                    final String uri = iter.next();
                    if (uri.equals(urn)) {
                        matchedURN = urn;
                    }
                }
            } else {
                final List<String> uris = vss.listValueSetDefinitionURIs();
                final Iterator<String> iter = uris.iterator();
                System.out.println("No URN was included in the command line. Current URNs in the database are:");
                while (iter.hasNext() && matchedURN == null) {
                    final String uri = iter.next();
                    System.out.println(uri);
                }
            }

            if (matchedURN == null) {
                Util.displayAndLogMessage("No matching ValueSetDefinition found for the given urn " + urn);

            } else {
                vss.removeValueSetDefinition(URI.create(matchedURN));
                Util.displayAndLogMessage("ValueSetDefinition removed: " +  matchedURN);
            }

        }
    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    private Options getCommandOptions() {
        final Options options = new Options();
        Option o;

        o = new Option("u", "urn", true,
                "URN uniquely identifying the code system.");
        o.setArgName("name");
        o.setRequired(false);
        options.addOption(o);

        o = new Option("f", "force", false,
                "Force deactivation and removal without confirmation.");
        o.setRequired(false);
        options.addOption(o);

        return options;
    }

}