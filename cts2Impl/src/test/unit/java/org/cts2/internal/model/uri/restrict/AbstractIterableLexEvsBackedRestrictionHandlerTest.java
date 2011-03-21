package org.cts2.internal.model.uri.restrict;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.types.SetOperator;
import org.cts2.core.types.TargetReferenceType;
import org.cts2.internal.match.AttributeResolver;
import org.cts2.internal.match.MatchAlgorithm;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.test.TestUtils;
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
				public List<ResolvableModelAttributeReference<TestThing>> registerSupportedModelAttributeReferences() {
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

				@Override
				public List<MatchAlgorithmReference> registerSupportedMatchAlgorithmReferences() {
					// TODO Auto-generated method stub
					return null;
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
		
		FilterComponent testFilterComponent = TestUtils.buildFilterComponent(
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
		
		FilterComponent testFilterComponent = TestUtils.buildFilterComponent(
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
		
		FilterComponent testFilterComponent = TestUtils.buildFilterComponent(
				0, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		Filter filter = TestUtils.buildFilter(testFilterComponent);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		IterableRestriction<TestThing> restriction = this.handler.restrict(filter);
		
		Iterables.addAll(returnList, restriction.processRestriction(Arrays.asList(one,two,three)));
		
		assertEquals(1, returnList.size());
		
		assertEquals("run", returnList.get(0).testAttribute);
	}
	
	@Test
	public void testUnionRestrict(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		TestThing three = new TestThing("run", "jump");
		
		FilterComponent testFilterComponent1 = TestUtils.buildFilterComponent(
				0, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		FilterComponent testFilterComponent2 = TestUtils.buildFilterComponent(
				1, SetOperator.UNION, "secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "else");
		
		Filter filter = TestUtils.buildFilter(testFilterComponent1,testFilterComponent2);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		
		IterableRestriction<TestThing> restriction = this.handler.restrict(filter);
		
		Iterables.addAll(returnList, restriction.processRestriction(Arrays.asList(one,two,three)));
		
		assertEquals(2, returnList.size());
	}
	
	@Test
	public void testSubtractRestrict(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		TestThing three = new TestThing("run", "jump");
		
		FilterComponent testFilterComponent1 = TestUtils.buildFilterComponent(
				0, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		FilterComponent testFilterComponent2 = TestUtils.buildFilterComponent(
				1, SetOperator.UNION, "firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "something");
		
		FilterComponent testFilterComponent3 = TestUtils.buildFilterComponent(
				2, SetOperator.SUBTRACT, "secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "else");
		
		Filter filter = TestUtils.buildFilter(testFilterComponent1,testFilterComponent2,testFilterComponent3);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		IterableRestriction<TestThing> restriction = this.handler.restrict(filter);
		
		Iterables.addAll(returnList, restriction.processRestriction(Arrays.asList(one,two,three)));

		assertEquals(1, returnList.size());
		
		assertEquals("run", returnList.get(0).testAttribute);
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
