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
package org.lexgrid.loader.data;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;

import org.LexGrid.commonTypes.Property;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.Unmarshaller;

/**
 * The Class DataUtils.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataUtils {

	public static <T extends Property> T deepCloneProperty(T property) throws Exception {
		StringWriter writer = new StringWriter();
		property.marshal(writer);
		writer.flush();
		
		String stringProp = writer.toString();
		
		StringReader reader = new StringReader(stringProp);
		
		return  (T) Unmarshaller.unmarshal(property.getClass(), reader);
	}

	
	/**
	 * Makes a deep copy of an Object -- assumes the Object to be copied has a Constructor
	 * that accepts a String
	 * 
	 * Example: String stringCopy = new String(String originalString);.
	 * 
	 * @param value the value
	 * 
	 * @return the T
	 * 
	 * @throws Exception the exception
	 */
	public static <T> T deepCopy(T value) throws Exception {
		if(value == null){
			return null;
		}
		Constructor constructor = value.getClass().getConstructor(String.class);
		return (T)constructor.newInstance(value.toString());	
	}
	
	public static String adjustNonNullValue(String value){
		if(StringUtils.isEmpty(value)){
			return " ";
		} else {
			return value;
		}
	}
}
