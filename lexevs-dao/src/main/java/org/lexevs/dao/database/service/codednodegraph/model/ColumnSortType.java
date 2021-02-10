
package org.lexevs.dao.database.service.codednodegraph.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The Enum ColumnSortType.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public enum ColumnSortType {

	CODE("code", "targetEntityCode", "sourceEntityCode", "entityCode", "entityCode", null),
	
	SOURCE_CODE("SOURCE_CODE", null,null,null,null,"eate.sourceEntityCode"),
	
	TARGET_CODE("TARGET_CODE", null,null,null,null,"eate.targetEntityCode"),
	
	SOURCE_ENTITY_DESCRIPTION("SOURCE_ENTITY_DESCRIPTION", null,null,null,null,"sourceEntity.description"),
	
	TARGET_ENTITY_DESCRIPTION("TARGET_ENTITY_DESCRIPTION", null,null,null,null,"targetEntity.description"),
	
	/** The QUALIFIER. */
	QUALIFIER("QUALIFIER", null,null,null,null,"qualifierValue"),
	
	RELATIONSHIP("RELATIONSHIP", null,null,null,null,"ap.associationName");
	
	/** The name. */
	private final String name;
	
	/** The subject column. */
	private final String subjectColumn;
	
	/** The object column. */
	private final String objectColumn;
	
	/** The root column. */
	private final String rootColumn;
	
	/** The tail column. */
	private final String tailColumn;
	
	/** The mapping column. */
	private final String mappingColumn;
	
	/** The Constant nameMap. */
	private static final Map<String,ColumnSortType> sortMap;

	static{
		sortMap = new HashMap<String, ColumnSortType>();
		for(ColumnSortType type: values()){
			sortMap.put(type.name, type);
		}
	}
	
	/**
	 * Gets the column sort type for name.
	 * 
	 * @param name the name
	 * 
	 * @return the column sort type for name
	 */
	public static ColumnSortType getColumnSortTypeForName(String name) {
		return sortMap.get(name);
	}
	
	/**
	 * Instantiates a new column sort type.
	 * 
	 * @param name the name
	 * @param subjectColumn the subject column
	 * @param objectColumn the object column
	 * @param rootColumn the root column
	 * @param tailColumn the tail column
	 * @param mappingColumn the mapping column
	 */
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

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the subject column.
	 * 
	 * @return the subject column
	 */
	public String getSubjectColumn() {
		return subjectColumn;
	}

	/**
	 * Gets the object column.
	 * 
	 * @return the object column
	 */
	public String getObjectColumn() {
		return objectColumn;
	}

	/**
	 * Gets the root column.
	 * 
	 * @return the root column
	 */
	public String getRootColumn() {
		return rootColumn;
	}

	/**
	 * Gets the tail column.
	 * 
	 * @return the tail column
	 */
	public String getTailColumn() {
		return tailColumn;
	}

	/**
	 * Gets the mapping column.
	 * 
	 * @return the mapping column
	 */
	protected String getMappingColumn() {
		return mappingColumn;
	}
}