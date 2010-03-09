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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.SemNetLoadPreferences.SemNetLoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.codingSchemes.CodingschemesFactory;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.commonTypes.Text;
import org.LexGrid.emf.concepts.Comment;
import org.LexGrid.emf.concepts.ConceptsFactory;
import org.LexGrid.emf.concepts.Definition;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.concepts.Entity;
import org.LexGrid.emf.concepts.Presentation;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.naming.NamingFactory;
import org.LexGrid.emf.naming.SupportedAssociation;
import org.LexGrid.emf.naming.SupportedHierarchy;
import org.LexGrid.emf.naming.SupportedProperty;
import org.LexGrid.emf.naming.URIMap;
import org.LexGrid.emf.naming.impl.SupportedAssociationImpl;
import org.LexGrid.emf.naming.impl.SupportedHierarchyImpl;
import org.LexGrid.emf.naming.impl.SupportedPropertyImpl;
import org.LexGrid.emf.naming.impl.SupportedSourceImpl;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.AssociationSource;
import org.LexGrid.emf.relations.AssociationTarget;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.emf.relations.RelationsFactory;
import org.LexGrid.emf.relations.util.RelationsUtil;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.log4j.Logger;

import com.ibm.icu.util.StringTokenizer;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Reads SemanticNet -> EMF
 * 
 * The UMLS semantic network files provide an indication of both stated and
 * inherited relationships. Parameter inheritanceLevel_ provided through
 * LoaderPreference XML file, controls which relationships are loaded and
 * navigable within the LexBIG repository. When selecting the option to load
 * none(inheritanceLevel_ = 0) of the inherited relationships, all associations
 * are extracted from the source file SRSTR. When loading all(inheritanceLevel_
 * = 1) inherited relations, associations are extracted from the source file
 * SRSTRE1. An additional option(inheritanceLevel_ = 2) is provided to load only
 * stated relations for direct subclass ('is_a') associations, but inherited
 * relationships for all other associations. Direct or stated relationships are
 * always imported, regardless of the selected option
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          05:42:24 -0600 (Mon, 30 Jan 2006) $
 */
public class SemNetRead extends EMFReadImpl implements EMFRead {
    private static Logger log = Logger.getLogger("convert.SemNetRead");

    URI semNetFolderLocation_;

    LgMessageDirectorIF messages_;
    private static final String ROOT_CODE = "@";
    private static final int NONE = 0;
    private static final int ALL = 1;
    private static final int EXCEPT_ISA = 2;
    private int inheritanceLevel_ = 2;

    public SemNetRead(URI fileLocation, LoaderPreferences loaderPrefs, LgMessageDirectorIF messages) {
        this.semNetFolderLocation_ = fileLocation;
        this.messages_ = messages;
        this.loaderPreferences = loaderPrefs;
    }

    public CodingScheme readCodingScheme() throws Exception {
        try {
            // Create and populate metadata for the source scheme ...
            CodingScheme scheme = CodingschemesFactory.eINSTANCE.createCodingScheme();
            initScheme(scheme);

            // Populate content ...
            Map codeToRelation = new HashMap();
            Map codeToRelDescription = new HashMap();
            Map codeToRevRelation = new HashMap();
            Map termToCode = new HashMap();

            if (loaderPreferences != null) {
                loadLoaderPreferences();
            }

            messages_.info("Loading with inheritanceLevel : " + inheritanceLevel_);

            populateConcepts(scheme, codeToRelation, codeToRelDescription, codeToRevRelation, termToCode);

            if (inheritanceLevel_ == NONE) {
                populateNoInferredRelations(scheme, codeToRelation, codeToRelDescription, codeToRevRelation,
                        termToCode, false);
            } else if (inheritanceLevel_ == ALL) {
                populateAllInferredRelations(scheme, codeToRelation, codeToRelDescription, codeToRevRelation, false);
            } else if (inheritanceLevel_ == EXCEPT_ISA) {
                populateAllInferredRelations(scheme, codeToRelation, codeToRelDescription, codeToRevRelation, true);
                populateNoInferredRelations(scheme, codeToRelation, codeToRelDescription, codeToRevRelation,
                        termToCode, true);
            }

            if (this.codingSchemeManifest != null) {
                ManifestUtil manifest = new ManifestUtil(null, messages_);
                manifest.applyManifest(codingSchemeManifest, scheme);
            }

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
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected void initScheme(CodingScheme scheme) throws IOException {
        // Initialize metadata ...
        scheme.setCodingSchemeName("UMLS_SemNet");
        Text txt = CommontypesFactory.eINSTANCE.createText();
        txt.setValue(getTermsAndConditions());

        scheme.setCopyright(txt);
        scheme.setDefaultLanguage("en");
        scheme.setFormalName("UMLS Semantic Network");
        scheme.getLocalName().add("UMLS_SemNet");
        scheme.setCodingSchemeURI("urn:lsid:nlm.nih.gov:semnet");
        scheme.setEntityDescription("The UMLS Semantic Network is one of three "
                + "UMLS Knowledge Sources developed as part of the "
                + "Unified Medical Language System project. The network "
                + "provides a consistent categorization of all concepts " + "represented in the UMLS Metathesaurus.");

        // Create top-level containers to hold concepts and relations ...
        Entities c = ConceptsFactory.eINSTANCE.createEntities();
        Relations r = RelationsFactory.eINSTANCE.createRelations();
        r.setContainerName("relations");
        r.setIsNative(Boolean.valueOf(true));
        scheme.setEntities(c);
        scheme.getRelations().add(r);

        // Initialize supported properties.
        // Note: The SRFLD file provides Field Descriptions.
        Mappings mappings = NamingFactory.eINSTANCE.createMappings();
        scheme.setMappings(mappings);
        BufferedReader reader = new BufferedReader(getReader("SRFLD"));
        String line;
        Collection supportedProps = new ArrayList();
        while (((line = reader.readLine()) != null)) {
            StringTokenizer st = new StringTokenizer(line, "|");
            if (st.countTokens() >= 4) {
                // Treat each field used in the definition file as a potential
                // property name.
                String fCOL = st.nextToken().trim();
                st.nextToken();
                String fREF = st.nextToken().trim();
                String fFIL = st.nextToken().trim();
                if (fFIL.contains("SRDEF") && !(supportedProps.contains(fCOL))) {
                    SupportedProperty sp = new SupportedPropertyImpl();
                    sp.setLocalId(fCOL);
                    sp.setUri("");
                    mappings.getSupportedProperty().add(sp);
                    supportedProps.add(fCOL);
                }
                // Pull version from documentation reference? Not much to grab
                // onto in the files.
                if (scheme.getRepresentsVersion() == null && fREF.length() > 0)
                    scheme.setRepresentsVersion(fREF);
            }
        }

        // Initialize fixed mappings ...
        URIMap map;
        map = NamingFactory.eINSTANCE.createSupportedCodingScheme();
        map.setLocalId("UMLS_SemNet");
        map.setUri("urn:lsid:nlm.nih.gov:semnet");
        mappings.getSupportedCodingScheme().add(map);

        map = NamingFactory.eINSTANCE.createSupportedDataType();
        map.setLocalId("text/plain");
        map.setUri("urn:oid:2.16.840.1.113883.6.10:text_plain");
        mappings.getSupportedDataType().add(map);

        map = NamingFactory.eINSTANCE.createSupportedLanguage();
        map.setLocalId("en");
        map.setUri("urn:oid:2.16.840.1.113883.6.84:en");
        mappings.getSupportedLanguage().add(map);

        map = new SupportedSourceImpl();
        map.setLocalId("NLM");
        map.setUri("urn:lsid:nlm.nih.gov");
        mappings.getSupportedSource().add(map);

        map = new SupportedHierarchyImpl();
        map.setLocalId("is_a");
        if (((SupportedHierarchy) map).getAssociationNames() == null)
            ((SupportedHierarchy) map).setAssociationNames(new ArrayList());
        ((SupportedHierarchy) map).getAssociationNames().add("hasSubtype");
        ((SupportedHierarchy) map).setRootCode(ROOT_CODE);
        ((SupportedHierarchy) map).setIsForwardNavigable(true);
        mappings.getSupportedHierarchy().add(map);
    }

    /**
     * Populates the concepts container for the coding scheme.
     * 
     * @param scheme
     * @param codeToRelation
     * @param codeToRelDescription
     * @param codeToRevRelation
     */
    @SuppressWarnings("unchecked")
    protected void populateConcepts(CodingScheme scheme, Map codeToRelation, Map codeToRelDescription,
            Map codeToRevRelation, Map termToCode) throws IOException {
        // Note: The SRDEF file provides basic information about the Semantic
        // Types and Relations.

        BufferedReader reader = new BufferedReader(getReader("SRDEF"));
        String line;
        int count = 0;
        Text txt = null;

        while (((line = reader.readLine()) != null)) {
            // Need to break up this line manually; StringTokenizer doesn't deal
            // well with consecutive delimiters in this case.
            int pos = 0;
            int t = 0;
            String[] tokens = new String[10];
            while (pos < line.length()) {
                int delimAt = line.indexOf('|', pos);
                tokens[t++] = line.substring(pos, delimAt);
                pos = delimAt + 1;
            }
            for (; t < 10; t++)
                tokens[t] = "";

            String fRT = tokens[0];
            String fUI = tokens[1];
            String fSTY_RL = tokens[2];
            // String fSTN_RTN = tokens[3];
            String fDEF = tokens[4];
            String fEX = tokens[5];
            String fUN = tokens[6];
            String fNH = tokens[7];
            String fABR = tokens[8];
            String fRIN = tokens[9];

            // Treat each field used in the definition file as a potential
            // property name.
            int propNum = 0;

            // Define the concept ...
            Entity ce = ConceptsFactory.eINSTANCE.createEntity();
            ce.setEntityCode(fUI);
            ce.setEntityDescription(fSTY_RL);

            ce.getEntityType().add(SQLTableConstants.ENTITYTYPE_CONCEPT);

            // Define properties to the concept ...
            if (fSTY_RL.length() > 0) {
                Presentation p = ConceptsFactory.eINSTANCE.createPresentation();
                p.setPropertyName("STY_RL");
                p.setPropertyId("P" + propNum++);
                txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) fSTY_RL);
                txt.setDataType("text/plain");
                p.setValue(txt);
                p.setIsPreferred(Boolean.valueOf(true));
                ce.getPresentation().add(p);
            }
            // Definition
            if (fDEF.length() > 0) {
                Definition d = ConceptsFactory.eINSTANCE.createDefinition();
                d.setPropertyName("DEF");
                d.setPropertyId("P" + propNum++);
                txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) fDEF);
                txt.setDataType("text/plain");
                d.setValue(txt);
                d.setIsPreferred(Boolean.valueOf(true));
                ce.getDefinition().add(d);
            }
            // Example
            if (fEX.length() > 0) {
                Comment c = ConceptsFactory.eINSTANCE.createComment();
                c.setPropertyName("EX");
                c.setPropertyId("P" + propNum++);
                txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) fEX);
                txt.setDataType("text/plain");
                c.setValue(txt);
                ce.getComment().add(c);
            }
            // Non-human flag
            if (fNH.length() > 0) {
                Property p = CommontypesFactory.eINSTANCE.createProperty();
                p.setPropertyName("NH");
                p.setPropertyId("P" + propNum++);
                txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) fNH);
                txt.setDataType("text/plain");
                p.setValue(txt);
                ce.getProperty().add(p);
            }
            // Abbreviation
            if (fABR.length() > 0) {
                Presentation p = ConceptsFactory.eINSTANCE.createPresentation();
                p.setPropertyName("ABR");
                p.setPropertyId("P" + propNum++);
                p.setIsPreferred(Boolean.valueOf(false));
                txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String) fABR);
                txt.setDataType("text/plain");
                p.setValue(txt);
                ce.getPresentation().add(p);
            }
            // Defining a relation?
            if ("RL".equals(fRT) && !codeToRelation.containsKey(fUI)) {
                codeToRelation.put(fUI, fSTY_RL);
                codeToRelDescription.put(fUI, fDEF);
                codeToRevRelation.put(fUI, fRIN);
            }

            // add the term and code into a HashMap which will be used while
            // creating relations.
            if (!termToCode.containsKey(fSTY_RL))
                termToCode.put(fSTY_RL, fUI);

            // Add and bump the count
            scheme.getEntities().getEntity().add(ce);
            count++;
        }
        scheme.setApproxNumConcepts(count);

        // Add all defined relations as supported ...
        for (Iterator relations = codeToRelation.values().iterator(); relations.hasNext();) {
            String rl = (String) relations.next(); // forward
            if (rl.equals("isa")) {
                rl = "hasSubtype";
            }
            SupportedAssociation assoc = new SupportedAssociationImpl();
            assoc.setLocalId(rl);
            assoc.setUri("");
            scheme.getMappings().getSupportedAssociation().add(assoc);
        }
    }

    /**
     * Populates the relations container with no inferred relations for the
     * coding scheme.
     * 
     * @param scheme
     * @param codeToRelation
     * @param codeToRelDescription
     * @param codeToRevRelation
     * @param termToCode
     */
    protected void populateNoInferredRelations(CodingScheme scheme, Map codeToRelation, Map codeToRelDescription,
            Map codeToRevRelation, Map termToCode, boolean onlyIsA) throws IOException {
        // Note: The SRSTR file provides a fully inherited set of Relations
        // (UIs).

        BufferedReader reader = new BufferedReader(getReader("SRSTR"));
        String line;
        while (((line = reader.readLine()) != null)) {
            StringTokenizer st = new StringTokenizer(line, "|");
            if (st.countTokens() >= 3) {
                // get code value for each term
                String fUI1 = (String) termToCode.get(st.nextToken().trim());
                String fUI2 = (String) termToCode.get(st.nextToken().trim());
                String fUI3 = (String) termToCode.get(st.nextToken().trim());

                // If fUI3 is null, fUI1 is a root node
                if (fUI3 == null) {
                    fUI3 = ROOT_CODE;
                }

                String name = (String) codeToRelation.get(fUI2);
                if (onlyIsA && !name.equals("isa"))
                    continue;

                // Relation
                Association assoc = RelationsFactory.eINSTANCE.createAssociation();
                if (name.equals("isa")) {
                    // set up the special hasSubtype association.
                    assoc.setEntityCode("hasSubtype");
                    assoc.setIsTransitive(true);

                    // need to reverse things for hasSubtype
                    assoc.setForwardName((String) codeToRevRelation.get(fUI2));
                    assoc.setReverseName(name);

                    // swap fUI1 and fUI3
                    String temp = fUI1;
                    fUI1 = fUI3;
                    fUI3 = temp;
                } else {
                    assoc.setEntityCode(name);
                    assoc.setForwardName(name);
                    assoc.setReverseName((String) codeToRevRelation.get(fUI2));
                }
                assoc.setEntityDescription((String) codeToRelDescription.get(fUI2));

                // Source
                AssociationSource ai = RelationsFactory.eINSTANCE.createAssociationSource();
                ai.setSourceEntityCode(fUI1);
                // Target
                AssociationTarget at = RelationsFactory.eINSTANCE.createAssociationTarget();
                at.setTargetEntityCode(fUI3);
                // Add them, ignoring duplicates ...
                assoc = RelationsUtil.subsume((Relations) scheme.getRelations().get(0), assoc);
                ai = RelationsUtil.subsume(assoc, ai);
                at = RelationsUtil.subsume(ai, at);
            }
        }
    }

    /**
     * Populates the relations container with all inferred relations for the
     * coding scheme.
     * 
     * @param scheme
     * @param codeToRelation
     * @param codeToRelDescription
     * @param codeToRevRelation
     */
    protected void populateAllInferredRelations(CodingScheme scheme, Map codeToRelation, Map codeToRelDescription,
            Map codeToRevRelation, boolean skipIsA) throws IOException {
        // Note: The SRSTRE1 file provides a fully inherited set of Relations
        // (UIs).

        BufferedReader reader = new BufferedReader(getReader("SRSTRE1"));
        String line;
        while (((line = reader.readLine()) != null)) {
            StringTokenizer st = new StringTokenizer(line, "|");
            if (st.countTokens() >= 3) {
                String fUI1 = st.nextToken().trim();
                String fUI2 = st.nextToken().trim();
                String fUI3 = st.nextToken().trim();
                String name = (String) codeToRelation.get(fUI2);

                // Skip isa relations from SRSTRE1 for option 2
                if (name.equals("isa") && skipIsA)
                    continue;

                // Relation
                Association assoc = RelationsFactory.eINSTANCE.createAssociation();

                if (name.equals("isa")) {
                    // set up the special hasSubtype association.
                    assoc.setEntityCode("hasSubtype");
                    // need to reverse things for hasSubtype
                    assoc.setForwardName((String) codeToRevRelation.get(fUI2));
                    assoc.setReverseName(name);

                    // swap fUI1 and fUI3
                    String temp = fUI1;
                    fUI1 = fUI3;
                    fUI3 = temp;
                } else {
                    assoc.setEntityCode(name);
                    assoc.setForwardName(name);
                    assoc.setReverseName((String) codeToRevRelation.get(fUI2));
                }
                assoc.setEntityDescription((String) codeToRelDescription.get(fUI2));

                // Source
                AssociationSource ai = RelationsFactory.eINSTANCE.createAssociationSource();
                ai.setSourceEntityCode(fUI1);
                // Target
                AssociationTarget at = RelationsFactory.eINSTANCE.createAssociationTarget();
                at.setTargetEntityCode(fUI3);
                // Add them, ignoring duplicates ...
                assoc = RelationsUtil.subsume((Relations) scheme.getRelations().get(0), assoc);
                ai = RelationsUtil.subsume(assoc, ai);
                at = RelationsUtil.subsume(ai, at);
            }
        }
    }

    /**
     * Return the license agreement. First try to resolve from a license.txt
     * file in the source directory. If not available, return the default.
     * 
     * @return The associated license terms for the source.
     */
    protected String getTermsAndConditions() {
        StringBuffer sb = new StringBuffer(2048);

        try {
            BufferedReader reader = new BufferedReader(getReader("license.txt"));
            String line;
            while (((line = reader.readLine()) != null))
                sb.append(line).append('\n');

        } catch (IOException ioe) {
            log.info("Unable to retrieve license information (license.txt file) from file '" + semNetFolderLocation_
                    + "'.  Default terms and conditions were "
                    + "substituted and are available in the copyright information of " + "the scheme.");

            sb.append("*Terms and Conditions for Use of the UMLS Semantic Network*");
            sb.append("\n");
            sb.append("\n*1.* *Introduction*");
            sb.append("\n");
            sb.append("\nThe following Terms and Conditions apply for use of the UMLS Semantic");
            sb.append("\nNetwork. Using the UMLS Semantic Network indicates your acceptance of");
            sb.append("\nthe following Terms and Conditions. These Terms and Conditions apply to");
            sb.append("\nall the UMLS Semantic Network files, independent of format and method of");
            sb.append("\nacquisition.");
            sb.append("\n");
            sb.append("\n*2. The UMLS Semantic Network*");
            sb.append("\n");
            sb.append("\nThe Lister Hill National Center for Biomedical Communications, National");
            sb.append("\nLibrary of Medicine, National Institutes of Health, Department of Health");
            sb.append("\nand Human Services, has developed the UMLS Semantic Network as one of");
            sb.append("\nthe three Unified Medical Language System Knowledge Sources. The UMLS");
            sb.append("\ninter-relates many vocabularies in the biomedical domain. The UMLS");
            sb.append("\nSemantic Network provides a consistent categorization of the concepts");
            sb.append("\nrepresented in the UMLS and provides an over-arching conceptual");
            sb.append("\nstructure for those concepts.");
            sb.append("\n");
            sb.append("\n*3. Availability*");
            sb.append("\n");
            sb.append("\nThe UMLS Semantic Network is available to all requesters, both within");
            sb.append("\nand outside the United States, at no charge.");
            sb.append("\n");
            sb.append("\n*4. Use of the UMLS Semantic Network*");
            sb.append("\n");
            sb.append("\na. Redistributions of the UMLS Semantic Network in source or binary form");
            sb.append("\nmust include this list of conditions in the documentation and/or other");
            sb.append("\nmaterials provided with the distribution.");
            sb.append("\n");
            sb.append("\nb. In any publication or distribution of all or any portion of the UMLS");
            sb.append("\nSemantic Network (1) you must attribute the source of the data as the");
            sb.append("\nUMLS Semantic Network with the release number and date; (2) you must");
            sb.append("\nstate any modifications made to the UMLS Semantic Network along with a");
            sb.append("\ncomplete description of the modifications, which may be in the form of");
            sb.append("\npatch files.");
            sb.append("\n");
            sb.append("\nc. You shall not assert any proprietary rights to any portion of the");
            sb.append("\nUMLS Semantic Network, nor represent the UMLS Semantic Network or any");
            sb.append("\npart thereof to anyone as other than a United States Government product.");
            sb.append("\n");
            sb.append("\nd. The name of the U.S. Department of Health and Human Services,");
            sb.append("\nNational Institutes of Health, National Library of Medicine, Lister Hill");
            sb.append("\nNational Center for Biomedical Communications may not be used to endorse");
            sb.append("\nor promote products derived from the UMLS Semantic Network without");
            sb.append("\nspecific prior written permission.");
            sb.append("\n");
            sb.append("\ne. Neither the United States Government, U.S. Department of Health and");
            sb.append("\nHuman Services, National Institutes of Health, National Library of");
            sb.append("\nMedicine, Lister Hill National Center for Biomedical Communications, nor");
            sb.append("\nany of its agencies, contractors, subcontractors or employees of the");
            sb.append("\nUnited States Government make any warranties, expressed or implied, with");
            sb.append("\nrespect to the UMLS Semantic Network, and, furthermore, assume no");
            sb.append("\nliability for any party's use, or the results of such use, of any part");
            sb.append("\nof the network.");
            sb.append("\n");
            sb.append("\nThese terms and conditions are in effect as long as the user retains any");
            sb.append("\npart of the UMLS Semantic Network.");
        }
        return sb.toString();
    }

    private Reader getReader(String fileName) throws MalformedURLException, IOException {
        URI temp = semNetFolderLocation_.resolve(fileName);
        if (temp.getScheme().equals("file")) {
            return new FileReader(new File(temp));
        } else {
            return new InputStreamReader(temp.toURL().openConnection().getInputStream());
        }
    }

    public URNVersionPair[] getUrnVersionPairs() throws Exception {
        return new URNVersionPair[] { new URNVersionPair("UMLS_SemNet", null) };
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

    /**
     * Loads the loader preferences. If the value is not null in the Preferences
     * Object, that value is used. Otherwise, the default values will be used.
     */
    private void loadLoaderPreferences() {
        SemNetLoaderPreferences semNetLoadPrefs = (SemNetLoaderPreferences) loaderPreferences;
        if (semNetLoadPrefs.getInheritanceLevel() != null) {
            inheritanceLevel_ = semNetLoadPrefs.getInheritanceLevel().getType();
        }

    }

    public void setStreamingOn(boolean streamOn) {
        // TODO Auto-generated method stub

    }

    public boolean getStreamingOn() {
        // TODO Auto-generated method stub
        return false;
    }
}