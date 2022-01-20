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
package org.LexGrid.LexBIG.Impl.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.OWL_Exporter;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.exporters.BaseExporter;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.logging.LoggerFactory;

//import edu.mayo.informatics.lexgrid.convert.exporters.owlrdf.LexGridToOwlRdfConverter;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;

/**
 * Exports content to OWL/RDF format.
 */
public class OwlRdfExporterImpl extends BaseExporter implements OWL_Exporter {

    private static final long serialVersionUID = -97175077552869283L;
    public final static String name = "OwlRdfExporter";
    private final static String description = "This loader exports LexGrid to OWL RDF";

    private CodedNodeGraph cng;
    private CodedNodeSet cns;

    public void setCns(CodedNodeSet cns) {
        this.cns = cns;
    }
    
    public void setCng(CodedNodeGraph cng) {
        this.cng = cng;
    }
    
    
    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    public OwlRdfExporterImpl() {
        super.name_ = OwlRdfExporterImpl.name;
        super.description_ = OwlRdfExporterImpl.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(OwlRdfExporterImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(OwlRdfExporterImpl.class.getName());
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
    
    protected void doExport() {
        exportCodingSchemeData();
    }
    
    private void verifyOutputDirectory(String directory) {
        if(directory == null) {
            String msg = "Output location value is null.";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);            
        }
        
        File F_directory = new File(directory);
        
        if(F_directory.exists() == false) {
            String msg = F_directory.getAbsolutePath() + " does not exist.";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);                        
        }
        
        if(F_directory.isDirectory() == false) {
            String msg = F_directory.getAbsolutePath() + " is not a directory.";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);                        
        }
    }    
    
    private String getCodingSchemeName(String csUri, String csVersion) {
        String rv = null;
        try {
            CodingScheme cs = LexBIGServiceImpl.defaultInstance().resolveCodingScheme(csUri, 
                    Constructors.createCodingSchemeVersionOrTagFromVersion(csVersion));
            rv = cs.getCodingSchemeName();
        } catch (LBException e) {
            e.printStackTrace();
        }
        return rv;
    }
    
    @Deprecated
    protected void exportCodingSchemeData(){
        
       //Converter removed.  We no longer support Jena based exportations
        throw new UnsupportedOperationException("We no longer support Jena based exportations");
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
       holder.setIsResourceUriFolder(true);
       holder.getBooleanOptions().add(new BooleanOption(LexGridConstants.OPTION_FORCE, (new Boolean(false))));
       return holder;
    }

    @Override
    public OptionHolder getOptions() {
        return super.getOptions();
    }

    @Override
    public void export(AbsoluteCodingSchemeVersionReference source, URI destination, boolean overwrite,
            boolean stopOnErrors, boolean async) throws LBException {
        // TODO: not sure how we should handle stopOnError and async
        //       - currently, async gets set to true by the super
        //       - not sure how stopOneErrors is used
        super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).setOptionValue(overwrite);
        super.export(source, destination);
    }       
}