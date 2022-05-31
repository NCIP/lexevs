package org.lexevs.dao.database.ibatis.ncihistory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.service.ncihistory.NciHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(readOnly=false)
public class MybatisNciHistoryDaoTest  extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	IbatisNciHistoryDao nciHistoryDao;

	@Test
	public final void testDoGetSupportedLgSchemaVersions() {
		List<LexGridSchemaVersion> schemas = nciHistoryDao.doGetSupportedLgSchemaVersions();
		assertNotNull(schemas);
		assertTrue(schemas.size() > 0);
		System.out.println(schemas.get(0));
	}

	@Test
	public final void testGetAncestors() {
		List<NCIChangeEvent> events = nciHistoryDao.getAncestors(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "Surgical_Procedure");
		assertNotNull(events);
		assertTrue(events.size() > 0);
		System.out.println(events.get(0));
	}

	@Test
	public final void testGetBaseLines() {
		List<SystemRelease> releases = nciHistoryDao.getBaseLines(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", null, null);
		assertNotNull(releases);
		assertTrue(releases.size() > 0);
		assertEquals(releases.size(), 29);
	}

	@Test
	public final void testGetConceptCreateVersion() {
		CodingSchemeVersion version = nciHistoryDao.getConceptCreateVersion("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "C49239");
		assertNotNull(version);
		assertNotNull(version.getVersion());
		LocalDate date = LocalDate.parse("2006-01-03");
		 DateTimeFormatter format =
			      DateTimeFormatter.ofPattern("dd-MMM-yy");
		 
		assertEquals(version.getVersion(),date.format(format).toUpperCase());
	}

	@Test
	public final void testGetConceptChangeVersions() throws ParseException {
		
	      String value = "2006-01-02";
	      String value1 = "2006-01-04";

	       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");      

	       Date date = formatter.parse(value);     
	       Date date2 = formatter.parse(value1);   

	       List<CodingSchemeVersion> versions = nciHistoryDao.getConceptChangeVersions(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "C16000", date, date2);
	       
	       assertNotNull(versions);
	       assertTrue(versions.size() > 0);
			LocalDate ldate = LocalDate.parse("2006-01-03");
			 DateTimeFormatter format =
				      DateTimeFormatter.ofPattern("dd-MMM-yy");
			 
			assertEquals(versions.get(0).getVersion(),ldate.format(format).toUpperCase());
	       
	}

	@Test
	public final void testGetDescendants() {
		List<NCIChangeEvent> events = nciHistoryDao.getDescendants(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "C50318");
		assertNotNull(events);
		assertTrue(events.size() > 0);
		System.out.println(events.get(0));
	}

	@Test
	public final void testGetEarliestBaseLine() {
		SystemRelease release = nciHistoryDao.getEarliestBaseLine(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		assertEquals("NCI Thesaurus with editing completed through July 22, 2002"
				, release.getEntityDescription().getContent());
		System.out.println(release.getEntityDescription().getContent());
	}

	@Test
	public final void testGetEditActionListStringStringDate() throws ParseException {
		
	      String value  = "2006-01-03 00:00:00";

	       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");      

	       Date date = formatter.parse(value); 
		 List<NCIChangeEvent> changeEvents = nciHistoryDao.getEditActionList(
				 "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "C50318", date);
		 assertNotNull(changeEvents);
		 assertTrue(changeEvents.size() > 0);
		 assertEquals(changeEvents.get(0).getEditaction().name(), "MERGE");
	}

	@Test
	public final void testGetEditActionListStringStringDateDate() throws ParseException {
	      String value  = "2006-01-02 00:00:00";
	      String value1  = "2006-01-04 00:00:00";

	       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");      

	       Date date = formatter.parse(value); 
	       Date date1 = formatter.parse(value1); 
		 List<NCIChangeEvent> changeEvents = nciHistoryDao.getEditActionList(
				 "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "C50318", date, date1);
		 
		 assertNotNull(changeEvents);
		 assertTrue(changeEvents.size() > 0);
		 assertEquals(changeEvents.get(0).getEditaction().name(), "MERGE");
	}

	@Test
	public final void testGetEditActionListStringStringString() {
		 List<NCIChangeEvent> changeEvents = nciHistoryDao.getEditActionList(
				 "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "C50318", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f");
		 assertNotNull(changeEvents);
		 assertTrue(changeEvents.size() > 0);
		 assertEquals(changeEvents.get(0).getEditaction().name(), "MERGE");
	}

	@Test
	public final void testGetLatestBaseLine() {
		SystemRelease release = nciHistoryDao.getLatestBaseLine(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		assertEquals("Editing of NCI Thesaurus 06.01c was completed on January 27, 2006.  Version 06.01c was January's third build in our development cycle."
				, release.getEntityDescription().getContent());
		System.out.println(release.getEntityDescription().getContent());
	}

	@Test
	public final void testGetSystemReleaseForReleaseUri() {
		SystemRelease release = nciHistoryDao.getSystemReleaseForReleaseUri(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#:05.12f");
		assertEquals("Editing of NCI Thesaurus 05.12f was completed on January 3, 2006.  Version 05.12f was December's sixth build in our development cycle.", release.getEntityDescription().getContent());
		System.out.println(release.getEntityDescription().getContent());
	}

	@Test
	public final void testGetSystemReleaseForReleaseUid() {
		SystemRelease release = nciHistoryDao.getSystemReleaseForReleaseUid(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "28");
		assertEquals("Editing of NCI Thesaurus 05.12f was completed on January 3, 2006.  Version 05.12f was December's sixth build in our development cycle.", release.getEntityDescription().getContent());
		System.out.println(release.getEntityDescription().getContent());
	}

	@Test
	public final void testGetSystemReleaseUidForDate() throws ParseException {
	      String value  = "2006-01-03 00:00:00";

	       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");      

	       Date date = formatter.parse(value); 
	       
		String release = nciHistoryDao.getSystemReleaseUidForDate(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", date);
		assertEquals("28", release);
	}

	@Test
	public final void testGetCodeListForVersion() {
		List<String> codes = nciHistoryDao.getCodeListForVersion("05.12f");
		assertNotNull(codes);
		assertTrue(codes.size() > 0);
		assertEquals(codes.size() , 1359);
	}

	@Test
	public final void testGetDateForVersion() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetVersionsForDateRange() {
		fail("Not yet implemented"); // TODO
	}

}
