package org.lexgrid.loader.rrf.processor;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.HighestRankingListProcessor;
import org.lexgrid.loader.processor.support.PropertyResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;

public class MrconsoGroupEntityProcessor extends HighestRankingListProcessor<Mrconso,CodingSchemeIdHolder<Entity>>{

	public PropertyResolver<Mrconso> propertyResolver;
	
	public List<QualifierResolver<Mrconso>> qualifierResolvers;
	
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
		
		return entityHolder;
	}
	
	protected Presentation buildPresentation(Mrconso mrconso, boolean preferred){
		Presentation pres = new Presentation();
		pres.setPropertyId(propertyResolver.getId(mrconso));
		pres.setDegreeOfFidelity(propertyResolver.getDegreeOfFidelity(mrconso));
		pres.setLanguage(propertyResolver.getLanguage(mrconso));
		pres.setMatchIfNoContext(propertyResolver.getMatchIfNoContext(mrconso));
		pres.setPropertyName(propertyResolver.getPropertyName(mrconso));
		pres.setPropertyType(propertyResolver.getPropertyType(mrconso));
		pres.setValue(DaoUtility.createText(propertyResolver.getPropertyValue(mrconso)));
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
	

	public PropertyResolver<Mrconso> getPropertyResolver() {
		return propertyResolver;
	}

	public void setPropertyResolver(PropertyResolver<Mrconso> propertyResolver) {
		this.propertyResolver = propertyResolver;
	}
}
