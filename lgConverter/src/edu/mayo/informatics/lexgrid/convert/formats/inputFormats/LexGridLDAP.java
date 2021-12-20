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
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.LDAPBase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.DeleteLexGridTerminology;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.IndexLexGridDatabase;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridLDAPOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLLiteOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridXMLOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.OBOOut;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.RegisterLexGridTerminology;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;
import edu.mayo.informatics.lexgrid.convert.utility.StringComparator;

/**
 * Details for connecting to LDAP.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LexGridLDAP extends LDAPBase implements InputFormatInterface {
    private static Logger log = LogManager.getLogger("convert.gui");

    public LexGridLDAP(String username, String password, String host, int port, String service) {
        if (host.toLowerCase().startsWith("ldap://")) {
            this.host = host.substring("ldap://".length());
        } else {
            this.host = host;
        }
        this.port = port;
        this.username = username;
        this.password = password;
        this.serviceDN = service;
    }

    public LexGridLDAP() {

    }

    public String[] getSupportedOutputFormats() {
        return new String[] { LexGridXMLOut.description, LexGridSQLOut.description, LexGridSQLLiteOut.description,
                LexGridLDAPOut.description, DeleteLexGridTerminology.description,
                RegisterLexGridTerminology.description, IndexLexGridDatabase.description, OBOOut.description };
    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.LDAP_PAGE_SIZE, new String(Constants.ldapPageSize + "")) };
    }

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError {
        try {
            String[] terminologies = new String[] {};

            String hostURL = "ldap://" + getHost() + ":" + getPort() + "/" + getServiceDN();

            ArrayList temp = new ArrayList();
            String searchBase = "dc=codingSchemes";
            String searchFilter = "(objectclass=codingSchemeClass)";
            SearchControls ctrl = new SearchControls();
            ctrl.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            ctrl.setReturningAttributes(new String[] { "codingScheme" });

            Hashtable env = new Hashtable(10);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, hostURL);
            env.put(Context.SECURITY_PRINCIPAL, getUsername());
            env.put(Context.SECURITY_CREDENTIALS, getPassword());
            env.put("com.sun.jndi.ldap.connect.timeout", "1000");

            DirContext ctx = new InitialDirContext(env);

            NamingEnumeration results = ctx.search(searchBase, searchFilter, ctrl);

            while (results.hasMore()) {
                SearchResult nextEntry = (SearchResult) results.next();
                temp.add((String) nextEntry.getAttributes().get("codingScheme").get());
            }
            terminologies = (String[]) temp.toArray(new String[temp.size()]);
            Arrays.sort(terminologies, new StringComparator());

            return terminologies;
        } catch (NamingException e) {
            log.error("Problem getting available terminologies", e);
            throw new ConnectionFailure("Problem getting the available terminologies", e);
        } catch (Exception e) {
            log.error("Problem getting available terminologies", e);
            throw new UnexpectedError("Problem getting the available terminologies", e);
        }
    }
}