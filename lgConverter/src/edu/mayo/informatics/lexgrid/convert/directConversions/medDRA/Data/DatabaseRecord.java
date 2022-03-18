
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public interface DatabaseRecord {
    public boolean fieldsValid() throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException;
    public String toString();
}