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
package org.LexGrid.LexBIG.Impl.loaders;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.HL7_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.emfConversions.hl7.HL72EMFConstants;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.HL7SQL;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * This loader loads a series of coding schemes to a LexBIG service according to
 * a mapping of HL7 RIM database elements to LexBIG/LexGrid.
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 * 
 */
public class HL7LoaderImpl extends BaseLoader implements HL7_Loader {

    private static final long serialVersionUID = 8781875750618588633L;
    public static final String name = "HL7Loader";
    private static final String description = "This loader loads coding schemes from the HL7 RIM database"
            + " into a LexGrid database.";
    // local constants
    private static final String HL7_DESIGNATION = "HL7 Reference Information Model";
    private static final String MSACCESS_SERVER = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
    private static final String MSACCESS_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
    private ArrayList<URNVersionPair> hl7CodingSchemes = new ArrayList<URNVersionPair>();
    ArrayList<String> urns = new ArrayList<String>();

    public String metaDataFileLocation;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public HL7LoaderImpl() {
        super();
    }

    // Normally we pass a string naming the driver here. We don't offer a choice
    // of drivers for MS Access
    // so it's hard coded as the constant, MSACCESS_DRIVER.
    public void load(String dbName, boolean stopOnErrors, boolean async) throws LBParameterException,
            LBInvocationException {

        if (!System.getProperties().getProperty("os.name").contains("Windows")) {
            throw new LBInvocationException(
                    "This loader loads from MS Access and as a result can only be run from Microsoft Windows", "");
        }
        if (dbName.startsWith("/")) {
            dbName = dbName.substring(1);
        } else if (dbName.startsWith("file")) {
            dbName = dbName.substring(6);
        }

        Connection c = null;
        PreparedStatement getCodingSchemeInfo = null;
        ResultSet results = null;
        String codingSchemeName = null;
        String representsVersion = null;

        // Get the coding scheme information
        // and close the connection to Access to keep it happy.
        try {

            // Conditional to take care path problems for MS ACCESS
            if (dbName.contains("%20"))
                dbName = URLDecoder.decode(dbName, "UTF-8");
            c = DBUtility.connectToDatabase(MSACCESS_SERVER + dbName.toString(), MSACCESS_DRIVER, null, null);
            getCodingSchemeInfo = c.prepareStatement("SELECT modelID, versionNumber FROM Model");
            results = getCodingSchemeInfo.executeQuery();
            results.next();
            codingSchemeName = results.getString(1);
            representsVersion = results.getString(2);
            if (representsVersion == null) {
                representsVersion = SQLTableConstants.TBLCOLVAL_MISSING;
            }
            hl7CodingSchemes.add(new URNVersionPair(codingSchemeName, representsVersion));
            urns.add(HL72EMFConstants.DEFAULT_URN);

        } catch (Exception e) {

            getLogger().warn(
                    "Failed to get Coding scheme " + codingSchemeName == null ? "unkown" : codingSchemeName
                            + " from HL7 RIM database", e);
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    results.close();
                    c.close();
                } catch (SQLException e) {
                    // noop
                }
            }
        }

        getStatus().setLoadSource("Provided SQL database " + dbName.toString());
        getStatus().setState(ProcessState.PROCESSING);
        getStatus().setStartTime(new Date(System.currentTimeMillis()));

        setInUse();

        baseLoad(async);

    }

    public void validate(String dbName, int validationLevel) throws LBException {

        Connection c = null;
        try {
            setInUse();
            String dbConnectionString = MSACCESS_SERVER + dbName;
            c = DBUtility.connectToDatabase(dbConnectionString, MSACCESS_DRIVER, null, null);
            PreparedStatement getCodingSchemeInfo = c.prepareStatement("SELECT name from Model");

            ResultSet results = getCodingSchemeInfo.executeQuery();
            results.next();
            String name = results.getString("name");
            name.trim();
            if (name.contains(HL7_DESIGNATION)) {
                System.out.println(name);
            } else {
                inUse = false;
                throw new LBParameterException("This is not a valid HL7 database", "HL7 database name", name);
            }
            results.close();

        } catch (Exception e) {
            inUse = false;
            getLogger().error("Problem connecting to the Access database containing the RIM database", e);
            throw new LBParameterException(
                    "There was a problem connecting to the Access instance containing the RIM database."
                            + "  Perhaps one of the connection parameters is incorrect.  " + e.toString(), "");
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    // noop
                }
            }
        }
    }
    
    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        holder.getBooleanOptions().add(new BooleanOption("FAIL_ON_ERROR", false));
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(HL7LoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(HL7LoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
    }

    // Will only be called after load method
    // Overrides BaseLoader method
    public AbsoluteCodingSchemeVersionReference[] getCodingSchemeReferences() {
        AbsoluteCodingSchemeVersionReference[] schemesToActivate = new AbsoluteCodingSchemeVersionReference[hl7CodingSchemes
                .size()];
        for (int i = 0; i < hl7CodingSchemes.size(); i++) {
            schemesToActivate[i] = new AbsoluteCodingSchemeVersionReference();
            schemesToActivate[i].setCodingSchemeURN(urns.get(i));
            schemesToActivate[i].setCodingSchemeVersion(hl7CodingSchemes.get(i).getVersion());
        }
        return schemesToActivate;
    }

    public String getMetaDataFileLocation() {
        return metaDataFileLocation;
    }

    public void setMetaDataFileLocation(String metaDataFileLocation) {
        this.metaDataFileLocation = metaDataFileLocation;
    }
}