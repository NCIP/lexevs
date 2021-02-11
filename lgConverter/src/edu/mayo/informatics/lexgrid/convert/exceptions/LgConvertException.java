
package edu.mayo.informatics.lexgrid.convert.exceptions;

/**
 * Exception for handling general conversion errors.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu ">Kevin Peterson </A>
 * @version 1.0 - cvs $Revision: 1.2 $ checked in on $Date: 2005/09/27 19:52:23
 *          $
 */
public class LgConvertException extends Exception {

    private static final long serialVersionUID = 937874412463504273L;

    public LgConvertException() {
        super();
    }

    public LgConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public LgConvertException(Throwable cause) {
        super(cause);
    }

    public LgConvertException(String message) {
        super(message);
    }

}