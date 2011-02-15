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
package org.cts2.web.rest.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.cts2.core.Message;
import org.cts2.core.Parameter;
import org.cts2.core.RESTResource;
import org.cts2.profile.BaseService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;

/**
 * The Class AbstractController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public abstract class AbstractMessageWrappingController implements InitializingBean {

	/** The base service. */
	private BaseService baseService;
	
	private Map<Class<?>, MethodWrapper<? extends Message,?>> messageWrappers = 
		new HashMap<Class<?>, MethodWrapper<? extends Message,?>>();
	
	
	/**
	 * Sets the base service.
	 *
	 * @param baseService the new base service
	 */
	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	/**
	 * Gets the base service.
	 *
	 * @return the base service
	 */
	public BaseService getBaseService() {
		return baseService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		messageWrappers = this.registerMessageWrappers(messageWrappers);
	}

	/**
	 * Wrap with method.
	 *
	 * @param <T> the
	 * @param <O> the
	 * @param payload the payload
	 * @param httpServletRequest the http servlet request
	 * @param methodWrapper the method wrapper
	 * @return the t
	 */
	protected <T> Message wrapWithMethod(T payload, HttpServletRequest httpServletRequest){
		@SuppressWarnings("unchecked")
		MethodWrapper<?,T> wrapper = (MethodWrapper<?, T>) this.messageWrappers.get(payload.getClass());
		
		return wrapper.wrapMessage(payload, httpServletRequest);
	}
	
	protected abstract Map<Class<?>, MethodWrapper<? extends Message, ?>> 
		registerMessageWrappers(Map<Class<?>, MethodWrapper<? extends Message, ?>> map);
	
	/**
	 * The Interface MethodWrapper.
	 *
	 * @param <T> the
	 * @param <O> the
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	protected interface MethodWrapper<T extends Message, O>{
		
		/**
		 * Wrap message.
		 *
		 * @param payload the payload
		 * @param httpServletRequest the http servlet request
		 * @return the t
		 */
		public T wrapMessage(O payload, HttpServletRequest httpServletRequest);
	}
	
	/**
	 * The Class AbstractMessageWrapper.
	 *
	 * @param <T> the
	 * @param <O> the
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	protected abstract class AbstractMessageWrapper<T extends Message, O> implements MethodWrapper<T,O>{
		
		/* (non-Javadoc)
		 * @see org.cts2.web.rest.controller.AbstractMessageWrappingController.MethodWrapper#wrapMessage(java.lang.Object, javax.servlet.http.HttpServletRequest)
		 */
		public T wrapMessage(O payload, HttpServletRequest httpServletRequest){
			T message = this.doBuildMessage();
			message = this.doSetPayload(payload, message);
			
			message.setHeading(this.getHeading(httpServletRequest));
			
			return message;
		}
		
		/**
		 * Do build message.
		 *
		 * @return the t
		 */
		protected abstract T doBuildMessage();
		
		/**
		 * Do set payload.
		 *
		 * @param payload the payload
		 * @param message the message
		 * @return the t
		 */
		protected abstract T doSetPayload(O payload, T message);
		
		/**
		 * Gets the heading.
		 *
		 * @param httpServletRequest the http servlet request
		 * @return the heading
		 */
		protected RESTResource getHeading(HttpServletRequest httpServletRequest){
			RESTResource resource = new RESTResource();
			
			@SuppressWarnings("unchecked")
			Map<Object,Object> parameterMap = httpServletRequest.getParameterMap();
			for(Object param : parameterMap.keySet()){
				Parameter headingParam = new Parameter();
				headingParam.setArg(param.toString());
				headingParam.setVal(getParamValue(
						parameterMap.get(param)));
				
				resource.addParameter(headingParam);
			}
			
			resource.setAccessDate(new Date());

			resource.setResourceURI(httpServletRequest.getRequestURL().toString());
			
			return resource;
		}
		
		private String getParamValue(Object value){
			if(value == null){
				return null;
			}
			
			if(value.getClass().isArray()){
				return ArrayUtils.toString(value);
			}
			
			return value.toString();
		}
	}
}
