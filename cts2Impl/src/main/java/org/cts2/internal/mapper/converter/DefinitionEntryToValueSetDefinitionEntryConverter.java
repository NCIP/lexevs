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
package org.cts2.internal.mapper.converter;

import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyReference;
import org.cts2.core.CodeSystemReference;
import org.cts2.core.FilterComponent;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.NameOrURI;
import org.cts2.core.PredicateReference;
import org.cts2.core.ScopedEntityName;
import org.cts2.core.ValueSetReference;
import org.cts2.core.types.SetOperator;
import org.cts2.core.types.TargetReferenceType;
import org.cts2.valueset.AssociatedEntitiesReference;
import org.cts2.valueset.CompleteCodeSystemReference;
import org.cts2.valueset.CompleteValueSetReference;
import org.cts2.valueset.PropertyQueryReference;
import org.cts2.valueset.ValueSetDefinitionEntry;
import org.cts2.valueset.types.AssociationDirection;
import org.cts2.valueset.types.LeafOrAll;
import org.cts2.valueset.types.TransitiveClosure;
import org.cts2.valueset.types.ValueSetDefinitionEntryType;
import org.dozer.DozerConverter;

/**
 * The Class DefinitionEntryToValueSetDefinitionEntryConverter.
 *
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class DefinitionEntryToValueSetDefinitionEntryConverter extends DozerConverter<DefinitionEntry,ValueSetDefinitionEntry> {

	private DefinitionOperatorToSetOperatorConverter definitionOperatorToSetOperatorConverter;
	
	public DefinitionEntryToValueSetDefinitionEntryConverter() {
		super(DefinitionEntry.class, ValueSetDefinitionEntry.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ValueSetDefinitionEntry convertTo(DefinitionEntry definitionEntry, 
			ValueSetDefinitionEntry valueSetDefinitionEntry) {
		if (definitionOperatorToSetOperatorConverter == null)
			definitionOperatorToSetOperatorConverter = new DefinitionOperatorToSetOperatorConverter();
		
		ValueSetDefinitionEntry vde = null;
		if (definitionEntry.getCodingSchemeReference() != null)
		{
			CompleteCodeSystemReference csRef = new CompleteCodeSystemReference();
			CodeSystemReference cs = new CodeSystemReference();
			cs.setContent(definitionEntry.getCodingSchemeReference().getCodingScheme());
			csRef.setCodeSystem(cs);
			
			csRef.setEntryType(ValueSetDefinitionEntryType.COMPLETE_CODE_SYSTEM);
			
			csRef.setOperator(this.definitionOperatorToSetOperatorConverter.convertTo(definitionEntry.getOperator()));
			
			return csRef;
		} else if (definitionEntry.getValueSetDefinitionReference() != null)
		{
			CompleteValueSetReference vsRef = new CompleteValueSetReference();
			
			ValueSetReference vs = new ValueSetReference();
			vs.setContent(definitionEntry.getValueSetDefinitionReference().getValueSetDefinitionURI());
			vsRef.setValueSet(vs);
			vsRef.setValueSetDefinition(vs);
		
			vsRef.setEntryType(ValueSetDefinitionEntryType.COMPLETE_VALUE_SET);
			
			vsRef.setOperator(this.definitionOperatorToSetOperatorConverter.convertTo(definitionEntry.getOperator()));
			
			return vsRef;
		} else if (definitionEntry.getEntityReference() != null)
		{
			EntityReference lgER = definitionEntry.getEntityReference();
			AssociatedEntitiesReference aeRef = new AssociatedEntitiesReference();
			
			CodeSystemReference cs = new CodeSystemReference();
			cs.setContent(lgER.getEntityCodeNamespace());
			aeRef.setCodeSystem(cs);
		
			// set directional
			if (lgER.getTargetToSource() != null)
			{
				if (lgER.isTargetToSource())
					aeRef.setDirection(AssociationDirection.TARGET_TO_SOURCE);
				else
					aeRef.setDirection(AssociationDirection.SOURCE_TO_TARGET);
			}

			
			// set leafOnly
			if (lgER.getLeafOnly() != null)
			{
				if (lgER.isLeafOnly())
					aeRef.setLeafOnly(LeafOrAll.LEAF_ONLY);
				else
					aeRef.setLeafOnly(LeafOrAll.ALL_INTERMEDIATE_NODES);
			}
			
			// set predicate
			PredicateReference pr = new PredicateReference();
			pr.setContent(lgER.getReferenceAssociation());
			aeRef.setPredicate(pr);
			
			// set referenceEntity
			org.cts2.core.EntityReference er = new org.cts2.core.EntityReference();
			ScopedEntityName entityName = new ScopedEntityName();
			entityName.setName(lgER.getEntityCode());
			entityName.setNamespace(lgER.getEntityCodeNamespace());
			er.setLocalEntityName(entityName);
			aeRef.setReferencedEntity(er);
			
			// set transitivity
			if (lgER.getTransitiveClosure() != null)
			{
				if (lgER.isTransitiveClosure())
					aeRef.setTransitivity(TransitiveClosure.TRANSITIVE_CLOSURE);
				else
					aeRef.setTransitivity(TransitiveClosure.DIRECTLY_ASSOCIATED);
			}
			
			aeRef.setEntryType(ValueSetDefinitionEntryType.ASSOCIATED_ENTITIES);
			
			aeRef.setOperator(this.definitionOperatorToSetOperatorConverter.convertTo(definitionEntry.getOperator()));
			
			return aeRef;
		} else if (definitionEntry.getPropertyReference() != null)
		{
			PropertyReference lgPropRef = definitionEntry.getPropertyReference();
			PropertyQueryReference propQueryRef = new PropertyQueryReference();
			
			CodeSystemReference cs = new CodeSystemReference();
			cs.setContent(lgPropRef.getCodingScheme());
			
			propQueryRef.setCodeSystem(cs);
			
			FilterComponent fc = new FilterComponent();
			
			if (lgPropRef.getPropertyName() != null)
			{
				org.cts2.core.PropertyReference propRef = new org.cts2.core.PropertyReference();
				NameOrURI nameOrURI = new NameOrURI();
				nameOrURI.setName(lgPropRef.getPropertyName());
				propRef.setReferenceType(TargetReferenceType.PROPERTY);
				
				fc.setFilterComponent(propRef);				
			}
			
			if (lgPropRef.getPropertyMatchValue() != null)
			{
				MatchAlgorithmReference matchRef = new MatchAlgorithmReference();
				matchRef.setContent(lgPropRef.getPropertyMatchValue().getMatchAlgorithm());
				
				fc.setMatchAlgorithm(matchRef);
				fc.setMatchValue(lgPropRef.getPropertyMatchValue().getContent());
			}
			
			propQueryRef.setFilter(fc);
		
			propQueryRef.setEntryType(ValueSetDefinitionEntryType.PROPERTY_QUERY);
			
			propQueryRef.setOperator(this.definitionOperatorToSetOperatorConverter.convertTo(definitionEntry.getOperator()));
			
			return propQueryRef;
		}
		return vde;
	}

	/*
	 * (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DefinitionEntry convertFrom(ValueSetDefinitionEntry valueSetDefinitionEntry,
			DefinitionEntry definitionEntry) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the definitionOperatorToSetOperatorConverter
	 */
	public DefinitionOperatorToSetOperatorConverter getDefinitionOperatorToSetOperatorConverter() {
		return definitionOperatorToSetOperatorConverter;
	}

	/**
	 * @param definitionOperatorToSetOperatorConverter the definitionOperatorToSetOperatorConverter to set
	 */
	public void setDefinitionOperatorToSetOperatorConverter(
			DefinitionOperatorToSetOperatorConverter definitionOperatorToSetOperatorConverter) {
		this.definitionOperatorToSetOperatorConverter = definitionOperatorToSetOperatorConverter;
	}
}
