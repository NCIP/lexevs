package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.cts2.entity.NamedEntityDescription;
import org.junit.Before;
import org.junit.Test;

public class ResolvedConceptReferenceToNamedEntityDescription extends
		BaseDozerBeanMapperTest {
	private ResolvedConceptReference ref;
	private NamedEntityDescription mapped;

	@Before
	public void initialize() {
		ref = new ResolvedConceptReference();
		ref.setCode("testCode");
		ref.setCodeNamespace("testNamespace");
		ref.setCodingSchemeName("codingSchemeName");
		ref.setCodingSchemeURI("codingSchemeURI");
		mapped = baseDozerBeanMapper.map(ref, NamedEntityDescription.class);

	}

	@Test
	public void testGetCode() {
		assertEquals("testCode", mapped.getEntityId().getName());
	}

	@Test
	public void testGetNamespace() {
		assertEquals("testNamespace", mapped.getEntityId().getNamespace());
	}

	@Test
	public void testGetCodingSchemeURI() {
		assertEquals("codingSchemeURI", mapped.getDescribingCodeSystemVersion()
				.getMeaning());
	}

	@Test
	public void testGetCodingSchemeName() {
		assertEquals("codingSchemeName", mapped
				.getDescribingCodeSystemVersion().getContent());
	}
}
