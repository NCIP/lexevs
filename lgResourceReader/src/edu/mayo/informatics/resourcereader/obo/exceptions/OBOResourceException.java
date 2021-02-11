
package edu.mayo.informatics.resourcereader.obo.exceptions;

import edu.mayo.informatics.resourcereader.core.IF.ResourceException;

/**
 * OBO specific ResourceException
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@SuppressWarnings("serial")
public class OBOResourceException extends ResourceException {
    public OBOResourceException(String msg) {
        super(msg);
    }
}