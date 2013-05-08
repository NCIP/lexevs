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
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import org.LexGrid.commonTypes.Properties;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class LexEVSRelationsValues {
    
/**
 * Properties from Mapping Related
 * ATN and ATV fields
 * Listed here:
 * MAPSETGRAMMER
 * MAPSETRSAB
 * MAPSETTYPE
 * MAPSETVSAB
 * MTH_MAPFROMEXHAUSTIVE
 * MTH_MAPSETCOMPLEXITY
 * MTH_MAPTOEXHAUSTIVE
 * MTH_MAPFROMCOMPLEXITY
 * MTH_MAPTOCOMPLEXITY
 * MR
 * DA
 * ST
 * 
 * Where MRSAT CUI is the same as MAPSETCUI
 */
public Properties properties;
/**
 * indication this is a mappings container.
 */
public static final boolean ISMAP = true;
/**
 * Where ATN is TORSAB use corresponding ATV from MRSAT
 */
public String sourceName;
/**
 * Where ATN is FROMRSAB use corresponding ATV from MRSAT
 */
public String targetName;

/**
 * Where ATN is TOVSAB use corresponding ATV from MRSAT
 */
public String sourceVersion;
/**
 * Where ATN is FROMVSAB use corresponding ATV from MRSAT
 */
public String targetVersion;
/**
 * Contains MRSATCUI from appropriate 
 * mappings properties set in MRSAT.
 */
public String containerName;
/**
 * Where ATN is MAPSETVERSION use corresponding ATV from MRSAT
 */
public String setRelationVersion;
/**
 * Where ATN is SOS or MAPSETNAME use corresponding ATV from MRSAT
 */
public String entityDescription;




}