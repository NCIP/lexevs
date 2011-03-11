package org.cts.internal.mapper;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.cts2.entity.NamedEntityDescription;
import org.junit.Before;
import org.junit.Test;

public class ResolvedConceptReferenceToNamedEntityDescription extends BaseDozerBeanMapperTest{
	private ResolvedConceptReference ref;
	private NamedEntityDescription mapped;
	
	@Before
	public void initialize() {
		ref = new ResolvedConceptReference();
		ref.setCodingSchemeName("codingSchemeName");
		ref.setCodingSchemeURI("codingSchemeURI");
		mapped = baseDozerBeanMapper.map(ref, NamedEntityDescription.class);
		
	}
	
	@Test
	public void testGetCode(){
		
	}

}
