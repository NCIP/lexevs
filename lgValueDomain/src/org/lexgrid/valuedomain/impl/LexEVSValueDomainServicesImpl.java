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
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
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
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.emf.versions.SystemRelease;
import org.LexGrid.emf.versions.impl.SystemReleaseImpl;
import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.InsertException;
import org.LexGrid.managedobj.ObjectAlreadyExistsException;
import org.LexGrid.managedobj.RemoveException;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;
import org.lexgrid.valuedomain.LexEVSValueDomainServices;
import org.lexgrid.valuedomain.dto.ResolvedValueDomainCodedNodeSet;
import org.lexgrid.valuedomain.dto.ResolvedValueDomainDefinition;
import org.lexgrid.valuedomain.persistence.SystemReleaseServices;
import org.lexgrid.valuedomain.persistence.VDMappingServices;
import org.lexgrid.valuedomain.persistence.VDSServices;
import org.lexgrid.valuedomain.persistence.VDServiceHelper;
import org.lexgrid.valuedomain.persistence.VDServices;
import org.lexgrid.valuedomain.persistence.VDXMLread;

import edu.mayo.informatics.lexgrid.convert.emfConversions.XMLWrite;

/**
 * Implementation of Value Domain extension for LexGrid.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSValueDomainServicesImpl implements LexEVSValueDomainServices {

	// Associated service ...
	private LexBIGService lbs_;
	private VDSServices vdss_;
	private VDServices vds_;
	private VDMappingServices mapS_;
	private VDServiceHelper sh_;
	protected MessageDirector md_;
	protected LoadStatus status_;
	private static final String name_ = "LexEVSValueDomainServicesImpl";
	private static final String desc_ = "Implements LexGrid Value Domain services.";
	private static final String provider_ = "Mayo Clinic";
	private static final String version_ = "1.0";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4995582014921448463L;

	public LexEVSValueDomainServicesImpl() {
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
	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#loadValueDomain(org.LexGrid.emf.valueDomains.ValueDomainDefinition)
	 */
	public void loadValueDomain(ValueDomainDefinition vddef, String systemReleaseURI, Mappings mappings)
			throws LBException {
		getLogger().logMethod(new Object[] { vddef });
		try {
			if (vddef != null)
			{
				md_.info("Loading value domain definition : " + vddef.getValueDomainURI());				
				getVDService().insert(vddef, systemReleaseURI, mappings);
			}
		} catch (ObjectAlreadyExistsException e) {
			md_.fatal("Failed while while loading value domain definition", e);
			throw new LBException("Failed while while loading value domain definition", e);
		} catch (InsertException e) {
			md_.fatal("Failed while while loading value domain definition", e);
			throw new LBException("Failed while while loading value domain definition", e);
		} catch (ServiceInitException e) {
			md_.fatal("Failed while while loading value domain definition", e);
			throw new LBException("Failed while while loading value domain definition", e);
		}
		md_.info("Finished loading value domain definition");
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#loadValueDomain(java.io.InputStream, boolean)
	 */
	public void loadValueDomain(InputStream inputStream, boolean failOnAllErrors)
			throws LBException {
		getLogger().logMethod(new Object[] { inputStream });
		VDXMLread vdXML = new VDXMLread(inputStream, md_, failOnAllErrors);

		internalLoadVD(vdXML);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#loadValueDomain(java.lang.String, boolean)
	 */
	public void loadValueDomain(String xmlFileLocation, boolean failOnAllErrors)
			throws LBException {
		getLogger().logMethod(new Object[] { xmlFileLocation });
		VDXMLread vdXML;
		try {
			vdXML = new VDXMLread(getStringFromURI(new URI(xmlFileLocation)), null, md_,failOnAllErrors);
		} catch (URISyntaxException e) {
			throw new LBException("Failed loading XML.", e);
		}
		
		internalLoadVD(vdXML);
	}

	/**
	 * Common method that will be used for loading value domain to database.
	 * @param vdXML
	 * @throws Exception
	 */
	private void internalLoadVD(VDXMLread vdXML) throws LBException {
		getLogger().logMethod(new Object[] { vdXML });
		
		try 
		{
			ValueDomainDefinition[] vdDefs = vdXML.readAllValueDomains();
			SystemRelease systemRelease = vdXML.getSystemRelease();
			
			String releaseURI = null;
			
			if (systemRelease != null)
			{
				releaseURI = systemRelease.getReleaseURI();
				
				md_.info("Loading System Release : " + releaseURI);
				
				SystemReleaseServices releaseService = (SystemReleaseServices) getVDService().getNestedService(SystemReleaseImpl.class);
				releaseService.insert(systemRelease);
				
				md_.info("Finished Loading System Release : " + releaseURI);
			}
			
			md_.info("Loading Value Domain Definitions");
			for (ValueDomainDefinition vddef : vdDefs) {
				loadValueDomain(vddef, releaseURI, vdXML.getVdMappings());			
			}
			md_.info("Finished Loading Value Domain Definitions");
			
			// Mappings have to belong to a specific value domain - they are passed through in the load call above
//			md_.info("Loading Mappings");
//			mapS_ = (VDMappingServices) getVDSService().getNestedService(
//					VDMappingServices.class);
//			mapS_.insert(vdXML.getVdMappings(), -1);
//			md_.info("Finished Loading Mappings");
			
			md_.info("Load Process Complete.");
		} 
		catch (ServiceInitException e)
		{
			throw new LBException("Problem loading valueDomain." + e.getStackTrace());
		} 
		catch (ObjectAlreadyExistsException e) 
		{
			throw new LBException("Problem loading valueDomain." + e.getStackTrace());
		} 
		catch (InsertException e) 
		{
			throw new LBException("Problem loading valueDomain." + e.getStackTrace());
		} 
		catch (Exception e) 
		{
			throw new LBException("Problem loading valueDomain." + e.getStackTrace());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#validate(java.net.URI, int)
	 */
	public void validate(URI uri, int validationLevel) throws LBParameterException{
		VDXMLread vdXML = new VDXMLread(uri.toString(), null, md_, true);
		vdXML.validate(uri, validationLevel);
	}
	   
    /* (non-Javadoc)
     * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#isConceptInDomain(java.lang.String, java.net.URI, java.lang.String)
     */
    public AbsoluteCodingSchemeVersionReference isEntityInDomain(
            String entityCode, URI valueDomainURI, String versionTag)  throws LBException {
        getLogger().logMethod(new Object[] { entityCode, valueDomainURI, versionTag });
        return isEntityInDomain(entityCode, null, valueDomainURI, null, versionTag);
    }
	
	/* (non-Javadoc)
     * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#isEntityInDomain(java.lang.String, java.net.URI, java.net.URI, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String)
     */
    public AbsoluteCodingSchemeVersionReference isEntityInDomain(
            String entityCode, URI entityCodeNamespace, URI valueDomainURI, AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) 
            throws LBException {

        getLogger().logMethod(new Object[] { entityCode, entityCodeNamespace, valueDomainURI, csVersionList, versionTag });
        String entityCodeNamespaceString = entityCodeNamespace != null && !StringUtils.isEmpty(entityCodeNamespace.toString())? entityCodeNamespace.toString() : null;
        
        ValueDomainDefinition vdDef = getServiceHelper().getValueDomain(valueDomainURI);
        
        if (vdDef != null) {
            ResolvedValueDomainCodedNodeSet rvdcns = getServiceHelper().getResolvedCodedNodeSetForValueDomain(vdDef, csVersionList, versionTag);            
            if (rvdcns != null && rvdcns.getCodedNodeSet() != null && rvdcns.getCodingSchemeVersionRefList() != null) {
                Iterator<AbsoluteCodingSchemeVersionReference> csUsedIter = rvdcns.getCodingSchemeVersionRefList().iterateAbsoluteCodingSchemeVersionReference();
                CodedNodeSet resolvedSet = rvdcns.getCodedNodeSet();
                while(csUsedIter.hasNext()) {
                    AbsoluteCodingSchemeVersionReference csUsed = csUsedIter.next();
                    ConceptReference cr = entityCodeNamespaceString == null? 
                            Constructors.createConceptReference(entityCode, csUsed.getCodingSchemeURN()) :
                            Constructors.createConceptReference(entityCode, entityCodeNamespaceString, csUsed.getCodingSchemeURN());
                    if(resolvedSet.isCodeInSet(cr))
                        return csUsed;
                }
            }
        } else {
            md_.fatal("No Value Domain found for URI : " + valueDomainURI);
            throw new LBException("No Value Domain found for URI : " + valueDomainURI);
        }
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#resolveValueDomain(java.net.URI, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public ResolvedValueDomainDefinition resolveValueDomain(
            URI valueDomainURI,AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException{
        getLogger().logMethod(new Object[] { valueDomainURI, csVersionList, versionTag });
        
 
        ValueDomainDefinition vdDef = getServiceHelper().getValueDomain(valueDomainURI);  
        if(vdDef != null) {
            ResolvedValueDomainCodedNodeSet domainNodes = getServiceHelper().getResolvedCodedNodeSetForValueDomain(vdDef, csVersionList, versionTag);
            
            // Assemble the reply
            ResolvedValueDomainDefinition rvddef = new ResolvedValueDomainDefinition();
            try {
                rvddef.setValueDomainURI(new URI(vdDef.getValueDomainURI()));
            } catch (URISyntaxException e) {
                md_.fatal("Value domain URI is not a valid URI : " + vdDef.getValueDomainURI());
                throw new LBException("Value domain URI is not a valid URI : " + vdDef.getValueDomainURI());
            }
            rvddef.setValueDomainName(vdDef.getValueDomainName());
            rvddef.setDefaultCodingScheme(vdDef.getDefaultCodingScheme());
            rvddef.setRepresentsRealmOrContext(vdDef.getRepresentsRealmOrContext());
            rvddef.setSource(vdDef.getSource());
            rvddef.setCodingSchemeVersionRefList(domainNodes.getCodingSchemeVersionRefList());
            if(domainNodes != null && domainNodes.getCodedNodeSet() != null)
                rvddef.setResolvedConceptReferenceIterator(domainNodes.getCodedNodeSet().restrictToStatus(ActiveOption.ACTIVE_ONLY, null).resolve(null, null, null));
            return rvddef;
        } else {
            md_.fatal("No Value Domain found for URI : " + valueDomainURI);
            throw new LBException("No Value Domain found for URI : " + valueDomainURI);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#isSubDomain
     * (java.net.URI, java.net.URI, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.Srting)
     */
    public boolean isSubDomain(URI childValueDomainURI, URI parentValueDomainURI,
            AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException {
        getLogger().logMethod(new Object[] { childValueDomainURI, parentValueDomainURI , csVersionList, versionTag});

        CodedNodeSet childCNS = null;
        CodedNodeSet parentCNS = null;
        
        // populate valueDomainDefinitions for both child and parent value domains
        ValueDomainDefinition childVDDef = getServiceHelper().getValueDomain(childValueDomainURI);
        if(childVDDef == null) {
            md_.fatal("No Value Domain found for child domain URI : " + childValueDomainURI);
            throw new LBException("No Value Domain found for child domain URI : " + childValueDomainURI);
        }
        ValueDomainDefinition parentVDDef = getServiceHelper().getValueDomain(parentValueDomainURI);
        if(parentVDDef == null) {
            md_.fatal("No Value Domain found for parent domain URI : " + parentValueDomainURI);
            throw new LBException("No Value Domain found for parent domain URI : " + parentValueDomainURI);
        }
        
        // Prune the return version list down to what we've got available
        HashMap<String,String> refVersions = getServiceHelper().pruneVersionList(csVersionList);
        
        // Resolve the child value domain and populate CodedNodeSet for this domain and the coding scheme version that was used.
        childCNS = getServiceHelper().getCodedNodeSetForValueDomain(childVDDef, refVersions, versionTag);
        if (childCNS == null) {
            md_.fatal("There was a problem creating CodedNodeSet for child valueDomain : " + childValueDomainURI);
            throw new LBException("There was a problem creating CodedNodeSet for child valueDomain : " + childValueDomainURI);
        }
            
        // Resolve the parent value domain and populate CodedNodeSet for this domain and the coding scheme version that was used.
        parentCNS = getServiceHelper().getCodedNodeSetForValueDomain(parentVDDef, refVersions, versionTag);
        if (parentCNS == null) {
            md_.fatal("There was a problem creating CodedNodeSet for parent valueDomain : " + parentValueDomainURI);
            throw new LBException("There was a problem creating CodedNodeSet for parent valueDomain : " + parentValueDomainURI);
        }
        LocalNameList lnl = new LocalNameList();
        return childCNS.difference(parentCNS.intersect(childCNS)).resolveToList(null, lnl, null, 1).getResolvedConceptReferenceCount() == 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.lexgrid.extension.valuedomain.LexEVSValueDomainServices#
     * getValueDomainDefinition (java.net.URI)
     */
    public ValueDomainDefinition getValueDomainDefinition(URI valueDomainURI) throws LBException {
        getLogger().logMethod(new Object[] { valueDomainURI });
        return getServiceHelper().getValueDomain(valueDomainURI);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#listValueDomains
     * (java.lang.String)
     */
    public URI[] listValueDomains(String valueDomainName) throws LBException {
        getLogger().logMethod(new Object[] { valueDomainName });
        URI[] uris;
        try {
            uris = getServiceHelper().getValueDomainServices().findByValueDomainName(valueDomainName);
        } catch (FindException e) {
            md_.fatal("Failed during list value domain for value domain name : " + valueDomainName, e);
            throw new LBException("Failed during list value domain for value domain name : " + valueDomainName, e);
        }     
        return uris;
    }

	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexgrid.extension.valuedomain.LexEVSValueDomainServices#
	 * getAllValueDomainsWithNoName()
	 */
	public URI[] getAllValueDomainsWithNoName() throws LBException {
		getLogger().logMethod(new Object[]{});
		URI[] uris;
		try {
			uris = getServiceHelper().getValueDomainServices().findByValueDomainName(" ");
		} catch (FindException e) {
			md_.fatal("Failed while finding Value Domain Definition with no names", e);
			throw new LBException("Failed while finding Value Domain Definition with no names", e);
		}
		return uris;
	}
	
	/* (non-Javadoc)
     * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#getValueDomainEntitiesForTerm(java.lang.String, java.lang.String java.net.URI, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.Srting)
     */
    @SuppressWarnings("unchecked")
    public ResolvedValueDomainCodedNodeSet getValueDomainEntitiesForTerm(String term, String matchAlgorithm, URI valueDomainURI,
            AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException {
        getLogger().logMethod(new Object[] { term, matchAlgorithm, valueDomainURI, csVersionList, versionTag });
        
        ValueDomainDefinition vdDef = getServiceHelper().getValueDomain(valueDomainURI);
        if (vdDef != null) {
            ResolvedValueDomainCodedNodeSet domainNodes = getServiceHelper().getResolvedCodedNodeSetForValueDomain(vdDef, csVersionList, versionTag);
            
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
            md_.fatal("Value domain uri : '"+ valueDomainURI + "' not found.");
            throw new LBException("Value domain uri : '"+ valueDomainURI + "' not found.");
        }
    }


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexgrid.extension.valuedomain.LexEVSValueDomainServices#
	 * getCodingSchemesInValueDomain(java.net.URI)
	 */
	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemesInValueDomain(
			URI valueDomainURI) throws LBException {
		getLogger().logMethod(new Object[] { valueDomainURI });
		
		AbsoluteCodingSchemeVersionReferenceList csList = new AbsoluteCodingSchemeVersionReferenceList();
		
		// Get valueDomain definition object for supplied uri.
		ValueDomainDefinition vd = getServiceHelper().getValueDomain(valueDomainURI);
		
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


	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#isDomain(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	public boolean isDomain(String domainId, String codingSchemeName,
			CodingSchemeVersionOrTag csvt) throws LBException {
		getLogger().logMethod(new Object[] { domainId, codingSchemeName, csvt });
		
		LocalNameList lnl = Constructors.createLocalNameList(SQLTableConstants.ENTRY_STATE_TYPE_VALUEDOMAIN);
		
		// Populate CodedNodeSet for supplied domainId and csVersion and restrict to 'valueDomain' type.
		CodedNodeSet cns = getLexBIGService().getNodeSet(codingSchemeName, csvt, lnl);
		cns.restrictToCodes(Constructors.createConceptReferenceList(domainId));
		
		Iterator<ResolvedConceptReference> rcrIter = cns.resolveToList(null, null, null, null, 1).iterateResolvedConceptReference();
		// If there were entities after resolving the codedNodeSet; the supplied domainId is a valueDomain
		if (rcrIter.hasNext()) {
			return true;
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#removeValueDomain(java.net.URI)
	 */
	public void removeValueDomain(URI valueDomainURI) throws LBException, RemoveException {
		try {
			getServiceHelper().getValueDomainServices().remove(valueDomainURI);
		} catch (FindException e) {
			md_.fatal("Failed during removing value domain : " + valueDomainURI, e);
			throw new LBException("Failed during removing value domain : " + valueDomainURI, e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#removeAllValueDomains()
	 */
	public void removeAllValueDomains() throws RemoveException, LBException{
		try {
			URI[] vdURIs = listValueDomains(null);
			
			for (URI vdURI : vdURIs)
			{
				getServiceHelper().getValueDomainServices().remove(vdURI);
			}
		} catch (FindException e) {
			md_.fatal("Failed during removing all value domains", e);
			throw new LBException("Failed during removing all value domains", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.extension.valuedomain.LexEVSValueDomainServices#dropValueDomainTables()
	 */
	public void dropValueDomainTables() throws RemoveException, LBException {
		try {
			getServiceHelper().getValueDomainServices().dropValueDomainTables(false);
		} catch (FindException e) {
			md_.fatal("Failed to drop value domain tables", e);
			throw new LBException("Failed to drop value domain tables", e);
		} catch (SQLException e) {
			md_.fatal("Failed to drop value domain tables", e);
			throw new LBException("Failed to drop value domain tables", e);
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

	public VDServices getVDService() throws ServiceInitException, LBException {
		if (vds_ == null)
			vds_ = getServiceHelper().getValueDomainServices();
		return vds_;
	}

	public VDSServices getVDSService() throws ServiceInitException {
		if (vdss_ == null)
			vdss_ = getServiceHelper().getValueDomainsServices();
		return vdss_;
	}
	
	public VDServiceHelper getServiceHelper(){
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
	
	public String getStringFromURI(URI uri) throws LBParameterException {
		if ("file".equals(uri.getScheme()))

        {
            File temp = new File(uri);
            return temp.getAbsolutePath();
        }
		return uri.toString();
    }
	
	
	/* (non-Javadoc)
	 * @see org.lexgrid.valuedomain.LexEVSValueDomainServices#exportValueDomainDefinition(java.net.URI, java.lang.String, boolean, boolean)
	 */
	public void exportValueDomainDefinition(URI valueDomainURI,
			String xmlFolderLocation, boolean overwrite, boolean failOnAllErrors)
			throws LBException {
		md_.info("Starting to export value domain definition : " + valueDomainURI);
		XMLWrite xmlWrite = new XMLWrite(xmlFolderLocation, overwrite, failOnAllErrors, md_);
		ValueDomainDefinition vdDef = getValueDomainDefinition(valueDomainURI);
		try {
			//xmlWrite.writeValueDomainDefinition(vdDef);
		} catch (Exception e) {
			md_.fatal("Problem exporting value domain definition", e);
			e.printStackTrace();
		}
		md_.info("Completed exporting value domain definition : " + valueDomainURI);
	}
}