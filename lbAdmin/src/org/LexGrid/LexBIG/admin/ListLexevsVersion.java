
package org.LexGrid.LexBIG.admin;

import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.annotations.LgAdminFunction;
import org.lexevs.system.ResourceManager;

/**
 * List the LexEVS version number and build timestamp
 *
 * <pre>
 * Example: ListLexevsVersion
 * </pre>
 */
@LgAdminFunction
public class ListLexevsVersion {
    public static void main(String[] args) {
        try {
            new ListLexevsVersion().run(args);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public ListLexevsVersion() {
        super();
    }

    /**
     * Primary entry point for the program.
     * 
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        synchronized (ResourceManager.instance()) {
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();

            Util.displayAndLogMessage("LexEVS Build Version:   " + lbs.getLexEVSBuildVersion());
            Util.displayAndLogMessage("LexEVS Build Timestamp: " + lbs.getLexEVSBuildTimestamp());
        }
    }

}