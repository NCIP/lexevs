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

import org.lexevs.cts2.admin.export.AssociationExportOperation;
import org.lexevs.cts2.admin.export.CodeSystemExportOperation;
import org.lexevs.cts2.admin.export.ValueSetExportOperation;
import org.lexevs.cts2.admin.load.AssociationLoadOperation;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.admin.load.ValueSetLoadOperation;

/**
 * Class returns individual CTS 2 Administration Operation interfaces.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 *
 */
public class AdminOperationImpl implements AdminOperation {
	
	@Override
	public AssociationExportOperation getAssociationExportOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationLoadOperation getAssociationLoadOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSystemExportOperation getCodeSystemExportOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSystemLoadOperation getCodeSystemLoadOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NotificationAdminOperation getNotificationAdminOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueSetExportOperation getValueSetExportOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueSetLoadOperation getValueSetLoadOperation() {
		// TODO Auto-generated method stub
		return null;
	}

}
