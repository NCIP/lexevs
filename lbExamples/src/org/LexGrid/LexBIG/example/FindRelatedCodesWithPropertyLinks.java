
package org.LexGrid.LexBIG.example;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
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
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;

/**
 * Example showing how to find all concepts codes related to another code with
 * distance 1, plus the Property Link relations.
 */
public class FindRelatedCodesWithPropertyLinks {

    public FindRelatedCodesWithPropertyLinks() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: FindRelatedCodes \"GM\" ");
            return;
        }
        ;

        try {
            String code = args[0];
            new FindRelatedCodesWithPropertyLinks().run(code);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String code) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css != null) {
            LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
            String scheme = css.getCodingSchemeURI();
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(css.getRepresentsVersion());

            printFrom(code, lbSvc, scheme, csvt);
            printTo(code, lbSvc, scheme, csvt);
            printPropertyLinks(code, lbSvc, scheme, csvt);
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
    protected void printFrom(String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        Util.displayMessage("Pointed at by ...");

        // Perform the query ...

        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), false, true, 1, 1, new LocalNameList(), null,
                null, 1024);

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
     * Display Property Link relations.
     * 
     * @param code
     * @param relation
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @throws LBException
     */
    protected void printPropertyLinks(String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        Util.displayMessage("Property Links ...");

        // Perform the query ...

        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), true, true, 1, 1, new LocalNameList(), null,
                null, 1024);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
                    .nextElement();

            // check to see if it has property links -- if so display the
            // relationships
            PropertyLink[] propertyLinks = ref.getEntity().getPropertyLink();

            // analyze the Property Links
            for (int i = 0; i < propertyLinks.length; i++) {
                PropertyLink propertyLink = propertyLinks[i];

                // find the source of the Property Link
                String sourcePropertyId = propertyLink.getSourceProperty();

                // find the target of the Property Link
                String targetPropertyId = propertyLink.getTargetProperty();

                String sourceText = "";
                String targetText = "";

                // Link the Property Link source and target to the correspond
                // presentation text.
                // Example: Find Presentation with Property ID = T-1 -> get its
                // value and present it.
                for (int j = 0; j < ref.getEntity().getPresentation().length; j++) {
                    Presentation pres = ref.getEntity().getPresentation(j);
                    String propertyId = pres.getPropertyId();
                    if (propertyId.equals(sourcePropertyId)) {
                        sourceText = pres.getValue().getContent();
                    }
                    if (propertyId.equals(targetPropertyId)) {
                        targetText = pres.getValue().getContent();
                    }

                }
                Util.displayMessage("\t\t" + "Association: " + propertyLinks[i].getPropertyLink() + " " + "\n"
                        + "\t\t\t" + "Source: " + propertyLinks[i].getSourceProperty() + " " + sourceText + "\n"
                        + "\t\t\t" + "Target: " + propertyLinks[i].getTargetProperty() + " " + targetText);
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
    protected void printTo(String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        Util.displayMessage("Points to ...");

        // Perform the query ...

        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), true, false, 1, 1, new LocalNameList(), null,
                null, 1024);

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