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
package edu.mayo.informatics.lexgrid.convert.utility;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfAssociationDefinition;
import org.LexGrid.LexOnt.CsmfCodingSchemeName;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.LexGrid.LexOnt.CsmfDefaultLanguage;
import org.LexGrid.LexOnt.CsmfEntityDescription;
import org.LexGrid.LexOnt.CsmfFormalName;
import org.LexGrid.LexOnt.CsmfLocalName;
import org.LexGrid.LexOnt.CsmfMappings;
import org.LexGrid.LexOnt.CsmfSource;
import org.LexGrid.LexOnt.CsmfText;
import org.LexGrid.LexOnt.CsmfVersion;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Source;
import org.LexGrid.emf.commonTypes.Text;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.naming.NamingFactory;
import org.LexGrid.emf.naming.SupportedHierarchy;
import org.LexGrid.emf.naming.URIMap;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.relations.Association;
import org.LexGrid.util.Utility;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.Marshaller;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

public class ManifestUtil {

    /** Holds the reference for the URI manifestURI_ */
    protected URI manifestURI_ = null;
    /** Holds the reference for LgMessageDirectorIF */
    private LgMessageDirectorIF messages_ = null;
    /** Holds the String reference indicating the last Validation Error */
    protected String lastValidationError_ = "";
    /** Holds the reference for CodingSchemeType */
    private CodingScheme emfScheme_ = null;
    /** Holds the reference for sqlConnection_ */
    private Connection sqlConnection_ = null;
    /** Holds the reference for sqlTableUtil_ */
    private SQLTableUtilities sqlTableUtil_ = null;
    /** Holds the reference for attributeMap_ */
    private Map<String, Object> attributeMap_ = null;
    /** Holds the reference for whereClause_ */
    private StringBuffer whereClause_ = null;
    /** Holds the type of database */
    private String dbType_ = null;

    /**
     * Constructor - initializes the manifest by extracting the
     * CodingSchemeManifest from the given URI. However applyManifest methods of
     * this class can be utilized even with out URI, by injecting the
     * CodingSchemeManifest object directly into the class using
     * setCodingSchemeManifest method.
     * 
     * @param uri
     * @param messages
     */
    public ManifestUtil(URI uri, LgMessageDirectorIF messages) {
        this.manifestURI_ = uri;
        this.messages_ = messages;
    }

    /**
     * This method validates and returns the manifest object for the
     * manifestURI_. Returns null if the manifestURI is invalid or the manifest
     * is invalid.
     * 
     * @param ontologyNameSpace
     * @return
     * @throws LgConvertException
     */
    public CodingSchemeManifest getManifest() {

        CodingSchemeManifest manifest = null;
        boolean valid = isValidFile(manifestURI_);
        String schema = "http://LexGrid.org/schema/2009/01/LexOnt/CodingSchemeManifest.xsd";
        if (valid) {
            try {
                org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
                        CodingSchemeManifest.class);
                manifest = (CodingSchemeManifest) um.unmarshal(new InputStreamReader(manifestURI_.toURL()
                        .openConnection().getInputStream()));

                StringWriter myWriter = new StringWriter();
                Marshaller m1 = new Marshaller(myWriter);
                m1.setNamespaceMapping("", schema);
                m1.setSchemaLocation("http://LexGrid.org/schema/2009/01/LexOnt/CodingSchemeManifest" + schema);
                m1.marshal(manifest);

            } catch (Exception e) {
                lastValidationError_ += "Manifest file [" + manifestURI_.toString() + "] is not valid for schema ["
                        + schema + "].";
                lastValidationError_ += ";" + e.getMessage();
                if (messages_ != null) {
                    messages_.warn(lastValidationError_);
                } else {
                    e.printStackTrace();
                }
            }
        }

        return manifest;
    }

    /**
     * Performs additional validation to see if the manifest xml file is valid
     * for its schema.
     * http://LexGrid.org/schema/LexBIG/2009/01/CodingSchemeManifest.xsd
     * 
     */
    public boolean isValidManifest() {
        if (getManifest() != null) {
            return true;
        }
        return false;
    }

    /**
     * This method validates the manifest file
     * 
     * @param uri
     * @return boolean if the URI indicates valid file.
     */
    private boolean isValidFile(URI uri) {
        boolean isValid = false;

        try {
            if (uri != null) {
                if (uri.getScheme().equals("file")) {
                    new FileReader(new File(uri));
                } else {
                    new InputStreamReader(uri.toURL().openConnection().getInputStream());
                }

                isValid = true;
            }
        }

        catch (Exception e) {
            lastValidationError_ += "[" + ((uri != null) ? uri.toString() : "NULL") + "] not found. ";
            messages_.fatal(lastValidationError_ + ":" + e.getMessage());
        }

        return isValid;
    }

    /**
     * Method applies the given manifest data to an EMF-based CodingScheme based
     * on the suitable flag values and conditions.
     * 
     * @param manifest
     * @param emfCodingScheme
     * @throws LgConvertException
     */
    public void applyManifest(CodingSchemeManifest manifest, CodingScheme emfCodingScheme) {

        if (manifest == null || emfCodingScheme == null)
            return;

        emfScheme_ = emfCodingScheme;
        Mappings emfMappings = emfScheme_.getMappings();
        if (emfMappings == null) {
            emfMappings = NamingFactory.eINSTANCE.createMappings();
            emfScheme_.setMappings(emfMappings);
        }

        // Set CodingScheme
        CsmfCodingSchemeName csName = manifest.getCodingScheme();
        if (csName != null)
            setCodingScheme(csName.getContent(), csName.getToOverride().booleanValue());

        // set FormalName
        CsmfFormalName frmlName = manifest.getFormalName();
        if (frmlName != null)
            setFormalName(frmlName.getContent(), frmlName.getToOverride().booleanValue());

        // set Registered Name
        CsmfCodingSchemeURI regName = manifest.getCodingSchemeURI();
        if (regName != null)
            setRegisteredName(regName.getContent(), regName.getToOverride().booleanValue());

        // set Entity Description
        CsmfEntityDescription entDesc = manifest.getEntityDescription();
        if (entDesc != null)
            setEntityDescription(entDesc.getContent(), entDesc.getToOverride().booleanValue());

        // set Default Language
        CsmfDefaultLanguage defLang = manifest.getDefaultLanguage();
        if (defLang != null)
            setDefaultLanguage(defLang.getContent(), defLang.getToOverride().booleanValue());

        // set Represents Version
        CsmfVersion version = manifest.getRepresentsVersion();
        if (version != null)
            setRepresentsVersion(version.getContent(), version.getToOverride().booleanValue());

        // set Copyright Text
        CsmfText txt = manifest.getCopyright();
        if (txt != null)
            setCopyrightText(txt.getContent(), txt.getToOverride().booleanValue());

        // Add Sources
        preLoadAddSources(manifest.getSource());

        // Add Local Names
        preLoadAddLocalNames(manifest.getLocalName());

        // Transfer Mappings
        preLoadAddSupportedMappings(manifest.getMappings());

    }

    /**
     * Applies the given manifest to an existing coding scheme definition in a
     * SQL-based repository.
     * 
     * @param manifest
     * @param sqlConfig
     * @param tablePrefix
     * @param failOnAllErrors
     * @param messages
     * @param codingSchemes
     * @throws LgConvertException
     * @throws SQLException
     */
    public void applyManifest(CodingSchemeManifest manifest, JDBCConnectionDescriptor sqlConfig, String tablePrefix,
            URNVersionPair codingSchemes) throws LgConvertException, SQLException {

        ResultSet queryResultSet = null;
        String codingSchemeName = null;

        try {
            try {
                sqlConnection_ = DBUtility.connectToDatabase(sqlConfig.getDbUrl(), sqlConfig.getDbDriver(), sqlConfig
                        .getDbUid(), sqlConfig.getDbPwd());
                dbType_ = sqlConnection_.getMetaData().getDatabaseProductName();
                sqlTableUtil_ = new SQLTableUtilities(sqlConnection_, tablePrefix);
                sqlConnection_.setAutoCommit(false);
            } catch (Exception e) {
                throw new LgConvertException("Exception occured while obtaining the connection to DB : "
                        + e.getMessage());
            }

            /*
             * ------------------------------------------------------------------
             * ---- codingScheme
             * ------------------------------------------------
             * ----------------------
             */
            try {

                attributeMap_ = new HashMap<String, Object>();

                attributeMap_.put("1", SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                attributeMap_.put("2", SQLTableConstants.TBLCOL_CODINGSCHEMEURI);
                attributeMap_.put("3", SQLTableConstants.TBLCOL_REPRESENTSVERSION);
                attributeMap_.put("4", SQLTableConstants.TBLCOL_FORMALNAME);
                attributeMap_.put("5", SQLTableConstants.TBLCOL_DEFAULTLANGUAGE);
                attributeMap_.put("6", SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);
                attributeMap_.put("7", SQLTableConstants.TBLCOL_COPYRIGHT);

                whereClause_ = new StringBuffer();
                whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMEURI + " = ");
                whereClause_.append("\'");
                whereClause_.append(codingSchemes.getUrn());
                whereClause_.append("\'");

                whereClause_.append(" AND ");
                whereClause_.append(SQLTableConstants.TBLCOL_REPRESENTSVERSION + " = ");
                whereClause_.append("\'");
                whereClause_.append(codingSchemes.getVersion());
                whereClause_.append("\'");

                queryResultSet = sqlTableUtil_.extractDataFromDB(SQLTableConstants.TBL_CODING_SCHEME, attributeMap_,
                        whereClause_.toString(), dbType_);

                if (queryResultSet.next()) {
                    // If a coding scheme name was specified in the manifest,
                    // ensure we have
                    // a match for the existing record. Coding scheme name is
                    // used as a key
                    // across tables, preventing simple modification of scheme
                    // metadata in
                    // post-load manifest application.
                    codingSchemeName = queryResultSet.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                    CsmfCodingSchemeName newCodingSchemeName = manifest.getCodingScheme();
                    if (newCodingSchemeName != null && (StringUtils.isNotBlank(newCodingSchemeName.getContent()))
                            && (!newCodingSchemeName.getContent().equals(codingSchemeName))) {
                        messages_
                                .info("Coding Scheme name cannot be changed when applying a manifest after the original load.");
                    }

                    // Process attributes in the primary coding scheme record.
                    attributeMap_ = new HashMap<String, Object>();

                    // Current values are pulled from the database for
                    // comparison when
                    // determining override behavior.
                    String regName = queryResultSet.getString(SQLTableConstants.TBLCOL_CODINGSCHEMEURI);
                    String repVersion = queryResultSet.getString(SQLTableConstants.TBLCOL_REPRESENTSVERSION);
                    String formalName = queryResultSet.getString(SQLTableConstants.TBLCOL_FORMALNAME);
                    String defaultLang = queryResultSet.getString(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE);

                    Object entityDesc = null;
                    Object copyRt = null;
                    if (dbType_.startsWith("Oracle")) {
                        entityDesc = (Clob) queryResultSet.getClob(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);
                        copyRt = (Clob) queryResultSet.getClob(SQLTableConstants.TBLCOL_COPYRIGHT);
                    } else {
                        entityDesc = (String) queryResultSet.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION);
                        copyRt = (String) queryResultSet.getString(SQLTableConstants.TBLCOL_COPYRIGHT);
                    }

                    // Process Formal Name ...
                    CsmfFormalName newFormalName = manifest.getFormalName();
                    if (newFormalName != null
                            && (StringUtils.isNotBlank(newFormalName.getContent()))
                            && (BooleanUtils.toBoolean(newFormalName.getToOverride()) || StringUtils
                                    .isBlank(formalName)))
                        attributeMap_.put(SQLTableConstants.TBLCOL_FORMALNAME, newFormalName.getContent());
                    // Process Coding Scheme URI ...
                    CsmfCodingSchemeURI newCodingSchemeURI = manifest.getCodingSchemeURI();
                    if (newCodingSchemeURI != null
                            && (StringUtils.isNotBlank(newCodingSchemeURI.getContent()))
                            && (BooleanUtils.toBoolean(newCodingSchemeURI.getToOverride()
                                    || StringUtils.isBlank(regName)))) {
                        attributeMap_.put(SQLTableConstants.TBLCOL_CODINGSCHEMEURI, newCodingSchemeURI.getContent());
                        addSupportedCodingSchemeToManifest(manifest, codingSchemeName, newCodingSchemeURI.getContent(),
                                true);

                    }
                    // Process Default Language ...
                    CsmfDefaultLanguage newDefaultLanguage = manifest.getDefaultLanguage();
                    if (newDefaultLanguage != null
                            && (StringUtils.isNotBlank(newDefaultLanguage.getContent()))
                            && (BooleanUtils.toBoolean(newDefaultLanguage.getToOverride()
                                    || StringUtils.isBlank(defaultLang))))
                        attributeMap_.put(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE, newDefaultLanguage.getContent());
                    // Process Version ...
                    CsmfVersion newVersion = manifest.getRepresentsVersion();
                    if (newVersion != null && (StringUtils.isNotBlank(newVersion.getContent()))
                            && (BooleanUtils.toBoolean(newVersion.getToOverride() || StringUtils.isBlank(repVersion))))
                        attributeMap_.put(SQLTableConstants.TBLCOL_REPRESENTSVERSION, newVersion.getContent());
                    // Process Entity Description ...
                    CsmfEntityDescription newDescription = manifest.getEntityDescription();
                    if (newDescription != null && (StringUtils.isNotBlank(newDescription.getContent()))
                            && (BooleanUtils.toBoolean(newDescription.getToOverride() || entityDesc == null)))
                        attributeMap_.put(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION, newDescription.getContent());
                    // Process Copyright ...
                    CsmfText newCopyright = manifest.getCopyright();
                    if (newCopyright != null && (StringUtils.isNotBlank(newCopyright.getContent()))
                            && (BooleanUtils.toBoolean(newCopyright.getToOverride() || copyRt == null)))
                        attributeMap_.put(SQLTableConstants.TBLCOL_COPYRIGHT, newCopyright.getContent());

                    // Insert the new values
                    whereClause_ = new StringBuffer();

                    whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMEURI + " = ");
                    whereClause_.append("\'");
                    whereClause_.append(regName);
                    whereClause_.append("\'");

                    whereClause_.append(" AND ");

                    whereClause_.append(SQLTableConstants.TBLCOL_REPRESENTSVERSION + " = ");
                    whereClause_.append("\'");
                    whereClause_.append(repVersion);
                    whereClause_.append("\'");

                    sqlTableUtil_.updateRow(SQLTableConstants.TBL_CODING_SCHEME, attributeMap_,
                            whereClause_.toString(), dbType_);

                    /* print updated attributes */

                    StringBuffer message = new StringBuffer();
                    Iterator itr = attributeMap_.keySet().iterator();
                    while (itr.hasNext()) {
                        message.append(itr.next());
                        message.append(",");
                    }
                    message.deleteCharAt(message.length() - 1);
                    messages_.info("Updated " + message.toString() + " successfully.");

                } else {
                    throw new LgConvertException("No entry for the table : " + tablePrefix
                            + SQLTableConstants.TBL_CODING_SCHEME);
                }
            } catch (SQLException e) {
                sqlConnection_.rollback();
                throw new LgConvertException("Exception occured while updating " + SQLTableConstants.TBL_CODING_SCHEME
                        + "- Rolling back applied changes : " + e.getMessage());
            }

            /*
             * ------------------------------------------------------------------
             * ---- codingSchemeMultiAttrib
             * --------------------------------------
             * --------------------------------
             */
            try {
                // Extract data from codingSchemeMultiAttrib table
                attributeMap_ = new HashMap<String, Object>();
                attributeMap_.put("1", "*");

                whereClause_ = new StringBuffer();

                if (codingSchemeName != null || !"".equals(codingSchemeName.trim())) {
                    whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ");
                    whereClause_.append("\'");
                    whereClause_.append(codingSchemeName);
                    whereClause_.append("\'");
                }

                queryResultSet = sqlTableUtil_.extractDataFromDB(SQLTableConstants.TBL_CODING_SCHEME_MULTI_ATTRIBUTES,
                        attributeMap_, whereClause_.toString(), sqlConnection_.getMetaData().getDatabaseProductName());

                if (queryResultSet.next()) {
                    // Add LocalNames
                    postLoadAddLocalNames(manifest.getLocalName(), queryResultSet);

                    // Add Sources
                    postLoadAddSource(manifest.getSource(), queryResultSet);
                }

            } catch (SQLException e) {
                sqlConnection_.rollback();
                throw new LgConvertException("Exception occured while updating "
                        + SQLTableConstants.TBL_CODING_SCHEME_MULTI_ATTRIBUTES + "- Rolling back applied changes : "
                        + e.getMessage());
            }

            /*
             * ------------------------------------------------------------------
             * ---- codingSchemeSupportedAttrib
             * ----------------------------------
             * ------------------------------------
             */

            // Extract data from codingSchemeSupportedAttrib table
            attributeMap_ = new HashMap<String, Object>();
            attributeMap_.put("1", "*");

            whereClause_ = new StringBuffer();

            if (codingSchemeName != null || !"".equals(codingSchemeName.trim())) {
                whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ");
                whereClause_.append("\'");
                whereClause_.append(codingSchemeName);
                whereClause_.append("\'");
            }

            try {
                queryResultSet = sqlTableUtil_.extractDataFromDB(
                        SQLTableConstants.TBL_CODING_SCHEME_SUPPORTED_ATTRIBUTES, attributeMap_, whereClause_
                                .toString(), sqlConnection_.getMetaData().getDatabaseProductName());

                if (queryResultSet.next()) {
                    // Add Supported Mappings
                    postLoadAddSupportedMapping(manifest.getMappings(), queryResultSet);
                    messages_.info("Updated Supported Mappings.");
                }
            } catch (SQLException e) {
                sqlConnection_.rollback();
                throw new LgConvertException("Exception occured while updating "
                        + SQLTableConstants.TBL_CODING_SCHEME_SUPPORTED_ATTRIBUTES
                        + "- Rolling back applied changes : " + e.getMessage());
            }

            /*
             * ------------------------------------------------------------------
             * ---- association Forward and Reverse Names.
             * ----------------------
             * ------------------------------------------------
             */

            CsmfAssociationDefinition assocDefinitions = manifest.getAssociationDefinitions();
            for (int i = 0; assocDefinitions != null && i < assocDefinitions.getAssocCount(); i++) {
                Association association = assocDefinitions.getAssoc(i);

                String entityCode = association.getEntityCode();

                attributeMap_ = new HashMap<String, Object>();
                attributeMap_.put("1", "*");

                whereClause_ = new StringBuffer();

                if (codingSchemeName != null || !"".equals(codingSchemeName.trim())) {
                    whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ");
                    whereClause_.append("\'");
                    whereClause_.append(codingSchemeName);
                    whereClause_.append("\'");

                    whereClause_.append(" AND ");

                    whereClause_.append(SQLTableConstants.TBLCOL_ENTITYCODE + " = ");
                    whereClause_.append("\'");
                    whereClause_.append(entityCode);
                    whereClause_.append("\'");
                }

                try {
                    queryResultSet = sqlTableUtil_.extractDataFromDB(SQLTableConstants.TBL_ASSOCIATION, attributeMap_,
                            whereClause_.toString(), sqlConnection_.getMetaData().getDatabaseProductName());

                    if (queryResultSet.next() && assocDefinitions.getToUpdate()) {
                        postLoadUpdateAssociation(association, queryResultSet);
                        messages_.info("Updated forwardName and reverseName for the association : "
                                + association.getAssociationName());
                    }
                } catch (SQLException e) {
                    sqlConnection_.rollback();
                    throw new LgConvertException("Exception occured while updating "
                            + SQLTableConstants.TBL_ASSOCIATION + "- Rolling back applied changes : " + e.getMessage());
                }
            }
            sqlConnection_.commit();

        } finally {
            if (sqlConnection_ != null)
                sqlConnection_.close();
        }
    }

    /**
     * Method loads the supported Mapping Data from manifest file into data
     * database.
     * 
     * @param castorMapping
     * @param queryResultSet
     * @throws LgConvertException
     */
    private void postLoadAddSupportedMapping(CsmfMappings castorMapping, ResultSet queryResultSet)
            throws LgConvertException {

        if (castorMapping == null)
            return;

        boolean toUpdate = castorMapping.getToUpdate().booleanValue();

        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedAssociation(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedAssociationQualifier(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedCodingScheme(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedContainer(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTAINERNAME);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedContext(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedDataType(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_DATATYPE);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedDegreeOfFidelity(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_DEGREEOFFIDELITY);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedEntityType(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_ENTITYTYPE);
        postLoadUpdateSupportedHierarchy(queryResultSet, castorMapping.getSupportedHierarchy(), toUpdate);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedLanguage(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedNamespace(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedProperty(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedPropertyLink(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYLINK);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedPropertyQualifier(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedPropertyQualifierType(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIERTYPE);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedPropertyType(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYTYPE);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedRepresentationalForm(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedSourceRole(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_ROLEGROUP);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedSortOrder(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_SORTORDER);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedSource(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
        postLoadUpdateSupportedMapping(queryResultSet, castorMapping.getSupportedStatus(), toUpdate,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_STATUS);
    }

    /**
     * Method loads the supported Hierarchy Data from manifest file into data
     * database.
     * 
     * @param queryResultSet
     * @param supportedHierarchy
     * @param toUpdate
     * @throws LgConvertException
     */
    private void postLoadUpdateSupportedHierarchy(ResultSet queryResultSet,
            org.LexGrid.naming.SupportedHierarchy[] supportedHierarchy, boolean toUpdate) throws LgConvertException {

        boolean present = false;
        String codingScheme = null;
        String suppAttribTag = null;
        String localId = null;
        String rootCode = null;

        try {
            for (int i = 0; i < supportedHierarchy.length; i++) {
                present = false;
                queryResultSet.beforeFirst();
                while (queryResultSet.next()) {

                    codingScheme = queryResultSet.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                    suppAttribTag = queryResultSet.getString(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG);
                    localId = queryResultSet.getString(SQLTableConstants.TBLCOL_ID);
                    rootCode = queryResultSet.getString(SQLTableConstants.TBLCOL_VAL1);

                    List<String> manifestAssocIds = arrayToList(queryResultSet.getString(
                            SQLTableConstants.TBLCOL_IDVALUE).split(","));
                    List<String> castorAssocIds = arrayToList(supportedHierarchy[i].getAssociationNames(0).split(","));

                    Collections.sort(manifestAssocIds);
                    Collections.sort(castorAssocIds);

                    if (suppAttribTag.equalsIgnoreCase("Hierarchy")
                            && localId.equalsIgnoreCase(supportedHierarchy[i].getLocalId())
                            && rootCode.equalsIgnoreCase(supportedHierarchy[i].getRootCode())
                            && manifestAssocIds.equals(castorAssocIds)) {
                        present = true;
                        if (toUpdate) {
                            String urn = supportedHierarchy[i].getUri();
                            Boolean isForwardNavigable = supportedHierarchy[i].getIsForwardNavigable();

                            attributeMap_ = new HashMap<String, Object>();

                            attributeMap_.put(SQLTableConstants.TBLCOL_URI, StringUtils.isEmpty(urn) ? " " : urn);

                            attributeMap_.put(SQLTableConstants.TBLCOL_VAL2, isForwardNavigable == null ? " " : String
                                    .valueOf(isForwardNavigable.booleanValue()));

                            whereClause_ = new StringBuffer();

                            if (codingScheme != null || !"".equals(codingScheme.trim())) {

                                whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ");
                                whereClause_.append("\'");
                                whereClause_.append(codingScheme);
                                whereClause_.append("\'");

                                whereClause_.append(" AND ");

                                whereClause_.append(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ");
                                whereClause_.append("\'");
                                whereClause_.append(suppAttribTag);
                                whereClause_.append("\'");

                                whereClause_.append(" AND ");

                                whereClause_.append(SQLTableConstants.TBLCOL_ID + " = ");
                                whereClause_.append("\'");
                                whereClause_.append(localId);
                                whereClause_.append("\'");

                                whereClause_.append(" AND ");

                                whereClause_.append(SQLTableConstants.TBLCOL_VAL1 + " = ");
                                whereClause_.append("\'");
                                whereClause_.append(rootCode);
                                whereClause_.append("\'");

                                whereClause_.append(" AND ");

                                whereClause_.append(SQLTableConstants.TBLCOL_IDVALUE + " = ");
                                whereClause_.append("\'");
                                whereClause_.append(manifestAssocIds);
                                whereClause_.append("\'");

                            }

                            sqlTableUtil_.updateRow(SQLTableConstants.TBL_CODING_SCHEME_SUPPORTED_ATTRIBUTES,
                                    attributeMap_, whereClause_.toString(), dbType_);

                        }
                        break;
                    }
                }

                if (!present) {
                    StringBuffer assocIds = new StringBuffer();
                    for (int j = 0; j < supportedHierarchy[i].getAssociationNamesCount(); j++) {
                        assocIds = assocIds.append(supportedHierarchy[i].getAssociationNames(j));
                        assocIds = assocIds.append(",");
                    }
                    assocIds.deleteCharAt(assocIds.length() - 1);

                    String urn = supportedHierarchy[i].getUri();
                    Boolean isForwardNavigable = supportedHierarchy[i].getIsForwardNavigable();

                    attributeMap_ = new HashMap<String, Object>();

                    attributeMap_.put("1", codingScheme);
                    attributeMap_.put("2", "Hierarchy");
                    attributeMap_.put("3", supportedHierarchy[i].getLocalId());
                    attributeMap_.put("4", StringUtils.isEmpty(urn) ? " " : urn);
                    attributeMap_.put("5", assocIds.toString());
                    attributeMap_.put("6", supportedHierarchy[i].getRootCode());
                    attributeMap_.put("7", isForwardNavigable == null ? " " : String.valueOf(isForwardNavigable
                            .booleanValue()));

                    sqlTableUtil_.insertRow(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES, attributeMap_);

                }
            }
        } catch (SQLException e) {
            throw new LgConvertException("Exception occured while updating supported Hierarchy: " + e.getMessage());
        }
    }

    /**
     * Method updates the supported mapping data from manifest with the database
     * entry for the corresponding supportedAttrib
     * 
     * @param queryResultSet
     * @param castorSupportedMaps
     * @param toUpdate
     * @param supportedAttrib
     * @throws LgConvertException
     */
    private void postLoadUpdateSupportedMapping(ResultSet queryResultSet,
            org.LexGrid.naming.URIMap[] castorSupportedMaps, boolean toUpdate, String supportedAttrib)
            throws LgConvertException {

        boolean present = false;
        String codingScheme = null;
        String suppAttribTag = null;
        String localId = null;
        Map<String, String> recentlyAddedSupportedMap = new HashMap<String, String>();

        try {
            for (int i = 0; i < castorSupportedMaps.length; i++) {

                present = false;
                queryResultSet.beforeFirst();

                while (queryResultSet.next()) {
                    codingScheme = queryResultSet.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                    suppAttribTag = queryResultSet.getString(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG);
                    localId = queryResultSet.getString(SQLTableConstants.TBLCOL_ID);
                    String id = recentlyAddedSupportedMap.get(castorSupportedMaps[i].getLocalId());

                    if (id != null
                            || (suppAttribTag.equalsIgnoreCase(supportedAttrib) && localId
                                    .equalsIgnoreCase(castorSupportedMaps[i].getLocalId()))) {
                        present = true;

                        if (toUpdate) {

                            String urn = castorSupportedMaps[i].getUri();
                            String content = castorSupportedMaps[i].getContent();

                            attributeMap_ = new HashMap<String, Object>();

                            attributeMap_.put(SQLTableConstants.TBLCOL_URI, StringUtils.isEmpty(urn) ? " " : urn);

                            attributeMap_.put(SQLTableConstants.TBLCOL_IDVALUE, StringUtils.isEmpty(content) ? " "
                                    : content);

                            if (supportedAttrib.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE)) {

                                // String agentRole = ((SupportedSource)
                                // castorSupportedMaps[i]).getAgentRole();

                                String assemblyRule = ((SupportedSource) castorSupportedMaps[i]).getAssemblyRule();

                                // attributeMap_.put(SQLTableConstants.TBLCOL_VAL1,
                                // agentRole == null ? "" : agentRole);

                                attributeMap_.put(SQLTableConstants.TBLCOL_VAL2,
                                        StringUtils.isEmpty(assemblyRule) ? " " : assemblyRule);

                            } else if (supportedAttrib
                                    .equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME)) {
                                Boolean isImported = ((SupportedCodingScheme) castorSupportedMaps[i]).getIsImported();
                                attributeMap_.put(SQLTableConstants.TBLCOL_VAL1, isImported != null ? String
                                        .valueOf(isImported.booleanValue()) : " ");

                            } else if (supportedAttrib.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE)) {
                                String equiCS = ((SupportedNamespace) castorSupportedMaps[i])
                                        .getEquivalentCodingScheme();
                                attributeMap_.put(SQLTableConstants.TBLCOL_VAL1, StringUtils.isEmpty(equiCS) ? " "
                                        : equiCS);
                            }

                            whereClause_ = new StringBuffer();

                            if (codingScheme != null || !"".equals(codingScheme.trim())) {

                                whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ");
                                whereClause_.append("\'");
                                whereClause_.append(codingScheme);
                                whereClause_.append("\'");

                                whereClause_.append(" AND ");

                                whereClause_.append(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ");
                                whereClause_.append("\'");
                                whereClause_.append(suppAttribTag);
                                whereClause_.append("\'");

                                whereClause_.append(" AND ");

                                whereClause_.append(SQLTableConstants.TBLCOL_ID + " = ");
                                whereClause_.append("\'");
                                whereClause_.append(localId);
                                whereClause_.append("\'");

                            }

                            sqlTableUtil_.updateRow(SQLTableConstants.TBL_CODING_SCHEME_SUPPORTED_ATTRIBUTES,
                                    attributeMap_, whereClause_.toString(), dbType_);

                        }
                        break;
                    }
                }

                if (!present) {

                    String urn = castorSupportedMaps[i].getUri();
                    String content = castorSupportedMaps[i].getContent();

                    attributeMap_ = new HashMap<String, Object>();

                    attributeMap_.put("1", codingScheme);
                    attributeMap_.put("2", supportedAttrib);
                    attributeMap_.put("3", castorSupportedMaps[i].getLocalId());
                    attributeMap_.put("4", StringUtils.isEmpty(urn) ? " " : urn);
                    attributeMap_.put("5", StringUtils.isEmpty(content) ? " " : content);
                    if (supportedAttrib.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE)) {

                        String assemblyRule = ((SupportedSource) castorSupportedMaps[i]).getAssemblyRule();
                        attributeMap_.put("6", StringUtils.isEmpty(assemblyRule) ? " " : assemblyRule);
                    } else if (supportedAttrib.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME)) {
                        Boolean isImported = ((SupportedCodingScheme) castorSupportedMaps[i]).getIsImported();
                        attributeMap_.put("6", isImported != null ? String.valueOf(isImported.booleanValue()) : " ");

                    } else if (supportedAttrib.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE)) {
                        String equiCodingScheme = ((SupportedNamespace) castorSupportedMaps[i])
                                .getEquivalentCodingScheme();
                        attributeMap_.put("6", StringUtils.isEmpty(equiCodingScheme) ? " " : equiCodingScheme);
                    } else {
                        attributeMap_.put("6", " ");
                    }
                    attributeMap_.put("7", " ");
                    sqlTableUtil_.insertRow(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES, attributeMap_);
                    recentlyAddedSupportedMap.put(castorSupportedMaps[i].getLocalId(), supportedAttrib);

                }
            }
        } catch (SQLException e) {
            throw new LgConvertException("Exception occured while updating supported mapping : " + e.getMessage());
        }
    }

    /**
     * Method updates the forwardName and reverseName for a given Association
     * from manifest.
     * 
     * @param assoc
     * @param rsltSet
     * @throws LgConvertException
     */
    private void postLoadUpdateAssociation(Association assoc, ResultSet rsltSet) throws LgConvertException {

        try {
            rsltSet.beforeFirst();

            while (rsltSet.next()) {

                String codingSchemeName = rsltSet.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                String containerName = rsltSet.getString(SQLTableConstants.TBLCOL_CONTAINERNAME);
                String entityCode = rsltSet.getString(SQLTableConstants.TBLCOL_ENTITYCODE);

                String forwardName = assoc.getForwardName();
                String reverseName = assoc.getReverseName();

                attributeMap_ = new HashMap<String, Object>();

                attributeMap_.put(SQLTableConstants.TBLCOL_FORWARDNAME, forwardName != null ? forwardName : " ");

                attributeMap_.put(SQLTableConstants.TBLCOL_REVERSENAME, reverseName != null ? reverseName : " ");

                whereClause_ = new StringBuffer();

                whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ");
                whereClause_.append("\'");
                whereClause_.append(codingSchemeName);
                whereClause_.append("\'");

                whereClause_.append(" AND ");

                whereClause_.append(SQLTableConstants.TBLCOL_CONTAINERNAME + " = ");
                whereClause_.append("\'");
                whereClause_.append(containerName);
                whereClause_.append("\'");

                whereClause_.append(" AND ");

                whereClause_.append(SQLTableConstants.TBLCOL_ENTITYCODE + " = ");
                whereClause_.append("\'");
                whereClause_.append(entityCode);
                whereClause_.append("\'");

                sqlTableUtil_.updateRow(SQLTableConstants.TBL_ASSOCIATION, attributeMap_, whereClause_.toString(),
                        dbType_);
            }

        } catch (SQLException e) {
            throw new LgConvertException("Exception occured while updating Association : " + e.getMessage());
        }
    }

    /**
     * Method adds the Local names from manifest into the database
     * 
     * @param castorLocalNames
     * @param queryResultSet
     * @throws LgConvertException
     */
    private void postLoadAddLocalNames(CsmfLocalName[] castorLocalNames, ResultSet queryResultSet)
            throws LgConvertException {

        boolean present = false;
        String codingScheme = null;
        String typeName = null;
        String attribvalue = null;
        List<String> recentAddedLocalnames = new ArrayList<String>();

        if (castorLocalNames != null)
            try {
                for (int i = 0; i < castorLocalNames.length; i++) {
                    present = false;
                    if (castorLocalNames[i].getToAdd().booleanValue()) {

                        queryResultSet.beforeFirst();

                        while (queryResultSet.next()) {
                            codingScheme = queryResultSet.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                            typeName = queryResultSet.getString(SQLTableConstants.TBLCOL_TYPENAME);
                            attribvalue = queryResultSet.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);

                            if (SQLTableConstants.TBLCOLVAL_LOCALNAME.equalsIgnoreCase(typeName)
                                    && (castorLocalNames[i].getContent().equalsIgnoreCase(attribvalue) || recentAddedLocalnames
                                            .contains(castorLocalNames[i].getContent()))) {
                                present = true;
                                break;
                            }
                        }

                        if (!present) {

                            attributeMap_ = new HashMap<String, Object>();

                            attributeMap_.put("1", codingScheme);
                            attributeMap_.put("2", SQLTableConstants.TBLCOLVAL_LOCALNAME);
                            attributeMap_.put("3", castorLocalNames[i].getContent());
                            attributeMap_.put("4", "");
                            attributeMap_.put("5", "");

                            sqlTableUtil_.insertRow(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES, attributeMap_);
                            recentAddedLocalnames.add(castorLocalNames[i].getContent());

                        }
                    }
                }
                if (castorLocalNames.length > 0)
                    messages_.info("Updated Local Names.");

            } catch (SQLException e) {
                throw new LgConvertException("Exception @ postLoadAddLocalNames while adding the Localnames : "
                        + e.getMessage());
            }
    }

    /**
     * Method adds the Source entires from manifest into the database
     * 
     * @param castorSource
     * @param queryResultSet
     * @throws LgConvertException
     */
    private void postLoadAddSource(CsmfSource[] castorSource, ResultSet queryResultSet) throws LgConvertException {

        boolean present = false;
        String codingScheme = null;
        String typeName = null;
        String attribvalue = null;
        String sCastorSource = null;
        List<String> recentlyAddedSource = new ArrayList<String>();

        if (castorSource != null)
            try {
                for (int i = 0; i < castorSource.length; i++) {
                    present = false;
                    queryResultSet.beforeFirst();
                    sCastorSource = castorSource[i].getContent();

                    if (recentlyAddedSource.contains(sCastorSource)) {

                        present = true;
                        postLoadUpdateSource(castorSource[i], codingScheme, SQLTableConstants.TBLCOLVAL_SOURCE);

                    } else {

                        while (queryResultSet.next()) {
                            codingScheme = queryResultSet.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME);
                            typeName = queryResultSet.getString(SQLTableConstants.TBLCOL_TYPENAME);
                            attribvalue = queryResultSet.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);

                            if (SQLTableConstants.TBLCOLVAL_SOURCE.equalsIgnoreCase(typeName)
                                    && sCastorSource.equalsIgnoreCase(attribvalue)) {

                                present = true;
                                postLoadUpdateSource(castorSource[i], codingScheme, typeName);

                                break;
                            }
                        }
                    }

                    if (!present) {
                        // Add Source
                        attributeMap_ = new HashMap<String, Object>();

                        attributeMap_.put("1", codingScheme);
                        attributeMap_.put("2", SQLTableConstants.TBLCOLVAL_SOURCE);
                        attributeMap_.put("3", castorSource[i].getContent());
                        attributeMap_.put("4", castorSource[i].getRole());
                        attributeMap_.put("5", castorSource[i].getSubRef());

                        sqlTableUtil_.insertRow(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES, attributeMap_);

                        // Add Supported Source
                        attributeMap_ = new HashMap<String, Object>();
                        attributeMap_.put("1", "*");

                        whereClause_ = new StringBuffer();

                        if (codingScheme != null || !"".equals(codingScheme.trim())) {
                            whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ");
                            whereClause_.append("\'");
                            whereClause_.append(codingScheme);
                            whereClause_.append("\'");

                            whereClause_.append(" AND ");

                            whereClause_.append(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ");
                            whereClause_.append("\'");
                            whereClause_.append(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
                            whereClause_.append("\'");
                        }

                        ResultSet rsltSet = sqlTableUtil_.extractDataFromDB(
                                SQLTableConstants.TBL_CODING_SCHEME_SUPPORTED_ATTRIBUTES, attributeMap_, whereClause_
                                        .toString(), sqlConnection_.getMetaData().getDatabaseProductName());

                        SupportedSource src = new SupportedSource();
                        src.setLocalId(sCastorSource);
                        src.setUri(castorSource[i].getSubRef());
                        src.setContent(castorSource[i].getRole());
                        src.setAssemblyRule("");

                        postLoadUpdateSupportedMapping(rsltSet, new SupportedSource[] { src }, true,
                                SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);

                        recentlyAddedSource.add(sCastorSource);
                    }

                    if (castorSource.length > 0)
                        messages_.info("Updated Sources.");
                }
            } catch (SQLException e) {
                throw new LgConvertException("Exception @ postLoadAddSource while adding sources : " + e.getMessage());
            }
    }

    /**
     * Method updates the Source entries from manifest with the database entry
     * for the corresponding codingScheme and typeName
     * 
     * @param castorSource
     * @param codingScheme
     * @param typeName
     * @throws LgConvertException
     */
    private void postLoadUpdateSource(CsmfSource castorSource, String codingScheme, String typeName)
            throws LgConvertException {

        String sCastorSource = castorSource.getContent();

        try {
            if (castorSource.getToUpdate().booleanValue()) {
                attributeMap_ = new HashMap<String, Object>();

                attributeMap_.put(SQLTableConstants.TBLCOL_VAL1, castorSource.getRole());
                attributeMap_.put(SQLTableConstants.TBLCOL_VAL2, castorSource.getSubRef());

                whereClause_ = new StringBuffer();

                if (codingScheme != null || !"".equals(codingScheme.trim())) {

                    whereClause_.append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ");
                    whereClause_.append("\'");
                    whereClause_.append(codingScheme);
                    whereClause_.append("\'");

                    whereClause_.append(" AND ");

                    whereClause_.append(SQLTableConstants.TBLCOL_TYPENAME + " = ");
                    whereClause_.append("\'");
                    whereClause_.append(typeName);
                    whereClause_.append("\'");

                    whereClause_.append(" AND ");

                    whereClause_.append(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE + " = ");
                    whereClause_.append("\'");
                    whereClause_.append(sCastorSource);
                    whereClause_.append("\'");

                }

                sqlTableUtil_.updateRow(SQLTableConstants.TBL_CODING_SCHEME_MULTI_ATTRIBUTES, attributeMap_,
                        whereClause_.toString(), dbType_);

            }
        } catch (SQLException e) {
            throw new LgConvertException("Exception @ postLoadUpdateSource while updating sources : " + e.getMessage());
        }

    }

    /**
     * This method sets the FormalName into CodingScheme.
     * 
     * @param fmlName
     * @param override
     */
    private void setFormalName(String fmlName, boolean override) {
        if (isNoop(fmlName))
            return;

        if ((override) || (isNoop(emfScheme_.getFormalName())))
            emfScheme_.setFormalName(fmlName.trim());
    }

    /**
     * This method sets the CodingScheme into CodingScheme.
     * 
     * @param csName
     * @param override
     */
    private void setCodingScheme(String csName, boolean override) {
        if (isNoop(csName))
            return;

        if ((override) || (isNoop(emfScheme_.getCodingSchemeName())))
            emfScheme_.setCodingSchemeName(csName.trim());
    }

    /**
     * This method sets the RegisteredName into CodingScheme.
     * 
     * @param regName
     * @param override
     */
    private void setRegisteredName(String regName, boolean override) {
        if (isNoop(regName))
            return;

        if ((override) || (isNoop(emfScheme_.getCodingSchemeURI()))) {
            emfScheme_.setCodingSchemeURI(regName.trim());
            List suppCodingSchemeList = emfScheme_.getMappings().getSupportedCodingScheme();
            boolean urnPresent = false;
            for (int i = 0; i < suppCodingSchemeList.size(); i++) {
                org.LexGrid.emf.naming.SupportedCodingScheme suppCodingScheme = (org.LexGrid.emf.naming.SupportedCodingScheme) suppCodingSchemeList
                        .get(i);
                if (suppCodingScheme.getLocalId().equals(emfScheme_.getCodingSchemeName())) {
                    urnPresent = true;
                    suppCodingScheme.setUri(regName);
                }
            }
            if (!urnPresent) {
                org.LexGrid.emf.naming.SupportedCodingScheme suppCodingScheme = NamingFactory.eINSTANCE
                        .createSupportedCodingScheme();
                suppCodingScheme.setLocalId(emfScheme_.getCodingSchemeName());
                suppCodingScheme.setUri(regName);
                suppCodingSchemeList.add(suppCodingScheme);
            }
        }
    }

    /**
     * This method sets the DefaultLanguage into CodingScheme.
     * 
     * @param lang
     * @param override
     */
    private void setDefaultLanguage(String lang, boolean override) {

        if (isNoop(lang))
            return;

        if ((override) || isNoop(emfScheme_.getDefaultLanguage())) {
            emfScheme_.setDefaultLanguage(lang.trim());
            List suppLanguageList = emfScheme_.getMappings().getSupportedLanguage();
            boolean urnPresent = false;

            for (int i = 0; i < suppLanguageList.size(); i++) {
                org.LexGrid.emf.naming.SupportedLanguage suppLanguage = (org.LexGrid.emf.naming.SupportedLanguage) suppLanguageList
                        .get(i);
                if (suppLanguage.getLocalId().equals(emfScheme_.getDefaultLanguage())) {
                    urnPresent = true;
                    suppLanguage.setUri(emfScheme_.getCodingSchemeURI());
                }
            }

            if (!urnPresent) {
                org.LexGrid.emf.naming.SupportedLanguage suppLanguage = NamingFactory.eINSTANCE
                        .createSupportedLanguage();
                suppLanguage.setLocalId(emfScheme_.getDefaultLanguage());
                suppLanguage.setUri(emfScheme_.getCodingSchemeURI());
                suppLanguageList.add(suppLanguage);
            }
        }
    }

    /**
     * This method sets the RepresentsVersion into CodingScheme.
     * 
     * @param version
     * @param override
     */
    private void setRepresentsVersion(String version, boolean override) {

        if (isNoop(version))
            return;

        if ((override) || isNoop(emfScheme_.getRepresentsVersion())) {
            emfScheme_.setRepresentsVersion(version.trim());
            return;
        }
    }

    /**
     * This method sets the CopyrightText into CodingScheme.
     * 
     * @param text
     * @param override
     */
    private void setCopyrightText(String text, boolean override) {
        if (isNoop(text))
            return;

        String copyright = null;

        if (emfScheme_.getCopyright() != null)
            copyright = emfScheme_.getCopyright().getValue();

        if ((override) || isNoop(copyright)) {
            Text txt = CommontypesFactory.eINSTANCE.createText();
            txt.setValue((String) text.trim());
            emfScheme_.setCopyright(txt);
            return;
        }

    }

    /**
     * This method sets the Entity Description into CodingScheme.
     * 
     * @param desc
     * @param override
     */
    private void setEntityDescription(String desc, boolean override) {
        if (isNoop(desc))
            return;

        if ((override) || isNoop(emfScheme_.getEntityDescription())) {
            emfScheme_.setEntityDescription(desc.trim());
        }
    }

    /**
     * Indicates whether the given string represents a null or empty resource.
     * 
     * @param s
     * @return boolean
     */
    private boolean isNoop(String s) {
        return s == null || s.equalsIgnoreCase("null") || s.trim().length() == 0;
    }

    /**
     * This method adds the sources from manifest into CodingScheme. NOTE:
     * Sources present in the manifest are also added to the Supported Coding
     * Scheme
     * 
     * @param castorSources
     */
    @SuppressWarnings("unchecked")
    private void preLoadAddSources(CsmfSource[] castorSources) {
        List<Source> emfSources = emfScheme_.getSource();
        List<SupportedSource> supportedSourcesList = new ArrayList();
        Source tempEmfSource = null;
        boolean present = false;

        for (int i = 0; i < castorSources.length; i++) {

            if (castorSources[i] != null && StringUtils.isNotBlank(castorSources[i].getContent())) {

                present = false;
                for (int j = 0; j < emfSources.size(); j++) {
                    tempEmfSource = (Source) emfSources.get(j);
                    if (tempEmfSource.getValue().equalsIgnoreCase(Utility.trim(castorSources[i].getContent()))) {
                        present = true;
                        // if toUpdate = true, and given source already
                        // exists (i.e present = true), update with new
                        // values.
                        if (castorSources[i].getToUpdate().booleanValue()) {
                            tempEmfSource.setRole(Utility.trim(castorSources[i].getRole()));
                            tempEmfSource.setSubRef(Utility.trim(castorSources[i].getSubRef()));
                        }
                        break;
                    }
                }

                if (!present) {
                    // Add castorSources data to the Sources of emfScheme_
                    Source emfSource = CommontypesFactory.eINSTANCE.createSource();
                    emfSource.setValue(Utility.trim(castorSources[i].getContent()));
                    emfSource.setRole(Utility.trim(castorSources[i].getRole()));
                    emfSource.setSubRef(Utility.trim(castorSources[i].getSubRef()));
                    emfScheme_.getSource().add(emfSource);

                    // Add castorSources data to the List EMF
                    // SupportedSource objects
                    SupportedSource emfSupportedSource = new SupportedSource();
                    emfSupportedSource.setContent(Utility.trim(castorSources[i].getContent()));
                    emfSupportedSource.setLocalId(Utility.trim(castorSources[i].getContent()));
                    // emfSupportedSource.setAgentRole(Utility.trim(castorSources[i].getRole()));
                    emfSupportedSource.setUri(Utility.trim(castorSources[i].getSubRef()));
                    supportedSourcesList.add(emfSupportedSource);
                }
            }
        }

        // Add the EMF SupportedSource objects to Supported Sources of
        // emfScheme_ mappings
        if (supportedSourcesList.size() > 0) {
            preLoadUpdateSupportedMapping(emfScheme_.getMappings().getSupportedSource(), supportedSourcesList
                    .toArray(new org.LexGrid.naming.URIMap[supportedSourcesList.size()]), false,
                    "createSupportedSource");
        }
    }

    /**
     * This method adds the LocalNames details from manifest into CodingScheme.
     * 
     * @param castorLocalNames
     */
    @SuppressWarnings("unchecked")
    private void preLoadAddLocalNames(CsmfLocalName[] castorLocalNames) {

        if (castorLocalNames != null) {

            List<String> emfLocalNames = emfScheme_.getLocalName();
            boolean present = false;

            for (int i = 0; i < castorLocalNames.length; i++) {
                // if toUpdate = true then add otherwise only add when CS does
                // not have any local names
                if (castorLocalNames[i] != null && StringUtils.isNotBlank(castorLocalNames[i].getContent())) {

                    present = false;

                    if ((castorLocalNames[i].getToAdd().booleanValue()) || (emfScheme_.getLocalName().isEmpty())) {

                        for (int j = 0; j < emfLocalNames.size(); j++) {

                            if (emfLocalNames.get(j).equalsIgnoreCase(Utility.trim(castorLocalNames[i].getContent()))) {
                                present = true;
                                break;
                            }
                        }

                        if (!present) {
                            emfScheme_.getLocalName().add(Utility.trim(castorLocalNames[i].getContent()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Method adds the supported mapping data.
     * 
     * @param castorMappings
     */
    private void preLoadAddSupportedMappings(CsmfMappings castorMappings) {

        Mappings emfMappings = emfScheme_.getMappings();

        if (castorMappings != null) {

            boolean toUpdate = castorMappings.getToUpdate().booleanValue();

            // Supported CodingScheme
            preLoadUpdateSupportedMapping(emfMappings.getSupportedCodingScheme(), castorMappings
                    .getSupportedCodingScheme(), toUpdate, "createSupportedCodingScheme");

            // Supported Source
            preLoadUpdateSupportedMapping(emfMappings.getSupportedSource(), castorMappings.getSupportedSource(),
                    toUpdate, "createSupportedSource");

            // Supported Language
            preLoadUpdateSupportedMapping(emfMappings.getSupportedLanguage(), castorMappings.getSupportedLanguage(),
                    toUpdate, "createSupportedLanguage");

            // Supported Property
            preLoadUpdateSupportedMapping(emfMappings.getSupportedProperty(), castorMappings.getSupportedProperty(),
                    toUpdate, "createSupportedProperty");

            // Supported Association
            preLoadUpdateSupportedMapping(emfMappings.getSupportedAssociation(), castorMappings
                    .getSupportedAssociation(), toUpdate, "createSupportedAssociation");

            // Supported Context
            preLoadUpdateSupportedMapping(emfMappings.getSupportedContext(), castorMappings.getSupportedContext(),
                    toUpdate, "createSupportedContext");

            // Supported AssociationQualifier
            preLoadUpdateSupportedMapping(emfMappings.getSupportedAssociationQualifier(), castorMappings
                    .getSupportedAssociationQualifier(), toUpdate, "createSupportedAssociationQualifier");

            // Supported RepresentationalForm
            preLoadUpdateSupportedMapping(emfMappings.getSupportedRepresentationalForm(), castorMappings
                    .getSupportedRepresentationalForm(), toUpdate, "createSupportedRepresentationalForm");

            // Supported PropertyLink
            preLoadUpdateSupportedMapping(emfMappings.getSupportedPropertyLink(), castorMappings
                    .getSupportedPropertyLink(), toUpdate, "createSupportedPropertyLink");

            // Supported DegreeOfFidelity
            preLoadUpdateSupportedMapping(emfMappings.getSupportedDegreeOfFidelity(), castorMappings
                    .getSupportedDegreeOfFidelity(), toUpdate, "createSupportedDegreeOfFidelity");

            // Supported PropertyQualifier
            preLoadUpdateSupportedMapping(emfMappings.getSupportedPropertyQualifier(), castorMappings
                    .getSupportedPropertyQualifier(), toUpdate, "createSupportedPropertyQualifier");

            // Supported Hierarchy
            preLoadUpdateSupportedHierarchy(emfMappings, castorMappings.getSupportedHierarchy(), toUpdate);
        }
    }

    /**
     * Method compares given supported mapping data with the existing EMF coding
     * scheme supported mappings and adds if doesn't exist.
     * 
     * @param emfMapList
     * @param castorSupportedMaps
     * @param toUpdate
     * @param methodToInvoke
     */
    @SuppressWarnings("unchecked")
    private void preLoadUpdateSupportedMapping(List emfMapList, org.LexGrid.naming.URIMap[] castorSupportedMaps,
            boolean toUpdate, String methodToInvoke) {

        Class[] mtClassList = new Class[0];
        Object[] mtObjectList = new Object[0];

        Method method = null;
        URIMap emfURNMap = null;
        boolean present = false;
        if (emfMapList == null || castorSupportedMaps == null) {
            return;
        }
        for (int i = 0; i < castorSupportedMaps.length; i++) {
            org.LexGrid.naming.URIMap tempCastorMap = castorSupportedMaps[i];
            if (tempCastorMap != null) {
                present = false;
                for (Iterator itr = emfMapList.iterator(); itr.hasNext();) {
                    URIMap supportedMap = (URIMap) itr.next();
                    if (tempCastorMap.getLocalId().equalsIgnoreCase(supportedMap.getLocalId().trim())) {
                        present = true;

                        if (toUpdate) {
                            supportedMap.setUri(Utility.trim(tempCastorMap.getUri()));
                            supportedMap.setValue(Utility.trim(tempCastorMap.getContent()));
                            if (tempCastorMap instanceof SupportedSource) {
                                // ((org.LexGrid.emf.naming.SupportedSource)
                                // supportedMap).setAgentRole(Utility
                                // .trim(((SupportedSource)
                                // tempCastorMap).getAgentRole()));
                                ((org.LexGrid.emf.naming.SupportedSource) supportedMap).setAssemblyRule(Utility
                                        .trim(((SupportedSource) tempCastorMap).getAssemblyRule()));
                            } else if (tempCastorMap instanceof SupportedCodingScheme) {
                                ((org.LexGrid.emf.naming.SupportedCodingScheme) supportedMap)
                                        .setIsImported(((SupportedCodingScheme) tempCastorMap).getIsImported());
                            }
                        }
                        break;
                    }
                }

                if (!present) {
                    try {
                        method = NamingFactory.eINSTANCE.getClass().getMethod(methodToInvoke, mtClassList);
                        emfURNMap = (URIMap) method.invoke(NamingFactory.eINSTANCE, mtObjectList);
                    } catch (Exception e) {
                        messages_.error("Exception @ updateSupportedMappings: Could not invoke the supplied method.");
                        e.printStackTrace();
                    }

                    emfURNMap.setUri(Utility.trim(tempCastorMap.getUri()));
                    emfURNMap.setLocalId(Utility.trim(tempCastorMap.getLocalId()));
                    emfURNMap.setValue(Utility.trim(tempCastorMap.getContent()));
                    if (tempCastorMap instanceof SupportedSource) {
                        // ((org.LexGrid.emf.naming.SupportedSource)
                        // emfURNMap).setAgentRole(Utility
                        // .trim(((SupportedSource)
                        // tempCastorMap).getAgentRole()));
                        ((org.LexGrid.emf.naming.SupportedSource) emfURNMap).setAssemblyRule(Utility
                                .trim(((SupportedSource) tempCastorMap).getAssemblyRule()));
                    } else if (tempCastorMap instanceof SupportedCodingScheme) {
                        ((org.LexGrid.emf.naming.SupportedCodingScheme) emfURNMap)
                                .setIsImported(((SupportedCodingScheme) tempCastorMap).getIsImported());
                    }
                    emfMapList.add(emfURNMap);
                }
            }
        }
    }

    /**
     * Metod to add the SupportedHierarchy from manifest to CodingScheme
     * 
     * @param castorSupportedHiers
     * @param emfMappings
     */
    @SuppressWarnings("unchecked")
    private void preLoadUpdateSupportedHierarchy(Mappings emfMappings,
            org.LexGrid.naming.SupportedHierarchy[] castorSupportedHiers, boolean toUpdate) {

        if (castorSupportedHiers != null) {
            for (int i = 0; i < castorSupportedHiers.length; i++) {
                org.LexGrid.naming.SupportedHierarchy tempCastorSuppHier = castorSupportedHiers[i];
                if (tempCastorSuppHier != null) {
                    // Is this candidate already registered as a supported
                    // hierarchy for this scheme?
                    boolean present = false;
                    for (Iterator itr = emfMappings.getSupportedHierarchy().iterator(); itr.hasNext();) {
                        SupportedHierarchy sHierEMF = (SupportedHierarchy) itr.next();
                        List registeredAssociations = arrayToList(tempCastorSuppHier.getAssociationNames(0).split(","));
                        List candidateAssociations = sHierEMF.getAssociationNames();

                        Collections.sort(registeredAssociations);
                        Collections.sort(candidateAssociations);

                        // Compare basic identifying attributes against the
                        // previously registered hierarchy ...
                        if (tempCastorSuppHier.getLocalId().equalsIgnoreCase(Utility.trim(sHierEMF.getLocalId()))
                                && tempCastorSuppHier.getRootCode().equalsIgnoreCase(
                                        Utility.trim(sHierEMF.getRootCode()))
                                && registeredAssociations.equals(candidateAssociations)) {
                            // compareList(registeredAssociations,
                            // candidateAssociations)) {

                            present = true;
                            if (toUpdate) {
                                sHierEMF.setUri(tempCastorSuppHier.getUri());
                                sHierEMF.setValue(tempCastorSuppHier.getContent());
                                sHierEMF.setIsForwardNavigable(tempCastorSuppHier.getIsForwardNavigable());
                            }
                            break;
                        }
                    }

                    // No matching hierarchy; add it now...
                    if (!present) {
                        SupportedHierarchy emfSuppHier = NamingFactory.eINSTANCE.createSupportedHierarchy();
                        emfSuppHier.setLocalId(Utility.trim(tempCastorSuppHier.getLocalId()));
                        emfSuppHier.setUri(Utility.trim(tempCastorSuppHier.getUri()));
                        emfSuppHier.setValue(Utility.trim(tempCastorSuppHier.getContent()));

                        List lHierAssoc = arrayToList(tempCastorSuppHier.getAssociationNames(0).split(","));
                        emfSuppHier.setAssociationNames(lHierAssoc);
                        emfSuppHier.setRootCode(Utility.trim(tempCastorSuppHier.getRootCode()));
                        emfSuppHier.setIsForwardNavigable(tempCastorSuppHier.getIsForwardNavigable().booleanValue());

                        emfMappings.getSupportedHierarchy().add(emfSuppHier);
                    }
                }
            }
        }
    }

    /**
     * Method populates the elements of given String array into a List.
     * 
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<String> arrayToList(String[] str) {
        List list = new ArrayList();
        for (int i = 0; i < str.length; i++) {
            list.add(str[i].trim());
        }
        return list;
    }

    /**
     * Find out what was the problem when a validation was done. This returns
     * the error message/prblem with the file, if no change has made to correct
     * the file since last validation.
     * 
     * @return - Validation error.
     */
    public String getLastValidationResults() {
        return lastValidationError_;
    }

    /**
     * This method returns the location of the manifest file.
     * 
     * @return manifest URI
     */
    public URI getManifestLocation() {
        return manifestURI_;
    }

    /**
     * Add a supportedCodingScheme entry into the manifest. If the localdId is
     * already present in the manifest, update the content with the value
     * provided
     */
    public SupportedCodingScheme addSupportedCodingSchemeToManifest(CodingSchemeManifest manifest, String localId,
            String uri, boolean isImported) {
        CsmfMappings mappings = manifest.getMappings();
        if (mappings == null) {
            mappings = new CsmfMappings();
            manifest.setMappings(mappings);
        }

        SupportedCodingScheme[] scs_array = mappings.getSupportedCodingScheme();

        if (scs_array != null) {
            // Check if the localId is already present in the manifest, and if
            // so, overwrite
            for (SupportedCodingScheme scs : scs_array) {
                if (scs.getLocalId().equalsIgnoreCase(localId)) {
                    scs.setUri(uri);
                    scs.setIsImported(isImported);
                    return scs;
                }
            }
        }
        // No match, add the new supportedCodingScheme
        SupportedCodingScheme scm = new SupportedCodingScheme();
        scm.setLocalId(localId);
        scm.setUri(uri);
        scm.setIsImported(isImported);
        manifest.getMappings().addSupportedCodingScheme(scm);
        manifest.getMappings().setToUpdate(true);
        return scm;
    }

}