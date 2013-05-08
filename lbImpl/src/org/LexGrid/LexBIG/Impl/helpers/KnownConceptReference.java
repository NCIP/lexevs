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
package org.LexGrid.LexBIG.Impl.helpers;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

/**
 * This class is used when I want to create a conceptReference that I know
 * doesn't require any translating of the codingScheme.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class KnownConceptReference extends ConceptReference {
    private static final long serialVersionUID = 752926435292695777L;

    public KnownConceptReference(String codingScheme, String conceptCode, String namespace) {
        this.setCodingSchemeName(codingScheme);
        this.setCode(conceptCode);
        this.setCodeNamespace(namespace);
    }

}