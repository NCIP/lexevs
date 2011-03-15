package org.cts2.internal.mapper;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.cts2.core.types.SetOperator;
import org.cts2.valueset.ValueSetDefinitionEntry;
import org.junit.Test;

public class ValueSetDefinitionToValueSetDefinitionTest extends BaseDozerBeanMapperTest {
	
	@Test
	public void testEntityDescription(){
		org.LexGrid.valueSets.ValueSetDefinition vsdLg = new org.LexGrid.valueSets.ValueSetDefinition();
		vsdLg.setEntityDescription(Constructors.createEntityDescription("test desc"));
		
		org.cts2.valueset.ValueSetDefinition mapped = 
			baseDozerBeanMapper.map(vsdLg, org.cts2.valueset.ValueSetDefinition.class);
		
		assertEquals("test desc", mapped.getResourceSynopsis().getValue());
	}
	
	@Test
	public void testDefinitionOperatorForward(){
		DefinitionOperator[] defOperators = {DefinitionOperator.AND,DefinitionOperator.OR,DefinitionOperator.SUBTRACT};
		SetOperator[] setOperators = {SetOperator.INTERSECT,SetOperator.UNION,SetOperator.SUBTRACT};

		for(int i=0;i<defOperators.length;i++){

			ValueSetDefinitionEntry def = new ValueSetDefinitionEntry();

			def.setOperator(setOperators[i]);

			DefinitionEntry mapped = 
				baseDozerBeanMapper.map(def, DefinitionEntry.class);

			assertEquals(defOperators[i], mapped.getOperator());
		}
	}
	
	@Test
	public void testDefinitionOperatorBackward(){
		DefinitionOperator[] defOperators = {DefinitionOperator.AND,DefinitionOperator.OR,DefinitionOperator.SUBTRACT};
		SetOperator[] setOperators = {SetOperator.INTERSECT,SetOperator.UNION,SetOperator.SUBTRACT};

		for(int i=0;i<defOperators.length;i++){

			DefinitionEntry def = new DefinitionEntry();

			def.setOperator(defOperators[i]);

			ValueSetDefinitionEntry mapped = 
				baseDozerBeanMapper.map(def, ValueSetDefinitionEntry.class);

			assertEquals(setOperators[i], mapped.getOperator());
		}
	}
}
