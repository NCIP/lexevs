
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