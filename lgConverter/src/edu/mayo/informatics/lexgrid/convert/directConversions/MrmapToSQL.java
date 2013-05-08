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
package edu.mayo.informatics.lexgrid.convert.directConversions;



import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.codingSchemes.CodingScheme;

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
           schemes = map.loadToRevision();
        } catch (LBRevisionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return schemes;
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        URI map = null;
        URI sat = null;
        try {

            try {
                map = new URI(args[0]);
                sat = new URI(args[1]);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            CodingScheme[] load = new MrmapToSQL().load(null, map, sat, null, null, null, null, null, null, null, null, null, null);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}