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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.LexGrid_Exporter;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Exporter for OBO files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexGridExport extends BaseExporter implements LexGrid_Exporter {

    private static final long serialVersionUID = -97175077552869283L;
    public final static String name = "LexGridExport";
    private final static String description = "This loader exports LexGrid XML files";

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public LexGridExport() {
        super.name_ = LexGridExport.name;
        super.description_ = LexGridExport.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexGridExport.class.getInterfaces()[0].getName());
        temp.setExtensionClass(LexGridExport.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        temp.setVersion(version_);

        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerExportExtension(temp);
    }

    public void export(AbsoluteCodingSchemeVersionReference source, URI destination, boolean overwrite,
            boolean stopOnErrors, boolean async) throws LBException {
        setInUse();

        try {
            setupInput(source);
            out_ = new LexGridXMLOut(new File(destination).getAbsolutePath());
            out_.testConnection();

            options_.add(new Option(Option.DO_WITH_EMF, new Boolean(true)));
            options_.add(new Option(Option.SQL_FETCH_SIZE, new String(Constants.mySqlBatchSize + "")));
            options_.add(new Option(Option.OVERWRITE, new Boolean(overwrite)));
            options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));

            status_ = new ExportStatus();
            status_.setDestination(source.toString());
            baseExport(async);
        } catch (ConnectionFailure e) {
            inUse = false;
            throw new LBParameterException("The OBO file path appears to be invalid - " + e);

        } catch (MissingResourceException e) {
            inUse = false;
            String id = getLogger().error("Problem getting sql info for export", e);
            throw new LBInvocationException(
                    "There was an unexpected problem getting the SQL information for exporting", id);
        }
    }

    public URI getSchemaURL() {
        try {
            return new URI("http://LexGrid.org/schema/" + getSchemaVersion() + "/LexGrid/service.xsd");
        } catch (URISyntaxException e) {
            getLogger().error("Unexpected Error", e);
            return null;
        }
    }

    public String getSchemaVersion() {
        return "2005/01";
    }
}