
package org.lexevs.logging;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;

/**
 * The Class SimpleLogger.
 * 
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SimpleLogger implements LgLoggerIF {

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#busy()
     */
    public void busy() {
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#debug(java.lang.String)
     */
    public String debug(String message) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#error(java.lang.String, java.lang.Throwable)
     */
    public String error(String message, Throwable sourceException) {
        System.out.println("Error: " + message);
        sourceException.printStackTrace();
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#error(java.lang.String)
     */
    public String error(String message) {
        System.out.println("Error: " + message);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatal(java.lang.String, java.lang.Throwable)
     */
    public String fatal(String message, Throwable sourceException) {
        System.out.println("Error: " + message);
        sourceException.printStackTrace();
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatal(java.lang.String)
     */
    public String fatal(String message) {
        System.out.println("Error: " + message);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatalAndThrowException(java.lang.String, java.lang.Throwable)
     */
    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception {
        System.out.println("Error: " + message);
        sourceException.printStackTrace();
        throw new Exception(message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatalAndThrowException(java.lang.String)
     */
    public void fatalAndThrowException(String message) throws Exception {
        System.out.println("Error: " + message);
        throw new Exception(message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#info(java.lang.String)
     */
    public String info(String message) {
        System.out.println("Info: " + message);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#warn(java.lang.String, java.lang.Throwable)
     */
    public String warn(String message, Throwable sourceException) {
        System.out.println("Warn: " + message);
        sourceException.printStackTrace();
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#warn(java.lang.String)
     */
    public String warn(String message) {
        System.out.println("Warn: " + message);
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#logMethod()
     */
    public void logMethod() {
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#logMethod(java.lang.Object[])
     */
    public void logMethod(Object[] params) {
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#loadLogDebug(java.lang.String)
     */
    public void loadLogDebug(String message) {
        System.out.println("Debug: " + message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#loadLogError(java.lang.String, java.lang.Throwable)
     */
    public void loadLogError(String message, Throwable e) {
        System.out.println("Error: " + message);
        e.printStackTrace();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#loadLogError(java.lang.String)
     */
    public void loadLogError(String message) {
        System.out.println("Error: " + message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#loadLogWarn(java.lang.String, java.lang.Throwable)
     */
    public void loadLogWarn(String message, Throwable e) {
        System.out.println("Warn: " + message);
        e.printStackTrace();
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#exportLogDebug(java.lang.String)
     */
    public void exportLogDebug(String message) {
        System.out.println("Debug: " + message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#exportLogError(java.lang.String, java.lang.Throwable)
     */
    public void exportLogError(String message, Throwable e) {
        System.out.println("Error: " + message);
        e.printStackTrace();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#exportLogError(java.lang.String)
     */
    public void exportLogError(String message) {
        System.out.println("Error: " + message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgLoggerIF#exportLogWarn(java.lang.String, java.lang.Throwable)
     */
    public void exportLogWarn(String message, Throwable e) {
        System.out.println("Warn: " + message);
        e.printStackTrace();
    }
}