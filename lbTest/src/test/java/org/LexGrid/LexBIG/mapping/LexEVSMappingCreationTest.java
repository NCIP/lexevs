package org.LexGrid.LexBIG.mapping;

import java.util.Arrays;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;

import junit.framework.TestCase;

public class LexEVSMappingCreationTest extends TestCase {
	
	   LexEVSAuthoringServiceImpl authoring;
	   LexBIGService lbs;
		LexBIGServiceManager lbsm;
		public static String SOURCE_SCHEME = "GermanMadeParts";
		public static String SOURCE_VERSION = "2.0";
		public static String MAPPING_SCHEME = "http://default.mapping.container";
		public static String MAPPING_VERSION = "1.0";
		public static String TARGET_SCHEME =  "Automobiles";
		public static String TARGET_VERSION = "1.0";
		
	   public void setUp(){

		   authoring = new LexEVSAuthoringServiceImpl();
		   lbs = LexBIGServiceImpl.defaultInstance();


		   try {
			lbsm = lbs.getServiceManager(null);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	
	public void testCreateNewMapping()throws LBException{
		   EntryState entryState = new EntryState();
		   entryState.setChangeType(ChangeType.NEW);
		   entryState.setContainingRevision("TestNewMapping1");
		  entryState.setRelativeOrder(new Long(1));
		   AbsoluteCodingSchemeVersionReference mappingCodingScheme = new AbsoluteCodingSchemeVersionReference();
		   mappingCodingScheme.setCodingSchemeURN(MAPPING_SCHEME);
		   mappingCodingScheme.setCodingSchemeVersion(MAPPING_VERSION);
		AbsoluteCodingSchemeVersionReference sourceCodingScheme = new AbsoluteCodingSchemeVersionReference();
		   sourceCodingScheme.setCodingSchemeURN(SOURCE_SCHEME);
		   sourceCodingScheme.setCodingSchemeVersion(SOURCE_VERSION);
		AbsoluteCodingSchemeVersionReference targetCodingScheme = new AbsoluteCodingSchemeVersionReference();
		   targetCodingScheme.setCodingSchemeURN(TARGET_SCHEME);
		   targetCodingScheme.setCodingSchemeVersion(TARGET_VERSION);
		   AssociationSource source = new AssociationSource();
		   source.setSourceEntityCode("E0001");
		   source.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		   AssociationTarget target = new AssociationTarget();
		   target.setTargetEntityCode("Ford");
		   target.setTargetEntityCodeNamespace("Automobiles");
		   source.setTarget(Arrays.asList(target));
		   
		AssociationSource[] associationSources = new AssociationSource[]{source};
		String associationType = "SY";
		String relationsContainerName = "Mapping_relations";
		Date effectiveDate = null;
		Revision revision = new Revision();

			authoring.createAssociationMapping(entryState, 
					   mappingCodingScheme, 
					   sourceCodingScheme, 
					   targetCodingScheme, 
					   associationSources, 
					   associationType , 
					   relationsContainerName, 
					   effectiveDate, 
					   null, 
					   revision, 
					   new Long(0));
	
	   }
	
	public void testCreateNewMappingWithNewRelationsContainer()throws LBException{
		   EntryState entryState = new EntryState();
		   entryState.setChangeType(ChangeType.NEW);
		   entryState.setContainingRevision("TestNewMapping2");
		  entryState.setRelativeOrder(new Long(1));
		   AbsoluteCodingSchemeVersionReference mappingCodingScheme = new AbsoluteCodingSchemeVersionReference();
		   mappingCodingScheme.setCodingSchemeURN(MAPPING_SCHEME);
		   mappingCodingScheme.setCodingSchemeVersion(MAPPING_VERSION);
		AbsoluteCodingSchemeVersionReference sourceCodingScheme = new AbsoluteCodingSchemeVersionReference();
		   sourceCodingScheme.setCodingSchemeURN(SOURCE_SCHEME);
		   sourceCodingScheme.setCodingSchemeVersion(SOURCE_VERSION);
		AbsoluteCodingSchemeVersionReference targetCodingScheme = new AbsoluteCodingSchemeVersionReference();
		   targetCodingScheme.setCodingSchemeURN(TARGET_SCHEME);
		   targetCodingScheme.setCodingSchemeVersion(TARGET_VERSION);
		   AssociationSource source = new AssociationSource();
		   source.setSourceEntityCode("E0001");
		   source.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
		   AssociationTarget target = new AssociationTarget();
		   target.setTargetEntityCode("C0001");
		   target.setTargetEntityCodeNamespace("Automobiles");
		   source.setTarget(Arrays.asList(target));
		   
		AssociationSource[] associationSources = new AssociationSource[]{source};
		String associationType = "SY";
		String relationsContainerName = "Mapping_relations_2";
		Date effectiveDate = null;
		Revision revision = new Revision();

			authoring.createAssociationMapping(entryState, 
					   mappingCodingScheme, 
					   sourceCodingScheme, 
					   targetCodingScheme, 
					   associationSources, 
					   associationType , 
					   relationsContainerName, 
					   effectiveDate, 
					   null, 
					   revision, 
					   new Long(0));
	}
	public void testCreateNewMappingWithNewAssociationPredicate()throws LBException{
			   EntryState entryState = new EntryState();
			   entryState.setChangeType(ChangeType.NEW);
			   entryState.setContainingRevision("TestNewMapping3");
			  entryState.setRelativeOrder(new Long(1));
			   AbsoluteCodingSchemeVersionReference mappingCodingScheme = new AbsoluteCodingSchemeVersionReference();
			   mappingCodingScheme.setCodingSchemeURN(MAPPING_SCHEME);
			   mappingCodingScheme.setCodingSchemeVersion(MAPPING_VERSION);
			AbsoluteCodingSchemeVersionReference sourceCodingScheme = new AbsoluteCodingSchemeVersionReference();
			   sourceCodingScheme.setCodingSchemeURN(SOURCE_SCHEME);
			   sourceCodingScheme.setCodingSchemeVersion(SOURCE_VERSION);
			AbsoluteCodingSchemeVersionReference targetCodingScheme = new AbsoluteCodingSchemeVersionReference();
			   targetCodingScheme.setCodingSchemeURN(TARGET_SCHEME);
			   targetCodingScheme.setCodingSchemeVersion(TARGET_VERSION);
			   AssociationSource source = new AssociationSource();
			   source.setSourceEntityCode("E0001");
			   source.setSourceEntityCodeNamespace("GermanMadePartsNamespace");
			   AssociationTarget target = new AssociationTarget();
			   target.setTargetEntityCode("C0001");
			   target.setTargetEntityCodeNamespace("Automobiles");
			   source.setTarget(Arrays.asList(target));
			   
			AssociationSource[] associationSources = new AssociationSource[]{source};
			String associationType = "FI";
			String relationsContainerName = "Mapping_relations_2";
			Date effectiveDate = null;
			Revision revision = new Revision();

				authoring.createAssociationMapping(entryState, 
						   mappingCodingScheme, 
						   sourceCodingScheme, 
						   targetCodingScheme, 
						   associationSources, 
						   associationType , 
						   relationsContainerName, 
						   effectiveDate, 
						   null, 
						   revision, 
						   new Long(0));
	}
}
