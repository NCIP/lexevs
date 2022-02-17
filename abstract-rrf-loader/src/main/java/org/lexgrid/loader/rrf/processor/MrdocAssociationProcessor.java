
package org.lexgrid.loader.rrf.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.CodingSchemeIdAwareProcessor;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrdoc;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.Assert;

/**
 * The Class MrdocAssociationProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdocAssociationProcessor extends CodingSchemeIdAwareProcessor implements ItemProcessor<List<Mrdoc>,List<CodingSchemeIdHolder<AssociationEntity>>> {
	
	/** The supported attribute template. */
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<CodingSchemeIdHolder<AssociationEntity>> process(List<Mrdoc> items) throws Exception {
		List<CodingSchemeIdHolder<AssociationEntity>> returnList = new ArrayList<CodingSchemeIdHolder<AssociationEntity>>();
	
		String relationName = getRelationName(items);
		String reverseName = getReverseName(items);
		String expandedName = getExpandedName(items);
		
		CodingScheme cs = LexEvsServiceLocator.getInstance().
			getDatabaseServiceManager().
				getCodingSchemeService().
					getCodingSchemeByUriAndVersion(
							this.getCodingSchemeIdSetter().getCodingSchemeUri(),
							this.getCodingSchemeIdSetter().getCodingSchemeVersion());
		
		Set<String> loadedAssociations = new HashSet<String>();
		
		for(Relations relations : cs.getRelations()) {
			for(AssociationPredicate predicate : relations.getAssociationPredicate()) {
				loadedAssociations.add(predicate.getAssociationName());
			}
		}
		
		if(loadedAssociations.contains(relationName)) {
			supportedAttributeTemplate.addSupportedAssociation(
					getCodingSchemeIdSetter().getCodingSchemeUri(), 
					getCodingSchemeIdSetter().getCodingSchemeVersion(), 
					relationName, 
					relationName, 
					expandedName);
			returnList.add(
					new CodingSchemeIdHolder<AssociationEntity>(
							this.getCodingSchemeIdSetter(), 
							buildAssociationEntity(relationName, reverseName, expandedName)));
		}
		
		return returnList;
	}
	
	/**
	 * Builds the association.
	 * 
	 * @param containerName the container name
	 * @param relationName the relation name
	 * @param reverseName the reverse name
	 * @param expandedName the expanded name
	 * 
	 * @return the association
	 */
	protected AssociationEntity buildAssociationEntity(String relationName, String reverseName, String expandedName){
		AssociationEntity assoc = EntityFactory.createAssociation();
		assoc.setForwardName(relationName);
		assoc.setReverseName(reverseName);
		assoc.setIsTransitive(isTransitive(relationName));
		assoc.setIsNavigable(true);
		assoc.setEntityDescription(Constructors.createEntityDescription(expandedName));
		assoc.setEntityCode(relationName);
		return assoc;
	}
	
	/**
	 * Checks if is transitive.
	 * 
	 * @param relationName the relation name
	 * 
	 * @return true, if is transitive
	 */
	protected boolean isTransitive(String relationName){
		return RrfLoaderConstants.TRANSITIVE_ASSOCIATIONS.contains(relationName);
	}
	
	/**
	 * Gets the expanded name.
	 * 
	 * @param items the items
	 * 
	 * @return the expanded name
	 */
	private String getExpandedName(List<Mrdoc> items){
		for(Mrdoc mrdoc : items){
			if(mrdoc.getType().equals(RrfLoaderConstants.RELATION_EXPANDED_FORM)){
				return mrdoc.getExpl();
			}
		}
		return null;
	}
	
	/**
	 * Gets the reverse name.
	 * 
	 * @param items the items
	 * 
	 * @return the reverse name
	 */
	private String getReverseName(List<Mrdoc> items){
		for(Mrdoc mrdoc : items){
			if(mrdoc.getType().endsWith(RrfLoaderConstants.RELATION_INVERSE)){
				return mrdoc.getExpl();
			}
		}
		return null;
	}
	
	/**
	 * Gets the relation name.
	 * 
	 * @param items the items
	 * 
	 * @return the relation name
	 */
	private String getRelationName(List<Mrdoc> items){
		Assert.notEmpty(items);
		return items.get(0).getValue();
	}

	/**
	 * Gets the supported attribute template.
	 * 
	 * @return the supported attribute template
	 */
	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}

	/**
	 * Sets the supported attribute template.
	 * 
	 * @param supportedAttributeTemplate the new supported attribute template
	 */
	public void setSupportedAttributeTemplate(
			SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}
}