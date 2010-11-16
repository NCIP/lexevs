/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexgrid.valuesets.helper.compiler;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 * The Class CachingValueSetDefinitionCompilerDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractCachingValueSetDefinitionCompilerDecorator implements ValueSetDefinitionCompiler {
	
	/** The delegate. */
	private ValueSetDefinitionCompiler delegate;

	/**
	 * Instantiates a new caching value set definition compiler decorator.
	 * 
	 * @param delegate the delegate
	 */
	public AbstractCachingValueSetDefinitionCompilerDecorator(ValueSetDefinitionCompiler delegate){
		this.delegate = delegate;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.valuesets.helper.compiler.ValueSetDefinitionCompiler#compileValueSetDefinition(org.LexGrid.valueSets.ValueSetDefinition, java.util.HashMap, java.lang.String)
	 */
	public CodedNodeSet compileValueSetDefinition(
			final ValueSetDefinition vdd, HashMap<String, String> refVersions, 
			String versionTag) {

		HashCountingFieldCallback callback = 
			new HashCountingFieldCallback(vdd);
		ReflectionUtils.doWithFields(vdd.getClass(), callback);
		
		int uuid = callback.getComputedHashCode();
		
		if(refVersions != null){
			uuid =+ refVersions.hashCode();
		}
		
		if(versionTag != null){
			uuid =+ versionTag.hashCode();
		}
		
		try {
			CodedNodeSet cns = this.retrieveCodedNodeSet(uuid);
			
			if(cns != null) {
				return cns;
			} else {
				cns = this.delegate.compileValueSetDefinition(vdd, refVersions, versionTag);
				
				this.persistCodedNodeSet(uuid, cns);
				
				return cns;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The Class HashCountingFieldCallback.
	 */
	private static class HashCountingFieldCallback implements FieldCallback {

		/** The obj. */
		private Object obj;
		
		/**
		 * Instantiates a new hash counting field callback.
		 * 
		 * @param obj the obj
		 */
		private HashCountingFieldCallback(Object obj){
			this.obj = obj;
		}
		
		/** The computed hash code. */
		int computedHashCode = 0;
		
		/* (non-Javadoc)
		 * @see org.springframework.util.ReflectionUtils.FieldCallback#doWith(java.lang.reflect.Field)
		 */
		@Override
		public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
			field.setAccessible(true);
			Object value = field.get(obj);

			if(value == null) {
				return;
			}

			if(value instanceof String ||
					ClassUtils.isPrimitiveOrWrapper(value.getClass()) || 
					value.getClass().isEnum()) {
				
				if(value.getClass().isEnum()) {
					computedHashCode += ((Enum<?>)value).toString().hashCode();
				} else {
					computedHashCode += value.hashCode();
				}
			} else {
				if(value instanceof Collection<?>) {
					for(Object o : (Collection<?>)value) {
						computedHashCode += recurse(o);
					}
				} else {
					computedHashCode += recurse(value);
				}
			}
		}

		/**
		 * Recurse.
		 * 
		 * @param o the o
		 * 
		 * @return the int
		 */
		private int recurse(Object o) {
			HashCountingFieldCallback callback = 
				new HashCountingFieldCallback(o);

			ReflectionUtils.doWithFields(o.getClass(), callback);
			return callback.getComputedHashCode();
		}

		/**
		 * Gets the computed hash code.
		 * 
		 * @return the computed hash code
		 */
		private int getComputedHashCode() {
			return computedHashCode;
		}		
	}
	
	/**
	 * Persist coded node set.
	 * 
	 * @param uuid the uuid
	 * @param cns the cns
	 * 
	 * @throws Exception the exception
	 */
	protected abstract void persistCodedNodeSet(int uuid, CodedNodeSet cns);
	

	/**
	 * Retrieve coded node set.
	 * 
	 * @param uuid the uuid
	 * 
	 * @return the coded node set
	 * 
	 * @throws Exception the exception
	 */
	protected abstract CodedNodeSet retrieveCodedNodeSet(int uuid) ;
    
}
