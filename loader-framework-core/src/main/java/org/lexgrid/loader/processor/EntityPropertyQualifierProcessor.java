
package org.lexgrid.loader.processor;

import org.LexGrid.commonTypes.PropertyQualifier;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.database.key.EntityPropertyKeyResolver;
import org.lexgrid.loader.processor.support.PropertyQualifierResolver;
import org.lexgrid.loader.processor.support.PropertyResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityPropertyQualifierProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyQualifierProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, ParentIdHolder<PropertyQualifier>>{

	private PropertyQualifierResolver<I> propertyQualifierResolver;
	
	private EntityPropertyKeyResolver entityPropertyKeyResolver;
	private PropertyResolver<I> propertyResolver;
	
	@Override
	public ParentIdHolder<PropertyQualifier> doProcess(I item) throws Exception {
		PropertyQualifier qual = DataUtils.createPropertyQualifier(propertyQualifierResolver, item);
		
		String parentId = 
			entityPropertyKeyResolver.
				resolveKey(
						getCodingSchemeIdSetter().getCodingSchemeUri(), 
						getCodingSchemeIdSetter().getCodingSchemeVersion(), 
						propertyResolver.getEntityCode(item), 
						propertyResolver.getEntityCodeNamespace(item), 
						propertyResolver.getId(item));
		
		return new ParentIdHolder<PropertyQualifier>(
				this.getCodingSchemeIdSetter(),
				parentId, qual);
	}

	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate s,
			ParentIdHolder<PropertyQualifier> item) {
		// TODO Auto-generated method stub
		
	}



}