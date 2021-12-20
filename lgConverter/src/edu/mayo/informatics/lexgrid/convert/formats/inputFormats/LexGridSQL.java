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
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.LexGrid.util.sql.sqlReconnect.WrappedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.SQLBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.ComputeTransitiveExpansionTable;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.DeleteLexGridTerminology;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.IndexLexGridDatabase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLLiteOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.OBOOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.RegisterLexGridTerminology;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;
import edu.mayo.informatics.lexgrid.convert.utility.StringComparator;

/**
 * Details for connecting to SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: 8601 $ checked in on $Date: 2008-06-05
 *          22:28:55 +0000 (Thu, 05 Jun 2008) $
 */
public class LexGridSQL extends SQLBase implements InputFormatInterface {
    protected static Logger log = LogManager.getLogger("convert.gui");

    public static final String description = "LexGrid SQL Database";

    public LexGridSQL(String username, String password, String server, String driver, String tablePrefix) {
        this.username = username;
        this.password = password;
        this.server = server;
        this.driver = driver;
        this.tablePrefix = tablePrefix;
    }

    public LexGridSQL() {

    }

    public String getConnectionSummary() {
        return getConnectionSummary(description);
    }

    public String getDescription() {
        return description;
    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.SQL_FETCH_SIZE, new String(Constants.mySqlBatchSize + "")) };
    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridXMLOut.description, LexGridSQLOut.description, LexGridSQLLiteOut.description,
                LexGridLDAPOut.description, DeleteLexGridTerminology.description,
                RegisterLexGridTerminology.description, IndexLexGridDatabase.description,
                ComputeTransitiveExpansionTable.description, OBOOut.description };
    }

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError {
        try {
            String[] terminologies = new String[] {};

            ArrayList temp = new ArrayList();

            DriverManager.setLoginTimeout(5);
            Connection sqlConnection;
            try {
                sqlConnection = new WrappedConnection(getUsername(), getPassword(), getDriver(), getServer());

            } catch (ClassNotFoundException e1) {
                log.error("The class you specified for your sql driver could not be found on the path.");
                throw new ConnectionFailure(
                        "The class you specified for your sql driver could not be found on the path.");
            }

            SQLTableConstants stc = new SQLTableUtilities(sqlConnection, getTablePrefix()).getSQLTableConstants();

            PreparedStatement getTerminologies = sqlConnection.prepareStatement("Select "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " from "
                    + stc.getTableName(SQLTableConstants.CODING_SCHEME));
            ResultSet results = getTerminologies.executeQuery();
            while (results.next()) {
                temp.add(results.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME));
            }
            results.close();

            getTerminologies.close();
            sqlConnection.close();

            terminologies = (String[]) temp.toArray(new String[temp.size()]);
            Arrays.sort(terminologies, new StringComparator());

            return terminologies;
        } catch (ConnectionFailure e) {
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while getting the terminologies.", e);
            throw new UnexpectedError("An error occurred while getting the terminologies.", e);
        }
    }

}