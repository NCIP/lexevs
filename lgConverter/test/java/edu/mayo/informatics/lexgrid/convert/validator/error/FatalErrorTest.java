
package edu.mayo.informatics.lexgrid.convert.validator.error;

import junit.framework.Assert;

import org.LexGrid.concepts.Entity;
import org.junit.Test;

public class FatalErrorTest {

    @Test
    public void testGetErrorObjectDescription() {
        FatalError fe =  new FatalError(new Entity(), new Exception("test fatal error"));
        Assert.assertEquals("java.lang.Exception: test fatal error", fe.getErrorObjectDescription());
    }

    @Test
    public void testGetErrorDescription() {
        FatalError fe =  new FatalError(new Entity(), new Exception("test fatal error"));
        Assert.assertEquals("A Fatal Error has occured. This is not recoverable.", fe.getErrorDescription());
    }

    @Test
    public void testGetErrorMessage() {
        FatalError fe =  new FatalError(new Entity(), new Exception("test fatal error"));
        System.out.println(fe.getErrorMessage());
        Assert.assertEquals(true, fe.getErrorMessage().length() > 0);
        
    }
}