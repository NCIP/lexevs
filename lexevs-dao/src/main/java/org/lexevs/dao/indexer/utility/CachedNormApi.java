/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.indexer.utility;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.collections.map.LRUMap;
import org.lexevs.dao.indexer.lucene.analyzers.NormAnalyzer;

/**
 * Puts a Least-recently-used cache in front of calls to norm.Mutate.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class CachedNormApi {
    private LRUMap cache_ = new LRUMap(NormAnalyzer.LVG_CACHE_SIZE);

    public CachedNormApi() {
        throw new UnsupportedOperationException();
    }

    public CachedNormApi(String arg0) {
        throw new UnsupportedOperationException();
    }

    public CachedNormApi(String arg0, Hashtable arg1) {
        throw new UnsupportedOperationException();
    }
    
    public Vector Mutate(String arg0, boolean arg1) throws Exception {
        throw new UnsupportedOperationException();
    }

    public Vector Mutate(String arg0) throws Exception {
        throw new UnsupportedOperationException();
    }
}