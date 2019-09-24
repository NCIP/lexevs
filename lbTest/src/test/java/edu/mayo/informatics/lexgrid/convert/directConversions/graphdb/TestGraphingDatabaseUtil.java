package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.utility.GraphingDatabaseUtil;


public class TestGraphingDatabaseUtil {
	String[] testSet = new String[]{
			"rdfs:seeAlso",
			"http://purl.org/dc/terms/source", 
			" Captain Kirk of the Enterprise", 
			"_underscoredName", 
			"{do not replace with _"};

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		//Test for exceptions on any
		Arrays.stream(testSet).forEach(x -> System.out.println(GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(x)));
		//known use cases
		assertTrue(Arrays.stream(testSet).anyMatch(x -> GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(x).equals("rdfs_seeAlso")));
		assertTrue(Arrays.stream(testSet).anyMatch(x -> GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(x).equals("http___purl_org_dc_terms_source")));
		assertTrue(Arrays.stream(testSet).anyMatch(x -> GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(x).equals("Captain_Kirk_of_the_Enterprise")));
		assertTrue(Arrays.stream(testSet).anyMatch(x -> GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(x).equals("underscoredName")));
		assertTrue(Arrays.stream(testSet).anyMatch(x -> GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(x).equals("do_not_replace_with__")));

	}

}
