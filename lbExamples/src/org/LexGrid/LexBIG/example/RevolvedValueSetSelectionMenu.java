
package org.LexGrid.LexBIG.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;

/**
 * Helper class used to display a list of available code systems and return the
 * user selection.
 * 
 */
public class RevolvedValueSetSelectionMenu {
    static final String Dash5 = "-----";
    static final String Dash15 = "---------------";
    static final String Dash25 = "-------------------------";
    static final String Dash30 = "------------------------------";

    public RevolvedValueSetSelectionMenu() {
        super();
    }

    /**
     * Displays a list of available coding schemes.
     * 
     * @return The ordered list of coding scheme summaries as displayed.
     * @throws LBException
     *             If an error occurs displaying the list.
     */
    protected List<CodingScheme> displayCodingSchemes() throws LBException {
        List<CodingScheme> choices = new ArrayList<CodingScheme>();

        LexEVSResolvedValueSetService lrvs = new LexEVSResolvedValueSetServiceImpl();
        List<CodingScheme> schemes = lrvs.listAllResolvedValueSets();

        if (schemes.size() == 0)
            Util.displayMessage("No coding schemes found.");
        else {
            Formatter f = new Formatter();
            String format = "%-5.5s|%-30.30s|%-25.25s|%-15.15s\n";
            Object[] hSep = new Object[] { Dash5, Dash30, Dash25, Dash15 };
            f.format(format, hSep);
            f.format(format, new Object[] { "#", "Local Name", "Version", "Tag" });
            f.format(format, hSep);
            for (int i = 1; i <= schemes.size(); i++) {
                String nu = String.valueOf(i);

                CodingScheme css = schemes.get(i - 1);
                choices.add(css);

                // Evaluate local name
                String localName = css.getCodingSchemeName();
                if (localName != null && localName.length() > 30)
                    localName = localName.substring(0, 28) + ">>";

                // Evaluate version
                String version = css.getRepresentsVersion();
                if (version != null && version.length() > 25)
                    version = version.substring(0, 23) + ">>";

                f.format(format, new Object[] { nu, localName, version, "No tag" });


                f.format(format, hSep);
            }
            Util.displayMessage(f.out().toString());
            f.close();
            Util.displayMessage("");
            Util.displayMessage("NOTE: >> indicates column value exceeds the available width.");
        }
        return choices;

    }

    /**
     * Display the list of available coding schemes and process the user
     * selection.
     * 
     * @return A coding scheme summary correponding to the user selection; null
     *         if no selection is made or an error occurs.
     */
    public CodingScheme displayAndGetSelection(String message) {
        CodingScheme css = null;
        try {
            // Display choices ...
            List<CodingScheme> choices = displayCodingSchemes();

            if (choices.size() > 0) {
                // Process the user input ...
                try {
                    Util.displayMessage("Enter the number of the Resolved Value Set to use, then <Enter> :");

                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    int i = new Integer(br.readLine()).intValue();
                    if (i > 0 && i <= choices.size())
                        css = choices.get(i - 1);
                    else
                        Util.displayMessage("Entered value is out of range.");
                } catch (NumberFormatException nfe) {
                    Util.displayMessage("Entered value must be numeric.");
                } catch (IOException ioe) {
                    Util.displayAndLogError("An error occurred while processing the entered value", ioe);
                }
            } else {
                Util.displayMessage("No code systems are available.");
            }
        } catch (Exception e) {
            Util.displayAndLogError("An error occurred processing the available code systems", e);
        }
        return css;
    }
}