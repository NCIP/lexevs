package org.lexevs.dao.database.hibernate.registry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.CodingSchemeEntry;
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
		CodingSchemeEntry entry = new CodingSchemeEntry();
		entry.setPrefix("prefix");
		entry.setStatus("status");
		entry.setTag("tag");
		entry.setUri("uri");
		entry.setVersion("version");
		
		hibernateRegistryDao.insertCodingSchemeEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from codingschemeentry", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "uri");
				assertEquals(rs.getString(3), "prefix");
				assertEquals(rs.getString(4), "status");
				assertEquals(rs.getString(5), "tag");
				assertEquals(rs.getString(6), "version");

				return true;
			}
		});
	}
	
	@Test
	@Transactional
	public void testGetCodingSchemeEntry(){
		CodingSchemeEntry entry = new CodingSchemeEntry();
		entry.setPrefix("prefix");
		entry.setStatus("status");
		entry.setTag("tag");
		entry.setUri("uri");
		entry.setVersion("version");
		
		hibernateRegistryDao.insertCodingSchemeEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		CodingSchemeEntry foundEntry = hibernateRegistryDao.getCodingSchemeEntryForUriAndVersion("uri", "version");
		
		assertNotNull(foundEntry);
	}
	
	@Test
	@Transactional
	public void testChangeTag(){
		CodingSchemeEntry entry = new CodingSchemeEntry();
		entry.setPrefix("prefix");
		entry.setStatus("status");
		entry.setTag("tag");
		entry.setUri("uri");
		entry.setVersion("version");

		hibernateRegistryDao.insertCodingSchemeEntry(entry);
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		hibernateRegistryDao.updateTag("uri", "version", "new tag");
		
		hibernateRegistryDao.getHibernateTemplate().flush();
		
		
	}
}
