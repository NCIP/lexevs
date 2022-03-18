
package edu.mayo.informatics.lexgrid.convert.validator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.easymock.EasyMock;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;


public class NullNamespaceValidatorTest {

    @Test
    public final void testDoGetValidClassesTrue() {
        NullNamespaceValidator nsv = new NullNamespaceValidator();
        List<Class<?>> cList = nsv.doGetValidClasses();
        Assert.assertEquals(3, cList.size());
        Assert.assertEquals(true, cList.contains(Entity.class));
        Assert.assertEquals(true, cList.contains(AssociationSource.class));
        Assert.assertEquals(true, cList.contains(AssociationTarget.class));
    }

    @Test
    public final void testDoValidateTrue() {
        NullNamespaceValidator nsv = new NullNamespaceValidator();
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        Entity e  = new Entity();
        e.setEntityCodeNamespace("namespace");
        nsv.doValidate(e, errors);
        
        Assert.assertEquals(0, errors.size());
        
        errors = new ArrayList<LoadValidationError>();
        AssociationSource as = new AssociationSource();
        as.setSourceEntityCodeNamespace("namespace");
        nsv.doValidate(as, errors);
        
        Assert.assertEquals(0, errors.size());
        
        errors = new ArrayList<LoadValidationError>();
        AssociationTarget at = new AssociationTarget();
        at.setTargetEntityCodeNamespace("namespace");
        nsv.doValidate(at, errors);
        
        Assert.assertEquals(0, errors.size());
        
    }
    
    @Test
    public final void testDoValidateFalse() {
        NullNamespaceValidator nsv = new NullNamespaceValidator();
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        Entity e  = new Entity();
        e.setEntityCodeNamespace("");
        nsv.doValidate(e, errors);
        
        Assert.assertEquals(1, errors.size());
        
        AssociationSource as = new AssociationSource();
        nsv.doValidate(as, errors);
        
        Assert.assertEquals(2, errors.size());
        
        AssociationTarget at = new AssociationTarget();
        at.setTargetEntityCodeNamespace(" ");
        nsv.doValidate(at, errors);
        
        Assert.assertEquals(3, errors.size());
        
    }

    @Test
    public final void testValidateFalse() {
        NullNamespaceValidator nsv = new NullNamespaceValidator();
        
        Entity e = new Entity();
        e.setEntityCodeNamespace(null);
        Assert.assertEquals(1, nsv.validate(e).size());
        e.setEntityCodeNamespace("");
        Assert.assertEquals(1, nsv.validate(e).size());
        e.setEntityCodeNamespace("    ");
        Assert.assertEquals(1, nsv.validate(e).size());
        
        AssociationSource as = new AssociationSource();
        as.setSourceEntityCodeNamespace(null);
        Assert.assertEquals(1, nsv.validate(as).size());
        as.setSourceEntityCodeNamespace("");
        Assert.assertEquals(1, nsv.validate(as).size());
        as.setSourceEntityCodeNamespace("   ");
        Assert.assertEquals(1, nsv.validate(as).size());
        
        AssociationTarget at = new AssociationTarget();
        at.setTargetEntityCodeNamespace(null);
        Assert.assertEquals(1, nsv.validate(at).size());
        at.setTargetEntityCodeNamespace("");
        Assert.assertEquals(1, nsv.validate(at).size());
        at.setTargetEntityCodeNamespace("    ");
        Assert.assertEquals(1, nsv.validate(at).size());
    }

    @Test
    public final void testValidateException() {
        NullNamespaceValidator nsv = new NullNamespaceValidator();
        Text t = new Text();
        try {
            nsv.validate(t);
            fail("an exception of illegal object should be thrown");
        }
        catch(Exception e){
            //success
        }
    }
    
    @Test
    public final void testValidateTrue() {
        NullNamespaceValidator nsv = new NullNamespaceValidator();
        
        Entity e = new Entity();
        e.setEntityCodeNamespace("namespace");
        Assert.assertEquals(0, nsv.validate(e).size());
        
        AssociationSource as = new AssociationSource();
        as.setSourceEntityCodeNamespace("namespace");
        Assert.assertEquals(0, nsv.validate(as).size());
        
        AssociationTarget at = new AssociationTarget();
        at.setTargetEntityCodeNamespace("namespace");
        Assert.assertEquals(0, nsv.validate(at).size());
    }

    @Test
    public final void testIsValidClassForValidator() {
        NullNamespaceValidator nsv = new NullNamespaceValidator();
        Assert.assertEquals(true, nsv.isValidClassForValidator((new Entity()).getClass()));
        Assert.assertEquals(true, nsv.isValidClassForValidator((new AssociationSource()).getClass()));
        Assert.assertEquals(true, nsv.isValidClassForValidator((new AssociationTarget()).getClass()));
        Assert.assertEquals(false, nsv.isValidClassForValidator((new Text()).getClass()));
        
        
    }

}