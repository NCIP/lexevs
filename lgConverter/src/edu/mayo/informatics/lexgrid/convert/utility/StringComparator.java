
package edu.mayo.informatics.lexgrid.convert.utility;

/**
 * Used for sorting strings.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 5296 $ checked in on $Date: 2007-05-16
 *          21:55:43 +0000 (Wed, 16 May 2007) $
 */
public class StringComparator implements java.util.Comparator {
    public int compare(Object o1, Object o2) {
        return ((String) o1).compareToIgnoreCase((String) o2);
    }

}