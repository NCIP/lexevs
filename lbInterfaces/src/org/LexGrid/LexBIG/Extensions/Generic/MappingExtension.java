/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
 * A grouping of Mapping Coding Scheme related functionality.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MappingExtension extends GenericExtension {
	
	/**
	 * The Enum MappingSortOptionName.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum MappingSortOptionName {
		
		/** Sort by Source Code */
		SOURCE_CODE,
		
		/** Sort by Target Code */
		TARGET_CODE,
		
		/** Sort Source Entity Description */
		SOURCE_ENTITY_DESCRIPTION,
		
		/** Sort Target Entity Description */
		TARGET_ENTITY_DESCRIPTION,
		
		/** Sort by Relationship Name. */
		RELATIONSHIP,
		
		/** Sort by a named Qualifier */
		QUALIFIER
	}
	
	/**
	 * Sort Direction.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum Direction  {
		
		/** Sort Ascending */
		ASC,
		
		/** Sort Descending */
		DESC
	}
	
	/**
	 * Describes a Sort.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public class MappingSortOption implements Serializable {
	
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -7602204298076863624L;
		
		/** The mapping sort option name. */
		private MappingSortOptionName mappingSortOptionName;
		
		/** The direction. */
		private Direction direction;
		
		/**
		 * Instantiates a new mapping sort option.
		 * 
		 * @param mappingSortOptionName the mapping sort option name
		 * @param direction the direction
		 */
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
		
		/**
		 * Gets the mapping sort option name.
		 * 
		 * @return the mapping sort option name
		 */
		public MappingSortOptionName getMappingSortOptionName() {
			return mappingSortOptionName;
		}

		/**
		 * Gets the direction.
		 * 
		 * @return the direction
		 */
		public Direction getDirection() {
			return direction;
		}
	}
	
	/**
	 * The Class QualifierSortOption.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public class QualifierSortOption extends MappingSortOption {
	
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 7414449265393660704L;

		/** The qualifier name. */
		private String qualifierName;
		
		/**
		 * Instantiates a new qualifier sort option.
		 * 
		 * @param direction the direction
		 * @param qualifierName the qualifier name
		 */
		public QualifierSortOption(Direction direction, String qualifierName) {
			super(null, direction);
			this.qualifierName = qualifierName;
		}
		
		/* (non-Javadoc)
		 * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption#getMappingSortOptionName()
		 */
		public MappingSortOptionName getMappingSortOptionName() {
			return MappingSortOptionName.QUALIFIER;
		}
		
		/**
		 * Gets the qualifier name.
		 * 
		 * @return the qualifier name
		 */
		public String getQualifierName() {
			return qualifierName;
		}
	}
	
	/**
	 * Checks if is mapping coding scheme.
	 * 
	 * @param codingScheme the coding scheme
	 * @param codingSchemeVersionOrTag the coding scheme version or tag
	 * 
	 * @return true, if is mapping coding scheme
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean isMappingCodingScheme(
			String codingScheme, 
			CodingSchemeVersionOrTag codingSchemeVersionOrTag) throws LBParameterException;
	
	/**
	 * Resolve the mapping coding scheme. The resulting Iterator will produce
	 * one (1) ResolvedConceptReference per 'next' call. Each ResolvedConceptReference
	 * will contain exactly one (1) AssociatedConcept as its 'sourceOf'.
	 * 
	 * @param codingScheme the coding scheme
	 * @param codingSchemeVersionOrTag the coding scheme version or tag
	 * @param relationsContainerName the relations container name
	 * @param sortOptionList the sort option list
	 * 
	 * @return the resolved concept references iterator
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public ResolvedConceptReferencesIterator resolveMapping(
			String codingScheme, 
			CodingSchemeVersionOrTag codingSchemeVersionOrTag,
			String relationsContainerName,
			List<MappingSortOption> sortOptionList) throws LBParameterException;

	/**
	 * Resolve a list of Mapping Coding Scheme References that the given
	 * Entity participates in as either a Source or Target.
	 * 
	 * If entityCodeNamespace is null, namespace will not be considered.
	 * 
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace (Optional)
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public AbsoluteCodingSchemeVersionReferenceList getMappingCodingSchemesEntityParticipatesIn(
			String entityCode, 
			String entityCodeNamespace)
			throws LBParameterException;
}