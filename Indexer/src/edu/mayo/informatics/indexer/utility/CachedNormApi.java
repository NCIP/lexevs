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
package edu.mayo.informatics.indexer.utility;

import edu.mayo.informatics.indexer.lucene.analyzers.NormAnalyzer;
import gov.nih.nlm.nls.lvg.Api.NormApi;
import gov.nih.nlm.nls.lvg.Trie.RamTrie;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.collections.map.LRUMap;

/**
 * Puts a Least-recently-used cache in front of calls to norm.Mutate.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class CachedNormApi extends NormApi {
    private LRUMap cache_ = new LRUMap(NormAnalyzer.LVG_CACHE_SIZE);

    public CachedNormApi() {
        super();
    }

    public CachedNormApi(String arg0) {
        super(arg0);

    }

    public CachedNormApi(String arg0, Hashtable arg1) {
        super(arg0, arg1);

    }

    public CachedNormApi(Connection arg0, RamTrie arg1) {
        super(arg0, arg1);
    }

    public Vector Mutate(String arg0, boolean arg1) throws Exception {
        Vector temp = (Vector) cache_.get(arg0);
        if (temp != null) {
            return temp;
        } else {
            temp = super.Mutate(arg0, arg1);
            cache_.put(arg0, temp);
            return temp;
        }
    }

    public Vector Mutate(String arg0) throws Exception {
        Vector temp = (Vector) cache_.get(arg0);
        if (temp != null) {
            return temp;
        } else {
            temp = super.Mutate(arg0);
            cache_.put(arg0, temp);
            return temp;
        }

    }
}