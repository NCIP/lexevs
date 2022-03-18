
package org.LexGrid.LexBIG.Extensions.Generic;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.custom.relations.TerminologyMapBean;

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
		
		/** Sort by Source Code. */
		SOURCE_CODE,
		
		/** Sort by Target Code. */
		TARGET_CODE,
		
		/** Sort Source Entity Description. */
		SOURCE_ENTITY_DESCRIPTION,
		
		/** Sort Target Entity Description. */
		TARGET_ENTITY_DESCRIPTION,
		
		/** Sort by Relationship Name. */
		RELATIONSHIP,
		
		/** Sort by a named Qualifier. */
		QUALIFIER
	}
	
	/**
	 * Sort Direction.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum Direction  {
		
		/** Sort Ascending. */
		ASC,
		
		/** Sort Descending. */
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
	 * @return the mapping coding schemes entity participates in
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public AbsoluteCodingSchemeVersionReferenceList getMappingCodingSchemesEntityParticipatesIn(
			String entityCode, 
			String entityCodeNamespace)
	throws LBParameterException;

	/**
	 * Gets the a mapping, which may be restricted and resolved.
	 * 
	 * @param codingScheme the mapping coding scheme
	 * @param codingSchemeVersionOrTag the mapping coding scheme version or tag
	 * @param relationsContainerName the relations container name
	 * 
	 * @return the mapping
	 * 
	 * @throws LBException the LB exception
	 */
	public Mapping getMapping(
			String codingScheme,
			CodingSchemeVersionOrTag codingSchemeVersionOrTag, 
			String relationsContainerName) throws LBException;
	
	
	public List<TerminologyMapBean> resolveBulkMapping(String mappingName, String mappingVersion);
	
	/**
	 * A mapping, which may be restricted and resolved.
	 */
	public static interface Mapping extends Serializable {
		
		/**
		 * The Enum SearchContext. Used to specify whether the search
		 * should apply to "Source" codes of the mapping, "Target" codes
		 * of the mapping, or "Both" source and target codes.
		 */
		public enum SearchContext {
			
			/** Apply the restriction to the "Source" codes of the mapping. */
			SOURCE_CODES, 
			
			/** Apply the restriction to the "Target" codes of the mapping. */
			TARGET_CODES, 
			
			/** Apply the restriction to Either the Source OR Target codes of the mapping. 
			 *  A result will be returned if the restriction matches the Source codes OR
			 *  the Target codes of the mapping.
			 */
			SOURCE_OR_TARGET_CODES
		}

		/**
		 * Restrict to matching designations.
		 * 
		 * @param matchText the match text
		 * @param option the option
		 * @param matchAlgorithm the match algorithms
		 * @param language the language
		 * @param searchContext the search context
		 * 
		 * @return the mapping
		 * 
		 * @throws LBInvocationException the LB invocation exception
		 * @throws LBParameterException the LB parameter exception
		 */
		public Mapping restrictToMatchingDesignations(
				String matchText,
				SearchDesignationOption option, 
				String matchAlgorithm, 
				String language,
				SearchContext searchContext)
		throws LBInvocationException,LBParameterException;
		
		/**
		 * Restrict to matching properties.
		 * 
		 * @param propertyNames the property names
		 * @param propertyTypes the property types
		 * @param sourceList the source list
		 * @param contextList the context list
		 * @param qualifierList the qualifier list
		 * @param matchText the match text
		 * @param matchAlgorithm the match algorithm
		 * @param language the language
		 * @param searchContext the search context
		 * 
		 * @return the mapping
		 * 
		 * @throws LBInvocationException the LB invocation exception
		 * @throws LBParameterException the LB parameter exception
		 */
		public Mapping restrictToMatchingProperties(
				LocalNameList propertyNames, 
				PropertyType[] propertyTypes,
				LocalNameList sourceList, 
				LocalNameList contextList, 
				NameAndValueList qualifierList,
				String matchText, 
				String matchAlgorithm, 
				String language,
				SearchContext searchContext)
				throws LBInvocationException,LBParameterException;
		
		/**
		 * Restrict to codes.
		 * 
		 * @param codeList the code list
		 * @param searchContext the search context
		 * 
		 * @return the Mapping
		 * 
		 * @throws LBInvocationException the LB invocation exception
		 * @throws LBParameterException the LB parameter exception
		 */
		public Mapping restrictToCodes(ConceptReferenceList codeList, SearchContext searchContext)
			throws LBInvocationException,LBParameterException;
		
		/**
		 * Restrict to relationship.
		 * 
		 * @param matchText the match text
		 * @param option the option
		 * @param matchAlgorithm the match algorithm
		 * @param language the language
		 * @param relationshipList the relationship list
		 * 
		 * @return the mapping
		 * 
		 * @throws LBInvocationException the LB invocation exception
		 * @throws LBParameterException the LB parameter exception
		 */
		public Mapping restrictToRelationship(
				String matchText,
				SearchDesignationOption option, 
				String matchAlgorithm, 
				String language,
				LocalNameList relationshipList)
		throws LBInvocationException,LBParameterException;

		/**
		 * Resolve mapping.
		 * 
		 * @return the resolved concept references iterator
		 * 
		 * @throws LBException the LB exception
		 */
		public ResolvedConceptReferencesIterator resolveMapping() throws LBException;
		
		/**
		 * Resolve mapping.
		 * 
		 * @param sortOptionList the sort option list
		 * 
		 * @return the resolved concept references iterator
		 * 
		 * @throws LBException the LB exception
		 */
		public ResolvedConceptReferencesIterator resolveMapping(List<MappingSortOption> sortOptionList) throws LBException;
		

	}
}