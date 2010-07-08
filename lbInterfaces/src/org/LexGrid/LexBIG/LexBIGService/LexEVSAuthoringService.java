package org.LexGrid.LexBIG.LexBIGService;


import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;

public interface LexEVSAuthoringService {
	
	public CodingScheme createCodingScheme(
			Revision revision,
			String codingSchemeName,
			String codingSchemeURI, 
			String formalName, 
			String defaultLanguage,
			long approxNumberofConcepts, 
			String representsVerison,
			List<String> localNameList, 
			List<Source> sourceList,
			Text copyright, 
			Mappings mappings, 
			Properties properties,
			Entities entities, 
			List<Relations> relationsList,
			EntryState entryState) throws LBException;
	
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
			Revision revision
			)throws LBException;
	
	public void createMappingWithDefaultValues(
			AssociationSource[] sourcesAndTargets, String sourceCodingScheme,
			String sourceCodingSchemeVersion, String targetCodingScheme,
			String targetCodingSchemeVersion, String associationName)
			throws LBException;

	public Entities createEntities(CodingScheme scheme)throws LBException;
	
	public Entity createEntity(Entities entities)throws LBException;
	
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
	
	public AssociationPredicate createAssociationPredicate(String associationName, 
            AssociationSource[] AsoocSources)throws LBException;
	
	public Properties createCodingSchemeProperties(CodingScheme scheme)throws LBException;
	
	public AssociationSource createAssociationSource(
			Revision revision,
			EntryState entryState,
			AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
			String sourceConceptCodeIdentifier,
			String relationsContainerName,
            String associationName,
			AssociationTarget[] targetList
			)throws LBException;
	
	public AssociationTarget createAssociationTarget(
	        EntryState entryState,
			//CodingScheme scheme, 
			AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
            String targetConceptCodeIdentifier)throws LBException;
	
	public boolean setAssociationStatus(Revision revision, EntryState entryState,
	            AbsoluteCodingSchemeVersionReference scheme, String relationsContainer, String associationName,
	            String sourceCode, String sourceNamespace, String targetCode, String targetNamespace, String instanceId, String status,
	            boolean isActive) throws LBException ;
	   
	public EntryState createDefaultMappingsEntryState(
			 String prevRevisionId );
	
	public AssociationSource mapTargetsToSource(
            EntryState entryState,
            CodingScheme scheme, 
            AssociationSource source,
            AbsoluteCodingSchemeVersionReference codingSchemeIdentifier,
            String relationsContainerName, 
            String associationName, 
            AssociationTarget[] associationTargets)throws LBException;
	

}
