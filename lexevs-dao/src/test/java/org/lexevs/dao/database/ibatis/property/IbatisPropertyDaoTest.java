package org.lexevs.dao.database.ibatis.property;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.ibatis.entity.IbatisEntityDao;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class IbatisPropertyDaoTest extends LexEvsDbUnitTestBase {

	@Autowired
	private IbatisPropertyDao ibatisPropertyDao;
	
	@Autowired
	private IbatisEntityDao ibatisEntityDao;
	
	@Test
	public void insertPresentation(){
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		Presentation property = new Presentation();
		property.setPropertyId("pId");
		property.setPropertyName("propName");
		property.setPropertyType("propType");
		property.setLanguage("lang");
		property.setIsActive(true);
		property.setIsPreferred(false);
		property.setMatchIfNoContext(true);
		property.setDegreeOfFidelity("DOF");
		property.setRepresentationalForm("repForm");
		
		property.setOwner("property owner");
		
		property.setStatus("testing");
		Text text = new Text();
		text.setContent("prop value");
		text.setDataType("format");
		property.setValue(text);
		
		property.setEffectiveDate(effectiveDate);
		property.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.VERSIONABLE);
		es.setRelativeOrder(23l);
		property.setEntryState(es);
		
		String id = ibatisPropertyDao.insertProperty("fake-cs-id", "fake-entityCode-id", PropertyType.ENTITY, property);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[])template.queryForObject("Select * from Property", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String id = rs.getString(1);
				assertEquals(rs.getString(2),"fake-entityCode-id");
				assertEquals(rs.getString(3),"entity");
				assertEquals(rs.getString(4),"pId");
				assertEquals(rs.getString(5),"propType");
				assertEquals(rs.getString(6),"propName");
				assertEquals(rs.getString(7),"lang");
				assertEquals(rs.getString(8),"format");
				assertTrue(rs.getBoolean(9) == false);
				assertTrue(rs.getBoolean(10) == true);
				assertEquals(rs.getString(11),"DOF");
				assertEquals(rs.getString(12),"repForm");
				assertEquals(rs.getString(13),"prop value");
				assertTrue(rs.getBoolean(14) == true);
				assertTrue(rs.getString(15).equals("property owner"));
				assertTrue(rs.getString(16).equals("testing"));
				assertTrue(rs.getTimestamp(17).equals(effectiveDate));
				assertTrue(rs.getTimestamp(18).equals(expirationDate));
				String entryStateId = rs.getString(19);
				
				String[] keys = new String[]{id, entryStateId};
				return keys;
			}
		});
		
		assertEquals(id,keys[0]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), keys[1]);
				assertEquals(rs.getString(2), keys[0]);
				assertEquals(rs.getString(3), "Property");
				assertEquals(rs.getString(4), ChangeType.VERSIONABLE.toString());
				assertEquals(rs.getLong(5), 23l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
	}
	
	@Test
	public void insertGenericProperty(){
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		Property property = new Property();
		property.setPropertyId("pId");
		property.setPropertyName("propName");
		property.setPropertyType("propType");
		property.setLanguage("lang");
		property.setIsActive(true);
		
		property.setOwner("property owner");
		
		property.setStatus("testing");
		Text text = new Text();
		text.setContent("prop value");
		text.setDataType("format");
		property.setValue(text);
		
		property.setEffectiveDate(effectiveDate);
		property.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.VERSIONABLE);
		es.setRelativeOrder(23l);
		property.setEntryState(es);
		
		String id = ibatisPropertyDao.insertProperty("fake-cs-id", "fake-entityCode-id", PropertyType.ENTITY, property);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[])template.queryForObject("Select * from Property", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String id = rs.getString(1);
				assertEquals(rs.getString(2),"fake-entityCode-id");
				assertEquals(rs.getString(3),"entity");
				assertEquals(rs.getString(4),"pId");
				assertEquals(rs.getString(5),"propType");
				assertEquals(rs.getString(6),"propName");
				assertEquals(rs.getString(7),"lang");
				assertEquals(rs.getString(8),"format");
				assertNull(rs.getString(11));
				assertNull(rs.getString(12));
				assertEquals(rs.getString(13),"prop value");
				assertTrue(rs.getBoolean(14) == true);
				assertTrue(rs.getString(15).equals("property owner"));
				assertTrue(rs.getString(16).equals("testing"));
				assertTrue(rs.getTimestamp(17).equals(effectiveDate));
				assertTrue(rs.getTimestamp(18).equals(expirationDate));
				String entryStateId = rs.getString(19);
				
				String[] keys = new String[]{id, entryStateId};
				return keys;
			}
		});
		
		assertEquals(id,keys[0]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), keys[1]);
				assertEquals(rs.getString(2), keys[0]);
				assertEquals(rs.getString(3), "Property");
				assertEquals(rs.getString(4), ChangeType.VERSIONABLE.toString());
				assertEquals(rs.getLong(5), 23l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
	}
	
	@Test
	public void insertPropertyQualifier(){
		PropertyQualifier qual = new PropertyQualifier();
		qual.setPropertyQualifierName("qualName");
		qual.setPropertyQualifierType("qualType");
		Text text = new Text();
		text.setContent("qual text");
		qual.setValue(text);
		
		ibatisPropertyDao.insertPropertyQualifier(null, null, "prop-id", qual);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "prop-id");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_QUALIFIER);
				assertEquals(rs.getString(4), "qualName");
				assertEquals(rs.getString(5), "qual text");
				assertNull(rs.getString(6));
				assertNull(rs.getString(7));
				assertNull(rs.getString(8));
				
				return null;
			}
		});
	}
	
	@Test
	public void insertPropertySource(){
		Source source = new Source();
		source.setContent("test source");
		source.setSubRef("test subref");
		source.setRole("test role");
		
		ibatisPropertyDao.insertPropertySource(null, null, "prop-id", source);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "prop-id");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_SOURCE);
				assertEquals(rs.getString(4), SQLTableConstants.TBLCOLVAL_SOURCE);
				assertEquals(rs.getString(5), "test source");
				assertEquals(rs.getString(6), "test subref");
				assertEquals(rs.getString(7), "test role");
				assertNull(rs.getString(8));
				
				return null;
			}
		});
	}
	
	@Test
	public void insertPropertyUsageContext(){
		String usageContext = "test usageContext";
		
		ibatisPropertyDao.insertPropertyUsageContext(null, null, "prop-id", usageContext);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "prop-id");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				assertEquals(rs.getString(4), SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				assertEquals(rs.getString(5), "test usageContext");
				assertNull(rs.getString(6));
				assertNull(rs.getString(7));
				assertNull(rs.getString(8));
				
				return null;
			}
		});
	}
	
	@Test
	public void insertPropertyLink(){
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		entity.addEntityType("type");
		
		String entityId = ibatisEntityDao.insertEntity("cs-id", entity);
		
		Property prop1 = new Property();
		prop1.setPropertyName("name");
		prop1.setPropertyType("type");
		prop1.setPropertyId("1");
		Text text1 = new Text();
		text1.setContent("text");
		prop1.setValue(text1);
		
		Property prop2 = new Property();
		prop2.setPropertyName("name");
		prop2.setPropertyType("type");
		prop2.setPropertyId("2");
		Text text2 = new Text();
		text2.setContent("text");
		prop2.setValue(text2);
		
		ibatisPropertyDao.insertProperty("cs-id", entityId, PropertyType.ENTITY, prop1);
		ibatisPropertyDao.insertProperty("cs-id", entityId, PropertyType.ENTITY, prop2);
		
		PropertyLink link = new PropertyLink();
		link.setSourceProperty("1");
		link.setPropertyLink("link");
		link.setTargetProperty("2");
		
		ibatisPropertyDao.insertPropertyLink("cs-id", entityId, link);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.queryForObject("Select * from propertylinks", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "1");
				assertEquals(rs.getString(3), "link");
				assertEquals(rs.getString(4), "2");
			
				return null;
			}
		});
	}
}
