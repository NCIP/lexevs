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
package edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences;

/**
 * Constants for XML file based preferences loaders
 * 
 * @author <A HREF="mailto:peterson.kevin@mayo.edu">Kevin Peterson</A>
 */
public class PreferenceLoaderConstants {
    // all of these paths are relative to the location in lbModel.jar
    public static final String HL7_XSD = "PreferencesLoaderXSDS/HL7LoadPreferences.xsd";
    public static final String Meta_XSD = "PreferencesLoaderXSDS/MetaLoadPreferences.xsd";
    public static final String OBO_XSD = "PreferencesLoaderXSDS/OBOLoadPreferences.xsd";
    public static final String OWL_XSD = "PreferencesLoaderXSDS/OWLLoadPreferences.xsd";
    public static final String SEMNET_XSD = "PreferencesLoaderXSDS/SemNetLoadPreferences.xsd";
    public static final String UMLS_XSD = "PreferencesLoaderXSDS/UMLSLoadPreferences.xsd";
    public static final String XML_XSD = "PreferencesLoaderXSDS/XMLLoadPreferences.xsd";
    public static final String MedDRA_XSD = "PreferencesLoaderXSDS/MedDRALoadPreferences.xsd";

    public static final String META_METADATA_FILE_NAME = "NCIMetaThes_metadata.xml";
    public static final String META_HL7_METADATA_FILE_NAME = "HL7_RIM_metadata.xml"; // CRS

}