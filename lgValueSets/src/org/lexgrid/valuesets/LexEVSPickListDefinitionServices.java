
package org.lexgrid.valuesets;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.PickListDefinition;
import org.lexgrid.valuesets.dto.ResolvedPickListEntryList;

/**
 * PickList Services Interface. 
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface LexEVSPickListDefinitionServices extends Serializable {

	/**
	 * Returns pickList definition for supplied pickListId.
	 *  
	 * @param pickListId
	 * 			pickListId of a pickListDefinition
	 * @throws LBException
	 * @return
	 * 			PickListDefinition object.
	 */
	public PickListDefinition getPickListDefinitionById(String pickListId) throws LBException;
	
	/**
	 * Returns all the pickList definition id's that represents supplied value set definition URI.
	 * 
	 * @param valueSetDefURI
	 * 			URI of an value set definition
	 * @throws LBException
	 * @return
	 * 			List of Pick List Definition Id's that represents supplied valueSetDefURI.
	 */
	public List<String> getPickListDefinitionIdForValueSetDefinitionUri(URI valueSetDefURI)throws LBException;
	
	/**
	 * Returns an URI of the represented value set definition of the pickList.
	 *  
	 * @param pickListId
	 * @throws LBException
	 * @return valueSetDefURI
	 */
	public URI getPickListValueSetDefinition(String pickListId) throws LBException;
	
	/**
	 * Returns list of pickListIds that are available in the system.
	 * 
	 * @return list of available pickListIds
	 * @throws LBException
	 */
	public List<String> listPickListIds() throws LBException;
	
	/**
	 * Loads supplied PickListDefinition object
	 * @param pldef pick list to load
	 * @param systemReleaseURI
	 * @param mappings
	 * @throws LBException
	 */
	public void loadPickList(PickListDefinition pldef, String systemReleaseURI, Mappings mappings) throws LBException;
	
	/**
	 * Loads pick list by reading XML file location supplied
	 * @param xmlFileLocation XML file containing pick list definitions
	 * @param failOnAllErrors
	 * @throws LBException
	 */
	public void loadPickList(String xmlFileLocation, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Removes pick list definition from the system that matches supplied pickListId.
	 * 
	 * @param pickListId id of pickList to remove
	 * @throws LBException
	 */
	void removePickList(String pickListId) throws LBException;
	
	/**
	 * Resolves pickList definition for supplied pickListId.
	 * 
	 * @param pickListId
	 * 			pickListId of a pickListDefinition.
	 * @param sortByText
	 * 			If True; the resolved pickListEntries will be sorted by text in ascending order.
	 * @param csVersionList        - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
     * @param versionTag           - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
	 * @return
	 * 			Resolved PickListEntries.
	 * @throws LBException
	 */
	public ResolvedPickListEntryList resolvePickList(String pickListId, boolean sortByText,
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;
	
	/**
	 * Resolves pickList definition for supplied pickListId.
	 * 
	 * @param pickListId
	 * 			pickListId of a pickListDefinition.
	 * @param sortByText
	 * 			If 1-Ascending, 2-Descending, and 3-Custom;
	 * @param csVersionList        - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
     * @param versionTag           - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
	 * @return
	 * 			Resolved PickListEntries.
	 * @throws LBException
	 */
	public ResolvedPickListEntryList resolvePickList(String pickListId, Integer sortType,
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;
	
	/**
	 * Resolves pick list definition object supplied against supplied coding scheme version list.
	 * 
	 * @param pickList
	 * 			pickListDefinition object.
	 * @param sortByText
	 * 			If True; the resolved pickListEntries will be sorted by text in ascending order.
	 * @param csVersionList        - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
     * @param versionTag           - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
	 * @return
	 * 			Resolved PickListEntries.
	 * @throws LBException
	 */
	public ResolvedPickListEntryList resolvePickList(PickListDefinition pickList, boolean sortByText,
	AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;

	/**
	 * Resolves pickList definition for supplied arguments.
	 * 
	 * @param pickListId 
	 * 			pickListId of a pickListDefinition. This is required argument.
	 * @param term
	 * 			Term to restrict. This is required argument.
	 * @param matchAlgorithm
	 * 			Optional, match algorithm to use.
	 * @param language
	 * 			Optional, language to restrict.
	 * @param context
	 * 			Optional, list of context to restrict.
	 * @param sortByText
	 * 			If True; the resolved pickListEntries will be sorted by text in ascending order.
	 * @param csVersionList        - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
     * @param versionTag           - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
	 * @return
	 * 			Resolved PickListEntries.
	 * @throws LBException
	 */
	public ResolvedPickListEntryList resolvePickListForTerm(String pickListId, String term, String matchAlgorithm, 
			String language, String[] context, boolean sortByText,
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;
	
	/**
	 * 
	 * @param uri XML file containing pickList definitions
	 * @param valicationLevel validate &lt;int&gt; Perform validation of the candidate
	 *         resource without loading data.  
	 *         Supported levels of validation include:
	 *         0 = Verify document is well-formed
	 *         1 = Verify document is valid
	 * @throws LBException
	 */
	public void validate(URI uri, int valicationLevel) throws LBException;
	
	/**
	 * Return the map set of pick list id and pick list name that references given entityCode, namespace and optionally propertyId
	 * 
	 * @param entityCode referenced entityCode
	 * @param entityCodeNameSpace referenced entityCodeNamespace
	 * @param propertyId Optional propertyId
	 * @param extractPickListName true means pick list name will be extracted in the map set.
	 * 
	 * @return Mapset of pick list id and pick list name
	 * 
	 * @throws LBException
	 */
	public Map<String, String> getReferencedPLDefinitions(String entityCode,
			String entityCodeNameSpace, String propertyId,
			Boolean extractPickListName) throws LBException ;

	/**
	 * Return the map set of pick list id and pick list name that references value set definition URI
	 * 
	 * @param valueSet URI of value set definition
	 * @param extractPickListName true means pick list name will be extracted in the map set
	 * 
	 * @return Mapset of pick list id and pick list name
	 * 
	 * @throws LBException
	 */
	public Map<String, String> getReferencedPLDefinitions(
			String valueSet, Boolean extractPickListName)
			throws LBException;

	/**
	 * Returns all the pickListIds that contain supplied supported tag and value.
	 * 
	 * @param supportedTag like SupportedCodingScheme, SupportedAssociation etc.
	 * @param value value to look for
	 * @return list of pickListIds that contains supportedTag with value.
	 */
	public List<String> getPickListIdsForSupportedTagAndValue(String supportedTag, String value);
	
	/**
	 * Exports the pick list definition in LexGrid XML format.
	 * 
	 * @param pickListId id of pick list definition to export
	 * @param xmlFolderLocation destination location
	 * @param overwrite 
	 * @param failOnAllErrors
	 * @throws LBException
	 */
	public void exportPickListDefinition(String pickListId,
			String xmlFolderLocation, boolean overwrite, boolean failOnAllErrors)
			throws LBException;
	public LogEntry[] getLogEntries();
	
	public PickListDefinition resolvePickListByRevision(String pickListId,
			String revisionId, Integer sortOrder) throws LBRevisionException;
}