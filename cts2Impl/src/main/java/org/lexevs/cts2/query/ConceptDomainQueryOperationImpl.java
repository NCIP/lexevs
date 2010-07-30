/**
 * 
 */
package org.lexevs.cts2.query;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexgrid.conceptdomain.LexEVSConceptDomainServices;
import org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl;

/**
 * LexEVS Implementation of CTS2 Concept Domain Query Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ConceptDomainQueryOperationImpl implements
		ConceptDomainQueryOperation {
	
	private transient LexEVSConceptDomainServices cdServ_;
	
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainBindings(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<String> getConceptDomainBindings(String conceptDomainId,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainBindings(conceptDomainId, versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodedNodeSet(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodedNodeSet getConceptDomainCodedNodeSet(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainCodedNodeSet(versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodingScheme(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodingScheme getConceptDomainCodingScheme(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainCodingScheme(versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntitisWithName(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> getConceptDomainEntitisWithName(
			String conceptDomainName, CodingSchemeVersionOrTag versionOrTag,
			SearchDesignationOption option, String matchAlgorithm,
			String language) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainEntitisWithName(conceptDomainName, versionOrTag, option, matchAlgorithm, language);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntity(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public Entity getConceptDomainEntity(String conceptDomainId,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainEntity(conceptDomainId, versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#isEntityInConceptDomain(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.util.List)
	 */
	@Override
	public List<String> isEntityInConceptDomain(String conceptDomainId,
			String entityCode,
			AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionList,
			List<String> usageContext) throws LBException {
		return getLexEVSConceptDomainServices().isEntityInConceptDomain(conceptDomainId, entityCode, codingSchemeVersionList, usageContext);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#listAllConceptDomainEntities(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<Entity> listAllConceptDomainEntities(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSConceptDomainServices().listAllConceptDomainEntities(versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#listAllConceptDomainIds(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<String> listAllConceptDomainIds(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSConceptDomainServices().listAllConceptDomainIds(versionOrTag);
	}
	
	/**
	 * Gets the LexEVS concept domain services.
	 * 
	 * @return the LexEVS concept domain services
	 */
	private LexEVSConceptDomainServices getLexEVSConceptDomainServices() {
		if (cdServ_ == null)
			cdServ_ = LexEVSConceptDomainServicesImpl.defaultInstance();
		
		return cdServ_;
	}
}
