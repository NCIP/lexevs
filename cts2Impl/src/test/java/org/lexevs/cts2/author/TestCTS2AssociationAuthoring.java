package org.lexevs.cts2.author;

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
import org.lexevs.cts2.author.AssociationAuthoringOperationImpl;

import junit.framework.TestCase;


public class TestCTS2AssociationAuthoring extends TestCase {
	
    LexEVSAuthoringServiceImpl authoring;
    LexBIGService lbs;
	LexBIGServiceManager lbsm;
	CodingSchemeVersionOrTag csvt;
	AssociationAuthoringOperationImpl associationAuthoringOp;
	public static String SOURCE_SCHEME = "GermanMadeParts";
	public static String SOURCE_VERSION = "2.0";
	public static String SOURCE_URN ="urn:oid:11.11.0.2";
	public static String MAPPING_SCHEME = "Mapping";
	public static String MAPPING_VERSION = "1.0";
	public static String MAPPING_URN = "http://default.mapping.container";
	public static String TARGET_SCHEME =  "Automobiles";
	public static String TARGET_VERSION = "1.0";
	public static String TARGET_URN = "urn:oid:11.11.0.1";
	
public void setUp(){
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
public void testCreateMappingCodingScheme() throws LBException{
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
	associationAuthoringOp.createAssociation(
			true,
			revision, 
			entryState, 
			null, 
			sourceCodeSystemIdentifier, 
			targetCodeSystemIdentifier, 
			sourceConceptCodeIdentifier, 
			targetConceptCodeIdentifier, 
			relationsContainerName, 
			associationType, 
			null);
	LexBIGServiceManager lbsm = lbs.getServiceManager(null);
	AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
	codingSchemeVersion.setCodingSchemeURN(MAPPING_URN);
	codingSchemeVersion.setCodingSchemeVersion(MAPPING_VERSION);
	lbsm.activateCodingSchemeVersion(codingSchemeVersion);
}

public void testCreateMappingForExistingScheme() throws LBException{
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
	associationAuthoringOp.createAssociation(
			false,
			revision, 
			entryState, 
			mappingCodeSystemIdentifier, 
			sourceCodeSystemIdentifier, 
			targetCodeSystemIdentifier, 
			sourceConceptCodeIdentifier, 
			targetConceptCodeIdentifier, 
			relationsContainerName, 
			associationType, 
			null);
}
public void testFailCreateNewAssociation(){
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
	try{
	associationAuthoringOp.createAssociation(
			false,
			revision, 
			entryState, 
			null, 
			sourceCodeSystemIdentifier, 
			targetCodeSystemIdentifier, 
			sourceConceptCodeIdentifier, 
			targetConceptCodeIdentifier, 
			relationsContainerName, 
			associationType, 
			null);
	fail("This association already exists and the method should throw an exception");
	}
	catch(LBException e){
		assertTrue(true);
	}

}
public void testCreateNewAssociation() throws LBException{
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

	associationAuthoringOp.createAssociation(
			false,
			revision, 
			entryState, 
			null, 
			sourceCodeSystemIdentifier, 
			targetCodeSystemIdentifier, 
			sourceConceptCodeIdentifier, 
			targetConceptCodeIdentifier, 
			relationsContainerName, 
			associationType, 
			null);
}
public void testUpdateAssociationState(){
//associationAuthoringOp.updateAssociationStatus(revision, entryState, scheme, 
//		relationsContainer, associationName, sourceCode, sourceNamespace, 
//		targetCode, targetNamespace, instanceId, status, isActive);
}

//public void loadMappings(AssociationSource[] sources) throws ClassNotFoundException, LBException{
//	   authoring.createMappingWithDefaultValues(sources, "GermanMadeParts", "2.0", "Automobiles", "1.0", "SY");
//		AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
//		codingSchemeVersion.setCodingSchemeURN("http://default.mapping.container");
//		codingSchemeVersion.setCodingSchemeVersion("1.0");
//	   lbsm.activateCodingSchemeVersion(codingSchemeVersion);
//}
}
