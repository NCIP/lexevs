package org.LexGrid.loader.dao.template;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.URIMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexgrid.loader.dao.template.MedRtCachingSupportedAttributeTemplate;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MedRtCachingSupportedAttributeTemplateTest {

	@Mock
	CodingSchemeIdSetter codingSchemeIdSetter;

	@InjectMocks
	MedRtCachingSupportedAttributeTemplate template;

	@Before
	public void setUp() throws Exception {
		when(codingSchemeIdSetter.getCodingSchemeName()).thenReturn("MED-RT");
		when(codingSchemeIdSetter.getCodingSchemeVersion()).thenReturn("2018_05_07");
		when(codingSchemeIdSetter.getCodingSchemeUri()).thenReturn("2.16.840.1.113883.6.345");
	}

	@Test
	public void testInsert() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		SupportedCodingScheme scheme = new SupportedCodingScheme();
		scheme.setContent("RXNORM");
		scheme.setIsImported(false);
		scheme.setLocalId("RXNORM");
		scheme.setUri("urn:oid:2.16.840.1.113883.6.88");


		template.insert("SomeCodingSchemeUri", "SomeCodingSchemeVersion", scheme);
		
		Method m = template.getClass().getSuperclass().getDeclaredMethod("getAttributeCache");
		m.setAccessible(true);
		@SuppressWarnings("unchecked")
		ConcurrentHashMap<String, CodingSchemeIdHolder<URIMap>> map = (ConcurrentHashMap<String, CodingSchemeIdHolder<URIMap>>) m
				.invoke(template);
		Method build = template.getClass().getSuperclass().getDeclaredMethod("buildCacheKey", URIMap.class);
		build.setAccessible(true);
		CodingSchemeIdHolder<URIMap> holder = map.get(build.invoke(template, scheme));
		
		assertEquals("RXNORM", holder.getItem().getContent());
		assertEquals("RXNORM", holder.getItem().getLocalId());
		assertEquals("urn:oid:2.16.840.1.113883.6.88", holder.getItem().getUri());
		assertEquals("2018_05_07", holder.getCodingSchemeIdSetter().getCodingSchemeVersion());
		assertEquals("2.16.840.1.113883.6.345", holder.getCodingSchemeIdSetter().getCodingSchemeUri());
	}

}
