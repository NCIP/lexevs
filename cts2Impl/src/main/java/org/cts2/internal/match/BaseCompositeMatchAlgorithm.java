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

/**
 * The Class AbstractMatchAlgorithm.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BaseCompositeMatchAlgorithm implements MatchAlgorithm {
	
	/** The name. */
	private String name;
	
	/** The algorithm. */
	private Matcher algorithm;
	
	/**
	 * Instantiates a new abstract match algorithm.
	 *
	 * @param name the name
	 * @param algorithm the algorithm
	 */
	public BaseCompositeMatchAlgorithm(String name, Matcher algorithm){
		this.name = name;
		this.algorithm = algorithm;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.match.MatchAlgorithm#isMatch(java.lang.String, java.lang.Object)
	 */
	@Override
	public float matchScore(String matchText, String compareString) {
		return this.algorithm.matchScore(matchText, compareString);
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.match.MatchAlgorithm#getName()
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * The Interface Matcher.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static interface Matcher {
		
		/**
		 * Checks if is match.
		 *
		 * @param matchText the match text
		 * @param cadidate the cadidate
		 * @return true, if is match
		 */
		public float matchScore(String matchText, String cadidate);
	}
}
