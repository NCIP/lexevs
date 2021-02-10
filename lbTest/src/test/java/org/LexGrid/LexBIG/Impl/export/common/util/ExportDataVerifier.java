
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