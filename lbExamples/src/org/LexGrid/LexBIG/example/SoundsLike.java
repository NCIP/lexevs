
package org.LexGrid.LexBIG.example;

import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;

/**
 * Example showing how to list concepts with presentation text that 'sounds
 * like' a specified value.
 */
public class SoundsLike {

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: SoundsLike \"hart ventrickl\"");
            return;
        }
        ;
        try {
            String text = args[0];
            new SoundsLike().run(text);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String text) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css != null) {
            LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
            String scheme = css.getCodingSchemeURI();
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(css.getRepresentsVersion());

            CodedNodeSet nodes = lbSvc.getCodingSchemeConcepts(scheme, csvt).restrictToStatus(ActiveOption.ALL, null)
                    .restrictToMatchingDesignations(text, SearchDesignationOption.ALL,
                            MatchAlgorithms.DoubleMetaphoneLuceneQuery.toString(), null);

            // Sort by search engine recommendation & code ...
            SortOptionList sortCriteria = Constructors.createSortOptionList(new String[] { "matchToQuery", "code" });

            // Analyze the result ...
            ResolvedConceptReferenceList matches = nodes.resolveToList(sortCriteria, null, null, 10);
            if (matches.getResolvedConceptReferenceCount() > 0) {
                for (Enumeration refs = matches.enumerateResolvedConceptReference(); refs.hasMoreElements();) {
                    ResolvedConceptReference ref = (ResolvedConceptReference) refs.nextElement();
                    Util.displayMessage("Matching code: " + ref.getConceptCode());
                    Util.displayMessage("\tDescription: " + ref.getEntityDescription().getContent());
                }
            } else {
                Util.displayMessage("No match found!");
            }
        }
    }

}