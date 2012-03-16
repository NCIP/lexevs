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
package org.lexgrid.loader.rxn.integration.dataload;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;

import util.integration.LoadRxnForIntegration;

/**
 * The Class DataLoadTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataLoadTestBase {
	
	/** The lbs. */
	protected LexBIGService lbs;
	protected CodedNodeSet cns;
	protected CodedNodeGraph cng;
	
	/**
	 * Sets the up lbs.
	 * @throws LBException 
	 */
	@Before
	public void setUpLbs() throws LBException{
		System.setProperty("LG_CONFIG_FILE", LoadRxnForIntegration.CONFIG_FILE);
		
		//Use this for local testing
		//System.setProperty("LG_CONFIG_FILE", "w:/services/lexbig/5_0_1_BatchLoader/resources/config/lbconfig.props");
		
		lbs = LexBIGServiceImpl.defaultInstance();
		cns = lbs.getCodingSchemeConcepts(LoadRxnForIntegration.RXN_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LoadRxnForIntegration.RXN_VERSION));
		cng = lbs.getNodeGraph(LoadRxnForIntegration.RXN_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LoadRxnForIntegration.RXN_VERSION), null);
	}
	
	public CodedNodeSet getCodedNodeSet() throws LBException {
		this.setUpLbs();
		return lbs.getCodingSchemeConcepts(LoadRxnForIntegration.RXN_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LoadRxnForIntegration.RXN_VERSION));
	}
}
