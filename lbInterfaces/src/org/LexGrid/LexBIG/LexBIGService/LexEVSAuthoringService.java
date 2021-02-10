
package org.LexGrid.LexBIG.LexBIGService;


import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;

/**
 * @author  <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public interface LexEVSAuthoringService {
	

	/**
	 * Creating a mapping to persist to an existing set of maps in a specified coding scheme
	 * If no mapping set exists -- start a new mapping set
	 * 
	 * @param entryState
	 * @param mappingCoding
	 * @param sourceCodingScheme
	 * @param targetCodingScheme
	 * @param associationSource
	 * @param associationType
	 * @param relationsContainerName
	 * @param effectiveDate
	 * @param associationQualifiers
	 * @param revision
	 * @throws LBException
	 * 
	 */
	public void createAssociationMapping(
			EntryState entryState, 
		    AbsoluteCodingSchemeVersionReference mappingCoding,
			AbsoluteCodingSchemeVersionReference sourceCodingScheme, 
			AbsoluteCodingSchemeVersionReference targetCodingScheme,
			AssociationSource[] associationSource,
			String associationType,
			String relationsContainerName,
			Date effectiveDate,
			AssociationQualification[] associationQualifiers,
			Revision revision,
			boolean loadEntities
			)throws LBException;
	
	/**
	 * 
	 * Create a new mapping coding scheme with many coding scheme meta data values
	 * preset. 
	 * 
	 * @param sourcesAndTargets
	 * @param sourceCodingScheme
	 * @param sourceCodingSchemeVersion
	 * @param targetCodingScheme
	 * @param targetCodingSchemeVersion
	 * @param associationName
	 * @param loadEntities
	 * @throws LBException
	 * 
	 * 
	 */
	public void createMappingWithDefaultValues(
			AssociationSource[] sourcesAndTargets, String sourceCodingScheme,
			String sourceCodingSchemeVersion, String targetCodingScheme,
			String targetCodingSchemeVersion, String associationName,  boolean loadEntities)
			throws LBException;

	/**
	 *
	 * Allows user to control every detail of the creation of a new mapping
	 * coding scheme.
	 * 
	 * @param mappingSchemeMetadata
	 * @param sourcesAndTargets
	 * @param sourceCodingScheme
	 * @param sourceCodingSchemeVersion
	 * @param targetCodingScheme
	 * @param targetCodingSchemeVersion
	 * @param associationName
	 * @param containerName
	 * @param revisionId
	 * @param loadEntities
	 * @throws LBException
	 *
	 */
	public void createMappingScheme(CodingScheme mappingSchemeMetadata,
			AssociationSource[] sourcesAndTargets,
			String sourceCodingScheme, String sourceCodingSchemeVersion,
			String targetCodingScheme, String targetCodingSchemeVersion,
			String associationName, String containerName, 
			String revisionId, boolean loadEntities) throws LBException;
	
	
	/**
	 * Creates and returns an entry state with 
	 * mapping related data members populated.
	 * 
	 * @param entryState
	 * @param scheme
	 * @param containerName
	 * @param effectiveDate
	 * @param sourceCodeSystemIdentifier
	 * @param targetCodeSystemIdentifier
	 * @param isMapping
	 * @param associationType
	 * @param relationProperties
	 * @return
	 * @throws LBException
	 */
	public Relations createRelationsContainer(
			EntryState entryState,
			CodingScheme scheme,
			String containerName,
			Date effectiveDate,
            AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
            AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
            boolean isMapping,
            String associationType,
            Properties relationProperties)throws LBException;
	
	/**
	 * Populates the predicate only -- does not persist.
	 * @param associationName
	 * @param AssocSources
	 * @return
	 * @throws LBException
	 */
	public AssociationPredicate createAssociationPredicate(String associationName, 
            AssociationSource[] AssocSources)throws LBException;
	
	
	/**
	 * Creates and persists an Association source and target set in 
	 * a given terminology.  Does not handle mappings to external sources.
	 * 
	 * @param revision
	 * @param entryState
	 * @param sourceCodeSystemIdentifier
	 * @param sourceConceptCodeIdentifier
	 * @param relationsContainerName
	 * @param associationName
	 * @param targetList
	 * @return
	 * @throws LBException
	 */
	public AssociationSource createAssociationSource(
			Revision revision,
			EntryState entryState,
			AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
			String sourceConceptCodeIdentifier,
			String relationsContainerName,
            String associationName,
			AssociationTarget[] targetList
			)throws LBException;
	
	/**
	 * Creates and returns an AssocitionTarget Object
	 * Must be created with a source and persisted as a revision
	 * Used as a method for creating targets when creating the source..
	 * 
	 * @param entryState
	 * @param versionableData
	 * @param instanceId
	 * @param isInferred
	 * @param isDefined
	 * @param usageContextList
	 * @param associationQualifiers
	 * @param targetCodeSystemIdentifier
	 * @param targetConceptCodeIdentifier
	 * @return
	 * @throws LBException
	 */
	public AssociationTarget createAssociationTarget(
	        EntryState entryState,
            Versionable versionableData,
            String instanceId,
            Boolean isInferred,
            Boolean isDefined,
            List<String> usageContextList,
            List<AssociationQualification> associationQualifiers,
			AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
            String targetConceptCodeIdentifier)throws LBException;
	
	/**
	 * Updates the status of an association allowing it to be 
	 * filtered depending status and isActive parameters.
	 * 
	 * @param revision
	 * @param entryState
	 * @param scheme
	 * @param relationsContainer
	 * @param associationName
	 * @param sourceCode
	 * @param sourceNamespace
	 * @param targetCode
	 * @param targetNamespace
	 * @param instanceId
	 * @param status
	 * @param isActive
	 * @return
	 * @throws LBException
	 */
	public boolean setAssociationStatus(Revision revision, EntryState entryState,
	            AbsoluteCodingSchemeVersionReference scheme, String relationsContainer, String associationName,
	            String sourceCode, String sourceNamespace, String targetCode, String targetNamespace, String instanceId, String status,
	            boolean isActive) throws LBException ;
	   
	/**
	 * Creates and returns an entry state with some preset 
	 * meta data.
	 * @param revisionId
	 * @param prevRevisionId
	 * @return
	 */
	public EntryState createDefaultMappingsEntryState(
			String revisionId, String prevRevisionId);
	
	/**
	 * Utility method Checks for the existence of a set of 
	 * targets for a given target.  If they don't already exist
	 * in the coding scheme versioning elements are applied
	 * @param entryState
	 * @param scheme
	 * @param source
	 * @param codingSchemeIdentifier
	 * @param relationsContainerName
	 * @param associationName
	 * @param associationTargets
	 * @return
	 * @throws LBException
	 */
	public AssociationSource mapTargetsToSource(
            EntryState entryState,
            CodingScheme scheme, 
            AssociationSource source,
            AbsoluteCodingSchemeVersionReference codingSchemeIdentifier,
            String relationsContainerName, 
            String associationName, 
            AssociationTarget[] associationTargets)throws LBException;
	
	/**
	 * Creates and persists an association predicate (association type).
	 * 
	 * @param revision
	 * @param entryState
	 * @param scheme
	 * @param relationsContainerName
	 * @param associationName
	 * @return
	 * @throws LBException
	 */
	public String createAssociationPredicate(Revision revision, 
			EntryState entryState, 
			AbsoluteCodingSchemeVersionReference scheme,
			String relationsContainerName,
			String associationName)throws LBException;


}