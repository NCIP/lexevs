
package org.lexevs.dao.index.operation;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * The Interface LexEvsIndexOperations.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LexEvsIndexOperations {

	/**
	 * Register coding scheme in the index. No information is actually indexed, but all metadata
	 * and information is created and stored, enabling future indexing to occur on this coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void registerCodingSchemeEntityIndex(String codingSchemeUri, String version);
	
	public void cleanUp(List<AbsoluteCodingSchemeVersionReference> expectedCodingSchemes, boolean reindexMissing);

	public String getLexEVSIndexLocation();
	
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference ref) throws LBParameterException;
	
}