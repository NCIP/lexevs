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
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Restriction;

/**
 * Holder for the directionalNames call on CodedNodeGraph.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToDirectionalNames implements Operation, Restriction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2978700134481702063L;
    private NameAndValueList directionalNames_;
    private NameAndValueList associationQualifiers_;

    public NameAndValueList getAssociationQualifiers() {
        return this.associationQualifiers_;
    }

    public NameAndValueList getDirectionalNames() {
        return this.directionalNames_;
    }

    public RestrictToDirectionalNames(NameAndValueList directionalNames, NameAndValueList associationQualifiers) {
        directionalNames_ = directionalNames;
        associationQualifiers_ = associationQualifiers;
    }
}