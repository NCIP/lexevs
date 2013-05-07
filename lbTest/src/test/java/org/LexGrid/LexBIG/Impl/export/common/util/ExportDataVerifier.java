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
package org.LexGrid.LexBIG.Impl.export.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ExportDataVerifier {
	
    public static boolean verifyOutFileHasContent(String fullyQualifiedFileName, String[] searchTargetAr) {
    	
    	Logger.log("ExportDataVerifier: verifyOutFileHasContent: file: " + fullyQualifiedFileName);
    	Logger.log("ExportDataVerifier: verifyOutFileHasContent: search target array: ");
    	for(int i=0; i<searchTargetAr.length; ++i) {
    		Logger.log(searchTargetAr[i]);
    	}
    	
    	File outFile = new File(fullyQualifiedFileName);
    	
        boolean rv = false;
        Reader r = null;
        BufferedReader in = null;
        
        try {
            r = new FileReader(outFile);
            in = new BufferedReader(r);
            if(in != null) {
                boolean done = false;
                int i = 0;
                boolean stayInBlock = false;
                String line = null;
                while(!done)
                {
                    line = in.readLine();
                    if(line == null) {
                        done = true;
                    } else {
                    	i = 0;
                    	stayInBlock = true;
                    	while(!done && stayInBlock==true) {
                    		//Logger.log("\tline:             " + line);
                    		//Logger.log("\tsearchTargetAr[ " + i + "]:" + searchTargetAr[i]);
                    		if(line.contains(searchTargetAr[i]) == false){
                    			stayInBlock = false;
                    		} else {
                    			if(i+1 == searchTargetAr.length) {  // end of targetArray -- item found 
                    				done = true;
                    				rv = true;
                    				Logger.log("      verifyOutFileHasContent: search item found!");
                    			} else {
                        			line = in.readLine();
                        			if(line == null) {
                        				done = true;
                        			}
                        			++i;                    				
                    			}
                    		}
                    	}
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rv;
    }

	

    public static boolean verifyOutFileHasContent(String fullyQualifiedFileName, String searchTarget) {
    	
    	Logger.log("ExportDataVerifier: verifyOutFileHasContent: file: " + fullyQualifiedFileName);
    	Logger.log("ExportDataVerifier: verifyOutFileHasContent: search string: " + searchTarget);
    	
    	File outFile = new File(fullyQualifiedFileName);
    	
        boolean verifyTrue = false;
        //final String searchTarget = "blah";
        Reader r = null;
        BufferedReader in = null;
        try {
            r = new FileReader(outFile);
            in = new BufferedReader(r);
            if(in != null) {
                boolean done = false;
                String line = null;
                while(!done)
                {
                    line = in.readLine();
                    if(line == null) {
                        done = true;
                    } else {
                        if(line.contains(searchTarget) == true) {
                            verifyTrue = true;
                            done = true;
                        }                        
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return verifyTrue;
    }
	
	
}