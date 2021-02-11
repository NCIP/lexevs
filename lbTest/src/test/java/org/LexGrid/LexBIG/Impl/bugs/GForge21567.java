
package org.LexGrid.LexBIG.Impl.bugs;

import java.util.Arrays;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.lexevs.system.ResourceManager;

public class GForge21567 extends LexBIGServiceTestCase {
    final static String testID = "GForge21567";
    
    @Override
    protected String getTestID() {
        return testID;
    }

public void testResourceManagerThreadInitialization(){
	ResourceManager mgr = new ResourceManager();
	boolean memberThreadExists = Arrays.asList(mgr.getClass().getDeclaredFields()).stream().
	 filter(x -> x.getName().equals("deactivatorThread_")).findFirst().isPresent();
	assertFalse(memberThreadExists);
	boolean runnableClassExists = Arrays.asList(mgr.getClass().getDeclaredClasses()).stream().filter(x -> x.getName().equals("FutureDeactivatorThread")).findFirst().isPresent();
	assertFalse(runnableClassExists);
}

}