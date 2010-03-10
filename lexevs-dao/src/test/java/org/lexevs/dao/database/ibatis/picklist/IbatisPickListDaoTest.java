package org.lexevs.dao.database.ibatis.picklist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.valueDomains.PickListDefinition;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class IbatisPickListDaoTest extends LexEvsDbUnitTestBase {

	@Autowired
	private IbatisPickListDao ibatisPickListDao;
	
	@Test
	public void testPickListDaoUp() {
		assertNotNull(this.ibatisPickListDao);
	}
	
	@Test
	public void testInsertPickListDefinition() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		PickListDefinition def = new PickListDefinition();
		def.setCompleteDomain(false);
		def.setDefaultEntityCodeNamespace("ns");
		def.addDefaultPickContext("context");
		def.setDefaultSortOrder("asc");
		def.setEffectiveDate(effectiveDate);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("description");
		def.setEntityDescription(ed);
		def.setExpirationDate(expirationDate);
		def.setIsActive(true);
		def.setRepresentsValueDomain("vd");
		def.setOwner("owner");
		def.setPickListId("plid");
		
		Source source = new Source();
		source.setContent("source");
		def.addSource(source);
		def.setStatus("testing");
		
		ibatisPickListDao.insertPickListDefinition("releaseuri", def);
		
		
		int count = template.queryForInt("Select count(*) from vdpicklist");
		assertEquals(1, count);
		
		template.queryForObject("Select * from vdpicklist", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals("plid", rs.getString(2));
				
				return null;
			}		
		});
	}
	
	@Test
	public void testGetPickListIds() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl1', 'id1', 'vd')");
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl2', 'id2', 'vd')");
		
		List<String> ids = this.ibatisPickListDao.getPickListIds();
		
		assertEquals(2, ids.size());
		assertTrue(ids.contains("id1"));
		assertTrue(ids.contains("id2"));
	}
	
	@Test
	public void testGetPickListDefinitionById() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl1', 'id1', 'vd')");
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl2', 'id2', 'vd')");
		
		PickListDefinition definition = this.ibatisPickListDao.getPickListDefinitionById("id1");
		
		assertEquals("id1", definition.getPickListId());
	}
	
	@Test
	public void testGetGuidFromPickListId() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl1', 'id1', 'vd')");
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl2', 'id2', 'vd')");
		
		String guid = this.ibatisPickListDao.getGuidFromPickListId("id2");
		
		assertEquals("pl2", guid);
	}
}
