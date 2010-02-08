package org.LexGrid.LexBIG.Impl.dataAccess;

import java.sql.Connection;

import org.LexGrid.LexBIG.Utility.CryptoUtility;
import org.LexGrid.util.sql.DBUtility;

import junit.framework.TestCase;

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
