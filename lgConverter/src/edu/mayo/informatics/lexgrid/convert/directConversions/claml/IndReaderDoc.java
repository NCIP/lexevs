package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

public class IndReaderDoc {

    private LinkedHashMap<String, IndTerm> termMap_;
    private BufferedReader buffRead_ = null;
    
    private boolean isDoc14 = false;        // doc14 codes start 1 off of where doc13 codes start
    private boolean doc14Hack = false;      // doc14 annex section leaves out codes
    private boolean doc7680Hack = false;    // where the doc starts to leave out $$ in front of codes
    
    private boolean skipMainRead = false;   // Vol 2 terms shouldn't be read twice
    
    
    public IndReaderDoc() throws LgConvertException, FileNotFoundException {
    }
    
    public LinkedHashMap<String, IndTerm>  getTermMap(LinkedHashMap<String, IndTerm> termMap, String docFilePath) throws LgConvertException, FileNotFoundException 
    {
        termMap_ = termMap;
        
        File file = new File(docFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        
        if(docFilePath.contains("8014"))
        {
            isDoc14 = true;
        }
            
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
                  if(line.length()>10)
                  {
                      skipMainRead = false;
                      line = parseVol2(line);
                  }
              }
              else if(docFilePath.contains("7676H") 
                      ||docFilePath.contains("7678H")
                      ||docFilePath.contains("7679H")
                      ||docFilePath.contains("7680H"))
              {
                  skipMainRead = false;
                  line = parseVol4(line);
              }
              else if(docFilePath.contains("7739"))
              {
                  skipMainRead = false;
                  line = parseVol5(line);
              }
              else if(docFilePath.contains("8138")
                      ||docFilePath.contains("8185")
                      ||docFilePath.contains("8196")
                      ||docFilePath.contains("8410")
                      ||docFilePath.contains("8250")
                      ||docFilePath.contains("8619")
                      ||docFilePath.contains("8842"))
              {
                  skipMainRead = false;
                  line = parseVol6(line);
              }
              else if(docFilePath.contains("8013")
                      ||docFilePath.contains("8014"))
              {
                  skipMainRead = false;
                  line = parseVol8(line);
              }
              
              if(line!=null && !skipMainRead &&
                      ((!doc14Hack && !line.startsWith("$$"))
                     || (doc14Hack && !line.startsWith("?"))
                     || (doc7680Hack && line.startsWith("$$")) ) )
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

           return termMap_;
	  }
    
    
    private String parseVol2(String line) throws IOException
    {
        if(line.length()>8 && line.substring(4,7).equals("   ") )
        {
            IndTerm term = new IndTerm();
            
            line = line.replaceAll("\"", "");
            
            String[] codeTerms = line.split("   ");
            term.setDocCode(codeTerms[0]);
            term.setName(codeTerms[1]);

            line = readLine();
            
            while(!line.startsWith("*")                     // seperates families
                    && !line.startsWith("-")                // page numbers
                    && !line.substring(4,7).equals("   "))  // new term
            {
                String tempLine = "";

                if( line.startsWith("Synonym") )
                {
                    line = line.replace("Synonyms:", "");
                    line = line.replace("Synonym:", "");
                    line = line.trim();
                    
                    term.setSynonym(line);
                }
                else if( line.startsWith("Note.") )
                {
                    line = line.replace("Note.", "");
                    line = line.trim();
                    term.setNote(line);
                }
                else if( line.startsWith("Notes.") )
                {   // if there are notes, there is not a note
                    // there are at most 4 notes
                    line = line.replace("Notes.", "");
                    line = line.trim();
                    tempLine = readLine();
                    while(tempLine.startsWith("1")||tempLine.startsWith("2")||tempLine.startsWith("3")||tempLine.startsWith("4"))
                    {
                        line = line +"   " +tempLine;
                        tempLine = readLine();
                    }
                    term.setNote(line);
                }
                else if( line.startsWith("Linguistic note.") )
                {
                    line = line.replace("Linguistic note.", "");
                    line = line.trim();
                    term.setLinguisticNote(line);
                }
                else 
                {
                    term.setDefinition(line);
                }
                
                if(tempLine.equalsIgnoreCase(""))
                {
                    line = readLine();
                }
                else
                {
                    line = tempLine;
                }
            }
            
            termMap_.put(term.getName().trim(), term);
            
            if(!line.startsWith("*")                        // seperates families
                    && !line.startsWith("-")                // page numbers
                    && line.substring(4,7).equals("   "))   // new term
            {
                skipMainRead = true;
            }
        }  
        return line;
    }
    
    private String parseVol4(String line) throws IOException
    {
        IndTerm term = new IndTerm();

        // in the Annex of doc 14, the concept codes are no longer listed
        if(line.startsWith("Annex 1"))
        {
            doc14Hack = true;
        }
        
        if( (!doc7680Hack && line.startsWith("$$")) 
                || (doc14Hack && line.startsWith("?")) 
                || (doc7680Hack && (line.startsWith("3") ||line.startsWith("4")||line.startsWith("5")||line.startsWith("6")) ) 
                || line.contains("Familial intrahepatic cholestasis syndromes") )
        {
            // some term names continue to the next line
            if(!doc7680Hack && !line.contains("%%"))
            {
                String tempLine = line;
                line = tempLine +" " +readLine();
                line = line.replaceAll("\\t+", "");
            }

            if(doc14Hack && line.startsWith("?"))
            {   // doc14Hack is to compensate for missing concept code in doc14 Annex section
                term.setName(line.substring(1, line.indexOf("%%")-1 )); 
            }
            if(doc7680Hack )
            {   // doc7680Hack is to compensate for missing $$ in front of concept code
                term.setName(line.substring(7, line.length() ));
                term.setDocCode(line.substring(0, 4));
            }
            else if(isDoc14)
            {   // the offset positions for code && name are different in doc13 & doc 14
                term.setName(line.substring(9, line.indexOf("%%") ));
                term.setDocCode(line.substring(2, 8));
            }
            else if(line.contains("congenital junctional epidermolysis bullosa"))
            {   // space missing between code && term
                term.setName(line.substring(8, line.indexOf("%%") ));
                term.setDocCode(line.substring(2, 8));
            }
            else if(line.contains("Familial intrahepatic cholestasis syndromes"))
            {   // listed as subchapter in doc but term in spreadsheet
                term.setName(line.substring(2, line.indexOf("&&") ));
            }
            else if(line.contains("squamous cell papilloma of the oesophagus"))
            {   // doc has two terms on same line - neither have defs, notes or synonyms
                term.setName("squamous cell papilloma of the oesophagus");
                term.setDocCode("3590");
                termMap_.put(term.getName().trim(), term);
                term = new IndTerm();
                term.setName("adenoma of the oesophagus");
                term.setDocCode("3600");
                termMap_.put(term.getName().trim(), term);
                line = "who wrote these docs?";
                
                // after these two terms, the subchapters start with $$ but terms do not
                doc7680Hack = true;
                
                return line;
            }
            else
            {
                term.setName(line.substring(9, line.indexOf("%%") ));
                term.setDocCode(line.substring(2, 7));
            }
            
            line = readLine();
            
            if( (!doc7680Hack && line.startsWith("$$")) || (doc14Hack && line.startsWith("?")) )
            {   // no definitions, synonyms or notes
                termMap_.put(term.getName().trim(), term);
                skipMainRead = true;
                return line;
            }
            
            if(line!=null 
                    && !line.contains("&&") 
                    && !line.contains("WANG 8014H") 
                    && !line.startsWith("?")
                    && !doc7680Hack)  // doc 7680 doesn't have definitions
            {   // has a defintion 
                term.setDefinition(line);
                line = readLine();
            }
            
            if(line!=null && line.contains("++Synonym"))
            {
                term.setSynonym(line.substring(line.indexOf("&&:")+5));
                line = readLine();
            }

            if(line!=null && line.contains("++Note"))
            {
                term.setNote(line.substring(line.indexOf("&&.")+5));
                line = readLine();
            }
            
            if(line!=null && line.contains("Familial intrahepatic cholestasis syndromes"))
            {   // listed as subchapter in doc but is term in spreadsheet
                skipMainRead = true;
            }
            
            if(line!=null && line.contains("squamous cell carcinoma of the oesophagus"))
            {   // listed as subchapter in doc but is term in spreadsheet
                System.out.println(line);
            }

            termMap_.put(term.getName().trim(), term);
        } 

        if(line!=null && ( (!doc7680Hack && line.startsWith("$$")) 
                        || (doc7680Hack && !line.startsWith("$$")) ) )
        {
            skipMainRead = true;
        }
        
        
        if(line!=null && ( line.contains("In addition to the lesion listed below") 
                        || line.contains("In addition to the lesions listed below")
                        || line.contains("A benign condition that involves the liver") 
                        || line.contains("- # -") ) )
        {   // this one line in 7680 defies every other line in every other doc...
            skipMainRead = false;
        }
        
        return line;
    }
    
    private String parseVol5(String line) throws IOException
    {
        IndTerm term = new IndTerm();

        // in the Annex of doc 14, the concept codes are no longer listed
        if(line.startsWith("Annex 1"))
        {
            doc14Hack = true;
        }
        
        if(line.startsWith("$$") || (doc14Hack && line.startsWith("?")) )
        {
            // some term names continue to the next line
            if(!line.contains("%%"))
            {
                String tempLine = line;
                line = tempLine +" " +readLine();
                line = line.replaceAll("\\t+", "");
            }
            
            if(line.contains("adenosquamous carcinoma of the vulva"))
            {   // for some reason this term has an extra character in the concept code
                line = line.substring(1, line.length());
            }

            if(doc14Hack && line.startsWith("?"))
            {   // doc14Hack is to compensate for missing concept code in doc14 Annex section
                term.setName(line.substring(1, line.indexOf("%%")-1 )); 
            }
            else if(isDoc14)
            {   // the offset positions for code && name are different in doc13 & doc 14
                term.setName(line.substring(9, line.indexOf("%%") ));
                term.setDocCode(line.substring(2, 8));
            }
            else
            {
                term.setName(line.substring(10, line.indexOf("%%")-1 ));
                term.setDocCode(line.substring(3, 8));
            }
            
            line = readLine();

            if(line.startsWith("$$") || (doc14Hack && line.startsWith("?")) )
            {   // no definitions, synonyms or notes
                termMap_.put(term.getName().trim(), term);
                return line;
            }
            
            if(line!=null && 
                       (line.contains("BARTHOLIN GLAND") 
                        || line.contains("THE OVARY") 
                        || line.contains("THE CERVIX UTERI")
                        || line.contains("DISEASES OF THE CERVIX UTERI")) )
            {  
                termMap_.put(term.getName().trim(), term);
                return line;
            }

            if(line!=null 
                    && !line.contains("&&") 
                    && !line.contains("++") 
                    && !line.contains("?") 
                    && !line.startsWith("WANG 8014H"))
            {   // has a defintion 
                term.setDefinition(line);
                line = readLine();
            }

            if(line!=null && line.contains("++Synonym"))
            {
                term.setSynonym(line.substring(line.indexOf("&&:")+5));
                line = readLine();
            }

            if(line!=null && line.contains("++Notes"))
            {   // if there are notes, there is not a note
                // there are at most 4 notes
                String tempLine = line.substring(line.indexOf("&&.")+5);
                line = readLine();
                while(line.startsWith("2")||line.startsWith("3")||line.startsWith("4"))
                {
                    tempLine = tempLine +"   " +line;
                    line = readLine();
                }
                term.setNote(tempLine);
            }

            if(line!=null && line.contains("++Note"))
            {
                if(term.getName().contains("endometrial schistosomiasis"))
                {
                    term.setNote(line.substring(line.indexOf("&&.")+6));
                }
                else
                {
                    term.setNote(line.substring(line.indexOf("&&.")+5));
                }
                line = readLine();
            }
            
            if(line!=null && line.contains("++Linguistic note"))
            {
                term.setLinguisticNote(line.substring(line.indexOf("&&.")+5));
                line = readLine();
            }
            
            termMap_.put(term.getName().trim(), term);
        } 
        
        if(line!=null && line.startsWith("$$"))
        {
            skipMainRead = true;
        }
        
        return line;
    }
    
    private String parseVol6(String line) throws IOException
    {
        IndTerm term = new IndTerm();

        if(line.contains("THE ADRENAL CORTEX"))
        {
            System.out.println(line);
        }
        if( line.startsWith("$$") )
        {
            if(line.indexOf("%%")!=0 && line.indexOf("%%")<8 )
            {   // some concept codes have %% after them for some reason
                line = line.replaceFirst("%%", "");
            }
            if(line.indexOf("þ”þ")!=0 && line.indexOf("þ”þ")==9 )
            {   // some concept codes have %% after them for some reason
                line = line.replaceFirst("þ”þ-", "");
            }
            if(line.indexOf("þ”þ")!=0 && line.indexOf("þ”þ")>9 )
            {   // some concept codes have %% after them for some reason
                line = line.replaceFirst("þ”þ", "");
            }
            
            // replace characters found in document but not in spreadsheet
            if(line.contains("++N&&$$"))
            {
                line = line.replace("++N&&$$", "N");
            }
            if(line.contains("17þaþ-") || line.contains("5þaþ-"))
            {
                line = line.replace("þaþ", "");
            }
            if(line.contains("þaþ-"))
            {
                line = line.replace("þaþ-", "");
            }
            if(line.contains("%%++b&& $$"))
            {
                line = line.replace("%%++b&& $$", " b ");
            }
            if(line.contains("þWþ"))
            {
                line = line.replace("þWþ", "");
            }
            
            // some term names continue to the next line
            if(!line.contains("%%"))
            {
                String tempLine = line;
                line = tempLine +" " +readLine();
                line = line.replaceAll("\\t+", "");
            }
            
            if(line.contains("fructose-bisphosphate aldolase B deficiency"))
            {   // missing space between code && term name
                term.setName(line.substring(8, line.indexOf("%%")));
            }
            else if(line.contains("glutathione") && line.contains("transferase deficiency"))
            {   // doc has characters not found in the spreadsheet 
                // glutathione%% ++S&&$$-transferase deficiency%%
                term.setName("glutathione-transferase deficiency");
            }
            else if(line.contains("cystathionine þqþ-lyase deficiency"))
            {   // doc has characters not found in the spreadsheet 
                term.setName("cystathionine-lyase deficiency");
            }
            else if(line.contains("dihydrouracil dehydrogenase"))
            {   // doc has characters not found in the spreadsheet 
                term.setName("dihydrouracil dehydrogenase (NADP$$u+) deficiency");
            }
            else if( line.contains("cytochrome") && line.contains("oxidase deficiency") )
            {   // doc has characters not found in the spreadsheet 
                term.setName("cytochrome-++c&& $$oxidase deficiency");
            }
            else if(line.contains("N-acetylglucosaminidase deficiency"))
            {   // doc has characters not found in the spreadsheet 
                // þaþ-%%++N&&$$-acetylglucosaminidase deficiency
                term.setName("N-acetylglucosaminidase deficiency");
            } 
            else if(line.contains("hydroxyprogesterone aldolase deficiency"))
            {   // for some reason there is a space in spreadsheet after the 17
                term.setName("17 -hydroxyprogesterone aldolase deficiency");
            } 
            else
            {
                String tempName = line.substring(9, line.indexOf("%%"));
                tempName = tempName.replaceAll("\\t+", "");
                tempName = tempName.replaceAll("  ", " ");
                term.setName(tempName);
            }
            term.setDocCode(line.substring(2, 6));

            line = readLine();

            if( line.startsWith("$$") || isAllUpper(line))
            {   // no definitions, synonyms or notes
                termMap_.put(term.getName().trim(), term);
                return line;
            }

            if(line.contains("THE ADRENAL CORTEX"))
            {
                System.out.println(line);
            }
            if( line!=null && !line.startsWith("++"))
            {   // has a defintion 
                term.setDefinition(line);
                line = readLine();
            }

            if(line!=null && line.contains("++Synonym"))
            {
                term.setSynonym(line.substring(line.indexOf("&&:")+5));
                line = readLine();
            }

            if(line!=null && line.contains("++Notes"))
            {   // if there are notes, there is not a note
                // there are at most 4 notes
                String tempLine = line.substring(line.indexOf("&&.")+5);
                line = readLine();
                while(line.startsWith("2")||line.startsWith("3")||line.startsWith("4"))
                {
                    tempLine = tempLine +"   " +line;
                    line = readLine();
                }
                term.setNote(tempLine);
            }

            if(line!=null && line.contains("++Note"))
            {
                term.setNote(line.substring(line.indexOf("&&.")+5));
                line = readLine();
            }
            
            if(line!=null && line.contains("++Linguistic note"))
            {
                term.setLinguisticNote(line.substring(line.indexOf("&&.")+5));
                line = readLine();
            }
            
            termMap_.put(term.getName().trim(), term);
        } 
        
        if(line!=null && line.startsWith("$$"))
        {
            skipMainRead = true;
        }
        
        return line;
    }
    
    private String parseVol8(String line) throws IOException
    {
        IndTerm term = new IndTerm();

        // in the Annex of doc 14, the concept codes are no longer listed
        if(line.startsWith("Annex 1"))
        {
            doc14Hack = true;
        }
        
        if(line.startsWith("$$") || (doc14Hack && line.startsWith("?")) )
        {
            // some term names continue to the next line
            if(!line.contains("%%"))
            {
                String tempLine = line;
                line = tempLine +" " +readLine();
                line = line.replaceAll("\\t+", "");
            }
            
            if(line.contains("adenosquamous carcinoma of the vulva"))
            {   // for some reason this term has an extra character in the concept code
                line = line.substring(1, line.length());
            }

            if(doc14Hack && line.startsWith("?"))
            {   // doc14Hack is to compensate for missing concept code in doc14 Annex section
                term.setName(line.substring(1, line.indexOf("%%")-1 )); 
            }
            else if(isDoc14)
            {   // the offset positions for code && name are different in doc13 & doc 14
                term.setName(line.substring(9, line.indexOf("%%") ));
                term.setDocCode(line.substring(2, 8));
            }
            else
            {
                term.setName(line.substring(10, line.indexOf("%%")-1 ));
                term.setDocCode(line.substring(3, 8));
            }
            
            line = readLine();

            if(line.startsWith("$$") || (doc14Hack && line.startsWith("?")) )
            {   // no definitions, synonyms or notes
                termMap_.put(term.getName().trim(), term);
                return line;
            }
            
            if(line!=null && 
                       (line.contains("BARTHOLIN GLAND") 
                        || line.contains("THE OVARY") 
                        || line.contains("THE CERVIX UTERI")
                        || line.contains("DISEASES OF THE CERVIX UTERI")) )
            {  
                termMap_.put(term.getName().trim(), term);
                return line;
            }

            if(line!=null 
                    && !line.contains("&&") 
                    && !line.contains("++") 
                    && !line.contains("?") 
                    && !line.startsWith("WANG 8014H"))
            {   // has a defintion 
                term.setDefinition(line);
                line = readLine();
            }

            if(line!=null && line.contains("++Synonym"))
            {
                term.setSynonym(line.substring(line.indexOf("&&:")+5));
                line = readLine();
            }

            if(line!=null && line.contains("++Notes"))
            {   // if there are notes, there is not a note
                // there are at most 4 notes
                String tempLine = line.substring(line.indexOf("&&.")+5);
                line = readLine();
                while(line.startsWith("2")||line.startsWith("3")||line.startsWith("4"))
                {
                    tempLine = tempLine +"   " +line;
                    line = readLine();
                }
                term.setNote(tempLine);
            }

            if(line!=null && line.contains("++Note"))
            {
                if(term.getName().contains("endometrial schistosomiasis"))
                {
                    term.setNote(line.substring(line.indexOf("&&.")+6));
                }
                else
                {
                    term.setNote(line.substring(line.indexOf("&&.")+5));
                }
                line = readLine();
            }
            
            if(line!=null && line.contains("++Linguistic note"))
            {
                term.setLinguisticNote(line.substring(line.indexOf("&&.")+5));
                line = readLine();
            }
            
            termMap_.put(term.getName().trim(), term);
        } 
        
        if(line!=null && line.startsWith("$$"))
        {
            skipMainRead = true;
        }
        
        return line;
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
        if(line!=null)
        {
            line = line.trim();
        }
        return line;
    }

}


