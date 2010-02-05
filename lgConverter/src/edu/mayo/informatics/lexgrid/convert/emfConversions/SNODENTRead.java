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
package edu.mayo.informatics.lexgrid.convert.emfConversions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.codingSchemes.CodingschemesFactory;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.commonTypes.Text;
import org.LexGrid.emf.concepts.Concept;
import org.LexGrid.emf.concepts.ConceptsFactory;
import org.LexGrid.emf.concepts.Definition;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.concepts.Presentation;
import org.LexGrid.emf.concepts.PropertyLink;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.naming.NamingFactory;
import org.LexGrid.emf.naming.SupportedProperty;
import org.LexGrid.emf.naming.SupportedPropertyLink;
import org.LexGrid.emf.naming.URIMap;
import org.LexGrid.emf.naming.impl.SupportedPropertyImpl;
import org.LexGrid.emf.naming.impl.SupportedPropertyLinkImpl;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.emf.relations.RelationsFactory;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Reads SNODENT MSAccess DB -> EMF
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          05:42:24 -0600 (Mon, 30 Jan 2006) $
 */
public class SNODENTRead extends EMFReadImpl implements EMFRead {
    private static Logger log = Logger.getLogger("convert.SNODENTRead");

    private Connection c;

    LgMessageDirectorIF messages_;

    public SNODENTRead(String server, String driver, String username, String password, LoaderPreferences loaderPrefs,
            LgMessageDirectorIF messages) throws Exception {
        c = DBUtility.connectToDatabase(server, driver, username, password);
        this.messages_ = messages;
        this.loaderPreferences = loaderPrefs;
    }

    public CodingScheme readCodingScheme() throws Exception {
        try {
            // Create and populate metadata for the source scheme ...
            CodingScheme scheme = CodingschemesFactory.eINSTANCE.createCodingScheme();
            initScheme(scheme);

            populateConcepts(scheme);

            c.close();
            return scheme;
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
            return null;
        }
    }

    /**
     * Initialize metadata for the scheme.
     * 
     * @param scheme
     * @throws Exception
     * @throws IOException
     */
    protected void initScheme(CodingScheme scheme) throws Exception {
        // Initialize metadata ...
        scheme.setCodingSchemeName("SNODENT");
        Text txt = CommontypesFactory.eINSTANCE.createText();
        txt.setValue((String) " ");
        scheme.setCopyright(txt);
        scheme.setDefaultLanguage("en");
        scheme.setFormalName("SNODENT");
        scheme.setCodingSchemeURI("SNODENT");
        scheme.setEntityDescription("This is the old SNODENT from the American Dental Association, "
                + "which was an extension of the old SNOMED II");

        // Create top-level containers to hold concepts and relations ...
        Entities c = ConceptsFactory.eINSTANCE.createEntities();
        Relations r = RelationsFactory.eINSTANCE.createRelations();
        r.setContainerName("relations");
        r.setIsNative(Boolean.valueOf(true));
        scheme.setEntities(c);
        scheme.getRelations().add(r);

        Mappings mappings = NamingFactory.eINSTANCE.createMappings();
        scheme.setMappings(mappings);
        mappings.getSupportedProperty().add(makeSupportedProperty("textualPresentation"));
        mappings.getSupportedProperty().add(makeSupportedProperty("definition"));
        mappings.getSupportedProperty().add(makeSupportedProperty("SECTION"));
        mappings.getSupportedProperty().add(makeSupportedProperty("PROPOSED CATEGORY"));
        mappings.getSupportedProperty().add(makeSupportedProperty("ICDCODE"));
        mappings.getSupportedProperty().add(makeSupportedProperty("ICD10"));

        SupportedPropertyLink spl = new SupportedPropertyLinkImpl();
        spl.setLocalId("from");

        mappings.getSupportedPropertyLink().add(spl);

        // this is setup to read snodent 2000
        scheme.setRepresentsVersion("2000");

        // Initialize fixed mappings ...
        URIMap map;
        map = NamingFactory.eINSTANCE.createSupportedCodingScheme();
        map.setLocalId("SNODENT");
        map.setUri("SNODENT");
        mappings.getSupportedCodingScheme().add(map);

        map = NamingFactory.eINSTANCE.createSupportedDataType();
        map.setLocalId("text/plain");
        map.setUri("urn:oid:2.16.840.1.113883.6.10:text_plain");
        mappings.getSupportedDataType().add(map);

        map = NamingFactory.eINSTANCE.createSupportedLanguage();
        map.setLocalId("en");
        map.setUri("urn:oid:2.16.840.1.113883.6.84:en");
        mappings.getSupportedLanguage().add(map);
    }

    private SupportedProperty makeSupportedProperty(String propertyName) {
        SupportedProperty sp = new SupportedPropertyImpl();
        sp.setLocalId(propertyName);
        return sp;
    }

    /**
     * Populates the concepts container for the coding scheme.
     * 
     * @param scheme
     * @param codeToRelation
     * @param codeToRelDescription
     * @param codeToRevRelation
     * @throws Exception
     */
    protected void populateConcepts(CodingScheme scheme) throws Exception {
        PreparedStatement ps = c.prepareStatement("Select * from \"Final SNODENT\" order by TERMCODE");

        ResultSet results = ps.executeQuery();

        String lastCode = null;
        String lastSection = null;
        String lastProposedCategory = null;
        String lastDefinition = null;
        Text txt = null;
        Concept ce = null;
        int propNum = 0;

        int count = 0;
        while (results.next()) {
            String code = results.getString("TERMCODE");
            String text = results.getString("ENOMEN");
            String section = results.getString("SECTION");
            String proposedCategory = results.getString("PROPOSED CATEGORY");
            String icdCode = results.getString("ICDCODE");
            String icd10 = results.getString("ICD10");
            String definition = results.getString("DEFINITION");

            if (!code.equals(lastCode)) {
                if (ce != null) {
                    // add the previous code.
                    scheme.getEntities().getEntity().add(ce);
                    count++;
                }
                lastCode = code;

                // starting a new code.
                ce = ConceptsFactory.eINSTANCE.createConcept();
                propNum = 0;
                ce.setEntityCode(code);
                ce.setEntityDescription(text);

                // clear this
                lastDefinition = null;

                // only one unique of each of these per code.
                if (section != null && section.length() > 0) {
                    Property pr = CommontypesFactory.eINSTANCE.createProperty();
                    pr.setPropertyName("SECTION");
                    pr.setPropertyId("S" + propNum++);
                    txt = CommontypesFactory.eINSTANCE.createText();
                    txt.setValue((String) section);
                    txt.setDataType("text/plain");
                    pr.setValue(txt);
                    ce.getProperty().add(pr);
                }

                if (proposedCategory != null && proposedCategory.length() > 0) {
                    Property pr = CommontypesFactory.eINSTANCE.createProperty();
                    pr.setPropertyName("PROPOSED CATEGORY");
                    pr.setPropertyId("PC" + propNum++);
                    txt = CommontypesFactory.eINSTANCE.createText();
                    txt.setValue((String) proposedCategory);
                    txt.setDataType("text/plain");
                    pr.setValue(txt);
                    ce.getProperty().add(pr);
                }
            } else {
                // continuing the same concept - these should be the same.
                if (!section.equals(lastSection) || !proposedCategory.equals(lastProposedCategory)) {
                    throw new Exception("Invalid assumption - multiple sections or categories per code.");
                }
            }

            // Define properties to the concept ...

            Presentation p = ConceptsFactory.eINSTANCE.createPresentation();
            p.setPropertyName("textualPresentation");
            p.setPropertyId("P" + propNum++);
            txt = CommontypesFactory.eINSTANCE.createText();
            txt.setValue((String) text);
            txt.setDataType("text/plain");
            p.setValue(txt);
            ce.getPresentation().add(p);

            // Definition
            if (definition != null && definition.length() > 0 && !definition.equals(lastDefinition)) {
                Definition d = ConceptsFactory.eINSTANCE.createDefinition();
                d.setPropertyName("definition");
                d.setPropertyId("D" + propNum++);
                txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) definition);
                txt.setDataType("text/plain");
                d.setValue(txt);
                d.setIsPreferred(Boolean.valueOf(true));
                ce.getDefinition().add(d);
            }

            if (icdCode != null && icdCode.length() > 0) {
                Property pr = CommontypesFactory.eINSTANCE.createProperty();
                pr.setPropertyName("ICDCODE");
                pr.setPropertyId("ICDC" + propNum++);
                txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) icdCode);
                txt.setDataType("text/plain");
                pr.setValue(txt);
                ce.getProperty().add(pr);

                // link the icdCode property to the presentation id
                PropertyLink pl = ConceptsFactory.eINSTANCE.createPropertyLink();
                pl.setPropertyLink("from");
                pl.setSourceProperty(p.getPropertyId());
                pl.setTargetProperty(pr.getPropertyId());
                ce.getPropertyLink().add(pl);

            }
            if (icd10 != null && icd10.length() > 0) {
                Property pr = CommontypesFactory.eINSTANCE.createProperty();
                pr.setPropertyName("ICD10");
                pr.setPropertyId("ICD10" + propNum++);
                txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) icd10);
                txt.setDataType("text/plain");
                pr.setValue(txt);
                ce.getProperty().add(pr);

                // link the icd10 property to the presentation id
                PropertyLink pl = ConceptsFactory.eINSTANCE.createPropertyLink();
                pl.setPropertyLink("from");
                pl.setSourceProperty(p.getPropertyId());
                pl.setTargetProperty(pr.getPropertyId());
                ce.getPropertyLink().add(pl);
            }

            lastSection = section;
            lastProposedCategory = proposedCategory;
            lastDefinition = definition;
        }
        if (ce != null) {
            // add the final one
            scheme.getEntities().getEntity().add(ce);
            count++;
        }
        results.close();
        scheme.setApproxNumConcepts(count);
    }

    public URNVersionPair[] getUrnVersionPairs() throws Exception {
        return new URNVersionPair[] { new URNVersionPair("SNODENT", null) };
    }

    //
    // Currently unsupported operations ...
    //

    // Full read (from EMFRead interface) ...
    public CodingScheme[] readAllCodingSchemes() throws Exception {
        throw new UnsupportedOperationException();
    }

    public CodingScheme readCodingScheme(String registeredName) throws Exception {
        // Coding scheme for this converter is not selected by name.
        // By throwing this exception, the conversion launcher will
        // fall through to the readCodingScheme() method.
        throw new UnsupportedOperationException();
    }

    // Incremental read (from EMFRead interface) ...
    public Iterator streamedReadOnAssociations(CodingScheme codingScheme, Relations relationsContainer)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    public Iterator streamedReadOnAssociationInstances(CodingScheme codingScheme, Relations relationsContainer,
            Association associationContainer) throws Exception {
        throw new UnsupportedOperationException();
    }

    public Iterator streamedReadOnConcepts(CodingScheme codingScheme, Entities conceptsContainer) throws Exception {
        throw new UnsupportedOperationException();
    }

    public boolean supportsStreamedRead(CodingScheme codingScheme) {
        return false;
    }

    public void closeStreamedRead() {
    }

    public void setStreamingOn(boolean streamOn) {
        // TODO Auto-generated method stub

    }

    public boolean getStreamingOn() {
        // TODO Auto-generated method stub
        return false;
    }
}