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
package org.lexevs.cts2.admin;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.versions.Revision;

public interface CodeSystemAdminOperation {
	
	/**
	 * Installs a code system (aka terminology) into the terminology service 
	 * for subsequent access by other service functions. This operation is used 
	 * for the initial install of the overall terminology structure itself. 
	 * This may include the full set of concepts, relationships and so on, or 
	 * some of these elements may be loaded using the Import Code System Revision 
	 * operation. The actual contents may be supplied by value or reference, 
	 * i.e. as a complete set of explicit content or as a reference to a location 
	 * where the content can be separately obtained for loading. 
	 * @return
	 * @throws 
	 */
	public int importCodeSystem() throws LBException;
	
	/**
	 * Installs either an entire new version or the necessary revision updates 
	 * for an already loaded code system (terminology) into the terminology server 
	 * repository (content included by value or by reference to a location). 
	 * Includes indicator as to whether intent is to replace whole code system or 
	 * just replace some elements (codes, associations etc).
	 * 
	 * @param codeSystemRevision
	 * @return
	 * @throws LBException
	 */
	public int importCodeSystemRevsion(Revision codeSystemRevision) throws LBException;
	
	public int importCodeSystemRevsion(String xmlFileLocation) throws LBException;
	
	public void exportCodeSystemContent() throws LBException;
	
	public void changeCodeSystemStatus()throws LBException;
	
	
	
}
