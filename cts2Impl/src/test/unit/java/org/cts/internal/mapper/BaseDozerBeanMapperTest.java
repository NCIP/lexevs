package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.cts.test.BaseCts2UnitTest;
import org.cts2.codesystem.CodeSystemDirectory;
import org.cts2.codesystemversion.CodeSystemVersionDirectoryEntry;
import org.cts2.internal.mapper.BaseDozerBeanMapper;
import org.junit.Test;

public class BaseDozerBeanMapperTest extends BaseCts2UnitTest{
	
	@Resource
	private BaseDozerBeanMapper baseDozerBeanMapper;
	
	@Test
	public void testInitDozer(){
		assertNotNull(baseDozerBeanMapper);
	}
	
	@Test
	public void testMapCodeSystemSummary(){
		
		CodingSchemeSummary css = new CodingSchemeSummary();
		css.setFormalName("test formal name");
		
		CodeSystemVersionDirectoryEntry mapped = 
			baseDozerBeanMapper.map(css, CodeSystemVersionDirectoryEntry.class);
		
		assertEquals("test formal name", mapped.getFormalName());
		
	}
	
	@Test
	public void CodingSchemeRenderingListToCodeSystemDirectoryCount(){
		
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		CodingSchemeRendering csr = new CodingSchemeRendering();
		csrl.addCodingSchemeRendering(csr);
		
		CodeSystemDirectory csd = baseDozerBeanMapper.map(csrl, CodeSystemDirectory.class);
		
		assertEquals(1, csd.getEntryCount());
	}
	
	@Test
	public void CodingSchemeRenderingListToCodeSystemDirectoryUriToAbout(){
		
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		CodingSchemeRendering csr = new CodingSchemeRendering();
		CodingSchemeSummary css = new CodingSchemeSummary();
		css.setCodingSchemeURI("someUri");
		csr.setCodingSchemeSummary(css);
		csrl.addCodingSchemeRendering(csr);
		
		CodeSystemDirectory csd = baseDozerBeanMapper.map(csrl, CodeSystemDirectory.class);
		
		assertEquals("someUri", csd.getEntry(0).getAbout());
	}
}
