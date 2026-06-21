
package org.lexevs.dao.database.ibatis.picklist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.valueSets.PickListDefinition;
import org.junit.Test;
import org.lexevs.dao.database.ibatis.valuesets.IbatisPickListDao;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisPickListDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Transactional
public class IbatisPickListDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis pick list dao. */
	@Autowired
	private IbatisPickListDao ibatisPickListDao;
	
	/**
	 * Test pick list dao up.
	 */
	@Test
	public void testPickListDaoUp() {
		assertNotNull(this.ibatisPickListDao);
	}
	
	/**
	 * Test insert pick list definition.
	 */
	@Test
	public void testInsertPickListDefinition() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		PickListDefinition def = new PickListDefinition();
		def.setCompleteSet(false);
		def.setDefaultEntityCodeNamespace("ns");
		def.addDefaultPickContext("context");
		def.setDefaultSortOrder("asc");
		def.setEffectiveDate(effectiveDate);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("description");
		def.setEntityDescription(ed);
		def.setExpirationDate(expirationDate);
		def.setIsActive(true);
		def.setRepresentsValueSetDefinition("vd");
		def.setOwner("owner");
		def.setPickListId("plid");
		
		Source source = new Source();
		source.setContent("source");
		def.addSource(source);
		def.setStatus("testing");
		
		ibatisPickListDao.insertPickListDefinition(def, "releaseuri", null);
		
		
		int count = template.queryForObject("Select count(*) from vspicklist", Integer.class).intValue();
		assertEquals(1, count);
		
		template.queryForObject("Select * from vspicklist", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals("plid", rs.getString(2));
				
				return null;
			}		
		});
	}
	
	/**
	 * Test get pick list ids.
	 */
	@Test
	public void testGetPickListIds() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vsPickList (vsPickListGuid, pickListId, representsvaluesetdefinition) values ('1', 'id1', 'vd')");
		template.execute("insert into vsPickList (vsPickListGuid, pickListId, representsvaluesetdefinition) values ('2', 'id2', 'vd')");
		
		List<String> ids = this.ibatisPickListDao.getPickListIds();
		
		assertEquals(2, ids.size());
		assertTrue(ids.contains("id1"));
		assertTrue(ids.contains("id2"));
	}
	
	/**
	 * Test get pick list definition by id.
	 */
	@Test
	public void testGetPickListDefinitionById() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vsPickList (vsPickListGuid, pickListId, representsvaluesetdefinition) values ('1', 'id1', 'vd')");
		template.execute("insert into vsPickList (vsPickListGuid, pickListId, representsvaluesetdefinition) values ('2', 'id2', 'vd')");
		
		PickListDefinition definition = this.ibatisPickListDao.getPickListDefinitionById("id1");
		
		assertEquals("id1", definition.getPickListId());
	}
	
	/**
	 * Test get guid from pick list id.
	 */
	@Test
	public void testGetGuidFromPickListId() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vsPickList (vsPickListGuid, pickListId, representsvaluesetdefinition) values ('1', 'id1', 'vd')");
		template.execute("insert into vsPickList (vsPickListGuid, pickListId, representsvaluesetdefinition) values ('2', 'id2', 'vd')");
		
		String guid = this.ibatisPickListDao.getPickListGuidFromPickListId("id2");
		
		assertEquals("2", guid);
	}
}