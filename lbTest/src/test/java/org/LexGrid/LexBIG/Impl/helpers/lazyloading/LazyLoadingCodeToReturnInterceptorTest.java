/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import org.aopalliance.intercept.MethodInvocation;

/**
 * The Class LazyLoadingCodeToReturnInterceptorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LazyLoadingCodeToReturnInterceptorTest extends LazyLoadableCodeToReturnTest {
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.lazyloading.LazyLoadableCodeToReturnTest#setUp()
     */
    @Override
    public void setUp() {
        super.setUp();
        LazyLoadingCodeToReturnInterceptor interceptor = new LazyLoadingCodeToReturnInterceptor();
        
        try {
            MethodInvocation mi = createMock(MethodInvocation.class);  
            expect(mi.getThis()).andReturn(super.codeToReturn);
            expectLastCall().times(2);
            expect(mi.proceed()).andReturn(super.codeToReturn);
            replay(mi);
            
            super.codeToReturn = (LazyLoadableCodeToReturn)interceptor.invoke(mi);
        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.lazyloading.LazyLoadableCodeToReturnTest#hydrate()
     */
    @Override
    public void hydrate(){
        //do nothing -- let the interceptor hydrate for us...
    }  

    /**
     * Test invoke wrong class.
     * 
     * @throws Throwable the throwable
     */
    public void testInvokeWrongClass() throws Throwable {
        LazyLoadingCodeToReturnInterceptor interceptor = new LazyLoadingCodeToReturnInterceptor();
        
        MethodInvocation mi = createMock(MethodInvocation.class);  
        expect(mi.getThis()).andReturn(new String());
        expectLastCall().times(2);
        replay(mi);
        
        boolean exceptionThrown = false;
        
        try {
            interceptor.invoke(mi);
        } catch (java.lang.IllegalArgumentException e) {
            exceptionThrown = true;
        }
        
        //fail if it doesn't.
        assertTrue("Didn't throw an exception.", exceptionThrown);
    }
}
