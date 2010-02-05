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
package edu.mayo.informatics.lexgrid.convert.emfConversions.obo1_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;

import org.LexGrid.messaging.LgMessageDirectorIF;

/**
 * Validate that the OBO format looks right.
 * 
 * @author: Pradip Kanjamala <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip
 *          Kanjamala</A>
 * @version subversion $Revision: 9509 $ checked in on $Date: 2006-06-28
 *          22:23:19 +0000 (Wed, 28 Jun 2006) $
 */
public class OBOFormatValidator {

    public static boolean isValidDocumentHeader(URI inputFile) throws Exception {
        String line;
        int linesRead = 0;
        BufferedReader reader = getBufferedReader(inputFile);
        while ((line = reader.readLine()) != null && linesRead < 1000) {
            linesRead++;
            int indexOfFormatVersion= line.indexOf(OBO2EMFConstants.FORMAT_VERSION);
            if (indexOfFormatVersion != -1) {
                String fversion = line.substring(indexOfFormatVersion+ OBO2EMFConstants.FORMAT_VERSION.length());
                fversion = fversion.trim();
                if (fversion.equals("1.0") || fversion.equals("1.2") || fversion.equals("GO_1.0")) {
                    return true;
                } else {
                    throw new Exception("The OBO file format needs to be 1.0 or 1.2 or GO_1.0");
                }
            }
        }
        return false;
    }

    private static BufferedReader getBufferedReader(URI input) throws MalformedURLException, IOException {
        if (input.getScheme().equals("file")) {
            return new BufferedReader(new FileReader(new File(input)));
        } else {
            return new BufferedReader(new InputStreamReader(input.toURL().openConnection().getInputStream()));
        }
    }

    public static boolean isValidDocumentContent(URI inputFile) throws Exception {
        String line;
        int linesRead = 0;
        BufferedReader reader = getBufferedReader(inputFile);
        boolean inTerm = false, foundTerm = false, foundRel = false;
        boolean foundTermId = true, foundTermName = true;
        while ((line = reader.readLine()) != null && linesRead < 1000) {
            linesRead++;
            if (line.startsWith("[Term]")) {
                inTerm = true;

                foundTerm = true;
                if (foundTermId && foundTermName) {
                    foundTermId = false;
                    foundTermName = false;
                } else {
                    // Previous Term did not have both a Id and Name....Invalid
                    // OBO Term
                    return false;
                }

            }

            if (line.startsWith("[Typedef]")) {
                inTerm = false;
                foundRel = true;
            }

            if (line.startsWith("id:")) {
                if (inTerm) {
                    foundTermId = true;
                }
            }
            if (line.startsWith("name:")) {
                if (inTerm) {
                    foundTermName = true;
                }

            }
        }

        if (foundTerm || foundRel) {
            return true;
        } else {
            return false;
        }
    }

}