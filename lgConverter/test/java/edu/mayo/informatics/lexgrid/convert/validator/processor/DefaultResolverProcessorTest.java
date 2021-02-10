
package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.ArrayList;
import java.util.List;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.WrappingLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver;

public class DefaultResolverProcessorTest {

    @Test
    public void testGetResolversForCodeTrue() {
        DefaultResolverProcessor processorResolver = new DefaultResolverProcessor();
        
        Resolver resolver = EasyMock.createMock(Resolver.class);
        EasyMock.expect(resolver.isResolverValidForError("123")).andReturn(true);
        EasyMock.replay(resolver);
        
        processorResolver.addResolver(resolver);
        
        List<Resolver> resolverList = processorResolver.getResolversForCode("123");
        
        Assert.assertEquals(1, resolverList.size());    
    }
    
    @Test
    public void testGetResolversForCodeFalse() {
        DefaultResolverProcessor processorResolver = new DefaultResolverProcessor();
        
        Resolver resolver = EasyMock.createMock(Resolver.class);
        EasyMock.expect(resolver.isResolverValidForError("123")).andReturn(false);
        EasyMock.replay(resolver);
        
        processorResolver.addResolver(resolver);
        
        List<Resolver> resolverList = processorResolver.getResolversForCode("123");
        
        Assert.assertEquals(0, resolverList.size());    
    }
    
    @Test
    public void testResolve() {
        DefaultResolverProcessor processorResolver = new DefaultResolverProcessor();
        
        LoadValidationError err = EasyMock.createMock(LoadValidationError.class);
        EasyMock.expect(err.getErrorCode()).andReturn("123");
        EasyMock.replay(err);
        
        List<LoadValidationError> errList = new ArrayList<LoadValidationError>();
        errList.add(err);
        
        Resolver resolver = EasyMock.createMock(Resolver.class);
        EasyMock.expect(resolver.isResolverValidForError("123")).andReturn(true);
        EasyMock.expect(resolver.resolveError(err)).andReturn(new WrappingLoadValidationError(null, null));
        EasyMock.replay(resolver);
        
        processorResolver.addResolver(resolver);
      
        List<ResolvedLoadValidationError> l = processorResolver.resolve(errList);
        
        Assert.assertEquals(1, l.size());
        
    }
}