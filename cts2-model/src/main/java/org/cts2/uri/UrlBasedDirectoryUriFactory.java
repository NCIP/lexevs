/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.uri;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.cts2.core.Directory;
import org.cts2.core.Filter;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

/**
 * The Class UrlBasedDirectoryUriFactory.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UrlBasedDirectoryUriFactory {

    /**
     * Creates the url based directory uri.
     *
     * @param targetClass the target class
     * @param uri the uri
     * @return the DirectoryURI
     */
    @SuppressWarnings("unchecked")
	public static <T extends DirectoryURI> T createUrlBasedDirectoryUri(String uri, Class<T> targetClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UrlStringBasedDirectoryURI.class);
        enhancer.setInterfaces(new Class[]{targetClass});
        enhancer.setCallback(NoOp.INSTANCE);
        return (T)enhancer.create(new Class[]{String.class}, new Object[]{uri});
   }

    /**
     * The Class UrlStringBasedDirectoryURI.
     *
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public static class UrlStringBasedDirectoryURI implements DirectoryURI {

    	/** The url. */
	    private String url;
    	
    	/**
	     * Instantiates a new url string based directory uri.
	     *
	     * @param url the url
	     */
	    public UrlStringBasedDirectoryURI(String url){
    		this.url = url;
    	}
    	
		/* (non-Javadoc)
		 * @see org.cts2.uri.DirectoryURI#get(org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext, java.lang.Class)
		 */
		@Override
		public <T extends Directory<?>> T get(QueryControl queryControl,
				ReadContext readContext, Class<T> content) {
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.cts2.uri.DirectoryURI#count(org.cts2.service.core.ReadContext)
		 */
		@Override
		public int count(ReadContext readContext) {
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.cts2.uri.DirectoryURI#restrict(org.cts2.core.Filter)
		 */
		@Override
		public DirectoryURI restrict(Filter filter) {
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.cts2.uri.DirectoryURI#marshall()
		 */
		@Override
		public String marshall() {
			return this.url;
		}
    }
}
