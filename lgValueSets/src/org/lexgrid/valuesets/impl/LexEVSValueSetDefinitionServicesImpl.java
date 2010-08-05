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
package org.lexgrid.valuesets.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.LexGrid_Exporter;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.naming.Mappings;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.service.SystemResourceService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.admin.Util;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.helper.VSDServiceHelper;

import edu.mayo.informatics.lexgrid.convert.formats.Option;

/**
 * Implementation of Value Set Definition for LexGrid.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSValueSetDefinitionServicesImpl implements LexEVSValueSetDefinitionServices {

	// Associated service ...
	private transient LexBIGService lbs_;
	private transient VSDServiceHelper sh_;
	protected MessageDirector md_;
	protected LoadStatus status_;
	private static final String name_ = "LexEVSValueSetDefinitionServicesImpl";
	private static final String desc_ = "Implements LexGrid Value Set Definition services.";
	private static final String provider_ = "Mayo Clinic";
	private static final String version_ = "2.0";
	
	private static LexEVSValueSetDefinitionServices valueSetService_ = null;
	
	private static final long serialVersionUID = 4995582014921448463L;

	/**
     * Returns a default singleton instance of the service.
     * <p>
     * Note: This is the recommended method of acquiring the service, since it
     * will allow the application to run without change in distributed LexBIG
     * environments (in which case the default instance is actually a
     * distributed service). However, use of the public constructor is supported
     * to preserve backward compatibility.
     * 
     * @return LexEVSValueSetDefinitionServices
     */
    public static LexEVSValueSetDefinitionServices defaultInstance() {
        if (valueSetService_ == null)
            valueSetService_ = new LexEVSValueSetDefinitionServicesImpl();
        return valueSetService_;
    }

    /**
     * Assigns the default singleton instance of the service.
     * <p>
     * Note: While this method is public, it is generally not intended to be
     * part of the externalized API. It is made public so that the runtime
     * system has the ability to assign the default instance when running in
     * distributed LexBIG environments, etc.
     * 
     * @param LexEVSValueSetDefinitionServicesImpl
     *            the default instance.
     */
    public static void setDefaultInstance(LexEVSValueSetDefinitionServicesImpl defaultInstance) {
    	valueSetService_ = defaultInstance;
    }
    
	public LexEVSValueSetDefinitionServicesImpl() {
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
	
	public LogEntry[] getLogEntries(){
		if (md_ != null)
			return md_.getLogEntries(null);
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#loadValueSetDefinition(org.LexGrid.valueSets.ValueSetDefinition, java.lang.String, org.LexGrid.naming.Mappings)
	 */
	@LgAdminFunction
	public void loadValueSetDefinition(ValueSetDefinition definition, String systemReleaseURI, Mappings mappings)
			throws LBException {
		getLogger().logMethod(new Object[] { definition });
		if (definition != null)
		{
			String uri = definition.getValueSetDefinitionURI();
			md_.info("Loading value set definition : " + uri);
			this.getDatabaseServiceManager().getValueSetDefinitionService().insertValueSetDefinition(definition, systemReleaseURI, mappings);
			md_.info("Finished loading value set definition URI : " + uri);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#loadValueSetDefinition(java.lang.String, boolean)
	 */
	@Override
	@LgAdminFunction
	public void loadValueSetDefinition(String xmlFileLocation,
			boolean failOnAllErrors) throws LBException {
		md_.info("Loading value set definitions from file : " + xmlFileLocation);
		
		Loader loader = (LexGridMultiLoaderImpl) getLexBIGService().getServiceManager(null).getLoader("LexGrid_Loader");
        
        md_.info("Loading value set definitions from file : " + xmlFileLocation);
        loader.getOptions().getBooleanOption(Option.getNameForType(Option.FAIL_ON_ERROR)).setOptionValue(failOnAllErrors);
		loader.load(Util.string2FileURI(xmlFileLocation));
		
        while (loader.getStatus().getEndTime() == null) {
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

		md_.info("Finished loading value set definitions");
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#validate(java.net.URI, int)
	 */
	public void validate(URI uri, int validationLevel) throws LBException{
		LexBIGServiceManager lbsm;
		lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
            	.getLoader("LexGrid_Loader");
		loader.validate(uri, validationLevel);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#isEntityInValueSet(java.lang.String, java.net.URI, java.lang.String, java.lang.String)
	 */
	@Override
	public AbsoluteCodingSchemeVersionReference isEntityInValueSet(
			String entityCode, URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, String versionTag)
			throws LBException {
		getLogger().logMethod(new Object[] { entityCode, valueSetDefinitionURI, versionTag });
        return isEntityInValueSet(entityCode, null, valueSetDefinitionURI, valueSetDefinitionRevisionId, null, versionTag);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#isEntityInValueSet(java.lang.String, java.net.URI, java.net.URI, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)
	 */
	@Override
	public AbsoluteCodingSchemeVersionReference isEntityInValueSet(
			String entityCode, URI entityCodeNamespace,
			URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, 
			AbsoluteCodingSchemeVersionReferenceList csVersionList,
			String versionTag) throws LBException {
		getLogger().logMethod(new Object[] { entityCode, entityCodeNamespace, valueSetDefinitionURI, csVersionList, versionTag });
		
		if (valueSetDefinitionURI == null)
			throw new LBException("Value Set Definition URI is empty");
		
        String entityCodeNamespaceString = entityCodeNamespace != null && !StringUtils.isEmpty(entityCodeNamespace.toString())? entityCodeNamespace.toString() : null;
        ValueSetDefinition vdDef = null;
        
        if (StringUtils.isEmpty(valueSetDefinitionRevisionId))
        {
        	vdDef = this.getValueSetDefinitionService().getValueSetDefinitionByUri(valueSetDefinitionURI);
        }
        else
        {
        	vdDef = this.getValueSetDefinitionService().getValueSetDefinitionByRevision(valueSetDefinitionURI.toString(), valueSetDefinitionRevisionId);
        }
        
        if (vdDef != null) {
            ResolvedValueSetCodedNodeSet rvdcns = getServiceHelper().getResolvedCodedNodeSetForValueDomain(vdDef, csVersionList, versionTag);            
            if (rvdcns != null && rvdcns.getCodedNodeSet() != null && rvdcns.getCodingSchemeVersionRefList() != null) {
                Iterator<? extends AbsoluteCodingSchemeVersionReference> csUsedIter = rvdcns.getCodingSchemeVersionRefList().iterateAbsoluteCodingSchemeVersionReference();
                CodedNodeSet resolvedSet = rvdcns.getCodedNodeSet();
                while(csUsedIter.hasNext()) {
                    AbsoluteCodingSchemeVersionReference csUsed = csUsedIter.next();
                    String csLocalName = LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(csUsed.getCodingSchemeURN(), csUsed.getCodingSchemeVersion());
                    ConceptReference cr = entityCodeNamespaceString == null? 
                            Constructors.createConceptReference(entityCode, csLocalName) :
                            Constructors.createConceptReference(entityCode, entityCodeNamespaceString, csLocalName);
                    if(resolvedSet.isCodeInSet(cr))
                        return csUsed;
                }
            }
        } else {
            md_.fatal("No Value set definition found for URI : " + valueSetDefinitionURI);
            throw new LBException("No Value set definition found for URI : " + valueSetDefinitionURI);
        }
        return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getCodedNodeSetForValueSetDefinition(java.net.URI, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)
	 */
	public ResolvedValueSetCodedNodeSet getCodedNodeSetForValueSetDefinition(
            URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, 
            AbsoluteCodingSchemeVersionReferenceList csVersionList,
            String versionTag) throws LBException {
        getLogger().logMethod(new Object[] { valueSetDefinitionURI, csVersionList, versionTag });
        if (valueSetDefinitionURI == null)
        	throw new LBException("Value Set Definition URI can not be empty");
        
        ResolvedValueSetCodedNodeSet domainNodes = null;
        ValueSetDefinition vdDef = null;
        
        if (StringUtils.isNotEmpty(valueSetDefinitionRevisionId))
        	vdDef = this.getValueSetDefinitionService().getValueSetDefinitionByRevision(valueSetDefinitionURI.toString(), valueSetDefinitionRevisionId);
        else
        	vdDef = this.getValueSetDefinitionService().getValueSetDefinitionByUri(valueSetDefinitionURI);
        
        if(vdDef != null) {
            domainNodes = getServiceHelper().getResolvedCodedNodeSetForValueDomain(vdDef, csVersionList, versionTag);
            if (domainNodes != null && domainNodes.getCodedNodeSet() != null)
            	domainNodes.getCodedNodeSet().restrictToStatus(ActiveOption.ACTIVE_ONLY, null);            
        }
        return domainNodes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#resolveValueSetDefinition(java.net.URI, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.SortOptionList)
	 */
	@Override
	public ResolvedValueSetDefinition resolveValueSetDefinition(
			URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, 
			AbsoluteCodingSchemeVersionReferenceList csVersionList,
			String versionTag, SortOptionList sortOptionList) throws LBException {
		getLogger().logMethod(new Object[] { valueSetDefinitionURI, csVersionList, versionTag });
        
		if (valueSetDefinitionURI == null)
        	throw new LBException("Value Set Definition URI can not be empty");
        
        ValueSetDefinition vdDef = null;
        
        if (StringUtils.isNotEmpty(valueSetDefinitionRevisionId))
        	vdDef = this.getValueSetDefinitionService().getValueSetDefinitionByRevision(valueSetDefinitionURI.toString(), valueSetDefinitionRevisionId);
        else
        	vdDef = this.getValueSetDefinitionService().getValueSetDefinitionByUri(valueSetDefinitionURI);
        
        if(vdDef != null) {
            ResolvedValueSetCodedNodeSet domainNodes = getServiceHelper().getResolvedCodedNodeSetForValueDomain(vdDef, csVersionList, versionTag);
            
            // Assemble the reply
            ResolvedValueSetDefinition rvddef = new ResolvedValueSetDefinition();
            try {
                rvddef.setValueSetDefinitionURI(new URI(vdDef.getValueSetDefinitionURI()));
            } catch (URISyntaxException e) {
                md_.fatal("Value Set Definition URI is not a valid URI : " + vdDef.getValueSetDefinitionURI());
                throw new LBException("Value Set Definition URI is not a valid URI : " + vdDef.getValueSetDefinitionURI());
            }
            rvddef.setValueSetDefinitionName(vdDef.getValueSetDefinitionName());
            rvddef.setDefaultCodingScheme(vdDef.getDefaultCodingScheme());
            rvddef.setRepresentsRealmOrContext(vdDef.getRepresentsRealmOrContextAsReference());
            rvddef.setSource(vdDef.getSourceAsReference());
            rvddef.setCodingSchemeVersionRefList(domainNodes.getCodingSchemeVersionRefList());
            if(domainNodes != null && domainNodes.getCodedNodeSet() != null)
                rvddef.setResolvedConceptReferenceIterator(domainNodes.getCodedNodeSet().restrictToStatus(ActiveOption.ACTIVE_ONLY, null).resolve(sortOptionList, null, null, null, true));
            return rvddef;
        } else {
            md_.fatal("No Value DomSet Definition found for URI : " + valueSetDefinitionURI);
            throw new LBException("No Value Set Definition found for URI : " + valueSetDefinitionURI);
        }
    }
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#resolveValueSetDefinition(org.LexGrid.valueSets.ValueSetDefinition, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.SortOptionList)
	 */
	@Override
	public ResolvedValueSetDefinition resolveValueSetDefinition(
			ValueSetDefinition vsDef,  
			AbsoluteCodingSchemeVersionReferenceList csVersionList,
			String versionTag, SortOptionList sortOptionList) throws LBException {
		getLogger().logMethod(new Object[] { vsDef, csVersionList, versionTag });
        
		if(vsDef != null) {
            ResolvedValueSetCodedNodeSet domainNodes = getServiceHelper().getResolvedCodedNodeSetForValueDomain(vsDef, csVersionList, versionTag);
            
            // Assemble the reply
            ResolvedValueSetDefinition rvddef = new ResolvedValueSetDefinition();
            try {
                rvddef.setValueSetDefinitionURI(new URI(vsDef.getValueSetDefinitionURI()));
            } catch (URISyntaxException e) {
                md_.fatal("Value Set Definition URI is not a valid URI : " + vsDef.getValueSetDefinitionURI());
                throw new LBException("Value Set Definition URI is not a valid URI : " + vsDef.getValueSetDefinitionURI());
            }
            rvddef.setValueSetDefinitionName(vsDef.getValueSetDefinitionName());
            rvddef.setDefaultCodingScheme(vsDef.getDefaultCodingScheme());
            rvddef.setRepresentsRealmOrContext(vsDef.getRepresentsRealmOrContextAsReference());
            rvddef.setSource(vsDef.getSourceAsReference());
            rvddef.setCodingSchemeVersionRefList(domainNodes.getCodingSchemeVersionRefList());
            if(domainNodes != null && domainNodes.getCodedNodeSet() != null)
                rvddef.setResolvedConceptReferenceIterator(domainNodes.getCodedNodeSet().restrictToStatus(ActiveOption.ACTIVE_ONLY, null).resolve(sortOptionList, null, null));
            return rvddef;
        }
		return null;
    }
    
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#isSubSet(java.net.URI, java.net.URI, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)
	 */
    @Override
	public boolean isSubSet(URI childValueSetDefinitionURI,
			URI parentValueSetDefinitionURI,
			AbsoluteCodingSchemeVersionReferenceList csVersionList,
			String versionTag) throws LBException {
		getLogger().logMethod(new Object[] { childValueSetDefinitionURI, parentValueSetDefinitionURI , csVersionList, versionTag});

        CodedNodeSet childCNS = null;
        CodedNodeSet parentCNS = null;
        
        // populate ValueSetDefinitions for both child and parent value set definition
        ValueSetDefinition childVDDef = this.getValueSetDefinitionService().getValueSetDefinitionByUri(childValueSetDefinitionURI);
        
        if(childVDDef == null) {
            md_.fatal("No Value set definition found for child domain URI : " + childValueSetDefinitionURI);
            throw new LBException("No Value set definition found for child domain URI : " + childValueSetDefinitionURI);
        }
        ValueSetDefinition parentVDDef = this.getValueSetDefinitionService().getValueSetDefinitionByUri(parentValueSetDefinitionURI);
        if(parentVDDef == null) {
            md_.fatal("No Value set definition found for parent domain URI : " + parentValueSetDefinitionURI);
            throw new LBException("No Value set definition found for parent domain URI : " + parentValueSetDefinitionURI);
        }
        
        // Prune the return version list down to what we've got available
        HashMap<String,String> refVersions = getServiceHelper().pruneVersionList(csVersionList);
        
        // Resolve the child value set definition and populate CodedNodeSet for this domain and the coding scheme version that was used.
        childCNS = getServiceHelper().getCodedNodeSetForValueDomain(childVDDef, refVersions, versionTag);
        if (childCNS == null) {
            md_.fatal("There was a problem creating CodedNodeSet for child value set definition : " + childValueSetDefinitionURI);
            throw new LBException("There was a problem creating CodedNodeSet for child value set definition : " + childValueSetDefinitionURI);
        }
            
        // Resolve the parent value set definition and populate CodedNodeSet for this domain and the coding scheme version that was used.
        parentCNS = getServiceHelper().getCodedNodeSetForValueDomain(parentVDDef, refVersions, versionTag);
        if (parentCNS == null) {
            md_.fatal("There was a problem creating CodedNodeSet for parent value set definition : " + parentValueSetDefinitionURI);
            throw new LBException("There was a problem creating CodedNodeSet for parent value set definition : " + parentValueSetDefinitionURI);
        }
        LocalNameList lnl = new LocalNameList();
        return childCNS.difference(parentCNS.intersect(childCNS)).resolveToList(null, lnl, null, 1).getResolvedConceptReferenceCount() == 0;
    }

    /*
     * (non-Javadoc)
     * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getValueSetDefinition(java.net.URI, java.lang.String)
     */
    public ValueSetDefinition getValueSetDefinition(URI valueSetDefURI, String valueSetRevisionId) throws LBException {
        getLogger().logMethod(new Object[] { valueSetDefURI });
        if (valueSetDefURI == null)
        	throw new LBException("Value Set Definition URI can not be null");
        ValueSetDefinition vsd = null;
        if (StringUtils.isNotEmpty(valueSetRevisionId))
        	vsd = this.getValueSetDefinitionService().getValueSetDefinitionByRevision(valueSetDefURI.toString(), valueSetRevisionId);
        else
        	vsd = this.getValueSetDefinitionService().getValueSetDefinitionByUri(valueSetDefURI);
        
        return vsd;
    }
    
    /*
     * (non-Javadoc)
     * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#listValueSetDefinitions(java.lang.String)
     */
    @Override
	public List<String> listValueSetDefinitions(String valueSetDefinitionName)
			throws LBException {
		getLogger().logMethod(new Object[] { valueSetDefinitionName });
        return this.getValueSetDefinitionService().getValueSetDefinitionURISForName(valueSetDefinitionName);        
    }
    
    /*
     * (non-Javadoc)
     * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#listValueSetDefinitionURIs()
     */
    @Override
	public List<String> listValueSetDefinitionURIs(){
		getLogger().logMethod(new Object[]{});
		return this.getValueSetDefinitionService().listValueSetDefinitionURIs();
	}
    
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getAllValueSetDefinitionsWithNoName()
	 */
	@Override
	public List<String> getAllValueSetDefinitionsWithNoName() throws LBException {
		getLogger().logMethod(new Object[]{});
		return this.getValueSetDefinitionService().getValueSetDefinitionURISForName(" ");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getValueSetDefinitionEntitiesForTerm(java.lang.String, java.lang.String, java.net.URI, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)
	 */
	@Override
	public ResolvedValueSetCodedNodeSet getValueSetDefinitionEntitiesForTerm(
			String term, String matchAlgorithm, URI valueSetDefinitionURI,
			AbsoluteCodingSchemeVersionReferenceList csVersionList,
			String versionTag) throws LBException {
		getLogger().logMethod(new Object[] { term, matchAlgorithm, valueSetDefinitionURI, csVersionList, versionTag });
        
        ValueSetDefinition vdDef = this.getValueSetDefinitionService().getValueSetDefinitionByUri(valueSetDefinitionURI);
        if (vdDef != null) {
            ResolvedValueSetCodedNodeSet domainNodes = getServiceHelper().getResolvedCodedNodeSetForValueDomain(vdDef, csVersionList, versionTag);
            
            // apply the term restriction to the codedNodeSet 
            if (domainNodes != null) {
                AbsoluteCodingSchemeVersionReferenceList csvList = domainNodes.getCodingSchemeVersionRefList();
                String tag = null;
                String version = null;
                
                if (csvList != null && csvList.getAbsoluteCodingSchemeVersionReferenceCount() == 1) {
                    AbsoluteCodingSchemeVersionReference csv = csvList.getAbsoluteCodingSchemeVersionReference(0);
                    String csURI = csv.getCodingSchemeURN();
                    version = csv.getCodingSchemeVersion();
                    CodedNodeSet cns = getServiceHelper().getLexBIGService().getCodingSchemeConcepts(csURI, Constructors.createCodingSchemeVersionOrTag(tag, version));
                    if(matchAlgorithm != null)
                    	cns.restrictToMatchingDesignations(term, null, matchAlgorithm, null);
                    domainNodes.setCodedNodeSet(domainNodes.getCodedNodeSet().intersect(cns));
                } else {
                    domainNodes.setCodedNodeSet(domainNodes.getCodedNodeSet().restrictToMatchingDesignations(term, null, matchAlgorithm == null ? MatchAlgorithms.LuceneQuery.name() : matchAlgorithm, null));
                }
            }
            return domainNodes;
        } else {
            md_.fatal("Value set definition uri : '"+ valueSetDefinitionURI + "' not found.");
            throw new LBException("Value set definition uri : '"+ valueSetDefinitionURI + "' not found.");
        }
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getCodingSchemesInValueSetDefinition(java.net.URI)
	 */
	@Override
	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemesInValueSetDefinition(
			URI valueSetDefinitionURI) throws LBException {
		getLogger().logMethod(new Object[] { valueSetDefinitionURI });
		
		AbsoluteCodingSchemeVersionReferenceList csList = new AbsoluteCodingSchemeVersionReferenceList();
		
		// Get value set definition object for supplied uri.
		ValueSetDefinition vd = this.getValueSetDefinitionService().getValueSetDefinitionByUri(valueSetDefinitionURI);
		
		// Get a list of all the coding schemes in the domain
		HashSet<String> vdURIs = getServiceHelper().getCodingSchemeURIs(vd);
		
		// For each URI, locate the version(s) available in the service itself.
		Iterator<String> uriIter = vdURIs.iterator();
		while(uriIter.hasNext()) {
		    String csURI = uriIter.next();
		    AbsoluteCodingSchemeVersionReferenceList csVersions = getServiceHelper().getAbsoluteCodingSchemeVersionReference(csURI);
		    if(csVersions.getAbsoluteCodingSchemeVersionReferenceCount() == 0) {
		        AbsoluteCodingSchemeVersionReference acvr = new AbsoluteCodingSchemeVersionReference();
		        acvr.setCodingSchemeURN(csURI);
		        csList.addAbsoluteCodingSchemeVersionReference(acvr);
		    } else {
		        csList.addAbsoluteCodingSchemeVersionReference(csVersions.getAbsoluteCodingSchemeVersionReference(0));
		    } 
		}
		return csList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#isValueSetDefinition(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public boolean isValueSetDefinition(String entityCode,
			String codingSchemeName, CodingSchemeVersionOrTag csvt)
			throws LBException {
		getLogger().logMethod(new Object[] { entityCode, codingSchemeName, csvt });
		
		LocalNameList lnl = Constructors.createLocalNameList(SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAIN);
		
		// Populate CodedNodeSet for supplied domainId and csVersion and restrict to 'valueDomain' type.
		CodedNodeSet cns = getLexBIGService().getNodeSet(codingSchemeName, csvt, lnl);
		cns.restrictToCodes(Constructors.createConceptReferenceList(entityCode));
		
		Iterator<? extends ResolvedConceptReference> rcrIter = cns.resolveToList(null, null, null, null, 1).iterateResolvedConceptReference();
		// If there were entities after resolving the codedNodeSet; the supplied domainId is a valueDomain
		if (rcrIter.hasNext()) {
			return true;
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#removeValueSetDefinition(java.net.URI)
	 */
	@Override
	@LgAdminFunction
	public void removeValueSetDefinition(URI valueSetDefinitionURI)
			throws LBException {
		if (valueSetDefinitionURI != null)
		{
			md_.info("removing value set definition : " + valueSetDefinitionURI);
			SystemResourceService service = LexEvsServiceLocator.getInstance().getSystemResourceService();
			this.getValueSetDefinitionService().removeValueSetDefinition(valueSetDefinitionURI.toString());
			service.removeValueSetDefinitionResourceFromSystem(valueSetDefinitionURI.toString(), null);
			md_.info("DONE removing value set definition : " + valueSetDefinitionURI);
		}
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

	private VSDServiceHelper getServiceHelper(){
		if (sh_ == null)
		{
			try {
				sh_ = new VSDServiceHelper(true, md_);
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
	
	public String getStringFromURI(URI uri) throws LBParameterException {
		if ("file".equals(uri.getScheme()))

        {
            File temp = new File(uri);
            return temp.getAbsolutePath();
        }
		return uri.toString();
    }
	
	
	/* (non-Javadoc)
	 * @see org.lexgrid.valuedomain.LexEVSValueDomainServices#exportValueSetDefinition(java.net.URI, java.lang.String, boolean, boolean)
	 */
	public void exportValueSetDefinition(URI valueDomainURI,
			String xmlFullPathName, boolean overwrite, boolean failOnAllErrors)
			throws LBException {
		md_.info("Starting to export value domain definition : " + valueDomainURI);
		if (StringUtils.isNotEmpty(xmlFullPathName))
		{
			File f = new File(xmlFullPathName.trim());
			LexGrid_Exporter exporter = (LexGrid_Exporter)getLexBIGService().getServiceManager(null).getExporter(org.LexGrid.LexBIG.Impl.exporters.LexGridExport.name);			
			exporter.exportValueSetDefinition(valueDomainURI, f.toURI(), overwrite, failOnAllErrors, true);
			
			while (exporter.getStatus().getEndTime() == null) {
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
			md_.info("Done exporting value domain definition : " + valueDomainURI + " to location : " + xmlFullPathName);
		}
		else
		{
			md_.error("XML file destination can not be blank.");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getValueSetDefinitionURIsForSupportedTagAndValue(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURIsForSupportedTagAndValue(
			String supportedTag, String value, String uri) {
		getLogger().logMethod(new Object[]{supportedTag, value});
		return this.getValueSetDefinitionService().getValueSetDefinitionURIForSupportedTagAndValue(supportedTag, value, uri);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getValueSetDefinitionURIsWithCodingScheme(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURIsWithCodingScheme(
			String codingSchemename, String codingSchemeURI) {
		getLogger().logMethod(new Object[]{codingSchemename});
		return this.getValueSetDefinitionService().getValueSetDefinitionURIForSupportedTagAndValue(SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME, codingSchemename, codingSchemeURI);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getValueSetDefinitionURIsWithConceptDomain(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURIsWithConceptDomain(
			String conceptDomain, String codingSchemeURI) {
		getLogger().logMethod(new Object[]{conceptDomain});
		
		return this.getValueSetDefinitionService().getValueSetDefinitionURIForSupportedTagAndValue(
				SQLTableConstants.TBLCOLVAL_SUPPTAG_CONCEPTDOMAIN, conceptDomain, codingSchemeURI);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getValueSetDefinitionURIsWithUsageContext(java.util.List, java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURIsWithUsageContext(
			List<String> usageContexts, String codingSchemeURI) {
		getLogger().logMethod(new Object[]{usageContexts});
		
		Set<String> ucList = new HashSet<String>();
		if (usageContexts != null)
			for (String uc : usageContexts)
			{
				ucList.addAll(this.getValueSetDefinitionService().getValueSetDefinitionURIForSupportedTagAndValue(
											SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT, uc, codingSchemeURI));
			}
		return new ArrayList<String>(ucList);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.LexEVSValueSetDefinitionServices#getValueSetDefinitionURIsWithConceptDomainAndUsageContext(java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURIsWithConceptDomainAndUsageContext(
			String conceptDomain, List<String> usageContexts, String codingSchemeURI) {
		getLogger().logMethod(new Object[]{conceptDomain, usageContexts});
		
		List<String> vsdURIs = new ArrayList<String>();		
		List<String> cdList = getValueSetDefinitionURIsWithConceptDomain(conceptDomain, codingSchemeURI);		
		List<String> ucList = getValueSetDefinitionURIsWithUsageContext(usageContexts, codingSchemeURI);
		
		if (cdList == null)
			return ucList;
		else if (ucList == null)
			return cdList;
		else
		{
			for (String cduri : cdList)
			{
				for (String ucuri : ucList)
				{
					if (ucuri.equals(cduri) && !vsdURIs.contains(ucuri))
						vsdURIs.add(ucuri);
				}
			}
		}
		
		cdList = null;
		ucList = null;
		
		return vsdURIs;
	}	
	
	private DatabaseServiceManager getDatabaseServiceManager() {
		return LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
	}
	
	private ValueSetDefinitionService getValueSetDefinitionService() {
		return this.getDatabaseServiceManager().getValueSetDefinitionService();
	}

}