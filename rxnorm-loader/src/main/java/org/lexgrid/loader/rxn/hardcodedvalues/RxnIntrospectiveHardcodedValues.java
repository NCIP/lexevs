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
package org.lexgrid.loader.rxn.hardcodedvalues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import org.LexGrid.persistence.dao.LexEvsDao;
//import org.LexGrid.persistence.model.CodingScheme;
//import org.LexGrid.persistence.model.CodingSchemeMultiAttrib;
//import org.LexGrid.persistence.model.CodingSchemeMultiAttribId;
//import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues;

/**
 * The Class RxnIntrospectiveHardcodedValues.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RxnIntrospectiveHardcodedValues extends AbstractIntrospectiveHardcodedValues {

	@Override
	public void loadObjects() {
		// TODO Auto-generated method stub
		
	}
//I took this out of the job id list.  All of the functionality is moved to RxnHardCodedValuesFactory.
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues#loadObjects()
	 */
//	@Override
//	public List<Object> loadObjects() {
//		List<Object> hardcodedValues = new ArrayList<Object>();
//		
//		LexEvsDao lexEvsDao = this.getLexEvsDao();
//		CodingSchemeNameSetter codingSchemeNameSetter = this.getCodingSchemeNameSetter();
//		
//		String codingSchemeName = this.getCodingSchemeNameSetter().getCodingSchemeName();
//		
//		getSupportedAttributeTemplate().addSupportedNamespace(codingSchemeName, codingSchemeName, null, codingSchemeName, codingSchemeName);	
//		
//		CodingScheme codingScheme;
//		try {
//			codingScheme = lexEvsDao.findById(CodingScheme.class, codingSchemeNameSetter.getCodingSchemeName());
//		} catch (Exception e) {
//			throw new RuntimeException("Coding not get the Coding Scheme info to load Hard Coded Values.", e);
//		}
//		
//		CodingSchemeMultiAttrib localName1 = buildLocalNameCodingSchemeMultiAttrib("localName");
//		localName1.getId().setAttributeValue(codingSchemeNameSetter.getCodingSchemeName());
//		hardcodedValues.add(localName1);
//		
//		CodingSchemeMultiAttrib localName2 = buildLocalNameCodingSchemeMultiAttrib("localName");
//		localName2.getId().setAttributeValue(codingScheme.getCodingSchemeUri());
//		hardcodedValues.add(localName2);
//		
//		CodingSchemeMultiAttrib source = buildLocalNameCodingSchemeMultiAttrib("source");
//		source.getId().setAttributeValue("RXN - " + codingScheme.getRepresentsVersion());
//		hardcodedValues.add(source);	
//		
//		
//		
//		return hardcodedValues;	
//	}
//	
//
//	/**
//	 * Builds the local name coding scheme multi attrib.
//	 * 
//	 * @param type the type
//	 * 
//	 * @return the coding scheme multi attrib
//	 */
//	private CodingSchemeMultiAttrib buildLocalNameCodingSchemeMultiAttrib(String type){
//		CodingSchemeMultiAttrib multiAttri = new CodingSchemeMultiAttrib(new CodingSchemeMultiAttribId());
//		multiAttri.getId().setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
//		multiAttri.getId().setTypeName(type);
//		return multiAttri;
//	}

}
