package org.lexgrid.loader.rrf.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.HighestRankingListProcessor;
import org.lexgrid.loader.processor.support.ListFilter;
import org.lexgrid.loader.processor.support.PropertyResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;

public class MrconsoGroupEntityProcessor extends HighestRankingListProcessor<Mrconso,CodingSchemeIdHolder<Entity>>{

	private PropertyResolver<Mrconso> presentationResolver;
	
	private List<PropertyResolver<Mrconso>> propertyResolvers;
	
	private Map<ListFilter<Mrconso>,PropertyResolver<Mrconso>> filteredPropertyResolvers;
	
	private List<QualifierResolver<Mrconso>> qualifierResolvers;
	
	private List<QualifierResolver<List<Mrconso>>> qualifierListResolvers;
	
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	@Override
	public CodingSchemeIdHolder<Entity> process(List<Mrconso> items)
			throws Exception {
		CodingSchemeIdHolder<Entity> entityHolder = super.process(items);
		
		String uri = entityHolder.getCodingSchemeIdSetter().getCodingSchemeUri();
		String version = entityHolder.getCodingSchemeIdSetter().getCodingSchemeVersion();
		
		//the highest ranking MRCONSO Line will be Preferred -- and every line
		//in MRCONSO is considered a Presentation;
		Mrconso preferred = items.get(0);

		entityHolder.getItem().addPresentation(buildPresentation(uri, version, preferred, true));
		
		//process the rest of the MRCONSO lines
		for(int i=1;i<items.size();i++){
			entityHolder.getItem().addPresentation(buildPresentation(uri, version, items.get(i), false));
		}
		
		if(propertyResolvers != null) {
			for(PropertyResolver<Mrconso> resolver : propertyResolvers) {
				for(Mrconso item : items) {
					entityHolder.getItem().addProperty(
							buildProperty(uri, version, item, resolver));
				}
			}
		}
		
		if(filteredPropertyResolvers != null) {
			for(Entry<ListFilter<Mrconso>,PropertyResolver<Mrconso>> entry : filteredPropertyResolvers.entrySet()) {
				List<Mrconso> filteredList = entry.getKey().filter(items);
				for(Mrconso mrconso : filteredList) {
					entityHolder.getItem().addProperty(
							buildProperty(
									uri,
									version,
									mrconso,
									entry.getValue()));	
				}
			}
		}
		
		return entityHolder;
	}
	
	protected Property buildProperty(String uri, String version, Mrconso mrconso, PropertyResolver<Mrconso> resolver){
		Property prop = new Property();
		prop.setPropertyId(resolver.getId(mrconso));
		prop.setLanguage(resolver.getLanguage(mrconso));
		prop.setPropertyName(resolver.getPropertyName(mrconso));
		prop.setPropertyType(resolver.getPropertyType(mrconso));
		prop.setValue(DaoUtility.createText(resolver.getPropertyValue(mrconso)));
		
		if(this.qualifierResolvers != null){
			prop.setPropertyQualifier(buildPropertyQualifiers(uri, version, mrconso).toArray(new PropertyQualifier[0]));
		}

		this.getSupportedAttributeTemplate().addSupportedProperty(uri, version, prop.getPropertyName(), null, prop.getPropertyName());
		return prop;	
	}
	
	protected Presentation buildPresentation(String uri, String version, Mrconso mrconso, boolean preferred){
		Presentation pres = new Presentation();
		pres.setPropertyId(presentationResolver.getId(mrconso));
		pres.setDegreeOfFidelity(presentationResolver.getDegreeOfFidelity(mrconso));
		pres.setLanguage(presentationResolver.getLanguage(mrconso));
		pres.setMatchIfNoContext(presentationResolver.getMatchIfNoContext(mrconso));
		pres.setPropertyName(presentationResolver.getPropertyName(mrconso));
		pres.setPropertyType(presentationResolver.getPropertyType(mrconso));
		pres.setValue(DaoUtility.createText(presentationResolver.getPropertyValue(mrconso)));
		pres.setIsPreferred(preferred);
		
		if(this.qualifierResolvers != null){
			pres.setPropertyQualifier(buildPropertyQualifiers(uri, version, mrconso).toArray(new PropertyQualifier[0]));
		}

		this.getSupportedAttributeTemplate().addSupportedProperty(uri, version, pres.getPropertyName(), null, pres.getPropertyName());
		return pres;
	}
	
	protected List<PropertyQualifier> buildPropertyQualifiers(String uri, String version, Mrconso mrconso){
		List<PropertyQualifier> returnList = new ArrayList<PropertyQualifier>();
		
		for(QualifierResolver<Mrconso> qualResolver : this.qualifierResolvers){
			PropertyQualifier qual = new PropertyQualifier();
			qual.setPropertyQualifierName(qualResolver.getQualifierName());
			qual.setValue(DaoUtility.createText(qualResolver.getQualifierValue(mrconso)));
			
			this.getSupportedAttributeTemplate().addSupportedPropertyQualifier(uri, version, qual.getPropertyQualifierName(), null, qual.getPropertyQualifierName());
			returnList.add(qual);
		}
		return returnList;
	}

	public List<QualifierResolver<Mrconso>> getQualifierResolvers() {
		return qualifierResolvers;
	}

	public void setQualifierResolvers(
			List<QualifierResolver<Mrconso>> qualifierResolvers) {
		this.qualifierResolvers = qualifierResolvers;
	}

	public PropertyResolver<Mrconso> getPresentationResolver() {
		return presentationResolver;
	}

	public void setPresentationResolver(
			PropertyResolver<Mrconso> presentationResolver) {
		this.presentationResolver = presentationResolver;
	}

	public List<PropertyResolver<Mrconso>> getPropertyResolvers() {
		return propertyResolvers;
	}

	public void setPropertyResolvers(
			List<PropertyResolver<Mrconso>> propertyResolvers) {
		this.propertyResolvers = propertyResolvers;
	}

	public List<QualifierResolver<List<Mrconso>>> getQualifierListResolvers() {
		return qualifierListResolvers;
	}

	public void setQualifierListResolvers(
			List<QualifierResolver<List<Mrconso>>> qualifierListResolvers) {
		this.qualifierListResolvers = qualifierListResolvers;
	}

	public Map<ListFilter<Mrconso>, PropertyResolver<Mrconso>> getFilteredPropertyResolvers() {
		return filteredPropertyResolvers;
	}

	public void setFilteredPropertyResolvers(
			Map<ListFilter<Mrconso>, PropertyResolver<Mrconso>> filteredPropertyResolvers) {
		this.filteredPropertyResolvers = filteredPropertyResolvers;
	}

	public void setSupportedAttributeTemplate(SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}

	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}
}
