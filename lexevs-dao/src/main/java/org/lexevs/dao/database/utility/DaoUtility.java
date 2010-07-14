/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.utility;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService.Order;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService.Sort;
import org.lexevs.dao.database.service.codednodegraph.model.ColumnSortType;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

/**
 * The Class DaoUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DaoUtility {
	
	public static Map<PropertyType,String> propertyTypeToStringMap = 
		new HashMap<PropertyType,String>();
	static {
		propertyTypeToStringMap.put(PropertyType.COMMENT, SQLTableConstants.TBLCOLVAL_COMMENT);
		propertyTypeToStringMap.put(PropertyType.PRESENTATION, SQLTableConstants.TBLCOLVAL_PRESENTATION);
		propertyTypeToStringMap.put(PropertyType.DEFINITION, SQLTableConstants.TBLCOLVAL_DEFINITION);
		propertyTypeToStringMap.put(PropertyType.GENERIC, SQLTableConstants.TBLCOLVAL_PROPERTY);
	}
	
	public static Map<String,PropertyType> propertyStringToTypeMap = 
		new HashMap<String,PropertyType>();
	static {
		propertyStringToTypeMap.put(SQLTableConstants.TBLCOLVAL_COMMENT, PropertyType.COMMENT);
		propertyStringToTypeMap.put(SQLTableConstants.TBLCOLVAL_PRESENTATION, PropertyType.PRESENTATION);
		propertyStringToTypeMap.put(SQLTableConstants.TBLCOLVAL_DEFINITION, PropertyType.DEFINITION);
		propertyStringToTypeMap.put(SQLTableConstants.TBLCOLVAL_PROPERTY, PropertyType.GENERIC);
	}
	
	public static Map<Class<? extends Property>,PropertyType> propertyClassToTypeMap = 
		new HashMap<Class<? extends Property>,PropertyType>();
	static {
		propertyClassToTypeMap.put(Comment.class, PropertyType.COMMENT);
		propertyClassToTypeMap.put(Presentation.class, PropertyType.PRESENTATION);
		propertyClassToTypeMap.put(Definition.class, PropertyType.DEFINITION);
		propertyClassToTypeMap.put(Property.class, PropertyType.GENERIC);
	}
	
	public static List<Sort> mapSortOptionListToSort(SortOptionList list){
		Boolean DEFAULT_ASCENDING = new Boolean(true);
		
		if(list == null || list.getEntryCount() == 0) {return null;}
		
		List<Sort> returnList = new ArrayList<Sort>();

		for(SortOption option : list.getEntry()) {
			
			Boolean ascending;
			if(option.getAscending() != null) {
				ascending = option.getAscending();
			} else {
				ascending = DEFAULT_ASCENDING;
			}

			ColumnSortType type = 
				ColumnSortType.getColumnSortTypeForName(option.getExtensionName());
			if(type != null) {
				Sort sort = new Sort(
					type,
					ascending ? Order.ASC : Order.DESC);
	
				returnList.add(sort);
			}
		}
		return returnList;
	}

	/**
	 * Creates the list.
	 * 
	 * @param itemClazz the item clazz
	 * @param items the items
	 * 
	 * @return the list< t>
	 */
	public static <T> List<T> createList(Class<T> itemClazz, T... items){
		List<T> returnList = new ArrayList<T>();
		for(T item : items) {
			returnList.add(item);
		}
		return returnList;
	}
	
	public static <T> List<T> createNonTypedList(T... items){
		List<T> returnList = new ArrayList<T>();
		for(T item : items) {
			returnList.add(item);
		}
		return returnList;
	}
	
	public static CodeNamespacePair toCodeNamespacePair(ConceptReference ref){
		return toCodeNamespacePair(createNonTypedList(ref)).get(0);
	}
	
	public static List<CodeNamespacePair> toCodeNamespacePair(List<ConceptReference> list){
		List<CodeNamespacePair> returnList = new ArrayList<CodeNamespacePair>();
		for(ConceptReference ref : list) {
			returnList.add(new CodeNamespacePair(ref.getCode(), ref.getCodeNamespace()));
		}
		return returnList;
	}
	
	public static String getEntityDescriptionText(EntityDescription ed) {
		if(ed == null) {
			return "";
		} else {
			return ed.getContent();
		}
	}
	
	/**
	 * Creates the text.
	 * 
	 * @param content the content
	 * 
	 * @return the text
	 */
	public static Text createText(String content){
		return createText(content, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deepClone(T obj){
		return (T) SerializationUtils.clone(obj);
	}
	
	/**
	 * Creates the text.
	 * 
	 * @param content the content
	 * @param format the format
	 * 
	 * @return the text
	 */
	public static Text createText(String content, String format){
		Text text = new Text();
		text.setContent(content);
		text.setDataType(format);
		return text;
	}
	
	/**
	 * Creates the absolute coding scheme version reference.
	 * 
	 * @param urn the urn
	 * @param version the version
	 * 
	 * @return the absolute coding scheme version reference
	 */
	public static AbsoluteCodingSchemeVersionReference createAbsoluteCodingSchemeVersionReference(String urn,
			String version) {
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(urn);
		acsvr.setCodingSchemeVersion(version);
		return acsvr;
	}
	
	/**
	 * Insert into mappings.
	 * 
	 * @param mappings the mappings
	 * @param uriMap the uri map
	 */
	public static void insertIntoMappings(Mappings mappings, URIMap uriMap) {
		String addPrefix = "add";
		
		Class<? extends URIMap> uriMapClass = uriMap.getClass();
		String name = uriMapClass.getSimpleName();
		
		try {
			Method method = mappings.getClass().getMethod(addPrefix + name, uriMapClass);
			method.invoke(mappings, uriMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static void updateBean(final Object changes, Object beanToUpdate) {
		final String asReferenceSuffix = "AsReference";
		
		final List<String> nullProperties = new ArrayList<String>();
		
		ReflectionUtils.doWithFields(changes.getClass(), new FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				field.setAccessible(true);
				Object value = field.get(changes);
				if(value == null) {
					nullProperties.add(removeLeadingUnderscore(field.getName()));
				}
				if(value instanceof Collection && ((Collection)value).size() == 0) {
					nullProperties.add(
							removeTrailingList(
									removeLeadingUnderscore(field.getName()))
					);
					nullProperties.add(
							removeTrailingList(
									removeLeadingUnderscore(field.getName())) + asReferenceSuffix
					);
				}
			}	
		});
		
		BeanUtils.copyProperties(changes, beanToUpdate, nullProperties.toArray(new String[nullProperties.size()]));
	}
	
	private static String removeLeadingUnderscore(String string) {
		if(StringUtils.isBlank(string)) {return null;}
		
		if(string.startsWith("_")) {
			string = StringUtils.removeStart(string, "_");
		}
		return string;
	}
	
	private static String removeTrailingList(String string) {
		if(StringUtils.isBlank(string)) {return null;}
		
		if(string.endsWith("List")) {
			string = StringUtils.removeEnd(string, "List");
		}
		return string;
	}
	
	/**
	 * Insert into mappings.
	 * 
	 * @param mappings the mappings
	 * @param uriMap the uri map
	 */
	public static List<URIMap> getAllURIMappings(final Mappings mappings) {
		final String getPrefix = "get";
		final String getSuffix = "AsReference";
		
		final List<URIMap> uriMapList = new ArrayList<URIMap>();

		ReflectionUtils.doWithMethods(mappings.getClass(), new MethodCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void doWith(Method method) throws IllegalArgumentException,
					IllegalAccessException {
				try {
					List<URIMap> list = (List<URIMap>) method.invoke(mappings);
					uriMapList.addAll(list);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			
		}, new MethodFilter() {

			@Override
			public boolean matches(Method method) {
				return method.getName().startsWith(getPrefix) &&
					method.getName().endsWith(getSuffix);
			}
			
		});
		
		return uriMapList;	
	}

	public static List<String> localNameListToString(LocalNameList lnl){
		if(lnl == null || lnl.getEntryCount() == 0) {
			return null;
		}
		List<String> returnList = new ArrayList<String>();
		for(String name : lnl.getEntry()){
			returnList.add(name);
		}
		return returnList;
	}

	public static List<String> propertyTypeArrayToString(PropertyType[] types){
		if(types == null || types.length == 0) {
			return null;
		}

		List<String> returnList = new ArrayList<String>();
		for(PropertyType type : types){
			returnList.add(type.toString());
		}
		return returnList;
	}
	
	public static <T> Iterable<T> emptyIterableIfNull(Iterable<T> iterable) {
	    return iterable == null ? Collections.<T>emptyList() : iterable;
	}
	
	public static <T> Iterable<T> emptyIterableIfNull(T[] iterable) {
	    return iterable == null ? Collections.<T>emptyList() : Arrays.asList(iterable);
	}
	
	public static interface Equality<T> {
		
		public boolean equals(T one, T two);
	}
	
	public static class EqualsEquality<T> implements Equality<T> {
		
		public boolean equals(T one, T two) {
			return one.equals(two);
		}
	}

	public static <T> List<T> getDelta(List<T> base, List<T> changeSet, Equality<T> equality) {
	    Assert.isTrue(base.size() <= changeSet.size(), "Only Additions are permitted.");
	    List<T> returnList = new ArrayList<T>();
	    
	    for(T t : changeSet) {
	    	if(! contains(base,t,equality)) {
	    		returnList.add(t);
	    	}
	    }
	    return returnList;
	}
	
	public static <T> boolean contains(List<T> list, T item, Equality<T> equality) {
		for(T t : list) {
	    	if(equality.equals(item, t)) {
	    		return true;
	    	}
	    }
		return false;
	}
	
	public static <T> boolean equals(T one, T two, Equality<T> equality) {
		return equality.equals(one, two);
	}

	//Ignore this -- just some helpers for generating Ibatis Mapping code. Will go away...
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		for(Method method : Mappings.class.getMethods()) {
			if(method.getName().startsWith("addSupported")) {
				if(method.getParameterTypes().length == 1) {
					/*
					System.out.println("<subMap value=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("Supported", "") + "\" resultMap=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "Result\" />");
					*/
					/*
					System.out.println("<resultMap id=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "Result\" class=\"" + 
							method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s") + "\" extends=\"uriMapResult\">"
							+ "\n" + "</resultMap>"		
					);
					*/
					/*
					System.out.println("<typeAlias alias=\"" + method.getParameterTypes()[0].getSimpleName().replaceFirst("S", "s")
					+ 	"\" type=\"" +	method.getParameterTypes()[0].getName() +"\"/>"
					);
					*/
					
					System.out.println("if(clazz == " + method.getParameterTypes()[0].getSimpleName() + ".class){ \n " +
									   "   return SQLTableConstants.TBLCOLVAL_SUPPTAG_" + method.getParameterTypes()[0].getSimpleName().replaceFirst("Supported", "").toUpperCase() + "; \n}");
				}
			}
		}
	}
}