
package org.LexGrid.LexBIG.LexBIGService;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * Interface to perform system-wide query over metadata for loaded code systems and providers.
 */
public interface LexBIGServiceMetadata extends Serializable
{
	/**
	 * List the coding schemes that are represented in the metadata index.
	 * 
	 * @throws LBInvocationException
	 */
	public abstract AbsoluteCodingSchemeVersionReferenceList listCodingSchemes() throws LBInvocationException;
	
    /**
     * Restrict the search to a particular coding scheme.
     * 
     * @param acsvr 
     *      The coding scheme to restrict the search to. You may provide the URN, 
     *      the version, or both.
     */
    public abstract LexBIGServiceMetadata restrictToCodingScheme(AbsoluteCodingSchemeVersionReference acsvr) throws LBParameterException;

    /**
     * Restrict the search to a particular property.  Currently, this can be any element or attribute
     * name from the OBO metadata schema.  When we move to the 2006 version of the schema, there will
     * be a method to get the available properties.
     * 
     * @param properties 
     *      The set of properties to restrict the search to.  If you provide multiple properties, 
     *      it is treated as an OR search.
     * @throws LBParameterException
     */
    public abstract LexBIGServiceMetadata restrictToProperties(String[] properties) throws LBParameterException;

    /**
     * Restrict the search by the parents of the metadata elements.
     * The OBO MetaData format is hierarchial - if you wish to restrict your search to properties
     * that are under another property, provide the required property containers here.  
     * 
     * @param propertyParents
     *      The containers to require as parents.  For example, to restrict the search  to 
     *      "contacts" that are under "about" that is under "authority" - provide "authority"
     *      and "about".  
     *      The order of the parents does not matter.  Multiple parents are treated as an AND - 
     *      so the result is required to be under each of the parents going up the parent tree.
     * @throws LBParameterException
     */
    public abstract LexBIGServiceMetadata restrictToPropertyParents(String[] propertyParents) throws LBParameterException;

    /**
     * Restrict the result to the metadata elements that match the supplied string, 
     * using the supplied matching algorithm 
     * 
     * @param matchText
     *          The match text.  Format is determined by the match algorithm.
     * @param matchAlgorithm
     *            Local name of the match algorithm - possible algorithms are
     *            returned in LexBigService.getMatchAlgorithms().
     * @throws LBParameterException
     */
    public abstract LexBIGServiceMetadata restrictToValue(String matchText, String matchAlgorithm) throws LBParameterException;

    /**
     * Apply all of the restrictions, and return the result.
     * 
     * @return
     * @throws LBParameterException
     * @throws LBInvocationException
     */
    public abstract MetadataPropertyList resolve() throws LBParameterException, LBInvocationException;

}