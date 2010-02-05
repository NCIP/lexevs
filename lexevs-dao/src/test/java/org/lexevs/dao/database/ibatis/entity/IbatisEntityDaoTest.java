package org.lexevs.dao.database.ibatis.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
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
public class IbatisEntityDaoTest extends LexEvsDbUnitTestBase {

	@Autowired
	private IbatisEntityDao ibatisEntityDao;
	
	@Test
	public void insertEntity(){
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		entity.setEntityDescription(Constructors.createEntityDescription("a description"));
		entity.addEntityType("type");
		
		Source owner = new Source();
		owner.setContent("entity owner");
		entity.setOwner(owner);
		
		entity.setStatus("testing");
		
		entity.setEffectiveDate(effectiveDate);
		entity.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.DEPENDENT);
		es.setRelativeOrder(23l);
		entity.setEntryState(es);
		
		final String id = ibatisEntityDao.insertEntity("fake-codingScheme-guid", entity);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[])template.queryForObject("Select * from Entity", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String id = rs.getString(1);
				assertTrue(rs.getString(2).equals("fake-codingScheme-guid"));
				assertTrue(rs.getString(3).equals("code"));
				assertTrue(rs.getString(4).equals("namespace"));
				assertTrue(rs.getBoolean(5) == true);
				assertTrue(rs.getBoolean(6) == true);
				assertTrue(rs.getString(7).equals("a description"));
				assertTrue(rs.getBoolean(8) == false);
				assertTrue(rs.getString(9).equals("entity owner"));
				assertTrue(rs.getString(10).equals("testing"));
				assertTrue(rs.getTimestamp(11).equals(effectiveDate));
				assertTrue(rs.getTimestamp(12).equals(expirationDate));
				
				String entryStateId = rs.getString(13);
				
				String[] keys = new String[]{id, entryStateId};
				return keys;
			}
		});
		
		assertEquals(id,keys[0]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), keys[1]);
				assertEquals(rs.getString(2), keys[0]);
				assertEquals(rs.getString(3), "Entity");
				assertEquals(rs.getString(4), ChangeType.DEPENDENT.toString());
				assertEquals(rs.getLong(5), 23l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
		
		template.queryForObject("Select * from EntityType", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), id);
				assertEquals(rs.getString(2), "type");
				
				return null;
			}
		});
	}
}
