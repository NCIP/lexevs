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

import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.apache.lucene.search.ScoreDoc;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

/**
 * A factory for creating LazyCodeHolder objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AopCodeHolderFactory extends AbstractLazyCodeHolderFactory {

    private static final long serialVersionUID = 5073632765208225313L;
    
    /** The Constant pattern. */
    private static final String pattern = ".*get.*";

    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.lazyloading.AbstractLazyCodeHolderFactory#buildCodeToReturn(org.apache.lucene.search.ScoreDoc, java.lang.String, java.lang.String)
     */
    @Override
    protected CodeToReturn buildCodeToReturn(ScoreDoc doc, String internalCodeSystemName, String internalVersionString) {
        AopLazyCodeToReturn codeToReturn = new AopLazyCodeToReturn(doc, internalCodeSystemName, internalVersionString);
        return makeProxy(codeToReturn);
    }

    /**
     * Make proxy.
     * 
     * @param codToReutrn the cod to reutrn
     * 
     * @return the code to return
     */
    protected static CodeToReturn makeProxy(LazyLoadableCodeToReturn codToReutrn){
        JdkRegexpMethodPointcut pc = new JdkRegexpMethodPointcut();

        pc.setPattern(pattern);
          
        Advisor advisor = new DefaultPointcutAdvisor(pc, new LazyLoadingCodeToReturnInterceptor());
            
        ProxyFactory pf = new ProxyFactory(codToReutrn);
        pf.setProxyTargetClass(true);
        
        pf.addAdvisor(advisor);
        
        Object obj = pf.getProxy();
        return (CodeToReturn)obj;
    }
}
