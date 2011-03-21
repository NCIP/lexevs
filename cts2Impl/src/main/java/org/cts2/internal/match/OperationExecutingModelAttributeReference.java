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

import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.ModelAttributeReference;
import org.cts2.core.types.SetOperator;

/**
 * The Class OperationExecutingModelAttributeReference.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class OperationExecutingModelAttributeReference<T> extends ModelAttributeReference {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5500382462242484409L;
	
	/** The operation. */
	private Operation<T> operation;
	
	/**
	 * Instantiates a new operation executing model attribute reference.
	 *
	 * @param operation the operation
	 */
	public OperationExecutingModelAttributeReference(Operation<T> operation){
		super();
		this.operation = operation;
	}

	/**
	 * Gets the model attribute value.
	 *
	 * @param stateObject the state object
	 * @param setOperator the set operator
	 * @param matchText the match text
	 * @param algorithm the algorithm
	 * @return the model attribute value
	 */
	public T executeOperation(T stateObject, SetOperator setOperator, String matchText, MatchAlgorithmReference algorithm){
		switch (setOperator){
			case UNION : {
				return this.operation.union(stateObject, matchText, algorithm);
			}
			case INTERSECT: {
				return this.operation.intersect(stateObject, matchText, algorithm);
			}
			case SUBTRACT: {
				return this.operation.subtract(stateObject, matchText, algorithm);
			}
			default : {
				throw new IllegalStateException();
			}
		}
	}

	/**
	 * The Interface Operation.
	 *
	 * @param <T> the
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static interface Operation<T> {
		
		/**
		 * Execute.
		 *
		 * @param stateObject the state object
		 * @param matchText the match text
		 * @param algorithm the algorithm
		 * @return the t
		 */
		public T union(T stateObject, String matchText, MatchAlgorithmReference algorithm);
		
		/**
		 * Intersect.
		 *
		 * @param stateObject the state object
		 * @param matchText the match text
		 * @param algorithm the algorithm
		 * @return the t
		 */
		public T intersect(T stateObject, String matchText, MatchAlgorithmReference algorithm);
		
		/**
		 * Subtract.
		 *
		 * @param stateObject the state object
		 * @param matchText the match text
		 * @param algorithm the algorithm
		 * @return the t
		 */
		public T subtract(T stateObject, String matchText, MatchAlgorithmReference algorithm);
	}
}
