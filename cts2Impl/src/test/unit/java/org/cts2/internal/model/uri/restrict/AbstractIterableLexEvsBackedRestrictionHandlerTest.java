package org.cts2.internal.model.uri.restrict;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cts2.core.Directory;
import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.types.SetOperator;
import org.cts2.core.types.TargetReferenceType;
import org.cts2.internal.match.AttributeResolver;
import org.cts2.internal.match.MatchAlgorithm;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.test.TestUtils;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.restriction.RestrictionState;
import org.cts2.uri.restriction.SetComposite;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class AbstractIterableLexEvsBackedRestrictionHandlerTest {

	private AbstractIterableLexEvsBackedRestrictionHandler<TestThing,TestDirectoryURI> handler;
	
	@Before
	public void buildTestHandler(){
		this.handler = 
			new AbstractIterableLexEvsBackedRestrictionHandler<TestThing,TestDirectoryURI>(){

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
					return null;
				}

				@Override
				public List<IterableRestriction<TestThing>> processOtherRestictions(
						TestDirectoryURI directoryUri) {
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
				"firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "this");
		
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
				"firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "__INVALID__");
		
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
				"firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		Filter filter = TestUtils.buildFilter(testFilterComponent);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		
		TestRestrictionState restrictionState = new TestRestrictionState();
		restrictionState.getFilters().add(filter);
		
		IterableRestriction<TestThing> restriction = this.handler.compile(new DefaultTestDirectoryURI(restrictionState));
		
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
				"firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		FilterComponent testFilterComponent2 = TestUtils.buildFilterComponent(
				"secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "else");
		
		Filter filter1 = TestUtils.buildFilter(testFilterComponent1);
		Filter filter2 = TestUtils.buildFilter(testFilterComponent2);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		
		TestRestrictionState restrictionState = new TestRestrictionState();
		restrictionState.setSetComposite(new SetComposite<TestDirectoryURI>());
		restrictionState.getSetComposite().setSetOperator(SetOperator.UNION);
		restrictionState.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(filter1));
		restrictionState.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(filter2));
		
		IterableRestriction<TestThing> restriction = this.handler.compile(new DefaultTestDirectoryURI(restrictionState));
		
		Iterables.addAll(returnList, restriction.processRestriction(Arrays.asList(one,two,three)));
		
		assertEquals(2, returnList.size());
	}
	
	@Test
	public void testSubtractRestrict(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		TestThing three = new TestThing("run", "jump");
		
		FilterComponent testFilterComponent1 = TestUtils.buildFilterComponent(
				"firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		FilterComponent testFilterComponent2 = TestUtils.buildFilterComponent(
				"secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "jump");

		Filter filter1 = TestUtils.buildFilter(testFilterComponent1);
		Filter filter2 = TestUtils.buildFilter(testFilterComponent2);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		
		TestRestrictionState restrictionState = new TestRestrictionState();
		restrictionState.setSetComposite(new SetComposite<TestDirectoryURI>());
		restrictionState.getSetComposite().setSetOperator(SetOperator.INTERSECT);
		restrictionState.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(filter1));
		restrictionState.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(filter2));
		
		IterableRestriction<TestThing> restriction = this.handler.compile(new DefaultTestDirectoryURI(restrictionState));
		
		Iterables.addAll(returnList, restriction.processRestriction(Arrays.asList(one,two,three)));

		assertEquals(1, returnList.size());
		
		assertEquals("run", returnList.get(0).testAttribute);
	}
	
	@Test
	public void test2DeepSetOperationsIntersect(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		TestThing three = new TestThing("run", "jump");
		
		FilterComponent testFilterComponent1 = TestUtils.buildFilterComponent(
				"firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		FilterComponent testFilterComponent2 = TestUtils.buildFilterComponent(
				"secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "jump");
		
		FilterComponent testFilterComponent3 = TestUtils.buildFilterComponent(
				"secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "that");

		Filter filter1 = TestUtils.buildFilter(testFilterComponent1);
		Filter filter2 = TestUtils.buildFilter(testFilterComponent2);
		Filter filter3 = TestUtils.buildFilter(testFilterComponent3);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		
		TestRestrictionState restrictionState1 = new TestRestrictionState();
		restrictionState1.setSetComposite(new SetComposite<TestDirectoryURI>());
		restrictionState1.getSetComposite().setSetOperator(SetOperator.UNION);
		restrictionState1.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(filter1));
		restrictionState1.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(filter3));
		
		TestRestrictionState restrictionState2 = new TestRestrictionState();
		restrictionState2.setSetComposite(new SetComposite<TestDirectoryURI>());
		restrictionState2.getSetComposite().setSetOperator(SetOperator.INTERSECT);
		restrictionState2.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(filter1));
		restrictionState2.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(filter2));
		
		TestRestrictionState restrictionState3 = new TestRestrictionState();
		restrictionState3.setSetComposite(new SetComposite<TestDirectoryURI>());
		restrictionState3.getSetComposite().setSetOperator(SetOperator.INTERSECT);
		restrictionState3.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(restrictionState1));
		restrictionState3.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(restrictionState2));
		
		IterableRestriction<TestThing> restriction = this.handler.compile(new DefaultTestDirectoryURI(restrictionState3));
		
		Iterables.addAll(returnList, restriction.processRestriction(Arrays.asList(one,two,three)));

		assertEquals(1, returnList.size());
		
		assertEquals("run", returnList.get(0).testAttribute);
	}
	
	@Test
	public void test2DeepSetOperationsSubtract(){
		TestThing one = new TestThing("this", "that");
		TestThing two = new TestThing("something", "else");
		TestThing three = new TestThing("run", "jump");
		
		FilterComponent testFilterComponent1 = TestUtils.buildFilterComponent(
				"firstAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "run");
		
		FilterComponent testFilterComponent2 = TestUtils.buildFilterComponent(
				"secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "jump");
		
		FilterComponent testFilterComponent3 = TestUtils.buildFilterComponent(
				"secondAttribute", TargetReferenceType.ATTRIBUTE, "test-algorithm", "that");

		Filter filter1 = TestUtils.buildFilter(testFilterComponent1);
		Filter filter2 = TestUtils.buildFilter(testFilterComponent2);
		Filter filter3 = TestUtils.buildFilter(testFilterComponent3);
		
		List<TestThing> returnList = new ArrayList<TestThing>();
		
		TestRestrictionState restrictionState1 = new TestRestrictionState();
		restrictionState1.setSetComposite(new SetComposite<TestDirectoryURI>());
		restrictionState1.getSetComposite().setSetOperator(SetOperator.UNION);
		restrictionState1.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(filter1));
		restrictionState1.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(filter3));
		
		TestRestrictionState restrictionState2 = new TestRestrictionState();
		restrictionState2.setSetComposite(new SetComposite<TestDirectoryURI>());
		restrictionState2.getSetComposite().setSetOperator(SetOperator.INTERSECT);
		restrictionState2.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(filter1));
		restrictionState2.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(filter2));
		
		TestRestrictionState restrictionState3 = new TestRestrictionState();
		restrictionState3.setSetComposite(new SetComposite<TestDirectoryURI>());
		restrictionState3.getSetComposite().setSetOperator(SetOperator.SUBTRACT);
		restrictionState3.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(restrictionState1));
		restrictionState3.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(restrictionState2));
		
		IterableRestriction<TestThing> restriction = this.handler.compile(new DefaultTestDirectoryURI(restrictionState3));
		
		Iterables.addAll(returnList, restriction.processRestriction(Arrays.asList(one,two,three)));

		assertEquals(1, returnList.size());
		
		assertEquals("this", returnList.get(0).testAttribute);
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
	
	private static class TestRestrictionState extends RestrictionState<TestDirectoryURI> {
		
	}
	
	private static interface TestDirectoryURI extends DirectoryURI {

		public TestRestrictionState getRestrictionState();
	}

	
	private static class DefaultTestDirectoryURI implements TestDirectoryURI {
		
		private TestRestrictionState restrictionState = new TestRestrictionState();
		
		private DefaultTestDirectoryURI(TestRestrictionState restrictionState){
			this.restrictionState = restrictionState;
		}
		
		private DefaultTestDirectoryURI(Filter filter){
			this.restrictionState.getFilters().add(filter);
		}
		
		private DefaultTestDirectoryURI(TestRestrictionState state1, TestRestrictionState state2, SetOperator setOperator){
			this.restrictionState.setSetComposite(new SetComposite<TestDirectoryURI>());
			
			this.restrictionState.getSetComposite().setDirectoryUri1(new DefaultTestDirectoryURI(state1));
			this.restrictionState.getSetComposite().setDirectoryUri2(new DefaultTestDirectoryURI(state2));
		}

		@Override
		public TestRestrictionState getRestrictionState() {
			return this.restrictionState;
		}

		@Override
		public <T extends Directory<?>> T get(QueryControl queryControl,
				ReadContext readContext, Class<T> content) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int count(ReadContext readContext) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public DirectoryURI restrict(Filter filter) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String marshall() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
