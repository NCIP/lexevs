package org.cts2.internal.model.uri.restrict;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.NameOrURI;
import org.cts2.core.PropertyReference;
import org.cts2.core.types.SetOperator;
import org.cts2.core.types.TargetReferenceType;
import org.cts2.internal.match.AttributeResolver;
import org.cts2.internal.match.MatchAlgorithm;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class AbstractIterableLexEvsBackedRestrictionHandlerTest {

	private AbstractIterableLexEvsBackedRestrictionHandler<TestThing> handler;
	
	@Before
	public void buildTestHandler(){
		this.handler = 
			new AbstractIterableLexEvsBackedRestrictionHandler<TestThing>(){

				@Override
				public List<ResolvableModelAttributeReference<TestThing>> registerSupportedModelAttributes() {
					List<ResolvableModelAttributeReference<TestThing>> returnList = new ArrayList<ResolvableModelAttributeReference<TestThing>>();
					
					ResolvableModelAttributeReference<TestThing> ref = new ResolvableModelAttributeReference<TestThing>(
							new AttributeResolver<TestThing>(){

								@Override
								public String resolveAttribute(
										TestThing modelObject) {
									return modelObject.testAttribute;
								}		
							});
					
					ResolvableModelAttributeReference<TestThing> ref2 = new ResolvableModelAttributeReference<TestThing>(
							new AttributeResolver<TestThing>(){

								@Override
								public String resolveAttribute(
										TestThing modelObject) {
									return modelObject.testOtherAttribute;
								}		
							});
	
					ref.setContent("firstAttribute");
					returnList.add(ref);
					
					ref2.setContent("secondAttribute");
					returnList.add(ref2);
					
					return returnList;
				}
		};
		
		MatchAlgorithm testAlgorithm = new MatchAlgorithm(){

			@Override
			public float matchScore(String matchText, String compareString) {
				if(matchText.equals(compareString)){
					return 1;
				} else {
					return 0;
				}
			}

			@Override
			public String getName() {
				return "test-algorithm";
			}
			
		};
		
		this.handler.setMatchAlgorithms(Arrays.asList(testAlgorithm));
	}
	
	@Test
	public void testDoRestrict(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		
		FilterComponent testFilterComponent = this.buildFilterComponent(
				0, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "this");
		
		Collection<TestThing> returnList = 
			this.handler.doRestrict(Arrays.asList(one,two), testFilterComponent, 1);
		
		assertEquals(1, returnList.size());
		
		assertEquals("this", returnList.iterator().next().testAttribute);
	}
	
	@Test
	public void testDoRestrictNoMatch(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		
		FilterComponent testFilterComponent = this.buildFilterComponent(
				0, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "__INVALID__");
		
		Collection<TestThing> returnList = 
			this.handler.doRestrict(Arrays.asList(one,two), testFilterComponent, 1);
		
		assertEquals(0, returnList.size());
	}
	
	@Test
	public void testSimpleRestrict(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		TestThing three = new TestThing("run", "jump");
		
		FilterComponent testFilterComponent = this.buildFilterComponent(
				0, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		Filter filter = this.buildFilter(testFilterComponent);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		Iterables.addAll(returnList,this.handler.restrict(Arrays.asList(one,two,three), filter));
		
		assertEquals(1, returnList.size());
		
		assertEquals("run", returnList.get(0).testAttribute);
	}
	
	@Test
	public void testUnionRestrict(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		TestThing three = new TestThing("run", "jump");
		
		FilterComponent testFilterComponent1 = this.buildFilterComponent(
				0, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		FilterComponent testFilterComponent2 = this.buildFilterComponent(
				1, SetOperator.UNION, "secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "else");
		
		Filter filter = this.buildFilter(testFilterComponent1,testFilterComponent2);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		Iterables.addAll(returnList, this.handler.restrict(Arrays.asList(one,two,three), filter));
		
		assertEquals(2, returnList.size());
	}
	
	@Test
	public void testSubtractRestrict(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		TestThing three = new TestThing("run", "jump");
		
		FilterComponent testFilterComponent1 = this.buildFilterComponent(
				0, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		FilterComponent testFilterComponent2 = this.buildFilterComponent(
				1, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "something");
		
		FilterComponent testFilterComponent3 = this.buildFilterComponent(
				2, SetOperator.SUBTRACT, "secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "else");
		
		Filter filter = this.buildFilter(testFilterComponent1,testFilterComponent2,testFilterComponent3);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		Iterables.addAll(returnList, this.handler.restrict(Arrays.asList(one,two,three), filter));

		assertEquals(1, returnList.size());
		
		assertEquals("run", returnList.get(0).testAttribute);
	}
	
	private Filter buildFilter(FilterComponent... filterComponents){
		Filter filter = new Filter();
		filter.setComponent(filterComponents);
		
		return filter;
	}
	
	private FilterComponent buildFilterComponent(
			long order, 
			SetOperator operator, 
			String propetyRefName, 
			TargetReferenceType type, 
			String matchAlgorithm, 
			String matchValue){
		
		FilterComponent testFilterComponent = new FilterComponent();
		testFilterComponent.setComponentOrder(order);
		testFilterComponent.setFilterOperator(operator);
		
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
	
	private static class TestThing {
		private String testAttribute;
		private String testOtherAttribute;
		
		private TestThing(){
			super();
		}
		
		private TestThing(String testAttribute, String testOtherAttribute) {
			super();
			this.testAttribute = testAttribute;
			this.testOtherAttribute = testOtherAttribute;
		}
		
	}
}
