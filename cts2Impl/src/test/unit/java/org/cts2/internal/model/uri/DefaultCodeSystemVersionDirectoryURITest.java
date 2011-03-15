package org.cts2.internal.model.uri;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.cts2.internal.model.uri.DefaultCodeSystemVersionDirectoryURI;
import org.cts2.service.core.types.ActiveOrAll;
import org.junit.Test;

public class DefaultCodeSystemVersionDirectoryURITest {
	
	@Test
	public void Test_Active_Or_All_With_Active_Only(){
		
		
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		CodingSchemeRendering csr = new CodingSchemeRendering();
		csr.setRenderingDetail(new RenderingDetail());
		csr.getRenderingDetail().setVersionStatus(CodingSchemeVersionStatus.ACTIVE);
		
		csrl.addCodingSchemeRendering(csr);
		
		DefaultCodeSystemVersionDirectoryURI uri = new DefaultCodeSystemVersionDirectoryURI(csrl, null);
		
		CodingSchemeRenderingList returnList = 
			uri.restrictToActiveOrAll(ActiveOrAll.ACTIVE_ONLY);
		
		assertEquals(1,returnList.getCodingSchemeRenderingCount());
	}
}
