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

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.OBO_Exporter;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;

import edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2.LG2OBO;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;

/**
 * Exporter for OBO files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOExport extends BaseExporter implements OBO_Exporter {

    private static final long serialVersionUID = -3420377793656375062L;
    public final static String name = "OBOExport";
    private final static String description = "This loader exports OBO files";


    public OBOExport() {
        super.name_ = OBOExport.name;
        super.description_ = OBOExport.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(OBOExport.class.getInterfaces()[0].getName());
        temp.setExtensionClass(OBOExport.class.getName());
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
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(OVERWRITE_OPTION).setOptionValue(overwrite);
       
        this.export(source, destination);
    }

    public String getOBOVersion() {
        return "1.2";

    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        this.getOptions().getBooleanOptions().add(
                new BooleanOption(OVERWRITE_OPTION, false));
        
        return holder;
    }

    @Override
    protected void doExport() throws Exception {
        LG2OBO lg2Obo = new LG2OBO(this.getSource().getCodingSchemeURN(),
                this.getSource().getCodingSchemeVersion(), this.getMessageDirector());
        
        File file = new File(this.getResourceUri());
        
        if (file.isDirectory())
        {
            String fileName = this.getSource().getCodingSchemeURN();           
            fileName = fileName.replaceAll("\\s+", "_");
            
            String version = this.getSource().getCodingSchemeVersion();
            
            if (version != null)
                version = version.replaceAll("\\s+", "_"); 
            
            file = new File(file.getAbsolutePath() + file.separator + fileName + "_" + version + ".obo");
        }
        
        if(this.getOptions().getBooleanOption(OVERWRITE_OPTION).getOptionValue()
                &&
                file.exists()) {
            file.delete();
        }
        
        file.createNewFile();
        
        this.getStatus().setDestination(file.getAbsolutePath());
        
        lg2Obo.save(file);
    }
}