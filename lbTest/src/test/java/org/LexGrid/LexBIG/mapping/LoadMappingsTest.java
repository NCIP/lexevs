
package org.LexGrid.LexBIG.mapping;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

import junit.framework.TestCase;

public class LoadMappingsTest extends TestCase {
	LexEVSAuthoringServiceImpl authoring;
	LexBIGService lbs;
	LexBIGServiceManager lbsm;

	public static String SOURCE_SCHEME = "GermanMadeParts";
	public static String SOURCE_VERSION = "2.0";
	public static String MAPPING_SCHEME = "http://default.mapping.container";
	public static String MAPPING_VERSION = "1.0";
	public static String TARGET_SCHEME = "Automobiles";
	public static String TARGET_VERSION = "1.0";

	public void setUp() {

		authoring = new LexEVSAuthoringServiceImpl();
		lbs = LexBIGServiceImpl.defaultInstance();

		try {
			lbsm = lbs.getServiceManager(null);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testMappingLoad() throws LBException {
		AssociationSource source = new AssociationSource();
		AssociationSource source1 = new AssociationSource();
		AssociationSource source2 = new AssociationSource();
		source.setSourceEntityCode("T0001");
		source.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode("005");
		target.setTargetEntityCodeNamespace("Automobiles");
		source.addTarget(target);

		source1.setSourceEntityCode("P0001");
		source1.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		AssociationTarget target1 = new AssociationTarget();
		target1.setTargetEntityCode("A0001");
		target1.setTargetEntityCodeNamespace("Automobiles");
		source1.addTarget(target1);

		source2.setSourceEntityCode("P0001");
		source2.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		AssociationTarget target2 = new AssociationTarget();
		target2.setTargetEntityCode("005");
		target2.setTargetEntityCodeNamespace("Automobiles");
		source2.addTarget(target2);
		AssociationSource[] sources = new AssociationSource[] { source,
				source1, source2 };

		try {
			loadMappings(sources);
		} catch (ClassNotFoundException e) {
			// do nothing
		}
	}

	public void testLoadMappings() throws LBException {
		// LocalNameList localNameList =
		// ConvenienceMethods.createLocalNameList(new String[]{"name1", "name2",
		// "name3"});
		List<String> localNameList = Arrays.asList(new String[] { "name1",
				"name2", "name3" });
		Source source = new Source();
		source.setContent("Source_Vocabulary");
		List<Source> sourceList = Arrays.asList();
		Text copyright = new Text();
		copyright.setContent("Mayo copyright");
		CodingScheme mappingSchemeMetadata = authoring.populateCodingScheme(
				"Mapping_Test", "Tested_URI", "Formal_Mapping_Name", "EN", 5L,
				"0.0", localNameList, sourceList, copyright, new Mappings(),
				null, null, null);

		AssociationTarget target1 = createTargetWithValuesPopulated();
		AssociationTarget target2 = createTarget("Ford", "Automobiles");
		AssociationTarget target3 = createTarget("73", "Automobiles");
		AssociationTarget[] targets = new AssociationTarget[] { target1,
				target2, target3 };
		AssociationSource associationSource = new AssociationSource();
		associationSource.setSourceEntityCode("R0001");
		associationSource
				.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		associationSource.setTarget(targets);
		AssociationSource[] sourcesAndTargets = new AssociationSource[] { associationSource };
		String sourceCodingScheme = SOURCE_SCHEME;
		String sourceCodingSchemeVersion = SOURCE_VERSION;
		String targetCodingScheme = TARGET_SCHEME;
		String targetCodingSchemeVersion = TARGET_VERSION;
		String associationName = "SY";
		String relationsContainerName = "GermanMadeParts_to_Automobiles_Mappings";
		String revisionId = "Non-Default_NEW_Mapping";
		authoring.createMappingScheme(mappingSchemeMetadata, sourcesAndTargets,
				sourceCodingScheme, sourceCodingSchemeVersion,
				targetCodingScheme, targetCodingSchemeVersion, associationName,
				relationsContainerName, revisionId, false);
		AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
		codingSchemeVersion.setCodingSchemeURN(mappingSchemeMetadata.getCodingSchemeURI());
		codingSchemeVersion.setCodingSchemeVersion(mappingSchemeMetadata.getRepresentsVersion());
		lbsm.activateCodingSchemeVersion(codingSchemeVersion);
	}
	
	private AssociationTarget createTargetWithValuesPopulated()
			throws LBException {
		Versionable versionableData = new Versionable();
		versionableData.setEffectiveDate(new Date());
		versionableData.setExpirationDate(new Date());
		versionableData.setIsActive(Boolean.TRUE);
		versionableData.setOwner("Mayo");
		versionableData.setStatus("ACTIVE");
		String instanceId = "instance_001";

		List<String> usageContextList = null;
		AssociationQualification qualifier = new AssociationQualification();
		qualifier.setAssociationQualifier("qualifier");
		Text text = new Text();
		text.setContent("qualifies SY");
		qualifier.setQualifierText(text);
		List<AssociationQualification> associationQualifiers = Arrays
				.asList(qualifier);
		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		targetCodeSystemIdentifier.setCodingSchemeURN("urn:oid:11.11.0.1");
		targetCodeSystemIdentifier.setCodingSchemeVersion(TARGET_VERSION);
		String targetConceptCodeIdentifier = "005";
		return authoring.createAssociationTarget(null, versionableData,
				instanceId, Boolean.FALSE, Boolean.TRUE, usageContextList,
				associationQualifiers, targetCodeSystemIdentifier,
				targetConceptCodeIdentifier);
	}
	
	private AssociationTarget createTarget(String code, String namespace) {
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode(code);
		target.setTargetEntityCodeNamespace(namespace);
		return target;
	}

	public void loadMappings(AssociationSource[] sources)
			throws ClassNotFoundException, LBException {
		authoring.createMappingWithDefaultValues(sources, "GermanMadeParts",
				"2.0", "Automobiles", "1.0", "SY", false);
		AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
		codingSchemeVersion
				.setCodingSchemeURN("http://default.mapping.container");
		codingSchemeVersion.setCodingSchemeVersion("1.0");
		lbsm.activateCodingSchemeVersion(codingSchemeVersion);
	}

}