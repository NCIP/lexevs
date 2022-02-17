
package org.lexgrid.loader.rrf.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A factory for creating MrsatUsage objects.
 */
public class MrsatUsageFactory implements FactoryBean, InitializingBean {

	/**
	 * The Enum MrsatPropertyTypes.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum MrsatPropertyTypes {/** The PRESENTATION. */
PRESENTATION, /** The COMMENT. */
 COMMENT, /** The SKIP. */
 SKIP};
	
	/** The mrsat map. */
	private Map<String,MrsatPropertyTypes> mrsatMap = new HashMap<String,MrsatPropertyTypes>();
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
        mrsatMap.put("AN", MrsatPropertyTypes.COMMENT);
        mrsatMap.put("CX", MrsatPropertyTypes.COMMENT);
        mrsatMap.put("HN", MrsatPropertyTypes.COMMENT);

        mrsatMap.put("EV", MrsatPropertyTypes.PRESENTATION);

        mrsatMap.put("DID", MrsatPropertyTypes.SKIP);
        mrsatMap.put("MUI", MrsatPropertyTypes.SKIP);

        // SNOMEDCT puts the language codes in attributes, instead of putting
        // them on the concept
        // don't want to add them as attributes - these will be pulled out in a
        // custom case in the load
        // concept options as necessary.
        mrsatMap.put("LANGUAGECODE", MrsatPropertyTypes.SKIP);
        mrsatMap.put("SUBSETLANGUAGECODE", MrsatPropertyTypes.SKIP);

        // also don't need these from snomed - because they don't really make
        // sense the way
        // that we load them - they have these for each presentation - while in
        // our representation
        // they attributes apply to the entire concept
        mrsatMap.put("DESCRIPTIONSTATUS", MrsatPropertyTypes.SKIP);
        mrsatMap.put("DESCRIPTIONTYPE", MrsatPropertyTypes.SKIP);
        mrsatMap.put("INITIALCAPITALSTATUS", MrsatPropertyTypes.SKIP);
        mrsatMap.put("CHARACTERISTICTYPE", MrsatPropertyTypes.SKIP);
        mrsatMap.put("REFINABILITY", MrsatPropertyTypes.SKIP);
        mrsatMap.put("SUBSETMEMBER", MrsatPropertyTypes.SKIP);
		
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