package org.lexevs.dao.database.hibernate5.registry;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.database.hibernate.registry.HibernateRegistryDao;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:lexevsDao.xml"})
@Transactional(readOnly=false)
public class Hibernate5RegistryDaoTest  extends AbstractTransactionalJUnit4SpringContextTests{

	/** The hibernate registry dao. */
	@Autowired
	private HibernateRegistryDao hdao;
	
	/** The data source. */
	@Autowired
	private DataSource dataSource;
	
	@Test
	public void testInsertCodingSchemeEntry(){
		final Timestamp activationDate = new Timestamp(1l);
		final Timestamp deActivationDate = new Timestamp(2l);
		final Timestamp lastUpdateDate = new Timestamp(3l);
		
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setActivationDate(activationDate);
		entry.setBaseRevision("1");
		entry.setDbName("db name");
		entry.setDbSchemaDescription("description");
		entry.setDbSchemaVersion("1.1");
		entry.setDbUri("dbUri://");
		entry.setDeactivationDate(deActivationDate);
		entry.setFixedAtRevision("2");
		entry.setLastUpdateDate(lastUpdateDate);
		entry.setIsLocked(true);
		entry.setPrefix("prefix");
		entry.setResourceUri("uri:my.my.my");
		entry.setResourceVersion("v1");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		entry.setStagingPrefix("staging-prefix");
			
		hdao.insertRegistryEntry(entry);
		
		
		List<RegistryEntry> entries = hdao.getAllRegistryEntriesOfTypeAndURI(ResourceType.CODING_SCHEME, "uri:my.my.my");
		
		assertEquals(entries.get(0).getResourceUri(),"uri:my.my.my"); 
	}
	
	@Test
	public void testdeleteRegistryEntry() {
		final Timestamp activationDate = new Timestamp(1l);
		final Timestamp deActivationDate = new Timestamp(2l);
		final Timestamp lastUpdateDate = new Timestamp(3l);
		
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setActivationDate(activationDate);
		entry.setBaseRevision("1");
		entry.setDbName("db name");
		entry.setDbSchemaDescription("LexGrid Table Schema Version 2.0");
		entry.setDbSchemaVersion("2.0");
		entry.setDbUri("dbUri://");
		entry.setDeactivationDate(deActivationDate);
		entry.setFixedAtRevision("2");
		entry.setLastUpdateDate(lastUpdateDate);
		entry.setIsLocked(true);
		entry.setPrefix("prefix");
		entry.setResourceUri("uri:my.my.my");
		entry.setResourceVersion("v1");
		entry.setStatus(CodingSchemeVersionStatus.ACTIVE.toString());
		entry.setTag("tag");
		entry.setStagingPrefix("staging-prefix");
			
		hdao.deleteRegistryEntry(entry);
		
		List<RegistryEntry> entries = hdao.getAllRegistryEntriesOfTypeAndURI(ResourceType.CODING_SCHEME, "uri:my.my.my");
		
		assertEquals(entries.size(),0); 
		
	}
	
	@Test
	public void getCodingSchemeEntry(){

		
		
		List<RegistryEntry> entries = hdao.getAllRegistryEntriesOfTypeAndURI(ResourceType.CODING_SCHEME, "urn:oid:11.11.0.1");
		
		assertEquals(entries.get(0).getResourceUri(),"urn:oid:11.11.0.1"); 
	}
}
