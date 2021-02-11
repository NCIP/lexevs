
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