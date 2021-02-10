
package org.lexgrid.loader.meta.processor.support;

import org.lexgrid.loader.processor.support.PropertyLinkResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants.RrfRelationType;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MetaPropertyLinkResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaPropertyLinkResolver implements PropertyLinkResolver<Mrrel>{

	/** The Rrfrelation type. */
	private RrfRelationType RrfrelationType;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyLinkResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(Mrrel item) {
		return item.getCui1();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyLinkResolver#getLink(java.lang.Object)
	 */
	public String getLink(Mrrel item) {
		if(RrfrelationType == RrfRelationType.REL){
			return item.getRel();
		} else if (RrfrelationType == RrfRelationType.RELA){
			return item.getRela();
		} else {
			throw new RuntimeException("RRF Relation Type must be set.");
		}
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyLinkResolver#getSourceId(java.lang.Object)
	 */
	public String getSourceId(Mrrel item) {
		return item.getAui1();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyLinkResolver#getTargetId(java.lang.Object)
	 */
	public String getTargetId(Mrrel item) {
		return item.getAui2();
	}
	
	/**
	 * Gets the rrfrelation type.
	 * 
	 * @return the rrfrelation type
	 */
	public RrfRelationType getRrfrelationType() {
		return RrfrelationType;
	}

	/**
	 * Sets the rrfrelation type.
	 * 
	 * @param rrfrelationType the new rrfrelation type
	 */
	public void setRrfrelationType(RrfRelationType rrfrelationType) {
		RrfrelationType = rrfrelationType;
	}
}