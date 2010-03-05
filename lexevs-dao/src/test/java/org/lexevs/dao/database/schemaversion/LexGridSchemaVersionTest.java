package org.lexevs.dao.database.schemaversion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class LexGridSchemaVersionTest extends LexEvsDbUnitTestBase {

	@Test
	public void testParseDatabaseVersion(){
		LexGridSchemaVersion version = LexGridSchemaVersion.parseStringToVersion("1.2");
		assertTrue(version.getMajorVersion() == 1);
		assertTrue(version.getMinorVersion() == 2);
	}
	
	@Test
	public void testEquals(){
		LexGridSchemaVersion version1 = LexGridSchemaVersion.parseStringToVersion("1.2");
		LexGridSchemaVersion version2 = LexGridSchemaVersion.parseStringToVersion("1.2");
		assertTrue(version1.isEqualVersion(version2));
		assertTrue(version2.isEqualVersion(version1));
	}
	
	@Test
	public void testNotEquals(){
		LexGridSchemaVersion version1 = LexGridSchemaVersion.parseStringToVersion("1.2");
		LexGridSchemaVersion version2 = LexGridSchemaVersion.parseStringToVersion("2.2");
		assertFalse(version1.isEqualVersion(version2));
		assertFalse(version2.isEqualVersion(version1));
	}
}
