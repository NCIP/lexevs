package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This program reads ICD10CM metadata from LexGrid and writes it to file in ClaML format
 * 
 * Sample program arguments
 * -csn "ICD10CM" -outputPath "C:\Temp\ICD10CM.xml"
 * 
 * Sample VM arguments
 * -DLG_CONFIG_FILE="X:\services\lexbig\5.0.kiefer\resources\config\lbconfig.props" -Xmx1024M
 *
 */
public class EMF2ClaMLMain 
{
    private static String outputPath_ = "";
    private static String codeSystemName_ = "";
    
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
        setArguments(args);

        EMF2ClaML converter = new EMF2ClaML();		
		try {
			converter.convertEMFToClaML(outputPath_, codeSystemName_);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses the command line to get the arguments
	 * @param args
	 * @throws ParseException
	 */
	private static void setArguments(String[] args)
	{
		CommandLine cl = null;
		Options options = getCommandOptions();
			
		try{
			cl = new BasicParser().parse(options, args);
	
			codeSystemName_ = cl.getOptionValue("csn");
			outputPath_ =  cl.getOptionValue("outputPath");
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
		
		o = new Option("csn", "codesystemname", true, "Code System Name");
		o.setArgName("csn");
		o.setRequired(true);
		options.addOption(o);
		
		o = new Option("outputPath", "outputPath", true, "Path to output file");
		o.setArgName("outputPath");
		o.setRequired(true);
		options.addOption(o);
		
		return options;
	}
}


