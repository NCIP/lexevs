/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexgrid.loader.umls.processor;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.data.DataUtils;
import org.lexgrid.loader.data.property.ParameterizedListIdSetter;
import org.lexgrid.loader.processor.AbstractParameterPassingDoubleListProcessor;
import org.lexgrid.loader.rrf.model.Mrsty;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class MrstyListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrstyListProcessor extends AbstractParameterPassingDoubleListProcessor<Mrsty, ParentIdHolder<Property>>{	
	
	/** The mrconso staging dao. */
	private MrconsoStagingDao mrconsoStagingDao;
	
	/** The sab. */
	private String sab;
	
	/** The parameterizedlist id setter. */
	private ParameterizedListIdSetter<Property> parameterizedlistIdSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.AbstractParameterPassingListProcessor#beforeProcessing(java.util.List)
	 */
	@Override
	protected List<Mrsty> beforeProcessing(List<Mrsty> items) {
		//noop
		return items;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.AbstractParameterPassingListProcessor#afterProcessing(java.util.List, java.util.List)
	 */
	@Override
	protected List<ParentIdHolder<Property>> afterProcessing(List<ParentIdHolder<Property>> processedItems, List<Mrsty> originalItems){
		List<ParentIdHolder<Property>> buffer = new ArrayList<ParentIdHolder<Property>>();

		List<String> codes = mrconsoStagingDao.getCodes(getGroupCui(originalItems), sab);

		for(String code : codes){
			for(ParentIdHolder<Property> prop : processedItems){
				Property clonedProp = null;
				try {
					prop.setItem(DataUtils.deepCloneProperty(prop.getItem()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
				buffer.add(prop);
			}
		}
		//parameterizedlistIdSetter.addIds(buffer, getGroupCui(originalItems));
		return buffer;
	}
	


	/**
	 * Gets the mrconso staging dao.
	 * 
	 * @return the mrconso staging dao
	 */
	public MrconsoStagingDao getMrconsoStagingDao() {
		return mrconsoStagingDao;
	}

	/**
	 * Sets the mrconso staging dao.
	 * 
	 * @param mrconsoStagingDao the new mrconso staging dao
	 */
	public void setMrconsoStagingDao(MrconsoStagingDao mrconsoStagingDao) {
		this.mrconsoStagingDao = mrconsoStagingDao;
	}
	
	/**
	 * Gets the group cui.
	 * 
	 * @param items the items
	 * 
	 * @return the group cui
	 */
	protected String getGroupCui(List<Mrsty> items){
		return items.get(0).getCui();
	}

	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab() {
		return sab;
	}

	/**
	 * Sets the sab.
	 * 
	 * @param sab the new sab
	 */
	public void setSab(String sab) {
		this.sab = sab;
	}
}
