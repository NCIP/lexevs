
package org.lexgrid.loader.processor.decorator;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.apache.commons.collections.CollectionUtils;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.processor.EntityPropertyProcessor;
import org.lexgrid.loader.processor.support.OptionalPropertyQualifierResolver;
import org.lexgrid.loader.processor.support.SourceResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemProcessor;

public class EntityPropertyQualifierAndSourceAddingDecorator<I> implements ItemProcessor<I, ParentIdHolder<Property>> {

	private EntityPropertyProcessor<I> delegate;
	
	private List<OptionalPropertyQualifierResolver<I>> qualifierResolvers;
	
	private List<SourceResolver<I>> sourceResolvers;
	
	public EntityPropertyQualifierAndSourceAddingDecorator(EntityPropertyProcessor<I> delegate){
		this.delegate = delegate;
	}

	@Override
	public ParentIdHolder<Property> process(I item) throws Exception {
		ParentIdHolder<Property> prop = delegate.process(item);
		
		if(CollectionUtils.isNotEmpty(qualifierResolvers)) {
			
			for(OptionalPropertyQualifierResolver<I> qualifierResolver : this.qualifierResolvers) {
				if(qualifierResolver.toProcess(item)) {
					prop.getItem().addPropertyQualifier(
							DataUtils.createPropertyQualifier(qualifierResolver, item));
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(sourceResolvers)) {
			
			for(SourceResolver<I> sourceResolver : sourceResolvers) {
				prop.getItem().addSource(
						DataUtils.createSource(sourceResolver, item));
			}
		}
		
		return prop;
	}

	public List<SourceResolver<I>> getSourceResolvers() {
		return sourceResolvers;
	}

	public void setSourceResolvers(List<SourceResolver<I>> sourceResolvers) {
		this.sourceResolvers = sourceResolvers;
	}

	public List<OptionalPropertyQualifierResolver<I>> getQualifierResolvers() {
		return qualifierResolvers;
	}

	public void setQualifierResolvers(
			List<OptionalPropertyQualifierResolver<I>> qualifierResolvers) {
		this.qualifierResolvers = qualifierResolvers;
	}
}