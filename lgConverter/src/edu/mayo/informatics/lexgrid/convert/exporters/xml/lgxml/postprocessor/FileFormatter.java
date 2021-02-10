
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.postprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
 *  Helper class that will remove/edit data that gets inserted by Castor
 *  because of how we call the marshaler.
 *  
 *  Tag(s) that get changed:
 *  
 *    Before:
 *      <lgRel:associationSource
 *    After:
 *      <lgRel:source
 *          
 *    Before:
 *      <lgRel:associationEntity
 *    After:
 *      <lgCon:associationEntity
 *  
 *  Extra Data to be removed:
 *    Entity tags extra schema information:
 *      Before:
 *           <lgCon:entity
 *           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *           xsi:schemaLocation="http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes  http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd"
 *           isActive="true" status="asf" entityCode="C0001" entityCodeNamespace="Automobiles">
 *      After:
 *           <lgCon:entity
 *           isActive="true" status="asf" entityCode="C0001" entityCodeNamespace="Automobiles">
 *           
 *  Some state information:
 *     -- Its OK for TEXT_XML_NS_XSI to be in the CS tag.  So we'll read it once, then set flag to remove all other occurrences. 
 *     -- Its OK for TEXT_XSI_SCHEMA_LOCATION to be in the CS tag.  So we'll read it once, then set flag to remove all other occurrences.
 *     
 */

public class FileFormatter {

    private final String TEXT_ASSOCIATION_SOURCE = "lgRel:associationSource";
    private final String TEXT_SOURCE = "lgRel:source";
    private final String TEXT_XML_NS_XSI = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
    private final String TEXT_XSI_SCHEMA_LOCATION="xsi:schemaLocation=\"http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes  http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd\"";
    private final String TEXT_ASSOCIATION_ENTITY_LGREL = "lgRel:associationEntity";
    private final String TEXT_ASSOCIATION_ENTITY_LGCON = "lgCon:associationEntity";
    
    
    private boolean retainXmlNsXsi = true;
    private boolean retainXsiSchemaLocation = true;
    
    public FileFormatter() {
        super();
    }
    
    public void process(File src) {
        File tempFile = this.createTempFileObjectFromOriginalFileObject(src);
        this.format(src, tempFile);
        this.deleteAndRenameFiles(src, tempFile);
    }
    
    private void format(File src, File tempFile) {
        // create temp file to write to
        
        
        // reader
        Reader r = null;
        BufferedReader in = null;
        
        // writer
        Writer w = null;
        BufferedWriter out = null;
        
        
        try {
            
            // create writers
            r = new FileReader(src);
            in = new BufferedReader(r);
            
            // crate readers
            w = new FileWriter(tempFile);
            out = new BufferedWriter(w);
            
            if(in != null) {
                boolean done = false;
                String inLine = null;
                String outLine = null;
                while(!done)
                {
                    inLine = in.readLine();
                    if(inLine == null) {
                        done = true;
                        if(out != null) {
                            out.flush();
                        }
                    } else {
                        outLine = this.processLine(inLine);
                        if(outLine != null) {
                            out.write(outLine);
                            out.newLine();
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
            
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /*
     * temp is now our good file.  delete src, and rename temp;
     */
    private void deleteAndRenameFiles(File src, File temp) {
        
        if(src != null && src.exists()) {
            boolean deleteResult = src.delete();
            if(deleteResult == false) {
                System.out.println("WARNING: file: " + src.getName() + " could not be deleted.");
                System.out.println("Please delete " + src.getName() + " manually.");
                System.out.println("File " + temp.getName() + " is the result of post processing and the final result of the export.");
            } else {
                boolean renameResult = temp.renameTo(src);
                if(renameResult == false) {
                    System.out.println("WARNING: rename of file: " + temp.getName() + " to " + src.getName() + " failed.");
                    System.out.println("File " + temp.getName() + " is the result of post processing and the final result of the export.");                    
                }
            }
        } else {
            System.out.println("WARNING: unexpected state: src file is null or does not exist");
        }
    }
    
    private String processLine(String lineOfText) {
        String newText = null;
        if(lineOfText.indexOf(this.TEXT_ASSOCIATION_SOURCE) != -1) 
        {
            newText = lineOfText.replace(this.TEXT_ASSOCIATION_SOURCE, this.TEXT_SOURCE);
        }
        else if(lineOfText.indexOf(this.TEXT_ASSOCIATION_ENTITY_LGREL) != -1) 
        {
            newText = lineOfText.replace(this.TEXT_ASSOCIATION_ENTITY_LGREL, this.TEXT_ASSOCIATION_ENTITY_LGCON);
        }
        else if(lineOfText.indexOf(this.TEXT_XML_NS_XSI) != -1) 
        {
            if(this.retainXmlNsXsi == true) 
            {
                this.retainXmlNsXsi = false;
                newText = lineOfText;
            } 
            else 
            {
                newText = null;
            }
        } 
        else if(lineOfText.indexOf(this.TEXT_XSI_SCHEMA_LOCATION) != -1) 
        {
            if(this.retainXsiSchemaLocation == true) 
            {
                this.retainXsiSchemaLocation = false;
                newText = lineOfText;
            } 
            else 
            {
                newText = null;
            }
        } 
        else 
        {
            newText = lineOfText;
        }
        
        return newText;
    }
    
    /*
     * original file:
     *    Automobiles2-exported.xml
     * temp file:
     *    tmpLgYYYYMMDDHHMMSS_Automobiles2-exported.xml
     *    
     *  Note: this will only create a File object.  It won't try to create it in the file system
     */
    private File createTempFileObjectFromOriginalFileObject(File originalFile) {

        String originalFileName = originalFile.getName();
        
        // create time stamp data
        final String DATE_PATTERN = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        Date now  = new Date();
        String timeStamp = sdf.format(now);
        
        // create new file with time stamp prefix
        StringBuffer sb = new StringBuffer();
        sb.append("tmpLg");
        sb.append(timeStamp);
        sb.append('_');
        sb.append(originalFile.getName());
        String newFileName = sb.toString();
        
        // create new file
        String originalFileCanonicalPath;
        String newFileCanonicalPath;
        File newFile = null;
        try {
            originalFileCanonicalPath = originalFile.getCanonicalPath();
            newFileCanonicalPath = originalFileCanonicalPath.replace(originalFileName, newFileName);
            newFile = new File(newFileCanonicalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFile;
    }
    
    private void testTmpFileCreate(final String FN) {
        File f1 = new File(FN);
        File f2 = this.createTempFileObjectFromOriginalFileObject(f1);
        try {
            System.out.println("f1: " + f1.getCanonicalPath());
            // System.out.println("f1: " + f1.getPath());
            System.out.println("f2: " + f2.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    private void test(String FN) {
        File f = new File(FN);
        this.process(f);
    }
    
    public static void main(String[] args) {
        final String INPUT_FILE_NAME1 = "resources/testData/Automobiles999-exported.xml";
        final String INPUT_FILE_NAME2 = "Automobiles999-exported.xml";
        final String INPUT_FILE_NAME3 = "colors_1.0.1.xml";
        
        FileFormatter ff = new FileFormatter();
        ff.test(INPUT_FILE_NAME3);
        
        // ff.testTmpFileCreate(INPUT_FILE_NAME1);
        // System.out.println("======================================================================");
        // ff.testTmpFileCreate(INPUT_FILE_NAME2);
    }

}