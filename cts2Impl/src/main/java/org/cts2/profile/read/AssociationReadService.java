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
package org.cts2.profile.read;

import org.cts2.association.Association;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

/**
 * Interface AssociationReadService
 * 
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 * 
 */
public interface AssociationReadService extends BaseReadService<Association> {

	/**
	 * Determine if the specified association exists on the service.
	 * 
	 * @param entryID
	 * @param context
	 * @return boolean
	 */
	public boolean exists(NameOrURI entryID, ReadContext context);

	/**
	 * Determine if the association specified by external statement identifier
	 * exists on the service.
	 * 
	 * @param assertingCodeSystemVersion
	 * @param externalStatementId
	 * @param context
	 * @return boolean
	 */
	public boolean existsByExternalStatementId(
			NameOrURI assertingCodeSystemVersion, String externalStatementId,
			ReadContext context);

	/**
	 * Retrieve the specified association from the service.
	 * 
	 * @param entryID
	 * @param queryControl
	 * @param context
	 */
	public Association read(NameOrURI entryID, QueryControl queryControl,
			ReadContext context);

	/**
	 * Retrieve the specified association by the external statement identifier.
	 * 
	 * @param assertingCodeSystemVersion
	 * @param externalStatementId
	 * @param queryControl
	 * @param context
	 * @return Association
	 */
	public Association readByExternalStatementId(
			NameOrURI assertingCodeSystemVersion, String externalStatementId,
			QueryControl queryControl, ReadContext context);
}
