package org.cts2.profile.read;

import java.net.URI;

import org.cts2.core.NameOrURI;
import org.cts2.core.NameOrURIList;
import org.cts2.core.VersionTagReference;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.valueset.ResolvedValueSetDirectory;
import org.cts2.valueset.ValueSetDefinition;

/**
 * Interface for CTS2 ValueSetDefinitionRead Service.
 * 
 *	@author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface ValueSetDefinitionReadService extends BaseReadService<ValueSetDefinition>{

	/**
	 * Determine if the specified value set definition exists on the service.
	 * 
	 * @param valueSetDefinitionURI
	 * @param context
	 */
	public Boolean exists(URI valueSetDefinitionURI, ReadContext context);

	/**
	 * Determine if the value set definition for the specified value set exists on the
	 * service.
	 * 
	 * @param valueSet
	 * @param tag
	 * @param context
	 */
	public Boolean existsDefinitionForValueSet(NameOrURI valueSet, NameOrURI tag, ReadContext context);

	/**
	 * Retrieve the value set definition for the specified value set.
	 * 
	 * @param valueSet
	 * @param tag
	 * @param queryControl
	 * @param context
	 */
	public ValueSetDefinition getDefinitionForValueSet(NameOrURI valueSet, NameOrURI tag, 
			QueryControl queryControl, ReadContext context);

	/**
	 * Retrieve the specified value set definition.
	 * 
	 * @param valueSetDefinitionURI
	 * @param queryControl
	 * @param context
	 */
	public ValueSetDefinition read(URI valueSetDefinitionURI, QueryControl queryControl, ReadContext context);
	
}
