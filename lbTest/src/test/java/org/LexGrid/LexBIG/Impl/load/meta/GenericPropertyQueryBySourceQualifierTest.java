
package org.LexGrid.LexBIG.Impl.load.meta;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class GenericPropertySourceQualifierTestIT
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class GenericPropertyQueryBySourceQualifierTest extends DataLoadTestBase {
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		NameAndValueList qualifierList = Constructors.createNameAndValueList("source-code", "D012711");
		LocalNameList sourceList = Constructors.createLocalNameList("MSH");
		cns.restrictToProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, sourceList, null, qualifierList);

	}
	
	@Test
	public void testQueryBySourceAndQual() throws Exception {
		ResolvedConceptReference[] rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
		
	
		assertTrue(rcr1.length > 0);

	}
}