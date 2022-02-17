
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class GForge27457 extends LexBIGServiceTestCase{
	final static String testID = "GForge27457";
    
    @Override
    protected String getTestID() {
        return testID;
    }

    public void testResolveWithQualifierAndValue() throws Exception {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();

    	CodedNodeSet cns = lbs.getNodeSet(META_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(SAMPLE_META_VERSION), null);
    	NameAndValueList nvList = Constructors.createNameAndValueList("CVF", "cvf");

    	LocalNameList lcList = Constructors.createLocalNameList("presentation");
    	cns = cns.restrictToProperties(lcList, null, null, null, nvList);
    	ResolvedConceptReferencesIterator iterator = cns.resolve(null, null,
    			null, null, true);
    	if (!iterator.hasNext()){
    		fail("no entity found");
    	}
    	
    	assertEquals("C0000039", iterator.next().getCode());
    
    	assertFalse(iterator.hasNext());
    }
    
    public void testResolveWithQualifierAndBlankValue() throws Exception {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();

    	CodedNodeSet cns = lbs.getNodeSet(META_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(SAMPLE_META_VERSION), null);
    	NameAndValueList nvList = Constructors.createNameAndValueList("CVF", "");

    	LocalNameList lcList = Constructors.createLocalNameList("presentation");
    	cns = cns.restrictToProperties(lcList, null, null, null, nvList);
    	ResolvedConceptReferencesIterator iterator = cns.resolve(null, null,
    			null, null, true);
    	
    	if (!iterator.hasNext()){
    		fail("no entity found");
    	}
    	
    	assertEquals("C0000039", iterator.next().getCode());
        
    	assertFalse(iterator.hasNext());
    }
    
    public void testResolveWithQualifierAndNullValue() throws Exception {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();

    	CodedNodeSet cns = lbs.getNodeSet(META_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(SAMPLE_META_VERSION), null);
    	NameAndValueList nvList = Constructors.createNameAndValueList("CVF", null);

    	LocalNameList lcList = Constructors.createLocalNameList("presentation");
    	cns = cns.restrictToProperties(lcList, null, null, null, nvList);
    	ResolvedConceptReferencesIterator iterator = cns.resolve(null, null,
    			null, null, true);
    	if (!iterator.hasNext()){
    		fail("no entity found");
    	}
    	
    	assertEquals("C0000039", iterator.next().getCode());
        
    	assertFalse(iterator.hasNext());
    }
}