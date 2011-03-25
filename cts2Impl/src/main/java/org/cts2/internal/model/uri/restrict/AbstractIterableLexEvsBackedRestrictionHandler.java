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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.NameOrURI;
import org.cts2.core.PredicateReference;
import org.cts2.core.types.SetOperator;
import org.cts2.core.types.TargetReferenceType;
import org.cts2.internal.match.MatchAlgorithm;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.restriction.RestrictionState;

import com.google.common.collect.Iterables;

/**
 * The Class AbstractIterableLexEvsBackedRestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractIterableLexEvsBackedRestrictionHandler<T,D extends DirectoryURI> extends AbstractRestrictionHandler 
	implements IterableBasedResolvingRestrictionHandler<T,D> {
	
	private static float DEFAULT_SCORE_THRESHOLD = 0.5f;
	
	/** The resolvable model attribute references. */
	private List<ResolvableModelAttributeReference<T>> resolvableModelAttributeReferences;
	
	/** The match algorithms. */
	private List<MatchAlgorithm> matchAlgorithms;	

	/**
	 * Instantiates a new abstract iterable lex evs backed restriction handler.
	 */
	public AbstractIterableLexEvsBackedRestrictionHandler(){
		super();
		this.resolvableModelAttributeReferences = this.registerSupportedModelAttributeReferences();
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
	public List<ResolvableModelAttributeReference<T>> getSupportedModelAttributeReferences() {
		return this.resolvableModelAttributeReferences;
	}
	
	/**
	 * Register supported model attributes.
	 *
	 * @return the list
	 */
	public abstract List<ResolvableModelAttributeReference<T>> registerSupportedModelAttributeReferences();
	
	public abstract List<IterableRestriction<T>> processOtherRestictions(D directoryUri);
	
	public Iterable<T> apply(IterableRestriction<T> restriction, Iterable<T> state){
		return restriction.processRestriction(state);
	}
	
	public IterableRestriction<T> processSetOperation(final D i1, final D i2, final SetOperator setOperator) {
		return new IterableRestriction<T>(){

			@Override
			public Iterable<T> processRestriction(Iterable<T> state) {
				Set<T> set1 = new HashSet<T>();
				Set<T> set2 = new HashSet<T>();

				IterableRestriction<T> restrictions1 = compile(i1);
				IterableRestriction<T> restrictions2 = compile(i2);

				Iterables.addAll(set1, restrictions1.processRestriction(state));

				Iterables.addAll(set2, restrictions2.processRestriction(state));

				switch (setOperator){
					case UNION : {
						set1.addAll(set2);
						return set1;
					}
					case INTERSECT : {
						set1.retainAll(set2);
						return set1;
					}
					case SUBTRACT : {
						set1.removeAll(set2);
						return set1;
					}
				}
				
				throw new IllegalStateException();
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.ListBasedResolvingRestrictionHandler#restrict(java.util.List, org.cts2.core.Filter)
	 */
	@SuppressWarnings("unchecked")
	public IterableRestriction<T> compile(D directoryURI) {
		RestrictionState<? extends DirectoryURI> restriction = directoryURI.getRestrictionState();

		final List<IterableRestriction<T>> returnList = new ArrayList<IterableRestriction<T>>();

		for(final Filter filter : restriction.getFilters()){

			returnList.add(new IterableRestriction<T>(){

				@Override
				public Iterable<T> processRestriction(Iterable<T> originalState) {

					originalState = Iterables.unmodifiableIterable(originalState);

					List<FilterComponent> filterComponents = Arrays.asList(filter.getComponent());

					Collection<T> restrictedState = new ArrayList<T>();

					for (FilterComponent filterComponent : filterComponents) {
						restrictedState.addAll(doRestrict(originalState, filterComponent, DEFAULT_SCORE_THRESHOLD));
					}

					return restrictedState;
				};
			});
		}
		
		List<IterableRestriction<T>> otherRestrictions = this.processOtherRestictions(directoryURI);
		if(CollectionUtils.isNotEmpty(otherRestrictions)){
			returnList.addAll(otherRestrictions);
		}
		
		if(restriction.getSetComposite() != null){
			returnList.add(this.processSetOperation(
					(D)restriction.getSetComposite().getDirectoryUri1(), 
					(D)restriction.getSetComposite().getDirectoryUri2(),
					restriction.getSetComposite().getSetOperator()));
		}

		return RestrictionUtils.combineIterableRestrictions(returnList);
	}
	
	

	/**
	 * Do restrict.
	 *
	 * @param originalState the original state
	 * @param filterComponent the filter component
	 * @param minScore the min score
	 * @return the list
	 */
	protected Collection<T> doRestrict(Iterable<T> originalState, FilterComponent filterComponent, float minScore){
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
	 * Gets the match algorithm.
	 *
	 * @param reference the reference
	 * @return the match algorithm
	 */
	protected MatchAlgorithm getMatchAlgorithm(MatchAlgorithmReference reference){
		for(MatchAlgorithm matchAlgorithm : this.matchAlgorithms){
			if(matchAlgorithm.getName().equals(reference.getContent())){
				return matchAlgorithm;
			}
		}
		
		//TODO: validate this instead of returning null
		return null;
	}
	
	/**
	 * Gets the match algorithms.
	 *
	 * @return the match algorithms
	 */
	public List<MatchAlgorithm> getMatchAlgorithms() {
		return matchAlgorithms;
	}

	/**
	 * Sets the match algorithms.
	 *
	 * @param matchAlgorithms the new match algorithms
	 */
	public void setMatchAlgorithms(List<MatchAlgorithm> matchAlgorithms) {
		this.matchAlgorithms = matchAlgorithms;
	}
}
