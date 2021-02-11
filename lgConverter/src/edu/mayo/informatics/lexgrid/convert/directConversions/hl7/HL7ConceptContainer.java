
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7;

/**
 * Concept data container
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * 
 */
public class HL7ConceptContainer {

    public HL7ConceptContainer(String internalId, String conceptCode, String status) {
        this.internalId = internalId;
        this.conceptCode = conceptCode;
        this.status = status;
    }

    private String internalId;
    private String conceptCode;
    private String status;

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getConceptCode() {
        return conceptCode;
    }

    public void setConceptCode(String conceptCode) {
        this.conceptCode = conceptCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}