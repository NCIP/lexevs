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
package org.cts2.castor.printer.modifiers;

import org.cts2.castor.printer.JClassModifier;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JType;

/**
 * The Class AssociationModifier.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationModifier implements JClassModifier {

	/* (non-Javadoc)
	 * @see org.cts2.castor.printer.JClassModifier#prePrintModifyJClass(org.exolab.javasource.JClass, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void prePrintModifyJClass(
			JClass jClass, 
			String outputDir,
			String lineSeparator, 
			String header) {
		JType jtype = new SimpleJType("java.lang.String");
		JMethod method = new JMethod("getEntryID", jtype, null);
		method.setSourceCode("return this.getExternalStatementId();");
		
		jClass.addMethod(method);
	}

	/* (non-Javadoc)
	 * @see org.cts2.castor.printer.JClassModifier#postPrintModifyJClass(org.exolab.javasource.JClass, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void postPrintModifyJClass(JClass jClass, String outputDir,
			String lineSeparator, String header) {
		//
	}

	/* (non-Javadoc)
	 * @see org.cts2.castor.printer.JClassModifier#isMatch(org.exolab.javasource.JClass)
	 */
	@Override
	public boolean isMatch(JClass jClass) {
		return jClass.getName().equals("org.cts2.association.Association");
	}
}
