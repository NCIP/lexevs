
package org.LexGrid.LexBIG.example;

import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

/**
 * Example showing how to find concept properties and associations based on a
 * code.
 */
public class FindPropsAndAssocForCode {

    public FindPropsAndAssocForCode() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: FindPropsAndAssocForCode \"GM\"");
            return;
        }

        try {
            String code = args[0];
            new FindPropsAndAssocForCode().run(code);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    /**
     * Process the provided code.
     * 
     * @param code
     * @throws LBException
     */
    public void run(String code) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css != null) {
            LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
            String scheme = css.getCodingSchemeURI();
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(css.getRepresentsVersion());

            printProps(code, lbSvc, scheme, csvt);
            printFrom(code, lbSvc, scheme, csvt);
            printTo(code, lbSvc, scheme, csvt);
        }
    }

    /**
     * Display properties for the given code.
     * 
     * @param code
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @return
     * @throws LBException
     */
    protected boolean printProps(String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        // Perform the query ...
        ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(new String[] { code }, scheme);

        ResolvedConceptReferenceList matches = lbSvc.getCodingSchemeConcepts(scheme, csvt).restrictToStatus(
                ActiveOption.ALL, null).restrictToCodes(crefs).resolveToList(null, null, null, 1);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
                    .nextElement();

            Entity node = ref.getEntity();

            Property[] props = node.getProperty();
            for (int i = 0; i < props.length; i++) {
                Property prop = props[i];
                Util.displayMessage(new StringBuffer().append("\tProperty name: ").append(prop.getPropertyName())
                        .append(" text: ").append(prop.getValue().getContent()).toString());
            }

        } else {
            Util.displayMessage("No match found!");
            return false;
        }

        return true;
    }

    /**
     * Display relations to the given code from other concepts.
     * 
     * @param code
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @throws LBException
     */
    @SuppressWarnings("unchecked")
    protected void printFrom(String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        Util.displayMessage("Pointed at by ...");

        // Perform the query ...
        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), false, true, 1, 1, new LocalNameList(), null,
                null, 1024);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

            while (refEnum.hasMoreElements()) {
                ResolvedConceptReference ref = refEnum.nextElement();
                AssociationList targetof = ref.getTargetOf();
                if(targetof != null){
                Association[] associations = targetof.getAssociation();

                for (int i = 0; i < associations.length; i++) {
                    Association assoc = associations[i];
                    Util.displayMessage("\t" + assoc.getAssociationName());

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

    /**
     * Display relations from the given code to other concepts.
     * 
     * @param code
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @throws LBException
     */
    @SuppressWarnings("unchecked")
    protected void printTo(String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        Util.displayMessage("Points to ...");

        // Perform the query ...
        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), true, false, 1, 1, new LocalNameList(), null,
                null, 1024);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

            while (refEnum.hasMoreElements()) {
                ResolvedConceptReference ref = refEnum.nextElement();
                AssociationList sourceof = ref.getSourceOf();
                
                if(sourceof != null){
                Association[] associations = sourceof.getAssociation();

                for (int i = 0; i < associations.length; i++) {
                    Association assoc = associations[i];
                    Util.displayMessage("\t" + assoc.getAssociationName());

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
}