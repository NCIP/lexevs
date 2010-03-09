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
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.RegisterLexGridTerminology;
import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.formatPanels.RegisterTerminologyEntryComposite;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * SWT Additions for details for removing a terminology from a server.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 258 $ checked in on $Date: 2005-11-15 11:11:26
 *          -0600 (Tue, 15 Nov 2005) $
 */
public class RegisterLexGridTerminologySWT extends RegisterLexGridTerminology implements OutputFormatSWTInterface {
    protected RegisterTerminologyEntryComposite sec;

    public Composite createComposite(Composite parent, int style, DialogHandler errorHandler) {
        sec = new RegisterTerminologyEntryComposite(parent, style, description, errorHandler);

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
        sec.setUserNameSuggestions(new String[] {});
        sec.setConnectionStringSuggestions(Constants.lsiServers);
    }

    private void getCompositeValues() {
        if (sec == null) {
            return;
        }
        driver = Constants.lsiDriver;
        password = sec.getPassword();
        server = sec.getConnectionString();
        username = sec.getUsername();
    }

    public Composite getComposite() {
        return sec;
    }

    public String getTextForActionButton() {
        return "Register Terminology";
    }

}