
package edu.mayo.informatics.lexgrid.convert.validator.error;

import junit.framework.Assert;

import org.LexGrid.concepts.Entity;
import org.junit.Test;

public class NullNamespaceErrorTest {

    @Test
    public void testGetErrorCode() {
        NullNamespaceError nse = new NullNamespaceError(new Entity());
        Assert.assertEquals("NULL-NAMESPACE", nse.getErrorCode());
    }

    @Test
    public void testGetErrorObjectDescription() {
        Entity e = new Entity();
        e.setEntityCode("entityCode");
        NullNamespaceError nse = new NullNamespaceError(e);
        Assert.assertEquals("Code: entityCode", nse.getErrorObjectDescription());
    }

    @Test
    public void testGetErrorDescription() {
        Entity e = new Entity();
        e.setEntityCode("entityCode");
        NullNamespaceError nse = new NullNamespaceError(e);
        Assert.assertEquals("A namespace is required.", nse.getErrorDescription());
    }

}