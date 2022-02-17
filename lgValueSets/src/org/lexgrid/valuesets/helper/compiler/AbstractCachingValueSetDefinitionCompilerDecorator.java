
package org.lexgrid.valuesets.helper.compiler;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.valueSets.CodingSchemeReference;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.lexgrid.valuesets.helper.VSDServiceHelper;
import org.lexgrid.valuesets.helper.ValueSetResolutionMD5Generator;
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
	
	private VSDServiceHelper vsdServiceHelper;
	
	private static String NULL_STRING = "NULL";
	
	private static int NULL_STRING_HASH_CODE = NULL_STRING.hashCode();

	/**
	 * Instantiates a new caching value set definition compiler decorator.
	 * 
	 * @param delegate the delegate
	 */
	public AbstractCachingValueSetDefinitionCompilerDecorator(ValueSetDefinitionCompiler delegate, VSDServiceHelper vsdServiceHelper){
		this.delegate = delegate;
		this.vsdServiceHelper = vsdServiceHelper;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.valuesets.helper.compiler.ValueSetDefinitionCompiler#compileValueSetDefinition(org.LexGrid.valueSets.ValueSetDefinition, java.util.HashMap, java.lang.String, java.util.HashMap)
	 */
	public CodedNodeSet compileValueSetDefinition(
			final ValueSetDefinition vdd, HashMap<String, String> refVersions, 
			String versionTag, HashMap<String, ValueSetDefinition> referencedVSDs) {
		
	    try {
		    
		    populateRefVersions(vdd, refVersions, versionTag);
		    
    		ValueSetResolutionMD5Generator vsrg= new ValueSetResolutionMD5Generator( vdd,
    				 refVersions,  versionTag, referencedVSDs);
    		String md5= vsrg.generateMD5();
		
			CodedNodeSet cns = this.retrieveCodedNodeSet(md5);
			
			if(cns != null) {
				return cns;
			} 
			else {
				cns = this.delegate.compileValueSetDefinition(vdd, refVersions, versionTag, referencedVSDs);
				this.persistCodedNodeSet(md5, cns);		
				return cns;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void populateRefVersions(ValueSetDefinition vdd, HashMap<String, String> refVersions, 
			String versionTag) throws LBException {
		for(DefinitionEntry definitionEntry : vdd.getDefinitionEntry()){
			EntityReference entityRef = definitionEntry.getEntityReference();
			
			if(entityRef != null){
				// Locate the coding scheme namespace
				String entityCodeCodingScheme = vsdServiceHelper.getCodingSchemeNameForNamespaceName(vdd.getMappings(), entityRef.getEntityCodeNamespace());
				if(StringUtils.isEmpty(entityCodeCodingScheme)) {
					entityCodeCodingScheme = vdd.getDefaultCodingScheme();
				}
				if(StringUtils.isNotEmpty(entityCodeCodingScheme)) {
					vsdServiceHelper.resolveCSVersion(entityCodeCodingScheme, vdd.getMappings(), versionTag, refVersions);
				}
			}

			PropertyReference propertyRef = definitionEntry.getPropertyReference();
			if(propertyRef != null){
				vsdServiceHelper.resolveCSVersion(propertyRef.getCodingScheme(), vdd.getMappings(), versionTag, refVersions);
			}

			CodingSchemeReference codingSchemeRef = definitionEntry.getCodingSchemeReference();
			if(codingSchemeRef != null){
				String csName = codingSchemeRef.getCodingScheme();

				if(StringUtils.isEmpty(csName))
					csName = vdd.getDefaultCodingScheme();
				if(!StringUtils.isEmpty(csName)) {
					vsdServiceHelper.resolveCSVersion(csName, vdd.getMappings(), versionTag, refVersions);
				}
			}
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
					value instanceof Date ||
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
	protected abstract void persistCodedNodeSet(String uuid, CodedNodeSet cns);
	

	/**
	 * Retrieve coded node set.
	 * 
	 * @param uuid the uuid
	 * 
	 * @return the coded node set
	 * 
	 * @throws Exception the exception
	 */
	protected abstract CodedNodeSet retrieveCodedNodeSet(String uuid) ;
    
}