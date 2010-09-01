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
package org.lexevs.dao.database.service.codednodegraph.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public enum ColumnSortType {

	CODE("code", "targetEntityCode", "sourceEntityCode", "entityCode", "entityCode", null),
	SOURCE_CODE("SOURCE_CODE", null,null,null,null,"eate.sourceEntityCode"),
	TARGET_CODE("TARGET_CODE", null,null,null,null,"eate.targetEntityCode"),
	SOURCE_ENTITY_DESCRIPTION("SOURCE_ENTITY_DESCRIPTION", null,null,null,null,"sourceEntity.description"),
	QUALIFIER("QUALIFIER", null,null,null,null,"quals.qualifierValue");
	
	private final String name;
	private final String subjectColumn;
	private final String objectColumn;
	private final String rootColumn;
	private final String tailColumn;
	private final String mappingColumn;
	
	/** The Constant nameMap. */
	private static final Map<String,ColumnSortType> sortMap;

	static{
		sortMap = new HashMap<String, ColumnSortType>();
		for(ColumnSortType type: values()){
			sortMap.put(type.name, type);
		}
	}
	
	public static ColumnSortType getColumnSortTypeForName(String name) {
		return sortMap.get(name);
	}
	
	private ColumnSortType(
			String name, 
			String subjectColumn,
			String objectColumn,
			String rootColumn,
			String tailColumn,
			String mappingColumn) {
		this.name = name;
		this.subjectColumn = subjectColumn;
		this.objectColumn = objectColumn;
		this.rootColumn = rootColumn;
		this.tailColumn = tailColumn;
		this.mappingColumn = mappingColumn;
	}

	public String getName() {
		return name;
	}

	public String getSubjectColumn() {
		return subjectColumn;
	}

	public String getObjectColumn() {
		return objectColumn;
	}

	public String getRootColumn() {
		return rootColumn;
	}

	public String getTailColumn() {
		return tailColumn;
	}

	protected String getMappingColumn() {
		return mappingColumn;
	}
}
