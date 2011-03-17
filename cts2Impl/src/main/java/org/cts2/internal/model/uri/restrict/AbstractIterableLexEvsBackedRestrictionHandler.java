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
package org.cts2.internal.model.uri.restrict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.NameOrURI;
import org.cts2.core.PredicateReference;
import org.cts2.core.types.TargetReferenceType;
import org.cts2.internal.match.MatchAlgorithm;
import org.cts2.internal.match.ResolvableModelAttributeReference;

/**
 * The Class AbstractIterableLexEvsBackedRestrictionHandler.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractIterableLexEvsBackedRestrictionHandler<T> implements ListBasedResolvingRestrictionHandler<T> {
	
	/** The DEFAUL t_ scor e_ threshold. */
	private static float DEFAULT_SCORE_THRESHOLD = 0.5f;

	/** The FILTE r_ orde r_ comparator. */
	private Comparator<FilterComponent> FILTER_ORDER_COMPARATOR = new FilterOrderComparator();

	/** The resolvable model attribute references. */
	private List<ResolvableModelAttributeReference<T>> resolvableModelAttributeReferences;
	
	/** The match algorithms. */
	private List<MatchAlgorithm> matchAlgorithms;	

	/**
	 * Instantiates a new abstract iterable lex evs backed restriction handler.
	 */
	public AbstractIterableLexEvsBackedRestrictionHandler(){
		super();
		this.resolvableModelAttributeReferences = this.registerSupportedModelAttributes();
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.RestrictionHandler#getSupportedPredicateReferences()
	 */
	@Override
	public List<PredicateReference> getSupportedPredicateReferences() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.ResolvingRestrictionHandler#getSupportedModelAttributes()
	 */
	@Override
	public List<ResolvableModelAttributeReference<T>> getSupportedModelAttributes() {
		return this.resolvableModelAttributeReferences;
	}
	
	/**
	 * Register supported model attributes.
	 *
	 * @return the list
	 */
	public abstract List<ResolvableModelAttributeReference<T>> registerSupportedModelAttributes();

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.ListBasedResolvingRestrictionHandler#restrict(java.util.List, org.cts2.core.Filter)
	 */
	public List<T> restrict(List<T> originalState, Filter filter) {
		originalState = Collections.unmodifiableList(originalState);
		
		List<FilterComponent> filterComponents = new ArrayList<FilterComponent>();
		
		filterComponents = Arrays.asList(filter.getComponent());
		
		Collections.sort(filterComponents, FILTER_ORDER_COMPARATOR);
		
		List<T> restrictedState = new ArrayList<T>();
		
		for (FilterComponent filterComponent : filterComponents) {
			switch (filterComponent.getFilterOperator()){
				case UNION : {
					restrictedState.addAll(this.doRestrict(originalState, filterComponent, DEFAULT_SCORE_THRESHOLD));
					break;
				}
				case INTERSECT: {
					restrictedState.retainAll(this.doRestrict(originalState, filterComponent, DEFAULT_SCORE_THRESHOLD));
					break;
				}
				case SUBTRACT: {
					restrictedState.removeAll(this.doRestrict(originalState, filterComponent, DEFAULT_SCORE_THRESHOLD));
					break;
				}
			}
		}	
		
		return restrictedState;
	}
	
	/**
	 * Do restrict.
	 *
	 * @param originalState the original state
	 * @param filterComponent the filter component
	 * @param minScore the min score
	 * @return the list
	 */
	protected List<T> doRestrict(List<T> originalState, FilterComponent filterComponent, float minScore){
		MatchAlgorithm algorithm = this.getMatchAlgorithm(filterComponent.getMatchAlgorithm());
		
		TargetReferenceType referenceType = filterComponent.getFilterComponent().getReferenceType();

		List<T> returnList = new ArrayList<T>();
		
		String matchText = filterComponent.getMatchValue();
		
		for(T candidate : originalState){
			String candidateText = 
				this.getCandidateText(filterComponent, referenceType, candidate);
			float score = algorithm.matchScore(matchText, candidateText);
			
			if(score != 0 && score >= minScore){
				returnList.add(candidate);
			}
		}

		return returnList;
	}
	
	/**
	 * Gets the candidate text.
	 *
	 * @param filterComponent the filter component
	 * @param referenceType the reference type
	 * @param candidate the candidate
	 * @return the candidate text
	 */
	private String getCandidateText(
			FilterComponent filterComponent,
			TargetReferenceType referenceType,
			T candidate) {
		String candidateText;
		
		switch (referenceType) {
			case PROPERTY : {
				throw new UnsupportedOperationException();
			}
			case ATTRIBUTE : {
				ResolvableModelAttributeReference<T> modelAttributeReference = 
					this.getResolvableModelAttributeReferences(filterComponent.getFilterComponent().getReferenceTarget());
				
				candidateText = modelAttributeReference.getModelAttributeValue(candidate);
				break;
			}
			case SPECIAL : {
				throw new UnsupportedOperationException();
			}
			
			default : {
				throw new IllegalStateException();
			}
		}
		
		return candidateText;
	}
	
	/**
	 * Gets the match algorithm.
	 *
	 * @param reference the reference
	 * @return the match algorithm
	 */
	private MatchAlgorithm getMatchAlgorithm(MatchAlgorithmReference reference){
		for(MatchAlgorithm matchAlgorithm : this.matchAlgorithms){
			if(matchAlgorithm.getName().equals(reference.getContent())){
				return matchAlgorithm;
			}
		}
		
		//TODO: validate this instead of returning null
		return null;
	}
	
	/**
	 * Gets the resolvable model attribute references.
	 *
	 * @param nameOrUri the name or uri
	 * @return the resolvable model attribute references
	 */
	private ResolvableModelAttributeReference<T> getResolvableModelAttributeReferences(NameOrURI nameOrUri){
		for(ResolvableModelAttributeReference<T> modelAttribute : this.resolvableModelAttributeReferences){
			if(modelAttribute.getContent().equals(nameOrUri.getName())){
				return modelAttribute;
			}
		}
		
		//TODO: validate this instead of returning null
		return null;
	}
	
	/**
	 * The Class FilterOrderComparator.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private static class FilterOrderComparator implements Comparator<FilterComponent>{

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(FilterComponent o1, FilterComponent o2) {
			return (int) (o1.getComponentOrder() - o2.getComponentOrder());
		}
	}

	public List<MatchAlgorithm> getMatchAlgorithms() {
		return matchAlgorithms;
	}

	public void setMatchAlgorithms(List<MatchAlgorithm> matchAlgorithms) {
		this.matchAlgorithms = matchAlgorithms;
	}
}
