package org.lexevs.dao.database.ibatis.tablemetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class IbatisTableMetadataDaoTest extends LexEvsDbUnitTestBase {

	@Autowired
	private IbatisTableMetadataDao ibatisTableMetadataDao;
	
	@Before
	public void truncateTable(){
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("delete from lexgridtablemetadata");
	}
	
	@Test
	public void testInsertVersionAndDescription(){
		ibatisTableMetadataDao.insertVersionAndDescription("1.1", "a test description");
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.queryForObject("Select * from lexgridtablemetadata", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertTrue(rs.getString(1).equals("1.1"));
				assertTrue(rs.getString(2).equals("a test description"));
				
				return null;
			}
		});
	}
	
	@Test
	public void testGetVersion(){
		ibatisTableMetadataDao.insertVersionAndDescription("1.3", "a test description");
		
		assertEquals(ibatisTableMetadataDao.getVersion(), "1.3");
	}
	
	@Test
	public void testGetDescription(){
		ibatisTableMetadataDao.insertVersionAndDescription("1.4", "another description");
		
		assertEquals(ibatisTableMetadataDao.getDescription(), "another description");
	}
}
