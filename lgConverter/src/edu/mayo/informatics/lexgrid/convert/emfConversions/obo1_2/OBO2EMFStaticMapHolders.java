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
package edu.mayo.informatics.lexgrid.convert.emfConversions.obo1_2;

import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.codingSchemes.CodingschemesFactory;
import org.LexGrid.emf.codingSchemes.impl.CodingschemesFactoryImpl;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.naming.NamingFactory;
import org.LexGrid.emf.naming.SupportedCodingScheme;
import org.LexGrid.emf.naming.SupportedDataType;
import org.LexGrid.emf.naming.SupportedLanguage;
import org.LexGrid.emf.naming.impl.NamingFactoryImpl;
import org.LexGrid.messaging.LgMessageDirectorIF;

import edu.mayo.informatics.resourcereader.obo.OBOConstants;
import edu.mayo.informatics.resourcereader.obo.OBOContents;
import edu.mayo.informatics.resourcereader.obo.OBOHeader;
import edu.mayo.informatics.resourcereader.obo.OBOResourceReader;
import edu.mayo.informatics.resourcereader.obo.OBOTerms;

public class OBO2EMFStaticMapHolders {
    private static Hashtable properties = new Hashtable();
    private static Hashtable association = new Hashtable();
    private static Hashtable conStatus = new Hashtable();
    private static Hashtable sources = new Hashtable();
    private static Hashtable repForms = new Hashtable();

    private NamingFactory nameFactory = new NamingFactoryImpl();
    private CodingschemesFactory csFactory = new CodingschemesFactoryImpl();

    private LgMessageDirectorIF messages_;

    public OBO2EMFStaticMapHolders() {
    }

    public OBO2EMFStaticMapHolders(LgMessageDirectorIF messages) {
        messages_ = messages;
    }

    private void prepareSupportedLanguages(List suppLang) {
        try {
            SupportedLanguage lang = nameFactory.createSupportedLanguage();
            lang.setLocalId(OBO2EMFConstants.LANG_ENGLISH);
            lang.setUri(OBO2EMFConstants.LANG_ENGLISH_URN);
            suppLang.add(lang);
        } catch (Exception e) {
            messages_.error("Failed while setting supported languages...", e);
        }
    }

    private void prepareSupportedCodingScheme(CodingScheme csclass) {
        try {
            List suppCodingScheme = csclass.getMappings().getSupportedCodingScheme();
            SupportedCodingScheme scs = nameFactory.createSupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());
            scs.setIsImported(true);
            suppCodingScheme.add(scs);
        } catch (Exception e) {
            messages_.error("Failed while setting supported codingScheme...", e);
        }
    }

    private void prepareSupportedFormats(List supportedFormats) {
        SupportedDataType fmt = nameFactory.createSupportedDataType();
        fmt.setLocalId(OBO2EMFConstants.PLAIN_FORMAT);
        fmt.setUri(OBO2EMFConstants.PLAIN_FORMAT_URN);
        supportedFormats.add(fmt);
    }

    public CodingScheme getOBOCodingScheme(OBOResourceReader oboRdr, String inputFileNameWithoutExt) {

        CodingScheme csclass = null;

        try {
            if (oboRdr == null) {
                messages_.fatalAndThrowException("Failed to create Coding Scheme Node bcause of OBO File Reader!");
                return csclass;
            }

            csclass = csFactory.createCodingScheme();
            Mappings mappings = nameFactory.createMappings();
            csclass.setMappings(mappings);

            // String csName = inputFileNameWithoutExt;
            String csName = "";
            OBOHeader oboHeader = oboRdr.getResourceHeader(false);
            OBOContents oboContents = oboRdr.getContents(false, false);
            OBOTerms terms = oboContents.getOBOTerms();
            TreeSet nameSpaceSet = terms.getNameSpaceSet();
            if (OBO2EMFUtils.isNull(csName) && nameSpaceSet.size() == 1)
                csName = nameSpaceSet.first().toString();

            if (OBO2EMFUtils.isNull(csName) && !OBO2EMFUtils.isNull(oboHeader.getDefaultNameSpace()))
                csName = oboHeader.getDefaultNameSpace();

            if (OBO2EMFUtils.isNull(csName) && !OBO2EMFUtils.isNull(terms.getTermPrefix()))
                csName = terms.getTermPrefix();

            if (OBO2EMFUtils.isNull(csName))
                csName = inputFileNameWithoutExt;

            csName = csName.trim();
            if (csName.length() > 50) {
                csName= csName.substring(0, 49);
            }

            csclass.setCodingSchemeName(csName);
            csclass.setFormalName(csName);
            csclass.setCodingSchemeURI(OBO2EMFConstants.BIOONTOLOGY_LSID_PREFIX + csName);
            OBO2EMFConstants.OBO_URN = OBO2EMFConstants.BIOONTOLOGY_LSID_PREFIX + csName + OBO2EMFConstants.URN_DELIM;
            csclass.setDefaultLanguage(OBO2EMFConstants.LANG_ENGLISH);

            if (!OBO2EMFUtils.isNull(oboHeader.getOntologyVersion()))
                csclass.setRepresentsVersion(oboHeader.getOntologyVersion());
            else
                csclass.setRepresentsVersion(OBOConstants.UNASSIGNED_LABEL);

            csclass.getLocalName().add(csName);
            csclass.getLocalName().add(OBO2EMFConstants.LOCAL_FULL_PREFIX + csName); // Pradip
            csclass.setApproxNumConcepts((int) terms.getMembersCount());
            // csclass.setIsNative(new Boolean(true));
            csclass.setEntityDescription(oboHeader.getRemarks());

            List supportedLanguages = csclass.getMappings().getSupportedLanguage();
            prepareSupportedLanguages(supportedLanguages);

            List supportedFormats = csclass.getMappings().getSupportedDataType();
            prepareSupportedFormats(supportedFormats);

            prepareSupportedCodingScheme(csclass);
        } catch (Exception e) {
            messages_.error("Failed while preparing for Coding Scheme Class", e);
        }

        return csclass;
    }

    public static Hashtable getFixedProperties() {
        properties.clear();
        properties.put(OBO2EMFConstants.PROPERTY_COMMENT, OBO2EMFConstants.PROPERTY_COMMENT);
        properties.put(OBO2EMFConstants.PROPERTY_DEFINITION, OBO2EMFConstants.PROPERTY_DEFINITION);
        properties.put(OBO2EMFConstants.PROPERTY_INSTRUCTION, OBO2EMFConstants.PROPERTY_INSTRUCTION);
        properties.put(OBO2EMFConstants.PROPERTY_TEXTPRESENTATION, OBO2EMFConstants.PROPERTY_TEXTPRESENTATION);
        properties.put(OBO2EMFConstants.PROPERTY_SYNONYM, OBO2EMFConstants.PROPERTY_SYNONYM);
        properties.put(OBO2EMFConstants.PROPERTY_ALTID, OBO2EMFConstants.PROPERTY_ALTID);

        return properties;
    }

    public static Hashtable getFixedConceptStatus() {
        conStatus.clear();
        return conStatus;
    }

    public static Hashtable getFixedSources() {
        sources.clear();
        return sources;
    }

    public static Hashtable getFixedRepresentationalForms() {
        repForms.clear();
        return repForms;
    }

    public static Hashtable getFixedAssociations() {
        association.clear();
        // association.put(OBO2EMFConstants.ASSOCIATION_HASSUBTYPE,
        // OBO2EMFConstants.ASSOCIATION_HASSUBTYPE);

        return association;
    }
}