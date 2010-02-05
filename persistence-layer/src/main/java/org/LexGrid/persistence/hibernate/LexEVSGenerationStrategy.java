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
package org.LexGrid.persistence.hibernate;

import java.util.List;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

/**
 * Used in Hibernate Java bean generation.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEVSGenerationStrategy extends DelegatingReverseEngineeringStrategy {

	/**
	 * Instantiates a new lex evs generation strategy.
	 * 
	 * @param delegate the delegate
	 */
	public LexEVSGenerationStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy#excludeForeignKeyAsCollection(java.lang.String, org.hibernate.cfg.reveng.TableIdentifier, java.util.List, org.hibernate.cfg.reveng.TableIdentifier, java.util.List)
	 */
	@Override
	public boolean excludeForeignKeyAsCollection(String keyname,
			TableIdentifier fromTable, List fromColumns,
			TableIdentifier referencedTable, List referencedColumns) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy#excludeForeignKeyAsManytoOne(java.lang.String, org.hibernate.cfg.reveng.TableIdentifier, java.util.List, org.hibernate.cfg.reveng.TableIdentifier, java.util.List)
	 */
	@Override
	public boolean excludeForeignKeyAsManytoOne(String keyname,
			TableIdentifier fromTable, List fromColumns,
			TableIdentifier referencedTable, List referencedColumns) {
		return true;
	}
	
	
}
