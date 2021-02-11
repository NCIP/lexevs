
package org.LexGrid.LexBIG.example;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.LexBIGServiceConvenienceMethodsImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public class GetAllIncomingConceptsForAssociation {

/**
     * @param args
     */
public GetAllIncomingConceptsForAssociation() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Example: GetAllIncomingConceptsForAssociation \"C14225\" \"Gene_Found_In_Organism\" \"3100\"");
            return;
        }
        ;

        try {
            String code = args[0];
            String relation = args[1];
            int max = Integer.valueOf(args[2]);
            new GetAllIncomingConceptsForAssociation().run(code, relation, max);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void run(String code, String rel, int maxToReturn) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css != null) {
            LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
            
            String scheme = css.getCodingSchemeURI();
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(css.getRepresentsVersion());
            LexBIGServiceConvenienceMethodsImpl lbscm = new LexBIGServiceConvenienceMethodsImpl();
            lbscm.setLexBIGService(lbSvc);
            long start = System.currentTimeMillis();
            AssociatedConceptList refList = lbscm.getallIncomingConceptsForAssociation(scheme, csvt, code, rel, maxToReturn);
            int counter = 0;
            Iterator<AssociatedConcept> iterator = (Iterator<AssociatedConcept>) refList.iterateAssociatedConcept();
            while(iterator.hasNext()){
                
                ResolvedConceptReference ref = iterator.next();
                System.out.println(ref.getEntityDescription().getContent());
                counter++;
            }
            long end = System.currentTimeMillis();
            System.out.println("count: " + counter);
            System.out.println("time expended: " + (end-start));
        }
    }

}