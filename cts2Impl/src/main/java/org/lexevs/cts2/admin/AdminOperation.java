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
 * LexEVS implementation of CTS2 Administration Operations.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface AdminOperation {
	
	/**
	 * Returns Association Export Operation interface.
	 * 
	 * @return AssociationExportOperation
	 */
	public AssociationExportOperation getAssociationExportOperation();
	
	/**
	 * Returns Association Load Operation interface.
	 * 
	 * @return AssociationLoadOperation
	 */
	public AssociationLoadOperation getAssociationLoadOperation();
	
	/**
	 * Returns Code System Load Operation interface.
	 * 
	 * @return CodeSystemLoadOperation
	 */
	public CodeSystemLoadOperation getCodeSystemLoadOperation();
	
	/**
	 * Returns Code System Export Operation interface.
	 * 
	 * @return CodeSystemExportOperation
	 */
	public CodeSystemExportOperation getCodeSystemExportOperation();
	
	/**
	 * Returns Notification Administration Operation interface.
	 * 
	 * @return NotificationAdminOperation
	 */
	public NotificationAdminOperation getNotificationAdminOperation();
	
	/**
	 * Returns Value Set Load Operation interface.
	 * 
	 * @return ValueSetAdminOperation
	 */
	public ValueSetLoadOperation getValueSetLoadOperation();
	
	/**
	 * Returns Value Set Export Operation interface.
	 * 
	 * @return ValueSetExportOperation
	 */
	public ValueSetExportOperation getValueSetExportOperation();
}
