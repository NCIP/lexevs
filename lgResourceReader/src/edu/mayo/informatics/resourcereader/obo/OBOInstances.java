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
package edu.mayo.informatics.resourcereader.obo;

import java.util.Collection;
import java.util.Hashtable;

import org.LexGrid.messaging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;
import edu.mayo.informatics.resourcereader.core.IF.ResourceEntity;
import edu.mayo.informatics.resourcereader.core.IF.ResourceException;

/**
 * This class stores the list of OBO Instances
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOInstances extends OBOCollection {
    private Hashtable<String, OBOInstance> instancesByID = new Hashtable<String, OBOInstance>();

    public OBOInstances(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public void addMember(ResourceEntity instancep) throws ResourceException {
        if ((instancep != null) && (instancep instanceof OBOInstance)) {
            OBOInstance instance = (OBOInstance) instancep;

            if (!StringUtils.isNull(instance.id)) {
                if (!instancesByID.containsKey(instance.id))
                    instancesByID.put(instance.id, instance);
            }
        }
    }

    public OBOInstance getMemberById(String id) throws ResourceException {
        return instancesByID.get(id);
    }

    public Collection<OBOInstance> getAllMembers() throws ResourceException {
        return instancesByID.values();
    }

    public long getMembersCount() {
        return instancesByID.size();
    }

    public String toString() {
        return instancesByID.toString();
    }
}