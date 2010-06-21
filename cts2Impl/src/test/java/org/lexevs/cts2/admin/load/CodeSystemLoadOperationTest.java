/**
 * 
 */
package org.lexevs.cts2.admin.load;

import static org.junit.Assert.fail;

import java.io.File;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.LexEvsCTS2Impl;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.Constants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory.SupportedAssociationFactory;

/**
 * @author m004181
 *
 */
public class CodeSystemLoadOperationTest {

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.CodeSystemLoadOperation#load(java.net.URI, java.net.URI, java.net.URI, org.lexevs.cts2.BaseService.LoadFormats, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadCSResourceOBO() throws LBException {
		LexEvsCTS2 cts2 = new LexEvsCTS2Impl();
		CodeSystemLoadOperation csLoad = cts2.getAdminOperation().getCodeSystemLoadOperation();
		csLoad.load(new File(
				"../lbTest/resources/testData/fungal_anatomy.obo").toURI(), null, null, null, "OBOLoader", true, true, true, "DEV", true);
	}
	
	@Test
	public void testLoadCSResourceXML() throws LBException {
		LexEvsCTS2 cts2 = new LexEvsCTS2Impl();
		CodeSystemLoadOperation csLoad = cts2.getAdminOperation().getCodeSystemLoadOperation();
		csLoad.load(new File(
				"../lbTest/resources/testData/ValueDomain/Automobiles.xml").toURI(), null, null, null, "LexGrid_Loader", true, true, true, "DEV", true);
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.CodeSystemLoadOperation#load(org.LexGrid.codingSchemes.CodingScheme, java.net.URI, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadCSObject() throws LBException {
		CodingScheme cs = new CodingScheme();
		cs.setApproxNumConcepts(new Long(4));
        cs.setCodingSchemeName("miniautomobiles");
        cs.setFormalName("miniautomobiles");
        cs.setCodingSchemeURI(Constants.VALUE_CODING_SCHEME_URI);
        Text copyright = new Text();
        copyright.setContent("Copyright Mayo Clinic.");
        cs.setCopyright(copyright);
        cs.setDefaultLanguage(Constants.VALUE_LANG_EN);
        cs.setMappings(new Mappings());
        cs.setRepresentsVersion(Constants.VALUE_CODING_SCHEME_VERSION);
        
        SupportedAssociation saHasSubType = SupportedAssociationFactory.createSupportedAssociationHasSubType();
        cs.getMappings().addSupportedAssociation(saHasSubType);
        SupportedAssociation saUses = SupportedAssociationFactory.createSupportedAssociationUses();
        cs.getMappings().addSupportedAssociation(saUses);
        
        Relations rels = new Relations();
        rels.setContainerName("asD");
        
        AssociationPredicate ap = new AssociationPredicate();
        ap.setAssociationName("hasSubtype");
        
        rels.addAssociationPredicate(ap);
        
        ap = new AssociationPredicate();
        ap.setAssociationName("uses");
        
        rels.addAssociationPredicate(ap);
        
        cs.addRelations(rels);
        
        CodeSystemLoadOperation csLoadOp = getLexEvsCTS2().getAdminOperation().getCodeSystemLoadOperation();
        csLoadOp.load(cs, null, null, true, true, true, "DEV", true);
	}

	@Test
	public void testLoadCSMetaData() throws LBException {
		LexEvsCTS2 cts2 = new LexEvsCTS2Impl();
		CodeSystemLoadOperation csLoad = cts2.getAdminOperation().getCodeSystemLoadOperation();
		csLoad.applyMetadataToCodeSystem("miniautomobiles", Constructors.createCodingSchemeVersionOrTag("DEV", null), (new File(
				"../lbTest/resources/testData/metadata1.xml").toURI()), true, true, true);
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystem()}.
	 */
	@Test
	public void testImportCodeSystem() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystemRevsion(org.LexGrid.versions.Revision)}.
	 */
	@Test
	public void testImportCodeSystemRevsionRevision() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystemRevsion(java.lang.String)}.
	 */
	@Test
	public void testImportCodeSystemRevsionString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.CodeSystemLoadOperation#changeCodeSystemStatus()}.
	 */
	@Test
	public void testChangeCodeSystemStatus() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.CodeSystemLoadOperation#validate(java.net.URI, java.net.URI, int)}.
	 */
	@Test
	public void testValidate() {
		fail("Not yet implemented"); // TODO
	}
	
	private LexEvsCTS2 getLexEvsCTS2(){
		return LexEvsCTS2Impl.defaultInstance();
	}

}
