
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