package org.LexGrid.LexBIG.mapping;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
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
		public static String SOURCE_SCHEME = "GermanMadeParts";
		public static String SOURCE_VERSION = "2.0";
		public static String MAPPING_SCHEME = "http://default.mapping.container";
		public static String MAPPING_VERSION = "1.0";
		public static String TARGET_SCHEME =  "Automobiles";
		public static String TARGET_VERSION = "1.0";
		
	   public void setUp(){

		   authoring = new LexEVSAuthoringServiceImpl();
		   lbs = LexBIGServiceImpl.defaultInstance();
		   csvt = new CodingSchemeVersionOrTag();
			 csvt.setVersion("1.0");

		   try {
			lbsm = lbs.getServiceManager(null);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   
	   public void testMappingLoad()throws LBException {
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
           AssociationSource[] sources = new AssociationSource[]{source, source1, source2};
           
           try{
        	   loadMappings(sources);
           }
           catch(ClassNotFoundException e){
        	   //do nothing
           }
	   }
	   public void testLoadedMappingsEntities() throws LBException{

		   CodedNodeSet cns = lbs.getCodingSchemeConcepts("http://default.mapping.container", csvt);
		   String[] codes = new String[]{"T0001", "P0001", "A0001", "005"};
		   ConceptReferenceList list = ConvenienceMethods.createConceptReferenceList(codes);
		   cns = cns.restrictToCodes(list);
		   ResolvedConceptReferencesIterator rcrl = cns.resolve(null, null, null);
		   List<String> codeList = Arrays.asList(codes);
		   while(rcrl.hasNext()){
			  ResolvedConceptReference rcr =  rcrl.next();
			  if(!codeList.contains(rcr.getConceptCode())){
				  fail("code not found in concept reference list");
			  }
		   }
	   }
	   
	   public void testLoadedMappedAssociations() throws LBException{
		   String[] codes = new String[]{"T0001", "P0001", "A0001", "005"};
		   List<String> codeList = Arrays.asList(codes);
		   CodedNodeGraph cng = lbs.getNodeGraph("http://default.mapping.container", csvt, null);
		  ResolvedConceptReferenceList rcrl = cng.resolveAsList(ConvenienceMethods.createConceptReference("T0001", "http://default.mapping.container"), true, true, -1, -1, null, null, null, -1);
		  ResolvedConceptReferenceList rcrl1 = cng.resolveAsList(ConvenienceMethods.createConceptReference("P0001", "http://default.mapping.container"), true, true, -1, -1, null, null, null, -1);
		  ResolvedConceptReference[] conceptList1 = rcrl.getResolvedConceptReference();
		  ResolvedConceptReference[] conceptList2 = rcrl1.getResolvedConceptReference();
		  for(ResolvedConceptReference rcr : conceptList1){
			 Association[] assoc = rcr.getSourceOf().getAssociation();
			 for(Association a : assoc){
				AssociatedConcept[] concepts = a.getAssociatedConcepts().getAssociatedConcept();
				for(AssociatedConcept ac : concepts){
					if(!codeList.contains(ac.getConceptCode())){
						fail("code not found in associated concept list");
					}
				}
			 }
		  }
		  
		  for(ResolvedConceptReference rcr : conceptList2){
				 Association[] assoc = rcr.getSourceOf().getAssociation();
				 for(Association a : assoc){
					AssociatedConcept[] concepts = a.getAssociatedConcepts().getAssociatedConcept();
					for(AssociatedConcept ac : concepts){
						if(!codeList.contains(ac.getConceptCode())){
							fail("code not found in associated concept list");
						}
					}
				 }
			  }
	   }
	   
	   public void loadMappings(AssociationSource[] sources) throws ClassNotFoundException, LBException{
		   authoring.createMappingWithDefaultValues(sources, "GermanMadeParts", "2.0", "Automobiles", "1.0", "SY");
			AbsoluteCodingSchemeVersionReference codingSchemeVersion = new AbsoluteCodingSchemeVersionReference();
			codingSchemeVersion.setCodingSchemeURN("http://default.mapping.container");
			codingSchemeVersion.setCodingSchemeVersion("1.0");
		   lbsm.activateCodingSchemeVersion(codingSchemeVersion);
	   }
	   
	   
	   
}
