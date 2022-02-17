
package org.LexGrid.LexBIG.Utility.logging;

/**
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public interface LgLoggerIF extends LgMessageDirectorIF {

    public void logMethod();

    public void logMethod(Object[] params);

    public void loadLogDebug(String message);

    public void loadLogError(String message, Throwable e);

    public void loadLogError(String message);

    public void loadLogWarn(String message, Throwable e);

    public void exportLogDebug(String message);

    public void exportLogError(String message, Throwable e);

    public void exportLogError(String message);

    public void exportLogWarn(String message, Throwable e);
}