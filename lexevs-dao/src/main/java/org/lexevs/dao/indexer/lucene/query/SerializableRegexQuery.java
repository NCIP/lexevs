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
package org.lexevs.dao.indexer.lucene.query;

import java.io.Serializable;

import org.apache.lucene.index.Term;
import org.apache.lucene.sandbox.queries.regex.RegexQuery;;  //org.apache.lucene.search.RegexpQuery; replace with this one?

/**
 * The Class SerializableRegexQuery.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SerializableRegexQuery extends RegexQuery implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2239755266727442903L;

    /**
     * Instantiates a new serializable regex query.
     * 
     * @param term the term
     */
    public SerializableRegexQuery(Term term) {
        super(term);
        this.setRegexImplementation(new SerializableRegexCapabilities());
      }
}