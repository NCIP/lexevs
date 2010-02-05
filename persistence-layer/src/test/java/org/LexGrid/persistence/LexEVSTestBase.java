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
package org.LexGrid.persistence;

import java.util.Properties;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo;
import org.LexGrid.persistence.connection.LexEvsPersistenceConnectionManager;
import org.LexGrid.persistence.properties.PropertiesFactory;
import org.LexGrid.persistence.spring.DynamicPropertyApplicationContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import static org.junit.Assert.*;

/**
 * The Class LexEVSTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEVSTestBase {

	/** The connection info. */
	private static SQLConnectionInfo connectionInfo;
	
	/** The ctx. */
	private static ApplicationContext ctx;
	
	/** The connection manager. */
	private static LexEvsPersistenceConnectionManager connectionManager = new LexEvsPersistenceConnectionManager();
	
	/** The properties factory. */
	private static PropertiesFactory propertiesFactory = new PropertiesFactory();
	
	private static String configFile = "src/test/config/config.props";
	/**
	 * Sets the up once.
	 */
	@BeforeClass
	public static void setUpOnce(){
		System.setProperty("LG_CONFIG_FILE", configFile);
		connectionInfo = connectionManager.getNewConnectionInfoForLoad(true);
		Properties props = propertiesFactory.getProperties(connectionInfo);
		ctx = new DynamicPropertyApplicationContext("persistence.xml", props);
	}
	
	/**
	 * Gets the ctx.
	 * 
	 * @return the ctx
	 */
	public ApplicationContext getCtx() {
		return ctx;
	}

	/**
	 * Sets the ctx.
	 * 
	 * @param ctx the new ctx
	 */
	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}

}
