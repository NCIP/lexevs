
package org.lexevs.dao.database.service;

import javax.annotation.Resource;

import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.error.DatabaseError;
import org.lexevs.dao.database.service.error.ErrorCallbackListener;
import org.lexevs.dao.database.service.property.PropertyService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The Class DatabaseServiceManagerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseServiceManagerTest extends LexEvsDbUnitTestBase {

	/** The database service manager. */
	@Resource
	private DatabaseServiceManager databaseServiceManager;
	
	@Resource
	private RegistryDao registryDao;
	
	/**
	 * Check coding scheme service.
	 */
	@Test
	public void checkCodingSchemeService(){
		assertNotNull(this.databaseServiceManager.getCodingSchemeService());
		assertTrue(this.databaseServiceManager.getCodingSchemeService() instanceof CodingSchemeService);
	}
	
	/**
	 * Check entity service.
	 */
	@Test
	public void checkEntityService(){
		assertNotNull(this.databaseServiceManager.getEntityService());
		assertTrue(this.databaseServiceManager.getEntityService() instanceof EntityService);
	}
	
	/**
	 * Check propery service.
	 */
	@Test
	public void checkProperyService(){
		assertNotNull(this.databaseServiceManager.getPropertyService());
		assertTrue(this.databaseServiceManager.getPropertyService() instanceof PropertyService);
	}
	
	
	@Test(expected=ExpectedException.class)
	public void testErrorCallback(){
		RegistryEntry entry = new RegistryEntry();
		entry.setId("0");
		entry.setResourceUri("csuri");
		entry.setResourceVersion("csversion");
		entry.setDbSchemaVersion("2.0");

		registryDao.insertRegistryEntry(entry);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		EntityService entityService = this.databaseServiceManager.wrapServiceForErrorHandling(
				databaseServiceManager.getEntityService(),
				new ErrorCallbackListener() {

			@Override
			public void onDatabaseError(DatabaseError databaseError) {
				assertEquals(DataIntegrityViolationException.class, databaseError.getErrorException().getClass());
				throw new ExpectedException();
			}
		});
		
		entityService.insertEntity("csuri", "csversion", new Entity());
		
	}		

	private static class ExpectedException extends RuntimeException {}
}