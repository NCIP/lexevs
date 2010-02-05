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
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Holder for the GetAllConcepts operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GetAllConcepts implements Operation {

    private static final long serialVersionUID = -1190549389939991576L;
    private String internalCodingSchemeName_;
    private String internalVersion_;

    public GetAllConcepts(String codingScheme, CodingSchemeVersionOrTag tagOrVersion) throws LBParameterException,
            LBResourceUnavailableException {
        String version = null;
        ResourceManager rm = ResourceManager.instance();
        if (tagOrVersion == null || tagOrVersion.getVersion() == null || tagOrVersion.getVersion().length() == 0) {
            version = rm.getInternalVersionStringFor(codingScheme,
                    (tagOrVersion == null ? null : tagOrVersion.getTag()));
        } else {
            version = tagOrVersion.getVersion();
        }

        // this throws the necessary exceptions if it can't be mapped / found
        internalCodingSchemeName_ = rm.getInternalCodingSchemeNameForUserCodingSchemeName(codingScheme, version);

        // make sure that it is active.
        String urn = rm.getURNForInternalCodingSchemeName(internalCodingSchemeName_);
        if (!rm.getRegistry().isActive(urn, version)) {
            throw new LBResourceUnavailableException("The requested coding scheme is not currently active");
        }

        internalVersion_ = version;
    }

    public GetAllConcepts(String internalCodingSchemeName, String internalVersionString) {
        // this throws the necessary exceptions if it can't be mapped / found
        internalCodingSchemeName_ = internalCodingSchemeName;
        internalVersion_ = internalVersionString;

    }

    @LgClientSideSafe
    public String getInternalCodingSchemeName() {
        return this.internalCodingSchemeName_;
    }

    @LgClientSideSafe
    public String getInternalVersionString() {
        return this.internalVersion_;
    }

}