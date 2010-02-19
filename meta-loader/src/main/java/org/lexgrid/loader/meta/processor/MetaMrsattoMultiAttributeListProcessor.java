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
package org.lexgrid.loader.meta.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.meta.processor.support.MetaMrSatMultiAttributeResolver;
import org.lexgrid.loader.processor.CodingSchemeIdAwareProcessor;
import org.lexgrid.loader.processor.support.MultiAttribResolver;
import org.lexgrid.loader.rrf.model.Mrsat;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 */
public class MetaMrsattoMultiAttributeListProcessor extends CodingSchemeIdAwareProcessor implements
		ItemProcessor<Mrsat, List<EntityPropertyMultiAttrib>> {

	/** Resolver for the source element from MRSAT */
	private MultiAttribResolver<Mrsat> multiAttribSourceResolver;
	/** Resolver for the METAUI element from MRSAT */
	private MultiAttribResolver<Mrsat> multiAttribAuiResolver;
	/** Resolver for the STYPE element from MRSAT */
	private MultiAttribResolver<Mrsat> multiAttribStypeResolver;
	/** Resolver for the SUPPRESS element from MRSAT */
	private MultiAttribResolver<Mrsat> multiAttribSuppressResolver;
	/** The supported attribute template. */
	private SupportedAttributeTemplate supportedAttributeTemplate;
	/** The iso map. */
	private Map<String,String> isoMap;
	/** resolver to process a MRSAT row */
	private MetaMrSatMultiAttributeResolver<Mrsat> resolver;
	/**
	 * @return source property qualifier from MRSAT
	 */
	public MultiAttribResolver<Mrsat> getMultiAttribSourceResolver() {
		return multiAttribSourceResolver;
	}

	/**
	 * @param multiAttribSourceResolver sets source resolver
	 */
	public void setMultiAttribSourceResolver(
			MultiAttribResolver<Mrsat> multiAttribSourceResolver) {
		this.multiAttribSourceResolver = multiAttribSourceResolver;
	}

	/**
	 * @return METAUI property qualifier from MRSAT
	 */
	public MultiAttribResolver<Mrsat> getMultiAttribAuiResolver() {
		return multiAttribAuiResolver;
	}

	/**
	 * @param multiAttribAuiResolver
	 */
	public void setMultiAttribAuiResolver(
			MultiAttribResolver<Mrsat> multiAttribAuiResolver) {
		this.multiAttribAuiResolver = multiAttribAuiResolver;
	}

	/**
	 * @return STYPE property qualifier from MRSAT
	 */
	public MultiAttribResolver<Mrsat> getMultiAttribStypeResolver() {
		return multiAttribStypeResolver;
	}

	/**
	 * @param multiAttribStypeResolver
	 */
	public void setMultiAttribStypeResolver(
			MultiAttribResolver<Mrsat> multiAttribStypeResolver) {
		this.multiAttribStypeResolver = multiAttribStypeResolver;
	}

	/**
	 * @return
	 */
	public MultiAttribResolver<Mrsat> getMultiAttribSuppressResolver() {
		return multiAttribSuppressResolver;
	}

	/**
	 * @param multiAttribSuppressResolver
	 */
	public void setMultiAttribSuppressResolver(
			MultiAttribResolver<Mrsat> multiAttribSuppressResolver) {
		this.multiAttribSuppressResolver = multiAttribSuppressResolver;
	}

	/**
	 * @return
	 */
	public MetaMrSatMultiAttributeResolver<Mrsat> getResolver() {
		return resolver;
	}

	/**
	 * @param resolver
	 */
	public void setResolver(MetaMrSatMultiAttributeResolver<Mrsat> resolver) {
		this.resolver = resolver;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<EntityPropertyMultiAttrib> process(Mrsat item) throws Exception {
		List<EntityPropertyMultiAttrib> mrsatPropertyQualifiers = new ArrayList<EntityPropertyMultiAttrib>();
		EntityPropertyMultiAttrib multiAttribSource = resolver.getEntitPropMultiAttrib(multiAttribSourceResolver, item);
		EntityPropertyMultiAttrib multiAttribAui = resolver.getEntitPropMultiAttrib(multiAttribAuiResolver, item);
		EntityPropertyMultiAttrib multiAttribStype = resolver.getEntitPropMultiAttrib(multiAttribStypeResolver, item);
		
		if(StringUtils.isNotBlank(multiAttribSource.getId().getVal1())){
			mrsatPropertyQualifiers.add(multiAttribSource);
		}
		
		if(StringUtils.isNotBlank(multiAttribAui.getId().getVal1())){
			mrsatPropertyQualifiers.add(multiAttribAui);
		}
		
		if(StringUtils.isNotBlank(multiAttribStype.getId().getVal1())){
			mrsatPropertyQualifiers.add(multiAttribStype);
		}
		
		EntityPropertyMultiAttrib multiAttribSupress = resolver.getEntitPropMultiAttrib(multiAttribSuppressResolver, item);
		if(multiAttribSupress != null){
			if(StringUtils.isNotBlank(multiAttribSupress.getId().getVal1())){
				mrsatPropertyQualifiers.add(multiAttribSupress);
			}
		}
		
		setSupportedPropertyQualifier(mrsatPropertyQualifiers);
		return mrsatPropertyQualifiers;
	}
	
	/**
	 * @param propertyQualifiers
	 * Checking a cached list to see if the property qualifier has been loaded yet.
	 */
	private void setSupportedPropertyQualifier(List<EntityPropertyMultiAttrib> propertyQualifiers){
		
		for(EntityPropertyMultiAttrib pq : propertyQualifiers){
			if(pq.getId().getTypeName().equals(SQLTableConstants.TBLCOLVAL_SOURCE)){
				supportedAttributeTemplate.addSupportedSource(
						this.getCodingSchemeIdSetter().getCodingSchemeName(), 
						pq.getId().getAttributeValue(), 
						isoMap.get(pq.getId().getAttributeValue()),
						pq.getId().getAttributeValue(),
						null);
			} else {
				supportedAttributeTemplate.addSupportedPropertyQualifier(
						this.getCodingSchemeIdSetter().getCodingSchemeName(), 
						pq.getId().getAttributeValue(), 
						null, 
						pq.getId().getAttributeValue());
			}
		}
	}
	/**
	 * Gets the supported attribute template.
	 * 
	 * @return the supported attribute template
	 */
	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}

	/**
	 * Sets the supported attribute template.
	 * 
	 * @param supportedAttributeTemplate the new supported attribute template
	 */
	public void setSupportedAttributeTemplate(
			SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}
	
	/**
	 * Gets the iso map.
	 * 
	 * @return the iso map
	 */
	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	/**
	 * Sets the iso map.
	 * 
	 * @param isoMap the iso map
	 */
	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}
}
