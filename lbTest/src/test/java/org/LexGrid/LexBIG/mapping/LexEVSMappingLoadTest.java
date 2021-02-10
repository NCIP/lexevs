
package org.LexGrid.LexBIG.mapping;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;

import junit.framework.TestCase;

public class LexEVSMappingLoadTest extends TestCase {

	LexEVSAuthoringServiceImpl authoring;
	LexBIGService lbs;
	LexBIGServiceManager lbsm;
	CodingSchemeVersionOrTag csvt;
	MappingTestUtility utility;

	public void setUp() {

		authoring = new LexEVSAuthoringServiceImpl();
		lbs = LexBIGServiceImpl.defaultInstance();
		csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("1.0");
		utility = new MappingTestUtility();
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
		authoring.createMappingWithDefaultValues(sources, "GermanMadeParts",
				"2.0", "Automobiles", "1.0", "SY", false);
		AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
		codingSchemeVersion
				.setCodingSchemeURN("http://default.mapping.container");
		codingSchemeVersion.setCodingSchemeVersion("1.0");
		lbsm.activateCodingSchemeVersion(codingSchemeVersion);
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

		AssociationTarget target1 = utility.createTargetWithValuesPopulated();
		AssociationTarget target2 = utility.createTarget("Ford", "Automobiles");
		AssociationTarget target3 = utility.createTarget("73", "Automobiles");
		AssociationTarget[] targets = new AssociationTarget[] { target1,
				target2, target3 };
		AssociationSource associationSource = new AssociationSource();
		associationSource.setSourceEntityCode("R0001");
		associationSource
				.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		associationSource.setTarget(targets);
		AssociationSource[] sourcesAndTargets = new AssociationSource[] { associationSource };
		String sourceCodingScheme = MappingTestConstants.SOURCE_SCHEME;
		String sourceCodingSchemeVersion = MappingTestConstants.SOURCE_VERSION;
		String targetCodingScheme = MappingTestConstants.TARGET_SCHEME;
		String targetCodingSchemeVersion = MappingTestConstants.TARGET_VERSION;
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