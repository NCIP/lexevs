
package org.LexGrid.LexBIG.Impl.testUtility;

import junit.framework.TestCase;

/**
 * This test removes the last of the JUnit temp files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CleanUpTest2 extends TestCase {
    public void testResetTestConfigFolder() {
        ServiceHolder.instance().removeCurrentConfigTestFolder();
    }
}