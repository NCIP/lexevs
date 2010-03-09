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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatSWTInterface;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIMetaThesaurusSQL;
import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.formatPanels.SQLEntryComposite;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Details for reading from NCIMetaThesaurus format.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class NCIMetaThesaurusSQLSWT extends NCIMetaThesaurusSQL implements InputFormatSWTInterface {
    protected SQLEntryComposite sec;

    public String testConnection() throws ConnectionFailure {
        getCompositeValues();
        return super.testConnection();
    }

    public Composite createComposite(Composite parent, int style, DialogHandler errorHandler) {
        sec = new SQLEntryComposite(parent, style, description, errorHandler);

        setCompositeVariables();
        return sec;
    }

    private void setCompositeVariables() {
        if (sec == null) {
            return;
        }
        sec.setUserNameSuggestions(new String[] {});
        sec.setDriverSuggestions(Constants.sqlDrivers);
        sec.setConnectionStringSuggestions(Constants.nciMTServers);
    }

    private void getCompositeValues() {
        if (sec == null) {
            return;
        }
        driver = sec.getDriver();
        password = sec.getPassword();
        server = sec.getConnectionString();
        username = sec.getUsername();
    }

    public Menu createToolsMenu(Shell shell, final DialogHandler errorHandler) {
        return null;
    }

}