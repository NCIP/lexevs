package org.lexevs.dao.database.service.Author;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

import javax.annotation.Resource;

import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class VersionableEventAuthoringServiceTest extends LexEvsDbUnitTestBase {

	@Resource(name = "authoringService")
	private VersionableEventAuthoringService service;

	private static final String CREATE_COMMON_SCRIPT = "sql/lexevs/common-create-mysql.sql";

	/** The Constant CREATE_CODINGSCHEME_SCRIPT. */
	private static final String CREATE_CODINGSCHEME_SCRIPT = "sql/lexevs/codingscheme-create-mysql.sql";

	/** */
	private static final String CREATE_CODINGSCHEME_HISTORY_SCRIPT = "sql/lexevs/codingschemehistory-create-mysql.sql";

/*	@Test
	public void testCodingSchemeRevisions() throws Exception {

		init();

		URI sourceURI = new File(
				"src/test/resources/Automobiles2010_Test_CS.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);
	}*/

/*	@Test
	public void testEntityRevisions() throws Exception {

		init();

		URI sourceURI = new File(
				"src/test/resources/Automobiles2010_Test_Entity.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);
	}*/
	
/*	@Test
	public void testPropertyRevisions() throws Exception {

		init();

		URI sourceURI = new File(
				"src/test/resources/Automobiles2010_Test_Property.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);
	}*/
	
	@Test
	public void testRelationRevisions() throws Exception {

		init();

		URI sourceURI = new File(
				"src/test/resources/Automobiles2010_Test_Relation.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);
	}

/*	@Test
	public void testTargetRevisions() throws Exception {

		init();

		URI sourceURI = new File(
				"src/test/resources/Automobiles2010_Test_AssociationTarget.xml").toURI();

		org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
				SystemRelease.class);
		SystemRelease systemRelease = (SystemRelease) um
				.unmarshal(new InputStreamReader(sourceURI.toURL()
						.openConnection().getInputStream()));

		service.loadSystemRelease(systemRelease);
	}*/
	
	private void init() throws Exception {
		methodCachingProxy.clearAll();

		String prefix = prefixResolver.resolveDefaultPrefix();
		JdbcOperations jdbcOps = new SimpleJdbcTemplate(dataSource)
				.getJdbcOperations();

		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "csSupportedAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "csMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix
				+ "propertyMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "propertyLinks");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "property");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entityType");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entityAssnQuals");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix
				+ "entityAssnsToEntity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix
				+ "entityAssnsToEntityTr");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entityAssnsToData");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix
				+ "associationPredicate");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "associationEntity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "relation");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "codingScheme");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "entryState");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "revision");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "systemRelease");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "registryMetaData");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "registry");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix
				+ "h_associationEntity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_codingScheme");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_csMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_entity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_entityAssnQuals");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix
				+ "h_entityAssnsToData");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix
				+ "h_entityAssnsToEntity");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_property");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_propertyLinks");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix
				+ "h_propertyMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS " + prefix + "h_relation");
		jdbcOps.execute("DROP TABLE IF EXISTS vsdEntry");
		jdbcOps.execute("DROP TABLE IF EXISTS valueSetDefinition");
		jdbcOps.execute("DROP TABLE IF EXISTS vsMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS vsPLEntry");
		jdbcOps.execute("DROP TABLE IF EXISTS vsPickList");
		jdbcOps.execute("DROP TABLE IF EXISTS vsPropertyMultiAttrib");
		jdbcOps.execute("DROP TABLE IF EXISTS vsProperty");
		jdbcOps.execute("DROP TABLE IF EXISTS vsEntryState");
		jdbcOps.execute("DROP TABLE IF EXISTS vsSupportedAttrib");

		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(
				new ClassPathResource(CREATE_COMMON_SCRIPT), prefix, prefix);
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(
				new ClassPathResource(CREATE_CODINGSCHEME_SCRIPT), prefix,
				prefix);
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(
				new ClassPathResource(CREATE_CODINGSCHEME_HISTORY_SCRIPT),
				prefix, prefix);
	}
}
