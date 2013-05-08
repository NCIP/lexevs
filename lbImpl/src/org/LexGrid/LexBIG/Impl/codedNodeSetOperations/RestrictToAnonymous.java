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

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;

/**
 * Holder for the RestrictToStatus operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToAnonymous implements Restriction, Operation {

    private static final long serialVersionUID = 4030470756164999491L;
    private AnonymousOption anonymousOption;

    public RestrictToAnonymous(AnonymousOption anonymousOption) throws LBParameterException {
        if (anonymousOption == null) {
            throw new LBParameterException("You must provide at least one parameter", "anonymousOption");
        }
        this.anonymousOption = anonymousOption;
    }

    public AnonymousOption getAnonymousOption() {
        return anonymousOption;
    }

    public void setAnonymousOption(AnonymousOption anonymousOption) {
        this.anonymousOption = anonymousOption;
    }
}