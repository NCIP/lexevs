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
package edu.mayo.informatics.indexer.lucene;

import org.apache.lucene.search.DefaultSimilarity;

/**
 * This class overrides the IDF scoring portion of the Lucene scoring algorithm.
 * See method description for details.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class IDFNeutralSimilarity extends DefaultSimilarity {
    private static final long serialVersionUID = 4291202549340371423L;

    /**
     * I'm returning a constant for idf instead of the way it used to be
     * calculated because if I had lots of documents with an other_designation
     * of "renal calculus" but only a couple of documents that contained a
     * preferred designation of "renal calculus" somewhere in the desgination -
     * the longer preferred_designation's were being scored higher than the
     * exact match other_designation.
     * 
     * In other words, by returing a constant, I should be removing the inverse
     * document frequency from the score calculation. IDF gave weight to a term
     * based on how often the term appeared in the index.
     */
    public float idf(int arg0, int arg1) {
        return 1;
    }
}