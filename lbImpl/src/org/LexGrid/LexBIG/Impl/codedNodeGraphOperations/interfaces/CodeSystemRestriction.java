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
package org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Interface for a code system restriction on a codedNodeGraph operation
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class CodeSystemRestriction implements Restriction, Operation {
    /**
     * 
     */
    private static final long serialVersionUID = 686440611568951158L;
    private String supportedCodeSystemOrURN_;

    public CodeSystemRestriction(String codeSystem) throws LBParameterException {
        if (codeSystem == null || codeSystem.length() == 0) {
            throw new LBParameterException("Missing parameter", "codeSystem");
        }

        supportedCodeSystemOrURN_ = codeSystem;
    }

    @LgClientSideSafe
    public String getSupportedCodeSystemOrURN() {
        return this.supportedCodeSystemOrURN_;
    }

}