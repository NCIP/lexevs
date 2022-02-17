
package org.lexgrid.loader.processor;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.database.key.EntityKeyResolver;
import org.lexgrid.loader.processor.support.PropertyResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The Class EntityPropertyProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyProcessor<I> extends AbstractSupportedAttributeRegisteringProcessor<I, ParentIdHolder<Property>> implements InitializingBean {
	
	/** The property resolver. */
	private PropertyResolver<I> propertyResolver;
	
	private EntityKeyResolver entityKeyResolver;
	
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(propertyResolver);
		Assert.notNull(entityKeyResolver);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<Property> doProcess(I item) throws Exception {
		
		Property prop = new Property();
		
		String parentKey = entityKeyResolver.
			resolveKey( getCodingSchemeIdSetter().getCodingSchemeUri(),
					getCodingSchemeIdSetter().getCodingSchemeVersion(),
								propertyResolver.getEntityCode(item), 
								propertyResolver.getEntityCodeNamespace(item));	
		
		prop.setPropertyId(propertyResolver.getId(item));
	
		Text text = DaoUtility.createText(
				propertyResolver.getPropertyValue(item), 
				propertyResolver.getFormat(item));
		
		prop.setValue(text);
		prop.setIsActive(propertyResolver.getIsActive(item));
		prop.setLanguage(propertyResolver.getLanguage(item));
		prop.setPropertyName(propertyResolver.getPropertyName(item));
		prop.setPropertyType(propertyResolver.getPropertyType(item));
	
		return new ParentIdHolder<Property>(
				this.getCodingSchemeIdSetter(),
				parentKey, prop);
	}
	
	protected void registerSupportedAttributes(SupportedAttributeTemplate supportedAttributeTemplate, ParentIdHolder<Property> item){
		supportedAttributeTemplate.addSupportedProperty(
				super.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				super.getCodingSchemeIdSetter().getCodingSchemeVersion(),
				item.getItem().getPropertyName(), null, null);
		
		String language = item.getItem().getLanguage();
		if(language != null){
			supportedAttributeTemplate.addSupportedLanguage(
					super.getCodingSchemeIdSetter().getCodingSchemeUri(), 
					super.getCodingSchemeIdSetter().getCodingSchemeVersion(),
					language, null, null);
		}
	}

	/**
	 * Gets the property resolver.
	 * 
	 * @return the property resolver
	 */
	public PropertyResolver<I> getPropertyResolver() {
		return propertyResolver;
	}

	/**
	 * Sets the property resolver.
	 * 
	 * @param propertyResolver the new property resolver
	 */
	public void setPropertyResolver(PropertyResolver<I> propertyResolver) {
		this.propertyResolver = propertyResolver;
	}

	public EntityKeyResolver getEntityKeyResolver() {
		return entityKeyResolver;
	}

	public void setEntityKeyResolver(EntityKeyResolver entityKeyResolver) {
		this.entityKeyResolver = entityKeyResolver;
	}
}