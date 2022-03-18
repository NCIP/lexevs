
package org.lexevs.dao.database.access.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;

/**
 * The Interface CodedNodeSetDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodedNodeSetDao extends LexGridSchemaVersionAwareDao {
	
	  /**
  	 * Builds the coded entry.
  	 * 
  	 * @param codingSchemeName the coding scheme name
  	 * @param version the version
  	 * @param code the code
  	 * @param namespace the namespace
  	 * @param restrictToProperties the restrict to properties
  	 * @param restrictToPropertyTypes the restrict to property types
  	 * 
  	 * @return the entity
  	 */
  	public Entity buildCodedEntry(String codingSchemeName, String version,
	            String code, String namespace, LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes);

	  /**
  	 * Gets the coding scheme copyright.
  	 * 
  	 * @param codingSchemeName the coding scheme name
  	 * @param version the version
  	 * 
  	 * @return the coding scheme copyright
  	 */
  	public String getCodingSchemeCopyright(String codingSchemeName, String version);
	  
	  /**
  	 * Gets the coding scheme.
  	 * 
  	 * @param codingSchemeName the coding scheme name
  	 * @param version the version
  	 * 
  	 * @return the coding scheme
  	 */
  	public CodingScheme getCodingScheme(String codingSchemeName, String version);
}