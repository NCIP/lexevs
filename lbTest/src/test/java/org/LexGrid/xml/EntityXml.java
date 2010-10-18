/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.LexGrid.xml;

public class EntityXml {
	private String entity = "<lgCon:entity" +
			"			entityCode=\"tobereplacedcode\" entityCodeNamespace=\"colors\" isAnonymous=\"false\"" +
			"			isDefined=\"true\">" +
			"			<lgCommon:entityDescription>Holder of colors</lgCommon:entityDescription>" +
			"			<lgCon:entityType>concept</lgCon:entityType>" +
			"			<lgCon:presentation propertyName=\"textPresentation\"" +
			"				propertyType=\"presentation\" isPreferred=\"true\">	" +
			"			<lgCommon:value>Holder of colors</lgCommon:value>" +
			"			</lgCon:presentation>" +
			"		</lgCon:entity>";
	public EntityXml(String code) {
		entity = entity.replaceFirst("tobereplacedcode", code);
	}
	public String toString() {
		return entity;
	}

}