package org.LexGrid.loader.processor;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexgrid.loader.dao.template.MedRtCachingSupportedAttributeTemplate;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.processor.MedRtEntityAssnsToEntityProcessor;
import org.lexgrid.loader.processor.MedRtEntityAssnsToEntityProcessor.SelfReferencingAssociationPolicy;
import org.lexgrid.loader.processor.support.OptionalQualifierResolver;
import org.lexgrid.loader.umls.data.codingscheme.MedRTUmlsCodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(MockitoJUnitRunner.class)
public class MedRtEntityAssnsToEntityProcessorTest {
	
	@Mock
	private Map<String,String> isoMap;
	
//	@Mock
//	private ;
	
	MedRtEntityAssnsToEntityProcessor processor;
	

	@Before
	public void setUp() throws Exception {
	processor = new MedRtEntityAssnsToEntityProcessor();
	
	when(isoMap.get("MED-RT")).thenReturn("2.16.840.1.113883.6.345");
	when(isoMap.get("MSH")).thenReturn("msh.urn.declaration");

	processor.setIsoMap(isoMap);
	CodingSchemeIdSetter codingSchemeIdSetter = mock(MedRTUmlsCodingSchemeIdSetter.class);
	when(codingSchemeIdSetter.getCodingSchemeName()).thenReturn("MED-RT");
	when(codingSchemeIdSetter.getCodingSchemeVersion()).thenReturn("2018_05_07");
	when(codingSchemeIdSetter.getCodingSchemeUri()).thenReturn("2.16.840.1.113883.6.345");
	MedRtCachingSupportedAttributeTemplate supportedAttributeTemplate = new MedRtCachingSupportedAttributeTemplate();
	supportedAttributeTemplate.setCodingSchemeIdSetter(codingSchemeIdSetter);
	processor.setCodingSchemeIdSetter(codingSchemeIdSetter);
	processor.setSupportedAttributeTemplate(supportedAttributeTemplate);
	
	}

	@Test
	public void registerSupportedAttributesTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		MedRtCachingSupportedAttributeTemplate template = mock(MedRtCachingSupportedAttributeTemplate.class);
		Method r = processor.getClass().getDeclaredMethod("registerSupportedAttributes", SupportedAttributeTemplate.class,
				ParentIdHolder.class);
		r.setAccessible(true);
		Method m = processor.getSupportedAttributeTemplate().getClass().getSuperclass().getDeclaredMethod("getAttributeCache");
		m.setAccessible(true);
		

		r.invoke(processor, template, new ParentIdHolder<AssociationSource>(
				processor.getCodingSchemeIdSetter(),
				"predicateId", 
				getTestSource()));
		@SuppressWarnings("unchecked")
		ConcurrentHashMap<String, CodingSchemeIdHolder<URIMap>> map = (ConcurrentHashMap<String, CodingSchemeIdHolder<URIMap>>) m
				.invoke(processor.getSupportedAttributeTemplate());
		assertNotNull(map);
		assertTrue(map.size() > 0);
		Method build = processor.getSupportedAttributeTemplate().getClass().getSuperclass().getDeclaredMethod("buildCacheKey", URIMap.class);
		build.setAccessible(true);
		
		SupportedCodingScheme scheme = new SupportedCodingScheme();
		scheme.setContent("MSH");
		scheme.setIsImported(false);
		scheme.setLocalId("MSH");
		scheme.setUri("msh.urn.declaration");
		CodingSchemeIdHolder<URIMap> holder = map.get(build.invoke(processor.getSupportedAttributeTemplate(), scheme));
		assertNotNull(holder);
		assertEquals("MSH", holder.getItem().getContent());
		assertEquals("MSH", holder.getItem().getLocalId());
		assertEquals("msh.urn.declaration", holder.getItem().getUri());
		assertEquals("2018_05_07", holder.getCodingSchemeIdSetter().getCodingSchemeVersion());
		assertEquals("2.16.840.1.113883.6.345", holder.getCodingSchemeIdSetter().getCodingSchemeUri());
		
	}
	
	private AssociationSource getTestSource(){
		AssociationSource source = new AssociationSource();
		AssociationTarget target = new AssociationTarget();
		
		String sourceCode = "CND0909";
		String sourceNamespace = "MED-RT";
		
		String targetCode = "09099";
		String targetNamespace = "MSH";

		
		source.setSourceEntityCode(sourceCode);
		source.setSourceEntityCodeNamespace(sourceNamespace);
		
		target.setTargetEntityCode(targetCode);
		target.setTargetEntityCodeNamespace(targetNamespace);
		
		target.setAssociationInstanceId("placeholderid");		
		
		target.setIsActive(true);
		target.setIsDefining(true);
		
		AssociationQualification qual = new AssociationQualification();
		qual.setAssociationQualifier("qualifier");
		qual.setQualifierText(Constructors.createText("qvalue"));
		target.addAssociationQualification(qual);	


		
		source.addTarget(target);
		
		return source;
	}

}
