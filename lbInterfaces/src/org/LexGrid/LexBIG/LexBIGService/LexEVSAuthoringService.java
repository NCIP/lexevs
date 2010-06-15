package org.LexGrid.LexBIG.LexBIGService;


import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;

public interface LexEVSAuthoringService {
	
	public CodingScheme createCodingScheme()throws LBException;
	
	public Entities createEntities(CodingScheme scheme)throws LBException;
	
	public Entity createEntity(Entities entities)throws LBException;
	
	public Relations createRelationsContainer(CodingScheme scheme,
			String containerName,
			Date effectiveDate,
            AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
            AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
            boolean isMapping,
            String associationType,
            Properties relationProperties)throws LBException;
	
	public AssociationPredicate createAssociationPredicate(CodingScheme scheme, String associationName)throws LBException;
	
	public Properties createCodingSchemeProperties(CodingScheme scheme)throws LBException;
	
	public AssociationSource createAssociationSource(Relations relations, AssociationPredicate predicate,
			AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
			String sourceConceptCodeIdentifier
			)throws LBException;
	
	public AssociationTarget createAssociationTarget(Association Source)throws LBException;
	
	public Association createAssociation()throws LBException;
	
	public void createAssociationMapping(Relations relation, 
			AbsoluteCodingSchemeVersionReference sourceCodingScheme, 
			AbsoluteCodingSchemeVersionReference targetCodingScheme,
			AssociationSource[] associationSource,
			String associationType,
			String relationsContainerName,
			Date effectiveDate,
			AssociationQualification[] associationQualifiers
			)throws LBException;
}
