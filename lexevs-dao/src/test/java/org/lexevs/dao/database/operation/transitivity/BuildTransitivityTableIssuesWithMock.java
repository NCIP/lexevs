package org.lexevs.dao.database.operation.transitivity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codingscheme.VersionableEventCodingSchemeService;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.service.DelegatingSystemResourceService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class BuildTransitivityTableIssuesWithMock {
	private static final String SNOMED_URI = "SNOMED_URI";
	private static final String SNOMED_US_VER = "SNOMED_US_VER";

	@InjectMocks
	DatabaseServiceManager databaseServiceManager = new DatabaseServiceManager();

	@Mock
	VersionableEventCodingSchemeService csService;
	
	@Mock
	DelegatingSystemResourceService resourceService = new DelegatingSystemResourceService();
	
	@Mock
	Registry registry;
	
	
	@Before
	public void initMock(){
		 MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getRegistryEntryforWrongVersionOfSchemeLEXEVS_669() throws LBParameterException{
		DefaultTransitivityBuilder builder = new DefaultTransitivityBuilder();
		builder.setDatabaseServiceManager(databaseServiceManager);
		databaseServiceManager.setCodingSchemeService(csService);
		CodingScheme scheme = new CodingScheme();
		Mappings mappings = new Mappings();
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setUri(SNOMED_URI);
		mappings.addSupportedCodingScheme(scs);
		scheme.setMappings(mappings);
		when(csService.getCodingSchemeByUriAndVersion(SNOMED_URI, SNOMED_US_VER)).thenReturn(scheme);
		when(resourceService.getUriForUserCodingSchemeName("SNOMED_US", null)).thenReturn(SNOMED_URI);
		builder.setSystemResourceService(resourceService);
		when(registry.getAllRegistryEntriesOfTypeAndURI(ResourceType.CODING_SCHEME, SNOMED_URI)).thenReturn(buildRegistryEntryList());
		builder.setRegistry(registry);
		RegistryEntry entry = builder.getRegistryEntryForCodingSchemeName("SNOMED_US", SNOMED_URI, SNOMED_US_VER);
		assertEquals(SNOMED_US_VER, entry.getResourceVersion());

	}

	private List<RegistryEntry> buildRegistryEntryList() {
		List<RegistryEntry> list = new ArrayList<RegistryEntry>();
		RegistryEntry regen1 = new RegistryEntry();
		RegistryEntry regen2 = new RegistryEntry();
		RegistryEntry regen3 = new RegistryEntry();
		regen1.setResourceType(ResourceType.CODING_SCHEME);
		regen1.setResourceUri(SNOMED_URI);
		regen1.setResourceVersion("Old_Production_Version");
		regen1.setTag("PRODUCTION");
		regen2.setResourceType(ResourceType.CODING_SCHEME);
		regen2.setResourceUri(SNOMED_URI);
		regen2.setResourceVersion("SOME_VERSION");
		regen3.setResourceType(ResourceType.CODING_SCHEME);
		regen3.setResourceUri(SNOMED_URI);
		regen3.setResourceVersion(SNOMED_US_VER);
		list.add(regen1);
		list.add(regen2);
		list.add(regen3);
		return list;
	}

}
