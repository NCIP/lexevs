
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

/**
 * The Interface UmlsBatchLoader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MedRtUmlsBatchLoader extends SpringBatchLoader {
	
	public static String NAME = "MedRtUmlsBatchLoader";
	public static String VERSION = "1.0";
	public static String DESCRIPTION = "This loader loads MED-RT contents in RRF format into the LexGrid database.";
	
	/**
	 * Load umls.
	 * 
	 * @param sab the sab
	 * @param rrfDir the rrf dir
	 * 
	 * @return the job execution
	 * 
	 * @throws Exception the exception
	 */
	public void loadMEDRT(URI rrfDir) throws Exception;
	
	/**
	 * Resume umls.
	 * 
	 * @param sab the sab
	 * @param rrfDir the rrf dir
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the job execution
	 * 
	 * @throws Exception the exception
	 */
	public void resumeMEDRT(URI rrfDir, String uri, String version) throws Exception;
	
	/**
	 * Removes the load.
	 * 
	 * @param sab the sab
	 * @param rrfDir the rrf dir
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @throws Exception the exception
	 */
	public void removeLoad(String uri, String version) throws Exception;
}