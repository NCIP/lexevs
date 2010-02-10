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
package org.lexevs.system.model;

import org.lexevs.system.ResourceManager;

/**
 * Holder for coding scheme information that I need to store.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LocalCodingScheme {
    public String codingSchemeName;
    public String version;

    public String getKey() {
        if (codingSchemeName.indexOf(ResourceManager.codingSchemeVersionSeparator_) == -1) {
            // if it is just a codingSchemeName - no version information
            // embedded, return a
            // combination
            return codingSchemeName + ResourceManager.codingSchemeVersionSeparator_ + version;
        } else {
            // if it already has the version stuff, then it should be good as
            // is.
            return codingSchemeName;
        }
    }

    public String getCodingSchemeNameWithoutVersion() {
        if (codingSchemeName.indexOf(ResourceManager.codingSchemeVersionSeparator_) == -1) {
            return codingSchemeName;
        } else {
            return codingSchemeName.substring(0, codingSchemeName
                    .indexOf(ResourceManager.codingSchemeVersionSeparator_));
        }
    }
}