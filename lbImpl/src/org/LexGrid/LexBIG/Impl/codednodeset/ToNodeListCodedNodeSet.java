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
package org.LexGrid.LexBIG.Impl.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Difference;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Intersect;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Union;
import org.LexGrid.LexBIG.Impl.helpers.AdditiveCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.DefaultGraphQueryBuilder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.utility.CodingSchemeReference;

/**
 * Implementation of the CodedNodeSet Interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ToNodeListCodedNodeSet extends CodedNodeSetImpl {
    
    private static final long serialVersionUID = -5959522938971242708L;
    
    private CodeHolder toNodeListCodes = new DefaultCodeHolder();
    
    public ToNodeListCodedNodeSet() {
        super();
    }
    
    public ToNodeListCodedNodeSet(String uri, String version, ConceptReferenceList codeList) throws LBInvocationException, LBParameterException, LBResourceUnavailableException {
        super(uri, Constructors.createCodingSchemeVersionOrTagFromVersion(version), null, null);
        
        AdditiveCodeHolder codeHolder = new DefaultCodeHolder();
        
        for(ConceptReference ref : codeList.getConceptReference()) {
            codeHolder.add(new CodeToReturn(uri, version, ref));
        }
        
        CodingSchemeReference ref = new CodingSchemeReference();
        ref.setCodingSchemeURN(uri);
        ref.setCodingSchemeVersion(version);
        
        this.setToNodeListCodes(codeHolder);  
        
        if(codeList.getConceptReferenceCount() == 0) {
            codeList.addConceptReference(DefaultGraphQueryBuilder.INVALID_MATCH_CONCEPT_REFERENCE);
        }
        
        this.restrictToCodes(codeList);
    }
    
    public ToNodeListCodedNodeSet(
            String codingScheme, 
            CodingSchemeVersionOrTag tagOrVersion, 
            Boolean activeOnly, 
            LocalNameList types) throws LBException {
        super(codingScheme,tagOrVersion,activeOnly, types);
    }
    
    @Override
    public CodedNodeSet restrictToMatchingDesignations(String matchText, boolean preferredOnly, String matchAlgorithm,
            String language) throws LBInvocationException, LBParameterException {
        this.setToNodeListCodes(new DefaultCodeHolder()); 
        return super.restrictToMatchingDesignations(matchText, preferredOnly, matchAlgorithm, language);
    }

    @Override
    public CodedNodeSet restrictToMatchingDesignations(String matchText, SearchDesignationOption option,
            String matchAlgorithm, String language) throws LBInvocationException, LBParameterException {
        this.setToNodeListCodes(new DefaultCodeHolder());
        return super.restrictToMatchingDesignations(matchText, option, matchAlgorithm, language);
    }

    @Override
    public CodedNodeSet restrictToMatchingProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList, String matchText,
            String matchAlgorithm, String language) throws LBInvocationException, LBParameterException {
        this.setToNodeListCodes(new DefaultCodeHolder());
        return super.restrictToMatchingProperties(propertyList, propertyTypes, sourceList, contextList, qualifierList, matchText, matchAlgorithm, language);
    }

    @Override
    public CodedNodeSet restrictToMatchingProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            String matchText, String matchAlgorithm, String language) throws LBInvocationException,
            LBParameterException {
        this.setToNodeListCodes(new DefaultCodeHolder());    
        return super.restrictToMatchingProperties(propertyList, propertyTypes, matchText, matchAlgorithm, language);
    }

    @Override
    public CodedNodeSet restrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
            LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList)
            throws LBInvocationException, LBParameterException {
        this.setToNodeListCodes(new DefaultCodeHolder());
        return super.restrictToProperties(propertyList, propertyTypes, sourceList, contextList, qualifierList);
    }

    @Override
    public CodedNodeSet restrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes)
            throws LBInvocationException, LBParameterException {
        this.setToNodeListCodes(new DefaultCodeHolder());;    
        return super.restrictToProperties(propertyList, propertyTypes);
    }

    @Override
    public CodedNodeSet restrictToStatus(ActiveOption activeOption, String[] conceptStatus)
            throws LBInvocationException, LBParameterException {
        this.setToNodeListCodes(new DefaultCodeHolder());   
        return super.restrictToStatus(activeOption, conceptStatus);
    }

    public CodedNodeSet union(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl ) {
            CodedNodeSetImpl unioned = (CodedNodeSetImpl)codes;
            
            CodeHolder codeHolder = new DefaultCodeHolder();
            codeHolder.union(this.getCodeHolder());
            codeHolder.union(unioned.getCodeHolder());
            
            CodedNodeSetImpl cns = new CodedNodeSetImpl(codeHolder, null, null);
            cns.getCodingSchemeReferences().addAll(this.getCodingSchemeReferences());
            cns.getCodingSchemeReferences().addAll(unioned.getCodingSchemeReferences());
            return cns;
        } else {
           return super.union(codes);
        }
    }
    
    public CodedNodeSet intersect(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl ) {
            CodedNodeSetImpl intersected = (CodedNodeSetImpl)codes;
            
            CodeHolder codeHolder = new DefaultCodeHolder();
            codeHolder.union(this.getCodeHolder());
            codeHolder.intersect(intersected.getCodeHolder());
            
            CodedNodeSetImpl cns = new CodedNodeSetImpl(codeHolder, null, null);
            cns.getCodingSchemeReferences().addAll(this.getCodingSchemeReferences());
            cns.getCodingSchemeReferences().addAll(intersected.getCodingSchemeReferences());
            
            return cns;
        } else {
           return super.intersect(codes);
        }
    }
    
    public CodedNodeSet difference(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl ) {
            CodedNodeSetImpl diffed = (CodedNodeSetImpl)codes;
            
            CodeHolder codeHolder = new DefaultCodeHolder();
            codeHolder.union(this.getCodeHolder());
            codeHolder.difference(diffed.getCodeHolder());
            
            CodedNodeSetImpl cns = new CodedNodeSetImpl(codeHolder, null, null);
            cns.getCodingSchemeReferences().addAll(this.getCodingSchemeReferences());
            cns.getCodingSchemeReferences().addAll(diffed.getCodingSchemeReferences());
            
            return cns;
        } else {
           return super.difference(codes);
        }
    }

    @Override
    public CodedNodeSet restrictToCodes(ConceptReferenceList codeList) throws LBInvocationException,
            LBParameterException {
        AdditiveCodeHolder newCodeHolder = new DefaultCodeHolder();
        
        for(ConceptReference ref : codeList.getConceptReference()) {
            CodeToReturn foundCode = this.findCodeToReturn(ref);
            if(foundCode != null) {
                newCodeHolder.add(foundCode);
            }
        }
        
        this.setToNodeListCodes(newCodeHolder);
        return super.restrictToCodes(codeList);
    }
    
    private CodeToReturn findCodeToReturn(ConceptReference ref) {
        for(CodeToReturn codeToReturn : this.getToNodeListCodes().getAllCodes()) {
            if(codeToReturn.getCode().equals(ref.getCode())) {
                if(StringUtils.isNotBlank(ref.getCodeNamespace())) {
                    if(codeToReturn.getNamespace().equals(ref.getCodeNamespace())) {
                        return codeToReturn;
                    }
                } else {
                    return codeToReturn;
                }
            }
        }
        
        return null;
    }

    @Override
    protected void doUnion(String internalCodeSystemName, String internalVersionString, Union union) throws LBException {
       //
    }

    @Override
    protected void doDifference(String internalCodeSystemName, String internalVersionString, Difference difference)
            throws LBException {
        //
    }

    @Override
    protected void doIntersect(String internalCodeSystemName, String internalVersionString, Intersect intersect)
            throws LBException {
        //
    }

    @Override
    protected void toBruteForceMode(String internalCodeSystemName, String internalVersionString)
            throws LBInvocationException, LBParameterException {
        super.toBruteForceMode(internalCodeSystemName, internalVersionString);

        if(this.getToNodeListCodes() == null || this.getToNodeListCodes().getNumberOfCodes() == 0) {
            this.codesToInclude_.union(getToNodeListCodes());
        } else {
            this.codesToInclude_.union(removeInactiveCodes(this.getToNodeListCodes()));
        }
    } 
    
    private CodeHolder removeInactiveCodes(CodeHolder codeHolder) {
        AdditiveCodeHolder returnCodeHolder = new DefaultCodeHolder();
        
        for(CodeToReturn code : codeHolder.getAllCodes()) {
            String uri = code.getUri();
            String version = code.getVersion();

            Entity entity = null;

            if(StringUtils.isNotBlank(code.getUri()) && StringUtils.isNotBlank(code.getVersion())){
                try {
                    entity = LexEvsServiceLocator.getInstance().
                        getDatabaseServiceManager().
                        getEntityService().
                        getEntity(uri, version, code.getCode(), code.getNamespace());
                } catch(Exception e) {
                    LoggerFactory.getLogger().warn("Active Status cannot be determined for: " + code.getCode() + ", " + code.getNamespace());
                }
            }

            if(entity != null) {
                if(entity.getIsActive() != false) {
                    returnCodeHolder.add(code);
                }
            } else {
                returnCodeHolder.add(code);
            }
        }
        return returnCodeHolder;
    }
    
    public void setToNodeListCodes(CodeHolder toNodeListCodes) {
        this.toNodeListCodes = toNodeListCodes;
    }

    public CodeHolder getToNodeListCodes() {
        return toNodeListCodes;
    }
}