
package org.LexGrid.valueset.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.junit.Test;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedPickListEntry;
import org.lexgrid.valuesets.dto.ResolvedPickListEntryList;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;

/**
 * JUnit for LexEVSPickListServices.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSPickListServicesImplTest extends TestCase{
	
	private LexEVSPickListDefinitionServices pls_;
	
	@Test
	public void testGetPickListDefinitionById() throws LBException {
		PickListDefinition pickList = getPickListService().getPickListDefinitionById("SRITEST:AUTO:DomesticAutoMakers");
		
		assertTrue(pickList.getDefaultEntityCodeNamespace().equals("Automobiles"));
		assertTrue(pickList.getDefaultLanguage().equals("en"));
		assertTrue(pickList.getEntityDescription().getContent().equals("DomesticAutoMakers"));
		
		Mappings maps = pickList.getMappings();
		List<SupportedCodingScheme> scss = maps.getSupportedCodingSchemeAsReference();
		assertTrue(scss.size() == 4);
		
		List<SupportedDataType> sdts = maps.getSupportedDataTypeAsReference();
		for (SupportedDataType sdt : sdts)
		{
			String localId = sdt.getLocalId();
			assertTrue(localId.equals("texthtml") || localId.equals("textplain"));
		}
		
		SupportedLanguage sl = (SupportedLanguage) maps.getSupportedLanguage()[0];
		assertTrue(sl.getLocalId().equals("en"));
		assertTrue(sl.getUri().equals("www.en.org/orsomething"));
		
		List<SupportedNamespace> sns = maps.getSupportedNamespaceAsReference();
		assertTrue(sns.size() == 3);
		
		List<SupportedProperty> sps = maps.getSupportedPropertyAsReference();
		assertTrue(sps.size() == 2);
		for (SupportedProperty sp : sps)
		{
			assertTrue(sp.getLocalId().equals("definition") || sp.getLocalId().equals("textualPresentation"));
		}
		
		List<SupportedSource> sss = maps.getSupportedSourceAsReference();
		assertTrue(sss.size() == 2);
		for (SupportedSource ss : sss)
		{
			String localId = ss.getLocalId();
			
			assertTrue(localId.equals("lexgrid.org") || localId.equals("_111101"));
			
			if (localId.equals("lexgrid.org"))
			{
				assertTrue(ss.getAssemblyRule().equals("rule1"));
				assertTrue(ss.getUri().equals("http://informatics.mayo.edu"));
			}
		}
		
		assertTrue(pickList.getPickListEntryNode().length == 8);
		
		List<PickListEntryNode> plens = pickList.getPickListEntryNodeAsReference();
		for (PickListEntryNode plen : plens)
		{
			String plEntryId = plen.getPickListEntryId();
			assertTrue(plEntryId.equals("PL005p1")
					|| plEntryId.equals("PL005p2")
					|| plEntryId.equals("PLFordp1")
					|| plEntryId.equals("PLJaguarp1")
					|| plEntryId.equals("PLChevroletp1")
					|| plEntryId.equals("PLOldsmobilep1")
					|| plEntryId.equals("PLGMp1")
					|| plEntryId.equals("PLGMp2"));
			
			if (plEntryId.equals("PL005p1"))
			{
				PickListEntry ple = plen.getPickListEntryNodeChoice().getInclusionEntry();
				assertTrue(ple.getEntityCode().equals("005"));
				assertTrue(ple.getEntityCodeNamespace().equals("Automobiles"));
				assertTrue(ple.getPropertyId().equals("p1"));
				assertTrue(ple.getPickText().equals("Domestic Auto Makers"));
				
//				EntryState es = plen.getEntryState();
//				assertTrue(es.getContainingRevision().equals("R001"));
//				assertTrue(es.getPrevRevision().equals("R00A"));
//				assertTrue(es.getRelativeOrder() == 1);
//				assertTrue(es.getChangeType().name().equals(ChangeType.NEW.name()));	
				
				List<String> pcs = ple.getPickContextAsReference();
				assertTrue(pcs.size() == 2);				
				for (String pc : pcs)
				{
					assertTrue(pc.equals("Domestic Auto Makers") || pc.equals("Cars"));
				}
				
				Property prop = (Property) plen.getProperties().getProperty()[0];
				assertTrue(prop.getPropertyName().equals("definition"));
				assertTrue(prop.getValue().getDataType().equals("textplain"));
				assertTrue(prop.getValue().getContent().equals("Definition for Domestic Auto Makers"));
//				EntryState pes = prop.getEntryState();
//				assertTrue(pes.getContainingRevision().equals("R001"));
//				assertTrue(pes.getPrevRevision().equals("R00A"));
//				assertTrue(pes.getRelativeOrder() == 1);
//				assertTrue(pes.getChangeType().name().equals(ChangeType.NEW.name()));				
			}
			else if (plEntryId.equals("PL005p2"))
			{
				PickListEntry ple = plen.getPickListEntryNodeChoice().getInclusionEntry();
				assertTrue(ple.getEntityCode().equals("005"));
				assertTrue(ple.getEntityCodeNamespace().equals("Automobiles"));
				assertTrue(ple.getPropertyId().equals("p2"));
				assertTrue(ple.getPickText().equals("American Car Companies"));
				
//				EntryState pes = plen.getEntryState();
//				assertTrue(pes.getContainingRevision().equals("R001"));
//				assertTrue(pes.getPrevRevision().equals("R00A"));
//				assertTrue(pes.getRelativeOrder() == 1);
//				assertTrue(pes.getChangeType().name().equals(ChangeType.NEW.name()));	
				
				List<String> pcs = Arrays.asList(ple.getPickContext());
				assertTrue(pcs.size() == 0);				
				
				Property prop = (Property) plen.getProperties().getProperty()[0];
				assertTrue(prop.getPropertyName().equals("definition"));
				assertTrue(prop.getValue().getDataType().equals("textplain"));
				assertTrue(prop.getValue().getContent().equals("Definition for Amerocan Auto Makers"));
//				pes = prop.getEntryState();
//				assertTrue(pes.getContainingRevision().equals("R001"));
//				assertTrue(pes.getPrevRevision().equals("R00A"));
//				assertTrue(pes.getRelativeOrder() == 1);
//				assertTrue(pes.getChangeType().name().equals(ChangeType.NEW.name()));				
			}
			else if (plEntryId.equals("PLFordp1"))
			{
				PickListEntry ple = plen.getPickListEntryNodeChoice().getInclusionEntry();
				assertTrue(ple.getEntityCode().equals("Ford"));
				assertTrue(ple.getEntityCodeNamespace().equals("Automobiles"));
				assertTrue(ple.getPropertyId().equals("p1"));
				assertTrue(ple.getPickText().equals("Ford"));
				
				List<String> pcs = Arrays.asList(ple.getPickContext());
				assertTrue(pcs.size() == 0);				
				
				Property prop = (Property) plen.getProperties().getProperty()[0];
				assertTrue(prop.getPropertyName().equals("textualPresentation"));
				assertTrue(prop.getIsActive() == true);
				assertTrue(prop.getLanguage().equals("en"));
				assertTrue(prop.getPropertyId().equals("p1"));
				assertTrue(prop.getPropertyType().equals("presentation"));
				assertTrue(prop.getStatus().equals("active"));
				String owner = prop.getOwner();
				assertTrue(owner.equals("Ford"));
				assertTrue(prop.getValue().getDataType().equals("textplain"));
				assertTrue(prop.getValue().getContent().equals("Property for Ford"));
//				EntryState pes = prop.getEntryState();
//				assertTrue(pes.getContainingRevision().equals("R001"));
//				assertTrue(pes.getPrevRevision().equals("R00A"));
//				assertTrue(pes.getRelativeOrder() == 1);
//				assertTrue(pes.getChangeType().name().equals(ChangeType.NEW.name()));				
			}
		}
		
		pickList = getPickListService().getPickListDefinitionById("SRITEST:FA:MicrobialStructureOntologyMinusMCell");
		
		assertTrue(pickList.getDefaultEntityCodeNamespace().equals("fungal_anatomy"));
		assertTrue(pickList.getDefaultLanguage().equals("en"));
		assertTrue(pickList.getDefaultSortOrder().equals("Assending"));
		assertTrue(pickList.getEntityDescription().getContent().equals("Microbial Structure Ontology  with out Mating cell"));
		
		plens = Arrays.asList(pickList.getPickListEntryNode());
		List<PickListEntry> plInclusionList = new ArrayList<PickListEntry>();
		List<PickListEntryExclusion> plExclusionList = new ArrayList<PickListEntryExclusion>();
		
		PickListEntry plInclusion = null;
		PickListEntryExclusion plExclusion = null;
		
		for (PickListEntryNode plen : plens)
		{
			plInclusion = plen.getPickListEntryNodeChoice().getInclusionEntry();
			if (plInclusion != null)
				plInclusionList.add(plInclusion);
			
			plExclusion = plen.getPickListEntryNodeChoice().getExclusionEntry();
			if (plExclusion != null)
				plExclusionList.add(plExclusion);
		}
		
		assertTrue(plInclusionList.size() == 8);
		assertTrue(plExclusionList.size() == 1);
		
		plInclusionList.clear();
		plExclusionList.clear();
	}
	
	@Test
	public void testResolvePickListForTerm() throws LBException {
		ResolvedPickListEntryList pickLists = getPickListService().resolvePickListForTerm("SRITEST:AUTO:AllDomesticButGM", "Jaguar", MatchAlgorithms.exactMatch.name(), null, null, false, null, null);
		assertTrue(pickLists.getResolvedPickListEntryCount() == 1);
		
		Iterator<ResolvedPickListEntry> plItr = pickLists.iterateResolvedPickListEntry();
		while(plItr.hasNext())
		{
			ResolvedPickListEntry pickList = plItr.next();
			assertTrue(pickList.getEntityCode().equals("Jaguar"));
			assertTrue(pickList.getPropertyId().equals("p1"));
		}
		
		pickLists = getPickListService().resolvePickListForTerm("SRITEST:FA:MicrobialStructureOntologyMinusMCell", "cell", MatchAlgorithms.LuceneQuery.name(), null, null, false, null, null);
		plItr = pickLists.iterateResolvedPickListEntry();
		while(plItr.hasNext())
		{
			ResolvedPickListEntry pickList = plItr.next();
			assertTrue(pickList.getEntityCode().equals("FAO:0000032"));
		}
		
		pickLists = getPickListService().resolvePickListForTerm("SRITEST:FA:MicrobialStructureOntologyMinusMCell", "structure", MatchAlgorithms.LuceneQuery.name(), null, null, false, null, null);
		plItr = pickLists.iterateResolvedPickListEntry();
		while(plItr.hasNext())
		{
			ResolvedPickListEntry pickList = plItr.next();
			assertTrue(pickList.getEntityCode().equals("FAO:0000001") || pickList.getEntityCode().equals("FAO:0000018"));
		}
		
		pickLists.removeAllResolvedPickListEntry();
	}
	
	@Test
	public void testResolvePickListForTermForCompleteSet() throws LBException {
		ResolvedPickListEntryList pickLists = getPickListService().resolvePickListForTerm("SRITEST:FA:MSOntologyAndHyphaInMycelium", "hypha", MatchAlgorithms.LuceneQuery.name(), null, null, false, null, null);
		
		assertTrue(pickLists.getResolvedPickListEntryCount() == 5);
		
		Iterator<ResolvedPickListEntry> plItr = pickLists.iterateResolvedPickListEntry();
		while(plItr.hasNext())
		{
			ResolvedPickListEntry pickList = plItr.next();
			assertTrue(pickList.getEntityCode().equals("FAO:0001014") // this will come twice b'cos there are two properties with term hypha in it.
					|| pickList.getEntityCode().equals("FAO:0001013")
					|| pickList.getEntityCode().equals("FAO:0001004")
					|| pickList.getEntityCode().equals("FAO:0001003"));
			assertTrue(pickList.getEntityCodeNamespace().equals("fungal_anatomy"));
			assertTrue(pickList.getPickText().contains("hypha"));
		}
		
		pickLists.removeAllResolvedPickListEntry();
	}
	
	@Test
	public void testResolvePickList() throws LBException {
		ResolvedPickListEntryList pleList = getPickListService().resolvePickList("SRITEST:AUTO:DomesticAutoMakers", true, null, null);
		
		assertTrue(pleList.getResolvedPickListEntryCount() == 8);
		
		Iterator<ResolvedPickListEntry> plItr = pleList.iterateResolvedPickListEntry();
		while(plItr.hasNext())
		{
			ResolvedPickListEntry plen = plItr.next();
			
			assertTrue(plen.getEntityCodeNamespace().equals("Automobiles"));
			
			String code = plen.getEntityCode();
			assertTrue(code.equals("005")
					|| code.equals("Ford")
					|| code.equals("GM")
					|| code.equals("Jaguar")
					|| code.equals("Chevy")
					|| code.equals("73"));
			
			if (code.equals("005"))
			{
				assertTrue((plen.getPickText().equals("Domestic Auto Makers") && plen.getPropertyId().equals("p1"))
						|| (plen.getPickText().equals("American Car Companies") && plen.getPropertyId().equals("p2")));
			}
			else if (code.equals("Chevy"))
			{
				assertTrue(plen.getPickText().equals("Chevrolet") && plen.getPropertyId().equals("p1"));
			}
			else if (code.equals("Ford"))
			{
				assertTrue(plen.getPickText().equals("Ford") && plen.getPropertyId().equals("p1"));
			}
			else if (code.equals("GM"))
			{
				assertTrue((plen.getPickText().equals("General Motors") && plen.getPropertyId().equals("p1"))
						|| (plen.getPickText().equals("GM") && plen.getPropertyId().equals("p2")));
			}
			else if (code.equals("Jaguar"))
			{
				assertTrue(plen.getPickText().equals("Jaguar") && plen.getPropertyId().equals("p1"));
			}
			else if (code.equals("73"))
			{
				assertTrue(plen.getPickText().equals("Oldsmobile") && plen.getPropertyId().equals("p1"));
			}
		}
		
		pleList = getPickListService().resolvePickList("SRITEST:FA:MicrobialStructureOntologyMinusMCell", true, null, null);
		
		assertTrue(pleList.getResolvedPickListEntryCount() == 8);
		
		plItr = pleList.iterateResolvedPickListEntry();
		while(plItr.hasNext())
		{
			ResolvedPickListEntry plen = plItr.next();
			
			assertTrue(plen.getEntityCodeNamespace().equals("fungal_anatomy"));
			
			String code = plen.getEntityCode();
			assertTrue(code.equals("FAO:0000001")
					|| code.equals("FAO:0000005")
					|| code.equals("FAO:0000018")
					|| code.equals("FAO:0000019")
					|| code.equals("FAO:0000032")
					|| code.equals("FAO:0001001"));
			
			if (code.equals("FAO:0000001"))
			{
				assertTrue((plen.getPickText().equals("microbial structure ontology") && plen.getPropertyId().equals("p1"))
						|| (plen.getPickText().equals("fungal structure ontology") && plen.getPropertyId().equals("p2")));
			}
			else if (code.equals("FAO:0000005"))
			{
				assertTrue(plen.getPickText().equals("obsolete") && plen.getPropertyId().equals("p1"));
			}
			else if (code.equals("FAO:0000018"))
			{
				assertTrue(plen.getPickText().equals("unicellular structure") && plen.getPropertyId().equals("p1"));
			}
			else if (code.equals("FAO:0000019"))
			{
				assertTrue(plen.getPickText().equals("spore") && plen.getPropertyId().equals("p1"));
			}
			else if (code.equals("FAO:0000032"))
			{
				assertTrue((plen.getPickText().equals("vegetative cell") && plen.getPropertyId().equals("p1"))
						|| (plen.getPickText().equals("yeast-form") && plen.getPropertyId().equals("p3")));
			}
			else if (code.equals("FAO:0001001"))
			{
				assertTrue(plen.getPickText().equals("hypha") && plen.getPropertyId().equals("p1"));
			}
		}
	}
	
	@Test
	public void testResolvePickListByObject() throws LBException {
		PickListDefinition pickList = new PickListDefinition();
		
		pickList.setCompleteSet(Boolean.TRUE);
		pickList.setDefaultEntityCodeNamespace("Automobiles");
		pickList.setDefaultLanguage("en");
		pickList.setRepresentsValueSetDefinition("SRITEST:AUTO:GM");
		pickList.setPickListId("PLObject");
		pickList.setStatus("active");
		
		AbsoluteCodingSchemeVersionReferenceList incsvrl = new AbsoluteCodingSchemeVersionReferenceList();
	    incsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.1"));
		
		ResolvedPickListEntryList pls = getPickListService().resolvePickList(pickList, true, incsvrl, null);
		
		assertTrue(pls.getResolvedPickListEntryCount() == 3);
		
		Iterator<ResolvedPickListEntry> plItr = pls.iterateResolvedPickListEntry();
	    
	    while(plItr.hasNext())
		{
			ResolvedPickListEntry pl = plItr.next();
			assertTrue(pl.getEntityCode().equalsIgnoreCase("Chevy")
					|| pl.getEntityCode().equalsIgnoreCase("GMC")
					|| pl.getEntityCode().equalsIgnoreCase("GM"));
		}
	    PickListEntry inclusionEntry = new PickListEntry();
	    inclusionEntry.setEntityCode("Ford");
	    inclusionEntry.setEntryOrder(0L);
	    inclusionEntry.setEntityCodeNamespace("Automobiles");
	    inclusionEntry.setPickText("Ford");
	    		
	    PickListEntryNodeChoice pickListEntryNodeChoice = new PickListEntryNodeChoice();
	    pickListEntryNodeChoice.setInclusionEntry(inclusionEntry);

	    PickListEntryNode pleNode = new PickListEntryNode();
	    pleNode.setPickListEntryId("pleNodeId1");
	    pleNode.setPickListEntryNodeChoice(pickListEntryNodeChoice);
	    
	    pickList.addPickListEntryNode(pleNode);
	    
	    pls = getPickListService().resolvePickList(pickList, true, incsvrl, null);
		
		assertTrue(pls.getResolvedPickListEntryCount() == 4);
		
		plItr = pls.iterateResolvedPickListEntry();
	    
	    while(plItr.hasNext())
		{
			ResolvedPickListEntry pl = plItr.next();
			assertTrue(pl.getEntityCode().equalsIgnoreCase("Chevy")
					|| pl.getEntityCode().equalsIgnoreCase("GMC")
					|| pl.getEntityCode().equalsIgnoreCase("GM")
					|| pl.getEntityCode().equalsIgnoreCase("Ford"));
		}
	}
	
	@Test
	public void testResolvePickListForCompleteSet() throws LBException {
		AbsoluteCodingSchemeVersionReferenceList incsvrl = new AbsoluteCodingSchemeVersionReferenceList();
	    incsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0"));
		
	    /**
	     * For version '1.0', only one pick list entry should return, and that is Chevy, 
	     * Jaguar should not be returned b'cos the only presentation it has is not preferred.
	     */
	    ResolvedPickListEntryList pickLists = getPickListService().resolvePickList("SRITEST:AUTO:DomasticLeafOnly", true, incsvrl, null);
	    assertTrue(pickLists.getResolvedPickListEntryCount() == 1); 
	    
	    Iterator<ResolvedPickListEntry> plItr = pickLists.iterateResolvedPickListEntry();
	    
	    while(plItr.hasNext())
		{
			ResolvedPickListEntry pickList = plItr.next();
			assertTrue(pickList.getEntityCode().equalsIgnoreCase("chevy"));
			assertTrue(pickList.getPickText().equalsIgnoreCase("chevy"));
		}
	    
	    incsvrl = new AbsoluteCodingSchemeVersionReferenceList();
	    incsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.1"));
		
	    /**
	     * For version '1.1', five pick list entry should return, and they are :windsor, f150, focus, gmc and chevy 
	     * Jaguar should not be returned b'cos the only presentation it has is not preferred.
	     */
	    pickLists = getPickListService().resolvePickList("SRITEST:AUTO:DomasticLeafOnly", true, incsvrl, null);
	    
	    assertTrue(pickLists.getResolvedPickListEntryCount() == 5);
	    
	    plItr = pickLists.iterateResolvedPickListEntry();
	    
	    while(plItr.hasNext())
		{
			ResolvedPickListEntry pickList = plItr.next();
			assertTrue(pickList.getEntityCode().equalsIgnoreCase("Windsor")
					|| pickList.getEntityCode().equalsIgnoreCase("F150")
					|| pickList.getEntityCode().equalsIgnoreCase("Focus")
					|| pickList.getEntityCode().equalsIgnoreCase("GMC")
					|| pickList.getEntityCode().equalsIgnoreCase("Chevy"));
			
			assertTrue(pickList.getPickText().equalsIgnoreCase("Windsor")
					|| pickList.getPickText().equalsIgnoreCase("F150")
					|| pickList.getPickText().equalsIgnoreCase("Focus")
					|| pickList.getPickText().equalsIgnoreCase("GMC trucks")
					|| pickList.getPickText().equalsIgnoreCase("Chevy"));
		}
	    
		pickLists = getPickListService().resolvePickList("SRITEST:FA:hyphaLeafOnly", true, null, null);
		
		assertTrue(pickLists.getResolvedPickListEntryCount() == 3);
		
		plItr = pickLists.iterateResolvedPickListEntry();
		while(plItr.hasNext())
		{
			ResolvedPickListEntry pickList = plItr.next();
			assertTrue((pickList.getEntityCode().equals("FAO:0001018")
						&& pickList.getPickText().equals("hypha with dolipore septa"))
					|| (pickList.getEntityCode().equals("FAO:0001014")
						&& pickList.getPickText().equals("aseptate hypha in mycelium"))
					|| (pickList.getEntityCode().equals("FAO:0001013")
						&& pickList.getPickText().equals("hypha with dolipore septa, in mycelium")));
			assertTrue(pickList.getEntityCodeNamespace().equals("fungal_anatomy"));
			assertTrue(pickList.getPickText().contains("hypha"));
		}
		
		pickLists.removeAllResolvedPickListEntry();
	}
	
	@Test
	public void testGetPickListValueSet() throws LBException {
		URI valueSetDefURI = getPickListService().getPickListValueSetDefinition("SRITEST:AUTO:AllDomesticButGM");		
		assertTrue(valueSetDefURI.toString().equals("SRITEST:AUTO:AllDomesticButGM"));
		
		valueSetDefURI = getPickListService().getPickListValueSetDefinition("SRITEST:AUTO:DomesticAutoMakers");		
		assertTrue(valueSetDefURI.toString().equals("SRITEST:AUTO:DomesticAutoMakers"));
	}

	@Test
	public void testGetPickListDefinitionIdForValueSetDefinitionUri() throws LBException, URISyntaxException {
		List<String> pickListIds = getPickListService().getPickListDefinitionIdForValueSetDefinitionUri(new URI("SRITEST:AUTO:DomesticAutoMakers"));
		
		for (String pickListId : pickListIds)
		{
			assertTrue(pickListId.equals("SRITEST:AUTO:DomesticAutoMakers"));
			
			PickListDefinition pickList = getPickListService().getPickListDefinitionById(pickListId);
			assertTrue(pickList.getDefaultEntityCodeNamespace().equals("Automobiles"));
			
			Iterator<PickListEntryNode> pleItr = pickList.getPickListEntryNodeAsReference().iterator();
			
			while (pleItr.hasNext())
			{
				PickListEntryNode plEntry = pleItr.next();
				assertTrue(plEntry.getPickListEntryId().equals("PL005p1")
						|| plEntry.getPickListEntryId().equals("PL005p2")
						|| plEntry.getPickListEntryId().equals("PLFordp1")
						|| plEntry.getPickListEntryId().equals("PLJaguarp1")
						|| plEntry.getPickListEntryId().equals("PLChevroletp1")
						|| plEntry.getPickListEntryId().equals("PLOldsmobilep1")
						|| plEntry.getPickListEntryId().equals("PLGMp1")
						|| plEntry.getPickListEntryId().equals("PLGMp2"));
			}
		}
	}
	
//	public void testExportPicklistDefinition(){
//		try {
//			getPickListService().exportPickListDefinition("SRITEST:FA:hyphaLeafOnly", "c:\\testExportPLD.xml", true, true);
//		} catch (LBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}
	
	private LexEVSPickListDefinitionServices getPickListService(){
		if (pls_ == null) {
			LexEVSPickListDefinitionServices pickListService = LexEVSPickListDefinitionServicesImpl.defaultInstance();
			pls_ = pickListService;
		}
		return pls_;
	}
}