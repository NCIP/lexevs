package org.LexGrid.LexBIG42.Impl.function.metadata;

import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.LexGridLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfFormalName;
import org.LexGrid.LexOnt.CsmfVersion;
import org.LexGrid.concepts.Presentation;

/**
 * Tests Metadata search for NCI Thes.
 * 
 * @author Kevin Peterson
 * 
 */
public class TestNCIThesMetadata extends LexBIGServiceTestCase {

	final static String testID = "T1_FNC_70";

	@Override
	protected String getTestID()
	{
		return testID;
	}

	public void testT1_FNC_50() throws LBException
	{
		LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();

		LexBIGServiceMetadata smd =  lbsi.getServiceMetadata();

		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(META_SCHEME_MANIFEST_URN);
		acsvr.setCodingSchemeVersion(META_SCHEME_MANIFEST_VERSION);
		smd.restrictToCodingScheme(acsvr);

		MetadataPropertyList mdpl = smd.resolve();
		Iterator<MetadataProperty> metaItr = mdpl.iterateMetadataProperty();
		assertTrue("Did not find the Metadata for the Coding Scheme", metaItr.hasNext());
		
		//Look through the Metadata and make sure its returning the coding schemes...
		//Here we check a few.
		boolean foundAIR = false;
		boolean foundMedra = false;
		while(metaItr.hasNext()){
			MetadataProperty property = metaItr.next();
			if(property.getName().equals("codingScheme") && property.getValue().equals("AIR")){
				foundAIR = true;
			}
			if(property.getName().equals("codingScheme") && property.getValue().equals("MDR")){
				foundMedra = true;
			}  
		}
		assertTrue("Didn't find the Metadata that was expected", foundAIR && foundMedra);
	}
}
