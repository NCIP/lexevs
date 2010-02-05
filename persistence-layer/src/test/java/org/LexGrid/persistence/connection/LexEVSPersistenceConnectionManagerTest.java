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
package org.LexGrid.persistence.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLInterface;
import org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo;
import org.LexGrid.persistence.LexEVSTestBase;
import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.Association;
import org.LexGrid.persistence.model.AssociationId;
import org.LexGrid.persistence.spring.DynamicPropertyApplicationContext;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.hibernate.HibernateException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import static org.junit.Assert.*;

/**
 * The Class LexEVSPersistenceConnectionManagerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEVSPersistenceConnectionManagerTest extends LexEVSTestBase {
	
	/** The dao. */
	private LexEvsDao dao;
	
	/**
	 * Sets the up.
	 */
	public void setUp(){
		dao = (LexEvsDao)getCtx().getBean("lexEvsDao");		
	}
	
	/**
	 * Test get new connection info for load.
	 */
	@Test
	public void testGetNewConnectionInfoForLoad(){
		LexEvsPersistenceConnectionManager manager = new LexEvsPersistenceConnectionManager();
		SQLConnectionInfo info1 = manager.getNewConnectionInfoForLoad(true);
		SQLConnectionInfo info2 = manager.getNewConnectionInfoForLoad(true);
		
		assertTrue(info1.dbName.equals(info2.dbName));	
	}

}
