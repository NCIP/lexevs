package org.cts2.test;

import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.NameOrURI;
import org.cts2.core.PropertyReference;
import org.cts2.core.types.TargetReferenceType;

public class TestUtils {
	
	public static Filter buildFilter(FilterComponent... filterComponents){
		Filter filter = new Filter();
		filter.setComponent(filterComponents);
		
		return filter;
	}
	
	public static FilterComponent buildFilterComponent(
			String propetyRefName, 
			TargetReferenceType type, 
			String matchAlgorithm, 
			String matchValue){
		
		FilterComponent testFilterComponent = new FilterComponent();
		
		PropertyReference pref = new PropertyReference();
		pref.setReferenceType(type);
		
		NameOrURI nameOrURI = new NameOrURI();
		nameOrURI.setName(propetyRefName);
		
		pref.setReferenceTarget(nameOrURI);
		
		testFilterComponent.setFilterComponent(pref);
		
		MatchAlgorithmReference ref = new MatchAlgorithmReference();
		ref.setContent(matchAlgorithm);
		
		testFilterComponent.setMatchAlgorithm(ref);
		
		testFilterComponent.setMatchValue(matchValue);
		
		return testFilterComponent;
	}
}
