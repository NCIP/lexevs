package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.List;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

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
}
