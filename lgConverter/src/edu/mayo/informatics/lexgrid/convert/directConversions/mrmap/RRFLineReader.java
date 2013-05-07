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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class RRFLineReader {
    
    BufferedReader reader = null;
    @SuppressWarnings("unused")
    private RRFLineReader(){
    }
    public RRFLineReader(String path) throws FileNotFoundException{
   
        reader = new BufferedReader(new FileReader(new File(path)));
    }

    public String[] readRRFLine() throws IOException{
        String string = reader.readLine();
        String[] rowArray = null;
        if(string != null){
        rowArray = string.split("\\|");
        for(String s: rowArray){
           s=s.trim(); 
        }}
       return rowArray;
    }
    
    public void close() throws IOException{
        reader.close();
    }
    
    
}