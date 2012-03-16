package org.lexgrid.loader.rxn.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class RxnMrsatUsageFactory implements FactoryBean, InitializingBean {
	/**
	 * The Enum MrsatPropertyTypes.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum RxnMrsatPropertyTypes {/** The PRESENTATION. */
PRESENTATION, /** The COMMENT. */
 COMMENT, /** The SKIP. */
 SKIP};
	
	/** The mrsat map. */
	private Map<String,RxnMrsatPropertyTypes> mrsatMap = new HashMap<String,RxnMrsatPropertyTypes>();
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
        mrsatMap.put("AN", RxnMrsatPropertyTypes.COMMENT);
        mrsatMap.put("CX", RxnMrsatPropertyTypes.COMMENT);
        mrsatMap.put("HN", RxnMrsatPropertyTypes.COMMENT);
        mrsatMap.put("EV", RxnMrsatPropertyTypes.PRESENTATION);
        mrsatMap.put("DID", RxnMrsatPropertyTypes.SKIP);
        mrsatMap.put("MUI", RxnMrsatPropertyTypes.SKIP);

        // SNOMEDCT puts the language codes in attributes, instead of putting
        // them on the concept
        // don't want to add them as attributes - these will be pulled out in a
        // custom case in the load
        // concept options as necessary.
        mrsatMap.put("LANGUAGECODE", RxnMrsatPropertyTypes.SKIP);
        mrsatMap.put("SUBSETLANGUAGECODE", RxnMrsatPropertyTypes.SKIP);

        // also don't need these from snomed - because they don't really make
        // sense the way
        // that we load them - they have these for each presentation - while in
        // our representation
        // they attributes apply to the entire concept
        mrsatMap.put("DESCRIPTIONSTATUS", RxnMrsatPropertyTypes.SKIP);
        mrsatMap.put("DESCRIPTIONTYPE", RxnMrsatPropertyTypes.SKIP);
        mrsatMap.put("INITIALCAPITALSTATUS", RxnMrsatPropertyTypes.SKIP);
        mrsatMap.put("CHARACTERISTICTYPE", RxnMrsatPropertyTypes.SKIP);
        mrsatMap.put("REFINABILITY", RxnMrsatPropertyTypes.SKIP);
        mrsatMap.put("SUBSETMEMBER", RxnMrsatPropertyTypes.SKIP);
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return mrsatMap;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Map.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

}
