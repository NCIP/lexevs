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
package org.lexgrid.loader.umls.hardcodedvalues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.CodingScheme;
import org.LexGrid.persistence.model.CodingSchemeMultiAttrib;
import org.LexGrid.persistence.model.CodingSchemeMultiAttribId;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues;

/**
 * The Class UmlsIntrospectiveHardcodedValues.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsIntrospectiveHardcodedValues extends AbstractIntrospectiveHardcodedValues {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues#loadObjects()
	 */
	@Override
	public List<Object> loadObjects() {
		List<Object> hardcodedValues = new ArrayList<Object>();
		
		LexEvsDao lexEvsDao = this.getLexEvsDao();
		CodingSchemeIdSetter codingSchemeIdSetter = this.getCodingSchemeNameSetter();
		
		String codingSchemeName = this.getCodingSchemeNameSetter().getCodingSchemeId();
		
		getSupportedAttributeTemplate().addSupportedNamespace(codingSchemeName, codingSchemeName, null, codingSchemeName, codingSchemeName);	
		
		CodingScheme codingScheme;
		try {
			codingScheme = lexEvsDao.findById(CodingScheme.class, codingSchemeIdSetter.getCodingSchemeId());
		} catch (Exception e) {
			throw new RuntimeException("Coding not get the Coding Scheme info to load Hard Coded Values.", e);
		}
		
		CodingSchemeMultiAttrib localName1 = buildLocalNameCodingSchemeMultiAttrib("localName");
		localName1.getId().setAttributeValue(codingSchemeIdSetter.getCodingSchemeId());
		hardcodedValues.add(localName1);
		
		CodingSchemeMultiAttrib localName2 = buildLocalNameCodingSchemeMultiAttrib("localName");
		localName2.getId().setAttributeValue(codingScheme.getCodingSchemeUri());
		hardcodedValues.add(localName2);
		
		CodingSchemeMultiAttrib source = buildLocalNameCodingSchemeMultiAttrib("source");
		source.getId().setAttributeValue("UMLS - " + codingScheme.getRepresentsVersion());
		hardcodedValues.add(source);	
		
		
		
		return hardcodedValues;	
	}
	

	/**
	 * Builds the local name coding scheme multi attrib.
	 * 
	 * @param type the type
	 * 
	 * @return the coding scheme multi attrib
	 */
	private CodingSchemeMultiAttrib buildLocalNameCodingSchemeMultiAttrib(String type){
		CodingSchemeMultiAttrib multiAttri = new CodingSchemeMultiAttrib(new CodingSchemeMultiAttribId());
		multiAttri.getId().setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeId());
		multiAttri.getId().setTypeName(type);
		return multiAttri;
	}

}
