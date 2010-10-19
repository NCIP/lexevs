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
package edu.mayo.informatics.resourcereader.obo;

import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * This class stores the OBO Instance information
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOInstance extends OBOEntity {
    public String instanceOfTermId = null;
    public String instanceOfTermPrefix = null;
    public Vector<String> properties = new Vector<String>();

    public OBOInstance(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public boolean isReady() {
        return ((!StringUtils.isNull(id)) && (!StringUtils.isNull(name)) && (!StringUtils.isNull(instanceOfTermId)));
    }

    public void addProperty(String str) {
        if (!StringUtils.isNull(str))
            properties.add(str);
    }

    public String printIt() {
        return "Instance:{" + "prefix: " + prefix + ", " + "id: " + id + ", " + "altIds: [" + altIds.toString() + "], "
                + "name: " + name + ", " + "namespace: " + namespace + ", " + "Anonymous: " + isAnonymous + ", "
                + "isObsolete: " + isObsolete + ", " + "Instanceof(prefix:id): (" + instanceOfTermPrefix + ", "
                + instanceOfTermId + "), " + "comment: " + comment + ", " + "Synonyms: [" + synonyms.toString() + "], "
                + "dbXref: [" + dbXrefs.toString() + "], " + "replacedBy: " + replacedBy + ", " + "consider: "
                + consider + "} ";

    }

    public String toString() {
        return "Instance:{" + "prefix: " + prefix + ", " + "id: " + id + ", " + "name: " + name + ", " + "namespace: "
                + namespace + ", " + "Instanceof(prefix:id): (" + instanceOfTermPrefix + ", " + instanceOfTermId
                + "), " + "Anonymous: " + isAnonymous + ", " + "isObsolete: " + isObsolete + "}\n ";

    }
}