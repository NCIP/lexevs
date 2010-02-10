package org.lexevs.dao.database.access.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;

public interface CodedNodeSetDao extends LexGridSchemaVersionAwareDao {
	
	  public Entity buildCodedEntry(String codingSchemeName, String version,
	            String code, String namespace, LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes);

	  public String getCodingSchemeCopyright(String codingSchemeName, String version);
	  
	  public CodingScheme getCodingScheme(String codingSchemeName, String version);
}
