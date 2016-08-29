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
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.AbstractJoinQueryRestriction;
import org.LexGrid.LexBIG.Impl.dataAccess.RestrictionImplementations;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.lucene.search.Query;
import org.lexevs.exceptions.InternalException;

/**
 * Holder for the RestrictToCodes operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToCodes extends AbstractJoinQueryRestriction {

    private static final long serialVersionUID = -8005913414543273170L;
    private ConceptReferenceList conceptReferenceList_;

    @LgClientSideSafe
    public ConceptReferenceList getConceptReferenceList() {
        return this.conceptReferenceList_;
    }

    public RestrictToCodes(ConceptReferenceList codeList) throws LBParameterException {
        try {
            if (codeList == null || codeList.getConceptReferenceCount() == 0) {
                throw new LBParameterException("The parameter is required", "codeList");
            }

            conceptReferenceList_ = codeList;
        } catch (LBParameterException e) {
            throw e;
        }
    }

    @Override
    protected Query doGetQuery() throws LBException, InternalException {
        return RestrictionImplementations.getQuery(this);
    }

    /**
     * Do not use this method. Its here as a hack to make a restriction to only
     * active codes work when they didn't provided any other restrictions.
     */
    public RestrictToCodes() {
        conceptReferenceList_ = null;
    }
}