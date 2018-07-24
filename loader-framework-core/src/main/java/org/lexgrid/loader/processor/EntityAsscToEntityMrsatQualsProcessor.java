package org.lexgrid.loader.processor;

import java.util.concurrent.TimeUnit;

import org.LexGrid.relations.AssociationQualification;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.database.key.AssociationInstanceKeyResolver;
import org.lexgrid.loader.processor.support.OptionalQualifierResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.beans.factory.InitializingBean;

public class EntityAsscToEntityMrsatQualsProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, ParentIdHolder<AssociationQualification>> implements InitializingBean{


	/** The key resolver. */
	private AssociationInstanceIdResolver<I> associationInstanceIdResolver;
	
	/** The qualifier resolver. */
	private OptionalQualifierResolver<I> qualifierResolver;

	@Override
	public ParentIdHolder<AssociationQualification> doProcess(I item) throws Exception {
	
		long start = System.nanoTime();
		System.out.println("Read Completed: " + TimeUnit.SECONDS.convert(start, TimeUnit.NANOSECONDS));
		if(!qualifierResolver.toProcess(item)) {
			return null;
		}
		
		AssociationQualification qual = DataUtils.createAssociationQualifier(qualifierResolver, item);

		String associationInstanceId = associationInstanceIdResolver.resolveAssociationInstanceId(item);
		long end = System.nanoTime();
		System.out.println("Processor completed: " + TimeUnit.SECONDS.convert(end, TimeUnit.NANOSECONDS));
		System.out.println("Processor Time: " + TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS));
		return new ParentIdHolder<AssociationQualification>(
				this.getCodingSchemeIdSetter(),
				 associationInstanceId, 
					qual);
	}

	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate s, ParentIdHolder<AssociationQualification> item) {
		this.getSupportedAttributeTemplate().addSupportedAssociationQualifier(
				super.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				super.getCodingSchemeIdSetter().getCodingSchemeVersion(),
				item.getItem().getAssociationQualifier(),
				null, 
				null);
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public AssociationInstanceIdResolver<I> getAssociationInstanceIdResolver() {
		return associationInstanceIdResolver;
	}

	public void setAssociationInstanceIdResolver(AssociationInstanceIdResolver<I> associationInstanceIdResolver) {
		this.associationInstanceIdResolver = associationInstanceIdResolver;
	}

	public OptionalQualifierResolver<I> getQualifierResolver() {
		return qualifierResolver;
	}

	public void setQualifierResolver(OptionalQualifierResolver<I> qualifierResolver) {
		this.qualifierResolver = qualifierResolver;
	}

}
