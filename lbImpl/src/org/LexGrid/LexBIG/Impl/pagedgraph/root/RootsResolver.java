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
package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

public interface RootsResolver extends Serializable {
    
    public enum ResolveDirection {FORWARD, BACKWARD}
    
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
            ResolveDirection direction,
            GraphQuery query,
            SortOptionList sortOptionList);
    
    public boolean isRootOrTail(ConceptReference ref);

    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String version, 
            String containerName,
            ResolveDirection direction, 
            GraphQuery graphQuery, 
            SortOptionList sortOptionList,
            int currentPosition, 
            int pageSize);
    
}