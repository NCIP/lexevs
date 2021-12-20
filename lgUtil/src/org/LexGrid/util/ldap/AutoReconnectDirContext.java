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
package org.LexGrid.util.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.LexGrid.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is an automatically reconnecting ldap dir context. If the
 * connection times out, this will automatically reconnect it.
 * 
 * There are numerous problems with this class - notably, all calls that return
 * sub contexts will give you a new context that won't automatically reconnect.
 * 
 * But it does work well for search-centric operations.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class AutoReconnectDirContext implements DirContext {
    // TODO - this should implement the ldap interface...
    public int MAX_RECONNECT_ATTEMPTS = 5;

    private DirContext context_;
    public final static Logger logger = LogManager
            .getLogger("edu.mayo.informatics.cts.utility.AutoReconnectDirContext");

    public AutoReconnectDirContext(Hashtable env, boolean logPassword) throws javax.naming.NamingException {
        logger.debug("Constructor called");
        logger.debug("Enviroment:");
        if (logPassword)
            logger.debug(Utility.hashTableToString(env));
        else
            logger.debug(Utility.hashTableToString(env, new String[] { "java.naming.security.credentials" }));
        // Don't print out the password

        context_ = new InitialDirContext(env);
    }

    public DirContext createSubcontext(String name, Attributes attrs) throws javax.naming.NamingException {
        logger.debug("Create Subcontext: name: " + (name == null ? "null" : name));
        try {
            return context_.createSubcontext(name, attrs);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.createSubcontext(name, attrs);
        }
    }

    public void rename(Name oldName, Name newName) throws javax.naming.NamingException {
        logger.debug("rename: oldName: " + (oldName == null ? "null" : oldName.toString()) + " newName: "
                + (newName == null ? "null" : newName.toString()));
        try {
            context_.rename(oldName, newName);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.rename(oldName, newName);
        }
    }

    public DirContext createSubcontext(Name name, Attributes attrs) throws javax.naming.NamingException {
        logger.debug("Create Subcontext: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.createSubcontext(name, attrs);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.createSubcontext(name, attrs);
        }
    }

    public Object lookupLink(Name name) throws javax.naming.NamingException {
        logger.debug("LookupLink: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.lookupLink(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.lookupLink(name);
        }
    }

    public void bind(Name name, Object obj, Attributes attrs) throws javax.naming.NamingException {
        logger.debug("bind: name: " + (name == null ? "null" : name.toString()));
        try {
            context_.bind(name, obj, attrs);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.bind(name, obj, attrs);
        }
    }

    public void destroySubcontext(String name) throws javax.naming.NamingException {
        logger.debug("destroySubcontext: name " + (name == null ? "null" : name.toString()));
        try {
            context_.destroySubcontext(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.destroySubcontext(name);
        }
    }

    public void unbind(Name name) throws javax.naming.NamingException {
        logger.debug("unbind: name: " + (name == null ? "null" : name.toString()));
        try {
            context_.unbind(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.unbind(name);
        }
    }

    public NameParser getNameParser(Name name) throws javax.naming.NamingException {
        logger.debug("getNameParser: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.getNameParser(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getNameParser(name);
        }
    }

    public void rebind(String name, Object obj) throws javax.naming.NamingException {
        logger.debug("rebind: name: " + (name == null ? "null" : name));
        try {
            context_.rebind(name, obj);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.rebind(name, obj);
        }
    }

    public String composeName(String name, String prefix) throws javax.naming.NamingException {
        logger.debug("composeName: name: " + (name == null ? "null" : name) + " prefix: "
                + (prefix == null ? "null" : prefix));
        try {
            return context_.composeName(name, prefix);
        }

        catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.composeName(name, prefix);
        }
    }

    public void bind(String name, Object obj) throws javax.naming.NamingException {
        logger.debug("bind: name: " + (name == null ? "null" : name));
        try {
            context_.bind(name, obj);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.bind(name, obj);
        }
    }

    public void modifyAttributes(String name, int mod_op, Attributes attrs) throws javax.naming.NamingException {
        logger.debug("modifyAttributes: name: " + (name == null ? "null" : name));
        try {
            context_.modifyAttributes(name, mod_op, attrs);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.modifyAttributes(name, mod_op, attrs);
        }
    }

    public NamingEnumeration search(Name name, String filter, SearchControls cons) throws javax.naming.NamingException {
        logger.debug("search: name: " + (name == null ? "null" : name.toString()) + " filter: "
                + (filter == null ? "null" : filter));
        try {
            return context_.search(name, filter, cons);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.search(name, filter, cons);
        }
    }

    public void rename(String oldName, String newName) throws javax.naming.NamingException {
        logger.debug("rename: oldName: " + (oldName == null ? "null" : oldName) + " newName: "
                + (newName == null ? "null" : newName));
        try {
            context_.rename(oldName, newName);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.rename(oldName, newName);
        }
    }

    public Object addToEnvironment(String propName, Object propVal) throws javax.naming.NamingException {
        logger.debug("addToEnviroment: propName: " + (propName == null ? "null" : propName) + " propVal: "
                + propVal.toString());
        try {
            return context_.addToEnvironment(propName, propVal);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.addToEnvironment(propName, propVal);
        }
    }

    public void modifyAttributes(String name, ModificationItem[] mods) throws javax.naming.NamingException {
        logger.debug("modifyAttributes: name: " + (name == null ? "null" : name));
        try {
            context_.modifyAttributes(name, mods);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.modifyAttributes(name, mods);
        }
    }

    public Context createSubcontext(Name name) throws javax.naming.NamingException {
        logger.debug("createSubcontext: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.createSubcontext(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.createSubcontext(name);
        }
    }

    public Attributes getAttributes(String name, String[] attrIds) throws javax.naming.NamingException {
        logger.debug("getAttributes: name: " + (name == null ? "null" : name) + " AttributeIds: "
                + Utility.stringArrayToString(attrIds));
        try {
            return context_.getAttributes(name, attrIds);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getAttributes(name, attrIds);
        }
    }

    public NamingEnumeration search(String name, String filter, SearchControls cons)
            throws javax.naming.NamingException {
        logger.debug("search: name: " + (name == null ? "null" : name) + " filter: "
                + (filter == null ? "null" : filter));
        try {
            return context_.search(name, filter, cons);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.search(name, filter, cons);
        }
    }

    public Name composeName(Name name, Name prefix) throws javax.naming.NamingException {
        logger.debug("composeName: name: " + (name == null ? "null" : name.toString()) + " prefix: "
                + (prefix == null ? "null" : prefix.toString()));
        try {
            return context_.composeName(name, prefix);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.composeName(name, prefix);
        }
    }

    public NamingEnumeration listBindings(Name name) throws javax.naming.NamingException {
        logger.debug("listBindings: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.listBindings(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.listBindings(name);
        }
    }

    public Object lookupLink(String name) throws javax.naming.NamingException {
        logger.debug("lookupLink: name: " + (name == null ? "null" : name));
        try {
            return context_.lookupLink(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.lookupLink(name);
        }
    }

    public void modifyAttributes(Name name, ModificationItem[] mods) throws javax.naming.NamingException {
        logger.debug("ModifyAttributes: name: " + (name == null ? "null" : name.toString()));
        try {
            context_.modifyAttributes(name, mods);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.modifyAttributes(name, mods);
        }
    }

    public NamingEnumeration search(String name, Attributes attrs, String[] attrsToReturn)
            throws javax.naming.NamingException {
        logger.debug("search: name: " + (name == null ? "null" : name) + " AttributesToReturn: "
                + Utility.stringArrayToString(attrsToReturn));
        try {
            return context_.search(name, attrs, attrsToReturn);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.search(name, attrs, attrsToReturn);
        }
    }

    public NamingEnumeration search(String name, Attributes matchingAttributes) throws javax.naming.NamingException {
        logger.debug("search: name: " + (name == null ? "null" : name));
        try {
            return context_.search(name, matchingAttributes);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.search(name, matchingAttributes);
        }
    }

    public Object removeFromEnvironment(String propName) throws javax.naming.NamingException {
        logger.debug("removeFromEnviroment: propName: " + (propName == null ? "null" : propName));
        try {
            return context_.removeFromEnvironment(propName);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.removeFromEnvironment(propName);
        }

    }

    public NameParser getNameParser(String name) throws javax.naming.NamingException {
        logger.debug("getNameParser: name: " + (name == null ? "null" : name));
        try {
            return context_.getNameParser(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getNameParser(name);
        }
    }

    public Attributes getAttributes(Name name) throws javax.naming.NamingException {
        logger.debug("getAttributes: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.getAttributes(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getAttributes(name);
        }
    }

    public void bind(String name, Object obj, Attributes attrs) throws javax.naming.NamingException {
        logger.debug("bind: name: " + (name == null ? "null" : name));
        try {
            context_.bind(name, obj, attrs);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.bind(name, obj, attrs);
        }
    }

    public void destroySubcontext(Name name) throws javax.naming.NamingException {
        logger.debug("destroySubcontext: name: " + (name == null ? "null" : name.toString()));
        try {
            context_.destroySubcontext(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.destroySubcontext(name);
        }
    }

    public DirContext getSchema(Name name) throws javax.naming.NamingException {
        logger.debug("getSchema: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.getSchema(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getSchema(name);
        }
    }

    public Object lookup(String name) throws javax.naming.NamingException {
        logger.debug("lookup: name: " + (name == null ? "null" : name));
        Hashtable env = context_.getEnvironment();
        String url = (String) env.get(Context.PROVIDER_URL);
        int i = url.lastIndexOf("/") + 1;
        String urlFront = url.substring(0, i);
        String urlBack = url.substring(i, url.length());
        url = urlFront + name + "," + urlBack;
        env.put(Context.PROVIDER_URL, url);
        try {
            return new AutoReconnectDirContext(env, false);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return new AutoReconnectDirContext(env, false);
        }
    }

    public NamingEnumeration search(Name name, String filterExpr, Object[] filterArgs, SearchControls cons)
            throws javax.naming.NamingException {
        logger.debug("search: name: " + (name == null ? "null" : name.toString()) + " filterExpr: "
                + (filterExpr == null ? "null" : filterExpr));
        try {
            return context_.search(name, filterExpr, filterArgs, cons);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.search(name, filterExpr, filterArgs, cons);
        }
    }

    public NamingEnumeration list(Name name) throws javax.naming.NamingException {
        logger.debug("list: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.list(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.list(name);
        }
    }

    public DirContext getSchema(String name) throws javax.naming.NamingException {
        logger.debug("getSchema: name: " + (name == null ? "null" : name));
        try {
            return context_.getSchema(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getSchema(name);
        }
    }

    public void rebind(String name, Object obj, Attributes attrs) throws javax.naming.NamingException {
        logger.debug("rebind: name: " + (name == null ? "null" : name));
        try {
            context_.rebind(name, obj, attrs);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.bind(name, obj);
        }
    }

    public void rebind(Name name, Object obj) throws javax.naming.NamingException {
        logger.debug("rebind: name: " + (name == null ? "null" : name.toString()));
        try {
            context_.rebind(name, obj);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.rebind(name, obj);
        }
    }

    public NamingEnumeration search(Name name, Attributes matchingAttributes, String[] attributesToReturn)
            throws javax.naming.NamingException {
        logger.debug("search: name: " + (name == null ? "null" : name.toString()) + " AttributesToReturn: "
                + Utility.stringArrayToString(attributesToReturn));
        try {
            return context_.search(name, matchingAttributes, attributesToReturn);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.search(name, matchingAttributes, attributesToReturn);
        }
    }

    public void close() throws javax.naming.NamingException {
        logger.debug("Close");
        context_.close();
    }

    public String getNameInNamespace() throws javax.naming.NamingException {
        logger.debug("getNameInNamespace");
        try {
            return context_.getNameInNamespace();
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getNameInNamespace();
        }
    }

    public NamingEnumeration list(String name) throws javax.naming.NamingException {
        logger.debug("list: name: " + (name == null ? "null" : name));
        try {
            return context_.list(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.list(name);
        }
    }

    public NamingEnumeration listBindings(String name) throws javax.naming.NamingException {
        logger.debug("listBindings: name: " + (name == null ? "null" : name));
        try {
            return context_.listBindings(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.listBindings(name);
        }
    }

    public Hashtable getEnvironment() throws javax.naming.NamingException {
        logger.debug("getEnviroment");
        try {
            return context_.getEnvironment();
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getEnvironment();
        }
    }

    public Context createSubcontext(String name) throws javax.naming.NamingException {
        logger.debug("createSubcontext: name: " + (name == null ? "null" : name));
        try {
            return context_.createSubcontext(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.createSubcontext(name);
        }
    }

    public DirContext getSchemaClassDefinition(String name) throws javax.naming.NamingException {
        logger.debug("getSchemaClassDefinition: name: " + (name == null ? "null" : name));
        try {
            return context_.getSchemaClassDefinition(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getSchemaClassDefinition(name);
        }
    }

    public Attributes getAttributes(String name) throws javax.naming.NamingException {
        logger.debug("getAttributes: name: " + (name == null ? "null" : name));
        try {
            return context_.getAttributes(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getAttributes(name);
        }
    }

    public NamingEnumeration search(Name name, Attributes matchingAttributes) throws javax.naming.NamingException {
        logger.debug("search: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.search(name, matchingAttributes);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.search(name, matchingAttributes);
        }
    }

    public Object lookup(Name name) throws javax.naming.NamingException {
        logger.debug("lookup: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.lookup(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.lookup(name);
        }
    }

    public DirContext getSchemaClassDefinition(Name name) throws javax.naming.NamingException {
        logger.debug("getSchemaClassDefinition: name: " + (name == null ? "null" : name.toString()));
        try {
            return context_.getSchemaClassDefinition(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getSchemaClassDefinition(name);
        }
    }

    public Attributes getAttributes(Name name, String[] attrIds) throws javax.naming.NamingException {
        logger.debug("getAttributes: name: " + (name == null ? "null" : name.toString()) + " attrIds: "
                + Utility.stringArrayToString(attrIds));
        try {
            return context_.getAttributes(name, attrIds);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.getAttributes(name, attrIds);
        }
    }

    public void unbind(String name) throws javax.naming.NamingException {
        logger.debug("unbind: name: " + (name == null ? "null" : name));
        try {
            context_.unbind(name);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.unbind(name);
        }
    }

    public NamingEnumeration search(String name, String filterExpr, Object[] filterArgs, SearchControls cons)
            throws javax.naming.NamingException {
        logger.debug("search: name: " + (name == null ? "null" : name) + " filterExpr: "
                + (filterExpr == null ? "null" : filterExpr));
        try {
            return context_.search(name, filterExpr, filterArgs, cons);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            return this.search(name, filterExpr, filterArgs, cons);
        }
    }

    public void bind(Name name, Object obj) throws javax.naming.NamingException {
        logger.debug("bind: name: " + (name == null ? "null" : name.toString()));
        try {
            context_.bind(name, obj);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.bind(name, obj);
        }
    }

    public void modifyAttributes(Name name, int mod_op, Attributes attrs) throws javax.naming.NamingException {
        logger.debug("modifyAttributes: name: " + (name == null ? "null" : name.toString()));
        try {
            context_.modifyAttributes(name, mod_op, attrs);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.modifyAttributes(name, mod_op, attrs);
        }
    }

    public void rebind(Name name, Object obj, Attributes attrs) throws javax.naming.NamingException {
        logger.debug("rebind: name: " + (name == null ? "null" : name.toString()));
        try {
            context_.rebind(name, obj, attrs);
        } catch (javax.naming.CommunicationException e) {
            this.reconnect();
            this.bind(name, obj, attrs);
        }
    }

    /**
     * The method that actually does the reconnection.
     * 
     * @throws javax.naming.NamingException
     */
    private void reconnect() throws javax.naming.NamingException {
        logger.warn("RECONNECT called");
        int reconnectAttempts = 0;
        while (true) {
            try {
                Hashtable env = context_.getEnvironment();
                context_.close();
                context_ = new InitialDirContext(env); // no exception means it
                                                       // succeeded
                break;
            } catch (NamingException namingException) {
                // When this happens, it just means that it couldn't
                // reconnect... try again, until MAX_RECONNECT_ATTEMPTS is
                // reached
                logger.warn("Network connection down, trying to reestablish.... "
                        + (MAX_RECONNECT_ATTEMPTS - 1 - reconnectAttempts) + " Tries remaining");
                reconnectAttempts++;
                if (reconnectAttempts == MAX_RECONNECT_ATTEMPTS) {
                    logger.error("Network connection failed and was not restoreable!");
                    throw namingException;
                }
            }
        }
    }
}