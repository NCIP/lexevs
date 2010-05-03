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
package org.lexevs.dao.database.sqlimplementedmethods.codednodeset;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class SIMCodedNodeSetDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SIMCodedNodeSetDao extends AbstractBaseDao implements CodedNodeSetDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("1.8");

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao#buildCodedEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.lexevs.dao.database.access.property.PropertyDao.PropertyType[])
	 */
	public Entity buildCodedEntry(String codingSchemeName, String version,
			String code, String namespace, LocalNameList restrictToProperties,
			PropertyType[] restrictToPropertyTypes) {
		try {
			return null;
			//return SQLImplementedMethods.buildCodedEntry(codingSchemeName, version, code, namespace, restrictToProperties, restrictToPropertyTypes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao#getCodingScheme(java.lang.String, java.lang.String)
	 */
	public CodingScheme getCodingScheme(String codingSchemeName,
			String versionString) {
		try {
			return null;
			//return SQLImplementedMethods.buildCodingScheme(codingSchemeName, versionString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao#getCodingSchemeCopyright(java.lang.String, java.lang.String)
	 */
	public String getCodingSchemeCopyright(String codingSchemeName,
			String tagOrVersion) {
		try {
			return null;
			//return SQLImplementedMethods.getCodingSchemeCopyright(codingSchemeName, tagOrVersion);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao#buildCodedEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.lexevs.dao.database.access.property.PropertyDao.PropertyType[])
	 */
	public Entity buildCodedEntry(
			String codingSchemeName,
			String version,
			String code,
			String namespace,
			LocalNameList restrictToProperties,
			org.lexevs.dao.database.access.property.PropertyDao.PropertyType[] restrictToPropertyTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao#executeInTransaction(org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao.IndividualDaoCallback)
	 */
	public <T> T executeInTransaction(IndividualDaoCallback<T> callback) {
		throw new UnsupportedOperationException();
	}
}
