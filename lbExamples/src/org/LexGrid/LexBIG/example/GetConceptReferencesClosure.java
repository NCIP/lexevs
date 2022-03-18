
package org.LexGrid.LexBIG.example;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.LexBIGServiceConvenienceMethodsImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public class GetConceptReferencesClosure {

/**
     * @param args
     */
public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Example: GetConceptReferencesClosure \"C3262\" \"subClassOf\"");
            return;
        }
        ;

        try {
            String code = args[0];
            String relation = args[1];
            new GetConceptReferencesClosure().run(code, relation);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String code, String rel) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css != null) {
            LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
            String scheme = css.getCodingSchemeURI();
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(css.getRepresentsVersion());
            LexBIGServiceConvenienceMethodsImpl methods = (LexBIGServiceConvenienceMethodsImpl) lbSvc.getGenericExtension("LexBIGServiceConvenienceMethods");
            printAncestors(methods, scheme, csvt, code, rel);
            printDescendants(methods, scheme, csvt, code, rel);
        }
        
        
    }
    
    public void printAncestors(
            LexBIGServiceConvenienceMethodsImpl methods, String scheme, 
            CodingSchemeVersionOrTag csvt, String code, String rel) throws LBParameterException{
        long start = System.currentTimeMillis();
        List<ResolvedConceptReference> references =  methods.getAncestorsInTransitiveClosure(scheme, csvt, code, rel);
        int counter = 0;
        for(ResolvedConceptReference ref: references){
            counter++;
            System.out.println("\t\t" + ref.getEntityDescription().getContent());
        }
        long end = System.currentTimeMillis();
        System.out.println("counter: " + counter);
        System.out.println("Time expended: " + (end - start));
    }
    
    public void printDescendants(
            LexBIGServiceConvenienceMethodsImpl methods, String scheme, 
            CodingSchemeVersionOrTag csvt, String code, String rel) throws LBParameterException{
        List<ResolvedConceptReference> references =  methods.getDescendentsInTransitiveClosure(scheme, csvt, code, rel);
        int counter = 0;
        long start = System.currentTimeMillis();
        
        for(ResolvedConceptReference ref: references){
            counter++;
            System.out.println(ref.getEntityDescription().getContent());
        }
        long end = System.currentTimeMillis();
        System.out.println("counter: " + counter);
        System.out.println("Time expended: " + (end - start));
    }


}