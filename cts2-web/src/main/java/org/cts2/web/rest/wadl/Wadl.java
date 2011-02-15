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
package org.cts2.web.rest.wadl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMethod;

import com.sun.research.ws.wadl.Application;
import com.sun.research.ws.wadl.Method;
import com.sun.research.ws.wadl.Param;
import com.sun.research.ws.wadl.Resource;

/**
 * The Class Wadl.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class Wadl {
	
	/** The wadl. */
	private Application wadl;
	
	/**
	 * Instantiates a new wadl.
	 *
	 * @param wadl the wadl
	 */
	protected Wadl(Application wadl){
		this.wadl = wadl;
	}
	
	/**
	 * Gets the resource by id.
	 *
	 * @param resourceId the resource id
	 * @return the resource by id
	 */
	public Resource getResourceById(String resourceId){
		for(Resource resource : wadl.getResources().getResource()){
			if(resource.getId().equals(resourceId)){
				return resource;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the method by id.
	 *
	 * @param resourceId the resource id
	 * @param methodId the method id
	 * @return the method by id
	 */
	public Method getMethodById(String resourceId, String methodId){
		return this.getMethodById(this.getResourceById(resourceId), methodId);
	}
	
	/**
	 * Gets the method by id.
	 *
	 * @param resource the resource
	 * @param methodId the method id
	 * @return the method by id
	 */
	public Method getMethodById(Resource resource, String methodId){
		for(Object methodOrResource : resource.getMethodOrResource()){
			if(methodOrResource instanceof Method){
				Method method = (Method)methodOrResource;
				if(method.getId().equals(methodId)){
					return method;
				}
			}
		}

		return null;
	}
	
	/**
	 * Gets the method request method.
	 *
	 * @param resourceId the resource id
	 * @param methodId the method id
	 * @return the method request method
	 */
	public RequestMethod getMethodRequestMethod(String resourceId, String methodId){
		return this.getMethodRequestMethod(this.getMethodById(resourceId, methodId));
		
	}
	
	/**
	 * Gets the method request method.
	 *
	 * @param method the method
	 * @return the method request method
	 */
	private RequestMethod getMethodRequestMethod(Method method){
		return RequestMethod.valueOf(method.getName());
	}
	
	/**
	 * Gets the method request method.
	 *
	 * @param resource the resource
	 * @param methodId the method id
	 * @return the method request method
	 */
	public RequestMethod getMethodRequestMethod(Resource resource, String methodId){
		return RequestMethod.valueOf(this.getMethodById(resource, methodId).getName());
	}

	/**
	 * Gets the method parameter names.
	 *
	 * @param method the method
	 * @return the method parameter names
	 */
	private String[] getMethodParameterNames(Method method){

		List<String> queryParams = new ArrayList<String>();

		for(Param queryParam : method.getRequest().getParam()){
			queryParams.add(queryParam.getName());
		}

		return queryParams.toArray(new String[queryParams.size()]);
	}
	
	/**
	 * Gets the wadl request mapping.
	 *
	 * @param resourceId the resource id
	 * @param methodId the method id
	 * @return the wadl request mapping
	 */
	public WadlRequestMappingInfo getWadlRequestMapping(String resourceId, String methodId){
		Resource resource = getResourceById(resourceId);
		Method method = this.getMethodById(resource, methodId);
		
		RequestMethod requestMethod = RequestMethod.GET;
		String[] parameters = new String[0];
		
		if(method != null){
			requestMethod = this.getMethodRequestMethod(method);
			parameters = this.getMethodParameterNames(method);
		}
		//TODO: get headers
		
		return new WadlRequestMappingInfo(resource.getPath(), requestMethod, parameters, null);
	}
	
	/**
	 * The Class WadlRequestMappingInfo.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class WadlRequestMappingInfo {
		
		/** The pattern. */
		private String pattern;
		
		/** The request method. */
		private RequestMethod requestMethod;
		
		/** The parameters. */
		private String[] parameters;
		
		/** The headers. */
		private String[] headers;
		
		/**
		 * Instantiates a new wadl request mapping info.
		 *
		 * @param pattern the pattern
		 * @param requestMethod the request method
		 * @param parameters the parameters
		 * @param headers the headers
		 */
		public WadlRequestMappingInfo(String pattern, RequestMethod requestMethod,
				String[] parameters, String[] headers) {
			super();
			this.pattern = pattern;
			this.requestMethod = requestMethod;
			this.parameters = parameters;
			this.headers = headers;
		}

		/**
		 * Gets the pattern.
		 *
		 * @return the pattern
		 */
		public String getPattern() {
			return pattern;
		}

		/**
		 * Sets the pattern.
		 *
		 * @param pattern the new pattern
		 */
		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		/**
		 * Gets the request method.
		 *
		 * @return the request method
		 */
		public RequestMethod getRequestMethod() {
			return requestMethod;
		}

		/**
		 * Sets the request method.
		 *
		 * @param requestMethod the new request method
		 */
		public void setRequestMethod(RequestMethod requestMethod) {
			this.requestMethod = requestMethod;
		}

		/**
		 * Gets the parameters.
		 *
		 * @return the parameters
		 */
		public String[] getParameters() {
			return parameters;
		}

		/**
		 * Sets the parameters.
		 *
		 * @param parameters the new parameters
		 */
		public void setParameters(String[] parameters) {
			this.parameters = parameters;
		}

		/**
		 * Gets the headers.
		 *
		 * @return the headers
		 */
		public String[] getHeaders() {
			return headers;
		}

		/**
		 * Sets the headers.
		 *
		 * @param headers the new headers
		 */
		public void setHeaders(String[] headers) {
			this.headers = headers;
		}
	}
	
	

	/**
	 * Gets the wadl.
	 *
	 * @return the wadl
	 */
	public Application getWadl() {
		return wadl;
	}

	/**
	 * Sets the wadl.
	 *
	 * @param wadl the new wadl
	 */
	public void setWadl(Application wadl) {
		this.wadl = wadl;
	}
}
