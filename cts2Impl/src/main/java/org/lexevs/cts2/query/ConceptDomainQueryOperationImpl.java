/**
 * 
 */
package org.lexevs.cts2.query;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
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
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainBindings(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getConceptDomainBindings(String conceptDomainId,
			String codeSystemNameOrURI) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainBindings(conceptDomainId, codeSystemNameOrURI);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodedNodeSet(java.lang.String, java.lang.String)
	 */
	@Override
	public CodedNodeSet getConceptDomainCodedNodeSet(
			String codeSystemNameOrURI, String codeSystemVersion) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainCodedNodeSet(
				codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainCodingScheme(java.lang.String, java.lang.String)
	 */
	@Override
	public CodingScheme getConceptDomainCodingScheme(
			String codeSystemNameOrURI, String codeSystemVersion) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainCodingScheme(
				codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntitisWithName(java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> getConceptDomainEntitisWithName(
			String conceptDomainName, String codeSystemNameOrURI, String codeSystemVersion,
			SearchDesignationOption option, String matchAlgorithm,
			String language) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainEntitisWithName(conceptDomainName, 
				codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion), 
				option, matchAlgorithm, language);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#getConceptDomainEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Entity getConceptDomainEntity(String conceptDomainId, String namespace,
			String codeSystemNameOrURI, String codeSystemVersion) throws LBException {
		return getLexEVSConceptDomainServices().getConceptDomainEntity(conceptDomainId, namespace,
				codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#isEntityInConceptDomain(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.util.List)
	 */
	@Override
	public List<String> isEntityInConceptDomain(String conceptDomainId, String namespace, String codeSystemNameOrURI,
			String entityCode, AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionList,
			List<String> usageContext) throws LBException {
		return getLexEVSConceptDomainServices().isEntityInConceptDomain(conceptDomainId, namespace, getCodeSystemURI(codeSystemNameOrURI),
				entityCode, codingSchemeVersionList, usageContext);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#listAllConceptDomainEntities(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> listAllConceptDomainEntities(
			String codeSystemNameOrURI, String codeSystemVersion) throws LBException {
		return getLexEVSConceptDomainServices().listAllConceptDomainEntities(
				codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.ConceptDomainQueryOperation#listAllConceptDomainIds(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> listAllConceptDomainIds(
			String codeSystemNameOrURI, String codeSystemVersion) throws LBException {
		return getLexEVSConceptDomainServices().listAllConceptDomainIds(
				codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
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
	
	private String getCodeSystemURI(String codeSystemNameOrUri) throws LBParameterException{
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
		
		return systemResourceService.getUriForUserCodingSchemeName(codeSystemNameOrUri, null);
	}
}
