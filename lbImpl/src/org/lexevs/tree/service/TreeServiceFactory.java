
package org.lexevs.tree.service;

import java.io.Serializable;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.springframework.aop.framework.Advised;

/**
 * A factory for creating TreeService objects.
 */
@Deprecated
public class TreeServiceFactory implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5794966638343867430L;
	
	/** The instance. */
	private static TreeServiceFactory instance;
	
	/** The remote tree service. */
	private static TreeService remoteTreeService;
	
	/** The local tree service. */
	private static TreeService localTreeService;

	/**
	 * Gets the single instance of TreeServiceFactory.
	 * 
	 * @return single instance of TreeServiceFactory
	 */
	public static synchronized TreeServiceFactory getInstance() {
		if (instance == null) {
			instance = new TreeServiceFactory();
		}
		return instance;
	}
	
	/**
	 * Instantiates a new tree service factory.
	 */
	protected TreeServiceFactory(){};
	
	/**
	 * Gets the tree service.
	 * 
	 * @param lbs the lbs
	 * 
	 * @return the tree service
	 */
	public synchronized TreeService getTreeService(LexBIGService lbs){
		if(lbs instanceof Advised){
			if(remoteTreeService ==  null){
				PathToRootTreeServiceImpl service = (PathToRootTreeServiceImpl)getTreeServiceExtension(lbs);
				remoteTreeService = service.getSpringManagedBean();
			}
			return remoteTreeService;
		} else {
			if(localTreeService ==  null){
				PathToRootTreeServiceImpl service = (PathToRootTreeServiceImpl)getTreeServiceExtension(lbs);
				localTreeService = service.getSpringManagedBean();
			}
			return localTreeService;
		}
	}
	
	/**
	 * Gets the tree service extension.
	 * 
	 * @param lbs the lbs
	 * 
	 * @return the tree service extension
	 */
	protected TreeService getTreeServiceExtension(LexBIGService lbs){
		try {
			return (TreeService)lbs.getGenericExtension("tree-utility");
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the tree service spring bean.
	 * 
	 * @return the tree service spring bean
	 */
	protected TreeService getTreeServiceSpringBean(){
		return (TreeService)ApplicationContextFactory.getInstance().getApplicationContext().getBean("pathToRootTreeServiceImpl");
	}
}