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
package org.LexGrid.LexBIG.Impl.export.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;

public class StringToFileUriConverter {

/**
     * Returns a file URI corresponding to the given string.
     * 
     * taken from org.LexGrid.LexBIG.admin.Util.java
     * 
     * @param s
     * @return java.net.URI
     * @throws org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException
     */
public static URI convert(String s) throws LBResourceUnavailableException {
        String trimmed = s.trim();
        try {
            // Resolve to file, treating the string as either a
            // standard file path or URI.
            File f;
            if (!(f = new File(trimmed)).exists()) {
                f = new File(new URI(trimmed.replace(" ", "%20")));
                if (!f.exists())
                    throw new FileNotFoundException();
            }

            // Accomodate embedded spaces ...
            return new URI(f.toURI().toString().replace(" ", "%20"));
        } catch (Exception e) {
            throw new LBResourceUnavailableException("UNABLE TO RESOLVE RESOURCE: " + trimmed);
        }
    }

}