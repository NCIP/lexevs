
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7;

import java.util.Hashtable;

/**
 * Hash for concepts
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * 
 */
public class HL7ConceptList {

    private Hashtable list;
    int x;

    HL7ConceptList() {
        super();
        list = new Hashtable();
        x = 0;
    }

    public void addConcept(HL7ConceptContainer concept) {
        list.put(new Integer(x), concept);
        x++;
    }

    public HL7ConceptContainer getConcept(int x) {
        return (HL7ConceptContainer) list.get(new Integer(x));
    }

    public int size() {
        return list.size();
    }

}