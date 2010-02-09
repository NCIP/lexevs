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
package org.lexevs.dao.database.setup.script;

import org.lexevs.dao.database.type.DatabaseType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * A factory for creating Script objects.
 */
public class ScriptFactory implements InitializingBean, FactoryBean{

	/**
	 * The Enum ScriptType.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum ScriptType {
		create, /** The create. */
		drop    /** The drop. */
		};
	
	/** The creation script prefix. */
	private String creationScriptPrefix = "schema-";
	
	/** The drop script prefix. */
	private String dropScriptPrefix = "schema-drop-";
	
	/** The scriptsuffix. */
	private String scriptsuffix = ".sql";
	
	/** The return resource. */
	private Resource returnResource;
	
	/** The script type. */
	private ScriptType scriptType;
	
	/** The database type. */
	private DatabaseType databaseType;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return returnResource;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Resource.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(scriptType, "Must specify a Script Type.");
		Assert.notNull(databaseType, "Must specify a Database Type.");
		
		
		String dbType = null;
		if(databaseType.equals(DatabaseType.MYSQL)){
			dbType = "mysql";
		} else if (databaseType.equals(DatabaseType.HSQL)){
			dbType = "hsqldb";
		} else if (databaseType.equals(DatabaseType.ORACLE)){
			dbType = "oracle";
		} else if (databaseType.equals(DatabaseType.DB2)){
			dbType = "db2";
		} else if (databaseType.equals(DatabaseType.POSTGRES)){
			dbType = "postgresql";
		} else {
			throw new RuntimeException("Database: " + databaseType.toString() + " is not supported.");
		}
		
		String prefix = null;
		if(scriptType.equals(ScriptType.create)){
			prefix = creationScriptPrefix;
		} else if(scriptType.equals(ScriptType.drop)){
			prefix = dropScriptPrefix;
		}
		returnResource = new ClassPathResource(prefix + dbType + scriptsuffix);
	}

	/**
	 * Gets the script type.
	 * 
	 * @return the script type
	 */
	public ScriptType getScriptType() {
		return scriptType;
	}

	/**
	 * Sets the script type.
	 * 
	 * @param scriptType the new script type
	 */
	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	/**
	 * Gets the database type.
	 * 
	 * @return the database type
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	/**
	 * Sets the database type.
	 * 
	 * @param databaseType the new database type
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * Gets the creation script prefix.
	 * 
	 * @return the creation script prefix
	 */
	public String getCreationScriptPrefix() {
		return creationScriptPrefix;
	}

	/**
	 * Sets the creation script prefix.
	 * 
	 * @param creationScriptPrefix the new creation script prefix
	 */
	public void setCreationScriptPrefix(String creationScriptPrefix) {
		this.creationScriptPrefix = creationScriptPrefix;
	}

	/**
	 * Gets the drop script prefix.
	 * 
	 * @return the drop script prefix
	 */
	public String getDropScriptPrefix() {
		return dropScriptPrefix;
	}

	/**
	 * Sets the drop script prefix.
	 * 
	 * @param dropScriptPrefix the new drop script prefix
	 */
	public void setDropScriptPrefix(String dropScriptPrefix) {
		this.dropScriptPrefix = dropScriptPrefix;
	}

	/**
	 * Gets the scriptsuffix.
	 * 
	 * @return the scriptsuffix
	 */
	public String getScriptsuffix() {
		return scriptsuffix;
	}

	/**
	 * Sets the scriptsuffix.
	 * 
	 * @param scriptsuffix the new scriptsuffix
	 */
	public void setScriptsuffix(String scriptsuffix) {
		this.scriptsuffix = scriptsuffix;
	}
}
