
package org.LexGrid.LexBIG.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * Helper class used to display a list of available value set definitions and return the
 * user selection.
 * 
 */
public class ValueSetDefinitionSelectionMenu {
    static final String Dash5 = "-----";
    static final String Dash15 = "---------------";
    static final String Dash25 = "-------------------------";
    static final String Dash30 = "------------------------------";

    public ValueSetDefinitionSelectionMenu() {
        super();
    }

    /**
     * Displays a list of available value set definitions.
     * 
     * @return The ordered list of value set definition as displayed.
     * @throws LBException
     *             If an error occurs displaying the list.
     */
    protected List<ValueSetDefinition> displayValueSetDefinitions() throws LBException {
        List<ValueSetDefinition> choices = new ArrayList<ValueSetDefinition>();

        LexEVSValueSetDefinitionServices vs = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
        List<String> vsURIS = vs.listValueSetDefinitionURIs();

        if (vsURIS == null || vsURIS.size() == 0)
            Util.displayMessage("No value set definitions found.");
        else {

            Formatter f = new Formatter();

            String format = "%-5.5s|%-30.30s|%-30.30s|%-30.30s\n";
            Object[] hSep = new Object[] { Dash5, Dash30, Dash30, Dash30 };
            f.format(format, hSep);
            f.format(format, new Object[] { "#", "ValueSet Definition URI", "ValueSet Definition Name", "ValueSet Definition Version" });
            f.format(format, hSep);
            int i = 0;
            for (String uri : vsURIS) {
                String nu = String.valueOf(i++);
                ValueSetDefinition vsd = null;
                try {
                    vsd = vs.getValueSetDefinition(new URI(uri), null);
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                choices.add(vsd);

                // Evaluate name
                String name = vsd.getValueSetDefinitionName();
                if (name != null && name.length() > 30)
                    name = name.substring(0, 28) + ">>";
                
                // Evaluate version(Revision)
                String version = null;
                if (vsd.getEntryState() != null && vsd.getEntryState().getContainingRevision() != null)
                    version = vsd.getEntryState().getContainingRevision();
                
                // Output the first line
                f.format(format, new Object[] { nu, uri, name, version });

                // Output separator
                f.format(format, hSep);
            }
            Util.displayMessage(f.out().toString());
            Util.displayMessage("");
            Util.displayMessage("NOTE: >> indicates column value exceeds the available width.");
        }
        return choices;

    }

    /**
     * Display the list of available value set definitions and process the user
     * selection.
     * 
     * @return A valueSetDefinition corresponding to the user selection; null
     *         if no selection is made or an error occurs.
     */
    public ValueSetDefinition displayAndGetSelection(String message) {
        ValueSetDefinition vsd = null;
        try {
            // Display choices ...
            List<ValueSetDefinition> choices = displayValueSetDefinitions();

            if (choices.size() >= 0) {
                // Process the user input ...
                try {
                    Util.displayMessage(message);

                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    int i = new Integer(br.readLine()).intValue();
                    if (i >= 0 && i <= choices.size())
                        vsd = choices.get(i);
                    else
                        Util.displayMessage("Entered value is out of range.");
                } catch (NumberFormatException nfe) {
                    Util.displayMessage("Entered value must be numeric.");
                } catch (IOException ioe) {
                    Util.displayAndLogError("An error occurred while processing the entered value", ioe);
                }
            } else {
                Util.displayMessage("No Value Set Definitions are available.");
            }
        } catch (Exception e) {
            Util.displayAndLogError("An error occurred processing the available value set definitions", e);
        }
        return vsd;
    }
}