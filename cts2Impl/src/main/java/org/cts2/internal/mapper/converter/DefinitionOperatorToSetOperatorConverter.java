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

import org.LexGrid.valueSets.types.DefinitionOperator;
import org.cts2.core.types.SetOperator;
import org.dozer.DozerConverter;

/**
 * The Class DefinitionOperatorToSetOperatorConverter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefinitionOperatorToSetOperatorConverter extends DozerConverter<DefinitionOperator,SetOperator> {

	public DefinitionOperatorToSetOperatorConverter() {
		super(DefinitionOperator.class, SetOperator.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DefinitionOperator convertFrom(
			SetOperator setOperator,
			DefinitionOperator definitionOperator) {
		switch (setOperator) {
			case UNION : {
				return DefinitionOperator.OR;
			}
			case INTERSECT :  {
				return DefinitionOperator.AND;
			}
			case SUBTRACT : {
				return DefinitionOperator.SUBTRACT;
			}
			default : {
				throw new RuntimeException(setOperator + " cannot be mapped.");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SetOperator convertTo(DefinitionOperator definitionOperator, SetOperator convertFrom) {
		switch (definitionOperator) {
			case OR : {
				return SetOperator.UNION;
			}
			case AND :  {
				return SetOperator.INTERSECT;
			}
			case SUBTRACT : {
				return SetOperator.SUBTRACT;
			}
			default : {
				throw new RuntimeException(definitionOperator + " cannot be mapped.");
			}
		}
	}
}
