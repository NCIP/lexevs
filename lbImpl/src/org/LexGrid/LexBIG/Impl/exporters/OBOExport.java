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

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.OBO_Exporter;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;

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
        //
    }

    public String getOBOVersion() {
        return "1.2";

    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        return holder;
    }

    @Override
    protected void doExport() throws Exception {
        //
    }
}