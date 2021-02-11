
package org.lexgrid.loader.processor;

import org.LexGrid.relations.AssociationQualification;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.database.key.AssociationInstanceKeyResolver;
import org.lexgrid.loader.processor.support.OptionalQualifierResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityAssnToEQualsProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnToEQualsProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, ParentIdHolder<AssociationQualification>> {

	/** The key resolver. */
	private AssociationInstanceIdResolver<I> associationInstanceIdResolver;
	
	private AssociationInstanceKeyResolver associationInstanceKeyResolver;
	
	/** The qualifier resolver. */
	private OptionalQualifierResolver<I> qualifierResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<AssociationQualification> doProcess(I item) throws Exception {
		if(!qualifierResolver.toProcess(item)) {
			return null;
		}
		
		AssociationQualification qual = DataUtils.createAssociationQualifier(qualifierResolver, item);

		String associationInstanceId = associationInstanceIdResolver.resolveAssociationInstanceId(item);
		return new ParentIdHolder<AssociationQualification>(
				this.getCodingSchemeIdSetter(),
				associationInstanceKeyResolver.
					resolveKey(this.getCodingSchemeIdSetter().getCodingSchemeName(), associationInstanceId), 
					qual);
	}

	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate s,
			ParentIdHolder<AssociationQualification> item) {
		this.getSupportedAttributeTemplate().addSupportedAssociationQualifier(
				super.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				super.getCodingSchemeIdSetter().getCodingSchemeVersion(),
				item.getItem().getAssociationQualifier(),
				null, 
				null);	
		
	}
	
	public AssociationInstanceIdResolver<I> getAssociationInstanceIdResolver() {
		return associationInstanceIdResolver;
	}

	public void setAssociationInstanceIdResolver(
			AssociationInstanceIdResolver<I> associationInstanceIdResolver) {
		this.associationInstanceIdResolver = associationInstanceIdResolver;
	}

	public AssociationInstanceKeyResolver getAssociationInstanceKeyResolver() {
		return associationInstanceKeyResolver;
	}

	public void setAssociationInstanceKeyResolver(
			AssociationInstanceKeyResolver associationInstanceKeyResolver) {
		this.associationInstanceKeyResolver = associationInstanceKeyResolver;
	}

	/**
	 * Gets the qualifier resolver.
	 * 
	 * @return the qualifier resolver
	 */
	public OptionalQualifierResolver<I> getQualifierResolver() {
		return qualifierResolver;
	}

	/**
	 * Sets the qualifier resolver.
	 * 
	 * @param qualifierResolver the new qualifier resolver
	 */
	public void setQualifierResolver(OptionalQualifierResolver<I> qualifierResolver) {
		this.qualifierResolver = qualifierResolver;
	}
}