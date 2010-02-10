package org.lexevs.dao.database.ibatis.codingscheme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

public class IbatisCodingSchemeDaoTest extends LexEvsDbUnitTestBase {

	@Resource
	private IbatisCodingSchemeDao ibatisCodingSchemeDao;
	
	@Test
	@Transactional
	public void testInsertCodingScheme() throws SQLException{
		CodingScheme cs = new CodingScheme();
		
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("cs Description");
		cs.setEntityDescription(ed);
		
		Text copyright = new Text();
		copyright.setContent("cs Copyright");
		cs.setCopyright(copyright);
		
		cs.setIsActive(false);
		
		cs.setOwner("cs owner");
		
		cs.setStatus("testing");
		
		cs.setEffectiveDate(effectiveDate);
		cs.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.REMOVE);
		es.setRelativeOrder(22l);
		cs.setEntryState(es);
		
		String id = ibatisCodingSchemeDao.insertCodingScheme(cs);

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[]) template.queryForObject("Select * from CodingScheme", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String csId = rs.getString(1);
				assertTrue(rs.getString(2).equals("csName"));
				assertTrue(rs.getString(3).equals("uri"));
				assertTrue(rs.getString(4).equals("1.2"));
				assertTrue(rs.getString(5).equals("csFormalName"));
				assertTrue(rs.getString(6).equals("lang"));
				assertTrue(rs.getLong(7) == 22l);
				assertTrue(rs.getString(8).equals("cs Description"));
				assertTrue(rs.getString(9).equals("cs Copyright"));
				assertTrue(rs.getBoolean(10) == false);
				assertTrue(rs.getString(11).equals("cs owner"));
				assertTrue(rs.getString(12).equals("testing"));
				assertTrue(rs.getTimestamp(13).equals(effectiveDate));
				assertTrue(rs.getTimestamp(14).equals(expirationDate));
				
				//TODO: Test with a Release GUID
				//assertTrue(rs.getString(15).equals("someReleaseGuid"));

				String csEntryState = rs.getString(16);
				
				String[] keys = new String[]{csEntryState, csId};
				return keys;
			}
		});
		
		assertEquals(id, keys[1]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertTrue(rs.getString(1) + " - " + keys[0], rs.getString(1).equals(keys[0]));
				assertTrue(rs.getString(2) + " - " + keys[1], rs.getString(2).equals(keys[1]));
				assertEquals(rs.getString(3), "CodingScheme");
				assertEquals(rs.getString(4), ChangeType.REMOVE.toString());
				assertEquals(rs.getLong(5), 22l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
	}
	
	@Test
	@Transactional
	public void testInsertURIMap() throws SQLException{
		SupportedCodingScheme cs = new SupportedCodingScheme();
		cs.setContent("supported cs");
		cs.setIsImported(true);
		cs.setLocalId("cs");
		cs.setUri("uri://test");
		
		ibatisCodingSchemeDao.insertURIMap("cs-guid", cs);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from Mapping", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "cs-guid");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME);
				assertEquals(rs.getString(4), "cs");
				assertEquals(rs.getString(5), "uri://test");
				assertEquals(rs.getString(6), "supported cs");
				assertEquals(rs.getBoolean(9), true);

				return true;
			}
		});
	}
	
	@Test
	@Transactional
	public void testInsertCodingSchemeSource() throws SQLException{
		
		Source source = new Source();
		source.setContent("a source");
		source.setSubRef("sub ref");
		source.setRole("source role");
		
		ibatisCodingSchemeDao.insertCodingSchemeSource("cs-guid", source);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from csMultiAttrib", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "cs-guid");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_SOURCE);
				assertEquals(rs.getString(4), "a source");
				assertEquals(rs.getString(5), "sub ref");
				assertEquals(rs.getString(6), "source role");

				return true;
			}
		});
	}
	
	@Test
	@Transactional
	public void testInsertCodingSchemeLocalName() throws SQLException{
		
		final String localName = "LocalName";
		
		ibatisCodingSchemeDao.insertCodingSchemeLocalName("cs-guid", localName);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from csMultiAttrib", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "cs-guid");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_LOCALNAME);
				assertEquals(rs.getString(4), localName);

				return true;
			}
		});
	}
	
	@Test
	@Transactional
	public void testGetCodingSchemeEntryState() throws SQLException{
		
		CodingScheme cs = new CodingScheme();
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		EntryState es = new EntryState();
		es.setChangeType(ChangeType.NEW);
		es.setRelativeOrder(1l);
		cs.setEntryState(es);
		
		ibatisCodingSchemeDao.insertCodingScheme(cs);
		
		CodingScheme returnedCs = ibatisCodingSchemeDao.getCodingSchemeByNameAndVersion("csName", "1.2");
		
		assertNotNull(returnedCs);
		assertNotNull(returnedCs.getEntryState());	
	}
	
	@Test
	@Transactional
	public void testInsertMappings() throws SQLException{
		
		CodingScheme cs = new CodingScheme();
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		EntryState es = new EntryState();
		es.setChangeType(ChangeType.NEW);
		es.setRelativeOrder(1l);
		cs.setEntryState(es);
		
		Mappings mappings = new Mappings();
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setContent("scs");
		scs.setUri("uri");
		scs.setLocalId("scs");
		
		SupportedSource ss = new SupportedSource();
		ss.setContent("scs");
		ss.setUri("uri");
		ss.setLocalId("id");
		
		mappings.addSupportedCodingScheme(scs);
		mappings.addSupportedSource(ss);
		
		cs.setMappings(mappings);
		
		ibatisCodingSchemeDao.insertCodingScheme(cs);
		
		CodingScheme returnedCs = ibatisCodingSchemeDao.getCodingSchemeByNameAndVersion("csName", "1.2");
		
		assertNotNull(returnedCs);
		assertNotNull(returnedCs.getMappings());	
	}
	
	@Test
	@Transactional
	public void testGetCodingSchemeLocalName() throws SQLException{
		
		CodingScheme cs = new CodingScheme();
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		cs.addLocalName("localName");
		
		ibatisCodingSchemeDao.insertCodingScheme(cs);
		
		CodingScheme returnedCs = ibatisCodingSchemeDao.getCodingSchemeByNameAndVersion("csName", "1.2");
		
		assertNotNull(returnedCs);
		assertEquals(returnedCs.getLocalNameCount(), 1);
		assertEquals(returnedCs.getLocalName(0), "localName");
	}
	
	@Test
	@Transactional
	public void testGetCodingSchemeMultipleLocalNames() throws SQLException{
		
		CodingScheme cs = new CodingScheme();
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		cs.addLocalName("localName1");
		cs.addLocalName("localName2");
		
		ibatisCodingSchemeDao.insertCodingScheme(cs);
		
		CodingScheme returnedCs = ibatisCodingSchemeDao.getCodingSchemeByNameAndVersion("csName", "1.2");
		
		assertNotNull(returnedCs);
		assertEquals(returnedCs.getLocalNameCount(), 2);
		assertTrue(ArrayUtils.contains(returnedCs.getLocalName(), "localName1"));
		assertTrue(ArrayUtils.contains(returnedCs.getLocalName(), "localName2"));
	}
	
	@Test
	@Transactional
	public void testGetCodingSchemeSource() throws SQLException{
		
		CodingScheme cs = new CodingScheme();
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		Source source = new Source();
		source.setContent("a source");
		
		cs.addSource(source);
		
		ibatisCodingSchemeDao.insertCodingScheme(cs);
		
		CodingScheme returnedCs = ibatisCodingSchemeDao.getCodingSchemeByNameAndVersion("csName", "1.2");
		
		assertNotNull(returnedCs);
		assertEquals(returnedCs.getSourceCount(), 1);
	}
	
	@Test
	@Transactional
	public void testGetCodingSchemeByNameAndVersion() throws SQLException{
		
		CodingScheme cs = new CodingScheme();
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		cs.addLocalName("localName");
		
		ibatisCodingSchemeDao.insertCodingScheme(cs);
		
		CodingScheme returnedCs = ibatisCodingSchemeDao.getCodingSchemeByNameAndVersion("csName", "1.2");
		
		assertNotNull(returnedCs);
	}
}
