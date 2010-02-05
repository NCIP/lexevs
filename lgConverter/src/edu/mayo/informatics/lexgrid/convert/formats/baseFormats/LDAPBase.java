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
package edu.mayo.informatics.lexgrid.convert.formats.baseFormats;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;

/**
 * Common bits for formats that read or write from ldap.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 8751 $ checked in on $Date: 2008-06-12
 *          16:21:49 +0000 (Thu, 12 Jun 2008) $
 */
public class LDAPBase extends CommonBase {

    public static final String description = "LexGrid LDAP Server";

    protected String host;
    protected String serviceDN;
    protected String username;
    protected String password;
    protected int port;

    public String getDescription() {
        return description;
    }

    public String testConnection() throws ConnectionFailure {
        if (host == null || host.length() == 0) {
            throw new ConnectionFailure("Host is required");
        }

        if (serviceDN == null || serviceDN.length() == 0) {
            throw new ConnectionFailure("The Service DN is required");
        }

        return LDAPUtility.testLDAPConnection(LDAPUtility.makeURL(host, port + "", serviceDN), username, password);
    }

    public String getHost() {
        return this.host;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPort() {
        return this.port;
    }

    public String getAddress() {
        return "ldap://" + this.host + ":" + this.port;
    }

    public String getServiceDN() {
        return this.serviceDN;
    }

    public String getUsername() {
        return this.username;
    }

    public String getConnectionSummary() {
        StringBuffer temp = new StringBuffer(description + "\n");
        temp.append("LDAP Url: " + LDAPUtility.makeURL(host, port + "", serviceDN) + "\n");
        temp.append("Username: " + username);

        return temp.toString();
    }
}