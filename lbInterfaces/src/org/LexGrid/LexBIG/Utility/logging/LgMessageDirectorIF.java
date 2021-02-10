
package org.LexGrid.LexBIG.Utility.logging;

/**
 * Interface to help pass debug, error, and warning types of messages around
 * from component to component.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu ">Kevin Peterson </A>
 * @author <A HREF="mailto:daniel.armbrust@mayo.edu ">Daniel Armbrust </A>
 */
public interface LgMessageDirectorIF {

    /**
     * Output to indicate system is busy - spin a cursor, print a dot, etc.
     */
    public void busy();

    /**
     * Displays a status type of message (not an error or a warning)
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String info(String message);

    /**
     * Take a debug message.
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String debug(String message);

    /**
     * Display a warning to the user.
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String warn(String message);

    /**
     * Display a warning to the user
     * 
     * @param sourceException
     *            The source exception that caused the warning
     * @return Implementations may return a string, such as a log id.
     */
    public String warn(String message, Throwable sourceException);

    /**
     * Display a non-fatal error to the user.
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String error(String message);

    /**
     * Display a non-fatal error to the user
     * 
     * @param sourceException
     *            The source exception that caused the error.
     */
    public String error(String message, Throwable sourceException);

    /**
     * Display a fatal error to the user.
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String fatal(String message);

    /**
     * Display a fatal error to the user.
     * 
     * @return Implementations may return a string, such as a log id.
     * @param sourceException
     *            The source exception that caused the error.
     */
    public String fatal(String message, Throwable sourceException);

    /**
     * Display a fatal error to the user - and then throw an exception.
     */
    public void fatalAndThrowException(String message) throws Exception;

    /**
     * Display a fatal error to the user - and then throw an exception.
     * 
     * @param sourceException
     *            The source exception that caused the error.
     */
    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception;
}