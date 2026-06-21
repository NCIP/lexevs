
package org.lexevs.dao.database.ibatis.property;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.constants.classifier.property.PropertyTypeClassifier;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.Classifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
//import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The Class IbatisPropertyDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
//@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(propagation= Propagation.REQUIRED,readOnly=false)
public class IbatisPropertyDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	/** The ibatis property dao. */
	@Autowired
	private IbatisPropertyDao ibatisPropertyDao;

    @Test
    public void getEntryStateUId() {
        String entryId = ibatisPropertyDao.getEntryStateUId("2003", "2231");
        assertEquals("Entry wrong", "2232", entryId);
    }

    @Test
    public void testGetPropertiesOfParents() {
        List<String> parentUids = new ArrayList<String>();
        parentUids.add("2183");
        List<Property> properties = ibatisPropertyDao.getPropertiesOfParents("2003", parentUids);
        assertNotNull("properties null", properties);
        assertTrue("properties empty", properties.size()>0);
    }

    @Test
    public void getPropertyByUid() {
        Property property= ibatisPropertyDao.getPropertyByUid("2003", "2195");
        assertNotNull("property null",property);
        assertEquals("property wrong", "label",property.getPropertyName());
    }

    @Test
    public void testGetPropertiesOfParents1() {
        List<String> propertyNames = new ArrayList<String>();
        propertyNames.add("Preferred_Name");
        List<String> propertyTypes = new ArrayList<String>();
        propertyTypes.add("presentation");
        List<String> parentUids = new ArrayList<String>();
        parentUids.add("REPLACE");
        List<Property> properties=ibatisPropertyDao.getPropertiesOfParents("2003", propertyNames, propertyTypes,parentUids);
        assertNotNull("properties null", properties);
    }

    @Test
    public void getAllPropertiesOfParent() {
        List<String> parentUids = new ArrayList<String>();
        parentUids.add("REPLACE");
        List<Property> properties = ibatisPropertyDao.getPropertiesOfParents("2003", parentUids);
        assertNotNull("properties null", properties);
    }

    @Test
    public void getAllHistoryPropertyUidsOfParentByRevisionId() {
        //TODO load or create a testable revision
        List<String> historyIds = ibatisPropertyDao.getAllHistoryPropertyUidsOfParentByRevisionId("2003", "REPLACE", "REPLACE");
    }

    @Test
    public void getHistoryPropertyByRevisionId() {
        //TODO load or create a testable revision
        Property historyProperty = ibatisPropertyDao.getHistoryPropertyByRevisionId("2003", "2197", "REPLACE");
    }

    @Test
    public void doGetPropertyMultiAttrib() {
        Class<Source> clz = Source.class;
        List multiAttrib = ibatisPropertyDao.doGetPropertyMultiAttrib("lbaaad", "2188", clz);
        assertNotNull("multiattrib null", multiAttrib);

    }

    @Test
    public void getPropertyTypeString() {
        Property property = new Property();
        property.setPropertyId("P0006");
        String propertyType = ibatisPropertyDao.getPropertyTypeString(property);
        assertEquals("propertyType wrong", "property",propertyType);
    }

    @Test
    public void getPropertyUIdFromParentUIdAndPropId() {
        String propertyUid = ibatisPropertyDao.getPropertyUIdFromParentUIdAndPropId("2003", "2183", "P0006");
        assertEquals("propertyId wrong","2192",propertyUid);
    }

    @Test
    public void doGetSupportedLgSchemaVersions() {
        List<LexGridSchemaVersion> versions = ibatisPropertyDao.doGetSupportedLgSchemaVersions();
        assertNotNull("versions null", versions);
        LexGridSchemaVersion version = new LexGridSchemaVersion();
        version.setMajorVersion(2);
        version.setMinorVersion(0);
        assertTrue("version should exist",versions.contains(version));
    }

    @Test
    public void getPropertyTypeClassifier() {
        Classifier<PropertyType,String> classifier = ibatisPropertyDao.getPropertyTypeClassifier();
        assertNotNull("classifier null", classifier);
        assertTrue("classifier wrong type", classifier instanceof PropertyTypeClassifier);
   }

    @Test
    public void getPropertyUIdByPropertyIdAndName() {
        String propertyUid = ibatisPropertyDao.getPropertyUIdByPropertyIdAndName("2003", "2183", "P0006", "FULL_SYN");
        assertEquals("propertyUid wrong", "2192", propertyUid);
    }

    @Test
    public void getLatestRevision() {
        //TODO load or create a testable revision
        String revision = ibatisPropertyDao.getLatestRevision("2003", "2192");
        assertEquals("revision wrong", "1.0",revision);
    }

    @Test
    public void getIbatisVersionsDao() {
        IbatisVersionsDao versionsDao = ibatisPropertyDao.getIbatisVersionsDao();
        assertNotNull("versionsDao null", versionsDao);
        assertTrue("versions wrong type", versionsDao instanceof IbatisVersionsDao);
    }

    @Test
    public void getPropertyByUId() {
        //TODO method cannot work
        Property property = ibatisPropertyDao.getPropertyByUId("2192");
    }

    @Test
    public void entryStateExists() {
        boolean exists = ibatisPropertyDao.entryStateExists("2003", "2004");
        assertTrue("entry state exists", exists);
    }
//
//    /**
//	 * Insert presentation.
//	 */
//	@Test
//	public void insertPresentation(){
//		final Timestamp effectiveDate = new Timestamp(1l);
//		final Timestamp expirationDate = new Timestamp(2l);
//
//		Presentation property = new Presentation();
//		property.setPropertyId("pId");
//		property.setPropertyName("propName");
//		property.setPropertyType("propType");
//		property.setLanguage("lang");
//		property.setIsActive(true);
//		property.setIsPreferred(false);
//		property.setMatchIfNoContext(true);
//		property.setDegreeOfFidelity("DOF");
//		property.setRepresentationalForm("repForm");
//
//		property.setOwner("property owner");
//
//		property.setStatus("testing");
//		Text text = new Text();
//		text.setContent("prop value");
//		text.setDataType("format");
//		property.setValue(text);
//
//		property.setEffectiveDate(effectiveDate);
//		property.setExpirationDate(expirationDate);
//
//		EntryState es = new EntryState();
//		es.setChangeType(ChangeType.VERSIONABLE);
//		es.setRelativeOrder(23l);
//		property.setEntryState(es);
//
//		String id = ibatisPropertyDao.insertProperty("1", "1", PropertyType.ENTITY, property);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		final String[] keys = (String[])template.queryForObject("Select * from Property", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				String id = rs.getString(1);
//				assertEquals(rs.getString(2),"1");
//				assertEquals(rs.getString(3),"entity");
//				assertEquals(rs.getString(4),"pId");
//				assertEquals(rs.getString(5),"propType");
//				assertEquals(rs.getString(6),"propName");
//				assertEquals(rs.getString(7),"lang");
//				assertEquals(rs.getString(8),"format");
//				assertEquals("0", rs.getString(9));
//				assertTrue(rs.getBoolean(10) == true);
//				assertEquals(rs.getString(11),"DOF");
//				assertEquals(rs.getString(12),"repForm");
//				assertEquals(rs.getString(13),"prop value");
//				assertTrue(rs.getBoolean(14) == true);
//				assertTrue(rs.getString(15).equals("property owner"));
//				assertTrue(rs.getString(16).equals("testing"));
//				assertTrue(rs.getTimestamp(17).equals(effectiveDate));
//				assertTrue(rs.getTimestamp(18).equals(expirationDate));
//				String entryStateId = rs.getString(19);
//
//				String[] keys = new String[]{id, entryStateId};
//				return keys;
//			}
//		});
//
//		assertEquals(id,keys[0]);
//
//		template.queryForObject("Select * from EntryState", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertEquals(rs.getString(1), keys[1]);
//				assertEquals(rs.getString(2), keys[0]);
//				assertEquals(rs.getString(3), "property");
//				assertEquals(rs.getString(4), ChangeType.VERSIONABLE.toString());
//				assertEquals(rs.getLong(5), 23l);
//
//				//TODO: Test with a Revision GUID
//				//TODO: Test with a Previous Revision GUID
//				//TODO: Test with a Previous EntryState GUID
//
//				return null;
//			}
//		});
//	}
//
//	@Test
//	public void updatePresentation(){
//		final Timestamp timestamp1 = new Timestamp(1l);
//		final Timestamp timestamp2 = new Timestamp(2l);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'propId')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		Presentation prop = new Presentation();
//		prop.setPropertyId("propId");
//		prop.setPropertyName("pname");
//		prop.setPropertyType("pType");
//		prop.setValue(DaoUtility.createText("some updated value", "testFormat"));
//		prop.setEffectiveDate(timestamp1);
//		prop.setExpirationDate(timestamp2);
//		prop.setIsActive(true);
//		prop.setOwner("me");
//		prop.setLanguage("Lang");
//		prop.setStatus("testing");
//		prop.setMatchIfNoContext(false);
//		prop.setDegreeOfFidelity("dof");
//		prop.setRepresentationalForm("testRepForm");
//		prop.setIsPreferred(true);
//
//		this.ibatisPropertyDao.updateProperty(
//				"1",
//				"1",
//				"1",
//				PropertyType.ENTITY,
//				prop);
//
//		template.queryForObject("Select * from property", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//
//				assertEquals(rs.getString(5), "pType");
//				assertEquals(rs.getString(6), "pname");
//				assertEquals(rs.getString(7), "Lang");
//				assertEquals(rs.getString(8), "testFormat");
//				assertEquals("1", rs.getString(9));
//				assertEquals("0", rs.getString(10));
//				assertEquals(rs.getString(11), "dof");
//				assertEquals(rs.getString(12), "testRepForm");
//				assertEquals(rs.getString(13), "some updated value");
//				/* These are being handled by another api call
//				assertEquals(rs.getBoolean(14), true);
//				assertEquals(rs.getString(15), "me");
//				assertEquals(rs.getString(16), "testing");
//				assertEquals(timestamp1.getTime(), rs.getTimestamp(17).getTime());
//				assertEquals(timestamp2.getTime(), rs.getTimestamp(18).getTime());
//				 */
//				return null;
//			}
//		});
//	}
//
//	@Test
//	public void updateProperty(){
//		final Timestamp timestamp1 = new Timestamp(1l);
//		final Timestamp timestamp2 = new Timestamp(2l);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'propId')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		Property prop = new Property();
//		prop.setPropertyId("propId");
//		prop.setPropertyName("pname");
//		prop.setPropertyType("pType");
//		prop.setValue(DaoUtility.createText("some updated value", "testFormat"));
//		prop.setEffectiveDate(timestamp1);
//		prop.setExpirationDate(timestamp2);
//		prop.setIsActive(true);
//		prop.setOwner("me");
//		prop.setLanguage("Lang");
//		prop.setStatus("testing");
//
//		this.ibatisPropertyDao.updateProperty(
//				"1",
//				"1",
//				"1",
//				PropertyType.ENTITY,
//				prop);
//
//		template.queryForObject("Select * from property", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//
//				assertEquals(rs.getString(5), "pType");
//				assertEquals(rs.getString(6), "pname");
//				assertEquals(rs.getString(7), "Lang");
//				assertEquals(rs.getString(8), "testFormat");
//				assertEquals(rs.getString(13), "some updated value");
//				/* These are being handled by another api call
//				assertEquals(rs.getBoolean(14), true);
//				assertEquals(rs.getString(15), "me");
//				assertEquals(rs.getString(16), "testing");
//				assertEquals(timestamp1.getTime(), rs.getTimestamp(17).getTime());
//				assertEquals(timestamp2.getTime(), rs.getTimestamp(18).getTime());
//				*/
//
//				return null;
//			}
//		});
//	}
//
//	/**
//	 * Insert generic property.
//	 */
//	@Test
//	public void insertGenericProperty(){
//		final Timestamp effectiveDate = new Timestamp(1l);
//		final Timestamp expirationDate = new Timestamp(2l);
//
//		Property property = new Property();
//		property.setPropertyId("pId");
//		property.setPropertyName("propName");
//		property.setPropertyType("propType");
//		property.setLanguage("lang");
//		property.setIsActive(true);
//
//		property.setOwner("property owner");
//
//		property.setStatus("testing");
//		Text text = new Text();
//		text.setContent("prop value");
//		text.setDataType("format");
//		property.setValue(text);
//
//		property.setEffectiveDate(effectiveDate);
//		property.setExpirationDate(expirationDate);
//
//		EntryState es = new EntryState();
//		es.setChangeType(ChangeType.VERSIONABLE);
//		es.setRelativeOrder(23l);
//		property.setEntryState(es);
//
//		String id = ibatisPropertyDao.insertProperty("1", "1", PropertyType.ENTITY, property);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		final String[] keys = (String[])template.queryForObject("Select * from Property", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				String id = rs.getString(1);
//				assertEquals(rs.getString(2),"1");
//				assertEquals(rs.getString(3),"entity");
//				assertEquals(rs.getString(4),"pId");
//				assertEquals(rs.getString(5),"propType");
//				assertEquals(rs.getString(6),"propName");
//				assertEquals(rs.getString(7),"lang");
//				assertEquals(rs.getString(8),"format");
//				assertNull(rs.getString(11));
//				assertNull(rs.getString(12));
//				assertEquals(rs.getString(13),"prop value");
//				assertTrue(rs.getBoolean(14) == true);
//				assertTrue(rs.getString(15).equals("property owner"));
//				assertTrue(rs.getString(16).equals("testing"));
//				assertTrue(rs.getTimestamp(17).equals(effectiveDate));
//				assertTrue(rs.getTimestamp(18).equals(expirationDate));
//				String entryStateId = rs.getString(19);
//
//				String[] keys = new String[]{id, entryStateId};
//				return keys;
//			}
//		});
//
//		assertEquals(id,keys[0]);
//
//		template.queryForObject("Select * from EntryState", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertEquals(rs.getString(1), keys[1]);
//				assertEquals(rs.getString(2), keys[0]);
//				assertEquals(rs.getString(3), "property");
//				assertEquals(rs.getString(4), ChangeType.VERSIONABLE.toString());
//				assertEquals(rs.getLong(5), 23l);
//
//				//TODO: Test with a Revision GUID
//				//TODO: Test with a Previous Revision GUID
//				//TODO: Test with a Previous EntryState GUID
//
//				return null;
//			}
//		});
//	}
//
//	/**
//	 * Insert generic property.
//	 */
//	@Test
//	public void insertHistoryProperty(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'propId', '99')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		Property property = new Property();
//		property.setPropertyId("pId");
//		property.setPropertyName("propName");
//		property.setPropertyType("propType");
//		property.setLanguage("lang");
//		property.setIsActive(true);
//
//		property.setOwner("property owner");
//
//		property.setStatus("testing");
//		Text text = new Text();
//		text.setContent("prop value");
//		text.setDataType("format");
//		property.setValue(text);
//
//		EntryState es = new EntryState();
//		es.setChangeType(ChangeType.MODIFY);
//		property.setEntryState(es);
//
//		ibatisPropertyDao.insertHistoryProperty("1", "1", property);
//
//		assertEquals(1, template.queryForObject("select count(*) from h_property",Integer.class).intValue());
//	}
//
//	@Test
//	@Transactional
//	public void testGetHistoryProperty() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//			"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation', '1')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//				"values ('1', '1', 'ecode', 'ens', '1')");
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('1', 'rid1', NOW() )");
//
//		template.execute("Insert into entrystate (entryStateGuid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('1', '1', 'entity', 'NEW', '0', '1')");
//
//		List<String> props = ibatisPropertyDao.getAllHistoryPropertyUidsOfParentByRevisionId("1", "1", "rguid1");
//
//		assertEquals(1, props.size());
//	}
//
//	@Test
//	@Transactional
//	public void testGetHistoryPropertyWithQualifier() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//			"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue, entryStateGuid) " +
//			"values ('1', '1', 'qualifier', 'aqual', 'some_qual', '1')");
//
//		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation', '1')");
//
//		template.execute("Insert into h_propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue, entryStateGuid) " +
//				"values ('1', '1', 'qualifier', 'aqual', 'some_qual', '1')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//				"values ('1', '1', 'ecode', 'ens', '1')");
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('1', 'rid1', NOW() )");
//
//		template.execute("Insert into entrystate (entryStateGuid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('1', '1', 'entity', 'NEW', '0', '1')");
//
//		List<String> props = ibatisPropertyDao.getAllHistoryPropertyUidsOfParentByRevisionId("1", "1", "rguid1");
//
//		assertEquals(1, props.size());
//	}
//
//	@Transactional
//	@Test
//	public void testGetHistoryPropertyWithTwoQualifiers() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//			"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue, entryStateGuid) " +
//			"values ('1', '1', 'qualifier', 'aqual', 'some_qual', '0')");
//
//		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation', '1')");
//
//		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation', '2')");
//
//		template.execute("Insert into h_propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue, entryStateGuid) " +
//				"values ('1', '1', 'qualifier', 'aqual', 'some_qual1', '1')");
//
//		template.execute("Insert into h_propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue, entryStateGuid) " +
//				"values ('1', '1', 'qualifier', 'aqual', 'some_qual2', '2')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//				"values ('1', '1', 'ecode', 'ens', '1')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//				"values ('1', '1', 'ecode', 'ens', '2')");
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('1', 'rid1', NOW() )");
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('2', 'rid2', NOW() )");
//
//		template.execute("Insert into entrystate (entryStateGuid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('1', '1', 'entity', 'NEW', '0', '1')");
//
//		template.execute("Insert into entrystate (entryStateGuid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('2', '1', 'entity', 'MODIFY', '0', '2')");
//
//		List<String> props = ibatisPropertyDao.getAllHistoryPropertyUidsOfParentByRevisionId("1", "1", "rguid1");
//
//		assertEquals(1, props.size());
//
//		props = ibatisPropertyDao.getAllHistoryPropertyUidsOfParentByRevisionId("1", "1", "rid2");
//
//		assertEquals(1, props.size());
//
//	}
//
//	@Test
//	@Transactional
//	public void testGetHistoryPropertyWithTwoProperties() {
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//			"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue1', 'presentation', '1')");
//
//		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue2', 'presentation', '2')");
//
//		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
//				"values ('1', '1', 'ecode', 'ens', '1')");
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('1', 'rid1', NOW() )");
//
//		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
//				"values ('2', 'rid2', NOW() )");
//
//		template.execute("Insert into entrystate (entryStateGuid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('1', '1', 'property', 'NEW', '0', '1')");
//
//		template.execute("Insert into entrystate (entryStateGuid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
//				"values ('2', '1', 'property', 'MODIFY', '0', '2')");
//
//		List<String> props = ibatisPropertyDao.getAllHistoryPropertyUidsOfParentByRevisionId("1", "1", "rguid2");
//
//		assertEquals(1, props.size());
//	}
//
//	/**
//	 * Insert property qualifier.
//	 */
//	@Test
//	public void insertPropertyQualifier(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		PropertyQualifier qual = new PropertyQualifier();
//		qual.setPropertyQualifierName("qualName");
//		qual.setPropertyQualifierType("qualType");
//		Text text = new Text();
//		text.setContent("qual text");
//		qual.setValue(text);
//
//		ibatisPropertyDao.insertPropertyQualifier("1", "1", qual);
//
//		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertNotNull(rs.getString(1));
//				assertEquals(rs.getString(2), "1");
//				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_QUALIFIER);
//				assertEquals(rs.getString(4), "qualName");
//				assertEquals(rs.getString(5), "qual text");
//				assertNull(rs.getString(6));
//				assertNull(rs.getString(7));
//
//				return null;
//			}
//		});
//	}
//
//	/**
//	 * Insert property source.
//	 */
//	@Test
//	public void insertPropertySource(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		Source source = new Source();
//		source.setContent("test source");
//		source.setSubRef("test subref");
//		source.setRole("test role");
//
//		ibatisPropertyDao.insertPropertySource("1","1", source);
//
//
//		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertNotNull(rs.getString(1));
//				assertEquals(rs.getString(2), "1");
//				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_SOURCE);
//				assertEquals("test source", rs.getString(5));
//				assertEquals(rs.getString(6), "test subref");
//				assertEquals(rs.getString(7), "test role");
//				assertNull(rs.getString(8));
//
//				return null;
//			}
//		});
//	}
//
//	/**
//	 * Insert property usage context.
//	 */
//	@Test
//	public void insertPropertyUsageContext(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		String usageContext = "test usageContext";
//
//		ibatisPropertyDao.insertPropertyUsageContext("1", "1", usageContext);
//
//
//		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertNotNull(rs.getString(1));
//				assertEquals(rs.getString(2), "1");
//				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
//				assertEquals(rs.getString(5), "test usageContext");
//				assertNull(rs.getString(6));
//				assertNull(rs.getString(7));
//
//				return null;
//			}
//		});
//	}
//
//	/**
//	 * Insert property link.
//	 */
//	@Test
//	public void insertPropertyLink(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
//				"values ('11', '1', 'entity', 'pname', 'pvalue', '1')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
//		"values ('12', '1', 'entity', 'pname', 'pvalue', '2')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//
//		assertEquals(1, template.queryForObject("Select count(*) from codingScheme", Integer.class).intValue());
//		assertEquals(1, template.queryForObject("Select count(*) from entity", Integer.class).intValue());
//		assertEquals(2, template.queryForObject("Select count(*) from property", Integer.class).intValue());
//
//
//		PropertyLink link = new PropertyLink();
//		link.setSourceProperty("1");
//		link.setPropertyLink("link");
//		link.setTargetProperty("2");
//
//		ibatisPropertyDao.insertPropertyLink("csgid", "1", link);
//
//
//		template.queryForObject("Select * from propertylinks", new RowMapper(){
//
//			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//				assertNotNull(rs.getString(1));
//				assertEquals(rs.getString(2), "1");
//				assertEquals(rs.getString(3), "11");
//				assertEquals(rs.getString(4), "link");
//				assertEquals(rs.getString(5), "12");
//
//				return null;
//			}
//		});
//	}
//
//	/**
//	 * Gets the property by parent.
//	 *
//	 * @return the property by parent
//	 */
//	@Test
//	public void getPropertyByParent(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.getAllPropertiesOfParent("1", "1", PropertyType.ENTITY);
//
//		assertEquals(1, props.size());
//	}
//
//	/**
//	 * Gets the property by parent.
//	 *
//	 * @return the property by parent
//	 */
//	@Test
//	public void getPropertiesOfParents(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.getPropertiesOfParents("1", DaoUtility.createNonTypedList("1"));
//
//		assertEquals(1, props.size());
//	}
//
//	@Test
//	public void getPropertiesOfParentsRestrictByName(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//				"values ('11', '1', 'entity', 'pname1', 'pType1', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//			"values ('12', '1', 'entity', 'pname2', 'pType2', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.
//			getPropertiesOfParents("1", DaoUtility.createNonTypedList("pname1"), null, DaoUtility.createNonTypedList("1"));
//
//		assertEquals(1, props.size());
//		assertEquals("pname1", props.get(0).getPropertyName());
//	}
//
//	@Test
//	public void getPropertiesOfParentsRestrictByType(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//				"values ('11', '1', 'entity', 'pname1', 'pType1', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//			"values ('12', '1', 'entity', 'pname2', 'pType2', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.
//			getPropertiesOfParents("1", null, DaoUtility.createNonTypedList("pType2"), DaoUtility.createNonTypedList("1"));
//
//		assertEquals(1, props.size());
//		assertEquals("pType2", props.get(0).getPropertyType());
//	}
//
//	@Test
//	public void getPropertiesOfParentsRestrictByNameAndType(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//				"values ('11', '1', 'entity', 'pname1', 'pType1', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//			"values ('12', '1', 'entity', 'pname2', 'pType2', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.
//			getPropertiesOfParents("1", DaoUtility.createNonTypedList("pname2"), DaoUtility.createNonTypedList("pType2"), DaoUtility.createNonTypedList("1"));
//
//		assertEquals(1, props.size());
//		assertEquals("pname2", props.get(0).getPropertyName());
//		assertEquals("pType2", props.get(0).getPropertyType());
//	}
//
//	@Test
//	public void getPropertiesOfParentsRestrictByNameAndTypeWrongName(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//				"values ('11', '1', 'entity', 'pname1', 'pType1', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//			"values ('12', '1', 'entity', 'pname2', 'pType2', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.
//			getPropertiesOfParents("1", DaoUtility.createNonTypedList("WRONG_NAME"), DaoUtility.createNonTypedList("pType2"), DaoUtility.createNonTypedList("1"));
//
//		assertEquals(0, props.size());
//	}
//
//	@Test
//	public void getPropertiesOfParentsRestrictByNameAndTypeWrongType(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//				"values ('11', '1', 'entity', 'pname1', 'pType1', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//			"values ('12', '1', 'entity', 'pname2', 'pType2', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.
//			getPropertiesOfParents("1", DaoUtility.createNonTypedList("pname2"), DaoUtility.createNonTypedList("WRONG_TYPE"), DaoUtility.createNonTypedList("1"));
//
//		assertEquals(0, props.size());
//	}
//
//	@Test
//	public void getPropertiesOfParentsRestrictByNameAndTypeWrongMismatched(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//				"values ('11', '1', 'entity', 'pname1', 'pType1', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyType, propertyValue) " +
//			"values ('12', '1', 'entity', 'pname2', 'pType2', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.
//			getPropertiesOfParents("1", DaoUtility.createNonTypedList("pname1"), DaoUtility.createNonTypedList("pType2"), DaoUtility.createNonTypedList("1"));
//
//		assertEquals(0, props.size());
//	}
//
//	@Test
//	public void getPropertiesOfParentsWithTwoDifferentEntities(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
//				"values ('11', '11', 'entity', 'pid1', 'pvalue1')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('11', '1', 'ecode1', 'ens1')");
//
//		List<Property> props = ibatisPropertyDao.getPropertiesOfParents("1", DaoUtility.createNonTypedList("11"));
//
//		assertEquals(1, props.size());
//
//		assertEquals("pvalue1", props.get(0).getValue().getContent());
//	}
//
//	@Test
//	public void getPropertiesOfParentsTwoProperties(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyId, propertyName, propertyValue) " +
//				"values ('1', '1', 'entity', 'pid', 'pname', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyId, propertyName, propertyValue) " +
//				"values ('11', '1', 'entity', 'pid1', 'pname', 'pvalue1')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('11', '1', 'ecode1', 'ens1')");
//
//		List<Property> props = ibatisPropertyDao.getPropertiesOfParents("1", DaoUtility.createNonTypedList("1"));
//
//		assertEquals(2, props.size());
//	}
//
//	@Test
//	public void getPropertiesOfParentsZeroProperties(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue')");
//
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
//				"values ('11', '1', 'entity', 'pid1', 'pvalue1')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('11', '1', 'ecode1', 'ens1')");
//
//		List<Property> props = ibatisPropertyDao.getPropertiesOfParents("1", DaoUtility.createNonTypedList("11"));
//
//		assertEquals(0, props.size());
//	}
//
//	@Test
//	public void getPropertyByParentWithEverything(){
//		Timestamp ts1 = new Timestamp(1l);
//		Timestamp ts2 = new Timestamp(2l);
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyId, propertyType, propertyName, language, format, isPreferred, matchIfNoContext, degreeOfFidelity, representationalForm, propertyValue, isActive, owner, status,    effectiveDate,         expirationDate, entryStateGuid) " +
//			"values (                             '1',      '1',       'entity',       'pid',  'presentation', 'pname',      'lang',  'xml',    '1',          '0',             'dof',             'repForm',       'pvalue',        '1',   'me', 'test', '"+ ts1.toString()+"', '"+ ts2.toString() + "',      '1')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		template.execute("Insert into propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeValue,  subRef, role, entryStateGuid) " +
//			"values ('1', '1', 'source', 'aValue', 'aSubRef', 'aRole', '1')");
//
//		template.execute("Insert into propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeValue) " +
//			"values ('2', '1', 'usageContext', 'usageContext1')");
//
//		template.execute("Insert into propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue) " +
//			"values ('3', '1', 'qualifier', 'aQual', 'aQualValue')");
//
//		template.execute("Insert into revision (revisionGuid, revisionId, revAppliedDate) " +
//			"values (								'1', 	 'rid1',       NOW() )");
//
//		template.execute("Insert into revision (revisionGuid, revisionId, revAppliedDate) " +
//			"values (								'2', 	 'rid2',       NOW() )");
//
//		template.execute("Insert into entrystate (entryStateGuid, entryGuid, entryType, changeType, relativeOrder, revisionGuid, prevRevisionGuid) " +
//			"values (								'2', 	   '12', 'property', 'DEPENDENT',     '1',        '1',    '2')");
//
//		template.execute("Insert into entrystate (entryStateGuid, entryGuid, entryType, changeType, relativeOrder, revisionGuid, prevRevisionGuid, preventryStateGuid) " +
//			"values (								'1', 	   '1',  'property', 'MODIFY',     '1',            '1',        '2',           '2')");
//
//		List<Property> props = ibatisPropertyDao.getAllPropertiesOfParent("1", "1", PropertyType.ENTITY);
//
//		assertEquals(1, props.size());
//
//		Presentation prop = (Presentation)props.get(0);
//		assertEquals("dof", prop.getDegreeOfFidelity());
//		assertEquals("lang", prop.getLanguage());
//		assertEquals("me", prop.getOwner());
//		assertEquals("pid", prop.getPropertyId());
//		assertEquals("pname", prop.getPropertyName());
//		assertEquals("presentation", prop.getPropertyType());
//		assertEquals("repForm", prop.getRepresentationalForm());
//		assertEquals("test", prop.getStatus());
//
//		assertEquals(ts2.getTime(), prop.getExpirationDate().getTime());
//		assertEquals(ts1.getTime(), prop.getEffectiveDate().getTime());
//		assertTrue(prop.getIsActive());
//		assertTrue(prop.getIsPreferred());
//		assertFalse(prop.getMatchIfNoContext());
//		assertEquals("pvalue", prop.getValue().getContent());
//		assertEquals("xml", prop.getValue().getDataType());
//
//		assertEquals(1, prop.getUsageContextCount());
//	    String usageContext = prop.getUsageContext(0);
//	    assertEquals("usageContext1", usageContext);
//
//		assertEquals(1, prop.getPropertyQualifierCount());
//		PropertyQualifier qual = prop.getPropertyQualifier(0);
//		assertEquals("aQual", qual.getPropertyQualifierName());
//		assertEquals("aQualValue", qual.getValue().getContent());
//
//		assertEquals(1, prop.getSourceCount());
//		Source source = prop.getSource(0);
//		assertEquals("aSubRef", source.getSubRef());
//		assertEquals("aRole", source.getRole());
//		assertEquals("aValue", source.getContent());
//
//		EntryState es = prop.getEntryState();
//		assertNotNull(es);
//		assertEquals("rid1", es.getContainingRevision());
//		assertEquals("rid2", es.getPrevRevision());
//		assertEquals(ChangeType.MODIFY, es.getChangeType());
//		assertTrue(1l == es.getRelativeOrder());
//
//	}
//
//	/**
//	 * Gets the propert id.
//	 *
//	 * @return the propert id
//	 */
//	@Test
//	public void getPropertId(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'id1')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		String id = ibatisPropertyDao.getPropertyUIdFromParentUIdAndPropId("1", "1", "id1");
//
//		assertEquals("1", id);
//	}
//
//	/**
//	 * Gets the presentation property by parent.
//	 *
//	 * @return the presentation property by parent
//	 */
//	@Test
//	public void getPresentationPropertyByParent(){
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		List<Property> props = ibatisPropertyDao.getAllPropertiesOfParent("1", "1", PropertyType.ENTITY);
//
//		assertEquals(1, props.size());
//
//		assertTrue(props.get(0) instanceof Presentation);
//	}
//
//	/**
//	 * Delete all entity properties of coding scheme.
//	 */
//	@Test
//	public void deleteAllEntityPropertiesOfCodingScheme(){
//
//		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
//		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
//				"values ('1', '1', 'entity', 'pid', 'pvalue')");
//
//		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
//			"values ('1', 'csname', 'csuri', 'csversion')");
//
//		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
//			"values ('1', '1', 'ecode', 'ens')");
//
//		assertEquals(1, template.queryForObject("Select count(*) from codingScheme", Integer.class).intValue());
//		assertEquals(1, template.queryForObject("Select count(*) from entity", Integer.class).intValue());
//		assertEquals(1, template.queryForObject("Select count(*) from property", Integer.class).intValue());
//
//		ibatisPropertyDao.deleteAllEntityPropertiesOfCodingScheme("1");
//
//		assertEquals(0, template.queryForObject("Select count(*) from property", Integer.class).intValue());
//
//	}
}