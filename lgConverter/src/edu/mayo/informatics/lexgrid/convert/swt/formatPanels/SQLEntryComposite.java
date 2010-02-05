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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class SQLEntryComposite extends Composite {
    Combo driver, connectionString, username;
    Text password;
    Text tablePrefix;

    public SQLEntryComposite(Composite parent, int style, String description, DialogHandler errorHandler) {
        super(parent, style);
        this.setLayout(new GridLayout(1, true));

        Group group = new Group(this, SWT.NONE);
        group.setText(description);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        group.setLayout(new GridLayout(2, false));

        SWTUtility.makeLabel("Connection String", group, GridData.BEGINNING);

        connectionString = new Combo(group, SWT.DROP_DOWN);
        SWTUtility.position(connectionString, GridData.FILL_HORIZONTAL, 1);
        connectionString.setVisibleItemCount(10);
        connectionString.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
            }

            public void widgetSelected(SelectionEvent arg0) {
                String[] drivers = driver.getItems();
                if (connectionString.getText().indexOf("Microsoft Access Driver") != -1) {
                    for (int i = 0; i < drivers.length; i++) {
                        if (drivers[i].indexOf("JdbcOdbcDriver") != -1) {
                            driver.select(i);
                            return;
                        }
                    }
                } else if (connectionString.getText().indexOf("hsqldb") != -1) {
                    for (int i = 0; i < drivers.length; i++) {
                        if (drivers[i].indexOf("hsqldb") != -1) {
                            driver.select(i);
                            username.setText("sa");
                            return;
                        }
                    }
                } else if (connectionString.getText().indexOf("mysql") != -1) {
                    for (int i = 0; i < drivers.length; i++) {
                        if (drivers[i].indexOf("mysql") != -1) {
                            driver.select(i);
                            return;
                        }
                    }
                } else if (connectionString.getText().indexOf("postgresql") != -1) {
                    for (int i = 0; i < drivers.length; i++) {
                        if (drivers[i].indexOf("postgresql") != -1) {
                            driver.select(i);
                            return;
                        }
                    }
                } else if (connectionString.getText().indexOf("db2") != -1) {
                    for (int i = 0; i < drivers.length; i++) {
                        if (drivers[i].indexOf("db2") != -1) {
                            driver.select(i);
                            return;
                        }
                    }
                } else if (connectionString.getText().indexOf("oracle") != -1) {
                    for (int i = 0; i < drivers.length; i++) {
                        if (drivers[i].indexOf("oracle") != -1) {
                            driver.select(i);
                            return;
                        }
                    }
                } else if (connectionString.getText().indexOf("sqlserver") != -1) {
                    for (int i = 0; i < drivers.length; i++) {
                        if (drivers[i].indexOf("sqlserver") != -1) {
                            driver.select(i);
                            return;
                        }
                    }
                }
            }

        });

        SWTUtility.makeLabel("Driver", "", group, GridData.BEGINNING, 90);

        driver = new Combo(group, SWT.DROP_DOWN);
        SWTUtility.position(driver, GridData.FILL_HORIZONTAL, 1);
        driver.setVisibleItemCount(10);

        SWTUtility.makeLabel("SQL Username", group, GridData.BEGINNING);

        username = new Combo(group, SWT.DROP_DOWN);
        SWTUtility.position(username, GridData.FILL_HORIZONTAL, 1);
        username.setVisibleItemCount(10);

        SWTUtility.makeLabel("SQL Password", group, GridData.BEGINNING);

        password = new Text(group, SWT.BORDER);
        password.setEchoChar('*');
        SWTUtility.position(password, GridData.FILL_HORIZONTAL, 1);

        SWTUtility.makeLabel("SQL Table Prefix", group, GridData.BEGINNING);

        tablePrefix = new Text(group, SWT.BORDER);
        tablePrefix.setToolTipText("Prefix to prepend onto the table names.  Optional.");
        SWTUtility.position(tablePrefix, GridData.FILL_HORIZONTAL, 1);
    }

    public String getDriver() {
        return driver.getText();
    }

    public void setDriverSuggestions(String[] hosts) {
        this.driver.setItems(hosts);
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

    public String getTablePrefix() {
        return this.tablePrefix.getText();
    }

    public void setUserNameSuggestions(String[] userNames) {
        this.username.setItems(userNames);
    }
}