package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class TestEqualsCodingSchemeReference {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEqual() {
		CodingSchemeReference ref1 = new CodingSchemeReference();
		ref1.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag = Constructors.createCodingSchemeVersionOrTag(null, "1.0");
		ref1.setVersionOrTag(versionOrTag);
		CodingSchemeReference ref2 = new CodingSchemeReference();
		ref2.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag2 = Constructors.createCodingSchemeVersionOrTag(null, "1.0");
		ref2.setVersionOrTag(versionOrTag2);
		assertTrue(ref1.equals(ref2));
		CodingSchemeReference ref3 = new CodingSchemeReference();
		ref3.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag3 = Constructors.createCodingSchemeVersionOrTag("Tag", null);
		ref3.setVersionOrTag(versionOrTag3);
		CodingSchemeReference ref4 = new CodingSchemeReference();
		ref4.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag4 = Constructors.createCodingSchemeVersionOrTag("Tag", null);
		ref4.setVersionOrTag(versionOrTag4);
		assertTrue(ref3.equals(ref4));
	}
	
	@Test
	public void testNotEqual() {
		CodingSchemeReference ref1 = new CodingSchemeReference();
		ref1.setCodingScheme("Scheme2");
		CodingSchemeVersionOrTag versionOrTag = Constructors.createCodingSchemeVersionOrTag(null, "1.0");
		ref1.setVersionOrTag(versionOrTag);
		CodingSchemeReference ref2 = new CodingSchemeReference();
		ref2.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag2 = Constructors.createCodingSchemeVersionOrTag(null, "1.0");
		ref2.setVersionOrTag(versionOrTag2);
		assertFalse(ref1.equals(ref2));
		CodingSchemeReference ref4 = new CodingSchemeReference();
		ref4.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag4 = Constructors.createCodingSchemeVersionOrTag("Tag", null);
		ref4.setVersionOrTag(versionOrTag4);
		assertFalse(ref2.equals(ref4));
		CodingSchemeReference ref5 = new CodingSchemeReference();
		ref5.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag5 = Constructors.createCodingSchemeVersionOrTag(null, "2.0");
		ref5.setVersionOrTag(versionOrTag5);
		assertFalse(ref2.equals(ref5));
		CodingSchemeReference ref6 = new CodingSchemeReference();
		ref6.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag6= Constructors.createCodingSchemeVersionOrTag("Tag1", null);
		ref6.setVersionOrTag(versionOrTag6);
		assertFalse(ref4.equals(ref6));
		assertFalse(ref5.equals(ref6));
	}
	
	@Test
	public void testCollectionsCorrectness() {
		CodingSchemeReference ref1 = new CodingSchemeReference();
		ref1.setCodingScheme("Scheme1");
		CodingSchemeVersionOrTag versionOrTag = Constructors.createCodingSchemeVersionOrTag(null, "1.0");
		ref1.setVersionOrTag(versionOrTag);
		CodingSchemeReference ref2 = new CodingSchemeReference();
		ref2.setCodingScheme("Scheme2");
		CodingSchemeVersionOrTag versionOrTag2 = Constructors.createCodingSchemeVersionOrTag(null, "2.0");
		ref2.setVersionOrTag(versionOrTag2);
		List<CodingSchemeReference> list = new ArrayList<CodingSchemeReference>();
		list.add(ref1);
		list.add(ref2);
		assertTrue(list.size() == 2);
		list.remove(ref2);
		assertTrue(list.size() == 1);
		list.add(ref2);
		List<CodingSchemeReference> list2 = new ArrayList<CodingSchemeReference>();
		list2.add(ref2);
		list.removeAll(list2);
		assertTrue(list.size() == 1);
		Set<CodingSchemeReference> set = new HashSet<CodingSchemeReference>();
		set.add(ref1);
		set.add(ref2);
		Set<CodingSchemeReference> set2 = new HashSet<CodingSchemeReference>();
		set2.add(ref2);
		set.removeAll(set2);
		assertTrue(set.size() == 1);
		
	}

}
