package org.cts.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.codesystemversion.CodeSystemVersionDirectoryEntry;
import org.junit.Test;

public class CsrToCsvdeTest extends BaseDozerBeanMapperTest {
	
	@Test
	public void testFormalName(){
		CodingSchemeRendering csr = new CodingSchemeRendering();
		CodingSchemeSummary css = new CodingSchemeSummary();
		css.setFormalName("test formal name");
		csr.setCodingSchemeSummary(css);
		
		CodeSystemVersionDirectoryEntry mapped = 
			baseDozerBeanMapper.map(csr, CodeSystemVersionDirectoryEntry.class);
		
		assertEquals("test formal name", mapped.getFormalName());
		
	}
	
	@Test
	public void testEntityDescription(){
		CodingSchemeRendering csr = new CodingSchemeRendering();
		CodingSchemeSummary css = new CodingSchemeSummary();
		css.setCodingSchemeDescription(Constructors.createEntityDescription("test desc"));
		csr.setCodingSchemeSummary(css);
		
		CodeSystemVersionDirectoryEntry mapped = 
			baseDozerBeanMapper.map(csr, CodeSystemVersionDirectoryEntry.class);
		
		assertEquals("test desc", mapped.getResourceSynopsis().getValue());
		
	}
}
