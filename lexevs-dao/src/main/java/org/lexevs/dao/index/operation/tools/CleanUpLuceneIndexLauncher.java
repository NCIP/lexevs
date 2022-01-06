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
package org.lexevs.dao.index.operation.tools;

import static org.kohsuke.args4j.ExampleMode.ALL;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;

public class CleanUpLuceneIndexLauncher {

	@Option(name="-h", aliases={"--help"}, usage="Prints usage information.") 
	private boolean help;
	
	@Option(name="-r", aliases={"--reindex"}, usage="Reindex any missing indicies.") 
	private boolean reindexMissing;

	private void execute() {
		LgLoggerIF logger = org.lexevs.logging.LoggerFactory.getLogger();
		try {
			List<AbsoluteCodingSchemeVersionReference> expectedList = 
				new ArrayList<AbsoluteCodingSchemeVersionReference>();
			
			for(RegistryEntry entry :
				LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME)) {
				
				AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
				ref.setCodingSchemeURN(entry.getResourceUri());
				ref.setCodingSchemeVersion(entry.getResourceVersion());
				
				expectedList.add(ref);
			}
			
			LexEvsServiceLocator.getInstance().getLexEvsIndexOperations().cleanUp(expectedList, reindexMissing);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		new CleanUpLuceneIndexLauncher().doMain(args);
	}
	
	public void doMain(String[] args) throws Exception{
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(160);

		try {
			parser.parseArgument(args);	

			if(help) {
				System.out.println("Cleanup the Lucene Index.");
				System.out.println();
				printUsage(parser, System.out);
				return;
			}

			this.execute();
		} catch(CmdLineException e) {
			System.err.println(e.getMessage());
            printUsage(parser, System.err);
            return;
        }
	}

	private void printUsage(CmdLineParser parser, PrintStream printStream) {
		printStream.println("[options...] arguments...");

		parser.printUsage(printStream);
		printStream.println();

		printStream.println("  Example: [ CleanUpLuceneIndex.bat | CleanUpLuceneIndex.sh ] " + parser.printExample(ALL));
	}
}