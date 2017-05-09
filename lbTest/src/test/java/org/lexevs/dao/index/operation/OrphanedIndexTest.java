package org.lexevs.dao.index.operation;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lexevs.system.constants.SystemVariables;

public class OrphanedIndexTest {
	
	String uri = "urn:oid:11.11.0.1";
	String ver = "1.0";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String location = SystemVariables.getAbsoluteIndexLocation();
		File testIndex = new File(location + System.getProperty("file.separator") + "myIndexTest" );
		testIndex.mkdir();
	}
	
	@Rule public ExpectedException thrown = ExpectedException.none();
	@Test
	public void test() throws RuntimeException{
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Indexes seem to be created in another context as they do not match "
				+ "database registrations. If these indexes were copied from another service then "
				+ "please edit the config.props file to match the source service. Otherwise delete "
				+ "them and rebuild them from scratch");
		LexBIGServiceImpl.defaultInstance();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		CleanUpUtility.removeAllUnusedDatabases();
		assertTrue(CleanUpUtility.listUnusedDatabases().length == 0);
		CleanUpUtility.removeAllUnusedIndexes();
		assertTrue(CleanUpUtility.listUnusedIndexes().length == 0);
	}

}
