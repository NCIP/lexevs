package org.LexGrid.LexBIG.Impl.export.owlrdf.util;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.export.common.util.Logger;
import org.LexGrid.LexBIG.Impl.export.common.util.StringToFileUriConverter;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;

public class ImportHelper {
    
    /*
     * runs the OWL Importer 
     */
    public static boolean importOwl(String fileName) throws LBException {
    	
    	boolean rv = false;
    	
    	Logger.log("ImportHelper: importOwl: exit");
    	Logger.log("ImportHelper: importOwl: source file: " + fileName);
    	// convert filename to URI
    	URI source = StringToFileUriConverter.convert(fileName);
    	
    	// get the importer from LexEVS
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        LexBIGServiceManager lbsm = lbs.getServiceManager(null);
        OWL_Loader loader = (OWL_Loader) lbsm.getLoader(org.LexGrid.LexBIG.Impl.loaders.OWLLoaderImpl.name);

        // do the load
        int memSafe = 1; // 1 = all in memory, 2 = cache owl to database
        URI manifest = null;
        loader.load(source, manifest, memSafe, false, false);
    	
    	// activate the coding scheme
        AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
        for (int i = 0; i < refs.length; i++) {
            AbsoluteCodingSchemeVersionReference ref = refs[i];
            lbsm.activateCodingSchemeVersion(ref);
            Logger.log("Scheme activated>> " + ref.getCodingSchemeURN() + " Version>> "
                    + ref.getCodingSchemeVersion());
        }
        Logger.log("ImportHelper: importOwl: exit");
        rv = true;
        return rv;
    }    
}
