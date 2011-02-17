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
import org.cts2.codesystem.CodeSystemDirectory;
import org.cts2.uri.DirectoryURI;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public interface AssociationQuery extends BaseQueryService<AssociationDirectory> {


	/**
	 * @return All associations in the Code System.
	 */
	public DirectoryURI<AssociationDirectory> getAllAssociations();
	
	/**
	 *  Resolve as list.
	 * @param directoryUri
	 * @return the list of Associations
	 */
	public AssociationList resolveAsList(DirectoryURI<AssociationDirectory> directoryUri);
}
