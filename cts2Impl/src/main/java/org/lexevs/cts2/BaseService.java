package org.lexevs.cts2;

import org.lexevs.dao.database.key.Java5UUIDKeyGenerator;

/**
 * The Class BaseService.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public abstract class BaseService extends LexEvsBasedService {

    private transient LexEvsCTS2 lexEvsCTS2;
    private ServiceInfo serviceInfo_ = null;
 
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

	protected LexEvsCTS2 getLexEvsCTS2() {
		if(this.lexEvsCTS2 == null) {
			this.lexEvsCTS2 = LexEvsCTS2Impl.defaultInstance();
		}
		return this.lexEvsCTS2;
	}
	
	/**
	 * Creates the unique id.
	 * 
	 * @return the string
	 */
	protected String createUniqueId(){
		return Java5UUIDKeyGenerator.getRandomUUID().toString();
	}
}
