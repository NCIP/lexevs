package org.lexevs.dao.database.ibatis.versions;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class IbatisVersionsDaoTest extends LexEvsDbUnitTestBase {

	@Autowired
	private IbatisVersionsDao ibatisVersionsDao;
	
	@Test
	public void insertEntryState(){
		EntryState es = new EntryState();
		es.setChangeType(ChangeType.REMOVE);
		es.setRelativeOrder(24l);
		es.setContainingRevision("containingRevision");
		es.setPrevRevision("previousRevision");
		
		
		ibatisVersionsDao.insertEntryState(
				"csName",
				"csVersion",
				"entryStateId", 
				"entryId", 
				"entryType", 
				"previousEntryStateId", 
				es);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), "entryStateId");
				assertEquals(rs.getString(2), "entryId");
				assertEquals(rs.getString(3), "entryType");
				assertEquals(rs.getString(4), ChangeType.REMOVE.toString());
				assertEquals(rs.getLong(5), 24l);
				assertEquals(rs.getString(6), "containingRevision");
				assertEquals(rs.getString(7), "previousRevision");
				assertEquals(rs.getString(8), "previousEntryStateId");
							
				return null;
			}
		});
	}
}
