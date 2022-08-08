
package org.lexevs.dao.database.hibernate.registry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
//import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class HibernateRegistryDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Transactional(readOnly=false)
public class HibernateRegistryDaoTest extends LexEvsDbUnitTestBase {

	/** The hibernate registry dao. */
	@Resource
	private HibernateRegistryDao hibernateRegistryDao;
	
	/** The data source. */
	@Resource
	private DataSource dataSource;
	
	
	/**
	 * Test insert coding scheme entry.
	 */
	@Test
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
		entry.setIsLocked(true);
		entry.setPrefix("prefix");
		entry.setResourceUri("uri://");
		entry.setResourceVersion("v1");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		entry.setStagingPrefix("staging-prefix");
			
		hibernateRegistryDao.insertRegistryEntry(entry);
		
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
				assertEquals(rs.getString(8), "staging-prefix");
				assertEquals(rs.getString(9), CodingSchemeVersionStatus.ACTIVE.toString());
				assertEquals(rs.getString(10), "tag");
				assertEquals(rs.getTimestamp(11), lastUpdateDate);
				assertEquals(rs.getTimestamp(12), activationDate);
				assertEquals(rs.getTimestamp(13), deActivationDate);
				assertEquals(rs.getString(14), "1");
				assertEquals(rs.getString(15), "2");
				assertEquals("1", rs.getString(16));
				assertEquals(rs.getString(17), "1.1");
				assertEquals(rs.getString(18), "description");
			
				return true;
			}
		});
	}
	
	/**
	 * Test get coding scheme entry.
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	@Test
	@Transactional
	public void testGetCodingSchemeEntry() throws LBParameterException{
		RegistryEntry entry = new RegistryEntry();
		entry.setPrefix("prefix");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		entry.setResourceUri("uri");
		entry.setResourceVersion("version");
		entry.setResourceType(ResourceType.CODING_SCHEME);
		
		hibernateRegistryDao.insertRegistryEntry(entry);
		
		RegistryEntry foundEntry = hibernateRegistryDao.getRegistryEntryForUriAndVersion("uri", "version");
		
		assertNotNull(foundEntry);
	}
	
	/**
	 * Test change tag.
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	@Test
	@Transactional
	public void testChangeTag() throws LBParameterException{
		RegistryEntry entry = new RegistryEntry();
		entry.setPrefix("prefix");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		entry.setResourceUri("uri2");
		entry.setResourceVersion("version");
		entry.setResourceType(ResourceType.CODING_SCHEME);

		hibernateRegistryDao.insertRegistryEntry(entry);
		
		entry.setTag("new tag");
		hibernateRegistryDao.updateRegistryEntry(entry);
		
		RegistryEntry foundEntry = hibernateRegistryDao.getRegistryEntryForUriAndVersion("uri2", "version");
		
		assertEquals("new tag", foundEntry.getTag());
	}
}