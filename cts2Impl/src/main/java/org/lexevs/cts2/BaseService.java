package org.lexevs.cts2;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.versions.Revision;
import org.lexevs.cts2.core.update.RevisionInfo;


public abstract class BaseService {

	private LexBIGServiceManager lbsm_ = null;
    private LexBIGService lbs_ = null;
    private ServiceInfo serviceInfo_ = null;
    protected static LexEvsCTS2 lexevsCTS2_ = LexEvsCTS2Impl.defaultInstance();
	
	public static enum SortableProperties {
        matchToQuery, code, codeSystem, entityDescription, conceptStatus, isActive
    };

    public static enum KnownTags {
        PRODUCTION
    };
    
    public ServiceInfo getServiceInfo(){
    	if (serviceInfo_ == null)
    		serviceInfo_ = new ServiceInfo();
    	
    	return serviceInfo_;
    }
    
    public ExtensionDescriptionList getSupportedSearchAlgorithms() throws LBException{
    	ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
    	if (extensionRegistry != null)
    		return extensionRegistry.getGenericExtensions();
    	
    	return null;
    }
    
    public List<String> getSupportedSearchAlgorithmNames() throws LBException{
    	List<String> searchAlgNames = new ArrayList<String>();
    	ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
    	if (extensionRegistry != null && extensionRegistry.getSearchExtensions() != null)
    	{
    		for (ExtensionDescription ed : extensionRegistry.getSearchExtensions().getExtensionDescription())
    			searchAlgNames.add(ed.getName());
    	}
    	return searchAlgNames;
    }
    
    public ExtensionDescriptionList getSupportedLoaders() throws LBException{
    	ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
    	if (extensionRegistry != null)
    		return extensionRegistry.getLoadExtensions();
    	
    	return null;
    }
    
    public List<String> getSupportedLoaderNames() throws LBException{
    	List<String> loaderNames = new ArrayList<String>();
    	ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
    	if (extensionRegistry != null && extensionRegistry.getLoadExtensions() != null)
    	{
    		for (ExtensionDescription ed : extensionRegistry.getLoadExtensions().getExtensionDescription())
    			loaderNames.add(ed.getName());
    	}
    	return loaderNames;
    }
    
    public ExtensionDescriptionList getSupportedExporters() throws LBException{
    	ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
    	if (extensionRegistry != null)
    		return extensionRegistry.getExportExtensions();
    	
    	return null;
    }
    
    public List<String> getSupportedExporterNames() throws LBException{
    	List<String> exporterNames = new ArrayList<String>();
    	ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
    	if (extensionRegistry != null && extensionRegistry.getExportExtensions() != null)
    	{
    		for (ExtensionDescription ed : extensionRegistry.getExportExtensions().getExtensionDescription())
    			exporterNames.add(ed.getName());
    	}
    	return exporterNames;
    }
    
    public LexBIGServiceManager getLexBIGServiceManager() throws LBException{
		if (lbsm_ == null)
			lbsm_ = getLexBIGService().getServiceManager(null);
		
		return lbsm_;
	}
	
    public LexBIGService getLexBIGService(){
		if (lbs_ == null)
			lbs_ = LexBIGServiceImpl.defaultInstance();
		
		return lbs_;
	}
    
    public Revision getLexGridRevisionObject(RevisionInfo revisionInfo)
	{
		Revision lgRevision = new Revision();
		lgRevision.setChangeAgent(revisionInfo.getChangeAgent());
		lgRevision.setEditOrder(revisionInfo.getEditOrder());
		if (revisionInfo.getDescription() != null)
		{
			EntityDescription ed= new EntityDescription();
			ed.setContent(revisionInfo.getDescription());
			lgRevision.setEntityDescription(ed);
		}
		lgRevision.setRevisionDate(revisionInfo.getRevisionDate());
		lgRevision.setRevisionId(revisionInfo.getRevisionId());
		
		return lgRevision;
	}
}
