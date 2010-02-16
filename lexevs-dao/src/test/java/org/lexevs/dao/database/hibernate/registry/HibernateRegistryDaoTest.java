package org.lexevs.dao.database.hibernate.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDaoTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration
public class HibernateRegistryDaoTest extends LexEvsDaoTestBase {

	@Resource
	private HibernateRegistryDao hibernateRegistryDao;
	
	@Resource
	private DataSource dataSource;
	
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
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setActivationDate(activationDate);
		entry.setBaseRevision("1");
		entry.setDbName("db name");
		entry.setDbSchemaDescription("description");
		entry.setDbSchemaVersion("1.1");
		entry.setDbUri("dbUri://");
		entry.setDeactivationDate(deActivationDate);
		entry.setFixedAtRevision("2");
		entry.setLastUpdateDate(lastUpdateDate);
		entry.setLocked(true);
		entry.setPrefix("prefix");
		entry.setResourceUri("uri://");
		entry.setResourceVersion("v1");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		
		
		hibernateRegistryDao.insertRegistryEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		
		template.queryForObject("Select * from registry", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "uri://");
				assertEquals(rs.getString(3), "v1");
				assertEquals(rs.getObject(4), ResourceType.CODING_SCHEME.toString());
				assertEquals(rs.getString(5), "dbUri://");
				assertEquals(rs.getString(6), "db name");
				assertEquals(rs.getString(7), "prefix");
				assertEquals(rs.getString(8), CodingSchemeVersionStatus.ACTIVE.toString());
				assertEquals(rs.getString(9), "tag");
				assertEquals(rs.getTimestamp(10), lastUpdateDate);
				assertEquals(rs.getTimestamp(11), activationDate);
				assertEquals(rs.getTimestamp(12), deActivationDate);
				assertEquals(rs.getString(13), "1");
				assertEquals(rs.getString(14), "2");
				assertEquals(rs.getBoolean(15), true);
				assertEquals(rs.getString(16), "1.1");
				assertEquals(rs.getString(17), "description");
			
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
		entry.setResourceType(ResourceType.CODING_SCHEME);
		
		hibernateRegistryDao.insertRegistryEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		RegistryEntry foundEntry = hibernateRegistryDao.getRegistryEntryForUriAndVersion("uri", "version");
		
		assertNotNull(foundEntry);
	}
	
	@Test
	@Transactional
	public void testChangeTag(){
		RegistryEntry entry = new RegistryEntry();
		entry.setPrefix("prefix");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		entry.setResourceUri("uri2");
		entry.setResourceVersion("version");
		entry.setResourceType(ResourceType.CODING_SCHEME);

		hibernateRegistryDao.insertRegistryEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		hibernateRegistryDao.updateTag("uri2", "version", "new tag");
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		RegistryEntry foundEntry = hibernateRegistryDao.getRegistryEntryForUriAndVersion("uri2", "version");
		
		assertEquals("new tag", foundEntry.getTag());
	}
}
