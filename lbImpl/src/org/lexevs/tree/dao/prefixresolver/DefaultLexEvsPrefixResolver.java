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
package org.lexevs.tree.dao.prefixresolver;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

/**
 * The Class DefaultLexEvsPrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class DefaultLexEvsPrefixResolver implements PrefixResolver {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8005105567332097673L;

	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.prefixresolver.PrefixResolver#getPrefix(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	public String getPrefix(String codingSchemeName, CodingSchemeVersionOrTag tagOrVersion) throws Exception {
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
	
		String version = ServiceUtility.getVersion(codingSchemeName, tagOrVersion);
		String codingSchemeUri = systemResourceService.getUriForUserCodingSchemeName(codingSchemeName, version);
                    
		return LexEvsServiceLocator.getInstance().
			getLexEvsDatabaseOperations().getPrefixResolver().
				resolvePrefixForCodingScheme(codingSchemeUri, version);
	}
}
