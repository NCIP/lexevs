
package org.lexgrid.loader.processor;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.relations.AssociationQualification;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.database.key.AssociationInstanceKeyResolver;
import org.lexgrid.loader.processor.support.OptionalQualifierResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityAssnToEQualsListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnToEQualsListProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, List<ParentIdHolder<AssociationQualification>>> {

	/** The key resolver. */
	private AssociationInstanceIdResolver<I> associationInstanceIdResolver;
	
	private AssociationInstanceKeyResolver associationInstanceKeyResolver;
	
	/** The qualifier resolver. */
	private List<OptionalQualifierResolver<I>> optionalQualifierResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<ParentIdHolder<AssociationQualification>> doProcess(I item) throws Exception {
		List<ParentIdHolder<AssociationQualification>> returnlist = 
			new ArrayList<ParentIdHolder<AssociationQualification>>();

		for(OptionalQualifierResolver<I> qualifierResolver : optionalQualifierResolver){
			if(qualifierResolver.toProcess(item)){
				returnlist.add(buildEntityAssnsToEquals(item, 
						qualifierResolver));
			}
		}
		return returnlist;	
	}

	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate supportedAttributeTemplate,
			List<ParentIdHolder<AssociationQualification>> items) {
		for(ParentIdHolder<AssociationQualification> qual : items){
			supportedAttributeTemplate.addSupportedAssociationQualifier(
					super.getCodingSchemeIdSetter().getCodingSchemeUri(), 
					super.getCodingSchemeIdSetter().getCodingSchemeVersion(),
					qual.getItem().getAssociationQualifier(),
					null, 
					qual.getItem().getAssociationQualifier());
		}		
	}

	protected ParentIdHolder<AssociationQualification> buildEntityAssnsToEquals(I item, QualifierResolver<I> qualifierResolver){
		AssociationQualification qual = new AssociationQualification();
		
		qual.setAssociationQualifier(qualifierResolver.getQualifierName(item));
		qual.setQualifierText(qualifierResolver.getQualifierValue(item));

		String associationInstanceId = associationInstanceIdResolver.resolveAssociationInstanceId(item);
		return new ParentIdHolder<AssociationQualification>(
				this.getCodingSchemeIdSetter(),
				associationInstanceKeyResolver.
					resolveKey(this.getCodingSchemeIdSetter().getCodingSchemeName(), associationInstanceId), 
					qual);
	}


	public AssociationInstanceIdResolver<I> getAssociationInstanceIdResolver() {
		return associationInstanceIdResolver;
	}

	public void setAssociationInstanceIdResolver(
			AssociationInstanceIdResolver<I> associationInstanceIdResolver) {
		this.associationInstanceIdResolver = associationInstanceIdResolver;
	}

	public List<OptionalQualifierResolver<I>> getOptionalQualifierResolver() {
		return optionalQualifierResolver;
	}

	public void setOptionalQualifierResolver(
			List<OptionalQualifierResolver<I>> optionalQualifierResolver) {
		this.optionalQualifierResolver = optionalQualifierResolver;
	}
}