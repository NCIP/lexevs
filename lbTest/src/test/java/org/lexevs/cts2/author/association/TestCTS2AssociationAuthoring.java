package org.lexevs.cts2.author.association;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.author.AssociationAuthoringOperationImpl;
import org.lexevs.cts2.author.CodeSystemAuthoringOperation;
import org.lexevs.cts2.core.update.RevisionInfo;

public class TestCTS2AssociationAuthoring extends TestCase {

	public static LexEVSAuthoringServiceImpl authoring;
	public static LexBIGService lbs;
	public static LexBIGServiceManager lbsm;
	public static CodingSchemeVersionOrTag csvt;
	public static AssociationAuthoringOperationImpl associationAuthoringOp;
	public static String SOURCE_SCHEME = "GermanMadeParts";
	public static String SOURCE_VERSION = "2.0";
	public static String SOURCE_URN = "urn:oid:11.11.0.2";
	public static String MAPPING_SCHEME = "Mapping";
	public static String MAPPING_VERSION = "1.0";
	public static String MAPPING_URN = "http://default.mapping.container";
	public static String TARGET_SCHEME = "Automobiles";
	public static String TARGET_VERSION = "1.0";
	public static String TARGET_URN = "urn:oid:cts:1.1.1";

	private static List<String> revIds_ = new ArrayList<String>();
	

	public void setUp() {
		associationAuthoringOp = new AssociationAuthoringOperationImpl();
		authoring = new LexEVSAuthoringServiceImpl();
		lbs = LexBIGServiceImpl.defaultInstance();
		csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("1.0");

		try {
			lbsm = lbs.getServiceManager(null);
		} catch (LBException e) {
			e.printStackTrace();
		}
		
	}
//	@BeforeClass
//	public static void loadGMPBeforeClass() {
//		associationAuthoringOp = new AssociationAuthoringOperationImpl();
//		authoring = new LexEVSAuthoringServiceImpl();
//		lbs = LexBIGServiceImpl.defaultInstance();
//		csvt = new CodingSchemeVersionOrTag();
//		csvt.setVersion("1.0");
//
//		try {
//			lbsm = lbs.getServiceManager(null);
//		} catch (LBException e) {
//			e.printStackTrace();
//		}
//		
//		CodeSystemLoadOperation csLoadOp = LexEvsCTS2Impl.defaultInstance().getAdminOperation().getCodeSystemLoadOperation();
//		
//		try {
//			
//			csLoadOp.load(new File("src/test/resources/testData/German_Made_Parts.xml").toURI(), null, null, "LexGrid_Loader", true, true, true, "DEV", true);
//			csLoadOp.activateCodeSystem(TARGET_URN, TARGET_VERSION);
//		} catch (LBException e) {
//			e.printStackTrace();
//		}
//	}
//	@AfterClass
//	public static void runAfterClass() throws LBException, URISyntaxException{
//		
//		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
//		AbsoluteCodingSchemeVersionReference ref = 
//			Constructors.createAbsoluteCodingSchemeVersionReference(Cts2TestConstants.CTS2_AUTOMOBILES_URI, Cts2TestConstants.CTS2_AUTOMOBILES_VERSION);
//		
//		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
//		
//		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
//		
//		ref = 
//			Constructors.createAbsoluteCodingSchemeVersionReference(TARGET_URN, TARGET_VERSION);
//		
//		lbs.getServiceManager(null).deactivateCodingSchemeVersion(ref, null);
//		
//		lbs.getServiceManager(null).removeCodingSchemeVersion(ref);
//		
//		AuthoringService authServ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
//		for (String revId : revIds_)
//		{
//			assertTrue(authServ.removeRevisionRecordbyId(revId));
//		}
//	}
	

	public void testCreateMappingCodingScheme() throws LBException {
		Revision revision = new Revision();

		revision.setChangeAgent("CTS2_Test_agent");
		Text changeInstructions = new Text();
		changeInstructions.setContent("CTS1 Test instructions");
		revision.setChangeInstructions(changeInstructions);

		revision.setEditOrder(new Long(1));
		EntityDescription entityDescription = new EntityDescription();
		entityDescription.setContent("TestCTS2AssociationRevision");
		revision.setEntityDescription(entityDescription);

		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision("TestNewCTSMapping");
		entryState.setRelativeOrder(new Long(1));

		AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		sourceCodeSystemIdentifier.setCodingSchemeURN(SOURCE_URN);
		sourceCodeSystemIdentifier.setCodingSchemeVersion(SOURCE_VERSION);
		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		targetCodeSystemIdentifier.setCodingSchemeURN(TARGET_URN);
		targetCodeSystemIdentifier.setCodingSchemeVersion(TARGET_VERSION);
		String sourceConceptCodeIdentifier = "E0001";
		String targetConceptCodeIdentifier = "Ford";
		String relationsContainerName = "relations";
		String associationType = "SY";
		associationAuthoringOp.createAssociation(true, revision, entryState,
				null, sourceCodeSystemIdentifier, targetCodeSystemIdentifier,
				sourceConceptCodeIdentifier, targetConceptCodeIdentifier,
				relationsContainerName, associationType, null);
		LexBIGServiceManager lbsm = lbs.getServiceManager(null);
		AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
		codingSchemeVersion.setCodingSchemeURN(MAPPING_URN);
		codingSchemeVersion.setCodingSchemeVersion(MAPPING_VERSION);
		lbsm.activateCodingSchemeVersion(codingSchemeVersion);
	}


	public void testCreateMappingForExistingScheme() throws LBException {
		Revision revision = new Revision();

		revision.setChangeAgent("CTS2_Test_agent");
		Text changeInstructions = new Text();
		changeInstructions.setContent("CTS1 Test instructions");
		revision.setChangeInstructions(changeInstructions);

		revision.setEditOrder(new Long(1));
		EntityDescription entityDescription = new EntityDescription();
		entityDescription.setContent("TestCTS2AssociationRevision");
		revision.setEntityDescription(entityDescription);

		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision("TestNewForExistingCTSMapping");
		entryState.setRelativeOrder(new Long(1));

		AbsoluteCodingSchemeVersionReference mappingCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		mappingCodeSystemIdentifier.setCodingSchemeURN(MAPPING_URN);
		mappingCodeSystemIdentifier.setCodingSchemeVersion(MAPPING_VERSION);
		AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		sourceCodeSystemIdentifier.setCodingSchemeURN(SOURCE_URN);
		sourceCodeSystemIdentifier.setCodingSchemeVersion(SOURCE_VERSION);
		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		targetCodeSystemIdentifier.setCodingSchemeURN(TARGET_URN);
		targetCodeSystemIdentifier.setCodingSchemeVersion(TARGET_VERSION);
		String sourceConceptCodeIdentifier = "P0001";
		String targetConceptCodeIdentifier = "005";
		String relationsContainerName = "Mapping_relations";
		String associationType = "SY";
		associationAuthoringOp.createAssociation(false, revision, entryState,
				mappingCodeSystemIdentifier, sourceCodeSystemIdentifier,
				targetCodeSystemIdentifier, sourceConceptCodeIdentifier,
				targetConceptCodeIdentifier, relationsContainerName,
				associationType, null);
	}


	public void testFailCreateNewAssociation() {
		Revision revision = new Revision();

		revision.setChangeAgent("CTS2_Test_agent");
		Text changeInstructions = new Text();
		changeInstructions.setContent("CTS1 Test instructions");
		revision.setChangeInstructions(changeInstructions);

		revision.setEditOrder(new Long(1));
		EntityDescription entityDescription = new EntityDescription();
		entityDescription.setContent("TestCTS2AssociationRevision");
		revision.setEntityDescription(entityDescription);

		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision("TestNewForExistingScheme");
		entryState.setRelativeOrder(new Long(1));

		AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		sourceCodeSystemIdentifier.setCodingSchemeURN(TARGET_URN);
		sourceCodeSystemIdentifier.setCodingSchemeVersion(TARGET_VERSION);
		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		targetCodeSystemIdentifier.setCodingSchemeURN(TARGET_URN);
		targetCodeSystemIdentifier.setCodingSchemeVersion(TARGET_VERSION);
		String sourceConceptCodeIdentifier = "005";
		String targetConceptCodeIdentifier = "GM";
		String relationsContainerName = "relations";
		String associationType = "hasSubtype";
		try {
			associationAuthoringOp.createAssociation(false, revision,
					entryState, null, sourceCodeSystemIdentifier,
					targetCodeSystemIdentifier, sourceConceptCodeIdentifier,
					targetConceptCodeIdentifier, relationsContainerName,
					associationType, null);
			fail("This association already exists and the method should throw an exception");
		} catch (LBException e) {
			assertTrue(true);
		}

	}


	public void testCreateNewAssociation() throws LBException {
		Revision revision = new Revision();

		revision.setChangeAgent("CTS2_Test_agent");
		Text changeInstructions = new Text();
		changeInstructions.setContent("CTS1 Test instructions");
		revision.setChangeInstructions(changeInstructions);

		revision.setEditOrder(new Long(1));
		EntityDescription entityDescription = new EntityDescription();
		entityDescription.setContent("TestCTS2AssociationRevision");
		revision.setEntityDescription(entityDescription);

		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision("TestNewForExistingScheme");
		entryState.setRelativeOrder(new Long(1));

		AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		sourceCodeSystemIdentifier.setCodingSchemeURN(TARGET_URN);
		sourceCodeSystemIdentifier.setCodingSchemeVersion(TARGET_VERSION);
		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		targetCodeSystemIdentifier.setCodingSchemeURN(TARGET_URN);
		targetCodeSystemIdentifier.setCodingSchemeVersion(TARGET_VERSION);
		String sourceConceptCodeIdentifier = "005";
		String targetConceptCodeIdentifier = "73";
		String relationsContainerName = "relations";
		String associationType = "uses";

		associationAuthoringOp.createAssociation(false, revision, entryState,
				null, sourceCodeSystemIdentifier, targetCodeSystemIdentifier,
				sourceConceptCodeIdentifier, targetConceptCodeIdentifier,
				relationsContainerName, associationType, null);
	}


	public void testUpdateAssociationState() throws LBException {
		Revision revision = new Revision();

		revision.setChangeAgent("CTS2_Test_agent");
		Text changeInstructions = new Text();
		changeInstructions.setContent("CTS1 Test instructions");
		revision.setChangeInstructions(changeInstructions);

		revision.setEditOrder(new Long(1));
		EntityDescription entityDescription = new EntityDescription();
		entityDescription.setContent("TestCTS2AssociationRevision");
		revision.setEntityDescription(entityDescription);

		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.MODIFY);
		entryState.setContainingRevision("TestModifyForUpdatePredicate");
		entryState.setRelativeOrder(new Long(1));

		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN(TARGET_URN);
		scheme.setCodingSchemeVersion(TARGET_VERSION);
		String sourceCode = "005";
		String targetCode = "A";
		String relationsContainerName = "relations";
		String associationName = "hasSubtype";
		String namespace = "Automobiles";
		associationAuthoringOp.updateAssociationStatus(revision, entryState,
				scheme, relationsContainerName, associationName, sourceCode,
				namespace, targetCode, namespace, "instance001", "PENDING",
				false);
	}

//	private static String getRevId(){
//		String revId = UUID.randomUUID().toString();
//		revIds_.add(revId);
//		
//		return revId;
//	}
	

//	public void testRemoveCodeSystem()   throws LBException, URISyntaxException{	
//		
//		String randomID = getRevId();
//		
//		RevisionInfo revInfo = new RevisionInfo();
//		revInfo.setChangeAgent("changeAgent");
//		revInfo.setChangeInstruction("changeInstruction");
//		revInfo.setDescription("description");
//		revInfo.setEditOrder(1L);
//		revInfo.setRevisionDate(new Date());
//		revInfo.setRevisionId(randomID);
//		
//	    
//	    String codingSchemeURI = MAPPING_URN;
//		String representsVersion = MAPPING_VERSION;
//		
//		Boolean removeStatus = false;
//	    
//	    CodeSystemAuthoringOperation codeSystemAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
//	
//	    try {
//			removeStatus = codeSystemAuthOp.removeCodeSystem(revInfo, codingSchemeURI, representsVersion);
//		} catch (LBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	
//		assertTrue(removeStatus);		
//	}	
}
