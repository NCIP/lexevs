
package edu.mayo.informatics.resourcereader.obo.exceptions;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * Obo specific exception to be thrown when some Method Has not been implemented
 * yet.
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@SuppressWarnings("serial")
public class OBOMethodNotImplementedException extends OBOResourceException {
    public OBOMethodNotImplementedException() {
        super("This method has not yet implemented.");
    }

    public OBOMethodNotImplementedException(String methodName) {
        super(((!StringUtils.isNull(methodName)) ? (methodName + "():") : "") + "This method has not yet implemented.");
    }
}