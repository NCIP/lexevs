
package org.lexgrid.loader.processor.support;

/**
 * The Class AbstractBasicAssociationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBasicAssociationResolver<I> implements AssociationResolver<I> {
	
	/** The entity code resolver. */
	private EntityCodeResolver<I> entityCodeResolver;
	
	/** The entity namespace resolver. */
	private EntityNamespaceResolver<I> entityNamespaceResolver;
	
	/** The entity description resolver. */
	private EntityDescriptionResolver<I> entityDescriptionResolver;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getAssociationName(java.lang.Object)
	 */
	public String getAssociationName(I item) {
		return entityCodeResolver.getEntityCode(item);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(I item) {
		return entityCodeResolver.getEntityCode(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getEntityCodeNamespace(java.lang.Object)
	 */
	public String getEntityCodeNamespace(I item) {
		return entityNamespaceResolver.getEntityNamespace(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getEntityDescription(java.lang.Object)
	 */
	public String getEntityDescription(I item) {
		return entityDescriptionResolver.getEntityDescription(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AssociationResolver#getForwardName(java.lang.Object)
	 */
	public String getForwardName(I item) {
		return entityCodeResolver.getEntityCode(item);
	}
}