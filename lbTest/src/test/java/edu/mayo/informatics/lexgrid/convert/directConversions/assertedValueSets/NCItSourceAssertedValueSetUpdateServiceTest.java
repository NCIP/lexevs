package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.valuesets.sourceasserted.impl.NCItSourceAssertedValueSetUpdateServiceImpl;

public class NCItSourceAssertedValueSetUpdateServiceTest {
	NCItSourceAssertedValueSetUpdateServiceImpl vsUpdate;
	@Before
	public void setUp() throws Exception {
		vsUpdate = new NCItSourceAssertedValueSetUpdateServiceImpl(
				"owl2lexevs", "0.1.5.1", "Concept_In_Subset", "true", 
				"http://evs.nci.nih.gov/valueset/","NCI","Contributing_Source",
				"Semantic_Type", "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
	}

	@Test
	public void getCodingSchemeNamespaceForURIandVersion() throws LBException {
		String namespace = vsUpdate.getCodingSchemeNamespaceForURIandVersion("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5.1");
		assertTrue(namespace != null);
		assertEquals(namespace, "owl2lexevs");
	}
	

	@Test
	public void getReferencesForVyersionTest() throws LBException {
		List<String> refs = vsUpdate.getReferencesForVersion("0.1.5.1");
		assertTrue(refs.size() > 0);
		assertEquals(refs.stream().filter(x -> x.equals("C48326")).findAny().get(), "C48326");
	}
	
	@Test 
	public void getDateForVersionTest() throws LBException, ParseException{
		Date date = vsUpdate.getDateForVersion("0.1.5.1");
		assertNotNull(date);
		DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		assertEquals(outputDateFormat.format(date).toString(), "2017-01-08 00:00:00");		
	}

	@Test
	public void getVersionsForDateRangeTest() throws LBInvocationException, LBException{
//		DateFormat fmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar current = Calendar.getInstance();
        Date myDate;
        current.set(Calendar.MONTH, 0);
        current.set(Calendar.DATE, 8);
        current.set(Calendar.YEAR, 2017);
        current.set(Calendar.HOUR,0);
        current.set(Calendar.MINUTE,0);
        current.set(Calendar.SECOND,0);
		Date currentDate = current.getTime();
		Calendar previous = Calendar.getInstance();
		previous.set(Calendar.MONTH, 7);
		previous.set(Calendar.DATE, 15);
		previous.set(Calendar.YEAR, 2016);
		previous.set(Calendar.HOUR,0);
		previous.set(Calendar.MINUTE,0);
		previous.set(Calendar.SECOND,0);
		Date previousDate = previous.getTime();
		List<String> versions = vsUpdate.getVersionsForDateRange(previousDate, 
				currentDate);
		assertTrue(versions.size() > 1);
	}
}
