package org.lexevs.dao.indexer.utility;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

public class CodingSchemeMetaDataTest {
	CodingSchemeMetaData csmd1;
	CodingSchemeMetaData csmd2;
	String codingSchemeUri = "this.is.a.uri";
	String codingSchemeVersion = "v6.7.8";
	String 	codingSchemeName = "TestScheme";
	NamedDirectory directory;
	NamedDirectory directory2;
	
	@Before
	public void setUp() throws Exception {
		directory = new NamedDirectory(FSDirectory.open(Paths.get("file")), codingSchemeName);
		directory2 = new NamedDirectory(FSDirectory.open(Paths.get("file")), codingSchemeName);
		csmd1 = new CodingSchemeMetaData(
				codingSchemeUri,
				codingSchemeVersion,
				codingSchemeName,
				directory);
		csmd2 = new CodingSchemeMetaData(
				codingSchemeUri,
				codingSchemeVersion,
				codingSchemeName,
				directory2);
		
	}

	@Test
	public void testCodingSchemeMetaDataEquals() {
		assertTrue(csmd1.equals(csmd2));
	}
	
	@Test
	public void testCodingSchemeMetaDataHash() {
        Set<CodingSchemeMetaData> csmds = new HashSet<CodingSchemeMetaData>();
        csmds.add(csmd1);
        csmds.add(csmd2);
        assertTrue(csmds.size() == 1);
	}
	
	@Test
	public void testCodingSchemeMetaDataGetURIVersion() {

        assertTrue(csmd1.getNameVersionKey().equals(codingSchemeName + "[:]" + codingSchemeVersion));
	}
	
	@After
	public void tearDown() throws Exception{
		FileUtils.deleteDirectory(new File("file"));
	}
	
	


}
