
package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import junit.framework.Assert;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.NullNamespaceError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport.ResolutionStatus;

public class NullNamespaceResolverTest {
    private CodingScheme csNullNs;
    private CodingScheme cs;
    private NullNamespaceResolver r; 
    
    @Before
    public void setUp() throws Exception {
        csNullNs = new CodingScheme();
        cs = new CodingScheme();
        cs.setCodingSchemeName("coding scheme name");
    }

    @After
    public void tearDown() throws Exception {
        csNullNs = null;
        cs = null;
        r = null;
    }

    @Test
    public void testDoGetValidErrorCodes() {
        r = new NullNamespaceResolver(cs);
        Assert.assertEquals(true, r.doGetValidErrorCodes().size() > 0);
        
        r = new NullNamespaceResolver(csNullNs);
        Assert.assertEquals(true, r.doGetValidErrorCodes().size() > 0);
    }

    @Test
    public void testGetDefaultNamespaceFromCodingScheme() {
        r = new NullNamespaceResolver(cs);
        Assert.assertEquals("coding scheme name", r.getDefaultNamespaceFromCodingScheme(cs));
        
        r = new NullNamespaceResolver(csNullNs);
        Assert.assertEquals(null, r.getDefaultNamespaceFromCodingScheme(csNullNs));
    }

    @Test
    public void testIsResolverValidForError() {
        r = new NullNamespaceResolver(cs);
        Assert.assertEquals(false, r.isResolverValidForError("errorCode"));
        Assert.assertEquals(true, r.isResolverValidForError(NullNamespaceError.NULL_NAMESPACE_CODE));
    }

    @Test
    public void testResolveError() {
        r = new NullNamespaceResolver(cs);
        LoadValidationError err = EasyMock.createMock(LoadValidationError.class);
        EasyMock.expect(err.getErrorObject()).andReturn(new Entity());
        EasyMock.replay(err);
        
        Assert.assertEquals(ResolutionStatus.RESOLVED, r.resolveError(err).getErrorResolutionReport().getResolutionStatus());
        
        err = EasyMock.createMock(LoadValidationError.class);
        EasyMock.expect(err.getErrorObject()).andReturn(new Entity());
        EasyMock.replay(err);
        
        r = new NullNamespaceResolver(csNullNs);
        Assert.assertEquals(ResolutionStatus.RESOLUTION_FAILED, r.resolveError(err).getErrorResolutionReport().getResolutionStatus());
    }
}