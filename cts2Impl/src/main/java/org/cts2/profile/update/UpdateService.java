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
package org.cts2.profile.update;

import java.util.Date;

import org.cts2.service.core.ChangeSetEntryList;
import org.cts2.service.core.SuccessIndicator;
import org.cts2.service.core.ValidationResponse;
import org.cts2.updates.ChangeSet;

/**
 * The Interface UpdateService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface UpdateService {

	/**
	 * Put change set.
	 *
	 * @param changeSet the change set
	 * @return the success indicator
	 */
	public SuccessIndicator putChangeSet(ChangeSet changeSet);
	
	/**
	 * Validate change set.
	 *
	 * @param changeSet the change set
	 * @return the validation response
	 */
	public ValidationResponse validateChangeSet(ChangeSet changeSet);
	
	/**
	 * List changes.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the change set entry list
	 */
	public ChangeSetEntryList listChanges(Date fromDate, Date toDate);
}
