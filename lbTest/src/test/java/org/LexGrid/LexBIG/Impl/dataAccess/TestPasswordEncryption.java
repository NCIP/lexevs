package org.LexGrid.LexBIG.Impl.dataAccess;

import java.sql.Connection;

import junit.framework.TestCase;

import org.LexGrid.util.sql.DBUtility;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.utility.CryptoUtility;

public class TestPasswordEncryption extends TestCase {

	public void testEncryptionDecryption() {

		String testPWD = "lexgrid";

		String cryptedPWD = CryptoUtility.encrypt(testPWD);

		assertFalse(testPWD.equals(cryptedPWD));

		String decryptPWD = CryptoUtility.decrypt(cryptedPWD);

		assertTrue(testPWD.equals(decryptPWD));

		SystemVariables sysVar = ResourceManager.instance()
				.getSystemVariables();

		String url = sysVar.getAutoLoadDBURL();
		String driver = sysVar.getAutoLoadDBDriver();
		String user = sysVar.getAutoLoadDBUsername();
		String password = sysVar.getAutoLoadDBPassword();

		try {
			Connection conn = DBUtility.connectToDatabase(url, driver, user,
					password);
		} catch (Exception e) {
			fail();
		}
	}
}
