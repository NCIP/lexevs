
package edu.mayo.informatics.lexgrid.convert.directConversions;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MRMAP2LexGrid;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class MrmapToSQL {

    
    public org.LexGrid.codingSchemes.CodingScheme[] load(LgMessageDirectorIF messageDirector, URI mrMapFileLocation,  URI mrSatFileLocation,
            String nameForMappingScheme,
            String nameForMappingVersion,
            String nameForMappingURI,
            String sourceScheme,
            String sourceVersion,
            String sourceURI,
            String targetScheme,
            String targetVersion,
            String targetURI,
            Map.Entry<String, Relations> relationsMap,
            CodingSchemeManifest manifest) throws LBException{
        CodingScheme[] schemes = null;
        if(mrSatFileLocation == null){
            throw new LBException("Source for MRSAT is not available -- Loading without MRSAT is not available at this time");
        }
        if(nameForMappingScheme == null || nameForMappingVersion == null || nameForMappingURI == null){
            messageDirector.info("One or more designations for the mapping coding schemes metadata have not been made." +
            		"default metadata will be employed.");
            
        }
        if(sourceScheme == null || sourceVersion == null || sourceURI == null){
            messageDirector.info("One or more designations for a loaded source scheme have not been made." +
                    "Full resolution of concepts will not be available");
            
        }
        if(targetScheme == null || targetVersion == null || targetURI == null){
            messageDirector.info("One or more designations for a loaded target scheme have not been made." +
                    "Full resolution of concepts will not be available");
            
        }

        MRMAP2LexGrid map = new MRMAP2LexGrid(messageDirector, 
                mrSatFileLocation.getPath(), 
                mrMapFileLocation.getPath(), 
                nameForMappingScheme,
                nameForMappingVersion,
                nameForMappingURI,
                sourceScheme,
                sourceVersion,
                sourceURI,
                targetScheme,
                targetVersion,
                targetURI);
        try {
           schemes = map.loadToRevision(relationsMap);
        } catch (LBRevisionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return schemes;
    }
    /**
     * @param args
     */
//    public static void main(String[] args) {
//        URI map = null;
//        URI sat = null;
//        try {
//
//            try {
//                map = new URI(args[0]);
//                sat = new URI(args[1]);
//            } catch (URISyntaxException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            CodingScheme[] load = new MrmapToSQL().load(null, map, sat, null, null, null, null, null, null, null, null, null, null);
//        } catch (LBException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

}