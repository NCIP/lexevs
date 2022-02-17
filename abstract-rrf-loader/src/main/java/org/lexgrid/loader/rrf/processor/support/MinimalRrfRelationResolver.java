
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MinimalRrfRelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class MinimalRrfRelationResolver extends AbstractRrfRelationResolver{
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getContainerName()
	 */
	public String getContainerName(){
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getRelation(org.lexgrid.loader.rrf.model.Mrrel)
	 */
	public String getRelation(Mrrel item){
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getSource(org.lexgrid.loader.rrf.model.Mrrel)
	 */
	public String getSource(Mrrel item){
		return null;
		
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getTarget(org.lexgrid.loader.rrf.model.Mrrel)
	 */
	public String getTarget(Mrrel item){
		return null;
	}


}