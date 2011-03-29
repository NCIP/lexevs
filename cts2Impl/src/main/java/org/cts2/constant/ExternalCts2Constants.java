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
package org.cts2.constant;

/**
 * The Class ExternalCts2Constants.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ExternalCts2Constants {
	
	public static final String CONCAT_STRING = ":";
	
	public static final String SLASH = "/";

	public static final String CTS2_URI = "http://cts2.org";
	
	public static final String MODEL = "model";
	
	public static final String ATTRIBUTE = "attribute";
	
	public static final String MA_ENTITY_DESCRIPTION_DESIGNATION_NAME = "entityDescriptionDesignation";
	
	public static final String MA_ABOUT_NAME = "about";
	
	private static final String MODEL_ATTRIBUTE_ROOT = CTS2_URI + SLASH + MODEL + SLASH + ATTRIBUTE;

	public static final String MA_ABOUT_URI = MODEL_ATTRIBUTE_ROOT + CONCAT_STRING + MA_ABOUT_NAME;
	
	public static final String MA_ENTITY_DESCRIPTION_DESIGNATION_URI = MODEL_ATTRIBUTE_ROOT + CONCAT_STRING + MA_ENTITY_DESCRIPTION_DESIGNATION_NAME;
	
}
