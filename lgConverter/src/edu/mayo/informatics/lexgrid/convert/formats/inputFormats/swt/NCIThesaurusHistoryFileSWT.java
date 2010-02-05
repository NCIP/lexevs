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
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt;

import java.io.File;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatSWTInterface;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIThesaurusHistoryFile;
import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.formatPanels.NCIHistoryLoadComposite;

/**
 * Details for reading a NCIThesaurusHistoryFile Text File.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          05:42:24 -0600 (Mon, 30 Jan 2006) $
 */
public class NCIThesaurusHistoryFileSWT extends NCIThesaurusHistoryFile implements InputFormatSWTInterface {
    protected NCIHistoryLoadComposite fec = null;

    public Composite createComposite(Composite parent, int style, DialogHandler errorHandler) {
        fec = new NCIHistoryLoadComposite(parent, style, description, errorHandler);
        fec.setFileSelection("C:\\full_pipe_out.txt");
        fec.setVersionsFileSelection("C:\\NCISystemReleaseHistory.txt");
        return fec;
    }

    public String testConnection() throws ConnectionFailure {
        getFileLocationsFromComposite();
        return super.testConnection();
    }

    public Menu createToolsMenu(Shell shell, final DialogHandler errorHandler) {
        return null;
    }

    private void getFileLocationsFromComposite() {
        if (fec != null) {
            setFileLocation(new File(fec.getFileSelection()).toURI());
            setHistoryVersionFileLocation(new File(fec.getVersionsFileSelection()).toURI());
        }
    }
}