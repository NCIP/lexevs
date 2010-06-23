/**
 * 
 */
package org.lexevs.cts2.admin.export;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.LexEvsCTS2Impl;

/**
 * @author m004181
 *
 */
public class CodeSystemExportOperationImplTest {

	/**
	 * Test method for {@link org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl#exportCodeSystemContent(java.lang.String, java.lang.String, java.net.URI, java.lang.String)}.
	 */
	@Test
	public void testExportCodeSystemContent() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl#exportCodedNodeGraph(java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph, java.net.URI, boolean, boolean, boolean)}.
	 * @throws LBException 
	 * @throws URISyntaxException 
	 */
	@Test
	public void testExportCodedNodeGraph() throws LBException, URISyntaxException {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl#exportCodedNodeSet(java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet, java.net.URI, boolean, boolean, boolean)}.
	 */
	@Test
	public void testExportCodedNodeSet() throws LBException, URISyntaxException {
		LexEvsCTS2 lexevsCTS2 = LexEvsCTS2Impl.defaultInstance();
		CodeSystemExportOperation csExportOp = lexevsCTS2.getAdminOperation().getCodeSystemExportOperation();
		
		// populate coded node set
		CodedNodeSet cns = csExportOp.getCodeSystemCodedNodeSet("urn:lsid:bioontology.org:fungal_anatomy", "UNASSIGNED");
		cns.restrictToMatchingDesignations("hypha", null, MatchAlgorithms.LuceneQuery.name(), null);
		
		URI uri = csExportOp.exportCodedNodeSet("urn:lsid:bioontology.org:fungal_anatomy", "UNASSIGNED", cns, new URI("c:/temp"), true, true, true);
		System.out.println("destination : " + uri.toString());
		
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl#getCodeSystemCodedNodeGraph(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetCodeSystemCodedNodeGraph() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl#getCodeSystemCodedNodeSet(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetCodeSystemCodedNodeSet() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.BaseService#getSupportedExporters()}.
	 */
	@Test
	public void testGetSupportedCodeSystemExporters() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.BaseService#getSupportedExporterNames()}.
	 * @throws LBException 
	 */
	@Test
	public void testGetSupportedCodeSystemExporterNames() throws LBException {
		LexEvsCTS2 lexevsCTS2 = LexEvsCTS2Impl.defaultInstance();
		CodeSystemExportOperation csExportOp = lexevsCTS2.getAdminOperation().getCodeSystemExportOperation();
		
		List<String> exporterNames = csExportOp.getSupportedExporterNames();
		
		for(String exporterName : exporterNames)
		{
			System.out.println("supported exporter : " + exporterName);
		}
	}

}
