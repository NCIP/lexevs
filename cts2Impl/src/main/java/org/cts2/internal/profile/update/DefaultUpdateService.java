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
package org.cts2.internal.profile.update;

import java.util.Date;

import org.cts2.internal.profile.AbstractBaseService;
import org.cts2.profile.update.UpdateService;
import org.cts2.service.core.ChangeSetEntryList;
import org.cts2.service.core.SuccessIndicator;
import org.cts2.service.core.ValidationResponse;
import org.cts2.updates.ChangeSet;

/**
 * The Class DefaultUpdateService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultUpdateService extends AbstractBaseService implements UpdateService{

	/* (non-Javadoc)
	 * @see org.cts2.profile.update.UpdateService#putChangeSet(org.cts2.updates.ChangeSet)
	 */
	@Override
	public SuccessIndicator putChangeSet(ChangeSet changeSet) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.update.UpdateService#validateChangeSet(org.cts2.updates.ChangeSet)
	 */
	@Override
	public ValidationResponse validateChangeSet(ChangeSet changeSet) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.update.UpdateService#listChanges(java.util.Date, java.util.Date)
	 */
	@Override
	public ChangeSetEntryList listChanges(Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
