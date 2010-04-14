package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * A program that started with the best intentions of clean well structured code but 
 * eventually turned into a house of cards with lots of hard coded hacks.  It attempts 
 * merge the information contained in a Word doc with the information contained in an
 * Excel file.  It seems that both files were hand crafted and not only differ between 
 * Vols but differ between Chapter && subChapter.  Comments/documentation are found
 * for most all the bending/twisting.
 */
public class IndMain {
    private static String docPath = "";
    private static String xlsPath = "";
    private static String outputPath = "";
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        System.out.println("conversion has started");
        
        setArguments(args);

           
        try {
            IndMerger merger = new IndMerger();  
            merger.merge(docPath, xlsPath, outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("conversion completed");
    }
    
    /**
     * Parses the command line to get the arguments
     * @param args
     * @throws ParseException
     */
    private static void setArguments(String[] args)
    {
        Options options = getCommandOptions();
        CommandLine cl = null;
            
        try{
            cl = new BasicParser().parse(options, args);
    
            xlsPath = cl.getOptionValue("xlsPath");
            docPath = cl.getOptionValue("docPath");
            outputPath = cl.getOptionValue("outputPath");
        }
        catch(ParseException pe)
        {
            pe.printStackTrace();
        }
    }
    
    /**
     * Gets the options from the command line arguments
     * @return Options
     */
    private static Options getCommandOptions() 
    {
        Options options = new Options();
        Option o;
        
        o = new Option("xlsPath", true, "Path to spreadsheet");
        options.addOption(o);
        
        o = new Option("docPath", true, "Path to doc file");
        options.addOption(o);
        
        o = new Option("outputPath", true, "Path to output file");
        options.addOption(o);
        
        return options;
    }
}


