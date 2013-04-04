package org.lexevs.graph.load.orientdb.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.graph.load.connect.OrientDbGraphDbConnect;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

public class OreintDBGraphTestIT {

	OrientDbGraphDbConnect  graphdb;
	Properties p;
	
	@Before
	public void setUp() throws Exception {
		 p = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("test.properties");
		p.load(inputStream);
		graphdb = new OrientDbGraphDbConnect(
				p.getProperty("user"), 
				p.getProperty("password"), 
				p.getProperty("path"));
		
	}

	@After
	public void tearDown() throws Exception {
		graphdb.delete(p.getProperty("path"));
	}

	@Test
	public void testGetSource() {
		
	assertNotNull(graphdb.getSource());
	}

	@Test
	public void testGetTarget() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEdge() {
		fail("Not yet implemented");
	}

	@Test
	public void testOrientDbGraphDbConnect() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitVerticesAndEdge() {
		fail("Not yet implemented");
	}

	@Test
	public void testOpenForWrite() {
		fail("Not yet implemented");
	}

	@Test
	public void testOpenForRead() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGraphDbFromPool() {
		fail("Not yet implemented");
	}

	@Test
	public void testCloseOGraphDatabase() {
		fail("Not yet implemented");
	}

	@Test
	public void testClose() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateVertex() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateEdge() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateTriple() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateVertexTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateEdgeTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testStoreTriple() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateTriplesPlus() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFieldNamesForVertex() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFieldNamesForEdge() {
		fail("Not yet implemented");
	}

	@Test
	public void testStoreVertex() {
		fail("Not yet implemented");
	}

}
