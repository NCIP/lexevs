
package edu.mayo.informatics.lexgrid.convert.validator.error;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport.ResolutionStatus;

public class WrappingLoadValidationErrorTest {
    public LoadValidationError lvd;
    public  WrappingLoadValidationError wle;
    
    @Before
    public void setUp() throws Exception {
        lvd = EasyMock.createMock(LoadValidationError.class);
        EasyMock.expect(lvd.getErrorCode()).andReturn("123");
        EasyMock.expect(lvd.getErrorDescription()).andReturn("error desc");
        EasyMock.expect(lvd.getErrorMessage()).andReturn("error message");
        EasyMock.expect(lvd.getErrorObject()).andReturn(new String("error object"));
        EasyMock.replay(lvd);
        wle = new WrappingLoadValidationError(lvd);
    }
    
    @After
    public void tearDown() throws Exception {
        lvd = null;
        wle = null;
    }

    @Test
    public void testBuildDefaultUnresolvedReport() {
        Assert.assertEquals("NONE",wle.getErrorResolutionReport().getResolutionDetails());
        Assert.assertEquals(ResolutionStatus.NOT_ADDRESSED, wle.getErrorResolutionReport().getResolutionStatus());
    }

    @Test
    public void testGetErrorCode() {
        Assert.assertEquals("123", wle.getErrorCode());
    }

    @Test
    public void testGetErrorDescription() {
        Assert.assertEquals("error desc", wle.getErrorDescription());
    }

    @Test
    public void testGetErrorMessage() {
        Assert.assertEquals("error message", wle.getErrorMessage());
    }

    @Test
    public void testGetErrorObject() {
        Assert.assertEquals(true, wle.getErrorObject().equals(new String("error object")));
    }

}