package org.lexevs.dao.database.ibatis.association;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class IbatisAssociationDaoTest extends LexEvsDbUnitTestBase {
	
	@Resource
	private IbatisAssociationDao ibatisAssociationDao;
	
	@Test
	public void testInsertRelations() throws SQLException{
		
		Relations relations = new Relations();
		relations.setContainerName("container name");
		relations.setEntityDescription(Constructors.createEntityDescription("a description"));
	
		
		ibatisAssociationDao.insertRelations("cs-guid", relations);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from relation", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "cs-guid");
				assertEquals(rs.getString(3), "container name");
				assertEquals(rs.getString(9), "a description");

				return true;
			}
		});
	}

	@Test
	public void testInsertAssociationSource() throws SQLException {
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode("s-code");
		source.setSourceEntityCodeNamespace("s-ns");
		
		AssociationTarget target = new AssociationTarget();
		target.setAssociationInstanceId("aii");
		target.setTargetEntityCode("t-code");
		target.setTargetEntityCodeNamespace("t-ns");
		target.setIsDefining(true);
		target.setIsInferred(false);
		target.setIsActive(true);
		
		source.addTarget(target);
		
		Source owner = new Source();
		owner.setContent("source owner");
		target.setOwner(owner);
		
		target.setStatus("testing");
		
		target.setEffectiveDate(effectiveDate);
		target.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.DEPENDENT);
		es.setRelativeOrder(23l);
		target.setEntryState(es);
	
		
		ibatisAssociationDao.insertAssociationSource("cs-id", "ap-id", source);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from entityassnstoentity", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "ap-id");
				assertEquals(rs.getString(3), "s-code");
				assertEquals(rs.getString(4), "s-ns");
				assertEquals(rs.getString(5), "t-code");
				assertEquals(rs.getString(6), "t-ns");
				assertEquals(rs.getString(7), "aii");
				assertEquals(rs.getBoolean(8), true);
				assertEquals(rs.getBoolean(9), false);
				assertEquals(rs.getBoolean(10), true);
				assertEquals(rs.getString(11), "source owner");
				assertEquals(rs.getString(12), "testing");
				assertEquals(rs.getTimestamp(13), effectiveDate);
				assertEquals(rs.getTimestamp(14), expirationDate);
				
				return true;
			}
		});
		
		//check to make sure it inserted the EntryState -- this will
		//throw an error if there is nothing in the EntryState table.
		assertEquals(1, template.queryForObject("Select count(*) from entryState", Integer.class));
	}

}
