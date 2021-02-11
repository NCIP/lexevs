
package org.LexGrid.LexBIG.example;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * Example showing how to find the entity description assigned to a specific
 * code. The program accepts one parameter, the entity code.
 * 
 * Example: FindDescriptionForCode "C43652"
 */
public class FindDescriptionForCode {
    public FindDescriptionForCode() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: FindDescriptionForCode \"C12933\"");
            return;
        }

        try {
            String code = args[0];
            new FindDescriptionForCode().run(code);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String code) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css != null) {
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(css.getRepresentsVersion());

            // Get a new node set and restrict to the provided code ...
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            CodedNodeSet cns = lbs.getNodeSet(css.getLocalName(), csvt, null);
            cns.restrictToCodes(Constructors.createConceptReferenceList(code));

            // Retrieve the concept reference, if available ...
            ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, null, true, 1);
            if (rcrl.getResolvedConceptReferenceCount() < 1)
                Util.displayMessage("No match found for code: " + code);
            else {
                ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
                Util.displayMessage("Match Found for Code...: " + rcr.getConceptCode());
                Util.displayMessage("\tCoding Scheme Name...: " + rcr.getCodingSchemeName());
                Util.displayMessage("\tCoding Scheme URI....: " + rcr.getCodingSchemeURI());
                Util.displayMessage("\tCoding Scheme Version: " + rcr.getCodingSchemeVersion());
                Util.displayMessage("\tCode Namespace...... : "
                        + (rcr.getCodeNamespace() != null ? rcr.getCodeNamespace() : "<default>"));
                Util.displayMessage("\tCode Description.... : "
                        + (rcr.getEntityDescription() != null ? rcr.getEntityDescription().getContent() : ""));
                String typeString = "";
                for (Iterator<? extends String> types = rcr.iterateEntityType(); types.hasNext();)
                    typeString += (types.next() + (types.hasNext() ? "," : ""));
                Util.displayMessage("\tCode Entity Types... : " + typeString);
            }
        }
    }
}