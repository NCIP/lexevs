
package org.lexgrid.loader.meta.processor.support;

import java.util.List;

import org.lexgrid.loader.meta.staging.processor.MetaMrconsoStagingDao;
import org.lexgrid.loader.rrf.processor.support.AbstractRrfRootNodeResolver;

/**
 * The Class UmlsRootNodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaRootNodeResolver extends AbstractRrfRootNodeResolver {
	
	private MetaMrconsoStagingDao metaMrconsoStagingDao;
	
	private List<String> rootCuis;
	/**
	 * Construct root node.
	 * 
	 * @return the string
	 */
	protected boolean isSourceRootNode(String root){
		//We can't populate rootCuis on bean init, because the staging tables
		//aren't loaded yet. Here, we just do it on the first call. On restarts,
		//this will repopulate itself as needed.
		if(rootCuis == null){
			rootCuis = metaMrconsoStagingDao.getMetaRootCuis();
		}
		return rootCuis.contains(root);
	}
	
	public MetaMrconsoStagingDao getMetaMrconsoStagingDao() {
		return metaMrconsoStagingDao;
	}
	public void setMetaMrconsoStagingDao(MetaMrconsoStagingDao metaMrconsoStagingDao) {
		this.metaMrconsoStagingDao = metaMrconsoStagingDao;
	}
}