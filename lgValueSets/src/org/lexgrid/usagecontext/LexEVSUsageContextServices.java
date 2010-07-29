package org.lexgrid.usagecontext;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.concepts.Entity;

/**
 * LexEVS Usage Context Services.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface LexEVSUsageContextServices extends Serializable {
	
	/**
	 * Gets the Usage Context coding scheme.
	 * @param versionOrTag usageContext coding scheme version or tag
	 * @return the usage context coding scheme
	 * @throws LBException
	 */
	public CodingScheme getUsageContextCodingScheme(CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns UsageContext entity object with usageContextId.
	 * 
	 * @param usageContextId  id of usage context
	 * @param versionOrTag usageContext coding scheme version or tag
	 * @return usage context entity object
	 * @throws LBException
	 */
	public Entity getUsageContextEntity(String usageContextId, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns list of usage context entities matching the name provided.
	 * @param usageContextName 
	 * 			  match name of usage context
	 * @param versionOrTag 
	 * 			  usage context coding scheme version or tag
	 * @param option 
	 *            Indicates the designations to search (one of the enumerated
	 *            type SearchDesignationOption).
	 * @param matchAlgorithm
	 *            Local name of the match algorithm - possible algorithms are
	 *            returned in LexBigService.getMatchAlgorithms().
	 * @param language
	 *            Language of search string. If missing, use the default
	 *            language specified in the context.
	 * @return list of entities containing matching usage context name
	 * @throws LBException
	 */
	public List<Entity> getUsageContextEntitisWithName(String usageContextName, CodingSchemeVersionOrTag versionOrTag, SearchDesignationOption option, String matchAlgorithm, String language) throws LBException;
	
	/**
	 * Returns coded node set for usage context entities.
	 * 
	 * @param versionOrTag 
	 * 			  usage context coding scheme version or tag
	 * @return codedNodeSet of usage context entities
	 * @throws LBException
	 */
	public CodedNodeSet getUsageContextCodedNodeSet(CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Gets all the usage context found in the system as entities.
	 * 
	 * @param versionOrTag 
	 * 			  usage context coding scheme version or tag
	 * @return List of usage context entities
	 * @throws LBException
	 */
	public List<Entity> listAllUsageContextEntities(CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Returns all the usage context identifiers found in the system.
	 * 
	 * @param versionOrTag 
	 * 			  usage context coding scheme version or tag
	 * @return List of usage context identifiers
	 * @throws LBException
	 */
	public List<String> listAllUsageContextIds(CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Inserts UsageContext into UsageContext coding scheme.
	 * 
	 * @param usageContextId - 
	 * 				Id of UsageContext
	 * @param usageContextName -
	 * 				UsageContext name
	 * @param revisionId - 
	 * 				revision id of UsageContext
	 * @param description - 
	 * 				UsageContext description
	 * @param status - 
	 * 				UsageContext status
	 * @param properties -
	 * 				UsageContext properties
	 * @param versionOrTag -
	 * 			  UsageContext coding scheme version or tag
	 * 
	 * @throws LBException
	 */
	public void insertUsageContext(String usageContextId, String usageContextName, 
			String revisionId, String description, String status, Properties properties, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Inserts UsageContext entity into UsageContext coding scheme.
	 * 
	 * @param usageContextn - 
	 * 				UsageContext entity object to be inserted
	 * @param versionOrTag -
	 * 			  UsageContext coding scheme version or tag
	 * @throws LBException
	 */
	public void insertUsageContext(Entity usageContext, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
	/**
	 * Remove UsageContext from the UsageContext coding scheme.
	 * 
	 * @param usageContextId - 
	 * 				Identifier of the UsageContext that will be removed
	 * @param versionOrTag -
	 * 			  UsageContext coding scheme version or tag
	 * @throws LBException
	 */
	public void removeUsageContext(String usageContextId, CodingSchemeVersionOrTag versionOrTag) throws LBException;
	
}
