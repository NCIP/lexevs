
package org.LexGrid.LexBIG.Impl.dataAccess;

import junit.framework.TestCase;

import org.lexevs.system.utility.CryptoUtility;

public class TestPasswordEncryption extends TestCase {

	public void testEncryptionDecryption() {

		String testPWD = "lexgrid";

		String cryptedPWD = CryptoUtility.encrypt(testPWD);

		assertFalse(testPWD.equals(cryptedPWD));

		String decryptPWD = CryptoUtility.decrypt(cryptedPWD);

		assertTrue(testPWD.equals(decryptPWD));
	}
}