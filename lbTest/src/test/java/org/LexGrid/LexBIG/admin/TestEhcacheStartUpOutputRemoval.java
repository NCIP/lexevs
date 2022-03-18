
package org.LexGrid.LexBIG.admin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class TestEhcacheStartUpOutputRemoval {
    private static PrintStream sysOut;
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void before() throws InterruptedException {
		sysOut = System.out;
		System.setOut(new PrintStream(outContent));
	}

	@AfterClass
	public static void cleanUp() {
		System.setOut(sysOut);
	}

	@Test
	public void outTest() throws LBInvocationException {
		ListSchemes.main(new String[] {});
		assertTrue(outContent.toString().contains("Local Name")
				|| outContent.toString().contains("No coding schemes found"));
		assertFalse(outContent.toString().contains("ehcache"));
	}

}