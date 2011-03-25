package org.cts2.internal.model.uri.restrict;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.types.TargetReferenceType;
import org.cts2.internal.match.BaseCompositeMatchAlgorithm;
import org.cts2.internal.match.ExactMatcher;
import org.cts2.internal.match.MatchAlgorithm;
import org.cts2.test.TestUtils;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;
import org.cts2.uri.restriction.RestrictionState;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class ValueSetDefinitionRestrictionHandlerTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUnionRestrict(){
		ValueSetDefinitionRestrictionHandler handler = new ValueSetDefinitionRestrictionHandler();
		
		MatchAlgorithm matchAlgorithm = new BaseCompositeMatchAlgorithm("exactMatch", new ExactMatcher());
		
		handler.setMatchAlgorithms(Arrays.asList(matchAlgorithm));
		
		FilterComponent filterComponent = 
			TestUtils.buildFilterComponent("valueSetDefinitionName", TargetReferenceType.ATTRIBUTE, "exactMatch", "test_name");
			
		ValueSetDefinition vsd1 = new ValueSetDefinition();
		vsd1.setValueSetDefinitionName("some_name");
		
		ValueSetDefinition vsd2 = new ValueSetDefinition();
		vsd2.setValueSetDefinitionName("test_name");
		
		List<ValueSetDefinition> definitions = Arrays.asList(vsd1,vsd2);
		
		Filter filter = TestUtils.buildFilter(filterComponent);
		
		RestrictionState restrictionState = new RestrictionState();
		restrictionState.getFilters().add(filter);
		
		ValueSetDefinitionDirectoryURI uri = EasyMock.createMock(ValueSetDefinitionDirectoryURI.class);
		EasyMock.expect(uri.getRestrictionState()).andReturn(restrictionState);
		
		EasyMock.replay(uri);
		
		IterableRestriction<ValueSetDefinition> restriction = handler.compile(uri);
		
		Iterable<ValueSetDefinition> restrictedDefs = restriction.processRestriction(definitions);
		
		List<ValueSetDefinition> returnList = new ArrayList<ValueSetDefinition>();
		
		Iterables.addAll(returnList, restriction.processRestriction(restrictedDefs));
		
		assertEquals(1,returnList.size());
		
		assertEquals("test_name", returnList.get(0).getValueSetDefinitionName());
	}
}
