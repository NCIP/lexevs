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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.cts2.castor.printer.JClassModifier;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JField;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JParameter;
import org.exolab.javasource.JType;

/**
 * The Class DirectoryURIMethodAddingModifier.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DirectoryURIMethodAddingModifier implements JClassModifier{

	/** The Constant DIRECTORY_QUALIFIED_NAME. */
	private static final String DIRECTORY_QUALIFIED_NAME = "org.cts2.core.Directory";

	/** The Constant URI_SUFFIX. */
	private static final String PREV_FIELD = "_prev";
	
	/** The Constant NEXT_FIELD. */
	private static final String NEXT_FIELD = "_next";
	
	/** The Constant PREV. */
	private static final String GET_PREV = "getPrev";
	
	/** The Constant NEXT. */
	private static final String SET_NEXT = "setNext";
	
	/** The Constant PREV. */
	private static final String GET_NEXT = "getNext";
	
	/** The Constant NEXT. */
	private static final String SET_PREV = "setPrev";
	
	/** The GENERICS. */
	private static String GENERICS = "<T>";
	
	private static String DIRECTORY_CLASS_DECLARATION = "public abstract class Directory";
	
	/** The GENERI c_ simpl e_ type. */
	private static SimpleJType GENERIC_SIMPLE_TYPE = new SimpleJType("T");
	
	/* (non-Javadoc)
	 * @see org.cts2.castor.printer.JClassModifier#prePrintModifyJClass(org.exolab.javasource.JClass, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void prePrintModifyJClass(JClass jClass, String outputDir,
			String lineSeparator, String header) {
		jClass.removeConstructor(jClass.getConstructors()[0]);

		this.adjustField(jClass, PREV_FIELD, GENERIC_SIMPLE_TYPE);
		this.adjustField(jClass, NEXT_FIELD, GENERIC_SIMPLE_TYPE);
		
		this.adjustJMethod(jClass, GET_PREV, GENERIC_SIMPLE_TYPE, MethodType.GET);
		this.adjustJMethod(jClass, SET_PREV, GENERIC_SIMPLE_TYPE, MethodType.SET);
		this.adjustJMethod(jClass, GET_NEXT, GENERIC_SIMPLE_TYPE, MethodType.GET);
		this.adjustJMethod(jClass, SET_NEXT, GENERIC_SIMPLE_TYPE, MethodType.SET);
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
	
	/**
	 * The Enum MethodType.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private enum MethodType {
		GET,
		SET}
	
	/**
	 * Adjust j method.
	 *
	 * @param jClass the j class
	 * @param name the name
	 * @param jType the j type
	 * @param methodType the method type
	 */
	protected void adjustJMethod(JClass jClass, String name, JType jType, MethodType methodType){
		JMethod oldMethod = jClass.getMethod(name, 0);
		
		JMethod newMethod;
		if(methodType.equals(MethodType.GET)){
			newMethod = new JMethod(name, jType, null);
		} else {
			newMethod = new JMethod(name);
			for(JParameter oldParam : oldMethod.getParameters()){
				if(oldParam != null){
					JParameter newParam = new JParameter(jType, oldParam.getName());
					newMethod.getSignature().addParameter(newParam);
					break;
				}
			}
		}

		newMethod.setSourceCode(oldMethod.getSourceCode());
	
		jClass.removeMethod(oldMethod);
		jClass.addMethod(newMethod);
	}
	
	/**
	 * The Class SimpleJType.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private static class SimpleJType extends JType{

		/**
		 * Instantiates a new simple j type.
		 *
		 * @param name the name
		 */
		public SimpleJType(String name) {
			super(name);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString(){
			return this.getName();
		}
		
	}

	/**
	 * Adds the generics.
	 *
	 * @param name the name
	 * @return the string
	 */
	protected String addGenerics(String name){
		return name + GENERICS;
	}
		
	/**
	 * Removes the generics.
	 *
	 * @param name the name
	 * @return the string
	 */
	protected String removeGenerics(String name){
		return name.replaceAll(GENERICS, "");
	}

	/* (non-Javadoc)
	 * @see org.cts2.castor.printer.JClassModifier#postPrintModifyJClass(org.exolab.javasource.JClass, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void postPrintModifyJClass(JClass jClass, String outputDir,
			String lineSeparator, String header) {

		try {
			File file = new File(jClass.getFilename(outputDir));

			String originalFile = FileUtils.readFileToString(file);
			
			String modifiedFile = 
				originalFile.replaceFirst(DIRECTORY_CLASS_DECLARATION, addGenerics(DIRECTORY_CLASS_DECLARATION));
			
			FileUtils.writeStringToFile(file, modifiedFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.cts2.castor.JClassModifier#isMatch(org.exolab.javasource.JClass)
	 */
	@Override
	public boolean isMatch(JClass jClass) {
		return jClass.getName().equals(DIRECTORY_QUALIFIED_NAME) ||
			jClass.getName().equals(DIRECTORY_QUALIFIED_NAME + GENERICS);
	}
}
