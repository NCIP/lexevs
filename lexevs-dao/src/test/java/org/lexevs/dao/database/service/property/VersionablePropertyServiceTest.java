
package org.lexevs.dao.database.service.property;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.hibernate.registry.HibernateRegistryDao;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.entity.VersionableEventEntityService;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.database.service.listener.DefaultServiceEventListener;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * The Class VersionableEntityServiceTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionablePropertyServiceTest extends LexEvsDbUnitTestBase {

	/** The service. */
	@Resource
	private VersionableEventPropertyService service;
	
	@Resource
	private VersionableEventEntityService entityService;

	@Resource
	private HibernateRegistryDao registryDao;
	
	/**
	 * Insert entity.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void updateProperty() throws Exception{

		JdbcTemplate template = new JdbcTemplate(getDataSource());

		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId, entryStateGuid) " +
		"values ('1', '1', 'entity', 'pname', 'pvalue', 'propId', '1')");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('1', 'rid', NOW() )");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('1', '1', 'property', 'NEW', '0', '1')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
		"values ('1', '1', 'ecode', 'ens')");

		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("csuri");
		entry.setResourceVersion("csversion");
		entry.setDbSchemaVersion("2.0");
		registryDao.insertRegistryEntry(entry);

		List<RegistryEntry> entries = registryDao.getAllRegistryEntries();
		assertEquals(1,entries.size());

		Property property = new Property();
		property.setPropertyId("propId");
		property.setPropertyName("pname");
		property.setValue(DaoUtility.createText("updated prop value"));

		TestListener testListener = new TestListener(entityService, false);

		List<DatabaseServiceEventListener> testListeners = new ArrayList<DatabaseServiceEventListener>();
		testListeners.add(testListener);
	
		service.updateEntityProperty("csuri", "csversion", "ecode", "ens", property);

		assertEquals("updated prop value", template.queryForObject("select propertyValue from property where propertyId = 'propId'", String.class));
	}

	
	@Test
	public void updatePropertyRollbackInListener() throws Exception{

		JdbcTemplate template = new JdbcTemplate(getDataSource());

		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
		"values ('1', '1', 'entity', 'pid', 'pvalue', 'propId')");

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
		"values ('1', '1', 'ecode', 'ens')");

		RegistryEntry entry = new RegistryEntry();
		entry.setResourceUri("csuri");
		entry.setResourceVersion("csversion");
		entry.setDbSchemaVersion("2.0");
		registryDao.insertRegistryEntry(entry);

		List<RegistryEntry> entries = registryDao.getAllRegistryEntries();
		assertEquals(1,entries.size());

		Property property = new Property();
		property.setPropertyId("propId");
		property.setValue(DaoUtility.createText("updated prop value"));

		TestListener testListener = new TestListener(entityService, true);

		List<DatabaseServiceEventListener> testListeners = new ArrayList<DatabaseServiceEventListener>();
		testListeners.add(testListener);
	
		boolean exceptionThrown = false;
		try {
			service.updateEntityProperty("csuri", "csversion", "ecode", "ens", property);
		} catch (Exception e) {
			exceptionThrown = true;
		} finally {
			assertTrue(exceptionThrown);
		}
		
		template.queryForObject("Select * from property", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertEquals("pvalue", rs.getString(13));

				return null;
			}
		});
	}

	private static class TestListener extends DefaultServiceEventListener {
		boolean foundUpdate = false;
		EntityService service;
		boolean throwException = false;
		
		TestListener(EntityService service, boolean throwException){
			this.service = service;
			this.throwException = throwException;
		}
		
		public boolean onPropertyUpdate(PropertyUpdateEvent event) {
			foundUpdate = true;
			Entity entity = service.getEntity("csuri", "csversion", "ecode", "ens");
			assertEquals("updated prop value", entity.getProperty()[0].getValue().getContent());
			
			if(this.throwException) {
				throw new RuntimeException();
			}
			return true;
		}
	}
}