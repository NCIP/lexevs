
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge21923 extends LexBIGServiceTestCase {
    final static String testID = "GForge21923";
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
   //TODO: This only is useful for backward compatibility tests.
   public void testMapNamespaceToCodingScheme() throws Exception {
	   /*
       Method method = SQLImplementedMethods.class.getDeclaredMethod("mapToCodingSchemeName", new Class[]{SQLInterface.class, String.class, String.class});
       method.setAccessible(true);
       
       SQLInterface sqlInterface = ResourceManager.instance().getSQLInterface(AUTO_SCHEME, AUTO_VERSION);
       
       int maxConnectionsPerDb = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getMaxConnectionsPerDB();
       
       //This used to leak one connection per call -- we'll call it 3x the defined maxConnectionsPerDb, just to make sure.
       //If this reaches the end, the JUnit will have passed. If it blocks (i.e., the pool is exhausted) this will never complete.
       for(int i=0;i< maxConnectionsPerDb * 3;i++){
           method.invoke(new SQLImplementedMethods(), sqlInterface, AUTO_SCHEME, String.valueOf(i));
       }
       
       assertTrue(true);
       */
   }
}