package org.lexevs.dao.database.ibatis.picklist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
		
		ibatisPickListDao.insertPickListDefinition("1234", def);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		int count = template.queryForInt("Select * from vdpicklist");
		assertEquals(1, count);
		
		template.queryForObject("Select * from vdpicklist", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				/*
				String id = rs.getString(1);
				assertTrue(rs.getString(2).equals(csId));
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
				*/
				
				return null;
			}
			
		});
	}
}
