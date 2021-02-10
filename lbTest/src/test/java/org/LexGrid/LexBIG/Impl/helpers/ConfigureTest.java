
package org.LexGrid.LexBIG.Impl.helpers;

import junit.framework.TestCase;

import org.lexevs.exceptions.InitializationException;
import org.lexevs.logging.Logger;
import org.lexevs.system.constants.SystemVariables;

public class ConfigureTest extends TestCase {

	public void testStartUpLexBigBadConfig() throws Exception {
		String oldName = SystemVariables.CONFIG_FILE_NAME;
		SystemVariables.CONFIG_FILE_NAME = "zzzzz.props";
		
		try {
			new SystemVariables(new Logger());
		} catch(InitializationException e){
			System.out.println(e.getMessage());
			return;
		} finally {
			SystemVariables.CONFIG_FILE_NAME = oldName;
		}
		
		fail("InitializationException not thrown.");
	}
}