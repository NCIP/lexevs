
package org.LexGrid.LexBIG.example;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.EntityDescription;

/**
 * Example showing how to find all concepts codes related to another code with
 * distance 1.
 */
public class FindRelatedCodes {

    public FindRelatedCodes() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Example: FindRelatedCodes \"GM\" \"hasSubtype\"");
            return;
        }
        ;

        try {
            String code = args[0];
            String relation = args[1];
            new FindRelatedCodes().run(code, relation);
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

            printFrom(code, rel, lbSvc, scheme, csvt);
            printTo(code, rel, lbSvc, scheme, csvt);
        }
    }

    /**
     * Display relations from the given code to other concepts.
     * 
     * @param code
     * @param relation
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @throws LBException
     */
    protected void printFrom(String code, String relation, LexBIGService lbSvc, String scheme,
            CodingSchemeVersionOrTag csvt) throws LBException {
        // Perform the query ...
        Util.displayMessage("Pointed at by ...");

        // Perform the query ...
        NameAndValue nv = new NameAndValue();
        NameAndValueList nvList = new NameAndValueList();
        nv.setName(relation);
        nvList.addNameAndValue(nv);

        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).restrictToAssociations(nvList,
                null).resolveAsList(ConvenienceMethods.createConceptReference(code, scheme), false, true, 1, 1,
                new LocalNameList(), null, null, 1024);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
                    .nextElement();

            // Print the associations
            AssociationList targetof = ref.getTargetOf();
            if(targetof != null){
            Association[] associations = targetof.getAssociation();
            for (int i = 0; i < associations.length; i++) {
                Association assoc = associations[i];
                AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
                for (int j = 0; j < acl.length; j++) {
                    AssociatedConcept ac = acl[j];
                    EntityDescription ed = ac.getEntityDescription();
                    Util.displayMessage("\t\t" + ac.getConceptCode() + "/"
                            + (ed == null ? "**No Description**" : ed.getContent()));
                }
            }
            }
        }

    }

    /**
     * Display relations to the given code from other concepts.
     * 
     * @param code
     * @param relation
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @throws LBException
     */
    protected void printTo(String code, String relation, LexBIGService lbSvc, String scheme,
            CodingSchemeVersionOrTag csvt) throws LBException {
        Util.displayMessage("Points to ...");

        // Perform the query ...
        NameAndValue nv = new NameAndValue();
        NameAndValueList nvList = new NameAndValueList();
        nv.setName(relation);
        nvList.addNameAndValue(nv);

        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).restrictToAssociations(nvList,
                null).resolveAsList(ConvenienceMethods.createConceptReference(code, scheme), true, false, 1, 1,
                new LocalNameList(), null, null, 1024);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
                    .nextElement();

            // Print the associations
            AssociationList sourceof = ref.getSourceOf();
            if(sourceof != null){
            Association[] associations = sourceof.getAssociation();
            for (int i = 0; i < associations.length; i++) {
                Association assoc = associations[i];
                AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
                for (int j = 0; j < acl.length; j++) {
                    AssociatedConcept ac = acl[j];
                    EntityDescription ed = ac.getEntityDescription();
                    Util.displayMessage("\t\t" + ac.getConceptCode() + "/"
                            + (ed == null ? "**No Description**" : ed.getContent()));
                }
            }
            }
        }
    }
}