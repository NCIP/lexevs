package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.assertFalse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class CleanUpResolvedValueSetUpdateLoads {
LexBIGServiceManager lbsm;
private SourceAssertedValueSetSearchIndexService service;
	@Before
	public void setUp() throws Exception {
	lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
	service = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
	}

	@Test
	public void testRemoveAllCodingSchemes() throws LBInvocationException {
		CodingSchemeRenderingList list = LexBIGServiceImpl.defaultInstance().getSupportedCodingSchemes();
		List<AbsoluteCodingSchemeVersionReference> refs = Arrays.asList(list.getCodingSchemeRendering()).stream().map(x -> {	
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(x.getCodingSchemeSummary().getCodingSchemeURI());
		ref.setCodingSchemeVersion(x.getCodingSchemeSummary().getRepresentsVersion());
		return ref;
		}).collect(Collectors.toList());
		
		refs.forEach(x -> {try {
			lbsm.deactivateCodingSchemeVersion(x, null);
		} catch (LBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} try {
			lbsm.removeCodingSchemeVersion(x);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
	}
	
	@Test
	public void testRemoveAllValueSetDefintions(){
		LexEVSValueSetDefinitionServices services = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		List<String> list = services.listValueSetDefinitionURIs();
		list.forEach(x -> {
			try {
				services.removeValueSetDefinition(new URI(x));
			} catch (LBException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}
	
	@Test
	public void dropAssertedValueSetIndex() {
		service.dropIndexForAllValueSets(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1");
		boolean doesExist = service.doesIndexExist(Constructors.
				createAbsoluteCodingSchemeVersionReference(
						"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1"));
		assertFalse(doesExist);
	}
	

}
