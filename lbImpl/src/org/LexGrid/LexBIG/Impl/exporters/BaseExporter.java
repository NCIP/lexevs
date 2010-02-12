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
package org.LexGrid.LexBIG.Impl.exporters;

import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.lexgrid.convert.formats.ConversionLauncher;
import edu.mayo.informatics.lexgrid.convert.formats.OptionHolder;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridSQL;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Base class with common methods for exporters.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class BaseExporter {
    boolean inUse = false;
    public String name_;
    public static String version_ = edu.mayo.informatics.lexgrid.convert.utility.Constants.version;
    public String description_;
    public String provider_ = "MAYO";

    MessageDirector md_;
    ExportStatus status_;

    LexGridSQL in_;
    OutputFormatInterface out_;

    OptionHolder options_ = new OptionHolder();
    String internalCodeSystemName_;

    protected void baseExport(boolean async) {
        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));
        md_ = new MessageDirector(getName(), status_);

        if (async) {
            Thread conversion = new Thread(new DoConversion());
            conversion.start();
        } else {
            new DoConversion().run();
        }
    }

    private class DoConversion implements Runnable {
        public void run() {
            try {
                @SuppressWarnings("unused")
                URNVersionPair[] loadedCodingSchemes = ConversionLauncher.startConversion(in_, out_,
                        new String[] { internalCodeSystemName_ }, options_, md_);
                loadedCodingSchemes = ConversionLauncher
                        .finishConversion(in_, out_, loadedCodingSchemes, options_, md_);
                status_.setState(ProcessState.COMPLETED);
                md_.info("Export process completed without error");
            } catch (Exception e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal("Failed while running the export", e);
            } finally {
                if (status_.getState() == null || status_.getState().getType() != ProcessState.COMPLETED_TYPE) {
                    status_.setState(ProcessState.FAILED);
                }
                status_.setEndTime(new Date(System.currentTimeMillis()));
                inUse = false;
            }

        }
    }

    protected void setInUse() throws LBInvocationException {
        if (inUse) {
            throw new LBInvocationException(
                    "This loader is already in use.  Construct a new loader to do two operations at the same time", "");
        }
        inUse = true;
    }

    protected void setupInput(AbsoluteCodingSchemeVersionReference source) throws LBParameterException,
            MissingResourceException {
        internalCodeSystemName_ = ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(
                source.getCodingSchemeURN(), source.getCodingSchemeVersion());

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodeSystemName_,
                source.getCodingSchemeVersion());

        JDBCConnectionDescriptor jcd = si.getConnectionDescriptor();

        in_ = new LexGridSQL(jcd.getDbUid(), jcd.getDbPwd(), jcd.getDbUrl(), jcd.getDbDriver(), si.getTablePrefix());
    }

    public ExportStatus getStatus() {
        return status_;
    }

    public String getProvider() {
        return provider_;
    }

    public String getName() {
        return name_;
    }

    public String getDescription() {
        return description_;
    }

    public String getVersion() {
        return version_;
    }

    public LogEntry[] getLog(LogLevel level) {
        if (md_ == null) {
            return new LogEntry[] {};
        }
        return md_.getLogEntries(level);
    }

    public void clearLog() {
        if (md_ != null) {
            md_.clearMessages();
        }
    }

    public URI[] getReferences() {
        try {
            return new URI[] { new URI(status_.getDestination()) };
        } catch (Exception e) {
            return new URI[] {};
        }
    }
}