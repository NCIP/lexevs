
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7;

/**
 * Association data container
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * 
 */
public class HL7AssocContainer {

    private String association;
    private int sourceCode;
    private int targetCode;

    HL7AssocContainer(String association, int source, int target) {
        this.association = association;
        this.sourceCode = source;
        this.targetCode = target;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public int getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(int sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(int targetCode) {
        this.targetCode = targetCode;
    }

}