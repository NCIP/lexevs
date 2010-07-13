package org.LexGrid.LexBIG.Impl.export.xml.lgxml.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ExportDataVerifier {

    public static boolean verifyOutFileHasContent(File outFile, String searchTarget) {
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
