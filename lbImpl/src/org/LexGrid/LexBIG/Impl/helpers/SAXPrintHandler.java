
package org.LexGrid.LexBIG.Impl.helpers;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.logging.LoggerFactory;
import org.xml.sax.SAXParseException;

/**
 * Simple handler that prints all encountered errors and retains the most recent
 * encountered error or fatal error.
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 */
public class SAXPrintHandler implements org.xml.sax.ErrorHandler {
    private SAXParseException err_ = null;

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * Construct a new handler printing to System.out as target.
     */
    public SAXPrintHandler() {
        super();
    }

    /**
     * Returns the most recent error or fatal error.
     * 
     * @return SAXParseException
     */
    @LgClientSideSafe
    public SAXParseException getError() {
        return err_;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException) This
     * corresponds to the definition of "error" in section 1.2 of the W3C XML
     * 1.0
     */
    @LgClientSideSafe
    public void error(SAXParseException sap) throws org.xml.sax.SAXException {
        getLogger().loadLogError("PARSING ERROR", sap);
        err_ = sap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     * The application must assume that the document is unusable after the
     * parser has invoked this method
     */
    @LgClientSideSafe
    public void fatalError(SAXParseException sap) throws org.xml.sax.SAXException {
        getLogger().loadLogError("PARSING ERROR", sap);
        err_ = sap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException) SAX
     * parsers will use this method to report conditions that are not errors or
     * fatal errors as defined by the XML recommendation.
     */
    @LgClientSideSafe
    public void warning(SAXParseException sap) throws org.xml.sax.SAXException {
        getLogger().loadLogWarn("PARSING ERROR", sap);
    }

}