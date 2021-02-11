
package org.LexGrid.LexBIG.Extensions.Load;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Extendable;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.StatusReporter;
import org.LexGrid.LexOnt.CodingSchemeManifest;

/**
 * The loader interface validates and/or loads content for a service.
 * 
 * @created 08-Feb-2006 3:45:32 PM
 * @author solbrigcvs
 * @version 1.0
 */
public interface Loader extends Extendable, StatusReporter {
	
	public void load(URI resource);
	
	public OptionHolder getOptions();

	/**
	 * Clears any associated log entries.
	 */
	public void clearLog();

	/**
	 * Returns absolute references for coding schemes loaded or used by the most
	 * recent operation; empty if not applicable.
	 */
	public AbsoluteCodingSchemeVersionReference[] getCodingSchemeReferences();

	/**
	 * Returns log entries for the current or most recent load operation
	 * that match a particular status; null if no operation has been attempted.
	 * 
	 * @param status One of several log levels defined by the system,
	 * or null to return all log entries. 
	 */
	public LogEntry[] getLog(LogLevel level);

	/**
	 * Returns status of the current or most recent load or validate operation;
	 * null if no operation has been attempted.
	 */
	public LoadStatus getStatus();
	
	/**
	 * Set the CodingSchemeManifest that would be used to modify the ontology content. Once the
	 * ontology is loaded from the source, the manifest would then be applied to modify the 
	 * loaded content. 
	 * @param csm
	 */
	public void setCodingSchemeManifest(CodingSchemeManifest codingSchemeManifest) ;
	
	/**
	 * Get the CodingSchemeManifest that would be used to modify the ontology content. Once the
	 * ontology is loaded from the source, the manifest would then be applied to modify the 
	 * loaded content. 
	 * @param csm
	 */
	public CodingSchemeManifest getCodingSchemeManifest();  
	
	/**
	 * Set the URI of the codingSchemeManifest that would be used to modify the ontology content. 
	 * The CodingSchemeManifest object referenced by the URI is used to set the codingSchemeManifest 
	 * as well.
	 * Once the ontology is loaded from the source, the manifest would then be applied to 
	 * modify the loaded content. 
	 * @param csm
	 */
	public void setCodingSchemeManifestURI(URI  codingSchemeManifestUri) throws LBException;
	
	/**
	 * Get the URI of the codingSchemeManifest that would be used to modify the ontology content. 
	 * Once the ontology is loaded from the source, the manifest would then be applied to modify the 
	 * loaded content. 
	 * @param csm
	 */
	public URI getCodingSchemeManifestURI();  
	
	/**
	 * Returns the current LoaderPreferences object.
	 * 
	 * @return
	 * 		The current LoaderPreferences
	 */
	public LoaderPreferences getLoaderPreferences();
	
	/**
	 * Sets the Loader's LoaderPreferences.
	 * 
	 * @param loaderPreferences
	 * 		The LoaderPreference object to be loaded. It is recommended that all implementing 
	 * 		classes check if the LoaderPreferences object is valid for the loader.
	 * @throws LBParameterException
	 */
	public void setLoaderPreferences(LoaderPreferences loaderPreferences) throws LBParameterException;
	
	/**
	 * Sets the Loader's LoaderPreferences URI.
	 * 
	 * @param loaderPreferences
	 * 		The LoaderPreference URI file to be loaded.
	 * @throws LBParameterException
	 */
	public void setLoaderPreferences(URI loaderPreferencesURI) throws LBParameterException;

	/**
	 * Return the ontology format the loader can handle 
	 */
	public OntologyFormat getOntologyFormat();
}