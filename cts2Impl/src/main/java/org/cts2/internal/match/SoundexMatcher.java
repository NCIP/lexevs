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
package org.cts2.internal.match;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.RefinedSoundex;

/**
 * The Class SoundexMatcher.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SoundexMatcher extends AbstractMatcher {

	/** The refined soundex. */
	private RefinedSoundex refinedSoundex = new RefinedSoundex();
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.match.AbstractMatcher#doIsMatch(java.lang.String, java.lang.String)
	 */
	@Override
	protected float doMatchScore(String matchText, String cadidate) {
		float score;
		try {
			score = this.refinedSoundex.difference(matchText, cadidate);
		} catch (EncoderException e) {
			throw new IllegalStateException(e);
		}
		
		int matchTextEncodedLength = this.getEncodedStringLength(matchText);
		int candidateEncodedLength = this.getEncodedStringLength(cadidate);

		return this.normalizeScore(score, Math.min(matchTextEncodedLength, candidateEncodedLength));
	}
	
	/**
	 * Gets the encoded string length.
	 *
	 * @param text the text
	 * @return the encoded string length
	 */
	private int getEncodedStringLength(String text){
		return this.refinedSoundex.encode(text).length();
	}
	
	/**
	 * Normalize score.
	 *
	 * @param score the score
	 * @param shortestEncodedLength the shortest encoded length
	 * @return the float
	 */
	protected float normalizeScore(float score, int shortestEncodedLength){
		return score / shortestEncodedLength;
	}
}
