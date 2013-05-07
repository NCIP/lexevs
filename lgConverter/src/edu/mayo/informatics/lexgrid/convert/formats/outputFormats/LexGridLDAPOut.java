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