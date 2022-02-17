
package edu.mayo.informatics.resourcereader.core.IF;

/**
 * ResourceType
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class ResourceType implements Comparable {
    private String typeName = null;

    protected ResourceType(String type) {
        typeName = type;
    }

    public String toString() {
        return typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public int compareTo(Object o) {
        return typeName.compareTo(o.toString());
    }
}