package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.cts2.codesystem.CodeSystemDirectory;
import org.junit.Test;

public class CodingSchemeRenderingListToCodeSystemDirectoryTest extends BaseDozerBeanMapperTest {
	
	@Test
	public void CodingSchemeRenderingList_To_CodeSystemDirectory_Count(){
		
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		CodingSchemeRendering csr = new CodingSchemeRendering();
		csrl.addCodingSchemeRendering(csr);
		
		CodeSystemDirectory csd = baseDozerBeanMapper.map(csrl, CodeSystemDirectory.class);
		
		assertEquals(1, csd.getEntryCount());
	}
	
	@Test
	public void CodingSchemeRenderingList_To_CodeSystemDirectoryUri_About(){
		
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
