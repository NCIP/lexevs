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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.SWTUtility;
import edu.mayo.informatics.lexgrid.convert.utility.StringComparator;

/**
 * A SWT Composite that collects information necessary to connect to a LDAP
 * server.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LDAPEntryComposite extends Composite {
    Combo host, serviceDN, username, port;
    Text password;

    private DialogHandler errorHandler_;

    public LDAPEntryComposite(Composite parent, int style, String description, DialogHandler errorHandler) {
        super(parent, style);
        errorHandler_ = errorHandler;
        this.setLayout(new GridLayout(1, true));

        Group group = new Group(this, SWT.NONE);
        group.setText(description);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        group.setLayout(new GridLayout(4, false));

        SWTUtility.makeLabel("Host", "", group, GridData.BEGINNING, 90);

        host = new Combo(group, SWT.DROP_DOWN);
        SWTUtility.position(host, GridData.FILL_HORIZONTAL, 1);

        SWTUtility.makeLabel("   Port ", group, GridData.HORIZONTAL_ALIGN_END);

        port = new Combo(group, SWT.DROP_DOWN);
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gd.minimumWidth = 75;
        gd.widthHint = 75;
        port.setLayoutData(gd);

        SWTUtility.makeLabel("Service DN", group, GridData.BEGINNING);

        serviceDN = new Combo(group, SWT.DROP_DOWN);
        SWTUtility.position(serviceDN, GridData.FILL_HORIZONTAL, 2);
        serviceDN.setVisibleItemCount(10);

        Button getDns = new Button(group, SWT.PUSH);
        getDns.setText("Get DNs");
        gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        getDns.setLayoutData(gd);
        getDns.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {

            }

            public void widgetSelected(SelectionEvent arg0) {
                populateDNs();
            }

        });

        SWTUtility.makeLabel("Ldap Username", group, GridData.BEGINNING);

        username = new Combo(group, SWT.DROP_DOWN);
        SWTUtility.position(username, GridData.FILL_HORIZONTAL, 2);

        SWTUtility.makeLabel("", group, GridData.BEGINNING);

        SWTUtility.makeLabel("Ldap Password", group, GridData.BEGINNING);

        password = new Text(group, SWT.BORDER);
        password.setEchoChar('*');
        SWTUtility.position(password, GridData.FILL_HORIZONTAL, 2);

    }

    public String getHost() {
        return host.getText();
    }

    public void setHostSuggestions(String[] hosts) {
        this.host.setItems(hosts);
    }

    public int getPort() throws NumberFormatException {
        return Integer.parseInt(port.getText());
    }

    public void setPortSuggestions(int[] ports) {
        String[] temp = new String[ports.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = ports[i] + "";
        }

        this.port.setItems(temp);
    }

    public String getPassword() {
        return this.password.getText();
    }

    public String getServiceDN() {
        return this.serviceDN.getText();
    }

    public String getUsername() {
        return this.username.getText();
    }

    public void setUserNameSuggestions(String[] userNames) {
        this.username.setItems(userNames);
    }

    private void populateDNs() {
        try {

            if (getHost().length() == 0) {
                errorHandler_.showError("Missing parameter", "Host is required to get the available DNs.");
                return;
            }

            try {
                getPort();
            } catch (NumberFormatException e) {
                errorHandler_.showError("Invalid parameter", "Port must be a number.");
                return;
            }

            String hostURL = "ldap://" + getHost() + ":" + getPort();

            /*
             * To get root DSE 1. set searchBase equals empty string 2. set
             * seatchFilter equals objectclass = 3. pass OBJECT_SCOPE to
             * setSearchScope() method
             */
            String searchBase = "";
            String searchFilter = "(objectclass=*)";
            SearchControls ctrl = new SearchControls();
            ctrl.setSearchScope(SearchControls.OBJECT_SCOPE);
            ctrl.setReturningAttributes(new String[] { "namingContexts" });

            Hashtable env = new Hashtable(5);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, hostURL);
            env.put("com.sun.jndi.ldap.connect.timeout", "1000");

            DirContext ctx = new InitialDirContext(env);

            NamingEnumeration results = ctx.search(searchBase, searchFilter, ctrl);

            SearchResult nextEntry = (SearchResult) results.next();
            Attributes attrSet = nextEntry.getAttributes();

            ArrayList temp = new ArrayList();

            if (attrSet != null) {
                NamingEnumeration attrs = attrSet.getAll();
                while (attrs.hasMoreElements()) {
                    Attribute attr = (Attribute) attrs.next();
                    NamingEnumeration values = attr.getAll();

                    while (values.hasMoreElements()) {
                        temp.add((String) values.nextElement());
                    }
                }
            }
            String[] temp2 = (String[]) temp.toArray(new String[temp.size()]);
            Arrays.sort(temp2, new StringComparator());
            serviceDN.setItems(temp2);
            if (temp2.length > 0) {
                serviceDN.select(0);
            }
        } catch (Exception e) {
            errorHandler_.showError("Problem getting DNs",
                    "Could not make a connection to the ldap server.\nMore details:  " + e.toString());
        }
    }
}