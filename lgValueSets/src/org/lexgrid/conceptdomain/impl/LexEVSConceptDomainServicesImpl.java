
package org.lexgrid.conceptdomain.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
import org.lexgrid.conceptdomain.LexEVSConceptDomainServices;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 *Implements LexEVS Concept Domain API.
 *
 *@author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSConceptDomainServicesImpl implements LexEVSConceptDomainServices {

	private static final long serialVersionUID = 6493716627706734222L;
	
	private transient LexBIGService lbsvc_;
	private LexEVSValueSetDefinitionServices vsd_;
	
	private static LexEVSConceptDomainServices cdServ_;
	private DatabaseServiceManager databaseServiceManager = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
	
	public static LexEVSConceptDomainServices defaultInstance(){
		if (cdServ_ == null)
			cdServ_ = new LexEVSConceptDomainServicesImpl();
		
		return cdServ_;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainCodingScheme(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodingScheme getConceptDomainCodingScheme(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexBIGService().resolveCodingScheme(codingSchemeNameOrURI, versionOrTag);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainCodingSchemeSummary(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodingSchemeSummary getConceptDomainCodingSchemeSummary(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException {
		String version = ServiceUtility.getVersion(codingSchemeNameOrURI, versionOrTag);
		return databaseServiceManager.getCodingSchemeService().
        	getCodingSchemeSummaryByUriAndVersion(getCodeSystemURI(codingSchemeNameOrURI, version), version);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainCodedNodeSet(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodedNodeSet getConceptDomainCodedNodeSet(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexBIGService().getNodeSet(codingSchemeNameOrURI, versionOrTag, 
				Constructors.createLocalNameList(ConceptDomainConstants.CONCEPT_DOMAIN_ENTITY_TYPE));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#listAllConceptDomainEntities(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<Entity> listAllConceptDomainEntities(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException {
		List<Entity> entityList = new ArrayList<Entity>();
		CodedNodeSet cns = getConceptDomainCodedNodeSet(codingSchemeNameOrURI, versionOrTag);
		
		if (cns != null)
		{
			ResolvedConceptReferencesIterator rcrIter = cns.resolve(null, null, null, null, true);
			while (rcrIter.hasNext())
			{
				ResolvedConceptReference rcr = rcrIter.next();
				entityList.add(rcr.getEntity());
			}
		}
		return entityList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#listAllConceptDomainIds(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<String> listAllConceptDomainIds(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException {
		List<String> idsList = new ArrayList<String>();
		List<Entity> entityList = listAllConceptDomainEntities(codingSchemeNameOrURI, versionOrTag);
		for (Entity entity : entityList)
		{
			idsList.add(entity.getEntityCode());
		}
		return idsList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainBindings(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getConceptDomainBindings(String conceptDomainId, String codingSchemeURI) throws LBException {
		return getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomain(conceptDomainId, codingSchemeURI);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainEntitisWithName(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> getConceptDomainEntitisWithName(String conceptDomainName, String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag, SearchDesignationOption option, String matchAlgorithm, String language) throws LBException {		
		List<Entity> entityList = new ArrayList<Entity>();		
		CodedNodeSet cns = getConceptDomainCodedNodeSet(codingSchemeNameOrURI, versionOrTag);
		
		if (cns != null)
		{
			cns.restrictToMatchingDesignations(conceptDomainName, option, matchAlgorithm, language);
			
			if (cns != null)
			{
				ResolvedConceptReferencesIterator rcrIter = cns.resolve(null, null, null, null, true);
				while (rcrIter.hasNext())
				{
					ResolvedConceptReference rcr = rcrIter.next();
					entityList.add(rcr.getEntity());
				}
			}
		}
		
		return entityList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainEntity(java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public Entity getConceptDomainEntity(String conceptDomainId, String namespace, String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException {
		Entity cdEntity = null;
		CodedNodeSet cns = this.getConceptDomainCodedNodeSet(codingSchemeNameOrURI, versionOrTag);
		if (cns != null)
		{
			cns.restrictToCodes(Constructors.createConceptReferenceList(conceptDomainId));
			ResolvedConceptReferencesIterator rcrItr = cns.resolve(null, null, null, null, true);
			if (rcrItr != null && rcrItr.hasNext())
			{
				ResolvedConceptReference rcr = rcrItr.next();
				cdEntity = rcr.getEntity();
			}
		}
		return cdEntity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#isEntityInConceptDomain(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.util.List)
	 */
	@Override
	public List<String> isEntityInConceptDomain(String conceptDomainId, String namespace, String codingSchemeURI, String entityCode, 
			AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionList, List<String> usageContext) 
		throws LBException {
		if (StringUtils.isEmpty(conceptDomainId) || StringUtils.isEmpty(entityCode))
			return null;
		
		List<String> vsdURIs = new ArrayList<String>();
		
		List<String> allVSD = getConceptDomainBindings(conceptDomainId, codingSchemeURI);
		
		for (String vsdURI : allVSD)
		{
			try {
				AbsoluteCodingSchemeVersionReference csvr = getValueSetDefinitionService().isEntityInValueSet(entityCode, 
						namespace == null ? null : new URI(namespace), 
						new URI(vsdURI), null, codingSchemeVersionList, null);
				
				if (csvr != null)
					vsdURIs.add(vsdURI);
			} catch (URISyntaxException e) {
				throw new LBException("Problem resolving isEntityInConceptDomain", e);
			}		
		}		
		
		if (vsdURIs.size() == 0)
			vsdURIs = null;
		
		return vsdURIs;
	}
	
	private LexBIGService getLexBIGService(){
		if (lbsvc_ == null)
			lbsvc_ = LexBIGServiceImpl.defaultInstance();
		
		return lbsvc_;
	}
	
	private LexEVSValueSetDefinitionServices getValueSetDefinitionService(){
		if (vsd_ == null)
			vsd_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		
		return vsd_;
	}
	
	private String getCodeSystemURI(String codeSystemNameOrUri, String version) throws LBParameterException{
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
		
		return systemResourceService.getUriForUserCodingSchemeName(codeSystemNameOrUri, version);
	}
}