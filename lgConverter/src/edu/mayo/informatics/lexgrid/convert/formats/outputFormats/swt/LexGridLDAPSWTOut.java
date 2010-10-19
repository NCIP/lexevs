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
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats.swt;

import org.eclipse.swt.widgets.Composite;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatSWTInterface;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.formatPanels.LDAPEntryComposite;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * SWT Details for writing to ldap.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LexGridLDAPSWTOut extends LexGridLDAPOut implements OutputFormatSWTInterface {
    protected LDAPEntryComposite lec = null;

    public String testConnection() throws ConnectionFailure {
        try {
            getCompositeValues();
        } catch (NumberFormatException e) {
            throw new ConnectionFailure("Port must be a number");
        }
        return super.testConnection();
    }

    public Composite createComposite(Composite parent, int style, DialogHandler errorHandler) {
        lec = new LDAPEntryComposite(parent, style, description, errorHandler);

        setCompositeVariables();
        return lec;
    }

    public Composite getComposite() {
        return lec;
    }

    private void setCompositeVariables() {
        if (lec == null) {
            return;
        }
        lec.setHostSuggestions(Constants.ldapServers);
        lec.setPortSuggestions(Constants.ldapPorts);
        lec.setUserNameSuggestions(Constants.ldapWriteUserNames);
    }

    protected void getCompositeValues() throws NumberFormatException {
        if (lec == null) {
            return;
        }
        host = lec.getHost();
        port = lec.getPort();
        password = lec.getPassword();
        serviceDN = lec.getServiceDN();
        username = lec.getUsername();
    }

    public String getTextForActionButton() {
        return "Begin Conversion";
    }
}