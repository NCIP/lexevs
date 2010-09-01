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
		RELATIONSHIP
	}
	
	public enum Direction  {
		ASC,
		DESC
	}
	
	public class MappingSortOption {
		private MappingSortOptionName mappingSortOptionName;
		private Direction direction;
		
		public MappingSortOption(MappingSortOptionName mappingSortOptionName,
				Direction direction) {
			super();
			this.mappingSortOptionName = mappingSortOptionName;
			this.direction = direction;
		}

		public MappingSortOptionName getMappingSortOptionName() {
			return mappingSortOptionName;
		}

		public void setMappingSortOptionName(
				MappingSortOptionName mappingSortOptionName) {
			this.mappingSortOptionName = mappingSortOptionName;
		}

		public Direction getDirection() {
			return direction;
		}

		public void setDirection(Direction direction) {
			this.direction = direction;
		}
	}
	
	public class QualifierSortOption extends MappingSortOption {
	
		private static final long serialVersionUID = 7414449265393660704L;
		
		private String qualifierName;
		
		private QualifierSortOption(
				MappingSortOptionName mappingSortOptionName,
				Direction direction,
				String qualifierName) {
			super(mappingSortOptionName, direction);
			this.qualifierName = qualifierName;
		}

		public void setQualifierName(String qualifierName) {
			this.qualifierName = qualifierName;
		}

		public String getQualifierName() {
			return qualifierName;
		}	
	}
	
	public ResolvedConceptReferencesIterator resolveMapping(
			String codingScheme, 
			CodingSchemeVersionOrTag codingSchemeVersionOrTag,
			String relationsContainerName,
			List<MappingSortOption> sortOptionList,
			QualifierSortOption qualifierSortOption) throws LBParameterException;
}