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

/**
 * Holder for association loaded from Text files.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala </A>
 * @version subversion $Revision: 5296 $ checked in on $Date: 2007-05-16
 *          21:55:43 +0000 (Wed, 16 May 2007) $
 */
public class Association {
    private String relationName;
    private String sourceCodingScheme;
    private String sourceCode;
    private String targetCodingScheme;
    private String targetCode;

    public Association(String relationName, String sourceCodingScheme, String sourceCode, String targetCodingScheme,
            String targetCode) {
        this.relationName = relationName;
        this.sourceCodingScheme = sourceCodingScheme;
        this.sourceCode = sourceCode;
        this.targetCodingScheme = targetCodingScheme;
        this.targetCode = targetCode;
    }

    /**
     * Parse the line with format Relation="rel_name" sourceCodingScheme="srcCS"
     * sourceCode="code" targetCodingScheme="tgtCS" targetCode="code"
     * 
     * @param line
     */
    public Association(String line) {
        relationName = parseValue(line, "Relation");
        sourceCodingScheme = parseValue(line, "sourceCodingScheme");
        sourceCode = parseValue(line, "sourceCode");
        targetCodingScheme = parseValue(line, "targetCodingScheme");
        targetCode = parseValue(line, "targetCode");

    }

    public String parseValue(String line, String name) {
        String value = null;
        int pos;
        if ((pos = line.indexOf(name)) != -1) {
            int firstQuoteIndex = line.indexOf("\"", pos);
            int secondQuoteIndex = -1;
            if (firstQuoteIndex != -1) {
                secondQuoteIndex = line.indexOf("\"", firstQuoteIndex + 1);
            }
            if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
                value = line.substring(firstQuoteIndex + 1, secondQuoteIndex);
            }
        }
        return value;

    }

    public boolean isValid() {
        return (relationName != null && relationName.trim().length() != 0 && sourceCode != null
                && sourceCode.trim().length() != 0 && targetCode != null && targetCode.trim().length() != 0);
    }

    public String toString() {
        return "RelationName: " + relationName + "  Source: " + sourceCodingScheme + ":" + sourceCode + " Target: "
                + targetCodingScheme + ":" + targetCode + "\n";
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceCodingScheme() {
        return sourceCodingScheme;
    }

    public void setSourceCodingScheme(String sourceCodingScheme) {
        this.sourceCodingScheme = sourceCodingScheme;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getTargetCodingScheme() {
        return targetCodingScheme;
    }

    public void setTargetCodingScheme(String targetCodingScheme) {
        this.targetCodingScheme = targetCodingScheme;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Association))
            return false;
        Association assoc = (Association) o;
        return (this.relationName.equals(assoc.relationName)
                && this.sourceCodingScheme.equals(assoc.sourceCodingScheme) && this.sourceCode.equals(assoc.sourceCode)
                && this.targetCodingScheme.equals(assoc.targetCodingScheme) && this.targetCode.equals(assoc.targetCode));
    }
}