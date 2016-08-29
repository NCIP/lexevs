package org.lexevs.dao.indexer.utility;

import static org.junit.Assert.*;

import java.util.Iterator;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

public class ConcurrentMetaDataTest {
	ConcurrentMetaData meta;
	ConcurrentMetaData meta2;
	private CodingSchemeMetaData csmd1;
	private String codingSchemeUri = "a.uri.here";
	private String codingSchemeVersion = "v1.0";
	private String codingSchemeName = "OneTest";
	private String indexName = "a_uri_here_v1_0";
	private NamedDirectory directory;
	private CodingSchemeMetaData csmd2;
	private String codingSchemeUri1 = "other.uri.here";
	private String codingSchemeVersion1 = "v2.0";
	private String codingSchemeName1 ="TwoTest";
	private String indexName1 = "other_uri_here_v2_0";
	private NamedDirectory directory1;
	
	@Before
	public void setUp() throws Exception {
		meta = ConcurrentMetaData.getInstance();
		meta2 = ConcurrentMetaData.getInstance();
		directory = mock(NamedDirectory.class);
		when(directory.getIndexName()).thenReturn(indexName);
		csmd1 = new CodingSchemeMetaData(
				codingSchemeUri,
				codingSchemeVersion,
				codingSchemeName,
				directory);
		directory1 = mock(NamedDirectory.class);
		when(directory1.getIndexName()).thenReturn(indexName1);
		csmd2 = new CodingSchemeMetaData(
				codingSchemeUri1,
				codingSchemeVersion1,
				codingSchemeName1,
				directory1);

	}

	@Test
	public void testIsSingletion() {
		meta.add(csmd1);
		meta.add(csmd2);
		assertTrue(meta == meta2);
	}
	
	@Test
	public void getMetaDataKeys(){
		assertTrue(meta.getIndexMetaDataKeys().length == 2);
	}
	
	@Test
	public void testIteratorRefresh(){
		Iterator<CodingSchemeMetaData> itr = meta.refreshIterator();
		int counter = 0;
		while(itr.hasNext()){
			meta.remove(csmd1);
			if(++counter == 1){
			assertTrue(itr.next().getCodingSchemeName().equals("OneTest"));
			}
			if(++counter == 2){
				assertTrue(itr.next().getCodingSchemeName().equals("TwoTest"));
			}
			assertTrue(meta.getCodingSchemeList().size() == 1);
		}
	}
	
	@Test
	public void getMetaDataValueUsingKey(){
		assertTrue(meta.getIndexMetaDataValue("TwoTest[:]v2.0").
				equals(meta.getCodingSchemeList().get(0).getDirectory().getIndexName()));
	}
	

	
	@Test
	public void removeMedaDataValueUsingKey(){
		assertTrue(meta.getCodingSchemeList().size() == 1);
		meta.removeIndexMetaDataValue("TwoTest[:]v2.0");
		assertTrue(meta.getCodingSchemeList().size() == 0);
	}
	


}
