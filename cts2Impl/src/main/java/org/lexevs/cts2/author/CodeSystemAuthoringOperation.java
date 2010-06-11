/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.author;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.versions.Revision;

public interface CodeSystemAuthoringOperation {
	/**
	 * 
	 * @param csURIAndVersion
	 * @param codeSystemProperties
	 * @return
	 * @throws LBException
	 */
	public CodingScheme createCodeSystem(AbsoluteCodingSchemeVersionReference csURIAndVersion, Properties codeSystemProperties) throws LBException;
	
	public int commitCodeSystem(CodingScheme codeSystem, URI metaMata, Boolean stopOnErrors, Boolean async) throws LBException;
	
	public Revision createCodeSystemChangeSet(String agent, String changeInstruction);
	
	public int commitChangeSet(Revision changeSet);
	
	public void updateCodeSystemVersion();
	public void updateCodeSystemVersionStatus();
	public void createCodeSystemSuppliment();
	public void updateCodeSystemSuppliment();
	public void createConcept();
	public void updateConcept();
	public void updateConceptStatus();
	public void createAssociationType();
	public void updateAssociationType();
}
