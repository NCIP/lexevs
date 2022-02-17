
package org.LexGrid.LexBIG.Impl.testUtility;

import junit.framework.TestCase;

/**
 * This set of tests loads the necessary data for the full suite of JUnit tests.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class FinalCleanUp extends TestCase {
    public void testResetTestConfigFolder() {
        ServiceHolder.instance().removeTestFolder();
    }
}