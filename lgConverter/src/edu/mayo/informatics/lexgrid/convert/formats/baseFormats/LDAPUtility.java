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
package edu.mayo.informatics.lexgrid.convert.formats.baseFormats;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;

/**
 * Common tools for LDAP formats.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LDAPUtility {
    public static String testLDAPConnection(String url, String username, String password) throws ConnectionFailure {
        try {
            Hashtable env = new Hashtable(10);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put("com.sun.jndi.ldap.connect.timeout", "1000");

            DirContext ctx = new InitialDirContext(env);
            ctx.close();
            return "";
        } catch (NamingException e) {
            throw new ConnectionFailure(e.toString());
        }
    }

    public static String makeURL(String host, String port, String service) {
        if (host == null) {
            host = "";
        }
        if (!host.toLowerCase().startsWith("ldap://")) {
            host = "ldap://" + host;
        }

        if (service == null) {
            service = "";
        }
        if (!service.startsWith("/")) {
            service = "/" + service;
        }

        return host + ":" + port + service;
    }
}