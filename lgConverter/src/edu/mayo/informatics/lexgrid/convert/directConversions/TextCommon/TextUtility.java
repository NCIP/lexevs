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
package edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.LexGrid.messaging.LgMessageDirectorIF;

/**
 * Common bits for the Text -> foo loaders.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 5882 $ checked in on $Date: 2007-05-16
 *          21:55:43 +0000 (Wed, 16 May 2007) $
 */
public class TextUtility {
    public static Concept getParent(Concept[] concepts, int curPos) {
        int depth = concepts[curPos].depth;
        for (int i = curPos; i >= 0; i--) {
            if (concepts[i].depth < depth) {
                return concepts[i];
            }
        }
        return null;
    }

    public static CodingScheme readAndVerifyConcepts(String fileLocation, LgMessageDirectorIF messages, String token,
            boolean forceTypeB) throws Exception {
        CodingScheme codingScheme = new CodingScheme();
        codingScheme.isTypeB = forceTypeB;
        int uniqueCodeIdentifer = 1;
        messages.info("Reading file into memory");
        try {
            ArrayList concepts = new ArrayList();
            ArrayList associations = new ArrayList();
            BufferedReader fileReader = new BufferedReader(new FileReader(fileLocation));
            String line = fileReader.readLine();

            int lineNo = 1;
            while (line != null) {
                if (line.length() > 0 && line.charAt(0) != '#') {
                    // read the "special" first line
                    StringTokenizer tokenizer = new StringTokenizer(line, token);

                    if (tokenizer.countTokens() < 4) {
                        messages
                                .fatalAndThrowException("FATAL ERROR - The beginning of the file must contain a token seperated"
                                        + " line that contain (in this order) 'codingSchemeName,codingSchemeId,defaultLanguage,formalName'"
                                        + "\nThe line may contain the following values (in this order) 'version,source,description,copyright'");
                    }
                    codingScheme.codingSchemeName = tokenizer.nextToken().trim();
                    codingScheme.codingSchemeId = tokenizer.nextToken().trim();
                    codingScheme.defaultLanguage = tokenizer.nextToken().trim();
                    codingScheme.formalName = tokenizer.nextToken().trim();

                    if (tokenizer.hasMoreTokens()) {
                        codingScheme.representsVersion = tokenizer.nextToken().trim();
                    }
                    if (tokenizer.hasMoreTokens()) {
                        codingScheme.source = tokenizer.nextToken().trim();
                    }
                    if (tokenizer.hasMoreTokens()) {
                        codingScheme.entityDescription = tokenizer.nextToken().trim();
                    }
                    if (tokenizer.hasMoreTokens()) {
                        codingScheme.copyright = tokenizer.nextToken().trim();
                    }

                    lineNo++;
                    line = fileReader.readLine();
                    break;
                }
                lineNo++;
                line = fileReader.readLine();
            }

            // read the rest of the lines
            while (line != null) {
                if (line.length() > 0 && line.charAt(0) != '#') {
                    if (line.startsWith("Relation")) {
                        Association assoc = new Association(line);
                        if (assoc.isValid() && !associations.contains(assoc)) {
                            associations.add(assoc);
                        } else {
                            messages.info("WARNING - Line number " + lineNo
                                    + " is missing required information.  Skipping.");
                        }
                    } else {
                        Concept temp = new Concept(line, token, codingScheme);
                        if (temp.name != null) {
                            concepts.add(temp);
                        } else {
                            messages.info("WARNING - Line number " + lineNo
                                    + " is missing required information.  Skipping.");
                        }
                    }

                }
                lineNo++;
                line = fileReader.readLine();
            }

            Concept[] allConcepts = (Concept[]) concepts.toArray(new Concept[concepts.size()]);
            concepts = null;

            for (int i = 0; i < allConcepts.length; i++) {
                if (codingScheme.isTypeB) {
                    if (allConcepts[i].code == null || allConcepts[i].code.length() == 0) {
                        // move them to the correct places for type B.
                        allConcepts[i].code = allConcepts[i].name;
                        allConcepts[i].name = allConcepts[i].description;
                        allConcepts[i].description = null;
                    }
                    // see if this code already exists - if so, the names must
                    // match.
                    for (int j = 0; j < i; j++) {
                        if (allConcepts[j].code.equals(allConcepts[i].code)
                                && (((allConcepts[i].name == null && allConcepts[j].name != null) || (allConcepts[i].name != null && allConcepts[j].name == null)) || ((allConcepts[i].name != null && allConcepts[j].name != null) && (!allConcepts[i].name
                                        .equals(allConcepts[j].name))))) {
                            // codes match, names don't, fatal error.
                            messages.fatalAndThrowException("FATAL ERROR - Concept code " + allConcepts[i].code
                                    + " occurs twice with different names.  This is illegal.");

                        }
                    }

                } else {
                    // type A - need to generate codes.
                    boolean found = false;
                    // see if this name already exists - if so, use that code.
                    for (int j = 0; j < i; j++) {
                        if (allConcepts[j].name.equals(allConcepts[i].name)) {
                            allConcepts[i].code = allConcepts[j].code;
                            found = true;
                            break;
                        }
                    }
                    // doesn't exist = generate a new code.
                    if (!found) {
                        allConcepts[i].code = uniqueCodeIdentifer++ + "";
                    }
                }
            }

            codingScheme.concepts = allConcepts;
            codingScheme.associations = (Association[]) associations.toArray(new Association[associations.size()]);

            return codingScheme;
        } catch (FileNotFoundException e) {
            messages.fatalAndThrowException("File not found", e);
            // this is actually unreachable
            return null;
        }
    }
}