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

/**
 * LexEVS implementation of CTS2 Administration Operations.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface AdminOperation {
	/**
	 * Returns Association Administration Operation interface.
	 * 
	 * @return AssociationAdminOperation
	 */
	public AssociationAdminOperation getAssociationAdminOperation();
	
	/**
	 * Returns Code System Administration Operation interface.
	 * 
	 * @return CodeSystemAdminOperation
	 */
	public CodeSystemAdminOperation getCodeSystemAdminOperation();
	
	/**
	 * Returns Notification Administration Operation interface.
	 * 
	 * @return NotificationAdminOperation
	 */
	public NotificationAdminOperation getNotificationAdminOperation();
	
	/**
	 * Returns Value Set Administration Operation interface.
	 * 
	 * @return ValueSetAdminOperation
	 */
	public ValueSetAdminOperation getValueSetAdminOperation();
}
