
package org.lexevs.dao.indexer.utility;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * Small utility type routines used in the indexer.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class Utility {
    public static void removeSubFolders(File folder) {
        File[] temp = folder.listFiles();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].isDirectory()) {
                deleteRecursive(temp[i]);
            }
        }

    }

    public static void deleteRecursive(File file) {
        if (file.isDirectory()) {
            File[] temp = file.listFiles();
            for (int i = 0; i < temp.length; i++) {
                deleteRecursive(temp[i]);
            }
        }
        file.delete();
    }

    public static String replaceStringsInString(StringBuffer input, String searchFor, String replaceWith) {
        // int loc = input.indexOf(searchFor);//jdk 1.3 compliance... grrr
        int loc = input.toString().indexOf(searchFor);
        while (loc != -1) {
            input.replace(loc, loc + searchFor.length(), replaceWith);
            // loc = input.indexOf(searchFor);
            loc = input.toString().indexOf(searchFor);
        }
        return input.toString();
    }

    public static String padStringBuffer(StringBuffer input, char padChar, int desiredLength, boolean prepend) {
        if (prepend) {

            while (input.length() < desiredLength) {
                input.insert(0, padChar);
            }
        } else {
            while (input.length() < desiredLength) {
                input.append(padChar);
            }
        }
        return input.toString();

    }

    public static String padString(String input, char padChar, int desiredLength, boolean prepend) {
        return padStringBuffer(new StringBuffer(input), padChar, desiredLength, prepend);
    }

    public static String padInt(int input, char padChar, int desiredLength, boolean prepend) {
        return padStringBuffer(new StringBuffer(input + ""), padChar, desiredLength, prepend);
    }
    
    public static String getIndexName(AbsoluteCodingSchemeVersionReference reference) throws LBParameterException {
    	
    	String codingSchemeName = LexEvsServiceLocator.getInstance().getSystemResourceService().
    		getInternalCodingSchemeNameForUserCodingSchemeName(reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
    	
	    String indexName = codingSchemeName + "-" + reference.getCodingSchemeVersion();
	    char charsToReplace[] = {
	    '!', '#', '$', '%' ,'&' ,'\'', '@', '^', '`', '~' ,'+',',','.', ';',':' ,'=' ,')', '(','>','<','+','|','\\','/','*','"'
	    };
	    for(char c: charsToReplace){
	    indexName = StringUtils.replaceChars(indexName, c, '_');
	    } 
	    return indexName;
	}
    
    public static String getSourceCodingSchemeName(AbsoluteCodingSchemeVersionReference reference) throws LBParameterException {
    	return LexEvsServiceLocator.getInstance().
    			getSystemResourceService().
    			getInternalCodingSchemeNameForUserCodingSchemeName(
    			reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
	}
    
    public static Analyzer getAnalyzer(){
    	return new StandardAnalyzer();
    }

}