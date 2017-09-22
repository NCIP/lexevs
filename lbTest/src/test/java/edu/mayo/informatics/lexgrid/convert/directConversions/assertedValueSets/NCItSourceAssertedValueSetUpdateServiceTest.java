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
		vsUpdate = new NCItSourceAssertedValueSetUpdateServiceImpl();
	}

	@Test
	public void getCodingSchemeNamespaceForURIandVersion() throws LBException {
		String namespace = vsUpdate.getCodingSchemeNamespaceForURIandVersion("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "17.07e");
		assertTrue(namespace != null);
		assertEquals(namespace, "Thesaurus");
	}
	

	@Test
	public void getReferencesForVyersionTest() throws LBException {
		List<String> refs = vsUpdate.getReferencesForVersion("17.07e");
		assertTrue(refs.size() > 0);
		assertEquals(refs.stream().filter(x -> x.equals("C100051")).findAny().get(), "C100051");
	}
	
	@Test 
	public void getDateForVersionTest() throws LBException, ParseException{
		Date date = vsUpdate.getDateForVersion("17.07e");
		assertNotNull(date);
		DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		assertEquals(outputDateFormat.format(date).toString(), "2017-07-31 00:00:00");		
	}

	@Test
	public void getVersionsForDateRangeTest() throws LBInvocationException, LBException{
//		DateFormat fmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar current = Calendar.getInstance();
		current.set(2017,  7, 30);
		Date currentDate = current.getTime();
		Calendar previous = Calendar.getInstance();
		previous.set(2017, 1, 30);
		Date previousDate = previous.getTime();
		List<String> versions = vsUpdate.getVersionsForDateRange(previousDate, 
				currentDate);
		assertTrue(versions.size() > 0);
	}
}
