/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.profile.query;

import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public interface AssociationQuery extends BaseQueryService<AssociationDirectoryURI> {

	/**
	 * @return All associations in the Code System.
	 */
	public AssociationDirectoryURI getAllAssociations();
	
	public AssociationDirectory resolve(AssociationDirectoryURI codeSystemQueryURI, QueryControl queryControl, ReadContext readContext);
	
	public AssociationList resolveAsList(AssociationDirectoryURI codeSystemQueryURI, QueryControl queryControl, ReadContext readContext);
}
