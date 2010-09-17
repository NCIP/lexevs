/**
 * 
 */
package org.lexevs.cts2.admin.load;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.Constants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory.SupportedAssociationFactory;

/**
 * @author m004181
 *
 */
public class CodeSystemLoadOperationTest {

	private static LexBIGService lbs_ = LexBIGServiceImpl.defaultInstance();
	private CodeSystemLoadOperation csLoadOp_ = new LexEvsCTS2Impl().getAdminOperation().getCodeSystemLoadOperation();
	
	@BeforeClass
	public static void beforeClass(){
		try {
			removeCS(Constants.VALUE_CODING_SCHEME_URI, Constants.VALUE_CODING_SCHEME_VERSION);
			removeCS("urn:lsid:bioontology.org:fungal_anatomy", "UNASSIGNED");
		} catch (RuntimeException e) {
//			e.printStackTrace();
		} 
	}
	
	@AfterClass
	public static void afterClass() throws LBException{
		removeCS(Constants.VALUE_CODING_SCHEME_URI, Constants.VALUE_CODING_SCHEME_VERSION);
		removeCS("urn:lsid:bioontology.org:fungal_anatomy", "UNASSIGNED");
	}
	
	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.CodeSystemLoadOperation#load(java.net.URI, java.net.URI, java.net.URI, org.lexevs.cts2.BaseService.LoadFormats, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadCSResourceOBO() throws LBException {
		
		csLoadOp_.load(new File(
				"../lbTest/resources/testData/fungal_anatomy.obo").toURI(), null, null, "OBOLoader", true, true, true, "DEV", true);
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
        
        csLoadOp_.load(cs, null, true, false, true, "DEV", true);
        
        CodingScheme loadedCS = lbs_.resolveCodingScheme("miniautomobiles", Constructors.createCodingSchemeVersionOrTag(null, Constants.VALUE_CODING_SCHEME_VERSION));
		
		assertTrue(loadedCS != null);
	}

	@Test
	public void testLoadCSMetaData(){
		try {
			csLoadOp_.applyMetadataToCodeSystem("miniautomobiles", Constructors.createCodingSchemeVersionOrTag("DEV", null), (new File(
					"../lbTest/resources/testData/metadata1.xml").toURI()), true, true, true);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void deactivateCodeSystem(){
		try {
			csLoadOp_.deactivateCodeSystem(Constants.VALUE_CODING_SCHEME_URI, Constants.VALUE_CODING_SCHEME_VERSION);
			
			RegistryEntry re = LexEvsServiceLocator.getInstance().getRegistry().getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(Constants.VALUE_CODING_SCHEME_URI, Constants.VALUE_CODING_SCHEME_VERSION));
			assertTrue(re.getStatus().equals("inactive"));
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void activateCodeSystem(){
		try {
			csLoadOp_.activateCodeSystem(Constants.VALUE_CODING_SCHEME_URI, Constants.VALUE_CODING_SCHEME_VERSION);
			RegistryEntry re = LexEvsServiceLocator.getInstance().getRegistry().getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(Constants.VALUE_CODING_SCHEME_URI, Constants.VALUE_CODING_SCHEME_VERSION));
			assertTrue(re.getStatus().equals("active"));
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void removeCS(String csURI, String csVersion){
		try {
			lbs_.getServiceManager(null).deactivateCodingSchemeVersion(Constructors.createAbsoluteCodingSchemeVersionReference(csURI, csVersion), new Date());
			lbs_.getServiceManager(null).removeCodingSchemeVersion(Constructors.createAbsoluteCodingSchemeVersionReference(csURI, csVersion));
		} catch (LBException e) {
//			e.printStackTrace();
		}
	}
}
