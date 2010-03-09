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
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt;

import org.eclipse.swt.widgets.Composite;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatSWTInterface;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;
import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.formatPanels.FolderEntryComposite;

/**
 * Details for writing an XML file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LexGridXMLSWTOut extends LexGridXMLOut implements OutputFormatSWTInterface {
    FolderEntryComposite fec;

    public Composite createComposite(Composite parent, int style, DialogHandler errorHandler) {
        fec = new FolderEntryComposite(parent, style, description, errorHandler);
        fec.setFolderSelection("C:\\temp");
        return fec;
    }

    public Composite getComposite() {
        return fec;
    }

    private void getFolderLocationFromComposite() {
        if (fec != null) {
            folderLocation_ = fec.getFolderSelection();
        }
    }

    public String testConnection() throws ConnectionFailure {
        getFolderLocationFromComposite();
        return super.testConnection();
    }

    public String getTextForActionButton() {
        return "Begin Conversion";
    }

}