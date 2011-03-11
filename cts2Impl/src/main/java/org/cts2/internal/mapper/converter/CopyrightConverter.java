/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.mapper.converter;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.URIMap;
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.core.DataTypeReference;
import org.cts2.core.OpaqueData;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class DefinitionOperatorToSetOperatorConverter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CopyrightConverter extends DozerConverter<CodingScheme,CodeSystemVersion> {

	public CopyrightConverter() {
		super(CodingScheme.class, CodeSystemVersion.class);
	}

	@Override
	public CodingScheme convertFrom(CodeSystemVersion csv, CodingScheme cs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSystemVersion convertTo(CodingScheme cs, CodeSystemVersion csv) {
		Text copyright = cs.getCopyright();
		
		if(copyright == null){
			return csv;
		}
		
		if(csv == null){
			csv = new CodeSystemVersion();
		}
		
		OpaqueData od = new OpaqueData();
		od.setValue(copyright.getContent());
		csv.setRights(od);

		String datatype = cs.getCopyright().getDataType();
		
		if(datatype != null){
			
			try {
				URIMap uriMap = 
					DaoUtility.getURIMap(cs, SupportedDataType.class, datatype);
				
				DataTypeReference dataTypeRef = new DataTypeReference();
				dataTypeRef.setContent(uriMap.getLocalId());
				dataTypeRef.setHref(uriMap.getUri());
				
				csv.getRights().setFormat(dataTypeRef);
			
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}

		csv.setRights(od);
		
		return csv;
	}
}
