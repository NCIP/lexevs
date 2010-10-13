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
package org.LexGrid.LexBIG.Impl.function;

import junit.framework.TestCase;

/**
 * Base text fixture for LexBIGService initialization and result logging
 * <p>
 * This program assumes the sample content or a full version of the
 * NCI_Thesaurus been loaded using the admin tools.
 */
abstract public class LexBIGServiceTestCase extends TestCase {
    // Info for test vocabularies ...
	public final static String AUTO_SCHEME = "Automobiles";
	public final static String AUTO_URN = "urn:oid:11.11.0.1";
    public final static String AUTO_VERSION = "1.0";
    protected final static String AUTO_EXPORT_SCHEME = "AutosEXPORT";
    protected final static String AUTO_EXPORT_URI = "AutosEXPORTURI";
    protected final static String AUTO_EXPORT_VERSION = "AutosEXPORTVersion";
    protected final static String HL7_SCHEME = "RIM_0219";
    protected final static String HL7_VERSION = "V 02-19";
    protected final static String PARTS_SCHEME = "GermanMadeParts";
    protected final static String PARTS_NAMESPACE = "GermanMadePartsNamespace";
    protected final static String PARTS_URN = "urn:oid:11.11.0.2";
    protected final static String PARTS_VERSION = "2.0";
    protected final static String THES_SCHEME = "Thesaurus.owl";
    protected final static String THES_URN = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
    protected final static String THES_VERSION = "05.09.bvt";
    protected final static String META_SCHEME = "NCI Metathesaurus";
    public final static String META_URN = "urn:oid:2.16.840.1.113883.3.26.1.2";
    protected final static String META_VERSION = "200510.bvt";
    protected final static String CELL_SCHEME = "cell";
    protected final static String CELL_URN = "urn:lsid:bioontology.org:cell";
    protected final static String CELL_VERSION = "UNASSIGNED";
    public final static String AIR_SCHEME = "AI/RHEUM";
    public final static String AIR_URN = "urn:oid:2.16.840.1.113883.6.110";
    public final static String AIR_VERSION = "1993.bvt";
    protected final static String AMINOACID_SCHEME = "AminoAcidRegisteredNameFromManifest";
    protected final static String AMINOACID_VERSION = "2005/10/11";
    protected final static String META_SCHEME_MANIFEST = "MetaManifest";
    protected final static String META_SCHEME_MANIFEST_URN = "MetaRegisteredName";
    protected final static String META_SCHEME_MANIFEST_VERSION = "MetaManifestV1";
    protected final static String CAMERA_SCHEME_MANIFEST = "CameraManifest";
    protected final static String CAMERA_SCHEME_MANIFEST_URN = "CameraRegisteredName";
    protected final static String CAMERA_SCHEME_MANIFEST_VERSION = "CameraV1";
    protected final static String OVARIANMASS_SCHEME_URN = "http://www.OntoReason.com/Ontologies/OvarianMass_SNOMED_ValueSets.owl";
    protected final static String OVARIANMASS_SCHEME_VERSION = "UNASSIGNED";
    protected final static String MAPPING_SCHEME_URI = "urn:oid:mapping:sample";
    protected final static String MAPPING_SCHEME_VERSION = "1.0";
    protected final static String PIZZA_SCHEME_URI = "http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl#";
    protected final static String PIZZA_SCHEME_VERSION = "version 1.2";
    protected final static String PIZZA_SCHEME_NAME = "pizza.owl";
 
    public final static String SAMPLE_META_VERSION = "200902_For_Test";
    /**
     * To be implemented by each descendant testcase.
     * 
     * @return String
     */
    abstract protected String getTestID();

}