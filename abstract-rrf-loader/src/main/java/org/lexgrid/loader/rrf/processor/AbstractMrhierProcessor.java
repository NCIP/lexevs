
package org.lexgrid.loader.rrf.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.CodingSchemeIdAwareProcessor;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrhier;
import org.lexgrid.loader.rrf.processor.support.HcdQualifierResolver;
import org.lexgrid.loader.wrappers.Triple;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class AbstractMrhierProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractMrhierProcessor extends CodingSchemeIdAwareProcessor implements InitializingBean, ItemProcessor<Mrhier,List<Object>> {

	/** The qualifier resolver. */
	private QualifierResolver<Mrhier> qualifierResolver = new HcdQualifierResolver();
	
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	/*
	 * Register known SupportedAttributes here -- save some processing time instead of having to check every one.
	 */
	public void afterPropertiesSet() throws Exception {
		/*
		if(supportedAttributeTemplate != null){
			supportedAttributeTemplate.addSupportedPropertyQualifier(
					this.getCodingSchemeIdSetter().getCodingSchemeName(),
					RrfLoaderConstants.HCD_QUALIFIER, null, RrfLoaderConstants.HCD_QUALIFIER);
			supportedAttributeTemplate.addSupportedAssociationQualifier(
					this.getCodingSchemeIdSetter().getCodingSchemeName(),
					RrfLoaderConstants.HCD_QUALIFIER, null, RrfLoaderConstants.HCD_QUALIFIER);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<Object> process(Mrhier mrhier) throws Exception {
		/*
		List<EntityAssnsToEquals> assocQuals = new ArrayList<EntityAssnsToEquals>();
		List<EntityPropertyMultiAttrib> propertyQuals = new ArrayList<EntityPropertyMultiAttrib>();
		
		String pathToRoot = mrhier.getPtr();

		List<String> pathToRootCodes = getPathToRoot(pathToRoot);

		String sourceCode = getInitalPathElementCode(mrhier);
		
		int pos = 0;
		
		//Add the Property Qual to the First AUI. Clone it so we make sure we don't
		//have any conflicts.
		propertyQuals.addAll(buildEntityPropertyMultiAttrib(
				mrhier, 
				new String(sourceCode), 
				getPropertyId(mrhier, pos)			
		));
		pos++;
		
		for(String targetCode : pathToRootCodes){
			List<EntityAssnsToEntity> assocs = getLexEvsDao().query(buildEntityAssnsToEntity(sourceCode, targetCode));
			if(assocs.size() == 0){
				assocs = getLexEvsDao().query(buildEntityAssnsToEntity(targetCode, sourceCode));
			}
			assocQuals.addAll(
					buildEntityAssnsToEqualsList(
							mrhier, 
							assocs
			));
			
			propertyQuals.addAll(buildEntityPropertyMultiAttrib(
					mrhier, 
					targetCode, 
					getPropertyId(mrhier, pos)			
			));
			
			sourceCode = targetCode;
			pos++;
		}
		
		ArrayList<Object> returnList = new ArrayList<Object>();
		
		returnList.addAll(
					//this we need to check for duplicates
					removeDuplicatesAssocQuals(assocQuals)
				);
		returnList.addAll(
					//this too, just in case.
					removeDuplicatesProps(propertyQuals)
				);
		
		return returnList;
	}
	
	protected List<EntityAssnsToEquals> removeDuplicatesAssocQuals(List<EntityAssnsToEquals> list){
		List<EntityAssnsToEquals> returnList = new ArrayList<EntityAssnsToEquals>();
		
		List<Triple<String>> uniqueAssocs = new ArrayList<Triple<String>>();
		
		for(EntityAssnsToEquals assocQual : list){
			Triple<String> triple = entityAssnsToEqualsToTriple(assocQual);
			if(!uniqueAssocs.contains(triple)){
				returnList.add(assocQual);
				uniqueAssocs.add(triple);
			}
		}
		
		return returnList;
		*/
		return null;
	}
	/*
	protected List<EntityPropertyMultiAttrib> removeDuplicatesProps(List<EntityPropertyMultiAttrib> list){
		List<EntityPropertyMultiAttrib> returnList = new ArrayList<EntityPropertyMultiAttrib>();
		
		List<Triple<String>> uniquePairs = new ArrayList<Triple<String>>();
		
		for(EntityPropertyMultiAttrib attrib : list){
			Triple<String> triple = entityPropertyMultiAttribToTriple(attrib);
			if(!uniquePairs.contains(triple)){
				returnList.add(attrib);
				uniquePairs.add(triple);
			}
		}
		
		return returnList;
	}
	
	protected Triple<String> entityAssnsToEqualsToTriple(EntityAssnsToEquals qual){
		return new Triple<String>(
				qual.getId().getMultiAttributesKey(),
				qual.getId().getQualifierName(),
				qual.getId().getQualifierValue()
				);
	}
	
	protected Triple<String> entityPropertyMultiAttribToTriple(EntityPropertyMultiAttrib attrib){
		return new Triple<String>(
				attrib.getId().getEntityCode(), 
				attrib.getId().getPropertyId(),
				attrib.getId().getVal1());
	}
	
	protected abstract List<String> getPathToRoot(String pathToRoot);
	
	protected abstract String getInitalPathElementCode(Mrhier mrhier);
	
	protected abstract String getPropertyId(Mrhier mrhier, int pos);
	
	protected String getAuiFromPos(Mrhier mrhier, int pos) {
		if(pos == 0) {
			return mrhier.getAui();
		} else {
			List<String> ptr = this.getPathToRootAuis(mrhier.getPtr());
			return ptr.get(pos - 1);
		}
	}
	
	protected List<EntityAssnsToEquals> buildEntityAssnsToEqualsList(Mrhier mrhier, List<EntityAssnsToEntity> assocs){
		List<EntityAssnsToEquals> quals = new ArrayList<EntityAssnsToEquals>();
		for(EntityAssnsToEntity assoc : assocs){
			quals.add(buildEntityAssnsToEquals(mrhier, assoc));
		}
		return quals;
	}
	
	protected EntityAssnsToEquals buildEntityAssnsToEquals(Mrhier mrhier, EntityAssnsToEntity assoc){
		EntityAssnsToEquals qual = new EntityAssnsToEquals();
		EntityAssnsToEqualsId qualId = new EntityAssnsToEqualsId();
		qualId.setCodingSchemeName(getCodingSchemeIdSetter().getCodingSchemeName());
		qualId.setMultiAttributesKey(assoc.getMultiAttributesKey());
		qualId.setQualifierName(qualifierResolver.getQualifierName());
		qualId.setQualifierValue(qualifierResolver.getQualifierValue(mrhier));
		qual.setId(qualId);
		return qual;
	}
	
	/**
	 * Gets the path to root auis.
	 * 
	 * @param pathToRoot the path to root
	 * 
	 * @return the path to root auis
	 */
	protected List<String> getPathToRootAuis(String pathToRoot){	
		String[] auis = pathToRoot.split("\\.");
		return Arrays.asList(auis);
	}

	/**
	 * Builds the entity assns to entity.
	 * 
	 * @param sourceCode the source code
	 * @param targetCode the target code
	 * 
	 * @return the entity assns to entity
	 */
	
	/*
	protected EntityAssnsToEntity buildEntityAssnsToEntity(String sourceCode, String targetCode){
		EntityAssnsToEntity assoc = new EntityAssnsToEntity();
		assoc.setCodingSchemeName(getCodingSchemeIdSetter().getCodingSchemeName());
		assoc.setSourceEntityCode(sourceCode);
		assoc.setTargetEntityCode(targetCode);
		return assoc;		
	}
	
	protected List<EntityPropertyMultiAttrib> buildEntityPropertyMultiAttrib(Mrhier mrhier, String sourceCode, String propertyId){
		List<EntityPropertyMultiAttrib> returnList = new ArrayList<EntityPropertyMultiAttrib>();
		
		if(propertyMultiAttribResolvers != null) {
			for(MinimalMultiAttribResolver<Mrhier> resolver : propertyMultiAttribResolvers) {
				EntityPropertyMultiAttrib qual = new EntityPropertyMultiAttrib();
				EntityPropertyMultiAttribId qualId = new EntityPropertyMultiAttribId();

				qualId.setCodingSchemeName(getCodingSchemeIdSetter().getCodingSchemeName());
				qualId.setEntityCode(sourceCode);
				qualId.setEntityCodeNamespace(getCodingSchemeIdSetter().getCodingSchemeName());
				qualId.setTypeName(resolver.getTypeName());
				qualId.setPropertyId(propertyId);
				qualId.setVal1(resolver.getVal1(mrhier));
				qual.setVal2(resolver.getVal2(mrhier));
				qualId.setAttributeValue(resolver.getAttributeValue(mrhier));
				qual.setId(qualId);

				returnList.add(qual);
			}
		}
		return returnList;		
	}
	*/
}