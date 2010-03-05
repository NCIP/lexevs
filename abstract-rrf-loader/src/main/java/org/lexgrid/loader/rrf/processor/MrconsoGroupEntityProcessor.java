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
import org.lexgrid.loader.processor.HighestRankingListProcessor;
import org.lexgrid.loader.processor.support.ListFilter;
import org.lexgrid.loader.processor.support.PropertyResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;

public class MrconsoGroupEntityProcessor extends HighestRankingListProcessor<Mrconso,CodingSchemeIdHolder<Entity>>{

	public PropertyResolver<Mrconso> presentationResolver;
	
	public List<PropertyResolver<Mrconso>> propertyResolvers;
	
	public Map<ListFilter<Mrconso>,PropertyResolver<Mrconso>> filteredPropertyResolvers;
	
	public List<QualifierResolver<Mrconso>> qualifierResolvers;
	
	public List<QualifierResolver<List<Mrconso>>> qualifierListResolvers;
	
	@Override
	public CodingSchemeIdHolder<Entity> process(List<Mrconso> items)
			throws Exception {
		CodingSchemeIdHolder<Entity> entityHolder = super.process(items);
		
		//the highest ranking MRCONSO Line will be Preferred -- and every line
		//in MRCONSO is considered a Presentation;
		Mrconso preferred = items.get(0);

		entityHolder.getItem().addPresentation(buildPresentation(preferred, true));
		
		//process the rest of the MRCONSO lines
		for(int i=1;i<items.size();i++){
			entityHolder.getItem().addPresentation(buildPresentation(items.get(i), false));
		}
		
		if(propertyResolvers != null) {
			for(PropertyResolver<Mrconso> resolver : propertyResolvers) {
				for(Mrconso item : items) {
					entityHolder.getItem().addProperty(
							buildProperty(item, resolver));
				}
			}
		}
		
		if(filteredPropertyResolvers != null) {
			for(Entry<ListFilter<Mrconso>,PropertyResolver<Mrconso>> entry : filteredPropertyResolvers.entrySet()) {
				List<Mrconso> filteredList = entry.getKey().filter(items);
				for(Mrconso mrconso : filteredList) {
					entityHolder.getItem().addProperty(
							buildProperty(
									mrconso,
									entry.getValue()));	
				}
			}
		}
		
		return entityHolder;
	}
	
	protected Property buildProperty(Mrconso mrconso, PropertyResolver<Mrconso> resolver){
		Property prop = new Property();
		prop.setPropertyId(resolver.getId(mrconso));
		prop.setLanguage(resolver.getLanguage(mrconso));
		prop.setPropertyName(resolver.getPropertyName(mrconso));
		prop.setPropertyType(resolver.getPropertyType(mrconso));
		prop.setValue(DaoUtility.createText(resolver.getPropertyValue(mrconso)));
		
		if(this.qualifierResolvers != null){
			prop.setPropertyQualifier(buildPropertyQualifiers(mrconso).toArray(new PropertyQualifier[0]));
		}

		return prop;
		
	}
	
	protected Presentation buildPresentation(Mrconso mrconso, boolean preferred){
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
			pres.setPropertyQualifier(buildPropertyQualifiers(mrconso).toArray(new PropertyQualifier[0]));
		}

		return pres;
	}
	
	protected List<PropertyQualifier> buildPropertyQualifiers(Mrconso mrconso){
		List<PropertyQualifier> returnList = new ArrayList<PropertyQualifier>();
		
		for(QualifierResolver<Mrconso> qualResolver : this.qualifierResolvers){
			PropertyQualifier qual = new PropertyQualifier();
			qual.setPropertyQualifierName(qualResolver.getQualifierName());
			qual.setValue(DaoUtility.createText(qualResolver.getQualifierValue(mrconso)));
			
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
}
