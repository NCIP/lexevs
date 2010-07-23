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

import org.lexevs.cts2.BaseService;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.admin.export.AssociationExportOperation;
import org.lexevs.cts2.admin.export.CodeSystemExportOperation;
import org.lexevs.cts2.admin.export.CodeSystemExportOperationImpl;
import org.lexevs.cts2.admin.export.ValueSetExportOperation;
import org.lexevs.cts2.admin.load.AssociationLoadOperation;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperation;
import org.lexevs.cts2.admin.load.CodeSystemLoadOperationImpl;
import org.lexevs.cts2.admin.load.ValueSetLoadOperation;
import org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl;

/**
 * Class returns individual CTS 2 Administration Operation interfaces.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 *
 */
public class AdminOperationImpl extends BaseService implements AdminOperation {
	private CodeSystemLoadOperation csLoadOp_;
	private CodeSystemExportOperation csExportOp_;
	private ValueSetLoadOperation vsLoadOp_;
	private NotificationAdminOperation notificationAdminOp_;
	
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
		if (csExportOp_ == null)
			csExportOp_ = new CodeSystemExportOperationImpl();
		
		return csExportOp_;
	}

	@Override
	public CodeSystemLoadOperation getCodeSystemLoadOperation() {
		if (csLoadOp_ == null)
			csLoadOp_ = new CodeSystemLoadOperationImpl();
		
		return csLoadOp_;
	}

	@Override
	public NotificationAdminOperation getNotificationAdminOperation() {
		if(this.notificationAdminOp_ == null) {
			notificationAdminOp_ = new NotificationAdminOperationImpl();
		}
		return this.notificationAdminOp_;
	}

	@Override
	public ValueSetExportOperation getValueSetExportOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueSetLoadOperation getValueSetLoadOperation() {
		if (vsLoadOp_ == null)
			vsLoadOp_ = new ValueSetLoadOperationImpl();
		
		return vsLoadOp_;
	}

}
