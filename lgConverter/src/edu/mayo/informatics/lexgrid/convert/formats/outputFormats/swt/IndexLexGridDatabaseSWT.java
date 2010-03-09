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
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.IndexLexGridDatabase;
import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.formatPanels.IndexerConfigComposite;

/**
 * SWT code for indexing a lexgrid database.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IndexLexGridDatabaseSWT extends IndexLexGridDatabase implements OutputFormatSWTInterface {
    protected IndexerConfigComposite sec;

    public Composite createComposite(Composite parent, int style, DialogHandler errorHandler) {
        sec = new IndexerConfigComposite(parent, style, description, errorHandler);

        setCompositeVariables();
        return sec;
    }

    public String testConnection() throws ConnectionFailure {
        getCompositeValues();
        return super.testConnection();
    }

    private void setCompositeVariables() {
        if (sec == null) {
            return;
        }
        sec.setIndexName("test");
        sec.setFolderSelection("C:\\temp");
        sec.setNormEnabled(false);
        sec
                .setNormFile("\\\\mir04\\LargeData\\LargeInstallers\\lvg2005Portable\\lvg2005\\data\\config\\lvg.properties");
    }

    private void getCompositeValues() {
        if (sec == null) {
            return;
        }
        indexLocation_ = sec.getFolderSelection();
        indexName_ = sec.getIndexName();
        normEnabled_ = sec.isNormEnabled();
        normConfigFile_ = sec.getNormFile();
    }

    public Composite getComposite() {
        return sec;
    }

    public String getTextForActionButton() {
        return "Begin Conversion";
    }

}