
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * The Interface MetaBatchLoader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MetaBatchLoader extends SpringBatchLoader {
	
	public String NAME = "MetaBatchLoader";
	public String VERSION = "1.1";
	public String DESCRIPTION = "This loader loads NCI MetaThesaurus in RRF format into the LexGrid database.";
	
	/**
	 * Load meta.
	 * 
	 * @param rrfDir the rrf dir
	 * 
	 * @return the job execution
	 * 
	 * @throws Exception the exception
	 */
	public void loadMeta(URI rrfDir) throws Exception;

	/**
	 * Resume meta.
	 * 
	 * @param rrfDir the rrf dir
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the job execution
	 * 
	 * @throws Exception the exception
	 */
	public void resumeMeta(URI rrfDir, String uri, String version) throws Exception;

	public void removeLoad(String uri, String version) throws LBParameterException;	

}