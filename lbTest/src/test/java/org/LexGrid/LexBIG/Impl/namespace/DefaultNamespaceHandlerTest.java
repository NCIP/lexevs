
package org.LexGrid.LexBIG.Impl.namespace;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.service.SystemResourceService;
import org.mockito.Mockito;

import junit.framework.TestCase;

public class DefaultNamespaceHandlerTest extends TestCase {
	
	String uri = "urn:oid:11.11.0.1";
	String ver1 = "1.0";
	String name1 = "Automobiles";
	String nmspc = "Autos";
	String ver2 = "1.1";
	

	CodingScheme scheme1;
	
	Mappings maps;
	private SupportedCodingScheme supScheme;
	private SupportedNamespace namespace;

	public LexEvsServiceLocator locator = Mockito.mock(LexEvsServiceLocator.class);
	public DatabaseServiceManager dbs = Mockito.mock(DatabaseServiceManager.class);
	public CodingSchemeService css = Mockito.mock(CodingSchemeService.class);
	public SystemResourceService srs = Mockito.mock(SystemResourceService.class);
	public DefaultNamespaceHandler constHandler = new DefaultNamespaceHandler(locator);
	private Registry registry = Mockito.mock(Registry.class);
	private List<RegistryEntry> entries;

	

	
	@Before
	public void setUp() throws Exception {
		scheme1 = new CodingScheme();
		scheme1.setCodingSchemeName(name1);
		scheme1.setCodingSchemeURI(uri);
		scheme1.setRepresentsVersion("1.0");
		
		supScheme = new SupportedCodingScheme();
		supScheme.setContent(name1);
		supScheme.setLocalId(name1);
		supScheme.setUri(uri);
	
		
		namespace = new SupportedNamespace();
		namespace.setContent(nmspc);
		namespace.setEquivalentCodingScheme(name1);
		namespace.setLocalId(nmspc);
		namespace.setUri(uri);
		
		maps = new Mappings();
		maps.addSupportedCodingScheme(supScheme);
		maps.addSupportedNamespace(namespace);
		scheme1.setMappings(maps);
		
		RegistryEntry ent1 = new RegistryEntry();
		ent1.setResourceType(ResourceType.CODING_SCHEME);
		ent1.setResourceUri(uri);
		ent1.setResourceVersion(ver1);
		
		RegistryEntry ent2 = new RegistryEntry();
		ent2.setResourceType(ResourceType.CODING_SCHEME);
		ent2.setResourceUri(uri);
		ent2.setResourceVersion(ver2);
		ent2.setTag("PRODUCTION");
		
		entries = new ArrayList<RegistryEntry>();
		entries.add(ent1);
		entries.add(ent2);

		Mockito.when(locator.getDatabaseServiceManager()).thenReturn(dbs);
		Mockito.when(dbs.getCodingSchemeService()).thenReturn(css);
		Mockito.when(css.getCodingSchemeByUriAndVersion(uri, "1.0")).thenReturn(scheme1);
		Mockito.when(srs.getUriForUserCodingSchemeName(name1, null)).thenReturn(uri);
		Mockito.when(locator.getRegistry()).thenReturn(registry);
		Mockito.when(registry.getAllRegistryEntriesOfTypeAndURI(ResourceType.CODING_SCHEME, uri)).thenReturn(entries);
	}
	
	@Test
	public void testDefaultHandler() throws LBParameterException{
		assertTrue(constHandler.getCodingSchemeForNamespace(uri, ver1, nmspc).getCodingSchemeVersion().equals(ver1));
	}

}