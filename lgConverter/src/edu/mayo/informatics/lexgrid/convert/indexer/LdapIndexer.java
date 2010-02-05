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
package edu.mayo.informatics.lexgrid.convert.indexer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.LexGrid.emf.commonTypes.EntityTypes;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;

import com.sun.jndi.ldap.ctl.PagedResultsControl;
import com.sun.jndi.ldap.ctl.PagedResultsResponseControl;

import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * A class to build indexes to be used in CTS.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 */
public class LdapIndexer extends LuceneLoaderCode {
    private LdapContext ldapConnection_;
    private LdapContext ldapConnection2_;
    private int pageSize_ = 50;
    private String defaultLanguage_;
    private String codingSchemeId_;
    private LgMessageDirectorIF md_;

    // /**
    // * Build a lucene index from an LDAP server - all parameters are loaded
    // from a properties file. Mostly used by
    // * command line runner.
    // *
    // * @throws Exception
    // */
    // public LdapLoader() throws Exception
    // {
    // md_ = new MessageDirector(true, null, null);
    // Properties props = null;
    //
    // try
    // {
    // props =
    // PropertiesUtility.locateAndLoadPropFileConfigureLog4J("ldapIndexLoader.props",
    // "ITS_LOADER_LOG4J_CONTROL_FILES");
    // }
    // catch (Exception e)
    // {
    // org.apache.log4j.BasicConfigurator.configure();
    // System.out.println("ERROR:  Unable to load props file.  Fatal error.");
    // System.exit(1);
    // }
    //
    // String compound = (String)props.get("COMPOUND_FILE");
    // if (compound != null)
    // {
    // useCompoundFile_ = new Boolean(compound).booleanValue();
    // }
    //        
    // String codingSchemesString = props.get("CODING_SCHEMES").toString();
    // String[] codingSchemes = WordUtility.getWords(codingSchemesString, ",");
    //
    // pageSize_ = Integer.parseInt(props.get("PAGE_SIZE").toString());
    //
    // index(props.get("INDEX_NAME").toString(),
    // props.get("INDEX_LOCATION").toString(), props
    // .get("SECURITY_PRINCIPAL").toString(),
    // props.get("SECURITY_PASSWORD").toString(), props
    // .get("X500_LDAP_ADDRESS").toString(),
    // props.get("LDAP_SERVICE").toString(), codingSchemes);
    // }

    /**
     * Build a lucene index from an ldap server, using the parameters supplied.
     * 
     * @param indexName
     *            The Name to use for the Index
     * @param indexLocation
     *            The full path to the index location on the system
     * @param ldapUserName
     *            The username to use for the ldap connection
     * @param ldapPassword
     *            The password to use for the ldap connection
     * @param ldapAddress
     *            The ldap server address
     * @param ldapService
     *            The ldap service portion of the address
     * @param codingSchemes
     *            The coding schemes to index from the ldap server
     * @param pageSize
     *            The page size to use while reading data
     * @throws Exception
     */
    public LdapIndexer(String indexName, String indexLocation, String ldapUserName, String ldapPassword,
            String ldapAddress, String ldapService, String[] codingSchemes, LgMessageDirectorIF md,
            boolean addNormFields, boolean addDoubleMetaphoneFields, boolean addStemFields, boolean useCompoundFile)
            throws Exception {
        pageSize_ = Constants.ldapPageSize;
        normEnabled_ = addNormFields;
        stemmingEnabled_ = addStemFields;
        doubleMetaphoneEnabled_ = addDoubleMetaphoneFields;
        md_ = md;
        useCompoundFile_ = useCompoundFile;
        index(indexName, indexLocation, ldapUserName, ldapPassword, ldapAddress, ldapService, codingSchemes);
    }

    private void index(String indexName, String indexLocation, String ldapUserName, String ldapPassword,
            String ldapAddress, String ldapService, String[] codingSchemes) throws Exception {
        initIndexes(indexName, indexLocation);
        createIndex();

        openIndexesClearExisting(codingSchemes);

        // connect LDAP
        initContexts(ldapUserName, ldapPassword, ldapAddress, ldapService);

        SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        for (int j = 0; j < codingSchemes.length; j++) {
            logger.info("Now indexing " + codingSchemes[j] + " " + temp.format(new Date(System.currentTimeMillis())));
            md_.info("Now indexing " + codingSchemes[j] + " " + temp.format(new Date(System.currentTimeMillis())));

            Date start = new Date(System.currentTimeMillis());
            String localName = findCodingScheme(codingSchemes[j]);
            loadCodedEntries("dc=Concepts," + localName, codingSchemes[j]);

            // TODO - version needs to be read from the ldap server.
            String version = "<Unknown - this is broken in the LDAP indexer>";
            indexerService_.getMetaData().setIndexMetaDataValue(codingSchemes[j] + "[:]" + version, indexName);
            indexerService_.getMetaData().setIndexMetaDataValue(indexName, "codingScheme", codingSchemes[j]);
            indexerService_.getMetaData().setIndexMetaDataValue(indexName, "version", version);
            indexerService_.getMetaData().setIndexMetaDataValue(indexName, "lgModel", "2008");
            indexerService_.getMetaData().setIndexMetaDataValue(indexName, "has 'Norm' fields", normEnabled_ + "");
            indexerService_.getMetaData().setIndexMetaDataValue(indexName, "has 'Double Metaphone' fields",
                    doubleMetaphoneEnabled_ + "");
            indexerService_.getMetaData().setIndexMetaDataValue(indexName, "indexing started", temp.format(start));
            indexerService_.getMetaData().setIndexMetaDataValue(indexName, "indexing finished",
                    temp.format(new Date(System.currentTimeMillis())));
        }
        md_.info("Closing Indexes " + temp.format(new Date(System.currentTimeMillis())));
        closeIndexes();
        md_.info("Finished " + temp.format(new Date(System.currentTimeMillis())));

    }

    private void loadCodedEntries(String name, String codingSchemeName) throws Exception {
        logger.debug("loadCodedEntries called");

        int codeCount = 0;
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(0);
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        searchControls.setTimeLimit(0);

        searchControls.setReturningAttributes(new String[] { SQLTableConstants.TBLCOL_ID,
                SQLTableConstants.TBLCOL_ISACTIVE, SQLTableConstants.TBLCOL_ISANONYMOUS,
                SQLTableConstants.TBLCOL_CONCEPTSTATUS, SQLTableConstants.TBLCOL_ENTITYDESCRIPTION });

        String filter = "(&(objectClass=concept))";
        ldapConnection2_.setRequestControls(new Control[] { new PagedResultsControl(pageSize_) });

        byte[] cookie = null;
        do {
            logger.debug("searching on connection2");
            NamingEnumeration result = ldapConnection2_.search(name, filter, searchControls);

            while (result.hasMore()) {
                SearchResult sRes = (SearchResult) result.next();

                Attributes attributes = sRes.getAttributes();

                String code = (String) attributes.get(SQLTableConstants.TBLCOL_ID).get();
                String namespace = (String) attributes.get(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE).get();

                Boolean isActive = new Boolean(true);
                if (attributes.get(SQLTableConstants.TBLCOL_ISACTIVE) != null) {
                    isActive = new Boolean((String) attributes.get(SQLTableConstants.TBLCOL_ISACTIVE).get());
                }

                if (attributes.get(SQLTableConstants.TBLCOL_ISANONYMOUS) != null) {
                    boolean isAnonymous = new Boolean((String) attributes.get(SQLTableConstants.TBLCOL_ISANONYMOUS)
                            .get()).booleanValue();
                    if (isAnonymous) {
                        // skip this one
                        continue;
                    }
                }

                String conceptStatus = null;
                if (attributes.get(SQLTableConstants.TBLCOL_CONCEPTSTATUS) != null) {
                    conceptStatus = (String) attributes.get(SQLTableConstants.TBLCOL_CONCEPTSTATUS).get();
                }
                String entityDescription = null;
                if (attributes.get(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION) != null) {
                    entityDescription = (String) attributes.get(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION).get();
                }

                loadCodedEntriesHelper(
                        SQLTableConstants.TBLCOL_ID + "=" + DBUtility.escapeLdapCode(code) + ", " + name,
                        codingSchemeName, code, namespace, isActive, conceptStatus, entityDescription);

                if (codeCount++ % 50 == 0) {
                    md_.busy();

                }
            }

            logger.debug("getting controls");
            Control[] controls = ldapConnection2_.getResponseControls();
            if (controls != null) {
                for (int i = 0; i < controls.length; i++) {
                    if (controls[i] instanceof PagedResultsResponseControl) {
                        PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                        cookie = prrc.getCookie();
                        logger.debug("found cookie");
                        break;
                    }
                }
            } else {
                cookie = null;
            }

            if (cookie != null) {
                logger.debug("setting cookie");
                ldapConnection2_.setRequestControls(new Control[] { new PagedResultsControl(pageSize_, cookie,
                        Control.CRITICAL) });
                if (codeCount % 1000 == 0) {
                    md_.info("Loaded " + codeCount + " codes...");
                    logger.info("Loaded " + codeCount + " codes...");
                }
            }

        } while (cookie != null);
        md_.info("loaded " + codeCount + " concept Codes");
        logger.info("loaded " + codeCount + " concept Codes");
    }

    private void loadCodedEntriesHelper(String ldapName, String codingSchemeName, String code, String namespace, Boolean isActive,
            String conceptStatus, String entityDescription) throws Exception {
        logger.debug("loadConceptProperty called - ldapName: " + ldapName + " codingSchemeName: " + codingSchemeName
                + " code: " + code);
        String propertyName = "";
        String propertyId = "";
        try {
            SearchControls searchControls = new SearchControls();
            searchControls.setCountLimit(0);
            searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            searchControls.setReturningAttributes(new String[] { SQLTableConstants.TBLCOL_PROPERTYNAME,
                    SQLTableConstants.TBLCOL_LANGUAGE, SQLTableConstants.TBLCOL_ISPREFERRED, "text",
                    SQLTableConstants.TBLCOL_FORMAT, SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT, "dataType",
                    SQLTableConstants.TBLCOL_DEGREEOFFIDELITY, SQLTableConstants.TBLCOL_REPRESENTATIONALFORM });

            logger.debug("Searching on connection1");
            NamingEnumeration result = ldapConnection_.search(ldapName, "(objectClass=propertyClass)", searchControls);

            while (result.hasMore()) {
                SearchResult sRes = (SearchResult) result.next();
                Attributes attributes = sRes.getAttributes();

                propertyName = (String) attributes.get(SQLTableConstants.TBLCOL_PROPERTYNAME).get();

                String propertyValue = attributes.get("text") == null ? null : (String) attributes.get("text").get();

                String presentationFormat = attributes.get(SQLTableConstants.TBLCOL_FORMAT) == null ? null
                        : (String) attributes.get(SQLTableConstants.TBLCOL_FORMAT).get();

                String language = attributes.get(SQLTableConstants.TBLCOL_LANGUAGE) == null ? "" : (String) attributes
                        .get(SQLTableConstants.TBLCOL_LANGUAGE).get();

                if (language == null || language.length() == 0) {
                    language = defaultLanguage_;
                }

                Boolean isPreferred = attributes.get(SQLTableConstants.TBLCOL_ISPREFERRED) == null ? null
                        : new Boolean((String) attributes.get(SQLTableConstants.TBLCOL_ISPREFERRED).get());

                Boolean matchIfNoContext = attributes.get(SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT) == null ? null
                        : new Boolean((String) attributes.get(SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT).get());

                String degreeOfFidelity = attributes.get(SQLTableConstants.TBLCOL_DEGREEOFFIDELITY) == null ? ""
                        : (String) attributes.get(SQLTableConstants.TBLCOL_DEGREEOFFIDELITY).get();

                String representationalForm = attributes.get(SQLTableConstants.TBLCOL_REPRESENTATIONALFORM) == null ? ""
                        : (String) attributes.get(SQLTableConstants.TBLCOL_REPRESENTATIONALFORM).get();

                // TODO - ldap reader isn't reading sources, usage contexts, or
                // qualifiers.
                try {
                    addEntity(codingSchemeName, codingSchemeId_, code, namespace, EntityTypes.CONCEPT_LITERAL.getLiteral(),
                            entityDescription, null, propertyName, propertyValue, isActive, presentationFormat,
                            language, isPreferred, conceptStatus, propertyId, degreeOfFidelity, matchIfNoContext,
                            representationalForm, null, null, null, null);
                } catch (Exception e) {
                    md_.fatalAndThrowException("Problem indexing concept", e);
                }
            }
        }

        catch (NamingException e) {

            logger
                    .error("***ERROR*** - problem loading properties for a code codingSchemeName: '" + codingSchemeName
                            + "' code: '" + code + "' propertyname: '" + propertyName + "' propertyId: '" + propertyId
                            + "'", e);

            throw e;

        }
    }

    private String findCodingScheme(String codingScheme) throws Exception {
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(0);
        searchControls.setDerefLinkFlag(false);
        searchControls.setReturningObjFlag(false);
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setTimeLimit(0);
        searchControls.setReturningAttributes(new String[] { "codingScheme", SQLTableConstants.TBLCOL_DEFAULTLANGUAGE,
                SQLTableConstants.TBLCOL_REGISTEREDNAME });

        String filter = "(&(objectClass=codingSchemeClass)(codingScheme=" + codingScheme + "))";

        NamingEnumeration result = ldapConnection_.search("", filter, searchControls);
        if (result.hasMore()) {
            SearchResult sRes = (SearchResult) result.next();
            defaultLanguage_ = (String) sRes.getAttributes().get(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE).get();

            codingSchemeId_ = (String) sRes.getAttributes().get(SQLTableConstants.TBLCOL_REGISTEREDNAME).get();

            return sRes.getName();
        } else {
            throw new Exception("Coding Scheme " + codingScheme + " not found.");
        }
    }

    private void initContexts(String userName, String userPassword, String address, String service)
            throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        String temp = "";
        if (!address.endsWith("/") && !service.startsWith("/")) {
            temp = "/";
        }
        env.put(Context.PROVIDER_URL, address + temp + service);
        env.put(Context.REFERRAL, "follow");
        env.put("java.naming.ldap.derefAliases", "never");

        // these only work in 1.4... and beyond
        env.put("com.sun.jndi.ldap.connect.pool", "true");
        env.put("com.sun.jndi.ldap.connect.timeout", "3000");

        env.put("java.naming.ldap.version", "3");

        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, userPassword);

        ldapConnection_ = new InitialLdapContext(env, null);
        ldapConnection2_ = new InitialLdapContext(env, null);
    }

    // public static void main(String[] args) throws Exception
    // {
    // new LdapLoader();
    // }

}