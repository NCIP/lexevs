
package org.LexGrid.LexBIG.Impl.function.query;

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class ValidateHashCode extends LexBIGServiceTestCase {
    final static String testID = "T1_FNC_16";

    @Override
    protected String getTestID() {
        return testID;
    }

    @Test
    public void testCSRHashCodEquals() throws LBException {

        CodingSchemeReference csr = new CodingSchemeReference();
        Set<CodingSchemeReference> refList = new HashSet<CodingSchemeReference>();
        refList.add(csr);
        csr.setCodingScheme("MyScheme");
        refList.add(csr);
        csr.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromTag("Production"));
        refList.add(csr);
        csr.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion("version"));
        refList.add(csr);
        CodingSchemeReference csr1 = new CodingSchemeReference();
        Set<CodingSchemeReference> refList1 = new HashSet<CodingSchemeReference>();
        refList1.add(csr1);
        csr1.setCodingScheme("MyScheme");
        refList1.add(csr1);
        csr1.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion("version"));
        refList1.add(csr1);
        csr1.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromTag("Production"));
        refList1.add(csr1);
        
        assertFalse(csr.equals(csr1));
        csr1.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion("version"));
        assertTrue(csr.equals(csr1));
        csr1.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromTag("Production"));
        assertFalse(csr.equals(csr1));
    }
}