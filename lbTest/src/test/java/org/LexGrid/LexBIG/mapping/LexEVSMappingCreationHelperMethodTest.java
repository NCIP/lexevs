package org.LexGrid.LexBIG.mapping;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;

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
                "hasSubtype",
                "005",
                "Automobiles",
                "GM",
                "Automobiles"));
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
                "GermanMadePartsNamespace",
                "R0001",
                "Automobiles"));
		assertFalse(authoring.doesAssociationExist(scheme, 
                "relations", 
                "uses",
                "005",
                "Automobiles",
                "R0001",
                "GermanMadePartsNamespace"));
		assertTrue(authoring.doesAssociationExist(scheme, 
                "relations", 
                "uses",
                "Ford",
                "Automobiles",
                "R0001",
                "GermanMadePartsNamespace"));
	}
	
	public void testEntryStateClone(){
		
	}
	
	public void testCreateAssociationTarget() throws LBException{
		
		EntryState entryState = new EntryState();
		entryState = authoring.createDefaultMappingsEntryState(null,null);
		Versionable versionableData = new Versionable();
		versionableData.setEffectiveDate(new Date());
		versionableData.setExpirationDate(new Date());
		versionableData.setIsActive(Boolean.TRUE);
		versionableData.setOwner("Mayo_owner_test");
		versionableData.setStatus("ACTIVE");
		String instanceId = "instance_001";
		Boolean isInferred = Boolean.valueOf(false);
		Boolean isDefined = Boolean.valueOf(false);
		List<String> usageContextList = Arrays.asList("usage_context_test");
		AssociationQualification qualifier = new AssociationQualification();
		qualifier.setAssociationQualifier("Qualifier_name");
		Text text = new Text();
		text.setContent("Qualifier_Value");
		qualifier.setQualifierText(text);
		List<AssociationQualification> associationQualifiers = Arrays.asList(qualifier);
		
 		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
 		targetCodeSystemIdentifier.setCodingSchemeURN(URI);
 		targetCodeSystemIdentifier.setCodingSchemeVersion(version);
		String targetConceptCodeIdentifier = "Ford";
		AssociationTarget targetAssociation = authoring.createAssociationTarget(entryState, 
				versionableData, 
				instanceId, 
				isInferred, 
				isDefined, 
				usageContextList, 
				associationQualifiers, 
				targetCodeSystemIdentifier, 
				targetConceptCodeIdentifier);
		
		assertTrue(targetAssociation.getAssociationQualification(0).getQualifierText().getContent().equals("Qualifier_Value"));
		assertTrue(targetAssociation.getAssociationInstanceId().equals(instanceId));
		assertNotNull(targetAssociation.getEffectiveDate());
		assertNotNull(targetAssociation.getExpirationDate());
		assertTrue(targetAssociation.getIsActive().booleanValue());
		assertFalse(targetAssociation.getIsDefining());
		assertFalse(targetAssociation.getIsInferred());
		assertTrue(targetAssociation.getOwner().equals("Mayo_owner_test"));
		assertTrue(targetAssociation.getStatus().equals("ACTIVE"));
		assertTrue(targetAssociation.getAssociationInstanceId().equals("instance_001"));
		assertTrue(targetAssociation.getUsageContext(0).equals("usage_context_test"));
		assertTrue(targetAssociation.getTargetEntityCode().equals("Ford"));
		assertTrue(targetAssociation.getTargetEntityCodeNamespace().equals("Automobiles"));

	}
	
	public void testCreateAssociationTargetWithNulls() throws LBException{
		
		EntryState entryState = new EntryState();
		entryState = authoring.createDefaultMappingsEntryState(null,null);

 		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
 		targetCodeSystemIdentifier.setCodingSchemeURN(URI);
 		targetCodeSystemIdentifier.setCodingSchemeVersion(version);
		String targetConceptCodeIdentifier = "Ford";
		AssociationTarget targetAssociation = authoring.createAssociationTarget(entryState, 
				null, 
				null, 
				null, 
				null, 
				null, 
				null, 
				targetCodeSystemIdentifier, 
				targetConceptCodeIdentifier);
		
		}
	
	public void testCreateDefaultMappingsEntryState(){
		
		String revisionId = "StateRevision_7-21-2010";
		String prevRevisionId = "Previous_state_Revision_7-20-2010";
		EntryState entryState = authoring.createDefaultMappingsEntryState(
				revisionId, prevRevisionId);
		assertTrue(entryState.getContainingRevision() != null);
		assertTrue(entryState.getContainingRevision().equals(revisionId));
		assertTrue(entryState.getPrevRevision().equals(prevRevisionId));
		assertTrue(entryState.getChangeType() == LexEVSAuthoringServiceImpl.CHANGE_TYPE);
		assertTrue(entryState.getRelativeOrder() == LexEVSAuthoringServiceImpl.RELATIVE_ORDER);

		EntryState entryState2 = authoring.createDefaultMappingsEntryState(
				null, null);
		assertNotNull(entryState2.getContainingRevision());
		assertTrue(entryState2.getContainingRevision().equals(
				LexEVSAuthoringServiceImpl.REVISIONID));
	 
	}
	public void testCreateRelationsContainer() throws LBException{
		EntryState entryState = authoring.createDefaultMappingsEntryState(null, null);
		String containerName = "relations_test";
		Date effectiveDate = new Date();
		AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		sourceCodeSystemIdentifier.setCodingSchemeURN(MappingTestConstants.SOURCE_URN);
		sourceCodeSystemIdentifier.setCodingSchemeVersion(MappingTestConstants.SOURCE_VERSION);
		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		targetCodeSystemIdentifier.setCodingSchemeURN(MappingTestConstants.TARGET_URN);
		targetCodeSystemIdentifier.setCodingSchemeVersion(MappingTestConstants.TARGET_VERSION);
		boolean isMapping = true;
		String associationType = "type_of";
		Properties relationProperties = null;

		CodingScheme codingScheme = authoring.populateCodingScheme(
				"TestRelationsScheme", 
				"http://test.relations.scheme", 
				null, 
				null, 
				0L, 
				"test.relations.1", 
				null, 
				null, 
				null, 
				new Mappings(), 
				null, 
				null, 
				null);
		Relations relation = authoring.createRelationsContainer(entryState, 
				codingScheme, 
				containerName, 
				effectiveDate, 
				sourceCodeSystemIdentifier, 
				targetCodeSystemIdentifier, 
				isMapping, 
				associationType, 
				relationProperties);
		
		assertTrue(relation.getSourceCodingScheme().equals(MappingTestConstants.SOURCE_SCHEME));
		assertTrue(relation.getSourceCodingSchemeVersion().equals(MappingTestConstants.SOURCE_VERSION));
		assertTrue(relation.getTargetCodingScheme().equals(MappingTestConstants.TARGET_SCHEME));
		assertTrue(relation.getTargetCodingSchemeVersion().equals(MappingTestConstants.TARGET_VERSION));
		assertTrue(relation.getAssociationPredicate(0).getAssociationName().equals(associationType));
	}
	
    public void testGetCodingSchemeNameForNamespace(){
    	
    }

}
