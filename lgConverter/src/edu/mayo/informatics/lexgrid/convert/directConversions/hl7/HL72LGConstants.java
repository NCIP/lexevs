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
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;

/**
 * HL7 default values as implied in the mapping.
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * 
 */
public class HL72LGConstants {

    // *********Required Defaults******************************//
    // HL7 has no default value for a coding scheme level language
    // value so we'll use 'en' on Russ Hamm's advice. Cannot be
    // null in LexGrid for EMF
    public static final String DEFAULT_LANGUAGE_EN = "en";
    public static final String ASSOCIATION_IS_A = "is_a";
    public static final String ASSOCIATION_HAS_SUBTYPE = "hasSubtype";
    public static final String DEFAULT_URN = "http://www.hl7.org/Library/data-model/RIM";
    public static final String DEFAULT_ROOT_NODE = "@";
    // Replaces values that cannot be null in LexGrid and which do not
    // have an appropriate default value
    public static final String MISSING = SQLTableConstants.TBLCOLVAL_MISSING;

    // presentation property for LexGrid
    public static final String PROPERTY_PRINTNAME = "print_name";
    public static final String PROPERTY_DEFINITION = "definition";
    public static final String PROPERTY_TYPE_GENERIC = "property";
    public static final String PROPERTY_NAME_OID = "oid";

    // *********SQL Statements for MSACCESS RIM db**************//
    public static final String GET_CODING_SCHEMES = "SELECT codeSystemid, "
            + "codeSystemType, codeSystemName, fullName, description, "
            + "releaseId, copyrightNotice FROM VCS_Code_System WHERE codeSystemid = ?";
    public static final String GET_CONCEPT = "SELECT internalId, conceptCode2, "
            + SQLTableConstants.TBLCOL_CONCEPTSTATUS + " FROM VCS_concept_code_xref WHERE codeSystemid2 = ?";
    public static final String CODE_SYSTEM_OID = "2.16.840.1.113883.5.22";
   
   

}