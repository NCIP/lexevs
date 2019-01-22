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
package org.LexGrid.LexBIG.admin;

import static org.kohsuke.args4j.ExampleMode.ALL;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;

public class CleanUpMetadataLauncher {

	@Option(name="-h", aliases={"--help"}, usage="Prints usage information.") 
	private boolean help;
	
	@Option(name="-f", aliases={"--force"}, usage="Remove metadata enties that have no loaded coding scheme without prompting.") 
	private boolean force=false;

	private void execute() {
		
		try {
			final Map<String,AbsoluteCodingSchemeVersionReference> metadataMap = 
					new HashMap<String,AbsoluteCodingSchemeVersionReference>();
			
			final Map<String,AbsoluteCodingSchemeVersionReference> codingSchemeMap = 
					new HashMap<String,AbsoluteCodingSchemeVersionReference>();
			
			
			
			for(RegistryEntry entry :
				LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME)) {
				
				AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
				ref.setCodingSchemeURN(entry.getResourceUri());
				ref.setCodingSchemeVersion(entry.getResourceVersion());
				String codingSchemeKey= LuceneLoaderCode.createCodingSchemeUriVersionKey(entry.getResourceUri(), entry.getResourceVersion());
				codingSchemeMap.put(codingSchemeKey, ref);
			}
			
			LexBIGService lbs= LexBIGServiceImpl.defaultInstance();
			LexBIGServiceMetadata lbsm = lbs.getServiceMetadata();
			for (Enumeration<? extends AbsoluteCodingSchemeVersionReference> items = lbsm.listCodingSchemes()
	                    .enumerateAbsoluteCodingSchemeVersionReference(); items.hasMoreElements();) {
			     AbsoluteCodingSchemeVersionReference ref = items.nextElement();
			     String codingSchemeKey= LuceneLoaderCode.createCodingSchemeUriVersionKey(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
			     metadataMap.put(codingSchemeKey, ref);
			}
			Set<String> missingMetadataSet = new HashSet<String>(metadataMap.keySet());
			missingMetadataSet.removeAll(codingSchemeMap.keySet());
			for (String metadataKey: missingMetadataSet) {
			    AbsoluteCodingSchemeVersionReference ref= metadataMap.get(metadataKey);
			    System.out.println("There is metadata without a loaded coding scheme for codingscheme= " + ref.getCodingSchemeURN()+ " and version= "+ ref.getCodingSchemeVersion());
			    if (! force) {
			        
			       
		            boolean confirmed = true;
		           
		                Util.displayMessage("CLEAR OPTIONAL METADATA? ('Y' to confirm, any other key to cancel)");
		                char choice = Util.getConsoleCharacter();
		                confirmed = choice == 'Y' || choice == 'y';
		            
		            if (confirmed) {
		                lbs.getServiceManager(null).removeCodingSchemeVersionMetaData(ref);
		                Util.displayAndLogMessage("Metadata removed for codingscheme= " + ref.getCodingSchemeURN()+ " and version= "+ ref.getCodingSchemeVersion());
		            } else {
		                Util.displayAndLogMessage("Skipping removal of metadata");

		            }
			        
			    } else {
			        lbs.getServiceManager(null).removeCodingSchemeVersionMetaData(ref);
                    Util.displayAndLogMessage("Metadata removed for codingscheme= " + ref.getCodingSchemeURN()+ " and version= "+ ref.getCodingSchemeVersion());

			    }
			}
			
			Util.displayAndLogMessage("CleanUpMetadata routine ended without error!");
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		new CleanUpMetadataLauncher().doMain(args);
	}
	
	public void doMain(String[] args) throws Exception{
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(160);

		try {
			parser.parseArgument(args);	

			if(help) {
				System.out.println("Cleanup the Metadata with no corresponding coding scheme.");
				System.out.println();
				printUsage(parser, System.out);
				return;
			}

			this.execute();
		} catch(CmdLineException e) {
			System.err.println(e.getMessage());
            printUsage(parser, System.err);
            Util.displayAndLogError("Metadata cleanup failed: ", e);
            return;
        }
	}

	private void printUsage(CmdLineParser parser, PrintStream printStream) {
		printStream.println("[options...] arguments...");

		parser.printUsage(printStream);
		printStream.println();

		printStream.println("  Example: [ CleanUpMetadata.bat | CleanUpMetadata.sh ] " + parser.printExample(ALL));
	}
}