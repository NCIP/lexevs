package org.lexevs.dao.database.hibernate.registry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration
public class HibernateRegistryDaoTest extends LexEvsDbUnitTestBase {

	@Resource
	private HibernateRegistryDao hibernateRegistryDao;
	
	@Test
	@Transactional
	public void testGetLastUpdateTime(){
		Date updateTime = hibernateRegistryDao.getLastUpdateTime();
		assertNotNull(updateTime);
	}
	
	@Test
	@Transactional
	public void testDetLastUsedDbIdentifier(){
		String dbId = hibernateRegistryDao.getLastUsedDbIdentifier();
		assertEquals("aaa", dbId);
	}
	
	@Test
	@Transactional
	public void testDetLastUsedHistoryIdentifier(){
		String historyId = hibernateRegistryDao.getLastUsedHistoryIdentifier();
		assertEquals("aaa", historyId);
	}
	
	@Test
	@Transactional
	public void testInsertCodingSchemeEntry(){
		final Timestamp activationDate = new Timestamp(1l);
		final Timestamp deActivationDate = new Timestamp(2l);
		final Timestamp lastUpdateDate = new Timestamp(3l);
		
		RegistryEntry entry = new RegistryEntry();
		entry.setActivationDate(activationDate);
		entry.setBaseRevision("1");
		entry.setDbName("db name");
		entry.setDbSchemaDescription("description");
		entry.setDbSchemaVersion("1.1");
		entry.setDbUrl("url://");
		entry.setDeactivationDate(deActivationDate);
		entry.setFixedAtRevision("2");
		entry.setLastUpdateDate(lastUpdateDate);
		entry.setLocked(true);
		entry.setPrefix("prefix");
		entry.setResourceType("type");
		entry.setResourceUri("uri://");
		entry.setResourceVersion("v1");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		
		
		hibernateRegistryDao.insertCodingSchemeEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from registry", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getTimestamp(2), activationDate);
				assertEquals(rs.getString(3), "1");
				assertEquals(rs.getString(4), "db name");
				assertEquals(rs.getString(5), "description");
				assertEquals(rs.getString(6), "1.1");
				assertEquals(rs.getString(7), "url://");
				assertEquals(rs.getTimestamp(8), deActivationDate);
				assertEquals(rs.getString(9), "2");
				assertEquals(rs.getBoolean(10), true);
				assertEquals(rs.getTimestamp(11), lastUpdateDate);
				assertEquals(rs.getString(12), "prefix");
				assertEquals(rs.getString(13), "type");
				assertEquals(rs.getString(14), "uri://");
				assertEquals(rs.getString(15), "v1");
				assertEquals(rs.getString(16), CodingSchemeVersionStatus.ACTIVE.toString());
				assertEquals(rs.getString(17), "tag");
				
				return true;
			}
		});
	}
	
	@Test
	@Transactional
	public void testGetCodingSchemeEntry(){
		RegistryEntry entry = new RegistryEntry();
		entry.setPrefix("prefix");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		entry.setResourceUri("uri");
		entry.setResourceVersion("version");
		
		hibernateRegistryDao.insertCodingSchemeEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		RegistryEntry foundEntry = hibernateRegistryDao.getCodingSchemeEntryForUriAndVersion("uri", "version");
		
		assertNotNull(foundEntry);
	}
	
	@Test
	@Transactional
	public void testChangeTag(){
		RegistryEntry entry = new RegistryEntry();
		entry.setPrefix("prefix");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		entry.setResourceUri("uri");
		entry.setResourceVersion("version");

		hibernateRegistryDao.insertCodingSchemeEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		hibernateRegistryDao.updateTag("uri", "version", "new tag");
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		
	}
}
