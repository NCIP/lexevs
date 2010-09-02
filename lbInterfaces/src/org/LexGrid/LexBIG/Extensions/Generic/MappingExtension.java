/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Extensions.Generic;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public interface MappingExtension extends GenericExtension {
	
	public enum MappingSortOptionName {
		SOURCE_CODE,
		TARGET_CODE,
		SOURCE_ENTITY_DESCRIPTION,
		TARGET_ENTITY_DESCRIPTION,
		RELATIONSHIP,
		QUALIFIER
	}
	
	public enum Direction  {
		ASC,
		DESC
	}
	
	public class MappingSortOption implements Serializable {
	
		private static final long serialVersionUID = -7602204298076863624L;
		
		private MappingSortOptionName mappingSortOptionName;
		private Direction direction;
		
		public MappingSortOption(MappingSortOptionName mappingSortOptionName,
				Direction direction) {
			super();
			if(mappingSortOptionName != null &&
					mappingSortOptionName.equals(MappingSortOptionName.QUALIFIER)) {
				throw new RuntimeException("Please use a QualifierSortOption for a QUALIFIER sort.");
			}
			this.mappingSortOptionName = mappingSortOptionName;
			this.direction = direction;
		}
		
		public MappingSortOptionName getMappingSortOptionName() {
			return mappingSortOptionName;
		}

		public Direction getDirection() {
			return direction;
		}
	}
	
	public class QualifierSortOption extends MappingSortOption {
	
		private static final long serialVersionUID = 7414449265393660704L;

		private String qualifierName;
		
		public QualifierSortOption(Direction direction, String qualifierName) {
			super(null, direction);
			this.qualifierName = qualifierName;
		}
		public MappingSortOptionName getMappingSortOptionName() {
			return MappingSortOptionName.QUALIFIER;
		}
		public String getQualifierName() {
			return qualifierName;
		}
	}
	
	public boolean isMappingCodingScheme(
			String codingScheme, 
			CodingSchemeVersionOrTag codingSchemeVersionOrTag) throws LBParameterException;
	
	public ResolvedConceptReferencesIterator resolveMapping(
			String codingScheme, 
			CodingSchemeVersionOrTag codingSchemeVersionOrTag,
			String relationsContainerName,
			List<MappingSortOption> sortOptionList) throws LBParameterException;
}