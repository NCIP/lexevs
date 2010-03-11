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
package org.lexevs.dao.database.ibatis.tablemetadata;

import org.lexevs.dao.database.access.tablemetadata.TableMetadataDao;
import org.lexevs.dao.database.ibatis.tablemetadata.parameter.InsertTableMetadataBean;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * The Class IbatisTableMetadataDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IbatisTableMetadataDao extends SqlMapClientDaoSupport implements TableMetadataDao {
	
	/** The prefix resolver. */
	private PrefixResolver prefixResolver;
	
	/**
	 * Insert version and description.
	 * 
	 * @param version the version
	 * @param description the description
	 * 
	 * @return the string
	 */
	public String insertVersionAndDescription(String version, String description) {
		InsertTableMetadataBean bean = new InsertTableMetadataBean();
		bean.setPrefix(prefixResolver.resolveDefaultPrefix());
		bean.setDescription(description);
		bean.setVersion(version);
		
		return (String) this.getSqlMapClientTemplate().insert("insertTableMetadata", bean);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.tablemetadata.TableMetadataDao#getDescription()
	 */
	public String getDescription() {
		return (String) this.getSqlMapClientTemplate().queryForObject("getDescription",
				prefixResolver.resolveDefaultPrefix());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.tablemetadata.TableMetadataDao#getVersion()
	 */
	public String getVersion() {
		return (String) this.getSqlMapClientTemplate().queryForObject("getVersion",
				prefixResolver.resolveDefaultPrefix());
	}

	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */
	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}
}
