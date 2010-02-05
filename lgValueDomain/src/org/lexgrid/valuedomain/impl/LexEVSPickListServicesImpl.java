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
package org.lexgrid.valuedomain.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.dataAccess.SystemVariables;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.Impl.logging.LgLoggerIF;
import org.LexGrid.LexBIG.Impl.logging.LoggerFactory;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.valueDomains.PickListDefinition;
import org.LexGrid.emf.valueDomains.PickListEntry;
import org.LexGrid.emf.valueDomains.PickListEntryExclusion;
import org.LexGrid.emf.valueDomains.PickListEntryNode;
import org.LexGrid.emf.versions.SystemRelease;
import org.LexGrid.emf.versions.impl.SystemReleaseImpl;
import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.InsertException;
import org.LexGrid.managedobj.ObjectAlreadyExistsException;
import org.LexGrid.managedobj.RemoveException;
import org.LexGrid.managedobj.ServiceInitException;
import org.apache.commons.lang.StringUtils;
import org.lexgrid.valuedomain.LexEVSPickListServices;
import org.lexgrid.valuedomain.LexEVSValueDomainServices;
import org.lexgrid.valuedomain.dto.ResolvedPickListEntry;
import org.lexgrid.valuedomain.dto.ResolvedPickListEntryList;
import org.lexgrid.valuedomain.dto.ResolvedValueDomainCodedNodeSet;
import org.lexgrid.valuedomain.dto.ResolvedValueDomainDefinition;
import org.lexgrid.valuedomain.helper.PLEntryNodeSortUtil;
import org.lexgrid.valuedomain.persistence.PickListServices;
import org.lexgrid.valuedomain.persistence.PickListsServices;
import org.lexgrid.valuedomain.persistence.SystemReleaseServices;
import org.lexgrid.valuedomain.persistence.VDMappingServices;
import org.lexgrid.valuedomain.persistence.VDServiceHelper;
import org.lexgrid.valuedomain.persistence.VDXMLread;

/**
 * Implements LexEVSPickListSerives.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSPickListServicesImpl implements LexEVSPickListServices {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Associated service ...
	private LexBIGService lbs_;
	private PickListsServices plss_;
	private PickListServices pls_;
	private VDMappingServices mapS_;
	private VDServiceHelper sh_;
	
	protected MessageDirector md_;
	protected LoadStatus status_;
	private static final String name_ = "LexEVSPickListServicesImpl";
	private static final String desc_ = "Implements LexGrid Pick List services.";
	private static final String provider_ = "Mayo Clinic";
	private static final String version_ = "1.0";
	
	
	public LexEVSPickListServicesImpl() {
		getStatus().setState(ProcessState.PROCESSING);
		getStatus().setStartTime(new Date(System.currentTimeMillis()));
		md_ = new MessageDirector(getName(), status_);
	}

	private LoadStatus getStatus() {
		if (status_ == null)
			status_ = new LoadStatus();
		return status_;
	}

	private LgLoggerIF getLogger() {
		return LoggerFactory.getLogger();
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#loadPickList(org.LexGrid.valueDomains.PickListDefinition, java.lang.String)
	 */
	public void loadPickList(PickListDefinition pldef, URI systemReleaseURI, Mappings mappings)
			throws LBException {
		getLogger().logMethod(new Object[] { pldef, systemReleaseURI});
		try {
			getPickListService().insert(pldef, systemReleaseURI, mappings);
		} catch (ObjectAlreadyExistsException e) {
			md_.fatal("Failed loading PickListDefinition : " + pldef.getPickListId(), e);
			throw new LBException(e.getMessage());
		} catch (InsertException e) {
			md_.fatal("Failed loading PickListDefinition : " + pldef.getPickListId(), e);
			throw new LBException(e.getMessage());
		} catch (ServiceInitException e) {
			md_.fatal("Failed loading PickListDefinition : " + pldef.getPickListId(), e);
			throw new LBException(e.getMessage());
		}

	}

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#loadPickList(java.io.InputStream, boolean)
	 */
	public void loadPickList(InputStream inputStream, boolean failOnAllErrors)
			throws LBException {
		getLogger().logMethod(new Object[] { inputStream });
		VDXMLread vdXML = new VDXMLread(inputStream, md_, failOnAllErrors);

		internalLoadPickList(vdXML);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#loadPickList(java.lang.String, boolean)
	 */
	public void loadPickList(String xmlFileLocation, boolean failOnAllErrors)
			throws LBException {
		getLogger().logMethod(new Object[] { xmlFileLocation });
		VDXMLread vdXML = null;
		try {
			vdXML = new VDXMLread(getStringFromURI(new URI(xmlFileLocation)), null, md_,failOnAllErrors);
		} catch (URISyntaxException e) {
			throw new LBException("Failed loading XML.", e);
		}

		internalLoadPickList(vdXML);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#validate(java.net.URI, int)
	 */
	public void validate(URI uri, int v1) throws LBParameterException{
		VDXMLread vdXML = new VDXMLread(uri.toString(), null, md_, true);
		vdXML.validate(uri, v1);
	}
	
	/**
	 * Common method that will be used for loading pick lists to database.
	 * @param vdXML
	 * @throws Exception
	 */
	private void internalLoadPickList(VDXMLread vdXML) throws LBException {
		getLogger().logMethod(new Object[] { vdXML });
		try {
			PickListDefinition[] plDefs = vdXML.readAllPickLists();
			SystemRelease systemRelease = vdXML.getSystemRelease();
			
			String releaseURI = null;
			
			if (systemRelease != null)
			{
				releaseURI = systemRelease.getReleaseURI();
				md_.info("Loading SystemRelease : " + releaseURI);
				SystemReleaseServices releaseService = (SystemReleaseServices) getPickListService().getNestedService(SystemReleaseImpl.class);
				releaseService.insert(systemRelease);
				md_.info("Finished Loading SystemRelease : " + releaseURI);
			}
			
			md_.info("Loading PickListDefinitions");
			for (PickListDefinition pldef : plDefs) {
				loadPickList(pldef, new URI(releaseURI), vdXML.getVdMappings());			
			}
			md_.info("Finished Loading PickListDefinitions");
			
//			md_.info("Loading Mappings");
//			mapS_ = (VDMappingServices) getPickListsService().getNestedService(
//					VDMappingServices.class);
//			mapS_.insert(vdXML.getVdMappings(), -1);
//			md_.info("Finished Loading Mappings");
			
			md_.info("Load Process Complete.");
		} 
		catch (Exception e)
		{
			throw new LBException("Problem loading PickLists", e);
		}
	}

	
	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#getPickListDefinitionById(java.lang.String)
	 */
	public PickListDefinition getPickListDefinitionById(String pickListId) throws LBException{
		PickListDefinition pickList = null;
		try {
			pickList = (PickListDefinition) getPickListService().findByPrimaryKey(pickListId);
		} catch (FindException e) {
			md_.fatal("Problem getting PickLists for pickListId : " + pickListId, e);
			throw new LBException("Problem getting PickLists for pickListId : " + pickListId, e);
		} catch (ServiceInitException e) {
			md_.fatal("Problem getting PickLists for pickListId : " + pickListId, e);
			throw new LBException("Problem getting PickLists for pickListId : " + pickListId, e);
		}
		return pickList;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#listPickListIds()
	 */
	public List<String> listPickListIds() throws LBException {
		List<String> pickListIds;
		try {
			pickListIds = getPickListService().listPickListIds();
		} catch (FindException e) {
			md_.fatal("Problem getting list of PickListIds", e);
			throw new LBException("Problem getting list of PickListIds", e);
		} catch (ServiceInitException e) {
			md_.fatal("Problem getting list of PickListIds", e);
			throw new LBException("Problem getting list of PickListIds", e);
		}
		return pickListIds;
	}	

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#getPickListValueDomain(java.lang.String)
	 */
	public URI getPickListValueDomain(String pickListId) throws LBException {
		URI valueDomainURI = null;
		
		try {
			PickListDefinition pickList = getPickListDefinitionById(pickListId);
			if (pickList != null)
				valueDomainURI = new URI(pickList.getRepresentsValueDomain()); // TODO need to change representsValueDomain from String to URI in XML schema
		} catch (URISyntaxException e) {
			md_.fatal("Problem getting PickLists for pickListId : " + pickListId, e);
			throw new LBException("Problem getting PickLists for pickListId : " + pickListId, e);
		}
		return valueDomainURI;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#getPickListDefinitionsForDomain(java.net.URI)
	 */
	public PickListDefinition[] getPickListDefinitionsForDomain(URI valueDomainURI) throws LBException {
		PickListDefinition[] pickLists = null;
		try {
			pickLists = getPickListService().findByValueDomainURI(valueDomainURI);
		} catch (FindException e) {
			md_.fatal("Problem getting PickLists for ValueDomainURI : " + valueDomainURI, e);
			throw new LBException("Problem getting PickLists for ValueDomainURI : " + valueDomainURI, e);
		} catch (ServiceInitException e) {
			md_.fatal("Problem getting PickLists for ValueDomainURI : " + valueDomainURI, e);
			throw new LBException("Problem getting PickLists for ValueDomainURI : " + valueDomainURI, e);
		} catch (LBException e) {
			md_.fatal("Problem getting PickLists for ValueDomainURI : " + valueDomainURI, e);
			throw new LBException("Problem getting PickLists for ValueDomainURI : " + valueDomainURI, e);
		}
		return pickLists;
	}
	
	
	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#resolvePickListForTerm(java.lang.String, java.lang.String, org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms)
	 */
	public ResolvedPickListEntryList resolvePickListForTerm(String pickListId,
			String term, String matchAlgorithm, String language, String[] context, boolean sortByText) throws LBException {
		ResolvedPickListEntryList plList = new ResolvedPickListEntryList();
		
		List<String> rcrCodes = new ArrayList<String>();
		List<String> excludeEntityCodes = new ArrayList<String>();
		
		PickListDefinition pickList = getPickListDefinitionById(pickListId);
		if (pickList != null)
		{
			String defaultCS = pickList.getDefaultEntityCodeNamespace();
			String defaultLang = pickList.getDefaultLanguage();
			
			boolean completeDomain = pickList.isCompleteDomain();
			
			//TODO, if completeDomain is true, dynamically populate pickListEntries
			if (completeDomain)
			{
				return internalResolvePickListForTerm(pickList.getRepresentsValueDomain(), term, matchAlgorithm, language, context, sortByText);
			}
			
			// get all static pickListEntryNodes to get any exclude entries.
			List<PickListEntryNode> plEntryNodeList = pickList.getPickListEntryNode();
			
			// Get all exclude list
			for (int i = 0; i < plEntryNodeList.size(); i++)
			{
				PickListEntryNode plEntryNode = plEntryNodeList.get(i);
				
				PickListEntryExclusion plEntryExclusion = plEntryNode.getExclusionEntry();
				if (plEntryExclusion != null)
					excludeEntityCodes.add(plEntryExclusion.getEntityCode());
			}
			
			CodedNodeSet cns = null;
			ConceptReferenceList crList = new ConceptReferenceList();
			
			
			
			for (int i = 0; i < plEntryNodeList.size(); i++)
			{
				PickListEntry plEntry = plEntryNodeList.get(i).getInclusionEntry();
				
				if (plEntry != null && !excludeEntityCodes.contains(plEntry.getEntityCode()))
				{
					String cs = plEntry.getEntityCodeNamespace();
					if (StringUtils.isEmpty(cs))
						cs = defaultCS;
					
					if (StringUtils.isNotEmpty(cs))
					{
						crList.addConceptReference(Constructors.createConceptReference(plEntry.getEntityCode(), cs));
						if (StringUtils.isEmpty(defaultCS))
							defaultCS = cs;
						if (StringUtils.isEmpty(defaultLang))
							defaultLang = plEntry.getLanguage();
					}
				}
			}
			
			if (crList.getConceptReferenceCount() > 0)
			{
				//TODO not sure about this.. should we get the coding scheme version from the user ?
				AbsoluteCodingSchemeVersionReferenceList acsvrList = sh_.getAbsoluteCodingSchemeVersionReference(defaultCS);
				CodingSchemeVersionOrTag csvt = null;
				
				if (acsvrList.getAbsoluteCodingSchemeVersionReferenceCount() > 0)
				{
					AbsoluteCodingSchemeVersionReference acsvr = acsvrList.getAbsoluteCodingSchemeVersionReference(0);
					csvt = Constructors.createCodingSchemeVersionOrTag(null, acsvr.getCodingSchemeVersion());
				}
				
				cns = getLexBIGService().getCodingSchemeConcepts(defaultCS, csvt);
				
				cns.restrictToCodes(crList);
				
				cns.restrictToMatchingDesignations(term, null, matchAlgorithm != null ? matchAlgorithm : MatchAlgorithms.LuceneQuery.name(), defaultLang);
				
				ResolvedConceptReferencesIterator rcrItr = cns.resolve(null, null, null, null, false);
				
				while (rcrItr.hasNext())
				{
					rcrCodes.add(rcrItr.next().getCode());
				}
				
				for (int i = 0; i < plEntryNodeList.size(); i++)
				{
					PickListEntry plEntry = plEntryNodeList.get(i).getInclusionEntry();
					
					if (plEntry != null && rcrCodes.contains(plEntry.getEntityCode()))
					{
						ResolvedPickListEntry rpl = new ResolvedPickListEntry();
						rpl.setDefault(plEntry.isIsDefault());
						rpl.setEntityCode(plEntry.getEntityCode());
						rpl.setEntityCodeNamespace(plEntry.getEntityCodeNamespace());
						rpl.setPickText(plEntry.getPickText());
						rpl.setPropertyId(plEntry.getPropertyId());
						plList.addResolvedPickListEntry(rpl);
					}
				}
			}
		}
		
		return plList;
	}
	
	private ResolvedPickListEntryList internalResolvePickListForTerm(String valueDomainURI,
			String term, String matchAlgorithm, String language, String[] context, boolean sortByText) throws LBException {
		
		ResolvedPickListEntryList plList = new ResolvedPickListEntryList();
		
		LexEVSValueDomainServices vds = new LexEVSValueDomainServicesImpl();
		
		ResolvedValueDomainCodedNodeSet rvdCNS;
		try {
			rvdCNS = vds.getValueDomainEntitiesForTerm(term, matchAlgorithm, new URI(valueDomainURI), null, null);
		} catch (URISyntaxException e) {
			throw new LBException("Problem with ValueDomain URI", e);
		}
		
		CodedNodeSet cns = rvdCNS.getCodedNodeSet();
		
		ResolvedConceptReferencesIterator rcrItr = cns.resolve(null, null, null, null, true);
		
		while (rcrItr.hasNext())
		{
			ResolvedConceptReference rcr = rcrItr.next();
			ResolvedPickListEntry rpl = new ResolvedPickListEntry();
			rpl.setEntityCode(rcr.getCode());
			rpl.setEntityCodeNamespace(rcr.getCodeNamespace());
			Entity entity = rcr.getEntity();
			Presentation[] presentations = entity.getPresentation();
			for (Presentation pres : presentations)
			{
				if (pres.isIsPreferred())
				{
					rpl.setPickText(pres.getValue().getContent());
					rpl.setPropertyId(pres.getPropertyId());
					plList.addResolvedPickListEntry(rpl);
				}				
			}
		}
		
		return plList;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#resolvePickList(java.lang.String, boolean)
	 */
	public ResolvedPickListEntryList resolvePickList(String pickListId, boolean sortByText) throws LBException {
		ResolvedPickListEntryList plList = new ResolvedPickListEntryList();
        
        List<String> excludeEntityCodes = new ArrayList<String>();
        Set<String> includedEntityCodes = new HashSet<String>();
        
        PickListDefinition pickList = getPickListDefinitionById(pickListId);
        if (pickList != null)
        {
            String defaultCS = pickList.getDefaultEntityCodeNamespace();
            String defaultLang = pickList.getDefaultLanguage();
            
            boolean completeDomain = pickList.isCompleteDomain();
            
            // get all static pickListEntryNodes to get any exclude entries.
            List<PickListEntryNode> plEntryNodeList = pickList.getPickListEntryNode();
            
            // Get all exclude list
            for (int i = 0; i < plEntryNodeList.size(); i++)
            {
                PickListEntryNode plEntryNode = plEntryNodeList.get(i);
                
                PickListEntryExclusion plEntryExclusion = plEntryNode.getExclusionEntry();
                if (plEntryExclusion != null)
                    excludeEntityCodes.add(plEntryExclusion.getEntityCode());
            }
            
            
            for (int i = 0; i < plEntryNodeList.size(); i++)
            {
                PickListEntry plEntry = plEntryNodeList.get(i).getInclusionEntry();
                
                if (plEntry != null && !excludeEntityCodes.contains(plEntry.getEntityCode()))
                {
                    String cs = plEntry.getEntityCodeNamespace();
                    if (StringUtils.isEmpty(cs))
                        cs = defaultCS;
                    
                    includedEntityCodes.add(plEntry.getEntityCode());
                    
                    ResolvedPickListEntry rpl = new ResolvedPickListEntry();
                    rpl.setDefault(plEntry.isIsDefault());
                    rpl.setEntityCode(plEntry.getEntityCode());
                    rpl.setEntityCodeNamespace(StringUtils.isEmpty(plEntry.getEntityCodeNamespace()) ? cs : plEntry.getEntityCodeNamespace());
                    rpl.setEntryOrder(plEntry.getEntryOrder());
                    rpl.setPickText(plEntry.getPickText());
                    rpl.setPropertyId(plEntry.getPropertyId());
                    plList.addResolvedPickListEntry(rpl);
                }
            }
            
            //if completeDomain is true, resolve value domain and add any missing entities to the list
            if (completeDomain)
            {
            	ResolvedPickListEntryList vdPLList = internalResolvePickListForTerm(pickList.getRepresentsValueDomain(), sortByText);
            	if (plList.getResolvedPickListEntryCount() > 0 )
            	{
            		for (int i = 0; i < vdPLList.getResolvedPickListEntryCount(); i++)
            		{
            			ResolvedPickListEntry vdple = vdPLList.getResolvedPickListEntry(i);
            			if (!includedEntityCodes.contains(vdple.getEntityCode()) && !excludeEntityCodes.contains(vdple.getEntityCode()))
            				plList.addResolvedPickListEntry(vdple);
            		}
            	}
            	else
            	{
            		return vdPLList;
            	}
            }
            
            
        }
        
        return plList;
    }
    
	/**
	 * Resolves pickList definition for supplied pickListId.
	 * 
	 * @param pickListId
	 * 			pickListId of a pickListDefinition.
	 * @param sortByText
	 * 			If 1-Ascending, 2-Descending, and 3-Custom;
	 * @return
	 * 			Resolved PickListEntries.
	 * @throws LBException
	 */
	public ResolvedPickListEntryList resolvePickList(String pickListId,
			Integer sortType) throws LBException {

		ResolvedPickListEntryList resolvedPLEntry = resolvePickList(pickListId,
				true);

		if( sortType == null ) {
			sortType = PLEntryNodeSortUtil.CUSTOM;
		}
		
		ResolvedPickListEntry[] sortedPLEntry = PLEntryNodeSortUtil.sort(
				resolvedPLEntry.getResolvedPickListEntry(), sortType);

		resolvedPLEntry.setResolvedPickListEntry(sortedPLEntry);

		return resolvedPLEntry;
	}
	
    private ResolvedPickListEntryList internalResolvePickListForTerm(String valueDomainURI, boolean sortByText) throws LBException {
        
        ResolvedPickListEntryList plList = new ResolvedPickListEntryList();
        
        LexEVSValueDomainServices vds = new LexEVSValueDomainServicesImpl();
        
        ResolvedValueDomainDefinition rvdDef;
        try {
            rvdDef = vds.resolveValueDomain(new URI(valueDomainURI), null, null);
        } catch (URISyntaxException e) {
            throw new LBException("Problem with ValueDomain URI", e);
        }
        
        ResolvedConceptReferencesIterator rcrItr = rvdDef.getResolvedConceptReferenceIterator();
        
        while (rcrItr.hasNext())
        {
            ResolvedConceptReference rcr = rcrItr.next();
            ResolvedPickListEntry rpl = new ResolvedPickListEntry();
            rpl.setEntityCode(rcr.getCode());
            rpl.setEntityCodeNamespace(rcr.getCodeNamespace());
            Entity entity = rcr.getEntity();
            Presentation[] presentations = entity.getPresentation();
            for (Presentation pres : presentations)
            {
                if (pres.isIsPreferred())
                {
                    rpl.setPickText(pres.getValue().getContent());
                    rpl.setPropertyId(pres.getPropertyId());
                    plList.addResolvedPickListEntry(rpl);
                }               
            }
        }
        
        return plList;
    }

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSPickListServices#removePickList(java.lang.String)
	 */
	public void removePickList(String pickListId) throws LBException, RemoveException {
		getLogger().logMethod(new Object[] { pickListId });
		try {
			getPickListService().remove(pickListId);
		} catch (FindException e) {
			md_.fatal("Problem removing PickListDefinition with id : " + pickListId, e);
			throw new LBException("Problem removing PickListDefinition with id : " + pickListId, e);
		} catch (ServiceInitException e) {
			md_.fatal("Problem removing PickListDefinition with id : " + pickListId, e);
			throw new LBException("Problem removing PickListDefinition with id : " + pickListId, e);
		}
	}
	
	public Map<String, String> getReferencedPLDefinitions(String entityCode,
			String entityCodeNameSpace, String propertyId,
			Boolean extractPickListName) throws FindException,
			ServiceInitException, LBException {
		return getPickListService().getReferencedPLDefinitions(entityCode,
				entityCodeNameSpace, propertyId, extractPickListName);
	}

	public Map<String, String> getReferencedPLDefinitions(
			String valueSet, Boolean extractPickListName)
			throws FindException, ServiceInitException, LBException {
		return getPickListService().getReferencedPLDefinitions(valueSet,
				extractPickListName);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.LexGrid.LexBIG.Extensions.Extendable#getDescription()
	 */
	public String getDescription() {
		return desc_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.LexGrid.LexBIG.Extensions.Extendable#getName()
	 */
	public String getName() {
		return name_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.LexGrid.LexBIG.Extensions.Extendable#getProvider()
	 */
	public String getProvider() {
		return provider_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.LexGrid.LexBIG.Extensions.Extendable#getVersion()
	 */
	public String getVersion() {
		return version_;
	}
	
	public LogEntry[] getLogEntries(){
		if (md_ != null)
			return md_.getLogEntries(null);
		
		return null;
	}
	
	/**
	 * Assign the associated LexBIGService instance.
	 * <p>
	 * Note: This method must be invoked by users of the distributed LexBIG API
	 * to set the service to an EVSApplicationService object, allowing client
	 * side implementations to use these convenience methods.
	 */
	@LgClientSideSafe
	public void setLexBIGService(LexBIGService lbs) {
		lbs_ = lbs;
	}

	/**
	 * Return the associated LexBIGService instance; lazy initialized as
	 * required.
	 */
	@LgClientSideSafe
	public LexBIGService getLexBIGService() {
		if (lbs_ == null)
			lbs_ = LexBIGServiceImpl.defaultInstance();
		return lbs_;
	}

	private PickListServices getPickListService() throws ServiceInitException, LBException {
		if (pls_ == null)
			pls_ = getServiceHelper().getPickListService();
		return pls_;
	}
	
	private PickListsServices getPickListsService() throws ServiceInitException, LBException {
		if (plss_ == null)
			plss_ = getServiceHelper().getPickListsService();
		return plss_;
	}

	private VDServiceHelper getServiceHelper(){
		if (sh_ == null)
		{
			SystemVariables sv = ResourceManager.instance().getSystemVariables();
			try {
				sh_ = new VDServiceHelper(sv.getAutoLoadDBURL(), sv.getAutoLoadDBDriver(), sv.getAutoLoadDBUsername(),
						sv.getAutoLoadDBPassword(), sv.getAutoLoadDBPrefix(), true, md_);
			} catch (LBParameterException e) {
				md_.fatal("Problem getting ServiceHelper", e);
				e.printStackTrace();
			} catch (LBInvocationException e) {
				md_.fatal("Problem getting ServiceHelper", e);
				e.printStackTrace();
			}
		}
		return sh_;
	}
	
	private String getStringFromURI(URI uri) throws LBParameterException {
        if ("file".equals(uri.getScheme()))

        {
            File temp = new File(uri);
            return temp.getAbsolutePath();
        } 
        
        return uri.toString();
    }
}