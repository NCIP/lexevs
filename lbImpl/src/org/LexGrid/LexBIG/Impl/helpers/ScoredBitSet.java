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
package org.LexGrid.LexBIG.Impl.helpers;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Hashtable;

import org.LexGrid.annotations.LgClientSideSafe;

/**
 * BitSet - extended to keep track of corresponding lucene scores.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ScoredBitSet extends BitSet {
    private static final long serialVersionUID = -1184756858948249255L;
    private Hashtable<Integer, Float> scores_;

    /**
     * Create a new ScoredBitSet object.
     * 
     */
    public ScoredBitSet() {
        super();
        scores_ = new Hashtable<Integer, Float>();
    }

    /**
     * Create new ScoredBitSet object with an initial size of nBits.
     * 
     * @param nbits
     */
    public ScoredBitSet(int nbits) {
        super(nbits);
        // scores are only saved for bits which are set to true...
        scores_ = new Hashtable<Integer, Float>(nbits / 4);
    }

    /**
     * Set a bit to true, and record its score.
     * 
     * @param bitIndex
     * @param score
     */
    public void set(int bitIndex, float score) {
        super.set(bitIndex);
        scores_.put(new Integer(bitIndex), new Float(score));
    }

    /**
     * Get the score at a bitIndex. Returns null if no score present.
     * 
     * @param bitIndex
     * @return
     */
    @LgClientSideSafe
    public Float getScore(int bitIndex) {
        return (Float) scores_.get(new Integer(bitIndex));
    }

    /**
     * Set the score for a particular bit.
     * 
     * @param bitIndex
     * @param score
     */
    @LgClientSideSafe
    public void setScore(int bitIndex, float score) {
        scores_.put(new Integer(bitIndex), new Float(score));
    }

    /**
     * Remove scores which correspond to a bit which is no longer set.
     * 
     * Useful after and'ing sets together.
     */
    @LgClientSideSafe
    public void cleanUpScores() {
        Enumeration<Integer> e = scores_.keys();
        while (e.hasMoreElements()) {
            Integer temp = (Integer) e.nextElement();
            // If this bit is no longer set, blow away the score.
            if (!this.get(temp.intValue())) {
                scores_.remove(temp);
            }
        }
    }

    /**
     * Sums the scores of two bit sets - use this after and'ing two bitsets
     * together. Also runs a clean up afterwords.
     * 
     * @param bits
     */
    @LgClientSideSafe
    public void mergeScores(ScoredBitSet bits) {
        for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
            Float incomingScore = bits.getScore(i);
            if (incomingScore != null) {
                Float currentScore = this.getScore(i);
                if (currentScore != null) {
                    this.setScore(i, (incomingScore + currentScore) / 2);
                } else {
                    this.setScore(i, incomingScore);
                }
            }
        }

        cleanUpScores();
    }

    /**
     * And two bitsets together.
     */
    @LgClientSideSafe
    public void and(ScoredBitSet set) {
        this.and((BitSet) set);
        this.mergeScores(set);
    }

}