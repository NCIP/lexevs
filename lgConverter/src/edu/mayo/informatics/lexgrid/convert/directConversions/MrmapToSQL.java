package edu.mayo.informatics.lexgrid.convert.directConversions;



import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MRMAP2LexGrid;

public class MrmapToSQL {

    
    public org.LexGrid.codingSchemes.CodingScheme[] load(boolean loadMrSat, String mrMapFileLocation,  String mrSatFileLocation, LgMessageDirectorIF messageDirector,
            CodingSchemeManifest manifest){
        MRMAP2LexGrid map = new MRMAP2LexGrid(true, messageDirector, mrSatFileLocation, mrMapFileLocation);
        try {
            map.loadToRevision();
        } catch (LBRevisionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        new MrmapToSQL().load(true, args[0], args[1], null, null);
    }

}
