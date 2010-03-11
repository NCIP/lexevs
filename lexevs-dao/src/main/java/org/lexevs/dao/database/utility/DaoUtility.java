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
package org.lexevs.dao.database.utility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.compass.core.config.CompassEnvironment.Mapping;

/**
 * The Class DaoUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DaoUtility {

	/**
	 * Creates the list.
	 * 
	 * @param itemClazz the item clazz
	 * @param items the items
	 * 
	 * @return the list< t>
	 */
	public static <T> List<T> createList(Class<T> itemClazz, T... items){
		List<T> returnList = new ArrayList<T>();
		for(T item : items) {
			returnList.add(item);
		}
		return returnList;
	}
	
	/**
	 * Creates the text.
	 * 
	 * @param content the content
	 * 
	 * @return the text
	 */
	public static Text createText(String content){
		return createText(content, null);
	}
	
	/**
	 * Creates the text.
	 * 
	 * @param content the content
	 * @param format the format
	 * 
	 * @return the text
	 */
	public static Text createText(String content, String format){
		Text text = new Text();
		text.setContent(content);
		text.setDataType(format);
		return text;
	}
	
	/**
	 * Creates the absolute coding scheme version reference.
	 * 
	 * @param urn the urn
	 * @param version the version
	 * 
	 * @return the absolute coding scheme version reference
	 */
	public static AbsoluteCodingSchemeVersionReference createAbsoluteCodingSchemeVersionReference(String urn,
			String version) {
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(urn);
		acsvr.setCodingSchemeVersion(version);
		return acsvr;
	}
	
	/**
	 * Insert into mappings.
	 * 
	 * @param mappings the mappings
	 * @param uriMap the uri map
	 */
	public static void insertIntoMappings(Mappings mappings, URIMap uriMap) {
		String addPrefix = "add";
		
		Class<? extends URIMap> uriMapClass = uriMap.getClass();
		String name = uriMapClass.getSimpleName();
		
		try {
			Method method = mappings.getClass().getMethod(addPrefix + name, uriMapClass);
			method.invoke(mappings, uriMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	//Ignore this -- just some helpers for generating Ibatis Mapping code. Will go away...
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		for(Method method : Mappings.class.getMethods()) {
			if(method.getName().startsWith("addSupported")) {
				if(method.getParameterTypes().length == 1) {
					/*
					System.out.println("<subMap value=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("Supported", "") + "\" resultMap=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "Result\" />");
					*/
					/*
					System.out.println("<resultMap id=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "Result\" class=\"" + 
							method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "\" extends=\"uriMapResult\">"
							+ "\n" + "</resultMap>"		
					);
					*/
					/*
					System.out.println("<typeAlias alias=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s")
					+ 	"\" type=\"" +	method.getParameterTypes()[0].getName() +"\"/>"
					);
					*/
					
					System.out.println("if(clazz == " + method.getParameterTypes()[0].getSimpleName() + ".class){ \n " +
									   "   return SQLTableConstants.TBLCOLVAL_SUPPTAG_" + method.getParameterTypes()[0].getSimpleName().replaceFirst("Supported", "").toUpperCase() + "; \n}");
				}
			}
		}
	}
}