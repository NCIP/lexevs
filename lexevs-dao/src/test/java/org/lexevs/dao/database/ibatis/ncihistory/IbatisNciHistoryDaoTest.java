
package org.lexevs.dao.database.ibatis.ncihistory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisEntityDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Transactional
public class IbatisNciHistoryDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis entity dao. */
	@Autowired
	private IbatisNciHistoryDao ibatisNciHistoryDao;
	
	@Test
	@Transactional
	public void testGetBaseLinesNullDates() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date = new Timestamp(new Date().getTime());

		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
	
		List<SystemRelease> systemReleases = ibatisNciHistoryDao.getBaseLines("1", null, null);
		
		assertEquals(1,systemReleases.size());
		
		SystemRelease systemRelease = systemReleases.get(0);
		
		assertEquals("basedonsomerelease", systemRelease.getBasedOnRelease() );
		assertEquals("testsystemrelease", systemRelease.getEntityDescription().getContent() );
		assertEquals("testagency",  systemRelease.getReleaseAgency() );
		assertEquals(date.getTime(), systemRelease.getReleaseDate().getTime());
		assertEquals("releaseid", systemRelease.getReleaseId() );
		assertEquals("releaseuri", systemRelease.getReleaseURI() );
	}
	
	@Test
	@Transactional
	public void testGetSystemReleaseForUri() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date = new Timestamp(new Date().getTime());
		
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
	
		SystemRelease systemRelease = ibatisNciHistoryDao.getSystemReleaseForReleaseUri("1", "releaseuri");

		assertEquals("basedonsomerelease", systemRelease.getBasedOnRelease() );
		assertEquals("testsystemrelease", systemRelease.getEntityDescription().getContent() );
		assertEquals("testagency",  systemRelease.getReleaseAgency() );
		assertEquals(date.getTime(), systemRelease.getReleaseDate().getTime());
		assertEquals("releaseid", systemRelease.getReleaseId() );
		assertEquals("releaseuri", systemRelease.getReleaseURI() );
	}
	
	
	@Test
	@Transactional
	public void testGetBaseLinesNullDatesWithTwoResults() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date = new Timestamp(new Date().getTime());
		
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', '1', 'releaseuri', 'releaseid2',  '" + date.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		List<SystemRelease> systemReleases = ibatisNciHistoryDao.getBaseLines("1", null, null);
		
		assertEquals(2,systemReleases.size());
	}
	
	@Test
	@Transactional
	public void testGetEarliestBaseline() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		Timestamp date2 = new Timestamp(10000l);
		
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', '1', 'releaseuri', 'releaseid2',  '" + date2.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		SystemRelease systemRelease = ibatisNciHistoryDao.getEarliestBaseLine("1");
		
		assertEquals("releaseid" ,systemRelease.getReleaseId());
	}
	
	@Test
	@Transactional
	public void testGetLatestBaseline() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		Timestamp date2 = new Timestamp(10000l);
		
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', '1', 'releaseuri', 'releaseid2',  '" + date2.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		SystemRelease systemRelease = ibatisNciHistoryDao.getLatestBaseLine("1");
		
		assertEquals("releaseid2" ,systemRelease.getReleaseId());
	}
	
	@Test
	@Transactional
	public void testInsertSystemRelease() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		final Timestamp date = new Timestamp(new Date().getTime());
		
		SystemRelease release = new SystemRelease();
		release.setReleaseAgency("testAgency");
		release.setReleaseURI("uri");
		release.setReleaseDate(date);
		release.setReleaseId("id");
		release.setBasedOnRelease("basedOn");
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("desc");
		release.setEntityDescription(ed);
		
		ibatisNciHistoryDao.insertSystemRelease("1", release);
		
		template.queryForObject("Select * from ncihistsystemrelease", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				int i=1;
				
				assertNotNull(rs.getString(i++));
				assertEquals("1", rs.getString(i++));
				assertEquals("id", rs.getString(i++));
				assertEquals("uri", rs.getString(i++));
				assertEquals("basedOn", rs.getString(i++));
				assertEquals(date.getTime(), rs.getTimestamp(i++).getTime());		
				assertEquals("testAgency", rs.getString(i++));
				assertEquals("desc", rs.getString(i++));
				
				return null;
			}
		});		
	}
	
	@Test
	@Transactional
	public void testGetDescendants() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date = new Timestamp(new Date().getTime());
		
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHist (ncitHistGuid, releaseGuid, entityCode, conceptName, editDate, editAction, referenceCode, referenceName)" +
				" values ('1', '123', 'C1234', 'name1', '" + date.toString() + "', 'merge', 'C3333', 'name2')");

		List<NCIChangeEvent> changeEvents = ibatisNciHistoryDao.getDescendants("1", "C1234");
		
		assertEquals(1,changeEvents.size());
		
		NCIChangeEvent event = changeEvents.get(0);

		assertEquals("C1234", event.getConceptcode());
		assertEquals("name1", event.getConceptName());
		assertEquals("C3333", event.getReferencecode());
		assertEquals(org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType.MERGE, event.getEditaction());
		assertEquals(date.getTime(), event.getEditDate().getTime());

	}
	
	@Test
	@Transactional
	public void testGetAncestors() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date = new Timestamp(new Date().getTime());
		
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHist (ncitHistGuid, releaseGuid, entityCode, conceptName, editDate, editAction, referenceCode, referenceName)" +
				" values ('1', '123', 'C1234', 'name1', '" + date.toString() + "', 'merge', 'C3333', 'name2')");

		List<NCIChangeEvent> changeEvents = ibatisNciHistoryDao.getAncestors("1", "C3333");
		
		assertEquals(1,changeEvents.size());
		
		NCIChangeEvent event = changeEvents.get(0);

		assertEquals("C1234", event.getConceptcode());
		assertEquals("name1", event.getConceptName());
		assertEquals("C3333", event.getReferencecode());
		assertEquals(org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType.MERGE, event.getEditaction());
		assertEquals(date.getTime(), event.getEditDate().getTime());

	}
	
	@Test
	@Transactional
	public void testInsertNciChangeEvent() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		final Timestamp date = new Timestamp(new Date().getTime());
		
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  NOW() , 'basedonsomerelease', 'testagency', 'testsystemrelease')");

		NCIChangeEvent event = new NCIChangeEvent();
		event.setConceptcode("C1");
		event.setConceptName("name1");
		event.setEditaction(org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType.MERGE);
		event.setEditDate(date);
		event.setReferencecode("C2");
		event.setReferencename("name2");
		
		ibatisNciHistoryDao.insertNciChangeEvent("123", event);
		
		template.queryForObject("Select * from ncihist", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				int i=1;
				
				assertNotNull(rs.getString(i++));
				assertEquals("123", rs.getString(i++));
				assertEquals("C1", rs.getString(i++));
				assertEquals("name1", rs.getString(i++));
				assertEquals("merge", rs.getString(i++));
				assertEquals(date.getTime(), rs.getTimestamp(i++).getTime());		
				assertEquals("C2", rs.getString(i++));
				assertEquals("name2", rs.getString(i++));
				
				return null;
			}
		});		

	}
	
	@Test
	@Transactional
	public void testSystemReleaseForDateBetween() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		Timestamp date2 = new Timestamp(10000l);
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', '1', 'releaseuri', 'releaseid2',  '" + date2.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		String systemReleaseUid = ibatisNciHistoryDao.getSystemReleaseUidForDate("1", new Timestamp(50l));
		
		assertEquals("1234", systemReleaseUid);
	}
	
	@Test
	@Transactional
	public void testSystemReleaseForDateEqualsFirst() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		Timestamp date2 = new Timestamp(10000l);
		
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', '1', 'releaseuri', 'releaseid2',  '" + date2.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		String systemReleaseUid = ibatisNciHistoryDao.getSystemReleaseUidForDate("1", date1);
		
		assertEquals("123", systemReleaseUid);
	}
	
	@Test
	@Transactional
	public void testSystemReleaseForDateEqualsSecond() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		Timestamp date2 = new Timestamp(10000l);
		
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', '1', 'releaseuri', 'releaseid',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', '1', 'releaseuri', 'releaseid2',  '" + date2.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		String systemReleaseUid = ibatisNciHistoryDao.getSystemReleaseUidForDate("1", date2);
		
		assertEquals("1234", systemReleaseUid);
	}
	
	@Test
	@Transactional
	public void testGetEditActionListWithUriSourceCode() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1', 'csuri', 'releaseuri', 'releaseid',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHist (ncithistguid, releaseguid, entitycode, conceptname, editaction, editdate, referencecode, referencename)" +
				" values ('1', '1', 'sc1', 'name1', 'merge', '" + date1.toString() + "', 'rc1', 'name2')");
	
		List<NCIChangeEvent> event = ibatisNciHistoryDao.getEditActionList("csuri", "sc1", "releaseuri");
		
		assertEquals(1, event.size());
		
		assertEquals("sc1", event.get(0).getConceptcode());
	}
	
	@Test
	@Transactional
	public void testGetEditActionListWithUriReferenceCode() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1', 'csuri', 'releaseuri', 'releaseid',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHist (ncithistguid, releaseguid, entitycode, conceptname, editaction, editdate, referencecode, referencename)" +
				" values ('1', '1', 'sc1', 'name1', 'merge', '" + date1.toString() + "', 'rc1', 'name2')");
	
		List<NCIChangeEvent> event = ibatisNciHistoryDao.getEditActionList("csuri", "rc1", "releaseuri");
		
		assertEquals(1, event.size());
		
		assertEquals("sc1", event.get(0).getConceptcode());
	}
	
	@Test
	@Transactional
	public void testGetEditActionListWithUriSourceAndReferenceCode() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1', 'csuri', 'releaseuri', 'releaseid',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHist (ncithistguid, releaseguid, entitycode, conceptname, editaction, editdate, referencecode, referencename)" +
				" values ('1', '1', 'sc1', 'name1', 'merge', '" + date1.toString() + "', 'rc1', 'name2')");
		
		template.execute("insert into nciHist (ncithistguid, releaseguid, entitycode, conceptname, editaction, editdate, referencecode, referencename)" +
				" values ('2', '1', 'sc2', 'name2', 'merge', '" + date1.toString() + "', 'sc1', 'name2')");
	
		List<NCIChangeEvent> event = ibatisNciHistoryDao.getEditActionList("csuri", "sc1", "releaseuri");
		
		assertEquals(2, event.size());
	}
	
	@Test
	@Transactional
	public void testSystemReleaseForDateWithThree() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(1l);
		Timestamp date2 = new Timestamp(10000l);
		Timestamp date3 = new Timestamp(1000000l);
		
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('12', '1', 'releaseuri1', 'releaseid1',  '" + date1.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('2', '1', 'releaseuri2', 'releaseid2',  '" + date2.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeUri, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('3', '1', 'releaseuri3', 'releaseid3',  '" + date3.toString() + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
		
		Date searchDate = new Timestamp(100000l);
		
		String systemReleaseUid = ibatisNciHistoryDao.getSystemReleaseUidForDate("1", searchDate);
		
		assertEquals("3", systemReleaseUid);
	}
	
}