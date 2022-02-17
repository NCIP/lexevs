
package org.LexGrid.LexBIG.example;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * Example showing how to find if one value set is a subset of another. 
 * 
 * A list of value Set Definition available in the system will be displayed for selection.
 * 
 * Example: CheckForValueSetSubSet
 * 
 */
public class CheckForValueSetSubSet {
    private String childVSDMessage = "Enter the number of the CHILD Value Set Definition to use, then <Enter> :";
    private String parentVSDMessage = "Enter the number of the PARENT Value Set Definition to use, then <Enter> :";
    
   public CheckForValueSetSubSet() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            new CheckForValueSetSubSet().run();
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run() throws LBException {
        ValueSetDefinition childVSD = Util.promptForValueSetDefinition(childVSDMessage);
        if (childVSD != null) {
            ValueSetDefinition parentVSD = Util.promptForValueSetDefinition(parentVSDMessage);
            if (parentVSD != null) {
                AbsoluteCodingSchemeVersionReferenceList acsvList = null;
                Util.displayMessage("Now select Code System to use to resolve Value Set Definitions");
                CodingSchemeSummary css = Util.promptForCodeSystem();
                if (css != null)
                {
                    acsvList = new AbsoluteCodingSchemeVersionReferenceList();
                    acsvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(css.getCodingSchemeURI(), css.getRepresentsVersion()));
                }
                
                LexEVSValueSetDefinitionServices vsdServ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
                boolean subset = false;
                try {
                    subset = vsdServ.isSubSet(new URI(childVSD.getValueSetDefinitionURI()), new URI(parentVSD.getValueSetDefinitionURI()), acsvList, null);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                
                if (subset)
                {
                    Util.displayMessage("Value Set : '" + childVSD.getValueSetDefinitionURI() + "' is subset of Value Set :" + parentVSD.getValueSetDefinitionURI());
                }
                else
                {
                    Util.displayMessage("Value Set : '" + childVSD.getValueSetDefinitionURI() + "' is NOT a subset of Value Set :" + parentVSD.getValueSetDefinitionURI());
                }
            }
        }
    }    
}