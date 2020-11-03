package org.LexGrid.LexBIG.Impl.load.umls;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexgrid.loader.rrf.data.codingscheme.DefaultMrsabUtility;
import org.lexgrid.loader.rrf.data.codingscheme.MrsabUtility;
import org.lexgrid.loader.umls.data.codingscheme.MedRTUmlsCodingSchemeIdSetter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class MedRTUmlsCodingSchemeIdSetterTest {
	
	@Mock
	MrsabUtility mrSabUtility;
	@Mock
	Map<String,String> isoMap;

	
	@InjectMocks
	MedRTUmlsCodingSchemeIdSetter setter;
	

	@Before
	public void setUp() throws Exception {
		setter.setSab("MED-RT");
		when(mrSabUtility.getCodingSchemeNameFromSab("MED-RT")).thenReturn("MED-RT");
		when(mrSabUtility.getCodingSchemeVersionFromSab("MED-RT")).thenReturn("2018_05_07");
		when(isoMap.get("MED-RT")).thenReturn("2.16.840.1.113883.6.345");
		setter.afterPropertiesSet();

	}

	@Test
	public void testCSName() throws Exception {
		assertEquals(setter.getCodingSchemeName(), "MED-RT");
	}
	
	@Test
	public void testCSVersion() throws Exception {
		assertEquals(setter.getCodingSchemeVersion(), "2018_05_07");
	}
	
	@Test
	public void testCSUri() throws Exception {
		assertEquals(setter.getCodingSchemeUri(), "2.16.840.1.113883.6.345");
	}

}
