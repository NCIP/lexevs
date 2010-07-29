/**
 * 
 */
package org.lexgrid.usagecontext.impl;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.usagecontext.LexEVSUsageContextServices;
import org.lexgrid.usagecontext.util.UsageContextConstants;
import org.lexgrid.valuesets.helper.CodingSchemeBuilder;

/**
 * Implementation of LexEVSUsageContextServices.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSUsageContextServicesImpl implements LexEVSUsageContextServices {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient LexBIGService lbsvc_;
	
	private static LexEVSUsageContextServices ucServ_;
	
	public static LexEVSUsageContextServices defaultInstance(){
		if (ucServ_ == null)
			ucServ_ = new LexEVSUsageContextServicesImpl();
		
		return ucServ_;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#getUsageContextCodedNodeSet(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodedNodeSet getUsageContextCodedNodeSet(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		CodingSchemeVersionOrTag csVT = versionOrTag;
		if (csVT == null || StringUtils.isEmpty(csVT.getVersion()))
		{
			csVT = Constructors.createCodingSchemeVersionOrTag(null, 
					UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		}
		return getLexBIGService().getNodeSet(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, 
				csVT, Constructors.createLocalNameList(UsageContextConstants.USAGE_CONTEXT_ENTITY_TYPE));
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#getUsageContextCodingScheme(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodingScheme getUsageContextCodingScheme(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		CodingSchemeVersionOrTag csVT = versionOrTag;
		if (csVT == null || StringUtils.isEmpty(csVT.getVersion()))
		{
			csVT = Constructors.createCodingSchemeVersionOrTag(null, 
					UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		}
		return getLexBIGService().resolveCodingScheme(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, 
				csVT);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#getUsageContextEntitisWithName(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> getUsageContextEntitisWithName(String usageContextName,
			CodingSchemeVersionOrTag versionOrTag,
			SearchDesignationOption option, String matchAlgorithm,
			String language) throws LBException {
		List<Entity> entityList = new ArrayList<Entity>();		
		CodedNodeSet cns = getUsageContextCodedNodeSet(versionOrTag);
		
		if (cns != null)
		{
			cns.restrictToMatchingDesignations(usageContextName, option, MatchAlgorithms.valueOf(matchAlgorithm).name(), language);
			
			if (cns != null)
			{
				ResolvedConceptReferencesIterator rcrIter = cns.resolve(null, null, null);
				while (rcrIter.hasNext())
				{
					ResolvedConceptReference rcr = rcrIter.next();
					entityList.add(rcr.getEntity());
				}
			}
		}
		
		return entityList;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#getUsageContextEntity(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public Entity getUsageContextEntity(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		CodingSchemeVersionOrTag csVT = versionOrTag;
		if (csVT == null || StringUtils.isEmpty(csVT.getVersion()))
		{
			csVT = Constructors.createCodingSchemeVersionOrTag(null, 
					UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		}
		return getDatabaseEntityService().getEntity(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, 
				csVT.getVersion(),
				usageContextId, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#insertUsageContext(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.commonTypes.Properties, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public void insertUsageContext(String usageContextId,
			String usageContextName, String revisionId, String description,
			String status, Properties properties,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		// create an entity object for concept domain
		Entity entity = new Entity();
		entity.setEntityCode(usageContextId);
		entity.setEntityCodeNamespace(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME);
		EntityDescription ed = new EntityDescription();
		ed.setContent(description);
		entity.setEntityDescription(ed);
		entity.setStatus(status);
		entity.addEntityType(UsageContextConstants.USAGE_CONTEXT_ENTITY_TYPE);
		
		if (StringUtils.isNotEmpty(revisionId))
		{
			EntryState es = new EntryState();
			es.setContainingRevision(revisionId);
			es.setChangeType(ChangeType.NEW);
			entity.setEntryState(es);
		}
		
		if (StringUtils.isNotEmpty(usageContextName))
		{
			Presentation pres = new Presentation();
			pres.setPropertyName(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION);
			Text text = new Text();
			text.setContent(usageContextName);
			pres.setValue(text);
			pres.setIsPreferred(true);
			
			entity.addPresentation(pres);
		}
		
		if (properties != null)
			entity.addAnyProperties(properties.getPropertyAsReference());
		
		insertUsageContext(entity, versionOrTag);

	}

	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#insertUsageContext(org.LexGrid.concepts.Entity, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public void insertUsageContext(Entity usageContext,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		if (usageContext == null)
			return;
		
		if (!validateType(usageContext))
		{
			throw new LBException("Invalid entity type found in entity object. Only valid entity type for usage context entity is 'usageContext'");
		}
		
		CodingSchemeVersionOrTag csVT = versionOrTag;
		if (csVT == null || StringUtils.isEmpty(csVT.getVersion()))
		{
			csVT = Constructors.createCodingSchemeVersionOrTag(null, 
					UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		}
		
		CodingScheme cs = null;
		
		try {
				cs = getLexBIGService().resolveCodingScheme(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, 
				csVT);
		} catch (LBParameterException e){ // if usage context coding scheme does not exists in the system, create it.
			if (e.getMessage().indexOf("No URI found") != -1)
			{
				CodingSchemeBuilder csBuilder = new CodingSchemeBuilder();
				cs = csBuilder.build(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI,
						UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME,
					csVT.getVersion(),
					UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME,
					null, 
					null, 
					null, 
					null);
				
				// insert usage context coding scheme into system
				getCodingSchemeService().insertCodingScheme(cs, null);
				
				AbsoluteCodingSchemeVersionReference acsvr = Constructors.createAbsoluteCodingSchemeVersionReference(
						UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, 
						csVT.getVersion());
				
				// activate usage context coding scheme
				getLexBIGService().getServiceManager(null).activateCodingSchemeVersion(acsvr);
				
				if (StringUtils.isNotEmpty(csVT.getTag()))
					getLexBIGService().getServiceManager(null).setVersionTag(acsvr, csVT.getTag());
				
				// create empty Lucene entry for usage context coding scheme
				getEntityIndexService().createIndex(acsvr);				
			}
			else
			{
				throw new LBException("Problem inserting usage context", e);
			}
		}
		
		// insert concept domain
		getDatabaseEntityService().insertEntity(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, 
				csVT.getVersion(), usageContext);
		
		// create lucene index for newly create usage context
		getEntityIndexService().addEntityToIndex(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI,
				csVT.getVersion(), usageContext);

	}

	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#listAllUsageContextEntities(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<Entity> listAllUsageContextEntities(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		List<Entity> entityList = new ArrayList<Entity>();
		CodedNodeSet cns = getUsageContextCodedNodeSet(versionOrTag);
		
		if (cns != null)
		{
			ResolvedConceptReferencesIterator rcrIter = cns.resolve(null, null, null);
			while (rcrIter.hasNext())
			{
				ResolvedConceptReference rcr = rcrIter.next();
				entityList.add(rcr.getEntity());
			}
		}
		return entityList;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#listAllUsageContextIds(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<String> listAllUsageContextIds(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		List<String> idsList = new ArrayList<String>();
		List<Entity> entityList = listAllUsageContextEntities(versionOrTag);
		for (Entity entity : entityList)
		{
			idsList.add(entity.getEntityCode());
		}
		return idsList;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#removeUsageContext(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public void removeUsageContext(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		if (StringUtils.isEmpty(usageContextId))
			throw new LBException("usage context id can not be empty");
		
		if (versionOrTag == null || StringUtils.isEmpty(versionOrTag.getVersion()))
			throw new LBException("Version can not be empty");
		
		Entity entity = getUsageContextEntity(usageContextId, versionOrTag);
		
		if (entity != null)
		{
			// remove from database
			getDatabaseEntityService().
					removeEntity(
							UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI,
							versionOrTag.getVersion(),
							entity);
			// remove from lucene index
			getEntityIndexService().deleteEntityFromIndex(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI,
				versionOrTag.getVersion(),
				entity);
		}
		else
		{
			throw new LBException("No usage context entity found with id : " + usageContextId);
		}
	}
	
	private boolean validateType(Entity usageContext){
		if (usageContext.getEntityTypeCount() == 0)
			return false;
		
		for (String type : usageContext.getEntityTypeAsReference())
		{
			if (type.equalsIgnoreCase(UsageContextConstants.USAGE_CONTEXT_ENTITY_TYPE))
				return true;
		}
		
		return false;
	}
	
	private LexBIGService getLexBIGService(){
		if (lbsvc_ == null)
			lbsvc_ = LexBIGServiceImpl.defaultInstance();
		
		return lbsvc_;
	}
	
	private EntityService getDatabaseEntityService() {
		return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();
	}
	
	private CodingSchemeService getCodingSchemeService() {
		return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
	}
	
	private EntityIndexService getEntityIndexService() {
		return LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService();
	}
}
