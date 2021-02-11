
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats;

import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.baseFormats.LDAPBase;

/**
 * Details for writing to ldap.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LexGridLDAPOut extends LDAPBase implements OutputFormatInterface {
    public LexGridLDAPOut(String username, String password, String host, int port, String service) {
        this.username = username;
        this.password = password;
        if (host.toLowerCase().startsWith("ldap://")) {
            this.host = host.substring("ldap://".length());
        } else {
            this.host = host;
        }
        this.port = port;
        this.serviceDN = service;
    }

    public LexGridLDAPOut() {

    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.FAIL_ON_ERROR, Boolean.FALSE) };
    }
}