package org.cts2.profile.query;

import java.util.List;

import org.cts2.core.EntityReference;
import org.cts2.service.core.QueryControl;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;
import org.cts2.valueset.ValueSetDefinitionDirectory;
import org.cts2.valueset.ValueSetDefinitionList;
import org.cts2.service.core.ReadContext;

/**
 * Interface for CTS2 ValueSetDefinitionQuery Service.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface ValueSetDefinitionQueryService extends BaseQueryService<ValueSetDefinitionDirectoryURI> {
	
	/**
	 * Retrieve the value set definitions available at the specified location on the service.
	 * 
	 * @param directory
	 * @param queryControl
	 * @param context
	 */
	public ValueSetDefinitionDirectory resolve(ValueSetDefinitionDirectoryURI valueSetDefinitionDirectoryURI, QueryControl queryControl, ReadContext context);

	/**
	 * Retrieve a listing of value set definitions available at the specified location
	 * on the service.
	 * 
	 * @param directory
	 * @param queryControl
	 * @param context
	 */
	public ValueSetDefinitionList resolveAsList(ValueSetDefinitionDirectoryURI valueSetDefinitionDirectoryURI, QueryControl queryControl, ReadContext context);

	/**
	 * Determine which value set definitions on the service contain the specified entities
	 * (concepts).
	 * 
	 * @param valueSetDirectory
	 * @param entityList
	 */
	public ValueSetDefinitionDirectoryURI restrictToEntities(ValueSetDefinitionDirectoryURI valueSetDefinitionDirectoryURI, List<EntityReference> entityList);
	
}
