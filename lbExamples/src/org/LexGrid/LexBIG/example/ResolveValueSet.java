
package org.LexGrid.LexBIG.example;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * Example showing how to resolve a value set definition. 
 * 
 * A list of value Set Definition available in the system will be displayed for selection. 
 * 
 * Also a list of Code System available in the system will be displayed for selection.
 * This code system will be used to resolve value set definition.
 * 
 * A list concepts that are member of value set definition will be displayed.
 * 
 * Example: ResolveValueSet
 * 
 */
public class ResolveValueSet {
    private String message = "Enter the number of the Value Set Definition to use, then <Enter> :";
   public ResolveValueSet() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            new ResolveValueSet().run();
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run() throws LBException {
        ValueSetDefinition vsd = Util.promptForValueSetDefinition(message);
        if (vsd != null) {
            AbsoluteCodingSchemeVersionReferenceList acsvList = null;
            Util.displayMessage("Now select Code System to use to resolve Value Set Definition");
            CodingSchemeSummary css = Util.promptForCodeSystem();
            if (css != null)
            {
                acsvList = new AbsoluteCodingSchemeVersionReferenceList();
                acsvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(css.getCodingSchemeURI(), css.getRepresentsVersion()));
            }
                
            LexEVSValueSetDefinitionServices vsdServ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
            ResolvedValueSetDefinition rVSD = null;
            try {
                rVSD = vsdServ.resolveValueSetDefinition(new URI(vsd.getValueSetDefinitionURI()), null, acsvList, null, null);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
                
            if (rVSD != null)
            {
                Util.displayMessage("Member of Value Set Definition (" + vsd.getValueSetDefinitionURI() + ") are :");
                ResolvedConceptReferencesIterator conceptItr = rVSD.getResolvedConceptReferenceIterator();
                while (conceptItr.hasNext())
                {
                    ResolvedConceptReference concept = conceptItr.next();
                    Util.displayMessage("Concept code : " + concept.getCode());
                    Util.displayMessage("\tCoding Scheme Name...: " + concept.getCodingSchemeName());
                    Util.displayMessage("\tCoding Scheme URI....: " + concept.getCodingSchemeURI());
                    Util.displayMessage("\tCoding Scheme Version: " + concept.getCodingSchemeVersion());
                }
            }
            else
            {
                Util.displayMessage("No members found for Value Set Definition : '" + vsd.getValueSetDefinitionURI() + "'");
            }
        }
    }    
}