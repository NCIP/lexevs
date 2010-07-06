package org.LexGrid.LexBIG.mapping;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;

import junit.framework.TestCase;



public class LexEVSMappingCreationHelperMethodTest extends TestCase {

	LexEVSAuthoringServiceImpl authoring;
	LexBIGService lbs;
	String URI;
	String version;
	CodingSchemeVersionOrTag csvt;
	CodingScheme scheme;
	
	
	@Override
	protected void setUp() throws Exception {
	authoring = new LexEVSAuthoringServiceImpl();
       URI = "urn:oid:11.11.0.1";
       version = "1.0";
       csvt = new CodingSchemeVersionOrTag();
       csvt.setVersion(version);
       lbs = LexBIGServiceImpl.defaultInstance();
       scheme = lbs.resolveCodingScheme(URI, csvt);
	}

	public void testGetCodingSchemes() {
	    CodingSchemeRendering[] rendering = authoring.getCodingSchemes();
	     assertTrue(rendering.length > 0);
	}

	public void testCodingSchemeExists() {
		try {
			assertTrue(authoring.conceptCodeExists("005", URI, version));
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void testGetCodingSchemeNamespace(){
		assertTrue(authoring.getCodingSchemeNamespace(scheme, URI).equals("Automobiles"));
	}
	public void testSupportedAssociationExists() {
		assertTrue(authoring.supportedAssociationExists(scheme, "hasSubtype"));
	}

	public void testSupportedAssociationQualifiersExists() {

	}
	
	public void testAssociationExistance(){
		assertTrue(authoring.doesAssociationExist(scheme, 
                "relations", 
                "uses",
                "Ford",
                "Automobiles",
                "R0001",
                "GermanMadeParts"));
		assertFalse(authoring.doesAssociationExist(scheme, 
                "relations", 
                "uses",
                "Ford",
                "Automobiles",
                "R0001",
                "Automobiles"));
		assertFalse(authoring.doesAssociationExist(scheme, 
                "relations", 
                "uses",
                "Ford",
                "GermanMadeParts",
                "R0001",
                "Automobiles"));
		assertFalse(authoring.doesAssociationExist(scheme, 
                "relations", 
                "uses",
                "005",
                "Automobiles",
                "R0001",
                "GermanMadeParts"));
		assertTrue(authoring.doesAssociationExist(scheme, 
                "relations", 
                "uses",
                "Ford",
                "Automobiles",
                "R0001",
                "GermanMadeParts"));
	}
	
	public void testEntryStateClone(){
		
	}
}
