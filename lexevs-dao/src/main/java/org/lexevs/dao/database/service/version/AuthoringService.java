
package org.lexevs.dao.database.service.version;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;

/**
 * The Interface VersionService.
 * 
 * @author <a href="mailto:rao.ramachandra@mayo.edu">Ramachandra Rao (Satya)</a>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AuthoringService {

	/**
	 * Load system release. A systemRelease can contain a codingScheme,
	 * valueSet, pickList and/or revision objects. All codingScheme, valueSet
	 * and pickLists loaded outside revision are wrapped under a system
	 * generated revision object.
	 * 
	 * @param systemRelease the system release
	 * @param indexNewCodingScheme the index new coding scheme
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public void loadSystemRelease(SystemRelease systemRelease, Boolean indexNewCodingScheme) throws LBRevisionException;

	/**
	 * Method Loads the revision of an entry point object in lexEVS system.
	 * Revision will be validated for proper syntax and sequence before loading.
	 * If invalid, LBRevisionException is thrown. Entry point objects in lexEVS
	 * system are CodingScheme, ValueSet and PickList. A revision can contain
	 * single or multiple instances of one or all of the entry point objects.
	 * ChangedEntries are loaded by ascending order of relativeOrder.
	 * 
	 * @param revision - revision object to be applied.
	 * @param systemReleaseURI - URI of the systemRelease (if any)
	 * @param indexNewCodingScheme - Boolean value to indicate if the any newly loaded codingScheme
	 * in this revision needs to Lucene indexed or not.
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public void loadRevision(Revision revision, String systemReleaseURI, Boolean indexNewCodingScheme) throws LBRevisionException;
	
	/**
	 * Method Loads an entry point versionable object by wrapping it into a
	 * revision. Revision will be validated for proper syntax and sequence
	 * before loading. If invalid, LBRevisionException is thrown. Entry point
	 * objects in lexEVS system are CodingScheme, ValueSet and PickList.
	 * 
	 * @param versionable the versionable
	 * @param releaseURI - URI of the systemRelease (if any)
	 * @param indexNewCodingScheme - Boolean value to indicate if the any newly loaded
	 * codingScheme in this revision needs to Lucene indexed or not.
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public void loadRevision(Versionable versionable, String releaseURI, Boolean indexNewCodingScheme) throws LBRevisionException;
	
	/**
	 * insert system release entry.
	 * 
	 * @param systemRelease the system release
	 * 
	 * @return the string
	 */
	public String insertSystemReleaseMetadata(SystemRelease systemRelease);
	
	/**
	 * get system release entry for a given uri.
	 * 
	 * @param systemReleaseUri the system release uri
	 * 
	 * @return the system release metadata by uri
	 */
	public SystemRelease getSystemReleaseMetadataByUri(String systemReleaseUri);
	
	/**
	 * get system release entry for a given unique id.
	 * 
	 * @param systemReleaseId the system release id
	 * 
	 * @return the system release metadata by id
	 */
	public SystemRelease getSystemReleaseMetadataById(String systemReleaseId);
	
	/**
	 * removes revision record if not referenced by any existing entries.
	 * 
	 * @param revisionId to be removed
	 * 
	 * @return true if removed successfully
	 * 
	 * @throws LBException the LB exception
	 */
	public boolean removeRevisionRecordbyId(String revisionId) throws LBException;
}