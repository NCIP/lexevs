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
package org.lexgrid.loader.processor;

import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.LexGrid.persistence.model.EntityPropertyMultiAttribId;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.support.MultiAttribResolver;

/**
 * The Class AbstractEntityPropertyMultiAttribProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractEntityPropertyMultiAttribProcessor<I,O> extends AbstractSupportedAttributeRegisteringProcessor<I,O>{
	
	/**
	 * Builds the property qualifier.
	 * 
	 * @param item the item
	 * 
	 * @return the entity property multi attrib
	 */
	protected EntityPropertyMultiAttrib buildPropertyQualifier(MultiAttribResolver<I> multiAttribResolver, I item){
		EntityPropertyMultiAttrib qualifier = new EntityPropertyMultiAttrib();
		EntityPropertyMultiAttribId qualifierId = new EntityPropertyMultiAttribId();
		
		qualifierId.setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
		qualifierId.setEntityCodeNamespace(getCodingSchemeNameSetter().getCodingSchemeName());
		qualifierId.setEntityCode(multiAttribResolver.getEntityCode(item));	
		qualifierId.setTypeName(multiAttribResolver.getTypeName());
		qualifierId.setPropertyId(multiAttribResolver.getId(item));
		qualifierId.setAttributeValue(multiAttribResolver.getAttributeValue(item));
		qualifierId.setVal1(multiAttribResolver.getVal1(item));
		
		qualifier.setId(qualifierId);
		return qualifier;
	}
	
	protected void registerEntityPropertyMultiAttrib(SupportedAttributeTemplate template,
			EntityPropertyMultiAttrib qualifier) {
		if(qualifier.getId().getTypeName().equals(SQLTableConstants.TBLCOLVAL_SOURCE)){
			template.addSupportedSource(
					this.getCodingSchemeNameSetter().getCodingSchemeName(), 
					qualifier.getId().getAttributeValue(), 
					null,
					qualifier.getId().getAttributeValue(),
					null);
		} else {
			template.addSupportedPropertyQualifier(
					this.getCodingSchemeNameSetter().getCodingSchemeName(), 
					qualifier.getId().getAttributeValue(), 
					null, 
					qualifier.getId().getAttributeValue());
		}
	}
}
