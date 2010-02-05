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
package edu.mayo.informatics.lexgrid.convert.swt.formatPanels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.SWTUtility;

/**
 * A SWT Composite that collects information necessary to connect to a SQL
 * server.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 186 $ checked in on $Date: 2005-11-07 10:44:31
 *          -0600 (Mon, 07 Nov 2005) $
 */
public class RegisterTerminologyEntryComposite extends Composite {
    Combo connectionString, username;
    Text password;

    public RegisterTerminologyEntryComposite(Composite parent, int style, String description, DialogHandler errorHandler) {
        super(parent, style);
        this.setLayout(new GridLayout(1, true));

        Group group = new Group(this, SWT.NONE);
        group.setText(description);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        group.setLayout(new GridLayout(2, false));

        SWTUtility.makeLabel("LSI Connection String", group, GridData.BEGINNING);

        connectionString = new Combo(group, SWT.DROP_DOWN);
        SWTUtility.position(connectionString, GridData.FILL_HORIZONTAL, 1);
        connectionString.setVisibleItemCount(10);

        SWTUtility.makeLabel("LSI Username", group, GridData.BEGINNING);

        username = new Combo(group, SWT.DROP_DOWN);
        SWTUtility.position(username, GridData.FILL_HORIZONTAL, 1);
        username.setVisibleItemCount(10);

        SWTUtility.makeLabel("LSI Password", group, GridData.BEGINNING);

        password = new Text(group, SWT.BORDER);
        password.setEchoChar('*');
        SWTUtility.position(password, GridData.FILL_HORIZONTAL, 1);
    }

    public String getPassword() {
        return this.password.getText();
    }

    public String getConnectionString() {
        return this.connectionString.getText();
    }

    public void setConnectionStringSuggestions(String[] connectionStrings) {
        this.connectionString.setItems(connectionStrings);
    }

    public String getUsername() {
        return this.username.getText();
    }

    public void setUserNameSuggestions(String[] userNames) {
        this.username.setItems(userNames);
    }
}