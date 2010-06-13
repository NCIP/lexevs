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

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.OwlRdf_Exporter;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;

/**
 * Exports content to OWL/RDF format.
 */
public class OwlRdfExport extends BaseExporter implements OwlRdf_Exporter {

    private static final long serialVersionUID = -97175077552869283L;
    public final static String name = "OwlRdfExport";
    private final static String description = "This loader exports LexGrid to OWL RDF";

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    public OwlRdfExport() {
        super.name_ = OwlRdfExport.name;
        super.description_ = OwlRdfExport.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(OwlRdfExport.class.getInterfaces()[0].getName());
        temp.setExtensionClass(OwlRdfExport.class.getName());
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
    
    protected void exportCodingSchemeData(){
        URI destination = super.getResourceUri();
        AbsoluteCodingSchemeVersionReference source = super.getSource();
        
        boolean overwrite = super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).getOptionValue().booleanValue();
        
        // construct out file name
        String separator = File.separator;
        String outDirWithEndingPathSeparator = destination.getPath();
        if(outDirWithEndingPathSeparator.endsWith(separator) == false) {
            outDirWithEndingPathSeparator = outDirWithEndingPathSeparator + separator;
        }
        
        String codingSchemeUri = source.getCodingSchemeURN();
        String codingSchemeVersion = source.getCodingSchemeVersion();
        String codingSchemeName = this.getCodingSchemeName(codingSchemeUri, codingSchemeVersion);
        String outFileName = outDirWithEndingPathSeparator + codingSchemeName + 
                    "_" + codingSchemeVersion + ".xml";  
        
        File outFile = new File(outFileName);
        
        System.out.println("Content will be exported to file: " + outFile.getAbsolutePath());
                
        if(outFile.exists() == true && overwrite == true) 
        {
            outFile.delete();
        } else if (outFile.exists() == true && overwrite == false) {
            String msg = "Output file \"" + outFileName + "\" already exists. Set force option to overwrite an existing file.";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);
        } else {
            // outFile did not exist.  do nothing.
        }
        
        // enter exporter code here
        
    }
    
    
    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
       holder.getResourceUriAllowedFileTypes().add("xml");
       holder.setIsResourceUriFolder(false);
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