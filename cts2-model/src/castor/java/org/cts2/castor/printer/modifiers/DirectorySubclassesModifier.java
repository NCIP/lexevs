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

import org.apache.commons.lang.StringUtils;
import org.cts2.castor.printer.JClassModifier;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JField;
import org.exolab.javasource.JType;

/**
 * The Class DirectoryURIMethodAddingModifier.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DirectorySubclassesModifier implements JClassModifier{

	/** The Constant DIRECTORY_QUALIFIED_NAME. */
	private static final String DIRECTORY_QUALIFIED_NAME = "org.cts2.core.Directory";
	
	/** The Constant DIRECTORYURI_PACKAGE. */
	private static final String DIRECTORYURI_PACKAGE = "org.cts2.uri";
	
	/** The Constant DIRECTORYURI_SUFFIX. */
	private static final String DIRECTORYURI_SUFFIX = "URI";
	
	/** The Constant GENERIC_REGEX. */
	private static final String GENERIC_REGEX = "<.*>$";
	
	/** The Constant LIST_REGEX. */
	private static final String LIST_REGEX = "List$";
	
	/** The Constant DIRECTORY_SUFFIX. */
	private static final String DIRECTORY_SUFFIX = "Directory";

	/* (non-Javadoc)
	 * @see org.cts2.castor.printer.JClassModifier#prePrintModifyJClass(org.exolab.javasource.JClass, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void prePrintModifyJClass(JClass jClass, String outputDir,
			String lineSeparator, String header) {
		jClass.setSuperClass(jClass.getSuperClassQualifiedName() + "<" + DIRECTORYURI_PACKAGE + "." + jClass.getLocalName().replaceAll(LIST_REGEX, DIRECTORY_SUFFIX) + DIRECTORYURI_SUFFIX + ">");
	}
	
	/**
	 * Adjust field.
	 *
	 * @param jClass the j class
	 * @param name the name
	 * @param type the type
	 */
	protected void adjustField(JClass jClass, String name, JType type){
		jClass.removeField(jClass.getField(name));
		jClass.addField(new JField(type, name));
	}

	/* (non-Javadoc)
	 * @see org.cts2.castor.JClassModifier#isMatch(org.exolab.javasource.JClass)
	 */
	@Override
	public boolean isMatch(JClass jClass) {
		return StringUtils.isNotBlank(jClass.getSuperClassQualifiedName()) &&
			(
			jClass.getSuperClassQualifiedName().matches(DIRECTORY_QUALIFIED_NAME) ||
			jClass.getSuperClassQualifiedName().matches(DIRECTORY_QUALIFIED_NAME + GENERIC_REGEX)
			);
	}

	/* (non-Javadoc)
	 * @see org.cts2.castor.printer.JClassModifier#postPrintModifyJClass(org.exolab.javasource.JClass, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void postPrintModifyJClass(JClass jClass, String outputDir,
			String lineSeparator, String header) {
		jClass.setSuperClass(jClass.getSuperClassQualifiedName().replaceAll(GENERIC_REGEX, ""));
	}
}
