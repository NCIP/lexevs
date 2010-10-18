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
package org.lexevs.dao.database.operation.tools;

import static org.kohsuke.args4j.ExampleMode.ALL;

import java.io.File;
import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.locator.LexEvsServiceLocator;

public class ScriptProducingLauncher {
	
	@Option(name="-db", aliases={"--databaseType"}, required=true, usage="Target database type.")   
	private DatabaseType databaseType;
	
	@Option(name="-p", aliases={"--prefix"}, usage="Prefix to append to all tables.", metaVar="PREFIX") 
	private String prefix = "";
	
	@Option(name="-o", aliases={"--out"}, required=true, usage="Output directory.", metaVar="DIR") 
	private String out;
	
	@Option(name="-f", aliases={"--force"}, usage="Force output directory creation.") 
	private boolean force;

	private void execute() {
		if(this.force) {
			new File(out).mkdir();
		}

		try {
			LexEvsServiceLocator.getInstance().
				getLexEvsDatabaseOperations().
					dumpSqlScripts(databaseType, out, prefix);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		new ScriptProducingLauncher().doMain(args);
	}
	
	public void doMain(String[] args) throws Exception{
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(160);

		try {
			parser.parseArgument(args);	
			this.execute();
		} catch(CmdLineException e) {
			System.err.println(e.getMessage());
            printUsage(parser);
            return;
        }
	}

	private void printUsage(CmdLineParser parser) {
		System.err.println("[options...] arguments...");

		parser.printUsage(System.err);
		System.err.println();

		System.err.println("  Example: [ ExportDDLScripts.bat | ExportDDLScripts.sh ] " + parser.printExample(ALL));
	}
}