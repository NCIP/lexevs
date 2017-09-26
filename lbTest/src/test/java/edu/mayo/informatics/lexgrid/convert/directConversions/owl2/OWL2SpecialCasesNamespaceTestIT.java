package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.Property;
import org.junit.Before;
import org.junit.Test;

public class OWL2SpecialCasesNamespaceTestIT extends DataLoadTestBaseSpecialCases{

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
	}
	//CodingScheme MetaData Tests
	
	@Test
	public void testForCodingSchemeMetaData() throws ParseException{
		assertTrue(cs.getDefaultLanguage().equals("en"));
		
		
		SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd, yyyy");
		Date date;
		String stringDate = "august 01, 2017";
		date = formatDate.parse(stringDate);
		assertNotNull(cs.getEffectiveDate());
		assertTrue(cs.getEffectiveDate().compareTo(date) == 0);
		
		assertTrue(cs.getEntityDescription().getContent().equals("Test of OWL2 constructions for import into LexEVS.  Testing namespaces."));
		boolean hasVersionIRI = false;
		for(Property prop: cs.getProperties().getProperty()){
			if(prop.getPropertyName().equals("versionIRI") && prop.getValue().getContent().equals("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl/0.2.0")){
				hasVersionIRI = true;
				break;
			}
		}
		assertTrue(hasVersionIRI);
		assertTrue(cs.getSourceCount() > 0);
		assertTrue(cs.getSource(0).getContent().equals("nci evs"));
		assertTrue(cs.getCodingSchemeName().equals("owl2lexevs"));		
	}

	//Entity Unit Tests

	@Test
	public void testNamespaceNotPresentInEntityName() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("http://purl.obolibrary.org/obo/CL_0000000"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertFalse(itr.hasNext());
	}

	@Test
	public void testNamespacePresentInEntityName() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("ncit:CL_0000000"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testRootNamespaceInEntityName() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("ncit:@@"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOboNamespaceInEntityName() throws LBInvocationException, LBParameterException, LBResourceUnavailableException{
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("obo:BFO_0000001"));
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		assertFalse(itr.hasNext());
	}
}
