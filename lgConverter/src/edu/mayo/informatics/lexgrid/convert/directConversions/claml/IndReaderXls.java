package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

public class IndReaderXls {

    private LinkedHashMap<String, IndTerm> termMap_;
    
    private LinkedHashMap<String, LinkedHashMap<String,ArrayList<IndTerm>>> chapMap;
    private BufferedReader buffRead_ = null;
    
    private boolean skipMainRead = false;   // Vol 2 terms shouldn't be read twice

    public IndReaderXls() throws LgConvertException, FileNotFoundException {
    }
    
    public LinkedHashMap<String, LinkedHashMap<String,ArrayList<IndTerm>>> getTermMap(LinkedHashMap<String, IndTerm> termMap, String docFilePath) throws LgConvertException, FileNotFoundException 
    {
        termMap_ = termMap;
        chapMap = new LinkedHashMap<String, LinkedHashMap<String,ArrayList<IndTerm>>>();
        
        File file = new File(docFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        
        try {
          fis = new FileInputStream(file);
          bis = new BufferedInputStream(fis);
          buffRead_ = new BufferedReader(new InputStreamReader(bis));
        
          String line = readLine();

          while (line != null) 
          {
              if(docFilePath.contains("7321H")
                 ||docFilePath.contains("7322H"))
              {
                  skipMainRead = false;
                  line = parseVol2(line);
              }
              else if(docFilePath.contains("7676H") 
                      ||docFilePath.contains("7678H")
                      ||docFilePath.contains("7679H")
                      ||docFilePath.contains("7680H"))
              {
                  line = parseVol4(line);
              }
              else if(docFilePath.contains("7739"))
              {
                  line = parseVol5(line);
              }
              else if(docFilePath.contains("8138")
                      ||docFilePath.contains("8158")
                      ||docFilePath.contains("8196")
                      ||docFilePath.contains("8410")
                      ||docFilePath.contains("8250")
                      ||docFilePath.contains("8619")
                      ||docFilePath.contains("8842"))
              {
                  line = parseVol6(line);
              }
              else if(docFilePath.contains("8013")
                      ||docFilePath.contains("8014"))
              {
                  line = parseVol8(line);
              }
              
              if(line!=null && !line.startsWith("$$") && !skipMainRead)
                {
                  line = readLine();
                }
          }
          fis.close();
          bis.close();
          buffRead_.close();
        
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }

           return chapMap;
      }
    
    private String parseVol2(String line) throws IOException
    {
        if(line.contains("Exact Match Y or N"))
        {   // Parasitic7321H_exact contains headers in different order than we will print
            return "";
        }
        
        if(line != null )
        {
            IndTerm indTerm = new IndTerm();
            ArrayList<IndTerm> indTerms = null;
            LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
            String term = "";
            String chapter = "";
            String subChapter = "";
            String[] terms;

            if(line.startsWith("SUPERFAMILY"))
            {   // split super family into chapter && subclass so it may hold terms
                terms = line.split(" ");
                chapter = terms[0];
                line = terms[1];
            }
            else
            {   
                chapter = line.replaceAll("\\t+", "");
                line = readLine();
            }

            // get all the subchapters within the family && put them into the chapter's map
            while(line!=null
                    && !(line.startsWith("FAMILY") || line.startsWith("SUPERFAMILY")) )
            {
                indTerms = new ArrayList<IndTerm>();

                terms = line.split("\\t+");
                while(terms.length==0 || terms[0].equalsIgnoreCase(""))
                {   // a blank line with tabs was read so keep reading until find content
                    line = readLine();
                    terms = line.split("\\t+");
                }

                subChapter = terms[0].replaceAll( "\\t+", "" );

                if(terms.length <= 2 )
                {   // subChapter was on its own line
                    line = readLine();
                }
                else
                {   // subchapter on same line as term
                    line = "";
                    for (int i = 1; i < terms.length; i++) 
                    {
                        line = line +"\t" +terms[i];
                    }
                }

                if(line!=null)
                {   
                    terms = line.split("\\t+");
                    while(terms.length==0 || (terms.length==2 && terms[1].equalsIgnoreCase(" ")))
                    {   // a blank line with tabs was read so keep reading until find content
                        line = readLine();
                        line = line.replaceAll("\"", "");   // terms with quotes not matching in map
                        terms = line.split("\\t+");
                    }
                }

                // get all the terms for the subchapter && put them into the subchapter's map
                // terms have first columns blank so stop reading once find something in term[0]
                while(line!=null && terms.length>0 && terms[0].equalsIgnoreCase(""))   
                {
                    if(terms.length == 2)
                    {   // found a blank line
                        break;
                    }
                    
                    int offset = 0;                 // no offset needed if exactMatch value is present
                    if(!terms[1].equalsIgnoreCase("N") && !terms[1].equalsIgnoreCase("Y") )
                    {   // since there is no exactMatch value, the term array is offset by 1 place
                        offset = 1;
                    }
                    term = terms[2-offset];
                    term = term.replaceAll("\"", "");
                    term = term.trim();
                    
                    if(term.contains("malariae malaria"))
                    {
                        System.out.println(term);
                    }
                    if(term.contains("hyperreactive malarial splenomegaly"))
                    {
                        System.out.println(term);
                    }
                    indTerm = termMap_.get(term);
                    if(offset==0)
                    {   
                        indTerm.setExactMatch(terms[1]);
                    }
                    indTerm.setXlsCode(terms[3-offset]);
                    if(terms.length >= 5)
                    {
                        indTerm.setXlsCode2(terms[4]);
                    }
                    indTerms.add(indTerm);
                    
                    line = readLine();
                    
                    if(line!=null)
                    {   
                        line = line.replaceAll("\"", "");   // terms with quotes not matching in map
                        terms = line.split("\\t+");
                    }
                }
                subChapMap.put(subChapter, indTerms);  
            }
            chapMap.put(chapter, subChapMap);
        }  
        
        skipMainRead = true;
        return line;
    }
    
    private String parseVol4(String line) throws IOException
    {
        if(line != null )
        {
            if(line.contains("Chapter One"))
            {   // chapter - next line subchapter in col B - next line terms in col C - terms are name / code / space / exactMatch
                line = parseVol4Ch1(line);
            }
            else if(line.contains("Chapter Two"))
            {   // chapter - same line subchapter in col B - same line term in col C - terms are name / code / space / exactMatch
                line = parseVol4Ch2(line);
            }
            else if(line.contains("Chapter 3"))
            {   // chapter - no subchapter - terms next line in col C - terms are name / code / space / exactMatch
                line = parseVol4Ch3(line);
            }
            else if(line.contains("Chapter 4"))
            {   // chapter - next line subchapter in col B - next line terms in col C - terms are name / exactMatch / code
                line = parseVol4Ch4(line);
            }
            else if(line.contains("\"Chapter 5"))
            {   // chapter - skip line - subchapter in col B - same line term in col C  - terms are name / exactMatch / code
                line = parseVol4Ch5(line);
            }
            else if(line.contains("Chapter 6"))
            {   // chapter with no name - next line subchapter in col B - next line terms  - terms are name / exactMatch / code
                line = parseVol4Ch6(line);
            }
            else if(line.contains("Chapter 7"))
            {   // chapter with no name - same line subchapter in col B - next line terms in col C -  - terms are name / exactMatch / code
                line = parseVol4Ch7(line);
            }
            else if(line.contains("Chapter 8"))
            {   // chapter with name on next line - subchapter on line with chapter name in col B - next line terms  - terms are name / exactMatch / code 
                line = parseVol4Ch8(line);
            }
            else if(line.contains("Chapter 9"))
            {   // chapter with name on next line - next line subchapter in column B - next line terms  - terms are name / exactMatch / code
                line = parseVol4Ch9(line);
            }
            else if(line.contains("Chapter 10"))
            {   // chapters with name on next line - next line subchapters - same line terms - terms are name / exactMatch / code
                line = parseVol4Ch10(line);
            }
            else
            {
                System.out.println(line);
                line = readLine();
            }
        }
        return line;
    }
    
    private String parseVol4Ch1(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String term = "";
        String chapter = "";
        String subChapter = "";
        String[] terms;
        
        chapter = line.trim();
        line = readLine();

        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && !line.startsWith("Chapter"))
        {
            indTerms = new ArrayList<IndTerm>();

            terms = line.split("\\t+");
            subChapter = terms[1].replaceAll( "\\t+", "" );

            if(terms.length==3)
            {
                line = readLine();
            }
            else
            {   // subchapter on same line as term
                line = "";
                for (int i = 2; i < terms.length; i++) 
                {
                    line = line +"\t" +terms[i];
                }
            }
            terms = line.split("\\t+");

            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null && !isAllUpper(terms[1]))   
            {
                if(line.contains("I85.0"))
                {
                    line = readLine();
                    terms = line.split("\\t+");
                    continue;
                }
                
                term = terms[1];
                term = term.replaceAll("\"", "");
                term = term.trim();
                indTerm = termMap_.get(term);
                if(terms[3].equalsIgnoreCase("No") || terms[3].equalsIgnoreCase("Yes"))
                {   
                    indTerm.setExactMatch(terms[3]);
                }
                else
                {
                    indTerm.setExactMatch(terms[4]);
                }
                indTerm.setXlsCode(terms[2]);
                indTerms.add(indTerm);

                line = readLine();
                terms = line.split("\\t+");
                
                if(line.contains("I85.0"))
                {
                    line = readLine();
                    terms = line.split("\\t+");
                }
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    private String parseVol4Ch2(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;
        chapter = line;
        terms = line.split("\\t+");
        chapter = terms[0];
        terms[0] = "";
        
        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && !terms[0].startsWith("Chapter"))
        {
            indTerms = new ArrayList<IndTerm>();

            subChapter = terms[1].replaceAll( "\\t+", "" );
            terms[1] = "";
            int offset = 1;    // the first line has subchapter on same line so every offset by one
            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null && !terms[0].startsWith("Chapter") && !isAllUpper(terms[1]))   
            {
                term = terms[1+offset].replaceAll("\"", "");
                term = term.trim();
                
                if(term.contains("chronic superficial fundal gastritis"))
                {
                    System.out.println(term);
                }
                
                if(term.contains("congenital junctional epidermolysis bullosa"))
                {   // spreadsheet used a synonym as term with code && exactMatch info
                    indTerm = termMap_.get(term);
                    indTerm.setExactMatch(terms[4]);
                    indTerm.setXlsCode(terms[2]);
                    
                    line = readLine();
                    terms = line.split("\\t+");
                    
                    indTerm.setExactMatch2(terms[3]);
                    indTerm.setXlsCode2(terms[2]);
                }
                else
                {
                    indTerm = termMap_.get(term);
                    
                    indTerm.setExactMatch(terms[3+offset]);
                    indTerm.setXlsCode(terms[2+offset]);
                    
                }
                indTerms.add(indTerm);

                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                }
                offset = 0;
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    
    private String parseVol4Ch3(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;
        
        chapter = line.trim();
        
        line = readLine();
        terms = line.split("\\t+");
        
        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null)
        {
            indTerms = new ArrayList<IndTerm>();

            // get all the terms for the subchapter && put them into the subchapter's map
            while(line!=null)   
            {
                term = terms[1].replaceAll("\"", "");
                term = term.trim();

                indTerm = termMap_.get(term);
                indTerm.setXlsCode(terms[2]);
                indTerm.setExactMatch(terms[3]);
                indTerms.add(indTerm);

                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                }

            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    
    
    private String parseVol4Ch4(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;
        
        chapter = line.trim();
        
        line = readLine();
        terms = line.split("\\t+");
        
        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && !terms[0].startsWith("\"Chapter"))
        {
            indTerms = new ArrayList<IndTerm>();

            subChapter = terms[1].replaceAll( "\\t+", "" );

            if(terms.length > 4)
            {   // subchapter on same line as term so get rid of it && prepare rest
                line = "";
                for (int i = 2; i < terms.length; i++) 
                {
                    line = line +"\t" +terms[i];
                }
                terms = line.split("\\t+");
            }
            else
            {
                line = readLine();
                terms = line.split("\\t+");
                while(terms.length == 0)
                {
                    line = readLine();
                    terms = line.split("\\t+");
                }
            }
            
            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null && !terms[0].startsWith("\"Chapter") && !isAllUpper(terms[1]))  
            {
                term = terms[1].replaceAll("\"", "");
                term = term.trim();

                indTerm = termMap_.get(term);
                indTerm.setXlsCode(terms[3]);
                indTerm.setExactMatch(terms[2]);
                indTerms.add(indTerm);

                line = readLine();
                terms = line.split("\\t+");
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    private String parseVol4Ch5(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;
        
        line = line.replaceAll("\"", "");
        chapter = line.trim();
        
        line = readLine();
        terms = line.split("\\t+");
        while(terms.length == 0)
        {   // blank lines
            line = readLine();
            terms = line.split("\\t+");
        }
        
        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && !terms[0].startsWith("\"Chapter"))
        {
            indTerms = new ArrayList<IndTerm>();

            subChapter = terms[1].replaceAll( "\\t+", "" );

            if(terms.length > 4)
            {   // subchapter on same line as term so get rid of it && prepare rest
                line = "";
                for (int i = 2; i < terms.length; i++) 
                {
                    line = line +"\t" +terms[i];
                }
                terms = line.split("\\t+");
            }
            else
            {
                line = readLine();
                terms = line.split("\\t+");
                while(terms.length == 0)
                {   // blank lines
                    line = readLine();
                    terms = line.split("\\t+");
                }
            }
            
            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null && !terms[0].startsWith("\"Chapter") && !isAllUpper(terms[1]))  
            {
                term = terms[1].replaceAll("\"", "");
                term = term.trim();

                indTerm = termMap_.get(term);
                indTerm.setXlsCode(terms[3]);
                indTerm.setExactMatch(terms[2]);
                indTerms.add(indTerm);

                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                }
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    private String parseVol4Ch6(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;

        line = line.replaceAll("\"", "");
        chapter = line.trim();
        
        line = readLine();
        terms = line.split("\\t+");
        while(terms.length == 0)
        {   // blank lines
            line = readLine();
            terms = line.split("\\t+");
        }

        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && (!terms[0].startsWith("\"Chapter") && !terms[0].startsWith("Chapter")) )
        {
            indTerms = new ArrayList<IndTerm>();

            subChapter = terms[1].replaceAll( "\\t+", "" );

            if(terms.length > 4)
            {   // subchapter on same line as term so get rid of it && prepare rest
                line = "";
                for (int i = 2; i < terms.length; i++) 
                {
                    line = line +"\t" +terms[i];
                }
                terms = line.split("\\t+");
            }
            else
            {
                line = readLine();
                terms = line.split("\\t+");
                while(terms.length == 0)
                {   // blank lines
                    line = readLine();
                    terms = line.split("\\t+");
                }
            }

            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null && !terms[0].startsWith("\"Chapter") && !isAllUpper(terms[1]))  
            {
                term = terms[1].replaceAll("\"", "");
                term = term.trim();
                
                if(term.equalsIgnoreCase("floating gallbladder"))
                {
                    term = "\"floating\" gallbladder";
                }

                indTerm = termMap_.get(term);
                indTerm.setXlsCode(terms[3]);
                indTerm.setExactMatch(terms[2]);
                indTerms.add(indTerm);

                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                }
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    private String parseVol4Ch7(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;
        
        // chapter number && first swubchapter are on same line
        line = line.replaceAll("\"", "");
        terms = line.split("\\t+");
        chapter = terms[0].trim();
        
        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && !terms[0].startsWith("\"Chapter"))
        {
            indTerms = new ArrayList<IndTerm>();

            subChapter = terms[1].replaceAll( "\\t+", "" );

            if(terms.length > 4)
            {   // subchapter on same line as term so get rid of it && prepare rest
                line = "";
                for (int i = 2; i < terms.length; i++) 
                {
                    line = line +"\t" +terms[i];
                }
                terms = line.split("\\t+");
            }
            else
            {
                line = readLine();
                terms = line.split("\\t+");
                while(terms.length == 0)
                {   // blank lines
                    line = readLine();
                    terms = line.split("\\t+");
                }
            }
            
            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null && !terms[0].startsWith("\"Chapter") && !isAllUpper(terms[1]))  
            {
                term = terms[1].replaceAll("\"", "");
                term = term.trim();
                
                if(term.equalsIgnoreCase("floating gallbladder"))
                {
                    term = "\"floating\" gallbladder";
                }

                indTerm = termMap_.get(term);
                indTerm.setXlsCode(terms[3]);
                indTerm.setExactMatch(terms[2]);
                indTerms.add(indTerm);

                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                }
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    private String parseVol4Ch8(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;

        line = line.replaceAll("\"", "");
        chapter = line.trim();
        
        // name of chapter on next line with subchapter in the next column
        line = readLine();
        terms = line.split("\\t+");
        while(terms.length == 0)
        {   // blank lines
            line = readLine();
            terms = line.split("\\t+");
        }
        chapter = chapter +" " +terms[0];

        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && (!terms[0].startsWith("\"Chapter") && !terms[0].startsWith("Chapter")) )
        {
            indTerms = new ArrayList<IndTerm>();

            subChapter = terms[1].replaceAll( "\\t+", "" );

            if(terms.length > 4)
            {   // subchapter on same line as term so get rid of it && prepare rest
                line = "";
                for (int i = 2; i < terms.length; i++) 
                {
                    line = line +"\t" +terms[i];
                }
                terms = line.split("\\t+");
            }
            else
            {
                line = readLine();
                terms = line.split("\\t+");
                while(terms.length == 0)
                {   // blank lines
                    line = readLine();
                    terms = line.split("\\t+");
                }
            }

            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null 
                    && !terms[0].startsWith("\"Chapter") 
                    && (terms.length>1 && !isAllUpper(terms[1])) )  
            {
                term = terms[1].replaceAll("\"", "");
                term = term.trim();
                
                if(term.equalsIgnoreCase("floating gallbladder"))
                {
                    term = "\"floating\" gallbladder";
                }

                indTerm = termMap_.get(term);
                indTerm.setXlsCode(terms[3]);
                indTerm.setExactMatch(terms[2]);
                if(terms.length > 4)
                {
                    indTerm.setExactMatch2(terms[4]); 
                }
                if(terms.length > 5)
                {
                    indTerm.setXlsCode2(terms[5]);
                }
                
                indTerms.add(indTerm);

                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                }
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    private String parseVol4Ch9(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;

        line = line.replaceAll("\"", "");
        chapter = line.trim();
        
        // name of chapter on next line by itself
        line = readLine();
        line = line.replaceAll("\"", "");
        chapter = chapter +" " +line.trim();
        
        line = readLine();
        terms = line.split("\\t+");
        while(terms.length == 0)
        {   // blank lines
            line = readLine();
            terms = line.split("\\t+");
        }
        
        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && (!terms[0].startsWith("\"Chapter") && !terms[0].startsWith("Chapter")) )
        {
            indTerms = new ArrayList<IndTerm>();

            terms[1] = terms[1].replaceAll("\"", "");
            subChapter = terms[1].replaceAll( "\\t+", "" );

            if(terms.length > 4)
            {   // subchapter on same line as term so get rid of it && prepare rest
                line = "";
                for (int i = 2; i < terms.length; i++) 
                {
                    line = line +"\t" +terms[i];
                }
                terms = line.split("\\t+");
            }
            else
            {
                line = readLine();
                terms = line.split("\\t+");
                while(terms.length == 0)
                {   // blank lines
                    line = readLine();
                    terms = line.split("\\t+");
                }
            }

            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null 
                    && !terms[0].startsWith("\"Chapter") 
                    && (terms.length>1 && !isAllUpper(terms[1])) )  
            {
                term = terms[1].replaceAll("\"", "");
                term = term.trim();
                
                if(term.equalsIgnoreCase("floating gallbladder"))
                {
                    term = "\"floating\" gallbladder";
                }

                indTerm = termMap_.get(term);
                indTerm.setXlsCode(terms[3]);
                indTerm.setExactMatch(terms[2]);
                if(terms.length > 4)
                {
                    indTerm.setExactMatch2(terms[4]); 
                }
                if(terms.length > 5)
                {
                    indTerm.setXlsCode2(terms[5]);
                }
                
                indTerms.add(indTerm);

                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                }
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    private String parseVol4Ch10(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = null;
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
        String chapter = "";
        String subChapter = "";
        String[] terms;
        String term;

        line = line.replaceAll("\"", "");
        chapter = line.trim();
        
        // name of chapter on next line by itself
        line = readLine();
        line = line.replaceAll("\"", "");
        chapter = chapter +" " +line.trim();
        
        line = readLine();
        terms = line.split("\\t+");
        while(terms.length == 0)
        {   // blank lines
            line = readLine();
            terms = line.split("\\t+");
        }
        
        if(terms[1].equalsIgnoreCase(""))
        {
            terms = null;
        }
        
        // get all the subchapters within the family && put them into the chapter's map
        while(line!=null && (!terms[0].startsWith("\"Chapter") && !terms[0].startsWith("Chapter")) )
        {
            indTerms = new ArrayList<IndTerm>();

            terms[1] = terms[1].replaceAll("\"", "");
            subChapter = terms[1].replaceAll( "\\t+", "" );
            
            if(terms[1].equalsIgnoreCase(""))
            {
                terms = null;
            }

            if(terms.length > 4)
            {   // subchapter on same line as term so get rid of it && prepare rest
                line = "";
                for (int i = 2; i < terms.length; i++) 
                {
                    line = line +"\t" +terms[i];
                }
                terms = line.split("\\t+");
            }
            else
            {
                line = readLine();
                terms = line.split("\\t+");
                while(terms.length == 0 || (terms.length >= 2 && terms[1].equalsIgnoreCase(" ")))
                {   // blank lines
                    line = readLine();
                    terms = line.split("\\t+");
                }
            }

            // get all the terms for the subchapter && put them into the subchapter's map
            // terms have first columns blank so stop reading once find something in term[0]
            while(line!=null && !isVol4Chap10SubChapter(terms[1]) )  
            {
                term = terms[1].replaceAll("\"", "");
                term = term.trim();
 
                if(term.contains("pseudosarcoma of the oesophagus"))
                {
                    term =  "\"pseudosarcoma\" of the oesophagus";
                }

                indTerm = termMap_.get(term);
                indTerm.setXlsCode(terms[3].replaceAll("\"", ""));
                indTerm.setExactMatch(terms[2].replaceAll("\"", ""));
                
                
                if(term.contains("squamous cell papilloma of the oesophagus")
                        || term.contains("adenoma of the oesophagus")
                        || term.contains("squamous cell carcinoma of the oesophagus")
                        || term.contains("leiomyoma of the oesophagus")
                        || term.contains("granular cell tumour of the oesophagus")
                        || term.contains("leiomyosarcoma of the oesophagus"))
                {   // these cells are wrapped so are read as two lines rather than one
                    line = readLine();
                    terms = line.split("\\t+");
                    indTerm.setExactMatch2(terms[1]); 
                    indTerm.setXlsCode2(terms[2]); 
                }
                
                if(terms.length > 4)
                {
                    indTerm.setExactMatch2(terms[4].replaceAll("\"", "")); 
                }
                if(terms.length > 5)
                {
                    indTerm.setXlsCode2(terms[5].replaceAll("\"", ""));
                }
                
                indTerms.add(indTerm);

                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                    while(terms.length == 0 || (terms.length >= 2 && terms[1].equalsIgnoreCase(" ")))
                    {   // blank lines
                        line = readLine();
                        terms = line.split("\\t+");
                    }
                }
            }
            subChapMap.put(subChapter, indTerms);  
        }
        chapMap.put(chapter, subChapMap);
    
        skipMainRead = true;
        return line;
    }
    
    
    private String parseVol5(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = new ArrayList<IndTerm>();
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap;
        String chapter = "";
        String subChapter = " ";
        String[] terms = null;
        String term = "";
        subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();

        while(!line.startsWith("Chapter"))
        {
            return line;
        }
        
        if(line.contains("•"))
        {   // remove comments
            line = line.substring(0, line.indexOf("•"));
        }
        chapter = line.trim();
        
        if(chapter.contains("Chapter 27"))
        {
            while(line!=null)
            {
                line = parseVol6Ch27(line);
            }
        }
        if(line!=null)
        {
            terms = line.split("\\t+");
            if(terms.length==1 
                    || (terms.length==2 && terms[1].equalsIgnoreCase(" "))
                    || (terms.length==2 && terms[1].contains("•")))
            {   // chapter number on different line than chapter name
                line = readLine();
                terms = line.split("\\t+");
                if(terms.length>1)
                {   // name of chapter on same line as terms
                    if(terms[0].equalsIgnoreCase(""))
                    {
                        chapter = chapter +" - " +terms[1].replaceAll("\\t+", ""); 
                        line = readLine();
                    }
                    else
                    {
                        chapter = chapter +" - " +terms[0].replaceAll("\\t+", ""); 
                        line = "";
                        for (int i = 1; i < terms.length; i++) 
                        {   // put line back together without chapter name
                            line = line +"\t" +terms[i];
                        }
                    }
                }
                else
                {
                    chapter = chapter +" - " +line.replaceAll("\\t+", ""); 
                    line = readLine();
                }
            }
            else
            {
                chapter = terms[0].replaceAll("\\t+", "").trim() +" - " +terms[1].replaceAll("\\t+", "").trim();
                line = readLine();
            }
        }

        while( line!=null && !(line.startsWith("Chapter")))
        {
            indTerms = new ArrayList<IndTerm>();

            if(line.contains("•"))
            {   // remove comments
                line = line.substring(0, line.indexOf("•"));
            }
            if(line.contains("}"))
            {   // remove comments
                line = line.substring(0, line.indexOf("}"));
            }
            
            if(line.contains("E14.6"))
            {
                line = readLine();
                break;
            }

            
            terms = line.split("\\t+");

            if( isAllUpper(terms[0]) 
                    || (terms.length>1 && isAllUpper(terms[1]))  
                    || isVol6SubChapter(terms[1]))
            {   // some terms are missing subchapter name
                int locSubChap = 0;
                if(isAllUpper(terms[0]) 
                        || (isVol6SubChapter(line) && !terms[0].equalsIgnoreCase("")) ) 
                {
                    subChapter = terms[0].trim();
                }
                else if(terms.length>1 && (isAllUpper(terms[1]) || !terms[1].equalsIgnoreCase(""))) 
                {
                    subChapter = terms[1].trim();
                    locSubChap = 1;
                }
                
                if(terms.length > 2)
                {   // subchapter on same line as term so get rid of it && prepare rest
                    line = "";
                    for (int i = locSubChap+1; i < terms.length; i++) 
                    {
                        line = line +"\t" +terms[i];
                    }
                }
                else
                {   //subchapter was on its own line
                    line = readLine();
                }
                if(line!=null)
                {
                    terms = line.split("\\t+");
                    while(terms.length == 0 )
 //                           || (terms.length >= 2 && terms[1].equalsIgnoreCase(" ")))
                    {   // blank lines
                        line = readLine();
                        terms = line.split("\\t+");
                    }
                }
            }
            else if(line.contains("PROTEIN- AND ENERGY-DEFICIENT STATES"))
            {   // chapter 20 in 8250 only has a subchapter && even that has no terms
                subChapter = terms[0].trim();
                line = readLine();
            }
            
            while(line!=null 
                    && !(line.startsWith("Chapter")) 
                    && (terms.length!=1 && !isAllUpper(terms[0])) 
                    && ( (terms.length!=1 && !isAllUpper(terms[1])) 
                    && !isVol6SubChapter(line)))
            {
                if( terms[0].trim().equalsIgnoreCase("") && terms[1].trim().equalsIgnoreCase("") )
                {
                    // blank line
                }
                else if(isAllUpper(terms[0]))    
                {   // subchapter is on same line as concept
                    subChapMap.put(subChapter, indTerms);
                    indTerms = new ArrayList<IndTerm>();
                    subChapter = terms[0];
                    term = terms[1].trim();
                    term = term.replaceAll("\"", "");
                    indTerm = termMap_.get(term);
                    indTerm.setExactMatch(terms[2]);
                    indTerm.setXlsCode(terms[3]);
                    indTerms.add(indTerm);
                     
                }
                else
                {   // concept on line by itself
                    term = terms[1];
                    term = term.replaceAll("%", "");
                    term = term.replaceAll("\"", "");
                    term = term.trim();
                    
                    if(term.contains("acanthosis nigricans hirsutism"))
                    {   // doc has the comma, spreadsheet left out
                        term = term.replace("acanthosis nigricans hirsutism", "acanthosis nigricans, hirsutism");
                    }
                    
                    if(term.equalsIgnoreCase("acromegaly"))
                    {
                        System.out.println(term);
                    }
                    if(term.equalsIgnoreCase("Tay--Sachs disease variant"))
                    {   // spreadsheet left off AB
                        term = "Tay--Sachs disease variant AB";
                    }
                    if(term.equalsIgnoreCase("nonfamilial hyperinsulinaemic isolated somatotropin"))
                    {   // spreadsheet left off deficiency
                        term = "nonfamilial hyperinsulinaemic isolated somatotropin deficiency";
                    }
                    if(term.equalsIgnoreCase("pituitary dwarfism with normal somatotropin level and low somatomedin"))
                    {   // not defined in document 8250
                        indTerm = new IndTerm();
                        indTerm.setName("pituitary dwarfism with normal somatotropin level and low somatomedin");
                    }
                    else
                    {
                        indTerm = termMap_.get(term);
                    }
                    if(terms.length>=3)
                    {   // some terms don't have exact match or xls code info
                        if(terms[2].equalsIgnoreCase("Yes") || terms[2].equalsIgnoreCase("No"))
                        {
                            indTerm.setExactMatch(terms[2]);
                            indTerm.setXlsCode(terms[3]);
                        }
                        else
                        {   // some don't have exact match info
                            indTerm.setXlsCode(terms[2]);
                        }
                    }
                    if(terms.length>=5 
                            && (terms[4].equalsIgnoreCase("Yes") || terms[4].equalsIgnoreCase("No")))
                    {   // some use those columns for internal reference notes
                        indTerm.setExactMatch2(terms[4]);
                    }
                    if(terms.length>=5)
                    {   // some use those columns for internal reference notes
                        indTerm.setXlsCode2(terms[3]);
                    }
                    if(terms.length>=6)
                    {
                        indTerm.setXlsCode2(terms[5]);
                    }
                    indTerms.add(indTerm);

                }
                
                line = readLine();
                
                
                if(line!=null)
                {
                    int loopPrevention = 0; 
                    terms = line.split("\\t+");
                            // last lines in last spreadsheet has tabs rather than null
                    while(terms.length == 0 && loopPrevention < 5)
 //                           || (terms.length >= 2 && terms[1].equalsIgnoreCase(" ")))
                    {   // blank lines
                        line = readLine();
                        terms = line.split("\\t+");
                        loopPrevention++;
                    }
                    if(loopPrevention==5)
                    {
                        line = null;
                    }
                }
                if(line!=null && line.contains("}"))
                {   // remove comments
                    line = line.substring(0, line.indexOf("}"));
                    terms = line.split("\\t+");
                }

            }
            subChapMap.put(subChapter, indTerms);
            chapMap.put(chapter, subChapMap);
        } 

        if(line!= null && (line.contains("Chapter")))
        {
            skipMainRead = true;
        }
        
        return line;
    }
    
    
    private String parseVol6(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = new ArrayList<IndTerm>();
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap;
        String chapter = "";
        String subChapter = " ";
        String[] terms = null;
        String term = "";
        subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();

        while(!line.startsWith("Chapter"))
        {
            return line;
        }
        
        if(line.contains("•"))
        {   // remove comments
            line = line.substring(0, line.indexOf("•"));
        }
        chapter = line.trim();
        
        if(chapter.contains("Chapter 27"))
        {
            while(line!=null)
            {
                line = parseVol6Ch27(line);
            }
        }
        if(line!=null)
        {
            terms = line.split("\\t+");
            if(terms.length==1 
                    || (terms.length==2 && terms[1].equalsIgnoreCase(" "))
                    || (terms.length==2 && terms[1].contains("•")))
            {   // chapter number on different line than chapter name
                line = readLine();
                terms = line.split("\\t+");
                if(terms.length>1)
                {   // name of chapter on same line as terms
                    if(terms[0].equalsIgnoreCase(""))
                    {
                        chapter = chapter +" - " +terms[1].replaceAll("\\t+", ""); 
                        line = readLine();
                    }
                    else
                    {
                        chapter = chapter +" - " +terms[0].replaceAll("\\t+", ""); 
                        line = "";
                        for (int i = 1; i < terms.length; i++) 
                        {   // put line back together without chapter name
                            line = line +"\t" +terms[i];
                        }
                    }
                }
                else
                {
                    chapter = chapter +" - " +line.replaceAll("\\t+", ""); 
                    line = readLine();
                }
            }
            else
            {
                chapter = terms[0].replaceAll("\\t+", "").trim() +" - " +terms[1].replaceAll("\\t+", "").trim();
                line = readLine();
            }
        }

        while( line!=null && !(line.startsWith("Chapter")))
        {
            indTerms = new ArrayList<IndTerm>();

            if(line.contains("•"))
            {   // remove comments
                line = line.substring(0, line.indexOf("•"));
            }
            if(line.contains("}"))
            {   // remove comments
                line = line.substring(0, line.indexOf("}"));
            }
            
            if(line.contains("E14.6"))
            {
                line = readLine();
                break;
            }

            
            terms = line.split("\\t+");

            if( isAllUpper(terms[0]) 
                    || (terms.length>1 && isAllUpper(terms[1]))  
                    || isVol6SubChapter(terms[1]))
            {   // some terms are missing subchapter name
                int locSubChap = 0;
                if(isAllUpper(terms[0]) 
                        || (isVol6SubChapter(line) && !terms[0].equalsIgnoreCase("")) ) 
                {
                    subChapter = terms[0].trim();
                }
                else if(terms.length>1 && (isAllUpper(terms[1]) || !terms[1].equalsIgnoreCase(""))) 
                {
                    subChapter = terms[1].trim();
                    locSubChap = 1;
                }
                
                if(terms.length > 2)
                {   // subchapter on same line as term so get rid of it && prepare rest
                    line = "";
                    for (int i = locSubChap+1; i < terms.length; i++) 
                    {
                        line = line +"\t" +terms[i];
                    }
                }
                else
                {   //subchapter was on its own line
                    line = readLine();
                }
                if(line!=null)
                {
                    terms = line.split("\\t+");
                    while(terms.length == 0 )
 //                           || (terms.length >= 2 && terms[1].equalsIgnoreCase(" ")))
                    {   // blank lines
                        line = readLine();
                        terms = line.split("\\t+");
                    }
                }
            }
            else if(line.contains("PROTEIN- AND ENERGY-DEFICIENT STATES"))
            {   // chapter 20 in 8250 only has a subchapter && even that has no terms
                subChapter = terms[0].trim();
                line = readLine();
            }
            
            while(line!=null 
                    && !(line.startsWith("Chapter")) 
                    && (terms.length!=1 && !isAllUpper(terms[0])) 
                    && ( (terms.length!=1 && !isAllUpper(terms[1])) 
                    && !isVol6SubChapter(line)))
            {
                if( terms[0].trim().equalsIgnoreCase("") && terms[1].trim().equalsIgnoreCase("") )
                {
                    // blank line
                }
                else if(isAllUpper(terms[0]))    
                {   // subchapter is on same line as concept
                    subChapMap.put(subChapter, indTerms);
                    indTerms = new ArrayList<IndTerm>();
                    subChapter = terms[0];
                    term = terms[1].trim();
                    term = term.replaceAll("\"", "");
                    indTerm = termMap_.get(term);
                    indTerm.setExactMatch(terms[2]);
                    indTerm.setXlsCode(terms[3]);
                    indTerms.add(indTerm);
                     
                }
                else
                {   // concept on line by itself
                    term = terms[1];
                    term = term.replaceAll("%", "");
                    term = term.replaceAll("\"", "");
                    term = term.trim();
                    
                    if(term.contains("acanthosis nigricans hirsutism"))
                    {   // doc has the comma, spreadsheet left out
                        term = term.replace("acanthosis nigricans hirsutism", "acanthosis nigricans, hirsutism");
                    }
                    
                    if(term.equalsIgnoreCase("acromegaly"))
                    {
                        System.out.println(term);
                    }
                    if(term.equalsIgnoreCase("Tay--Sachs disease variant"))
                    {   // spreadsheet left off AB
                        term = "Tay--Sachs disease variant AB";
                    }
                    if(term.equalsIgnoreCase("nonfamilial hyperinsulinaemic isolated somatotropin"))
                    {   // spreadsheet left off deficiency
                        term = "nonfamilial hyperinsulinaemic isolated somatotropin deficiency";
                    }
                    if(term.equalsIgnoreCase("pituitary dwarfism with normal somatotropin level and low somatomedin"))
                    {   // not defined in document 8250
                        indTerm = new IndTerm();
                        indTerm.setName("pituitary dwarfism with normal somatotropin level and low somatomedin");
                    }
                    else
                    {
                        indTerm = termMap_.get(term);
                    }
                    if(terms.length>=3)
                    {   // some terms don't have exact match or xls code info
                        if(terms[2].equalsIgnoreCase("Yes") || terms[2].equalsIgnoreCase("No"))
                        {
                            indTerm.setExactMatch(terms[2]);
                            indTerm.setXlsCode(terms[3]);
                        }
                        else
                        {   // some don't have exact match info
                            indTerm.setXlsCode(terms[2]);
                        }
                    }
                    if(terms.length>=5 
                            && (terms[4].equalsIgnoreCase("Yes") || terms[4].equalsIgnoreCase("No")))
                    {   // some use those columns for internal reference notes
                        indTerm.setExactMatch2(terms[4]);
                    }
                    if(terms.length>=5)
                    {   // some use those columns for internal reference notes
                        indTerm.setXlsCode2(terms[3]);
                    }
                    if(terms.length>=6)
                    {
                        indTerm.setXlsCode2(terms[5]);
                    }
                    indTerms.add(indTerm);

                }
                
                line = readLine();
                
                
                if(line!=null)
                {
                    int loopPrevention = 0; 
                    terms = line.split("\\t+");
                            // last lines in last spreadsheet has tabs rather than null
                    while(terms.length == 0 && loopPrevention < 5)
 //                           || (terms.length >= 2 && terms[1].equalsIgnoreCase(" ")))
                    {   // blank lines
                        line = readLine();
                        terms = line.split("\\t+");
                        loopPrevention++;
                    }
                    if(loopPrevention==5)
                    {
                        line = null;
                    }
                }
                if(line!=null && line.contains("}"))
                {   // remove comments
                    line = line.substring(0, line.indexOf("}"));
                    terms = line.split("\\t+");
                }

            }
            subChapMap.put(subChapter, indTerms);
            chapMap.put(chapter, subChapMap);
        } 

        if(line!= null && (line.contains("Chapter")))
        {
            skipMainRead = true;
        }
        
        return line;
    }
    
    private String parseVol6Ch27(String line) throws IOException
    {
        IndTerm indTerm = new IndTerm();
        ArrayList<IndTerm> indTerms = new ArrayList<IndTerm>();
        LinkedHashMap<String, ArrayList<IndTerm>> subChapMap;
        String chapter = "";
        String subChapter = " ";
        String[] terms = null;
        String term = "";
        subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();

        if(line.contains("•"))
        {   // remove comments
            line = line.substring(0, line.indexOf("•"));
        }
        chapter = line.trim();

        if(chapter.equalsIgnoreCase("Chapter 27"))
        {
            line = readLine();
            if(line.contains("•"))
            {   // remove comments
                line = line.substring(0, line.indexOf("•"));
            }
            chapter = chapter +" - " +line.replaceAll(("\\t+"), "");
        }

        line = readLine();
        
        if(line!=null)
        {
            terms = line.split("\\t+");
            while(line!=null 
                    && ((terms.length == 0) || line.replaceAll("\\t+", "").equalsIgnoreCase(" ")) )
            {   // blank lines
                line = readLine();
                if(line!=null)
                {
                    terms = line.split("\\t+");
                }
            }
        }
        
        while( line!=null && !isAllUpper(line))
        {
            indTerms = new ArrayList<IndTerm>();

            if(line.contains("carcinoid tumour") || line.contains("multiple endocrine neoplasia"))
            {
                subChapter = "";
            }
            else
            {
                if(line.contains("•"))
                {   // remove comments
                    line = line.substring(0, line.indexOf("•"));
                }
                subChapter = line.replaceAll(("\\t+"), "");
                
                line = readLine();
                if(line.contains("•"))
                {   // remove comments
                    line = line.substring(0, line.indexOf("•"));
                }
            if(line.contains(".5 With peripheral circulatory complications"))
            {   // remove comments
                line = line.substring(0, line.indexOf("."));
            }
            }
            
            while(line!=null && !isVol6SubChapter(line) && !isAllUpper(line))
            {
                terms = line.split("\\t+");
                if( terms[0].trim().equalsIgnoreCase("") && terms[1].trim().equalsIgnoreCase("") )
                {
                    // blank line
                }
                else
                {   // concept on line by itself
                    term = terms[1];
                    term = term.replaceAll("%", "");
                    term = term.replaceAll("\"", "");
                    term = term.trim();
                    
                    indTerm = termMap_.get(term);

                    if(terms.length>=3)
                    {   // some terms don't have exact match or xls code info
                        if(terms[2].equalsIgnoreCase("Yes") || terms[2].equalsIgnoreCase("No"))
                        {
                            indTerm.setExactMatch(terms[2]);
                            indTerm.setXlsCode(terms[3]);
                        }
                        else
                        {   // some don't have exact match info
                            indTerm.setXlsCode(terms[2]);
                        }
                    }
                    if(terms.length>=5 
                            && (terms[4].equalsIgnoreCase("Yes") || terms[4].equalsIgnoreCase("No")))
                    {   // some use those columns for internal reference notes
                        indTerm.setExactMatch2(terms[4]);
                    }
                    if(terms.length>=5)
                    {   // some use those columns for internal reference notes
                        indTerm.setXlsCode2(terms[3]);
                    }
                    if(terms.length>=6)
                    {
                        indTerm.setXlsCode2(terms[5]);
                    }
                    indTerms.add(indTerm);

                }
                
                line = readLine();

                
                if(line!=null)
                {
                    terms = line.split("\\t+");
                    while(line!=null 
                            && ((terms.length == 0) || line.replaceAll("\\t+", "").equalsIgnoreCase(" ")) )
                    {   // blank lines
                        line = readLine();
                        if(line!=null)
                        {
                            terms = line.split("\\t+");
                        }
                    }
                }
            }
            subChapMap.put(subChapter, indTerms);
            chapMap.put(chapter, subChapMap);
        } 
        return line;
    }
    
    
  private String parseVol8(String line) throws IOException
  {
      IndTerm indTerm = new IndTerm();
      ArrayList<IndTerm> indTerms = new ArrayList<IndTerm>();
      LinkedHashMap<String, ArrayList<IndTerm>> subChapMap;
      String chapter = "";
      String subChapter = "";
      String[] terms;
      String term = "";
      
      subChapMap = new LinkedHashMap<String, ArrayList<IndTerm>>();
      
      chapter = line.trim();

      if(line.length()==4)
      {
          line = readLine();
      }
      if(!line.contains("DISEASES OF THE"))
      {   // "DISEASES OF THE" chapters do not have numbers
          line = readLine();
          line = line.replaceAll( "\\t+", "" );
          while(line!=null && line.equalsIgnoreCase(""))
          {
              line = readLine();
          }
          chapter = chapter.concat(" - " +line);
          chapter = chapter.replaceAll( "\\t+", "" );
      }
      
      line = readLine();
      
      while(line!=null && !(line.startsWith("Chapter") || line.startsWith("DISEASES OF THE ")))
      {
          // the first part of this chapter has terms but no subchapter
          if(chapter.contains("DISEASES OF THE CORPUS UTERI") && line.contains("senile endometritis"))
          {
              subChapter = "";
          }
          else
          {
              subChapter = line.replaceAll( "\\t+", "" );
              line = readLine();
          }
          
          while(line!=null && !(line.startsWith("Chapter") || line.startsWith("DISEASES OF THE ")))
          {
              terms = line.split("\\t+");
              
              if(terms.length==1 || (terms.length==2 && !terms[0].trim().equalsIgnoreCase("") && terms[1].trim().equalsIgnoreCase("")) )
              {   // no Chapter number given - some have one column, some have two.
                  // two column chapter lines have to be destinguished from blank two column lines && subchapters
                  chapter = line;
                  line = readLine();
                  terms = line.split("\\t+");
              }
              else if( terms[0].trim().equalsIgnoreCase("") && terms[1].trim().equalsIgnoreCase("") )
              {
                  // blank line
              }
              else if(terms.length < 3 || terms[2].trim().equalsIgnoreCase(""))
              {   // subchapter is on one line
                  subChapMap.put(subChapter, indTerms);
                  indTerms = new ArrayList<IndTerm>();
                  subChapter = terms[1];
              }
              else if(terms[3].equalsIgnoreCase("No") || terms[3].equalsIgnoreCase("Yes"))    
              {   // subchapter is on same line as concept
                  subChapMap.put(subChapter, indTerms);
                  indTerms = new ArrayList<IndTerm>();
                  subChapter = terms[1];
                  term = terms[2].trim();
                  term = term.replaceAll("\"", "");
                  indTerm = termMap_.get(term);
                  indTerm.setExactMatch(terms[3]);
                  indTerm.setXlsCode(terms[4]);
                if(terms.length>=6)
                {
                    if(terms[5].equalsIgnoreCase("No") || terms[5].equalsIgnoreCase("Yes")) 
                    {
                        indTerm.setExactMatch2(terms[5]);
                    }
                    else
                    {
                        indTerm.setXlsCode2(terms[5]);
                    }
                }
                if(terms.length>=7)
                {
                    indTerm.setXlsCode2(terms[6]);
                }
                  indTerms.add(indTerm);
                   
              }
              else
              {   // concept on line by itself
                  term = terms[1];
                  term = term.replaceFirst("\"", "");
                  term = term.replaceFirst("\"", "");
                  term = term.trim();
                  indTerm = termMap_.get(term);
                  indTerm.setExactMatch(terms[2]);
                  indTerm.setXlsCode(terms[3]);
                if(terms.length>=5)
                {
                    if(terms[4].equalsIgnoreCase("No") || terms[4].equalsIgnoreCase("Yes")) 
                    {
                        indTerm.setExactMatch2(terms[4]);
                    }
                    else
                    {
                        indTerm.setXlsCode2(terms[4]);
                    }
                }
                if(terms.length>=6)
                {
                    indTerm.setXlsCode2(terms[5]);
                }
                  indTerms.add(indTerm); 
              }
              line = readLine();
              
              if(line!=null && line.contains("Soft-Tissue Tumours not Specific to the Ovary"))
              {   // double lined subchapter - keep second line
                  line = readLine();
              }
          }
          subChapMap.put(subChapter, indTerms);
          chapMap.put(chapter, subChapMap);
      } 

      if(line!= null && (line.contains("Chapter") || line.contains("DISEASES OF THE") ))
      {
          skipMainRead = true;
      }
      
      return line;
  }
    
    
    
  private boolean isVol4Chap10SubChapter(String line)
  {
      boolean isSub = false;

      if(line.contains("Benign")
              || line.contains("Malignant")
              || line.contains("Miscellaneous Tumours of the")
              || line.contains("Tumour-like Lesions of the")
              || line.contains("Other Non-epithelial Tumours of the")
              || line.contains("Lymphoid Neoplasms of the Stomach")
              || line.contains("Miscellaneous Tumours of")
              || line.contains("Carcinoid Tumours of the Appendix")
              || line.contains("Non-epithelial Tumours of")
              || line.contains("Epithelial Abnormalities of")
              || line.contains("Tumours of Pancreatic Islets"))
      {
          isSub = true;
      }
      
      return isSub;
  }
  
  private boolean isVol6SubChapter(String line)
  {
      boolean isSub = false;

      if(line.contains("Androgen Receptor Disorders")
              || line.contains("Somatotropin Excess")
              || line.contains("Benign Glandular Epithelial Tumours")
              || line.contains("Benign Epithelial Tumours")
              || line.contains("Malignant Epithelial Tumours")
              || line.contains("Benign Mesenchymal Tumours and Tumour-like Lesions")
              || line.contains("Malignant Mesenchymal Tumours of the Adrenal Cortex")
              || line.contains("Benign Paragangliomas")
              || line.contains("Malignant Paragangliomas")
              || line.contains("Benign Neural Tumours")
              || line.contains("Malignant Neural Tumours")
              || line.contains("Miscellaneous Tumours")
              || line.contains("Tumour-like Lesions")
              || line.contains("Miscellaneous Tumours")
              || line.contains("Nonepithelial Tumours")
              || line.contains("Islet Cell Tumours")
              || line.contains("Diffuse Endocrine System Tumours"))
      {
          isSub = true;
      }
      
      return isSub;
  }

    
    private static boolean isAllUpper(String s) {
        if(s.equalsIgnoreCase(""))
        {
            return false;
        }
        for(char c : s.toCharArray()) {
           if(Character.isLetter(c) && Character.isLowerCase(c)) {
               return false;
            }
        }
        return true;
    }

    private String readLine() throws IOException
    {
        String line = buffRead_.readLine();
        while(line!=null && line.length()==0)
        {
            line = buffRead_.readLine();
        }

        return line;
    }

}

